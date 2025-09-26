package org.siemac.metamac.statistical_resources.rest.common.impl.export;

import static org.siemac.metamac.statistical_resources.rest.common.impl.export.utils.ExportUtils.getContentDisposition;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.IOUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.io.DeleteOnCloseFileInputStream;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.statistical.resources.core.common.domain.DimensionsFilter;
import org.siemac.metamac.statistical_resources.rest.common.impl.exceptions.ExceptionUtils;
import org.siemac.metamac.statistical_resources.rest.common.impl.exceptions.RestStatisticalResourcesCommonServiceExceptionType;
import org.siemac.metamac.statistical_resources.rest.common.impl.export.enume.ResourcesFormat;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DatasetBase;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.QueryBase;

import es.gobcan.istac.edatos.dataset.repository.dto.TabularDataDto;

public class ExportResourceAccessToPlainText {

    public static Response buildResponseExportTabularDataToPlainText(TabularDataDto tabularDataDto, DimensionsFilter dimensionsFilter, List<String> geographicCodes, String filename, ResourcesFormat format)
            throws MetamacException, IOException {

        FileOutputStream outputStreamObservations = null;
        try {

            final File tmpFileObservations = File.createTempFile(filename, format.getExtension());
            outputStreamObservations = new FileOutputStream(tmpFileObservations);
            org.siemac.metamac.statistical.resources.core.export.PlainTextExporter plainTextTransposedExporter = new org.siemac.metamac.statistical.resources.core.export.PlainTextExporter(
                    null, tabularDataDto, dimensionsFilter, geographicCodes);
            plainTextTransposedExporter.writeObservations(outputStreamObservations);

            return Response.ok(new DeleteOnCloseFileInputStream(tmpFileObservations), format.getMimeType()).header("Content-Disposition", getContentDisposition(filename, format.getExtension()))
                    .build();
        } finally {
            IOUtils.closeQuietly(outputStreamObservations);
        }
    }

    public static void exportResourceAccessToPlainText(ResourceAccess resourceAccess, ResourcesFormat format, OutputStream os) throws MetamacException {
        try {
            PlainTextExporter exporter = new PlainTextExporter(resourceAccess, format);
            exporter.writeObservationsAndAttributesWithObservationAttachmentLevel(os);
        } catch (Exception e) {
            throw ExceptionUtils.manageException(e);
        }
    }

    public static ResourceAccess buildResourceAccess(DatasetBase dataset, List<String> selectedLanguages) {
        try {
            DatasetSelection datasetSelection = DatasetSelectionMapper.datasetToDatasetSelection(dataset.getData().getDimensions(), dataset.getMetadata().getAttributes(),
                    dataset.getMetadata().getRelatedDsd());

            return new ResourceAccess(dataset, datasetSelection, selectedLanguages);

        } catch (Exception e) {
            throw ExceptionUtils.manageException(e);
        }
    }

    public static ResourceAccess buildResourceAccess(QueryBase query, List<String> selectedLanguages) {
        try {
            DatasetSelection datasetSelection = DatasetSelectionMapper.datasetToDatasetSelection(query.getData().getDimensions(), query.getMetadata().getAttributes(),
                    query.getMetadata().getRelatedDsd());

            return new ResourceAccess(query, datasetSelection, selectedLanguages);

        } catch (Exception e) {
            throw ExceptionUtils.manageException(e);
        }
    }

    public static void checkMaxRowsInXlsxFormat(ResourceAccess resourceAccess, ResourcesFormat format, String maxXlsxRows) throws RestException {
        if (!ResourcesFormat.XLSX.equals(format)) {
            return;
        }
        if (resourceAccess.getObservationsNumber() > Long.parseLong(maxXlsxRows)) {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestStatisticalResourcesCommonServiceExceptionType.OBSERVATIONS_EXCEED_MAX_FOR_XLSX,
                    resourceAccess.getUrn());
            throw new RestException(exception, Status.NOT_FOUND);
        }
    }

    public static Response buildResponseExportResourceAccessToPlainText(ResourceAccess resourceAccess, String filename, ResourcesFormat format)
            throws MetamacException, IOException, FileNotFoundException {

        FileOutputStream outputStreamObservations = null;
        try {

            final File tmpFileObservations = File.createTempFile(filename, format.getExtension());
            outputStreamObservations = new FileOutputStream(tmpFileObservations);
            exportResourceAccessToPlainText(resourceAccess, format, outputStreamObservations);

            return Response.ok(new DeleteOnCloseFileInputStream(tmpFileObservations), format.getMimeType()).header("Content-Disposition", getContentDisposition(filename, format.getExtension()))
                    .build();
        } finally {
            IOUtils.closeQuietly(outputStreamObservations);
        }
    }
}
