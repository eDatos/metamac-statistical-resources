
package org.siemac.metamac.statistical_resources.rest.internal.v1_0.mapper.base;

import static org.siemac.metamac.core.common.util.rest.RequestUtil.containsField;
import static org.siemac.metamac.statistical_resources.rest.common.service.utils.StatisticalResourcesRestImplCommonUtils.isDateAfterNowSetNull;
import static org.siemac.metamac.statistical_resources.rest.internal.StatisticalResourcesRestInternalConstants.KEY_DIMENSIONS_SEPARATOR;
import static org.siemac.metamac.statistical_resources.rest.internal.StatisticalResourcesRestInternalConstants.SERVICE_CONTEXT;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.siemac.edatos.core.common.constants.shared.SDMXCommonRegExpV2_1;
import org.siemac.metamac.core.common.enume.domain.IstacTimeGranularityEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.time.IstacTimeUtils;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.rest.api.constants.RestApiConstants;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.common_metadata.v1_0.domain.Configuration;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Instance;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operation;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Agency;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodeResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Concept;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Concepts;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ContentConstraint;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataType;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DimensionVisualisation;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Item;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ItemResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Key;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.KeyPart;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Quantity;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ShowDecimalPrecision;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.TextFormat;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VariableElementResourceInternal;
import org.siemac.metamac.rest.utils.RestCommonUtil;
import org.siemac.metamac.rest.utils.RestUtils;
import org.siemac.metamac.srm.rest.common.SrmRestConstants;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionRationaleType;
import org.siemac.metamac.statistical.resources.core.common.domain.DimensionOrder;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResource;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceResult;
import org.siemac.metamac.statistical.resources.core.common.serviceapi.TranslationService;
import org.siemac.metamac.statistical.resources.core.common.utils.DsdProcessor;
import org.siemac.metamac.statistical.resources.core.common.utils.DsdProcessor.DsdAttribute;
import org.siemac.metamac.statistical.resources.core.common.utils.DsdProcessor.DsdComponentType;
import org.siemac.metamac.statistical.resources.core.common.utils.DsdProcessor.DsdDimension;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.dataset.domain.AttributeValue;
import org.siemac.metamac.statistical.resources.core.dataset.domain.CodeDimension;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.TemporalCode;
import org.siemac.metamac.statistical.resources.core.dataset.serviceapi.DatasetService;
import org.siemac.metamac.statistical.resources.core.dataset.utils.DatasetVersionUtils;
import org.siemac.metamac.statistical.resources.core.enume.domain.NextVersionTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.VersionRationaleTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.utils.IstacTimeGranularityCodeEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.invocation.utils.InternalWebApplicationNavigation;
import org.siemac.metamac.statistical.resources.core.io.domain.TemporalAttributeValues;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.multidataset.serviceapi.MultidatasetService;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.publication.serviceapi.PublicationService;
import org.siemac.metamac.statistical.resources.core.query.domain.CodeItem;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.query.serviceapi.QueryService;
import org.siemac.metamac.statistical.resources.core.utils.AttributesUtils;
import org.siemac.metamac.statistical.resources.core.utils.InternationalStringUtils;
import org.siemac.metamac.statistical_resources.rest.common.StatisticalResourcesRestConstants;
import org.siemac.metamac.statistical_resources.rest.common.impl.export.utils.DimensionsFilter;
import org.siemac.metamac.statistical_resources.rest.common.impl.mappers.external.resources.ExternalRestObjectsMapper;
import org.siemac.metamac.statistical_resources.rest.common.service.utils.StatisticalResourcesRestImplCommonUtils;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attribute;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.AttributeAttachmentLevelType;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.AttributeDimension;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.AttributeDimensions;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.AttributeValues;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attributes;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.CodeRepresentation;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.CodeRepresentations;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ComponentType;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ConstraintCodeRepresentation;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ConstraintCodeRepresentations;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ConstraintDimensionRepresentation;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ConstraintDimensionRepresentations;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Contact;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Contacts;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Data;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DataAttribute;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DataAttributes;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DataInternationalAttribute;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DataStructureDefinition;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Dimension;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionRepresentation;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionRepresentations;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionType;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionValues;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Dimensions;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionsId;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.EnumeratedAttributeValue;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.EnumeratedAttributeValues;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.EnumeratedDimensionValue;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.EnumeratedDimensionValues;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.MeasureQuantity;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.NextVersionType;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.NonEnumeratedAttributeValue;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.NonEnumeratedAttributeValues;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.NonEnumeratedDimensionValue;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.NonEnumeratedDimensionValues;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Organisation;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ProcStatusType;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ResourceStatisticalResourceBase;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ResourcesStatisticalResourceBase;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.SelectedLanguages;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.StatisticalResourceBase;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.StatisticalResourceType;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.VersionRationaleTypes;
import org.siemac.metamac.statistical_resources.rest.internal.StatisticalResourcesRestInternalConstants;
import org.siemac.metamac.statistical_resources.rest.internal.exception.RestServiceExceptionType;
import org.siemac.metamac.statistical_resources.rest.internal.invocation.CommonMetadataRestExternalFacade;
import org.siemac.metamac.statistical_resources.rest.internal.invocation.SrmRestInternalFacade;
import org.siemac.metamac.statistical_resources.rest.internal.invocation.StatisticalOperationsRestInternalFacade;
import org.siemac.metamac.statistical_resources.rest.internal.service.utils.LookupUtil;
import org.siemac.metamac.statistical_resources.rest.internal.v1_0.domain.DsdProcessorResult;
import org.siemac.metamac.statistical_resources.rest.internal.v1_0.mapper.collection.CollectionsDo2RestMapperV10;
import org.siemac.metamac.statistical_resources.rest.internal.v1_0.mapper.dataset.DatasetsDo2RestMapperV10;
import org.siemac.metamac.statistical_resources.rest.internal.v1_0.mapper.multidataset.MultidatasetsDo2RestMapperV10;
import org.siemac.metamac.statistical_resources.rest.internal.v1_0.mapper.query.QueriesDo2RestMapperV10;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceBasicDto;
import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceDto;
import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceObservationDto;
import es.gobcan.istac.edatos.dataset.repository.dto.CodeDimensionDto;
import es.gobcan.istac.edatos.dataset.repository.dto.ConditionDimensionDto;
import es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto;
import es.gobcan.istac.edatos.dataset.repository.dto.LocalisedStringDto;
import es.gobcan.istac.edatos.dataset.repository.dto.ObservationExtendedDto;
import es.gobcan.istac.edatos.dataset.repository.service.DatasetRepositoriesServiceFacade;

@Component
public class CommonDo2RestMapperV10Impl implements CommonDo2RestMapperV10 {

    private static final Logger                     logger                 = LoggerFactory.getLogger(CommonDo2RestMapperV10.class);

    private static final String                     INCLUDE_SPATIAL_FIELDS = SrmRestConstants.FIELD_INCLUDE_OPENNES + RestApiConstants.COMMA + SrmRestConstants.FIELD_INCLUDE_ORDER
            + RestApiConstants.COMMA + SrmRestConstants.FIELD_INCLUDE_VARIABLE_ELEMENT;

    @Autowired
    private StatisticalResourcesConfiguration       configurationService;

    @Autowired
    private StatisticalOperationsRestInternalFacade statisticalOperationsRestInternalFacade;

    @Autowired
    private DatasetService                          datasetService;

    @Autowired
    private QueryService                            queryService;

    @Autowired
    private MultidatasetService                     multidatasetService;

    @Autowired
    private PublicationService                      publicationService;

    @Autowired
    private TranslationService                      translationService;

    @Autowired
    private DatasetRepositoriesServiceFacade        datasetRepositoriesServiceFacade;

    @Autowired
    private SrmRestInternalFacade                   srmRestExternalFacade;

    @Autowired
    private CommonMetadataRestExternalFacade        commonMetadataRestExternalFacade;

    @Autowired
    private DatasetsDo2RestMapperV10                datasetsDo2RestMapper;

    @Autowired
    private CollectionsDo2RestMapperV10             collectionsDo2RestMapper;

    @Autowired
    private QueriesDo2RestMapperV10                 queriesDo2RestMapper;

    @Autowired
    private MultidatasetsDo2RestMapperV10           multidatasetsDo2RestMapper;

    private String                                  statisticalResourcesApiInternalEndpointV10;
    private String                                  srmApiInternalEndpoint;
    private String                                  statisticalOperationsApiInternalEndpoint;
    private String                                  statisticalResourcesInternalWebApplication;
    private String                                  srmInternalWebApplication;
    private String                                  commonMetadataInternalWebApplication;
    private String                                  statisticalOperationsWebApplication;
    private String                                  defaultLanguage;

    private InternalWebApplicationNavigation        internalWebApplicationNavigation;

    @PostConstruct
    public void init() throws Exception {

        // APPLICATIONS
        statisticalResourcesInternalWebApplication = configurationService.retrieveStatisticalResourcesInternalWebApplicationUrlBase();
        srmInternalWebApplication = configurationService.retrieveSrmInternalWebApplicationUrlBase();
        commonMetadataInternalWebApplication = configurationService.retrieveCommonMetadataInternalWebApplicationUrlBase();
        statisticalOperationsWebApplication = configurationService.retrieveStatisticalOperationsInternalWebApplicationUrlBase();

        // ENDPOINTS
        // Statistical resources internal Api V1.0
        String statisticalResourcesApiInternalEndpoint = configurationService.retrieveStatisticalResourcesInternalApiUrlBase();
        statisticalResourcesApiInternalEndpointV10 = RestUtils.createLink(statisticalResourcesApiInternalEndpoint, StatisticalResourcesRestInternalConstants.API_VERSION_1_0);

        // SRM external Api (do not add api version! it is already stored in database (~latest))
        srmApiInternalEndpoint = configurationService.retrieveSrmInternalApiUrlBase();
        srmApiInternalEndpoint = StringUtils.removeEnd(srmApiInternalEndpoint, "/");

        // Statistical operations external Api (do not add api version! it is already stored in database (~latest))
        statisticalOperationsApiInternalEndpoint = configurationService.retrieveStatisticalOperationsInternalApiUrlBase();
        statisticalOperationsApiInternalEndpoint = StringUtils.removeEnd(statisticalOperationsApiInternalEndpoint, "/");

        // MISC
        defaultLanguage = configurationService.retrieveLanguageDefault();

        internalWebApplicationNavigation = new InternalWebApplicationNavigation(statisticalResourcesInternalWebApplication);
    }

    @Override
    public InternationalString toSdmxObservationalTimePeriod(String sdmxValue, List<String> selectedLanguages) throws MetamacException {
        if (StringUtils.isNotBlank(sdmxValue)) {
            Map<String, String> internationalStringValue = translationService.retrieveTimeTranslation(SERVICE_CONTEXT, sdmxValue);
            return toInternationalString(internationalStringValue, selectedLanguages);
        }
        return null;
    }

    @Override
    public InternalWebApplicationNavigation getInternalWebApplicationNavigation() {
        return internalWebApplicationNavigation;
    }

    @Override
    public Concept toConcept(String urn) {
        return srmRestExternalFacade.retrieveConceptByUrn(urn);
    }

    @Override
    public DsdProcessorResult processDataStructure(String urn) throws MetamacException {
        DsdProcessorResult dsdProcessorResult = new DsdProcessorResult();
        DataStructure dataStructure = srmRestExternalFacade.retrieveDataStructureByUrn(urn);
        dsdProcessorResult.setDataStructure(dataStructure);
        dsdProcessorResult.setDimensions(DsdProcessor.getDimensions(dataStructure));
        dsdProcessorResult.setAttributes(DsdProcessor.getAttributes(dataStructure));
        dsdProcessorResult.setGroups(DsdProcessor.getGroups(dataStructure));
        return dsdProcessorResult;
    }

    @Override
    public ConstraintDimensionRepresentations processDatasetConstraint(String datasetUrn) throws MetamacException {

        ContentConstraint contentConstraint = srmRestExternalFacade.retrieveDatasetContentConstraint(datasetUrn);

        if (contentConstraint != null) {

            ConstraintDimensionRepresentations constraintDimensionRepresentations = new ConstraintDimensionRepresentations();

            constraintDimensionRepresentations.setId(contentConstraint.getId());
            constraintDimensionRepresentations.setUrn(contentConstraint.getUrn());

            if (contentConstraint.getRegions().getRegions().isEmpty()) {
                constraintDimensionRepresentations.setTotal(BigInteger.valueOf(0));
            } else {
                constraintDimensionRepresentations.setTotal(contentConstraint.getRegions().getRegions().get(0).getKeys().getTotal());
                constraintDimensionRepresentations.getDimensions().addAll(toConstraintDimensionRepresentations(contentConstraint));
            }

            return constraintDimensionRepresentations;
        }
        return null;
    }

    private List<ConstraintDimensionRepresentation> toConstraintDimensionRepresentations(ContentConstraint contentConstraint) throws MetamacException {
        List<ConstraintDimensionRepresentation> constraintDimensionRepresentations = new ArrayList<ConstraintDimensionRepresentation>();

        for (Key key : contentConstraint.getRegions().getRegions().get(0).getKeys().getKeies()) {
            ConstraintDimensionRepresentation constraintDimensionRepresentation = new ConstraintDimensionRepresentation();
            constraintDimensionRepresentation.setIncluded(key.isIncluded());

            constraintDimensionRepresentation.setRepresentations(toConstraintCodeRepresentation(key.getKeyParts().getKeyParts()));

            if (!constraintDimensionRepresentation.getRepresentations().getRepresentations().isEmpty()) {
                constraintDimensionRepresentation.setPosition(key.getKeyParts().getKeyParts().get(0).getPosition());
                constraintDimensionRepresentation.setDimensionId(key.getKeyParts().getKeyParts().get(0).getIdentifier());
            }

            constraintDimensionRepresentations.add(constraintDimensionRepresentation);
        }

        return constraintDimensionRepresentations;

    }

