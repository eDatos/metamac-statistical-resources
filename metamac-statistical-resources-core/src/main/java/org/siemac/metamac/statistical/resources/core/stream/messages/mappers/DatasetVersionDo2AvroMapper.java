package org.siemac.metamac.statistical.resources.core.stream.messages.mappers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResource;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResourceResult;
import org.siemac.metamac.statistical.resources.core.dataset.domain.AttributeValue;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Categorisation;
import org.siemac.metamac.statistical.resources.core.dataset.domain.CodeDimension;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.TemporalCode;
import org.siemac.metamac.statistical.resources.core.stream.messages.AttributeValueAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.CategorisationAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.CodeDimensionAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetVersionAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.ExternalItemAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.RelatedResourceAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.TemporalCodeAvro;
import org.siemac.metamac.statistical.resources.core.utils.shared.MetamacPortalWebUtils;

public class DatasetVersionDo2AvroMapper {

    protected DatasetVersionDo2AvroMapper() {
    }

    public static DatasetVersionAvro do2Avro(DatasetVersion source, String urlBaseExternalVisualizer) throws MetamacException {
        List<CodeDimensionAvro> dimensions = dimensionsCoverage2Avro(source);
        List<AttributeValueAvro> coverageList = attributesCoverage2Avro(source);
        List<CategorisationAvro> categorisations = categorisations2Avro(source);
        List<ExternalItemAvro> geographicCoverageList = geographicCoverageList2Avro(source);
        List<TemporalCodeAvro> temporalCoverageList = temporalCoverageList2Avro(source);
        List<ExternalItemAvro> measureCoverageList = measureCoverageList2Avro(source);
        List<ExternalItemAvro> geoGranList = geoGranularitesList2Avro(source);
        List<ExternalItemAvro> temporalGranList = tempGranularitesList2Avro(source);
        List<ExternalItemAvro> statisticalUnitList = statisticalUnitList2Avro(source);

        DatasetVersionAvro target = DatasetVersionAvro.newBuilder()
                .setSiemacMetadataStatisticalResource(SiemacMetadataStatisticalResourceDo2AvroMapper.do2Avro(source.getSiemacMetadataStatisticalResource()))
                .setDateStart(AvroMapperUtils.toSdmxObservationalTimePeriod(source.getDateStart())).setDateEnd(AvroMapperUtils.toSdmxObservationalTimePeriod(source.getDateEnd()))
                .setDatasetRepositoryId(source.getDatasetRepositoryId()).setFormatExtentDimensions(source.getFormatExtentDimensions())
                .setDateNextUpdate(AvroMapperUtils.toSdmxObservationalTimePeriod(source.getDateNextUpdate())).setUserModifiedDateNextUpdate(source.getUserModifiedDateNextUpdate())
                .setDataset(DatasetDo2AvroMapper.do2Avro(source.getDataset())).setRelatedDsd(ExternalItemDo2AvroMapper.do2Avro(source.getRelatedDsd()))
                .setUpdateFrequency(ExternalItemDo2AvroMapper.do2Avro(source.getUpdateFrequency())).setStatisticOfficiality(StatisticOfficialityDo2AvroMapper.do2Avro(source.getStatisticOfficiality()))
                .setBibliographicCitation(InternationalStringDo2AvroMapper.do2Avro(source.getBibliographicCitation())).setDimensionsCoverage(dimensions).setAttributesCoverage(coverageList)
                .setCategorisations(categorisations).setGeographicCoverage(geographicCoverageList).setTemporalCoverage(temporalCoverageList).setMeasureCoverage(measureCoverageList)
                .setGeographicGranularities(geoGranList).setTemporalGranularities(temporalGranList).setStatisticalUnit(statisticalUnitList)
                .setIsPartOf(relatedResourceList2Avro(AvroMapperUtils.getDatasetVersionRepository().retrieveIsPartOf(source)))
                .setVisualizerHtmlLink(getDatasetVersionVisualizerHtmlLink(source, urlBaseExternalVisualizer)).setFormatExtentObservations(source.getFormatExtentObservations()).build();
        return target;
    }

