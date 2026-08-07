package org.siemac.metamac.statistical.resources.web.server.handlers.publication;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.dto.publication.PublicationVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.web.client.enums.LifeCycleActionEnum;
import org.siemac.metamac.statistical.resources.web.server.dtos.ResourceNotificationBaseDto;
import org.siemac.metamac.statistical.resources.web.server.handlers.UpdateResourceProcStatusBaseActionHandler;
import org.siemac.metamac.statistical.resources.web.server.rest.NoticesRestInternalFacade;
import org.siemac.metamac.statistical.resources.web.shared.publication.UpdatePublicationVersionsProcStatusAction;
import org.siemac.metamac.statistical.resources.web.shared.publication.UpdatePublicationVersionsProcStatusResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdatePublicationVersionsProcStatusActionHandler extends UpdateResourceProcStatusBaseActionHandler<UpdatePublicationVersionsProcStatusAction, UpdatePublicationVersionsProcStatusResult> {

    @Autowired
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;

    @Autowired
    private NoticesRestInternalFacade         noticesRestInternalFacade;

    public UpdatePublicationVersionsProcStatusActionHandler() {
        super(UpdatePublicationVersionsProcStatusAction.class);
    }

    @Override
    public UpdatePublicationVersionsProcStatusResult executeSecurityAction(UpdatePublicationVersionsProcStatusAction action) throws ActionException {

        List<PublicationVersionBaseDto> publicationVersionsToUpdateProcStatus = action.getPublicationVersionsToUpdateProcStatus();
        LifeCycleActionEnum lifeCycleAction = action.getLifeCycleAction();

        MetamacException metamacException = new MetamacException();
        List<ResourceNotificationBaseDto> notificationsToSend = new ArrayList<ResourceNotificationBaseDto>();

        for (PublicationVersionBaseDto publicationVersionDto : publicationVersionsToUpdateProcStatus) {
            try {

                PublicationVersionBaseDto updatedPublicationVersionBaseDto = null;

                switch (lifeCycleAction) {
                    case SEND_TO_PRODUCTION_VALIDATION:
                        updatedPublicationVersionBaseDto = statisticalResourcesServiceFacade.sendPublicationVersionToProductionValidation(ServiceContextHolder.getCurrentServiceContext(),
                                publicationVersionDto);
                        break;

                    case SEND_TO_DIFFUSION_VALIDATION:
                        updatedPublicationVersionBaseDto = statisticalResourcesServiceFacade.sendPublicationVersionToDiffusionValidation(ServiceContextHolder.getCurrentServiceContext(),
                                publicationVersionDto);
                        break;

                    case REJECT_VALIDATION:
                        updatedPublicationVersionBaseDto = statisticalResourcesServiceFacade.sendPublicationVersionToValidationRejected(ServiceContextHolder.getCurrentServiceContext(),
                                publicationVersionDto);
                        break;

                    case PUBLISH:
                        updatedPublicationVersionBaseDto = statisticalResourcesServiceFacade.publishPublicationVersion(ServiceContextHolder.getCurrentServiceContext(), publicationVersionDto);
                        break;

                    case VERSION:
                        updatedPublicationVersionBaseDto = statisticalResourcesServiceFacade.versioningPublicationVersion(ServiceContextHolder.getCurrentServiceContext(), publicationVersionDto,
                                action.getVersionType());
                        break;

                    default:
                        break;
                }

                ResourceNotificationBaseDto notification = new ResourceNotificationBaseDto.Builder(publicationVersionDto, lifeCycleAction)
                        .updatedResource(updatedPublicationVersionBaseDto).reasonOfRejection(action.getReasonOfRejection()).build();
                notificationsToSend.add(notification);

            } catch (MetamacException e) {
                addExceptionsItemToMetamacException(lifeCycleAction, publicationVersionDto, metamacException, e);
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
            return new UpdatePublicationVersionsProcStatusResult.Builder().notificationException(notificationException).build();
        } else {
            MetamacWebException metamacWebException = WebExceptionUtils.createMetamacWebException(metamacException);
            if (notificationException != null) {
                metamacWebException.getWebExceptionItems().addAll(notificationException.getWebExceptionItems());
            }
            throw metamacWebException;
        }
    }
}
