package org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.core.dto.constraint.KeyPartDto;
import org.siemac.metamac.statistical.resources.core.dto.constraint.KeyValueDto;
import org.siemac.metamac.statistical.resources.core.dto.constraint.RegionValueDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdDimensionDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.ItemDto;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DimensionConstraintsDS;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.ItemsSelectionTreeItem;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;

public abstract class ConstraintEnumeratedValuesSelectionBaseForm extends GroupDynamicForm {

    protected ItemsSelectionTreeItem treeItem;

    protected DsdDimensionDto        dsdDimensionDto;

    public ConstraintEnumeratedValuesSelectionBaseForm(String groupTitle) {
        super(groupTitle);
    }

    public void setRegionValues(RegionValueDto regionValueDto, DsdDimensionDto dsdDimensionDto, ExternalItemDto itemScheme, List<ItemDto> items) {
        clearErrors(true);

        this.dsdDimensionDto = dsdDimensionDto;
        treeItem.setItems(itemScheme, items);

        setSavedRegionValues(regionValueDto);
    }

    public void setSavedRegionValues(RegionValueDto regionValueDto) {
        KeyValueDto selectedDimensionKeyValue = CommonUtils.getKeyValueOfDimension(dsdDimensionDto, regionValueDto);

        if (selectedDimensionKeyValue == null) {
            setValue(DimensionConstraintsDS.INCLUSION_TYPE, StringUtils.EMPTY);
        } else {
            setInclusionTypeValue(selectedDimensionKeyValue.getIncluded());
            treeItem.selectItems(buildMap(selectedDimensionKeyValue.getParts()), false);
        }
    }

    public void setSavedRegionValues(List<KeyPartDto> regionKeyParts) {
        treeItem.selectItems(buildMap(regionKeyParts), true);
    }

    public DsdDimensionDto getSelectedDimension() {
        return dsdDimensionDto;
    }

    private Map<String, KeyPartDto> buildMap(List<KeyPartDto> keyPartDtos) {
        Map<String, KeyPartDto> keyPartsMap = new HashMap<String, KeyPartDto>();
        for (KeyPartDto keyPart : keyPartDtos) {
            keyPartsMap.put(keyPart.getValue(), keyPart);
        }
        return keyPartsMap;
    }

    protected abstract void setInclusionTypeValue(Boolean isIncluded);
}
