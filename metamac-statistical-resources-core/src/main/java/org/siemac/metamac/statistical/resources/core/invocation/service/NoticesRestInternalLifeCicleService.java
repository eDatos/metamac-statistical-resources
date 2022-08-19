package org.siemac.metamac.statistical.resources.core.invocation.service;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;

public interface NoticesRestInternalLifeCicleService {

    public static final String BEAN_ID = "NoticesRestInternalLifeCicleService";

    void createLifeCycleNotification(ServiceContext serviceContext, ProcStatusEnum procStatus, DatasetVersion datasetVersion) throws MetamacException;
}
