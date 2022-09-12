package org.siemac.metamac.statistical.resources.web.client.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.cleanInternationalString;

import java.util.List;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.statistical.resources.core.base.checks.MetadataEditionChecks;
import org.siemac.metamac.statistical.resources.core.dto.LifeCycleStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.dto.VersionRationaleTypeDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.NextVersionTypeEnum;
import org.siemac.metamac.statistical.resources.web.client.base.widgets.SearchVersionRationaleTypeItem;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.CustomDatePickerItem;
import org.siemac.metamac.statistical.resources.web.client.model.ds.VersionableResourceDS;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.web.common.client.utils.CustomRequiredValidator;
import org.siemac.metamac.web.common.client.utils.DateUtils;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomDateItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageRichTextEditorItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;

public class LifeCycleResourceVersionEditionForm extends GroupDynamicForm {
    CustomDatePickerItem nextVersionDate1;
    protected LifeCycleStatisticalResourceDto lifeCycleStatisticalResourceDto;
    
    public LifeCycleResourceVersionEditionForm() {
        super(getConstants().formVersion());

        ViewTextItem versionLogic = new ViewTextItem(VersionableResourceDS.VERSION, getConstants().versionableStatisticalResourceVersionLogic());

        final SearchVersionRationaleTypeItem versionRationaleTypeItem = new SearchVersionRationaleTypeItem(VersionableResourceDS.VERSION_RATIONALE_TYPES, getConstants()
                .versionableStatisticalResourceVersionRationaleTypes(), true);
        versionRationaleTypeItem.setShowIfCondition(getVersionRationaleTypesFormItemIfFunction());
        versionRationaleTypeItem.setValidators(new CustomRequiredValidator() {

            @Override
            protected boolean condition(Object value) {
                if (CommonUtils.isResourceInProductionValidationOrGreaterProcStatus(lifeCycleStatisticalResourceDto.getProcStatus())) {
                    return versionRationaleTypeItem.getSelectedVersionRationaleTypeDtos().size() > 0;
                }
                return true;
            }
        });

        SearchVersionRationaleTypeItem staticVersionRationaleTypeItem = new SearchVersionRationaleTypeItem(VersionableResourceDS.VERSION_RATIONALE_TYPES_VIEW, getConstants()
                .versionableStatisticalResourceVersionRationaleTypes(), false);
        staticVersionRationaleTypeItem.setShowIfCondition(getStaticVersionRationaleTypesFormItemIfFunction());

        MultiLanguageRichTextEditorItem versionRationale = new MultiLanguageRichTextEditorItem(VersionableResourceDS.VERSION_RATIONALE, getConstants().versionableStatisticalResourceVersionRationale());
        versionRationale.setValidators(new CustomRequiredValidator() {

            @Override
            protected boolean condition(Object value) {
                if (CommonUtils.isResourceInProductionValidationOrGreaterProcStatus(lifeCycleStatisticalResourceDto.getProcStatus())) {
                    List<VersionRationaleTypeDto> types = versionRationaleTypeItem.getSelectedVersionRationaleTypeDtos();
                    if (CommonUtils.containsMinorErrataVersionRationaleType(types)) {
                        InternationalStringDto versionRationale = cleanInternationalString(getValueAsInternationalStringDto(VersionableResourceDS.VERSION_RATIONALE));
                        return versionRationale != null;
                    }
                }
                return true;
            }
        });

        ViewTextItem validFrom = new ViewTextItem(VersionableResourceDS.VALID_FROM, getConstants().versionableStatisticalResourceValidFrom());
        validFrom.setCanEdit(false);

        ViewTextItem validTo = new ViewTextItem(VersionableResourceDS.VALID_TO, getConstants().versionableStatisticalResourceValidTo());
        validTo.setCanEdit(false);

        final CustomSelectItem nextVersion = new CustomSelectItem(VersionableResourceDS.NEXT_VERSION, getConstants().versionableStatisticalResourceNextVersion());
        nextVersion.setValueMap(CommonUtils.getStatisticalResourceNextVersionHashMap());
        nextVersion.addChangedHandler(FormItemUtils.getMarkForRedrawChangedHandler(this));
        nextVersion.setValidators(new CustomRequiredValidator() {

            @Override
            protected boolean condition(Object value) {
                return CommonUtils.isResourceInProductionValidationOrGreaterProcStatus(lifeCycleStatisticalResourceDto.getProcStatus()) ? !StringUtils.isBlank(nextVersion.getValueAsString()) : true;
            }
        });

        CustomDateItem nextVersionDate = new CustomDateItem(VersionableResourceDS.DATE_NEXT_VERSION, getConstants().versionableStatisticalResourceNextVersionDate());
        nextVersionDate.setShowIfCondition(getNextVersionDateFormItemIfFunction());
        
        nextVersionDate1 = createFieldDateNextVersion();
        nextVersionDate1.setShowIfCondition(getNextVersionDateFormItemIfFunction());
        
        setFields(versionLogic, versionRationaleTypeItem, staticVersionRationaleTypeItem, versionRationale, validFrom, validTo, nextVersion, nextVersionDate1, nextVersionDate);
    }

