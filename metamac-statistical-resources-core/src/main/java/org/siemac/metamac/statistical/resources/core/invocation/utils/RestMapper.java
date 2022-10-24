package org.siemac.metamac.statistical.resources.core.invocation.utils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.avro.generic.GenericRecord;
import org.apache.avro.specific.SpecificData;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodeResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ItemResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.rest.utils.RestUtils;
import org.siemac.metamac.statistical.resources.core.base.domain.HasSiemacMetadata;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.common.domain.InternationalString;
import org.siemac.metamac.statistical.resources.core.common.domain.LocalisedString;
import org.siemac.metamac.statistical.resources.core.common.mapper.CommonDto2DoMapper;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.stream.messages.InternationalStringAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.InternationalStringItemAvro;
import org.siemac.metamac.statistical_resources.rest.common.StatisticalResourcesRestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import es.ibestat.jaxi.stream.messages.DatasetAvro;
import es.ibestat.jaxi.stream.messages.ExternalItemAvro;

@Component
public class RestMapper {

    private String                           statisticalResourcesApiInternalEndpointV10;
    private InternalWebApplicationNavigation internalWebApplicationNavigation;

    @Autowired
    @Qualifier("commonDto2DoMapper")
    private CommonDto2DoMapper               dto2DoMapper;

    @Autowired
    private ConfigurationService             configurationService;

    @PostConstruct
    public void init() throws MetamacException {
        String statisticalResourcesInternalWebUrlBase = configurationService.retrieveStatisticalResourcesInternalWebApplicationUrlBase();
        internalWebApplicationNavigation = new InternalWebApplicationNavigation(statisticalResourcesInternalWebUrlBase);

        String statisticalResourcesApiInternalEndpoint = configurationService.retrieveStatisticalResourcesInternalApiUrlBase();
        statisticalResourcesApiInternalEndpointV10 = RestUtils.createLink(statisticalResourcesApiInternalEndpoint, StatisticalResourcesRestConstants.API_VERSION_1_0);
    }

    public List<ExternalItem> buildExternalItemsFromResourcesInternal(List<ResourceInternal> resources) throws MetamacException {
        List<ExternalItem> externalItems = new ArrayList<ExternalItem>();
        for (ResourceInternal resource : resources) {
            externalItems.add(buildExternalItemFromResourceInternal(resource));
        }
        return externalItems;
    }

    public List<ExternalItem> buildExternalItemFromJaxiExternalPublication(DatasetAvro jaxiDatasetVersionAvro) throws MetamacException {
        List<ExternalItem> externalItems = new ArrayList<ExternalItem>();
        for (ExternalItemAvro externalAvro : jaxiDatasetVersionAvro.getGeographicCoverage()) {

            ExternalItem externalItem = new ExternalItem();

            externalItem.setType(TypeExternalArtefactsEnum.valueOf(externalAvro.getType().name()));
            externalItem.setCode(externalAvro.getCode());
            externalItem.setCodeNested(externalAvro.getCodeNested());
            
            //TODO EDATOS-3770 VER SI NECESARIO URI Y MANAGEMENTAPPURL
            ///latest/variables/VR_TERRITORIO/variableelements/CCAA_CANARIAS
            // externalItem.setUri(dto2DoMapper.externalItemApiUrlDtoToDo(externalAvro.getType(), resource.getSelfLink().getHref()));
            externalItem.setUri("FIXME EDATOS-3770 RELLENAR");
            externalItem.setUrn(externalAvro.getUrn());
            // externalItem.setManagementAppUrl(dto2DoMapper.externalItemWebAppUrlDtoToDo(externalAvro.getType(), resource.getManagementAppLink()));
            externalItem.setTitle(getInternationalStringFromInternationalStringAvro(externalAvro.getTitle()));
            externalItems.add(externalItem);
        }

        return externalItems;
    }
    
    public ExternalItem buildExternalItemFromResourceInternal(ResourceInternal resource) throws MetamacException {
        ExternalItem externalItem = new ExternalItem();
        TypeExternalArtefactsEnum type = TypeExternalArtefactsEnum.fromValue(resource.getKind());

        externalItem.setType(type);
        externalItem.setCode(resource.getId());
        externalItem.setCodeNested(resource.getNestedId());
        externalItem.setUri(dto2DoMapper.externalItemApiUrlDtoToDo(type, resource.getSelfLink().getHref()));
        externalItem.setUrn(resource.getUrn());
        externalItem.setUrnProvider(resource.getUrnProvider());
        externalItem.setManagementAppUrl(dto2DoMapper.externalItemWebAppUrlDtoToDo(type, resource.getManagementAppLink()));
        externalItem.setTitle(getInternationalStringFromInternationalStringResource(resource.getName()));
        return externalItem;
    }

    public ExternalItem buildExternalItemFromSrmItemResourceInternal(ItemResourceInternal itemResourceInternal) throws MetamacException {
        return buildExternalItemFromResourceInternal(itemResourceInternal);
    }

