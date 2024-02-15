package org.siemac.metamac.statistical.resources.web.client.dataset.widgets;

import org.siemac.metamac.statistical.resources.core.dto.datasets.DatasetVersionDto;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;

public class DatasetInGroupMainFormLayout extends InternationalMainFormLayout {

    private DatasetVersionDto datasetVersionDto;

    public DatasetInGroupMainFormLayout() {
        super();
        createButtonsForToolStrip();
    }

    private void createButtonsForToolStrip() {
    }

    public DatasetInGroupMainFormLayout(boolean canEdit) {
        createButtonsForToolStrip();
    }

    public void setDatasetVersion(DatasetVersionDto datasetVersionDto) {
        this.datasetVersionDto = datasetVersionDto;
        setCanEdit(true);
    }

    //
    // SECURITY
    //

}
