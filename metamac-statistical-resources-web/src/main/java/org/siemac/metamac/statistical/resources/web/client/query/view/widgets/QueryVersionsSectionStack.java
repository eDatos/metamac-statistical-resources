package org.siemac.metamac.statistical.resources.web.client.query.view.widgets;

import static org.siemac.metamac.statistical.resources.web.client.StatisticalResourcesWeb.getConstants;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.statistical.resources.core.dto.query.QueryVersionBaseDto;
import org.siemac.metamac.statistical.resources.web.client.base.widgets.VersionableResourceSectionStack;
import org.siemac.metamac.statistical.resources.web.client.model.ds.LifeCycleResourceDS;
import org.siemac.metamac.statistical.resources.web.client.query.model.ds.QueryDS;
import org.siemac.metamac.statistical.resources.web.client.utils.StatisticalResourcesRecordUtils;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.widgets.grid.ListGridField;

public class QueryVersionsSectionStack extends VersionableResourceSectionStack {

    public QueryVersionsSectionStack(String title) {
        super(title);

        List<ListGridField> gridFields = new ArrayList<ListGridField>();
        addPublicationStreamStatusToGrid(gridFields);
        addPublicationXStatusGrid(gridFields);
        super.setListGridFields(gridFields.toArray(new ListGridField[gridFields.size()]));
    }

    public void setQueryVersions(List<QueryVersionBaseDto> queryVersionBaseDtos) {
        listGrid.selectAllRecords();
        listGrid.removeSelectedData();
        for (QueryVersionBaseDto queryDto : queryVersionBaseDtos) {
            listGrid.addData(StatisticalResourcesRecordUtils.getQueryRecord(queryDto));
        }
        listGrid.sort(QueryDS.VERSION, SortDirection.DESCENDING);
    }

    public void selectQueryVersion(String currentQueryVersionUrn) {
        selectRecord(QueryDS.URN, currentQueryVersionUrn);
    }

    protected void addPublicationStreamStatusToGrid(List<ListGridField> fieldList) {
        ListGridField publicationStreamStatus = new ListGridField(LifeCycleResourceDS.PUBLICATION_STREAM_STATUS, getConstants().publicationStreamStatus());
        publicationStreamStatus.setType(ListGridFieldType.IMAGE);
        publicationStreamStatus.setAlign(Alignment.CENTER);
        fieldList.add(publicationStreamStatus);
    }

    private void addPublicationXStatusGrid(List<ListGridField> fieldList) {
        ListGridField publicationXStatus = new ListGridField(LifeCycleResourceDS.PUBLICATION_X_STATUS, getConstants().publicationXStatus());
        publicationXStatus.setType(ListGridFieldType.IMAGE);
        publicationXStatus.setAlign(Alignment.CENTER);
        fieldList.add(publicationXStatus);
    }
}
