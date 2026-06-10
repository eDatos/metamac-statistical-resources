package org.siemac.metamac.statistical.resources.web.shared.dataset;

import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdGranularityAttributeInstanceDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class SaveGranularityAttributeInstance {

    @In(1)
    String                              datasetVersionUrn;

    @In(2)
    DsdGranularityAttributeInstanceDto  dsdGranularityAttributeInstanceDto;

    @Out(1)
    DsdGranularityAttributeInstanceDto  savedDto;
}
