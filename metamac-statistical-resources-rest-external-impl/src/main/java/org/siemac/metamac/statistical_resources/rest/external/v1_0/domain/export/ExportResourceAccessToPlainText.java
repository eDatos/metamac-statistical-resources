package org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export;

import static org.siemac.metamac.statistical_resources.rest.common.service.utils.StatisticalResourcesRestImplCommonUtils.manageException;

import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Dataset;
import org.siemac.metamac.statistical_resources.rest.external.service.utils.DsdExternalProcessor;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export.entities.PlainTextResourceAccess;

public class ExportResourceAccessToPlainText {

    public List<PlainTextResourceAccess> exportResourceAccessToPlainText(ResourceAccess resourceAccess) {

        try {
            return exportResourceToPlainTextWithoutAttributes(resourceAccess);
        } catch (Exception e) {
            throw manageException(e);
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

    public List<PlainTextResourceAccess> exportResourceToPlainTextWithoutAttributes(ResourceAccess resourceAccess) throws MetamacException {
        PlainTextExporter exporter = new PlainTextExporter(resourceAccess);
        return exporter.writeObservationsAndAttributesWithObservationAttachmentLevel();
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
