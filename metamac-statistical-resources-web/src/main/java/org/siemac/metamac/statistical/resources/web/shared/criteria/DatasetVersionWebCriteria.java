package org.siemac.metamac.statistical.resources.web.shared.criteria;

import java.util.Date;

import org.siemac.metamac.statistical.resources.web.shared.criteria.base.HasDataCriteria;

public class DatasetVersionWebCriteria extends SiemacMetadataStatisticalResourceWebCriteria implements HasDataCriteria {

    private static final long serialVersionUID = 7050528031069849463L;

    private Boolean           hasData;
    private String            geographicalGranularityUrn;
    private String            temporalGranularityUrn;
    private Date              dateStart;
    private Date              dateEnd;
    private String            dsdUrn;
    private Date              dateNextUpdate;
    private String            dateNextUpdate1;
    private String            statisticOfficialityIdentifier;

    public DatasetVersionWebCriteria() {
        super();
        this.hasData = null;
    }

    public DatasetVersionWebCriteria(String criteria, boolean hasData) {
        super(criteria);
        this.hasData = hasData;
    }

    public DatasetVersionWebCriteria(String criteria) {
        super(criteria);
        this.hasData = null;
    }

    @Override
    public Boolean getHasData() {
        return hasData;
    }
    @Override
    public void setHasData(Boolean hasData) {
        this.hasData = hasData;
    }

    public String getGeographicalGranularityUrn() {
        return geographicalGranularityUrn;
    }

    public String getTemporalGranularityUrn() {
        return temporalGranularityUrn;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public String getDsdUrn() {
        return dsdUrn;
    }

    public Date getDateNextUpdate() {
        return dateNextUpdate;
    }

    public String getStatisticOfficialityIdentifier() {
        return statisticOfficialityIdentifier;
    }

    public void setGeographicalGranularityUrn(String geographicalGranularityUrn) {
        this.geographicalGranularityUrn = geographicalGranularityUrn;
    }

    public void setTemporalGranularityUrn(String temporalGranularityUrn) {
        this.temporalGranularityUrn = temporalGranularityUrn;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public void setDsdUrn(String dsdUrn) {
        this.dsdUrn = dsdUrn;
    }

    public void setDateNextUpdate(Date dateNextUpdate) {
        this.dateNextUpdate = dateNextUpdate;
    }

    public void setStatisticOfficialityIdentifier(String statisticOfficialityIdentifier) {
        this.statisticOfficialityIdentifier = statisticOfficialityIdentifier;
    }

    public String getDateNextUpdate1() {
        return dateNextUpdate1;
    }

    public void setDateNextUpdate1(String dateNextUpdate1) {
        this.dateNextUpdate1 = dateNextUpdate1;
    }
}
