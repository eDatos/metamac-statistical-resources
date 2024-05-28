package org.siemac.metamac.statistical_resources.rest.internal.invocation;

import org.apache.commons.lang.BooleanUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.rest.common.v1_0.domain.ComparisonOperator;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Agency;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelist;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Concept;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Concepts;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ContentConstraint;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ContentConstraintCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ContentConstraints;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VariableElementsGeoInfo;
import org.siemac.metamac.srm.rest.common.SrmRestConstants;
import org.siemac.metamac.statistical_resources.rest.common.impl.utils.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("srmRestInternalFacade")
public class SrmRestInternalFacadeImpl implements SrmRestInternalFacade {

    private final Logger       logger = LoggerFactory.getLogger(SrmRestInternalFacadeImpl.class);

    @Autowired
    @Qualifier("metamacApisLocatorRest")
    private MetamacApisLocator restApiLocator;

    @Override
    public DataStructure retrieveDataStructureByUrn(String urn) {
        try {
            String[] urnSplited = UrnUtils.splitUrnStructure(urn);
            String agencyID = urnSplited[0];
            String resourceID = urnSplited[1];
            String version = urnSplited[2];
            return restApiLocator.getSrmRestExternalFacadeV10().retrieveDataStructure(agencyID, resourceID, version, SrmRestConstants.FIELD_INCLUDE_DIMENSION_PLURAL_NAME);
        } catch (Exception e) {
            throw toRestException(e);
        }
    }

    @Override
    public Codelist retrieveCodelistByUrn(String urn) {
        try {
            String[] urnSplited = UrnUtils.splitUrnItemScheme(urn);
            String agencyID = urnSplited[0];
            String resourceID = urnSplited[1];
            String version = urnSplited[2];
            return restApiLocator.getSrmRestExternalFacadeV10().retrieveCodelist(agencyID, resourceID, version);
        } catch (Exception e) {
            throw toRestException(e);
        }
    }

    @Override
    public Codes retrieveCodesByCodelistUrn(String urn, String order, String openness, String fields) {
        try {
            String[] urnSplited = UrnUtils.splitUrnItemScheme(urn);
            String agencyID = urnSplited[0];
            String resourceID = urnSplited[1];
            String version = urnSplited[2];
            return restApiLocator.getSrmRestExternalFacadeV10().findCodes(agencyID, resourceID, version, null, null, null, null, order, openness, null, null, fields);
        } catch (Exception e) {
            throw toRestException(e);
        }
    }

    @Override
    public Concepts retrieveConceptsByConceptSchemeByUrn(String urn, String fields) {
        try {
            String[] urnSplited = UrnUtils.splitUrnItemScheme(urn);
            String agencyID = urnSplited[0];
            String resourceID = urnSplited[1];
            String version = urnSplited[2];
            return restApiLocator.getSrmRestExternalFacadeV10().findConcepts(agencyID, resourceID, version, null, null, null, null, fields);
        } catch (Exception e) {
            throw toRestException(e);
        }
    }

    @Override
    public Concept retrieveConceptByUrn(String urn) {
        try {
            String[] urnSplited = UrnUtils.splitUrnItem(urn);
            String agencyID = urnSplited[0];
            String itemSchemeID = urnSplited[1];
            String version = urnSplited[2];
            String itemId = urnSplited[3];
            return restApiLocator.getSrmRestExternalFacadeV10().retrieveConcept(agencyID, itemSchemeID, version, itemId);
        } catch (Exception e) {
            throw toRestException(e);
        }
    }

    @Override
    public Agency retrieveAgency(String urn) {
        try {
            String[] urnSplited = UrnUtils.splitUrnItem(urn);
            String agencyID = urnSplited[0];
            String resourceID = urnSplited[1];
            String version = urnSplited[2];
            String organisationID = urnSplited[3];
            return restApiLocator.getSrmRestExternalFacadeV10().retrieveAgency(agencyID, resourceID, version, organisationID);
        } catch (Exception e) {
            throw toRestException(e);
        }
    }

    @Override
    public VariableElementsGeoInfo findVariableElementsGeoInfo(String urn) {
        String[] urnSplited = UrnUtils.splitUrnByDots(UrnUtils.splitUrnItem(urn)[0]);
        String variableID = urnSplited[0];
        String resourceID = urnSplited[1];
        return restApiLocator.getSrmRestExternalFacadeV10().findVariableElementsGeoInfoXml(variableID, resourceID, null, null, null, null, null);
    }

    @Override
    public ContentConstraint retrieveDatasetContentConstraint(String datasetUrn) {
        try {
            String includeDraft = BooleanUtils.toStringTrueFalse(true);
            String urnConstraint = findContentConstraints(datasetUrn, includeDraft);
            if (urnConstraint != null) {
                String[] contentConstraintComponents = GeneratorUrnUtils.extractVersionableArtefactParts(urnConstraint);
                String agencyId = contentConstraintComponents[0];
                String resourceId = contentConstraintComponents[1];
                String version = contentConstraintComponents[2];
                return restApiLocator.getSrmRestExternalFacadeV10().retrieveContentConstraint(agencyId, resourceId, version, includeDraft);
            }
            return null;
        } catch (Exception e) {
            throw toRestException(e);
        }
    }

    private String findContentConstraints(String datasetUrn, String includeDraft) throws MetamacException {

        StringBuilder queryBuilder = new StringBuilder(ContentConstraintCriteriaPropertyRestriction.ARTEFACT_URN.value());
        queryBuilder.append(CommonConstants.SPACE).append(ComparisonOperator.EQ).append(CommonConstants.SPACE).append(CommonConstants.DOUBLE_QUOTE).append(datasetUrn)
                .append(CommonConstants.DOUBLE_QUOTE);

        ContentConstraints contentConstraint = restApiLocator.getSrmRestExternalFacadeV10().findContentConstraints(queryBuilder.toString(), null, null, null, includeDraft);

        if (contentConstraint != null && !contentConstraint.getContentConstraints().isEmpty()) {
            Resource datasetConstraint = contentConstraint.getContentConstraints().get(0);
            return datasetConstraint.getUrn();
        }

        return null;
    }

    private RestException toRestException(Exception e) {
        logger.error("Error", e);
        return RestExceptionUtils.toRestException(e, WebClient.client(restApiLocator.getSrmRestExternalFacadeV10()));
    }
}
