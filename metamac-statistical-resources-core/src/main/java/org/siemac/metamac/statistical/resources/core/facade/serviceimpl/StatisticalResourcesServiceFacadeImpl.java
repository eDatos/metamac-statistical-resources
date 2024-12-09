package org.siemac.metamac.statistical.resources.core.facade.serviceimpl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.avro.specific.SpecificRecordBase;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.errorhandling.ApplicationException;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.criteria.SculptorCriteria;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ContentConstraint;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Key;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.RegionReference;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.sso.utils.SecurityUtils;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.common.mapper.CommonDo2DtoMapper;
import org.siemac.metamac.statistical.resources.core.common.utils.DsdProcessor;
import org.siemac.metamac.statistical.resources.core.common.utils.DsdProcessor.DsdAttribute;
import org.siemac.metamac.statistical.resources.core.constraint.api.ConstraintsService;
import org.siemac.metamac.statistical.resources.core.constraint.mapper.ConstraintDto2RestMapper;
import org.siemac.metamac.statistical.resources.core.constraint.mapper.ConstraintRest2DtoMapper;
import org.siemac.metamac.statistical.resources.core.dataset.criteria.mapper.DatasetMetamacCriteria2SculptorCriteriaMapper;
import org.siemac.metamac.statistical.resources.core.dataset.criteria.mapper.DatasetSculptorCriteria2MetamacCriteriaMapper;
import org.siemac.metamac.statistical.resources.core.dataset.criteria.mapper.DatasetVersionMetamacCriteria2SculptorCriteriaMapper;
import org.siemac.metamac.statistical.resources.core.dataset.criteria.mapper.DatasetVersionSculptorCriteria2MetamacCriteriaMapper;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Categorisation;
import org.siemac.metamac.statistical.resources.core.dataset.domain.CodeDimension;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionProperties;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Datasource;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DimensionRepresentationMapping;
import org.siemac.metamac.statistical.resources.core.dataset.domain.StatisticOfficiality;
import org.siemac.metamac.statistical.resources.core.dataset.mapper.DatasetDo2DtoMapper;
import org.siemac.metamac.statistical.resources.core.dataset.mapper.DatasetDto2DoMapper;
import org.siemac.metamac.statistical.resources.core.dataset.mapper.KafkaMapper;
import org.siemac.metamac.statistical.resources.core.dataset.mapper.StatRepoDto2StatisticalResourcesDtoMapper;
import org.siemac.metamac.statistical.resources.core.dataset.mapper.StatisticalResourcesDto2StatRepoDtoMapper;
import org.siemac.metamac.statistical.resources.core.dto.BasicVersionableStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.constraint.ContentConstraintDto;
import org.siemac.metamac.statistical.resources.core.dto.constraint.RegionValueDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.CategorisationDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionMainCoveragesDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasourceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DimensionRepresentationMappingDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdDimensionDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.StatisticOfficialityDto;
import org.siemac.metamac.statistical.resources.core.dto.multidataset.MultidatasetCubeDto;
import org.siemac.metamac.statistical.resources.core.dto.multidataset.MultidatasetVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.multidataset.MultidatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.ChapterDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.CubeDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.PublicationStructureDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.PublicationVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.PublicationVersionDto;
import org.siemac.metamac.statistical.resources.core.dto.query.CodeItemDto;
import org.siemac.metamac.statistical.resources.core.dto.query.PurposeDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.utils.IstacTimeGranularityCodeEnum;
import org.siemac.metamac.statistical.resources.core.enume.utils.IstacTimeUtils;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.invocation.service.SrmRestInternalService;
import org.siemac.metamac.statistical.resources.core.io.serviceimpl.validators.CodeHierarchy;
import org.siemac.metamac.statistical.resources.core.io.serviceimpl.validators.ConstraintsValidator;
import org.siemac.metamac.statistical.resources.core.io.serviceimpl.validators.ValidateDataVersusDsd;
import org.siemac.metamac.statistical.resources.core.lifecycle.serviceapi.LifecycleService;
import org.siemac.metamac.statistical.resources.core.multidataset.criteria.mapper.MultidatasetMetamacCriteria2SculptorCriteriaMapper;
import org.siemac.metamac.statistical.resources.core.multidataset.criteria.mapper.MultidatasetSculptorCriteria2MetamacCriteriaMapper;
import org.siemac.metamac.statistical.resources.core.multidataset.criteria.mapper.MultidatasetVersionMetamacCriteria2SculptorCriteriaMapper;
import org.siemac.metamac.statistical.resources.core.multidataset.criteria.mapper.MultidatasetVersionSculptorCriteria2MetamacCriteriaMapper;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetCube;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersionProperties;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.multidataset.mapper.MultidatasetDo2DtoMapper;
import org.siemac.metamac.statistical.resources.core.multidataset.mapper.MultidatasetDto2DoMapper;
import org.siemac.metamac.statistical.resources.core.publication.criteria.mapper.PublicationMetamacCriteria2SculptorCriteriaMapper;
import org.siemac.metamac.statistical.resources.core.publication.criteria.mapper.PublicationSculptorCriteria2MetamacCriteriaMapper;
import org.siemac.metamac.statistical.resources.core.publication.criteria.mapper.PublicationVersionMetamacCriteria2SculptorCriteriaMapper;
import org.siemac.metamac.statistical.resources.core.publication.criteria.mapper.PublicationVersionSculptorCriteria2MetamacCriteriaMapper;
import org.siemac.metamac.statistical.resources.core.publication.domain.Chapter;
import org.siemac.metamac.statistical.resources.core.publication.domain.Cube;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersionProperties;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersionRepository;
import org.siemac.metamac.statistical.resources.core.publication.mapper.PublicationDo2DtoMapper;
import org.siemac.metamac.statistical.resources.core.publication.mapper.PublicationDto2DoMapper;
import org.siemac.metamac.statistical.resources.core.query.criteria.mapper.QueryMetamacCriteria2SculptorCriteriaMapper;
import org.siemac.metamac.statistical.resources.core.query.criteria.mapper.QuerySculptorCriteria2MetamacCriteriaMapper;
import org.siemac.metamac.statistical.resources.core.query.criteria.mapper.QueryVersionMetamacCriteria2SculptorCriteriaMapper;
import org.siemac.metamac.statistical.resources.core.query.criteria.mapper.QueryVersionSculptorCriteria2MetamacCriteriaMapper;
import org.siemac.metamac.statistical.resources.core.query.domain.Purpose;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersionProperties;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersionRepository;
import org.siemac.metamac.statistical.resources.core.query.mapper.QueryDo2DtoMapper;
import org.siemac.metamac.statistical.resources.core.query.mapper.QueryDto2DoMapper;
import org.siemac.metamac.statistical.resources.core.security.ConstraintsSecurityUtils;
import org.siemac.metamac.statistical.resources.core.security.DatasetsSecurityUtils;
import org.siemac.metamac.statistical.resources.core.security.MultidatasetsSecurityUtils;
import org.siemac.metamac.statistical.resources.core.security.PublicationsSecurityUtils;
import org.siemac.metamac.statistical.resources.core.security.QueriesSecurityUtils;
import org.siemac.metamac.statistical.resources.core.security.shared.SharedDatasetsSecurityUtils;
import org.siemac.metamac.statistical.resources.core.security.shared.SharedMultidatasetsSecurityUtils;
import org.siemac.metamac.statistical.resources.core.security.shared.SharedPublicationsSecurityUtils;
import org.siemac.metamac.statistical.resources.core.security.shared.SharedQueriesSecurityUtils;
import org.siemac.metamac.statistical.resources.core.task.domain.TaskInfoDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceDto;
import es.gobcan.istac.edatos.dataset.repository.dto.CodeDimensionDto;
import es.gobcan.istac.edatos.dataset.repository.service.DatasetRepositoriesServiceFacade;

/**
 * Implementation of StatisticalResourcesServiceFacade.
 */
@Service("statisticalResourcesServiceFacade")
public class StatisticalResourcesServiceFacadeImpl extends StatisticalResourcesServiceFacadeImplBase {

    private static Logger                                             logger = LoggerFactory.getLogger(StatisticalResourcesServiceFacadeImpl.class);

    @Autowired
    private CommonDo2DtoMapper                                        commonDo2DtoMapper;

    @Autowired
    private QueryDo2DtoMapper                                         queryDo2DtoMapper;

    @Autowired
    private QueryDto2DoMapper                                         queryDto2DoMapper;

    @Autowired
    private DatasetDo2DtoMapper                                       datasetDo2DtoMapper;

    @Autowired
    private DatasetDto2DoMapper                                       datasetDto2DoMapper;

    @Autowired
    private PublicationDo2DtoMapper                                   publicationDo2DtoMapper;

    @Autowired
    private PublicationDto2DoMapper                                   publicationDto2DoMapper;

    @Autowired
    private MultidatasetDto2DoMapper                                  multidatasetDto2DoMapper;

    @Autowired
    private MultidatasetDo2DtoMapper                                  multidatasetDo2DtoMapper;

    @Autowired
    private ConstraintDto2RestMapper                                  constraintDto2RestMapper;

    @Autowired
    private ConstraintRest2DtoMapper                                  constraintRest2DtoMapper;

    @Autowired
    private QueryVersionMetamacCriteria2SculptorCriteriaMapper        queryVersionMetamacCriteria2SculptorCriteriaMapper;

    @Autowired
    private QueryVersionSculptorCriteria2MetamacCriteriaMapper        queryVersionSculptorCriteria2MetamacCriteriaMapper;

    @Autowired
    private QueryMetamacCriteria2SculptorCriteriaMapper               queryMetamacCriteria2SculptorCriteriaMapper;

    @Autowired
    private QuerySculptorCriteria2MetamacCriteriaMapper               querySculptorCriteria2MetamacCriteriaMapper;

    @Autowired
    private DatasetVersionMetamacCriteria2SculptorCriteriaMapper      datasetVersionMetamacCriteria2SculptorCriteriaMapper;

    @Autowired
    private DatasetVersionSculptorCriteria2MetamacCriteriaMapper      datasetVersionSculptorCriteria2MetamacCriteriaMapper;

    @Autowired
    private DatasetMetamacCriteria2SculptorCriteriaMapper             datasetMetamacCriteria2SculptorCriteriaMapper;

    @Autowired
    private DatasetSculptorCriteria2MetamacCriteriaMapper             datasetSculptorCriteria2MetamacCriteriaMapper;

    @Autowired
    private PublicationVersionMetamacCriteria2SculptorCriteriaMapper  publicationVersionMetamacCriteria2SculptorCriteriaMapper;

    @Autowired
    private PublicationSculptorCriteria2MetamacCriteriaMapper         publicationSculptorCriteria2MetamacCriteriaMapper;

    @Autowired
    private PublicationMetamacCriteria2SculptorCriteriaMapper         publicationMetamacCriteria2SculptorCriteriaMapper;

    @Autowired
    private PublicationVersionSculptorCriteria2MetamacCriteriaMapper  publicationVersionSculptorCriteria2MetamacCriteriaMapper;

    @Autowired
    private MultidatasetVersionMetamacCriteria2SculptorCriteriaMapper multidatasetVersionMetamacCriteria2SculptorCriteriaMapper;

    @Autowired
    private MultidatasetVersionSculptorCriteria2MetamacCriteriaMapper multidatasetVersionSculptorCriteria2MetamacCriteriaMapper;

    @Autowired
    MultidatasetMetamacCriteria2SculptorCriteriaMapper                multidatasetMetamacCriteria2SculptorCriteriaMapper;

    @Autowired
    MultidatasetSculptorCriteria2MetamacCriteriaMapper                multidatasetSculptorCriteria2MetamacCriteriaMapper;

    @Autowired
    private LifecycleService<DatasetVersion>                          datasetLifecycleService;

    @Autowired
    private LifecycleService<PublicationVersion>                      publicationLifecycleService;

    @Autowired
    private LifecycleService<QueryVersion>                            queryLifecycleService;

    @Autowired
    private LifecycleService<MultidatasetVersion>                     multidatasetLifecycleService;

    @Autowired
    private StatisticalResourcesDto2StatRepoDtoMapper                 statisticalResourcesDto2StatRepoDtoMapper;

    @Autowired
    private StatRepoDto2StatisticalResourcesDtoMapper                 statRepoDto2StatisticalResourcesDtoMapper;

    @Autowired
    private SrmRestInternalService                                    srmRestInternalService;

    @Autowired
    private ConstraintsService                                        constraintsService;

    @Autowired
    private DatasetVersionRepository                                  datasetVersionRepository;
    @Autowired
    private PublicationVersionRepository                              publicationVersionRepository;
    @Autowired
    private QueryVersionRepository                                    queryVersionRepository;
    @Autowired
    private MultidatasetVersionRepository                             multidatasetVersionRepository;

    @Autowired
    private DatasetRepositoriesServiceFacade                          datasetRepositoriesServiceFacade;

    @Autowired
    private KafkaMapper                                               kafkaMapper;

    public StatisticalResourcesServiceFacadeImpl() {
    }

    // ------------------------------------------------------------------------
    // QUERIES
    // ------------------------------------------------------------------------

    @Override
    public MetamacCriteriaResult<RelatedResourceDto> findQueriesByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        QueriesSecurityUtils.canFindQueriesByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = queryMetamacCriteria2SculptorCriteriaMapper.getQueryCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Add condition for latest queryVersion
        ConditionalCriteria latestQueryVersionRestriction = ConditionalCriteriaBuilder.criteriaFor(QueryVersion.class).withProperty(QueryVersionProperties.lifeCycleStatisticalResource().lastVersion())
                .eq(Boolean.TRUE).buildSingle();
        sculptorCriteria.getConditions().add(latestQueryVersionRestriction);

        // Find
        PagedResult<QueryVersion> result = getQueryService().findQueryVersionsByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<RelatedResourceDto> metamacCriteriaResult = querySculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultQueryRelatedResourceDto(result,
                sculptorCriteria.getPageSize());

