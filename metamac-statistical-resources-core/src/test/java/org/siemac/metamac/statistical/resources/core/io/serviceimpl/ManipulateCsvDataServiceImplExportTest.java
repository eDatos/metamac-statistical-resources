package org.siemac.metamac.statistical.resources.core.io.serviceimpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.utils.SrmMockUtils;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceDto;
import es.gobcan.istac.edatos.dataset.repository.dto.GranularityAttributeInstanceDto;
import es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto;
import es.gobcan.istac.edatos.dataset.repository.dto.LocalisedStringDto;
import es.gobcan.istac.edatos.dataset.repository.service.DatasetRepositoriesServiceFacade;

/**
 * Unit tests for the attribute export logic in {@link ManipulateCsvDataServiceImpl}.
 *
 * Tests cover:
 * - VALUE instances exported with TIPO_INSTANCIA=VALUE
 * - GRANULARITY instances exported with TIPO_INSTANCIA=GRANULARITY
 * - Both instance types coexisting in the same export
 * - TSV header always present
 * - Multiple granularity codes exported as comma-separated values
 */
@RunWith(MockitoJUnitRunner.class)
public class ManipulateCsvDataServiceImplExportTest {

    private static final String TIME_DIMENSION_ID  = "TIME_PERIOD";
    private static final String ATTRIBUTE_ID       = "ATTR_01";
    private static final String DATASET_REPO_ID    = "dsrepo-01";
    private static final List<String> LANGUAGES    = Arrays.asList("es");

    @Mock
    private DatasetRepositoriesServiceFacade datasetRepositoriesServiceFacade;

    @InjectMocks
    private ManipulateCsvDataServiceImpl service;

    private DatasetVersion datasetVersion;

    @Before
    public void setUp() {
        datasetVersion = Mockito.mock(DatasetVersion.class);
        Mockito.when(datasetVersion.getDatasetRepositoryId()).thenReturn(DATASET_REPO_ID);
    }

    // ─── Header ──────────────────────────────────────────────────────────────────

    @Test
    public void testExport_headerContainsTipoInstanciaColumn() throws Exception {
        mockFacade(Collections.<AttributeInstanceDto>emptyList(), Collections.<GranularityAttributeInstanceDto>emptyList());

        String output = runExport(SrmMockUtils.buildDsdWithTimeDimensionAndTextAttribute(TIME_DIMENSION_ID, ATTRIBUTE_ID));
        String headerLine = splitLines(output)[0];

        // Verify TIPO_INSTANCIA column is present between ID_ATRIBUTO and DIMENSIONES
        assertTrue(headerLine.contains("TIPO_INSTANCIA"));
        String[] columns = headerLine.split("\t");
        assertEquals("ID_ATRIBUTO", columns[0]);
        assertEquals("TIPO_INSTANCIA", columns[1]);
        assertEquals("DIMENSIONES", columns[2]);
        assertEquals("VALORES_DIMENSION", columns[3]);
        assertEquals("VALOR_ATRIBUTO#es", columns[4]);
    }

    // ─── VALUE instances ─────────────────────────────────────────────────────────

    @Test
    public void testExport_valueInstance_writesValueRow() throws Exception {
        AttributeInstanceDto valueInstance = buildValueInstance(ATTRIBUTE_ID, TIME_DIMENSION_ID, Arrays.asList("2010"), "test-value");
        mockFacade(Arrays.asList(valueInstance), Collections.<GranularityAttributeInstanceDto>emptyList());

        String output = runExport(SrmMockUtils.buildDsdWithTimeDimensionAndTextAttribute(TIME_DIMENSION_ID, ATTRIBUTE_ID));
        String[] lines = splitLines(output);

        // Header + 1 data row
        assertEquals(2, lines.length);
        String[] cols = lines[1].split("\t");
        assertEquals(ATTRIBUTE_ID, cols[0]);
        assertEquals("VALUE", cols[1]);
        assertEquals(TIME_DIMENSION_ID, cols[2]);
        assertEquals("2010", cols[3]);
        assertEquals("test-value", cols[4]);
    }

    @Test
    public void testExport_valueInstance_multipleCodes() throws Exception {
        AttributeInstanceDto valueInstance = buildValueInstance(ATTRIBUTE_ID, TIME_DIMENSION_ID, Arrays.asList("2010", "2011"), "my-value");
        mockFacade(Arrays.asList(valueInstance), Collections.<GranularityAttributeInstanceDto>emptyList());

        String output = runExport(SrmMockUtils.buildDsdWithTimeDimensionAndTextAttribute(TIME_DIMENSION_ID, ATTRIBUTE_ID));
        String[] cols = splitLines(output)[1].split("\t");

        assertEquals("2010, 2011", cols[3]);
    }

    // ─── GRANULARITY instances ────────────────────────────────────────────────────

    @Test
    public void testExport_granularityInstance_writesGranularityRow() throws Exception {
        GranularityAttributeInstanceDto granInstance = buildGranularityInstance(ATTRIBUTE_ID, TIME_DIMENSION_ID, Arrays.asList("A", "Q"), "gran-value");
        mockFacade(Collections.<AttributeInstanceDto>emptyList(), Arrays.asList(granInstance));

        String output = runExport(SrmMockUtils.buildDsdWithTimeDimensionAndTextAttribute(TIME_DIMENSION_ID, ATTRIBUTE_ID));
        String[] lines = splitLines(output);

        // Header + 1 data row
        assertEquals(2, lines.length);
        String[] cols = lines[1].split("\t");
        assertEquals(ATTRIBUTE_ID, cols[0]);
        assertEquals("GRANULARITY", cols[1]);
        assertEquals(TIME_DIMENSION_ID, cols[2]);
        assertEquals("A, Q", cols[3]);
        assertEquals("gran-value", cols[4]);
    }

