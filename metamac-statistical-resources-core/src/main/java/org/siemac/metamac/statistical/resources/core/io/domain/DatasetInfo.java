package org.siemac.metamac.statistical.resources.core.io.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ApplicationException;
import org.siemac.metamac.core.common.util.SdmxTimeUtils;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.io.domain.AttributeInfo.AttachmentLevel;
import org.siemac.metamac.statistical.resources.core.io.mapper.MetamacSdmx2StatRepoMapper;
import org.siemac.metamac.statistical.resources.core.io.utils.ManipulateDataUtils;

import com.arte.statistic.parser.sdmx.v2_1.domain.ComponentInfo;
import com.arte.statistic.parser.sdmx.v2_1.domain.ComponentInfoTypeEnum;
import com.arte.statistic.parser.sdmx.v2_1.domain.DimensionCodeInfo;
import com.arte.statistic.parser.sdmx.v2_1.domain.IdValuePair;
import com.arte.statistic.parser.sdmx.v2_1.mapper.DataSetDo2JaxbDomainMapper;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceBasicDto;
import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceDto;
import es.gobcan.istac.edatos.dataset.repository.dto.DatasetRepositoryDto;
import es.gobcan.istac.edatos.dataset.repository.service.DatasetRepositoriesServiceFacade;

public class DatasetInfo {

    private DatasetVersion                               datasetVersion         = null;
    private DimensionCodeInfo                            dimensionAtObservation = null;
    private DimensionCodeInfo                            timeDimension          = null;
    private DimensionCodeInfo                            measureDimension       = null;
    private Map<String, List<String>>                    coverage               = null;
    private List<DimensionCodeInfo>                      conditions             = null; // Input conditions for query
    private Map<String, List<String>>                    conditionsMap          = null; // Input conditions for query transformed into a map
    private Map<String, List<AttributeInstanceBasicDto>> attributeInstances     = null;
    private DsdSdmxInfo                                  dsdSdmxInfo            = null;
    private RequestParameter                             requestParameter       = null;

    public DatasetInfo(DatasetRepositoriesServiceFacade datasetRepositoriesServiceFacade, MetamacSdmx2StatRepoMapper metamac2StatRepoMapper, String queryKey, DatasetVersion datasetVersion,
            DsdSdmxInfo dsdSdmxInfo, RequestParameter requestParameter) throws Exception {

        this.datasetVersion = datasetVersion;
        this.dsdSdmxInfo = dsdSdmxInfo;
        this.requestParameter = requestParameter;

        DatasetRepositoryDto datasetRepository = datasetRepositoriesServiceFacade.retrieveDatasetRepository(datasetVersion.getDatasetRepositoryId());

        // 1- Calculate coverage
        coverage = calculateCoverage(datasetRepositoriesServiceFacade, datasetRepository);

        // 2- Calculate conditions
        conditions = calculateConditionsFromQuery(datasetRepository, queryKey);

        // 3- Calculate conditions map
        conditionsMap = metamac2StatRepoMapper.conditionsToRepositoryMap(conditions);

        // 4- Calculate MeasureDimension
        measureDimension = calculateMeasureDimension();

        // 5- Calculate TimeDimension
        timeDimension = calculateTimeDimension();

        // 6- Calculate DimensionAtObservation
        dimensionAtObservation = calculateDimensionAtObservation(requestParameter);

        // 7- Calculate attributeInstances
        attributeInstances = calculateAttributes(datasetRepositoriesServiceFacade);
    }

    public void setAttributeInstances(Map<String, List<AttributeInstanceBasicDto>> attributeInstances) {
        this.attributeInstances = attributeInstances;
    }

    public Map<String, List<AttributeInstanceBasicDto>> getAttributeInstances() {
        return attributeInstances;
    }

    public DatasetVersion getDatasetVersion() {
        return datasetVersion;
    }

    public Map<String, List<String>> getCoverage() {
        return coverage;
    }

    public List<DimensionCodeInfo> getConditions() {
        return conditions;
    }

    public Map<String, List<String>> getConditionsMap() {
        return conditionsMap;
    }

    public DimensionCodeInfo getTimeDimension() {
        return timeDimension;
    }

    public DimensionCodeInfo getMeasureDimension() {
        return measureDimension;
    }

    public DimensionCodeInfo getDimensionAtObservation() {
        return dimensionAtObservation;
    }

    public DsdSdmxInfo getDsdSdmxInfo() {
        return dsdSdmxInfo;
    }

