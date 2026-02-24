package org.siemac.metamac.statistical.resources.web.client.dataset.widgets;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import org.siemac.metamac.statistical.resources.core.enume.dataset.domain.DecimalSeparatorTypeEnum;
import org.siemac.metamac.statistical.resources.web.client.utils.CommonUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.VisibilityChangedEvent;
import com.smartgwt.client.widgets.events.VisibilityChangedHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;

public class ExportDatasourcesWindow extends Window {

    private static final String FIELD_DECIMAL_SEPARATOR = "decimal-separator";
    private static final String FIELD_ACCEPT            = "accept";

    private CustomDynamicForm   form;

    public ExportDatasourcesWindow() {
        super();

        setWidth(450);
        setHeight(130);
        setTitle(getConstants().actionExportDatasources());
        setShowMinimizeButton(false);
        setIsModal(true);
        setShowModalMask(true);
        setAutoCenter(true);
        addCloseClickHandler(new CloseClickHandler() {

            @Override
            public void onCloseClick(CloseClickEvent event) {
                hide();
            }
        });
        addVisibilityChangedHandler(new VisibilityChangedHandler() {

            @Override
            public void onVisibilityChanged(VisibilityChangedEvent event) {
                if (event.getIsVisible()) {
                    form.setValue(FIELD_DECIMAL_SEPARATOR, DecimalSeparatorTypeEnum.POINT.name());
                }
            }
        });

        SelectItem decimalSeparatorItem = new SelectItem(FIELD_DECIMAL_SEPARATOR, getConstants().decimalSeparatorTypeEnumLabel());
        decimalSeparatorItem.setValueMap(CommonUtils.getDecimalSeparatorTypeHashMap());
        decimalSeparatorItem.setValue(DecimalSeparatorTypeEnum.POINT.name());
        decimalSeparatorItem.setAlign(Alignment.LEFT);
        decimalSeparatorItem.setWidth("*");

        CustomButtonItem acceptItem = new CustomButtonItem(FIELD_ACCEPT, MetamacWebCommon.getConstants().accept());

        form = new CustomDynamicForm();
        form.setMargin(5);
        form.setTitleWidth(130);
        form.setFields(decimalSeparatorItem, acceptItem);

        addItem(form);
    }

    public HasClickHandlers getSaveButtonHandlers() {
        return form.getItem(FIELD_ACCEPT);
    }

    public DecimalSeparatorTypeEnum getDecimalSeparator() {
        String value = form.getValueAsString(FIELD_DECIMAL_SEPARATOR);
        return DecimalSeparatorTypeEnum.valueOf(value);
    }
}
