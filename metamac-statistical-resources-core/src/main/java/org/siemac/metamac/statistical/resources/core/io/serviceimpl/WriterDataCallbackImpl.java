package org.siemac.metamac.statistical.resources.core.io.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.joda.time.DateTime;
import org.sdmx.resources.sdmxml.rest.schemas.v2_1.types.DataParameterDetailEnum;
import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.io.domain.AttributeInfo;
import org.siemac.metamac.statistical.resources.core.io.domain.AttributeInfo.AttachmentLevel;
import org.siemac.metamac.statistical.resources.core.io.domain.DatasetInfo;
import org.siemac.metamac.statistical.resources.core.io.domain.DsdSdmxInfo;
import org.siemac.metamac.statistical.resources.core.io.domain.DsdSdmxInfo.DsdSdmxExtractor;
import org.siemac.metamac.statistical.resources.core.io.domain.GroupInfo;
import org.siemac.metamac.statistical.resources.core.io.domain.RequestParameter;
import org.siemac.metamac.statistical.resources.core.io.mapper.MetamacSdmx2StatRepoMapper;
import org.siemac.metamac.statistical.resources.core.io.utils.ManipulateDataUtils;

import com.arte.statistic.parser.sdmx.v2_1.WriterDataCallback;
import com.arte.statistic.parser.sdmx.v2_1.constants.MappingConstants;
import com.arte.statistic.parser.sdmx.v2_1.domain.ComponentInfo;
import com.arte.statistic.parser.sdmx.v2_1.domain.DimensionCodeInfo;
import com.arte.statistic.parser.sdmx.v2_1.domain.Group;
import com.arte.statistic.parser.sdmx.v2_1.domain.Header;
import com.arte.statistic.parser.sdmx.v2_1.domain.IdValuePair;
import com.arte.statistic.parser.sdmx.v2_1.domain.Observation;
import com.arte.statistic.parser.sdmx.v2_1.domain.PayloadStructure;
import com.arte.statistic.parser.sdmx.v2_1.domain.Serie;
import com.arte.statistic.parser.sdmx.v2_1.domain.StructureReferenceBase;
import com.arte.statistic.parser.sdmx.v2_1.domain.TypeSDMXDataMessageEnum;
import com.arte.statistic.parser.sdmx.v2_1.mapper.DataSetDo2JaxbDomainMapper;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceBasicDto;
import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceDto;
import es.gobcan.istac.edatos.dataset.repository.dto.CodeDimensionDto;
import es.gobcan.istac.edatos.dataset.repository.dto.ObservationExtendedDto;
import es.gobcan.istac.edatos.dataset.repository.service.DatasetRepositoriesServiceFacade;

public class WriterDataCallbackImpl implements WriterDataCallback {

    private DatasetRepositoriesServiceFacade datasetRepositoriesServiceFacade = null;
    private MetamacSdmx2StatRepoMapper       metamac2StatRepoMapper           = null;
    private DsdSdmxExtractor                 dsdSdmxExtractor                 = null;

    // Calculated an cache data
    // --- For MessageContext
    private String                           queryKey                         = null;
    private String                           sender                           = null;
    private RequestParameter                 requestParameter                 = null;
    private TypeSDMXDataMessageEnum          typeofMessage                    = null;

    // --- For DatasetContext
    private List<DatasetInfo>                datasetInfoCache                 = null;
    private int                              currentDatasetVersionIndex       = -1;
    private Set<String>                      addedAttributesKey               = null;

    public WriterDataCallbackImpl(DatasetRepositoriesServiceFacade datasetRepositoriesServiceFacade, MetamacSdmx2StatRepoMapper metamac2StatRepoMapper, DsdSdmxExtractor dsdSdmxExtractor,
            PagedResult<DatasetVersion> datasetsToFetch, String querykey, RequestParameter requestParameter, String sender) throws Exception {
        this.datasetRepositoriesServiceFacade = datasetRepositoriesServiceFacade;
        this.metamac2StatRepoMapper = metamac2StatRepoMapper;
        queryKey = querykey;
        this.requestParameter = requestParameter;
        this.sender = sender;
        this.dsdSdmxExtractor = dsdSdmxExtractor;

        calculateCacheInfo(datasetsToFetch);
    }

