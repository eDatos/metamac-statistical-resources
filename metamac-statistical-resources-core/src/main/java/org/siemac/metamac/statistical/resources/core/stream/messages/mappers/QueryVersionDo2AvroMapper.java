package org.siemac.metamac.statistical.resources.core.stream.messages.mappers;

import static org.siemac.edatos.core.common.util.GeneratorUrnUtils.generateSiemacStatisticalResourceQueryUrn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersionRepository;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryStatusEnum;
import org.siemac.metamac.statistical.resources.core.enume.query.domain.QueryTypeEnum;
import org.siemac.metamac.statistical.resources.core.query.domain.CodeItem;
import org.siemac.metamac.statistical.resources.core.query.domain.QuerySelectionItem;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersionRepository;
import org.siemac.metamac.statistical.resources.core.stream.messages.CodeItemAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.QueryStatusEnumAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.QueryTypeEnumAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.QueryVersionAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.RelatedResourceAvro;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Component("queryVersionDo2AvroMapper")
public class QueryVersionDo2AvroMapper {

    @Autowired
    private DatasetVersionRepository datasetVersionRepository;

    @Autowired
    private QueryVersionRepository   queryVersionRepository;

    public QueryVersionAvro queryVersionDoToAvro(QueryVersion source) throws MetamacException {
        if (source == null) {
            return null;
        }

        DatasetVersion relatedDatasetVersion = getCurrentDatasetVersionInQuery(source);
        RelatedResourceAvro relatedDatasetVersionAvro = RelatedResourceDo2AvroMapper.lifecycleStatisticalResourceDoToRelatedResourceAvro(relatedDatasetVersion.getSiemacMetadataStatisticalResource(),
                TypeRelatedResourceEnum.DATASET_VERSION);

        // @formatter:off
        QueryVersionAvro target = QueryVersionAvro.newBuilder()
                .setLifecycleStatisticalResource(LifecycleStatisticalResourceDo2AvroMapper.do2Avro(source.getLifeCycleStatisticalResource()))
                .setLatestDataNumber(source.getLatestDataNumber())
                .setSelection(selectionToAvro(source.getSelection()))
                .setRelatedDatasetVersion(relatedDatasetVersionAvro)
                .setStatus(queryStatusEnumDoToAvro(source.getStatus()))
                .setQueryType(queryTypeEnumDoToAvro(source.getQueryType()))
                .setIsPartOf(AvroMapperUtils.relatedResourceList2Avro(queryVersionRepository.retrieveIsPartOf(source)))
                .build();
        // @formatter:on

        // Replace URN with URN withou Version
        String queryUrnWithoutVersion = toQueryUrn(source.getLifeCycleStatisticalResource().getMaintainer().getCodeNested(), source.getLifeCycleStatisticalResource().getCode());
        target.getLifecycleStatisticalResource().getVersionableStatisticalResource().getNameableStatisticalResource().getIdentifiableStatisticalResource().setUrn(queryUrnWithoutVersion);
        return target;
    }

    private String toQueryUrn(String maintainerNestedCode, String code) {
        return generateSiemacStatisticalResourceQueryUrn(new String[]{maintainerNestedCode}, code); // global urn without version
    }

    private QueryStatusEnumAvro queryStatusEnumDoToAvro(QueryStatusEnum source) {
        if (source == null) {
            return null;
        }

        QueryStatusEnumAvro target = QueryStatusEnumAvro.valueOf(source.getName());

        return target;
    }

    private QueryTypeEnumAvro queryTypeEnumDoToAvro(QueryTypeEnum source) {
        if (source == null) {
            return null;
        }

        QueryTypeEnumAvro target = QueryTypeEnumAvro.valueOf(source.getName());

        return target;
    }

    private DatasetVersion getCurrentDatasetVersionInQuery(QueryVersion queryVersion) throws MetamacException {
        if (queryVersion.getFixedDatasetVersion() != null) {
            return queryVersion.getFixedDatasetVersion();
        } else if (queryVersion.getDataset() != null) {
            return datasetVersionRepository.retrieveLastVersion(queryVersion.getDataset().getIdentifiableStatisticalResource().getUrn());
        }
        return null;
    }

    private Map<String, List<CodeItemAvro>> selectionToAvro(List<QuerySelectionItem> source) {
        Map<String, List<CodeItemAvro>> target = new HashMap<String, List<CodeItemAvro>>();

        for (QuerySelectionItem querySelectionItem : source) {
            List<CodeItemAvro> codesResult = new ArrayList<CodeItemAvro>();
            for (CodeItem codeItem : querySelectionItem.getCodes()) {
                codesResult.add(CodeItemAvro.newBuilder().setCode(codeItem.getCode()).setTitle(codeItem.getTitle()).build());
            }
            target.put(querySelectionItem.getDimension(), codesResult);
        }

        return target;
    }

}
