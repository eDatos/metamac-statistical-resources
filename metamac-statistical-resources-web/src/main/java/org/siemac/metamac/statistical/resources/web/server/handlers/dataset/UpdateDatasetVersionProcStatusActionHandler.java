package org.siemac.metamac.statistical.resources.web.server.handlers.dataset;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.web.client.enums.LifeCycleActionEnum;
import org.siemac.metamac.statistical.resources.web.server.dtos.ResourceNotificationDto;
import org.siemac.metamac.statistical.resources.web.server.handlers.UpdateResourceProcStatusBaseActionHandler;
import org.siemac.metamac.statistical.resources.web.server.rest.NoticesRestInternalFacade;
import org.siemac.metamac.statistical.resources.web.shared.dataset.UpdateDatasetVersionProcStatusAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.UpdateDatasetVersionProcStatusResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateDatasetVersionProcStatusActionHandler extends UpdateResourceProcStatusBaseActionHandler<UpdateDatasetVersionProcStatusAction, UpdateDatasetVersionProcStatusResult> {

    @Autowired
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;

    @Autowired
    private NoticesRestInternalFacade         noticesRestInternalFacade;

    public UpdateDatasetVersionProcStatusActionHandler() {
        super(UpdateDatasetVersionProcStatusAction.class);
    }

    @Override
    public UpdateDatasetVersionProcStatusResult executeSecurityAction(UpdateDatasetVersionProcStatusAction action) throws ActionException {

        LifeCycleActionEnum lifeCycleAction = action.getLifeCycleAction();

        try {

            DatasetVersionDto datasetVersionDto = null;

            switch (lifeCycleAction) {
                case SEND_TO_PRODUCTION_VALIDATION:
                    datasetVersionDto = statisticalResourcesServiceFacade.sendDatasetVersionToProductionValidation(ServiceContextHolder.getCurrentServiceContext(),
                            action.getDatasetVersionToUpdateProcStatus());
                    break;

                case SEND_TO_DIFFUSION_VALIDATION:
                    datasetVersionDto = statisticalResourcesServiceFacade.sendDatasetVersionToDiffusionValidation(ServiceContextHolder.getCurrentServiceContext(),
                            action.getDatasetVersionToUpdateProcStatus());
                    break;

                case REJECT_VALIDATION:
                    datasetVersionDto = statisticalResourcesServiceFacade.sendDatasetVersionToValidationRejected(ServiceContextHolder.getCurrentServiceContext(),
                            action.getDatasetVersionToUpdateProcStatus());
                    break;

                case PUBLISH:
                    datasetVersionDto = statisticalResourcesServiceFacade.publishDatasetVersion(ServiceContextHolder.getCurrentServiceContext(), action.getDatasetVersionToUpdateProcStatus());
                    break;

                case VERSION:
                    datasetVersionDto = statisticalResourcesServiceFacade.versioningDatasetVersion(ServiceContextHolder.getCurrentServiceContext(), action.getDatasetVersionToUpdateProcStatus(),
                            action.getVersionType());
                    break;
            }

            try {
                ResourceNotificationDto notificationDto = new ResourceNotificationDto.Builder(action.getDatasetVersionToUpdateProcStatus(), lifeCycleAction)
                        .updatedResource(datasetVersionDto).reasonOfRejection(action.getReasonOfRejection()).build();
                noticesRestInternalFacade.createLifeCycleNotification(ServiceContextHolder.getCurrentServiceContext(), notificationDto);
            } catch (MetamacWebException e) {
                return new UpdateDatasetVersionProcStatusResult.Builder(datasetVersionDto).notificationException(e).build();
            }

            return new UpdateDatasetVersionProcStatusResult(datasetVersionDto);

        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