    public ExternalItem buildExternalItemFromCode(CodeResourceInternal code) throws MetamacException {
        return buildExternalItemFromSrmItemResourceInternal(code);
    }

    private InternationalString getInternationalStringFromInternationalStringAvro(InternationalStringAvro internationalStringAvro) {
        InternationalString result = new InternationalString();
        List<InternationalStringItemAvro> internationalStringItemAvro = internationalStringAvro.getLocalisedStrings();

        for (Object element : internationalStringItemAvro) {
            GenericRecord record = (GenericRecord) element;
            InternationalStringItemAvro item = (InternationalStringItemAvro) SpecificData.get().deepCopy(InternationalStringItemAvro.getClassSchema(), record);
            result.addText(new LocalisedString(item.getLocale(), item.getLabel()));
        }
        return result;
    }
    
    public InternationalString getInternationalStringFromInternationalStringResource(org.siemac.metamac.rest.common.v1_0.domain.InternationalString intString) {
        InternationalString result = new InternationalString();
        for (org.siemac.metamac.rest.common.v1_0.domain.LocalisedString text : intString.getTexts()) {
            LocalisedString localised = new LocalisedString(text.getLang(), text.getValue());
            result.addText(localised);
        }

        return result;
    }

    public List<org.siemac.metamac.rest.notices.v1_0.domain.ResourceInternal> generateResourceInternalList(List<HasSiemacMetadata> affectedResources) {
        List<org.siemac.metamac.rest.notices.v1_0.domain.ResourceInternal> list = new ArrayList<org.siemac.metamac.rest.notices.v1_0.domain.ResourceInternal>();
        for (HasSiemacMetadata item : affectedResources) {
            org.siemac.metamac.rest.notices.v1_0.domain.ResourceInternal generateResourceInternal = generateResourceInternal(item);
            list.add(generateResourceInternal);
        }
        return list;
    }

    public List<org.siemac.metamac.rest.notices.v1_0.domain.ResourceInternal> generateQueryVersionResourceInternalList(List<QueryVersion> affectedResources) {
        List<org.siemac.metamac.rest.notices.v1_0.domain.ResourceInternal> list = new ArrayList<org.siemac.metamac.rest.notices.v1_0.domain.ResourceInternal>();
        for (QueryVersion item : affectedResources) {
            org.siemac.metamac.rest.notices.v1_0.domain.ResourceInternal generateResourceInternal = generateResourceInternal(item);
            list.add(generateResourceInternal);
        }
        return list;
    }

    public org.siemac.metamac.rest.notices.v1_0.domain.ResourceInternal generateResourceInternal(HasSiemacMetadata resource) {
        org.siemac.metamac.rest.notices.v1_0.domain.ResourceInternal resourceInternal = new org.siemac.metamac.rest.notices.v1_0.domain.ResourceInternal();

        if (resource != null) {
            LifeCycleStatisticalResource lifeCycleStatisticalResource = resource.getLifeCycleStatisticalResource();
            StatisticalResourceTypeEnum type = resource.getSiemacMetadataStatisticalResource().getType();

            resourceInternal.setId(lifeCycleStatisticalResource.getCode());
            resourceInternal.setUrn(lifeCycleStatisticalResource.getUrn());
            resourceInternal.setKind(getKindByType(type));
            resourceInternal.setName(toRestInternationalString(lifeCycleStatisticalResource.getTitle()));
            resourceInternal.setManagementAppLink(buildManagementAppLinkByType(resource));
            resourceInternal.setSelfLink(createSelfLink(lifeCycleStatisticalResource, type));
        }

        return resourceInternal;
    }

    public org.siemac.metamac.rest.notices.v1_0.domain.ResourceInternal generateResourceInternal(QueryVersion resource) {
        org.siemac.metamac.rest.notices.v1_0.domain.ResourceInternal resourceInternal = new org.siemac.metamac.rest.notices.v1_0.domain.ResourceInternal();

        if (resource != null) {
            LifeCycleStatisticalResource lifeCycleStatisticalResource = resource.getLifeCycleStatisticalResource();
            StatisticalResourceTypeEnum type = StatisticalResourceTypeEnum.QUERY;

            resourceInternal.setId(lifeCycleStatisticalResource.getCode());
            resourceInternal.setUrn(lifeCycleStatisticalResource.getUrn());
            resourceInternal.setKind(getKindByType(StatisticalResourceTypeEnum.QUERY));
            resourceInternal.setName(toRestInternationalString(lifeCycleStatisticalResource.getTitle()));
            resourceInternal.setManagementAppLink(buildManagementAppLinkByType(resource));
            resourceInternal.setSelfLink(createSelfLink(lifeCycleStatisticalResource, type));
        }
        return resourceInternal;
    }

