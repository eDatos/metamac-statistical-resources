package org.siemac.metamac.rest.structural_resources.v1_0.utils;

import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.QUERY_1_CODE;
import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.QUERY_2_CODE;
import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.QUERY_3_CODE;
import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.QUERY_4_CODE;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.statistical.resources.core.base.domain.IdentifiableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionRationaleType;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.common.domain.InternationalString;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResource;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceResult;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.dataset.domain.AttributeValue;
import org.siemac.metamac.statistical.resources.core.dataset.domain.CodeDimension;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Dataset;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.TemporalCode;
import org.siemac.metamac.statistical.resources.core.enume.domain.NextVersionTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.VersionRationaleTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryTypeEnum;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.CodeItem;
import org.siemac.metamac.statistical.resources.core.query.domain.QuerySelectionItem;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesDoMocks;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesPersistedDoMocks;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceDto;
import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceObservationDto;
import es.gobcan.istac.edatos.dataset.repository.dto.CodeDimensionDto;
import es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto;
import es.gobcan.istac.edatos.dataset.repository.dto.LocalisedStringDto;
import es.gobcan.istac.edatos.dataset.repository.dto.ObservationExtendedDto;

public class RestDoMocks {

    public StatisticalResourcesPersistedDoMocks coreDoMocks;

    public RestDoMocks(StatisticalResourcesPersistedDoMocks coreDoMocks) {
        this.coreDoMocks = coreDoMocks;
    }

