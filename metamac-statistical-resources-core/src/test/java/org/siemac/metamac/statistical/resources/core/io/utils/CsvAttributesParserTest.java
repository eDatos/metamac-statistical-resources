package org.siemac.metamac.statistical.resources.core.io.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.statistical.resources.core.dataset.domain.CodeDimension;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdGranularityAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.utils.SrmMockUtils;

/**
 * Unit tests for {@link CsvAttributesParser}.
 * Tests cover:
 * - Parsing TSV format with TIPO_INSTANCIA column
 * - Validation of granularity codes against the default frequency classification
 * - Error accumulation across multiple rows
 */
public class CsvAttributesParserTest {

    private static final String       TIME_DIMENSION_ID       = "TIME_PERIOD";
    private static final String       ATTRIBUTE_ID            = "ATTR_01";
    private static final String       CODELIST_URN            = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=ISTAC:CL_FREQ(1.0)";

    private static final List<String> VALID_GRANULARITY_CODES = Arrays.asList("A", "S", "Q", "M", "W", "D");

    // TSV column header line
    private static final String       NEW_FORMAT_HEADER       = "ID_ATRIBUTO\tTIPO_INSTANCIA\tDIMENSIONES\tVALORES_DIMENSION\tVALOR_ATRIBUTO#es";

    // ─── Test helpers ────────────────────────────────────────────────────────────

    private CsvAttributesParser buildParser(String tsvContent, List<String> validGranularityCodes, String codelistUrn) throws Exception {
        InputStream is = new ByteArrayInputStream(tsvContent.getBytes(StandardCharsets.UTF_8));
        return new CsvAttributesParser(is, "UTF-8", '\t', SrmMockUtils.buildDsdWithTimeDimensionAndTextAttribute(TIME_DIMENSION_ID, ATTRIBUTE_ID), Arrays.asList("es"), validGranularityCodes,
                codelistUrn);
    }

    private void parseAllLines(CsvAttributesParser parser, List<DsdAttributeInstanceDto> valueInstances, List<DsdGranularityAttributeInstanceDto> granularityInstances) throws Exception {
        Map<String, List<CodeDimension>> codeDimensions = new HashMap<>();
        Map<String, List<ExternalItemDto>> externalItems = new HashMap<>();
        String idAttribute;
        do {
            idAttribute = parser.nextLine(valueInstances, granularityInstances, codeDimensions, externalItems);
        } while (idAttribute != null);
    }

    // ─── New format — granularity instances ──────────────────────────────────────

    @Test
    public void testNewFormat_granularityInstance_validCodes() throws Exception {
        String tsv = NEW_FORMAT_HEADER + "\n" + ATTRIBUTE_ID + "\tGRANULARITY\t" + TIME_DIMENSION_ID + "\tA, Q\ttest-value";

        CsvAttributesParser parser = buildParser(tsv, VALID_GRANULARITY_CODES, CODELIST_URN);

        List<DsdAttributeInstanceDto> valueInstances = new ArrayList<>();
        List<DsdGranularityAttributeInstanceDto> granularityInstances = new ArrayList<>();
        parseAllLines(parser, valueInstances, granularityInstances);

        assertTrue(parser.getExceptions().isEmpty());
        assertTrue(valueInstances.isEmpty());
        assertEquals(1, granularityInstances.size());

        DsdGranularityAttributeInstanceDto dto = granularityInstances.get(0);
        assertEquals(ATTRIBUTE_ID, dto.getAttributeId());
        assertNotNull(dto.getGranularityCodesByDimension());
        assertEquals(Arrays.asList("A", "Q"), dto.getGranularityCodesByDimension().get(TIME_DIMENSION_ID));
    }

    @Test
    public void testNewFormat_granularityInstance_singleCode() throws Exception {
        String tsv = NEW_FORMAT_HEADER + "\n" + ATTRIBUTE_ID + "\tGRANULARITY\t" + TIME_DIMENSION_ID + "\tA\ttest-value";

        CsvAttributesParser parser = buildParser(tsv, VALID_GRANULARITY_CODES, CODELIST_URN);

        List<DsdAttributeInstanceDto> valueInstances = new ArrayList<>();
        List<DsdGranularityAttributeInstanceDto> granularityInstances = new ArrayList<>();
        parseAllLines(parser, valueInstances, granularityInstances);

        assertTrue(parser.getExceptions().isEmpty());
        assertEquals(1, granularityInstances.size());
        assertEquals(Arrays.asList("A"), granularityInstances.get(0).getGranularityCodesByDimension().get(TIME_DIMENSION_ID));
    }

    // ─── New format — value instances ────────────────────────────────────────────

