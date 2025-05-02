package org.siemac.metamac.statistical.resources.core.utils;

import java.util.List;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.statistical.resources.core.common.domain.InternationalString;
import org.siemac.metamac.statistical.resources.core.common.domain.LocalisedString;

public class InternationalStringUtils {

    public static InternationalString copy(InternationalString source) {
        if (source == null) {
            return null;
        }
        InternationalString target = new InternationalString();

        for (LocalisedString sourceLocalisedString : source.getTexts()) {
            LocalisedString targetLocalisedString = copy(sourceLocalisedString);
            if (targetLocalisedString != null) {
                target.addText(targetLocalisedString);
            }
        }

        return target;
    }

    public static LocalisedString copy(LocalisedString source) {
        if (source == null) {
            return null;
        }
        LocalisedString target = new LocalisedString();
        target.setLabel(source.getLabel());
        target.setLocale(source.getLocale());
        target.setIsUnmodifiable(source.getIsUnmodifiable());

        return target;
    }

    public static InternationalString getCommonInternationalStringFromRestInternationalString(org.siemac.metamac.rest.common.v1_0.domain.InternationalString restInternationalString) {
        if (restInternationalString != null) {
            InternationalString commonInternationalString = new InternationalString();
            List<org.siemac.metamac.rest.common.v1_0.domain.LocalisedString> restLocalisedStrings = restInternationalString.getTexts();
            for (org.siemac.metamac.rest.common.v1_0.domain.LocalisedString restLocalisedString : restLocalisedStrings) {
                LocalisedString commonLocalisedString = new LocalisedString();
                commonLocalisedString.setLocale(restLocalisedString.getLang());
                commonLocalisedString.setLabel(restLocalisedString.getValue());
                commonInternationalString.addText(commonLocalisedString);
            }
            return commonInternationalString;
        }
        return null;
    }

    public static InternationalString getCommonInternationalStringFromDatasetRepositoryInternationalStringDto(
            es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto internationalStringDto) {
        if (internationalStringDto != null) {
            InternationalString commonInternationalString = new InternationalString();
            for (es.gobcan.istac.edatos.dataset.repository.dto.LocalisedStringDto repositoryLocalisedStringDto : internationalStringDto.getTexts()) {
                LocalisedString commonLocalisedString = new LocalisedString();
                commonLocalisedString.setLocale(repositoryLocalisedStringDto.getLocale());
                commonLocalisedString.setLabel(repositoryLocalisedStringDto.getLabel());
                commonInternationalString.addText(commonLocalisedString);
            }
            return commonInternationalString;
        }
        return null;
    }

    public static InternationalStringDto getCommonInternationalStringDtoFromDatasetRepositoryInternationalStringDto(
            es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto internationalStringDto) {
        if (internationalStringDto == null || internationalStringDto.getTexts().isEmpty()) {
            return null;
        }
        InternationalStringDto datasetRepositoryInternationalStringDto = new InternationalStringDto();
        for (es.gobcan.istac.edatos.dataset.repository.dto.LocalisedStringDto locale : internationalStringDto.getTexts()) {
            LocalisedStringDto localised = new LocalisedStringDto();
            localised.setLabel(locale.getLabel());
            localised.setLocale(locale.getLocale());
            datasetRepositoryInternationalStringDto.addText(localised);
        }
        return datasetRepositoryInternationalStringDto;
    }

    public static es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto buildDatasetRepositoryInternationalStringDtoFromCommonInternationalStringDto(
            InternationalStringDto internationalStringDto) {
        if (!internationalStringDto.hasTexts()) {
            return null;
        }
        es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto datasetRepositoryInternationalStringDto = new es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto();
        for (LocalisedStringDto locale : internationalStringDto.getTexts()) {
            es.gobcan.istac.edatos.dataset.repository.dto.LocalisedStringDto localised = new es.gobcan.istac.edatos.dataset.repository.dto.LocalisedStringDto();
            localised.setLabel(locale.getLabel());
            localised.setLocale(locale.getLocale());
            datasetRepositoryInternationalStringDto.addText(localised);
        }
        return datasetRepositoryInternationalStringDto;
    }

    public static LocalisedStringDto createCommonLocalisedStringDto(String locale, String label, Boolean isUnmodifiable) {
        LocalisedStringDto localisedStringDto = new LocalisedStringDto();
        localisedStringDto.setLocale(locale);
        localisedStringDto.setLabel(label);
        localisedStringDto.setIsUnmodifiable(isUnmodifiable);
        return localisedStringDto;

    }

    public static es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto copy(es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto source, boolean scapeData) {
        if (source == null) {
            return null;
        }
        es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto target = new es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto();

        for (es.gobcan.istac.edatos.dataset.repository.dto.LocalisedStringDto sourceLocalisedString : source.getTexts()) {
            es.gobcan.istac.edatos.dataset.repository.dto.LocalisedStringDto targetLocalisedString = copy(sourceLocalisedString, scapeData);
            if (targetLocalisedString != null) {
                target.addText(targetLocalisedString);
            }
        }

        return target;
    }

    public static es.gobcan.istac.edatos.dataset.repository.dto.LocalisedStringDto copy(es.gobcan.istac.edatos.dataset.repository.dto.LocalisedStringDto source, boolean scapeData) {
        if (source == null) {
            return null;
        }
        es.gobcan.istac.edatos.dataset.repository.dto.LocalisedStringDto target = new es.gobcan.istac.edatos.dataset.repository.dto.LocalisedStringDto();
        target.setLabel(scapeData ? AttributesUtils.escapeValueToData(source.getLabel()) : source.getLabel());
        target.setLocale(source.getLocale());
        return target;
    }

}
