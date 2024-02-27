package org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers;

import org.siemac.metamac.statistical.resources.web.client.base.view.handlers.StatisticalResourceUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.dataset.utils.DatasetMetadataExternalField;
import org.siemac.metamac.statistical.resources.web.shared.criteria.DsdWebCriteria;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmItemRestCriteria;

public interface DatasetMetadataCommonTabUiHandlers extends BaseUiHandlers, StatisticalResourceUiHandlers {

    void retrieveMainCoveragesForDatasetVersion(String datasetVersionUrn);

    // DSD
    void retrieveDsdsForRelatedDsd(int firstResult, int maxResults, DsdWebCriteria criteria);
    void retrieveStatisticalOperationsForDsdSelection();

    // Geographic granularities
    void retrieveCodesForGeographicalGranularities(int firstResult, int maxResults, MetamacWebCriteria criteria);
    // Time codes
    void retrieveTemporalCodesForField(int firstResult, int maxResults, MetamacWebCriteria webCriteria, DatasetMetadataExternalField updateFrequency);

    // Concept schemes, concepts
    void retrieveConceptSchemesForStatisticalUnit(int firstResult, int maxResults, MetamacWebCriteria webCriteria);
    void retrieveConceptsForStatisticalUnit(int firstResult, int maxResults, SrmItemRestCriteria webCriteria);
}