    @Override
    public Header retrieveHeader() throws Exception {
        Header header = new Header();
        header.setId(generateMessageId());
        header.setTest(false);
        header.setPrepared(CoreCommonUtil.jodaDateTime2xsDateTime(new DateTime()));
        header.setSenderID(sender);

        // Structures
        for (DatasetInfo datasetInfo : getDatasetInfoCache()) {
            PayloadStructure payloadStructure = new PayloadStructure();

            payloadStructure.setDimensionAtObservation((datasetInfo.getDimensionAtObservation() == null) ? null : datasetInfo.getDimensionAtObservation().getCode());
            payloadStructure.setStructureID(datasetInfo.getDatasetVersion().getDatasetRepositoryId());

            String[] splitItemScheme = UrnUtils.splitUrnStructure(datasetInfo.getDatasetVersion().getRelatedDsd().getUrn());
            StructureReferenceBase structureReferenceBase = new StructureReferenceBase();
            structureReferenceBase.setAgency(splitItemScheme[0]);
            structureReferenceBase.setCode(splitItemScheme[1]);
            structureReferenceBase.setVersionLogic(splitItemScheme[2]);

            payloadStructure.setStructure(structureReferenceBase);

            if (TypeSDMXDataMessageEnum.SPECIFIC_2_1.equals(getTypeofMessage()) || TypeSDMXDataMessageEnum.SPECIFIC_TIME_SERIES_2_1.equals(getTypeofMessage())) {
                StringBuilder namespace = new StringBuilder(datasetInfo.getDatasetVersion().getRelatedDsd().getUrn());
                namespace.append(":ObsLevelDim:");
                namespace.append((datasetInfo.getDimensionAtObservation() == null) ? DataSetDo2JaxbDomainMapper.ALL_DIMENSIONS : datasetInfo.getDimensionAtObservation().getCode());

                payloadStructure.setNamespace(namespace.toString());
            }

            header.addStructure(payloadStructure);
        }

        return header;
    }

    @Override
    public Set<IdValuePair> retrieveAttributesAtDatasetLevel() throws Exception {
        Set<IdValuePair> attributes = new HashSet<IdValuePair>();

        List<AttributeInstanceDto> attributesFound = datasetRepositoriesServiceFacade.findAttributesInstancesWithDatasetAttachmentLevel(getCurrentDatasetVersion().getDatasetRepositoryId(), null);

        for (AttributeInstanceDto attributeDto : attributesFound) {
            attributes.add(new IdValuePair(attributeDto.getAttributeId(), attributeDto.getValue().getLocalisedLabel(StatisticalResourcesConstants.DEFAULT_DATA_REPOSITORY_LOCALE)));
        }

        return attributes;
    }

    @Override
    public Serie fecthSerie(List<DimensionCodeInfo> serieConditions) throws Exception {
        Serie serie = new Serie();

        // Key
        Pair<List<IdValuePair>, IdValuePair> keyParts = generateKeyPartsFromConditions(serieConditions);

        // The last element in list is the key of dimension at observation level, the other elements have only one key
        serie.getSeriesKey().addAll(keyParts.getLeft());

        // Add serie attributes
        if (!isSkipAttributeGeneration()) {
            List<Pair<String, IdValuePair>> attributes = extractAttributesForCurrentLevel(serie.getSeriesKey(), getCurrentDatasetInfo().getAttributeInstances());
            for (Pair<String, IdValuePair> attribute : attributes) {
                serie.getAttributes().add(attribute.getRight());
                addAttributeInstanceToAddedSet(attribute.getLeft());
            }
        }

        // Observations
        if (!isSkipData()) {
            Map<String, ObservationExtendedDto> observationsMap = datasetRepositoriesServiceFacade
                    .findObservationsExtendedByDimensions(getCurrentDatasetInfo().getDatasetVersion().getDatasetRepositoryId(), metamac2StatRepoMapper.conditionsToRepositoryList(serieConditions));

            for (Map.Entry<String, ObservationExtendedDto> entry : observationsMap.entrySet()) {
                serie.getObs().add(observationRepositoryToGroupedObservationWriter(entry.getValue(), serie.getSeriesKey(), keyParts.getRight(), true));
            }
        }

        return serie;
    }

