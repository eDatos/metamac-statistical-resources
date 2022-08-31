package org.siemac.metamac.statistical.resources.web.client.base.view.handlers;

import org.siemac.metamac.statistical.resources.web.client.base.utils.SiemacMetadataExternalField;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;
import org.siemac.metamac.web.common.shared.criteria.CommonConfigurationRestCriteria;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmItemRestCriteria;

public interface StatisticalResourceUiHandlers extends BaseUiHandlers {

    // COMMON METADATA
    void retrieveCommonConfigurations(CommonConfigurationRestCriteria criteria);

    // STATISTICAL OPERATIONS INSTANCES
    void retrieveStatisticalOperationInstances(String statisticalOperationCode, int firstResult, int maxResults, MetamacWebCriteria webCriteria);

    // languages
    void retrieveLanguagesCodes(int firstResult, int maxResults, MetamacWebCriteria criteria);

    // ORGANIZATION UNITS and SCHEMES
    void retrieveOrganisationUnitSchemes(int firstResult, int maxResults, MetamacWebCriteria webCriteria, SiemacMetadataExternalField field);
    void retrieveOrganisationUnits(int firstResult, int maxResults, SrmItemRestCriteria webCriteria, SiemacMetadataExternalField field);
    
 // DATA_PROVIDERS and SCHEMES
    void retrieveDataProviderSchemes(int firstResult, int maxResults, MetamacWebCriteria webCriteria, SiemacMetadataExternalField field);
    void retrieveDataProviderUnits(int firstResult, int maxResults, SrmItemRestCriteria webCriteria, SiemacMetadataExternalField field);
}