    private CustomDatePickerItem createFieldDateNextVersion() {
        CustomDatePickerItem customDatePickerItem = new CustomDatePickerItem(VersionableResourceDS.DATE_NEXT_VERSION1, getConstants().versionableStatisticalResourceNextVersionDate(), false, false);
        customDatePickerItem.defaultDateType();
        return customDatePickerItem;
    }
        
    public void setLifeCycleStatisticalResourceDto(LifeCycleStatisticalResourceDto lifeCycleStatisticalResourceDto) {
        this.lifeCycleStatisticalResourceDto = lifeCycleStatisticalResourceDto;

        setValue(VersionableResourceDS.VERSION, lifeCycleStatisticalResourceDto.getVersionLogic());

        ((SearchVersionRationaleTypeItem) getItem(VersionableResourceDS.VERSION_RATIONALE_TYPES)).setVersionRationaleTypes(lifeCycleStatisticalResourceDto.getVersionRationaleTypes());
        ((SearchVersionRationaleTypeItem) getItem(VersionableResourceDS.VERSION_RATIONALE_TYPES_VIEW)).setVersionRationaleTypes(lifeCycleStatisticalResourceDto.getVersionRationaleTypes());

        setValue(VersionableResourceDS.VERSION_RATIONALE, lifeCycleStatisticalResourceDto.getVersionRationale());
        setValue(VersionableResourceDS.VALID_FROM, DateUtils.getFormattedDateTime(lifeCycleStatisticalResourceDto.getValidFrom()));
        setValue(VersionableResourceDS.VALID_TO, DateUtils.getFormattedDateTime(lifeCycleStatisticalResourceDto.getValidTo()));
        setValue(VersionableResourceDS.NEXT_VERSION, lifeCycleStatisticalResourceDto.getNextVersion() != null ? lifeCycleStatisticalResourceDto.getNextVersion().name() : null);
        setValue(VersionableResourceDS.DATE_NEXT_VERSION, lifeCycleStatisticalResourceDto.getNextVersionDate());
        nextVersionDate1.setValue(lifeCycleStatisticalResourceDto.getNextVersionDate1());
        
        markForRedraw();
    }
    
    public LifeCycleStatisticalResourceDto getLifeCycleStatisticalResourceDto(LifeCycleStatisticalResourceDto lifeCycleStatisticalResourceDto) {

        lifeCycleStatisticalResourceDto.getVersionRationaleTypes().clear();
        lifeCycleStatisticalResourceDto.getVersionRationaleTypes().addAll(
                ((SearchVersionRationaleTypeItem) getItem(VersionableResourceDS.VERSION_RATIONALE_TYPES)).getSelectedVersionRationaleTypeDtos());

        lifeCycleStatisticalResourceDto.setVersionRationale(getValueAsInternationalStringDto(VersionableResourceDS.VERSION_RATIONALE));
        lifeCycleStatisticalResourceDto.setNextVersion(!StringUtils.isBlank(getValueAsString(VersionableResourceDS.NEXT_VERSION)) ? NextVersionTypeEnum
                .valueOf(getValueAsString(VersionableResourceDS.NEXT_VERSION)) : null);
        lifeCycleStatisticalResourceDto.setNextVersionDate(((CustomDateItem) getItem(VersionableResourceDS.DATE_NEXT_VERSION)).getValueAsDate());
        lifeCycleStatisticalResourceDto.setNextVersionDate1(((CustomDatePickerItem) getItem(VersionableResourceDS.DATE_NEXT_VERSION1)).getValue());
        return lifeCycleStatisticalResourceDto;
    }

    protected FormItemIfFunction getNextVersionDateFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return isNextVersionScheduledUpdate();
            }
        };
    }

    protected boolean isNextVersionScheduledUpdate() {
        String nextVersionValue = getValueAsString(VersionableResourceDS.NEXT_VERSION);
        return StringUtils.equals(NextVersionTypeEnum.SCHEDULED_UPDATE.toString(), nextVersionValue);
    }

    // ------------------------------------------------------------------------------------------------------------
    // FORM ITEM IF FUNCTIONS
    // ------------------------------------------------------------------------------------------------------------

    // VERSION RATIONALE TYPES

    private FormItemIfFunction getVersionRationaleTypesFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return MetadataEditionChecks.canVersionRationaleTypesBeEdited(lifeCycleStatisticalResourceDto.getVersionLogic());
            }
        };
    }

    private FormItemIfFunction getStaticVersionRationaleTypesFormItemIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !MetadataEditionChecks.canVersionRationaleTypesBeEdited(lifeCycleStatisticalResourceDto.getVersionLogic());
            }
        };
    }

}
