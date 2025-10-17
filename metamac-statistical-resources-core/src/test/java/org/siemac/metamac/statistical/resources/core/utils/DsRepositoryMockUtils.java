package org.siemac.metamac.statistical.resources.core.utils;

import es.gobcan.istac.edatos.dataset.repository.dto.CodeDimensionDto;
import es.gobcan.istac.edatos.dataset.repository.dto.ConditionObservationDto;
import es.gobcan.istac.edatos.dataset.repository.dto.DatasetRepositoryDto;
import es.gobcan.istac.edatos.dataset.repository.dto.DimensionDto;

public class DsRepositoryMockUtils {

    public static DatasetRepositoryDto mockDatasetRepository(String datasetId, String... dimensionIds) {
        DatasetRepositoryDto datasetRepo = new DatasetRepositoryDto();
        datasetRepo.setDatasetId(datasetId);
        for (String dimensionId : dimensionIds) {
            DimensionDto dimension = new DimensionDto();
            dimension.setDimensionId(dimensionId);
            dimension.setSourceUrn("urn:uuid:" + datasetId + ":" + dimensionId);
            datasetRepo.addDimension(dimension);

        }

        return datasetRepo;
    }

    public static ConditionObservationDto mockCodeDimensions(String dimensionId, String... codes) {
        ConditionObservationDto condition = new ConditionObservationDto();
        for (String code : codes) {
            condition.getCodesDimension().add(new CodeDimensionDto(dimensionId, code));
        }
        return condition;
    }
}
