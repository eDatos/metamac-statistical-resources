package org.siemac.metamac.statistical.resources.web.shared.dataset;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.statistical.resources.core.dto.datasets.ItemDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetItems {

    @In(1)
    String                    itemSchemeUrn;

    @In(2)
    TypeExternalArtefactsEnum itemSchemeType;

    @In(3)
    String                    codeSrmRestriction;

    @Out(1)
    ExternalItemDto           itemScheme;

    @Out(2)
    List<ItemDto>             items;
}
