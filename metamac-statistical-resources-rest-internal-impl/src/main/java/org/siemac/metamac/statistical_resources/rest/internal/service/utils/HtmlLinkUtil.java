package org.siemac.metamac.statistical_resources.rest.internal.service.utils;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.utils.shared.MetamacPortalWebUtils;

public class HtmlLinkUtil {

    public static String getVisualizerHtmlLink(StatisticalResourceTypeEnum type, LifeCycleStatisticalResource lifeCycleStatisticalResource, StatisticalResourcesConfiguration configurationService)
            throws MetamacException {
        String url = configurationService.retrievePortalInternalWebApplicationUrlVisualizer();
        String maintainer = lifeCycleStatisticalResource.getMaintainer() != null ? lifeCycleStatisticalResource.getMaintainer().getCode() : null;
        
        switch (type) {
            case QUERY:
                return MetamacPortalWebUtils.buildQueryVersionUrl(maintainer, lifeCycleStatisticalResource.getCode(), url);
            case DATASET:
                return MetamacPortalWebUtils.buildDatasetVersionUrl(maintainer, lifeCycleStatisticalResource.getCode(), lifeCycleStatisticalResource.getVersionLogic(), url);
            case MULTIDATASET:
                return MetamacPortalWebUtils.buildMultidatasetVersionUrl(maintainer, lifeCycleStatisticalResource.getCode(), url);
            case COLLECTION:
                return MetamacPortalWebUtils.buildPublicationVersionUrl(maintainer, lifeCycleStatisticalResource.getCode(), url);
            default:
                throw new IllegalArgumentException("StatisticalResourceTypeEnum " + type + " not valid.");
        }
        
    }
}
