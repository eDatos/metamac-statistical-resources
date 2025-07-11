package org.siemac.metamac.statistical_resources.rest.common.impl.utils;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response.Status;

import org.siemac.metamac.rest.exception.RestCommonServiceExceptionType;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MathUtils {

    private static final Logger logger = LoggerFactory.getLogger(MathUtils.class);

    public static int calculateDataSize(List<String> dimensions, Map<String, List<String>> dimensionsCodesSelectedEffective) {
        int dataSize = 1;
        for (String dimension : dimensions) {
            dataSize = safeMultiply(dataSize, dimensionsCodesSelectedEffective.get(dimension).size());
        }
        return dataSize;
    }

    private static int safeMultiply(int a, int b) {
        long res = (long) a * (long) b;
        if (res > Integer.MAX_VALUE || res < Integer.MIN_VALUE) {
            logger.error("An overflow occurred while performing the multiplication " + a + " and " + b);
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestCommonServiceExceptionType.UNKNOWN);
            throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }
        return (int) res;
    }
}
