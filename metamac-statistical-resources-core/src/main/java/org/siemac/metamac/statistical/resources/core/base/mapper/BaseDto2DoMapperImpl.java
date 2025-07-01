package org.siemac.metamac.statistical.resources.core.base.mapper;

import static org.siemac.metamac.statistical.resources.core.error.utils.ServiceExceptionParametersUtils.addParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.OptimisticLockingUtils;
import org.siemac.metamac.statistical.resources.core.base.checks.MetadataEditionChecks;
import org.siemac.metamac.statistical.resources.core.base.domain.IdentifiableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.NameableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.StatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionRationaleType;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionRationaleTypeRepository;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.common.domain.InternationalString;
import org.siemac.metamac.statistical.resources.core.common.domain.LocalisedString;
import org.siemac.metamac.statistical.resources.core.common.mapper.CommonDto2DoMapperImpl;
import org.siemac.metamac.statistical.resources.core.common.utils.CommonVersioningCopyUtils;
import org.siemac.metamac.statistical.resources.core.dto.IdentifiableStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.NameableStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.SiemacMetadataStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.StatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.VersionRationaleTypeDto;
import org.siemac.metamac.statistical.resources.core.dto.VersionableStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.VersionRationaleTypeEnum;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionSingleParameters;
import org.siemac.metamac.statistical.resources.core.utils.StatisticalResourcesCollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Component("baseDto2DoMapper")
public class BaseDto2DoMapperImpl extends CommonDto2DoMapperImpl implements BaseDto2DoMapper {

    @Autowired
    private VersionRationaleTypeRepository versionRationaleTypeRepository;

    // ------------------------------------------------------------
    // BASE HIERARCHY
    // ------------------------------------------------------------

    @Override
    public SiemacMetadataStatisticalResource siemacMetadataStatisticalResourceDtoToDo(SiemacMetadataStatisticalResourceDto source, SiemacMetadataStatisticalResource target, String metadataName)
            throws MetamacException {

        InternationalString oldTitle = cloneInternationalString(target.getTitle());
        InternationalString oldDescription = cloneInternationalString(target.getDescription());

        // Hierarchy
        lifeCycleStatisticalResourceDtoToDo(source, target, metadataName);

        // KEYWORDS
        boolean keywordsModifiedByUser = hasInternationalStringBeModified(target.getKeywords(), source.getKeywords());
        if (keywordsModifiedByUser) {
            target.setUserModifiedKeywords(true);
            target.setKeywords(internationalStringDtoToDo(source.getKeywords(), target.getKeywords(), addParameter(metadataName, ServiceExceptionSingleParameters.KEYWORDS)));
        } else {
            if (shouldKeywordsBeRebuilt(oldTitle, oldDescription, target, source)) {
                target.setUserModifiedKeywords(false);
                target.setKeywords(buildKeywords(target));
            }
        }

        if (MetadataEditionChecks.canLanguageBeEdited(source.getId())) {
            target.setLanguage(externalItemDtoToDo(source.getLanguage(), target.getLanguage(), addParameter(metadataName, ServiceExceptionSingleParameters.LANGUAGE)));
        }
        // Always Modifiable
        target.setLastUpdate(new DateTime());

        languagesDtoListToDoListEnsuringLanguageIsContained(source.getLanguages(), target.getLanguages(), target.getLanguage(), addParameter(metadataName, ServiceExceptionSingleParameters.LANGUAGES));

        externalItemDtoCollectionToDoList(source.getStatisticalOperationInstances(), target.getStatisticalOperationInstances(),
                addParameter(metadataName, ServiceExceptionSingleParameters.STATISTICAL_OPERATION_INSTANCES));

        target.setSubtitle(internationalStringDtoToDo(source.getSubtitle(), target.getSubtitle(), addParameter(metadataName, ServiceExceptionSingleParameters.SUBTITLE)));
        target.setTitleAlternative(internationalStringDtoToDo(source.getTitleAlternative(), target.getTitleAlternative(),
                addParameter(metadataName, ServiceExceptionSingleParameters.TITLE_ALTERNATIVE)));
        target.setAbstractLogic(internationalStringDtoToDo(source.getAbstractLogic(), target.getAbstractLogic(), addParameter(metadataName, ServiceExceptionSingleParameters.ABSTRACT_LOGIC)));

        target.setCreator(externalItemDtoToDo(source.getCreator(), target.getCreator(), addParameter(metadataName, ServiceExceptionSingleParameters.CREATOR)));
        externalItemDtoCollectionToDoList(source.getDataProvider(), target.getDataProvider(), addParameter(metadataName, ServiceExceptionSingleParameters.DATA_PROVIDER));
        target.setDataProviderAnnotations(internationalStringDtoToDo(source.getDataProviderAnnotations(), target.getDataProviderAnnotations(), addParameter(metadataName, ServiceExceptionSingleParameters.DATA_PROVIDER_ANNOTATIONS)));
        externalItemDtoCollectionToDoList(source.getContributor(), target.getContributor(), addParameter(metadataName, ServiceExceptionSingleParameters.CONTRIBUTOR));
        target.setConformsTo(internationalStringDtoToDo(source.getConformsTo(), target.getConformsTo(), addParameter(metadataName, ServiceExceptionSingleParameters.CONFORMS_TO)));
        target.setConformsToInternal(internationalStringDtoToDo(source.getConformsToInternal(), target.getConformsToInternal(),
                addParameter(metadataName, ServiceExceptionSingleParameters.CONFORMS_TO_INTERNAL)));

        externalItemDtoCollectionToDoList(source.getPublisher(), target.getPublisher(), addParameter(metadataName, ServiceExceptionSingleParameters.PUBLISHER));
        externalItemDtoCollectionToDoList(source.getPublisherContributor(), target.getPublisherContributor(), addParameter(metadataName, ServiceExceptionSingleParameters.PUBLISHER_CONTRIBUTOR));
        externalItemDtoCollectionToDoList(source.getMediator(), target.getMediator(), addParameter(metadataName, ServiceExceptionSingleParameters.MEDIATOR));
        target.setNewnessUntilDate(dateDtoToDo(source.getNewnessUntilDate()));

        target.setReplaces(relatedResourceDtoToDo(source.getReplaces(), target.getReplaces(), addParameter(metadataName, ServiceExceptionSingleParameters.REPLACES)));
        target.setIsReplacedBy(relatedResourceDtoToDo(source.getIsReplacedBy(), target.getIsReplacedBy(), addParameter(metadataName, ServiceExceptionSingleParameters.IS_REPLACED_BY)));

        target.setCommonMetadata(externalItemDtoToDo(source.getCommonMetadata(), target.getCommonMetadata(), addParameter(metadataName, ServiceExceptionSingleParameters.COMMON_METADATA)));
        target.setAccessRights(internationalStringDtoToDo(source.getAccessRights(), target.getAccessRights(), addParameter(metadataName, ServiceExceptionSingleParameters.ACCESS_RIGHTS)));

        return target;
    }