    @Override
    public List<Observation> fetchUngroupedObservations(List<DimensionCodeInfo> serieConditions) throws Exception {
        List<Observation> observations = new LinkedList<Observation>();

        // Key
        Pair<List<IdValuePair>, IdValuePair> keyParts = generateKeyPartsFromConditions(serieConditions);

        // Observations
        if (!isSkipData()) {
            Map<String, ObservationExtendedDto> observationsMap = datasetRepositoriesServiceFacade
                    .findObservationsExtendedByDimensions(getCurrentDatasetInfo().getDatasetVersion().getDatasetRepositoryId(), metamac2StatRepoMapper.conditionsToRepositoryList(serieConditions));

            for (Map.Entry<String, ObservationExtendedDto> entry : observationsMap.entrySet()) {
                observations.add(observationRepositoryToGroupedObservationWriter(entry.getValue(), keyParts.getLeft(), keyParts.getRight(), false));
            }
        }

        return observations;
    }

    @Override
    public List<Group> fetchAttributesInGroups() throws Exception {
        List<Group> groups = new LinkedList<Group>();

        if (isSkipGroupGeneration()) {
            return groups;
        }

        Map<String, List<AttributeInstanceBasicDto>> normalizedAttributesMap = getCurrentDatasetInfo().getAttributeInstances();

        if (normalizedAttributesMap != null) {
            // For all groups definition: generate keys and add attributes
            for (GroupInfo groupInfo : getCurrentDatasetInfo().getDsdSdmxInfo().getGroups().values()) {
                buildGroups(groups, groupInfo, normalizedAttributesMap, new LinkedList<IdValuePair>(), 0);
            }
        }

        return groups;
    }

    @Override
    public List<DimensionCodeInfo> getQueryConditions() throws Exception {
        return getCurrentDatasetInfo().getConditions();
    }

    @Override
    public DimensionCodeInfo getDimensionAtObservation() throws Exception {
        return getCurrentDatasetInfo().getDimensionAtObservation();
    }

    @Override
    public DimensionCodeInfo getMeasureDimension() throws Exception {
        return getCurrentDatasetInfo().getMeasureDimension();
    }

    @Override
    public DimensionCodeInfo getTimeDimension() throws Exception {
        return getCurrentDatasetInfo().getTimeDimension();
    }

