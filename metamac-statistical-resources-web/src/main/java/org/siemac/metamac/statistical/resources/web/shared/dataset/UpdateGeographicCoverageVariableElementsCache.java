package org.siemac.metamac.statistical.resources.web.shared.dataset;

import java.util.List;

import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Optional;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class UpdateGeographicCoverageVariableElementsCache {

    @In(0)
    @Out(0)
    @Optional
    DatasetVersionDto                 datasetVersionDto;

    @In(1)
    List<StatisticalResourceTypeEnum> resourcesToUpdate;

    @In(2)
    List<StatisticalResourceTypeEnum> externalResourcesToUpdate;

    @Out(1)
    @Optional
    MetamacWebException               notificationException;
}
