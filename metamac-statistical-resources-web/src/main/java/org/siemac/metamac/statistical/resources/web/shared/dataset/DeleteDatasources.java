package org.siemac.metamac.statistical.resources.web.shared.dataset;

import java.util.List;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class DeleteDatasources {

    @In(1)
    List<String> urns;

    @In(2)
    boolean      deleteAttributes;

    @Out(1)
    int          observationsDeleted;
}
