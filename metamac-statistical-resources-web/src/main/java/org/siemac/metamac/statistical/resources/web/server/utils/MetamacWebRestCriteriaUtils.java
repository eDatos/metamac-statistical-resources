package org.siemac.metamac.statistical.resources.web.server.utils;

import static org.siemac.metamac.rest.api.utils.RestCriteriaUtils.appendConditionToQuery;
import static org.siemac.metamac.rest.api.utils.RestCriteriaUtils.fieldComparison;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.rest.common.v1_0.domain.ComparisonOperator;
import org.siemac.metamac.rest.common.v1_0.domain.LogicalOperator;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.InstanceCriteriaPropertyRestriction;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.OperationCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategorySchemeCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodeCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodelistCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptSchemeCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructureCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationSchemeCriteriaPropertyRestriction;
import org.siemac.metamac.statistical.resources.web.shared.criteria.DsdWebCriteria;
import org.siemac.metamac.statistical.resources.web.shared.criteria.base.HasStatisticalOperationCriteria;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmExternalResourceRestCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmItemRestCriteria;
import org.siemac.metamac.web.common.shared.criteria.base.HasItemSchemeCriteria;
import org.siemac.metamac.web.common.shared.criteria.base.HasOnlyLastVersionCriteria;
import org.siemac.metamac.web.common.shared.criteria.base.HasSimpleCriteria;

public class MetamacWebRestCriteriaUtils {

    // -------------------------------------------------------------------------------------------------------------
    // STATISTICAL OPERATION
    // -------------------------------------------------------------------------------------------------------------

    public static String buildQueryStatisticalOperation(MetamacWebCriteria webCriteria) {
        StringBuilder queryBuilder = new StringBuilder();
        if (webCriteria != null) {
            addSimpleRestCriteria(queryBuilder, webCriteria, OperationCriteriaPropertyRestriction.TITLE, OperationCriteriaPropertyRestriction.ID,OperationCriteriaPropertyRestriction.ACRONYM);
        }
        return queryBuilder.toString();
    }

    // -------------------------------------------------------------------------------------------------------------
    // STATISTICAL OPERATION INSTANCE
    // -------------------------------------------------------------------------------------------------------------

    public static String buildQueryStatisticalOperationInstance(MetamacWebCriteria webCriteria) {
        StringBuilder queryBuilder = new StringBuilder();
        if (webCriteria != null) {
            addSimpleRestCriteria(queryBuilder, webCriteria, InstanceCriteriaPropertyRestriction.TITLE, InstanceCriteriaPropertyRestriction.ID);
        }
        return queryBuilder.toString();
    }

    // -------------------------------------------------------------------------------------------------------------
    // DSD
    // -------------------------------------------------------------------------------------------------------------

    public static String buildQueryDsd(DsdWebCriteria webCriteria) {
        StringBuilder queryBuilder = new StringBuilder();
        if (webCriteria != null) {
            String dsdCode = webCriteria.getFixedDsdCode();

            addSimpleRestCriteria(queryBuilder, webCriteria, DataStructureCriteriaPropertyRestriction.NAME, DataStructureCriteriaPropertyRestriction.ID, DataStructureCriteriaPropertyRestriction.URN);

            if (StringUtils.isNotBlank(dsdCode)) {
                String dsdCodeCondition = fieldComparison(DataStructureCriteriaPropertyRestriction.ID, ComparisonOperator.EQ, dsdCode);
                appendConditionToQuery(queryBuilder, dsdCodeCondition);
            }

            addStatisticalOperationRestCriteria(queryBuilder, webCriteria, DataStructureCriteriaPropertyRestriction.STATISTICAL_OPERATION_URN);

            addOnlyLastVersionRestCriteria(queryBuilder, webCriteria, DataStructureCriteriaPropertyRestriction.LATEST);
        }
        return queryBuilder.toString();
    }

    // -------------------------------------------------------------------------------------------------------------
    // CODE LIST
    // -------------------------------------------------------------------------------------------------------------

    public static String buildQueryCodelist(SrmExternalResourceRestCriteria webCriteria, String variableUrn) {
        StringBuilder queryBuilder = new StringBuilder();
        if (webCriteria != null) {
            addSimpleRestCriteria(queryBuilder, webCriteria, CodelistCriteriaPropertyRestriction.NAME, CodelistCriteriaPropertyRestriction.ID, CodelistCriteriaPropertyRestriction.URN);

            addOnlyLastVersionRestCriteria(queryBuilder, webCriteria, CodelistCriteriaPropertyRestriction.LATEST);
        }
        if (variableUrn != null) {
            String condition = fieldComparison(CodelistCriteriaPropertyRestriction.VARIABLE_URN, ComparisonOperator.EQ, variableUrn);
            appendConditionToQuery(queryBuilder, condition);
        }
        return queryBuilder.toString();
    }

