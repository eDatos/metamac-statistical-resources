package org.siemac.metamac.statistical.resources.core.invocation.utils;

import java.util.List;

import org.apache.avro.generic.GenericRecord;
import org.apache.avro.specific.SpecificData;
import org.siemac.metamac.statistical.resources.core.common.domain.InternationalString;
import org.siemac.metamac.statistical.resources.core.common.domain.LocalisedString;
import org.siemac.metamac.statistical.resources.core.stream.messages.InternationalStringAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.InternationalStringItemAvro;

public class JaxiMapper {

    public static InternationalString getInternationalStringFromInternationalStringAvro(InternationalStringAvro internationalStringAvro) {
        InternationalString result = new InternationalString();
        List<InternationalStringItemAvro> internationalStringItemAvro = internationalStringAvro.getLocalisedStrings();

        for (Object element : internationalStringItemAvro) {
            GenericRecord record = (GenericRecord) element;
            InternationalStringItemAvro item = (InternationalStringItemAvro) SpecificData.get().deepCopy(InternationalStringItemAvro.getClassSchema(), record);
            result.addText(new LocalisedString(item.getLocale(), item.getLabel()));
        }
        return result;
    }

}
