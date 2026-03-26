package org.siemac.metamac.statistical.resources.core.stream.messages.mappers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.base.domain.IdentifiableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.NameableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionRationaleType;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.common.domain.InternationalString;
import org.siemac.metamac.statistical.resources.core.common.domain.LocalisedString;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResource;
import org.siemac.metamac.statistical.resources.core.dataset.domain.AttributeValue;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Categorisation;
import org.siemac.metamac.statistical.resources.core.dataset.domain.CodeDimension;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Dataset;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Datasource;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DimensionRepresentationMapping;
import org.siemac.metamac.statistical.resources.core.dataset.domain.StatisticOfficiality;
import org.siemac.metamac.statistical.resources.core.dataset.domain.TemporalCode;
import org.siemac.metamac.statistical.resources.core.dataset.utils.DatasetVersionUtils;
import org.siemac.metamac.statistical.resources.core.enume.domain.NextVersionTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.VersionRationaleTypeEnum;
import org.siemac.metamac.statistical.resources.core.publication.domain.Publication;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.stream.messages.AttributeValueAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.CategorisationAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.CodeDimensionAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetVersionAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.DimensionRepresentationMappingAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.ExternalItemAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.IdentifiableStatisticalResourceAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.InternationalStringAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.InternationalStringItemAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.LifecycleStatisticalResourceAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.NameableStatisticalResourceAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.PublicationAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.PublicationVersionAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.RelatedResourceAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.SiemacMetadataStatisticalResourceAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.StatisticOfficialityAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.TemporalCodeAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.TypeExternalArtefactsEnumAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.VersionRationaleTypeEnumAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.VersionableStatisticalResourceAvro;

public class MappersMockUtils {

    private static final boolean                         EXPECTED_TRUE                       = true;
    protected static final boolean                       EXPECTED_FALSE                      = false;
    protected static final String                        EXPECTED_TITLE                      = "EXPECTED_TITLE";
    protected static final String                        EXPECTED_FILENAME                   = "EXPECTED_FILENAME";
    protected static final String                        EXPECTED_IDENTIFIER                 = "EXPECTED_IDENTIFIER";
    protected static final TypeRelatedResourceEnum       EXPECTED_RELATED_RESOURCE_TYPE      = TypeRelatedResourceEnum.DATASET;
    protected static final ProcStatusEnum                PRODUCTION_VALIDATION               = ProcStatusEnum.PRODUCTION_VALIDATION;
    protected static final boolean                       EXPECTED_LAST_VERSION               = true;
    protected static final String                        EXPECTED_USER                       = "Expected User Name";
    protected static final NextVersionTypeEnum           EXPECTED_NEXT_VERSION_TYPE          = NextVersionTypeEnum.NON_SCHEDULED_UPDATE;
    protected static final DateTime                      EXPECTED_PAST_DATE                  = new DateTime(2016, 06, 01, 0, 0, 0, 0).minusDays(30);
    protected static final DateTime                      EXPECTED_FUTURE_DATE                = new DateTime(2016, 06, 01, 0, 0, 0, 0).plusDays(30);
    protected static final String                        EXPECTED_PAST_DATE_SDMX             = "2016-M05";
    protected static final String                        EXPECTED_FUTURE_DATE_SDMX           = "2016-M07";
    protected static final String                        EXPECTED_VERSION_LOGIC              = "EXPECTED_VERSION_LOGIC";
    protected static final long                          EXPECTED_VERSION                    = 33l;
    protected static final String                        EXPECTED_URI                        = "/EXPECTED_URI";
    protected static final String                        EXPECTED_URN                        = "EXPECTED_URN";
    protected static final String                        EXPECTED_URN_PROVIDER               = "EXPECTED_URN_PROVIDER";
    protected static final TypeExternalArtefactsEnum     EXPECTED_TYPE                       = TypeExternalArtefactsEnum.CONFIGURATION;
    protected static final TypeExternalArtefactsEnumAvro EXPECTED_TYPE_AVRO                  = TypeExternalArtefactsEnumAvro.CONFIGURATION;
    protected static final String                        EXPECTED_LOCALE                     = "EXPECTED_LOCALE";
    protected static final String                        EXPECTED_LABEL                      = "EXPECTED_LABEL";
    protected static final String                        EXPECTED_MANAGEMENT_APP_URL         = "EXPECTED_MANAGEMENT_APP_URL";
    protected static final String                        EXPECTED_CODE_NESTED                = "EXPECTED_CODE_NESTED";
    protected static final String                        EXPECTED_CODE                       = "EXPECTED_CODE";
    public static final String                           EXPECTED_API_BASE                   = "http://EXPECTED_API_BASE";
    protected static final String                        EXPECTED_SELF_LINK                  = EXPECTED_API_BASE + EXPECTED_URI;
    protected static final Long                          EXPECTED_FORMAT_EXTENT_OBSERVATIONS = 1000L;
    private static final int                             EXPECTED_COPYRIGHT                  = 0;
    public static final String                           SRM_EXTERNAL_API_URL_BASE                    = "//apis.metamac.org/srm-external" + EXPECTED_URI;
    public static final String                           STATISTICAL_OPERATIONS_EXTERNAL_API_URL_BASE = "//apis.metamac.org/statistical-operations-external" + EXPECTED_URI;
    public static final String                           COMMON_METADATA_EXTERNAL_API_URL_BASE        = "//apis.metamac.org/common-metadata-external" + EXPECTED_URI;

    public static ExternalItem mockExternalItem() {
        ExternalItem source = new ExternalItem();
        source.setCode(EXPECTED_CODE);
        source.setCodeNested(EXPECTED_CODE_NESTED);
        source.setManagementAppUrl(EXPECTED_MANAGEMENT_APP_URL);

        InternationalString is = mockInternationalString();
        source.setTitle(is);

        source.setType(EXPECTED_TYPE);
        source.setUrn(EXPECTED_URN);
        source.setVersion(EXPECTED_VERSION);
        source.setUrnProvider(EXPECTED_URN_PROVIDER);
        source.setUri(EXPECTED_URI);

        return source;
    }

