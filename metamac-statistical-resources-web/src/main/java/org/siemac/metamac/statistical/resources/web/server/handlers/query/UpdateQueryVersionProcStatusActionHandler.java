package org.siemac.metamac.statistical.resources.web.server.handlers.query;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.web.client.enums.LifeCycleActionEnum;
import org.siemac.metamac.statistical.resources.web.server.dtos.ResourceNotificationDto;
import org.siemac.metamac.statistical.resources.web.server.handlers.UpdateResourceProcStatusBaseActionHandler;
import org.siemac.metamac.statistical.resources.web.server.rest.NoticesRestInternalFacade;
import org.siemac.metamac.statistical.resources.web.shared.query.UpdateQueryVersionProcStatusAction;
import org.siemac.metamac.statistical.resources.web.shared.query.UpdateQueryVersionProcStatusResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateQueryVersionProcStatusActionHandler extends UpdateResourceProcStatusBaseActionHandler<UpdateQueryVersionProcStatusAction, UpdateQueryVersionProcStatusResult> {

    @Autowired
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;

    @Autowired
    private NoticesRestInternalFacade         noticesRestInternalFacade;

    public UpdateQueryVersionProcStatusActionHandler() {
        super(UpdateQueryVersionProcStatusAction.class);
    }

    @Override
    public UpdateQueryVersionProcStatusResult executeSecurityAction(UpdateQueryVersionProcStatusAction action) throws ActionException {

        LifeCycleActionEnum lifeCycleAction = action.getLifeCycleAction();

        QueryVersionDto queryVersionDto = null;

        try {

            switch (lifeCycleAction) {
                case SEND_TO_PRODUCTION_VALIDATION:
                    queryVersionDto = statisticalResourcesServiceFacade.sendQueryVersionToProductionValidation(ServiceContextHolder.getCurrentServiceContext(),
                            action.getQueryVersionToUpdateProcStatus());
                    break;

                case SEND_TO_DIFFUSION_VALIDATION:
                    queryVersionDto = statisticalResourcesServiceFacade.sendQueryVersionToDiffusionValidation(ServiceContextHolder.getCurrentServiceContext(),
                            action.getQueryVersionToUpdateProcStatus());
                    break;

                case REJECT_VALIDATION:
                    queryVersionDto = statisticalResourcesServiceFacade.sendQueryVersionToValidationRejected(ServiceContextHolder.getCurrentServiceContext(),
                            action.getQueryVersionToUpdateProcStatus());
                    break;

                case PUBLISH:
                    queryVersionDto = statisticalResourcesServiceFacade.publishQueryVersion(ServiceContextHolder.getCurrentServiceContext(), action.getQueryVersionToUpdateProcStatus());
                    break;


                case VERSION:
                    queryVersionDto = statisticalResourcesServiceFacade.versioningQueryVersion(ServiceContextHolder.getCurrentServiceContext(), action.getQueryVersionToUpdateProcStatus(),
                            action.getVersionType());
                    break;
                default:
                    break;
            }

            try {
                ResourceNotificationDto notificationDto = new ResourceNotificationDto.Builder(action.getQueryVersionToUpdateProcStatus(), lifeCycleAction)
                        .updatedResource(queryVersionDto).reasonOfRejection(action.getReasonOfRejection()).build();
                noticesRestInternalFacade.createLifeCycleNotification(ServiceContextHolder.getCurrentServiceContext(), notificationDto);
            } catch (MetamacWebException e) {
                return new UpdateQueryVersionProcStatusResult.Builder(queryVersionDto).notificationException(e).build();
            }

            return new UpdateQueryVersionProcStatusResult(queryVersionDto);

        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