    // -------------------------------------------------------------------------------------------------------------
    // CODE
    // -------------------------------------------------------------------------------------------------------------

    public static String buildQueryCode(MetamacWebCriteria webCriteria) {
        StringBuilder queryBuilder = new StringBuilder();
        if (webCriteria != null) {
            addSimpleRestCriteria(queryBuilder, webCriteria, CodeCriteriaPropertyRestriction.NAME, CodeCriteriaPropertyRestriction.ID, CodeCriteriaPropertyRestriction.URN);
        }
        return queryBuilder.toString();
    }

    // -------------------------------------------------------------------------------------------------------------
    // CATEGORY SCHEME
    // -------------------------------------------------------------------------------------------------------------

    public static String buildQueryCategoryScheme(MetamacWebCriteria webCriteria) {
        StringBuilder queryBuilder = new StringBuilder();
        if (webCriteria != null) {
            addSimpleRestCriteria(queryBuilder, webCriteria, CategorySchemeCriteriaPropertyRestriction.NAME, CategorySchemeCriteriaPropertyRestriction.ID,
                    CategorySchemeCriteriaPropertyRestriction.URN);
        }
        return queryBuilder.toString();
    }

    // -------------------------------------------------------------------------------------------------------------
    // CATEGORY
    // -------------------------------------------------------------------------------------------------------------

    public static String buildQueryCategory(SrmItemRestCriteria webCriteria) {
        StringBuilder queryBuilder = new StringBuilder();
        if (webCriteria != null) {
            addSimpleRestCriteria(queryBuilder, webCriteria, CategoryCriteriaPropertyRestriction.NAME, CategoryCriteriaPropertyRestriction.ID, CategoryCriteriaPropertyRestriction.URN);
            addSchemeRestCriteria(queryBuilder, webCriteria, CategoryCriteriaPropertyRestriction.CATEGORY_SCHEME_URN, CategoryCriteriaPropertyRestriction.CATEGORY_SCHEME_LATEST);
        }
        return queryBuilder.toString();
    }

    // -------------------------------------------------------------------------------------------------------------
    // CONCEPT SCHEME
    // -------------------------------------------------------------------------------------------------------------

    public static String buildQueryConceptScheme(MetamacWebCriteria webCriteria) {
        StringBuilder queryBuilder = new StringBuilder();
        if (webCriteria != null) {
            addSimpleRestCriteria(queryBuilder, webCriteria, ConceptSchemeCriteriaPropertyRestriction.NAME, ConceptSchemeCriteriaPropertyRestriction.ID, ConceptSchemeCriteriaPropertyRestriction.URN);
        }
        return queryBuilder.toString();
    }

    // -------------------------------------------------------------------------------------------------------------
    // CONCEPT
    // -------------------------------------------------------------------------------------------------------------

    public static String buildQueryConcept(SrmItemRestCriteria webCriteria) {
        StringBuilder queryBuilder = new StringBuilder();
        if (webCriteria != null) {
            addSimpleRestCriteria(queryBuilder, webCriteria, ConceptCriteriaPropertyRestriction.NAME, ConceptCriteriaPropertyRestriction.ID, ConceptCriteriaPropertyRestriction.URN);

            addSchemeRestCriteria(queryBuilder, webCriteria, ConceptCriteriaPropertyRestriction.CONCEPT_SCHEME_URN, ConceptCriteriaPropertyRestriction.CONCEPT_SCHEME_LATEST);
        }
        return queryBuilder.toString();
    }

    // -------------------------------------------------------------------------------------------------------------
    // ORGANISATION SCHEME
    // -------------------------------------------------------------------------------------------------------------

    public static String buildQueryOrganisationScheme(MetamacWebCriteria webCriteria, TypeExternalArtefactsEnum type) {
        StringBuilder queryBuilder = new StringBuilder();
        if (webCriteria != null) {
            addSimpleRestCriteria(queryBuilder, webCriteria, OrganisationSchemeCriteriaPropertyRestriction.NAME, OrganisationSchemeCriteriaPropertyRestriction.ID,
                    OrganisationSchemeCriteriaPropertyRestriction.URN);
        }

        addTypeRestCriteria(queryBuilder, type, OrganisationSchemeCriteriaPropertyRestriction.TYPE);

        return queryBuilder.toString();
    }