    public static ExternalItemAvro mockExternalItemAvro() {
        // @formatter:off
        ExternalItemAvro expected = ExternalItemAvro.newBuilder().setCode(MappersMockUtils.EXPECTED_CODE).setCodeNested(MappersMockUtils.EXPECTED_CODE_NESTED)
                .setManagementAppUrl(MappersMockUtils.EXPECTED_MANAGEMENT_APP_URL)
                .setTitle(InternationalStringDo2AvroMapper.do2Avro(MappersMockUtils.mockInternationalString()))
                .setType(EXPECTED_TYPE_AVRO)
                .setUrn(MappersMockUtils.EXPECTED_URN)
                .setUrnProvider(MappersMockUtils.EXPECTED_URN_PROVIDER)
                .setSelfLink(EXPECTED_SELF_LINK)
                .build();
        // @formatter:on
        return expected;
    }

    public static ExternalItemAvro mockExternalItemStructuralResourcesAvro() {
        // @formatter:off
        ExternalItemAvro expected = ExternalItemAvro.newBuilder().setCode(MappersMockUtils.EXPECTED_CODE).setCodeNested(MappersMockUtils.EXPECTED_CODE_NESTED)
                .setManagementAppUrl(MappersMockUtils.EXPECTED_MANAGEMENT_APP_URL)
                .setTitle(InternationalStringDo2AvroMapper.do2Avro(MappersMockUtils.mockInternationalString()))
                .setType(EXPECTED_TYPE_AVRO)
                .setUrn(MappersMockUtils.EXPECTED_URN)
                .setUrnProvider(MappersMockUtils.EXPECTED_URN_PROVIDER)
                .setSelfLink(SRM_EXTERNAL_API_URL_BASE)
                .build();
        // @formatter:on
        return expected;
    }

    public static ExternalItemAvro mockExternalItemOperationsAvro() {
        // @formatter:off
        ExternalItemAvro expected = ExternalItemAvro.newBuilder().setCode(MappersMockUtils.EXPECTED_CODE).setCodeNested(MappersMockUtils.EXPECTED_CODE_NESTED)
                .setManagementAppUrl(MappersMockUtils.EXPECTED_MANAGEMENT_APP_URL)
                .setTitle(InternationalStringDo2AvroMapper.do2Avro(MappersMockUtils.mockInternationalString()))
                .setType(EXPECTED_TYPE_AVRO)
                .setUrn(MappersMockUtils.EXPECTED_URN)
                .setUrnProvider(MappersMockUtils.EXPECTED_URN_PROVIDER)
                .setSelfLink(STATISTICAL_OPERATIONS_EXTERNAL_API_URL_BASE)
                .build();
        // @formatter:on
        return expected;
    }

    public static ExternalItemAvro mockExternalItemCommonMetadataAvro() {
        // @formatter:off
        ExternalItemAvro expected = ExternalItemAvro.newBuilder().setCode(MappersMockUtils.EXPECTED_CODE).setCodeNested(MappersMockUtils.EXPECTED_CODE_NESTED)
                .setManagementAppUrl(MappersMockUtils.EXPECTED_MANAGEMENT_APP_URL)
                .setTitle(InternationalStringDo2AvroMapper.do2Avro(MappersMockUtils.mockInternationalString()))
                .setType(EXPECTED_TYPE_AVRO)
                .setUrn(MappersMockUtils.EXPECTED_URN)
                .setUrnProvider(MappersMockUtils.EXPECTED_URN_PROVIDER)
                .setSelfLink(COMMON_METADATA_EXTERNAL_API_URL_BASE)
                .build();
        // @formatter:on
        return expected;
    }

    public static InternationalString mockInternationalString() {
        LocalisedString ls = new LocalisedString();
        ls.setLabel(EXPECTED_LABEL);
        ls.setLocale(EXPECTED_LOCALE);
        InternationalString is = new InternationalString();
        is.addText(ls);
        return is;
    }

    private static InternationalStringAvro mockInternationalStringAvro() {
        List<InternationalStringItemAvro> list = new ArrayList<>();
        InternationalStringItemAvro item = InternationalStringItemAvro.newBuilder().setLabel(EXPECTED_LABEL).setLocale(EXPECTED_LOCALE).build();
        list.add(item);
        InternationalStringAvro target = InternationalStringAvro.newBuilder().setLocalisedStrings(list).build();
        return target;
    }

    public static InternationalStringAvro mockInternationalStringAvroFutureDate() throws MetamacException {
        List<InternationalStringItemAvro> list = new ArrayList<>();
        InternationalStringItemAvro item = InternationalStringItemAvro.newBuilder().setLabel(EXPECTED_FUTURE_DATE_SDMX).setLocale(EXPECTED_LOCALE).build();
        list.add(item);
        InternationalStringAvro target = InternationalStringAvro.newBuilder().setLocalisedStrings(list).build();
        return target;
    }

    public static RelatedResource mockRelatedResource(DatasetVersion datasetVersion) {
        RelatedResource target = new RelatedResource();
        target.setVersion(EXPECTED_VERSION);
        target.setType(TypeRelatedResourceEnum.DATASET_VERSION);
        target.setDatasetVersion(datasetVersion);
        return target;
    }

    public static RelatedResource mockRelatedResource(TypeRelatedResourceEnum type) {
        RelatedResource target = new RelatedResource();
        target.setVersion(EXPECTED_VERSION);
        target.setType(type);
        switch (type) {
            case DATASET:
                Dataset dataset = mockDataset();
                target.setDataset(dataset);
                break;
            case DATASET_VERSION:
                DatasetVersion datasetVersion = mockDatasetVersion();
                target.setDatasetVersion(datasetVersion);
            case PUBLICATION_VERSION:
                PublicationVersion publicationVersion = mockIsolatedPublicationVersion();
                target.setPublicationVersion(publicationVersion);
                break;
            default:
                break;
        }
        return target;
    }

    public static RelatedResourceAvro mockRelatedResourceAvro(TypeRelatedResourceEnum type) {
        // @formatter:off
        RelatedResourceAvro target = RelatedResourceAvro.newBuilder()
                .setCode(EXPECTED_CODE)
                .setStatisticalOperationUrn(EXPECTED_URN)
                .setTitle(InternationalStringDo2AvroMapper.do2Avro(mockInternationalString()))
                .setType(TypeRelatedResourceEnumDo2AvroMapper.do2Avro(type))
                .setUrn(EXPECTED_URN)
                .build();
        // @formatter:on
        return target;
    }

