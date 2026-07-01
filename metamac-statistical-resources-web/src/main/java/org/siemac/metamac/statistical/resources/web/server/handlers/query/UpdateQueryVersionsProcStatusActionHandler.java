package org.siemac.metamac.statistical.resources.web.server.handlers.query;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.web.client.enums.LifeCycleActionEnum;
import org.siemac.metamac.statistical.resources.web.server.dtos.ResourceNotificationBaseDto;
import org.siemac.metamac.statistical.resources.web.server.handlers.UpdateResourceProcStatusBaseActionHandler;
import org.siemac.metamac.statistical.resources.web.server.rest.NoticesRestInternalFacade;
import org.siemac.metamac.statistical.resources.web.shared.query.UpdateQueryVersionsProcStatusAction;
import org.siemac.metamac.statistical.resources.web.shared.query.UpdateQueryVersionsProcStatusResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateQueryVersionsProcStatusActionHandler extends UpdateResourceProcStatusBaseActionHandler<UpdateQueryVersionsProcStatusAction, UpdateQueryVersionsProcStatusResult> {

    @Autowired
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;

    @Autowired
    private NoticesRestInternalFacade         noticesRestInternalFacade;

    public UpdateQueryVersionsProcStatusActionHandler() {
        super(UpdateQueryVersionsProcStatusAction.class);
    }

    @Override
    public UpdateQueryVersionsProcStatusResult executeSecurityAction(UpdateQueryVersionsProcStatusAction action) throws ActionException {

        List<QueryVersionBaseDto> queryVersionsToUpdateProcStatus = action.getQueryVersionsToUpdateProcStatus();
        LifeCycleActionEnum lifeCycleAction = action.getLifeCycleAction();

        MetamacException metamacException = new MetamacException();
        List<ResourceNotificationBaseDto> notificationsToSend = new ArrayList<ResourceNotificationBaseDto>();

        for (QueryVersionBaseDto queryVersionToUpdate : queryVersionsToUpdateProcStatus) {
            try {

                QueryVersionBaseDto updatedQueryVersionBaseDto = null;

                switch (lifeCycleAction) {
                    case SEND_TO_PRODUCTION_VALIDATION:
                        updatedQueryVersionBaseDto = statisticalResourcesServiceFacade.sendQueryVersionToProductionValidation(ServiceContextHolder.getCurrentServiceContext(), queryVersionToUpdate);
                        break;

                    case SEND_TO_DIFFUSION_VALIDATION:
                        updatedQueryVersionBaseDto = statisticalResourcesServiceFacade.sendQueryVersionToDiffusionValidation(ServiceContextHolder.getCurrentServiceContext(), queryVersionToUpdate);
                        break;

                    case REJECT_VALIDATION:
                        updatedQueryVersionBaseDto = statisticalResourcesServiceFacade.sendQueryVersionToValidationRejected(ServiceContextHolder.getCurrentServiceContext(), queryVersionToUpdate);
                        break;

                    case PUBLISH:
                        updatedQueryVersionBaseDto = statisticalResourcesServiceFacade.publishQueryVersion(ServiceContextHolder.getCurrentServiceContext(), queryVersionToUpdate);
                        break;

                    case VERSION:
                        updatedQueryVersionBaseDto = statisticalResourcesServiceFacade.versioningQueryVersion(ServiceContextHolder.getCurrentServiceContext(), queryVersionToUpdate,
                                action.getVersionType());
                        break;
                    default:
                        break;
                }

                ResourceNotificationBaseDto notification = new ResourceNotificationBaseDto.Builder(queryVersionToUpdate, lifeCycleAction)
                        .updatedResource(updatedQueryVersionBaseDto).reasonOfRejection(action.getReasonOfRejection()).build();
                notificationsToSend.add(notification);

            } catch (MetamacException e) {
                addExceptionsItemToMetamacException(lifeCycleAction, queryVersionToUpdate, metamacException, e);
            }
        }

        // SEND NOTIFICATIONS

        MetamacWebException notificationException = null;
        try {
            noticesRestInternalFacade.createLifeCycleNotifications(ServiceContextHolder.getCurrentServiceContext(), notificationsToSend);
        } catch (MetamacWebException e) {
            notificationException = e;
        }

        // MANAGE EXCEPTION

        if (metamacException.getExceptionItems() == null || metamacException.getExceptionItems().isEmpty()) {
            return new UpdateQueryVersionsProcStatusResult.Builder().notificationException(notificationException).build();
        } else {
            MetamacWebException metamacWebException = WebExceptionUtils.createMetamacWebException(metamacException);
            if (notificationException != null) {
                metamacWebException.getWebExceptionItems().addAll(notificationException.getWebExceptionItems());
            }
            throw metamacWebException;
        }
    }
}
