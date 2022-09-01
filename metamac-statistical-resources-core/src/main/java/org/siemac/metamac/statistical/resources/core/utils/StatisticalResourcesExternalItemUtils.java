package org.siemac.metamac.statistical.resources.core.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Code;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Concept;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Item;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;

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

    public static ExternalItem buildExternalItemFromItem(Item item, TypeExternalArtefactsEnum type) {
        ExternalItem externalItem = new ExternalItem();
        externalItem.setCode(item.getId());
        externalItem.setCodeNested(item.getNestedId());
        externalItem.setUrn(item.getUrn());
        externalItem.setUrnProvider(item.getUrnProvider());
        externalItem.setType(type);       
        externalItem.setUri(item.getSelfLink().getHref());
        externalItem.setManagementAppUrl(item.getManagementAppLink());
        //title not necessary
        return externalItem;
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
}
