package org.siemac.metamac.statistical.resources.web.server.handlers.task;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.enume.domain.StatisticalResourceTypeEnum;
import org.siemac.metamac.statistical.resources.core.task.serviceapi.TaskServiceFacade;
import org.siemac.metamac.statistical.resources.web.shared.task.ReloadKafkaTopicAction;
import org.siemac.metamac.statistical.resources.web.shared.task.ReloadKafkaTopicResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class ReloadKafkaTopicActionHandler extends SecurityActionHandler<ReloadKafkaTopicAction, ReloadKafkaTopicResult> {

    @Autowired
    private TaskServiceFacade taskServiceFacade;

    public ReloadKafkaTopicActionHandler() {
        super(ReloadKafkaTopicAction.class);
    }

    @Override
    public ReloadKafkaTopicResult executeSecurityAction(ReloadKafkaTopicAction action) throws ActionException {
        try {
            List<StatisticalResourceTypeEnum> resourceTypes = action.getResourceTypes();

            if (resourceTypes.contains(StatisticalResourceTypeEnum.DATASET)) {
                taskServiceFacade.scheduleReloadTopicDatasetJob(ServiceContextHolder.getCurrentServiceContext());
            }

            if (resourceTypes.contains(StatisticalResourceTypeEnum.COLLECTION)) {
                taskServiceFacade.scheduleReloadTopicPublicationJob(ServiceContextHolder.getCurrentServiceContext());
            }

            if (resourceTypes.contains(StatisticalResourceTypeEnum.QUERY)) {
                taskServiceFacade.scheduleReloadTopicQueryJob(ServiceContextHolder.getCurrentServiceContext());
            }

            return new ReloadKafkaTopicResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
