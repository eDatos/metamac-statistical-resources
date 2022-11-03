package org.siemac.metamac.statistical.resources.core.common.serviceimpl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.time.DateTimeSdmx;
import org.siemac.metamac.core.common.time.GregorianTimeSdmx;
import org.siemac.metamac.core.common.time.ReportingTimePeriodSdmx;
import org.siemac.metamac.core.common.time.TimeRangeSdmx;
import org.siemac.metamac.core.common.time.TimeSdmx;
import org.siemac.metamac.core.common.time.TimeSdmxTypeEnum;
import org.siemac.metamac.core.common.util.SdmxTimeUtils;
import org.siemac.metamac.statistical.resources.core.common.domain.LocalisedString;
import org.siemac.metamac.statistical.resources.core.common.domain.Translation;
import org.siemac.metamac.statistical.resources.core.common.serviceapi.validators.TranslationServiceInvocationValidator;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionParameters;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * Implementation of TranslationService.
 */
@Service("translationService")
public class TranslationServiceImpl extends TranslationServiceImplBase {
    
    private static final String                   TRANSLATION_SERVICE_CACHE_NAME = "translationServiceCache";

    @Autowired
    private TranslationServiceInvocationValidator translationServiceInvocationValidator;

    @Autowired
    private StatisticalResourcesConfiguration     configurationService;

    private CacheManager                          translationServiceCacheManager;
    private Cache                                 translationServiceCache;

    public TranslationServiceImpl() {
    }

    @PostConstruct
    public void init() {
        translationServiceCacheManager = CacheManager.getInstance();
        translationServiceCacheManager.addCacheIfAbsent(TRANSLATION_SERVICE_CACHE_NAME);

        translationServiceCache = translationServiceCacheManager.getCache(TRANSLATION_SERVICE_CACHE_NAME);
    }

    @PreDestroy
    public void stop() {
        translationServiceCacheManager.shutdown();
    }

    @Override
    public Map<String, String> retrieveTimeTranslation(ServiceContext ctx, String time) throws MetamacException {

        // Validations
        translationServiceInvocationValidator.checkRetrieveTimeTranslation(ctx, time);

        // Parse
        TimeSdmx timeSdmx = SdmxTimeUtils.parseTime(time);
        if (timeSdmx == null) {
            throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.TIME);
        }

               
        // Translate
        String translationCode = getTimeSdmxTranslationCode(timeSdmx);
        
        Translation translation = findTranslationByCode(translationCode);