    private static String getDatasetVersionVisualizerHtmlLink(DatasetVersion source, String urlBaseExternalVisualizer) {

        String htmlLink = MetamacPortalWebUtils.buildDatasetVersionUrl(source.getLifeCycleStatisticalResource().getMaintainer().getCode(), source.getLifeCycleStatisticalResource().getCode(),
                source.getLifeCycleStatisticalResource().getVersionLogic(), urlBaseExternalVisualizer);
        return htmlLink;
    }

    private static List<RelatedResourceAvro> relatedResourceList2Avro(List<RelatedResourceResult> sourceList) throws MetamacException {
        List<RelatedResourceAvro> targetList = new ArrayList<RelatedResourceAvro>();
        for (RelatedResourceResult item : sourceList) {
            RelatedResource relatedResource = AvroMapperUtils.createRelatedResourceFromRelatedResourceResult(item);
            targetList.add(RelatedResourceDo2AvroMapper.do2Avro(relatedResource));
        }
        return targetList;
    }

    protected static List<ExternalItemAvro> genericExternalItemList2Avro(Collection<ExternalItem> source) {
        List<ExternalItemAvro> measureCoverageList = new ArrayList<ExternalItemAvro>();
        for (ExternalItem externalItem : source) {
            ExternalItemAvro externalItemAvro = ExternalItemDo2AvroMapper.do2Avro(externalItem);
            measureCoverageList.add(externalItemAvro);
        }
        return measureCoverageList;
    }

    protected static List<ExternalItemAvro> geoGranularitesList2Avro(DatasetVersion source) {
        return genericExternalItemList2Avro(source.getGeographicGranularities());
    }

    protected static List<ExternalItemAvro> tempGranularitesList2Avro(DatasetVersion source) {
        return genericExternalItemList2Avro(source.getTemporalGranularities());
    }

    protected static List<ExternalItemAvro> statisticalUnitList2Avro(DatasetVersion source) {
        return genericExternalItemList2Avro(source.getStatisticalUnit());
    }

    protected static List<ExternalItemAvro> measureCoverageList2Avro(DatasetVersion source) {
        return genericExternalItemList2Avro(source.getMeasureCoverage());
    }

    protected static List<TemporalCodeAvro> temporalCoverageList2Avro(DatasetVersion source) {
        List<TemporalCodeAvro> temporalCoverageList = new ArrayList<TemporalCodeAvro>();
        for (TemporalCode temporalCode : source.getTemporalCoverage()) {
            TemporalCodeAvro temporalCodeAvro = TemporalCodeDo2AvroMapper.do2Avro(temporalCode);
            temporalCoverageList.add(temporalCodeAvro);
        }
        return temporalCoverageList;
    }

    protected static List<ExternalItemAvro> geographicCoverageList2Avro(DatasetVersion source) {
        return genericExternalItemList2Avro(source.getGeographicCoverage());
    }

    protected static List<AttributeValueAvro> attributesCoverage2Avro(DatasetVersion source) {
        List<AttributeValueAvro> coverageList = new ArrayList<AttributeValueAvro>();
        for (AttributeValue attribute : source.getAttributesCoverage()) {
            AttributeValueAvro attributeAvro = AttributeValueDo2AvroMapper.do2Avro(attribute);
            coverageList.add(attributeAvro);
        }
        return coverageList;
    }

    protected static List<CodeDimensionAvro> dimensionsCoverage2Avro(DatasetVersion source) {
        List<CodeDimensionAvro> dimensions = new ArrayList<CodeDimensionAvro>();
        for (CodeDimension dimension : source.getDimensionsCoverage()) {
            CodeDimensionAvro dimensionAvro = CodeDimensionDo2AvroMapper.do2Avro(dimension);
            dimensions.add(dimensionAvro);
        }
        return dimensions;
    }

    protected static List<CategorisationAvro> categorisations2Avro(DatasetVersion source) throws MetamacException {
        List<CategorisationAvro> categorisations = new ArrayList<CategorisationAvro>();
        for (Categorisation categorisation : source.getCategorisations()) {
            CategorisationAvro categorisationAvro = CategorisationDo2AvroMapper.do2Avro(categorisation);
            categorisations.add(categorisationAvro);
        }
        return categorisations;
    }
}
