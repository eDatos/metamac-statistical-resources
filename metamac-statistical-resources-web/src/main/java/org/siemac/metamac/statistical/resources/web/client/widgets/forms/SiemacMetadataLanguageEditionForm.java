package org.siemac.metamac.statistical.resources.web.client.widgets.forms;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import static org.siemac.metamac.statistical.resources.web.client.widgets.forms.StatisticalResourcesFormUtils.getExternalItemsValue;
import static org.siemac.metamac.statistical.resources.web.client.widgets.forms.StatisticalResourcesFormUtils.setExternalItemsValue;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.statistical.resources.core.dto.SiemacMetadataStatisticalResourceDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.web.client.base.view.handlers.StatisticalResourceUiHandlers;
import org.siemac.metamac.statistical.resources.web.client.constants.StatisticalResourceWebConstants;
import org.siemac.metamac.statistical.resources.web.client.dataset.widgets.utils.DatasetWidgetsUtil;
import org.siemac.metamac.statistical.resources.web.client.model.ds.SiemacMetadataDS;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.web.common.client.utils.CustomRequiredValidator;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.ExternalItemLinkItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.SearchMultiExternalItemSimpleItem;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

public class SiemacMetadataLanguageEditionForm extends GroupDynamicForm {

    protected ProcStatusEnum                  procStatus;

    private StatisticalResourceUiHandlers     uiHandlers;

    private SearchMultiExternalItemSimpleItem languagesItem;

    public SiemacMetadataLanguageEditionForm() {
        super(getConstants().formLanguages());
        init(false);
    }

    public SiemacMetadataLanguageEditionForm(boolean isMultipleUpdate) {
        super(getConstants().formLanguages());
        init(isMultipleUpdate);

    }

    private void init(boolean isMultipleUpdate) {

        ExternalItemLinkItem language = new ExternalItemLinkItem(SiemacMetadataDS.LANGUAGE, getConstants().siemacMetadataStatisticalResourceLanguage());

        languagesItem = createLanguagesItem();
        languagesItem.setValidators(new CustomRequiredValidator() {

            @Override
            protected boolean condition(Object value) {
                List<ExternalItemDto> values = getExternalItemsValue(getItem(SiemacMetadataDS.LANGUAGES));
                return CommonUtils.isResourceInProductionValidationOrGreaterProcStatus(procStatus) ? (values != null && values.size() > 0) : true;
            }
        });

        language.setShowIfCondition(DatasetWidgetsUtil.getIsSingleUpdateFunction(isMultipleUpdate));

        setFields(language, languagesItem);
    }

    public void setSiemacMetadataStatisticalResourceDto(SiemacMetadataStatisticalResourceDto dto) {
        this.procStatus = dto.getProcStatus();

        setValue(SiemacMetadataDS.LANGUAGE, dto.getLanguage());
        setExternalItemsValue(getItem(SiemacMetadataDS.LANGUAGES), dto.getLanguages());
    }

    public SiemacMetadataStatisticalResourceDto getSiemacMetadataStatisticalResourceDto(SiemacMetadataStatisticalResourceDto dto) {
        dto.getLanguages().clear();
        dto.getLanguages().addAll(getExternalItemsValue(getItem(SiemacMetadataDS.LANGUAGES)));
        return dto;
    }

    public void setCodesForLanguages(List<ExternalItemDto> items, int firstResult, int totalResults) {
        languagesItem.setResources(items, firstResult, totalResults);
    }

    private SearchMultiExternalItemSimpleItem createLanguagesItem() {
        return new SearchMultiExternalItemSimpleItem(SiemacMetadataDS.LANGUAGES, getConstants().siemacMetadataStatisticalResourceLanguages(), StatisticalResourceWebConstants.FORM_LIST_MAX_RESULTS) {

            @Override
            protected void retrieveResources(int firstResult, int maxResults, MetamacWebCriteria webCriteria) {
                uiHandlers.retrieveLanguagesCodes(firstResult, maxResults, webCriteria);
            }
        };
    }

    public void setUiHandlers(StatisticalResourceUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }
}
