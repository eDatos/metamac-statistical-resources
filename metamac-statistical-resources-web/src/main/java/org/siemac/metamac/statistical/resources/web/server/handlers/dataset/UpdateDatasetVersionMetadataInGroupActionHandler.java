package org.siemac.metamac.statistical.resources.web.server.handlers.dataset;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.conf.StatisticalResourcesConfiguration;
import org.siemac.metamac.statistical.resources.core.dto.datasets.CategorisationDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.statistical.resources.core.facade.serviceapi.StatisticalResourcesServiceFacade;
import org.siemac.metamac.statistical.resources.web.server.handlers.UpdateResourceProcStatusBaseActionHandler;
import org.siemac.metamac.statistical.resources.web.server.rest.SrmRestInternalFacade;
import org.siemac.metamac.statistical.resources.web.shared.dataset.UpdateDatasetVersionMetadataInGroupAction;
import org.siemac.metamac.statistical.resources.web.shared.dataset.UpdateDatasetVersionMetadataInGroupResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateDatasetVersionMetadataInGroupActionHandler extends UpdateResourceProcStatusBaseActionHandler<UpdateDatasetVersionMetadataInGroupAction, UpdateDatasetVersionMetadataInGroupResult> {

    @Autowired
    private StatisticalResourcesServiceFacade statisticalResourcesServiceFacade;

    @Autowired
    private SrmRestInternalFacade             srmRestInternalFacade;

    @Autowired
    private StatisticalResourcesConfiguration configurationService;

    public UpdateDatasetVersionMetadataInGroupActionHandler() {
        super(UpdateDatasetVersionMetadataInGroupAction.class);
    }

    @Override
    public UpdateDatasetVersionMetadataInGroupResult executeSecurityAction(UpdateDatasetVersionMetadataInGroupAction action) throws ActionException {
        MetamacException metamacException = new MetamacException();
        DatasetVersionDto datasetVersionTemplateDto = action.getDatasetVersion();
        try {
            updateCategorisation(action.getCategorisations());
            statisticalResourcesServiceFacade.updateDatasetVersionInGroup(ServiceContextHolder.getCurrentServiceContext(), action.getDatasetVersion(), action.getDatasetsUrnsoUpdate(),
                    action.getCategorisations());

        } catch (MetamacException e) {
            metamacException = e;
            // datasetVersionTemplateDto.setUrn(urn);
            // addExceptionsItemToMetamacException(action.getDatasetVersion().getProcStatus(), datasetVersionTemplateDto, metamacException, e);
        }

        MetamacWebException notificationException = null;

        if (metamacException.getExceptionItems() == null || metamacException.getExceptionItems().isEmpty()) {
            return new UpdateDatasetVersionMetadataInGroupResult.Builder().notificationException(notificationException).build();
        } else {
            MetamacWebException metamacWebException = WebExceptionUtils.createMetamacWebException(metamacException);
            if (notificationException != null) {
                metamacWebException.getWebExceptionItems().addAll(notificationException.getWebExceptionItems());
            }
            throw metamacWebException;
        }
    }

    private void updateCategorisation(List<CategorisationDto> categorisationsDto) throws MetamacException, MetamacWebException {
        if (categorisationsDto != null && !categorisationsDto.isEmpty()) {
            String organisationUrn = configurationService.retrieveOrganisationUrn();
            ExternalItemDto maintainer = srmRestInternalFacade.retrieveAgencyByUrn(organisationUrn);
            for (CategorisationDto categorisationDto : categorisationsDto) {
                categorisationDto.setMaintainer(maintainer);
                categorisationDto.setCode(null);
                categorisationDto.setUrn(null);
            }
        }
    }
}
