package org.siemac.metamac.statistical.resources.core.lifecycle.serviceimpl.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionRationaleType;
import org.siemac.metamac.statistical.resources.core.common.serviceapi.TranslationService;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.TemporalCode;
import org.siemac.metamac.statistical.resources.core.enume.domain.VersionRationaleTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.XStreamStatusEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.query.domain.CodeItem;
import org.siemac.metamac.statistical.resources.core.query.domain.QuerySelectionItem;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersionRepository;
import org.siemac.metamac.statistical.resources.core.utils.TemporalDimensionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.edatos.dataset.repository.dto.CodeDimensionDto;
import es.gobcan.istac.edatos.dataset.repository.dto.ConditionDimensionDto;
import es.gobcan.istac.edatos.dataset.repository.dto.ObservationExtendedDto;
import es.gobcan.istac.edatos.dataset.repository.service.DatasetRepositoriesServiceFacade;
import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.tweet.Tweet;
import io.github.redouane59.twitter.signature.TwitterCredentials;

/**
 * Utility component for posting messages to Twitter/X social network.
 * Centralizes Twitter posting logic for both Dataset and Query lifecycle services.
 */
@Component
public class TwitterPostUtils {

    private static final Logger              logger = LoggerFactory.getLogger(TwitterPostUtils.class);

    @Autowired
    private QueryVersionRepository           queryVersionRepository;

    @Autowired
    private DatasetRepositoriesServiceFacade datasetRepositoriesServiceFacade;

    @Autowired
    private TranslationService               translationService;

    @Autowired
    private StatisticalResourcesConfiguration configurationService;

