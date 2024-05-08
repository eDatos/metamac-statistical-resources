package org.siemac.metamac.statistical.resources.web.client.query.view.handlers;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionDto;
import org.siemac.metamac.statistical.resources.web.shared.criteria.DatasetVersionWebCriteria;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmItemRestCriteria;

public interface QueryUiHandlers extends BaseUiHandlers {

    void goToQueries();

    void retrieveQuery(String urn);
    void saveQuery(QueryVersionDto query);
    void deleteQuery(String urn);

    void retrieveDatasetsForQuery(int firstResult, int maxResults, DatasetVersionWebCriteria criteria);
    void retrieveStatisticalOperationsForDatasetSelection();

    void retrieveDimensionsForDataset(String urn);

    void retrieveDimensionCodesForDataset(String urn, String dimensionId, MetamacWebCriteria webCriteria);

    void retrieveAgencySchemes(int firstResult, int maxResults, MetamacWebCriteria webCriteria);
    void retrieveAgencies(int firstResult, int maxResults, SrmItemRestCriteria webCriteria);

    void previewData(QueryVersionDto queryVersionDto);

    // Life cycle

    void sendToProductionValidation(QueryVersionDto queryVersionDto);
    void sendToDiffusionValidation(QueryVersionDto queryVersionDto);
    void rejectValidation(QueryVersionDto queryVersionDto, String reasonOfRejection);

    void publish(QueryVersionDto queryVersionDto);
    void version(QueryVersionDto queryVersionDto, VersionTypeEnum versionType);
    void resendStreamMessage(QueryVersionDto queryVersionDto);
    // Time codes
    void retrieveTemporalCodesForField(String datasetUrn);
}
