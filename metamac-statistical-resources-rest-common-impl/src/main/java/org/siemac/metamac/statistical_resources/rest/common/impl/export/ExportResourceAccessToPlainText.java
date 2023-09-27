package org.siemac.metamac.statistical_resources.rest.common.impl.export;

import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.api.export.mapper.PlainTextResource;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.statistical_resources.rest.common.impl.exceptions.ExceptionUtils;
import org.siemac.metamac.statistical_resources.rest.common.impl.exceptions.RestStatisticalResourcesCommonServiceExceptionType;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DatasetBase;

public class ExportResourceAccessToPlainText {

    public List<PlainTextResource> exportResourceAccessToPlainText(ResourceAccess resourceAccess, List<String> selectedLanguages, String format) {

        try {
            return exportResourceToPlainTextWithoutAttributes(resourceAccess, selectedLanguages, format);
        } catch (Exception e) {
            throw ExceptionUtils.manageException(e);
        }
    }

    public List<PlainTextResource> exportResourceToPlainTextWithoutAttributes(ResourceAccess resourceAccess, List<String> selectedLanguages, String format) throws MetamacException {
        PlainTextExporter exporter = new PlainTextExporter(resourceAccess, selectedLanguages, format);
        return exporter.writeObservationsAndAttributesWithObservationAttachmentLevel();
    }

    public ResourceAccess buildResourceAccessForDataset(DatasetBase dataset, List<String> selectedLanguages) {
        try {
            DatasetSelection datasetSelection = DatasetSelectionMapper.datasetToDatasetSelection(dataset.getData().getDimensions(), dataset.getMetadata().getAttributes(),
                    dataset.getMetadata().getRelatedDsd());

            return new ResourceAccess(dataset, datasetSelection, selectedLanguages);
        } catch (Exception e) {
            throw ExceptionUtils.manageException(e);
        }
    }

    public void checkMaxRowsInXlsxFormat(ResourceAccess resourceAccess, String format, String maxXlsxRows, String datasetUrn) throws RestException {
        if ("xlsx".equals(format) && resourceAccess.getDataSelection().getRows() > Long.parseLong(maxXlsxRows)) {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils
                    .getException(RestStatisticalResourcesCommonServiceExceptionType.DATASET_OBSERVATIONS_EXCEED_MAX_FOR_XLSX, datasetUrn);
            throw new RestException(exception, Status.NOT_FOUND);
        }
    }
}
