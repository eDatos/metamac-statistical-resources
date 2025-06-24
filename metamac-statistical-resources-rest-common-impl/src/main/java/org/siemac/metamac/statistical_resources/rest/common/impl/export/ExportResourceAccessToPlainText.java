package org.siemac.metamac.statistical_resources.rest.common.impl.export;

import java.io.OutputStream;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.statistical_resources.rest.common.impl.exceptions.ExceptionUtils;
import org.siemac.metamac.statistical_resources.rest.common.impl.exceptions.RestStatisticalResourcesCommonServiceExceptionType;
import org.siemac.metamac.statistical_resources.rest.common.impl.export.enume.ResourcesFormat;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DatasetBase;

public class ExportResourceAccessToPlainText {

    public void exportResourceAccessToPlainText(ResourceAccess resourceAccess, String format, OutputStream os) throws MetamacException {
        try {
            PlainTextExporter exporter = new PlainTextExporter(resourceAccess, format);
            exporter.writeObservationsAndAttributesWithObservationAttachmentLevel(os);
        } catch (Exception e) {
            throw ExceptionUtils.manageException(e);
        }
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
        if (ResourcesFormat.XLSX.name().equals(format.toUpperCase()) && (resourceAccess.getObservationsNumber() > Long.parseLong(maxXlsxRows))) {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils
                    .getException(RestStatisticalResourcesCommonServiceExceptionType.DATASET_OBSERVATIONS_EXCEED_MAX_FOR_XLSX, datasetUrn);
            throw new RestException(exception, Status.NOT_FOUND);
        }
    }
}
