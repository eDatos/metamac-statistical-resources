package org.siemac.metamac.statistical.resources.web.client.dataset.view;
import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.InformationLabel;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomTextItem;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;

public class CopyResourceWindow extends CustomWindow {

    private static final String FIELD_CODE   = "version-scheme";
    private static final String FIELD_ACCEPT = "accept-item";

    private CustomDynamicForm   form;

    public CopyResourceWindow(String title) {
        super(title);
        setWidth(330);
        setHeight(170);

        InformationLabel informationLabel = new InformationLabel(getConstants().copyResourceInfoMessage());
        informationLabel.setWidth100();
        informationLabel.setMargin(10);

        CustomTextItem codeItem = new CustomTextItem(FIELD_CODE, getConstants().code());
        codeItem.setRequired(true);

        CustomButtonItem acceptButtonItem = new CustomButtonItem(FIELD_ACCEPT, MetamacWebCommon.getConstants().accept());

        form = new CustomDynamicForm();
        form.setFields(codeItem, acceptButtonItem);
        form.setMargin(10);
        form.setAlign(Alignment.CENTER);

        addItem(informationLabel);
        addItem(form);
        show();
    }

    public String getSelectedCode() {
        return form.getValueAsString(FIELD_CODE);
    }

    public boolean validateForm() {
        return form.validate();
    }

    public HasClickHandlers getSave() {
        return form.getItem(FIELD_ACCEPT);
    }
}
