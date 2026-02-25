package org.siemac.metamac.statistical_resources.rest.external.interceptor.cache;

import org.apache.cxf.jaxrs.model.OperationResourceInfo;
import org.apache.cxf.jaxrs.model.URITemplate;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetCube;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical_resources.rest.external.service.StatisticalResourcesRestExternalCommonService;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.annotation.Cacheable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class CacheRequestInterceptor extends AbstractPhaseInterceptor<Message> {

    private static final Logger logger = LoggerFactory.getLogger(CacheRequestInterceptor.class);

    @Autowired
    private StatisticalResourcesRestExternalCommonService commonService;

    public CacheRequestInterceptor() {
        super(Phase.PRE_INVOKE);
    }

    @Override
    public void handleMessage(Message message) {
        try {
            HttpServletRequest request = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);

            String httpMethod = request.getMethod();
            if (!"GET".equals(httpMethod) && !"HEAD".equals(httpMethod)) {
                return;
            }

            Method method = getTargetMethod(message);
            if (method == null) {
                return;
            }

            Cacheable cacheableAnnotation = method.getAnnotation(Cacheable.class);
            if (cacheableAnnotation == null) {
                return;
            }

            Date lastModified = getResourceLastModified(message, cacheableAnnotation.value());
            if (lastModified == null) {
                return;
            }

            message.getExchange().put("lastModified", lastModified);

            String ifModifiedSinceHeader = request.getHeader("If-Modified-Since");
            if (ifModifiedSinceHeader == null) {
                return;
            }

            Date clientDate = parseHttpDate(ifModifiedSinceHeader);
            if (clientDate == null) {
                return;
            }

            if (!isModifiedSince(lastModified, clientDate)) {
                HttpServletResponse response = (HttpServletResponse) message.get(AbstractHTTPDestination.HTTP_RESPONSE);
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                message.getInterceptorChain().abort();
            }

        } catch (Exception e) {
            logger.error("Error in annotation-based cache request interceptor", e);
        }
    }

    private Method getTargetMethod(Message message) {
        OperationResourceInfo ori = message.getExchange().get(OperationResourceInfo.class);
        if (ori != null && ori.getMethodToInvoke() != null) {
            return ori.getMethodToInvoke();
        }
        return null;
    }

    private Date getResourceLastModified(Message message, Cacheable.ResourceType resourceType) {
        String agencyID = extractPathParam(message, "agencyID");
        String resourceID = extractPathParam(message, "resourceID");
        String version = extractPathParam(message, "version");

        if (agencyID == null || resourceID == null) {
            logger.debug("Missing required path parameters for caching");
            return null;
        }

        // FIXME: hay que revisar las fechas
        switch (resourceType) {
            case DATASET:
                if (version == null) {
                    logger.debug("Missing version parameter for dataset");
                    return null;
                }
                DatasetVersion datasetVersion = commonService.retrieveDatasetVersion(agencyID, resourceID, version);
                return datasetVersion != null ? getMostRecentDate(datasetVersion.getSiemacMetadataStatisticalResource().getLastUpdate().toDate(),
                                                                  datasetVersion.getLifeCycleStatisticalResource().getLastUpdated().toDate()) : null;

            case QUERY:
                QueryVersion queryVersion = commonService.retrieveQueryVersion(agencyID, resourceID);
                return queryVersion != null ? getMostRecentDate(queryVersion.getFixedDatasetVersion().getSiemacMetadataStatisticalResource().getLastUpdate().toDate(),
                                                                queryVersion.getQuery().getIdentifiableStatisticalResource().getLastUpdated().toDate(),
                                                                queryVersion.getLifeCycleStatisticalResource().getLastUpdated().toDate()) : null;

            case COLLECTION:
                PublicationVersion publicationVersion = commonService.retrievePublicationVersion(agencyID, resourceID);
                return publicationVersion != null ? getMostRecentDate(publicationVersion.getSiemacMetadataStatisticalResource().getLastUpdate().toDate(),
                                                                      publicationVersion.getLifeCycleStatisticalResource().getLastUpdated().toDate()) : null;

            case MULTIDATASET:
                MultidatasetVersion multidatasetVersion = commonService.retrieveMultidatasetVersionWithCubes(agencyID, resourceID);
                if (multidatasetVersion != null) {
                    List<Date> dates = new ArrayList<>();

                    dates.add(multidatasetVersion.getSiemacMetadataStatisticalResource().getLastUpdate().toDate());
                    dates.add(multidatasetVersion.getLifeCycleStatisticalResource().getLastUpdated().toDate());

                    if (multidatasetVersion.getCubes() != null) {
                        for (MultidatasetCube cube : multidatasetVersion.getCubes()) {
                            dates.add(cube.getLastUpdated().toDate());
                        }
                    }

                    return getMostRecentDate(dates.toArray(new Date[0]));
                }
            default:
                logger.warn("Unknown resource type: {}", resourceType);
        }

        return null;
    }

    private String extractPathParam(Message message, String paramName) {
        Object pathParamsObj = message.get(URITemplate.TEMPLATE_PARAMETERS);

        if (pathParamsObj instanceof java.util.Map) {
            java.util.Map<?, ?> pathParams = (java.util.Map<?, ?>) pathParamsObj;
            Object valueList = pathParams.get(paramName);

            if (valueList instanceof java.util.List && !((java.util.List<?>) valueList).isEmpty()) {
                Object value = ((java.util.List<?>) valueList).get(0);
                return value != null ? value.toString() : null;
            }
        }

        return null;
    }

    private Date getMostRecentDate(Date... dates) {
        if (dates == null || dates.length == 0) {
            return null;
        }

        Date mostRecent = null;
        for (Date date : dates) {
            if (date != null) {
                if (mostRecent == null || date.after(mostRecent)) {
                    mostRecent = date;
                }
            }
        }

        return mostRecent;
    }

    public Date parseHttpDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }

        try {
            return Date.from(ZonedDateTime.parse(dateString, DateTimeFormatter.RFC_1123_DATE_TIME).toInstant());
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isModifiedSince(Date lastModified, Date clientDate) {
        if (lastModified == null || clientDate == null) {
            return true; // if we can't determine, assume modified
        }

        // truncate to seconds precision (HTTP dates don't include milliseconds)
        long lastModifiedSeconds = lastModified.getTime() / 1000;
        long clientDateSeconds = clientDate.getTime() / 1000;

        return lastModifiedSeconds > clientDateSeconds;
    }
}
