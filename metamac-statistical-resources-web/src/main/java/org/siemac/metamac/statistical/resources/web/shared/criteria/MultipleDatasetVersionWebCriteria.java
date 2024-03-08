package org.siemac.metamac.statistical.resources.web.shared.criteria;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

public class MultipleDatasetVersionWebCriteria extends MetamacWebCriteria {

    private static final long serialVersionUID   = 7050528031069849463L;
    private List<String>      datasetVersionUrns = new ArrayList<String>();

    public List<String> getDatasetVersionUrns() {
        return datasetVersionUrns;
    }
    public void setDatasetVersionUrns(List<String> datasetVersionUrns) {
        this.datasetVersionUrns = datasetVersionUrns;
    }

}
