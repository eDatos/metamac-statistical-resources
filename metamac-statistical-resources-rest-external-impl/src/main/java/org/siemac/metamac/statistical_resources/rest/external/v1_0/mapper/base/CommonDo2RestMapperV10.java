package org.siemac.metamac.statistical_resources.rest.external.v1_0.mapper.base;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.common.v1_0.domain.Resources;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Attributes;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Data;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.DataStructureDefinition;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Dimensions;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.NextVersionType;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.SelectedLanguages;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.StatisticalResource;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.DataStructure;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResource;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceResult;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.TemporalCode;
import org.siemac.metamac.statistical.resources.core.enume.domain.NextVersionTypeEnum;
import org.siemac.metamac.statistical.resources.core.query.domain.CodeItem;
import org.siemac.metamac.statistical_resources.rest.external.v1_0.domain.DsdProcessorResult;

public interface CommonDo2RestMapperV10 {

    public DsdProcessorResult processDataStructure(String urn) throws MetamacException;
    public void toMetadataStatisticalResource(SiemacMetadataStatisticalResource source, StatisticalResource target, List<String> selectedLanguages) throws MetamacException;
    public Data toData(DatasetVersion source, DsdProcessorResult dsdProcessorResult, Map<String, List<String>> dimensionValuesSelected, List<String> selectedLanguages) throws Exception;
    public DataStructureDefinition toDataStructureDefinition(ExternalItem source, DataStructure dataStructure, List<String> selectedLanguages);
    public Dimensions toDimensions(String datasetVersionUrn, DsdProcessorResult dsdProcessorResult, Map<String, List<String>> effectiveDimensionValuesToDataByDimension, List<String> selectedLanguages,
            Set<String> fields) throws MetamacException;
    public Attributes toAttributes(String datasetVersionUrn, DsdProcessorResult dsdProcessorResult, List<String> selectedLanguages) throws MetamacException;

    public Resource toResource(Resource source, List<String> selectedLanguages);
    public void toResource(Resource source, Resource target, List<String> selectedLanguages);
    public Resources toResources(List<RelatedResource> sources, List<String> selectedLanguages) throws MetamacException;
    public Resource toResource(RelatedResource source, List<String> selectedLanguages) throws MetamacException;
    public Resource toResource(RelatedResourceResult source, List<String> selectedLanguages) throws MetamacException;

    public ResourceLink toResourceLink(String kind, String href);
    public String toResourceLink(String resourceSubpath, String agencyID, String resourceID, String version);

    public String toI18nValue(InternationalString source, String selectedLanguage);
    public String toI18nValue(org.siemac.metamac.statistical.resources.core.common.domain.InternationalString source,String selectedLanguage);

    public InternationalString toInternationalString(org.siemac.metamac.statistical.resources.core.common.domain.InternationalString sources, List<String> selectedLanguages);
    public InternationalString toInternationalString(InternationalString sources, List<String> selectedLanguages);
    public InternationalString toInternationalString(String source, List<String> selectedLanguages);
    public InternationalString toInternationalString(Map<String, String> sources, List<String> selectedLanguages);

    public Date toDate(DateTime source);

    public NextVersionType toNextVersionType(NextVersionTypeEnum source, List<String> selectedLanguages);

    public SelectedLanguages toLanguages(List<String> selectedLanguages);

    public List<String> codeItemToString(List<CodeItem> sources);
    public List<String> temporalCoverageToString(List<TemporalCode> sources);

    public Resources toResourcesExternalItemsSrm(Collection<ExternalItem> sources, List<String> selectedLanguages);
    public Resource toResourceExternalItemSrm(ExternalItem source, List<String> selectedLanguages);
    public void toResourceExternalItemSrm(ExternalItem source, Resource target, List<String> selectedLanguages);
    public Resource toResourceExternalItemStatisticalOperations(ExternalItem source, List<String> selectedLanguages);
    public Resources toResourcesExternalItemsStatisticalOperations(List<ExternalItem> sources, List<String> selectedLanguages);
    public InternationalString toSdmxObservationalTimePeriod(String sdmxValue, List<String> selectedLanguages) throws MetamacException;

    public RestException buildRestException(String message);
}
