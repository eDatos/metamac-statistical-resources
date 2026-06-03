package org.siemac.metamac.statistical_resources.rest.common.impl.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceBasicDto;
import es.gobcan.istac.edatos.dataset.repository.dto.GranularityAttributeInstanceDto;
import es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto;
import es.gobcan.istac.edatos.dataset.repository.dto.LocalisedStringDto;

public class GranularityAttributeResolverTest {

    private static final String TIME_DIM  = "TIME_PERIOD";
    private static final String GEO_DIM   = "GEO_DIM";
    private static final String OTHER_DIM = "dim01";

    // -------------------------------------------------------------------------
    // buildGranularityAttributesByCodeDimensions — single temporal dimension
    // -------------------------------------------------------------------------

    @Test
    public void testYearlyGranularity_onlyAnnualCodesMatch() throws MetamacException {
        List<String> dims = Arrays.asList(TIME_DIM);
        Map<String, List<String>> effective = effectiveCodes(TIME_DIM, "2011", "2012", "2013-Q1", "2013-Q2");
        GranularityAttributeInstanceDto instance = instance("val1", granularityCodes(TIME_DIM, "A"), null);

        Map<String, AttributeInstanceBasicDto> result = GranularityAttributeResolver.buildGranularityAttributesByCodeDimensions(dims, effective, list(instance));

        assertEquals(2, result.size());
        assertTrue(result.containsKey("2011"));
        assertTrue(result.containsKey("2012"));
    }

    @Test
    public void testQuarterlyGranularity_onlyQuarterlyCodesMatch() throws MetamacException {
        List<String> dims = Arrays.asList(TIME_DIM);
        Map<String, List<String>> effective = effectiveCodes(TIME_DIM, "2011", "2012", "2013-Q1", "2013-Q2");
        GranularityAttributeInstanceDto instance = instance("val1", granularityCodes(TIME_DIM, "Q"), null);

        Map<String, AttributeInstanceBasicDto> result = GranularityAttributeResolver.buildGranularityAttributesByCodeDimensions(dims, effective, list(instance));

        assertEquals(2, result.size());
        assertTrue(result.containsKey("2013-Q1"));
        assertTrue(result.containsKey("2013-Q2"));
    }

    @Test
    public void testMonthlyGranularity_onlyMonthlyCodesMatch() throws MetamacException {
        List<String> dims = Arrays.asList(TIME_DIM);
        Map<String, List<String>> effective = effectiveCodes(TIME_DIM, "2011", "2013-Q1", "2013-M01", "2013-M02");
        GranularityAttributeInstanceDto instance = instance("val1", granularityCodes(TIME_DIM, "M"), null);

        Map<String, AttributeInstanceBasicDto> result = GranularityAttributeResolver.buildGranularityAttributesByCodeDimensions(dims, effective, list(instance));

        assertEquals(2, result.size());
        assertTrue(result.containsKey("2013-M01"));
        assertTrue(result.containsKey("2013-M02"));
    }

    @Test
    public void testMultipleGranularities_annualAndQuarterly_bothMatch() throws MetamacException {
        List<String> dims = Arrays.asList(TIME_DIM);
        Map<String, List<String>> effective = effectiveCodes(TIME_DIM, "2011", "2012", "2013-Q1", "2013-Q2");
        GranularityAttributeInstanceDto instance = instance("val1", granularityCodes(TIME_DIM, "A", "Q"), null);

        Map<String, AttributeInstanceBasicDto> result = GranularityAttributeResolver.buildGranularityAttributesByCodeDimensions(dims, effective, list(instance));

        assertEquals(4, result.size());
        assertTrue(result.containsKey("2011"));
        assertTrue(result.containsKey("2012"));
        assertTrue(result.containsKey("2013-Q1"));
        assertTrue(result.containsKey("2013-Q2"));
    }