    private String buildManagementAppLinkByType(HasSiemacMetadata item) {
        switch (item.getSiemacMetadataStatisticalResource().getType()) {
            case DATASET:
                return internalWebApplicationNavigation.buildDatasetVersionUrl((DatasetVersion) item);
            case COLLECTION:
                return internalWebApplicationNavigation.buildPublicationVersionUrl((PublicationVersion) item);
            case MULTIDATASET:
                return internalWebApplicationNavigation.buildMultidatasetVersionUrl((MultidatasetVersion) item);
            // Nótese que las queries NO se publican y por tanto no hace falta el case aquí
            default:
                throw new RuntimeException("Invalid value for statistical resource type " + item.getSiemacMetadataStatisticalResource().getType());
        }
    }

    private String buildManagementAppLinkByType(QueryVersion item) {
        return internalWebApplicationNavigation.buildQueryVersionUrl(item);
    }

    private ResourceLink createSelfLink(LifeCycleStatisticalResource lifeCycleStatisticalResource, StatisticalResourceTypeEnum type) {
        String agencyID = lifeCycleStatisticalResource.getMaintainer().getCodeNested();
        String resourceID = lifeCycleStatisticalResource.getCode();
        String version = lifeCycleStatisticalResource.getVersionLogic();

        switch (type) {
            case DATASET:
                return toDatasetSelfLink(agencyID, resourceID, version);
            case COLLECTION:
                return toCollectionSelfLink(agencyID, resourceID);
            case QUERY:
                return toQuerySelfLink(agencyID, resourceID);
            case MULTIDATASET:
                return toMultidatasetSelfLink(agencyID, resourceID);
            default:
                throw new RuntimeException("Invalid value for statistical resource type " + type);
        }
    }

    private ResourceLink toDatasetSelfLink(String agencyID, String resourceID, String version) {
        String link = toDatasetLink(agencyID, resourceID, version);
        return toResourceLink(StatisticalResourcesRestConstants.KIND_DATASET, link);
    }

    private String toDatasetLink(String agencyID, String resourceID, String version) {
        String resourceSubpath = StatisticalResourcesRestConstants.LINK_SUBPATH_DATASETS;
        return toResourceLink(resourceSubpath, agencyID, resourceID, version);
    }

    private ResourceLink toCollectionSelfLink(String agencyID, String resourceID) {
        String link = toCollectionLink(agencyID, resourceID);
        return toResourceLink(StatisticalResourcesRestConstants.KIND_COLLECTION, link);
    }

    private String toCollectionLink(String agencyID, String resourceID) {
        String resourceSubpath = StatisticalResourcesRestConstants.LINK_SUBPATH_COLLECTIONS;
        String version = null; // do not return version
        return toResourceLink(resourceSubpath, agencyID, resourceID, version);
    }

    private ResourceLink toQuerySelfLink(String agencyID, String resourceID) {
        String link = toQueryLink(agencyID, resourceID);
        return toResourceLink(StatisticalResourcesRestConstants.KIND_QUERY, link);
    }

    private String toQueryLink(String agencyID, String resourceID) {
        String resourceSubpath = StatisticalResourcesRestConstants.LINK_SUBPATH_QUERIES;
        String version = null; // do not return version
        return toResourceLink(resourceSubpath, agencyID, resourceID, version);
    }

    private ResourceLink toMultidatasetSelfLink(String agencyID, String resourceID) {
        String link = toMultidatasetLink(agencyID, resourceID);
        return toResourceLink(StatisticalResourcesRestConstants.KIND_MULTIDATASET, link);
    }

    private String toMultidatasetLink(String agencyID, String resourceID) {
        String resourceSubpath = StatisticalResourcesRestConstants.LINK_SUBPATH_MULTIDATASETS;
        String version = null; // do not return version
        return toResourceLink(resourceSubpath, agencyID, resourceID, version);
    }

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

    private org.siemac.metamac.rest.common.v1_0.domain.InternationalString toRestInternationalString(org.siemac.metamac.statistical.resources.core.common.domain.InternationalString source) {
        org.siemac.metamac.rest.common.v1_0.domain.InternationalString target = new org.siemac.metamac.rest.common.v1_0.domain.InternationalString();
        if (source != null) {
            for (LocalisedString item : source.getTexts()) {
                org.siemac.metamac.rest.common.v1_0.domain.LocalisedString targetString = new org.siemac.metamac.rest.common.v1_0.domain.LocalisedString();
                targetString.setValue(item.getLabel());
                targetString.setLang(item.getLocale());
                target.getTexts().add(targetString);
            }
        }
        return target;
    }

    private String getKindByType(StatisticalResourceTypeEnum type) {
        switch (type) {
            case DATASET:
                return TypeExternalArtefactsEnum.DATASET.getValue();
            case COLLECTION:
                return TypeExternalArtefactsEnum.COLLECTION.getValue();
            case QUERY:
                return TypeExternalArtefactsEnum.QUERY.getValue();
            case MULTIDATASET:
                return TypeExternalArtefactsEnum.MULTIDATASET.getValue();
            default:
                throw new RuntimeException("Invalid value for statistical resource type " + type);
        }
    }

}