        return metamacCriteriaResult;
    }

    // ------------------------------------------------------------------------
    // QUERY VERSION
    // ------------------------------------------------------------------------

    @Override
    public QueryVersionDto retrieveQueryVersionByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Retrieve
        QueryVersion queryVersion = getQueryService().retrieveQueryVersionByUrn(ctx, urn);

        // Security
        QueriesSecurityUtils.canRetrieveQueryVersionByUrn(ctx, queryVersion);

        // Transform
        QueryVersionDto queryVersionDto = queryDo2DtoMapper.queryVersionDoToDto(ctx, queryVersion);
        queryVersionDto.getTemporalGranularities().addAll(commonDo2DtoMapper.externalItemDoCollectionToDtoCollection(queryVersion.getTemporalGranularities()));

        return queryVersionDto;
    }

    @Override
    public List<QueryVersionBaseDto> retrieveQueriesVersions(ServiceContext ctx, String queryVersionUrn) throws MetamacException {
        // Security
        QueriesSecurityUtils.canRetrieveQueriesVersions(ctx);

        // Retrieve
        List<QueryVersion> queryVersions = getQueryService().retrieveQueryVersions(ctx, queryVersionUrn);

        // Transform
        List<QueryVersionBaseDto> queries = new ArrayList<QueryVersionBaseDto>();
        for (QueryVersion version : queryVersions) {
            queries.add(queryDo2DtoMapper.queryVersionDoToBaseDto(ctx, version));
        }
        return queries;
    }

    @Override
    public QueryVersionDto retrieveLatestQueryVersion(ServiceContext ctx, String queryUrn) throws MetamacException {
        // Retrieve
        QueryVersion query = getQueryService().retrieveLatestQueryVersionByQueryUrn(ctx, queryUrn);

        // Security
        if (!SharedQueriesSecurityUtils.canRetrieveLatestQueryVersion(SecurityUtils.getMetamacPrincipal(ctx), query.getLifeCycleStatisticalResource().getStatisticalOperation().getCode(),
                query.getLifeCycleStatisticalResource().getEffectiveProcStatus())) {
            query = getQueryService().retrieveLatestPublishedQueryVersionByQueryUrn(ctx, queryUrn);
        }

        // Transform
        QueryVersionDto queryVersionDto = queryDo2DtoMapper.queryVersionDoToDto(ctx, query);
        return queryVersionDto;
    }

    @Override
    public QueryVersionDto retrieveLatestPublishedQueryVersion(ServiceContext ctx, String queryUrn) throws MetamacException {
        // Security
        QueriesSecurityUtils.canRetrieveLatestPublishedQueryVersion(ctx);

        // Retrieve
        QueryVersion query = getQueryService().retrieveLatestPublishedQueryVersionByQueryUrn(ctx, queryUrn);

        // Transform
        QueryVersionDto queryVersionDto = queryDo2DtoMapper.queryVersionDoToDto(ctx, query);
        return queryVersionDto;
    }

    @Override
    public List<QueryVersionBaseDto> retrieveQueriesVersions(ServiceContext ctx) throws MetamacException {
        // Security
        QueriesSecurityUtils.canRetrieveQueriesVersions(ctx);

        // Retrieve
        List<QueryVersion> queryVersions = getQueryService().retrieveQueryVersions(ctx);

        // Transform
        List<QueryVersionBaseDto> queriesDto = queryDo2DtoMapper.queryVersionDoListToDtoList(ctx, queryVersions);

        return queriesDto;
    }

    @Override
    public QueryVersionDto createQuery(ServiceContext ctx, QueryVersionDto queryVersionDto, ExternalItemDto statisticalOperationDto) throws MetamacException {
        // Security
        QueriesSecurityUtils.canCreateQuery(ctx, statisticalOperationDto.getCode());

        // Transform
        QueryVersion queryVersion = queryDto2DoMapper.queryVersionDtoToDo(queryVersionDto);
        ExternalItem statisticalOperation = datasetDto2DoMapper.externalItemDtoToDo(statisticalOperationDto, null, ServiceExceptionParameters.STATISTICAL_OPERATION);

        // Create
        queryVersion = getQueryService().createQueryVersion(ctx, queryVersion, statisticalOperation);

        // Transform to DTO
        queryVersionDto = queryDo2DtoMapper.queryVersionDoToDto(ctx, queryVersion);

        return queryVersionDto;
    }

    @Override
    public QueryVersionDto updateQueryVersion(ServiceContext ctx, QueryVersionDto queryVersionDto) throws MetamacException {
        // Security
        QueriesSecurityUtils.canUpdateQueryVersion(ctx, queryVersionDto);

        // Transform
        QueryVersion queryVersion = queryDto2DoMapper.queryVersionDtoToDo(queryVersionDto);

        // Update
        queryVersion = getQueryService().updateQueryVersion(ctx, queryVersion);

        // Transform to Dto
        queryVersionDto = queryDo2DtoMapper.queryVersionDoToDto(ctx, queryVersion);
        queryVersionDto.getTemporalGranularities().addAll(commonDo2DtoMapper.externalItemDoCollectionToDtoCollection(queryVersion.getTemporalGranularities()));

        return queryVersionDto;
    }

    @Override
    public MetamacCriteriaResult<QueryVersionBaseDto> findQueriesVersionsByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        QueriesSecurityUtils.canFindQueriesVersionsByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = queryVersionMetamacCriteria2SculptorCriteriaMapper.getQueryCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<QueryVersion> result = getQueryService().findQueryVersionsByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<QueryVersionBaseDto> metamacCriteriaResult = queryVersionSculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultQuery(ctx, result,
                sculptorCriteria.getPageSize());

        return metamacCriteriaResult;
    }

    @Override
    public void deleteQueryVersion(ServiceContext ctx, String urn) throws MetamacException {
        // Retrieve
        QueryVersionDto queryVersionDto = retrieveQueryVersionByUrn(ctx, urn);

        // Security
        QueriesSecurityUtils.canDeleteQueryVersion(ctx, queryVersionDto);

        // Delete
        getQueryService().deleteQueryVersion(ctx, urn);
    }

    @Override
    public QueryVersionDto sendQueryVersionToProductionValidation(ServiceContext ctx, QueryVersionDto queryVersionDto) throws MetamacException {
        // Security
        QueriesSecurityUtils.canSendQueryVersionToProductionValidation(ctx, queryVersionDto.getStatisticalOperation().getCode());

        // Transform
        QueryVersion queryVersion = queryDto2DoMapper.queryVersionDtoToDo(queryVersionDto);

        // Send to production validation and retrieve
        queryVersion = queryLifecycleService.sendToProductionValidation(ctx, queryVersion.getLifeCycleStatisticalResource().getUrn());

        // Transform
        queryVersionDto = queryDo2DtoMapper.queryVersionDoToDto(queryVersion);
        queryVersionDto.getTemporalGranularities().addAll(commonDo2DtoMapper.externalItemDoCollectionToDtoCollection(queryVersion.getTemporalGranularities()));
        return queryVersionDto;
    }

    @Override
    public QueryVersionBaseDto sendQueryVersionToProductionValidation(ServiceContext ctx, QueryVersionBaseDto queryVersionDto) throws MetamacException {
        // Security
        QueriesSecurityUtils.canSendQueryVersionToProductionValidation(ctx, queryVersionDto.getStatisticalOperation().getCode());

        // Check optimistic locking
        queryDto2DoMapper.checkOptimisticLocking(queryVersionDto);

        // Send to production validation and retrieve
        QueryVersion queryVersion = queryLifecycleService.sendToProductionValidation(ctx, queryVersionDto.getUrn());

        // Transform
        queryVersionDto = queryDo2DtoMapper.queryVersionDoToBaseDto(ctx, queryVersion);

        return queryVersionDto;
    }

    @Override
    public QueryVersionDto sendQueryVersionToDiffusionValidation(ServiceContext ctx, QueryVersionDto queryVersionDto) throws MetamacException {
        // Security
        QueriesSecurityUtils.canSendQueryVersionToDiffusionValidation(ctx, queryVersionDto.getStatisticalOperation().getCode());

        // Transform
        QueryVersion queryVersion = queryDto2DoMapper.queryVersionDtoToDo(queryVersionDto);

        // Send to production validation and retrieve
        queryVersion = queryLifecycleService.sendToDiffusionValidation(ctx, queryVersion.getLifeCycleStatisticalResource().getUrn());

        // Transform
        queryVersionDto = queryDo2DtoMapper.queryVersionDoToDto(queryVersion);
        queryVersionDto.getTemporalGranularities().addAll(commonDo2DtoMapper.externalItemDoCollectionToDtoCollection(queryVersion.getTemporalGranularities()));
        return queryVersionDto;
    }

    @Override
    public QueryVersionBaseDto sendQueryVersionToDiffusionValidation(ServiceContext ctx, QueryVersionBaseDto queryVersionDto) throws MetamacException {
        // Security
        QueriesSecurityUtils.canSendQueryVersionToDiffusionValidation(ctx, queryVersionDto.getStatisticalOperation().getCode());

        // Check optimistic locking
        queryDto2DoMapper.checkOptimisticLocking(queryVersionDto);

        // Send to production validation and retrieve
        QueryVersion queryVersion = queryLifecycleService.sendToDiffusionValidation(ctx, queryVersionDto.getUrn());

        // Transform
        queryVersionDto = queryDo2DtoMapper.queryVersionDoToBaseDto(ctx, queryVersion);

        return queryVersionDto;
    }

    @Override
    public QueryVersionDto sendQueryVersionToValidationRejected(ServiceContext ctx, QueryVersionDto queryVersionDto) throws MetamacException {
        // Security
        QueriesSecurityUtils.canSendQueryVersionToValidationRejected(ctx, queryVersionDto.getStatisticalOperation().getCode());

        // Transform
        QueryVersion queryVersion = queryDto2DoMapper.queryVersionDtoToDo(queryVersionDto);

        // Send to production validation and retrieve
        queryVersion = queryLifecycleService.sendToValidationRejected(ctx, queryVersion.getLifeCycleStatisticalResource().getUrn());

        // Transform
        queryVersionDto = queryDo2DtoMapper.queryVersionDoToDto(queryVersion);
        queryVersionDto.getTemporalGranularities().addAll(commonDo2DtoMapper.externalItemDoCollectionToDtoCollection(queryVersion.getTemporalGranularities()));
        return queryVersionDto;
    }

    @Override
    public QueryVersionBaseDto sendQueryVersionToValidationRejected(ServiceContext ctx, QueryVersionBaseDto queryVersionDto) throws MetamacException {
        // Security
        QueriesSecurityUtils.canSendQueryVersionToValidationRejected(ctx, queryVersionDto.getStatisticalOperation().getCode());

        // Check optimistic locking
        queryDto2DoMapper.checkOptimisticLocking(queryVersionDto);

        // Send to production validation and retrieve
        QueryVersion queryVersion = queryLifecycleService.sendToValidationRejected(ctx, queryVersionDto.getUrn());

        // Transform
        queryVersionDto = queryDo2DtoMapper.queryVersionDoToBaseDto(ctx, queryVersion);

        return queryVersionDto;
    }

    @Override
    public QueryVersionDto publishQueryVersion(ServiceContext ctx, QueryVersionDto queryVersionDto) throws MetamacException {
        // Security
        QueriesSecurityUtils.canPublishQueryVersion(ctx, queryVersionDto.getStatisticalOperation().getCode());

        // Transform
        QueryVersion queryVersion = queryDto2DoMapper.queryVersionDtoToDo(queryVersionDto);

        // Send to publish and retrieve
        queryVersion = queryLifecycleService.sendToPublished(ctx, queryVersion.getLifeCycleStatisticalResource().getUrn());

        // Transform
        queryVersionDto = queryDo2DtoMapper.queryVersionDoToDto(queryVersion);
        queryVersionDto.getTemporalGranularities().addAll(commonDo2DtoMapper.externalItemDoCollectionToDtoCollection(queryVersion.getTemporalGranularities()));
        return queryVersionDto;
    }

    @Override
    public QueryVersionBaseDto publishQueryVersion(ServiceContext ctx, QueryVersionBaseDto queryVersionDto) throws MetamacException {
        // Security
        QueriesSecurityUtils.canPublishQueryVersion(ctx, queryVersionDto.getStatisticalOperation().getCode());

        // Check optimistic locking
        queryDto2DoMapper.checkOptimisticLocking(queryVersionDto);

        // Send to publish and retrieve
        QueryVersion queryVersion = queryLifecycleService.sendToPublished(ctx, queryVersionDto.getUrn());

        // Transform
        queryVersionDto = queryDo2DtoMapper.queryVersionDoToBaseDto(ctx, queryVersion);

        return queryVersionDto;
    }

    @Override
    public QueryVersionBaseDto resendPublishedQueryVersionStreamMessage(ServiceContext ctx, String queryVersionUrn) throws MetamacException {

        // Retrieve Query
        QueryVersion queryVersion = queryVersionRepository.retrieveByUrn(queryVersionUrn);

        // Security
        QueriesSecurityUtils.canResendPublishedQueryVersionStreamMessage(ctx, queryVersion.getLifeCycleStatisticalResource().getStatisticalOperation().getCode());

        // Send stream message to stream messaging service (like Apache Kafka)
        queryLifecycleService.sendNewVersionPublishedStreamMessageByResource(ctx, queryVersion);

        // Transform
        return queryDo2DtoMapper.queryVersionDoToBaseDto(ctx, queryVersion);
    }

    @Override
    public QueryVersionDto versioningQueryVersion(ServiceContext ctx, QueryVersionDto queryVersionDto, VersionTypeEnum versionType) throws MetamacException {
        // Security
        QueriesSecurityUtils.canVersionQueryVersion(ctx, queryVersionDto.getStatisticalOperation().getCode());

        // Transform
        QueryVersion queryVersion = queryDto2DoMapper.queryVersionDtoToDo(queryVersionDto);

        // Versioning
        queryVersion = queryLifecycleService.versioning(ctx, queryVersion.getLifeCycleStatisticalResource().getUrn(), versionType);
        // Transform
        queryVersionDto = queryDo2DtoMapper.queryVersionDoToDto(queryVersion);
        queryVersionDto.getTemporalGranularities().addAll(commonDo2DtoMapper.externalItemDoCollectionToDtoCollection(queryVersion.getTemporalGranularities()));
        return queryVersionDto;
    }

    @Override
    public QueryVersionBaseDto versioningQueryVersion(ServiceContext ctx, QueryVersionBaseDto queryVersionDto, VersionTypeEnum versionType) throws MetamacException {
        // Security
        QueriesSecurityUtils.canVersionQueryVersion(ctx, queryVersionDto.getStatisticalOperation().getCode());

        // Check optimistic locking
        queryDto2DoMapper.checkOptimisticLocking(queryVersionDto);

        // Versioning
        QueryVersion queryVersion = queryLifecycleService.versioning(ctx, queryVersionDto.getUrn(), versionType);

        // Transform
        return queryDo2DtoMapper.queryVersionDoToBaseDto(ctx, queryVersion);
    }

    // ------------------------------------------------------------------------
    // DATASOURCES
    // ------------------------------------------------------------------------

    @Override
    public DatasourceDto createDatasource(ServiceContext ctx, String urnDatasetVersion, DatasourceDto datasourceDto) throws MetamacException {
        // Security
        DatasetsSecurityUtils.canCreateDatasource(ctx, datasourceDto.getStatisticalOperation().getCode());

        // Transform
        Datasource datasource = datasetDto2DoMapper.datasourceDtoToDo(datasourceDto);

        // Create
        datasource = getDatasetService().createDatasource(ctx, urnDatasetVersion, datasource);

        // Transform to DTO
        datasourceDto = datasetDo2DtoMapper.datasourceDoToDto(datasource);

        return datasourceDto;
    }

    @Override
    public DatasourceDto updateDatasource(ServiceContext ctx, DatasourceDto datasourceDto) throws MetamacException {
        // Security
        DatasetsSecurityUtils.canUpdateDatasource(ctx, datasourceDto.getStatisticalOperation().getCode());

        // Transform
        Datasource datasource = datasetDto2DoMapper.datasourceDtoToDo(datasourceDto);

        // Update
        datasource = getDatasetService().updateDatasource(ctx, datasource);

        // Transform to Dtos
        datasourceDto = datasetDo2DtoMapper.datasourceDoToDto(datasource);

        return datasourceDto;
    }

    @Override
    public DatasourceDto retrieveDatasourceByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Retrieve
        Datasource datasource = getDatasetService().retrieveDatasourceByUrn(ctx, urn);

        // Security
        DatasetsSecurityUtils.canRetrieveDatasourceByUrn(ctx, datasource.getIdentifiableStatisticalResource().getStatisticalOperation().getCode());

        // Transform
        DatasourceDto datasourceDto = datasetDo2DtoMapper.datasourceDoToDto(datasource);

        return datasourceDto;
    }

    @Override
    public int deleteDatasource(ServiceContext ctx, String urn, boolean deleteAttributes) throws MetamacException {
        // Retrieve
        String operationCode = getDatasetService().retrieveDatasourceByUrn(ctx, urn).getIdentifiableStatisticalResource().getStatisticalOperation().getCode();

        // Security
        DatasetsSecurityUtils.canDeleteDatasource(ctx, operationCode);

        // Delete
        logger.info("Deleting a datasource. Dataset = {}", new Object[]{urn});
        return getDatasetService().deleteDatasource(ctx, urn, deleteAttributes);
    }

    @Override
    public List<String> deleteDatasourcesNotUsed(ServiceContext ctx, String datasetUrn, boolean deleteAttributes) throws MetamacException {
        // Retrieve
        String operationCode = getDatasetService().retrieveDatasetVersionByUrn(ctx, datasetUrn).getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();

        // Security
        DatasetsSecurityUtils.canDeleteDatasource(ctx, operationCode);

        // Delete
        logger.info("Deleting a not used datasource. Dataset = {}", new Object[]{datasetUrn});
        return getDatasetService().deleteDatasourcesNotUsed(ctx, datasetUrn, deleteAttributes);
    }

    @Override
    public List<DatasourceDto> retrieveDatasourcesByDatasetVersion(ServiceContext ctx, String urnDatasetVersion) throws MetamacException {
        // Retrieve
        String operationCode = getDatasetService().retrieveDatasetVersionByUrn(ctx, urnDatasetVersion).getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();

        // Security
        DatasetsSecurityUtils.canRetrieveDatasourcesByDatasetVersion(ctx, operationCode);

        // Retrieve
        List<Datasource> datasources = getDatasetService().retrieveDatasourcesByDatasetVersion(ctx, urnDatasetVersion);

        // Transform
        List<DatasourceDto> datasourcesDto = datasetDo2DtoMapper.datasourceDoListToDtoList(datasources);

        return datasourcesDto;
    }

    @Override
    public DimensionRepresentationMappingDto retrieveDimensionRepresentationMappings(ServiceContext ctx, String datasetVersionUrn, String filename) throws MetamacException {

        // Retrieve
        DatasetVersion datasetVersion = getDatasetService().retrieveDatasetVersionByUrn(ctx, datasetVersionUrn);
        String operationCode = datasetVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();

        // Security
        DatasetsSecurityUtils.canRetrieveDatasourceDimensionRepresentationMappings(ctx, operationCode);

        // Retrieve
        DimensionRepresentationMapping mapping = getDatasetService().retrieveDimensionRepresentationMapping(ctx, datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn(), filename);

        // Transform
        return datasetDo2DtoMapper.dimensionRepresentationMappingDoToDto(mapping);
    }

    @Override
    public String exportDatasourcesTsv(ServiceContext ctx, String datasetVersionUrn, boolean useCommaDecimalSeparator) throws MetamacException {

        // Security
        DatasetsSecurityUtils.canExportDatasourcesTsv(ctx);

        return getDatasetService().exportDatasourcesTsv(ctx, datasetVersionUrn, useCommaDecimalSeparator);
    }

    @Override
    public String exportAttributesTsv(ServiceContext ctx, String datasetVersionUrn) throws MetamacException {

        // Security
        DatasetsSecurityUtils.canExportAttributesTsv(ctx);

        return getDatasetService().exportAttributesTsv(ctx, datasetVersionUrn);
    }

    // ------------------------------------------------------------------------
    // DATASETS
    // ------------------------------------------------------------------------

    @Override
    public MetamacCriteriaResult<RelatedResourceDto> findDatasetsByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        DatasetsSecurityUtils.canFindDatasetsByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = datasetMetamacCriteria2SculptorCriteriaMapper.getDatasetCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Add condition for latest datasetVersions
        ConditionalCriteria latestDatasetVersionRestriction = ConditionalCriteriaBuilder.criteriaFor(DatasetVersion.class)
                .withProperty(DatasetVersionProperties.siemacMetadataStatisticalResource().lastVersion()).eq(Boolean.TRUE).buildSingle();
        sculptorCriteria.getConditions().add(latestDatasetVersionRestriction);

        // Find
        PagedResult<DatasetVersion> result = getDatasetService().findDatasetVersionsByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<RelatedResourceDto> metamacCriteriaResult = datasetSculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultDatasetRelatedResourceDto(result,
                sculptorCriteria.getPageSize());

        return metamacCriteriaResult;
    }

    // ------------------------------------------------------------------------
    // DATASETS VERSIONS
    // ------------------------------------------------------------------------

    @Override
    public DatasetVersionDto createDataset(ServiceContext ctx, DatasetVersionDto datasetVersionDto, ExternalItemDto statisticalOperationDto) throws MetamacException {
        // Security
        DatasetsSecurityUtils.canCreateDataset(ctx, statisticalOperationDto.getCode());

        // Transform
        DatasetVersion datasetVersion = datasetDto2DoMapper.datasetVersionDtoToDo(datasetVersionDto);
        ExternalItem statisticalOperation = datasetDto2DoMapper.externalItemDtoToDo(statisticalOperationDto, null, ServiceExceptionParameters.STATISTICAL_OPERATION);

        // Retrieve
        DatasetVersion datasetVersionCreated = getDatasetService().createDatasetVersion(ctx, datasetVersion, statisticalOperation);

        // Transform
        DatasetVersionDto datasetCreated = datasetDo2DtoMapper.datasetVersionDoToDto(ctx, datasetVersionCreated);

        return datasetCreated;
    }

    @Override
    public DatasetVersionDto updateDatasetVersion(ServiceContext ctx, DatasetVersionDto datasetVersionDto) throws MetamacException {
        // Security
        DatasetsSecurityUtils.canUpdateDatasetVersion(ctx, datasetVersionDto);

        // Transform
        DatasetVersion datasetVersion = datasetDto2DoMapper.datasetVersionDtoToDo(datasetVersionDto);

        // Update
        datasetVersion = getDatasetService().updateDatasetVersion(ctx, datasetVersion);

        // Transform
        return datasetDo2DtoMapper.datasetVersionDoToDto(ctx, datasetVersion);
    }

    @Override
    public void deleteDatasetVersion(ServiceContext ctx, String urn) throws MetamacException {
        // Retrieve
        DatasetVersionDto datasetVersionDto = retrieveDatasetVersionByUrn(ctx, urn);

        // Security
        DatasetsSecurityUtils.canDeleteDatasetVersion(ctx, datasetVersionDto);

        // Delete
        getDatasetService().deleteDatasetVersion(ctx, urn);
    }

    @Override
    public MetamacCriteriaResult<DatasetVersionBaseDto> findDatasetsVersionsByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        DatasetsSecurityUtils.canFindDatasetsVersionsByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = datasetVersionMetamacCriteria2SculptorCriteriaMapper.getDatasetVersionCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<DatasetVersion> result = getDatasetService().findDatasetVersionsByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<DatasetVersionBaseDto> metamacCriteriaResult = datasetVersionSculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultDatasetVersion(ctx, result,
                sculptorCriteria.getPageSize());

        return metamacCriteriaResult;
    }

    @Override
    public DatasetVersionDto retrieveDatasetVersionByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Retrieve
        DatasetVersion datasetVersion = getDatasetService().retrieveDatasetVersionByUrn(ctx, urn);

        // Security
        DatasetsSecurityUtils.canRetrieveDatasetVersionByUrn(ctx, datasetVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode(),
                datasetVersion.getSiemacMetadataStatisticalResource().getEffectiveProcStatus());

        // Transform
        return datasetDo2DtoMapper.datasetVersionDoToDto(ctx, datasetVersion);
    }

    @Override
    public List<ExternalItemDto> retrieveExternalItemsByDatasetUrn(ServiceContext ctx, String urn, List<ExternalItemDto> externalItemDtos) throws MetamacException {
        List<ExternalItemDto> externalItemsDto = new ArrayList<>();
        List<CodeItemDto> codeItemsDto = filterCoverageForDatasetVersionDimension(ctx, urn, "TIME_PERIOD", null, null);
        List<IstacTimeGranularityCodeEnum> istacTimeGranularityCodesEnum = getTemporalGranularities(codeItemsDto);
        for (ExternalItemDto externalItemDto : externalItemDtos) {
            if (checkGranularityInList(istacTimeGranularityCodesEnum, externalItemDto)) {
                externalItemsDto.add(externalItemDto);
            }
        }
        return externalItemsDto;
    }

    private Boolean checkGranularityInList(List<IstacTimeGranularityCodeEnum> istacTimeGranularityCodesEnum, ExternalItemDto externalItemDto) {
        for (IstacTimeGranularityCodeEnum istacTimeGranularityCodeEnum : istacTimeGranularityCodesEnum) {
            if (istacTimeGranularityCodeEnum.getLabel().equals(externalItemDto.getCode())) {
                return true;
            }
        }
        return false;
    }

    private List<IstacTimeGranularityCodeEnum> getTemporalGranularities(List<CodeItemDto> codes) throws MetamacException {
        List<IstacTimeGranularityCodeEnum> istacTimeGranularityCodesEnum = new ArrayList<>();
        for (CodeItemDto codeItem : codes) {
            IstacTimeGranularityCodeEnum istacTimeGranularityCodeEnum = IstacTimeUtils.guessTimeGranularity(codeItem.getCode());
            if (!istacTimeGranularityCodesEnum.contains(istacTimeGranularityCodeEnum)) {
                istacTimeGranularityCodesEnum.add(istacTimeGranularityCodeEnum);
            }
        }
        return istacTimeGranularityCodesEnum;
    }

    @Override
    public List<DatasetVersionBaseDto> retrieveDatasetVersions(ServiceContext ctx, String datasetVersionUrn) throws MetamacException {
        // Security
        DatasetsSecurityUtils.canRetrieveDatasetVersions(ctx);

        // Retrieve
        List<DatasetVersion> datasetVersions = getDatasetService().retrieveDatasetVersions(ctx, datasetVersionUrn);

        // Transform
        List<DatasetVersionBaseDto> datasets = new ArrayList<DatasetVersionBaseDto>();
        for (DatasetVersion version : datasetVersions) {
            datasets.add(datasetDo2DtoMapper.datasetVersionDoToBaseDto(ctx, version));
        }
        return datasets;
    }

    @Override
    public List<String> retrieveDatasetVersionDimensionsIds(ServiceContext ctx, String datasetVersionUrn) throws MetamacException {
        // Security
        DatasetsSecurityUtils.canRetrieveDatasetVersionDimensionsIds(ctx);

        return getDatasetService().retrieveDatasetVersionDimensionsIds(ctx, datasetVersionUrn);
    }

    @Override
    public List<CodeItemDto> retrieveCoverageForDatasetVersionDimension(ServiceContext ctx, String datasetVersionUrn, String dsdDimensionId) throws MetamacException {
        // Security
        DatasetsSecurityUtils.canRetrieveCoverageForDatasetVersionDimension(ctx);

        List<CodeDimension> codeDimensions = getDatasetService().retrieveCoverageForDatasetVersionDimension(ctx, datasetVersionUrn, dsdDimensionId);

        return datasetDo2DtoMapper.codeDimensionDoListToCodeItemDtoList(codeDimensions);
    }

    @Override
    public List<CodeItemDto> filterCoverageForDatasetVersionDimension(ServiceContext ctx, String datasetVersionUrn, String dsdDimensionId, String filter, List<String> temporalGranularities)
            throws MetamacException {
        // Security
        DatasetsSecurityUtils.canFilterCoverageForDatasetVersionDimension(ctx);

        List<CodeDimension> codeDimensions = getDatasetService().filterCoverageForDatasetVersionDimension(ctx, datasetVersionUrn, dsdDimensionId, filter);
        if (temporalGranularities != null && dsdDimensionId.equals("TIME_PERIOD")) {
            return datasetDo2DtoMapper.codeDimensionDoListToCodeItemDtoList(getCodeDimensionsFiltered(codeDimensions, temporalGranularities));
        }

        return datasetDo2DtoMapper.codeDimensionDoListToCodeItemDtoList(codeDimensions);
    }

    private List<CodeDimension> getCodeDimensionsFiltered(List<CodeDimension> codeDimensions, List<String> temporalGranularities) throws MetamacException {
        List<CodeDimension> codeDimensionsFiltered = new ArrayList<>();
        for (CodeDimension codeDimension : codeDimensions) {
            IstacTimeGranularityCodeEnum istacTimeGranularityCodeEnum = IstacTimeUtils.guessTimeGranularity(codeDimension.getIdentifier());
            if (temporalGranularities.contains(istacTimeGranularityCodeEnum.getLabel())) {
                codeDimensionsFiltered.add(codeDimension);
            }
        }
        return codeDimensionsFiltered;
    }

    @Override
    public DatasetVersionDto sendDatasetVersionToProductionValidation(ServiceContext ctx, DatasetVersionDto datasetVersionDto) throws MetamacException {
        // Security
        DatasetsSecurityUtils.canSendDatasetVersionToProductionValidation(ctx, datasetVersionDto.getStatisticalOperation().getCode());

        // Transform
        DatasetVersion datasetVersion = datasetDto2DoMapper.datasetVersionDtoToDo(datasetVersionDto);

        // Send to production validation and retrieve
        datasetVersion = datasetLifecycleService.sendToProductionValidation(ctx, datasetVersion.getSiemacMetadataStatisticalResource().getUrn());

        // Transform
        datasetVersionDto = datasetDo2DtoMapper.datasetVersionDoToDto(ctx, datasetVersion);

        return datasetVersionDto;
    }

    @Override
    public DatasetVersionBaseDto sendDatasetVersionToProductionValidation(ServiceContext ctx, DatasetVersionBaseDto datasetVersionDto) throws MetamacException {
        // Security
        DatasetsSecurityUtils.canSendDatasetVersionToProductionValidation(ctx, datasetVersionDto.getStatisticalOperation().getCode());

        // Check optimistic locking
        datasetDto2DoMapper.checkOptimisticLocking(datasetVersionDto);

        // Send to production validation and retrieve
        DatasetVersion datasetVersion = datasetLifecycleService.sendToProductionValidation(ctx, datasetVersionDto.getUrn());

        // Transform
        datasetVersionDto = datasetDo2DtoMapper.datasetVersionDoToBaseDto(ctx, datasetVersion);

        return datasetVersionDto;
    }

    @Override
    public DatasetVersionDto sendDatasetVersionToDiffusionValidation(ServiceContext ctx, DatasetVersionDto datasetVersionDto) throws MetamacException {
        // Security
        DatasetsSecurityUtils.canSendDatasetVersionToDiffusionValidation(ctx, datasetVersionDto.getStatisticalOperation().getCode());

        // Transform
        DatasetVersion datasetVersion = datasetDto2DoMapper.datasetVersionDtoToDo(datasetVersionDto);

        // Send to production validation and retrieve
        datasetVersion = datasetLifecycleService.sendToDiffusionValidation(ctx, datasetVersion.getSiemacMetadataStatisticalResource().getUrn());

        // Transform
        datasetVersionDto = datasetDo2DtoMapper.datasetVersionDoToDto(ctx, datasetVersion);

        return datasetVersionDto;
    }

    @Override
    public DatasetVersionBaseDto sendDatasetVersionToDiffusionValidation(ServiceContext ctx, DatasetVersionBaseDto datasetVersionDto) throws MetamacException {
        // Security
        DatasetsSecurityUtils.canSendDatasetVersionToDiffusionValidation(ctx, datasetVersionDto.getStatisticalOperation().getCode());

        // Check optimistic locking
        datasetDto2DoMapper.checkOptimisticLocking(datasetVersionDto);

        // Send to production validation and retrieve
        DatasetVersion datasetVersion = datasetLifecycleService.sendToDiffusionValidation(ctx, datasetVersionDto.getUrn());

        // Transform
        datasetVersionDto = datasetDo2DtoMapper.datasetVersionDoToBaseDto(ctx, datasetVersion);

        return datasetVersionDto;
    }

    @Override
    public DatasetVersionDto sendDatasetVersionToValidationRejected(ServiceContext ctx, DatasetVersionDto datasetVersionDto) throws MetamacException {
        // Security
        DatasetsSecurityUtils.canSendDatasetVersionToValidationRejected(ctx, datasetVersionDto.getStatisticalOperation().getCode());

        // Transform
        DatasetVersion datasetVersion = datasetDto2DoMapper.datasetVersionDtoToDo(datasetVersionDto);

        // Send to production validation and retrieve
        datasetVersion = datasetLifecycleService.sendToValidationRejected(ctx, datasetVersion.getSiemacMetadataStatisticalResource().getUrn());

        // Transform
        datasetVersionDto = datasetDo2DtoMapper.datasetVersionDoToDto(ctx, datasetVersion);

        return datasetVersionDto;
    }

    @Override
    public DatasetVersionBaseDto sendDatasetVersionToValidationRejected(ServiceContext ctx, DatasetVersionBaseDto datasetVersionDto) throws MetamacException {
        // Security
        DatasetsSecurityUtils.canSendDatasetVersionToValidationRejected(ctx, datasetVersionDto.getStatisticalOperation().getCode());

        // Check optimistic locking
        datasetDto2DoMapper.checkOptimisticLocking(datasetVersionDto);

        // Send to production validation and retrieve
        DatasetVersion datasetVersion = datasetLifecycleService.sendToValidationRejected(ctx, datasetVersionDto.getUrn());

        // Transform
        datasetVersionDto = datasetDo2DtoMapper.datasetVersionDoToBaseDto(ctx, datasetVersion);

        return datasetVersionDto;
    }

    @Override
    public DatasetVersionDto publishDatasetVersion(ServiceContext ctx, DatasetVersionDto datasetVersionDto) throws MetamacException {
        // Security
        DatasetsSecurityUtils.canPublishDatasetVersion(ctx, datasetVersionDto.getStatisticalOperation().getCode());

        // Transform
        DatasetVersion datasetVersion = datasetDto2DoMapper.datasetVersionDtoToDo(datasetVersionDto);

        // Send to publish and retrieve
        datasetVersion = datasetLifecycleService.sendToPublished(ctx, datasetVersion.getSiemacMetadataStatisticalResource().getUrn());

        datasetLifecycleService.checkTwitterPostActivatedAndPostTwit(ctx, datasetVersion);
        // Transform
        datasetVersionDto = datasetDo2DtoMapper.datasetVersionDoToDto(ctx, datasetVersion);

        return datasetVersionDto;
    }

    @Override
    public DatasetVersionBaseDto resendPublishedDatasetVersionStreamMessage(ServiceContext ctx, String datasetVersionUrn) throws MetamacException {
        // Retrieve Dataset
        DatasetVersion datasetVersion = datasetVersionRepository.retrieveByUrn(datasetVersionUrn);

        // Security
        DatasetsSecurityUtils.canResendPublishedDatasetVersionStreamMessage(ctx, datasetVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode());

        // Send stream message to stream messaging service (like Apache Kafka)
        datasetLifecycleService.sendNewVersionPublishedStreamMessageByResource(ctx, datasetVersion);

        // Transform
        return datasetDo2DtoMapper.datasetVersionDoToBaseDto(ctx, datasetVersion);
    }

    @Override
    public DatasetVersionBaseDto publishDatasetVersion(ServiceContext ctx, DatasetVersionBaseDto datasetVersionDto) throws MetamacException {
        // Security
        DatasetsSecurityUtils.canPublishDatasetVersion(ctx, datasetVersionDto.getStatisticalOperation().getCode());

        // Check optimistic locking
        datasetDto2DoMapper.checkOptimisticLocking(datasetVersionDto);

        // Send to publish and retrieve
        DatasetVersion datasetVersion = datasetLifecycleService.sendToPublished(ctx, datasetVersionDto.getUrn());

        // Transform
        datasetVersionDto = datasetDo2DtoMapper.datasetVersionDoToBaseDto(ctx, datasetVersion);

        return datasetVersionDto;
    }

    @Override
    public DatasetVersionDto versioningDatasetVersion(ServiceContext ctx, DatasetVersionDto datasetVersionDto, VersionTypeEnum versionType) throws MetamacException {
        // Security
        DatasetsSecurityUtils.canVersionDataset(ctx, datasetVersionDto.getStatisticalOperation().getCode());

        // Transform
        DatasetVersion datasetVersion = datasetDto2DoMapper.datasetVersionDtoToDo(datasetVersionDto);

        // Versioning
        datasetVersion = datasetLifecycleService.versioning(ctx, datasetVersion.getSiemacMetadataStatisticalResource().getUrn(), versionType);

        // Transform
        return datasetDo2DtoMapper.datasetVersionDoToDto(ctx, datasetVersion);
    }

    @Override
    public DatasetVersionBaseDto versioningDatasetVersion(ServiceContext ctx, DatasetVersionBaseDto datasetVersionDto, VersionTypeEnum versionType) throws MetamacException {
        // Security
        DatasetsSecurityUtils.canVersionDataset(ctx, datasetVersionDto.getStatisticalOperation().getCode());

        // Check optimistic locking
        datasetDto2DoMapper.checkOptimisticLocking(datasetVersionDto);

        // Versioning
        DatasetVersion datasetVersion = datasetLifecycleService.versioning(ctx, datasetVersionDto.getUrn(), versionType);

        // Transform
        return datasetDo2DtoMapper.datasetVersionDoToBaseDto(ctx, datasetVersion);
    }

    @Override
    public DatasetVersionDto retrieveLatestDatasetVersion(ServiceContext ctx, String datasetUrn) throws MetamacException {
        // Retrieve
        DatasetVersion dataset = getDatasetService().retrieveLatestDatasetVersionByDatasetUrn(ctx, datasetUrn);

        // Security
        if (!SharedDatasetsSecurityUtils.canRetrieveLatestDatasetVersion(SecurityUtils.getMetamacPrincipal(ctx), dataset.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode(),
                dataset.getSiemacMetadataStatisticalResource().getEffectiveProcStatus())) {
            dataset = getDatasetService().retrieveLatestPublishedDatasetVersionByDatasetUrn(ctx, datasetUrn);
        }

        // Transform
        DatasetVersionDto datasetVersionDto = datasetDo2DtoMapper.datasetVersionDoToDto(ctx, dataset);
        return datasetVersionDto;
    }

    @Override
    public DatasetVersionDto retrieveLatestPublishedDatasetVersion(ServiceContext ctx, String datasetUrn) throws MetamacException {
        // Security
        DatasetsSecurityUtils.canRetrieveLatestPublishedDatasetVersion(ctx);

        // Retrieve
        DatasetVersion dataset = getDatasetService().retrieveLatestPublishedDatasetVersionByDatasetUrn(ctx, datasetUrn);

        // Transform
        DatasetVersionDto datasetVersionDto = datasetDo2DtoMapper.datasetVersionDoToDto(ctx, dataset);
        return datasetVersionDto;
    }

    @Override
    public DatasetVersionMainCoveragesDto retrieveDatasetVersionMainCoverages(ServiceContext ctx, String datasetVersionUrn) throws MetamacException {
        // Security
        DatasetsSecurityUtils.canRetrieveDatasetVersionMainCoverages(ctx);

        // Retrieve
        DatasetVersion dataset = getDatasetService().retrieveDatasetVersionByUrn(ctx, datasetVersionUrn);

        // Transform
        DatasetVersionMainCoveragesDto mainCoveragesDto = new DatasetVersionMainCoveragesDto();
        mainCoveragesDto.getGeographicCoverage().addAll(commonDo2DtoMapper.externalItemDoCollectionToDtoList(dataset.getGeographicCoverage()));
        mainCoveragesDto.getTemporalCoverage().addAll(commonDo2DtoMapper.temporalCodeDoCollectionToDtoList(dataset.getTemporalCoverage()));
        mainCoveragesDto.getMeasureCoverage().addAll(commonDo2DtoMapper.externalItemDoCollectionToDtoList(dataset.getMeasureCoverage()));

        return mainCoveragesDto;
    }

    @Override
    public List<StatisticOfficialityDto> findStatisticOfficialities(ServiceContext ctx) throws MetamacException {
        // Security
        DatasetsSecurityUtils.canFindStatisticOfficialities(ctx);

        // Retrieve
        List<StatisticOfficiality> statisticOfficialities = getDatasetService().findStatisticOfficialities(ctx);

        // Transform
        return datasetDo2DtoMapper.statisticOfficialityDoList2DtoList(statisticOfficialities);
    }

    @Override
    public List<PurposeDto> findPurposes(ServiceContext ctx) throws MetamacException {
        // Security
        DatasetsSecurityUtils.canFindPurposes(ctx);
        // retrieve
        List<Purpose> purposes = getQueryService().findPurposes(ctx);

        // transform
        return queryDo2DtoMapper.purposeDoListToDtoList(purposes);
    }

    @Override
    public void importDatasourcesInDatasetVersion(ServiceContext ctx, DatasetVersionDto datasetVersionDto, List<URL> fileUrls, Map<String, String> dimensionRepresentationMapping,
            boolean storeDimensionRepresentationMapping, BasicVersionableStatisticalResourceDto basicVersionableStatisticalResourceDto) throws MetamacException {
        // Security
        DatasetsSecurityUtils.canImportDatasourcesInDatasetVersion(ctx, datasetVersionDto.getStatisticalOperation().getCode());

        // Transform for optimistic locking
        DatasetVersion datasetVersion = datasetDto2DoMapper.datasetVersionDtoToDo(datasetVersionDto);

        // Service
        getDatasetService().importDatasourcesInDatasetVersion(ctx, datasetVersion.getSiemacMetadataStatisticalResource().getUrn(), fileUrls, dimensionRepresentationMapping,
                storeDimensionRepresentationMapping, basicVersionableStatisticalResourceDto);
    }

    @Override
    public void importAttributesFromFile(ServiceContext ctx, DatasetVersionDto datasetVersionDto, List<URL> fileUrls) throws MetamacException {
        DatasetsSecurityUtils.canImportDatasourcesInDatasetVersion(ctx, datasetVersionDto.getStatisticalOperation().getCode());
        getDatasetService().importAttributesFromFile(ctx, datasetVersionDto.getUrn(), fileUrls);
    }

    @Override
    public MetamacException importDatasourcesInStatisticalOperation(ServiceContext ctx, String statisticalOperationCode, List<URL> fileUrls,
            BasicVersionableStatisticalResourceDto basicVersionableStatisticalResourceDto) throws MetamacException {
        // Security
        DatasetsSecurityUtils.canImportDatasourcesInStatisticalOperation(ctx, statisticalOperationCode);

        return getDatasetService().importDatasourcesInStatisticalOperation(ctx, statisticalOperationCode, fileUrls, basicVersionableStatisticalResourceDto);
    }

    @Override
    public DsdAttributeInstanceDto createAttributeInstance(ServiceContext ctx, String datasetVersionUrn, DsdAttributeInstanceDto dsdAttributeInstanceDto) throws MetamacException {
        // Retrieve
        DatasetVersionDto datasetVersionDto = retrieveDatasetVersionByUrn(ctx, datasetVersionUrn);

        // Security
        DatasetsSecurityUtils.canCreateAttributeInstance(ctx, datasetVersionDto);

        // Transform
        AttributeInstanceDto attributeInstanceDto = statisticalResourcesDto2StatRepoDtoMapper.dsdAttributeInstanceDtoToAttributeInstanceDto(dsdAttributeInstanceDto);

        // Create attribute
        AttributeInstanceDto attributeInstanceCreated = getDatasetService().createAttributeInstance(ctx, datasetVersionUrn, attributeInstanceDto);

        DsdAttribute dsdAttribute = getDatasetVersionAttribute(ctx, datasetVersionUrn, attributeInstanceCreated.getAttributeId());

        return statRepoDto2StatisticalResourcesDtoMapper.attributeDtoToDsdAttributeInstanceDto(datasetVersionDto.getId(), dsdAttribute, attributeInstanceCreated);
    }

    @Override
    public void checkAttributeInstance(ServiceContext ctx, String datasetVersionUrn, DsdAttributeInstanceDto dsdAttributeInstanceDto, List<MetamacExceptionItem> exceptions) throws MetamacException {
        DatasetVersion datasetVersion = getDatasetService().retrieveDatasetVersionByUrn(ctx, datasetVersionUrn);
        DataStructure dsd = srmRestInternalService.retrieveDsdByUrn(datasetVersion.getRelatedDsd().getUrn());
        TaskInfoDataset taskInfoDataset = new TaskInfoDataset();
        taskInfoDataset.setDatasetUrn(datasetVersion.getDataset().getIdentifiableStatisticalResource().getUrn());
        taskInfoDataset.setDatasetVersionId(datasetVersion.getSiemacMetadataStatisticalResource().getUrn());
        taskInfoDataset.setDataStructureUrn(datasetVersion.getRelatedDsd().getUrn());
        ValidateDataVersusDsd validator = new ValidateDataVersusDsd(ctx, dsd, srmRestInternalService, null, taskInfoDataset);
        AttributeInstanceDto attributeInstanceDto = statisticalResourcesDto2StatRepoDtoMapper.dsdAttributeInstanceDtoToAttributeInstanceDto(dsdAttributeInstanceDto);

        List<AttributeInstanceDto> attributesInstances = Arrays.asList(attributeInstanceDto);

        validator.checkAttributesInstancesRepresentation(attributesInstances, exceptions);
    }

    @Override
    public DsdAttributeInstanceDto updateAttributeInstance(ServiceContext ctx, String datasetVersionUrn, DsdAttributeInstanceDto dsdAttributeInstanceDto) throws MetamacException {
        // Retrieve
        DatasetVersionDto datasetVersionDto = retrieveDatasetVersionByUrn(ctx, datasetVersionUrn);

        // Security
        DatasetsSecurityUtils.canUpdateAttributeInstance(ctx, datasetVersionDto);

        // Transform
        AttributeInstanceDto attributeInstanceDto = statisticalResourcesDto2StatRepoDtoMapper.dsdAttributeInstanceDtoToAttributeInstanceDto(dsdAttributeInstanceDto);
        // Update attribute
        AttributeInstanceDto attributeInstanceUpdated = getDatasetService().updateAttributeInstance(ctx, datasetVersionUrn, attributeInstanceDto);

        if (attributeInstanceUpdated != null) {
            DsdAttribute dsdAttribute = getDatasetVersionAttribute(ctx, datasetVersionUrn, attributeInstanceUpdated.getAttributeId());

            return statRepoDto2StatisticalResourcesDtoMapper.attributeDtoToDsdAttributeInstanceDto(datasetVersionDto.getId(), dsdAttribute, attributeInstanceUpdated);
        } else {
            return null;
        }
    }

    @Override
    public void deleteAttributeInstance(ServiceContext ctx, String datasetVersionUrn, String uuid) throws MetamacException {
        // Retrieve
        DatasetVersionDto datasetVersionDto = retrieveDatasetVersionByUrn(ctx, datasetVersionUrn);

        // Security
        DatasetsSecurityUtils.canDeleteAttributeInstance(ctx, datasetVersionDto);

        // Delete attribute
        getDatasetService().deleteAttributeInstance(ctx, datasetVersionUrn, uuid);
    }

    @Override
    public List<DsdAttributeInstanceDto> retrieveAttributeInstances(ServiceContext ctx, String datasetVersionUrn, String attributeId) throws MetamacException {
        // Retrieve
        DatasetVersionDto datasetVersionDto = retrieveDatasetVersionByUrn(ctx, datasetVersionUrn);

        // Security
        DatasetsSecurityUtils.canRetrieveAttributeInstances(ctx, datasetVersionDto);

        // Retrieve
        List<AttributeInstanceDto> instances = getDatasetService().retrieveAttributeInstances(ctx, datasetVersionUrn, attributeId);

        DsdAttribute dsdAttribute = getDatasetVersionAttribute(ctx, datasetVersionUrn, attributeId);

        return statRepoDto2StatisticalResourcesDtoMapper.attributeDtosToDsdAttributeInstanceDtos(datasetVersionDto.getId(), dsdAttribute, instances);
    }

    @Override
    public void updateGeographicCoverageVariableElementsCache(ServiceContext ctx, String datasetVersionUrn) throws MetamacException {
        // Retrieve
        DatasetVersion datasetVersion = getDatasetService().retrieveDatasetVersionByUrn(ctx, datasetVersionUrn);

        // Security
        DatasetsSecurityUtils.canUpdateGeographicCoverageVariableElementsCache(ctx);

        // Operate
        getDatasetService().updateGeographicCoverageVariableElementsCache(ctx, datasetVersion);
    }

    @Override
    public void updateAllGeographicCoverageVariableElementsCache(ServiceContext ctx, List<StatisticalResourceTypeEnum> resourcesToUpdate, List<StatisticalResourceTypeEnum> externalResourcesToUpdate)
            throws MetamacException {
        // Security
        DatasetsSecurityUtils.canUpdateGeographicCoverageVariableElementsCache(ctx);

        logger.info("Execution start - updateAllGeographicCoverageVariableElementsCache - existing dataset : {} ", new DateTime());

        updateAllGeographicCoverageVariableElementsCache(ctx, resourcesToUpdate);

        logger.info("Execution end - updateAllGeographicCoverageVariableElementsCache - existing dataset : {} ", new DateTime());

        getCacheService().updateAllGeographicExternalCoverageVariableElementsCache(ctx, externalResourcesToUpdate);
    }

    private void updateAllGeographicCoverageVariableElementsCache(ServiceContext ctx, List<StatisticalResourceTypeEnum> resourcesToUpdate) throws MetamacException {
        for (StatisticalResourceTypeEnum resourceType : resourcesToUpdate) {
            switch (resourceType) {
                case DATASET:
                    getDatasetService().updateAllGeographicCoverageVariableElementsCache(ctx);
                    break;
                case QUERY:
                    getQueryService().updateAllGeographicalCache(ctx);
                    break;
                case COLLECTION:
                    boolean mustWait = false;
                    if (resourcesToUpdate.contains(StatisticalResourceTypeEnum.DATASET) || resourcesToUpdate.contains(StatisticalResourceTypeEnum.QUERY)) {
                        mustWait = true;
                    }
                    getPublicationService().updateAllGeographicalCache(ctx, mustWait);
                    break;
                default:
                    throw MetamacExceptionBuilder.builder().withPrincipalException(new MetamacExceptionItem(ServiceExceptionType.UPDATE_GEOCOVERAGE_CACHE_RESOURCE_NOT_FOUND, resourceType.getName()))
                            .build();
            }
        }
    }

    @Override
    public void updateGeographicCoverageExternalPublicationCache(ServiceContext ctx, SpecificRecordBase message) throws MetamacException {
        // Security
        DatasetsSecurityUtils.canUpdateGeographicCoverageVariableElementsCache(ctx);

        getDatasetService().updateGeographicCoverageExternalPublicationCache(ctx, message);
    }

    private DsdAttribute getDatasetVersionAttribute(ServiceContext ctx, String datasetVersionUrn, String attributeId) throws MetamacException {
        DatasetVersion datasetVersion = getDatasetService().retrieveDatasetVersionByUrn(ctx, datasetVersionUrn);

        DataStructure dsd = srmRestInternalService.retrieveDsdByUrn(datasetVersion.getRelatedDsd().getUrn());

        return DsdProcessor.getAttribute(dsd, attributeId);
    }

    @Override
    public void updateGeoCacheRelatedResource(ServiceContext ctx, String resourceUrn, StatisticalResourceTypeEnum resourceType) throws MetamacException {

        // Security
        DatasetsSecurityUtils.canUpdateGeoCacheRelatedResource(ctx);

        if (StatisticalResourceTypeEnum.COLLECTION.equals(resourceType)) {
            PublicationVersion publicationVersion = getPublicationService().retrievePublicationVersionByUrn(ctx, resourceUrn);

            getPublicationService().updateGeographicalCache(ctx, publicationVersion);
        }
        if (StatisticalResourceTypeEnum.MULTIDATASET.equals(resourceType)) {
            MultidatasetVersion multidatasetVersion = getMultidatasetService().retrieveMultidatasetVersionByUrn(ctx, resourceUrn);
            getMultidatasetService().updateGeographicalCache(ctx, multidatasetVersion);
        } else if (StatisticalResourceTypeEnum.QUERY.equals(resourceType)) {
            QueryVersion queryVersion = getQueryService().retrieveQueryVersionByUrn(ctx, resourceUrn);

            this.getQueryService().updateGeographicalCache(ctx, queryVersion);
        }
    }

    @Override
    public CategorisationDto createCategorisation(ServiceContext ctx, String datasetVersionUrn, CategorisationDto categorisationDto) throws MetamacException {
        // Retrieve
        DatasetVersionDto datasetVersionDto = retrieveDatasetVersionByUrn(ctx, datasetVersionUrn);

        // Security
        DatasetsSecurityUtils.canCreateCategorisation(ctx, datasetVersionDto);

        // Transform
        Categorisation categorisation = datasetDto2DoMapper.categorisationDtoToDo(categorisationDto);

        // Create
        categorisation = getDatasetService().createCategorisation(ctx, datasetVersionUrn, categorisation);

        // Transform
        return datasetDo2DtoMapper.categorisationDoToDto(categorisation);
    }

    @Override
    public CategorisationDto retrieveCategorisationByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Retrieve
        Categorisation categorisation = getDatasetService().retrieveCategorisationByUrn(ctx, urn);

        // Security
        DatasetsSecurityUtils.canRetrieveCategorisationByUrn(ctx, categorisation);

        // Transform
        CategorisationDto categorisationDto = datasetDo2DtoMapper.categorisationDoToDto(categorisation);
        return categorisationDto;
    }

    @Override
    public void deleteCategorisation(ServiceContext ctx, String urn) throws MetamacException {
        // Retrieve
        Categorisation categorisation = getDatasetService().retrieveCategorisationByUrn(ctx, urn);

        // Security
        DatasetsSecurityUtils.canDeleteCategorisation(ctx, categorisation);

        // Delete
        getDatasetService().deleteCategorisation(ctx, urn);
    }

    @Override
    public List<CategorisationDto> retrieveCategorisationsByDatasetVersion(ServiceContext ctx, String datasetVersionUrn) throws MetamacException {
        // Retrieve
        DatasetVersionDto datasetVersionDto = retrieveDatasetVersionByUrn(ctx, datasetVersionUrn);

        // Security
        DatasetsSecurityUtils.canRetrieveCategorisationsByDatasetVersion(ctx, datasetVersionDto);

        // Retrieve
        List<Categorisation> categorisations = getDatasetService().retrieveCategorisationsByDatasetVersion(ctx, datasetVersionUrn);

        // Transform
        List<CategorisationDto> categorisationsDto = datasetDo2DtoMapper.categorisationDoListToDtoList(categorisations);
        return categorisationsDto;
    }

    @Override
    public CategorisationDto endCategorisationValidity(ServiceContext ctx, String urn, Date validTo) throws MetamacException {
        // Retrieve
        Categorisation categorisation = getDatasetService().retrieveCategorisationByUrn(ctx, urn);

        // Security
        DatasetsSecurityUtils.canEndCategorisationValidity(ctx, categorisation);

        // Delete
        categorisation = getDatasetService().endCategorisationValidity(ctx, urn, CoreCommonUtil.transformDateToDateTime(validTo));

        // Transform
        CategorisationDto categorisationDto = datasetDo2DtoMapper.categorisationDoToDto(categorisation);
        return categorisationDto;
    }

    // ------------------------------------------------------------------------
    // PUBLICATIONS
    // ------------------------------------------------------------------------

    @Override
    public MetamacCriteriaResult<RelatedResourceDto> findPublicationsByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        PublicationsSecurityUtils.canFindPublicationsByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = publicationMetamacCriteria2SculptorCriteriaMapper.getPublicationCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Add condition for latest queryVersion
        ConditionalCriteria latestPublicationVersionRestriction = ConditionalCriteriaBuilder.criteriaFor(PublicationVersion.class)
                .withProperty(PublicationVersionProperties.siemacMetadataStatisticalResource().lastVersion()).eq(Boolean.TRUE).buildSingle();
        sculptorCriteria.getConditions().add(latestPublicationVersionRestriction);

        // Find
        PagedResult<PublicationVersion> result = getPublicationService().findPublicationVersionsByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<RelatedResourceDto> metamacCriteriaResult = publicationSculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultPublicationRelatedResourceDto(result,
                sculptorCriteria.getPageSize());

        return metamacCriteriaResult;
    }

    // ------------------------------------------------------------------------
    // PUBLICATIONS VERSIONS
    // ------------------------------------------------------------------------

    @Override
    public PublicationVersionDto createPublication(ServiceContext ctx, PublicationVersionDto publicationVersionDto, ExternalItemDto statisticalOperationDto) throws MetamacException {
        // Security
        PublicationsSecurityUtils.canCreatePublication(ctx, statisticalOperationDto.getCode());

        // Transform
        PublicationVersion publicationVersion = publicationDto2DoMapper.publicationVersionDtoToDo(publicationVersionDto);
        ExternalItem statisticalOperation = publicationDto2DoMapper.externalItemDtoToDo(statisticalOperationDto, null, ServiceExceptionParameters.STATISTICAL_OPERATION);

        // Retrieve
        PublicationVersion publicationVersionCreated = getPublicationService().createPublicationVersion(ctx, publicationVersion, statisticalOperation);

        // Transform
        PublicationVersionDto publicationCreated = publicationDo2DtoMapper.publicationVersionDoToDto(publicationVersionCreated);

        return publicationCreated;
    }

    @Override
    public PublicationVersionDto updatePublicationVersion(ServiceContext ctx, PublicationVersionDto publicationVersionDto) throws MetamacException {
        // Security
        PublicationsSecurityUtils.canUpdatePublicationVersion(ctx, publicationVersionDto);

        // Transform
        PublicationVersion publicationVersion = publicationDto2DoMapper.publicationVersionDtoToDo(publicationVersionDto);

        // Update
        publicationVersion = getPublicationService().updatePublicationVersion(ctx, publicationVersion);

        // Transform
        return publicationDo2DtoMapper.publicationVersionDoToDto(publicationVersion);
    }

    @Override
    public void deletePublicationVersion(ServiceContext ctx, String urn) throws MetamacException {
        // Retrieve
        PublicationVersionDto publicationVersionDto = retrievePublicationVersionByUrn(ctx, urn);

        // Security
        PublicationsSecurityUtils.canDeletePublicationVersion(ctx, publicationVersionDto);

        // Delete
        getPublicationService().deletePublicationVersion(ctx, urn);

    }

    @Override
    public MetamacCriteriaResult<PublicationVersionBaseDto> findPublicationVersionByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        PublicationsSecurityUtils.canFindPublicationsVersionsByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = publicationVersionMetamacCriteria2SculptorCriteriaMapper.getPublicationVersionCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<PublicationVersion> result = getPublicationService().findPublicationVersionsByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<PublicationVersionBaseDto> metamacCriteriaResult = publicationVersionSculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultPublicationVersion(result,
                sculptorCriteria.getPageSize());

        return metamacCriteriaResult;
    }

    @Override
    public PublicationVersionDto retrievePublicationVersionByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Retrieve
        PublicationVersion publicationVersion = getPublicationService().retrievePublicationVersionByUrn(ctx, urn);

        // Security
        PublicationsSecurityUtils.canRetrievePublicationVersionByUrn(ctx, publicationVersion);

        // Transform
        return publicationDo2DtoMapper.publicationVersionDoToDto(publicationVersion);
    }

    @Override
    public PublicationVersionDto retrieveLatestPublicationVersion(ServiceContext ctx, String publicationUrn) throws MetamacException {
        // Retrieve
        PublicationVersion publication = getPublicationService().retrieveLatestPublicationVersionByPublicationUrn(ctx, publicationUrn);

        // Security
        if (!SharedPublicationsSecurityUtils.canRetrieveLatestPublicationVersion(SecurityUtils.getMetamacPrincipal(ctx),
                publication.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode(), publication.getSiemacMetadataStatisticalResource().getEffectiveProcStatus())) {
            publication = getPublicationService().retrieveLatestPublishedPublicationVersionByPublicationUrn(ctx, publicationUrn);
        }

        // Transform
        PublicationVersionDto publicationVersionDto = publicationDo2DtoMapper.publicationVersionDoToDto(publication);
        return publicationVersionDto;
    }

    @Override
    public PublicationVersionDto retrieveLatestPublishedPublicationVersion(ServiceContext ctx, String publicationUrn) throws MetamacException {
        // Security
        PublicationsSecurityUtils.canRetrieveLatestPublishedPublicationVersion(ctx);

        // Retrieve
        PublicationVersion publication = getPublicationService().retrieveLatestPublishedPublicationVersionByPublicationUrn(ctx, publicationUrn);

        // Transform
        PublicationVersionDto publicationVersionDto = publicationDo2DtoMapper.publicationVersionDoToDto(publication);
        return publicationVersionDto;
    }

    @Override
    public List<PublicationVersionBaseDto> retrievePublicationVersions(ServiceContext ctx, String urn) throws MetamacException {
        // Security
        PublicationsSecurityUtils.canRetrievePublicationVersions(ctx);

        // Retrieve
        List<PublicationVersion> publicationVersions = getPublicationService().retrievePublicationVersions(ctx, urn);

        // Transform
        List<PublicationVersionBaseDto> publications = publicationDo2DtoMapper.publicationVersionDoListToDtoList(publicationVersions);

        return publications;
    }

    @Override
    public PublicationStructureDto retrievePublicationVersionStructure(ServiceContext ctx, String publicationVersionUrn) throws MetamacException {
        // Retrieve
        PublicationVersion publicationVersion = getPublicationService().retrievePublicationVersionByUrn(ctx, publicationVersionUrn);

        // Security
        PublicationsSecurityUtils.canRetrievePublicationVersionStructure(ctx, publicationVersion);

        // Build structure
        PublicationStructureDto publicationStructureDto = publicationDo2DtoMapper.publicationVersionStructureDoToDto(publicationVersion);

        return publicationStructureDto;
    }

    @Override
    public void importPublicationVersionStructure(ServiceContext ctx, String publicationVersionUrn, URL fileURL, String language) throws MetamacException {

        // Retrieve
        PublicationVersion publicationVersion = getPublicationService().retrievePublicationVersionByUrn(ctx, publicationVersionUrn);

        // Security
        PublicationsSecurityUtils.canImportPublicationVersionStructure(ctx, publicationVersion);

        // Import
        getPublicationService().importPublicationVersionStructure(ctx, publicationVersionUrn, fileURL, language);
    }

    @Override
    public PublicationVersionDto sendPublicationVersionToProductionValidation(ServiceContext ctx, PublicationVersionDto publicationVersionDto) throws MetamacException {
        // Security
        PublicationsSecurityUtils.canSendPublicationVersionToProductionValidation(ctx, publicationVersionDto.getStatisticalOperation().getCode());

        // Transform
        PublicationVersion publicationVersion = publicationDto2DoMapper.publicationVersionDtoToDo(publicationVersionDto);

        // Send to production validation and retrieve
        publicationVersion = publicationLifecycleService.sendToProductionValidation(ctx, publicationVersion.getSiemacMetadataStatisticalResource().getUrn());

        // Transform
        publicationVersionDto = publicationDo2DtoMapper.publicationVersionDoToDto(publicationVersion);

        return publicationVersionDto;
    }

    @Override
    public PublicationVersionBaseDto sendPublicationVersionToProductionValidation(ServiceContext ctx, PublicationVersionBaseDto publicationVersionDto) throws MetamacException {
        // Security
        PublicationsSecurityUtils.canSendPublicationVersionToProductionValidation(ctx, publicationVersionDto.getStatisticalOperation().getCode());

        // Check optimistic locking
        publicationDto2DoMapper.checkOptimisticLocking(publicationVersionDto);

        // Send to production validation and retrieve
        PublicationVersion publicationVersion = publicationLifecycleService.sendToProductionValidation(ctx, publicationVersionDto.getUrn());

        // Transform
        publicationVersionDto = publicationDo2DtoMapper.publicationVersionDoToBaseDto(publicationVersion);

        return publicationVersionDto;
    }

    @Override
    public PublicationVersionDto sendPublicationVersionToDiffusionValidation(ServiceContext ctx, PublicationVersionDto publicationVersionDto) throws MetamacException {
        // Security
        PublicationsSecurityUtils.canSendPublicationVersionToDiffusionValidation(ctx, publicationVersionDto.getStatisticalOperation().getCode());

        // Transform
        PublicationVersion publicationVersion = publicationDto2DoMapper.publicationVersionDtoToDo(publicationVersionDto);

        // Send to production validation and retrieve
        publicationVersion = publicationLifecycleService.sendToDiffusionValidation(ctx, publicationVersion.getSiemacMetadataStatisticalResource().getUrn());

        // Transform
        publicationVersionDto = publicationDo2DtoMapper.publicationVersionDoToDto(publicationVersion);

        return publicationVersionDto;
    }

    @Override
    public PublicationVersionBaseDto sendPublicationVersionToDiffusionValidation(ServiceContext ctx, PublicationVersionBaseDto publicationVersionDto) throws MetamacException {
        // Security
        PublicationsSecurityUtils.canSendPublicationVersionToDiffusionValidation(ctx, publicationVersionDto.getStatisticalOperation().getCode());

        // Check optimistic locking
        publicationDto2DoMapper.checkOptimisticLocking(publicationVersionDto);

        // Send to production validation and retrieve
        PublicationVersion publicationVersion = publicationLifecycleService.sendToDiffusionValidation(ctx, publicationVersionDto.getUrn());

        // Transform
        publicationVersionDto = publicationDo2DtoMapper.publicationVersionDoToBaseDto(publicationVersion);

        return publicationVersionDto;
    }

    @Override
    public PublicationVersionDto sendPublicationVersionToValidationRejected(ServiceContext ctx, PublicationVersionDto publicationVersionDto) throws MetamacException {
        // Security
        PublicationsSecurityUtils.canSendPublicationVersionToValidationRejected(ctx, publicationVersionDto.getStatisticalOperation().getCode());

        // Transform
        PublicationVersion publicationVersion = publicationDto2DoMapper.publicationVersionDtoToDo(publicationVersionDto);

        // Send to production validation and retrieve
        publicationVersion = publicationLifecycleService.sendToValidationRejected(ctx, publicationVersion.getSiemacMetadataStatisticalResource().getUrn());

        // Transform
        publicationVersionDto = publicationDo2DtoMapper.publicationVersionDoToDto(publicationVersion);

        return publicationVersionDto;
    }

    @Override
    public PublicationVersionBaseDto sendPublicationVersionToValidationRejected(ServiceContext ctx, PublicationVersionBaseDto publicationVersionDto) throws MetamacException {
        // Security
        PublicationsSecurityUtils.canSendPublicationVersionToValidationRejected(ctx, publicationVersionDto.getStatisticalOperation().getCode());

        // Check optimistic locking
        publicationDto2DoMapper.checkOptimisticLocking(publicationVersionDto);

        // Send to production validation and retrieve
        PublicationVersion publicationVersion = publicationLifecycleService.sendToValidationRejected(ctx, publicationVersionDto.getUrn());

        // Transform
        publicationVersionDto = publicationDo2DtoMapper.publicationVersionDoToBaseDto(publicationVersion);

        return publicationVersionDto;
    }

    @Override
    public PublicationVersionDto publishPublicationVersion(ServiceContext ctx, PublicationVersionDto publicationVersionDto) throws MetamacException {
        // Security
        PublicationsSecurityUtils.canPublishPublicationVersion(ctx, publicationVersionDto.getStatisticalOperation().getCode());

        // Transform
        PublicationVersion publicationVersion = publicationDto2DoMapper.publicationVersionDtoToDo(publicationVersionDto);

        // Send to publish and retrieve
        publicationVersion = publicationLifecycleService.sendToPublished(ctx, publicationVersion.getSiemacMetadataStatisticalResource().getUrn());

        // Transform
        publicationVersionDto = publicationDo2DtoMapper.publicationVersionDoToDto(publicationVersion);

        return publicationVersionDto;
    }

    @Override
    public PublicationVersionBaseDto publishPublicationVersion(ServiceContext ctx, PublicationVersionBaseDto publicationVersionDto) throws MetamacException {
        // Security
        PublicationsSecurityUtils.canPublishPublicationVersion(ctx, publicationVersionDto.getStatisticalOperation().getCode());

        // Check optimistic locking
        publicationDto2DoMapper.checkOptimisticLocking(publicationVersionDto);

        // Send to publish and retrieve
        PublicationVersion publicationVersion = publicationLifecycleService.sendToPublished(ctx, publicationVersionDto.getUrn());

        // Transform
        publicationVersionDto = publicationDo2DtoMapper.publicationVersionDoToBaseDto(publicationVersion);

        return publicationVersionDto;
    }

    @Override
    public PublicationVersionBaseDto resendPublishedPublicationVersionStreamMessage(ServiceContext ctx, String publicationVersionUrn) throws MetamacException {
        // Retrieve Publication
        PublicationVersion publicationVersion = publicationVersionRepository.retrieveByUrn(publicationVersionUrn);

        // Security
        PublicationsSecurityUtils.canResendPublishedPublicationVersionStreamMessage(ctx, publicationVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode());

        // Send stream message to stream messaging service (like Apache Kafka)
        publicationLifecycleService.sendNewVersionPublishedStreamMessageByResource(ctx, publicationVersion);

        // Transform
        return publicationDo2DtoMapper.publicationVersionDoToBaseDto(publicationVersion);
    }

    @Override
    public PublicationVersionDto versioningPublicationVersion(ServiceContext ctx, PublicationVersionDto publicationVersionDto, VersionTypeEnum versionType) throws MetamacException {
        // Security
        PublicationsSecurityUtils.canVersionPublication(ctx, publicationVersionDto.getStatisticalOperation().getCode());

        // Transform
        PublicationVersion publicationVersion = publicationDto2DoMapper.publicationVersionDtoToDo(publicationVersionDto);

        // Versioning
        publicationVersion = publicationLifecycleService.versioning(ctx, publicationVersion.getSiemacMetadataStatisticalResource().getUrn(), versionType);

        // Transform
        publicationVersionDto = publicationDo2DtoMapper.publicationVersionDoToDto(publicationVersion);

        return publicationVersionDto;
    }

    @Override
    public PublicationVersionBaseDto versioningPublicationVersion(ServiceContext ctx, PublicationVersionBaseDto publicationVersionDto, VersionTypeEnum versionType) throws MetamacException {
        // Security
        PublicationsSecurityUtils.canVersionPublication(ctx, publicationVersionDto.getStatisticalOperation().getCode());

        // Check optimistic locking
        publicationDto2DoMapper.checkOptimisticLocking(publicationVersionDto);

        // Versioning
        PublicationVersion publicationVersion = publicationLifecycleService.versioning(ctx, publicationVersionDto.getUrn(), versionType);

        // Transform
        publicationVersionDto = publicationDo2DtoMapper.publicationVersionDoToBaseDto(publicationVersion);

        return publicationVersionDto;
    }

    // ------------------------------------------------------------------------
    // CHAPTERS
    // ------------------------------------------------------------------------

    @Override
    public ChapterDto createChapter(ServiceContext ctx, String publicationUrn, ChapterDto chapterDto) throws MetamacException {
        // Transform
        Chapter chapter = publicationDto2DoMapper.chapterDtoToDo(chapterDto);

        PublicationVersion publicationVersion = getPublicationService().retrievePublicationVersionByUrn(ctx, publicationUrn);

        // Security
        PublicationsSecurityUtils.canCreateChapter(ctx, publicationVersion);

        // Create
        chapter = getPublicationService().createChapter(ctx, publicationUrn, chapter);

        // Transform to dto
        chapterDto = publicationDo2DtoMapper.chapterDoToDto(chapter);
        return chapterDto;
    }

    @Override
    public ChapterDto updateChapter(ServiceContext ctx, ChapterDto chapterDto) throws MetamacException {
        // Transform
        Chapter chapter = publicationDto2DoMapper.chapterDtoToDo(chapterDto);

        // Security
        PublicationsSecurityUtils.canUpdateChapter(ctx, chapter);

        // Update
        chapter = getPublicationService().updateChapter(ctx, chapter);

        // Transform
        chapterDto = publicationDo2DtoMapper.chapterDoToDto(chapter);
        return chapterDto;
    }

    @Override
    public ChapterDto updateChapterLocation(ServiceContext ctx, String chapterUrn, String parentTargetUrn, Long orderInLevel) throws MetamacException {
        // Retrieve to check security
        Chapter chapter = getPublicationService().retrieveChapter(ctx, chapterUrn);

        // Security
        PublicationsSecurityUtils.canUpdateChapterLocation(ctx, chapter);

        // Update
        chapter = getPublicationService().updateChapterLocation(ctx, chapterUrn, parentTargetUrn, orderInLevel);

        // Transform to dto
        ChapterDto chapterDto = publicationDo2DtoMapper.chapterDoToDto(chapter);
        return chapterDto;
    }

    @Override
    public ChapterDto retrieveChapter(ServiceContext ctx, String chapterUrn) throws MetamacException {
        // Retrieve
        Chapter chapter = getPublicationService().retrieveChapter(ctx, chapterUrn);

        // Security
        PublicationsSecurityUtils.canRetrieveChapter(ctx, chapter);

        // Transform
        ChapterDto chapterDto = publicationDo2DtoMapper.chapterDoToDto(chapter);
        return chapterDto;
    }

    @Override
    public void deleteChapter(ServiceContext ctx, String chapterUrn) throws MetamacException {
        // Retrieve
        Chapter chapter = getPublicationService().retrieveChapter(ctx, chapterUrn);

        // Security
        PublicationsSecurityUtils.canDeleteChapter(ctx, chapter);

        // Delete
        getPublicationService().deleteChapter(ctx, chapterUrn);
    }

    // ------------------------------------------------------------------------
    // CUBES
    // ------------------------------------------------------------------------

    @Override
    public CubeDto createCube(ServiceContext ctx, String publicationUrn, CubeDto cubeDto) throws MetamacException {
        // Transform
        Cube cube = publicationDto2DoMapper.cubeDtoToDo(cubeDto);

        PublicationVersion publicationVersion = getPublicationService().retrievePublicationVersionByUrn(ctx, publicationUrn);

        // Security
        PublicationsSecurityUtils.canCreateCube(ctx, publicationVersion);

        // Create
        cube = getPublicationService().createCube(ctx, publicationUrn, cube);

        // Transform
        cubeDto = publicationDo2DtoMapper.cubeDoToDto(cube);
        return cubeDto;

    }

    @Override
    public CubeDto updateCube(ServiceContext ctx, CubeDto cubeDto) throws MetamacException {
        // Transform
        Cube cube = publicationDto2DoMapper.cubeDtoToDo(cubeDto);

        // Security
        PublicationsSecurityUtils.canUpdateCube(ctx, cube);

        // Update
        cube = getPublicationService().updateCube(ctx, cube);

        // Transform
        cubeDto = publicationDo2DtoMapper.cubeDoToDto(cube);
        return cubeDto;
    }

    @Override
    public CubeDto updateCubeLocation(ServiceContext ctx, String cubeUrn, String parentTargetUrn, Long orderInLevel) throws MetamacException {
        // Retrieve cube for security checkings
        Cube cube = getPublicationService().retrieveCube(ctx, cubeUrn);

        // Security
        PublicationsSecurityUtils.canUpdateCubeLocation(ctx, cube);

        // Update
        cube = getPublicationService().updateCubeLocation(ctx, cubeUrn, parentTargetUrn, orderInLevel);

        // Transform
        CubeDto cubeDto = publicationDo2DtoMapper.cubeDoToDto(cube);
        return cubeDto;
    }

    @Override
    public CubeDto retrieveCube(ServiceContext ctx, String cubeUrn) throws MetamacException {
        // Retrieve
        Cube cube = getPublicationService().retrieveCube(ctx, cubeUrn);

        // Security
        PublicationsSecurityUtils.canRetrieveCube(ctx, cube);

        // Transform
        CubeDto cubeDto = publicationDo2DtoMapper.cubeDoToDto(cube);
        return cubeDto;

    }

    @Override
    public void deleteCube(ServiceContext ctx, String cubeUrn) throws MetamacException {
        // Retrieve
        Cube cube = getPublicationService().retrieveCube(ctx, cubeUrn);

        // Security
        PublicationsSecurityUtils.canDeleteCube(ctx, cube);

        // Delete
        getPublicationService().deleteCube(ctx, cubeUrn);
    }

    // ------------------------------------------------------------------------
    // CONSTRAINTS
    // ------------------------------------------------------------------------

    @Override
    public List<ExternalItemDto> findContentConstraintsForArtefact(ServiceContext ctx, String artefactUrn) throws MetamacException {
        // Security
        ConstraintsSecurityUtils.canFindContentConstraintsForArtefact(ctx);

        // Find
        List<ResourceInternal> contentConstraintsForArtefact = constraintsService.findContentConstraintsForArtefact(ctx, artefactUrn);

        // Transform
        return constraintRest2DtoMapper.externalItemConstraintToExternalItemDto(contentConstraintsForArtefact);
    }

    @Override
    public ContentConstraintDto createContentConstraint(ServiceContext ctx, ContentConstraintDto contentConstraintDto) throws MetamacException {
        // Security
        DatasetVersion datasetVersion = obtainDatasetVersionFromContentConstraint(ctx, contentConstraintDto);
        ConstraintsSecurityUtils.canCreateContentConstraint(ctx, datasetVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode(),
                datasetVersion.getLifeCycleStatisticalResource().getProcStatus());

        // Transform
        ContentConstraint contentConstraint = constraintDto2RestMapper.constraintDtoTo(contentConstraintDto);

        // Create
        contentConstraint = constraintsService.createContentConstraint(ctx, contentConstraint);

        // Transform
        return constraintRest2DtoMapper.toConstraintDto(contentConstraint);
    }

    @Override
    public ContentConstraintDto retrieveContentConstraintByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Retrieve
        ContentConstraint contentConstraint = constraintsService.retrieveContentConstraintByUrn(ctx, urn, true);

        // Security
        DatasetVersion datasetVersion = obtainDatasetVersionFromContentConstraint(ctx, contentConstraint); // Dataset for extract operation
        ConstraintsSecurityUtils.canRetrieveContentConstraintByUrn(ctx, datasetVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode(),
                datasetVersion.getLifeCycleStatisticalResource().getProcStatus());

        // Transform
        return constraintRest2DtoMapper.toConstraintDto(contentConstraint);
    }

    @Override
    public void deleteContentConstraint(ServiceContext ctx, String urn) throws MetamacException {
        // Retrieve
        ContentConstraint contentConstraint = constraintsService.retrieveContentConstraintByUrn(ctx, urn, Boolean.TRUE);

        // Security
        DatasetVersion datasetVersion = obtainDatasetVersionFromContentConstraint(ctx, contentConstraint); // Dataset for extract operation
        ConstraintsSecurityUtils.canDeleteContentConstraint(ctx, datasetVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode(),
                datasetVersion.getLifeCycleStatisticalResource().getProcStatus());

        // Delete
        constraintsService.deleteContentConstraint(ctx, urn);
    }

    @Override
    public RegionValueDto saveRegionForContentConstraint(ServiceContext ctx, String contentConstraintUrn, RegionValueDto regionValueDto, DsdDimensionDto selectedDimensionDto, String datasetUrn)
            throws MetamacException {
        // Retrieve
        ContentConstraint contentConstraint = constraintsService.retrieveContentConstraintByUrn(ctx, contentConstraintUrn, Boolean.TRUE);

        // Security
        DatasetVersion datasetVersion = obtainDatasetVersionFromContentConstraint(ctx, contentConstraint); // Dataset for extract operation
        ConstraintsSecurityUtils.canSaveForContentConstraint(ctx, datasetVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode(),
                datasetVersion.getLifeCycleStatisticalResource().getProcStatus());

        if (contentConstraint.isIsFinal()) {
            // Can not update externally_published constraints. Internally published constraint can be updated because dataset is yet in draft.
            throw MetamacExceptionBuilder.builder()
                    .withPrincipalException(new MetamacExceptionItem(ServiceExceptionType.CONSTRAINTS_UPDATE_FINAL, contentConstraint.getConstraintAttachment().getUrn())).build();
        }

        // Transform
        RegionReference regionReference = constraintDto2RestMapper.toRegionReference(regionValueDto);

        checkObservationsWithContentConstraint(ctx, datasetUrn, regionReference, selectedDimensionDto);

        // Create
        regionReference = constraintsService.saveRegionForContentConstraint(ctx, regionReference);

        // Transform
        return constraintRest2DtoMapper.toRegionDto(regionReference);
    }

    private void checkObservationsWithContentConstraint(ServiceContext ctx, String datasetUrn, RegionReference regionReference, DsdDimensionDto selectedDimension) throws MetamacException {
        List<CodeDimensionDto> observationValuesByDimensionId = new ArrayList<>();
        List<MetamacExceptionItem> exceptions = new LinkedList<>();
        Map<String, CodeHierarchy> codeHierarchyMap = ConstraintsValidator.getCodeHierarchyMap(srmRestInternalService, selectedDimension);
        try {

            observationValuesByDimensionId = datasetRepositoriesServiceFacade.findObservationsValuesByDimensionId(datasetUrn, selectedDimension.getDimensionId());

            if (observationValuesByDimensionId == null || observationValuesByDimensionId.isEmpty()) {
                return;
            }

            Key keysByDimensionId = ConstraintsValidator.getConstraintKeysByDimensionId(regionReference, selectedDimension.getDimensionId());
            if (keysByDimensionId != null) {
                ConstraintsValidator.checkObservationContentConstraints(observationValuesByDimensionId, keysByDimensionId, codeHierarchyMap, exceptions);
            }

        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.CONSTRAINTS_UPDATE_CHECK_EXISTING_OBSERVATIONS, datasetUrn);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    @Override
    public void deleteRegion(ServiceContext ctx, String contentConstraintUrn, String regionCode) throws MetamacException {
        // Retrieve
        ContentConstraint contentConstraint = constraintsService.retrieveContentConstraintByUrn(ctx, contentConstraintUrn, Boolean.TRUE);

        // Security
        DatasetVersion datasetVersion = obtainDatasetVersionFromContentConstraint(ctx, contentConstraint); // Dataset for extract operation
        ConstraintsSecurityUtils.canDeleteRegion(ctx, datasetVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode(),
                datasetVersion.getLifeCycleStatisticalResource().getProcStatus());

        if (contentConstraint.isIsFinal()) {
            // Can not update final constraint
            throw MetamacExceptionBuilder.builder()
                    .withPrincipalException(new MetamacExceptionItem(ServiceExceptionType.CONSTRAINTS_UPDATE_FINAL, datasetVersion.getSiemacMetadataStatisticalResource().getUrn())).build();
        }

        // Delete
        constraintsService.deleteRegion(ctx, contentConstraintUrn, regionCode);
    }

    @Override
    public RegionValueDto retrieveRegionForContentConstraint(ServiceContext ctx, String contentConstraintUrn, String regionCode) throws MetamacException {
        // Retrieve
        ContentConstraint contentConstraint = constraintsService.retrieveContentConstraintByUrn(ctx, contentConstraintUrn, Boolean.TRUE);

        // Security
        DatasetVersion datasetVersion = obtainDatasetVersionFromContentConstraint(ctx, contentConstraint); // Dataset for extract operation
        ConstraintsSecurityUtils.canRetrieveRegionForContentConstraint(ctx, datasetVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode(),
                datasetVersion.getLifeCycleStatisticalResource().getProcStatus());

        // Find
        RegionReference regionReference = constraintsService.retrieveRegionForContentConstraint(ctx, contentConstraintUrn, regionCode);

        // Transform
        return constraintRest2DtoMapper.toRegionDto(regionReference);
    }

    protected DatasetVersion obtainDatasetVersionFromContentConstraint(ServiceContext ctx, ContentConstraintDto contentConstraintDto) throws MetamacException {
        if (contentConstraintDto == null || contentConstraintDto.getConstraintAttachment() == null || contentConstraintDto.getConstraintAttachment().getType() == null) {
            throw new MetamacException(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.CONTENT_CONSTRAINT_ATTACHMENT);
        }

        if (TypeExternalArtefactsEnum.DATASET.equals(contentConstraintDto.getConstraintAttachment().getType())) {
            throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.CONTENT_CONSTRAINT_ATTACHMENT);
        }

        // Retrieve
        DatasetVersion datasetVersion = getDatasetService().retrieveDatasetVersionByUrn(ctx, contentConstraintDto.getConstraintAttachment().getUrn());

        // Security
        DatasetsSecurityUtils.canRetrieveDatasetVersionByUrn(ctx, datasetVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode(),
                datasetVersion.getSiemacMetadataStatisticalResource().getEffectiveProcStatus());

        return datasetVersion;
    }

    protected DatasetVersion obtainDatasetVersionFromContentConstraint(ServiceContext ctx, ContentConstraint contentConstraint) throws MetamacException {
        if (contentConstraint == null || contentConstraint.getConstraintAttachment() == null || contentConstraint.getConstraintAttachment().getKind() == null) {
            throw new MetamacException(ServiceExceptionType.METADATA_REQUIRED, ServiceExceptionParameters.CONTENT_CONSTRAINT_ATTACHMENT);
        }

        if (!TypeExternalArtefactsEnum.DATASET.getValue().equals(contentConstraint.getConstraintAttachment().getKind())) {
            throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.CONTENT_CONSTRAINT_ATTACHMENT);
        }

        // Retrieve
        DatasetVersion datasetVersion = getDatasetService().retrieveDatasetVersionByUrn(ctx, contentConstraint.getConstraintAttachment().getUrn());

        // Security
        DatasetsSecurityUtils.canRetrieveDatasetVersionByUrn(ctx, datasetVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode(),
                datasetVersion.getSiemacMetadataStatisticalResource().getEffectiveProcStatus());

        return datasetVersion;
    }

    // ------------------------------------------------------------------------
    // MULTIDATASET VERSIONS
    // ------------------------------------------------------------------------

    @Override
    public MultidatasetVersionDto createMultidataset(ServiceContext ctx, MultidatasetVersionDto multidatasetVersionDto, ExternalItemDto statisticalOperationDto) throws MetamacException {
        // Security
        MultidatasetsSecurityUtils.canCreateMultidataset(ctx, statisticalOperationDto.getCode());

        // Transform
        MultidatasetVersion multidatasetVersion = multidatasetDto2DoMapper.multidatasetVersionDtoToDo(multidatasetVersionDto);
        ExternalItem statisticalOperation = multidatasetDto2DoMapper.externalItemDtoToDo(statisticalOperationDto, null, ServiceExceptionParameters.STATISTICAL_OPERATION);

        // Retrieve
        MultidatasetVersion multidatasetVersionCreated = getMultidatasetService().createMultidatasetVersion(ctx, multidatasetVersion, statisticalOperation);

        // Transform
        MultidatasetVersionDto multidatasetCreated = multidatasetDo2DtoMapper.multidatasetVersionDoToDto(multidatasetVersionCreated);

        return multidatasetCreated;
    }

    @Override
    public MultidatasetVersionDto updateMultidatasetVersion(ServiceContext ctx, MultidatasetVersionDto multidatasetVersionDto) throws MetamacException {
        // Security
        MultidatasetsSecurityUtils.canUpdateMultidatasetVersion(ctx, multidatasetVersionDto);

        // Transform
        MultidatasetVersion multidatasetVersion = multidatasetDto2DoMapper.multidatasetVersionDtoToDo(multidatasetVersionDto);

        // Update
        multidatasetVersion = getMultidatasetService().updateMultidatasetVersion(ctx, multidatasetVersion);

        // Transform
        return multidatasetDo2DtoMapper.multidatasetVersionDoToDto(multidatasetVersion);
    }

    @Override
    public void deleteMultidatasetVersion(ServiceContext ctx, String urn) throws MetamacException {
        // Retrieve
        MultidatasetVersionDto multidatasetVersionDto = retrieveMultidatasetVersionByUrn(ctx, urn);

        // Security
        MultidatasetsSecurityUtils.canDeleteMultidatasetVersion(ctx, multidatasetVersionDto);

        // Delete
        getMultidatasetService().deleteMultidatasetVersion(ctx, urn);
    }

    @Override
    public MetamacCriteriaResult<MultidatasetVersionBaseDto> findMultidatasetVersionByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        MultidatasetsSecurityUtils.canFindMultidatasetsVersionsByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = multidatasetVersionMetamacCriteria2SculptorCriteriaMapper.getMultidatasetVersionCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Find
        PagedResult<MultidatasetVersion> result = getMultidatasetService().findMultidatasetVersionsByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        MetamacCriteriaResult<MultidatasetVersionBaseDto> metamacCriteriaResult = multidatasetVersionSculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultMultidatasetVersion(result,
                sculptorCriteria.getPageSize());

        return metamacCriteriaResult;
    }

    @Override
    public MultidatasetVersionDto retrieveMultidatasetVersionByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Retrieve
        MultidatasetVersion multidatasetVersion = getMultidatasetService().retrieveMultidatasetVersionByUrn(ctx, urn);

        // Security
        MultidatasetsSecurityUtils.canRetrieveMultidatasetVersionByUrn(ctx, multidatasetVersion);

        // Transform
        return multidatasetDo2DtoMapper.multidatasetVersionDoToDto(multidatasetVersion);
    }

    @Override
    public List<MultidatasetVersionBaseDto> retrieveMultidatasetVersions(ServiceContext ctx, String multidatasetVersionUrn) throws MetamacException {
        // Security
        MultidatasetsSecurityUtils.canRetrieveMultidatasetVersions(ctx);

        // Retrieve
        List<MultidatasetVersion> multidatasetVersions = getMultidatasetService().retrieveMultidatasetVersions(ctx, multidatasetVersionUrn);

        // Transform
        List<MultidatasetVersionBaseDto> multidatasets = multidatasetDo2DtoMapper.multidatasetVersionDoListToDtoList(multidatasetVersions);

        return multidatasets;
    }

    @Override
    public MultidatasetVersionDto retrieveLatestMultidatasetVersion(ServiceContext ctx, String multidatasetUrn) throws MetamacException {
        // Retrieve
        MultidatasetVersion multidataset = getMultidatasetService().retrieveLatestMultidatasetVersionByMultidatasetUrn(ctx, multidatasetUrn);

        // Security
        if (!SharedMultidatasetsSecurityUtils.canRetrieveLatestMultidatasetVersion(SecurityUtils.getMetamacPrincipal(ctx),
                multidataset.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode(), multidataset.getSiemacMetadataStatisticalResource().getEffectiveProcStatus())) {
            multidataset = getMultidatasetService().retrieveLatestPublishedMultidatasetVersionByMultidatasetUrn(ctx, multidatasetUrn);
        }

        // Transform
        MultidatasetVersionDto multidatasetVersionDto = multidatasetDo2DtoMapper.multidatasetVersionDoToDto(multidataset);
        return multidatasetVersionDto;
    }

    @Override
    public MultidatasetVersionDto retrieveLatestPublishedMultidatasetVersion(ServiceContext ctx, String multidatasetUrn) throws MetamacException {
        // Security
        MultidatasetsSecurityUtils.canRetrieveLatestPublishedMultidatasetVersion(ctx);

        // Retrieve
        MultidatasetVersion multidataset = getMultidatasetService().retrieveLatestPublishedMultidatasetVersionByMultidatasetUrn(ctx, multidatasetUrn);

        // Transform
        MultidatasetVersionDto multidatasetVersionDto = multidatasetDo2DtoMapper.multidatasetVersionDoToDto(multidataset);
        return multidatasetVersionDto;
    }

    @Override
    public MultidatasetVersionDto sendMultidatasetVersionToProductionValidation(ServiceContext ctx, MultidatasetVersionDto multidatasetVersionDto) throws MetamacException {
        // Security
        MultidatasetsSecurityUtils.canSendMultidatasetVersionToProductionValidation(ctx, multidatasetVersionDto.getStatisticalOperation().getCode());

        // Transform
        MultidatasetVersion multidatasetVersion = multidatasetDto2DoMapper.multidatasetVersionDtoToDo(multidatasetVersionDto);

        // Send to production validation and retrieve
        multidatasetVersion = multidatasetLifecycleService.sendToProductionValidation(ctx, multidatasetVersion.getSiemacMetadataStatisticalResource().getUrn());

        // Transform
        multidatasetVersionDto = multidatasetDo2DtoMapper.multidatasetVersionDoToDto(multidatasetVersion);

        return multidatasetVersionDto;
    }

    @Override
    public MultidatasetVersionBaseDto sendMultidatasetVersionToProductionValidation(ServiceContext ctx, MultidatasetVersionBaseDto multidatasetVersionDto) throws MetamacException {
        // Security
        MultidatasetsSecurityUtils.canSendMultidatasetVersionToProductionValidation(ctx, multidatasetVersionDto.getStatisticalOperation().getCode());

        // Check optimistic locking
        multidatasetDto2DoMapper.checkOptimisticLocking(multidatasetVersionDto);

        // Send to production validation and retrieve
        MultidatasetVersion multidatasetVersion = multidatasetLifecycleService.sendToProductionValidation(ctx, multidatasetVersionDto.getUrn());

        // Transform
        multidatasetVersionDto = multidatasetDo2DtoMapper.multidatasetVersionDoToBaseDto(multidatasetVersion);

        return multidatasetVersionDto;
    }

    @Override
    public MultidatasetVersionDto sendMultidatasetVersionToDiffusionValidation(ServiceContext ctx, MultidatasetVersionDto multidatasetVersionDto) throws MetamacException {
        // Security
        MultidatasetsSecurityUtils.canSendMultidatasetVersionToDiffusionValidation(ctx, multidatasetVersionDto.getStatisticalOperation().getCode());

        // Transform
        MultidatasetVersion multidatasetVersion = multidatasetDto2DoMapper.multidatasetVersionDtoToDo(multidatasetVersionDto);

        // Send to production validation and retrieve
        multidatasetVersion = multidatasetLifecycleService.sendToDiffusionValidation(ctx, multidatasetVersion.getSiemacMetadataStatisticalResource().getUrn());

        // Transform
        multidatasetVersionDto = multidatasetDo2DtoMapper.multidatasetVersionDoToDto(multidatasetVersion);

        return multidatasetVersionDto;
    }

    @Override
    public MultidatasetVersionBaseDto sendMultidatasetVersionToDiffusionValidation(ServiceContext ctx, MultidatasetVersionBaseDto multidatasetVersionDto) throws MetamacException {
        // Security
        MultidatasetsSecurityUtils.canSendMultidatasetVersionToDiffusionValidation(ctx, multidatasetVersionDto.getStatisticalOperation().getCode());

        // Check optimistic locking
        multidatasetDto2DoMapper.checkOptimisticLocking(multidatasetVersionDto);

        // Send to production validation and retrieve
        MultidatasetVersion multidatasetVersion = multidatasetLifecycleService.sendToDiffusionValidation(ctx, multidatasetVersionDto.getUrn());

        // Transform
        multidatasetVersionDto = multidatasetDo2DtoMapper.multidatasetVersionDoToBaseDto(multidatasetVersion);

        return multidatasetVersionDto;
    }

    @Override
    public MultidatasetVersionDto sendMultidatasetVersionToValidationRejected(ServiceContext ctx, MultidatasetVersionDto multidatasetVersionDto) throws MetamacException {
        // Security
        MultidatasetsSecurityUtils.canSendMultidatasetVersionToValidationRejected(ctx, multidatasetVersionDto.getStatisticalOperation().getCode());

        // Transform
        MultidatasetVersion multidatasetVersion = multidatasetDto2DoMapper.multidatasetVersionDtoToDo(multidatasetVersionDto);

        // Send to production validation and retrieve
        multidatasetVersion = multidatasetLifecycleService.sendToValidationRejected(ctx, multidatasetVersion.getSiemacMetadataStatisticalResource().getUrn());

        // Transform
        multidatasetVersionDto = multidatasetDo2DtoMapper.multidatasetVersionDoToDto(multidatasetVersion);

        return multidatasetVersionDto;
    }

    @Override
    public MultidatasetVersionBaseDto sendMultidatasetVersionToValidationRejected(ServiceContext ctx, MultidatasetVersionBaseDto multidatasetVersionDto) throws MetamacException {
        // Security
        MultidatasetsSecurityUtils.canSendMultidatasetVersionToValidationRejected(ctx, multidatasetVersionDto.getStatisticalOperation().getCode());

        // Check optimistic locking
        multidatasetDto2DoMapper.checkOptimisticLocking(multidatasetVersionDto);

        // Send to production validation and retrieve
        MultidatasetVersion multidatasetVersion = multidatasetLifecycleService.sendToValidationRejected(ctx, multidatasetVersionDto.getUrn());

        // Transform
        multidatasetVersionDto = multidatasetDo2DtoMapper.multidatasetVersionDoToBaseDto(multidatasetVersion);

        return multidatasetVersionDto;
    }

    @Override
    public MultidatasetVersionDto publishMultidatasetVersion(ServiceContext ctx, MultidatasetVersionDto multidatasetVersionDto) throws MetamacException {
        // Security
        MultidatasetsSecurityUtils.canPublishMultidatasetVersion(ctx, multidatasetVersionDto.getStatisticalOperation().getCode());

        // Transform
        MultidatasetVersion multidatasetVersion = multidatasetDto2DoMapper.multidatasetVersionDtoToDo(multidatasetVersionDto);

        // Send to publish and retrieve
        multidatasetVersion = multidatasetLifecycleService.sendToPublished(ctx, multidatasetVersion.getSiemacMetadataStatisticalResource().getUrn());

        // Transform
        multidatasetVersionDto = multidatasetDo2DtoMapper.multidatasetVersionDoToDto(multidatasetVersion);

        return multidatasetVersionDto;
    }

    @Override
    public MultidatasetVersionBaseDto publishMultidatasetVersion(ServiceContext ctx, MultidatasetVersionBaseDto multidatasetVersionDto) throws MetamacException {
        // Security
        MultidatasetsSecurityUtils.canPublishMultidatasetVersion(ctx, multidatasetVersionDto.getStatisticalOperation().getCode());

        // Check optimistic locking
        multidatasetDto2DoMapper.checkOptimisticLocking(multidatasetVersionDto);

        // Send to publish and retrieve
        MultidatasetVersion multidatasetVersion = multidatasetLifecycleService.sendToPublished(ctx, multidatasetVersionDto.getUrn());

        // Transform
        multidatasetVersionDto = multidatasetDo2DtoMapper.multidatasetVersionDoToBaseDto(multidatasetVersion);

        return multidatasetVersionDto;
    }

    @Override
    public MultidatasetVersionBaseDto resendPublishedMultidatasetVersionStreamMessage(ServiceContext ctx, String multidatasetVersionUrn) throws MetamacException {
        // Retrieve Multidataset
        MultidatasetVersion multidatasetVersion = multidatasetVersionRepository.retrieveByUrn(multidatasetVersionUrn);

        // Security
        MultidatasetsSecurityUtils.canResendPublishedMultidatasetVersionStreamMessage(ctx, multidatasetVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode());

        // Transform
        return multidatasetDo2DtoMapper.multidatasetVersionDoToBaseDto(multidatasetVersion);
    }

    @Override
    public MultidatasetVersionDto versioningMultidatasetVersion(ServiceContext ctx, MultidatasetVersionDto multidatasetVersionDto, VersionTypeEnum versionType) throws MetamacException {
        // Security
        MultidatasetsSecurityUtils.canVersionMultidataset(ctx, multidatasetVersionDto.getStatisticalOperation().getCode());

        // Transform
        MultidatasetVersion multidatasetVersion = multidatasetDto2DoMapper.multidatasetVersionDtoToDo(multidatasetVersionDto);

        // Versioning
        multidatasetVersion = multidatasetLifecycleService.versioning(ctx, multidatasetVersion.getSiemacMetadataStatisticalResource().getUrn(), versionType);

        // Transform
        multidatasetVersionDto = multidatasetDo2DtoMapper.multidatasetVersionDoToDto(multidatasetVersion);

        return multidatasetVersionDto;
    }

    @Override
    public MultidatasetVersionBaseDto versioningMultidatasetVersion(ServiceContext ctx, MultidatasetVersionBaseDto multidatasetVersionDto, VersionTypeEnum versionType) throws MetamacException {
        // Security
        MultidatasetsSecurityUtils.canVersionMultidataset(ctx, multidatasetVersionDto.getStatisticalOperation().getCode());

        // Check optimistic locking
        multidatasetDto2DoMapper.checkOptimisticLocking(multidatasetVersionDto);

        // Versioning
        MultidatasetVersion multidatasetVersion = multidatasetLifecycleService.versioning(ctx, multidatasetVersionDto.getUrn(), versionType);

        // Transform
        multidatasetVersionDto = multidatasetDo2DtoMapper.multidatasetVersionDoToBaseDto(multidatasetVersion);

        return multidatasetVersionDto;
    }

    // ------------------------------------------------------------------------
    // MULTIDATASET CUBES
    // ------------------------------------------------------------------------

    @Override
    public MultidatasetCubeDto createMultidatasetCube(ServiceContext ctx, String multidatasetUrn, MultidatasetCubeDto multidatasetcubeDto) throws MetamacException {
        // Transform
        MultidatasetCube multidatasetcube = multidatasetDto2DoMapper.multidatasetCubeDtoToDo(multidatasetcubeDto);

        MultidatasetVersion multidatasetVersion = getMultidatasetService().retrieveMultidatasetVersionByUrn(ctx, multidatasetUrn);

        // Security
        MultidatasetsSecurityUtils.canCreateMultidatasetCube(ctx, multidatasetVersion);

        // Create
        multidatasetcube = getMultidatasetService().createMultidatasetCube(ctx, multidatasetUrn, multidatasetcube);

        // Transform
        multidatasetcubeDto = multidatasetDo2DtoMapper.multidatasetCubeDoToDto(multidatasetcube);
        return multidatasetcubeDto;

    }

    @Override
    public MultidatasetCubeDto updateMultidatasetCube(ServiceContext ctx, MultidatasetCubeDto multidatasetcubeDto) throws MetamacException {
        // Transform
        MultidatasetCube multidatasetcube = multidatasetDto2DoMapper.multidatasetCubeDtoToDo(multidatasetcubeDto);

        // Security
        MultidatasetsSecurityUtils.canUpdateMultidatasetCube(ctx, multidatasetcube);

        // Update
        multidatasetcube = getMultidatasetService().updateMultidatasetCube(ctx, multidatasetcube);

        // Transform
        multidatasetcubeDto = multidatasetDo2DtoMapper.multidatasetCubeDoToDto(multidatasetcube);
        return multidatasetcubeDto;
    }

    @Override
    public MultidatasetCubeDto retrieveMultidatasetCube(ServiceContext ctx, String multidatasetcubeUrn) throws MetamacException {
        // Retrieve
        MultidatasetCube multidatasetcube = getMultidatasetService().retrieveMultidatasetCube(ctx, multidatasetcubeUrn);

        // Security
        MultidatasetsSecurityUtils.canRetrieveMultidatasetCube(ctx, multidatasetcube);

        // Transform
        MultidatasetCubeDto multidatasetcubeDto = multidatasetDo2DtoMapper.multidatasetCubeDoToDto(multidatasetcube);
        return multidatasetcubeDto;
    }

    @Override
    public void deleteMultidatasetCube(ServiceContext ctx, String multidatasetcubeUrn) throws MetamacException {
        // Retrieve
        MultidatasetCube multidatasetcube = getMultidatasetService().retrieveMultidatasetCube(ctx, multidatasetcubeUrn);

        // Security
        MultidatasetsSecurityUtils.canDeleteMultidatasetCube(ctx, multidatasetcube);

        // Delete
        getMultidatasetService().deleteMultidatasetCube(ctx, multidatasetcubeUrn);
    }

    @Override
    public MultidatasetCubeDto updateMultidatasetCubeLocation(ServiceContext ctx, String multidatasetCubeUrn, Long orderInMultidataset) throws MetamacException {
        // Retrieve multidatasetcube for security checkings
        MultidatasetCube multidatasetCube = getMultidatasetService().retrieveMultidatasetCube(ctx, multidatasetCubeUrn);

        // Security
        MultidatasetsSecurityUtils.canUpdateMultidatasetCubeLocation(ctx, multidatasetCube);

        // Update
        multidatasetCube = getMultidatasetService().updateMultidatasetCubeLocation(ctx, multidatasetCubeUrn, orderInMultidataset);

        // Transform
        MultidatasetCubeDto multidatasetCubeDto = multidatasetDo2DtoMapper.multidatasetCubeDoToDto(multidatasetCube);
        return multidatasetCubeDto;
    }

    @Override
    public MetamacCriteriaResult<RelatedResourceDto> findMultidatasetsByCondition(ServiceContext ctx, MetamacCriteria criteria) throws MetamacException {
        // Security
        MultidatasetsSecurityUtils.canFindMultidatasetsByCondition(ctx);

        // Transform
        SculptorCriteria sculptorCriteria = multidatasetMetamacCriteria2SculptorCriteriaMapper.getMultidatasetCriteriaMapper().metamacCriteria2SculptorCriteria(criteria);

        // Add condition for latest multidatasetVersions
        ConditionalCriteria latestMultidatasetVersionRestriction = ConditionalCriteriaBuilder.criteriaFor(MultidatasetVersion.class)
                .withProperty(MultidatasetVersionProperties.siemacMetadataStatisticalResource().lastVersion()).eq(Boolean.TRUE).buildSingle();
        sculptorCriteria.getConditions().add(latestMultidatasetVersionRestriction);

        // Find
        PagedResult<MultidatasetVersion> result = getMultidatasetService().findMultidatasetVersionsByCondition(ctx, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        return multidatasetSculptorCriteria2MetamacCriteriaMapper.pageResultToMetamacCriteriaResultMultidatasetRelatedResourceDto(result, sculptorCriteria.getPageSize());
    }

    @Override
    public void createDatabaseDatasourceInDatasetVersion(ServiceContext ctx, String datasetVersionUrn, String tableName) throws MetamacException {
        // Retrieve
        DatasetVersionDto datasetVersionDto = retrieveDatasetVersionByUrn(ctx, datasetVersionUrn);

        // Security
        DatasetsSecurityUtils.canImportDatasourcesInDatasetVersion(ctx, datasetVersionDto.getStatisticalOperation().getCode());

        // Transform for optimistic locking
        DatasetVersion datasetVersion = datasetDto2DoMapper.datasetVersionDtoToDo(datasetVersionDto);

        // Service
        getDatasetService().createDatabaseDatasourceInDatasetVersion(ctx, datasetVersion.getSiemacMetadataStatisticalResource().getUrn(), tableName);
    }

    @Override
    public DatasetVersionDto copyDatasetVersion(ServiceContext ctx, DatasetVersionDto datasetVersionDto) throws MetamacException {
        // Security
        DatasetsSecurityUtils.canCreateDataset(ctx, datasetVersionDto.getStatisticalOperation().getCode());
        // Copy
        DatasetVersion datasetVersionCreated = getDatasetService().copyDatasetVersion(ctx, datasetVersionDto.getUrn());
        return datasetDo2DtoMapper.datasetVersionDoToDto(ctx, datasetVersionCreated);
    }

    @Override
    public void updateDatasetVersionInGroup(ServiceContext ctx, DatasetVersionDto datasetVersionMetadataToChangeDto, String datasetUrnToChange, List<CategorisationDto> categorisationsDto)
            throws MetamacException {

        DatasetVersion datasetVersion = datasetDto2DoMapper.datasetVersionDtoToDo(datasetVersionMetadataToChangeDto);

        for (CategorisationDto categorisationDto : categorisationsDto) {
            datasetVersion.addCategorisation(datasetDto2DoMapper.categorisationDtoToDo(categorisationDto));
        }

        getDatasetService().updateDatasetVersionInGroup(ctx, datasetVersion, datasetUrnToChange);

    }

    @Override
    public QueryVersionDto resendPublishedQueryVersionXMessage(ServiceContext ctx, String datasetVersionUrn) throws MetamacException {
        // Retrieve Query
        QueryVersion datasetVersion = queryVersionRepository.retrieveByUrn(datasetVersionUrn);

        // Security
        DatasetsSecurityUtils.canResendPublishedQueryVersionXMessage(ctx, datasetVersion.getLifeCycleStatisticalResource().getStatisticalOperation().getCode());

        // Send x message
        queryLifecycleService.checkTwitterPostActivatedAndPostTwit(ctx, datasetVersion);

        // Transform
        return queryDo2DtoMapper.queryVersionDoToDto(datasetVersion);
    }

    @Override
    public void processSrmResourcesKafkaMessage(ServiceContext ctx, SpecificRecordBase message) throws MetamacException {
        es.gobcan.istac.edatos.dataset.repository.dto.ExternalItemDto externalItemDto = kafkaMapper.kafkaMessageToRepositoryExternalItemDto(ctx, message);
        try {
            datasetRepositoriesServiceFacade.processSrmResources(kafkaMapper.kafkaMessageToRepositoryExternalItemDto(ctx, message));
        } catch (ApplicationException e) {
            throw new MetamacException(e, ServiceExceptionType.CREATE_OR_UPDATE_SRM_RESOURCE, externalItemDto.getUrn());
        }
    }

}