    @Test
    public void testNoMatchingCodes_instanceSkipped() throws MetamacException {
        List<String> dims = Arrays.asList(TIME_DIM);
        Map<String, List<String>> effective = effectiveCodes(TIME_DIM, "2011", "2012");
        GranularityAttributeInstanceDto instance = instance("val1", granularityCodes(TIME_DIM, "Q"), null);

        Map<String, AttributeInstanceBasicDto> result = GranularityAttributeResolver.buildGranularityAttributesByCodeDimensions(dims, effective, list(instance));

        assertTrue(result.isEmpty());
    }

    // -------------------------------------------------------------------------
    // buildGranularityAttributesByCodeDimensions — multiple dimensions
    // -------------------------------------------------------------------------

    @Test
    public void testMultipleDimensions_specificNonTemporalCodes_generatesCombinations() throws MetamacException {
        List<String> dims = Arrays.asList(GEO_DIM, TIME_DIM);
        Map<String, List<String>> effective = new HashMap<String, List<String>>();
        effective.put(GEO_DIM, Arrays.asList("tenerife", "lanzarote"));
        effective.put(TIME_DIM, Arrays.asList("2011", "2012", "2013-Q1"));

        // Instance specifies only tenerife for GEO_DIM, annual for TIME
        GranularityAttributeInstanceDto instance = instance("val1", granularityCodes(TIME_DIM, "A"), singleDimCodes(GEO_DIM, "tenerife"));

        Map<String, AttributeInstanceBasicDto> result = GranularityAttributeResolver.buildGranularityAttributesByCodeDimensions(dims, effective, list(instance));

        assertEquals(2, result.size());
        assertTrue(result.containsKey("tenerife#2011"));
        assertTrue(result.containsKey("tenerife#2012"));
    }

    @Test
    public void testMultipleDimensions_noSpecificNonTemporalCodes_usesAllEffectiveCodes() throws MetamacException {
        List<String> dims = Arrays.asList(GEO_DIM, TIME_DIM);
        Map<String, List<String>> effective = new HashMap<String, List<String>>();
        effective.put(GEO_DIM, Arrays.asList("tenerife", "lanzarote"));
        effective.put(TIME_DIM, Arrays.asList("2011", "2012"));

        // Instance has no codesByDimension -> uses all effective GEO codes
        GranularityAttributeInstanceDto instance = instance("val1", granularityCodes(TIME_DIM, "A"), null);

        Map<String, AttributeInstanceBasicDto> result = GranularityAttributeResolver.buildGranularityAttributesByCodeDimensions(dims, effective, list(instance));

        assertEquals(4, result.size());
        assertTrue(result.containsKey("tenerife#2011"));
        assertTrue(result.containsKey("tenerife#2012"));
        assertTrue(result.containsKey("lanzarote#2011"));
        assertTrue(result.containsKey("lanzarote#2012"));
    }

    @Test
    public void testMultipleDimensions_temporalNoMatch_wholeInstanceSkipped() throws MetamacException {
        List<String> dims = Arrays.asList(GEO_DIM, OTHER_DIM, TIME_DIM);
        Map<String, List<String>> effective = new HashMap<String, List<String>>();
        effective.put(GEO_DIM, Arrays.asList("tenerife"));
        effective.put(OTHER_DIM, Arrays.asList("code01"));
        effective.put(TIME_DIM, Arrays.asList("2011", "2012")); // annual only

        // Instance requests quarterly — no annual codes exist, skip
        GranularityAttributeInstanceDto instance = instance("val1", granularityCodes(TIME_DIM, "Q"), null);

        Map<String, AttributeInstanceBasicDto> result = GranularityAttributeResolver.buildGranularityAttributesByCodeDimensions(dims, effective, list(instance));

        assertTrue(result.isEmpty());
    }

    // -------------------------------------------------------------------------
    // buildGranularityAttributesByCodeDimensions — instance properties
    // -------------------------------------------------------------------------

    @Test
    public void testInstanceWithNullGranularityCodesByDimension_isSkipped() throws MetamacException {
        List<String> dims = Arrays.asList(TIME_DIM);
        Map<String, List<String>> effective = effectiveCodes(TIME_DIM, "2011");
        GranularityAttributeInstanceDto instance = new GranularityAttributeInstanceDto();
        instance.setGranularityCodesByDimension(null);

        Map<String, AttributeInstanceBasicDto> result = GranularityAttributeResolver.buildGranularityAttributesByCodeDimensions(dims, effective, list(instance));

        assertTrue(result.isEmpty());
    }

