package org.siemac.metamac.rest.statistical_resources.v1_0.domain;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.ALWAYS)
public class JsonStatExtension {

    String datasetId;
    String datasetUrn;
    String lang;
    String survey;
    String publishers;
    String dataProviders;
    String dataProvidersAnnotations;

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public String getDatasetUrn() {
        return datasetUrn;
    }

    public void setDatasetUrn(String datasetUrn) {
        this.datasetUrn = datasetUrn;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getSurvey() {
        return survey;
    }

    public void setSurvey(String survey) {
        this.survey = survey;
    }

    public String getPublishers() {
        return publishers;
    }

    public void setPublishers(String publishers) {
        this.publishers = publishers;
    }

    public String getDataProviders() {
        return dataProviders;
    }

    public void setDataProviders(String dataProviders) {
        this.dataProviders = dataProviders;
    }

    public String getDataProvidersAnnotations() {
        return dataProvidersAnnotations;
    }

    public void setDataProvidersAnnotations(String dataProvidersAnnotations) {
        this.dataProvidersAnnotations = dataProvidersAnnotations;
    }

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).toString();
    }
}
