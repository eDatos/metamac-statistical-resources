package org.siemac.metamac.statistical.resources.core.lifecycle.serviceimpl.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.statistical.resources.core.common.serviceapi.TranslationService;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.dataset.domain.TemporalCode;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.XStreamStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.utils.ProcStatusEnumUtils;
import org.siemac.metamac.statistical.resources.core.enume.utils.QueryStatusEnumUtils;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.lifecycle.LifecycleCommonMetadataChecker;
import org.siemac.metamac.statistical.resources.core.lifecycle.serviceapi.query.QueryLifecycleService;
import org.siemac.metamac.statistical.resources.core.lifecycle.serviceimpl.LifecycleTemplateService;
import org.siemac.metamac.statistical.resources.core.notices.ServiceNoticeMessage;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersionRepository;
import org.siemac.metamac.statistical.resources.core.query.domain.CodeItem;
import org.siemac.metamac.statistical.resources.core.query.domain.QuerySelectionItem;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersionRepository;
import org.siemac.metamac.statistical.resources.core.query.serviceapi.QueryService;
import org.siemac.metamac.statistical.resources.core.query.utils.QueryVersioningCopyUtils;
import org.siemac.metamac.statistical.resources.core.utils.TemporalDimensionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.edatos.dataset.repository.dto.CodeDimensionDto;
import es.gobcan.istac.edatos.dataset.repository.dto.ConditionDimensionDto;
import es.gobcan.istac.edatos.dataset.repository.dto.ObservationExtendedDto;
import es.gobcan.istac.edatos.dataset.repository.service.DatasetRepositoriesServiceFacade;
import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.tweet.Tweet;
import io.github.redouane59.twitter.signature.TwitterCredentials;

@Service("queryLifecycleService")
public class QueryLifecycleServiceImpl extends LifecycleTemplateService<QueryVersion> implements QueryLifecycleService {

    @Autowired
    private LifecycleCommonMetadataChecker    lifecycleCommonMetadataChecker;

    @Autowired
    private QueryVersionRepository            queryVersionRepository;

    @Autowired
    private PublicationVersionRepository      publicationVersionRepository;

    @Autowired
    private QueryService                      queryService;

    @Autowired
    private TranslationService                translationService;

    @Autowired
    private DatasetVersionRepository          datasetVersionRepository;

    @Autowired
    private StatisticalResourcesConfiguration configurationService;

    @Autowired
    private DatasetRepositoriesServiceFacade  datasetRepositoriesServiceFacade;
    
    private List<String>                      temporalCodes = new ArrayList<>();

    private static Logger       logger              = LoggerFactory.getLogger(QueryLifecycleServiceImpl.class);

    @Override
    protected String getResourceMetadataName() throws MetamacException {
        return ServiceExceptionParameters.QUERY_VERSION;
    }

    // ------------------------------------------------------------------------------------------------------
    // >> PRODUCTION VALIDATION
    // ------------------------------------------------------------------------------------------------------

    @Override
    protected void checkSendToProductionValidationResource(QueryVersion resource, List<MetamacExceptionItem> exceptions) throws MetamacException {
        // nothing specific to check
    }

    @Override
    protected void applySendToProductionValidationResource(ServiceContext ctx, QueryVersion resource) throws MetamacException {
        // nothing specific to apply
    }

    // ------------------------------------------------------------------------------------------------------
    // >> DIFFUSION VALIDATION
    // ------------------------------------------------------------------------------------------------------

    @Override
    protected void checkSendToDiffusionValidationResource(QueryVersion resource, List<MetamacExceptionItem> exceptions) throws MetamacException {
        // nothing specific to check
    }

    @Override
    protected void applySendToDiffusionValidationResource(ServiceContext ctx, QueryVersion resource) throws MetamacException {
        // nothing specific to apply
    }

    // ------------------------------------------------------------------------------------------------------
    // >> VALIDATION REJECTED
    // ------------------------------------------------------------------------------------------------------

