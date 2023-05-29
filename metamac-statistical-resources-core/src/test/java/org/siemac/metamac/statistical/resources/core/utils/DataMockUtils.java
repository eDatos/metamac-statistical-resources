package org.siemac.metamac.statistical.resources.core.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.Mockito;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AttributeRelationship;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Attributes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Concepts;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.TextFormat;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.dataset.domain.AttributeValue;
import org.siemac.metamac.statistical.resources.core.dataset.domain.CodeDimension;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.TemporalCode;
import org.siemac.metamac.statistical.resources.core.invocation.service.MetamacApisLocator;
import org.siemac.metamac.statistical.resources.core.invocation.service.SrmRestInternalService;

import es.gobcan.istac.edatos.dataset.repository.dto.DatasetRepositoryDto;
import es.gobcan.istac.edatos.dataset.repository.service.DatasetRepositoriesServiceFacade;

public class DataMockUtils {

    private static final String GEOCODELIST_TEST_URN = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=TEST:codelist-01(1.0)";
    private static final String CODE_TEST_URN_PREFIX = "urn:uuid:";

    public static void mockDsdAndDataRepositorySimpleDimensionsNoAttributes(DatasetRepositoriesServiceFacade datasetRepositoriesServiceFacade, SrmRestInternalService srmRestInternalService)
            throws Exception {
        Mockito.reset(datasetRepositoriesServiceFacade);
        Mockito.reset(srmRestInternalService);

        mockDataRepositorySimpleDimensionsNoAttributes(datasetRepositoriesServiceFacade);

        // Mock codelist and concept Scheme
        mockDsdAndRelatedWithNoAttributes(srmRestInternalService);
    }
    public static void mockDataRepositorySimpleDimensionsNoAttributes(DatasetRepositoriesServiceFacade datasetRepositoriesServiceFacade) throws Exception {
        Map<String, List<String>> coverageMap = new HashMap<String, List<String>>();
        coverageMap.put("GEO_DIM", Arrays.asList("code-01", "code-02", "code-03"));
        coverageMap.put("TIME_PERIOD", Arrays.asList("2010", "2011", "2012"));
        coverageMap.put("MEAS_DIM", Arrays.asList("concept-01", "concept-02", "concept-03"));

        Mockito.when(datasetRepositoriesServiceFacade.findCodeDimensions(Mockito.anyString())).thenReturn(coverageMap);

        DatasetRepositoryDto datasetRepoDto = DsRepositoryMockUtils.mockDatasetRepository("dsrepo-01", "GEO_DIM", "TIME_PERIOD", "MEAS_DIM");
        Mockito.when(datasetRepositoriesServiceFacade.retrieveDatasetRepository(Mockito.anyString())).thenReturn(datasetRepoDto);

        Mockito.when(datasetRepositoriesServiceFacade.countObservations(Mockito.anyString())).thenReturn(27L);
    }

    public static void mockDsdAndDataRepositorySimpleDimensionsWithObservationAttributes(DatasetRepositoriesServiceFacade datasetRepositoriesServiceFacade,
            SrmRestInternalService srmRestInternalService) throws Exception {
        Mockito.reset(datasetRepositoriesServiceFacade);
        Mockito.reset(srmRestInternalService);

        Map<String, List<String>> coverageMap = new HashMap<String, List<String>>();
        coverageMap.put("GEO_DIM", Arrays.asList("code-01", "code-02", "code-03"));
        coverageMap.put("TIME_PERIOD", Arrays.asList("2010", "2011", "2012"));
        coverageMap.put("MEAS_DIM", Arrays.asList("concept-01", "concept-02", "concept-03"));

        Mockito.when(datasetRepositoriesServiceFacade.findCodeDimensions(Mockito.anyString())).thenReturn(coverageMap);

        Map<String, List<String>> attrCoverageMap = new HashMap<String, List<String>>();
        attrCoverageMap.put("OBS_ATTR01", Arrays.asList("code-01", "code-02", "code-03"));
        attrCoverageMap.put("OBS_ATTR02", Arrays.asList("2010", "2011", "2012"));
        attrCoverageMap.put("OBS_ATTR03", Arrays.asList("concept-01", "concept-02", "concept-03"));

        Mockito.when(datasetRepositoriesServiceFacade.findAttributeInstancesValuesWithObservationAttachmentLevel(Mockito.anyString(), Mockito.anyString())).thenReturn(attrCoverageMap);

        DatasetRepositoryDto datasetRepoDto = DsRepositoryMockUtils.mockDatasetRepository("dsrepo-01", "GEO_DIM", "TIME_PERIOD", "MEAS_DIM");
        Mockito.when(datasetRepositoriesServiceFacade.retrieveDatasetRepository(Mockito.anyString())).thenReturn(datasetRepoDto);

        Mockito.when(datasetRepositoriesServiceFacade.countObservations(Mockito.anyString())).thenReturn(27L);

        // Mock codelist and concept Scheme
        mockDsdAndRelatedWithObservationAttributes(srmRestInternalService);
    }