    private ConstraintCodeRepresentations toConstraintCodeRepresentation(List<KeyPart> keyParts) {
        ConstraintCodeRepresentations codeRepresentations = new ConstraintCodeRepresentations();
        codeRepresentations.setTotal(BigInteger.valueOf(keyParts.size()));
        for (KeyPart keyPart : keyParts) {
            ConstraintCodeRepresentation codeRepresentation = new ConstraintCodeRepresentation();
            codeRepresentation.setCode(keyPart.getValue());
            codeRepresentations.getRepresentations().add(codeRepresentation);
        }

        return codeRepresentations;
    }

    @Override
    public void toMetadataStatisticalResource(SiemacMetadataStatisticalResource source, StatisticalResourceBase target, List<String> selectedLanguages) throws MetamacException {
        if (source == null) {
            return;
        }
        ExternalRestObjectsMapper externalRestObjectsMapper = new ExternalRestObjectsMapper();

        target.setLanguage(toResourceExternalItemSrm(source.getLanguage(), selectedLanguages));
        target.setLanguages(toResourcesExternalItemsSrm(source.getLanguages(), selectedLanguages));
        target.setStatisticalOperation(toResourceExternalItemStatisticalOperations(source.getStatisticalOperation(), selectedLanguages, externalRestObjectsMapper));
        target.setStatisticalOperationInstances(toResourcesExternalItemsStatisticalOperations(source.getStatisticalOperationInstances(), selectedLanguages, externalRestObjectsMapper));
        target.setSubtitle(toInternationalString(source.getSubtitle(), selectedLanguages));
        target.setTitleAlternative(toInternationalString(source.getTitleAlternative(), selectedLanguages));
        target.setAbstract(toInternationalString(source.getAbstractLogic(), selectedLanguages));
        target.setType(toStatisticalResourceType(source.getType()));
        target.setCreator(toResourceExternalItemSrm(source.getCreator(), selectedLanguages));
        target.setDataProviders(toResourcesExternalItemsSrm(source.getDataProvider(), selectedLanguages));
        target.setDataProviderAnnotations(toInternationalString(source.getDataProviderAnnotations(), selectedLanguages));
        target.setContributors(toResourcesExternalItemsSrm(source.getContributor(), selectedLanguages));
        target.setCreatedDate(toDate(source.getResourceCreatedDate()));
        target.setLastUpdate(toDate(source.getLastUpdate()));
        target.setConformsTo(toInternationalString(source.getConformsTo(), selectedLanguages));
        target.setPublishers(toResourcesExternalItemsSrm(source.getPublisher(), selectedLanguages));
        target.setPublisherContributors(toResourcesExternalItemsSrm(source.getPublisherContributor(), selectedLanguages));
        target.setMediators(toResourcesExternalItemsSrm(source.getMediator(), selectedLanguages));
        target.setNewnessUntilDate(toDate(source.getNewnessUntilDate()));

        // note: hasPart, isPartOf: in concrete mappers

        toCommonMetadata(source, target, selectedLanguages);

        target.setCopyrightDate(source.getCopyrightedDate());
        target.setAccessRights(toInternationalString(source.getAccessRights(), selectedLanguages));

        // Lifecycle
        // note: replacesVersion and isReplacedByVersion are only valid to datasets. So, they are mapped in Dataset mapper
        target.setMaintainer(toResourceExternalItemSrm(source.getMaintainer(), selectedLanguages));

        // Versionable
        target.setVersion(source.getVersionLogic());
        target.setVersionRationaleTypes(toVersionRationaleTypes(source.getVersionRationaleTypes(), selectedLanguages));
        target.setVersionRationale(toInternationalString(source.getVersionRationale(), selectedLanguages));
        target.setValidFrom(toDate(source.getValidFrom()));
        target.setValidTo(toDate(isDateAfterNowSetNull(source.getValidTo())));
        target.setNextVersion(toNextVersionType(source.getNextVersion(), selectedLanguages));

    }

    @Override
    public Data toData(DatasetVersion source, DsdProcessorResult dsdProcessorResult, Map<String, List<String>> dimensionValuesSelected, List<String> selectedLanguages,
            DimensionsFilter dimensionsFilter) throws Exception {
        return toData(source, dsdProcessorResult, dimensionValuesSelected, selectedLanguages, true, dimensionsFilter);
    }

    @Override
    public Data toData(DatasetVersion source, DsdProcessorResult dsdProcessorResult, Map<String, List<String>> dimensionValuesSelected, List<String> selectedLanguages, boolean checkNoData,
            DimensionsFilter dimensionsFilter) throws Exception {
        if (source == null) {
            return null;
        }
        Data target = new Data();
        try {
            // Filter codes
            List<String> datasetDimensions = datasetService.retrieveDatasetVersionDimensionsIds(SERVICE_CONTEXT, source.getSiemacMetadataStatisticalResource().getUrn());
            Map<String, List<String>> dimensionsCodesSelectedEffective = modifyDimensionsCodesSelected(buildDimensionsSelectedWithValues(source, dimensionValuesSelected, datasetDimensions),
                    dimensionsFilter);

            // Transform data
            // Dimensions
            toDataDimensionRepresentations(datasetDimensions, dimensionsCodesSelectedEffective, target);
            // Observations and attributes
            target.setAttributes(new DataAttributes());
            toDataAttributesWithDatasetAndDimensionAttachmenteLevel(dsdProcessorResult, source.getDatasetRepositoryId(), datasetDimensions, dimensionsCodesSelectedEffective, target.getAttributes(),
                    selectedLanguages);
            toDataObservationsAndAttributeWithObservationAttachmentLevel(source, dsdProcessorResult, datasetDimensions, dimensionValuesSelected, dimensionsCodesSelectedEffective, target,
                    dimensionsFilter);
            if (CollectionUtils.isEmpty(target.getAttributes().getAttributes())) {
                target.setAttributes(null);
            } else {
                target.getAttributes().setTotal(getTotalAttributes(target.getAttributes()));
            }
        } catch (MetamacException e) {
            handleDatasetException(e, checkNoData);
        }
        return target;
    }

    private BigInteger getTotalAttributes(DataAttributes attributes) {
        BigInteger totalAttributes = BigInteger.valueOf(0);
        if (attributes.getAttributes() != null) {
            totalAttributes = totalAttributes.add(BigInteger.valueOf(attributes.getAttributes().size()));
        }
        if (!CollectionUtils.isEmpty(attributes.getInternationalAttributes())) {
            totalAttributes = totalAttributes.add(BigInteger.valueOf(attributes.getInternationalAttributes().size()));
        }
        return totalAttributes;

    }

    @Override
    public DimensionsFilter getDimensionFilter(Dimensions dimensions) {
        DimensionsFilter dimensionsFilter = null;
        if (dimensions != null) {
            dimensionsFilter = new DimensionsFilter();
            for (Dimension dimension : dimensions.getDimensions()) {
                if (DimensionType.TIME_DIMENSION.equals(dimension.getType())) {
                    dimensionsFilter.setTemporalDimensionId(dimension.getId());
                    dimensionsFilter.setTemporalDimensionValuesIds(getTemporalDimensionsValuesIds(dimension));
                }
                if (DimensionType.GEOGRAPHIC_DIMENSION.equals(dimension.getType())) {
                    dimensionsFilter.setGeographicDimensionId(dimension.getId());
                    dimensionsFilter.setGeographicDimensionValuesIds(getGeographicDimensionsValuesIds(dimension));
                }
            }
        }
        return dimensionsFilter;
    }

    private List<String> getTemporalDimensionsValuesIds(Dimension dimension) {
        List<String> dimensionsValuesIds = new ArrayList<>();
        NonEnumeratedDimensionValues dimensionValues = ((NonEnumeratedDimensionValues) dimension.getDimensionValues());
        if (dimensionValues != null) {
            for (NonEnumeratedDimensionValue dimensionValue : dimensionValues.getValues()) {
                dimensionsValuesIds.add(dimensionValue.getId());
            }
        }
        return dimensionsValuesIds;
    }

    private List<String> getGeographicDimensionsValuesIds(Dimension dimension) {
        List<String> dimensionsValuesIds = new ArrayList<>();
        EnumeratedDimensionValues dimensionValues = ((EnumeratedDimensionValues) dimension.getDimensionValues());
        if (dimensionValues != null) {
            for (EnumeratedDimensionValue dimensionValue : dimensionValues.getValues()) {
                dimensionsValuesIds.add(dimensionValue.getId());
            }
        }
        return dimensionsValuesIds;
    }

