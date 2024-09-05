package org.siemac.metamac.statistical.resources.web.server.rest;

import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdDimensionDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.ItemDto;
import org.siemac.metamac.statistical.resources.web.shared.criteria.DsdWebCriteria;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmExternalResourceRestCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmItemRestCriteria;
import org.siemac.metamac.web.common.shared.domain.ExternalItemsResult;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

public interface SrmRestInternalFacade {

    // DSDs

    public ExternalItemsResult findDsds(int firstResult, int maxResult, DsdWebCriteria condition) throws MetamacWebException;
    public List<String> retrieveDsdDimensionsIds(String dsdUrn) throws MetamacWebException;
    public List<DsdDimensionDto> retrieveDsdDimensions(String dsdUrn) throws MetamacWebException;
    public DataStructure retrieveDsd (String dsdUrn) throws MetamacWebException;
    public Map<String, List<String>> retrieveDsdGroupDimensionsIds(String dsdUrn) throws MetamacWebException;
    public List<DsdAttributeDto> retrieveDsdAttributes(String dsdUrn) throws MetamacWebException;

    // CODELISTS

    public ExternalItemDto retrieveCodelist(String urn) throws MetamacWebException;
    public Map<String, String> findMappeableDimensionsInDsdWithVariables(String dsdUrn) throws MetamacWebException;
    public ExternalItemsResult findCodelistsWithVariable(String variableUrn, int firstResult, int maxResult, SrmExternalResourceRestCriteria srmExternalResourceRestCriteria)
            throws MetamacWebException;
    public List<ExternalItemDto> retrieveSrmResourceRestrictions(SrmItemRestCriteria condition) throws MetamacWebException;

    // CODES

    public ExternalItemsResult findCodesInCodelist(String codelistUrn, int firstResult, int maxResult, MetamacWebCriteria condition) throws MetamacWebException;
    public ExternalItemsResult findCodesInCodelist(String codelistUrn, Integer firstResult, Integer maxResult, MetamacWebCriteria criteria) throws MetamacWebException;
    public ExternalItemsResult findCodes(int firstResult, int maxResult, SrmItemRestCriteria condition) throws MetamacWebException;
    public ExternalItemDto retrieveCodeByUrn(String urn) throws MetamacWebException;
    public List<ItemDto> retrieveCodes(String codelistUrn, String codeRestriction) throws MetamacWebException;

    // CONCEPTS

    public ExternalItemDto retrieveConceptScheme(String urn) throws MetamacWebException;
    public ExternalItemsResult findConceptSchemes(int firstResult, int maxResult, MetamacWebCriteria condition) throws MetamacWebException;
    public ExternalItemsResult findConcepts(int firstResult, int maxResult, SrmItemRestCriteria condition) throws MetamacWebException;
    public List<ItemDto> retrieveConcepts(String conceptSchemeUrn) throws MetamacWebException;

    // ORGANISATIONS

    public ExternalItemsResult findOrganisationSchemes(int firstResult, int maxResult, MetamacWebCriteria criteria, TypeExternalArtefactsEnum type) throws MetamacWebException;
    public ExternalItemsResult findOrganisations(int firstResult, int maxResult, SrmItemRestCriteria criteria, TypeExternalArtefactsEnum type) throws MetamacWebException;
    public ExternalItemDto retrieveAgencyByUrn(String agencyUrn) throws MetamacWebException;

    // CATEGORY SCHEMES

    public ExternalItemsResult findCategorySchemes(int firstResult, int maxResult, MetamacWebCriteria criteria) throws MetamacWebException;

    // CATEGORIES

    public ExternalItemDto retrieveCategoryByUrn(String urn) throws MetamacWebException;
    public ExternalItemsResult findCategories(int firstResult, int maxResult, SrmItemRestCriteria condition) throws MetamacWebException;
}
