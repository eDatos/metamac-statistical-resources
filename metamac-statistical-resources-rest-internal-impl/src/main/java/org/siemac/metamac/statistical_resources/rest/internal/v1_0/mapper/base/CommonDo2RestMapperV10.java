package org.siemac.metamac.statistical_resources.rest.internal.v1_0.mapper.base;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Attributes;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Data;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.DataStructureDefinition;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Dimensions;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.NextVersionType;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.ProcStatusType;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.ResourcesInternal;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.SelectedLanguages;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.StatisticalResource;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResource;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceResult;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.TemporalCode;
import org.siemac.metamac.statistical.resources.core.enume.domain.NextVersionTypeEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.invocation.utils.InternalWebApplicationNavigation;
import org.siemac.metamac.statistical.resources.core.query.domain.CodeItem;
import org.siemac.metamac.statistical_resources.rest.internal.v1_0.domain.DsdProcessorResult;

public interface CommonDo2RestMapperV10 {

    public InternalWebApplicationNavigation getInternalWebApplicationNavigation();

    public DsdProcessorResult processDataStructure(String urn) throws MetamacException;
    public void toMetadataStatisticalResource(SiemacMetadataStatisticalResource source, StatisticalResource target, List<String> selectedLanguages) throws MetamacException;
    public Data toData(DatasetVersion source, DsdProcessorResult dsdProcessorResult, Map<String, List<String>> dimensionValuesSelected, List<String> selectedLanguages) throws Exception;
    public DataStructureDefinition toDataStructureDefinition(ExternalItem source, DataStructure dataStructure, List<String> selectedLanguages);
    public Dimensions toDimensions(String datasetVersionUrn, DsdProcessorResult dsdProcessorResult, Map<String, List<String>> effectiveDimensionValuesToDataByDimension, List<String> selectedLanguages,
            Set<String> fields) throws MetamacException;
    public Attributes toAttributes(String datasetVersionUrn, DsdProcessorResult dsdProcessorResult, List<String> selectedLanguages) throws MetamacException;

    public ResourceInternal toResource(ResourceInternal source, List<String> selectedLanguages);
    public void toResource(ResourceInternal source, ResourceInternal target, List<String> selectedLanguages);
    public ResourcesInternal toResources(List<RelatedResource> sources, List<String> selectedLanguages) throws MetamacException;
    public ResourceInternal toResource(RelatedResource source, List<String> selectedLanguages) throws MetamacException;
    public ResourceInternal toResource(RelatedResourceResult source, List<String> selectedLanguages) throws MetamacException;
    public ResourcesInternal toUrlResources(List<String> sources) throws MetamacException;

    public ResourceLink toResourceLink(String kind, String href);
    public String toResourceLink(String resourceSubpath, String agencyID, String resourceID, String version);

    public InternationalString toInternationalString(org.siemac.metamac.statistical.resources.core.common.domain.InternationalString sources, List<String> selectedLanguages);
    public InternationalString toInternationalString(InternationalString sources, List<String> selectedLanguages);
    public InternationalString toInternationalString(String source, List<String> selectedLanguages);
    public InternationalString toInternationalString(Map<String, String> sources, List<String> selectedLanguages);

    public Date toDate(DateTime source);

    public NextVersionType toNextVersionType(NextVersionTypeEnum source, List<String> selectedLanguages);

    public ProcStatusType toProcStatusType(ProcStatusEnum source, List<String> selectedLanguages);

    public SelectedLanguages toLanguages(List<String> selectedLanguages);

    public List<String> codeItemToString(List<CodeItem> sources);
    public List<String> temporalCoverageToString(List<TemporalCode> sources);

    public ResourcesInternal toResourcesExternalItemsSrm(Collection<ExternalItem> sources, List<String> selectedLanguages);
    public ResourceInternal toResourceExternalItemSrm(ExternalItem source, List<String> selectedLanguages);
    public void toResourceExternalItemSrm(ExternalItem source, ResourceInternal target, List<String> selectedLanguages);
    public ResourceInternal toResourceExternalItemStatisticalOperations(ExternalItem source, List<String> selectedLanguages);
    public ResourcesInternal toResourcesExternalItemsStatisticalOperations(List<ExternalItem> sources, List<String> selectedLanguages);
    public InternationalString toSdmxObservationalTimePeriod(String sdmxValue, List<String> selectedLanguages) throws MetamacException;

    public RestException buildRestException(String message);
}
