package org.siemac.metamac.statistical.resources.web.shared.dataset;

import java.util.List;

import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetStubAndHeadingDimensions {
    @In(1)
    String                dsdUrn;

    @Out(1)
    List<RelatedResourceDto> stubDimensions;

    @Out(2)
    List<RelatedResourceDto> headingDimensions;
}
