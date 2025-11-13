package org.siemac.metamac.statistical.resources.web.shared.criteria;

import java.util.Date;

import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryTypeEnum;

public class QueryVersionWebCriteria extends LifeCycleStatisticalResourceWebCriteria {

    private static final long serialVersionUID = 1L;

    private String            datasetVersionUrn;
    private QueryStatusEnum   queryStatus;
    private QueryTypeEnum     queryType;
    private Date              newnessUntilDate;
    private Date              featuredUntilDate;

    public QueryVersionWebCriteria() {
        super();
    }

    public QueryVersionWebCriteria(String criteria) {
        super(criteria);
    }

    public String getDatasetVersionUrn() {
        return datasetVersionUrn;
    }

    public QueryStatusEnum getQueryStatus() {
        return queryStatus;
    }

    public QueryTypeEnum getQueryType() {
        return queryType;
    }

    public void setDatasetVersionUrn(String datasetVersionUrn) {
        this.datasetVersionUrn = datasetVersionUrn;
    }

    public void setQueryStatus(QueryStatusEnum queryStatus) {
        this.queryStatus = queryStatus;
    }

    public void setQueryType(QueryTypeEnum queryType) {
        this.queryType = queryType;
    }

    public Date getNewnessUntilDate() {
        return newnessUntilDate;
    }

    public void setNewnessUntilDate(Date newnessUntilDate) {
        this.newnessUntilDate = newnessUntilDate;
    }

    public Date getFeaturedUntilDate() {
        return featuredUntilDate;
    }

    public void setFeaturedUntilDate(Date featuredUntilDate) {
        this.featuredUntilDate = featuredUntilDate;
    }

}