    @Test
    public void testNewFormat_valueInstance() throws Exception {
        String tsv = NEW_FORMAT_HEADER + "\n" + ATTRIBUTE_ID + "\tVALUE\t" + TIME_DIMENSION_ID + "\t2010\ttest-value";

        CsvAttributesParser parser = buildParser(tsv, VALID_GRANULARITY_CODES, CODELIST_URN);

        List<DsdAttributeInstanceDto> valueInstances = new ArrayList<>();
        List<DsdGranularityAttributeInstanceDto> granularityInstances = new ArrayList<>();
        parseAllLines(parser, valueInstances, granularityInstances);

        assertTrue(parser.getExceptions().isEmpty());
        assertEquals(1, valueInstances.size());
        assertTrue(granularityInstances.isEmpty());
        assertEquals(ATTRIBUTE_ID, valueInstances.get(0).getAttributeId());
    }

    @Test
    public void testNewFormat_bothValueAndGranularityInstances() throws Exception {
        String tsv = NEW_FORMAT_HEADER + "\n" + ATTRIBUTE_ID + "\tVALUE\t" + TIME_DIMENSION_ID + "\t2010\tvalue-for-period\n" + ATTRIBUTE_ID + "\tGRANULARITY\t" + TIME_DIMENSION_ID
                + "\tA, Q\tvalue-for-granularity";

        CsvAttributesParser parser = buildParser(tsv, VALID_GRANULARITY_CODES, CODELIST_URN);

        List<DsdAttributeInstanceDto> valueInstances = new ArrayList<>();
        List<DsdGranularityAttributeInstanceDto> granularityInstances = new ArrayList<>();
        parseAllLines(parser, valueInstances, granularityInstances);

        assertTrue(parser.getExceptions().isEmpty());
        assertEquals(1, valueInstances.size());
        assertEquals(1, granularityInstances.size());
        assertEquals(ATTRIBUTE_ID, valueInstances.get(0).getAttributeId());
        assertEquals(ATTRIBUTE_ID, granularityInstances.get(0).getAttributeId());
        assertEquals(Arrays.asList("A", "Q"), granularityInstances.get(0).getGranularityCodesByDimension().get(TIME_DIMENSION_ID));
    }

    // ─── Granularity code validation ─────────────────────────────────────────────

    @Test
    public void testInvalidGranularityCode_addsException() throws Exception {
        String tsv = NEW_FORMAT_HEADER + "\n" + ATTRIBUTE_ID + "\tGRANULARITY\t" + TIME_DIMENSION_ID + "\tX\ttest-value";

        CsvAttributesParser parser = buildParser(tsv, VALID_GRANULARITY_CODES, CODELIST_URN);

        List<DsdAttributeInstanceDto> valueInstances = new ArrayList<>();
        List<DsdGranularityAttributeInstanceDto> granularityInstances = new ArrayList<>();
        parseAllLines(parser, valueInstances, granularityInstances);

        assertEquals(1, parser.getExceptions().size());
        assertEquals(ServiceExceptionType.IMPORTATION_ATTRIBUTES_GRANULARITY_CODE_INVALID.getCode(), parser.getExceptions().get(0).getCode());
        assertEquals("X", parser.getExceptions().get(0).getMessageParameters()[0]);
        assertEquals(CODELIST_URN, parser.getExceptions().get(0).getMessageParameters()[1]);
    }

    @Test
    public void testInvalidGranularityCode_invalidCodeExcludedFromResult() throws Exception {
        // "X" is invalid and must NOT appear in the resulting granularity codes list
        String tsv = NEW_FORMAT_HEADER + "\n" + ATTRIBUTE_ID + "\tGRANULARITY\t" + TIME_DIMENSION_ID + "\tA, X, Q\ttest-value";

        CsvAttributesParser parser = buildParser(tsv, VALID_GRANULARITY_CODES, CODELIST_URN);

        List<DsdAttributeInstanceDto> valueInstances = new ArrayList<>();
        List<DsdGranularityAttributeInstanceDto> granularityInstances = new ArrayList<>();
        parseAllLines(parser, valueInstances, granularityInstances);

        assertEquals(1, parser.getExceptions().size());
        assertEquals("X", parser.getExceptions().get(0).getMessageParameters()[0]);

        assertEquals(1, granularityInstances.size());
        List<String> codes = granularityInstances.get(0).getGranularityCodesByDimension().get(TIME_DIMENSION_ID);
        assertEquals(Arrays.asList("A", "Q"), codes);
    }