    @Override
    protected void checkSendToValidationRejectedResource(QueryVersion resource, List<MetamacExceptionItem> exceptions) throws MetamacException {
        // nothing specific to check
    }

    @Override
    protected void applySendToValidationRejectedResource(ServiceContext ctx, QueryVersion resource) throws MetamacException {
        // nothing specific to apply
    }

    // ------------------------------------------------------------------------------------------------------
    // >> PUBLISHED
    // ------------------------------------------------------------------------------------------------------

    @Override
    protected void checkSendToPublishedResource(ServiceContext ctx, QueryVersion resource, List<MetamacExceptionItem> exceptionItems) throws MetamacException {
        QueryStatusEnumUtils.checkPossibleQueryStatus(resource, QueryStatusEnum.ACTIVE, QueryStatusEnum.DISCONTINUED);
        this.checkLinkedDatasetOrDatasetVersionPublishedForQuery(ctx, resource, exceptionItems);
    }

    @Override
    public void checkLinkedDatasetOrDatasetVersionPublishedBeforeQuery(ServiceContext ctx, QueryVersion resource) throws MetamacException {
        if (ProcStatusEnumUtils.isInAnyProcStatus(resource, ProcStatusEnum.PUBLISHED)) {
            List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
            checkLinkedDatasetOrDatasetVersionPublishedForQuery(ctx, resource, exceptionItems);
            ExceptionUtils.throwIfException(exceptionItems);
        }
    }

    @Override
    protected void applySendToPublishedCurrentResource(ServiceContext ctx, QueryVersion resource, QueryVersion previousResource) throws MetamacException {
        // nothing to do
    }

    @Override
    protected void applySendToPublishedPreviousResource(ServiceContext ctx, QueryVersion resource) throws MetamacException {
        // nothing to do
    }

