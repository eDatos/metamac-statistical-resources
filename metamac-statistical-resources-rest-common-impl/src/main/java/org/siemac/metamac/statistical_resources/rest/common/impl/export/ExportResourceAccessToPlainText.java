package org.siemac.metamac.statistical_resources.rest.common.impl.export;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.api.export.mapper.PlainTextResource;
import org.siemac.metamac.statistical_resources.rest.common.impl.exceptions.ExceptionUtils;
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
        PlainTextExporter exporter = new PlainTextExporter(resourceAccess, selectedLanguages);
        return exporter.writeObservationsAndAttributesWithObservationAttachmentLevel(format);
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
}
