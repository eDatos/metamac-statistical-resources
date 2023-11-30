package org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getCoreMessages;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.BooleanUtils;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.core.dto.constraint.KeyPartDto;
import org.siemac.metamac.statistical.resources.core.dto.constraint.KeyValueDto;
import org.siemac.metamac.statistical.resources.core.dto.constraint.RegionValueDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.DsdDimensionDto;
import org.siemac.metamac.statistical.resources.core.enume.constraint.domain.KeyPartTypeEnum;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DimensionConstraintsDS;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.statistical.resources.web.client.widgets.RangeItem;
import org.siemac.metamac.statistical.resources.web.shared.dtos.RangeDto;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.smartgwt.client.types.Alignment;

public class ConstraintNonEnumeratedValuesSelectionForm extends GroupDynamicForm {

    private DsdDimensionDto dsdDimensionDto;

    public ConstraintNonEnumeratedValuesSelectionForm(String groupTitle) {
        super(groupTitle);

        ViewTextItem inclusionTypeItem = new ViewTextItem(DimensionConstraintsDS.INCLUSION_TYPE, getConstants().datasetConstraintInclusionType());
        inclusionTypeItem.setAlign(Alignment.LEFT);
        inclusionTypeItem.setWidth(100);

        ViewTextItem keyPartTypeItem = new ViewTextItem(DimensionConstraintsDS.KEY_PART_TYPE, getConstants().datasetConstraintKeyPartType());
        keyPartTypeItem.setAlign(Alignment.LEFT);
        keyPartTypeItem.setWidth(100);

        ViewTextItem valuesItem = new ViewTextItem(DimensionConstraintsDS.VALUES, getConstants().datasetConstraintValues());

        RangeItem timeRangeItem = new RangeItem(DimensionConstraintsDS.TIME_RANGE, getConstants().datasetConstraintTimeRange(), false);

        setFields(inclusionTypeItem, keyPartTypeItem, valuesItem, timeRangeItem);
    }

    public void setRegionValues(RegionValueDto regionValueDto, DsdDimensionDto dsdDimensionDto) {
        clearFormValues();
        this.dsdDimensionDto = dsdDimensionDto;
        KeyValueDto keyValueDto = CommonUtils.getKeyValueOfDimension(dsdDimensionDto, regionValueDto);
        if (keyValueDto != null) {
            KeyPartTypeEnum keyPartType = CommonUtils.getKeyPartTypeOfKeyValue(keyValueDto);
            setValue(DimensionConstraintsDS.INCLUSION_TYPE,
                    BooleanUtils.isTrue(keyValueDto.getIncluded()) ? getCoreMessages().datasetConstraintInclusionTypeEnumINCLUSION() : getCoreMessages().datasetConstraintInclusionTypeEnumEXCLUSION());
            setValue(DimensionConstraintsDS.KEY_PART_TYPE, keyPartType == null ? StringUtils.EMPTY : getCoreMessages().getString(getCoreMessages().keyPartTypeEnum() + keyPartType.name()));
            if (KeyPartTypeEnum.NORMAL.equals(keyPartType)) {
                List<String> values = CommonUtils.getValuesOfKeyValue(keyValueDto);
                setValue(DimensionConstraintsDS.VALUES, CommonWebUtils.getStringListToString(values));
                getItem(DimensionConstraintsDS.VALUES).show();
            } else if (KeyPartTypeEnum.TIME_RANGE.equals(keyPartType)) {
                // KeyValues with TIME_RANGE type only have one KeyPart
                KeyPartDto keyPartDto = keyValueDto.getParts().get(0);
                RangeDto rangeDto = CommonUtils.buildRangeDto(keyPartDto);
                ((RangeItem) getItem(DimensionConstraintsDS.TIME_RANGE)).setValue(rangeDto);
                getItem(DimensionConstraintsDS.TIME_RANGE).show();
            }
        }
    }

    public DsdDimensionDto getSelectedDimension() {
        return dsdDimensionDto;
    }

    private void clearFormValues() {
        setValue(DimensionConstraintsDS.INCLUSION_TYPE, StringUtils.EMPTY);
        setValue(DimensionConstraintsDS.KEY_PART_TYPE, StringUtils.EMPTY);
        setValue(DimensionConstraintsDS.VALUES, StringUtils.EMPTY);
        getItem(DimensionConstraintsDS.VALUES).hide();
        setValue(DimensionConstraintsDS.TIME_RANGE, StringUtils.EMPTY);
        getItem(DimensionConstraintsDS.TIME_RANGE).hide();
    }
}
