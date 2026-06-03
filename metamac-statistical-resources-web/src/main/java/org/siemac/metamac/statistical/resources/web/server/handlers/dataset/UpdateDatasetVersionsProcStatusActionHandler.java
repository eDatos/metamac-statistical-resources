package org.siemac.metamac.statistical.resources.web.server.handlers.dataset;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.web.client.enums.LifeCycleActionEnum;
import org.siemac.metamac.statistical.resources.web.server.dtos.ResourceNotificationBaseDto;
import org.siemac.metamac.statistical.resources.web.server.handlers.UpdateResourceProcStatusBaseActionHandler;
import org.siemac.metamac.statistical.resources.web.server.rest.NoticesRestInternalFacade;
import org.siemac.metamac.statistical.resources.web.shared.dataset.UpdateDatasetVersionsProcStatusAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.UpdateDatasetVersionsProcStatusResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateDatasetVersionsProcStatusActionHandler extends UpdateResourceProcStatusBaseActionHandler<UpdateDatasetVersionsProcStatusAction, UpdateDatasetVersionsProcStatusResult> {

    @Autowired
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;

    @Autowired
    private NoticesRestInternalFacade         noticesRestInternalFacade;

    public UpdateDatasetVersionsProcStatusActionHandler() {
        super(UpdateDatasetVersionsProcStatusAction.class);
    }

    @Override
    public UpdateDatasetVersionsProcStatusResult executeSecurityAction(UpdateDatasetVersionsProcStatusAction action) throws ActionException {

        List<DatasetVersionBaseDto> datasetVersionsToUpdateProcStatus = action.getDatasetVersionsToUpdateProcStatus();
        LifeCycleActionEnum lifeCycleAction = action.getLifeCycleAction();

        MetamacException metamacException = new MetamacException();
        List<ResourceNotificationBaseDto> notificationsToSend = new ArrayList<ResourceNotificationBaseDto>();

        for (DatasetVersionBaseDto datasetVersionToUpdate : datasetVersionsToUpdateProcStatus) {
            try {

                DatasetVersionBaseDto updatedDatasetVersionBaseDto = null;

                switch (lifeCycleAction) {
                    case SEND_TO_PRODUCTION_VALIDATION:
                        updatedDatasetVersionBaseDto = statisticalResourcesServiceFacade.sendDatasetVersionToProductionValidation(ServiceContextHolder.getCurrentServiceContext(),
                                datasetVersionToUpdate);
                        break;

                    case SEND_TO_DIFFUSION_VALIDATION:
                        updatedDatasetVersionBaseDto = statisticalResourcesServiceFacade.sendDatasetVersionToDiffusionValidation(ServiceContextHolder.getCurrentServiceContext(),
                                datasetVersionToUpdate);
                        break;

                    case REJECT_VALIDATION:
                        updatedDatasetVersionBaseDto = statisticalResourcesServiceFacade
                                .sendDatasetVersionToValidationRejected(ServiceContextHolder.getCurrentServiceContext(), datasetVersionToUpdate);
                        break;

                    case PUBLISH:
                        updatedDatasetVersionBaseDto = statisticalResourcesServiceFacade.publishDatasetVersion(ServiceContextHolder.getCurrentServiceContext(), datasetVersionToUpdate);
                        break;

                    case VERSION:
                        updatedDatasetVersionBaseDto = statisticalResourcesServiceFacade.versioningDatasetVersion(ServiceContextHolder.getCurrentServiceContext(), datasetVersionToUpdate,
                                action.getVersionType());
                        break;
                    default:
                        break;
                }

                ResourceNotificationBaseDto notification = new ResourceNotificationBaseDto.Builder(datasetVersionToUpdate, lifeCycleAction)
                        .updatedResource(updatedDatasetVersionBaseDto).reasonOfRejection(action.getReasonOfRejection()).build();
                notificationsToSend.add(notification);

            } catch (MetamacException e) {
                addExceptionsItemToMetamacException(lifeCycleAction, datasetVersionToUpdate, metamacException, e);
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
            return new UpdateDatasetVersionsProcStatusResult.Builder().notificationException(notificationException).build();
        } else {
            MetamacWebException metamacWebException = WebExceptionUtils.createMetamacWebException(metamacException);
            if (notificationException != null) {
                metamacWebException.getWebExceptionItems().addAll(notificationException.getWebExceptionItems());
            }
            throw metamacWebException;
        }
    }
}
