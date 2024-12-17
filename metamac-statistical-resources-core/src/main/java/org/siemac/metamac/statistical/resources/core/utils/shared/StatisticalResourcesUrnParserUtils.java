package org.siemac.metamac.statistical.resources.core.utils.shared;

import static org.siemac.edatos.core.common.constants.shared.UrnConstants.URN_SIEMAC_CLASS_CHAPTER_PREFIX;
import static org.siemac.edatos.core.common.constants.shared.UrnConstants.URN_SIEMAC_CLASS_COLLECTION_PREFIX;
import static org.siemac.edatos.core.common.constants.shared.UrnConstants.URN_SIEMAC_CLASS_CUBE_PREFIX;
import static org.siemac.edatos.core.common.constants.shared.UrnConstants.URN_SIEMAC_CLASS_DATASET_PREFIX;
import static org.siemac.edatos.core.common.constants.shared.UrnConstants.URN_SIEMAC_CLASS_MULTIDATASET_PREFIX;
import static org.siemac.edatos.core.common.constants.shared.UrnConstants.URN_SIEMAC_CLASS_QUERY_PREFIX;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;

public class StatisticalResourcesUrnParserUtils {

    // PUBLICATIONS

    public static boolean isPublicationUrn(String urn) {
        return matches(URN_SIEMAC_CLASS_COLLECTION_PREFIX, UrnUtils.extractPrefix(urn));
    }

    public static boolean isPublicationChapterUrn(String urn) {
        return matches(URN_SIEMAC_CLASS_CHAPTER_PREFIX, UrnUtils.extractPrefix(urn));
    }

    public static boolean isPublicationCubeUrn(String urn) {
        return matches(URN_SIEMAC_CLASS_CUBE_PREFIX, UrnUtils.extractPrefix(urn));
    }

    public static String getPublicationVersionCodeFromUrnWithoutPrefix(String tripletIdentifier) {
        if (StringUtils.isBlank(tripletIdentifier)) {
            throw new IllegalArgumentException("Triplet identifier can not be blank");
        }

        return StatisticalResourcesUrnUtils.splitPublicationUrnWithoutPrefix(tripletIdentifier)[1];
    }

    // DATASETS

    public static boolean isDatasetUrn(String urn) {
        return matches(URN_SIEMAC_CLASS_DATASET_PREFIX, UrnUtils.extractPrefix(urn));
    }

    public static String getDatasetVersionCodeFromUrnWithoutPrefix(String tripletIdentifier) {
        if (StringUtils.isBlank(tripletIdentifier)) {
            throw new IllegalArgumentException("Triplet identifier can not be blank");
        }

        return StatisticalResourcesUrnUtils.splitDatasetUrnWithoutPrefix(tripletIdentifier)[1];
    }

    // QUERIES

    public static boolean isQueryUrn(String urn) {
        return matches(URN_SIEMAC_CLASS_QUERY_PREFIX, UrnUtils.extractPrefix(urn));
    }

    public static String getPrefixQueryUrn() {
        return URN_SIEMAC_CLASS_QUERY_PREFIX;
    }

    public static String getQueryVersionCodeFromUrnWithoutPrefix(String tripletIdentifier) {
        if (StringUtils.isBlank(tripletIdentifier)) {
            throw new IllegalArgumentException("Triplet identifier can not be blank");
        }

        return StatisticalResourcesUrnUtils.splitQueryUrnWithoutPrefix(tripletIdentifier)[1];
    }

    // MULTIDATASETS

    public static boolean isMultidatasetUrn(String urn) {
        return matches(URN_SIEMAC_CLASS_MULTIDATASET_PREFIX, UrnUtils.extractPrefix(urn));
    }

    public static String getMultidatasetVersionCodeFromUrnWithoutPrefix(String tripletIdentifier) {
        if (StringUtils.isBlank(tripletIdentifier)) {
            throw new IllegalArgumentException("Triplet identifier can not be blank");
        }

        return StatisticalResourcesUrnUtils.splitMultidatasetUrnWithoutPrefix(tripletIdentifier)[1];
    }

    public static String getPrefixMultidatasetUrn() {
        return URN_SIEMAC_CLASS_MULTIDATASET_PREFIX;
    }

    // Generic methods

    public static StatisticalResourceTypeEnum getResourceType(String resourceUrn) {

        if (StatisticalResourcesUrnParserUtils.isQueryUrn(resourceUrn)) {
            return StatisticalResourceTypeEnum.QUERY;
        } else if (StatisticalResourcesUrnParserUtils.isPublicationUrn(resourceUrn)) {
            return StatisticalResourceTypeEnum.COLLECTION;
        } else if (StatisticalResourcesUrnParserUtils.isMultidatasetUrn(resourceUrn)) {
            return StatisticalResourceTypeEnum.MULTIDATASET;
        }
        if (StatisticalResourcesUrnParserUtils.isDatasetUrn(resourceUrn)) {
            return StatisticalResourceTypeEnum.DATASET;
        }
        return null;
    }

    protected static boolean matches(String prefix, String urn) {
        if (StringUtils.isBlank(prefix)) {
            throw new IllegalArgumentException("Prefix can not be empty");
        }
        if (StringUtils.isBlank(urn)) {
            throw new IllegalArgumentException("URN can not be empty");
        }
        return StringUtils.equals(prefix, UrnUtils.extractPrefix(urn));
    }
}