    public static NameableStatisticalResource mockNameableStatisticalResource() {
        NameableStatisticalResource target = new NameableStatisticalResource();
        target.setCode(EXPECTED_CODE);
        target.setStatisticalOperation(mockExternalItem());
        target.setTitle(mockInternationalString());
        target.setDescription(mockInternationalString());
        target.setUrn(EXPECTED_URN);
        return target;
    }

    public static NameableStatisticalResourceAvro mockNameableStatisticalResourceAvro() {
        // @formatter:off
        NameableStatisticalResourceAvro target = NameableStatisticalResourceAvro.newBuilder()
                .setIdentifiableStatisticalResource(mockIdentifiableStatisticalResourceAvro())
                .setDescription(InternationalStringDo2AvroMapper.do2Avro(mockInternationalString()))
                .setTitle(InternationalStringDo2AvroMapper.do2Avro(mockInternationalString()))
                .build();
        // @formatter:on
        return target;
    }

    public static IdentifiableStatisticalResourceAvro mockIdentifiableStatisticalResourceAvro() {
        IdentifiableStatisticalResourceAvro target = IdentifiableStatisticalResourceAvro.newBuilder().setCode(EXPECTED_CODE).setUrn(EXPECTED_URN).setStatisticalOperation(mockExternalItemOperationsAvro())
                .build();
        return target;
    }

    public static IdentifiableStatisticalResource mockIdentifiableStatisticalResource() {
        IdentifiableStatisticalResource target = new IdentifiableStatisticalResource();
        target.setCode(EXPECTED_CODE);
        target.setUrn(EXPECTED_URN);
        target.setStatisticalOperation(mockExternalItem());
        return target;
    }

    public static VersionableStatisticalResourceAvro mockVersionableStatisticalResourceAvro() throws MetamacException {
        // @formatter:off
        VersionableStatisticalResourceAvro target = VersionableStatisticalResourceAvro.newBuilder()
                .setNameableStatisticalResource(mockNameableStatisticalResourceAvro())
                .setNextVersion(NextVersionTypeEnumDo2AvroMapper.do2Avro(EXPECTED_NEXT_VERSION_TYPE))
                .setNextVersionDate(mockInternationalStringAvroFutureDate())
                .setValidFrom(DateTimeDo2AvroMapper.do2Avro(EXPECTED_PAST_DATE))
                .setVersionRationale(InternationalStringDo2AvroMapper.do2Avro(mockInternationalString()))
                .setValidTo(DateTimeDo2AvroMapper.do2Avro(EXPECTED_FUTURE_DATE))
                .setVersionLogic(EXPECTED_VERSION_LOGIC)
                .setVersionRationaleTypes(new ArrayList<VersionRationaleTypeEnumAvro>() {
                    {
                        add(VersionRationaleTypeEnumAvro.MAJOR_CATEGORIES);
                    }
                }).build();
        // @formatter:on
        return target;
    }

    public static VersionableStatisticalResource mockVersionableStatisticalResource() {
        VersionableStatisticalResource target = new VersionableStatisticalResource();
        target.setCode(EXPECTED_CODE);
        target.setStatisticalOperation(mockExternalItem());
        target.setTitle(mockInternationalString());
        target.setDescription(mockInternationalString());
        target.setUrn(EXPECTED_URN);
        target.setNextVersionDate(EXPECTED_FUTURE_DATE_SDMX);
        target.setValidFrom(EXPECTED_PAST_DATE);
        target.setValidTo(EXPECTED_FUTURE_DATE);
        target.setVersionRationale(mockInternationalString());
        target.setNextVersion(EXPECTED_NEXT_VERSION_TYPE);
        target.setVersionLogic(EXPECTED_VERSION_LOGIC);
        target.addVersionRationaleType(new VersionRationaleType(VersionRationaleTypeEnum.MAJOR_CATEGORIES));
        return target;
    }

    public static LifeCycleStatisticalResource mockLifeCycleStatisticalResource(TypeRelatedResourceEnum type) {
        LifeCycleStatisticalResource target = new LifeCycleStatisticalResource();
        target.setCode(EXPECTED_CODE);
        target.setStatisticalOperation(mockExternalItem());
        target.setTitle(mockInternationalString());
        target.setDescription(mockInternationalString());
        target.setUrn(EXPECTED_URN);
        target.setNextVersionDate(EXPECTED_FUTURE_DATE_SDMX);
        target.setValidFrom(EXPECTED_PAST_DATE);
        target.setValidTo(EXPECTED_FUTURE_DATE);
        target.setVersionRationale(mockInternationalString());
        target.setNextVersion(EXPECTED_NEXT_VERSION_TYPE);
        target.setVersionLogic(EXPECTED_VERSION_LOGIC);

        target.setCreationDate(EXPECTED_PAST_DATE);
        target.setCreationUser(EXPECTED_USER + "Creation");
        target.setProductionValidationDate(EXPECTED_PAST_DATE);
        target.setProductionValidationUser(EXPECTED_USER + "ProductionValidation");
        target.setDiffusionValidationDate(EXPECTED_PAST_DATE);
        target.setDiffusionValidationUser(EXPECTED_USER + "DiffusionValidation");
        target.setRejectValidationDate(EXPECTED_PAST_DATE);
        target.setRejectValidationUser(EXPECTED_USER + "RejectValidation");
        target.setPublicationDate(EXPECTED_PAST_DATE);
        target.setPublicationUser(EXPECTED_USER + "Publication");

        target.setLastVersion(EXPECTED_LAST_VERSION);
        target.setProcStatus(PRODUCTION_VALIDATION);
        target.setReplacesVersion(mockRelatedResource(type));
        target.setIsReplacedByVersion(mockRelatedResource(type));
        target.setMaintainer(mockExternalItem());
        target.addVersionRationaleType(new VersionRationaleType(VersionRationaleTypeEnum.MAJOR_CATEGORIES));
        target.setNewnessUntilDate(EXPECTED_PAST_DATE);
        target.setFeaturedUntilDate(EXPECTED_PAST_DATE);
        return target;
    }