    // -------------------------------------------------------------------------------------------------------------
    // ORGANISATION
    // -------------------------------------------------------------------------------------------------------------

    public static String buildQueryOrganisation(SrmItemRestCriteria webCriteria, TypeExternalArtefactsEnum type) {
        StringBuilder queryBuilder = new StringBuilder();
        if (webCriteria != null) {
            addSimpleRestCriteria(queryBuilder, webCriteria, OrganisationCriteriaPropertyRestriction.NAME, OrganisationCriteriaPropertyRestriction.ID, OrganisationCriteriaPropertyRestriction.URN);

            addSchemeRestCriteria(queryBuilder, webCriteria, OrganisationCriteriaPropertyRestriction.ORGANISATION_SCHEME_URN, OrganisationCriteriaPropertyRestriction.ORGANISATION_SCHEME_LATEST);
        }

        addTypeRestCriteria(queryBuilder, type, OrganisationCriteriaPropertyRestriction.TYPE);

        return queryBuilder.toString();
    }

    @SuppressWarnings("rawtypes")
    private static void addSimpleRestCriteria(StringBuilder queryBuilder, HasSimpleCriteria criteria, Enum... fields) {
        String simpleCriteria = criteria.getCriteria();
        if (StringUtils.isNotBlank(simpleCriteria)) {
            StringBuilder conditionBuilder = new StringBuilder();

            List<String> conditions = new ArrayList<String>();
            for (Enum field : fields) {
                conditions.add(fieldComparison(field, ComparisonOperator.ILIKE, simpleCriteria));
            }

            conditionBuilder.append("(");
            for (int i = 0; i < conditions.size(); i++) {
                if (i > 0) {
                    conditionBuilder.append(" ").append(LogicalOperator.OR).append(" ");
                }
                conditionBuilder.append(conditions.get(i));
            }
            conditionBuilder.append(")");
            appendConditionToQuery(queryBuilder, conditionBuilder.toString());
        }
    }

    @SuppressWarnings("rawtypes")
    private static void addSchemeRestCriteria(StringBuilder queryBuilder, HasItemSchemeCriteria criteria, Enum schemeField, Enum lastVersionField) {
        String schemeUrn = criteria.getItemSchemeUrn();
        if (StringUtils.isNotBlank(schemeUrn)) {
            String schemeCondition = fieldComparison(schemeField, ComparisonOperator.EQ, schemeUrn);
            appendConditionToQuery(queryBuilder, schemeCondition);
        }

        if (criteria.isItemSchemeLastVersion()) {
            String lastVersionCondition = fieldComparison(lastVersionField, ComparisonOperator.EQ, criteria.isItemSchemeLastVersion());
            appendConditionToQuery(queryBuilder, lastVersionCondition);
        }

    }

    @SuppressWarnings("rawtypes")
    private static void addTypeRestCriteria(StringBuilder queryBuilder, TypeExternalArtefactsEnum type, Enum typeField) {
        if (type != null) {
            String typeCondition = fieldComparison(typeField, ComparisonOperator.EQ, type);
            appendConditionToQuery(queryBuilder, typeCondition);
        }
    }

    @SuppressWarnings("rawtypes")
    private static void addStatisticalOperationRestCriteria(StringBuilder queryBuilder, HasStatisticalOperationCriteria criteria, Enum statisticalOperationField) {
        if (StringUtils.isNotBlank(criteria.getStatisticalOperationUrn())) {
            String statOperCondition = fieldComparison(statisticalOperationField, ComparisonOperator.ILIKE, criteria.getStatisticalOperationUrn());
            appendConditionToQuery(queryBuilder, statOperCondition);
        }
    }

    @SuppressWarnings("rawtypes")
    private static void addOnlyLastVersionRestCriteria(StringBuilder queryBuilder, HasOnlyLastVersionCriteria criteria, Enum lastVersionField) {

        if (criteria.isOnlyLastVersion()) {
            String lastVersionCondition = fieldComparison(lastVersionField, ComparisonOperator.EQ, criteria.isOnlyLastVersion());
            appendConditionToQuery(queryBuilder, lastVersionCondition);
        }
    }

}
