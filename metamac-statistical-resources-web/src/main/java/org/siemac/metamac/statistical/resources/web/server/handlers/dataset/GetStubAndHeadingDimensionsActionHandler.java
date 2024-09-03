package org.siemac.metamac.statistical.resources.web.server.handlers.dataset;

import java.util.List;

import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure;
import org.siemac.metamac.statistical.resources.core.dto.RelatedResourceDto;
import org.siemac.metamac.statistical.resources.web.server.rest.SrmRestInternalFacade;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetStubAndHeadingDimensionsAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.GetStubAndHeadingDimensionsResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetStubAndHeadingDimensionsActionHandler extends SecurityActionHandler<GetStubAndHeadingDimensionsAction, GetStubAndHeadingDimensionsResult> {

    @Autowired
    private SrmRestInternalFacade srmRestInternalFacade;

    public GetStubAndHeadingDimensionsActionHandler() {
        super(GetStubAndHeadingDimensionsAction.class);
    }

    @Override
    public GetStubAndHeadingDimensionsResult executeSecurityAction(GetStubAndHeadingDimensionsAction action) throws ActionException {
        DataStructure dataStructure = srmRestInternalFacade.retrieveDsd(action.getDsdUrn());
        List<RelatedResourceDto> stubDimensions = srmRestInternalFacade.retrieveDsdStubDimensions(dataStructure);
        List<RelatedResourceDto> headingDimensions = srmRestInternalFacade.retrieveDsdHeadingDimensions(dataStructure);
        return new GetStubAndHeadingDimensionsResult(stubDimensions, headingDimensions);
    }
}