    public static LifecycleStatisticalResourceAvro mockLifeCycleStatisticalResourceAvro(TypeRelatedResourceEnum replacesVersionType) throws MetamacException {
        // @formatter:off
        LifecycleStatisticalResourceAvro target = LifecycleStatisticalResourceAvro.newBuilder()
                .setVersionableStatisticalResource(mockVersionableStatisticalResourceAvro())
                .setCreationDate(DateTimeDo2AvroMapper.do2Avro(EXPECTED_PAST_DATE))
                .setCreationUser(EXPECTED_USER + "Creation")
                .setProductionValidationDate(DateTimeDo2AvroMapper.do2Avro(EXPECTED_PAST_DATE))
                .setProductionValidationUser(EXPECTED_USER + "ProductionValidation")
                .setDiffusionValidationDate(DateTimeDo2AvroMapper.do2Avro(EXPECTED_PAST_DATE))
                .setDiffusionValidationUser(EXPECTED_USER + "DiffusionValidation")
                .setRejectValidationDate(DateTimeDo2AvroMapper.do2Avro(EXPECTED_PAST_DATE))
                .setRejectValidationUser(EXPECTED_USER + "RejectValidation")
                .setPublicationDate(DateTimeDo2AvroMapper.do2Avro(EXPECTED_PAST_DATE))
                .setPublicationUser(EXPECTED_USER + "Publication")
                .setLastVersion(EXPECTED_LAST_VERSION)
                .setProcStatus(ProcStatusEnumDo2AvroMapper.do2Avro(PRODUCTION_VALIDATION))
                .setReplacesVersion(mockRelatedResourceAvro(replacesVersionType))
                .setIsReplacedByVersion(mockRelatedResourceAvro(replacesVersionType))
                .setMaintainer(mockExternalItemStructuralResourcesAvro())
                .setNewnessUntilDate(DateTimeDo2AvroMapper.do2Avro(EXPECTED_PAST_DATE))
                .setFeaturedUntilDate(DateTimeDo2AvroMapper.do2Avro(EXPECTED_PAST_DATE))
                .build();
        // @formatter:on
        return target;
    }

    public static Dataset mockDataset() {
        return mockDataset(null);
    }

    public static Dataset mockDataset(DatasetVersion datasetVersion) {
        Dataset dataset = new Dataset();
        dataset.setIdentifiableStatisticalResource(mockNameableStatisticalResource());
        dataset.getIdentifiableStatisticalResource().setCode(EXPECTED_CODE);
        dataset.setViewCode(DatasetVersionUtils.generateViewCode(EXPECTED_CODE));
        dataset.setVersion(EXPECTED_VERSION);
        dataset.addDimensionRepresentationMapping(mockDimensionRepresentationMapping(false));
        if (datasetVersion != null) {
            dataset.addVersion(datasetVersion);
        } else {
            dataset.addVersion(mockDatasetVersion());
            dataset.addVersion(mockDatasetVersion(EXPECTED_URN + "2"));
            dataset.addVersion(mockDatasetVersion(EXPECTED_URN + "3"));
        }
        return dataset;
    }

    public static DatasetVersion mockDatasetVersion(String expectedUrn) {
        DatasetVersion d = new DatasetVersion();
        SiemacMetadataStatisticalResource siemac = mockSiemacMetadataStatisticalResource(TypeRelatedResourceEnum.DATASET_VERSION, d, expectedUrn, true);
        d.setSiemacMetadataStatisticalResource(siemac);
        d.setDateStart(EXPECTED_PAST_DATE_SDMX);
        d.setDateEnd(EXPECTED_FUTURE_DATE_SDMX);
        d.setRelatedDsdChanged(EXPECTED_TRUE);
        d.setDatasetRepositoryId(EXPECTED_IDENTIFIER);
        d.setFormatExtentDimensions(EXPECTED_COPYRIGHT);
        d.setFormatExtentObservations(EXPECTED_FORMAT_EXTENT_OBSERVATIONS);
        d.setFormatExtentTableSize(EXPECTED_FORMAT_EXTENT_OBSERVATIONS);
        d.setDateNextUpdate(EXPECTED_FUTURE_DATE_SDMX);
        d.setUserModifiedDateNextUpdate(EXPECTED_TRUE);
        d.setVersion(EXPECTED_VERSION);
        d.setDataset(mockDataset(d));
        d.setRelatedDsd(mockExternalItem());
        d.setUpdateFrequency(mockExternalItem());
        d.setStatisticOfficiality(mockStatisticOfficiality());
        d.setBibliographicCitation(mockInternationalString());
        mockDatasourceList(d);
        mockCodeDimensionList(d);
        mockAttributeValueList(d);
        mockCategorisationList(d);
        mockExternalItemList(d.getGeographicCoverage());
        mockTemporalCodeList(d);
        mockExternalItemList(d.getMeasureCoverage());
        mockExternalItemList(d.getGeographicGranularities());
        mockExternalItemList(d.getTemporalGranularities());
        mockExternalItemList(d.getStatisticalUnit());
        d.setPatch(0);
        return d;
    }

    public static DatasetVersion mockDatasetVersion() {
        return mockDatasetVersion(EXPECTED_URN);
    }

