package org.siemac.metamac.statistical.resources.web.shared.dataset;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

@GenDispatch(isSecure = false)
public class DeleteTemporalFile {
    @In(1)
    String temporalFile;
}