    public DatasetVersion mockDatasetVersion(String agencyID, String resourceID, String version) {
        DatasetVersion target = mockDatasetVersionBasic(agencyID, resourceID, version);

        target.setDataset(new Dataset());
        target.getDataset().setIdentifiableStatisticalResource(new IdentifiableStatisticalResource());
        target.getDataset().getIdentifiableStatisticalResource().setCode(resourceID);
        target.getDataset().getIdentifiableStatisticalResource().setUrn("urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Dataset=" + agencyID + ":" + resourceID);

        target.setBibliographicCitation(new InternationalString("es", "bibliographicCitation. More info: #URI#"));
        target.setDatasetRepositoryId("datasetRepository01");

        target.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("santa-cruz-tenerife"));
        target.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("tenerife"));
        target.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("la-laguna"));
        target.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("santa-cruz"));
        target.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("la-palma"));
        target.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("santa-cruz-la-palma"));
        target.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("los-llanos-de-aridane"));
        target.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("la-gomera"));
        target.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("el-hierro"));
        target.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("las-palmas-gran-canaria"));
        target.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("gran-canaria"));
        target.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("fuerteventura"));
        target.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("lanzarote"));

        target.addTemporalCoverage(mockTemporalCode("2014"));
        target.addTemporalCoverage(mockTemporalCode("2013"));
        target.addTemporalCoverage(mockTemporalCode("2012"));
        target.addTemporalCoverage(mockTemporalCode("2011"));
        target.addMeasureCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("code-d2-1"));
        target.addMeasureCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("code-d2-2"));
        target.addGeographicGranularity(StatisticalResourcesDoMocks.mockCodeExternalItem("municipalities"));
        target.addGeographicGranularity(StatisticalResourcesDoMocks.mockCodeExternalItem("countries"));
        target.addTemporalGranularity(StatisticalResourcesDoMocks.mockCodeExternalItem("yearly"));
        target.addTemporalGranularity(StatisticalResourcesDoMocks.mockCodeExternalItem("monthly"));
        target.setDateStart(new DateTime(2013, 1, 2, 3, 4, 5, 0));
        target.setDateEnd(new DateTime(2013, 3, 5, 6, 7, 8, 0));
        target.addStatisticalUnit(StatisticalResourcesDoMocks.mockConceptExternalItem("statisticalUnit1"));
        target.addStatisticalUnit(StatisticalResourcesDoMocks.mockConceptExternalItem("statisticalUnit2"));
        target.addStatisticalUnit(StatisticalResourcesDoMocks.mockConceptExternalItem("statisticalUnit3"));
        target.setRelatedDsd(StatisticalResourcesDoMocks.mockDsdExternalItem("DSD01"));
        target.setFormatExtentObservations(Long.valueOf(468));
        target.setFormatExtentDimensions(Integer.valueOf(5));
        target.setDateNextUpdate(CoreCommonUtil.jodaDateTime2IsoDate(new DateTime(2013, 12, 2, 3, 4, 5, 0).toDate()));
        target.setUpdateFrequency(StatisticalResourcesDoMocks.mockCodeExternalItem("updateFrequency01"));
        target.setStatisticOfficiality(coreDoMocks.mockStatisticOfficiality("statisticOfficiality01"));

        ExternalItem maintainerCategorisation = StatisticalResourcesDoMocks.mockAgencyExternalItem(agencyID);
        target.addCategorisation(coreDoMocks.mockCategorisationWithGeneratedCategory(target, maintainerCategorisation, "category01", "cat_data_01"));
        target.addCategorisation(coreDoMocks.mockCategorisationWithGeneratedCategory(target, maintainerCategorisation, "category02", "cat_data_02"));

        mockSiemacMetadataStatisticalResource(agencyID, resourceID, version, target.getSiemacMetadataStatisticalResource());
        target.getSiemacMetadataStatisticalResource().setReplaces(mockDatasetVersionResource(agencyID, "replace01", "01.000"));
        return target;
    }

    public RelatedResource mockDatasetRelatedResource(String agencyID, String resourceID, String version) {
        DatasetVersion datasetVersion = mockDatasetVersionBasic(agencyID, resourceID, version);
        return StatisticalResourcesDoMocks.mockDatasetRelated(datasetVersion.getDataset());
    }

    public RelatedResource mockDatasetVersionResource(String agencyID, String resourceID, String version) {
        DatasetVersion datasetVersion = mockDatasetVersionBasic(agencyID, resourceID, version);
        return StatisticalResourcesDoMocks.mockDatasetVersionRelated(datasetVersion);
    }

    public RelatedResourceResult mockDatasetVersionRelatedResourceResult(String agencyID, String resourceID, String version) {
        RelatedResourceResult query = new RelatedResourceResult();
        query.setMaintainerNestedCode(agencyID);
        query.setCode(resourceID);
        query.setVersion(version);
        query.setType(TypeRelatedResourceEnum.DATASET_VERSION);
        query.setUrn(GeneratorUrnUtils.generateSiemacStatisticalResourceDatasetVersionUrn(new String[]{agencyID}, resourceID, version));
        query.setTitle(StatisticalResourcesDoMocks.mockInternationalStringAsMap("es", "título " + resourceID, "en", "title " + resourceID));
        return query;
    }

    public PublicationVersion mockPublicationVersion(String agencyID, String resourceID, String version) {
        PublicationVersion target = mockPublicationVersionBasic(agencyID, resourceID, version);
        target.setFormatExtentResources(Integer.valueOf(5));
        target.getSiemacMetadataStatisticalResource().setReplaces(mockPublicationRelatedResource(agencyID, "replace01", "01.000"));
        target.addHasPart(mockDatasetRelatedResource(agencyID, "hasPart01", "01.000"));
        target.addHasPart(mockDatasetRelatedResource(agencyID, "hasPart02", "01.000"));
        target.addHasPart(mockQueryRelatedResource(agencyID, "hasPart03", "01.000"));
        return target;
    }

    public RelatedResource mockPublicationRelatedResource(String agencyID, String resourceID, String version) {
        PublicationVersion publication = mockPublicationVersionBasic(agencyID, resourceID, version);
        return StatisticalResourcesDoMocks.mockPublicationVersionRelated(publication);
    }

    public RelatedResourceResult mockPublicationRelatedResourceResult(String agencyID, String resourceID, String version) {
        RelatedResourceResult query = new RelatedResourceResult();
        query.setMaintainerNestedCode(agencyID);
        query.setCode(resourceID);
        query.setVersion(version);
        query.setType(TypeRelatedResourceEnum.PUBLICATION_VERSION);
        query.setUrn("urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Collection=" + agencyID + ":" + resourceID + "(" + version + ")");
        query.setTitle(StatisticalResourcesDoMocks.mockInternationalStringAsMap("es", "título " + resourceID, "en", "title " + resourceID));
        return query;
    }

    public QueryVersion mockQueryVersion(String agencyID, String resourceID, String version) {
        QueryVersion target = mockQueryVersionBasic(agencyID, resourceID, version);
        mockLifeCycleStatisticalResource(agencyID, resourceID, version, target.getLifeCycleStatisticalResource());

        DatasetVersion mockDatasetVersion = mockDatasetVersion(agencyID, "dataset01", "01.000");
        Dataset mockDataset = mockDatasetVersion(agencyID, "dataset01", "01.000").getDataset();

        target.setFixedDatasetVersion(mockDatasetVersion);
        target.getSelection().clear();

        target.setStatus(QueryStatusEnum.ACTIVE);
        if (QUERY_1_CODE.equals(resourceID)) {
            mockQueryVersionFixed(target);
        } else if (QUERY_2_CODE.equals(resourceID)) {
            target.setDataset(mockDataset);
            target.setFixedDatasetVersion(null);
            mockQueryVersionAutoincremental(target);
        } else if (QUERY_3_CODE.equals(resourceID)) {
            mockQueryVersionLatestData(target);
        } else if (QUERY_4_CODE.equals(resourceID)) {
            mockQueryVersionFixedWithoutAllParents(target);
        } else {
            // any
            mockQueryVersionFixed(target);
        }

        return target;
    }

    public QueryVersion mockQueryVersionGlobalDataset(String agencyID, String resourceID, String version) {
        QueryVersion target = mockQueryVersion(agencyID, resourceID, version);
        target.setDataset(mockDatasetVersion(agencyID, "dataset01", "01.000").getDataset());
        target.setFixedDatasetVersion(null);
        return target;
    }

    private void mockQueryVersionFixed(QueryVersion target) {
        target.setType(QueryTypeEnum.FIXED);
        target.addSelection(mockQuerySelectionItem("GEO_DIM",
                Arrays.asList("santa-cruz-tenerife", "tenerife", "la-laguna", "santa-cruz", "la-palma", "los-llanos-de-aridane", "las-palmas-gran-canaria", "fuerteventura")));
        target.addSelection(mockQuerySelectionItem("measure01", Arrays.asList("measure01-conceptScheme01-concept01", "measure01-conceptScheme01-concept02", "measure01-conceptScheme01-concept05")));
        target.addSelection(mockQuerySelectionItem("dim01", Arrays.asList("dim01-codelist01-code01")));
        target.addSelection(mockQuerySelectionItem("TIME_PERIOD", Arrays.asList("2011", "2013")));
        target.setLatestDataNumber(null);
    }
    private void mockQueryVersionFixedWithoutAllParents(QueryVersion target) {
        target.setType(QueryTypeEnum.FIXED);
        target.addSelection(
                mockQuerySelectionItem("GEO_DIM", Arrays.asList("santa-cruz-tenerife", "tenerife", "la-laguna", "santa-cruz", "los-llanos-de-aridane", "las-palmas-gran-canaria", "fuerteventura")));
        target.addSelection(mockQuerySelectionItem("measure01", Arrays.asList("measure01-conceptScheme01-concept01", "measure01-conceptScheme01-concept02", "measure01-conceptScheme01-concept05")));
        target.addSelection(mockQuerySelectionItem("dim01", Arrays.asList("dim01-codelist01-code01")));
        target.addSelection(mockQuerySelectionItem("TIME_PERIOD", Arrays.asList("2011", "2013")));
        target.setLatestDataNumber(null);
    }
    private void mockQueryVersionAutoincremental(QueryVersion target) {
        target.setType(QueryTypeEnum.AUTOINCREMENTAL);
        target.addSelection(mockQuerySelectionItem("GEO_DIM", Arrays.asList("santa-cruz-tenerife", "las-palmas-gran-canaria")));
        target.addSelection(mockQuerySelectionItem("measure01", Arrays.asList("measure01-conceptScheme01-concept01", "measure01-conceptScheme01-concept02", "measure01-conceptScheme01-concept05")));
        target.addSelection(mockQuerySelectionItem("dim01", Arrays.asList("dim01-codelist01-code01")));
        target.addSelection(mockQuerySelectionItem("TIME_PERIOD", Arrays.asList("2011")));
        target.setLatestDataNumber(null);
    }
    private void mockQueryVersionLatestData(QueryVersion target) {
        target.setType(QueryTypeEnum.LATEST_DATA);
        target.addSelection(mockQuerySelectionItem("GEO_DIM", Arrays.asList("santa-cruz-tenerife", "las-palmas-gran-canaria")));
        target.addSelection(mockQuerySelectionItem("measure01", Arrays.asList("measure01-conceptScheme01-concept01", "measure01-conceptScheme01-concept02", "measure01-conceptScheme01-concept05")));
        target.addSelection(mockQuerySelectionItem("dim01", Arrays.asList("dim01-codelist01-code01")));
        target.addSelection(mockQuerySelectionItem("TIME_PERIOD", null));
        target.setLatestDataNumber(2);
    }

    public CodeDimension mockCodeDimension(String componentId, String id) {
        CodeDimension codeDimension = new CodeDimension();
        codeDimension.setDsdComponentId(componentId);
        codeDimension.setIdentifier(id);
        codeDimension.setTitle(codeDimension.getIdentifier() + " en Español");
        return codeDimension;
    }

    public AttributeValue mockAttributeValue(String componentId, String id) {
        AttributeValue attributeValue = new AttributeValue();
        attributeValue.setDsdComponentId(componentId);
        attributeValue.setIdentifier(id);
        return attributeValue;
    }

    public ObservationExtendedDto mockObservation(String valueGeoDimension, String valueTimePeriod, String valueMeasure01, String valueDimension01, int primaryMeasure) {
        ObservationExtendedDto observationExtendedDto = new ObservationExtendedDto();
        observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("GEO_DIM", valueGeoDimension));
        observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("TIME_PERIOD", valueTimePeriod));
        observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("measure01", valueMeasure01));
        observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("dim01", valueDimension01));
        observationExtendedDto.setPrimaryMeasure(String.valueOf(primaryMeasure));
        return observationExtendedDto;
    }

    public AttributeInstanceDto mockAttributeInstanceWithDatasetAttachmentLevel(String attributeId, String value) {
        AttributeInstanceDto attributeDto = new AttributeInstanceDto();
        attributeDto.setAttributeId(attributeId);
        attributeDto.setValue(mockInternationalStringAttributeDto(value));
        return attributeDto;
    }

    public AttributeInstanceDto mockAttributeInstanceWithDimensionAttachmentLevelDenormalized(String attributeId, String value, String dimension1, String codeDimension1, String dimension2,
            String codeDimension2, String dimension3, String codeDimension3, String dimension4, String codeDimension4) {
        AttributeInstanceDto attributeDto = new AttributeInstanceDto();
        attributeDto.setAttributeId(attributeId);
        attributeDto.setValue(mockInternationalStringAttributeDto(value));
        attributeDto.setCodesByDimension(new HashMap<String, List<String>>());
        if (dimension1 != null) {
            attributeDto.getCodesByDimension().put(dimension1, Arrays.asList(codeDimension1));
        }
        if (dimension2 != null) {
            attributeDto.getCodesByDimension().put(dimension2, Arrays.asList(codeDimension2));
        }
        if (dimension3 != null) {
            attributeDto.getCodesByDimension().put(dimension3, Arrays.asList(codeDimension3));
        }
        if (dimension4 != null) {
            attributeDto.getCodesByDimension().put(dimension4, Arrays.asList(codeDimension4));
        }
        return attributeDto;
    }

    public AttributeInstanceObservationDto mockAttributeInstanceObservation(String attributeId, String value) {
        AttributeInstanceObservationDto attributeDto = new AttributeInstanceObservationDto();
        attributeDto.setAttributeId(attributeId);
        attributeDto.setValue(mockInternationalStringAttributeDto(value));
        return attributeDto;
    }

    private DatasetVersion mockDatasetVersionBasic(String agencyID, String resourceID, String version) {
        DatasetVersion target = coreDoMocks.mockDatasetVersion();
        target.getSiemacMetadataStatisticalResource().setUrn("urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Dataset=" + agencyID + ":" + resourceID + "(" + version + ")");
        target.getSiemacMetadataStatisticalResource().getMaintainer().setCodeNested(agencyID);
        target.getSiemacMetadataStatisticalResource().setCode(resourceID);
        target.getSiemacMetadataStatisticalResource().setVersionLogic(version);
        target.getSiemacMetadataStatisticalResource().setTitle(StatisticalResourcesDoMocks.mockInternationalStringMetadata(resourceID, "title"));
        return target;
    }

    private PublicationVersion mockPublicationVersionBasic(String agencyID, String resourceID, String version) {
        PublicationVersion target = PublicationVersionMockFactory.createComplexStructure();
        target.getSiemacMetadataStatisticalResource().setUrn("urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Collection=" + agencyID + ":" + resourceID + "(" + version + ")");
        target.getSiemacMetadataStatisticalResource().getMaintainer().setCodeNested(agencyID);
        // This is needed because the file in resources has daylight saving and we need to force summer time
        target.getSiemacMetadataStatisticalResource().setResourceCreatedDate(new DateTime(2013, 06, 20, 20, 00, 20, 21));
        target.getSiemacMetadataStatisticalResource().setLastUpdate(new DateTime(2013, 06, 22, 20, 00, 20, 21));
        target.getSiemacMetadataStatisticalResource().setCode(resourceID);
        target.getSiemacMetadataStatisticalResource().setVersionLogic(version);
        target.getSiemacMetadataStatisticalResource().setTitle(StatisticalResourcesDoMocks.mockInternationalStringMetadata(resourceID, "title"));
        return target;
    }

    private QueryVersion mockQueryVersionBasic(String agencyID, String resourceID, String version) {
        QueryVersion target = coreDoMocks.mockQueryVersionWithGeneratedDatasetVersion();
        target.getLifeCycleStatisticalResource().setUrn("urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Query=" + agencyID + ":" + resourceID + "(" + version + ")");
        target.getLifeCycleStatisticalResource().getMaintainer().setCodeNested(agencyID);
        target.getLifeCycleStatisticalResource().setCode(resourceID);
        target.getLifeCycleStatisticalResource().setVersionLogic(version);
        target.getLifeCycleStatisticalResource().setTitle(StatisticalResourcesDoMocks.mockInternationalStringMetadata(resourceID, "title"));
        return target;
    }

    private void mockSiemacMetadataStatisticalResource(String agencyID, String resourceID, String version, SiemacMetadataStatisticalResource target) {
        target.addStatisticalOperationInstance(StatisticalResourcesDoMocks.mockStatisticalOperationInstanceExternalItem("instance01"));
        target.addStatisticalOperationInstance(StatisticalResourcesDoMocks.mockStatisticalOperationInstanceExternalItem("instance02"));
        target.setResourceCreatedDate(new DateTime(2012, 1, 2, 3, 4, 5, 0));
        target.setLastUpdate(new DateTime(2013, 1, 2, 3, 4, 5, 0));
        target.setNewnessUntilDate(new DateTime(2013, 9, 2, 15, 4, 5, 0));
        target.setCopyrightedDate(2014);
        target.setNextVersion(NextVersionTypeEnum.NO_UPDATES);
        mockLifeCycleStatisticalResource(agencyID, resourceID, version, target);
    }

    private void mockLifeCycleStatisticalResource(String agencyID, String resourceID, String version, LifeCycleStatisticalResource target) {
        target.setPublicationDate(new DateTime(2013, 1, 1, 3, 4, 13, 0));
        target.setReplacesVersion(mockDatasetRelatedResource(agencyID, resourceID, "00.001"));
        mockVersionableStatisticalResource(agencyID, resourceID, version, target);
    }

    private void mockVersionableStatisticalResource(String agencyID, String resourceID, String version, VersionableStatisticalResource target) {
        target.setVersionRationale(StatisticalResourcesDoMocks.mockInternationalStringMetadata(resourceID, "versionRationale"));
        target.addVersionRationaleType(new VersionRationaleType(VersionRationaleTypeEnum.MAJOR_CATEGORIES));
        target.addVersionRationaleType(new VersionRationaleType(VersionRationaleTypeEnum.MINOR_METADATA));
        target.setValidFrom(new DateTime(2013, 1, 1, 3, 4, 12, 0));
        target.setValidTo(new DateTime(2013, 2, 16, 14, 4, 12, 0));
    }

    private QuerySelectionItem mockQuerySelectionItem(String dimensionId, List<String> codes) {
        QuerySelectionItem querySelectionItem = new QuerySelectionItem();
        querySelectionItem.setDimension(dimensionId);
        if (!CollectionUtils.isEmpty(codes)) {
            for (String code : codes) {
                querySelectionItem.addCode(mockCodeItem(code));
            }
        }
        return querySelectionItem;
    }

    public RelatedResource mockQueryRelatedResource(String agencyID, String resourceID, String version) {
        QueryVersion queryVersion = mockQueryVersionBasic(agencyID, resourceID, version);
        return StatisticalResourcesDoMocks.mockQueryRelated(queryVersion.getQuery());
    }

    public RelatedResourceResult mockQueryRelatedResourceResult(String agencyID, String resourceID) {
        RelatedResourceResult query = new RelatedResourceResult();
        query.setMaintainerNestedCode(agencyID);
        query.setCode(resourceID);
        query.setType(TypeRelatedResourceEnum.QUERY);
        query.setUrn(GeneratorUrnUtils.generateSiemacStatisticalResourceQueryUrn(new String[]{agencyID}, resourceID));
        query.setTitle(StatisticalResourcesDoMocks.mockInternationalStringAsMap("es", "título " + resourceID, "en", "title " + resourceID));
        return query;
    }

    private CodeItem mockCodeItem(String codeId) {
        CodeItem codeItem = new CodeItem();
        codeItem.setCode(codeId);
        return codeItem;
    }

    private TemporalCode mockTemporalCode(String code) {
        return StatisticalResourcesDoMocks.mockTemporalCode(code, code);
    }

    private InternationalStringDto mockInternationalStringAttributeDto(String label) {
        InternationalStringDto internationalStringDto = new InternationalStringDto();
        internationalStringDto.addText(new LocalisedStringDto(StatisticalResourcesConstants.DEFAULT_DATA_REPOSITORY_LOCALE, label));
        return internationalStringDto;
    }

}