    public static SiemacMetadataStatisticalResourceAvro mockSiemacMetadataStatisticalResourceAvro(TypeRelatedResourceEnum type) throws MetamacException {
        List<ExternalItemAvro> listExternalItemStructuralResourcesAvro = mockListExternalItemStructuralResourcesAvro();
        List<ExternalItemAvro> listExternalItemOperationsAvro = mockListExternalItemOprationsAvro();
        // @formatter:off
        SiemacMetadataStatisticalResourceAvro target = SiemacMetadataStatisticalResourceAvro.newBuilder()
                .setLifecycleStatisticalResource(mockLifeCycleStatisticalResourceAvro(type))
                .setAbstractLogic(mockInternationalStringAvro())
                .setAccessRights(mockInternationalStringAvro())
                .setCommonMetadata(mockExternalItemCommonMetadataAvro())
                .setConformsTo(mockInternationalStringAvro())
                .setConformsToInternal(mockInternationalStringAvro())
                .setContributors(listExternalItemStructuralResourcesAvro)
                .setCopyrightedDate(EXPECTED_COPYRIGHT)
                .setCreator(mockExternalItemStructuralResourcesAvro())
                .setKeywords(mockInternationalStringAvro())
                .setLanguage(mockExternalItemStructuralResourcesAvro())
                .setLanguages(listExternalItemStructuralResourcesAvro)
                .setLastUpdate(DateTimeDo2AvroMapper.do2Avro(EXPECTED_PAST_DATE))
                .setMediators(listExternalItemStructuralResourcesAvro)
                .setNewnessUntilDate(DateTimeDo2AvroMapper.do2Avro(EXPECTED_FUTURE_DATE)).setPublisherContributors(listExternalItemStructuralResourcesAvro)
                .setPublishers(listExternalItemStructuralResourcesAvro)
                .setReplaces(mockRelatedResourceAvro(type))
                .setResourceCreatedDate(DateTimeDo2AvroMapper.do2Avro(EXPECTED_PAST_DATE))
                .setStatisticalOperationInstances(listExternalItemOperationsAvro)
                .setSubtitle(mockInternationalStringAvro())
                .setTitleAlternative(mockInternationalStringAvro())
                .setType(StatisticalResourceTypeEnumDo2AvroMapper.do2Avro(StatisticalResourceTypeEnum.COLLECTION))
                .setIsReplacedBy(mockRelatedResourceAvro(type))
                .setNewnessUntilDate(DateTimeDo2AvroMapper.do2Avro(EXPECTED_PAST_DATE))
                .setFeaturedUntilDate(DateTimeDo2AvroMapper.do2Avro(EXPECTED_PAST_DATE))
                .build();
        // @formatter:on
        return target;
    }

    public static SiemacMetadataStatisticalResource mockSiemacMetadataStatisticalResource(TypeRelatedResourceEnum type) {
        return mockSiemacMetadataStatisticalResource(type, null);
    }

    public static SiemacMetadataStatisticalResource mockSiemacMetadataStatisticalResource(TypeRelatedResourceEnum type, DatasetVersion datasetVersion) {
        return mockSiemacMetadataStatisticalResource(type, datasetVersion, null, true);
    }

    public static SiemacMetadataStatisticalResource mockSiemacMetadataStatisticalResource(TypeRelatedResourceEnum type, DatasetVersion datasetVersion, String urn, boolean mockRelatedResources) {
        List<ExternalItem> listExternalItem = mockListExternalItem();
        SiemacMetadataStatisticalResource target = new SiemacMetadataStatisticalResource();
        target.setCode(EXPECTED_CODE);
        target.setStatisticalOperation(mockExternalItem());
        target.setTitle(mockInternationalString());
        target.setDescription(mockInternationalString());
        target.setUrn(urn != null ? urn : EXPECTED_URN);
        target.setNextVersionDate(EXPECTED_FUTURE_DATE_SDMX);
        target.setValidFrom(EXPECTED_PAST_DATE);
        target.setValidTo(EXPECTED_FUTURE_DATE);
        target.setVersionRationale(mockInternationalString());
        target.setNextVersion(EXPECTED_NEXT_VERSION_TYPE);
        target.setVersionLogic(EXPECTED_VERSION_LOGIC);
        target.addVersionRationaleType(new VersionRationaleType(VersionRationaleTypeEnum.MAJOR_CATEGORIES));
        target.setTitle(mockInternationalString());

        target.setCreationDate(EXPECTED_PAST_DATE);
        target.setCreationUser(EXPECTED_USER + "Creation");
        target.setProductionValidationDate(EXPECTED_PAST_DATE);
        target.setProductionValidationUser(EXPECTED_USER + "ProductionValidation");
        target.setDiffusionValidationDate(EXPECTED_PAST_DATE);
        target.setDiffusionValidationUser(EXPECTED_USER + "DiffusionValidation");
        target.setRejectValidationDate(EXPECTED_PAST_DATE);
        target.setRejectValidationUser(EXPECTED_USER + "RejectValidation");
        target.setPublicationDate(EXPECTED_PAST_DATE);
        target.setPublicationUser(EXPECTED_USER + "Publication");

        target.setLastVersion(EXPECTED_LAST_VERSION);
        target.setProcStatus(PRODUCTION_VALIDATION);

        if (datasetVersion != null) {
            target.setReplacesVersion(mockRelatedResource(datasetVersion));
            target.setReplaces(mockRelatedResource(datasetVersion));
            target.setIsReplacedBy(mockRelatedResource(datasetVersion));
            target.setIsReplacedByVersion(mockRelatedResource(datasetVersion));
        } else if (mockRelatedResources) {
            target.setReplacesVersion(mockRelatedResource(type));
            target.setReplaces(mockRelatedResource(type));
            target.setIsReplacedBy(mockRelatedResource(type));
            target.setIsReplacedByVersion(mockRelatedResource(type));
        }

        target.setMaintainer(mockExternalItem());
        target.setAbstractLogic(mockInternationalString());
        target.setAccessRights(mockInternationalString());
        target.setCommonMetadata(mockExternalItem());
        target.setConformsTo(mockInternationalString());
        target.setConformsToInternal(mockInternationalString());
        target.setCopyrightedDate(EXPECTED_COPYRIGHT);
        target.setCreator(mockExternalItem());
        target.setKeywords(mockInternationalString());
        target.setLanguage(mockExternalItem());
        target.setLastUpdate(EXPECTED_PAST_DATE);
        target.setNewnessUntilDate(EXPECTED_FUTURE_DATE);
        target.setResourceCreatedDate(EXPECTED_PAST_DATE);
        target.setSubtitle(mockInternationalString());
        target.setTitleAlternative(mockInternationalString());
        target.setType(StatisticalResourceTypeEnum.COLLECTION);
        target.setUserModifiedKeywords(true);
        for (ExternalItem item : listExternalItem) {
            target.addContributor(item);
            target.addLanguage(item);
            target.addMediator(item);
            target.addPublisher(item);
            target.addPublisherContributor(item);
            target.addStatisticalOperationInstance(item);
        }
        target.setNewnessUntilDate(EXPECTED_PAST_DATE);
        target.setFeaturedUntilDate(EXPECTED_PAST_DATE);
        return target;
    }

