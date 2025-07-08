package org.siemac.metamac.statistical.resources.web.client.query.model.ds;

import org.siemac.metamac.statistical.resources.web.client.model.ds.LifeCycleResourceDS;

public class QueryDS extends LifeCycleResourceDS {

    public static final String ID                      = "query-id";
    public static final String TYPE                    = "query-type";
    public static final String PURPOSE_TYPE            = "purpose_query-type";
    public static final String X_TEMPLATE              = "x-query-template";
    public static final String TYPE_GRANULARITIES      = "query-granularities";
    public static final String STATUS                  = "query-status";

    public static final String RELATED_DATASET_VERSION = "query-dataset-version";
    public static final String SELECTION               = "query-dataset-version";
    public static final String LATEST_N_DATA           = "query-latest-n-data";

    // RESOURCE RELATION DESCRIPTORS
    public static final String IS_PART_OF              = "query-is-part-of";

    public QueryDS() {
        super();
    }
}
