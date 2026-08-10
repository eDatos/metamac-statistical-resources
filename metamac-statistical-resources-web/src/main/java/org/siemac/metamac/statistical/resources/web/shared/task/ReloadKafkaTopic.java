package org.siemac.metamac.statistical.resources.web.shared.task;

import java.util.List;

import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

@GenDispatch(isSecure = false)
public class ReloadKafkaTopic {

    @In(1)
    List<StatisticalResourceTypeEnum> resourceTypes;
}
