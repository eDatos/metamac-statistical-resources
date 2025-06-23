package org.siemac.metamac.statistical.resources.web.client.multidataset.model.ds;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;

public class MultidatasetCubeDS extends DataSource {

    public static final String IDENTIFIER            = "str-identifier";
    public static final String TITLE                 = "str-title";
    public static final String DESCRIPTION           = "str-description";
    public static final String ORDER_IN_LEVEL        = "str-order-level";
    public static final String URN                   = "str-urn";

    public static final String RESOURCE_TYPE_TO_LINK = "str-type-link";  // Not mapped in DTO
    public static final String DATASET               = "str-dataset";
    public static final String QUERY                 = "str-query";

    public static final String INFO                  = "str-info";

    public static final String DTO                   = "str-dto";

    public static final String DSD                   = "str-dsd";
    public static final String QUERY_DATASET    = "str-query-dataset";

    public MultidatasetCubeDS() {
        DataSourceIntegerField identifier = new DataSourceIntegerField(URN, getConstants().multidatasetStructureCubeURN());
        identifier.setPrimaryKey(true);
        addField(identifier);
    }
}