    public RequestParameter getRequestParameter() {
        return requestParameter;
    }

    private List<DimensionCodeInfo> calculateConditionsFromQuery(DatasetRepositoryDto datasetRepository, String queryKey) throws ApplicationException {
        // The key are formed by dimension codes splitted by dots
        // Wildcarding is supported by omitting the dimension code for the dimension.
        // The OR operator is supported using the '+' character.
        // Examples: Normal key -> D.USD.EUR.SP00.A
        // Examples: Wildcard key -> D..EUR.SP00.A
        // Examples: Wildcard key -> D.USD+CHF.EUR.SP00.A
        List<DimensionCodeInfo> conditions = new LinkedList<DimensionCodeInfo>();
        if (StringUtils.isEmpty(queryKey)) {
            // If is a empty query, then all observations are needed
            for (int i = 0; i < datasetRepository.getDimensions().size(); i++) {
                addAllCodesConditionForDimension(datasetRepository, i, conditions);
            }
        } else {
            // Split the key by dots
            String str = queryKey.replaceAll("\\.", ". ");
            String[] split = str.split("\\.");
            for (int i = 0; i < split.length; i++) {
                split[i] = (StringUtils.isBlank(split[i]) ? null : split[i].trim());

                if (StringUtils.isEmpty(split[i])) {
                    // If is a wildcard dimension, all dimension codes are needed
                    addAllCodesConditionForDimension(datasetRepository, i, conditions);
                } else {
                    DimensionCodeInfo dimensionCodeInfo = new DimensionCodeInfo(datasetRepository.getDimensions().get(i).getDimensionId(),
                            getDsdSdmxInfo().getDimensions().get(datasetRepository.getDimensions().get(i).getDimensionId()).getTypeComponentInfo());
                    // Split the code by '+' to find OR operators for codes
                    String[] codes = split[i].split("\\+");
                    for (int j = 0; j < codes.length; j++) {
                        dimensionCodeInfo.addCode(codes[j]);
                    }
                    conditions.add(applyStartPeriodAndEndPeriodParameterFilter(dimensionCodeInfo));
                }
            }
        }

        return conditions;
    }

    private void addAllCodesConditionForDimension(DatasetRepositoryDto datasetRepository, int dimensionOrder, List<DimensionCodeInfo> conditions) throws ApplicationException {
        String dimensionID = datasetRepository.getDimensions().get(dimensionOrder).getDimensionId();
        DimensionCodeInfo dimensionCodeInfo = new DimensionCodeInfo(dimensionID, getDsdSdmxInfo().getDimensions().get(dimensionID).getTypeComponentInfo());
        dimensionCodeInfo.getCodes().addAll(getCoverage().get(dimensionID));

        conditions.add(applyStartPeriodAndEndPeriodParameterFilter(dimensionCodeInfo));
    }

    private DimensionCodeInfo applyStartPeriodAndEndPeriodParameterFilter(DimensionCodeInfo dimensionCodeInfo) {
        // Add filter startPeriod and endPeriod parameters
        if (ComponentInfoTypeEnum.TIME_DIMENSION.equals(dimensionCodeInfo.getTypeComponentInfo())) {
            Iterator<String> itCodes = dimensionCodeInfo.getCodes().iterator();
            while (itCodes.hasNext()) {
                String codeTemporal = itCodes.next();
                // Remove invalid temporal codes for this request
                if (!SdmxTimeUtils.isValidTimeInInterval(codeTemporal, getRequestParameter().getStartPeriod(), getRequestParameter().getEndPeriod())) {
                    itCodes.remove();
                }
            }
        }
        return dimensionCodeInfo;
    }

    private Map<String, List<String>> calculateCoverage(DatasetRepositoriesServiceFacade datasetRepositoriesServiceFacade, DatasetRepositoryDto datasetRepositoryDto) throws ApplicationException {
        return datasetRepositoriesServiceFacade.findCodeDimensions(datasetRepositoryDto.getDatasetId());
    }

    private DimensionCodeInfo calculateMeasureDimension() throws Exception {
        for (DimensionCodeInfo dimensionCodeInfo : getConditions()) {
            if (ComponentInfoTypeEnum.MEASURE_DIMENSION.equals(dimensionCodeInfo.getTypeComponentInfo())) {
                return dimensionCodeInfo;
            }
        }
        return null;
    }

