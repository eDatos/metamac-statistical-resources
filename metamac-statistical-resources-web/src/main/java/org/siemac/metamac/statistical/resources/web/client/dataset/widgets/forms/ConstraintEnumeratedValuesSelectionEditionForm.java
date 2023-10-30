package org.siemac.metamac.statistical.resources.web.client.dataset.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.util.shared.BooleanUtils;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.core.dto.constraint.KeyPartDto;
import org.siemac.metamac.statistical.resources.core.dto.constraint.KeyValueDto;
import org.siemac.metamac.statistical.resources.core.dto.constraint.RegionValueDto;
import org.siemac.metamac.statistical.resources.core.dto.datasets.ItemDto;
import org.siemac.metamac.statistical.resources.core.enume.constraint.domain.KeyPartTypeEnum;
import org.siemac.metamac.statistical.resources.web.client.dataset.model.ds.DimensionConstraintsDS;
import org.siemac.metamac.statistical.resources.web.client.dataset.view.handlers.DatasetConstraintsTabUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.ItemsSelectionTreeItem;
import org.siemac.metamac.statistical.resources.web.client.enums.DatasetConstraintInclusionTypeEnum;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;

public class ConstraintEnumeratedValuesSelectionEditionForm extends ConstraintEnumeratedValuesSelectionBaseForm {

    private CustomSelectItem                inclusionTypeField;
    private CustomSelectItem                srmResourceRestriction;
    protected List<ExternalItemDto>         restrictions;
    private DatasetConstraintsTabUiHandlers uiHandlers;
    private List<ItemDto>                   srmRestrictionCodes;
    private CustomButtonItem                srmRestrictionButton;

    public ConstraintEnumeratedValuesSelectionEditionForm(String groupTitle) {
        super(groupTitle);
        srmResourceRestriction = new CustomSelectItem(DimensionConstraintsDS.SRM_RESOURCE_RESTRICTION, getConstants().srmResourceRestriction());
        srmResourceRestriction.setAlign(Alignment.LEFT);
        srmResourceRestriction.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                String codeSrmRestriction = getValueAsString(DimensionConstraintsDS.SRM_RESOURCE_RESTRICTION);
                if (!StringUtils.isEmpty(codeSrmRestriction)) {
                    srmRestrictionButton.disable();
                    getUiHandlers().retrieveCodes(getSelectedDimension(), codeSrmRestriction);
                }
            }
        });

        inclusionTypeField = new CustomSelectItem(DimensionConstraintsDS.INCLUSION_TYPE, getConstants().datasetConstraintInclusionType());
        inclusionTypeField.setRequired(true);
        inclusionTypeField.setValueMap(CommonUtils.getConstraintInclusionTypeHashMap());
        inclusionTypeField.setAlign(Alignment.LEFT);
        inclusionTypeField.setWidth(100);

        createSrmRestrictionButton();

        treeItem = new ItemsSelectionTreeItem(DimensionConstraintsDS.VALUES, "tree-values-selection", true);
        treeItem.setShowTitle(false);
        treeItem.setStartRow(true);
        treeItem.setColSpan(4);

        setFields(srmResourceRestriction, inclusionTypeField, srmRestrictionButton, treeItem);
    }

    /**
     * Updates the region with the specified values. Each {@link RegionValueDto} has a list of {@link KeyValueDto}. All the {@link KeyPartDto} of a {@link KeyValueDto} belongs to the same dimension.
     * 
     * @param regionValueDto
     */
    public RegionValueDto updateRegionDto(RegionValueDto regionValueDto) {
        Boolean included = DatasetConstraintInclusionTypeEnum.INCLUSION.equals(CommonUtils.getDatasetConstraintInclusionTypeEnum(inclusionTypeField.getValueAsString()));
        Map<String, Boolean> selectedItems = treeItem.getSelectedItems();

        KeyValueDto keyValueDto = CommonUtils.getKeyValueOfDimension(dsdDimensionDto, regionValueDto);
        if (keyValueDto == null) {
            keyValueDto = new KeyValueDto();
            keyValueDto.setRegion(regionValueDto);
            regionValueDto.addKey(keyValueDto);
        }
        keyValueDto.setIncluded(included);
        keyValueDto.removeAllParts();
        for (String itemCode : selectedItems.keySet()) {
            KeyPartDto keyPartDto = new KeyPartDto();
            keyPartDto.setType(KeyPartTypeEnum.NORMAL);
            keyPartDto.setIdentifier(dsdDimensionDto.getDimensionId());
            keyPartDto.setValue(itemCode);
            keyPartDto.setCascadeValues(selectedItems.get(itemCode));
            keyPartDto.setPosition(dsdDimensionDto.getPosition());
            keyValueDto.addPart(keyPartDto);
        }
        if (keyValueDto.getParts().isEmpty()) {
            regionValueDto.removeKey(keyValueDto);
        }
        return regionValueDto;
    }

    public void setTreeFromSrmRestriction() {

        List<KeyPartDto> srmRestrictionCodesKeyParts = new ArrayList<KeyPartDto>();
        for (ItemDto codeRestriction : srmRestrictionCodes) {
            KeyPartDto keyPartDto = new KeyPartDto();
            keyPartDto.setType(KeyPartTypeEnum.NORMAL);
            keyPartDto.setIdentifier(dsdDimensionDto.getDimensionId());
            keyPartDto.setValue(codeRestriction.getCode());
            keyPartDto.setCascadeValues(false);
            keyPartDto.setPosition(dsdDimensionDto.getPosition());
            srmRestrictionCodesKeyParts.add(keyPartDto);
        }

        setSavedRegionValues(srmRestrictionCodesKeyParts);

    }

    @Override
    protected void setInclusionTypeValue(Boolean isIncluded) {
        setValue(DimensionConstraintsDS.INCLUSION_TYPE, BooleanUtils.isTrue(isIncluded) ? DatasetConstraintInclusionTypeEnum.INCLUSION.name() : DatasetConstraintInclusionTypeEnum.EXCLUSION.name());
    }

    public List<ExternalItemDto> getRestrictions() {
        return restrictions;
    }

    public CustomSelectItem getSrmResourceRestriction() {
        return srmResourceRestriction;
    }

    public void setRestrictions(List<ExternalItemDto> restrictions) {
        this.restrictions = restrictions;
        srmResourceRestriction.setValueMap(CommonUtils.getRestrictionsHashMap(restrictions));
    }

    public void showRestrictions(Boolean isIncluded) {
        if (Boolean.TRUE.equals(isIncluded)) {
            srmResourceRestriction.show();
            srmRestrictionButton.show();
        } else {
            srmResourceRestriction.clearValue();
            srmResourceRestriction.hide();
            srmRestrictionButton.hide();
        }
    }

    public void setUiHandlers(DatasetConstraintsTabUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public DatasetConstraintsTabUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    public void setSrmRestrictionCodes(List<ItemDto> itemDtos) {
        this.srmRestrictionCodes = itemDtos;
        srmRestrictionButton.enable();
    }

    private CustomButtonItem createSrmRestrictionButton() {
        srmRestrictionButton = new CustomButtonItem(DimensionConstraintsDS.ACTION_APPLY_SRM_RESTRICTION, getConstants().actionApplySrmRestriction());
        srmRestrictionButton.setAlign(Alignment.LEFT);
        srmRestrictionButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                setTreeFromSrmRestriction();
            }
        });
        srmRestrictionButton.setVisible(false);
        return srmRestrictionButton;
    }
}