    @Test
    public void testMultipleInvalidGranularityCodes_errorsAccumulated() throws Exception {
        // Each invalid code adds a separate exception — all errors are collected, not just the first
        String tsv = NEW_FORMAT_HEADER + "\n" + ATTRIBUTE_ID + "\tGRANULARITY\t" + TIME_DIMENSION_ID + "\tBAD1\ttest-value\n" + ATTRIBUTE_ID + "\tGRANULARITY\t" + TIME_DIMENSION_ID
                + "\tBAD2\ttest-value";

        CsvAttributesParser parser = buildParser(tsv, VALID_GRANULARITY_CODES, CODELIST_URN);

        List<DsdAttributeInstanceDto> valueInstances = new ArrayList<>();
        List<DsdGranularityAttributeInstanceDto> granularityInstances = new ArrayList<>();
        parseAllLines(parser, valueInstances, granularityInstances);

        assertEquals(2, parser.getExceptions().size());
        assertEquals("BAD1", parser.getExceptions().get(0).getMessageParameters()[0]);
        assertEquals("BAD2", parser.getExceptions().get(1).getMessageParameters()[0]);
    }

    @Test
    public void testEmptyValidGranularityCodes_skipsValidation() throws Exception {
        // When no valid codes list is configured, any code is accepted without error
        String tsv = NEW_FORMAT_HEADER + "\n" + ATTRIBUTE_ID + "\tGRANULARITY\t" + TIME_DIMENSION_ID + "\tUNKNOWN_CODE\ttest-value";

        CsvAttributesParser parser = buildParser(tsv, Collections.<String> emptyList(), CODELIST_URN);

        List<DsdAttributeInstanceDto> valueInstances = new ArrayList<>();
        List<DsdGranularityAttributeInstanceDto> granularityInstances = new ArrayList<>();
        parseAllLines(parser, valueInstances, granularityInstances);

        assertTrue(parser.getExceptions().isEmpty());
        assertEquals(1, granularityInstances.size());
        assertEquals(Arrays.asList("UNKNOWN_CODE"), granularityInstances.get(0).getGranularityCodesByDimension().get(TIME_DIMENSION_ID));
    }

    // ─── Structural validation ────────────────────────────────────────────────────

    @Test
    public void testMissingHeader_throwsException() {
        try {
            buildParser("", VALID_GRANULARITY_CODES, CODELIST_URN);
            fail("Expected exception for missing header");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Header not found"));
        }
    }

    @Test
    public void testLegacyFormatWithoutInstanceTypeColumn_throwsException() {
        // Old TSV format without TIPO_INSTANCIA column is no longer accepted
        String legacyHeader = "ID_ATRIBUTO\tDIMENSIONES\tVALORES_DIMENSION\tVALOR_ATRIBUTO#es";
        String tsv = legacyHeader + "\n" + ATTRIBUTE_ID + "\t" + TIME_DIMENSION_ID + "\t2010\ttest-value";
        try {
            buildParser(tsv, VALID_GRANULARITY_CODES, CODELIST_URN);
            fail("Expected exception for missing TIPO_INSTANCIA column");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("TIPO_INSTANCIA"));
        }
    }

    @Test
    public void testInvalidAttributeId_addsException() throws Exception {
        String tsv = NEW_FORMAT_HEADER + "\n" + "UNKNOWN_ATTR\tVALUE\t" + TIME_DIMENSION_ID + "\t2010\ttest-value";

        CsvAttributesParser parser = buildParser(tsv, VALID_GRANULARITY_CODES, CODELIST_URN);

        List<DsdAttributeInstanceDto> valueInstances = new ArrayList<>();
        List<DsdGranularityAttributeInstanceDto> granularityInstances = new ArrayList<>();
        parseAllLines(parser, valueInstances, granularityInstances);

        assertEquals(1, parser.getExceptions().size());
        assertEquals(ServiceExceptionType.IMPORTATION_ATTRIBUTES_ATTRIBUTE_ID_INVALID.getCode(), parser.getExceptions().get(0).getCode());
    }

    @Test
    public void testInvalidGranularityCode_andInvalidAttributeId_bothErrorsAccumulated() throws Exception {
        String tsv = NEW_FORMAT_HEADER + "\n" + "UNKNOWN_ATTR\tGRANULARITY\t" + TIME_DIMENSION_ID + "\tBAD_CODE\ttest-value";

        CsvAttributesParser parser = buildParser(tsv, VALID_GRANULARITY_CODES, CODELIST_URN);

        List<DsdAttributeInstanceDto> valueInstances = new ArrayList<>();
        List<DsdGranularityAttributeInstanceDto> granularityInstances = new ArrayList<>();
        parseAllLines(parser, valueInstances, granularityInstances);

        // Two exceptions: one for the unknown attribute ID, one for the invalid granularity code
        assertEquals(2, parser.getExceptions().size());
        assertEquals(ServiceExceptionType.IMPORTATION_ATTRIBUTES_ATTRIBUTE_ID_INVALID.getCode(), parser.getExceptions().get(0).getCode());
        assertEquals(ServiceExceptionType.IMPORTATION_ATTRIBUTES_GRANULARITY_CODE_INVALID.getCode(), parser.getExceptions().get(1).getCode());
    }
}