    private void checkLinkedDatasetOrDatasetVersionPublishedForQuery(ServiceContext ctx, QueryVersion resource, List<MetamacExceptionItem> exceptionItems) throws MetamacException {
        if (resource.getDataset() != null) {
            this.checkLinkedDatasetPublishedOrVisibleBeforeQuery(ctx, resource, exceptionItems);
        } else if (resource.getFixedDatasetVersion() != null) {
            this.checkLinkedDatasetVersionPublishedOrVisibleForQuery(resource, exceptionItems);
        } else {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.QUERY_VERSION_PUBLISH_MUST_LINK_TO_DATASET, resource.getLifeCycleStatisticalResource().getUrn()));
        }
    }

    private void checkLinkedDatasetPublishedOrVisibleBeforeQuery(ServiceContext ctx, QueryVersion resource, List<MetamacExceptionItem> exceptionItems) throws MetamacException {
        String datasetUrn = resource.getDataset().getIdentifiableStatisticalResource().getUrn();
        DatasetVersion lastVersion = this.datasetVersionRepository.retrieveLastVersion(datasetUrn);

        if (!this.isDatasetVersionPublishedAndVisibleBeforeOrEqualDate(lastVersion, resource.getLifeCycleStatisticalResource().getValidFrom())) {
            DatasetVersion lastPublishedVersion = this.datasetVersionRepository.retrieveLastPublishedVersion(datasetUrn);
            if (lastPublishedVersion != null) {
                if (!this.queryService.checkQueryCompatibility(ctx, resource, lastPublishedVersion)) {
                    exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.QUERY_VERSION_NOT_COMPATIBLE_WITH_LAST_PUBLISHED_DATASET_VERSION, datasetUrn));
                }
            } else {
                exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.QUERY_VERSION_DATASET_WITH_NO_PUBLISHED_VERSION, datasetUrn));
            }
        }
    }

    private void checkLinkedDatasetVersionPublishedOrVisibleForQuery(QueryVersion resource, List<MetamacExceptionItem> exceptionItems) {
        String datasetVersionUrn = resource.getFixedDatasetVersion().getSiemacMetadataStatisticalResource().getUrn();
        DatasetVersion datasetVersion = resource.getFixedDatasetVersion();

        if (!this.isDatasetVersionPublishedAndVisibleBeforeOrEqualDate(datasetVersion, resource.getLifeCycleStatisticalResource().getValidFrom())) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.QUERY_VERSION_DATASET_VERSION_MUST_BE_PUBLISHED, datasetVersionUrn));
        }
    }

    private boolean isDatasetVersionPublishedAndVisibleBeforeOrEqualDate(DatasetVersion datasetVersion, DateTime date) {
        if (ProcStatusEnumUtils.isInAnyProcStatus(datasetVersion, ProcStatusEnum.PUBLISHED)) {
            return true;
        }
        return false;
    }

    // ------------------------------------------------------------------------------------------------------
    // >> VERSIONING
    // ------------------------------------------------------------------------------------------------------

    @Override
    protected void checkVersioningResource(QueryVersion resource, List<MetamacExceptionItem> exceptionItems) throws MetamacException {
        // nothing specific to check
    }

    @Override
    protected QueryVersion copyResourceForVersioning(ServiceContext ctx, QueryVersion previousResource) throws MetamacException {
        return QueryVersioningCopyUtils.copyQueryVersion(previousResource);
    }

    @Override
    protected void applyVersioningNewResource(ServiceContext ctx, QueryVersion resource, QueryVersion previous) throws MetamacException {
        // nothing specific to apply
    }

    @Override
    protected void applyVersioningPreviousResource(ServiceContext ctx, QueryVersion resource) throws MetamacException {
        // nothing specific to apply
    }

    @Override
    protected QueryVersion updateResourceUrn(QueryVersion resource) throws MetamacException {
        String[] creator = new String[]{resource.getLifeCycleStatisticalResource().getMaintainer().getCodeNested()};
        resource.getLifeCycleStatisticalResource().setUrn(GeneratorUrnUtils.generateSiemacStatisticalResourceQueryVersionUrn(creator, resource.getLifeCycleStatisticalResource().getCode(),
                resource.getLifeCycleStatisticalResource().getVersionLogic()));
        return resource;
    }

    // ------------------------------------------------------------------------------------------------------
    // GENERAL ABSTRACT METHODS
    // ------------------------------------------------------------------------------------------------------

    @Override
    protected QueryVersion saveResource(QueryVersion resource) {
        return this.queryVersionRepository.save(resource);
    }

    @Override
    protected QueryVersion retrieveResourceByUrn(String urn) throws MetamacException {
        return this.queryVersionRepository.retrieveByUrn(urn);
    }

    @Override
    protected QueryVersion retrieveResourceByResource(QueryVersion resource) throws MetamacException {
        return this.queryVersionRepository.retrieveByUrn(resource.getLifeCycleStatisticalResource().getUrn());
    }

    @Override
    protected QueryVersion retrievePreviousPublishedResourceByResource(QueryVersion resource) throws MetamacException {
        return this.queryVersionRepository.retrieveLastPublishedVersion(resource.getQuery().getIdentifiableStatisticalResource().getUrn());
    }

    @Override
    protected void checkResourceMetadataAllActions(ServiceContext ctx, QueryVersion resource, List<MetamacExceptionItem> exceptions) throws MetamacException {
        this.lifecycleCommonMetadataChecker.checkQueryVersionCommonMetadata(resource, ServiceExceptionParameters.QUERY_VERSION, exceptions);
    }

    @Override
    protected String getResourceUrn(QueryVersion resource) {
        return resource.getLifeCycleStatisticalResource().getUrn();
    }

    @Override
    public void sendNewVersionPublishedStreamMessageByResource(ServiceContext ctx, QueryVersion resource) {
        try {
            streamMessagingServiceFacade.sendNewVersionPublished(resource);
        } catch (MetamacException e) {
            createStreamMessageSentNotification(ctx, resource);
        }
    }

    @Override
    public void resendDatasetStreamMessage(ServiceContext ctx) throws MetamacException {
        // ONLY FOR DATASETS
    }

    @Override
    public void checkTwitterPostActivatedAndPostTwit(ServiceContext ctx, QueryVersion resource) {
        try {
            if (!configurationService.retrieveTwitterSentEnable()) {
                return;
            }
            if (resource.getPurposes() != null && StatisticalResourcesConstants.SOCIAL_NETWORK_PURPOSE.equals(resource.getPurposes().getIdentifier())) {
                DatasetVersion lastDatasetVersion = datasetVersionRepository.retrieveLastPublishedVersion(resource.getDataset().getIdentifiableStatisticalResource().getUrn());
                int dataSize = safeCalculateDataSize(resource.getSelection(), lastDatasetVersion, resource);
                if (dataSize > 1) {
                    throw new MetamacException(ServiceExceptionType.UNKNOWN, "there are too many observations in the query " + resource.getLifeCycleStatisticalResource().getCode());
                }
                List<ConditionDimensionDto> conditions = generateConditions(resource.getSelection());
                Map<String, ObservationExtendedDto> observations = datasetRepositoriesServiceFacade.findObservationsExtendedByDimensions(lastDatasetVersion.getDatasetRepositoryId(), conditions);
                sendPostTwitter(ctx, resource, observations);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            createXMessageSentNotification(ctx, resource, ServiceNoticeMessage.X_MESSAGE_SEND_ERROR);
            updateXStreamStatus(resource, XStreamStatusEnum.FAILED);
        }
    }

    private void sendPostTwitter(ServiceContext ctx, QueryVersion queryVersion, Map<String, ObservationExtendedDto> observations) throws MetamacException {
        if (StringUtils.isBlank(queryVersion.getXTemplate())) {
            createXMessageSentNotification(ctx, queryVersion, ServiceNoticeMessage.QUERY_NOT_HAVE_X_TEMPLATE);
            updateXStreamStatus(queryVersion, XStreamStatusEnum.FAILED);
            return;
        }
        String xPublication = getXPublication(observations, queryVersion, ctx);
        // remember that we have to update the x page for the application to be read and write
        TwitterClient twitterClient = new TwitterClient(
                TwitterCredentials.builder().accessToken(configurationService.retrieveTwitterAccessToken()).accessTokenSecret(configurationService.retrieveTwitterAccesTokenSecret())
                        .apiKey(configurationService.retrieveTwitterApiKey()).apiSecretKey(configurationService.retrieveTwitterApiSecretKey()).build());
        twitterClient.setAutomaticRetry(false);
        Tweet tweet = twitterClient.postTweet(xPublication);
        if (tweet.getText() == null) {
            createXMessageSentNotification(ctx, queryVersion, ServiceNoticeMessage.X_MESSAGE_SEND_ERROR);
            updateXStreamStatus(queryVersion, XStreamStatusEnum.FAILED);
            return;
        }
        updateXStreamStatus(queryVersion, XStreamStatusEnum.SENT);
    }

    private int safeCalculateDataSize(List<QuerySelectionItem> querySelectionItems, DatasetVersion datasetVersion, QueryVersion queryVersion) throws MetamacException {
        int dataSize = 1;
        for (QuerySelectionItem selectionItem : querySelectionItems) {
            if (StatisticalResourcesConstants.TEMPORAL_DIMENSION_ID.equals(selectionItem.getDimension())) {
                this.temporalCodes = TemporalDimensionUtils.calculateEffectiveTemporalDimensionValuesToQuery(queryVersion, getTemporalCoverageCodes(datasetVersion), getCodeItemList(selectionItem.getCodes()));
                dataSize = safeMultiply(dataSize, this.temporalCodes.size());
            } else {
                dataSize = safeMultiply(dataSize, selectionItem.getCodes().size());
            }
        }
        return dataSize;
    }

    private int safeMultiply(int a, int b) throws MetamacException {
        long res = (long) a * (long) b;
        if (res > Integer.MAX_VALUE || res < Integer.MIN_VALUE) {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "An overflow occurred while performing the multiplication " + a + " and " + b);
        }
        return (int) res;
    }

    private List<String> getTemporalCoverageCodes(DatasetVersion datasetVersion) {
        List<String> temporalCoverageCodes = new ArrayList<>();
        for (TemporalCode temporalCode : datasetVersion.getTemporalCoverage()) {
            temporalCoverageCodes.add(temporalCode.getTitle());
        }
        return temporalCoverageCodes;
    }

    private List<String> getCodeItemList(List<CodeItem> codeItems) {
        if (codeItems == null) {
            return null;
        }
        List<String> temporalCoverageCodes = new ArrayList<>();
        for (CodeItem temporalCode : codeItems) {
            temporalCoverageCodes.add(temporalCode.getTitle());
        }
        return temporalCoverageCodes;
    }

    private void updateXStreamStatus(QueryVersion resource, XStreamStatusEnum status) {
        resource.getLifeCycleStatisticalResource().setXStreamStatus(status);
        saveResource(resource);
    }

    private String getXPublication(Map<String, ObservationExtendedDto> observations, QueryVersion query, ServiceContext ctx) throws MetamacException {
        if (observations.size() > 1) {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "there are too many observations in the query " + query.getLifeCycleStatisticalResource().getCode());
        }

        if (!observations.isEmpty()) {
            // Get the only entry in the map
            Entry<String, ObservationExtendedDto> entry = observations.entrySet().iterator().next();

            return setMessageLanguageDefault(query, entry, ctx);
        }
        return "";
    }

    private String setMessageLanguageDefault(QueryVersion query, Entry<String, ObservationExtendedDto> entry, ServiceContext ctx) throws MetamacException {
        String temporalDimensionValue = getTemporalDimensionValueName(entry.getValue(), ctx);
        String decoded = query.getXTemplate().replace("{periodo}", temporalDimensionValue);
        decoded = Jsoup.clean(decoded.replace("{datos}", entry.getValue().getPrimaryMeasure()), new Whitelist());
        return Parser.unescapeEntities(decoded, true);
    }

    private String getTemporalDimensionValueName(ObservationExtendedDto observation, ServiceContext ctx) throws MetamacException {
        for (CodeDimensionDto codeDimension : observation.getCodesDimension())  {
            if (StatisticalResourcesConstants.TEMPORAL_DIMENSION_ID.equals(codeDimension.getDimensionId())) {
                Map<String, String> title = translationService.retrieveTimeTranslation(ctx, codeDimension.getCodeDimensionId());
                return title.get(configurationService.retrieveLanguageDefault());
            }
        }
        return "";
    }

    private List<ConditionDimensionDto> generateConditions(List<QuerySelectionItem> querySelectionItems) {
        List<ConditionDimensionDto> conditionDimensionDtos = new ArrayList<ConditionDimensionDto>();
        for (QuerySelectionItem querySelectionItem : querySelectionItems) {
            ConditionDimensionDto conditionDimensionDto = new ConditionDimensionDto();
            conditionDimensionDto.setDimensionId(querySelectionItem.getDimension());
            if (StatisticalResourcesConstants.TEMPORAL_DIMENSION_ID.equals(querySelectionItem.getDimension())) {
                conditionDimensionDto.getCodesDimension().addAll(this.temporalCodes);
            } else {
                for (CodeItem codeItem : querySelectionItem.getCodes()) {
                    conditionDimensionDto.getCodesDimension().add(codeItem.getCode());
                }
            }
            conditionDimensionDtos.add(conditionDimensionDto);
        }
        return conditionDimensionDtos;
    }
}