package org.siemac.metamac.statistical.resources.web.server.handlers.base;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.dto.datasets.StatisticOfficialityDto;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb;
import org.siemac.metamac.statistical.resources.web.server.rest.SrmRestInternalFacade;
import org.siemac.metamac.statistical.resources.web.shared.base.GetInitialValuesAction;
import org.siemac.metamac.statistical.resources.web.shared.base.GetInitialValuesResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetInitialValuesActionHandler extends SecurityActionHandler<GetInitialValuesAction, GetInitialValuesResult> {

    @Autowired
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;

    @Autowired
    private SrmRestInternalFacade             srmRestInternalFacade;

    @Autowired
    private StatisticalResourcesConfiguration configurationService;
    
    private static Logger log  = Logger.getLogger(GetInitialValuesActionHandler.class.getName());


    public GetInitialValuesActionHandler() {
        super(GetInitialValuesAction.class);
    }

    @Override
    public GetInitialValuesResult executeSecurityAction(GetInitialValuesAction action) throws ActionException {
        try {

            // Statistic officialities

            List<StatisticOfficialityDto> statisticOfficialities = statisticalResourcesServiceFacade.findStatisticOfficialities(ServiceContextHolder.getCurrentServiceContext());

            // Default agency

            ExternalItemDto defaultAgency = null;
            try {
	            String agencyUrn = configurationService.retrieveOrganisationUrn();
	            if (StringUtils.isNotBlank(agencyUrn)) {
	                defaultAgency = srmRestInternalFacade.retrieveAgencyByUrn(agencyUrn);
	            }
            } catch(MetamacException e) {
            	log.log(Level.WARNING, "Error retrieving application OPTIONAL Organisation Urn", e);
            }

            // Default languages

            ExternalItemDto defaultLanguage = null;
            try {
	            String languageCodeUrn = configurationService.retrieveDefaultCodeLanguageUrn();
	            if (StringUtils.isNotBlank(languageCodeUrn)) {
	                defaultLanguage = srmRestInternalFacade.retrieveCodeByUrn(languageCodeUrn);
	            }
	        } catch(MetamacException e) {
            	log.log(Level.WARNING, "Error retrieving application OPTIONAL Default Code Language Urn", e);
	        }

            return new GetInitialValuesResult(statisticOfficialities, defaultAgency, defaultLanguage);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
