package org.siemac.metamac.statistical.resources.core.utils;

import static org.siemac.edatos.core.common.util.shared.UrnUtils.splitUrnByDots;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Code;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Concept;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Item;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.common.mapper.CommonDto2DoMapper;

public class StatisticalResourcesExternalItemUtils {

    public static ExternalItemDto buildExternalItemDtoFromCode(Code code) {
        return buildExternalItemDtoFromItem(code, TypeExternalArtefactsEnum.CODE);
    }

    public static ExternalItemDto buildExternalItemDtoFromConcept(Concept concept) {
        return buildExternalItemDtoFromItem(concept, TypeExternalArtefactsEnum.CONCEPT);
    }

    public static Set<ExternalItem> extractCodelistsUsedFromExternalItemCodes(Set<ExternalItem> externalItemList) {

        Map<String, ExternalItem> externalItemsMap = new HashMap<String, ExternalItem>();

        for (ExternalItem externalItem : externalItemList) {
            if (TypeExternalArtefactsEnum.CODE.equals(externalItem.getType())) {
                String[] splitUrnItem = UrnUtils.splitUrnItem(externalItem.getUrn());
                String agencyID = splitUrnItem[0];
                String[] agenciesID = agencyID.contains(".") ? agencyID.split(".") : new String[]{agencyID};
                String itemSchemeID = splitUrnItem[1];
                String version = splitUrnItem[2];
                String codelistUrn = GeneratorUrnUtils.generateSdmxCodelistUrn(agenciesID, itemSchemeID, version);

                if (!externalItemsMap.containsKey(codelistUrn)) {
                    ExternalItem aux = new ExternalItem();
                    aux.setCode(itemSchemeID);
                    aux.setUrn(codelistUrn);
                    aux.setType(TypeExternalArtefactsEnum.CODELIST);

                    externalItemsMap.put(codelistUrn, aux);
                }
            }
        }

        return new HashSet<ExternalItem>(externalItemsMap.values());
    }

    private static ExternalItemDto buildExternalItemDtoFromItem(Item item, TypeExternalArtefactsEnum type) {
        ExternalItemDto externalItemDto = new ExternalItemDto();
        externalItemDto.setCode(item.getId());
        externalItemDto.setUri(item.getSelfLink().getHref());
        externalItemDto.setUrn(item.getUrn());
        externalItemDto.setUrnProvider(item.getUrnProvider());
        externalItemDto.setType(type);
        externalItemDto.setTitle(getInternationalStringDtoFromInternationalString(item.getName()));
        externalItemDto.setManagementAppUrl(item.getManagementAppLink());
        return externalItemDto;
    }

    public static ExternalItem buildExternalItemFromItem(Item item, TypeExternalArtefactsEnum type, CommonDto2DoMapper dto2DoMapper) throws MetamacException {
        ExternalItem externalItem = new ExternalItem();
        externalItem.setCode(item.getId());
        externalItem.setCodeNested(item.getNestedId());
        externalItem.setUrn(item.getUrn());
        externalItem.setUrnProvider(item.getUrnProvider());
        externalItem.setType(type);
        externalItem.setUri(dto2DoMapper.externalItemApiUrlDtoToDo(type, item.getSelfLink().getHref()));
        externalItem.setManagementAppUrl(dto2DoMapper.externalItemWebAppUrlDtoToDo(type, item.getManagementAppLink()));
        externalItem.setTitle(getInternationalStringFromRestInternationalString(item.getName()));

        return externalItem;
    }

    private static org.siemac.metamac.statistical.resources.core.common.domain.InternationalString getInternationalStringFromRestInternationalString(InternationalString restInternationalString) {
        if (restInternationalString != null) {
            org.siemac.metamac.statistical.resources.core.common.domain.InternationalString internationalString = new org.siemac.metamac.statistical.resources.core.common.domain.InternationalString();
            List<LocalisedString> restLocalisedString = restInternationalString.getTexts();
            for (LocalisedString localisedString : restLocalisedString) {
                org.siemac.metamac.statistical.resources.core.common.domain.LocalisedString newLocalisedString = new org.siemac.metamac.statistical.resources.core.common.domain.LocalisedString();
                newLocalisedString.setLocale(localisedString.getLang());
                newLocalisedString.setLabel(localisedString.getValue());
                internationalString.addText(newLocalisedString);
            }
            return internationalString;
        }
        return null;
    }

    private static InternationalStringDto getInternationalStringDtoFromInternationalString(InternationalString internationalString) {
        if (internationalString != null) {
            InternationalStringDto internationalStringDto = new InternationalStringDto();
            List<LocalisedString> localisedStringList = internationalString.getTexts();
            for (LocalisedString localisedString : localisedStringList) {
                LocalisedStringDto localisedStringDto = new LocalisedStringDto();
                localisedStringDto.setLocale(localisedString.getLang());
                localisedStringDto.setLabel(localisedString.getValue());
                internationalStringDto.addText(localisedStringDto);
            }
            return internationalStringDto;
        }
        return null;
    }

    public static List<ExternalItemDto> buildExternalItemDtoFromCodes(Codes codes) {
        List<ExternalItemDto> externalItemDtos = new ArrayList<>();
        for (ResourceInternal resource : codes.getCodes()) {
            externalItemDtos.add(buildExternalItemDtoFromResource(resource, TypeExternalArtefactsEnum.CODE));
        }
        return externalItemDtos;
    }

    private static ExternalItemDto buildExternalItemDtoFromResource(ResourceInternal resource, TypeExternalArtefactsEnum type) {
        ExternalItemDto externalItemDto = new ExternalItemDto();
        externalItemDto.setCode(resource.getId());
        externalItemDto.setCodeNested(resource.getNestedId());
        externalItemDto.setUri(resource.getSelfLink().getHref());
        externalItemDto.setUrn(resource.getUrn());
        externalItemDto.setUrnProvider(resource.getUrnProvider());
        externalItemDto.setType(type);
        externalItemDto.setTitle(getInternationalStringDtoFromInternationalString(resource.getName()));
        externalItemDto.setManagementAppUrl(resource.getManagementAppLink());
        return externalItemDto;
    }

    public static String getCodelistFromCodeUrn(String urn) {
        if (urn == null) {
            return null;
        }

        // e.g.: urn:sdmx:org.sdmx.infomodel.codelist.Code=ISTAC:CL_AREA_ES70_DS_20111120(01.000).ES70 is converted to
        // [urn:sdmx:org, sdmx, infomodel, codelist, Code=ISTAC:CL_AREA_ES70_DS_20111120(01.000), ES70]
        // The use of LinkedList is because Arrays.asList returns a fixed-size list
        List<String> splittedByDotUrn = new LinkedList<>(Arrays.asList(splitUrnByDots(urn)));

        int lastElementIndex = splittedByDotUrn.size() - 1;
        if (lastElementIndex >= 0) {
            // remove the element corresponding to the code id
            splittedByDotUrn.remove(lastElementIndex);
        }

        return String.join(".", splittedByDotUrn);
    }
}
