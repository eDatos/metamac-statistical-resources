package org.siemac.metamac.statistical.resources.core.dataset.repositoryimpl;

import static org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.criteriaFor;
import static org.siemac.metamac.statistical.resources.core.base.domain.utils.RelatedResourceResultUtils.getRelatedResourceResultsFromLifeCycleResourceRows;
import static org.siemac.metamac.statistical.resources.core.base.domain.utils.RelatedResourceResultUtils.getRelatedResourceResultsFromSiemacResourceRows;
import static org.siemac.metamac.statistical.resources.core.base.domain.utils.RepositoryUtils.isLastPublishedVersionConditions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria.Operator;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.accessapi.FindByConditionAccess;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.criteria.utils.CriteriaUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.base.domain.utils.RelatedResourceResultUtils;
import org.siemac.metamac.statistical.resources.core.base.domain.utils.RepositoryUtils;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResource;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceResult;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionProperties;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for DatasetVersion
 */
@Repository("datasetVersionRepository")
public class DatasetVersionRepositoryImpl extends DatasetVersionRepositoryBase {

    public DatasetVersionRepositoryImpl() {
    }

    @Override
    public void buildPropertyCriteriaForDateProperty() {
        LocalDate desde = LocalDate.of(2021, 1, 1);

        CriteriaBuilder cb  = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(DatasetVersion.class);
        Root root = cq.from(DatasetVersion.class);

        Expression<java.sql.Date> fecha =
                cb.function("sdmx_to_date", java.sql.Date.class,
                            root.get("dateStart"));

        Predicate p = cb.greaterThanOrEqualTo(
                         fecha,
                         cb.parameter(java.sql.Date.class, "desde"));

        cq.where(p);

        TypedQuery q = getEntityManager().createQuery(cq);
        q.setParameter("desde", java.sql.Date.valueOf(desde));

        List lista = q.getResultList();
    }
    
