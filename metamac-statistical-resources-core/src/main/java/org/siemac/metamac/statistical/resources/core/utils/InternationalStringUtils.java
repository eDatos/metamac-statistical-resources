package org.siemac.metamac.statistical.resources.core.utils;

import java.util.List;

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

}