    @Test
    public void testInstanceWithEmptyGranularityCodesByDimension_isSkipped() throws MetamacException {
        List<String> dims = Arrays.asList(TIME_DIM);
        Map<String, List<String>> effective = effectiveCodes(TIME_DIM, "2011");
        GranularityAttributeInstanceDto instance = new GranularityAttributeInstanceDto();
        instance.setGranularityCodesByDimension(new HashMap<String, List<String>>());

        Map<String, AttributeInstanceBasicDto> result = GranularityAttributeResolver.buildGranularityAttributesByCodeDimensions(dims, effective, list(instance));

        assertTrue(result.isEmpty());
    }

    @Test
    public void testEmptyInstanceList_returnsEmptyMap() throws MetamacException {
        List<String> dims = Arrays.asList(TIME_DIM);
        Map<String, List<String>> effective = effectiveCodes(TIME_DIM, "2011");

        Map<String, AttributeInstanceBasicDto> result = GranularityAttributeResolver.buildGranularityAttributesByCodeDimensions(
                dims, effective, Collections.<GranularityAttributeInstanceDto>emptyList());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // -------------------------------------------------------------------------
    // buildGranularityAttributesByCodeDimensions — precedence
    // -------------------------------------------------------------------------

    @Test
    public void testTwoInstancesSameKey_firstInstanceWins() throws MetamacException {
        List<String> dims = Arrays.asList(TIME_DIM);
        Map<String, List<String>> effective = effectiveCodes(TIME_DIM, "2011");

        GranularityAttributeInstanceDto first  = instance("first",  granularityCodes(TIME_DIM, "A"), null);
        GranularityAttributeInstanceDto second = instance("second", granularityCodes(TIME_DIM, "A"), null);

        Map<String, AttributeInstanceBasicDto> result = GranularityAttributeResolver.buildGranularityAttributesByCodeDimensions(dims, effective, Arrays.asList(first, second));

        assertEquals(1, result.size());
        assertSame(first, result.get("2011"));
    }

    @Test
    public void testInstanceValue_isPreservedInResult() throws MetamacException {
        List<String> dims = Arrays.asList(TIME_DIM);
        Map<String, List<String>> effective = effectiveCodes(TIME_DIM, "2011");
        GranularityAttributeInstanceDto instance = instance("expectedValue", granularityCodes(TIME_DIM, "A"), null);

        Map<String, AttributeInstanceBasicDto> result = GranularityAttributeResolver.buildGranularityAttributesByCodeDimensions(dims, effective, list(instance));

        assertSame(instance, result.get("2011"));
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private static GranularityAttributeInstanceDto instance(String value, Map<String, List<String>> granularityCodes, Map<String, List<String>> codesByDimension) {
        GranularityAttributeInstanceDto dto = new GranularityAttributeInstanceDto();
        InternationalStringDto internationalStringDto = new InternationalStringDto();
        internationalStringDto.addText(new LocalisedStringDto("es", value));
        dto.setValue(internationalStringDto);
        dto.setGranularityCodesByDimension(granularityCodes);
        dto.setCodesByDimension(codesByDimension);
        return dto;
    }

    private static Map<String, List<String>> granularityCodes(String dimId, String... codes) {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        map.put(dimId, Arrays.asList(codes));
        return map;
    }

    private static Map<String, List<String>> singleDimCodes(String dimId, String... codes) {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        map.put(dimId, Arrays.asList(codes));
        return map;
    }

    private static Map<String, List<String>> effectiveCodes(String dimId, String... codes) {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        map.put(dimId, Arrays.asList(codes));
        return map;
    }

    private static List<GranularityAttributeInstanceDto> list(GranularityAttributeInstanceDto... instances) {
        return Arrays.asList(instances);
    }
}