        if (translation == null) {
            // Put code as title
            Map<String, String> title = new HashMap<String, String>(1);
            String defaultLocale = configurationService.retrieveLanguageDefault();
            title.put(defaultLocale, time);
            return title;
        } else {
            return translateTimeWithTemplate(timeSdmx, translation);
        }
    }
    
    private Translation findTranslationByCode(String translationCode) {
        Element element;

        if ((element = translationServiceCache.get(translationCode)) == null) {
            Translation translation = getTranslationRepository().findTranslationByCode(translationCode);
            element = new Element(translationCode, translation);
            element.setEternal(Boolean.TRUE);
            translationServiceCache.put(element);
        }
        return (Translation) element.getObjectValue();
    }

    /**
     * Get translate code will be in translation table database
     */
    private String getTimeSdmxTranslationCode(TimeSdmx timeSdmx) throws MetamacException {
        switch (timeSdmx.getType()) {
            case DATETIME:
                return StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_DATETIME;
            case GREGORIAN_TIME_YEAR:
                return StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_YEAR;
            case GREGORIAN_TIME_MONTH:
                return StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_MONTH;
            case GREGORIAN_TIME_DATE:
                return StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_DATE;
            case TIME_RANGE_DATE:
                return StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_DATE_RANGE;
            case TIME_RANGE_DATETIME:
                return StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_DATETIME_RANGE;
            case REPORTING_TIME_PERIOD_YEAR:
                return StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_YEAR;
            case REPORTING_TIME_PERIOD_SEMESTER:
                return StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_SEMESTER_PREFIX + "." + ((ReportingTimePeriodSdmx) timeSdmx).getSubperiodComplete();
            case REPORTING_TIME_PERIOD_TRIMESTER:
                return StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_TRIMESTER_PREFIX + "." + ((ReportingTimePeriodSdmx) timeSdmx).getSubperiodComplete();
            case REPORTING_TIME_PERIOD_QUARTER:
                return StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_QUARTER_PREFIX + "." + ((ReportingTimePeriodSdmx) timeSdmx).getSubperiodComplete();
            case REPORTING_TIME_PERIOD_MONTH:
                return StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_MONTH;
            case REPORTING_TIME_PERIOD_WEEK:
                return StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_WEEK;
            case REPORTING_TIME_PERIOD_DAY:
                return StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_DATE;
            default:
                throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.TIME);
        }
    }

    private Map<String, String> translateTimeWithTemplate(TimeSdmx timeSdmx, Translation translationTemplate) throws MetamacException {
        Map<String, String> title = new HashMap<String, String>(translationTemplate.getTitle().getTexts().size());
        for (LocalisedString localisedString : translationTemplate.getTitle().getTexts()) {
            String translationTemplateLabel = localisedString.getLabel();
            String label = translateTimeWithTemplate(timeSdmx, translationTemplateLabel);
            title.put(localisedString.getLocale(), label);
        }
        return title;
    }

    /**
     * Replace year, month... in translation template
     */
    private String translateTimeWithTemplate(TimeSdmx timeSdmx, String label) throws MetamacException {
        if (timeSdmx.isDateTime()) {
            DateTimeSdmx dateTimeSdmx = (DateTimeSdmx) timeSdmx;
            label = label.replace(StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_YEAR_IN_LABEL, dateTimeSdmx.getYear());
            label = label.replace(StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_MONTH_IN_LABEL, dateTimeSdmx.getMonth());
            label = label.replace(StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_DAY_IN_LABEL, dateTimeSdmx.getDay());
            label = label.replace(StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_HOUR_IN_LABEL, dateTimeSdmx.getHour());
            label = label.replace(StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_MINUTE_IN_LABEL, dateTimeSdmx.getMinute());
            label = label.replace(StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_SECOND_IN_LABEL, dateTimeSdmx.getSecond());
            return label;
        } else if (timeSdmx.isGregorianTime()) {
            GregorianTimeSdmx gregorianTimeSdmx = (GregorianTimeSdmx) timeSdmx;
            label = label.replace(StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_YEAR_IN_LABEL, gregorianTimeSdmx.getYear());
            if (gregorianTimeSdmx.getMonth() != null) {
                label = label.replace(StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_MONTH_IN_LABEL, gregorianTimeSdmx.getMonth());
                if (gregorianTimeSdmx.getDay() != null) {
                    label = label.replace(StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_DAY_IN_LABEL, gregorianTimeSdmx.getDay());
                }
            }
            return label;
        } else if (timeSdmx.isReportingTimePeriod()) {
            ReportingTimePeriodSdmx reportingTimePeriodSdmx = (ReportingTimePeriodSdmx) timeSdmx;
            label = label.replace(StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_YEAR_IN_LABEL, reportingTimePeriodSdmx.getYear());
            // special reporting date
            if (TimeSdmxTypeEnum.REPORTING_TIME_PERIOD_MONTH.equals(timeSdmx.getType()) || TimeSdmxTypeEnum.REPORTING_TIME_PERIOD_DAY.equals(timeSdmx.getType())) {
                label = label.replace(StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_MONTH_IN_LABEL,
                        StringUtils.leftPad(String.valueOf(reportingTimePeriodSdmx.getStartDateTime().getMonthOfYear()), 2, '0'));
                label = label.replace(StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_DAY_IN_LABEL,
                        StringUtils.leftPad(String.valueOf(reportingTimePeriodSdmx.getStartDateTime().getDayOfMonth()), 2, '0'));
            } else if (TimeSdmxTypeEnum.REPORTING_TIME_PERIOD_WEEK.equals(timeSdmx.getType())) {
                label = label.replace(StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_WEEK_IN_LABEL, reportingTimePeriodSdmx.getSubperiod());
            }
            return label;
        } else if (timeSdmx.isTimeRange()) {
            TimeRangeSdmx timeRangeSdmx = (TimeRangeSdmx) timeSdmx;
            label = label.replace(StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_YEAR_IN_LABEL, timeRangeSdmx.getStartYear());
            label = label.replace(StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_MONTH_IN_LABEL, timeRangeSdmx.getStartMonth());
            label = label.replace(StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_DAY_IN_LABEL, timeRangeSdmx.getStartDay());
            label = label.replace(StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_YEAR_END_IN_LABEL, timeRangeSdmx.getEndYear());
            label = label.replace(StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_MONTH_END_IN_LABEL, timeRangeSdmx.getEndMonth());
            label = label.replace(StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_DAY_END_IN_LABEL, timeRangeSdmx.getEndDay());
            if (TimeSdmxTypeEnum.TIME_RANGE_DATETIME.equals(timeSdmx.getType())) {
                label = label.replace(StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_HOUR_IN_LABEL, timeRangeSdmx.getStartHour());
                label = label.replace(StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_MINUTE_IN_LABEL, timeRangeSdmx.getStartMinute());
                label = label.replace(StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_SECOND_IN_LABEL, timeRangeSdmx.getStartSecond());
                label = label.replace(StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_HOUR_END_IN_LABEL, timeRangeSdmx.getEndHour());
                label = label.replace(StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_MINUTE_END_IN_LABEL, timeRangeSdmx.getEndMinute());
                label = label.replace(StatisticalResourcesConstants.TRANSLATION_TIME_SDMX_SECOND_END_IN_LABEL, timeRangeSdmx.getEndSecond());
            }
            return label;
        } else {
            throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.TIME);
        }
    }
}
