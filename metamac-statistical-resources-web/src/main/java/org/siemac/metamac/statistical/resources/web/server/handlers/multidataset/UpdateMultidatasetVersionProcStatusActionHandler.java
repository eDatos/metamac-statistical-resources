package org.siemac.metamac.statistical.resources.web.server.handlers.multidataset;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.dto.multidataset.MultidatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.web.client.enums.LifeCycleActionEnum;
import org.siemac.metamac.statistical.resources.web.server.dtos.ResourceNotificationDto;
import org.siemac.metamac.statistical.resources.web.server.handlers.UpdateResourceProcStatusBaseActionHandler;
import org.siemac.metamac.statistical.resources.web.server.rest.NoticesRestInternalFacade;
import org.siemac.metamac.statistical.resources.web.shared.multidataset.UpdateMultidatasetVersionProcStatusAction;
import org.siemac.metamac.statistical.resources.web.shared.multidataset.UpdateMultidatasetVersionProcStatusResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateMultidatasetVersionProcStatusActionHandler extends UpdateResourceProcStatusBaseActionHandler<UpdateMultidatasetVersionProcStatusAction, UpdateMultidatasetVersionProcStatusResult> {

    @Autowired
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;

    @Autowired
    private NoticesRestInternalFacade         noticesRestInternalFacade;

    public UpdateMultidatasetVersionProcStatusActionHandler() {
        super(UpdateMultidatasetVersionProcStatusAction.class);
    }

    @Override
    public UpdateMultidatasetVersionProcStatusResult executeSecurityAction(UpdateMultidatasetVersionProcStatusAction action) throws ActionException {

        LifeCycleActionEnum lifeCycleAction = action.getLifeCycleAction();

        MultidatasetVersionDto multidatasetVersionDto = null;

        try {

            switch (lifeCycleAction) {
                case SEND_TO_PRODUCTION_VALIDATION:
                    multidatasetVersionDto = statisticalResourcesServiceFacade.sendMultidatasetVersionToProductionValidation(ServiceContextHolder.getCurrentServiceContext(),
                            action.getMultidatasetVersionToUpdateProcStatus());
                    break;

                case SEND_TO_DIFFUSION_VALIDATION:
                    multidatasetVersionDto = statisticalResourcesServiceFacade.sendMultidatasetVersionToDiffusionValidation(ServiceContextHolder.getCurrentServiceContext(),
                            action.getMultidatasetVersionToUpdateProcStatus());
                    break;

                case REJECT_VALIDATION:
                    multidatasetVersionDto = statisticalResourcesServiceFacade.sendMultidatasetVersionToValidationRejected(ServiceContextHolder.getCurrentServiceContext(),
                            action.getMultidatasetVersionToUpdateProcStatus());
                    break;

                case PUBLISH:
                    multidatasetVersionDto = statisticalResourcesServiceFacade.publishMultidatasetVersion(ServiceContextHolder.getCurrentServiceContext(),
                            action.getMultidatasetVersionToUpdateProcStatus());
                    break;

                case VERSION:
                    multidatasetVersionDto = statisticalResourcesServiceFacade.versioningMultidatasetVersion(ServiceContextHolder.getCurrentServiceContext(),
                            action.getMultidatasetVersionToUpdateProcStatus(), action.getVersionType());
                    break;

                default:
                    break;
            }

            try {
                ResourceNotificationDto notificationDto = new ResourceNotificationDto.Builder(action.getMultidatasetVersionToUpdateProcStatus(), lifeCycleAction).updatedResource(multidatasetVersionDto).reasonOfRejection(action.getReasonOfRejection()).build();
                noticesRestInternalFacade.createLifeCycleNotification(ServiceContextHolder.getCurrentServiceContext(), notificationDto);
            } catch (MetamacWebException e) {
                return new UpdateMultidatasetVersionProcStatusResult.Builder(multidatasetVersionDto).notificationException(e).build();
            }

            return new UpdateMultidatasetVersionProcStatusResult(multidatasetVersionDto);

        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
