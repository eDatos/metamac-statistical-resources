package org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers;

import java.util.List;

import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdAttributeInstanceDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.RepresentationDto;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

public interface DatasetAttributesTabUiHandlers extends BaseUiHandlers {

    void retrieveAttributeInstances(DsdAttributeDto dsdAttributeDto);
    void retrieveAttributeInstancesForRefresh(DsdAttributeDto dsdAttributeDto);
    void saveAttributeInstance(DsdAttributeDto dsdAttributeDto, DsdAttributeInstanceDto dsdAttributeInstanceDto);
    void deleteAttributeInstance(DsdAttributeDto dsdAttributeDto, DsdAttributeInstanceDto dsdAttributeInstanceDto);
    void deleteAttributeInstances(DsdAttributeDto dsdAttributeDto, List<String> uuid);

    void retrieveDimensionsCoverage(List<String> dimensionId, MetamacWebCriteria metamacWebCriteria);

    void retrieveItemsFromItemSchemeForDatasetLevelAttribute(RepresentationDto representationDto, int firstResult, int maxResults, MetamacWebCriteria criteria);
    void retrieveItemsFromItemSchemeForDimensionOrGroupLevelAttribute(RepresentationDto representationDto, int firstResult, int maxResults, MetamacWebCriteria criteria);
    void attributesImportationFailed(String errorMessage);
    void attributesImportationSucceed(String fileName);

    void exportAttributes(String urn);
}