    @Test
    public void testExport_granularityInstance_singleCode() throws Exception {
        GranularityAttributeInstanceDto granInstance = buildGranularityInstance(ATTRIBUTE_ID, TIME_DIMENSION_ID, Arrays.asList("A"), "annual-value");
        mockFacade(Collections.<AttributeInstanceDto>emptyList(), Arrays.asList(granInstance));

        String output = runExport(SrmMockUtils.buildDsdWithTimeDimensionAndTextAttribute(TIME_DIMENSION_ID, ATTRIBUTE_ID));
        String[] cols = splitLines(output)[1].split("\t");

        assertEquals("GRANULARITY", cols[1]);
        assertEquals("A", cols[3]);
        assertEquals("annual-value", cols[4]);
    }

    @Test
    public void testExport_granularityInstance_multipleGranularityCodes() throws Exception {
        GranularityAttributeInstanceDto granInstance = buildGranularityInstance(ATTRIBUTE_ID, TIME_DIMENSION_ID, Arrays.asList("A", "Q", "M"), "multi-gran-value");
        mockFacade(Collections.<AttributeInstanceDto>emptyList(), Arrays.asList(granInstance));

        String output = runExport(SrmMockUtils.buildDsdWithTimeDimensionAndTextAttribute(TIME_DIMENSION_ID, ATTRIBUTE_ID));
        String[] cols = splitLines(output)[1].split("\t");

        assertEquals("A, Q, M", cols[3]);
    }

    // ─── Both instance types ─────────────────────────────────────────────────────

    @Test
    public void testExport_bothValueAndGranularityInstances_twoDataRows() throws Exception {
        AttributeInstanceDto valueInstance = buildValueInstance(ATTRIBUTE_ID, TIME_DIMENSION_ID, Arrays.asList("2010"), "value-for-period");
        GranularityAttributeInstanceDto granInstance = buildGranularityInstance(ATTRIBUTE_ID, TIME_DIMENSION_ID, Arrays.asList("A"), "value-for-granularity");
        mockFacade(Arrays.asList(valueInstance), Arrays.asList(granInstance));

        String output = runExport(SrmMockUtils.buildDsdWithTimeDimensionAndTextAttribute(TIME_DIMENSION_ID, ATTRIBUTE_ID));
        String[] lines = splitLines(output);

        // Header + 2 data rows
        assertEquals(3, lines.length);

        String[] valueCols = lines[1].split("\t");
        assertEquals("VALUE", valueCols[1]);
        assertEquals("2010", valueCols[3]);
        assertEquals("value-for-period", valueCols[4]);

        String[] granCols = lines[2].split("\t");
        assertEquals("GRANULARITY", granCols[1]);
        assertEquals("A", granCols[3]);
        assertEquals("value-for-granularity", granCols[4]);
    }

    // ─── Test helpers ─────────────────────────────────────────────────────────────

    private AttributeInstanceDto buildValueInstance(String attributeId, String dimensionId, List<String> codes, String value) {
        AttributeInstanceDto dto = new AttributeInstanceDto();
        dto.setAttributeId(attributeId);
        Map<String, List<String>> codesByDimension = new HashMap<>();
        codesByDimension.put(dimensionId, codes);
        dto.setCodesByDimension(codesByDimension);
        InternationalStringDto intStr = new InternationalStringDto();
        intStr.addText(new LocalisedStringDto("es", value));
        dto.setValue(intStr);
        return dto;
    }

    private GranularityAttributeInstanceDto buildGranularityInstance(String attributeId, String dimensionId, List<String> codes, String value) {
        GranularityAttributeInstanceDto dto = new GranularityAttributeInstanceDto();
        dto.setAttributeId(attributeId);
        Map<String, List<String>> granCodes = new HashMap<>();
        granCodes.put(dimensionId, codes);
        dto.setGranularityCodesByDimension(granCodes);
        InternationalStringDto intStr = new InternationalStringDto();
        intStr.addText(new LocalisedStringDto("es", value));
        dto.setValue(intStr);
        return dto;
    }

    @SuppressWarnings("unchecked")
    private void mockFacade(List<AttributeInstanceDto> valueInstances, List<GranularityAttributeInstanceDto> granularityInstances) throws Exception {
        Mockito.when(datasetRepositoriesServiceFacade.findAttributesInstancesWithDimensionAttachmentLevel(
                Mockito.eq(DATASET_REPO_ID), Mockito.eq(ATTRIBUTE_ID), Mockito.any(Map.class))).thenReturn(valueInstances);
        Mockito.when(datasetRepositoriesServiceFacade.findGranularityAttributesInstances(
                Mockito.eq(DATASET_REPO_ID), Mockito.eq(ATTRIBUTE_ID))).thenReturn(granularityInstances);
    }

    private String runExport(DataStructure dsd) throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        service.exportCreateBodyForAttributes(dsd, datasetVersion, os, LANGUAGES);
        return os.toString(StandardCharsets.UTF_8.name());
    }

    private String[] splitLines(String output) {
        // Normalize line endings and remove trailing empty lines
        String[] lines = output.replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n").trim().split("\n");
        List<String> nonEmpty = new ArrayList<>();
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                nonEmpty.add(line);
            }
        }
        return nonEmpty.toArray(new String[0]);
    }
}
