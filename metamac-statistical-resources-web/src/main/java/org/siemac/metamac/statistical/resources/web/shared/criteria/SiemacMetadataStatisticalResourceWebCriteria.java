package org.siemac.metamac.statistical.resources.web.shared.criteria;

import java.util.Date;

import org.siemac.metamac.statistical.resources.core.enume.domain.StreamMessageStatusEnum;

public class SiemacMetadataStatisticalResourceWebCriteria extends LifeCycleStatisticalResourceWebCriteria {

    private static final long       serialVersionUID = 1L;

    private String                  titleAlternative;
    private String                  keywords;
    private Date                    newnessUntilDate;
    private Date                    featuredUntilDate;
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

    public Date getNewnessUntilDate() {
        return newnessUntilDate;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void setNewnessUntilDate(Date newnessUntilDate) {
        this.newnessUntilDate = newnessUntilDate;
    }

    public StreamMessageStatusEnum getPublicationStreamStatus() {
        return publicationStreamStatus;
    }

    public void setPublicationStreamStatus(StreamMessageStatusEnum publicationStreamStatus) {
        this.publicationStreamStatus = publicationStreamStatus;
    }

    public Date getFeaturedUntilDate() {
        return featuredUntilDate;
    }

    public void setFeaturedUntilDate(Date featuredUntilDate) {
        this.featuredUntilDate = featuredUntilDate;
    }

}