    public static List<ExternalItem> mockListExternalItem() {
        List<ExternalItem> listExternalItem = new ArrayList<>();
        listExternalItem.add(mockExternalItem());
        listExternalItem.add(mockExternalItem());
        return listExternalItem;
    }

    public static List<ExternalItemAvro> mockListExternalItemAvro() {
        List<ExternalItemAvro> listExternalItemAvro = new ArrayList<>();
        listExternalItemAvro.add(mockExternalItemAvro());
        listExternalItemAvro.add(mockExternalItemAvro());
        return listExternalItemAvro;
    }

    public static List<ExternalItemAvro> mockListExternalItemOprationsAvro() {
        List<ExternalItemAvro> listExternalItemAvro = new ArrayList<>();
        listExternalItemAvro.add(mockExternalItemOperationsAvro());
        listExternalItemAvro.add(mockExternalItemOperationsAvro());
        return listExternalItemAvro;
    }
    
    public static List<ExternalItemAvro> mockListExternalItemStructuralResourcesAvro() {
        List<ExternalItemAvro> listExternalItemAvro = new ArrayList<>();
        listExternalItemAvro.add(mockExternalItemStructuralResourcesAvro());
        listExternalItemAvro.add(mockExternalItemStructuralResourcesAvro());
        return listExternalItemAvro;
    }

    public static StatisticOfficialityAvro mockStatisticOfficialityAvro() {
        // @formatter:off
        StatisticOfficialityAvro target = StatisticOfficialityAvro.newBuilder()
                .setDescription(mockInternationalStringAvro())
                .setIdentifier(EXPECTED_IDENTIFIER)
                .build();
        // @formatter:on
        return target;
    }

    public static StatisticOfficiality mockStatisticOfficiality() {
        StatisticOfficiality target = new StatisticOfficiality();
        target.setDescription(mockInternationalString());
        target.setIdentifier(EXPECTED_IDENTIFIER);
        target.setVersion(EXPECTED_VERSION);
        return target;
    }

    public static Datasource mockDatasource() {
        return mockDatasource(false);
    }

    public static Datasource mockDatasource(boolean mockDataset) {
        Datasource target = new Datasource();
        if (mockDataset) {
            target.setDatasetVersion(mockDatasetVersion());
        }
        target.setDateNextUpdate(EXPECTED_FUTURE_DATE);
        target.setSourceName(EXPECTED_FILENAME);
        target.setIdentifiableStatisticalResource(mockIdentifiableStatisticalResource());
        target.setVersion(EXPECTED_VERSION);
        return target;
    }

    public static CodeDimensionAvro mockCodeDimensionAvro() {
        // @formatter:off
        CodeDimensionAvro target = CodeDimensionAvro.newBuilder()
                .setDatasetVersionUrn(EXPECTED_URN)
                .setDsdComponentId(EXPECTED_IDENTIFIER)
                .setIdentifier(EXPECTED_IDENTIFIER)
                .setTitle(EXPECTED_TITLE)
                .build();
        // @formatter:off
        return target;
    }

    public static CodeDimension mockCodeDimension() {
        return mockCodeDimension(null);
    }

    public static CodeDimension mockCodeDimension(DatasetVersion datasetVersion) {
        CodeDimension target = new CodeDimension();
        target.setDatasetVersion(datasetVersion != null ? datasetVersion : mockDatasetVersion());
        target.setDsdComponentId(EXPECTED_IDENTIFIER);
        target.setIdentifier(EXPECTED_IDENTIFIER);
        target.setTitle(EXPECTED_TITLE);
        target.setVersion(EXPECTED_VERSION);
        return target;
    }

    public static AttributeValueAvro mockAttributeValueAvro() {
        // @formatter:off
        AttributeValueAvro target = AttributeValueAvro.newBuilder()
                .setDatasetVersionUrn(EXPECTED_URN)
                .setDsdComponentId(EXPECTED_IDENTIFIER)
                .setIdentifier(EXPECTED_IDENTIFIER)
                .setTitle(EXPECTED_TITLE)
                .build();
        // @formatter:on
        return target;
    }

    public static AttributeValue mockAttributeValue() {
        return mockAttributeValue(null);
    }

    public static AttributeValue mockAttributeValue(DatasetVersion datasetVersion) {
        AttributeValue target = new AttributeValue();
        target.setDatasetVersion(datasetVersion != null ? datasetVersion : mockDatasetVersion());
        target.setDsdComponentId(EXPECTED_IDENTIFIER);
        target.setIdentifier(EXPECTED_IDENTIFIER);
        target.setTitle(EXPECTED_TITLE);
        target.setVersion(EXPECTED_VERSION);
        return target;
    }

    public static CategorisationAvro mockCategorisationAvro() throws MetamacException {
        // @formatter:off
        CategorisationAvro target = CategorisationAvro.newBuilder()
                .setDatasetVersionUrn(EXPECTED_URN)
                .setCategory(mockExternalItemStructuralResourcesAvro())
                .setCreatedBy(EXPECTED_USER)
                .setCreatedDate(DateTimeDo2AvroMapper.do2Avro(EXPECTED_PAST_DATE))
                .setVersion(EXPECTED_VERSION)
                .setLastUpdated(DateTimeDo2AvroMapper.do2Avro(EXPECTED_PAST_DATE))
                .setLastUpdatedBy(EXPECTED_USER)
                .setMaintainer(mockExternalItemStructuralResourcesAvro())
                .setValidFromEffective(DateTimeDo2AvroMapper.do2Avro(EXPECTED_PAST_DATE))
                .setValidToEffective(DateTimeDo2AvroMapper.do2Avro(EXPECTED_FUTURE_DATE))
                .setVersionableStatisticalResource(mockVersionableStatisticalResourceAvro())
                .build();
        // @formatter:on
        return target;
    }