    private DimensionCodeInfo calculateTimeDimension() throws Exception {
        for (DimensionCodeInfo dimensionCodeInfo : getConditions()) {
            if (ComponentInfoTypeEnum.TIME_DIMENSION.equals(dimensionCodeInfo.getTypeComponentInfo())) {
                return dimensionCodeInfo;
            }
        }
        return null;
    }

    private DimensionCodeInfo calculateDimensionAtObservation(RequestParameter requestParameter) throws Exception {
        if (StringUtils.isNotBlank(requestParameter.getDimensionAtObservation())) {
            if (DataSetDo2JaxbDomainMapper.ALL_DIMENSIONS.toLowerCase().equals(requestParameter.getDimensionAtObservation().toLowerCase())) {
                return null; // All dimensions
            }
            for (DimensionCodeInfo dimensionCodeInfo : getConditions()) {
                if (dimensionCodeInfo.getCode().equals(requestParameter.getDimensionAtObservation())) {
                    return dimensionCodeInfo;
                }
            }
            return null; // All dimensions
        } else {
            DimensionCodeInfo timeDimension = getTimeDimension();
            if (timeDimension != null) {
                return timeDimension;
            } else {
                List<DimensionCodeInfo> queryConditions = getConditions();
                int maxValue = -1;
                DimensionCodeInfo currentDimensionAtObservation = null;
                for (DimensionCodeInfo dimensionCodeInfo : queryConditions) {
                    if (dimensionCodeInfo.getCodes().size() > 0 && dimensionCodeInfo.getCodes().size() > maxValue) {
                        maxValue = dimensionCodeInfo.getCodes().size();
                        currentDimensionAtObservation = dimensionCodeInfo;
                    }
                }
                return currentDimensionAtObservation;
            }
        }
    }

    private Map<String, List<AttributeInstanceBasicDto>> calculateAttributes(DatasetRepositoriesServiceFacade datasetRepositoriesServiceFacade) throws Exception {
        Map<String, List<AttributeInstanceBasicDto>> normalizedAttributesMap = new HashMap<String, List<AttributeInstanceBasicDto>>(); // Generate Map of attributes by key

        // For all attributes that can be on the message group element
        Iterator<AttributeInfo> it = getDsdSdmxInfo().getAttributes().values().iterator();
        while (it.hasNext()) {
            AttributeInfo attributeInfo = it.next();
            if (AttachmentLevel.DIMENSION.equals(attributeInfo.getAttachmentLevel()) || AttachmentLevel.GROUP.equals(attributeInfo.getAttachmentLevel())) {
                List<AttributeInstanceDto> attributes = datasetRepositoriesServiceFacade.findAttributesInstancesWithDimensionAttachmentLevelDenormalized(getDatasetVersion().getDatasetRepositoryId(),
                        attributeInfo.getAttributeId(), getConditionsMap());
                if (CollectionUtils.isEmpty(attributes)) {
                    continue;
                }

                if (AttachmentLevel.DIMENSION.equals(attributeInfo.getAttachmentLevel())) {
                    addDenormalizedAttributesToCurrentNormalizedAttributes(normalizedAttributesMap, attributes, attributeInfo.getDimensionsInfoList());
                } else {
                    // Group
                    addDenormalizedAttributesToCurrentNormalizedAttributes(normalizedAttributesMap, attributes, attributeInfo.getGroup().getDimensionsInfoList());
                }
            }
        }
        return normalizedAttributesMap;
    }

    private void addDenormalizedAttributesToCurrentNormalizedAttributes(Map<String, List<AttributeInstanceBasicDto>> normalizedAttributesMap, List<AttributeInstanceDto> attributeInstances,
            List<ComponentInfo> attributeDimensionsOrdered) {

        for (AttributeInstanceDto attributeInstanceDto : attributeInstances) {
            List<IdValuePair> keyList = new ArrayList<IdValuePair>();
            for (ComponentInfo dimension : attributeDimensionsOrdered) {
                keyList.add(new IdValuePair(dimension.getCode(), attributeInstanceDto.getCodesByDimension().get(dimension.getCode()).get(0)));
            }

            // Add current attribute to this key in the map
            String key = ManipulateDataUtils.generateKeyFromIdValuePairs(keyList);
            if (normalizedAttributesMap.containsKey(key)) {
                normalizedAttributesMap.get(key).add(attributeInstanceDto);
            } else {
                List<AttributeInstanceBasicDto> attributeDtos = new LinkedList<AttributeInstanceBasicDto>();
                attributeDtos.add(attributeInstanceDto);
                normalizedAttributesMap.put(key, attributeDtos);
            }
        }
    }
}
