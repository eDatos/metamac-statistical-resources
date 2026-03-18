package org.siemac.metamac.statistical.resources.core.utils.asserts;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.siemac.metamac.statistical.resources.core.geocache.domain.GeoCacheResource;

public class GeoCacheAssertUtils {

    public static void assertEqualsArrayGeoCacheResource(Set<GeoCacheResource> actual, Set<GeoCacheResource> expected, String urnRelated) {
        List<GeoCacheResource> geoCacheResourceNotFound = new ArrayList<>();
        for (GeoCacheResource geoCacheResourceActual : actual) {
            boolean found = false;
            for (GeoCacheResource geoCacheResourceExpected : expected) {
                if (geoCacheResourceActual.getUrn().equals(geoCacheResourceExpected.getUrn())) {
                    assertEqualsGeoCacheResource(geoCacheResourceActual, geoCacheResourceExpected);
                    found = true;
                    break;
                }
            }
            if (!found) {
                geoCacheResourceNotFound.add(geoCacheResourceActual);
            }
        }

        if (!geoCacheResourceNotFound.isEmpty()) {
            String urns = "";
            for (GeoCacheResource geoCacheResource : geoCacheResourceNotFound) {
                if (!urns.isEmpty()) {
                    urns += ", ";
                }
                urns += geoCacheResource.getUrn();
            }

            Assert.fail(String.format("The next related resources to %s were not found: %s", urnRelated, urns));
        }
    }

    private static void assertEqualsGeoCacheResource(GeoCacheResource geoCacheResourceActual, GeoCacheResource geoCacheResourceExpected) {
        assertEquals(geoCacheResourceActual.getType(), geoCacheResourceExpected.getType());
        assertEquals(geoCacheResourceActual.getCode(), geoCacheResourceExpected.getCode());
        assertEquals(geoCacheResourceActual.getHtmlLink(), geoCacheResourceExpected.getHtmlLink());
        assertEquals(geoCacheResourceActual.getOperationCode(), geoCacheResourceExpected.getOperationCode());
        assertEquals(geoCacheResourceActual.getOperationUrn(), geoCacheResourceExpected.getOperationUrn());
        assertEquals(geoCacheResourceActual.getTerritories().size(), geoCacheResourceExpected.getTerritories().size());
    }
}