    public static Categorisation mockCategorisation() {
        return mockCategorisation(null);
    }

    public static Categorisation mockCategorisation(DatasetVersion datasetVersion) {
        Categorisation target = new Categorisation();
        target.setDatasetVersion(datasetVersion != null ? datasetVersion : mockDatasetVersion());
        target.setCategory(mockExternalItem());
        target.setCreatedBy(EXPECTED_USER);
        target.setCreatedDate(EXPECTED_PAST_DATE);
        target.setVersion(EXPECTED_VERSION);
        target.setLastUpdated(EXPECTED_PAST_DATE);
        target.setLastUpdatedBy(EXPECTED_USER);
        target.setMaintainer(mockExternalItem());
        target.setValidFromEffective(EXPECTED_PAST_DATE);
        target.setValidToEffective(EXPECTED_FUTURE_DATE);
        target.setVersionableStatisticalResource(mockVersionableStatisticalResource());
        return target;
    }

    public static TemporalCodeAvro mockTemporalCodeAvro() {
        // @formatter:off
        TemporalCodeAvro target = TemporalCodeAvro.newBuilder()
                .setIdentifier(EXPECTED_IDENTIFIER)
                .setTitle(EXPECTED_TITLE)
                .build();
        // @formatter:on
        return target;
    }

    public static TemporalCode mockTemporalCode() {
        TemporalCode target = new TemporalCode();
        target.setIdentifier(EXPECTED_IDENTIFIER);
        target.setTitle(EXPECTED_TITLE);
        target.setVersion(EXPECTED_VERSION);
        return target;
    }

    public static DimensionRepresentationMappingAvro mockDimensionRepresentationMappingAvro() {
        // @formatter:off
        DimensionRepresentationMappingAvro target = DimensionRepresentationMappingAvro.newBuilder()
                .setDatasetUrn(EXPECTED_URN)
                .setDatasourceFilename(EXPECTED_FILENAME)
                .setMapping(EXPECTED_LABEL)
                .build();
        // @formatter:on
        return target;
    }

    public static DimensionRepresentationMapping mockDimensionRepresentationMapping() {
        return mockDimensionRepresentationMapping(true);
    }

    public static DimensionRepresentationMapping mockDimensionRepresentationMapping(boolean mockDataset) {
        DimensionRepresentationMapping target = new DimensionRepresentationMapping();
        if (mockDataset) {
            target.setDataset(mockDataset());
        }
        target.setDatasourceFilename(EXPECTED_FILENAME);
        target.setMapping(EXPECTED_LABEL);
        target.setVersion(EXPECTED_VERSION);
        return target;
    }

    public static DatasetAvro mockDatasetAvro() {
        return mockDatasetAvro(1);
    }

    public static DatasetAvro mockDatasetAvro(int nVersions) {
        List<DimensionRepresentationMappingAvro> dimensions = new ArrayList<DimensionRepresentationMappingAvro>();
        dimensions.add(mockDimensionRepresentationMappingAvro());
        List<String> versions = new ArrayList<String>();
        for (int i = 1; i <= nVersions; i++) {
            versions.add(EXPECTED_URN + (i == 1 ? "" : "" + i));
        }
        DatasetAvro target = DatasetAvro.newBuilder().setDimensionRepresentationMappings(dimensions).setIdentifiableStatisticalResource(mockIdentifiableStatisticalResourceAvro())
                .setDatasetVersionsUrns(versions).build();
        return target;
    }

    public static DatasetVersionAvro mockDatasetVersionAvro() throws MetamacException {
        // @formatter:off
        DatasetVersionAvro target = DatasetVersionAvro.newBuilder()
                .setSiemacMetadataStatisticalResource(SiemacMetadataStatisticalResourceDo2AvroMapper.do2Avro(mockSiemacMetadataStatisticalResource(TypeRelatedResourceEnum.DATASET_VERSION)))
                .setDateStart(mockInternationalStringAvroFutureDate())
                .setDateEnd(mockInternationalStringAvroFutureDate())
                .setDatasetRepositoryId(EXPECTED_IDENTIFIER)
                .setFormatExtentObservations(EXPECTED_FORMAT_EXTENT_OBSERVATIONS)
                .setFormatExtentDimensions(EXPECTED_COPYRIGHT)
                .setFormatExtentTableSize(EXPECTED_FORMAT_EXTENT_OBSERVATIONS)
                .setDateNextUpdate(mockInternationalStringAvroFutureDate())
                .setUserModifiedDateNextUpdate(EXPECTED_TRUE)
                .setDataset(mockDatasetAvro()).setRelatedDsd(mockExternalItemStructuralResourcesAvro())
                .setUpdateFrequency(mockExternalItemStructuralResourcesAvro())
                .setStatisticOfficiality(mockStatisticOfficialityAvro())
                .setBibliographicCitation(mockInternationalStringAvro())
                .setDimensionsCoverage(mockCodeDimensionAvroList())
                .setAttributesCoverage(mockAttributeValueAvroList())
                .setCategorisations(mockCategorisationAvroList())
                .setGeographicCoverage(mockExternalItemAvroList())
                .setTemporalCoverage(mockTemporalCodeAvroList())
                .setMeasureCoverage(mockExternalItemAvroList())
                .setGeographicGranularities(mockExternalItemAvroList())
                .setTemporalGranularities(mockExternalItemAvroList())
                .setStatisticalUnit(mockExternalItemAvroList())
                .setIsPartOf(new ArrayList<RelatedResourceAvro>())
                .setVisualizerHtmlLink(mockDatasetVisualizerHtmlLink())
                .build();
        // @formatter:on
        return target;
    }

    protected static String mockDatasetVisualizerHtmlLink() {
        return EXPECTED_API_BASE + "/data.html?resourceType=dataset&agencyId=" + EXPECTED_CODE + "&resourceId=" + EXPECTED_CODE + "&version=" + EXPECTED_CODE;
    }

    protected static List<CodeDimensionAvro> mockCodeDimensionAvroList() {
        List<CodeDimensionAvro> dimensions = new ArrayList<CodeDimensionAvro>();
        CodeDimensionAvro dimension = mockCodeDimensionAvro();
        dimensions.add(dimension);
        return dimensions;
    }

