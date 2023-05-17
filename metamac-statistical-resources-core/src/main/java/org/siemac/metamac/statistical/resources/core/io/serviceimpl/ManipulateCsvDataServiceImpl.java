package org.siemac.metamac.statistical.resources.core.io.serviceimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.io.FileUtils;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure;
import org.siemac.metamac.statistical.resources.core.dataset.domain.CodeDimension;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.core.io.mapper.MetamacCsv2StatRepoMapper;
import org.siemac.metamac.statistical.resources.core.io.serviceimpl.validators.ValidateDataVersusDsd;
import org.siemac.metamac.statistical.resources.core.io.utils.CsvAttributesParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.parser.csv.CsvParser;
import com.arte.statistic.parser.csv.CsvReader;
import com.arte.statistic.parser.csv.constants.CsvConstants;

import es.gobcan.istac.edatos.dataset.repository.dto.ObservationExtendedDto;
import es.gobcan.istac.edatos.dataset.repository.service.DatasetRepositoriesServiceFacade;

@Component(ManipulateCsvDataService.BEAN_ID)
public class ManipulateCsvDataServiceImpl implements ManipulateCsvDataService {

    @Autowired
    private MetamacCsv2StatRepoMapper        metamacCsv2StatRepoMapper;

    @Autowired
    private DatasetRepositoriesServiceFacade datasetRepositoriesServiceFacade;

    @Autowired
    StatisticalResourcesServiceFacade        statisticalResourcesServiceFacade;

    private static int                       SPLIT_DATA_FACTOR = 5000;

    @Override
    public void importCsv(ServiceContext ctx, File csvFile, DataStructure dataStructure, String datasetID, String dataSourceID, ValidateDataVersusDsd validateDataVersusDsd) throws Exception {
        InputStream is = null;
        try {
            // Parse Csv
            String charsetName = FileUtils.guessCharset(csvFile);
            is = new FileInputStream(csvFile);

            CsvReader csvReader = CsvParser.parseCsv(is, charsetName, CsvConstants.SEPARATOR_TAB);

            List<ObservationExtendedDto> dataDtos = new LinkedList<ObservationExtendedDto>();
            ObservationExtendedDto observationExtendedDto = null;

            boolean processData = true;
            while (processData) {
                for (int i = 0; i < SPLIT_DATA_FACTOR; i++) {
                    observationExtendedDto = metamacCsv2StatRepoMapper.toObservation(csvReader.next(), dataSourceID);
                    if (observationExtendedDto == null) {
                        // Insert incomplete slice
                        insertDataAndAttributes(datasetID, dataDtos, validateDataVersusDsd);
                        processData = false;
                        break;
                    }
                    dataDtos.add(observationExtendedDto);
                }
                // Insert slice
                if (processData) {
                    insertDataAndAttributes(datasetID, dataDtos, validateDataVersusDsd);
                    dataDtos.clear();
                }
            }
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    @Override
    public void importCsvAttributes(File csvFile, DataStructure dataStructure, Map<String, List<CodeDimension>> codeDimensions, Map<String, List<ExternalItemDto>> externalItemsAttributeId,
            ServiceContext ctx, String datasetVersionUrn) throws Exception {
        InputStream is = null;
        try {
            // Parse Csv
            String charsetName = FileUtils.guessCharset(csvFile);
            is = new FileInputStream(csvFile);

            CsvAttributesParser csvReader = new CsvAttributesParser(is, charsetName, CsvConstants.SEPARATOR_TAB, dataStructure);

            List<DsdAttributeInstanceDto> dsdAttributeInstanceDto = new ArrayList<>();

            String idAttribute = "";
            for (int i = 0; i < SPLIT_DATA_FACTOR || idAttribute != null; i++) {
                idAttribute = csvReader.nextLine(dsdAttributeInstanceDto, codeDimensions, externalItemsAttributeId);
            }
            insertAttributes(ctx, datasetVersionUrn, dsdAttributeInstanceDto);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    private void insertAttributes(ServiceContext ctx, String datasetVersionUrn, List<DsdAttributeInstanceDto> dsdAttributeInstanceDto) throws MetamacException {
        for (DsdAttributeInstanceDto entry : dsdAttributeInstanceDto) {
            List<DsdAttributeInstanceDto> attributeInstances = new ArrayList<>();
            //to define the attribute at the dataset level we need to know if it has already been created previously
            if (entry.getCodeDimensions() == null || entry.getCodeDimensions().isEmpty()) {
                attributeInstances = statisticalResourcesServiceFacade.retrieveAttributeInstances(ctx, datasetVersionUrn, entry.getAttributeId());
            }
            if (attributeInstances.isEmpty()) {
                statisticalResourcesServiceFacade.createAttributeInstance(ctx, datasetVersionUrn, entry);
            } else {
                DsdAttributeInstanceDto attributeInstanceDto = attributeInstances.get(0);
                attributeInstanceDto.setValue(entry.getValue());
                statisticalResourcesServiceFacade.updateAttributeInstance(ctx, datasetVersionUrn, attributeInstanceDto);
            }
        }
    }

    private void insertDataAndAttributes(String datasetID, List<ObservationExtendedDto> dataDtos, ValidateDataVersusDsd validateDataVersusDsd) throws Exception {
        // Persist Observations and attributes at level observation.
        if (!dataDtos.isEmpty()) {
            validateDataVersusDsd.checkObservation(dataDtos);
            datasetRepositoriesServiceFacade.createOrUpdateObservationsExtended(datasetID, dataDtos);
        }
    }
}
