package org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;

public interface DatasetMetadataTabUiHandlers extends DatasetMetadataCommonTabUiHandlers {

    // Life cycle
    void sendToProductionValidation(DatasetVersionDto dataset);
    void sendToDiffusionValidation(DatasetVersionDto dataset);
    void rejectValidation(DatasetVersionDto dataset, String reasonOfRejection);

    void publish(DatasetVersionDto dataset);
    void version(DatasetVersionDto dataset, VersionTypeEnum versionType);
    void resendStreamMessage(DatasetVersionDto dataset);
    void updateGeocoverageCache(DatasetVersionDto dataset);

    void previewData(DatasetVersionDto datasetVersionDto);

    void saveDataset(DatasetVersionDto datasetDto);

    void deleteDatasetVersion(String urn);
    void copyDataset(String urn);
}
