package org.siemac.metamac.statistical.resources.web.client.widgets.windows;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomTextAreaItem;

import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;

public class GeographicalCacheUpdateOptionsWindow extends CustomWindow {

    private static final int    FORM_ITEM_CUSTOM_WIDTH = 300;

    private static final String REASON_OF_REJECTION    = "reason-rej";
    private static final String FIELD_ACCEPT           = "accept";

    private CustomDynamicForm   form;

    public GeographicalCacheUpdateOptionsWindow(String title) {
        super(title);
        setAutoSize(true);

        CustomTextAreaItem reasonItem = new CustomTextAreaItem(REASON_OF_REJECTION, getConstants().lifeCycleReasonOfRejection());
        reasonItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);

        CustomButtonItem acceptItem = new CustomButtonItem(FIELD_ACCEPT, getConstants().lifeCycleRejectValidation());

        form = new CustomDynamicForm();
        form.setMargin(15);
        form.setFields(reasonItem, acceptItem);

        addItem(form);
        show();
    }

    public HasClickHandlers getAccept() {
        return form.getItem(FIELD_ACCEPT);
    }

    public boolean validateForm() {
        return form.validate();
    }

    public String getReasonOfRejection() {
        return form.getValueAsString(REASON_OF_REJECTION);
    }
}
