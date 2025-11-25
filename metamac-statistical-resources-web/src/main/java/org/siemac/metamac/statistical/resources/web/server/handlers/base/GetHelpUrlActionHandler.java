package org.siemac.metamac.statistical.resources.web.server.handlers.base;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.web.shared.base.GetHelpUrlAction;
import org.siemac.metamac.statistical.resources.web.shared.base.GetHelpUrlResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetHelpUrlActionHandler extends SecurityActionHandler<GetHelpUrlAction, GetHelpUrlResult> {

    private static final Logger               log = LoggerFactory.getLogger(GetHelpUrlActionHandler.class);

    @Autowired
    private StatisticalResourcesConfiguration configurationService;

    public GetHelpUrlActionHandler() {
        super(GetHelpUrlAction.class);
    }

    @Override
    public GetHelpUrlResult executeSecurityAction(GetHelpUrlAction action) throws ActionException {
        try {
            String helpUrl = configurationService.retrieveHelpUrl();
            return new GetHelpUrlResult(helpUrl);
        } catch (MetamacException e) {
            log.debug("Error retrieving application statistical-resources OPTIONAL help url", e);
            return new GetHelpUrlResult("");
        }
    }
}
