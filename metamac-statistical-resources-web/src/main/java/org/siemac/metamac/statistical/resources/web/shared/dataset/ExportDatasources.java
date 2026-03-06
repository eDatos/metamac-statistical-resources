package org.siemac.metamac.statistical.resources.web.shared.dataset;

import org.siemac.metamac.statistical.resources.core.enume.dataset.domain.DecimalSeparatorTypeEnum;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class ExportDatasources {

    @In(1)
    String datasetVersionUrn;

    @In(2)
    DecimalSeparatorTypeEnum decimalSeparator;

    @Out(2)
    String fileName;
}