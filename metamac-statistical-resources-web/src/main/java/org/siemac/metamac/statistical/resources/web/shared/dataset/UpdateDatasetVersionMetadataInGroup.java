package org.siemac.metamac.statistical.resources.web.shared.dataset;

import java.util.List;

import org.siemac.metamac.statistical.resources.core.dto.datasets.CategorisationDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Optional;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class UpdateDatasetVersionMetadataInGroup {

    @In(1)
    List<String>            datasetsUrnsoUpdate;

    @In(2)
    DatasetVersionDto       datasetVersion;

    @In(3)
    List<CategorisationDto> categorisations;

    @Optional
    @Out(1)
    MetamacWebException     notificationException;
}
