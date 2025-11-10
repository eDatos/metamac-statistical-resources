package org.siemac.metamac.statistical.resources.web.shared.criteria;

import java.util.Date;

import org.siemac.metamac.statistical.resources.core.enume.domain.StreamMessageStatusEnum;

public class SiemacMetadataStatisticalResourceWebCriteria extends LifeCycleStatisticalResourceWebCriteria {

    private static final long       serialVersionUID = 1L;

    private String                  titleAlternative;
    private String                  keywords;
    private Date                    newnessUtilDate;
    private Date                    featuredUtilDate;
    private StreamMessageStatusEnum publicationStreamStatus;

    public SiemacMetadataStatisticalResourceWebCriteria() {
        super();
    }

    SiemacMetadataStatisticalResourceWebCriteria(String criteria) {
        super(criteria);
    }

    public String getTitleAlternative() {
        return titleAlternative;
    }

    public void setTitleAlternative(String titleAlternative) {
        this.titleAlternative = titleAlternative;
    }

    public String getKeywords() {
        return keywords;
    }

    public Date getNewnessUtilDate() {
        return newnessUtilDate;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void setNewnessUtilDate(Date newnessUtilDate) {
        this.newnessUtilDate = newnessUtilDate;
    }

    public StreamMessageStatusEnum getPublicationStreamStatus() {
        return publicationStreamStatus;
    }

    public void setPublicationStreamStatus(StreamMessageStatusEnum publicationStreamStatus) {
        this.publicationStreamStatus = publicationStreamStatus;
    }

    public Date getFeaturedUtilDate() {
        return featuredUtilDate;
    }

    public void setFeaturedUtilDate(Date featuredUtilDate) {
        this.featuredUtilDate = featuredUtilDate;
    }

}
