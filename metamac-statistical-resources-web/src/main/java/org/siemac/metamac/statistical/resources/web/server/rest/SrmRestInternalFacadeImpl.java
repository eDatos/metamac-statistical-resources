package org.siemac.metamac.statistical.resources.web.server.rest;

import static org.siemac.metamac.statistical.resources.web.server.utils.MetamacWebRestCriteriaUtils.buildQueryCode;
import static org.siemac.metamac.statistical.resources.web.server.utils.MetamacWebRestCriteriaUtils.buildQueryCodelist;
import static org.siemac.metamac.statistical.resources.web.server.utils.MetamacWebRestCriteriaUtils.buildQueryConcept;
import static org.siemac.metamac.statistical.resources.web.server.utils.MetamacWebRestCriteriaUtils.buildQueryConceptScheme;
import static org.siemac.metamac.statistical.resources.web.server.utils.MetamacWebRestCriteriaUtils.buildQueryDsd;
import static org.siemac.metamac.statistical.resources.web.server.utils.MetamacWebRestCriteriaUtils.buildQueryOrganisation;
import static org.siemac.metamac.statistical.resources.web.server.utils.MetamacWebRestCriteriaUtils.buildQueryOrganisationScheme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Agency;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categories;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Category;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategorySchemes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Code;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelist;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodelistRestrictions;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelists;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptSchemes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Concepts;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructures;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Dimension;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DimensionBase;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Dimensions;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Group;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Groups;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ItemResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.MeasureDimension;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationSchemes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Organisations;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.TimeDimension;
import org.siemac.metamac.statistical.resources.core.common.utils.DsdProcessor;
import org.siemac.metamac.statistical.resources.core.common.utils.DsdProcessor.DsdAttribute;
import org.siemac.metamac.statistical.resources.core.common.utils.DsdProcessor.DsdDimension;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdDimensionDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.ItemDto;
import org.siemac.metamac.statistical.resources.core.invocation.service.SrmRestInternalService;
import org.siemac.metamac.statistical.resources.web.server.utils.ExternalItemWebUtils;
import org.siemac.metamac.statistical.resources.web.server.utils.MetamacWebRestCriteriaUtils;
import org.siemac.metamac.statistical.resources.web.shared.criteria.DsdWebCriteria;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmExternalResourceRestCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmItemRestCriteria;
import org.siemac.metamac.web.common.shared.domain.ExternalItemsResult;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SrmRestInternalFacadeImpl implements SrmRestInternalFacade {

    @Autowired
    private SrmRestInternalService srmRestInternalService;

    @Autowired
    private RestMapper             restMapper;

    // DSDs

    @Override
    public ExternalItemsResult findDsds(int firstResult, int maxResult, DsdWebCriteria criteria) throws MetamacWebException {
        try {
            String query = buildQueryDsd(criteria);

            DataStructures structures = srmRestInternalService.findDsds(firstResult, maxResult, query);

            List<ExternalItemDto> dsdsExternalItems = new ArrayList<ExternalItemDto>();
            for (ResourceInternal resource : structures.getDataStructures()) {
                dsdsExternalItems.add(ExternalItemWebUtils.buildExternalItemDtoFromResource(resource, TypeExternalArtefactsEnum.DATASTRUCTURE));
            }
            return ExternalItemWebUtils.createExternalItemsResultFromListBase(structures, dsdsExternalItems);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public List<DsdDimensionDto> retrieveDsdDimensions(String dsdUrn) throws MetamacWebException {
        try {
            DataStructure dataStructure = srmRestInternalService.retrieveDsdByUrn(dsdUrn);
            List<DsdDimension> dsdDimensions = DsdProcessor.getDimensions(dataStructure);
            List<DsdDimensionDto> dsdDimensionDtos = restMapper.buildDsdDimensionDtosFromDsdDimensions(dsdDimensions);
            return dsdDimensionDtos;
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public DataStructure retrieveDsd (String dsdUrn) throws MetamacWebException {
        try {
            DataStructure structure = srmRestInternalService.retrieveDsdByUrn(dsdUrn);
            return structure;
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public List<String> retrieveDsdDimensionsIds(String dsdUrn) throws MetamacWebException {
        try {
            DataStructure structure = srmRestInternalService.retrieveDsdByUrn(dsdUrn);
            Dimensions dimensionsList = structure.getDataStructureComponents().getDimensions();

            List<String> dimensions = new ArrayList<String>();
            for (DimensionBase dimObj : dimensionsList.getDimensions()) {
                if (dimObj instanceof Dimension) {
                    Dimension dim = (Dimension) dimObj;
                    dimensions.add(dim.getId());
                } else if (dimObj instanceof MeasureDimension) {
                    MeasureDimension dim = (MeasureDimension) dimObj;
                    dimensions.add(dim.getId());
                } else if (dimObj instanceof TimeDimension) {
                    TimeDimension dim = (TimeDimension) dimObj;
                    dimensions.add(dim.getId());
                }
            }
            return dimensions;
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public Map<String, List<String>> retrieveDsdGroupDimensionsIds(String dsdUrn) throws MetamacWebException {
        try {

            DataStructure structure = srmRestInternalService.retrieveDsdByUrn(dsdUrn);
            Groups groups = structure.getDataStructureComponents().getGroups();

            Map<String, List<String>> groupDimensionIds = new HashMap<String, List<String>>();
            if (groups != null) {
                for (Group group : groups.getGroups()) {
                    groupDimensionIds.put(group.getId(), group.getDimensions().getDimensions());
                }
            }
            return groupDimensionIds;
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public List<DsdAttributeDto> retrieveDsdAttributes(String dsdUrn) throws MetamacWebException {
        try {
            DataStructure dataStructure = srmRestInternalService.retrieveDsdByUrn(dsdUrn);
            List<DsdAttribute> dsdAttributes = DsdProcessor.getAttributes(dataStructure);
            Map<String, List<String>> groupDimensions = retrieveDsdGroupDimensionsIds(dsdUrn);
            List<DsdAttributeDto> dsdAttributeDtos = restMapper.buildDsdAttributeDtosFromDsdAttributes(dsdAttributes, groupDimensions);
            return dsdAttributeDtos;
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    // CATEGORY SCHEMES

    @Override
    public ExternalItemsResult findCategorySchemes(int firstResult, int maxResult, MetamacWebCriteria criteria) throws MetamacWebException {
        try {
            String query = MetamacWebRestCriteriaUtils.buildQueryCategoryScheme(criteria);

            CategorySchemes categorySchemes = srmRestInternalService.findCategorySchemes(firstResult, maxResult, query);

            List<ExternalItemDto> items = new ArrayList<ExternalItemDto>();
            for (ResourceInternal resource : categorySchemes.getCategorySchemes()) {
                items.add(ExternalItemWebUtils.buildExternalItemDtoFromResource(resource, TypeExternalArtefactsEnum.CATEGORY_SCHEME));
            }
            return ExternalItemWebUtils.createExternalItemsResultFromListBase(categorySchemes, items);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    // CATEGORIES

    @Override
    public ExternalItemDto retrieveCategoryByUrn(String urn) throws MetamacWebException {
        try {
            Category category = srmRestInternalService.retrieveCategoryByUrn(urn);

            return ExternalItemWebUtils.buildExternalItemDtoFromCategory(category);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public ExternalItemsResult findCategories(int firstResult, int maxResult, SrmItemRestCriteria condition) throws MetamacWebException {
        try {
            String query = MetamacWebRestCriteriaUtils.buildQueryCategory(condition);

            Categories categories = srmRestInternalService.findCategories(firstResult, maxResult, query);

            List<ExternalItemDto> items = new ArrayList<ExternalItemDto>();
            for (ResourceInternal resource : categories.getCategories()) {
                items.add(ExternalItemWebUtils.buildExternalItemDtoFromResource(resource, TypeExternalArtefactsEnum.CATEGORY));
            }
            return ExternalItemWebUtils.createExternalItemsResultFromListBase(categories, items);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    // CODELISTS

    @Override
    public ExternalItemDto retrieveCodelist(String urn) throws MetamacWebException {
        try {
            Codelist codelist = srmRestInternalService.retrieveCodelistByUrn(urn);
            return ExternalItemWebUtils.buildExternalItemDtoFromCodelist(codelist);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public List<ExternalItemDto> retrieveSrmResourceRestrictions(SrmItemRestCriteria condition) throws MetamacWebException {

        try {
            String codelistUrn = null;
            List<ItemResourceInternal> resourcesInternal = new ArrayList<ItemResourceInternal>();
            TypeExternalArtefactsEnum typeExternalArtefactsEnum = null;
            if (condition != null) {
                codelistUrn = condition.getUrn();
                typeExternalArtefactsEnum = condition.getExternalArtifactType();
            }

            if (TypeExternalArtefactsEnum.CODELIST_RESTRICTION.equals(typeExternalArtefactsEnum)) {
                CodelistRestrictions codelistRestrictions = srmRestInternalService.findCodelistRestrictions(codelistUrn);
                resourcesInternal = codelistRestrictions.getRestrictions();
            } else {
                // here put other restrictions like concept schema restriction if they are developed.
                return new ArrayList<ExternalItemDto>();
            }

            List<ExternalItemDto> items = new ArrayList<ExternalItemDto>();
            for (ResourceInternal resource : resourcesInternal) {
                items.add(ExternalItemWebUtils.buildExternalItemDtoFromResource(resource, typeExternalArtefactsEnum));
            }
            return items;

        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public Map<String, String> findMappeableDimensionsInDsdWithVariables(String dsdUrn) throws MetamacWebException {
        try {
            DataStructure dsd = srmRestInternalService.retrieveDsdByUrn(dsdUrn);

            Map<String, String> dimensionMapping = new HashMap<String, String>();

            List<DsdDimension> dimensions = DsdProcessor.getDimensions(dsd);
            for (DsdDimension dsdDimension : dimensions) {
                if (dsdDimension.getCodelistRepresentationUrn() != null) {
                    Codelist codelist = srmRestInternalService.retrieveCodelistByUrn(dsdDimension.getCodelistRepresentationUrn());
                    dimensionMapping.put(dsdDimension.getComponentId(), codelist.getVariable().getUrn());
                }
            }
            return dimensionMapping;
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public ExternalItemsResult findCodelistsWithVariable(String variableUrn, int firstResult, int maxResult, SrmExternalResourceRestCriteria criteria) throws MetamacWebException {
        try {
            String query = buildQueryCodelist(criteria, variableUrn);

            Codelists codelists = srmRestInternalService.findCodelists(firstResult, maxResult, query);

            List<ExternalItemDto> codelistsExternalItems = new ArrayList<ExternalItemDto>();
            for (ResourceInternal resource : codelists.getCodelists()) {
                codelistsExternalItems.add(ExternalItemWebUtils.buildExternalItemDtoFromResource(resource, TypeExternalArtefactsEnum.CODE));
            }
            return ExternalItemWebUtils.createExternalItemsResultFromListBase(codelists, codelistsExternalItems);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    public ExternalItemsResult findCodelists(SrmExternalResourceRestCriteria criteria) throws MetamacWebException {
        try {
            String query = buildQueryCodelist(criteria, null);

            List<ResourceInternal> codelists = srmRestInternalService.findCodelists(query);

            List<ExternalItemDto> codelistsExternalItems = new ArrayList<ExternalItemDto>();
            for (ResourceInternal resource : codelists) {
                codelistsExternalItems.add(ExternalItemWebUtils.buildExternalItemDtoFromResource(resource, TypeExternalArtefactsEnum.CODE));
            }
            return new ExternalItemsResult(codelistsExternalItems, 0, codelistsExternalItems.size());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    // CODES

    @Override
    public ExternalItemsResult findCodesInCodelist(String codelistUrn, int firstResult, int maxResult, MetamacWebCriteria criteria) throws MetamacWebException {
        try {
            String query = buildQueryCode(criteria);

            Codes codes = srmRestInternalService.findCodes(codelistUrn, firstResult, maxResult, query);

            List<ExternalItemDto> codesExternalItems = new ArrayList<ExternalItemDto>();
            for (ResourceInternal resource : codes.getCodes()) {
                codesExternalItems.add(ExternalItemWebUtils.buildExternalItemDtoFromResource(resource, TypeExternalArtefactsEnum.CODE));
            }
            return ExternalItemWebUtils.createExternalItemsResultFromListBase(codes, codesExternalItems);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public ExternalItemsResult findCodesInCodelist(String codelistUrn, Integer firstResult, Integer maxResult, MetamacWebCriteria criteria) throws MetamacWebException {
        try {
            String query = buildQueryCode(criteria);

            Codes codes = srmRestInternalService.findCodes(codelistUrn, firstResult, maxResult, query);

            List<ExternalItemDto> codesExternalItems = new ArrayList<ExternalItemDto>();
            for (ResourceInternal resource : codes.getCodes()) {
                codesExternalItems.add(ExternalItemWebUtils.buildExternalItemDtoFromResource(resource, TypeExternalArtefactsEnum.CODE));
            }
            return ExternalItemWebUtils.createExternalItemsResultFromListBase(codes, codesExternalItems);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public ExternalItemDto retrieveCodeByUrn(String urn) throws MetamacWebException {
        try {
            Code code = srmRestInternalService.retrieveCodeByUrn(urn);
            return ExternalItemWebUtils.buildExternalItemDtoFromCode(code);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public ExternalItemsResult findCodes(int firstResult, int maxResult, SrmItemRestCriteria condition) throws MetamacWebException {
        try {
            String query = null;
            String codelistUrn = null;
            if (condition != null) {
                query = buildQueryCode(condition);
                codelistUrn = condition.getItemSchemeUrn();
            }
            Codes codes = srmRestInternalService.findCodes(codelistUrn, firstResult, maxResult, query);

            List<ExternalItemDto> codesExternalItems = new ArrayList<ExternalItemDto>();
            for (ResourceInternal resource : codes.getCodes()) {
                codesExternalItems.add(ExternalItemWebUtils.buildExternalItemDtoFromResource(resource, TypeExternalArtefactsEnum.CODE));
            }
            return ExternalItemWebUtils.createExternalItemsResultFromListBase(codes, codesExternalItems);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public List<ItemDto> retrieveCodes(String codelistUrn, String codeRestriction) throws MetamacWebException {
        try {
            Codes codes = srmRestInternalService.retrieveCodesOfCodelistEfficiently(codelistUrn, codeRestriction);
            return restMapper.buildItemDtosFromCodes(codes);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    // CONCEPTS

    @Override
    public ExternalItemDto retrieveConceptScheme(String urn) throws MetamacWebException {
        try {
            ConceptScheme conceptScheme = srmRestInternalService.retrieveConceptSchemeByUrn(urn);
            return ExternalItemWebUtils.buildExternalItemDtoFromConceptScheme(conceptScheme);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public ExternalItemsResult findConceptSchemes(int firstResult, int maxResult, MetamacWebCriteria criteria) throws MetamacWebException {
        try {
            String query = buildQueryConceptScheme(criteria);

            ConceptSchemes conceptSchemes = srmRestInternalService.findConceptSchemes(firstResult, maxResult, query);

            List<ExternalItemDto> conceptSchemesExternalItems = new ArrayList<ExternalItemDto>();
            for (ResourceInternal resource : conceptSchemes.getConceptSchemes()) {
                conceptSchemesExternalItems.add(ExternalItemWebUtils.buildExternalItemDtoFromResource(resource, TypeExternalArtefactsEnum.CONCEPT_SCHEME));
            }
            return ExternalItemWebUtils.createExternalItemsResultFromListBase(conceptSchemes, conceptSchemesExternalItems);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public ExternalItemsResult findConcepts(int firstResult, int maxResult, SrmItemRestCriteria condition) throws MetamacWebException {
        try {
            String query = null;
            String conceptSchemeUrn = null;
            if (condition != null) {
                query = buildQueryConcept(condition);
                conceptSchemeUrn = condition.getItemSchemeUrn();
            }
            Concepts concepts = srmRestInternalService.findConcepts(conceptSchemeUrn, firstResult, maxResult, query);

            List<ExternalItemDto> conceptsExternalItems = new ArrayList<ExternalItemDto>();
            for (ResourceInternal resource : concepts.getConcepts()) {
                conceptsExternalItems.add(ExternalItemWebUtils.buildExternalItemDtoFromResource(resource, TypeExternalArtefactsEnum.CONCEPT));
            }
            return ExternalItemWebUtils.createExternalItemsResultFromListBase(concepts, conceptsExternalItems);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public List<ItemDto> retrieveConcepts(String conceptSchemeUrn) throws MetamacWebException {
        try {
            Concepts concepts = srmRestInternalService.retrieveConceptsOfConceptSchemeEfficiently(conceptSchemeUrn);
            return restMapper.buildItemDtosFromConcepts(concepts);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    // ORGANISATIONS

    @Override
    public ExternalItemsResult findOrganisationSchemes(int firstResult, int maxResult, MetamacWebCriteria criteria, TypeExternalArtefactsEnum type) throws MetamacWebException {
        try {
            String query = buildQueryOrganisationScheme(criteria, type);

            OrganisationSchemes schemes = srmRestInternalService.findOrganisationSchemes(firstResult, maxResult, query);

            List<ExternalItemDto> items = new ArrayList<ExternalItemDto>();
            for (ResourceInternal resource : schemes.getOrganisationSchemes()) {
                items.add(ExternalItemWebUtils.buildExternalItemDtoFromResource(resource, type));
            }
            return ExternalItemWebUtils.createExternalItemsResultFromListBase(schemes, items);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public ExternalItemsResult findOrganisations(int firstResult, int maxResult, SrmItemRestCriteria criteria, TypeExternalArtefactsEnum type) throws MetamacWebException {
        try {
            String query = buildQueryOrganisation(criteria, type);

            Organisations organizations = srmRestInternalService.findOrganisations(firstResult, maxResult, query);

            List<ExternalItemDto> items = new ArrayList<ExternalItemDto>();
            for (ResourceInternal resource : organizations.getOrganisations()) {
                items.add(ExternalItemWebUtils.buildExternalItemDtoFromResource(resource, type));
            }
            return ExternalItemWebUtils.createExternalItemsResultFromListBase(organizations, items);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public ExternalItemDto retrieveAgencyByUrn(String agencyUrn) throws MetamacWebException {
        try {
            Agency agency = srmRestInternalService.retrieveAgencyByUrn(agencyUrn);
            return ExternalItemWebUtils.buildExternalItemDtoFromAgency(agency);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
