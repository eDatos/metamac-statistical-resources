package org.siemac.metamac.rest.statistical_resources.constants;

import org.siemac.metamac.rest.common.v1_0.domain.ComparisonOperator;
import org.siemac.metamac.rest.common.v1_0.domain.LogicalOperator;
import org.siemac.metamac.rest.common.v1_0.domain.OrderOperator;

public class RestTestConstants {

    public static String NOT_EXISTS                                = "notexists";
    public static String AGENCY_1                                  = "agency1";
    public static String AGENCY_2                                  = "agency2";

    public static String DATASET_1_CODE                            = "dataset1";
    public static String DATASET_2_CODE                            = "dataset2";
    public static String DATASET_3_CODE                            = "dataset3";
    public static String DATASET_4_CODE                            = "dataset4";

    public static String COLLECTION_1_CODE                         = "collection1";
    public static String COLLECTION_2_CODE                         = "collection2";
    public static String COLLECTION_3_CODE                         = "collection3";
    public static String COLLECTION_4_CODE                         = "collection4";

    public static String QUERY_1_CODE                              = "query1";
    public static String QUERY_2_CODE                              = "query2";
    public static String QUERY_3_CODE                              = "query3";
    public static String QUERY_4_CODE                              = "query4";

    public static String VERSION_1                                 = "01.000";
    public static String VERSION_2                                 = "02.000";

    public static String QUERY_ID_LIKE_1                           = "ID " + ComparisonOperator.LIKE + " \"1\"";
    public static String QUERY_ID_LIKE_1_NAME_LIKE_2               = "ID " + ComparisonOperator.LIKE + " \"1\"" + " " + LogicalOperator.AND + " " + "NAME " + ComparisonOperator.LIKE + " \"2\"";
    public static String QUERY_LATEST                              = "LATEST " + ComparisonOperator.EQ + " \"true\"";

    public static String ORDER_BY_ID_DESC                          = "ID " + OrderOperator.DESC;

    public static String ATTRIBUTE_1_GLOBAL                        = "at1";
    public static String ATTRIBUTE_2_GLOBAL                        = "at2";
    public static String ATTRIBUTE_3_DIMENSION                     = "at3";
    public static String ATTRIBUTE_4_DIMENSION                     = "at4";
    public static String ATTRIBUTE_5_DIMENSION                     = "at5";
    public static String ATTRIBUTE_6_DIMENSION                     = "at6";
    public static String ATTRIBUTE_7_DIMENSION                     = "at7";
    public static String ATTRIBUTE_8_DIMENSION                     = "at8";
    public static String ATTRIBUTE_9_DIMENSION                     = "at9";
    public static String ATTRIBUTE_10_OBSERVATION                  = "at10";
    public static String ATTRIBUTE_11_OBSERVATION                  = "at11";
    public static String ATTRIBUTE_MULTILINGUAL_1_GLOBAL           = "at12";
    public static String ATTRIBUTE_MULTILINGUAL_2_GLOBAL           = "at13";
    public static String ATTRIBUTE_DIMENSION_MULTILINGUAL_1_GLOBAL = "at14";
    public static String ATTRIBUTE_GROUP_MULTILINGUAL_1_GLOBAL     = "at15";
}
