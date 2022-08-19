package org.siemac.metamac.statistical.resources.core.invocation.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.core.Response;

import static org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum.DIFFUSION_VALIDATION;
import static org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum.PRODUCTION_VALIDATION;
import static org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum.DRAFT;
import static org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum.VALIDATION_REJECTED;
import static org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum.PUBLISHED;

import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.lang.LocaleUtil;
import org.siemac.metamac.core.common.lang.shared.LocaleConstants;
import org.siemac.metamac.core.common.util.ServiceContextUtils;
import org.siemac.metamac.rest.notices.v1_0.domain.Message;
import org.siemac.metamac.rest.notices.v1_0.domain.ResourceInternal;
import org.siemac.metamac.rest.notices.v1_0.domain.enume.MetamacApplicationsEnum;
import org.siemac.metamac.rest.notices.v1_0.domain.enume.MetamacRolesEnum;
import org.siemac.metamac.rest.notices.v1_0.domain.utils.MessageBuilder;
import org.siemac.metamac.rest.notices.v1_0.domain.utils.NoticeBuilder;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.invocation.service.MetamacApisLocator;
import org.siemac.metamac.statistical.resources.core.invocation.utils.RestMapper;
import org.siemac.metamac.statistical.resources.core.notices.ServiceNoticeAction;
import org.siemac.metamac.statistical.resources.core.notices.ServiceNoticeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(NoticesRestInternalLifeCicleService.BEAN_ID)
public class NoticesRestInternalLifeCicleServiceImpl implements NoticesRestInternalLifeCicleService {

    private static Logger                     logger = LoggerFactory.getLogger(NoticesRestInternalLifeCicleService.class);
    
    @Autowired
    private MetamacApisLocator                                  metamacApisLocator;

    @Autowired
    private RestMapper                        restMapper;
    
    private static Map<ProcStatusEnum, MetamacRolesEnum[]> roles;
    private static Map<ProcStatusEnum, String>             actionCodes;
    private static Map<ProcStatusEnum, String>             messageCodes;

    static {
        roles = new HashMap<ProcStatusEnum, MetamacRolesEnum[]>();
        roles.put(PRODUCTION_VALIDATION, new MetamacRolesEnum[]{MetamacRolesEnum.TECNICO_PRODUCCION});
        roles.put(DIFFUSION_VALIDATION, new MetamacRolesEnum[]{MetamacRolesEnum.TECNICO_DIFUSION, MetamacRolesEnum.TECNICO_APOYO_DIFUSION});
        roles.put(PUBLISHED, new MetamacRolesEnum[]{MetamacRolesEnum.JEFE_PRODUCCION, MetamacRolesEnum.TECNICO_PRODUCCION, MetamacRolesEnum.TECNICO_APOYO_PRODUCCION});

        actionCodes = new HashMap<ProcStatusEnum, String>();
        actionCodes.put(PRODUCTION_VALIDATION, ServiceNoticeAction.RESOURCE_SEND_PRODUCTION_VALIDATION);
        actionCodes.put(DIFFUSION_VALIDATION, ServiceNoticeAction.RESOURCE_SEND_DIFFUSION_VALIDATION);
        actionCodes.put(VALIDATION_REJECTED, ServiceNoticeAction.RESOURCE_CANCEL_VALIDATION);
        actionCodes.put(PUBLISHED, ServiceNoticeAction.RESOURCE_PUBLICATION);

        messageCodes = new HashMap<ProcStatusEnum, String>();
        messageCodes.put(PRODUCTION_VALIDATION, ServiceNoticeMessage.RESOURCE_SEND_PRODUCTION_VALIDATION_OK);
        messageCodes.put(DIFFUSION_VALIDATION, ServiceNoticeMessage.RESOURCE_SEND_DIFFUSION_VALIDATION_OK);
        messageCodes.put(VALIDATION_REJECTED, ServiceNoticeMessage.RESOURCE_CANCEL_VALIDATION_OK);
        messageCodes.put(PUBLISHED, ServiceNoticeMessage.RESOURCE_PUBLICATION_OK);
    }

    @Override
    public void createLifeCycleNotification(ServiceContext serviceContext, ProcStatusEnum procStatus, DatasetVersion datasetVersion) throws MetamacException {
        
        
        MetamacRolesEnum[] notificationRoles = roles.containsKey(procStatus) ? roles.get(procStatus) : null;
        createNotification(serviceContext, procStatus, datasetVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getUrn(), notificationRoles, getResourceInternalFromDataset(datasetVersion));
 
    }

    private List<ResourceInternal> getResourceInternalFromDataset(DatasetVersion datasetVersion) {
        ResourceInternal resourceInternal = restMapper.generateResourceInternal(datasetVersion);
        List<ResourceInternal> resourceInternalList = new ArrayList<ResourceInternal>();
        resourceInternalList.add(resourceInternal);
        return resourceInternalList;
    }
    
    private void createNotification(ServiceContext ctx, ProcStatusEnum procStatus, String urnStatisticalOperation, MetamacRolesEnum[] notificationRoles, List<ResourceInternal> resourcesInternal)
            throws MetamacException {

        String actionCode = getActionCode(procStatus);
        String messageCode = getMessageCode(procStatus);

        String subject = buildSubject(ctx, actionCode);
        Message message = buildMessage(ctx, messageCode, resourcesInternal);

        NoticeBuilder noticeBuilder = NoticeBuilder.notification().withMessages(message).withSendingApplication(getSendingApp()).withSendingUser(ctx.getUserId()).withSubject(subject);
        if (notificationRoles != null) {
            noticeBuilder = noticeBuilder.withRoles(notificationRoles);
        }
        if (StringUtils.isNotBlank(urnStatisticalOperation)) {
            noticeBuilder = noticeBuilder.withStatisticalOperations(urnStatisticalOperation);
        }

        try {
            Response response = metamacApisLocator.getNoticesRestInternalFacadeV10().createNotice(noticeBuilder.build());
        } catch (MetamacException e) {
            logger.error("Error creating notification for error on view creation:", e);
        }
    }

    private Message buildMessage(ServiceContext ctx, String messageCode, List<ResourceInternal> resources) {
        Locale locale = ServiceContextUtils.getLocale(ctx);
        String localisedMessage = LocaleUtil.getMessageForCode(messageCode, locale);
        return MessageBuilder.message().withText(localisedMessage).withResources(resources).build();
    }

    private String buildSubject(ServiceContext ctx, String actionCode) {
        Locale locale = ServiceContextUtils.getLocale(ctx);
        return LocaleUtil.getMessageForCode(actionCode, locale);
    }

    private String getActionCode(ProcStatusEnum lifeCycleAction) {
        return actionCodes.containsKey(lifeCycleAction) ? actionCodes.get(lifeCycleAction) : StringUtils.EMPTY;
    }

    private String getMessageCode(ProcStatusEnum lifeCycleAction) {
        return messageCodes.containsKey(lifeCycleAction) ? messageCodes.get(lifeCycleAction) : StringUtils.EMPTY;
    }

    private String getSendingApp() {
        return MetamacApplicationsEnum.GESTOR_RECURSOS_ESTADISTICOS.getName();
    }
}
