package org.siemac.metamac.statistical.resources.web.shared.base;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.statistical.resources.core.dto.query.PurposeDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.StatisticOfficialityDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetInitialValues {

    @Out(1)
    List<StatisticOfficialityDto> statisticOfficialities;

    @Out(2)
    ExternalItemDto               agency;

    @Out(3)
    ExternalItemDto               defaultLanguage;

    @Out(4)
    List<PurposeDto>              purposesDto;
}