    /**
     * Post Twitter messages for all queries associated with a published dataset version.
     * Only processes queries with SOCIAL_NETWORK purpose and valid version rationale types.
     *
     * @param ctx Service context
     * @param datasetVersion The published dataset version
     * @return TwitterPostResult with status and any errors
     */
    public TwitterPostResult postTwitterForDatasetQueries(ServiceContext ctx, DatasetVersion datasetVersion) {
        TwitterPostResult result = new TwitterPostResult();
        List<QueryVersion> queriesDataset = queryVersionRepository.findQueriesPublishedLinkedToDataset(datasetVersion.getDataset().getId());

        try {
            if (!configurationService.retrieveTwitterSentEnable()) {
                result.setSkipped(true);
                return result;
            }
            if (!checkVersionRationaleTypeEnum(datasetVersion)) {
                result.setSkipped(true);
                return result;
            }
        } catch (MetamacException e) {
            for (QueryVersion queryVersion : queriesDataset) {
                updateXStreamStatus(queryVersion, XStreamStatusEnum.FAILED);
            }
            result.addError(TwitterPostError.configuration(e));
            return result;
        }

        for (QueryVersion queryVersion : queriesDataset) {
            try {
                if (queryVersion.getPurposes() != null
                        && StatisticalResourcesConstants.SOCIAL_NETWORK_PURPOSE.equals(queryVersion.getPurposes().getIdentifier())) {
                    TwitterPostError error = postTwitterForQueryVersion(ctx, queryVersion, datasetVersion);
                    if (error != null) {
                        result.addError(error);
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                updateXStreamStatus(queryVersion, XStreamStatusEnum.FAILED);
                result.addError(TwitterPostError.forQuery(queryVersion, TwitterPostErrorType.SEND_ERROR, e));
            }
        }

        return result;
    }

    /**
     * Post Twitter message for a single query version.
     *
     * @param ctx Service context
     * @param queryVersion The query version to post
     * @return TwitterPostResult with status and any errors
     */
    public TwitterPostResult postTwitterForQuery(ServiceContext ctx, QueryVersion queryVersion) {
        TwitterPostResult result = new TwitterPostResult();

        try {
            if (!configurationService.retrieveTwitterSentEnable()) {
                result.setSkipped(true);
                return result;
            }

            DatasetVersion datasetVersion = resolveDatasetVersion(queryVersion);
            if (datasetVersion == null) {
                updateXStreamStatus(queryVersion, XStreamStatusEnum.FAILED);
                result.addError(TwitterPostError.forQuery(queryVersion, TwitterPostErrorType.NO_DATA, null));
                return result;
            }

            TwitterPostError error = postTwitterForQueryVersion(ctx, queryVersion, datasetVersion);
            if (error != null) {
                result.addError(error);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            updateXStreamStatus(queryVersion, XStreamStatusEnum.FAILED);
            result.addError(TwitterPostError.forQuery(queryVersion, TwitterPostErrorType.SEND_ERROR, e));
        }

        return result;
    }

    /**
     * Internal method to post Twitter message for a query version with associated dataset.
     *
     * @return TwitterPostError if there was an error, null otherwise
     */
    private TwitterPostError postTwitterForQueryVersion(ServiceContext ctx, QueryVersion queryVersion, DatasetVersion datasetVersion) throws Exception {

        // Check if query has X template
        if (StringUtils.isBlank(queryVersion.getXTemplate())) {
            updateXStreamStatus(queryVersion, XStreamStatusEnum.FAILED);
            return TwitterPostError.forQuery(queryVersion, TwitterPostErrorType.NO_X_TEMPLATE, null);
        }

        // Calculate data size to ensure there's only one observation
        List<String> temporalCodes = new ArrayList<String>();
        int dataSize = calculateDataSize(queryVersion.getSelection(), datasetVersion, queryVersion, temporalCodes);
        if (dataSize > 1) {
            updateXStreamStatus(queryVersion, XStreamStatusEnum.FAILED);
            throw new MetamacException(ServiceExceptionType.QUERY_SOCIAL_NETWORK_NOT_UNIQUE_RESULT, queryVersion.getLifeCycleStatisticalResource().getCode());
        }
        if (dataSize == 0) {
            updateXStreamStatus(queryVersion, XStreamStatusEnum.FAILED);
            return TwitterPostError.forQuery(queryVersion, TwitterPostErrorType.NO_DATA, null);
        }

        // Get observations from dataset repository
        List<ConditionDimensionDto> conditions = generateConditions(queryVersion.getSelection(), temporalCodes);
        Map<String, ObservationExtendedDto> observations = datasetRepositoriesServiceFacade.findObservationsExtendedByDimensions(datasetVersion.getDatasetRepositoryId(),
                conditions);

        // Send tweet
        return sendTweetToTwitter(ctx, queryVersion, observations);
    }

    /**
     * Send tweet to Twitter/X.
     *
     * @return TwitterPostError if there was an error, null otherwise
     */
    private TwitterPostError sendTweetToTwitter(ServiceContext ctx, QueryVersion queryVersion, Map<String, ObservationExtendedDto> observations) throws MetamacException {

        String xPublication = buildXPublication(observations, queryVersion, ctx);
        if (StringUtils.isBlank(xPublication)) {
            updateXStreamStatus(queryVersion, XStreamStatusEnum.FAILED);
            return TwitterPostError.forQuery(queryVersion, TwitterPostErrorType.NO_DATA, null);
        }

        // Create Twitter client and post tweet
        TwitterClient twitterClient = new TwitterClient(TwitterCredentials.builder().accessToken(configurationService.retrieveTwitterAccessToken())
                .accessTokenSecret(configurationService.retrieveTwitterAccesTokenSecret()).apiKey(configurationService.retrieveTwitterApiKey())
                .apiSecretKey(configurationService.retrieveTwitterApiSecretKey()).build());
        twitterClient.setAutomaticRetry(false);

        Tweet tweet = twitterClient.postTweet(xPublication);

        if (tweet.getText() == null) {
            updateXStreamStatus(queryVersion, XStreamStatusEnum.FAILED);
            return TwitterPostError.forQuery(queryVersion, TwitterPostErrorType.SEND_ERROR, null);
        } else {
            updateXStreamStatus(queryVersion, XStreamStatusEnum.SENT);
            return null;
        }
    }

    /**
     * Build the X/Twitter publication message by replacing template variables.
     */
    private String buildXPublication(Map<String, ObservationExtendedDto> observations, QueryVersion query, ServiceContext ctx) throws MetamacException {
        if (observations.size() > 1) {
            throw new MetamacException(ServiceExceptionType.QUERY_SOCIAL_NETWORK_NOT_UNIQUE_RESULT, query.getLifeCycleStatisticalResource().getCode());
        }

        if (!observations.isEmpty()) {
            // Get the only entry in the map
            Entry<String, ObservationExtendedDto> entry = observations.entrySet().iterator().next();
            return buildMessageFromTemplate(query, entry, ctx);
        }
        return "";
    }

    /**
     * Build the message from the X template by replacing placeholders.
     */
    private String buildMessageFromTemplate(QueryVersion query, Entry<String, ObservationExtendedDto> entry, ServiceContext ctx) throws MetamacException {
        String temporalDimensionValue = getTemporalDimensionValueName(entry.getValue(), ctx);
        String formattedData = formatNumericValue(entry.getValue().getPrimaryMeasure());
        String decoded = query.getXTemplate().replace("{periodo}", temporalDimensionValue);
        decoded = decoded.replace("{datos}", formattedData);

        // Preserve line breaks before Jsoup cleaning (Jsoup removes them)
        String newlinePlaceholder = "{{NEWLINE}}";
        decoded = decoded.replace("\r\n", newlinePlaceholder).replace("\n", newlinePlaceholder).replace("\r", newlinePlaceholder);

        decoded = Jsoup.clean(decoded, new Whitelist());
        decoded = Parser.unescapeEntities(decoded, true);

        // Restore line breaks
        decoded = decoded.replace(newlinePlaceholder, "\n");

        return decoded;
    }

    /**
     * Format a numeric string with Spanish locale (thousands separator: '.', decimal separator: ',').
     * If the value is not a valid number, returns it unchanged.
     *
     * @param value The numeric string to format
     * @return Formatted number string or original value if not a number
     */
    private String formatNumericValue(String value) {
        if (StringUtils.isBlank(value)) {
            return " ";
        }

        try {
            // Parse the number (handles both integer and decimal values)
            BigDecimal number = new BigDecimal(value.trim());

            // Format with Spanish locale (thousands: '.', decimal: ',')
            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(new Locale("es", "ES"));
            formatter.setGroupingUsed(true);
            formatter.setMaximumFractionDigits(number.scale() > 0 ? number.scale() : 0);
            formatter.setMinimumFractionDigits(0);

            return formatter.format(number);
        } catch (NumberFormatException e) {
            // Not a valid number, return original value
            return value;
        }
    }

    /**
     * Get the translated name for the temporal dimension value.
     */
    private String getTemporalDimensionValueName(ObservationExtendedDto observation, ServiceContext ctx) throws MetamacException {
        for (CodeDimensionDto codeDimension : observation.getCodesDimension()) {
            if (StatisticalResourcesConstants.TEMPORAL_DIMENSION_ID.equals(codeDimension.getDimensionId())) {
                Map<String, String> title = translationService.retrieveTimeTranslation(ctx, codeDimension.getCodeDimensionId());
                return title.get(configurationService.retrieveLanguageDefault());
            }
        }
        return "";
    }

    /**
     * Calculate the data size for the query selection.
     */
    private int calculateDataSize(List<QuerySelectionItem> querySelectionItems, DatasetVersion datasetVersion, QueryVersion queryVersion, List<String> temporalCodesOutput)
            throws MetamacException {
        int dataSize = 1;
        for (QuerySelectionItem selectionItem : querySelectionItems) {
            if (StatisticalResourcesConstants.TEMPORAL_DIMENSION_ID.equals(selectionItem.getDimension())) {
                List<String> temporalCodes = TemporalDimensionUtils.calculateEffectiveTemporalDimensionValuesToQuery(queryVersion, getTemporalCoverageCodes(datasetVersion),
                        getCodeItemList(selectionItem.getCodes()));
                temporalCodesOutput.addAll(temporalCodes);
                dataSize = safeMultiply(dataSize, temporalCodes.size());
            } else {
                dataSize = safeMultiply(dataSize, selectionItem.getCodes().size());
            }
        }
        return dataSize;
    }

    /**
     * Generate conditions for querying the dataset repository.
     */
    private List<ConditionDimensionDto> generateConditions(List<QuerySelectionItem> querySelectionItems, List<String> temporalCodes) {
        List<ConditionDimensionDto> conditionDimensionDtos = new ArrayList<ConditionDimensionDto>();
        for (QuerySelectionItem querySelectionItem : querySelectionItems) {
            ConditionDimensionDto conditionDimensionDto = new ConditionDimensionDto();
            conditionDimensionDto.setDimensionId(querySelectionItem.getDimension());
            if (StatisticalResourcesConstants.TEMPORAL_DIMENSION_ID.equals(querySelectionItem.getDimension())) {
                conditionDimensionDto.getCodesDimension().addAll(temporalCodes);
            } else {
                for (CodeItem codeItem : querySelectionItem.getCodes()) {
                    conditionDimensionDto.getCodesDimension().add(codeItem.getCode());
                }
            }
            conditionDimensionDtos.add(conditionDimensionDto);
        }
        return conditionDimensionDtos;
    }

    /**
     * Get temporal coverage codes from dataset version.
     */
    private List<String> getTemporalCoverageCodes(DatasetVersion datasetVersion) {
        List<String> temporalCoverageCodes = new ArrayList<String>();
        for (TemporalCode temporalCode : datasetVersion.getTemporalCoverage()) {
            temporalCoverageCodes.add(temporalCode.getTitle());
        }
        return temporalCoverageCodes;
    }

    /**
     * Convert code items to list of strings.
     */
    private List<String> getCodeItemList(List<CodeItem> codeItems) {
        if (codeItems == null) {
            return null;
        }
        List<String> codes = new ArrayList<String>();
        for (CodeItem codeItem : codeItems) {
            codes.add(codeItem.getTitle());
        }
        return codes;
    }

    /**
     * Safe multiplication to avoid integer overflow.
     */
    private int safeMultiply(int a, int b) throws MetamacException {
        long res = (long) a * (long) b;
        if (res > Integer.MAX_VALUE || res < Integer.MIN_VALUE) {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "An overflow occurred while performing the multiplication " + a + " and " + b);
        }
        return (int) res;
    }

    /**
     * Check if the version rationale type allows Twitter posting.
     */
    private boolean checkVersionRationaleTypeEnum(DatasetVersion resource) {
        if (resource.getSiemacMetadataStatisticalResource().getVersionRationaleTypes() != null
                && !resource.getSiemacMetadataStatisticalResource().getVersionRationaleTypes().isEmpty()) {
            for (VersionRationaleType versionRationaleType : resource.getSiemacMetadataStatisticalResource().getVersionRationaleTypes()) {
                if (VersionRationaleTypeEnum.MINOR_DATA_UPDATE.equals(versionRationaleType.getValue())
                        || VersionRationaleTypeEnum.MINOR_SERIES_UPDATE.equals(versionRationaleType.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Update the X stream status for a query version.
     */
    private void updateXStreamStatus(QueryVersion resource, XStreamStatusEnum status) {
        resource.getLifeCycleStatisticalResource().setXStreamStatus(status);
        queryVersionRepository.save(resource);
    }

    /**
     * Resolve the dataset version from a query version.
     */
    private DatasetVersion resolveDatasetVersion(QueryVersion queryVersion) {
        if (queryVersion.getFixedDatasetVersion() != null) {
            return queryVersion.getFixedDatasetVersion();
        }
        return null;
    }

    /**
     * Result object containing the outcome of Twitter posting operations.
     */
    public static class TwitterPostResult {
        private boolean                  skipped = false;
        private List<TwitterPostError>   errors  = new ArrayList<TwitterPostError>();

        public boolean isSkipped() {
            return skipped;
        }

        public void setSkipped(boolean skipped) {
            this.skipped = skipped;
        }

        public List<TwitterPostError> getErrors() {
            return errors;
        }

        public void addError(TwitterPostError error) {
            this.errors.add(error);
        }

        public boolean hasErrors() {
            return !errors.isEmpty();
        }
    }

    /**
     * Error information for Twitter posting failures.
     */
    public static class TwitterPostError {
        private QueryVersion        queryVersion;
        private TwitterPostErrorType errorType;
        private Exception           exception;

        private TwitterPostError(QueryVersion queryVersion, TwitterPostErrorType errorType, Exception exception) {
            this.queryVersion = queryVersion;
            this.errorType = errorType;
            this.exception = exception;
        }

        public static TwitterPostError forQuery(QueryVersion queryVersion, TwitterPostErrorType errorType, Exception exception) {
            return new TwitterPostError(queryVersion, errorType, exception);
        }

        public static TwitterPostError configuration(Exception exception) {
            return new TwitterPostError(null, TwitterPostErrorType.CONFIGURATION, exception);
        }

        public QueryVersion getQueryVersion() {
            return queryVersion;
        }

        public TwitterPostErrorType getErrorType() {
            return errorType;
        }

        public Exception getException() {
            return exception;
        }
    }

    /**
     * Types of errors that can occur during Twitter posting.
     */
    public enum TwitterPostErrorType {
        NO_X_TEMPLATE, NO_DATA, SEND_ERROR, CONFIGURATION
    }
}
