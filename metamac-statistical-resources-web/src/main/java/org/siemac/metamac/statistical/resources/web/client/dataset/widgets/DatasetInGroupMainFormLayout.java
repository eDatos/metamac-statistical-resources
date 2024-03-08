package org.siemac.metamac.statistical.resources.web.client.dataset.widgets;

import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;

public class DatasetInGroupMainFormLayout extends InternationalMainFormLayout {

    public DatasetInGroupMainFormLayout() {
        super();
    }

    public void setDatasetVersion() {
        setCanEdit(false);
        setCanDelete(false);
    }

    @Override
    public void setEditionMode() {
        viewFormLayout.hide();
        editionFormLayout.show();
        editToolStripButton.hide();
        deleteToolStringButton.hide();
        saveToolStripButton.hide();
        cancelToolStripButton.hide();
    }

}
