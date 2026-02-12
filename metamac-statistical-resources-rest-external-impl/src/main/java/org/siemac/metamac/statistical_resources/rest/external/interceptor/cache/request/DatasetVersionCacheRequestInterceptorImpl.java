package org.siemac.metamac.statistical_resources.rest.external.interceptor.cache.request;

import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical_resources.rest.external.service.StatisticalResourcesRestExternalCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DatasetVersionCacheRequestInterceptorImpl extends AbstractResourceCacheRequestInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(DatasetVersionCacheRequestInterceptorImpl.class);

    // matchs dataset URIs: /datasets/{agencyID}/{resourceID}/{version}
    private static final Pattern DATASET_PATTERN = Pattern.compile(".*/datasets/([^/]+)/([^/]+)/([^/]+?)(?:\\.[a-zA-Z]+)?(?:\\?.*)?$");

    @Autowired
    private StatisticalResourcesRestExternalCommonService commonService;

    public String[] extractDatasetPathParameters(HttpServletRequest request) {
        try {
            String requestUri = request.getRequestURI();
            if (requestUri == null) {
                return null;
            }

            Matcher matcher = DATASET_PATTERN.matcher(requestUri);
            if (matcher.matches()) {
                return new String[] {
                        matcher.group(1),
                        matcher.group(2),
                        matcher.group(3)
                };
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.warn("Error extracting dataset path parameters", e);
        }
        return null;
    }

    @Override
    Date getResourceLastModified(HttpServletRequest request) {
        String[] pathParams = extractDatasetPathParameters(request);
        if (pathParams == null) {
            return null;
        }

        String agencyID = pathParams[0];
        String resourceID = pathParams[1];
        String version = pathParams[2];

        DatasetVersion datasetVersion = commonService.retrieveDatasetVersion(agencyID, resourceID, version);
        if (datasetVersion == null) {
            return null;
        }

        return datasetVersion.getSiemacMetadataStatisticalResource().getLastUpdate().toDate();
    }
}
