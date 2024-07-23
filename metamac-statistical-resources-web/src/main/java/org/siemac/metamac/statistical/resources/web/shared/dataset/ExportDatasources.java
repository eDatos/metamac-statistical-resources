package org.siemac.metamac.statistical.resources.web.shared.dataset;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class ExportDatasources {

    @In(1)
    String datasetVersionUrn;

    @Out(2)
    String fileName;
}