package org.siemac.metamac.statistical.resources.web.client.widgets;

import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

public class WarningWindow extends CustomWindow {

    private final IButton acceptButtomItem;
    private final IButton cancelButtomItem;

    Layout warningWindowLayout;

    public WarningWindow(String body) {
        super(getConstants().warning());
        setHeaderIcon(GlobalResources.RESOURCE.warn().getURL());
        setHeight(110);
        setWidth(360);

        warningWindowLayout = new VLayout();

        // Label with warning text
        HLayout labelLayout = new HLayout(2);
        Label label = new Label(body);
        label.setAutoHeight();
        label.setAutoWidth();
        label.setWrap(false);

        // Center the label
        labelLayout.addMember(label);
        labelLayout.setAlign(Alignment.CENTER);
        warningWindowLayout.addMember(labelLayout);

        // Buttons
        acceptButtomItem = new IButton(MetamacWebCommon.getConstants().actionContinue());
        acceptButtomItem.setIcon(GlobalResources.RESOURCE.success().getURL());

        cancelButtomItem = new IButton(MetamacWebCommon.getConstants().actionCancel());
        cancelButtomItem.setIcon(GlobalResources.RESOURCE.cancelListGrid().getURL());

        // Buttons layout
        HLayout buttonsLayout = new HLayout(2);
        buttonsLayout.addMember(cancelButtomItem);
        buttonsLayout.addMember(acceptButtomItem);
        buttonsLayout.setAlign(Alignment.CENTER);
        warningWindowLayout.addMember(buttonsLayout);

        // Main layout settings
        warningWindowLayout.setMembersMargin(20);
        warningWindowLayout.setMargin(10);


        addItem(warningWindowLayout);

        show();
    }

    public IButton getAcceptButtomItem() {
        return acceptButtomItem;
    }

    public IButton getCancelButtomItem() {
        return cancelButtomItem;
    }
}