    protected static List<AttributeValueAvro> mockAttributeValueAvroList() {
        List<AttributeValueAvro> attributeValueList = new ArrayList<AttributeValueAvro>();
        AttributeValueAvro attributeValueAvro = mockAttributeValueAvro();
        attributeValueList.add(attributeValueAvro);
        return attributeValueList;
    }

    protected static List<CategorisationAvro> mockCategorisationAvroList() throws MetamacException {
        List<CategorisationAvro> categorisations = new ArrayList<CategorisationAvro>();
        CategorisationAvro categorisationAvro = mockCategorisationAvro();
        categorisations.add(categorisationAvro);
        return categorisations;
    }

    protected static List<ExternalItemAvro> mockExternalItemAvroList() {
        List<ExternalItemAvro> externalItem = new ArrayList<ExternalItemAvro>();
        ExternalItemAvro categorisationAvro = mockExternalItemStructuralResourcesAvro();
        externalItem.add(categorisationAvro);
        return externalItem;
    }

    protected static List<TemporalCodeAvro> mockTemporalCodeAvroList() {
        List<TemporalCodeAvro> categorisations = new ArrayList<TemporalCodeAvro>();
        TemporalCodeAvro categorisationAvro = mockTemporalCodeAvro();
        categorisations.add(categorisationAvro);
        return categorisations;
    }

    protected static void mockCodeDimensionList(DatasetVersion datasetVersion) {
        CodeDimension dimension = mockCodeDimension(datasetVersion);
        datasetVersion.addDimensionsCoverage(dimension);
    }

    protected static void mockDatasourceList(DatasetVersion datasetVersion) {
        Datasource datasource = mockDatasource();
        datasetVersion.addDatasource(datasource);
    }

    protected static void mockAttributeValueList(DatasetVersion datasetVersion) {
        AttributeValue attributeValue = mockAttributeValue(datasetVersion);
        datasetVersion.addAttributesCoverage(attributeValue);
    }

    protected static void mockCategorisationList(DatasetVersion datasetVersion) {
        Categorisation categorisation = mockCategorisation(datasetVersion);
        datasetVersion.addCategorisation(categorisation);
    }

    protected static void mockExternalItemList(Collection<ExternalItem> list) {
        ExternalItem externalItem = mockExternalItem();
        list.add(externalItem);
    }

    protected static void mockTemporalCodeList(DatasetVersion datasetVersion) {
        TemporalCode temporalCode = mockTemporalCode();
        datasetVersion.addTemporalCoverage(temporalCode);
    }

    public static PublicationVersion mockPublicationVersion() {
        Publication mockPublication = mockPublication(true);
        PublicationVersion publicationVersion = mockPublicationVersion(EXPECTED_URN, mockPublication);
        return publicationVersion;
    }

    public static PublicationVersion mockIsolatedPublicationVersion() {
        Publication mockPublication = mockPublication(false);

        PublicationVersion mock = new PublicationVersion();
        mock.setVersion(EXPECTED_VERSION);
        mock.setPublication(mockPublication);
        mock.setSiemacMetadataStatisticalResource(mockSiemacMetadataStatisticalResource(TypeRelatedResourceEnum.PUBLICATION_VERSION, null, null, false));
        mock.getSiemacMetadataStatisticalResource().setUrn(EXPECTED_URN);

        return mock;
    }

    public static PublicationVersion mockPublicationVersion(String urn, Publication publication) {
        PublicationVersion mock = new PublicationVersion();
        mock.setVersion(EXPECTED_VERSION);
        mock.setPublication(publication);
        mock.setSiemacMetadataStatisticalResource(mockSiemacMetadataStatisticalResource(TypeRelatedResourceEnum.PUBLICATION_VERSION));
        if (urn != null) {
            mock.getSiemacMetadataStatisticalResource().setUrn(urn);
        }
        mock.addHasPart(mockRelatedResource(TypeRelatedResourceEnum.PUBLICATION_VERSION));
        return mock;
    }

    public static PublicationVersionAvro mockPublicationVersionAvro() throws MetamacException {
        // @formatter:off
        PublicationVersionAvro mock = PublicationVersionAvro.newBuilder()
                .setPublication(mockPublicationAvro())
                .setSiemacMetadataStatisticalResource(mockSiemacMetadataStatisticalResourceAvro(TypeRelatedResourceEnum.PUBLICATION_VERSION))
                .setHasPart(mockRelatedResultAvroList(TypeRelatedResourceEnum.PUBLICATION_VERSION))
                .build();
        // @formatter:on
        return mock;
    }

    private static List<RelatedResourceAvro> mockRelatedResultAvroList(TypeRelatedResourceEnum type) {
        List<RelatedResourceAvro> list = new ArrayList<RelatedResourceAvro>();
        list.add(mockRelatedResourceAvro(type));
        return list;
    }

    public static Publication mockPublication(boolean mockPublicationVersions) {
        Publication mock = new Publication();
        mock.setVersion(EXPECTED_VERSION);
        mock.setIdentifiableStatisticalResource(mockIdentifiableStatisticalResource());
        if (mockPublicationVersions) {
            for (String urn : mockListUrns()) {
                mock.addVersion(mockPublicationVersion(urn, mock));
            }
        }
        return mock;
    }

    public static PublicationAvro mockPublicationAvro() {
        // @formatter:off
        PublicationAvro mock = PublicationAvro.newBuilder()
                .setIdentifiableStatisticalResource(mockIdentifiableStatisticalResourceAvro())
                .setPublicationVersionsUrns(mockListUrns())
                .build();
        // @formatter:on
        return mock;
    }

    private static List<String> mockListUrns() {
        List<String> list = new ArrayList<String>();
        list.add(EXPECTED_URN);
        list.add(EXPECTED_URN + "2");
        return list;
    }

    public static Map<String, String> mockMapTranslateDateSdmx() {
        Map<String, String> map = new HashMap<>();
        map.put(MappersMockUtils.EXPECTED_LOCALE, MappersMockUtils.EXPECTED_FUTURE_DATE_SDMX);
        return map;
    }
}
