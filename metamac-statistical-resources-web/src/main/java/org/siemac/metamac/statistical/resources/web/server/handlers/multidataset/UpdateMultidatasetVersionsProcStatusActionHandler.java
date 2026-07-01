package org.siemac.metamac.statistical.resources.web.server.handlers.multidataset;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.dto.multidataset.MultidatasetVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.web.client.enums.LifeCycleActionEnum;
import org.siemac.metamac.statistical.resources.web.server.dtos.ResourceNotificationBaseDto;
import org.siemac.metamac.statistical.resources.web.server.handlers.UpdateResourceProcStatusBaseActionHandler;
import org.siemac.metamac.statistical.resources.web.server.rest.NoticesRestInternalFacade;
import org.siemac.metamac.statistical.resources.web.shared.multidataset.UpdateMultidatasetVersionsProcStatusAction;
import org.siemac.metamac.statistical.resources.web.shared.multidataset.UpdateMultidatasetVersionsProcStatusResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateMultidatasetVersionsProcStatusActionHandler extends UpdateResourceProcStatusBaseActionHandler<UpdateMultidatasetVersionsProcStatusAction, UpdateMultidatasetVersionsProcStatusResult> {

    @Autowired
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;

    @Autowired
    private NoticesRestInternalFacade         noticesRestInternalFacade;

    public UpdateMultidatasetVersionsProcStatusActionHandler() {
        super(UpdateMultidatasetVersionsProcStatusAction.class);
    }

    @Override
    public UpdateMultidatasetVersionsProcStatusResult executeSecurityAction(UpdateMultidatasetVersionsProcStatusAction action) throws ActionException {

        List<MultidatasetVersionBaseDto> multidatasetVersionsToUpdateProcStatus = action.getMultidatasetVersionsToUpdateProcStatus();
        LifeCycleActionEnum lifeCycleAction = action.getLifeCycleAction();

        MetamacException metamacException = new MetamacException();
        List<ResourceNotificationBaseDto> notificationsToSend = new ArrayList<ResourceNotificationBaseDto>();

        for (MultidatasetVersionBaseDto multidatasetVersionDto : multidatasetVersionsToUpdateProcStatus) {
            try {

                MultidatasetVersionBaseDto updatedMultidatasetVersionBaseDto = null;

                switch (lifeCycleAction) {
                    case SEND_TO_PRODUCTION_VALIDATION:
                        updatedMultidatasetVersionBaseDto = statisticalResourcesServiceFacade.sendMultidatasetVersionToProductionValidation(ServiceContextHolder.getCurrentServiceContext(),
                                multidatasetVersionDto);
                        break;

                    case SEND_TO_DIFFUSION_VALIDATION:
                        updatedMultidatasetVersionBaseDto = statisticalResourcesServiceFacade.sendMultidatasetVersionToDiffusionValidation(ServiceContextHolder.getCurrentServiceContext(),
                                multidatasetVersionDto);
                        break;

                    case REJECT_VALIDATION:
                        updatedMultidatasetVersionBaseDto = statisticalResourcesServiceFacade.sendMultidatasetVersionToValidationRejected(ServiceContextHolder.getCurrentServiceContext(),
                                multidatasetVersionDto);
                        break;

                    case PUBLISH:
                        updatedMultidatasetVersionBaseDto = statisticalResourcesServiceFacade.publishMultidatasetVersion(ServiceContextHolder.getCurrentServiceContext(), multidatasetVersionDto);
                        break;

                    case VERSION:
                        updatedMultidatasetVersionBaseDto = statisticalResourcesServiceFacade.versioningMultidatasetVersion(ServiceContextHolder.getCurrentServiceContext(), multidatasetVersionDto,
                                action.getVersionType());
                        break;

                    default:
                        break;
                }

                ResourceNotificationBaseDto notification = new ResourceNotificationBaseDto.Builder(multidatasetVersionDto, lifeCycleAction)
                        .updatedResource(updatedMultidatasetVersionBaseDto).reasonOfRejection(action.getReasonOfRejection()).build();
                notificationsToSend.add(notification);

            } catch (MetamacException e) {
                addExceptionsItemToMetamacException(lifeCycleAction, multidatasetVersionDto, metamacException, e);
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
            return new UpdateMultidatasetVersionsProcStatusResult.Builder().notificationException(notificationException).build();
        } else {
            MetamacWebException metamacWebException = WebExceptionUtils.createMetamacWebException(metamacException);
            if (notificationException != null) {
                metamacWebException.getWebExceptionItems().addAll(notificationException.getWebExceptionItems());
            }
            throw metamacWebException;
        }
    }
}