    public static void mockDsdAndDataRepositoryEmpty(DatasetRepositoriesServiceFacade datasetRepositoriesServiceFacade, SrmRestInternalService srmRestInternalService) throws Exception {
        Mockito.reset(datasetRepositoriesServiceFacade);
        Mockito.reset(srmRestInternalService);

        Map<String, List<String>> coverageMap = new HashMap<String, List<String>>();
        coverageMap.put("GEO_DIM", new ArrayList<String>());
        coverageMap.put("TIME_PERIOD", new ArrayList<String>());
        coverageMap.put("MEAS_DIM", new ArrayList<String>());

        Mockito.when(datasetRepositoriesServiceFacade.findCodeDimensions(Mockito.anyString())).thenReturn(coverageMap);

        DatasetRepositoryDto datasetRepoDto = DsRepositoryMockUtils.mockDatasetRepository("dsrepo-01", "GEO_DIM", "TIME_PERIOD", "MEAS_DIM");
        Mockito.when(datasetRepositoriesServiceFacade.retrieveDatasetRepository(Mockito.anyString())).thenReturn(datasetRepoDto);

        Mockito.when(datasetRepositoriesServiceFacade.countObservations(Mockito.anyString())).thenReturn(0L);

        // Mock codelist and concept Scheme
        mockDsdAndRelatedWithNoAttributes(srmRestInternalService);
    }