    private InternationalString cloneInternationalString(InternationalString source) {
        if (source == null) {
            return null;
        }
        InternationalString target = new InternationalString();
        for (LocalisedString localised : source.getTexts()) {
            target.addText(new LocalisedString(localised.getLocale(), localised.getLabel()));
        }
        return target;
    }

    public Collection<ExternalItem> languagesDtoListToDoListEnsuringLanguageIsContained(Collection<ExternalItemDto> sources, Collection<ExternalItem> targets, final ExternalItem language,
            String metadataName) throws MetamacException {
        if (targets == null) {
            targets = new ArrayList<ExternalItem>();
        }

        Collection<ExternalItem> targetsBefore = targets;

        Collection<ExternalItem> newTargets = calculateNewExternalItemsToPersit(sources, metadataName, targetsBefore);

        if (language != null && !StatisticalResourcesCollectionUtils.isExternalItemInCollection(newTargets, language)) {
            newTargets.add(CommonVersioningCopyUtils.copyExternalItem(language));
        }

        // Delete missing
        deleteExternalItemsNotFoundInSource(targetsBefore, newTargets, metadataName);

        targets.clear();
        for (ExternalItem target : newTargets) {
            targets.add(target);
        }

        return targets;
    }

    private boolean shouldKeywordsBeRebuilt(InternationalString oldTitle, InternationalString oldDescription, SiemacMetadataStatisticalResource previous, SiemacMetadataStatisticalResourceDto current) {
        boolean titleChanged = hasInternationalStringBeModified(oldTitle, current.getTitle());
        boolean descriptionChanged = hasInternationalStringBeModified(oldDescription, current.getDescription());
        return BooleanUtils.isNotTrue(previous.getUserModifiedKeywords()) && (titleChanged || descriptionChanged);
    }

