package org.siemac.metamac.statistical_resources.rest.common.impl.export;

import static org.siemac.metamac.statistical_resources.rest.common.impl.export.utils.ExportUtils.getContentDisposition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.io.DeleteOnCloseFileInputStream;
import org.siemac.metamac.statistical_resources.rest.common.impl.exceptions.ExceptionUtils;
import org.siemac.metamac.statistical_resources.rest.common.impl.export.enume.ResourcesFormat;
import org.siemac.metamac.statistical_resources.rest.common.impl.export.utils.DimensionsFilter;

import es.gobcan.istac.edatos.dataset.repository.dto.TabularDataDto;

public class ExportDatasetToTransposedPlainText {

    public static Response buildResponseExportResourceAccessToPlainText(TabularDataDto tabularDataDto, DimensionsFilter dimensionsFilter, List<String> geographicCodes, String filename, ResourcesFormat format)
            throws MetamacException, IOException {

        FileOutputStream outputStreamObservations = null;
        try {

            final File tmpFileObservations = File.createTempFile(filename, format.getExtension());
            outputStreamObservations = new FileOutputStream(tmpFileObservations);
            PlainTextTransposedExporter plainTextTransposedExporter = new PlainTextTransposedExporter(tabularDataDto, dimensionsFilter, geographicCodes);
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
}
