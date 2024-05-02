package org.siemac.metamac.statistical.resources.web.shared.dataset;

import java.util.List;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class DeleteDatasourcesNotUsed {

    @In(1)
    String       datasetUrn;

    @In(2)
    boolean      deleteAttributes;

    @Out(1)
    List<String> datasourcesDeleted;
}