    private boolean hasInternationalStringBeModified(InternationalString previous, InternationalStringDto current) {
        if ((previous == null && current != null) || (previous != null && current == null)) {
            return true;
        }
        if (previous == null && current == null) {
            return false;
        }
        if (previous.getTexts().size() != current.getTexts().size()) {
            return true;
        }
        for (String locale : previous.getLocales()) {
            if (hasLocalisedStringBeModified(previous.getLocalisedLabelEntity(locale), current.getLocalised(locale))) {
                return true;
            }
        }
        return false;
    }

    private boolean hasLocalisedStringBeModified(org.siemac.metamac.statistical.resources.core.common.domain.LocalisedString previous, LocalisedStringDto current) {
        if ((previous == null && current != null) || (previous != null && current == null)) {
            return true;
        }
        if (previous == null && current == null) {
            return false;
        }
        return !(StringUtils.equals(previous.getLocale(), current.getLocale()) && StringUtils.equals(previous.getLabel(), current.getLabel()));
    }

    private InternationalString buildKeywords(SiemacMetadataStatisticalResource resource) {
        Set<String> locales = new HashSet<String>();
        if (resource.getTitle() != null) {
            locales.addAll(resource.getTitle().getLocales());
        }
        if (resource.getDescription() != null) {
            locales.addAll(resource.getDescription().getLocales());
        }
        InternationalString keywords = new InternationalString();
        for (String locale : locales) {
            Set<String> words = new HashSet<String>();
            words.addAll(processLocalisedText(resource.getTitle(), locale));
            words.addAll(processLocalisedText(resource.getDescription(), locale));
            words = filterKeywords(words);
            if (words.size() > 0) {
                LocalisedString localisedText = new LocalisedString(locale, StringUtils.join(words, " "));
                keywords.addText(localisedText);
            }
        }
        if (keywords.getTexts().size() > 0) {
            return keywords;
        }
        return null;
    }

    private Collection<String> processLocalisedText(InternationalString intString, String locale) {
        Set<String> words = new HashSet<String>();
        String text = intString != null ? intString.getLocalisedLabel(locale) : null;
        if (text != null) {
            String label = Jsoup.clean(text.trim(), Whitelist.none());
            label = removeHtmlEntitiesLeft(label);
            for (String word : Arrays.asList(label.split("\\s+"))) {
                words.add(word.toLowerCase());
            }
        }
        return words;
    }

    protected String removeHtmlEntitiesLeft(String word) {
        word = StringEscapeUtils.unescapeHtml(word);
        return word;
    }

    private Set<String> filterKeywords(Set<String> words) {
        Set<String> filteredWords = new HashSet<String>();
        for (String word : words) {
            if (word.length() > 3) {
                filteredWords.add(word);
            }
        }
        return filteredWords;
    }

    @Override
    public LifeCycleStatisticalResource lifeCycleStatisticalResourceDtoToDo(LifeCycleStatisticalResourceDto source, LifeCycleStatisticalResource target, String metadataName) throws MetamacException {
        // Hierarchy
        versionableStatisticalResourceDtoToDo(source, target, metadataName);

        // Only modifiable in creation
        if (MetadataEditionChecks.canMaintainerBeEdited(target.getId())) {
            target.setMaintainer(externalItemDtoToDo(source.getMaintainer(), target.getMaintainer(), addParameter(metadataName, ServiceExceptionSingleParameters.MAINTAINER)));
        }

        target.setPublicationStreamStatus(source.getPublicationStreamStatus());
        target.setXStreamStatus(source.getXStreamStatus());

        // Other attributes are automatic, non modifiable

        return target;
    }

    @Override
    public VersionableStatisticalResource versionableStatisticalResourceDtoToDo(VersionableStatisticalResourceDto source, VersionableStatisticalResource target, String metadataName)
            throws MetamacException {
        // Hierarchy
        nameableStatisticalResourceDtoToDo(source, target, metadataName);

        // Non modifiable: versionLogic, versionDate

        // Non modifiable in creation mode

        // Attributes modifiable
        target.setNextVersion(source.getNextVersion());

        if (MetadataEditionChecks.canNextVersionDateBeEdited(target.getNextVersion())) {
            target.setNextVersionDate(source.getNextVersionDate());
        } else {
            target.setNextVersionDate(null);
        }

        target.setVersionRationale(internationalStringDtoToDo(source.getVersionRationale(), target.getVersionRationale(),
                addParameter(metadataName, ServiceExceptionSingleParameters.VERSION_RATIONALE)));

        if (MetadataEditionChecks.canVersionRationaleTypesBeEdited(source.getVersionLogic())) {
            versionRationaleTypeDtoListToDoList(source.getVersionRationaleTypes(), target.getVersionRationaleTypes(),
                    addParameter(metadataName, ServiceExceptionSingleParameters.VERSION_RATIONALE_TYPES));
        }
        return target;
    }

