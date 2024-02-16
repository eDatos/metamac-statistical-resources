package org.siemac.metamac.statistical.resources.web.client.dataset.widgets;

import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;

public class DatasetInGroupMainFormLayout extends InternationalMainFormLayout {

    public DatasetInGroupMainFormLayout() {
        super();
    }

    public void setDatasetVersion() {
        setCanEdit(true);
        setCanDelete(false);
    }

}
