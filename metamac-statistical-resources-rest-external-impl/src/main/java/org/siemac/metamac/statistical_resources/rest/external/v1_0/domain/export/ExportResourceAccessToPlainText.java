package org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export;

import static org.siemac.metamac.statistical_resources.rest.common.service.utils.StatisticalResourcesRestImplCommonUtils.manageException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.ws.rs.core.Response;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.io.DeleteOnCloseFileInputStream;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Dataset;
import org.siemac.metamac.statistical_resources.rest.external.service.utils.DsdExternalProcessor;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export.enume.PlainTextTypeEnum;

public class ExportResourceAccessToPlainText {

    public Response exportResourceAccessToPlainText(ResourceAccess resourceAccess, String proposedFilename, PlainTextTypeEnum plainTextTypeEnum) {
        FileOutputStream outputStreamObservations = null;
        FileOutputStream outputStreamAttributes = null;
        try {
            final File tmpFileObservations = File.createTempFile("metamac", plainTextTypeEnum.getExtension());
            outputStreamObservations = new FileOutputStream(tmpFileObservations);
            exportResourceToPlainTextWithoutAttributes(PlainTextTypeEnum.TSV, resourceAccess, outputStreamObservations);
            String filename = addExtensionToFilenameIfNeeded(plainTextTypeEnum.getExtension(), proposedFilename + "-observations");
            return buildResponseOkWithFile(tmpFileObservations, filename, plainTextTypeEnum.getMimeType());
        } catch (Exception e) {
            throw manageException(e);
        } finally {
            IOUtils.closeQuietly(outputStreamObservations);
            IOUtils.closeQuietly(outputStreamAttributes);
        }
    }
    public static String addExtensionToFilenameIfNeeded(String proposedExtension, String existingFilename) {
        String currentExtension = FilenameUtils.getExtension(existingFilename);
        if (!StringUtils.isEmpty(currentExtension)) {
            return existingFilename;
        } else {
            return buildFilename("." + proposedExtension, existingFilename);
        }
    }
    private static String buildFilename(String extension, String... parts) {
        return buildFilenameWithoutExtension(parts) + extension;
    }
    private static String buildFilenameWithoutExtension(String... parts) {
        StringBuilder filename = new StringBuilder();
        return filename.append(StringUtils.join(parts, "-")).toString().replace(".", "_");
    }
    private Response buildResponseOkWithFile(File file, String filename, String mimeType) throws FileNotFoundException {
        return Response.ok(new DeleteOnCloseFileInputStream(file), mimeType).header("Content-Disposition", "attachment; filename=" + filename).build();
    }
    public void exportResourceToPlainTextWithoutAttributes(PlainTextTypeEnum plainTextTypeEnum, ResourceAccess resourceAccess, OutputStream resultObservationsOutputStream) throws MetamacException {
        PlainTextExporter exporter = new PlainTextExporter(plainTextTypeEnum, resourceAccess);
        exporter.writeObservationsAndAttributesWithObservationAttachmentLevel(resultObservationsOutputStream);
    }

    public ResourceAccess buildResourceAccessForDataset(Dataset dataset, String lang) {
        try {
            DatasetSelection datasetSelection = DatasetSelectionMapper.datasetToDatasetSelection(dataset.getData().getDimensions(), dataset.getMetadata().getAttributes(),
                    dataset.getMetadata().getRelatedDsd());
            String langDefault = "es";
            if (lang == null) {
                lang = langDefault;
            }
            return new ResourceAccess(DsdExternalProcessor.getSrmRestExternalFacade(), dataset, datasetSelection, lang, langDefault);
        } catch (Exception e) {
            throw manageException(e);
        }
    }
}
