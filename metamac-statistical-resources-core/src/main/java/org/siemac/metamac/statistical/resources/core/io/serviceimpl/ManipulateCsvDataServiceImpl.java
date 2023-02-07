package org.siemac.metamac.statistical.resources.core.io.serviceimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.io.FileUtils;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure;
import org.siemac.metamac.statistical.resources.core.io.mapper.MetamacCsv2StatRepoMapper;
import org.siemac.metamac.statistical.resources.core.io.serviceimpl.validators.ValidateDataVersusDsd;
import org.siemac.metamac.statistical.resources.core.io.utils.CsvAttributesParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.parser.csv.CsvParser;
import com.arte.statistic.parser.csv.CsvReader;
import com.arte.statistic.parser.csv.constants.CsvConstants;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceDto;
import es.gobcan.istac.edatos.dataset.repository.dto.ObservationExtendedDto;
import es.gobcan.istac.edatos.dataset.repository.service.DatasetRepositoriesServiceFacade;

@Component(ManipulateCsvDataService.BEAN_ID)
public class ManipulateCsvDataServiceImpl implements ManipulateCsvDataService {

    @Autowired
    private MetamacCsv2StatRepoMapper        metamacCsv2StatRepoMapper;

    @Autowired
    private DatasetRepositoriesServiceFacade datasetRepositoriesServiceFacade;

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
    public void importCsvAttributes(File csvFile) throws Exception {
        InputStream is = null;
        try {
            // Parse Csv
            String charsetName = FileUtils.guessCharset(csvFile);
            is = new FileInputStream(csvFile);

            CsvAttributesParser csvReader = new CsvAttributesParser(is, charsetName, CsvConstants.SEPARATOR_TAB);

            Map<String, List<AttributeInstanceDto>> dataDtos = new HashMap<>();
            AttributeInstanceDto attributeInstanceDto = null;

            boolean processData = true;
            while (processData) {
                for (int i = 0; i < SPLIT_DATA_FACTOR; i++) {
//                    attributeInstanceDto = metamacCsv2StatRepoMapper.toObservation(csvReader.next(), dataSourceID);
                    if (attributeInstanceDto == null) {
                        // Insert incomplete slice
//                        insertDataAndAttributes(datasetID, dataDtos, validateDataVersusDsd);
                        processData = false;
                        break;
                    }
//                    dataDtos.add(attributeInstanceDto);
                }
                // Insert slice
                if (processData) {
//                    insertDataAndAttributes(datasetID, dataDtos, validateDataVersusDsd);
                    dataDtos.clear();
                }
            }
        } finally {
            IOUtils.closeQuietly(is);
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
