package org.siemac.metamac.statistical.resources.web.shared.dataset;

import java.util.List;

import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdGranularityAttributeInstanceDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetGranularityAttributeInstances {

    @In(1)
    String                                   datasetVersionUrn;

    @In(2)
    String                                   attributeId;

    @Out(1)
    List<DsdGranularityAttributeInstanceDto> instances;
}
