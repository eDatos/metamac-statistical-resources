package org.siemac.metamac.statistical_resources.rest.external.service;

import org.joda.time.DateTime;
import org.siemac.metamac.core.common.cache.domain.CacheableResource;
import org.siemac.metamac.core.common.cache.service.CacheableResourceService;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticalResourcesCacheableResourceServiceImpl implements CacheableResourceService {
    private static final Logger logger = LoggerFactory.getLogger(StatisticalResourcesCacheableResourceServiceImpl.class);

    @Autowired
    private StatisticalResourcesRestExternalCommonService commonService;

    @Override
    public boolean supports(Class<? extends CacheableResource> resourceType) {
        return DatasetVersion.class.isAssignableFrom(resourceType) ||
               PublicationVersion.class.isAssignableFrom(resourceType) ||
               QueryVersion.class.isAssignableFrom(resourceType) ||
               MultidatasetVersion.class.isAssignableFrom(resourceType);
    }

    @Override
    public DateTime getLastModifiedDate(Class<? extends CacheableResource> resourceType, String agencyID, String resourceID, String version) {
        try {
            CacheableResource resource = null;
            if (DatasetVersion.class.isAssignableFrom(resourceType)) {
                resource = commonService.retrieveDatasetVersion(agencyID, resourceID, version);
            } else if (PublicationVersion.class.isAssignableFrom(resourceType)) {
                resource = commonService.retrievePublicationVersion(agencyID, resourceID);
            } else if (QueryVersion.class.isAssignableFrom(resourceType)) {
                resource = commonService.retrieveQueryVersion(agencyID, resourceID);
            } else if (MultidatasetVersion.class.isAssignableFrom(resourceType)) {
                resource = commonService.retrieveMultidatasetVersion(agencyID, resourceID);
            }

            if (resource != null) {
                DateTime lastModified = resource.getLastModifiedDate();
                if (lastModified != null) {
                    return lastModified;
                }
            }
        } catch (Exception e) {
            logger.warn("Error getting last modified date for resource " + resourceID + " (parsed as " + resourceType + ")", e);
        }
        return null;
    }
}

