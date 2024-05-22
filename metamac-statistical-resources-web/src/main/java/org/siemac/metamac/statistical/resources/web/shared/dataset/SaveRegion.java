package org.siemac.metamac.statistical.resources.web.shared.dataset;

import org.siemac.metamac.statistical.resources.core.dto.constraint.RegionValueDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdDimensionDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class SaveRegion {

    @In(1)
    String          contentConstraintUrn;

    @In(2)
    RegionValueDto  regionToSave;

    @In(3)
    DsdDimensionDto selectedDimension;

    @In(4)
    String          datasetUrn;

    @Out(1)
    RegionValueDto  savedRegion;
}
