package org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers;

import java.util.List;

import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.statistical.resources.core.dto.constraint.ContentConstraintDto;
import org.siemac.metamac.statistical.resources.core.dto.constraint.RegionValueDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdDimensionDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.ItemDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface DatasetConstraintsTabUiHandlers extends UiHandlers {

    void createConstraint();
    void deleteConstraint(ContentConstraintDto contentConstraintDto, RegionValueDto regionValueDto);
    void saveRegion(String contentConstraintUrn, RegionValueDto regionToSave, DsdDimensionDto selectedDimension);
    void retrieveCodes(DsdDimensionDto dsdDimensionDto, String codeSrmRestriction);
    void retrieveConcepts(DsdDimensionDto dsdDimensionDto);
    void retrieveRestrictions(DsdDimensionDto dsdDimensionDto, TypeExternalArtefactsEnum type);
    void applySrmRestrictions(List<ItemDto> srmRestrictionCodes);
}