    public static void mockDsdAPIAndRelatedWithNoAttributes(MetamacApisLocator apisLocator) throws Exception {
        ResourceInternal codelistReference = SrmMockUtils.buildCodelistRef(GEOCODELIST_TEST_URN);
        Codes codes = SrmMockUtils.buildCodes(3);
        Mockito.when(apisLocator.getSrmRestInternalFacadeV10().findCodes(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString())).thenReturn(codes);

        ResourceInternal conceptSchemeReference = SrmMockUtils.buildConceptSchemeRef("urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=TEST:cshm-01(1.0)");
        Concepts concepts = SrmMockUtils.buildConcepts(3);
        Mockito.when(apisLocator.getSrmRestInternalFacadeV10().findConcepts(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(concepts);

        // Create a datastructure with dimensions marked as measure temporal and spatial

        DataStructure dsd = SrmMockUtils.mockDsdWithGeoTimeAndMeasureDimensions("urn:sdmx:org.sdmx.infomodel.datastructure.DataStructure=TFFS:CRED_EXT_DEBT(1.0)", "GEO_DIM", "TIME_PERIOD", "MEAS_DIM",
                conceptSchemeReference, codelistReference);
        Mockito.when(apisLocator.getSrmRestInternalFacadeV10().retrieveDataStructure(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), (String) Mockito.isNull())).thenReturn(dsd);
    }

    public static void mockDsdAndRelatedWithNoAttributes(SrmRestInternalService srmRestInternalService) throws Exception {
        ResourceInternal codelistReference = SrmMockUtils.buildCodelistRef(GEOCODELIST_TEST_URN);
        Codes codes = SrmMockUtils.buildCodes(3);
        Mockito.when(srmRestInternalService.retrieveCodesOfCodelistEfficiently(codelistReference.getUrn())).thenReturn(codes);

        ResourceInternal conceptSchemeReference = SrmMockUtils.buildConceptSchemeRef("urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=TEST:cshm-01(1.0)");
        Concepts concepts = SrmMockUtils.buildConcepts(3);
        Mockito.when(srmRestInternalService.retrieveConceptsOfConceptSchemeEfficiently(conceptSchemeReference.getUrn())).thenReturn(concepts);

        // Create a datastructure with dimensions marked as measure temporal and spatial

        DataStructure dsd = SrmMockUtils.mockDsdWithGeoTimeAndMeasureDimensions("urn:sdmx:org.sdmx.infomodel.datastructure.DataStructure=TFFS:CRED_EXT_DEBT(1.0)", "GEO_DIM", "TIME_PERIOD", "MEAS_DIM",
                conceptSchemeReference, codelistReference);
        Mockito.when(srmRestInternalService.retrieveDsdByUrn(Mockito.anyString())).thenReturn(dsd);
    }

    public static void mockDsdAndRelatedWithObservationAttributes(SrmRestInternalService srmRestInternalService) throws Exception {
        ResourceInternal codelistReference = SrmMockUtils.buildCodelistRef(GEOCODELIST_TEST_URN);
        Codes codes = SrmMockUtils.buildCodes(3);
        Mockito.when(srmRestInternalService.retrieveCodesOfCodelistEfficiently(codelistReference.getUrn())).thenReturn(codes);

        ResourceInternal conceptSchemeReference = SrmMockUtils.buildConceptSchemeRef("urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=TEST:cshm-01(1.0)");
        Concepts concepts = SrmMockUtils.buildConcepts(3);
        Mockito.when(srmRestInternalService.retrieveConceptsOfConceptSchemeEfficiently(conceptSchemeReference.getUrn())).thenReturn(concepts);

        // Create a datastructure with dimensions marked as measure temporal and spatial

        DataStructure dsd = SrmMockUtils.mockDsdWithGeoTimeAndMeasureDimensions("urn:sdmx:org.sdmx.infomodel.datastructure.DataStructure=TFFS:CRED_EXT_DEBT(1.0)", "GEO_DIM", "TIME_PERIOD", "MEAS_DIM",
                conceptSchemeReference, codelistReference);

        Attributes attributes = new Attributes();
        attributes.getAttributes().add(SrmMockUtils.buildSpatialAttribute("OBS_ATTR01", codelistReference, buildObservationLevelRelationship()));
        attributes.getAttributes().add(SrmMockUtils.buildTimeAttribute("OBS_ATTR02", new TextFormat(), buildObservationLevelRelationship()));
        attributes.getAttributes().add(SrmMockUtils.buildMeasureAttribute("OBS_ATTR03", conceptSchemeReference, buildObservationLevelRelationship()));
        dsd.getDataStructureComponents().setAttributes(attributes);

        Mockito.when(srmRestInternalService.retrieveDsdByUrn(Mockito.anyString())).thenReturn(dsd);
    }

    private static AttributeRelationship buildObservationLevelRelationship() {
        AttributeRelationship attributeRelationship = new AttributeRelationship();
        attributeRelationship.setPrimaryMeasure("value");
        return attributeRelationship;
    }

    public static void mockDsdAndCreateDatasetRepository(DatasetRepositoriesServiceFacade datasetRepositoriesServiceFacade, SrmRestInternalService srmRestInternalService, String datasetRepositoryId)
            throws Exception {
        mockDsdAndRelatedWithNoAttributes(srmRestInternalService);
        DatasetRepositoryDto datasetRepositoryDto = new DatasetRepositoryDto();
        datasetRepositoryDto.setDatasetId(datasetRepositoryId);
        Mockito.when(datasetRepositoriesServiceFacade.createDatasetRepository(Mockito.any(DatasetRepositoryDto.class))).thenReturn(datasetRepositoryDto);
    }

    public static void fillDatasetVersionWithCalculatedMetadataFromData(DatasetVersion datasetVersion) {
        datasetVersion.addGeographicCoverage(buildExternalItem("code-01", TypeExternalArtefactsEnum.CODE));
        datasetVersion.addGeographicCoverage(buildExternalItem("code-02", TypeExternalArtefactsEnum.CODE));
        datasetVersion.addGeographicCoverage(buildExternalItem("code-03", TypeExternalArtefactsEnum.CODE));

        datasetVersion.addTemporalCoverage(buildTemporalCode("2010"));
        datasetVersion.addTemporalCoverage(buildTemporalCode("2011"));
        datasetVersion.addTemporalCoverage(buildTemporalCode("2012"));

        datasetVersion.addMeasureCoverage(buildExternalItem("concept-01", TypeExternalArtefactsEnum.CONCEPT));
        datasetVersion.addMeasureCoverage(buildExternalItem("concept-02", TypeExternalArtefactsEnum.CONCEPT));
        datasetVersion.addMeasureCoverage(buildExternalItem("concept-03", TypeExternalArtefactsEnum.CONCEPT));

        datasetVersion.addDimensionsCoverage(new CodeDimension("MEAS_DIM", "concept-01", "Concept 01"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("MEAS_DIM", "concept-02", "Concept 02"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("MEAS_DIM", "concept-03", "Concept 03"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("TIME_PERIOD", "2012"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("TIME_PERIOD", "2011"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("TIME_PERIOD", "2010"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("GEO_DIM", "code-01", "Code 01"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("GEO_DIM", "code-02", "Code 02"));
        datasetVersion.addDimensionsCoverage(new CodeDimension("GEO_DIM", "code-03", "Code 03"));

        datasetVersion.setFormatExtentDimensions(3);
        datasetVersion.setFormatExtentObservations(27L);

        datasetVersion.setDateStart("2010");
        datasetVersion.setDateEnd("2012");
    }

    public static void fillDatasetVersionWithCalculatedMetadataFromDataWithObservationAttributes(DatasetVersion datasetVersion) {

        fillDatasetVersionWithCalculatedMetadataFromData(datasetVersion);

        datasetVersion.addAttributesCoverage(new AttributeValue("OBS_ATTR01", "code-01", "Code 01"));
        datasetVersion.addAttributesCoverage(new AttributeValue("OBS_ATTR01", "code-02", "Code 02"));
        datasetVersion.addAttributesCoverage(new AttributeValue("OBS_ATTR01", "code-03", "Code 03"));
        datasetVersion.addAttributesCoverage(new AttributeValue("OBS_ATTR02", "2010"));
        datasetVersion.addAttributesCoverage(new AttributeValue("OBS_ATTR02", "2011"));
        datasetVersion.addAttributesCoverage(new AttributeValue("OBS_ATTR02", "2012"));
        datasetVersion.addAttributesCoverage(new AttributeValue("OBS_ATTR03", "concept-01", "Concept 01"));
        datasetVersion.addAttributesCoverage(new AttributeValue("OBS_ATTR03", "concept-02", "Concept 02"));
        datasetVersion.addAttributesCoverage(new AttributeValue("OBS_ATTR03", "concept-03", "Concept 03"));

    }

    public static void fillDatasetVersionWithEmptyCalculatedMetadataFromData(DatasetVersion datasetVersion) {
        datasetVersion.getGeographicCoverage().clear();
        datasetVersion.getTemporalCoverage().clear();
        datasetVersion.getMeasureCoverage().clear();
        datasetVersion.getDimensionsCoverage().clear();

        datasetVersion.setFormatExtentDimensions(3);
        datasetVersion.setFormatExtentObservations(0L);

        datasetVersion.setDateStart(null);
        datasetVersion.setDateEnd(null);
    }

    private static TemporalCode buildTemporalCode(String identifier) {
        TemporalCode code = new TemporalCode();
        code.setIdentifier(identifier);
        code.setTitle(identifier);
        return code;
    }

    private static ExternalItem buildExternalItem(String code, TypeExternalArtefactsEnum type) {
        ExternalItem item = new ExternalItem();
        item.setCode(code);
        item.setUrn(CODE_TEST_URN_PREFIX + code);
        item.setType(type);
        return item;
    }

    /*
     * protected static SrmRestInternalService getSrmRestInternalService() {
     * if (srmRestInternalService == null) {
     * srmRestInternalService = ApplicationContextProvider.getApplicationContext().getBean(SrmRestInternalService.class);
     * }
     * return srmRestInternalService;
     * }
     * private static DatasetRepositoriesServiceFacade getDatasetRepositoriesServiceFacade() {
     * if (datasetRepositoriesServiceFacade == null) {
     * datasetRepositoriesServiceFacade = ApplicationContextProvider.getApplicationContext().getBean(DatasetRepositoriesServiceFacade.class);
     * }
     * return datasetRepositoriesServiceFacade;
     * }
     * public static void setSrmRestInternalService(SrmRestInternalService srmRestInternalService) {
     * DataMockUtils.srmRestInternalService = srmRestInternalService;
     * }
     * public static void setDatasetRepositoriesServiceFacade(DatasetRepositoriesServiceFacade datasetRepositoriesServiceFacade) {
     * DataMockUtils.datasetRepositoriesServiceFacade = datasetRepositoriesServiceFacade;
     * }
     */
}
