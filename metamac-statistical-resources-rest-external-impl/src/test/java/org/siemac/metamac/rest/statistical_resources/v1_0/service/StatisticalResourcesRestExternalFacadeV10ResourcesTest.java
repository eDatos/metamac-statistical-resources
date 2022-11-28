package org.siemac.metamac.rest.statistical_resources.v1_0.service;

import org.junit.Test;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatisticalResourcesRestExternalFacadeV10ResourcesTest extends StatisticalResourcesRestExternalFacadeV10BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticalResourcesRestExternalFacadeV10ResourcesTest.class);

    @Test
    public void testFindResources() throws Exception {
        String query = "GEOCOV_VARELEM_ID EQ " + "'variableElement01'" + " AND IS_LAST_VERSION EQ 'true'";
        Resources resources = statisticalResourcesRestExternalFacadeClientXml.findResources(query, null, null, null, null);
        String d = "";
  
    }

}