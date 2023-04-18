package org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.export;

import static org.siemac.metamac.statistical_resources.rest.common.service.utils.StatisticalResourcesRestImplCommonUtils.manageException;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.api.export.mapper.FlattenResource;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Dataset;

public class ExportResourceAccessToPlainText {

    public List<FlattenResource> exportResourceAccessToPlainText(ResourceAccess resourceAccess, List<String> selectedLanguages) {

        try {
            return exportResourceToPlainTextWithoutAttributes(resourceAccess, selectedLanguages);
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    public List<FlattenResource> exportResourceToPlainTextWithoutAttributes(ResourceAccess resourceAccess, List<String> selectedLanguages) throws MetamacException {
        PlainTextExporter exporter = new PlainTextExporter(resourceAccess, selectedLanguages);
        return exporter.writeObservationsAndAttributesWithObservationAttachmentLevel();
    }

    public ResourceAccess buildResourceAccessForDataset(Dataset dataset, List<String> selectedLanguages) {
        try {
            DatasetSelection datasetSelection = DatasetSelectionMapper.datasetToDatasetSelection(dataset.getData().getDimensions(), dataset.getMetadata().getAttributes(),
                    dataset.getMetadata().getRelatedDsd());

            return new ResourceAccess(dataset, datasetSelection, selectedLanguages);
        } catch (Exception e) {
            throw manageException(e);
        }
    }
}
