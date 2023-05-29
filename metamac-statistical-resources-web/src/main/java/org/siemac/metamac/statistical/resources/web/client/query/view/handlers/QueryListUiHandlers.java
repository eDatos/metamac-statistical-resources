package org.siemac.metamac.statistical.resources.web.client.query.view.handlers;

import java.util.List;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionDto;
import org.siemac.metamac.statistical.resources.web.shared.criteria.DatasetVersionWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.criteria.QueryVersionWebCriteria;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

public interface QueryListUiHandlers extends BaseUiHandlers {

    void retrieveQueries(int firstResult, int maxResults, QueryVersionWebCriteria criteria);
    void deleteQueries(List<String> urns);

    void goToQuery(String urn);
    void goToNewQuery();

    // LifeCycle

    void sendToProductionValidation(List<QueryVersionBaseDto> queryVersionBaseDtos);
    void sendToDiffusionValidation(List<QueryVersionBaseDto> queryVersionBaseDtos);
    void rejectValidation(List<QueryVersionBaseDto> queryVersionBaseDtos, String reasonOfRejection);
    void publish(List<QueryVersionBaseDto> queryVersionBaseDtos);

    void version(List<QueryVersionBaseDto> queryVersionBaseDtos, VersionTypeEnum versionType);

    // Related resources

    void retrieveStatisticalOperationsForDatasetVersionSelectionInSearchSection();
    void retrieveDatasetVersionsForSearchSection(int firstResult, int maxResults, DatasetVersionWebCriteria criteria);
    void retrieveStatisticalOperationsForSearchSection(int firstResult, int maxResults, MetamacWebCriteria criteria);
    void retrieveDatasetForRelatedDataset(int firstResult, int maxResults, DatasetVersionWebCriteria criteria);
    void retrieveDatasetsForQuery(int firstResult, int maxResults, DatasetVersionWebCriteria criteria);
    void retrieveStatisticalOperationsForDatasetSelection();
    void createQuery(QueryVersionDto datasetDto);
    void retrieveDimensionCodesForDataset(String urn, String dimensionId, MetamacWebCriteria webCriteria);
    void retrieveDimensionsForDataset(String urn);
}