    @Override
    public boolean hasNextDataset() {
        if (currentDatasetVersionIndex >= datasetInfoCache.size() - 1) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String nextDataset() {
        if (currentDatasetVersionIndex >= datasetInfoCache.size() - 1) {
            throw new NoSuchElementException();
        }
        currentDatasetVersionIndex++;

        // Clean datasets context info
        addedAttributesKey = new HashSet<String>();

        return getCurrentDatasetVersion().getDatasetRepositoryId();
    }

    @Override
    public TypeSDMXDataMessageEnum getTypeofMessage() {
        return typeofMessage;
    }

    /**************************************************************************
     * PROCESSORS
     **************************************************************************/
    private void calculateCacheInfo(PagedResult<DatasetVersion> datasetsToFetch) throws Exception {

        // Build Data Structure cache
        Map<String, DsdSdmxInfo> dataStructureDefinitionCache = new HashMap<String, DsdSdmxInfo>();
        for (DatasetVersion datasetVersion : datasetsToFetch.getValues()) {
            if (!dataStructureDefinitionCache.containsKey(datasetVersion.getRelatedDsd().getUrn())) {
                dataStructureDefinitionCache.put(datasetVersion.getRelatedDsd().getUrn(), dsdSdmxExtractor.extractDsdInfo(datasetVersion.getRelatedDsd().getUrn()));
            }
        }

        // Build DatasetInfo cache
        boolean allDatasetsContainsTimeDimension = true;
        List<DatasetInfo> datasetInfoCache = new ArrayList<DatasetInfo>(datasetsToFetch.getValues().size());
        for (DatasetVersion datasetVersion : datasetsToFetch.getValues()) {
            DatasetInfo datasetInfo = new DatasetInfo(datasetRepositoriesServiceFacade, metamac2StatRepoMapper, queryKey, datasetVersion,
                    dataStructureDefinitionCache.get(datasetVersion.getRelatedDsd().getUrn()), getRequestParameter());
            datasetInfoCache.add(datasetInfo);

            if (datasetInfo.getTimeDimension() == null) {
                allDatasetsContainsTimeDimension = false;
            }
        }

        this.datasetInfoCache = datasetInfoCache;
        typeofMessage = determineTypeOfResponseMessage(allDatasetsContainsTimeDimension);
    }

    private TypeSDMXDataMessageEnum determineTypeOfResponseMessage(boolean allDatasetsContainsTimeDimension) throws Exception {
        TypeSDMXDataMessageEnum typeMessage = TypeSDMXDataMessageEnum.fromValue(getRequestParameter().getProposeContentTypeString());
        // No valid content request
        if (typeMessage == null) {
            if (allDatasetsContainsTimeDimension) {
                return TypeSDMXDataMessageEnum.GENERIC_TIME_SERIES_2_1; // By default if exists a time dimension as dimension at observation level
            } else {
                return TypeSDMXDataMessageEnum.GENERIC_2_1; // By default if exists a non time dimension as dimension at observation level
            }
        }

        // Check for valid request content type: If is a time series request
        if (TypeSDMXDataMessageEnum.SPECIFIC_TIME_SERIES_2_1.equals(typeMessage) || TypeSDMXDataMessageEnum.GENERIC_TIME_SERIES_2_1.equals(typeMessage)) {
            if (allDatasetsContainsTimeDimension) {
                return typeMessage;
            } else {
                return TypeSDMXDataMessageEnum.GENERIC_2_1; // Wrong request: By default if exists a non time dimension as dimension at observation level
            }
        }

        return typeMessage;
    }

    /**
     * Build all instances of group for the current query conditions
     *
     * @param instanceGroups
     * @param groupInfo
     * @param normalizedAttributesMap
     * @param observationkeys
     * @param numDimProcess
     */
    private void buildGroups(List<Group> instanceGroups, GroupInfo groupInfo, Map<String, List<AttributeInstanceBasicDto>> normalizedAttributesMap, List<IdValuePair> observationkeys,
            int numDimProcess) {

        // If the key of current group is fully generated
        if (numDimProcess == groupInfo.getDimensionsInfoList().size()) {
            List<Pair<String, IdValuePair>> attributes = extractAttributesForCurrentLevel(observationkeys, getCurrentDatasetInfo().getAttributeInstances());
            if (!attributes.isEmpty()) {
                // new instance of group
                Group group = new Group();
                // Keys
                group.getGroupKey().addAll(observationkeys);

                for (Pair<String, IdValuePair> attributePair : attributes) {
                    // Only attributes that can be in this group
                    AttributeInfo attributeInfo = getCurrentDatasetInfo().getDsdSdmxInfo().getAttributes().get(attributePair.getRight().getCode());
                    if (isValidAttributeForThisGroup(attributeInfo, groupInfo.getGroupId())) {
                        group.addAttribute(new IdValuePair(attributePair.getRight().getCode(), attributePair.getRight().getValue()));
                        addAttributeInstanceToAddedSet(attributePair.getLeft());
                    }
                }

                if (!group.getAttributes().isEmpty()) {
                    instanceGroups.add(group);
                }
            }
        } else {
            // Next dimension to process code
            ComponentInfo dimensionInfo = groupInfo.getDimensionsInfoList().get(numDimProcess);
            List<String> actualConditionsForCurrentDimension = getCurrentDatasetInfo().getConditionsMap().get(dimensionInfo.getCode());

            for (String code : actualConditionsForCurrentDimension) {
                // Auxiliary
                IdValuePair idValuePairAux = new IdValuePair(dimensionInfo.getCode(), code);

                List<IdValuePair> newObservationConditions = new ArrayList<IdValuePair>(observationkeys.size() + 1);
                newObservationConditions.addAll(observationkeys);
                newObservationConditions.add(idValuePairAux);

                buildGroups(instanceGroups, groupInfo, normalizedAttributesMap, newObservationConditions, numDimProcess + 1);
            }
        }
    }

    private List<Pair<String, IdValuePair>> extractAttributesForCurrentLevel(List<IdValuePair> keys, Map<String, List<AttributeInstanceBasicDto>> normalizedAttributesMap) {
        List<Pair<String, IdValuePair>> attributes = new LinkedList<Pair<String, IdValuePair>>();

        Stack<List<IdValuePair>> stack = new Stack<List<IdValuePair>>();
        stack.push(keys);
        while (!stack.isEmpty()) {
            List<IdValuePair> pop = stack.pop();

            // Check if exist a attribute that conforms with this key and add
            String key = ManipulateDataUtils.generateKeyFromIdValuePairs(pop);
            if (normalizedAttributesMap.containsKey(key)) {
                // Attributes
                Iterator<AttributeInstanceBasicDto> itAttributes = normalizedAttributesMap.get(key).iterator();
                while (itAttributes.hasNext()) {
                    IdValuePair attributeIdValuePair = ManipulateDataUtils.attributeInstanceBasicDto2IdValuePair(itAttributes.next());

                    String attributeKey = ManipulateDataUtils.generateKeyForAttribute(attributeIdValuePair.getCode(), key);
                    if (!getAddedAttributesKey().contains(attributeKey)) {
                        attributes.add(new ImmutablePair<String, IdValuePair>(attributeKey, attributeIdValuePair));
                    }
                }
            }

            // Build partial keys deleting one element
            for (int i = 0; i < pop.size(); i++) {
                Iterator<IdValuePair> itPop = pop.iterator();
                ArrayList<IdValuePair> subList = new ArrayList<IdValuePair>(pop.size() - 1);
                int j = 0;
                while (itPop.hasNext()) {
                    IdValuePair next = itPop.next();
                    if (i != j) {
                        subList.add(next);
                    }
                    j++;
                }
                if (!subList.isEmpty()) {
                    stack.push(subList); // To next iteration
                }
            }
        }

        return attributes;
    }

    private boolean isValidAttributeForThisGroup(AttributeInfo attributeInfo, String groupId) {
        if (attributeInfo != null) {
            if (AttachmentLevel.DIMENSION.equals(attributeInfo.getAttachmentLevel())) {
                for (GroupInfo groupInfo : attributeInfo.getAttachmentGroups()) {
                    if (groupId.equals(groupInfo.getGroupId())) {
                        return true;
                    }
                }
            } else {
                // GROUP
                if (groupId.equals(attributeInfo.getGroup().getGroupId())) {
                    return true;
                }
            }
        }
        return false;
    }

    private Observation observationRepositoryToGroupedObservationWriter(ObservationExtendedDto observation, List<IdValuePair> observationPartialKey, IdValuePair dimensionAtObservationKey,
            boolean isGroupedObservation) throws Exception {
        Observation result = new Observation();

        // Calculate Key
        List<IdValuePair> observationKeyFull = new LinkedList<IdValuePair>();
        observationKeyFull.addAll(observationPartialKey);

        if (isGroupedObservation) {
            IdValuePair dimensionCodeAtObservationKey = getDimensionAtObservationKey(dimensionAtObservationKey.getCode(), dimensionAtObservationKey.getValue(), observation);
            observationKeyFull.add(dimensionCodeAtObservationKey);
            result.getObservationKey().add(dimensionCodeAtObservationKey);
        } else {
            observationKeyFull.add(dimensionAtObservationKey);
            result.getObservationKey().addAll(observationKeyFull);
        }

        // Observation Value
        result.setObservationValue(new IdValuePair(MappingConstants.OBS_VALUE, observation.getPrimaryMeasure()));

        if (!isSkipAttributeGeneration()) {
            // Attribute add observation level
            for (AttributeInstanceBasicDto attributeBasicDto : observation.getAttributes()) {
                if (!StatisticalResourcesConstants.ATTRIBUTE_DATA_SOURCE_ID.equals(attributeBasicDto.getAttributeId())) {
                    result.addAttribute(
                            new IdValuePair(attributeBasicDto.getAttributeId(), attributeBasicDto.getValue().getLocalisedLabel(StatisticalResourcesConstants.DEFAULT_DATA_REPOSITORY_LOCALE)));
                }
            }

            // Other attributes that are in observation in this dataset
            List<Pair<String, IdValuePair>> attributes = extractAttributesForCurrentLevel(observationKeyFull, getCurrentDatasetInfo().getAttributeInstances());
            for (Pair<String, IdValuePair> attribute : attributes) {
                result.getAttributes().add(attribute.getRight());
                addAttributeInstanceToAddedSet(attribute.getLeft());
            }

        }

        return result;
    }

    private IdValuePair getDimensionAtObservationKey(String code, String defaultValue, ObservationExtendedDto observation) {

        for (CodeDimensionDto codeDimensionDto : observation.getCodesDimension()) {
            if (codeDimensionDto.getDimensionId().equals(code)) {
                return new IdValuePair(code, codeDimensionDto.getCodeDimensionId());
            }
        }

        return new IdValuePair(code, defaultValue);
    }

    private String generateMessageId() {
        StringBuilder stringBuilder = new StringBuilder("DATA_");
        stringBuilder.append(sender).append("_").append(UUID.randomUUID());
        return stringBuilder.toString();
    }

    private DatasetInfo getCurrentDatasetInfo() {
        return datasetInfoCache.get(currentDatasetVersionIndex);
    }

    private DatasetVersion getCurrentDatasetVersion() {
        return datasetInfoCache.get(currentDatasetVersionIndex).getDatasetVersion();
    }

    private List<DatasetInfo> getDatasetInfoCache() {
        return datasetInfoCache;
    }

    private RequestParameter getRequestParameter() {
        return requestParameter;
    }

    public Set<String> getAddedAttributesKey() {
        return addedAttributesKey;
    }

    private boolean isSkipAttributeGeneration() {
        // Skip attributes for details="dataonly" and details="serieskeyonly""
        if (DataParameterDetailEnum.DATAONLY.equals(getRequestParameter().getDetail()) || DataParameterDetailEnum.SERIESKEYSONLY.equals(getRequestParameter().getDetail())) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isSkipGroupGeneration() {
        // Skip group generation for details="dataonly" and details="serieskeyonly""
        if (DataParameterDetailEnum.DATAONLY.equals(getRequestParameter().getDetail()) || DataParameterDetailEnum.SERIESKEYSONLY.equals(getRequestParameter().getDetail())) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isSkipData() {
        // Skip group generation for details="dataonly" and details="serieskeyonly""
        if (DataParameterDetailEnum.NODATA.equals(getRequestParameter().getDetail()) || DataParameterDetailEnum.SERIESKEYSONLY.equals(getRequestParameter().getDetail())) {
            return true;
        } else {
            return false;
        }
    }

    private Pair<List<IdValuePair>, IdValuePair> generateKeyPartsFromConditions(List<DimensionCodeInfo> serieConditions) {
        int i = 0;
        List<IdValuePair> observationPartialKey = new LinkedList<IdValuePair>();
        Iterator<DimensionCodeInfo> itSerieCondition = serieConditions.iterator();
        while (i < serieConditions.size() - 1) {
            DimensionCodeInfo keyCondition = itSerieCondition.next();
            observationPartialKey.add(new IdValuePair(keyCondition.getCode(), keyCondition.getCodes().iterator().next()));
            i++;
        }
        DimensionCodeInfo keyCondition = itSerieCondition.next();
        IdValuePair dimensionAtObservationKey = new IdValuePair(keyCondition.getCode(), keyCondition.getCodes().iterator().next());

        return new ImmutablePair<List<IdValuePair>, IdValuePair>(observationPartialKey, dimensionAtObservationKey);
    }

    private void addAttributeInstanceToAddedSet(String key) {
        addedAttributesKey.add(key);
    }

}