    @Override
    public PagedResult<DatasetVersion> findByConditionCopy(List<ConditionalCriteria> conditions, PagingParameter pagingParameter) {
        FindByConditionAccess<DatasetVersion> ao = createFindByConditionAccess();
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<DatasetVersion> criteriaQuery = criteriaBuilder.createQuery(DatasetVersion.class);
        Root<DatasetVersion> root = criteriaQuery.from(DatasetVersion.class);
        List<Predicate> predicates = new ArrayList<>();
        List<Order> orderList = new ArrayList<Order>();
        Boolean distinct = false;
        for (ConditionalCriteria condition : conditions) {
            toPredicate(condition, criteriaBuilder, root, predicates, orderList, distinct);
        }
        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
        }
        if (!orderList.isEmpty()) {
            criteriaQuery.orderBy(orderList);
        }
        if (distinct) {
            criteriaQuery.distinct(distinct);
        }
        TypedQuery<DatasetVersion> typedQuery = getEntityManager().createQuery(criteriaQuery);
        List<DatasetVersion> result = typedQuery.getResultList();
        return null;
    }

    private PagedResult<DatasetVersion> getPagedResult(PagingParameter pagingParameter, FindByConditionAccess<DatasetVersion> ao, List<DatasetVersion> result) {
        int rowCount = PagedResult.UNKNOWN;
        int additionalRows = PagedResult.UNKNOWN;
        if (pagingParameter.getStartRow() != PagedResult.UNKNOWN && pagingParameter.getRealFetchCount() != 0) {
            int resultSize = result.size();
            if (resultSize > 0 && resultSize < pagingParameter.getRealFetchCount()) {
                // Not enough rows fetched - end of result reached, we should fill row
                // count and also additional pages without real counting.
                // Fill it even when nobody ask (isCountTotal), don't cost nothing and can be used on client side
                rowCount = pagingParameter.getStartRow() + resultSize;
                additionalRows = resultSize - pagingParameter.getRowCount();
                additionalRows = additionalRows < 0 ? 0 : additionalRows;
            } else {
                if (pagingParameter.isCountTotal()) {
                    ao.executeCount();
                    Number countNumber = ao.getResultCount();

                    rowCount = countNumber == null ? PagedResult.UNKNOWN : countNumber.intValue();
                }
                if (rowCount != PagedResult.UNKNOWN) {
                    additionalRows = rowCount - pagingParameter.getEndRow();
                    additionalRows = additionalRows < 0 ? 0 : additionalRows;
                } else {
                    additionalRows = resultSize - pagingParameter.getRowCount();
                    additionalRows = additionalRows < 0 ? 0 : additionalRows;
                }

                additionalRows = additionalRows > pagingParameter.getAdditionalResultRows() ? pagingParameter.getAdditionalResultRows() : additionalRows;
            }
        }

        return new PagedResult<DatasetVersion>(result, pagingParameter.getStartRow(), pagingParameter.getRowCount(), pagingParameter.getPageSize(), rowCount,
                additionalRows);
    }

    private void toPredicate(ConditionalCriteria condition, CriteriaBuilder criteriaBuilder, Root<?> root, List<Predicate> predicates, List<Order> orders, Boolean distinct) {
        LocalDate desde = LocalDate.of(2021, 1, 1);
        Expression<String> expression = null;
        if (condition.getPropertyFullName() != null && condition.getPropertyFullName().contains("dateStart")) {
            expression = criteriaBuilder.function("sdmx_to_date", String.class, root.get("dateStart"));
        }
        /*  A. Localizar el atributo (propertyPath puede tener varios tramos) */
        Path<?> exp = resolvePath(root, condition.getPropertyFullName(), getEntityManager().getMetamodel());
        /*  B. Crear el Predicate según el operador ----------------------- */
        Object valor = condition.getFirstOperant();

        /*  Null-safe: equal / notEqual con null -> isNull / isNotNull       */
        if (valor == null && condition.getOperator() == Operator.Equal) {
            predicates.add(criteriaBuilder.isNull(exp));
        }

        switch (condition.getOperator()) {
            case Equal: // =
                predicates.add( criteriaBuilder.equal(exp, valor));
                break;
            case GreatThan: // >
                predicates.add( criteriaBuilder.greaterThan(cast(exp), (Comparable) valor));
                break;
            case GreatThanOrEqual: // >=
                predicates.add( criteriaBuilder.greaterThanOrEqualTo(cast(exp), (Comparable) valor));
                break;
            case LessThan: // <
                if (expression != null) {
                    predicates.add( criteriaBuilder.lessThan(cast(expression), java.sql.Date.valueOf(desde)));
                }
                predicates.add( criteriaBuilder.lessThan(cast(exp), (Comparable) valor));
                break;
            case LessThanOrEqual: // <=
                predicates.add( criteriaBuilder.lessThanOrEqualTo(cast(exp), (Comparable) valor));
                break;
            case Like: // %_%
                predicates.add( criteriaBuilder.like(exp.as(String.class), valor.toString()));
                break;
            case IgnoreCaseLike: // %_% (ignore case)
                predicates.add( criteriaBuilder.like(criteriaBuilder.lower(exp.as(String.class)), valor.toString().toLowerCase()));
                break;
            case In: // in (...)
                predicates.add( exp.in((Collection<?>) valor));
                break;
            case OrderAsc:
                orders.add(criteriaBuilder.asc(resolvePath(root, condition)));
                break;
            case OrderDesc:
                orders.add(criteriaBuilder.desc(resolvePath(root, condition)));
                break;
            case DistinctRoot:
                distinct = true;
                break;
            default:
                throw new UnsupportedOperationException("Operador no soportado: " + condition.getOperator());
        }
    }

    private Path<?> resolvePath(Root<?> root, ConditionalCriteria cc) {

        String[] parts;

        // 1) Si ya tienes propertyPath úsalo, si no divide el fullName
        if (cc.getPropertyPath() != null && cc.getPropertyPath().length > 0) {
            parts = cc.getPropertyPath();
        } else {
            parts = cc.getPropertyFullName().split("\\.");
        }

        Path<?> path = root;      // empieza en la raíz

        // 2) recorre cada tramo menos el último:
        for (int i = 0; i < parts.length - 1; i++) {
            if (path instanceof From) {
                System.out.println(path);
            }
            path = (path instanceof From)
                   ? ((From<?, ?>) path).join(parts[i], JoinType.LEFT) // relación
                   : path.get(parts[i]);                               // embebido
        }

        // 3) último tramo – atributo simple
        return path.get(parts[parts.length - 1]);
    }

    private Path<?> resolvePath(Root<?> root, String propertyFullName, Metamodel mm) {
        if (propertyFullName == null) {
            return root;
        }
        Set<String> SQL_TYPE_TOKENS = new HashSet<String>(Arrays.asList("datetime", "timestamp", "date", "varchar", "char", "int" /* … */
        ));
        Path<?> path = root;
        Class<?> jType = root.getJavaType();

        /* 1. Dividir la ruta y limpiar tokens “raro-tipo” --------------- */
        List<String> cleaned = new ArrayList<String>();
        for (String part : propertyFullName.split("\\.")) {
            if (part == null || part.isEmpty())
                continue; // vacío
            if (SQL_TYPE_TOKENS.contains(part.toLowerCase(Locale.ENGLISH)))
                continue; // tipo SQL
            cleaned.add(part);
        }

        /* 2. Recorrer tokens limpios (igual que antes) ------------------ */
        for (int i = 0; i < cleaned.size(); i++) {
            String part = cleaned.get(i);

            // omite el nombre de la entidad raíz si viene repetido
            if (part.equalsIgnoreCase(jType.getSimpleName())) {
                continue;
            }

            EntityType<?> et;
            try {
                et = mm.entity(jType);
            } catch (IllegalArgumentException ex) { // jType ya no es entidad
                throw new IllegalArgumentException("No se puede navegar dentro de un atributo básico (" + jType.getName() + ") usando '" + part + "' en la ruta '" + propertyFullName + '\'', ex);
            }

            Attribute<?, ?> attr = et.getAttribute(part);

            path = attr.isAssociation() ? ((From<?, ?>) path).join(part, JoinType.LEFT) : path.get(part);

            jType = attr.getJavaType(); // tipo para el siguiente salto
        }
        return path;
    }

    private <T extends Comparable<? super T>> Expression<T> cast(Expression<?> exp) {
        return (Expression<T>) exp;
    }

    @Override
    public DatasetVersion retrieveByUrn(String urn) throws MetamacException {
        // Prepare criteria
        List<ConditionalCriteria> condition = criteriaFor(DatasetVersion.class).withProperty(DatasetVersionProperties.siemacMetadataStatisticalResource().urn()).eq(urn).distinctRoot().build();

        // Find
        List<DatasetVersion> result = findByCondition(condition);

        // Check for unique result and return
        if (result.size() == 0) {
            throw new MetamacException(ServiceExceptionType.DATASET_VERSION_NOT_FOUND, urn);
        } else if (result.size() > 1) {
            // Exists a database constraint that makes URN unique
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "More than one dataset with urn " + urn);
        }

        return result.get(0);
    }

    @Override
    public DatasetVersion retrieveByUrnPublished(String urn) throws MetamacException {
        // Prepare criteria
        // @formatter:off
        List<ConditionalCriteria> condition = criteriaFor(DatasetVersion.class)
            .withProperty(DatasetVersionProperties.siemacMetadataStatisticalResource().urn()).eq(urn)
            .and()
            .withProperty(DatasetVersionProperties.siemacMetadataStatisticalResource().procStatus()).eq(ProcStatusEnum.PUBLISHED)
            .distinctRoot().build();
        // @formatter:on

        // Find
        List<DatasetVersion> result = findByCondition(condition);

        // Check for unique result and return
        if (result.size() == 0) {
            throw new MetamacException(ServiceExceptionType.DATASET_VERSION_NOT_FOUND, urn);
        } else if (result.size() > 1) {
            // Exists a database constraint that makes URN unique
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "More than one dataset with urn " + urn);
        }

        return result.get(0);
    }

    @Override
    public DatasetVersion retrieveLastVersion(String datasetUrn) throws MetamacException {
        // Prepare criteria
        // @formatter:off
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(DatasetVersion.class)
            .withProperty(DatasetVersionProperties.dataset().identifiableStatisticalResource().urn()).eq(datasetUrn)
            .and()
            .withProperty(DatasetVersionProperties.siemacMetadataStatisticalResource().lastVersion()).eq(Boolean.TRUE)
            .distinctRoot().build();
        // @formatter:on

        PagingParameter paging = PagingParameter.rowAccess(0, 1);
        // Find
        PagedResult<DatasetVersion> result = findByCondition(conditions, paging);

        // Check for unique result and return. We have at least one datasetVersion
        if (result.getRowCount() == 0) {
            throw new MetamacException(ServiceExceptionType.DATASET_LAST_VERSION_NOT_FOUND, datasetUrn);
        }

        return result.getValues().get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public DatasetVersion retrieveLastPublishedVersion(String datasetUrn) throws MetamacException {
        // Prepare criteria
        Date now = new DateTime().toDate();
        // @formatter:off
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(DatasetVersion.class)
            .withProperty(DatasetVersionProperties.dataset().identifiableStatisticalResource().urn()).eq(datasetUrn)
            .and()
            .withProperty(DatasetVersionProperties.siemacMetadataStatisticalResource().procStatus()).eq(ProcStatusEnum.PUBLISHED)
            .and()
                .lbrace()
                    .withProperty(CriteriaUtils.getDatetimeLeafPropertyEmbedded(DatasetVersionProperties.siemacMetadataStatisticalResource().validTo(), DatasetVersion.class)).isNull()
                    .or()
                    .withProperty(CriteriaUtils.getDatetimeLeafPropertyEmbedded(DatasetVersionProperties.siemacMetadataStatisticalResource().validTo(), DatasetVersion.class)).greaterThan(now)
                .rbrace()
            .distinctRoot().build();
        // @formatter:on
        PagingParameter paging = PagingParameter.rowAccess(0, 1);
        // Find
        PagedResult<DatasetVersion> result = findByCondition(conditions, paging);

        // Check for unique result and return
        if (result.getRowCount() != 0) {
            return result.getValues().get(0);
        } else {
            return null;
        }
    }

    @Override
    public boolean isLastVersion(String datasetVersionUrn) throws MetamacException {
        DatasetVersion datasetVersion = retrieveByUrn(datasetVersionUrn);
        DatasetVersion lastVersion = retrieveLastVersion(datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn());

        return lastVersion.getSiemacMetadataStatisticalResource().getUrn().equals(datasetVersionUrn);
    }

    @Override
    public DatasetVersion retrieveByVersion(Long statisticalResourceId, String versionLogic) throws MetamacException {
        // Prepare criteria
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(DatasetVersion.class).withProperty(DatasetVersionProperties.dataset().id()).eq(statisticalResourceId)
                .withProperty(DatasetVersionProperties.siemacMetadataStatisticalResource().versionLogic()).eq(versionLogic).distinctRoot().build();

        // Find
        List<DatasetVersion> result = findByCondition(conditions);

        // Check for unique result and return
        if (result.size() == 0) {
            throw new MetamacException(ServiceExceptionType.DATASET_VERSION_NOT_FOUND, statisticalResourceId, versionLogic);
        } else if (result.size() > 1) {
            // Exists a database constraint that makes URN unique
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "More than one dataset version with id " + statisticalResourceId + " and versionLogic " + versionLogic + " found");
        }
        return result.get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> retrieveDimensionsIds(DatasetVersion datasetVersion) throws MetamacException {
      //@formatter:off
        Query query = getEntityManager().createNativeQuery(
                "select code.DSD_COMPONENT_ID " +
                "from TB_CODE_DIMENSIONS code " +
                "join (" +
                "    select DSD_COMPONENT_ID, max(id) max_id" +
                "    from TB_CODE_DIMENSIONS c " +
                "    where DATASET_VERSION_FK = :datasetVersionFk " +
                "    group by DSD_COMPONENT_ID" +
                ") code2 " +
                "ON code.DSD_COMPONENT_ID = code2.DSD_COMPONENT_ID AND code.id = code2.max_id " +
                "order by code.id ");
        //@formatter:on
        query.setParameter("datasetVersionFk", datasetVersion.getId());
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RelatedResourceResult> retrieveIsRequiredByOnlyLastPublished(DatasetVersion datasetVersion) throws MetamacException {
        //@formatter:off
        String queryLinkedDirectlyToDatasetVersion =
            "         (query.Dataset_Version_Fk = :datasetVersionFk) ";

        String queryLinkedToDatasetAndDatasetVersionLastPublishedVersion =
            "         (query.dataset_fk = dataset_version.dataset_fk" +
            "           And " + RepositoryUtils.buildLastPublishedVersionCondition("stat_dataset") + ") ";

        Query query = getEntityManager().createNativeQuery(
                "select stat.code,"+
                "       stat.urn, " +
                "       operItems.code as statOperCode, " +
                "       operItems.urn as statOperUrn, " +
                "       maintainerItems.code_nested, " +
                "       stat.version_logic, " +
                "       loc.locale, " +
                "       loc.label, " +
                "       stat.type " + // will be null, lifecycle don't have type
                " from  Tb_External_Items operItems, " +
                "       Tb_External_Items maintainerItems, " +
                "       Tb_Queries_Versions query Inner Join Tb_Stat_Resources stat On query.lifecycle_resource_fk = stat.Id, " +
                "       Tb_localised_strings loc, "+
                "       Tb_datasets_versions dataset_version inner join tb_stat_resources stat_dataset "+
                "           on dataset_version.siemac_resource_fk = stat_dataset.id " +
                " Where stat.Stat_Operation_Fk = operItems.Id " +
                "   And stat.Maintainer_Fk = maintainerItems.Id "+
                "   And stat.Title_Fk = loc.International_String_Fk " +
                "   And dataset_version.id = :datasetVersionFk " +
                "   And ( " +
                "           "+queryLinkedDirectlyToDatasetVersion+" "+
                "       OR "+
                "           "+queryLinkedToDatasetAndDatasetVersionLastPublishedVersion+" "+
                "       ) " +
                "   And  " + isLastPublishedVersionConditions);

        //     @formatter:on
        query.setParameter("publishedProcStatus", ProcStatusEnum.PUBLISHED.name());
        query.setParameter("datasetVersionFk", datasetVersion.getId());
        query.setParameter("now", new DateTime().toDate());

        List<Object> rows = query.getResultList();
        List<RelatedResourceResult> resources = getRelatedResourceResultsFromLifeCycleResourceRows(rows, TypeRelatedResourceEnum.QUERY_VERSION);
        return resources;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RelatedResourceResult> retrieveIsRequiredBy(DatasetVersion datasetVersion) throws MetamacException {
        //     @formatter:off
        String queryLinkedDirectlyToDatasetVersion =
            "         (query.Dataset_Version_Fk = :datasetVersionFk) ";

        String queryLinkedToDatasetAndDatasetVersionIsLastVersion =
            "         (query.dataset_fk = dataset_version.dataset_fk" +
            "           And stat_dataset.Last_Version = true ) ";

        String queryLinkedToDatasetAndDatasetVersionLastPublishedVersion =
            "         (query.dataset_fk = dataset_version.dataset_fk" +
            "           And "+RepositoryUtils.buildLastPublishedVersionCondition("stat_dataset")+ ") ";

        Query query = getEntityManager().createNativeQuery(
                "select stat.code, " +
                "       stat.urn, " +
                "       operItems.code as statOperCode, " +
                "       operItems.urn as statOperUrn, " +
                "       maintainerItems.code_nested, " +
                "       stat.version_logic, " +
                "       loc.locale, " +
                "       loc.label, " +
                "       stat.type " + // will be null, lifecycle don't have type
                "from   Tb_External_Items operItems, " +
                "       Tb_External_Items maintainerItems, " +
                "       Tb_Queries_Versions query Inner Join Tb_Stat_Resources stat " +
                "           On query.lifecycle_resource_fk = stat.Id, " +
                "       Tb_localised_strings loc, " +
                "       Tb_datasets_versions dataset_version inner join tb_stat_resources stat_dataset "+
                "           on dataset_version.siemac_resource_fk = stat_dataset.id " +
        		"Where stat.Stat_Operation_Fk = operItems.Id " +
        		"     And stat.Maintainer_Fk = maintainerItems.Id " +
        		"     And stat.Title_Fk = loc.International_String_Fk " +
        		"     And dataset_version.id = :datasetVersionFk " +
        		"     And ( "+
        		            queryLinkedDirectlyToDatasetVersion +
        		"         OR "+
        		            queryLinkedToDatasetAndDatasetVersionIsLastVersion +
	            "         OR "+
        		            queryLinkedToDatasetAndDatasetVersionLastPublishedVersion +
	            "          ) ");

        //     @formatter:on
        query.setParameter("publishedProcStatus", ProcStatusEnum.PUBLISHED.name());
        query.setParameter("now", new DateTime().toDate());
        query.setParameter("datasetVersionFk", datasetVersion.getId());

        List<Object> rows = query.getResultList();
        List<RelatedResourceResult> resources = getRelatedResourceResultsFromLifeCycleResourceRows(rows, TypeRelatedResourceEnum.QUERY_VERSION);
        return resources;
    }

    @Override
    public List<RelatedResourceResult> retrieveIsPartOf(DatasetVersion datasetVersion) throws MetamacException {
        return retrieveIsPartOf(datasetVersion, false);
    }

    @Override
    public List<RelatedResourceResult> retrieveIsPartOfOnlyLastPublished(DatasetVersion datasetVersion) throws MetamacException {
        return retrieveIsPartOf(datasetVersion, true);
    }

    @SuppressWarnings("unchecked")
    private List<RelatedResourceResult> retrieveIsPartOf(DatasetVersion datasetVersion, boolean onlyLastPublished) throws MetamacException {
        //     @formatter:off
        Query query = getEntityManager().createNativeQuery(
                "SELECT     distinct stat.code, " +
                "           stat.urn, " +
                "           operation.code AS operCode, " +
                "           operation.urn AS operUrn,  " +
                "           maintainer.code_nested AS code_nested,  " +
                "           stat.version_logic, " +
                "           loc.locale, " +
                "           loc.label, " +
                "           stat.type " +
                "FROM       tb_stat_resources stat " +
                "INNER JOIN ( " +

                    // Publications
                    "SELECT pub.siemac_resource_fk " +
                    "FROM tb_publications_versions pub " +
                    "INNER JOIN tb_elements_levels elem on elem.publication_version_all_fk = pub.ID " +
                    "INNER JOIN tb_cubes cubes on cubes.ID = elem.table_fk " +

                    // - Datasets
                    "INNER JOIN tb_datasets dataset on cubes.dataset_fk = dataset.ID " +
                    "INNER JOIN tb_datasets_versions dataset_version on dataset_version.dataset_fk = dataset.ID " +
                    "INNER JOIN tb_stat_resources stat_dataset  ON dataset_version.siemac_resource_fk = stat_dataset.ID " +
                    "WHERE " + RepositoryUtils.buildLastPublishedVersionCondition("stat_dataset", onlyLastPublished) +
                    "AND     dataset_version.ID = :datasetVersionFk " +

                    "UNION " +

                    // Multidatasets
                    "SELECT mul.siemac_resource_fk " +
                    "FROM tb_multidatasets_versions  mul " +
                    "INNER JOIN tb_md_cubes cubes on cubes.MULTIDATASET_VERSION_FK = mul.id " +

                    // - Datasets
                    "INNER JOIN tb_datasets dataset on cubes.dataset_fk = dataset.ID " +
                    "INNER JOIN tb_datasets_versions dataset_version on dataset_version.dataset_fk = dataset.ID " +
                    "INNER JOIN tb_stat_resources stat_dataset  ON dataset_version.siemac_resource_fk = stat_dataset.ID " +
                    "WHERE " + RepositoryUtils.buildLastPublishedVersionCondition("stat_dataset", onlyLastPublished) +
                    "AND     dataset_version.ID = :datasetVersionFk " +

                ") as resource on resource.siemac_resource_fk = stat.ID " +

                "inner join tb_localised_strings loc on  stat.title_fk = loc.international_string_fk " +
                "inner join tb_external_items operation on operation.ID = stat.stat_operation_fk " +
                "inner join tb_external_items maintainer on maintainer.id = stat.maintainer_fk " +
                "where " + RepositoryUtils.buildLastPublishedVersionCondition("stat", onlyLastPublished));
            //     @formatter:on
        query.setParameter("datasetVersionFk", datasetVersion.getId());
        if (onlyLastPublished) {
            query.setParameter("publishedProcStatus", ProcStatusEnum.PUBLISHED.name());
            query.setParameter("now", new DateTime().toDate());
        }

        List<Object> rows = query.getResultList();
        List<RelatedResourceResult> resources = getRelatedResourceResultsFromSiemacResourceRows(rows);
        return resources;
    }

    @Override
    public RelatedResourceResult retrieveIsReplacedByVersionOnlyLastPublished(DatasetVersion datasetVersion) throws MetamacException {
        DatasetVersion next = datasetVersion;
        DatasetVersion replacing = null;

        while (next != null && next.getSiemacMetadataStatisticalResource().getIsReplacedByVersion() != null) {
            next = next.getSiemacMetadataStatisticalResource().getIsReplacedByVersion().getDatasetVersion();
            if (next.getSiemacMetadataStatisticalResource().getProcStatus() == ProcStatusEnum.PUBLISHED) {
                replacing = next;
            }
        }
        RelatedResourceResult result = RelatedResourceResultUtils.from(replacing, TypeRelatedResourceEnum.DATASET_VERSION);
        return result;
    }

    @Override
    public RelatedResourceResult retrieveIsReplacedByOnlyLastPublished(DatasetVersion datasetVersion) throws MetamacException {
        DatasetVersion next = datasetVersion;
        DatasetVersion replacing = null;

        while (next != null && next.getSiemacMetadataStatisticalResource().getIsReplacedBy() != null) {
            next = next.getSiemacMetadataStatisticalResource().getIsReplacedBy().getDatasetVersion();
            if (next.getSiemacMetadataStatisticalResource().getProcStatus() == ProcStatusEnum.PUBLISHED) {
                replacing = next;
            }
        }
        RelatedResourceResult result = RelatedResourceResultUtils.from(replacing, TypeRelatedResourceEnum.DATASET_VERSION);
        return result;
    }

    @Override
    public RelatedResourceResult retrieveIsReplacedByOnlyIfPublished(DatasetVersion datasetVersion) throws MetamacException {
        RelatedResourceResult result = null;
        if (datasetVersion != null && datasetVersion.getSiemacMetadataStatisticalResource().getIsReplacedBy() != null) {
            DatasetVersion replacing = datasetVersion.getSiemacMetadataStatisticalResource().getIsReplacedBy().getDatasetVersion();
            if (ProcStatusEnum.PUBLISHED == replacing.getSiemacMetadataStatisticalResource().getProcStatus()) {
                result = RelatedResourceResultUtils.from(replacing, TypeRelatedResourceEnum.DATASET_VERSION);
            }
        }
        return result;
    }

    @Override
    public RelatedResourceResult retrieveIsReplacedByVersionOnlyIfPublished(DatasetVersion datasetVersion) throws MetamacException {
        RelatedResourceResult result = null;
        if (datasetVersion != null && datasetVersion.getSiemacMetadataStatisticalResource().getIsReplacedByVersion() != null) {
            DatasetVersion replacing = datasetVersion.getSiemacMetadataStatisticalResource().getIsReplacedByVersion().getDatasetVersion();
            if (ProcStatusEnum.PUBLISHED == replacing.getSiemacMetadataStatisticalResource().getProcStatus()) {
                result = RelatedResourceResultUtils.from(replacing, TypeRelatedResourceEnum.DATASET_VERSION);
            }
        }
        return result;
    }

    @Override
    public RelatedResourceResult retrieveIsReplacedByVersion(DatasetVersion datasetVersion) throws MetamacException {
        RelatedResource replacingRelated = datasetVersion.getSiemacMetadataStatisticalResource().getIsReplacedByVersion();
        RelatedResourceResult replacing = null;
        if (replacingRelated != null && TypeRelatedResourceEnum.DATASET_VERSION == replacingRelated.getType()) {
            replacing = RelatedResourceResultUtils.from(replacingRelated.getDatasetVersion(), TypeRelatedResourceEnum.DATASET_VERSION);
        }

        return replacing;
    }

    @Override
    public RelatedResourceResult retrieveIsReplacedBy(DatasetVersion datasetVersion) throws MetamacException {
        RelatedResource replacingRelated = datasetVersion.getSiemacMetadataStatisticalResource().getIsReplacedBy();
        RelatedResourceResult replacing = null;
        if (replacingRelated != null && TypeRelatedResourceEnum.DATASET_VERSION == replacingRelated.getType()) {
            replacing = RelatedResourceResultUtils.from(replacingRelated.getDatasetVersion(), TypeRelatedResourceEnum.DATASET_VERSION);
        }

        return replacing;
    }

}
