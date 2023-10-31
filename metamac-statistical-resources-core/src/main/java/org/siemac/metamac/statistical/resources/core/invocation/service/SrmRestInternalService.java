package org.siemac.metamac.statistical.resources.core.invocation.service;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
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
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Concept;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptSchemes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Concepts;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ContentConstraint;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ContentConstraints;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataProvider;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructures;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationSchemes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Organisations;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.RegionReference;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VariableElement;

public interface SrmRestInternalService {

    public static final String BEAN_ID = "srmRestInternalService";

    // DSD
    public List<ResourceInternal> findDsds(String query) throws MetamacException;
    public DataStructures findDsds(int firstResult, int maxResult, String query) throws MetamacException;
    public List<String> findDsdsAsUrnsList(String query) throws MetamacException;

    public DataStructure retrieveDsdByUrn(String urn) throws MetamacException;

    // CONCEPT SCHEMES
    public List<ResourceInternal> findConceptSchemes(String query) throws MetamacException;
    public ConceptSchemes findConceptSchemes(int firstResult, int maxResult, String query) throws MetamacException;
    public List<String> findConceptSchemesAsUrnsList(String query) throws MetamacException;

    public ConceptScheme retrieveConceptSchemeByUrn(String urn) throws MetamacException;

    // CONCEPTS
    public List<ResourceInternal> findConcepts(String query) throws MetamacException;
    public Concepts findConcepts(int firstResult, int maxResult, String query) throws MetamacException;
    public Concepts findConcepts(String conceptSchemeUrn, int firstResult, int maxResult, String query) throws MetamacException;
    public List<String> findConceptsAsUrnsList(String query) throws MetamacException;

    public Concept retrieveConceptByUrn(String urn) throws MetamacException;
    public Concepts retrieveConceptsOfConceptSchemeEfficiently(String conceptSchemeUrn) throws MetamacException;

    // CODELIST
    public List<ResourceInternal> findCodelists(String query) throws MetamacException;
    public Codelists findCodelists(int firstResult, int maxResult, String query) throws MetamacException;
    public List<String> findCodelistsAsUrnsList(String query) throws MetamacException;

    public Codelist retrieveCodelistByUrn(String urn) throws MetamacException;
    public CodelistRestrictions findCodelistRestrictions(String urn) throws MetamacException;

    // CODES
    public List<ResourceInternal> findCodes(String query) throws MetamacException;
    public Codes findCodes(int firstResult, int maxResult, String query) throws MetamacException;
    public Codes findCodes(String codelistUrn, Integer firstResult, Integer maxResult, String query) throws MetamacException;
    public List<String> findCodesAsUrnsList(String query) throws MetamacException;

    public Codes retrieveCodesOfCodelistEfficiently(String codelistUrn) throws MetamacException;
    public Codes retrieveCodesOfCodelistEfficiently(String codelistUrn, String restriction) throws MetamacException;
    public Code retrieveCodeByUrn(String urn) throws MetamacException;

    // VARIABLE ELEMENT
    public VariableElement retrieveVariableElement(String codelistUrn) throws MetamacException;

    // ORGANISATION SCHEMES
    public List<ResourceInternal> findOrganisationSchemes(String query) throws MetamacException;
    public OrganisationSchemes findOrganisationSchemes(int firstResult, int maxResult, String query) throws MetamacException;
    public List<String> findOrganisationSchemesAsUrnsList(String query) throws MetamacException;

    // ORGANISATION
    public List<ResourceInternal> findOrganisations(String query) throws MetamacException;
    public Organisations findOrganisations(int firstResult, int maxResult, String query) throws MetamacException;
    public List<String> findOrganisationsAsUrnsList(String query) throws MetamacException;

    public Organisations retrieveOrganisationsOfOrganisationSchemeEfficiently(String organisationSchemeUrn) throws MetamacException;
    public Agency retrieveAgencyByUrn(String agencyUrn) throws MetamacException;
    public DataProvider retrieveDataProviderByUrn(String dataProviderUrn) throws MetamacException;

    // CATEGORY SCHEMES
    public List<ResourceInternal> findCategorySchemes(String query) throws MetamacException;
    public CategorySchemes findCategorySchemes(int firstResult, int maxResult, String query) throws MetamacException;
    public List<String> findCategorySchemesAsUrnsList(String query) throws MetamacException;

    // CATEGORY
    public Category retrieveCategoryByUrn(String urn) throws MetamacException;
    public List<ResourceInternal> findCategories(String query) throws MetamacException;
    public Categories findCategories(int firstResult, int maxResult, String query) throws MetamacException;
    public List<String> findCategoriesAsUrnsList(String query) throws MetamacException;

    public Categories retrieveCategoriesOfCategorySchemeEfficiently(String categorySchemeUrn) throws MetamacException;

    // CONSTRAINTS
    public ContentConstraints findContentConstraints(int firstResult, int maxResult, String query, Boolean includeDraft) throws MetamacException;
    public List<ResourceInternal> findContentConstraints(String query, Boolean includeDraft) throws MetamacException;
    public List<String> findContentConstraintsAsUrnsList(String query, Boolean includeDraft) throws MetamacException;

    public RegionReference retrieveRegionForContentConstraint(String contentConstraintUrn, String regionCode, Boolean includeDraft) throws MetamacException;
    public ContentConstraint saveContentConstraint(ServiceContext serviceContext, ContentConstraint contentConstraint) throws MetamacException;
    public RegionReference saveRegionForContentConstraint(ServiceContext serviceContext, RegionReference regionReference) throws MetamacException;
    public void deleteContentConstraint(ServiceContext serviceContext, String urn, Boolean forceDeleteFinal) throws MetamacException;
    public void publishContentConstraint(ServiceContext serviceContext, String urn, Boolean alsoMarkAsPublic) throws MetamacException;
    public void versioningContentConstraintsForArtefact(ServiceContext serviceContext, String artefactUrn, String newAttachmentConstraintUrn, VersionTypeEnum versionTypeEnum) throws MetamacException;
    public void revertContentConstraintsForArtefactToDraft(ServiceContext serviceContext, String artefactUrn) throws MetamacException;

    public ContentConstraint retrieveContentConstraintByUrn(String urn, Boolean includeDraft) throws MetamacException;
    public void deleteRegion(ServiceContext serviceContext, String contentConstraintUrn, String regionCode) throws MetamacException;
}