    private Map<String, List<String>> modifyDimensionsCodesSelected(Map<String, List<String>> dimensionsCodesSelectedEffective, DimensionsFilter dimensionsFilter) {
        if (dimensionsFilter == null) {
            return dimensionsCodesSelectedEffective;
        }

        Map<String, List<String>> dimensionsCodesSelectedEffectiveFinal = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : dimensionsCodesSelectedEffective.entrySet()) {
            if (entry.getKey().equals(dimensionsFilter.getTemporalDimensionId())) {
                dimensionsCodesSelectedEffectiveFinal.put(dimensionsFilter.getTemporalDimensionId(), oderDimensionValues(dimensionsFilter.getTemporalDimensionValuesIds(), entry.getValue()));
            } else if (entry.getKey().equals(dimensionsFilter.getGeographicDimensionId())) {
                dimensionsCodesSelectedEffectiveFinal.put(dimensionsFilter.getGeographicDimensionId(),
                        filterGeographicDimensionsId(dimensionsFilter.getGeographicDimensionValuesIds(), entry.getValue()));
            } else {
                dimensionsCodesSelectedEffectiveFinal.put(entry.getKey(), entry.getValue());
            }
        }
        return dimensionsCodesSelectedEffectiveFinal;
    }

    private List<String> filterGeographicDimensionsId(List<String> dimensionFilterGeographicDimensionsIds, List<String> dimensionsCodesSelectedEffective) {
        List<String> geographicDimensionsIDFiltred = new ArrayList<>();
        for (String dimensionFilterGeographicDimensionsId : dimensionFilterGeographicDimensionsIds) {
            if (dimensionsCodesSelectedEffective.contains(dimensionFilterGeographicDimensionsId)) {
                geographicDimensionsIDFiltred.add(dimensionFilterGeographicDimensionsId);
            }
        }
        return geographicDimensionsIDFiltred;
    }

    private List<String> oderDimensionValues(List<String> dimensionFilterDimensionValues, List<String> dimensionsCodesSelectedEffectiveDimensionsValues) {
        List<String> dimensionsValuesOrdered = new ArrayList<>();
        for (String dimensionsCodesSelectedEffectiveDimensionsValue : dimensionsCodesSelectedEffectiveDimensionsValues) {
            if (dimensionFilterDimensionValues.contains(dimensionsCodesSelectedEffectiveDimensionsValue)) {
                dimensionsValuesOrdered.add(dimensionsCodesSelectedEffectiveDimensionsValue);
            }
        }
        return dimensionsValuesOrdered;
    }

    private void handleDatasetException(MetamacException e, boolean checkNoData) throws MetamacException {
        if (checkNoData || (e.getExceptionItems().isEmpty() || !e.getExceptionItems().iterator().next().getCode().equals(ServiceExceptionType.DATASET_NO_DATA.getCode()))) {
            StatisticalResourcesRestImplCommonUtils.handleException(e);
        }
    }

    @Override
    public DataStructureDefinition toDataStructureDefinition(ExternalItem source, DataStructure dataStructure, List<String> selectedLanguages, List<DimensionOrder> headingDimensions,
            List<DimensionOrder> stubDimensions) {
        if (source == null) {
            return null;
        }
        DataStructureDefinition target = new DataStructureDefinition();
        toResourceExternalItemSrm(source, target, selectedLanguages);
        target.setHeading(
                checkDatasetHaveHeadingAndStubDimensions(headingDimensions, stubDimensions) ? toDimensionsIdByDataset(headingDimensions, dataStructure) : toDimensionsId(dataStructure.getHeading()));
        target.setStub(checkDatasetHaveHeadingAndStubDimensions(headingDimensions, stubDimensions) ? toDimensionsIdByDataset(stubDimensions, dataStructure) : toDimensionsId(dataStructure.getStub()));
        target.setAutoOpen(dataStructure.isAutoOpen());
        target.setShowNullValuesByDefault(dataStructure.isShowNullValuesByDefault());
        target.setShowZeroValuesByDefault(dataStructure.isShowZeroValuesByDefault());
        target.setShowDecimals(dataStructure.getShowDecimals());
        return target;
    }

    private boolean checkDatasetHaveHeadingAndStubDimensions(List<DimensionOrder> headingDimensions, List<DimensionOrder> stubDimensions) {
        return ((stubDimensions != null && !stubDimensions.isEmpty()) || (headingDimensions != null && !headingDimensions.isEmpty()));
    }

    private DimensionsId toDimensionsIdByDataset(List<DimensionOrder> dimensionsOrders, DataStructure dataStructure) {
        if (dimensionsOrders == null) {
            return null;
        }
        DimensionsId targets = new DimensionsId();
        for (DimensionOrder source : dimensionsOrders) {
            targets.getDimensionIds().add(getDimensionName(source.getUrnDimComponentFk(), dataStructure.getDataStructureComponents().getDimensions()));
        }
        targets.setTotal(BigInteger.valueOf(targets.getDimensionIds().size()));
        return targets;
    }

    private String getDimensionName(String urn, org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Dimensions dimensions) {
        for (org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DimensionBase dimension : dimensions.getDimensions()) {
            if (dimension.getUrn().equals(urn)) {
                return dimension.getId();
            }
        }
        return "";
    }

    /**
     * @param effectiveDimensionValuesToDataByDimension It is necessary when query is retrieved, to filter dimension values. It can be null; in this case, returns all
     */
    @Override
    public Dimensions toDimensions(String datasetVersionUrn, DsdProcessorResult dsdProcessorResult, Map<String, List<String>> effectiveDimensionValuesToDataByDimension, List<String> selectedLanguages,
            Set<String> fields, DimensionsFilter dimensionsFilter) throws MetamacException {

        List<DsdDimension> sources = dsdProcessorResult.getDimensions();
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }

        List<String> dimensionsId = new LinkedList<>();
        for (DsdDimension dsdDimension : dsdProcessorResult.getDimensions()) {
            dimensionsId.add(dsdDimension.getComponentId());
        }

        if (CollectionUtils.isEmpty(dimensionsId)) {
            return null;
        }

        // Visualisations
        DataStructure dataStructure = dsdProcessorResult.getDataStructure();
        Map<String, DimensionVisualisation> dimensionVisualisationByDimensionId = null;
        if (dataStructure.getDimensionVisualisations() != null && !CollectionUtils.isEmpty(dataStructure.getDimensionVisualisations().getDimensionVisualisations())) {
            dimensionVisualisationByDimensionId = new HashMap<String, DimensionVisualisation>(dataStructure.getDimensionVisualisations().getDimensionVisualisations().size());
            for (DimensionVisualisation dimensionVisualisation : dataStructure.getDimensionVisualisations().getDimensionVisualisations()) {
                dimensionVisualisationByDimensionId.put(dimensionVisualisation.getDimension(), dimensionVisualisation);
            }
        }

        // Transform dimensions
        Dimensions targets = new Dimensions();
        for (DsdDimension source : sources) {
            String dimensionId = source.getComponentId();
            DimensionVisualisation dimensionVisualisation = dimensionVisualisationByDimensionId != null ? dimensionVisualisationByDimensionId.get(dimensionId) : null;
            List<String> effectiveDimensionValuesToData = null;
            if (effectiveDimensionValuesToDataByDimension != null) {
                effectiveDimensionValuesToData = effectiveDimensionValuesToDataByDimension.get(dimensionId);
            }
            Dimension target = toDimension(datasetVersionUrn, dataStructure, source, dimensionVisualisation, effectiveDimensionValuesToData, selectedLanguages, fields, dimensionsFilter);
            targets.getDimensions().add(target);
        }
        targets.setTotal(BigInteger.valueOf(targets.getDimensions().size()));
        return targets;
    }

    @Override
    public Attributes toAttributes(String datasetVersionUrn, DsdProcessorResult dsdProcessorResult, List<String> selectedLanguages) throws MetamacException {

        return toAttributes(datasetVersionUrn, dsdProcessorResult, selectedLanguages, true);
    }

    @Override
    public Attributes toAttributes(String datasetVersionUrn, DsdProcessorResult dsdProcessorResult, List<String> selectedLanguages, boolean checkNoData) throws MetamacException {

        List<DsdAttribute> sources = dsdProcessorResult.getAttributes();
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }

        Attributes targets = new Attributes();
        try {
            List<String> datasetDimensionsOrdered = datasetService.retrieveDatasetVersionDimensionsIds(SERVICE_CONTEXT, datasetVersionUrn);

            for (DsdAttribute source : sources) {
                Attribute target = toAttribute(datasetVersionUrn, source, dsdProcessorResult, datasetDimensionsOrdered, selectedLanguages);
                targets.getAttributes().add(target);
            }
            targets.setTotal(BigInteger.valueOf(targets.getAttributes().size()));
        } catch (MetamacException e) {
            handleDatasetException(e, checkNoData);
        }
        return targets;
    }

    @Override
    public List<String> codeItemToString(List<CodeItem> sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }
        List<String> targets = new ArrayList<String>(sources.size());
        for (CodeItem source : sources) {
            targets.add(source.getCode());
        }
        return targets;
    }

    @Override
    public List<String> temporalCoverageToString(List<TemporalCode> sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }
        List<String> targets = new ArrayList<String>(sources.size());
        for (TemporalCode source : sources) {
            targets.add(source.getIdentifier());
        }
        return targets;
    }

    @Override
    public ResourceStatisticalResourceBase toResourceExternalItemStatisticalOperations(ExternalItem source, List<String> selectedLanguages, ExternalRestObjectsMapper externalRestObjectsMapper) {
        if (source == null) {
            return null;
        }
        ResourceStatisticalResourceBase target = new ResourceStatisticalResourceBase();
        toResourceExternalItem(source, statisticalOperationsApiInternalEndpoint, statisticalOperationsWebApplication, target, selectedLanguages);
        updateNameForNonVersionableResource(target, source, externalRestObjectsMapper);
        return target;
    }

    private void updateNameForNonVersionableResource(ResourceStatisticalResourceBase target, ExternalItem source, ExternalRestObjectsMapper externalRestObjectsMapper) {
        switch (source.getType()) {
            case STATISTICAL_OPERATION:
                target.setName(getUpdatedStatisticalOperationName(source.getCode(), externalRestObjectsMapper));
                break;
            case STATISTICAL_OPERATION_INSTANCE:
                String identifier = UrnUtils.removePrefix(source.getUrn());
                String[] parts = UrnUtils.splitUrnByDots(identifier);
                target.setName(getUpdatedStatisticalOperationInstanceName(parts[0], parts[1], externalRestObjectsMapper));
                break;
            default:
                break;
        }
    }

    @Override
    public ResourcesStatisticalResourceBase toResourcesExternalItemsStatisticalOperations(List<ExternalItem> sources, List<String> selectedLanguages,
            ExternalRestObjectsMapper externalRestObjectsMapper) {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }
        ResourcesStatisticalResourceBase targets = new ResourcesStatisticalResourceBase();
        for (ExternalItem source : sources) {
            ResourceStatisticalResourceBase target = toResourceExternalItemStatisticalOperations(source, selectedLanguages, externalRestObjectsMapper);
            targets.getResources().add(target);
        }
        targets.setTotal(BigInteger.valueOf(targets.getResources().size()));
        return targets;
    }

    @Override
    public RestException buildRestException(String message) {
        logger.error(message);
        org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
        return new RestException(exception, Response.Status.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResourceStatisticalResourceBase toResourceExternalItemSrm(ExternalItem source, List<String> selectedLanguages) {
        if (source == null) {
            return null;
        }
        ResourceStatisticalResourceBase target = new ResourceStatisticalResourceBase();
        toResourceExternalItemSrm(source, target, selectedLanguages);
        return target;
    }

    @Override
    public void toResourceExternalItemSrm(ExternalItem source, ResourceStatisticalResourceBase target, List<String> selectedLanguages) {
        if (source == null) {
            return;
        }
        toResourceExternalItem(source, srmApiInternalEndpoint, srmInternalWebApplication, target, selectedLanguages);
    }

    @Override
    public ResourcesStatisticalResourceBase toResourcesExternalItemsSrm(Collection<ExternalItem> sources, List<String> selectedLanguages) {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }
        ResourcesStatisticalResourceBase targets = new ResourcesStatisticalResourceBase();
        for (ExternalItem source : sources) {
            ResourceStatisticalResourceBase target = toResourceExternalItemSrm(source, selectedLanguages);
            targets.getResources().add(target);
        }
        targets.setTotal(BigInteger.valueOf(targets.getResources().size()));
        return targets;
    }

    @Override
    public InternationalString toInternationalString(org.siemac.metamac.statistical.resources.core.common.domain.InternationalString sources, List<String> selectedLanguages) {
        if (sources == null) {
            return null;
        }
        InternationalString targets = new InternationalString();
        for (org.siemac.metamac.statistical.resources.core.common.domain.LocalisedString source : sources.getTexts()) {
            if (selectedLanguages.contains(source.getLocale())) {
                LocalisedString target = new LocalisedString();
                target.setLang(source.getLocale());
                target.setValue(source.getLabel());
                targets.getTexts().add(target);
            }
        }
        return targets;
    }

    @Override
    public InternationalString toInternationalString(InternationalString sources, List<String> selectedLanguages) {
        if (sources == null) {
            return null;
        }
        InternationalString targets = new InternationalString();
        for (LocalisedString source : sources.getTexts()) {
            if (selectedLanguages.contains(source.getLang())) {
                targets.getTexts().add(source);
            }
        }
        return targets;
    }

    @Override
    public InternationalString toInternationalString(Map<String, String> sources, List<String> selectedLanguages) {
        if (MapUtils.isEmpty(sources)) {
            return null;
        }
        InternationalString targets = new InternationalString();
        for (String locale : sources.keySet()) {
            if (selectedLanguages.contains(locale)) {
                LocalisedString target = new LocalisedString();
                target.setLang(locale);
                target.setValue(sources.get(locale));
                targets.getTexts().add(target);
            }
        }
        return targets;
    }

    @Override
    public InternationalString toInternationalString(String source, List<String> selectedLanguages) {
        if (source == null) {
            return null;
        }
        InternationalString targets = new InternationalString();
        LocalisedString target = new LocalisedString();
        target.setLang(defaultLanguage);
        target.setValue(source);
        targets.getTexts().add(target);
        return targets;
    }

    @Override
    public Date toDate(DateTime source) {
        return RestCommonUtil.transformDateTimeToDate(source);
    }

    @Override
    public SelectedLanguages toLanguages(List<String> selectedLanguages) {
        SelectedLanguages target = new SelectedLanguages();
        target.getLanguages().addAll(selectedLanguages);
        target.setTotal(BigInteger.valueOf(target.getLanguages().size()));
        return target;
    }

    @Override
    public void toResource(ResourceStatisticalResourceBase source, ResourceStatisticalResourceBase target, List<String> selectedLanguages) {
        if (source == null) {
            return;
        }
        target.setId(source.getId());
        target.setUrn(source.getUrn());
        target.setKind(source.getKind());
        target.setName(toInternationalString(source.getName(), selectedLanguages));
        target.setDescription(toInternationalString(source.getDescription(), selectedLanguages));
        target.setNestedId(source.getNestedId());
        target.setSelfLink(source.getSelfLink());
    }

    private void toResource(org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ResourceInternal source, ResourceStatisticalResourceBase target, List<String> selectedLanguages) {
        if (source == null) {
            return;
        }
        target.setId(source.getId());
        target.setUrn(source.getUrn());
        target.setKind(source.getKind());
        target.setName(toInternationalString(source.getName(), selectedLanguages));
        target.setDescription(toInternationalString(source.getDescription(), selectedLanguages));
        target.setNestedId(source.getNestedId());
        target.setSelfLink(source.getSelfLink());
        if (source.getManagementAppLink() != null) {
            target.setManagementAppLink(RestUtils.createLink(srmInternalWebApplication, source.getManagementAppLink()));
        }
        target.setUrnProvider(source.getUrnProvider());
    }

    @Override
    public ResourceStatisticalResourceBase toResource(ResourceStatisticalResourceBase source, List<String> selectedLanguages) {
        if (source == null) {
            return null;
        }
        ResourceStatisticalResourceBase target = new ResourceStatisticalResourceBase();
        toResource(source, target, selectedLanguages);
        return target;
    }

    private ResourceStatisticalResourceBase toResource(org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ResourceInternal source, List<String> selectedLanguages) {
        if (source == null) {
            return null;
        }

        ResourceStatisticalResourceBase target = new ResourceStatisticalResourceBase();
        toResource(source, target, selectedLanguages);
        return target;
    }

    @Override
    public ResourceLink toResourceLink(String kind, String href) {
        ResourceLink target = new ResourceLink();
        target.setKind(kind);
        target.setHref(href);
        return target;
    }

    // API/[ARTEFACT_TYPE]
    // API/[ARTEFACT_TYPE]/{agencyID}
    // API/[ARTEFACT_TYPE]/{agencyID}/{resourceID}
    // API/[ARTEFACT_TYPE]/{agencyID}/{resourceID}/{version}
    @Override
    public String toResourceLink(String resourceSubpath, String agencyID, String resourceID, String version) {
        String link = RestUtils.createLink(statisticalResourcesApiInternalEndpointV10, resourceSubpath);
        if (agencyID != null) {
            link = RestUtils.createLink(link, agencyID);
            if (resourceID != null) {
                link = RestUtils.createLink(link, resourceID);
                if (version != null) {
                    link = RestUtils.createLink(link, version);
                }
            }
        }
        return link;
    }

    @Override
    public ResourcesStatisticalResourceBase toResources(List<RelatedResource> sources, List<String> selectedLanguages) throws MetamacException {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }
        ResourcesStatisticalResourceBase targets = new ResourcesStatisticalResourceBase();
        for (RelatedResource source : sources) {
            targets.getResources().add(toResource(source, selectedLanguages));
        }
        targets.setTotal(BigInteger.valueOf(targets.getResources().size()));
        return targets;
    }

    @Override
    public ResourcesStatisticalResourceBase toUrlResources(List<String> sources) throws MetamacException {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }
        ResourcesStatisticalResourceBase targets = new ResourcesStatisticalResourceBase();
        for (String source : sources) {
            targets.getResources().add(toUrlResource(source));
        }
        targets.setTotal(BigInteger.valueOf(targets.getResources().size()));
        return targets;
    }

    private ResourceStatisticalResourceBase toUrlResource(String url) {
        if (url == null) {
            return null;
        }
        ResourceStatisticalResourceBase target = new ResourceStatisticalResourceBase();
        target.setKind(StatisticalResourcesRestConstants.KIND_URL);
        target.setVisualizerHtmlLink(url);
        return target;
    }

    @Override
    public ResourceStatisticalResourceBase toResource(RelatedResource source, List<String> selectedLanguages) throws MetamacException {
        if (source == null) {
            return null;
        }

        ExternalRestObjectsMapper externalRestObjectsMapper = new ExternalRestObjectsMapper();

        switch (source.getType()) {
            case DATASET_VERSION:
                return datasetsDo2RestMapper.toResource(source.getDatasetVersion(), selectedLanguages, null, externalRestObjectsMapper);
            case QUERY_VERSION:
                return queriesDo2RestMapper.toResource(source.getQueryVersion(), selectedLanguages, null, externalRestObjectsMapper);
            case PUBLICATION_VERSION:
                return collectionsDo2RestMapper.toResource(source.getPublicationVersion(), selectedLanguages, null, externalRestObjectsMapper);
            case DATASET:
                DatasetVersion datasetVersion = datasetService.retrieveLatestDatasetVersionByDatasetUrn(SERVICE_CONTEXT, source.getDataset().getIdentifiableStatisticalResource().getUrn());
                return datasetsDo2RestMapper.toResourceAsLatest(datasetVersion, selectedLanguages);
            case QUERY:
                QueryVersion queryVersion = queryService.retrieveLatestPublishedQueryVersionByQueryUrn(SERVICE_CONTEXT, source.getQuery().getIdentifiableStatisticalResource().getUrn());
                return queriesDo2RestMapper.toResource(queryVersion, selectedLanguages, null, externalRestObjectsMapper);
            case MULTIDATASET:
                MultidatasetVersion multidatasetVersion = multidatasetService.retrieveLatestPublishedMultidatasetVersionByMultidatasetUrn(SERVICE_CONTEXT,
                        source.getMultidataset().getIdentifiableStatisticalResource().getUrn());
                return multidatasetsDo2RestMapper.toResource(multidatasetVersion, selectedLanguages, null, externalRestObjectsMapper);
            case PUBLICATION:
                PublicationVersion publicationVersion = publicationService.retrieveLatestPublishedPublicationVersionByPublicationUrn(SERVICE_CONTEXT,
                        source.getPublication().getIdentifiableStatisticalResource().getUrn());
                return collectionsDo2RestMapper.toResource(publicationVersion, selectedLanguages, null, externalRestObjectsMapper);
            default:
                logger.error("RelatedResource unsupported: " + source.getType());
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
                throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResourceStatisticalResourceBase toResource(RelatedResourceResult source, List<String> selectedLanguages) throws MetamacException {
        if (source == null) {
            return null;
        }
        switch (source.getType()) {
            case DATASET_VERSION:
                return datasetsDo2RestMapper.toResource(source, selectedLanguages);
            case QUERY_VERSION:
                return queriesDo2RestMapper.toResource(source, selectedLanguages);
            case PUBLICATION_VERSION:
                return collectionsDo2RestMapper.toResource(source, selectedLanguages);
            case MULTIDATASET_VERSION:
                return multidatasetsDo2RestMapper.toResource(source, selectedLanguages);
            case DATASET:
                DatasetVersion datasetVersion = datasetService.retrieveLatestDatasetVersionByDatasetUrn(SERVICE_CONTEXT, source.getUrn());
                return datasetsDo2RestMapper.toResourceAsLatest(datasetVersion, selectedLanguages);
            case QUERY:
                QueryVersion queryVersion = queryService.retrieveLatestPublishedQueryVersionByQueryUrn(SERVICE_CONTEXT, source.getUrn());
                return queriesDo2RestMapper.toResource(queryVersion, selectedLanguages, null, new ExternalRestObjectsMapper());
            default:
                logger.error("RelatedResource unsupported: " + source.getType());
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
                throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ProcStatusType toProcStatusType(ProcStatusEnum source, List<String> selectedLanguages) {
        if (source == null) {
            return null;
        }

        ProcStatusType result;
        try {
            result = LookupUtil.lookup(ProcStatusType.class, source.getName());
        } catch (Exception e) {
            logger.error("ProcStatusEnum unsupported: " + source);
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
            throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }

        return result;
    }

    @Override
    public NextVersionType toNextVersionType(NextVersionTypeEnum source, List<String> selectedLanguages) {
        if (source == null) {
            return null;
        }

        NextVersionType result;
        try {
            result = LookupUtil.lookup(NextVersionType.class, source.getName());
        } catch (Exception e) {
            logger.error("NextVersionTypeEnum unsupported: " + source);
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
            throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }

        return result;
    }

    private void toResourceExternalItem(ExternalItem source, String apiExternalItemBase, String webApplicationExternalItemBase, ResourceStatisticalResourceBase target,
            List<String> selectedLanguages) {
        if (source == null) {
            return;
        }
        target.setId(source.getCode());
        target.setNestedId(source.getCodeNested());
        target.setUrn(source.getUrn());
        target.setKind(source.getType().getValue());
        target.setSelfLink(toResourceLink(target.getKind(), RestUtils.createLink(apiExternalItemBase, source.getUri())));
        target.setManagementAppLink(RestUtils.createLink(webApplicationExternalItemBase, source.getManagementAppUrl()));
        target.setName(toInternationalString(source.getTitle(), selectedLanguages));
    }

    private Dimension toDimension(String datasetVersionUrn, DataStructure dataStructure, DsdDimension source, DimensionVisualisation dimensionVisualisation,
            List<String> effectiveDimensionValuesToData, List<String> selectedLanguages, Set<String> fields, DimensionsFilter dimensionsFilter) throws MetamacException {
        if (source == null) {
            return null;
        }
        Dimension target = new Dimension();
        target.setId(source.getComponentId());
        target.setType(toDimensionType(source.getType()));
        target.setName(toInternationalString(source.getConceptIdentity().getName(), selectedLanguages));
        target.setPluralName(source.getPluralName());
        target.setShowCode(dimensionVisualisation != null && dimensionVisualisation.isShowCode());

        // Dimension values
        target.setDimensionValues(toDimensionValues(datasetVersionUrn, dataStructure, source, dimensionVisualisation, effectiveDimensionValuesToData, selectedLanguages, fields, dimensionsFilter));
        return target;
    }

    private DimensionValues toDimensionValues(String datasetVersionUrn, DataStructure dataStructure, DsdDimension dimension, DimensionVisualisation dimensionVisualisation,
            List<String> effectiveDimensionValuesToData, List<String> selectedLanguages, Set<String> fields, DimensionsFilter dimensionsFilters) throws MetamacException {
        if (dimension == null) {
            return null;
        }
        List<CodeDimension> coverages = datasetService.retrieveCoverageForDatasetVersionDimension(SERVICE_CONTEXT, datasetVersionUrn, dimension.getComponentId());
        if (CollectionUtils.isEmpty(coverages)) {
            return null;
        }
        Map<String, CodeDimension> coveragesById = new HashMap<String, CodeDimension>(coverages.size());
        for (CodeDimension coverage : coverages) {
            coveragesById.put(coverage.getIdentifier(), coverage);
        }

        DimensionValues targets = null;
        if (dimension.getCodelistRepresentationUrn() != null) {
            targets = toEnumeratedDimensionValuesFromCodelist(coveragesById, dimension.getCodelistRepresentationUrn(), dimensionVisualisation, effectiveDimensionValuesToData, selectedLanguages,
                    dimension, fields, dimensionsFilters);
        } else if (dimension.getConceptSchemeRepresentationUrn() != null) {
            targets = toEnumeratedDimensionValuesFromConceptScheme(coveragesById, dataStructure, dimension.getType(), dimension.getConceptSchemeRepresentationUrn(), effectiveDimensionValuesToData,
                    selectedLanguages, fields);
        } else if (dimension.getTextFormatRepresentation() != null) {
            targets = toNonEnumeratedDimensionValuesFromTextFormatType(coverages, dimension.getTextFormatRepresentation(), dimension.getType(), effectiveDimensionValuesToData, selectedLanguages,
                    dimension.getComponentId(), dimensionsFilters);
        } else {
            logger.error("Dimension definition unsupported for dimension: " + dimension.getComponentId());
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
            throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }

        return targets;
    }

    private EnumeratedDimensionValues toEnumeratedDimensionValuesFromCodelist(Map<String, CodeDimension> coveragesById, String codelistUrn, DimensionVisualisation dimensionVisualisation,
            List<String> effectiveDimensionValuesToData, List<String> selectedLanguages, DsdDimension dimension, Set<String> fields, DimensionsFilter dimensionsFilter) throws MetamacException {
        if (codelistUrn == null) {
            return null;
        }
        EnumeratedDimensionValues targets = new EnumeratedDimensionValues();

        // This map contains nodes that are not in the result. If a child of this nodes is in the result, we use this map to put it inside the nearest parent node in result
        Map<String, String> parentsReplacedToVisualisation = new HashMap<String, String>();

        String order = dimensionVisualisation != null ? dimensionVisualisation.getOrder() : null;
        String openness = dimensionVisualisation != null ? dimensionVisualisation.getOpenness() : null;

        boolean includeDescription = containsField(fields, StatisticalResourcesRestInternalConstants.FIELD_INCLUDE_DIMENSION_DESCRIPTION);
        String description = includeDescription ? RestApiConstants.COMMA + SrmRestConstants.FIELD_INCLUDE_DESCRIPTION : StringUtils.EMPTY;

        Codes codes = null;
        if (DsdComponentType.SPATIAL.equals(dimension.getType())) {
            codes = srmRestExternalFacade.retrieveCodesByCodelistUrn(codelistUrn, order, openness, INCLUDE_SPATIAL_FIELDS + description); // note: srm api returns codes in order
        } else {
            codes = srmRestExternalFacade.retrieveCodesByCodelistUrn(codelistUrn, order, openness,
                    SrmRestConstants.FIELD_INCLUDE_OPENNES + RestApiConstants.COMMA + SrmRestConstants.FIELD_INCLUDE_ORDER + description); // note: srm api returns codes in order
        }

        for (CodeResourceInternal code : codes.getCodes()) {
            String id = code.getId();
            boolean skip = false;
            if (effectiveDimensionValuesToData != null) {
                // note: all values in effectiveDimensionValuesToData are always in coverages
                if (!effectiveDimensionValuesToData.contains(id)) {
                    // skip to include only values in query
                    skip = true;
                }
            } else {
                if (!coveragesById.containsKey(id)) {
                    // skip to include only values in coverage
                    skip = true;
                }
            }
            if (skip) {
                updateParentsReplacedToVisualisationWithNotEffectiveDimensionValue(parentsReplacedToVisualisation, code);
                continue;
            }
            if (!checkCodeDimensionsInGeographicalGranularities(code, dimensionsFilter.getGeographicDimensionValuesIds(), dimension)) {
                continue;
            }
            targets.getValues().add(toEnumeratedDimensionValue(code, parentsReplacedToVisualisation, selectedLanguages));
        }
        targets.setTotal(BigInteger.valueOf(targets.getValues().size()));
        return targets;
    }

    private boolean checkCodeDimensionsInGeographicalGranularities(CodeResourceInternal source, List<String> geographicGranularities, DsdDimension dimension) throws MetamacException {
        if (!DsdComponentType.SPATIAL.equals(dimension.getType()) || geographicGranularities == null || geographicGranularities.isEmpty()) {
            return true;
        }
        if (source.getVariableElement() != null && source.getVariableElement() instanceof VariableElementResourceInternal) {
            ItemResourceInternal geographicalGranularity = ((VariableElementResourceInternal) source.getVariableElement()).getGeographicalGranularity();
            return geographicGranularities.contains(geographicalGranularity.getId());
        }

        return true;
    }

    private EnumeratedDimensionValues toEnumeratedDimensionValuesFromConceptScheme(Map<String, CodeDimension> coveragesById, DataStructure dataStructure, DsdComponentType dimensionType,
            String conceptSchemeUrn, List<String> effectiveDimensionValuesToData, List<String> selectedLanguages, Set<String> fields) throws MetamacException {
        if (conceptSchemeUrn == null) {
            return null;
        }
        EnumeratedDimensionValues targets = new EnumeratedDimensionValues();

        Map<String, Integer> showDecimalPrecisionsByUrn = null;
        if (DsdComponentType.MEASURE.equals(dimensionType)) {
            if (dataStructure.getShowDecimalsPrecisions() != null && !CollectionUtils.isEmpty(dataStructure.getShowDecimalsPrecisions().getShowDecimalPrecisions())) {
                showDecimalPrecisionsByUrn = new HashMap<String, Integer>(dataStructure.getShowDecimalsPrecisions().getShowDecimalPrecisions().size());
                for (ShowDecimalPrecision showDecimalPrecision : dataStructure.getShowDecimalsPrecisions().getShowDecimalPrecisions()) {
                    showDecimalPrecisionsByUrn.put(showDecimalPrecision.getConcept().getUrn(), showDecimalPrecision.getShowDecimals());
                }
            }
        }

        // This map contains nodes that are not in the result. If a child of this nodes is in the result, we use this map to put it inside the nearest parent node in result
        Map<String, String> parentsReplacedToVisualisation = new HashMap<String, String>();

        boolean includeDescription = containsField(fields, StatisticalResourcesRestInternalConstants.FIELD_INCLUDE_DIMENSION_DESCRIPTION);
        String conceptField = includeDescription ? SrmRestConstants.FIELD_INCLUDE_DESCRIPTION : null;
        Concepts concepts = srmRestExternalFacade.retrieveConceptsByConceptSchemeByUrn(conceptSchemeUrn, conceptField);
        for (ItemResourceInternal concept : concepts.getConcepts()) {
            String id = concept.getId();
            boolean skip = false;
            if (effectiveDimensionValuesToData != null) {
                // note: all values in effectiveDimensionValuesToData are always in coverages
                if (!effectiveDimensionValuesToData.contains(id)) {
                    // skip to include only values in query
                    skip = true;
                }
            } else {
                if (!coveragesById.containsKey(id)) {
                    // skip to include only values in coverage
                    skip = true;
                }
            }
            if (skip) {
                updateParentsReplacedToVisualisationWithNotEffectiveDimensionValue(parentsReplacedToVisualisation, concept);
                continue;
            }
            targets.getValues().add(toEnumeratedDimensionValue(concept, showDecimalPrecisionsByUrn, parentsReplacedToVisualisation, selectedLanguages, dimensionType));
        }
        targets.setTotal(BigInteger.valueOf(targets.getValues().size()));
        return targets;
    }

    private NonEnumeratedDimensionValues toNonEnumeratedDimensionValuesFromTextFormatType(List<CodeDimension> coverages, TextFormat textFormatType, DsdComponentType dimensionType,
            List<String> effectiveDimensionValuesToData, List<String> selectedLanguages, String dimensionId, DimensionsFilter dimensionsFilter) throws MetamacException {
        if (textFormatType == null) {
            return null;
        }
        NonEnumeratedDimensionValues targets = new NonEnumeratedDimensionValues();

        // Sort
        if (DsdComponentType.TEMPORAL.equals(dimensionType)) {
            DatasetVersionUtils.sortTemporalCodeDimensions(coverages);
        }

        for (CodeDimension coverage : coverages) {
            if (!checkCodeDimensionsInTemporalGranularities(coverage, dimensionsFilter, dimensionId)
                    || (effectiveDimensionValuesToData != null && !effectiveDimensionValuesToData.contains(coverage.getIdentifier()))) {
                // skip to include only values in query
                continue;
            }
            targets.getValues().add(toNonEnumeratedDimensionValue(coverage, dimensionType, selectedLanguages));
        }
        targets.setTotal(BigInteger.valueOf(targets.getValues().size()));
        return targets;
    }

    private boolean checkCodeDimensionsInTemporalGranularities(CodeDimension codeDimension, DimensionsFilter dimensionsFilter, String dimensionId) throws MetamacException {
        if (!"TIME_PERIOD".equals(dimensionId) || ((dimensionsFilter.getTemporalDimensionValuesQueriesIds() == null || dimensionsFilter.getTemporalDimensionValuesQueriesIds().isEmpty())
                && (dimensionsFilter.getTemporalDimensionValuesIds() == null || dimensionsFilter.getTemporalDimensionValuesIds().isEmpty()))) {
            return true;
        }
        IstacTimeGranularityCodeEnum istacTimeGranularityCodeEnum = org.siemac.metamac.statistical.resources.core.enume.utils.IstacTimeUtils.guessTimeGranularity(codeDimension.getIdentifier());
        if (dimensionsFilter.getTemporalDimensionValuesQueriesIds() != null && dimensionsFilter.getTemporalDimensionValuesQueriesIds().contains(istacTimeGranularityCodeEnum.getLabel())
                && (dimensionsFilter.getTemporalDimensionValuesIds() == null || dimensionsFilter.getTemporalDimensionValuesIds().isEmpty())) {
            return true;
        }

        if (dimensionsFilter.getTemporalDimensionValuesIds() != null && dimensionsFilter.getTemporalDimensionValuesIds().contains(istacTimeGranularityCodeEnum.getLabel())
                && (dimensionsFilter.getTemporalDimensionValuesQueriesIds() == null || dimensionsFilter.getTemporalDimensionValuesQueriesIds().isEmpty())) {
            return true;
        }
        return dimensionsFilter.getTemporalDimensionValuesQueriesIds() != null && dimensionsFilter.getTemporalDimensionValuesIds() != null
                && dimensionsFilter.getTemporalDimensionValuesQueriesIds().contains(istacTimeGranularityCodeEnum.getLabel())
                && dimensionsFilter.getTemporalDimensionValuesIds().contains(istacTimeGranularityCodeEnum.getLabel());
    }

    private EnumeratedDimensionValue toEnumeratedDimensionValue(CodeResourceInternal source, Map<String, String> parentsReplacedToVisualisation, List<String> selectedLanguages)
            throws MetamacException {
        if (source == null) {
            return null;
        }
        EnumeratedDimensionValue target = new EnumeratedDimensionValue();
        toResource(source, target, selectedLanguages);
        if (source.getParent() != null) {
            if (!parentsReplacedToVisualisation.containsKey(source.getParent())) {
                target.setVisualisationParent(source.getParent());
            } else {
                target.setVisualisationParent(parentsReplacedToVisualisation.get(source.getParent()));
            }
        }
        target.setVariableElement(toResource(source.getVariableElement(), selectedLanguages));
        if (source.getVariableElement() != null) {
            if (source.getVariableElement() instanceof VariableElementResourceInternal) {
                ItemResourceInternal geographicalGranularity = ((VariableElementResourceInternal) source.getVariableElement()).getGeographicalGranularity();
                target.setGeographicGranularity(toResource(geographicalGranularity, selectedLanguages));
            }
        }
        target.setOpen(source.isOpen());
        return target;
    }

    private EnumeratedDimensionValue toEnumeratedDimensionValue(ItemResourceInternal source, Map<String, Integer> showDecimalPrecisionsByUrn, Map<String, String> effectiveParentVisualisation,
            List<String> selectedLanguages, DsdComponentType dimensionType) throws MetamacException {
        if (source == null) {
            return null;
        }
        EnumeratedDimensionValue target = new EnumeratedDimensionValue();
        toResource(source, target, selectedLanguages);
        if (source.getParent() != null) {
            if (!effectiveParentVisualisation.containsKey(source.getParent())) {
                target.setVisualisationParent(source.getParent());
            } else {
                target.setVisualisationParent(effectiveParentVisualisation.get(source.getParent()));
            }
        }
        if (showDecimalPrecisionsByUrn != null) {
            target.setShowDecimalsPrecision(showDecimalPrecisionsByUrn.get(source.getUrn()));
        }

        // Measure Dimension Type
        if (DsdComponentType.MEASURE.equals(dimensionType)) {
            Concept conceptDetail = srmRestExternalFacade.retrieveConceptByUrn(source.getUrn());
            if (conceptDetail.getQuantity() != null) {
                target.setMeasureQuantity(toQuantity(conceptDetail.getQuantity(), selectedLanguages));
            }
        }
        return target;
    }

    private MeasureQuantity toQuantity(Quantity source, List<String> selectedLanguages) throws MetamacException {
        if (source == null) {
            return null;
        }

        MeasureQuantity target = new MeasureQuantity();

        org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ItemResource unitCodeItemResource = new org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ItemResource();
        toResource(source.getUnitCode(), unitCodeItemResource, selectedLanguages);
        target.setUnitCode(unitCodeItemResource);
        org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ItemResource unitMultiplierItemResource = new org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ItemResource();
        toResource(source.getUnitMultiplier(), unitMultiplierItemResource, selectedLanguages);
        target.setUnitMultiplier(unitMultiplierItemResource);
        return target;
    }

    private NonEnumeratedDimensionValue toNonEnumeratedDimensionValue(CodeDimension source, DsdComponentType dimensionType, List<String> selectedLanguages) throws MetamacException {
        if (source == null) {
            return null;
        }
        NonEnumeratedDimensionValue target = new NonEnumeratedDimensionValue();
        target.setId(source.getIdentifier());
        if (DsdComponentType.TEMPORAL.equals(dimensionType)) {
            Map<String, String> title = translationService.retrieveTimeTranslation(SERVICE_CONTEXT, source.getIdentifier());
            target.setName(toInternationalString(title, selectedLanguages));
            IstacTimeGranularityEnum timeGranularity = IstacTimeUtils.guessTimeGranularity(source.getIdentifier());
            target.setTemporalGranularity(timeGranularity == null ? null : timeGranularity.name());
        } else {
            target.setName(toInternationalString(source.getTitle(), selectedLanguages));
        }
        return target;
    }

    /**
     * Update map with parent visualisation.
     * This map contains nodes that are not in the result. If a child of this nodes is in the result, we use this map to put it inside the nearest parent node in result
     * The parent visualisation will be the nearest parent in result
     */
    private void updateParentsReplacedToVisualisationWithNotEffectiveDimensionValue(Map<String, String> parentsReplacedToVisualisation, ItemResourceInternal code) {
        String parentVisualisationEffective = null;
        if (code.getParent() == null) {
            parentVisualisationEffective = null;
        } else {
            if (!parentsReplacedToVisualisation.containsKey(code.getParent())) {
                parentVisualisationEffective = code.getParent();
            } else {
                parentVisualisationEffective = parentsReplacedToVisualisation.get(code.getParent());
            }
        }
        parentsReplacedToVisualisation.put(code.getUrn(), parentVisualisationEffective);
    }

    private DimensionType toDimensionType(DsdComponentType source) {
        switch (source) {
            case OTHER:
                return DimensionType.DIMENSION;
            case SPATIAL:
                return DimensionType.GEOGRAPHIC_DIMENSION;
            case TEMPORAL:
                return DimensionType.TIME_DIMENSION;
            case MEASURE:
                return DimensionType.MEASURE_DIMENSION;
            default:
                logger.error("DsdComponentType unsupported: " + source);
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
                throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }
    }

    private Attribute toAttribute(String datasetVersionUrn, DsdAttribute source, DsdProcessorResult dsdProcessorResult, List<String> datasetDimensionsOrdered, List<String> selectedLanguages)
            throws MetamacException {
        if (source == null) {
            return null;
        }
        Attribute target = new Attribute();
        target.setId(source.getComponentId());
        target.setName(toInternationalString(source.getConceptIdentity().getName(), selectedLanguages));

        if (source.getAttributeRelationship().getNone() != null) {
            target.setAttachmentLevel(AttributeAttachmentLevelType.DATASET);
        } else if (!CollectionUtils.isEmpty(source.getAttributeRelationship().getDimensions())) {
            target.setAttachmentLevel(AttributeAttachmentLevelType.DIMENSION);
            List<String> attributeDimensions = source.getAttributeRelationship().getDimensions();
            target.setDimensions(toAttributeDimensions(attributeDimensions, datasetDimensionsOrdered));
        } else if (source.getAttributeRelationship().getGroup() != null) {
            target.setAttachmentLevel(AttributeAttachmentLevelType.DIMENSION);
            List<String> attributeDimensions = dsdProcessorResult.getGroups().get(source.getAttributeRelationship().getGroup());
            target.setDimensions(toAttributeDimensions(attributeDimensions, datasetDimensionsOrdered));
        } else if (source.getAttributeRelationship().getPrimaryMeasure() != null) {
            target.setAttachmentLevel(AttributeAttachmentLevelType.PRIMARY_MEASURE);
        } else {
            logger.error("AttributeRelationship unsupported to attributeId " + source.getComponentId());
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
            throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }

        // Specific type
        target.setType(ComponentType.valueOf(source.getType().name()));

        // Attributes values
        target.setAttributeValues(toAttributeValues(datasetVersionUrn, source, selectedLanguages));
        return target;
    }

    private AttributeValues toAttributeValues(String datasetVersionUrn, DsdAttribute attribute, List<String> selectedLanguages) throws MetamacException {
        if (attribute == null) {
            return null;
        }
        if (attribute.getEnumeratedRepresentationUrn() == null && !DsdComponentType.TEMPORAL.equals(attribute.getType())) {
            // Translate only representations or time attributes
            return null;
        }
        List<AttributeValue> coverages = datasetService.retrieveCoverageForDatasetVersionAttribute(SERVICE_CONTEXT, datasetVersionUrn, attribute.getComponentId());
        if (CollectionUtils.isEmpty(coverages)) {
            return null;
        }
        Map<String, AttributeValue> coveragesById = new HashMap<String, AttributeValue>(coverages.size());
        for (AttributeValue coverage : coverages) {
            coveragesById.put(coverage.getIdentifier(), coverage);
        }

        AttributeValues targets = null;
        if (attribute.getCodelistRepresentationUrn() != null) {
            targets = toEnumeratedAttributeValuesFromCodelist(coveragesById, attribute.getCodelistRepresentationUrn(), attribute.getType(), selectedLanguages);
        } else if (attribute.getConceptSchemeRepresentationUrn() != null) {
            targets = toEnumeratedAttributeValuesFromConceptScheme(coveragesById, attribute.getConceptSchemeRepresentationUrn(), attribute.getType(), selectedLanguages);
        } else if (DsdComponentType.TEMPORAL.equals(attribute.getType())) {
            targets = toNonEnumeratedAttributeValuesFromTextFormatType(coverages, attribute.getTextFormatRepresentation(), attribute.getType(), selectedLanguages);
        } else {
            logger.error("Attribute definition unsupported for attribute: " + attribute.getComponentId());
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
            throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }
        return targets;
    }

    private EnumeratedAttributeValues toEnumeratedAttributeValuesFromCodelist(Map<String, AttributeValue> coveragesById, String codelistUrn, DsdComponentType attributeType,
            List<String> selectedLanguages) throws MetamacException {
        if (codelistUrn == null) {
            return null;
        }
        EnumeratedAttributeValues targets = new EnumeratedAttributeValues();
        Codes codes = srmRestExternalFacade.retrieveCodesByCodelistUrn(codelistUrn, null, null, null);
        for (CodeResourceInternal code : codes.getCodes()) {
            String id = code.getId();
            if (!coveragesById.containsKey(id)) {
                // skip to include only values in coverage
                continue;
            }
            targets.getValues().add(toEnumeratedAttributeValue(code, attributeType, selectedLanguages));
        }
        targets.setTotal(BigInteger.valueOf(targets.getValues().size()));
        return targets;
    }

    private EnumeratedAttributeValues toEnumeratedAttributeValuesFromConceptScheme(Map<String, AttributeValue> coveragesById, String conceptSchemeUrn, DsdComponentType attributeType,
            List<String> selectedLanguages) throws MetamacException {
        if (conceptSchemeUrn == null) {
            return null;
        }
        EnumeratedAttributeValues targets = new EnumeratedAttributeValues();
        Concepts concepts = srmRestExternalFacade.retrieveConceptsByConceptSchemeByUrn(conceptSchemeUrn, null);
        for (ItemResourceInternal concept : concepts.getConcepts()) {
            String id = concept.getId();
            if (!coveragesById.containsKey(id)) {
                // skip to include only values in coverage
                continue;
            }
            targets.getValues().add(toEnumeratedAttributeValue(concept, attributeType, selectedLanguages));
        }
        targets.setTotal(BigInteger.valueOf(targets.getValues().size()));
        return targets;
    }

    private EnumeratedAttributeValue toEnumeratedAttributeValue(ItemResourceInternal source, DsdComponentType attributeType, List<String> selectedLanguages) throws MetamacException {
        if (source == null) {
            return null;
        }
        EnumeratedAttributeValue target = new EnumeratedAttributeValue();
        toResource(source, target, selectedLanguages);

        // Measure Attribute Type
        if (DsdComponentType.MEASURE.equals(attributeType)) {
            Concept conceptDetail = srmRestExternalFacade.retrieveConceptByUrn(source.getUrn());
            if (conceptDetail.getQuantity() != null) {
                target.setMeasureQuantity(toQuantity(conceptDetail.getQuantity(), selectedLanguages));
            }
        }

        return target;
    }

    private NonEnumeratedAttributeValues toNonEnumeratedAttributeValuesFromTextFormatType(List<AttributeValue> coverages, TextFormat textFormatType, DsdComponentType attributeType,
            List<String> selectedLanguages) throws MetamacException {
        if (textFormatType == null) {
            return null;
        }
        NonEnumeratedAttributeValues targets = new NonEnumeratedAttributeValues();
        for (AttributeValue coverage : coverages) {
            targets.getValues().add(toNonEnumeratedAttributeValue(coverage, attributeType, selectedLanguages));
        }
        targets.setTotal(BigInteger.valueOf(targets.getValues().size()));
        return targets;
    }

    private NonEnumeratedAttributeValue toNonEnumeratedAttributeValue(AttributeValue source, DsdComponentType attributeType, List<String> selectedLanguages) throws MetamacException {
        if (source == null) {
            return null;
        }
        NonEnumeratedAttributeValue target = new NonEnumeratedAttributeValue();
        target.setId(source.getIdentifier());
        if (DsdComponentType.TEMPORAL.equals(attributeType)) {
            Map<String, String> title = translationService.retrieveTimeTranslation(SERVICE_CONTEXT, source.getIdentifier());
            target.setName(toInternationalString(title, selectedLanguages));
        } else {
            logger.error("Attribute must be time to add values in metadata: " + source.getDsdComponentId());
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
            throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }
        return target;
    }

    private AttributeDimensions toAttributeDimensions(List<String> attributeDimensions, List<String> datasetDimensionsOrdered) throws MetamacException {
        if (CollectionUtils.isEmpty(attributeDimensions)) {
            return null;
        }
        List<String> attributeDimensionsOrdered = toAttributeDimensionsOrdered(datasetDimensionsOrdered, attributeDimensions);
        AttributeDimensions targets = new AttributeDimensions();
        for (String attributeDimensionOrdered : attributeDimensionsOrdered) {
            AttributeDimension target = new AttributeDimension();
            target.setDimensionId(attributeDimensionOrdered);
            targets.getDimensions().add(target);
        }
        targets.setTotal(BigInteger.valueOf(targets.getDimensions().size()));
        return targets;
    }

    private VersionRationaleTypes toVersionRationaleTypes(List<VersionRationaleType> sources, List<String> selectedLanguages) {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }
        VersionRationaleTypes targets = new VersionRationaleTypes();
        for (VersionRationaleType source : sources) {
            targets.getVersionRationaleTypes().add(toVersionRationaleType(source.getValue()));
        }
        targets.setTotal(BigInteger.valueOf(targets.getVersionRationaleTypes().size()));
        return targets;
    }

    private StatisticalResourceType toStatisticalResourceType(StatisticalResourceTypeEnum source) {
        if (source == null) {
            return null;
        }
        switch (source) {
            case DATASET:
                return StatisticalResourceType.DATASET;
            case QUERY:
                return StatisticalResourceType.QUERY;
            case COLLECTION:
                return StatisticalResourceType.COLLECTION;
            case MULTIDATASET:
                return StatisticalResourceType.MULTIDATASET;
            default:
                logger.error("StatisticalResourceTypeEnum unsupported: " + source);
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
                throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }
    }

    private org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.VersionRationaleType toVersionRationaleType(VersionRationaleTypeEnum source) {
        if (source == null) {
            return null;
        }
        switch (source) {
            case MAJOR_NEW_RESOURCE:
                return org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.VersionRationaleType.MAJOR_NEW_RESOURCE;
            case MAJOR_ESTIMATORS:
                return org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.VersionRationaleType.MAJOR_ESTIMATORS;
            case MAJOR_CATEGORIES:
                return org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.VersionRationaleType.MAJOR_CATEGORIES;
            case MAJOR_VARIABLES:
                return org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.VersionRationaleType.MAJOR_VARIABLES;
            case MAJOR_OTHER:
                return org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.VersionRationaleType.MAJOR_OTHER;
            case MINOR_ERRATA:
                return org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.VersionRationaleType.MINOR_ERRATA;
            case MINOR_METADATA:
                return org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.VersionRationaleType.MINOR_METADATA;
            case MINOR_DATA_UPDATE:
                return org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.VersionRationaleType.MINOR_DATA_UPDATE;
            case MINOR_SERIES_UPDATE:
                return org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.VersionRationaleType.MINOR_SERIES_UPDATE;
            case MINOR_OTHER:
                return org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.VersionRationaleType.MINOR_OTHER;
            default:
                logger.error("VersionRationaleTypeEnum unsupported: " + source);
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
                throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Build dimensions selected, with codes selected or all codes if codes are not selected to one dimension
     */
    private Map<String, List<String>> buildDimensionsSelectedWithValues(DatasetVersion source, Map<String, List<String>> dimensionsSelected, List<String> dimensions) throws MetamacException {
        Map<String, List<String>> dimensionsCodesSelected = new HashMap<String, List<String>>();
        for (String dimension : dimensions) {
            List<String> dimensionValues = dimensionsSelected.get(dimension);
            List<String> dimensionValuesSelected = new ArrayList<String>();
            if (dimensionValues == null) {
                // if dimension is not selected in query, retrieve all codes from coverage BUT if the filter returns empty list, we do that
                List<CodeDimension> codeDimensions = datasetService.retrieveCoverageForDatasetVersionDimension(SERVICE_CONTEXT, source.getSiemacMetadataStatisticalResource().getUrn(), dimension);
                for (CodeDimension codeDimension : codeDimensions) {
                    dimensionValuesSelected.add(codeDimension.getIdentifier());
                }
            } else {
                dimensionValuesSelected.addAll(dimensionValues);
            }
            dimensionsCodesSelected.put(dimension, dimensionValuesSelected);
        }
        return dimensionsCodesSelected;
    }

    private void toDataDimensionRepresentations(List<String> datasetDimensions, Map<String, List<String>> dimensionsCodesSelectedEffective, Data target) {
        DimensionRepresentations targets = new DimensionRepresentations();
        for (String dimension : datasetDimensions) {
            DimensionRepresentation representation = new DimensionRepresentation();
            representation.setDimensionId(dimension);
            representation.setRepresentations(new CodeRepresentations());

            int valuesSize = 0;
            for (String dimensionValue : dimensionsCodesSelectedEffective.get(dimension)) {
                CodeRepresentation codeRepresentation = new CodeRepresentation();
                codeRepresentation.setCode(dimensionValue);
                codeRepresentation.setIndex(valuesSize);
                representation.getRepresentations().getRepresentations().add(codeRepresentation);
                valuesSize++;
            }
            representation.getRepresentations().setTotal(BigInteger.valueOf(representation.getRepresentations().getRepresentations().size()));
            targets.getDimensions().add(representation);
        }
        targets.setTotal(BigInteger.valueOf(targets.getDimensions().size()));
        target.setDimensions(targets);
    }

    /**
     * Retrieve observations of selected codes
     */
    private void toDataObservationsAndAttributeWithObservationAttachmentLevel(DatasetVersion source, DsdProcessorResult dsdProcessorResult, List<String> dimensions,
            Map<String, List<String>> dimensionsSelected, Map<String, List<String>> dimensionsCodesSelectedEffective, Data target, DimensionsFilter dimensionsFilter) throws Exception {

        if (MapUtils.isEmpty(dimensionsCodesSelectedEffective)) {
            return;
        }

        // Search observations and attributes in repository
        List<ConditionDimensionDto> conditions = generateConditions(dimensionsSelected);
        Map<String, ObservationExtendedDto> observations = dimensionsFilter == null
                ? datasetRepositoriesServiceFacade.findObservationsExtendedByDimensions(source.getDatasetRepositoryId(), conditions)
                : getObservationsFiltered(datasetRepositoriesServiceFacade.findObservationsExtendedByDimensions(source.getDatasetRepositoryId(), conditions), dimensionsFilter);

        // Build data (observations and attribute in observation attachment level)
        DataProcessorForObservation dataProcessor = new DataProcessorForObservation(observations);
        toDataCommon(dimensions, dimensionsCodesSelectedEffective, dataProcessor);

        // Observations
        target.setObservations(dataProcessor.getDataObservationsForResponse());

        // Attributes
        for (DsdAttribute dsdAttribute : dsdProcessorResult.getAttributes()) {
            if (dsdAttribute.getAttributeRelationship().getPrimaryMeasure() != null) {
                String attributeId = dsdAttribute.getComponentId();
                String dataAttributeValue = dataProcessor.getDataAttributeForResponse(attributeId);
                if (dataAttributeValue != null) {
                    DataAttribute dataAttribute = new DataAttribute();
                    dataAttribute.setId(attributeId);
                    dataAttribute.setValue(dataAttributeValue);
                    target.getAttributes().getAttributes().add(dataAttribute);
                }
            }
        }
    }

    private Map<String, ObservationExtendedDto> getObservationsFiltered(Map<String, ObservationExtendedDto> observations, DimensionsFilter dimensionsFilter) {
        Map<String, ObservationExtendedDto> observationsFiltered = new HashMap<String, ObservationExtendedDto>();
        for (Map.Entry<String, ObservationExtendedDto> entry : observations.entrySet()) {
            boolean checkTemporalId = !checkHaveDimension(entry.getValue().getCodesDimension(), dimensionsFilter.getTemporalDimensionId());
            boolean checkGreographicId = !checkHaveDimension(entry.getValue().getCodesDimension(), dimensionsFilter.getGeographicDimensionId());
            for (CodeDimensionDto codeDimension : entry.getValue().getCodesDimension()) {
                if (dimensionsFilter != null && codeDimension.getDimensionId().equals(dimensionsFilter.getGeographicDimensionId()) && dimensionsFilter.getGeographicDimensionValuesIds() != null
                        && dimensionsFilter.getGeographicDimensionValuesIds().contains(codeDimension.getCodeDimensionId())) {
                    checkGreographicId = true;
                }
                if (dimensionsFilter != null && codeDimension.getDimensionId().equals(dimensionsFilter.getTemporalDimensionId()) && dimensionsFilter.getTemporalDimensionValuesIds() != null
                        && dimensionsFilter.getTemporalDimensionValuesIds().contains(codeDimension.getCodeDimensionId())) {
                    checkTemporalId = true;
                }
                if (checkGreographicId && checkTemporalId) {
                    observationsFiltered.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return observationsFiltered;
    }

    private boolean checkHaveDimension(List<CodeDimensionDto> codesDimension, String dimensionId) {
        for (CodeDimensionDto codeDimension : codesDimension) {
            if (codeDimension.getDimensionId().equals(dimensionId)) {
                return true;
            }
        }
        return false;
    }

    private void toDataAttributesWithDatasetAndDimensionAttachmenteLevel(DsdProcessorResult dsdProcessorResult, String datasetRepositoryId, List<String> datasetDimensionsOrdered,
            Map<String, List<String>> dimensionsCodesSelectedEffective, DataAttributes targets, List<String> selectedLanguages) throws Exception {

        List<DsdAttribute> sources = dsdProcessorResult.getAttributes();
        if (CollectionUtils.isEmpty(sources) || MapUtils.isEmpty(dimensionsCodesSelectedEffective)) {
            return;
        }

        for (DsdAttribute source : sources) {
            String attributeId = source.getComponentId();
            TemporalAttributeValues temporalAttributeValues = new TemporalAttributeValues();

            if (source.getAttributeRelationship().getNone() != null) {

                if (isTextFormatAttributeMultilingual(source)) {

                    temporalAttributeValues.setInternationalStringValues(datasetRepositoriesServiceFacade.findAttributeInstancesValues(datasetRepositoryId, attributeId));
                    temporalAttributeValues.setMultilingualValue(true);
                } else {
                    temporalAttributeValues.setValues(toDataAttributeWithDatasetAttachmentLevel(datasetRepositoryId, attributeId));
                }

            } else if (!CollectionUtils.isEmpty(source.getAttributeRelationship().getDimensions())) {
                List<String> attributeDimensions = source.getAttributeRelationship().getDimensions();
                temporalAttributeValues = toDataAttributeWithDimensionAttachmentLevel(attributeId, attributeDimensions, datasetDimensionsOrdered, dimensionsCodesSelectedEffective, datasetRepositoryId,
                        isTextFormatAttributeMultilingual(source));
            } else if (source.getAttributeRelationship().getGroup() != null) {
                List<String> attributeDimensions = dsdProcessorResult.getGroups().get(source.getAttributeRelationship().getGroup());
                temporalAttributeValues = toDataAttributeWithDimensionAttachmentLevel(attributeId, attributeDimensions, datasetDimensionsOrdered, dimensionsCodesSelectedEffective, datasetRepositoryId,
                        isTextFormatAttributeMultilingual(source));
            } else if (source.getAttributeRelationship().getPrimaryMeasure() != null) {
                // These attributes are transformed with observations
            } else {
                logger.error("AttributeRelationship unsupported to attributeId " + source.getComponentId());
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
                throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
            }
            if (temporalAttributeValues.hasValues()) {
                getAttributes(targets, attributeId, temporalAttributeValues, selectedLanguages);
            }
        }
    }

    private void getAttributes(DataAttributes targets, String attributeId, TemporalAttributeValues temporalAttributeValues, List<String> selectedLanguages) {
        if (temporalAttributeValues.isMultilingualValue()) {
            getInternationalStringAttributes(targets, attributeId, temporalAttributeValues, selectedLanguages);
        } else {
            getStringAttributes(targets, attributeId, temporalAttributeValues);
        }
    }

    private void getStringAttributes(DataAttributes targets, String attributeId, TemporalAttributeValues temporalAttributeValues) {
        if (temporalAttributeValues.getValues() != null && temporalAttributeValues.getValues().get(0) != null) {
            DataAttribute target = new DataAttribute();
            target.setId(attributeId);
            target.setValue(temporalAttributeValues.getValues().get(0));
            targets.getAttributes().add(target);
        }
    }

    private void getInternationalStringAttributes(DataAttributes targets, String attributeId, TemporalAttributeValues temporalAttributeValues, List<String> selectedLanguages) {
        if (!temporalAttributeValues.getInternationalStringValues().isEmpty()) {
            DataInternationalAttribute target = new DataInternationalAttribute();
            target.setId(attributeId);
            for (InternationalStringDto attributeValue : temporalAttributeValues.getInternationalStringValues()) {
                target.getValues().addAll(getNonEnumeratedAttributeDataMultilingualValues(Arrays.asList(attributeValue), selectedLanguages));

            }
            targets.getInternationalAttributes().add(target);
        }
    }

    private List<InternationalString> getNonEnumeratedAttributeDataMultilingualValues(List<InternationalStringDto> values, List<String> selectedLanguages) {
        List<InternationalString> dataMultilingualValues = new ArrayList<>();

        for (InternationalStringDto attributeMultilingualValue : values) {
            dataMultilingualValues.add(toInternationalString(attributeMultilingualValue, selectedLanguages));
        }

        return dataMultilingualValues;
    }

    private boolean isTextFormatAttributeMultilingual(DsdAttribute dsdAttribute) {
        return dsdAttribute.getTextFormatRepresentation() != null && DataType.INTERNATIONAL_STRING.equals(dsdAttribute.getTextFormatRepresentation().getTextType());
    }

    private String toDataAttributeWithDatasetAttachmentLevel(String datasetId, String attributeId) throws Exception {

        // Find attributes
        List<AttributeInstanceDto> sources = datasetRepositoriesServiceFacade.findAttributesInstancesWithDatasetAttachmentLevel(datasetId, attributeId);
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }
        AttributeInstanceDto source = sources.get(0); // must be only one
        return toAttributeInstanceValueToData(source);
    }

    private TemporalAttributeValues toDataAttributeWithDimensionAttachmentLevel(String attributeId, List<String> attributeDimensions, List<String> datasetDimensionsOrdered,
            Map<String, List<String>> dimensionsCodesSelectedEffective, String datasetId, boolean isMultilingualAttribute) throws Exception {

        TemporalAttributeValues temporalAttributeValues = new TemporalAttributeValues();

        DataProcessorForAttributeWithDimensionAttachmentLevel dataProcessor = getDataProcessorForAttributeWithDimensionAttachmentLevel(attributeId, attributeDimensions, datasetDimensionsOrdered,
                dimensionsCodesSelectedEffective, datasetId);

        if (dataProcessor == null) {
            return temporalAttributeValues;
        }

        if (isMultilingualAttribute) {
            temporalAttributeValues.setInternationalStringValues(dataProcessor.getDataMultilingualAttributeForResponse());
            temporalAttributeValues.setMultilingualValue(true);
        } else {
            temporalAttributeValues.setValues(dataProcessor.getDataAttributeForResponse());
        }

        return temporalAttributeValues;
    }

    private DataProcessorForAttributeWithDimensionAttachmentLevel getDataProcessorForAttributeWithDimensionAttachmentLevel(String attributeId, List<String> attributeDimensions,
            List<String> datasetDimensionsOrdered, Map<String, List<String>> dimensionsCodesSelectedEffective, String datasetId) throws Exception {

        // Find attributes
        List<AttributeInstanceDto> sources = datasetRepositoriesServiceFacade.findAttributesInstancesWithDimensionAttachmentLevelDenormalized(datasetId, attributeId, dimensionsCodesSelectedEffective);
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }

        List<String> attributeDimensionsOrdered = toAttributeDimensionsOrdered(datasetDimensionsOrdered, attributeDimensions);
        Map<String, AttributeInstanceDto> attributesByCodeDimensions = buildMapToAttributesWithDimensionAttachmentLevelDenormalizedByCodeDimensions(attributeDimensionsOrdered, sources);

        // Build data
        DataProcessorForAttributeWithDimensionAttachmentLevel dataProcessor = new DataProcessorForAttributeWithDimensionAttachmentLevel(attributesByCodeDimensions);
        toDataCommon(attributeDimensionsOrdered, dimensionsCodesSelectedEffective, dataProcessor);

        return dataProcessor;
    }

    private List<ConditionDimensionDto> generateConditions(Map<String, List<String>> dimensions) {
        List<ConditionDimensionDto> conditionDimensionDtos = new ArrayList<ConditionDimensionDto>();
        for (Map.Entry<String, List<String>> entry : dimensions.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            ConditionDimensionDto conditionDimensionDto = new ConditionDimensionDto();
            conditionDimensionDto.setDimensionId(entry.getKey());
            for (String value : entry.getValue()) {
                conditionDimensionDto.getCodesDimension().add(value);
            }
            conditionDimensionDtos.add(conditionDimensionDto);
        }
        return conditionDimensionDtos;
    }

    private DimensionsId toDimensionsId(org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DimensionReferences sources) {
        if (sources == null) {
            DimensionsId dimensionsId = new DimensionsId();
            dimensionsId.setTotal(BigInteger.valueOf(0L));
            return dimensionsId;
        }
        DimensionsId targets = new DimensionsId();
        for (String source : sources.getDimensions()) {
            targets.getDimensionIds().add(source);
        }
        targets.setTotal(BigInteger.valueOf(targets.getDimensionIds().size()));
        return targets;
    }

    private void toCommonMetadata(SiemacMetadataStatisticalResource source, StatisticalResourceBase target, List<String> selectedLanguages) {
        if (source == null) {
            return;
        }
        if (source.getCommonMetadata() == null) {
            return;
        }
        String configurationId = source.getCommonMetadata().getCode();
        Configuration configuration = commonMetadataRestExternalFacade.retrieveConfiguration(configurationId);
        Agency agency = srmRestExternalFacade.retrieveAgency(configuration.getContact().getUrn());
        target.setRightsHolder(toOrganisation(agency, selectedLanguages));
        target.setLicense(toInternationalString(configuration.getLicense(), selectedLanguages));
    }

    private class OrderingStackElement {

        private String codeId = null;
        private int    dimNum = -1;

        public OrderingStackElement(String codeId, int dimNum) {
            super();
            this.codeId = codeId;
            this.dimNum = dimNum;
        }

        public String getCodeId() {
            return codeId;
        }

        public int getDimNum() {
            return dimNum;
        }
    }

    private List<String> toAttributeDimensionsOrdered(List<String> datasetDimensionsOrdered, List<String> attributeDimensions) {
        List<String> attributeDimensionsOrdered = new ArrayList<String>(attributeDimensions.size());
        for (String datasetDimension : datasetDimensionsOrdered) {
            if (attributeDimensions.contains(datasetDimension)) {
                attributeDimensionsOrdered.add(datasetDimension);
            }
        }
        return attributeDimensionsOrdered;
    }

    /**
     * Retrieves attribute instance value in locale of dataset-repository. NOTE: This value will be escaped to not contain separator in DATA
     */
    private String toAttributeInstanceValueToData(AttributeInstanceBasicDto attributeDto) {
        String attributeValue = attributeDto.getValue().getLocalisedLabel(StatisticalResourcesConstants.DEFAULT_DATA_REPOSITORY_LOCALE); // all attributes has only one locale
        return AttributesUtils.escapeValueToData(attributeValue);
    }

    private Map<String, AttributeInstanceDto> buildMapToAttributesWithDimensionAttachmentLevelDenormalizedByCodeDimensions(List<String> attributeDimensionsOrdered,
            List<AttributeInstanceDto> attributeInstances) {
        Map<String, AttributeInstanceDto> attributesByCodeDimensions = new HashMap<String, AttributeInstanceDto>(attributeInstances.size());
        for (AttributeInstanceDto attributeInstanceDto : attributeInstances) {
            StringBuilder key = new StringBuilder();
            for (int i = 0; i < attributeDimensionsOrdered.size(); i++) {
                String dimensionId = attributeDimensionsOrdered.get(i);
                String codeDimension = attributeInstanceDto.getCodesByDimension().get(dimensionId).get(0); // only to denormalized!!
                key.append(codeDimension);
                if (i != attributeDimensionsOrdered.size() - 1) {
                    key.append(KEY_DIMENSIONS_SEPARATOR);
                }
            }
            attributesByCodeDimensions.put(key.toString(), attributeInstanceDto);
        }
        return attributesByCodeDimensions;
    }

    private void toDataCommon(List<String> dimensions, Map<String, List<String>> dimensionsCodesSelectedEffective, DataProcessor dataProcessor) throws Exception {

        // Build data
        Stack<OrderingStackElement> stack = new Stack<OrderingStackElement>();
        stack.push(new OrderingStackElement(StringUtils.EMPTY, -1));
        ArrayList<String> entryId = new ArrayList<String>(dimensions.size());
        for (int i = 0; i < dimensions.size(); i++) {
            entryId.add(i, StringUtils.EMPTY);
        }

        int lastDimension = dimensions.size() - 1;
        while (stack.size() > 0) {
            // POP
            OrderingStackElement elem = stack.pop();
            int elemDimension = elem.getDimNum();
            String elemCode = elem.getCodeId();

            // The first time we don't need a hash (#)
            if (elemDimension != -1) {
                entryId.set(elemDimension, elemCode);
            }

            // The entry is complete
            if (elemDimension == lastDimension) {
                String id = StringUtils.join(entryId, KEY_DIMENSIONS_SEPARATOR);

                // We have the full entry here
                dataProcessor.processFullEntry(id);
                entryId.set(elemDimension, StringUtils.EMPTY);
            } else {
                String dimension = dimensions.get(elemDimension + 1);
                List<String> dimensionValues = dimensionsCodesSelectedEffective.get(dimension);
                for (int i = dimensionValues.size() - 1; i >= 0; i--) {
                    OrderingStackElement temp = new OrderingStackElement(dimensionValues.get(i), elemDimension + 1);
                    stack.push(temp);
                }
            }
        }
    }

    private abstract class DataProcessor {

        @SuppressWarnings("rawtypes")
        public String transformDataListToDataResponse(Iterator iterator) {
            return StringUtils.join(iterator, StatisticalResourcesRestInternalConstants.DATA_SEPARATOR);
        }

        protected abstract void processFullEntry(String key);
    }

    private class DataProcessorForAttributeWithDimensionAttachmentLevel extends DataProcessor {

        private final Map<String, AttributeInstanceDto> attributesByCodeDimensions;
        private final List<AttributeInstanceDto>        targets;

        public DataProcessorForAttributeWithDimensionAttachmentLevel(Map<String, AttributeInstanceDto> attributesByCodeDimensions) {
            this.attributesByCodeDimensions = attributesByCodeDimensions;
            targets = new ArrayList<AttributeInstanceDto>();
        }

        @Override
        protected void processFullEntry(String key) {
            AttributeInstanceDto attributeInstanceDto = attributesByCodeDimensions.get(key);
            targets.add(attributeInstanceDto);
        }

        public String getDataAttributeForResponse() {
            Iterator<String> iterator = new DataAttributeWithDimensionAttachmentLevelIterator(targets);
            return transformDataListToDataResponse(iterator);
        }

        public List<InternationalStringDto> getDataMultilingualAttributeForResponse() {
            List<InternationalStringDto> attributeDimensionValues = new ArrayList<>();
            for (AttributeInstanceDto attributeInstanceDto : targets) {
                if (attributeInstanceDto != null) {
                    attributeDimensionValues.add(InternationalStringUtils.copy(attributeInstanceDto.getValue(), true));
                } else {
                    attributeDimensionValues.add(null);
                }
            }

            return attributeDimensionValues;

        }
    }

    private class DataProcessorForObservation extends DataProcessor {

        private final Map<String, ObservationExtendedDto> sources;
        private final List<ObservationExtendedDto>        targets;

        public DataProcessorForObservation(Map<String, ObservationExtendedDto> observations) {
            sources = observations;
            targets = new ArrayList<ObservationExtendedDto>();
        }

        @Override
        public void processFullEntry(String key) {
            ObservationExtendedDto observationDto = sources.get(key);
            targets.add(observationDto);
        }

        public String getDataObservationsForResponse() {
            Iterator<String> iterator = new DataObservationIterator(targets);
            return transformDataListToDataResponse(iterator);
        }

        public String getDataAttributeForResponse(String attributeId) {
            DataAttributeObservationIterator iterator = new DataAttributeObservationIterator(targets, attributeId);
            String value = transformDataListToDataResponse(iterator);
            if (!iterator.isAnyObservationHasAttribute()) {
                value = null;
            }
            return value;
        }
    }

    private abstract class DataIterator implements Iterator<String> {

        private int       pos = 0;
        private int       max = -1;
        protected List<?> list;

        public DataIterator(List<?> list) {
            this.list = list;
            max = list.size();
        }

        @Override
        public boolean hasNext() {
            return pos < max;
        }

        @Override
        public String next() {
            String value = getValue(pos);
            pos++;
            return value;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove unsupported");
        }

        protected abstract String getValue(int position);
    }

    private class DataAttributeWithDimensionAttachmentLevelIterator extends DataIterator {

        public DataAttributeWithDimensionAttachmentLevelIterator(List<AttributeInstanceDto> list) {
            super(list);
        }

        @Override
        protected String getValue(int position) {
            String value = null;
            AttributeInstanceDto attributeInstanceDto = (AttributeInstanceDto) list.get(position);
            if (attributeInstanceDto != null) {
                value = toAttributeInstanceValueToData(attributeInstanceDto);
            }
            return value;
        }
    }

    private class DataObservationIterator extends DataIterator {

        public DataObservationIterator(List<ObservationExtendedDto> list) {
            super(list);
        }

        @Override
        protected String getValue(int position) {
            String value = null;
            ObservationExtendedDto observationExtendedDto = (ObservationExtendedDto) list.get(position);
            if (observationExtendedDto != null) {
                value = observationExtendedDto.getPrimaryMeasure();
            }
            return value;
        }
    }

    private class DataAttributeObservationIterator extends DataIterator {

        private final String attributeId;
        private boolean      anyObservationHasAttribute = false;

        public DataAttributeObservationIterator(List<ObservationExtendedDto> list, String attributeId) {
            super(list);
            this.attributeId = attributeId;
        }

        @Override
        protected String getValue(int position) {
            String value = null;
            ObservationExtendedDto observationExtendedDto = (ObservationExtendedDto) list.get(position);
            if (observationExtendedDto != null) {
                AttributeInstanceObservationDto attributeInstanceObservationDto = observationExtendedDto.getAttributesAsMap().get(attributeId);
                if (attributeInstanceObservationDto != null) {
                    anyObservationHasAttribute = true;
                    value = toAttributeInstanceValueToData(attributeInstanceObservationDto);
                }
            }
            return value;
        }

        public boolean isAnyObservationHasAttribute() {
            return anyObservationHasAttribute;
        }
    }

    private InternationalString getUpdatedStatisticalOperationName(String operationCode, ExternalRestObjectsMapper externalRestObjectsMapper) {

        InternationalString operationTitle = externalRestObjectsMapper.getOperationTitlesByCode().get(operationCode);

        if (operationTitle == null) {

            Operation operation = statisticalOperationsRestInternalFacade.retrieveOperation(operationCode);
            if (operation != null) {
                externalRestObjectsMapper.getOperationTitlesByCode().put(operationCode, operation.getName());
                return operation.getName();

            }
            return null;
        }

        return operationTitle;

    }

    private InternationalString getUpdatedStatisticalOperationInstanceName(String operationId, String instanceId, ExternalRestObjectsMapper externalRestObjectsMapper) {

        String keyInstance = operationId + " - " + instanceId;
        InternationalString operationInstanceTitle = externalRestObjectsMapper.getOperationInstancesTitlesByCode().get(keyInstance);

        if (operationInstanceTitle == null) {

            Instance instance = statisticalOperationsRestInternalFacade.retrieveInstanceById(operationId, instanceId);
            if (instance != null) {
                externalRestObjectsMapper.getOperationInstancesTitlesByCode().put(keyInstance, instance.getName());
                return instance.getName();

            }
            return null;
        }

        return operationInstanceTitle;
    }

    /*** SRM **/
    private Organisation toOrganisation(Agency agency, List<String> selectedLanguages) {
        if (agency == null) {
            return null;
        }

        Organisation organisation = new Organisation();
        toResource(agency, organisation, selectedLanguages);
        organisation.setContacts(toContacts(agency.getContacts(), selectedLanguages));

        return organisation;
    }

    private void toResource(Agency agency, ResourceStatisticalResourceBase resource, List<String> selectedLanguages) {
        toResource(agency, resource, agency.getKind(), selectedLanguages);
    }

    private void toResource(Item source, ResourceStatisticalResourceBase target, String kind, List<String> selectedLanguages) {
        target.setKind(kind);
        target.setId(source.getId());
        target.setNestedId(source.getNestedId());
        target.setUrn(source.getUrn());
        target.setSelfLink(source.getSelfLink());
        target.setName(toInternationalString(source.getName(), selectedLanguages));
        target.setDescription(toInternationalString(source.getDescription(), selectedLanguages));
        if (source.getManagementAppLink() != null) {
            target.setManagementAppLink(RestUtils.createLink(srmInternalWebApplication, source.getManagementAppLink()));
        }
        target.setUrnProvider(source.getUrnProvider());
    }

    private Contacts toContacts(org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Contacts sources, List<String> selectedLanguages) {
        if (sources == null) {
            return null;
        }

        Contacts targets = new Contacts();
        for (org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Contact source : sources.getContacts()) {
            Contact target = toContact(source, selectedLanguages);
            targets.getContacts().add(target);
        }
        targets.setTotal(BigInteger.valueOf(sources.getContacts().size()));

        return targets;
    }

    private Contact toContact(org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Contact source, List<String> selectedLanguages) {
        if (source == null) {
            return null;
        }
        Contact target = new Contact();
        target.setId(source.getId());
        target.setName(source.getName());
        target.setOrganisationUnit(source.getOrganisationUnit());
        target.setResponsibility(source.getResponsibility());
        target.getTelephones().addAll(source.getTelephones());
        target.getFaxes().addAll(source.getFaxes());
        target.getUrls().addAll(source.getUrls());
        target.getEmails().addAll(source.getEmails());
        return target;
    }

    @Override
    public List<ExternalItem> getTemporalGranularitiesFiter(Map<String, List<String>> granularities) {
        List<ExternalItem> temporalGranularitiesFilter = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : granularities.entrySet()) {
            if ("TIME_PERIOD".equals(entry.getKey())) {
                temporalGranularitiesFilter = getExternalItemByCodeMap(entry);
            }
        }
        return temporalGranularitiesFilter;
    }

    private List<ExternalItem> getExternalItemByCodeMap(Map.Entry<String, List<String>> entry) {
        List<ExternalItem> externalItems = new ArrayList<>();
        for (String granularity : entry.getValue()) {
            ExternalItem externalItem = new ExternalItem();
            externalItem.setCode(granularity);
            externalItems.add(externalItem);
        }
        return externalItems;
    }

    @Override
    public List<ExternalItem> getGeographicGranularitiesFilter(Map<String, List<String>> granularities, DsdProcessorResult dsdProcessorResult) {
        List<ExternalItem> geographicalGranularitiesFilter = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : granularities.entrySet()) {
            if (checkCorrectCodeDimension(dsdProcessorResult, entry.getKey())) {
                geographicalGranularitiesFilter = getExternalItemByCodeMap(entry);
            }
        }
        return geographicalGranularitiesFilter;
    }

    private boolean checkCorrectCodeDimension(DsdProcessorResult dsdProcessorResult, String code) {
        for (DsdDimension source : dsdProcessorResult.getDimensions()) {
            if (code.equals(source.getComponentId()) && DsdComponentType.SPATIAL.equals(source.getType())) {
                return true;
            }
        }
        return false;
    }
    @Override
    public Map<String, List<String>> parseParamExpression(String paramExpression) {
        String afterPatternRegex = "~after=(" + removeCapturing(SDMXCommonRegExpV2_1.OBSERVATIONAL_TIME_PERIOD) + ")";
        String lastPatternRegex = "~last=(\\d+)";
        String rangePatternRegex = "~range=(" + removeCapturing(SDMXCommonRegExpV2_1.OBSERVATIONAL_TIME_PERIOD) + ");(" + removeCapturing(SDMXCommonRegExpV2_1.OBSERVATIONAL_TIME_PERIOD) + ")";
        Pattern patternDimension = Pattern.compile("(\\w+)\\[((" + "[^\\]]" + ")+)\\]");
        String code = removeCapturing(rangePatternRegex) + "|" + removeCapturing(afterPatternRegex) + "|" + removeCapturing(lastPatternRegex) + "|"
                + removeCapturing(SDMXCommonRegExpV2_1.OBSERVATIONAL_TIME_PERIOD) + "|" + removeCapturing(SDMXCommonRegExpV2_1.IDTYPE);
        Pattern patternCodes = Pattern.compile("^(" + code + ")$");
        if (StringUtils.isBlank(paramExpression)) {
            return MapUtils.EMPTY_MAP;
        }

        Matcher matcherDimension = patternDimension.matcher(paramExpression);

        Map<String, List<String>> selectedDimension = new HashMap<>();
        while (matcherDimension.find()) {
            String dimIdentifier = matcherDimension.group(1);
            String codes = matcherDimension.group(2);

            List<String> codeDimensions = selectedDimension.get(dimIdentifier);

            if (codeDimensions == null) {
                codeDimensions = new ArrayList<>();
                selectedDimension.put(dimIdentifier, codeDimensions);
            }

            codeDimensions.addAll(parseCodes(patternCodes, codes));
        }
        return selectedDimension;
    }

    private List<String> parseCodes(Pattern patternCode, String codes) {
        List<String> codeDimensions = new ArrayList<>();

        if (!StringUtils.isBlank(codes)) {
            List<String> splittedCodes = Arrays.asList(StringUtils.split(codes, "|"));

            for (String splittedCode : splittedCodes) {
                Matcher matcherCode = patternCode.matcher(splittedCode);
                while (matcherCode.find()) {
                    codeDimensions.add(matcherCode.group(1));
                }
            }
        }
        return codeDimensions;
    }

    private String removeCapturing(String regex) {
        return regex.replace("(?:", "(").replace("(", "(?:");
    }

    @Override
    public DimensionsFilter getDimensionsFilter(Map<String, List<String>> granularities, DsdProcessorResult dsdProcessorResult) {
        DimensionsFilter dimensionsFilter = new DimensionsFilter();
        for (Map.Entry<String, List<String>> entry : granularities.entrySet()) {
            if (checkCorrectCodeDimension(dsdProcessorResult, entry.getKey(), DsdComponentType.SPATIAL)) {
                dimensionsFilter.setGeographicDimensionId(entry.getKey());
                dimensionsFilter.setGeographicDimensionValuesIds(entry.getValue());
            }
            if (checkCorrectCodeDimension(dsdProcessorResult, entry.getKey(), DsdComponentType.TEMPORAL)) {
                dimensionsFilter.setTemporalDimensionId(entry.getKey());
                dimensionsFilter.setTemporalDimensionValuesIds(getTemporalGranularitiesCodes(entry.getValue()));
            }
        }
        return dimensionsFilter;
    }

    private List<String> getTemporalGranularitiesCodes(List<String> temporalGranularities) {
        List<String> temporalGranularitiesCodes = new ArrayList<>();
        for (String temporalGranularity : temporalGranularities) {
            try {
                StringUtils.isNotBlank(temporalGranularity);
                IstacTimeGranularityCodeEnum istacTimeGranularityCodeEnum = IstacTimeGranularityCodeEnum.valueOf(temporalGranularity);
                temporalGranularitiesCodes.add(istacTimeGranularityCodeEnum.getLabel());
            } catch (Exception e) {
                temporalGranularitiesCodes.add(temporalGranularity);
            }
        }

        return temporalGranularitiesCodes;
    }

    private boolean checkCorrectCodeDimension(DsdProcessorResult dsdProcessorResult, String code, DsdComponentType type) {
        for (DsdDimension source : dsdProcessorResult.getDimensions()) {
            if (code.equals(source.getComponentId()) && type.equals(source.getType())) {
                return true;
            }
        }
        return false;
    }

    private InternationalString toInternationalString(InternationalStringDto sources, List<String> selectedLanguages) {
        if (sources == null) {
            return null;
        }
        InternationalString targets = new InternationalString();
        for (LocalisedStringDto source : sources.getTexts()) {
            if (selectedLanguages.contains(source.getLocale())) {
                LocalisedString target = new LocalisedString();
                target.setLang(source.getLocale());
                target.setValue(source.getLabel());
                targets.getTexts().add(target);
            }
        }
        return targets;
    }
}