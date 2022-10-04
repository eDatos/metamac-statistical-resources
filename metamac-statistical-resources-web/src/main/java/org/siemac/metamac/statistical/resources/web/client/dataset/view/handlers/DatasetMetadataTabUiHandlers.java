package org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.web.client.base.view.handlers.StatisticalResourceUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.dataset.utils.DatasetMetadataExternalField;
import org.siemac.metamac.statistical.resources.web.shared.criteria.DsdWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.criteria.VersionableStatisticalResourceWebCriteria;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmItemRestCriteria;

public interface DatasetMetadataTabUiHandlers extends BaseUiHandlers, StatisticalResourceUiHandlers {

    // Life cycle
    void sendToProductionValidation(DatasetVersionDto dataset);
    void sendToDiffusionValidation(DatasetVersionDto dataset);
    void rejectValidation(DatasetVersionDto dataset, String reasonOfRejection);

    void publish(DatasetVersionDto dataset);
    void version(DatasetVersionDto dataset, VersionTypeEnum versionType);
    void resendStreamMessage(DatasetVersionDto dataset);
    void updateGeocoverageCache(DatasetVersionDto dataset);

    void previewData(DatasetVersionDto datasetVersionDto);

    void retrieveDataset(String datasetIdentifier);
    void saveDataset(DatasetVersionDto datasetDto);
    void retrieveMainCoveragesForDatasetVersion(String datasetVersionUrn);
    void deleteDatasetVersion(String urn);

    // DSD
    void retrieveDsdsForRelatedDsd(int firstResult, int maxResults, DsdWebCriteria criteria);
    void retrieveStatisticalOperationsForDsdSelection();

    // Geographic granularities
    void retrieveCodesForGeographicalGranularities(int firstResult, int maxResults, MetamacWebCriteria criteria);
    // Time codes
    void retrieveTemporalCodesForField(int firstResult, int maxResults, MetamacWebCriteria webCriteria, DatasetMetadataExternalField updateFrequency);

    // RELATED DATASETS
    void retrieveStatisticalOperationsForReplacesSelection();
    void retrieveDatasetsForReplaces(int firstResult, int maxResults, VersionableStatisticalResourceWebCriteria criteria);

    // Concept schemes, concepts
    void retrieveConceptSchemesForStatisticalUnit(int firstResult, int maxResults, MetamacWebCriteria webCriteria);
    void retrieveConceptsForStatisticalUnit(int firstResult, int maxResults, SrmItemRestCriteria webCriteria);
}
