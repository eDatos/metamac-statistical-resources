package org.siemac.metamac.statistical_resources.rest.common.impl.exceptions;

import org.siemac.metamac.rest.exception.RestCommonServiceExceptionType;

public class RestStatisticalResourcesCommonServiceExceptionType extends RestCommonServiceExceptionType {

    public static final RestCommonServiceExceptionType DATASET_NOT_FOUND                = create("exception.statistical_resources.dataset.not_found");
    public static final RestCommonServiceExceptionType COLLECTION_NOT_FOUND             = create("exception.statistical_resources.collection.not_found");
    public static final RestCommonServiceExceptionType QUERY_NOT_FOUND                  = create("exception.statistical_resources.query.not_found");
    public static final RestCommonServiceExceptionType MULTIDATASET_NOT_FOUND           = create("exception.statistical_resources.multidataset.not_found");
    public static final RestCommonServiceExceptionType OBSERVATIONS_EXCEED_MAX_FOR_XLSX = create("exception.statistical_resources..observations_exceed_max_for_xlsx");
}