    @Override
    public NameableStatisticalResource nameableStatisticalResourceDtoToDo(NameableStatisticalResourceDto source, NameableStatisticalResource target, String metadataName) throws MetamacException {

        // Hierarchy
        identifiableStatisticalResourceDtoToDo(source, target, metadataName);

        // Non modifiable after creation

        // Attributes modifiable
        target.setTitle(internationalStringDtoToDo(source.getTitle(), target.getTitle(), addParameter(metadataName, ServiceExceptionSingleParameters.TITLE)));
        target.setDescription(internationalStringDtoToDo(source.getDescription(), target.getDescription(), addParameter(metadataName, ServiceExceptionSingleParameters.DESCRIPTION)));

        return target;
    }

    @Override
    public void identifiableStatisticalResourceDtoToDo(IdentifiableStatisticalResourceDto source, IdentifiableStatisticalResource target, String metadataName) throws MetamacException {
        // Hierarchy
        statisticalResourceDtoToDo(source, target, metadataName);

        if (target.getId() == null) {
            target.setCode(source.getCode());
        }
    }

    @Override
    public void statisticalResourceDtoToDo(StatisticalResourceDto source, StatisticalResource target, String metadataName) throws MetamacException {
        if (target.getId() != null) {
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getOptimisticLockingVersion());
        }

        // Only modifiable in creation
        if (MetadataEditionChecks.canStatisticalOperationBeEdited(target.getId())) {
            target.setStatisticalOperation(externalItemDtoToDo(source.getStatisticalOperation(), target.getStatisticalOperation(),
                    addParameter(metadataName, ServiceExceptionSingleParameters.STATISTICAL_OPERATION)));
        }

        // Optimistic locking: Update "update date" attribute to force update to root entity, to increment "version" attribute
        target.setUpdateDate(new DateTime());
    }

    // ------------------------------------------------------------
    // VERSION RATIONALE TYPE
    // ------------------------------------------------------------

    @Override
    public VersionRationaleType versionRationaleTypeDtoToDo(VersionRationaleTypeDto source, VersionRationaleType target, String metadataName) throws MetamacException {
        if (source == null) {
            if (target != null) {
                // delete previous entity
                versionRationaleTypeRepository.delete(target);
            }
            return null;
        }

        if (target == null) {
            target = new VersionRationaleType(source.getValue());
        }
        return target;
    }

    @Override
    public List<VersionRationaleType> versionRationaleTypeDtoListToDoList(List<VersionRationaleTypeDto> sources, List<VersionRationaleType> targets, String metadataName) throws MetamacException {
        List<VersionRationaleType> targetsBefore = targets;
        List<VersionRationaleType> newTargets = new ArrayList<VersionRationaleType>();

        sources = cleanDuplicateVersionRationaleTypes(sources);

        for (VersionRationaleTypeDto source : sources) {
            boolean existsBefore = false;
            for (VersionRationaleType target : targetsBefore) {
                if (source.getValue().equals(target.getValue())) {
                    newTargets.add(versionRationaleTypeDtoToDo(source, target, metadataName));
                    existsBefore = true;
                    break;
                }
            }
            if (!existsBefore) {
                newTargets.add(versionRationaleTypeDtoToDo(source, null, metadataName));
            }
        }

        // Delete missing
        for (VersionRationaleType oldTarget : targetsBefore) {
            boolean found = false;
            for (VersionRationaleType newTarget : newTargets) {
                found = found || (oldTarget.getValue().equals(newTarget.getValue()));
            }
            if (!found) {
                // Delete
                versionRationaleTypeDtoToDo(null, oldTarget, metadataName);
            }
        }

        targets.clear();
        for (VersionRationaleType target : newTargets) {
            targets.add(target);
        }

        return targets;
    }

    private List<VersionRationaleTypeDto> cleanDuplicateVersionRationaleTypes(List<VersionRationaleTypeDto> sources) {
        LinkedHashMap<VersionRationaleTypeEnum, VersionRationaleTypeDto> cleanMap = new LinkedHashMap<VersionRationaleTypeEnum, VersionRationaleTypeDto>();
        for (VersionRationaleTypeDto item : sources) {
            cleanMap.put(item.getValue(), item);
        }
        return new ArrayList<VersionRationaleTypeDto>(cleanMap.values());
    }
}
