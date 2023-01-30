package org.siemac.metamac.statistical_resources.rest.external.service.utils;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.api.constants.RestApiConstants;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.utils.shared.MetamacPortalWebUtils;

public class HtmlLinkUtil {

    public static String getVisualizerHtmlLink(StatisticalResourceTypeEnum type, LifeCycleStatisticalResource lifeCycleStatisticalResource, StatisticalResourcesConfiguration configurationService,
            Boolean asLatest) throws MetamacException {
        String maintainer = lifeCycleStatisticalResource.getMaintainer() != null ? lifeCycleStatisticalResource.getMaintainer().getCode() : null;
        return getVisualizerHtmlLink(type, maintainer, lifeCycleStatisticalResource.getCode(), lifeCycleStatisticalResource.getVersionLogic(), configurationService, asLatest);
    }
    
    public static String getVisualizerHtmlLink(StatisticalResourceTypeEnum type, String maintainer, String code, String version, StatisticalResourcesConfiguration configurationService,
            Boolean asLatest) throws MetamacException {
        String url = configurationService.retrievePortalExternalWebApplicationUrlVisualizer();

        switch (type) {
            case QUERY:
                return MetamacPortalWebUtils.buildQueryVersionUrl(maintainer, code, url);
            case DATASET:
                return MetamacPortalWebUtils.buildDatasetVersionUrl(maintainer, code, asLatest ? RestApiConstants.WILDCARD_LATEST : version, url);
            case MULTIDATASET:
                return MetamacPortalWebUtils.buildMultidatasetVersionUrl(maintainer, code, url);
            case COLLECTION:
                return MetamacPortalWebUtils.buildPublicationVersionUrl(maintainer, code, url);
            default:
                throw new IllegalArgumentException("StatisticalResourceTypeEnum " + type + " not valid.");
        }
    }
}
