package org.siemac.metamac.sdmx.data.rest.external.v2_1.utils;

import static org.junit.Assert.assertNotNull;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;
import org.sdmx.resources.sdmxml.schemas.v2_1.common.CodelistReferenceType;
import org.sdmx.resources.sdmxml.schemas.v2_1.common.ConceptReferenceType;
import org.sdmx.resources.sdmxml.schemas.v2_1.common.DimensionTypeType;
import org.sdmx.resources.sdmxml.schemas.v2_1.common.SimpleDataType;
import org.sdmx.resources.sdmxml.schemas.v2_1.common.TimeDataType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.AttributeListType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.AttributeRelationshipType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.AttributeType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.DataStructureComponentsType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.DataStructureType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.DimensionListType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.DimensionType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.GroupDimensionType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.GroupType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.MeasureListType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.PrimaryMeasureType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.SimpleComponentTextFormatType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.SimpleDataStructureRepresentationType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.TimeDimensionRepresentationType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.TimeDimensionType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.TimeTextFormatType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.UsageStatusType;
import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.statistical.resources.core.base.domain.IdentifiableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.LifeCycleStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.NameableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.StatisticalResource;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionRationaleType;
import org.siemac.metamac.statistical.resources.core.base.domain.VersionableStatisticalResource;
import org.siemac.metamac.statistical.resources.core.common.domain.InternationalString;
import org.siemac.metamac.statistical.resources.core.common.domain.LocalisedString;
import org.siemac.metamac.statistical.resources.core.common.domain.RelatedResource;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Categorisation;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.TemporalCode;
import org.siemac.metamac.statistical.resources.core.enume.domain.TypeRelatedResourceEnum;
import org.siemac.metamac.statistical.resources.core.enume.domain.VersionRationaleTypeEnum;
import org.siemac.metamac.statistical.resources.core.utils.mocks.factories.StatisticalResourcesMockFactory;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesDoMocks;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesPersistedDoMocks;

import com.arte.statistic.parser.sdmx.v2_1.domain.IdValuePair;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceDto;
import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceObservationDto;
import es.gobcan.istac.edatos.dataset.repository.dto.CodeDimensionDto;
import es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto;
import es.gobcan.istac.edatos.dataset.repository.dto.LocalisedStringDto;
import es.gobcan.istac.edatos.dataset.repository.dto.ObservationExtendedDto;

public class SdmxDataCoreMocks extends BaseJaxbMocks {

    public StatisticalResourcesPersistedDoMocks coreDoMocks;

    public SdmxDataCoreMocks(StatisticalResourcesPersistedDoMocks coreDoMocks) {
        this.coreDoMocks = coreDoMocks;
    }

    public static Map<String, ObservationExtendedDto> mockObservations() {

        Map<String, ObservationExtendedDto> observationsMap = new HashMap<String, ObservationExtendedDto>();

        // OBS: 2010-08#M#EUR#SP00#E#CHF
        {
            ObservationExtendedDto observationExtendedDto = new ObservationExtendedDto();
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("FREQ", "M"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("CURRENCY", "CHF"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("CURRENCY_DENOM", "EUR"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("EXR_TYPE", "SP00"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("EXR_VAR", "E"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("TIME_PERIOD", "2010-08"));

            observationExtendedDto.addAttribute(createAttributeInstanceObservationDto("OBS_STATUS", "A"));
            observationExtendedDto.addAttribute(createAttributeInstanceObservationDto("CONF_STATUS_OBS", "F"));

            observationExtendedDto.setPrimaryMeasure("1.3413");
            observationsMap.put(observationExtendedDto.getUniqueKey(), observationExtendedDto);
        }

        // OBS: 2010-08#M#EUR#SP00#E#JPY
        {
            ObservationExtendedDto observationExtendedDto = new ObservationExtendedDto();
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("FREQ", "M"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("CURRENCY", "JPY"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("CURRENCY_DENOM", "EUR"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("EXR_TYPE", "SP00"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("EXR_VAR", "E"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("TIME_PERIOD", "2010-08"));

            observationExtendedDto.addAttribute(createAttributeInstanceObservationDto("OBS_STATUS", "A"));
            observationExtendedDto.addAttribute(createAttributeInstanceObservationDto("CONF_STATUS_OBS", "F"));

            observationExtendedDto.setPrimaryMeasure("110.04");
            observationsMap.put(observationExtendedDto.getUniqueKey(), observationExtendedDto);
        }

        // OBS: 2010-08#M#EUR#SP00#E#GBP
        {
            ObservationExtendedDto observationExtendedDto = new ObservationExtendedDto();
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("FREQ", "M"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("CURRENCY", "GBP"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("CURRENCY_DENOM", "EUR"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("EXR_TYPE", "SP00"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("EXR_VAR", "E"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("TIME_PERIOD", "2010-08"));

            observationExtendedDto.addAttribute(createAttributeInstanceObservationDto("OBS_STATUS", "A"));
            observationExtendedDto.addAttribute(createAttributeInstanceObservationDto("CONF_STATUS_OBS", "F"));

            observationExtendedDto.setPrimaryMeasure("0.82363");
            observationsMap.put(observationExtendedDto.getUniqueKey(), observationExtendedDto);
        }

        // OBS: 2010-08#M#EUR#SP00#E#USD
        {
            ObservationExtendedDto observationExtendedDto = new ObservationExtendedDto();
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("FREQ", "M"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("CURRENCY", "USD"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("CURRENCY_DENOM", "EUR"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("EXR_TYPE", "SP00"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("EXR_VAR", "E"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("TIME_PERIOD", "2010-08"));

            observationExtendedDto.addAttribute(createAttributeInstanceObservationDto("OBS_STATUS", "A"));
            observationExtendedDto.addAttribute(createAttributeInstanceObservationDto("CONF_STATUS_OBS", "F"));

            observationExtendedDto.setPrimaryMeasure("1.2894");
            observationsMap.put(observationExtendedDto.getUniqueKey(), observationExtendedDto);
        }

        // OBS: 2010-09#M#EUR#SP00#E#CHF
        {
            ObservationExtendedDto observationExtendedDto = new ObservationExtendedDto();
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("FREQ", "M"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("CURRENCY", "CHF"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("CURRENCY_DENOM", "EUR"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("EXR_TYPE", "SP00"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("EXR_VAR", "E"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("TIME_PERIOD", "2010-09"));

            observationExtendedDto.addAttribute(createAttributeInstanceObservationDto("OBS_STATUS", "A"));
            observationExtendedDto.addAttribute(createAttributeInstanceObservationDto("CONF_STATUS_OBS", "F"));

            observationExtendedDto.setPrimaryMeasure("1.3089");
            observationsMap.put(observationExtendedDto.getUniqueKey(), observationExtendedDto);
        }

        // OBS: 2010-09#M#EUR#SP00#E#GBP
        {
            ObservationExtendedDto observationExtendedDto = new ObservationExtendedDto();
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("FREQ", "M"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("CURRENCY", "GBP"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("CURRENCY_DENOM", "EUR"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("EXR_TYPE", "SP00"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("EXR_VAR", "E"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("TIME_PERIOD", "2010-09"));

            observationExtendedDto.addAttribute(createAttributeInstanceObservationDto("OBS_STATUS", "A"));
            observationExtendedDto.addAttribute(createAttributeInstanceObservationDto("CONF_STATUS_OBS", "F"));

            observationExtendedDto.setPrimaryMeasure("0.83987");
            observationsMap.put(observationExtendedDto.getUniqueKey(), observationExtendedDto);
        }

        // OBS: 2010-09#M#EUR#SP00#E#JPY
        {
            ObservationExtendedDto observationExtendedDto = new ObservationExtendedDto();
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("FREQ", "M"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("CURRENCY", "JPY"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("CURRENCY_DENOM", "EUR"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("EXR_TYPE", "SP00"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("EXR_VAR", "E"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("TIME_PERIOD", "2010-09"));

            observationExtendedDto.addAttribute(createAttributeInstanceObservationDto("OBS_STATUS", "A"));
            observationExtendedDto.addAttribute(createAttributeInstanceObservationDto("CONF_STATUS_OBS", "F"));

            observationExtendedDto.setPrimaryMeasure("110.26");
            observationsMap.put(observationExtendedDto.getUniqueKey(), observationExtendedDto);
        }

        // OBS: 2010-09#M#EUR#SP00#E#USD
        {
            ObservationExtendedDto observationExtendedDto = new ObservationExtendedDto();
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("FREQ", "M"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("CURRENCY", "USD"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("CURRENCY_DENOM", "EUR"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("EXR_TYPE", "SP00"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("EXR_VAR", "E"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("TIME_PERIOD", "2010-09"));

            observationExtendedDto.addAttribute(createAttributeInstanceObservationDto("OBS_STATUS", "A"));
            observationExtendedDto.addAttribute(createAttributeInstanceObservationDto("CONF_STATUS_OBS", "F"));

            observationExtendedDto.setPrimaryMeasure("1.3067");
            observationsMap.put(observationExtendedDto.getUniqueKey(), observationExtendedDto);
        }

        // OBS: 2010-10#M#EUR#SP00#E#CHF
        {
            ObservationExtendedDto observationExtendedDto = new ObservationExtendedDto();
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("FREQ", "M"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("CURRENCY", "CHF"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("CURRENCY_DENOM", "EUR"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("EXR_TYPE", "SP00"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("EXR_VAR", "E"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("TIME_PERIOD", "2010-10"));

            observationExtendedDto.addAttribute(createAttributeInstanceObservationDto("OBS_STATUS", "A"));
            observationExtendedDto.addAttribute(createAttributeInstanceObservationDto("CONF_STATUS_OBS", "F"));

            observationExtendedDto.setPrimaryMeasure("1.3452");
            observationsMap.put(observationExtendedDto.getUniqueKey(), observationExtendedDto);
        }

        // OBS: 2010-10#M#EUR#SP00#E#GBP
        {
            ObservationExtendedDto observationExtendedDto = new ObservationExtendedDto();
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("FREQ", "M"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("CURRENCY", "GBP"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("CURRENCY_DENOM", "EUR"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("EXR_TYPE", "SP00"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("EXR_VAR", "E"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("TIME_PERIOD", "2010-10"));

            observationExtendedDto.addAttribute(createAttributeInstanceObservationDto("OBS_STATUS", "A"));
            observationExtendedDto.addAttribute(createAttributeInstanceObservationDto("CONF_STATUS_OBS", "F"));

            observationExtendedDto.setPrimaryMeasure("0.87637");
            observationsMap.put(observationExtendedDto.getUniqueKey(), observationExtendedDto);
        }

        // OBS: 2010-10#M#EUR#SP00#E#JPY
        {
            ObservationExtendedDto observationExtendedDto = new ObservationExtendedDto();
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("FREQ", "M"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("CURRENCY", "JPY"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("CURRENCY_DENOM", "EUR"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("EXR_TYPE", "SP00"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("EXR_VAR", "E"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("TIME_PERIOD", "2010-10"));

            observationExtendedDto.addAttribute(createAttributeInstanceObservationDto("OBS_STATUS", "A"));
            observationExtendedDto.addAttribute(createAttributeInstanceObservationDto("CONF_STATUS_OBS", "F"));

            observationExtendedDto.setPrimaryMeasure("113.67");
            observationsMap.put(observationExtendedDto.getUniqueKey(), observationExtendedDto);
        }

        // OBS: 2010-10#M#EUR#SP00#E#USD
        {
            ObservationExtendedDto observationExtendedDto = new ObservationExtendedDto();
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("FREQ", "M"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("CURRENCY", "USD"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("CURRENCY_DENOM", "EUR"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("EXR_TYPE", "SP00"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("EXR_VAR", "E"));
            observationExtendedDto.getCodesDimension().add(new CodeDimensionDto("TIME_PERIOD", "2010-10"));

            observationExtendedDto.addAttribute(createAttributeInstanceObservationDto("OBS_STATUS", "A"));
            observationExtendedDto.addAttribute(createAttributeInstanceObservationDto("CONF_STATUS_OBS", "F"));

            observationExtendedDto.setPrimaryMeasure("1.3898");
            observationsMap.put(observationExtendedDto.getUniqueKey(), observationExtendedDto);
        }
        return observationsMap;
    }

    public static List<String> mockAttributesIds() {
        List<String> result = new ArrayList<String>();
        result.add("COLL_METHOD");
        result.add("DECIMALS");
        result.add("UNIT_MEASURE");
        result.add("UNIT_MULT");
        result.add("CONF_STATUS_OBS");
        result.add("OBS_STATUS");
        result.add("TITLE");

        return result;
    }

    public static List<Pair<List<IdValuePair>, AttributeInstanceDto>> mockAttributes() {

        List<Pair<List<IdValuePair>, AttributeInstanceDto>> attributesList = new ArrayList<Pair<List<IdValuePair>, AttributeInstanceDto>>();

        // COLL_METHOD:EXR_TYPE:SP00,EXR_VAR:E
        {
            AttributeInstanceDto attributeInstanceDto = new AttributeInstanceDto();
            attributeInstanceDto.setAttributeId("COLL_METHOD");
            List<IdValuePair> idValuePairs = new ArrayList<IdValuePair>(2);
            idValuePairs.add(new IdValuePair("EXR_TYPE", "SP00"));
            idValuePairs.add(new IdValuePair("EXR_VAR", "E"));

            Map<String, List<String>> conditionsMap = new HashMap<String, List<String>>();
            for (IdValuePair idValuePair : idValuePairs) {
                conditionsMap.put(idValuePair.getCode(), Arrays.asList(idValuePair.getValue()));
            }
            attributeInstanceDto.setCodesByDimension(conditionsMap);
            attributeInstanceDto.setValue(mockInternationalStringDto("Average of observations through period"));

            Pair pair = new ImmutablePair<List<IdValuePair>, AttributeInstanceDto>(idValuePairs, attributeInstanceDto);
            attributesList.add(pair);
        }

        // DECIMALS:CURRENCY:CHF,CURRENCY_DENOM:EUR,EXR_TYPE:SP00
        {
            AttributeInstanceDto attributeInstanceDto = new AttributeInstanceDto();
            attributeInstanceDto.setAttributeId("DECIMALS");
            List<IdValuePair> idValuePairs = new ArrayList<IdValuePair>(3);
            idValuePairs.add(new IdValuePair("CURRENCY", "CHF"));
            idValuePairs.add(new IdValuePair("CURRENCY_DENOM", "EUR"));
            idValuePairs.add(new IdValuePair("EXR_TYPE", "SP00"));

            Map<String, List<String>> conditionsMap = new HashMap<String, List<String>>();
            for (IdValuePair idValuePair : idValuePairs) {
                conditionsMap.put(idValuePair.getCode(), Arrays.asList(idValuePair.getValue()));
            }
            attributeInstanceDto.setCodesByDimension(conditionsMap);
            attributeInstanceDto.setValue(mockInternationalStringDto("4"));

            Pair pair = new ImmutablePair<List<IdValuePair>, AttributeInstanceDto>(idValuePairs, attributeInstanceDto);
            attributesList.add(pair);
        }

        // DECIMALS:CURRENCY:GBP,CURRENCY_DENOM:EUR,EXR_TYPE:SP00
        {
            AttributeInstanceDto attributeInstanceDto = new AttributeInstanceDto();
            attributeInstanceDto.setAttributeId("DECIMALS");
            List<IdValuePair> idValuePairs = new ArrayList<IdValuePair>(3);
            idValuePairs.add(new IdValuePair("CURRENCY", "GBP"));
            idValuePairs.add(new IdValuePair("CURRENCY_DENOM", "EUR"));
            idValuePairs.add(new IdValuePair("EXR_TYPE", "SP00"));

            Map<String, List<String>> conditionsMap = new HashMap<String, List<String>>();
            for (IdValuePair idValuePair : idValuePairs) {
                conditionsMap.put(idValuePair.getCode(), Arrays.asList(idValuePair.getValue()));
            }
            attributeInstanceDto.setCodesByDimension(conditionsMap);
            attributeInstanceDto.setValue(mockInternationalStringDto("5"));

            Pair pair = new ImmutablePair<List<IdValuePair>, AttributeInstanceDto>(idValuePairs, attributeInstanceDto);
            attributesList.add(pair);
        }

        // DECIMALS:CURRENCY:JPY,CURRENCY_DENOM:EUR,EXR_TYPE:SP00
        {
            AttributeInstanceDto attributeInstanceDto = new AttributeInstanceDto();
            attributeInstanceDto.setAttributeId("DECIMALS");
            List<IdValuePair> idValuePairs = new ArrayList<IdValuePair>(3);
            idValuePairs.add(new IdValuePair("CURRENCY", "JPY"));
            idValuePairs.add(new IdValuePair("CURRENCY_DENOM", "EUR"));
            idValuePairs.add(new IdValuePair("EXR_TYPE", "SP00"));

            Map<String, List<String>> conditionsMap = new HashMap<String, List<String>>();
            for (IdValuePair idValuePair : idValuePairs) {
                conditionsMap.put(idValuePair.getCode(), Arrays.asList(idValuePair.getValue()));
            }
            attributeInstanceDto.setCodesByDimension(conditionsMap);
            attributeInstanceDto.setValue(mockInternationalStringDto("2"));

            Pair pair = new ImmutablePair<List<IdValuePair>, AttributeInstanceDto>(idValuePairs, attributeInstanceDto);
            attributesList.add(pair);
        }

        // DECIMALS:CURRENCY:USD,CURRENCY_DENOM:EUR,EXR_TYPE:SP00
        {
            AttributeInstanceDto attributeInstanceDto = new AttributeInstanceDto();
            attributeInstanceDto.setAttributeId("DECIMALS");
            List<IdValuePair> idValuePairs = new ArrayList<IdValuePair>(3);
            idValuePairs.add(new IdValuePair("CURRENCY", "USD"));
            idValuePairs.add(new IdValuePair("CURRENCY_DENOM", "EUR"));
            idValuePairs.add(new IdValuePair("EXR_TYPE", "SP00"));

            Map<String, List<String>> conditionsMap = new HashMap<String, List<String>>();
            for (IdValuePair idValuePair : idValuePairs) {
                conditionsMap.put(idValuePair.getCode(), Arrays.asList(idValuePair.getValue()));
            }
            attributeInstanceDto.setCodesByDimension(conditionsMap);
            attributeInstanceDto.setValue(mockInternationalStringDto("4"));

            Pair pair = new ImmutablePair<List<IdValuePair>, AttributeInstanceDto>(idValuePairs, attributeInstanceDto);
            attributesList.add(pair);
        }

        // UNIT_MEASURE:CURRENCY:CHF,CURRENCY_DENOM:EUR,EXR_TYPE:SP00
        {
            AttributeInstanceDto attributeInstanceDto = new AttributeInstanceDto();
            attributeInstanceDto.setAttributeId("UNIT_MEASURE");
            List<IdValuePair> idValuePairs = new ArrayList<IdValuePair>(3);
            idValuePairs.add(new IdValuePair("CURRENCY", "CHF"));
            idValuePairs.add(new IdValuePair("CURRENCY_DENOM", "EUR"));
            idValuePairs.add(new IdValuePair("EXR_TYPE", "SP00"));

            Map<String, List<String>> conditionsMap = new HashMap<String, List<String>>();
            for (IdValuePair idValuePair : idValuePairs) {
                conditionsMap.put(idValuePair.getCode(), Arrays.asList(idValuePair.getValue()));
            }
            attributeInstanceDto.setCodesByDimension(conditionsMap);
            attributeInstanceDto.setValue(mockInternationalStringDto("CHF"));

            Pair pair = new ImmutablePair<List<IdValuePair>, AttributeInstanceDto>(idValuePairs, attributeInstanceDto);
            attributesList.add(pair);
        }

        // UNIT_MEASURE:CURRENCY:GBP,CURRENCY_DENOM:EUR,EXR_TYPE:SP00
        {
            AttributeInstanceDto attributeInstanceDto = new AttributeInstanceDto();
            attributeInstanceDto.setAttributeId("UNIT_MEASURE");
            List<IdValuePair> idValuePairs = new ArrayList<IdValuePair>(3);
            idValuePairs.add(new IdValuePair("CURRENCY", "GBP"));
            idValuePairs.add(new IdValuePair("CURRENCY_DENOM", "EUR"));
            idValuePairs.add(new IdValuePair("EXR_TYPE", "SP00"));

            Map<String, List<String>> conditionsMap = new HashMap<String, List<String>>();
            for (IdValuePair idValuePair : idValuePairs) {
                conditionsMap.put(idValuePair.getCode(), Arrays.asList(idValuePair.getValue()));
            }
            attributeInstanceDto.setCodesByDimension(conditionsMap);
            attributeInstanceDto.setValue(mockInternationalStringDto("GBP"));

            Pair pair = new ImmutablePair<List<IdValuePair>, AttributeInstanceDto>(idValuePairs, attributeInstanceDto);
            attributesList.add(pair);
        }

        // UNIT_MEASURE:CURRENCY:JPY,CURRENCY_DENOM:EUR,EXR_TYPE:SP00
        {
            AttributeInstanceDto attributeInstanceDto = new AttributeInstanceDto();
            attributeInstanceDto.setAttributeId("UNIT_MEASURE");
            List<IdValuePair> idValuePairs = new ArrayList<IdValuePair>(3);
            idValuePairs.add(new IdValuePair("CURRENCY", "JPY"));
            idValuePairs.add(new IdValuePair("CURRENCY_DENOM", "EUR"));
            idValuePairs.add(new IdValuePair("EXR_TYPE", "SP00"));

            Map<String, List<String>> conditionsMap = new HashMap<String, List<String>>();
            for (IdValuePair idValuePair : idValuePairs) {
                conditionsMap.put(idValuePair.getCode(), Arrays.asList(idValuePair.getValue()));
            }
            attributeInstanceDto.setCodesByDimension(conditionsMap);
            attributeInstanceDto.setValue(mockInternationalStringDto("JPY"));

            Pair pair = new ImmutablePair<List<IdValuePair>, AttributeInstanceDto>(idValuePairs, attributeInstanceDto);
            attributesList.add(pair);
        }

        // UNIT_MEASURE:CURRENCY:USD,CURRENCY_DENOM:EUR,EXR_TYPE:SP00
        {
            AttributeInstanceDto attributeInstanceDto = new AttributeInstanceDto();
            attributeInstanceDto.setAttributeId("UNIT_MEASURE");
            List<IdValuePair> idValuePairs = new ArrayList<IdValuePair>(3);
            idValuePairs.add(new IdValuePair("CURRENCY", "USD"));
            idValuePairs.add(new IdValuePair("CURRENCY_DENOM", "EUR"));
            idValuePairs.add(new IdValuePair("EXR_TYPE", "SP00"));

            Map<String, List<String>> conditionsMap = new HashMap<String, List<String>>();
            for (IdValuePair idValuePair : idValuePairs) {
                conditionsMap.put(idValuePair.getCode(), Arrays.asList(idValuePair.getValue()));
            }
            attributeInstanceDto.setCodesByDimension(conditionsMap);
            attributeInstanceDto.setValue(mockInternationalStringDto("USD"));

            Pair pair = new ImmutablePair<List<IdValuePair>, AttributeInstanceDto>(idValuePairs, attributeInstanceDto);
            attributesList.add(pair);
        }

        // UNIT_MULT:CURRENCY:CHF,CURRENCY_DENOM:EUR,EXR_TYPE:SP00
        {
            AttributeInstanceDto attributeInstanceDto = new AttributeInstanceDto();
            attributeInstanceDto.setAttributeId("UNIT_MULT");
            List<IdValuePair> idValuePairs = new ArrayList<IdValuePair>(3);
            idValuePairs.add(new IdValuePair("CURRENCY", "CHF"));
            idValuePairs.add(new IdValuePair("CURRENCY_DENOM", "EUR"));
            idValuePairs.add(new IdValuePair("EXR_TYPE", "SP00"));

            Map<String, List<String>> conditionsMap = new HashMap<String, List<String>>();
            for (IdValuePair idValuePair : idValuePairs) {
                conditionsMap.put(idValuePair.getCode(), Arrays.asList(idValuePair.getValue()));
            }
            attributeInstanceDto.setCodesByDimension(conditionsMap);
            attributeInstanceDto.setValue(mockInternationalStringDto("0"));

            Pair pair = new ImmutablePair<List<IdValuePair>, AttributeInstanceDto>(idValuePairs, attributeInstanceDto);
            attributesList.add(pair);
        }

        // UNIT_MULT:CURRENCY:GBP,CURRENCY_DENOM:EUR,EXR_TYPE:SP00
        {
            AttributeInstanceDto attributeInstanceDto = new AttributeInstanceDto();
            attributeInstanceDto.setAttributeId("UNIT_MULT");
            List<IdValuePair> idValuePairs = new ArrayList<IdValuePair>(3);
            idValuePairs.add(new IdValuePair("CURRENCY", "GBP"));
            idValuePairs.add(new IdValuePair("CURRENCY_DENOM", "EUR"));
            idValuePairs.add(new IdValuePair("EXR_TYPE", "SP00"));

            Map<String, List<String>> conditionsMap = new HashMap<String, List<String>>();
            for (IdValuePair idValuePair : idValuePairs) {
                conditionsMap.put(idValuePair.getCode(), Arrays.asList(idValuePair.getValue()));
            }
            attributeInstanceDto.setCodesByDimension(conditionsMap);
            attributeInstanceDto.setValue(mockInternationalStringDto("0"));

            Pair pair = new ImmutablePair<List<IdValuePair>, AttributeInstanceDto>(idValuePairs, attributeInstanceDto);
            attributesList.add(pair);
        }

        // UNIT_MULT:CURRENCY:JPY,CURRENCY_DENOM:EUR,EXR_TYPE:SP00
        {
            AttributeInstanceDto attributeInstanceDto = new AttributeInstanceDto();
            attributeInstanceDto.setAttributeId("UNIT_MULT");
            List<IdValuePair> idValuePairs = new ArrayList<IdValuePair>(3);
            idValuePairs.add(new IdValuePair("CURRENCY", "JPY"));
            idValuePairs.add(new IdValuePair("CURRENCY_DENOM", "EUR"));
            idValuePairs.add(new IdValuePair("EXR_TYPE", "SP00"));

            Map<String, List<String>> conditionsMap = new HashMap<String, List<String>>();
            for (IdValuePair idValuePair : idValuePairs) {
                conditionsMap.put(idValuePair.getCode(), Arrays.asList(idValuePair.getValue()));
            }
            attributeInstanceDto.setCodesByDimension(conditionsMap);
            attributeInstanceDto.setValue(mockInternationalStringDto("0"));

            Pair pair = new ImmutablePair<List<IdValuePair>, AttributeInstanceDto>(idValuePairs, attributeInstanceDto);
            attributesList.add(pair);
        }

        // UNIT_MULT:CURRENCY:USD,CURRENCY_DENOM:EUR,EXR_TYPE:SP00
        {
            AttributeInstanceDto attributeInstanceDto = new AttributeInstanceDto();
            attributeInstanceDto.setAttributeId("UNIT_MULT");
            List<IdValuePair> idValuePairs = new ArrayList<IdValuePair>(3);
            idValuePairs.add(new IdValuePair("CURRENCY", "USD"));
            idValuePairs.add(new IdValuePair("CURRENCY_DENOM", "EUR"));
            idValuePairs.add(new IdValuePair("EXR_TYPE", "SP00"));

            Map<String, List<String>> conditionsMap = new HashMap<String, List<String>>();
            for (IdValuePair idValuePair : idValuePairs) {
                conditionsMap.put(idValuePair.getCode(), Arrays.asList(idValuePair.getValue()));
            }
            attributeInstanceDto.setCodesByDimension(conditionsMap);
            attributeInstanceDto.setValue(mockInternationalStringDto("0"));

            Pair pair = new ImmutablePair<List<IdValuePair>, AttributeInstanceDto>(idValuePairs, attributeInstanceDto);
            attributesList.add(pair);
        }

        // TITLE:CURRENCY:CHF,CURRENCY_DENOM:EUR,EXR_TYPE:SP00,EXR_VAR:E
        {
            AttributeInstanceDto attributeInstanceDto = new AttributeInstanceDto();
            attributeInstanceDto.setAttributeId("TITLE");
            List<IdValuePair> idValuePairs = new ArrayList<IdValuePair>(3);
            idValuePairs.add(new IdValuePair("CURRENCY", "CHF"));
            idValuePairs.add(new IdValuePair("CURRENCY_DENOM", "EUR"));
            idValuePairs.add(new IdValuePair("EXR_TYPE", "SP00"));
            idValuePairs.add(new IdValuePair("EXR_VAR", "E"));

            Map<String, List<String>> conditionsMap = new HashMap<String, List<String>>();
            for (IdValuePair idValuePair : idValuePairs) {
                conditionsMap.put(idValuePair.getCode(), Arrays.asList(idValuePair.getValue()));
            }
            attributeInstanceDto.setCodesByDimension(conditionsMap);
            attributeInstanceDto.setValue(mockInternationalStringDto("ECB reference exchange rate, Swiss franc/Euro"));

            Pair pair = new ImmutablePair<List<IdValuePair>, AttributeInstanceDto>(idValuePairs, attributeInstanceDto);
            attributesList.add(pair);
        }

        // TITLE:CURRENCY:GBP,CURRENCY_DENOM:EUR,EXR_TYPE:SP00,EXR_VAR:E
        {
            AttributeInstanceDto attributeInstanceDto = new AttributeInstanceDto();
            attributeInstanceDto.setAttributeId("TITLE");
            List<IdValuePair> idValuePairs = new ArrayList<IdValuePair>(3);
            idValuePairs.add(new IdValuePair("CURRENCY", "GBP"));
            idValuePairs.add(new IdValuePair("CURRENCY_DENOM", "EUR"));
            idValuePairs.add(new IdValuePair("EXR_TYPE", "SP00"));
            idValuePairs.add(new IdValuePair("EXR_VAR", "E"));

            Map<String, List<String>> conditionsMap = new HashMap<String, List<String>>();
            for (IdValuePair idValuePair : idValuePairs) {
                conditionsMap.put(idValuePair.getCode(), Arrays.asList(idValuePair.getValue()));
            }
            attributeInstanceDto.setCodesByDimension(conditionsMap);
            attributeInstanceDto.setValue(mockInternationalStringDto("ECB reference exchange rate, U.K. Pound sterling /Euro"));

            Pair pair = new ImmutablePair<List<IdValuePair>, AttributeInstanceDto>(idValuePairs, attributeInstanceDto);
            attributesList.add(pair);
        }

        // TITLE:CURRENCY:JPY,CURRENCY_DENOM:EUR,EXR_TYPE:SP00,EXR_VAR:E
        {
            AttributeInstanceDto attributeInstanceDto = new AttributeInstanceDto();
            attributeInstanceDto.setAttributeId("TITLE");
            List<IdValuePair> idValuePairs = new ArrayList<IdValuePair>(3);
            idValuePairs.add(new IdValuePair("CURRENCY", "JPY"));
            idValuePairs.add(new IdValuePair("CURRENCY_DENOM", "EUR"));
            idValuePairs.add(new IdValuePair("EXR_TYPE", "SP00"));
            idValuePairs.add(new IdValuePair("EXR_VAR", "E"));

            Map<String, List<String>> conditionsMap = new HashMap<String, List<String>>();
            for (IdValuePair idValuePair : idValuePairs) {
                conditionsMap.put(idValuePair.getCode(), Arrays.asList(idValuePair.getValue()));
            }
            attributeInstanceDto.setCodesByDimension(conditionsMap);
            attributeInstanceDto.setValue(mockInternationalStringDto("ECB reference exchange rate, Japanese yen/Euro"));

            Pair pair = new ImmutablePair<List<IdValuePair>, AttributeInstanceDto>(idValuePairs, attributeInstanceDto);
            attributesList.add(pair);
        }

        // TITLE:CURRENCY:USD,CURRENCY_DENOM:EUR,EXR_TYPE:SP00,EXR_VAR:E
        {
            AttributeInstanceDto attributeInstanceDto = new AttributeInstanceDto();
            attributeInstanceDto.setAttributeId("TITLE");
            List<IdValuePair> idValuePairs = new ArrayList<IdValuePair>(3);
            idValuePairs.add(new IdValuePair("CURRENCY", "USD"));
            idValuePairs.add(new IdValuePair("CURRENCY_DENOM", "EUR"));
            idValuePairs.add(new IdValuePair("EXR_TYPE", "SP00"));
            idValuePairs.add(new IdValuePair("EXR_VAR", "E"));

            Map<String, List<String>> conditionsMap = new HashMap<String, List<String>>();
            for (IdValuePair idValuePair : idValuePairs) {
                conditionsMap.put(idValuePair.getCode(), Arrays.asList(idValuePair.getValue()));
            }
            attributeInstanceDto.setCodesByDimension(conditionsMap);
            attributeInstanceDto.setValue(mockInternationalStringDto("ECB reference exchange rate, U.S. dollar/Euro"));

            Pair pair = new ImmutablePair<List<IdValuePair>, AttributeInstanceDto>(idValuePairs, attributeInstanceDto);
            attributesList.add(pair);
        }

        return attributesList;
    }

    public static InternationalStringDto mockInternationalStringDto(String value) {
        InternationalStringDto internationalStringDto = new InternationalStringDto();
        LocalisedStringDto localisedStringDto = new LocalisedStringDto();
        localisedStringDto.setLabel(value);
        localisedStringDto.setLocale(StatisticalResourcesConstants.DEFAULT_DATA_REPOSITORY_LOCALE);
        internationalStringDto.addText(localisedStringDto);
        return internationalStringDto;
    }

    public static AttributeInstanceObservationDto createAttributeInstanceObservationDto(String attributeId, String value) {
        InternationalStringDto internationalStringDto = new InternationalStringDto();
        LocalisedStringDto localisedStringDto = new LocalisedStringDto();
        localisedStringDto.setLabel(value);
        // In SDMX the attributes aren't localized. For use localised in SDMX must be use a enumerated representation.
        // In this case, in the repo exists the code of enumerated representation, never the i18n of code.
        localisedStringDto.setLocale(StatisticalResourcesConstants.DEFAULT_DATA_REPOSITORY_LOCALE);
        internationalStringDto.addText(localisedStringDto);

        return new AttributeInstanceObservationDto(attributeId, internationalStringDto);
    }

    private DatasetVersion mockDatasetVersionBasic(String agencyID, String resourceID, String version) {
        DatasetVersion target = coreDoMocks.mockDatasetVersion();
        target.getSiemacMetadataStatisticalResource().setUrn("urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Dataset=" + agencyID + ":" + resourceID + "(" + version + ")");
        target.getSiemacMetadataStatisticalResource().getMaintainer().setCodeNested(agencyID);
        target.getSiemacMetadataStatisticalResource().setCode(resourceID);
        target.getSiemacMetadataStatisticalResource().setVersionLogic(version);
        target.getSiemacMetadataStatisticalResource().setTitle(StatisticalResourcesDoMocks.mockInternationalStringMetadata(resourceID, "title"));
        return target;
    }

    public DatasetVersion mockDatasetVersion(String agencyID, String resourceID, String version) {
        DatasetVersion target = mockDatasetVersionBasic(agencyID, resourceID, version);
        target.setBibliographicCitation(new InternationalString("es", "bibliographicCitation. More info: #URI#"));
        target.setDatasetRepositoryId("datasetRepository01");

        target.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("santa-cruz-tenerife"));
        target.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("tenerife"));
        target.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("la-laguna"));
        target.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("santa-cruz"));
        target.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("la-palma"));
        target.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("santa-cruz-la-palma"));
        target.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("los-llanos-de-aridane"));
        target.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("la-gomera"));
        target.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("el-hierro"));
        target.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("las-palmas-gran-canaria"));
        target.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("gran-canaria"));
        target.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("fuerteventura"));
        target.addGeographicCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("lanzarote"));

        target.addTemporalCoverage(mockTemporalCode("2014"));
        target.addTemporalCoverage(mockTemporalCode("2013"));
        target.addTemporalCoverage(mockTemporalCode("2012"));
        target.addTemporalCoverage(mockTemporalCode("2011"));
        target.addMeasureCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("code-d2-1"));
        target.addMeasureCoverage(StatisticalResourcesDoMocks.mockCodeExternalItem("code-d2-2"));
        target.addGeographicGranularity(StatisticalResourcesDoMocks.mockCodeExternalItem("municipalities"));
        target.addGeographicGranularity(StatisticalResourcesDoMocks.mockCodeExternalItem("countries"));
        target.addTemporalGranularity(StatisticalResourcesDoMocks.mockCodeExternalItem("yearly"));
        target.addTemporalGranularity(StatisticalResourcesDoMocks.mockCodeExternalItem("monthly"));
        target.setDateStart("2013-1-2");
        target.setDateEnd("2013-3-5");
        target.addStatisticalUnit(StatisticalResourcesDoMocks.mockConceptExternalItem("statisticalUnit1"));
        target.addStatisticalUnit(StatisticalResourcesDoMocks.mockConceptExternalItem("statisticalUnit2"));
        target.addStatisticalUnit(StatisticalResourcesDoMocks.mockConceptExternalItem("statisticalUnit3"));
        target.setRelatedDsd(StatisticalResourcesDoMocks.mockDsdExternalItem("DSD01"));
        target.setFormatExtentObservations(Long.valueOf(3));
        target.setFormatExtentDimensions(Integer.valueOf(5));
        // TODO: target.setFormatExtentTableSize(Integer.valueOf(5));
        target.setDateNextUpdate(CoreCommonUtil.jodaDateTime2IsoDate(new DateTime(2013, 12, 2, 3, 4, 5, 0).toDate()));
        target.setUpdateFrequency(StatisticalResourcesDoMocks.mockCodeExternalItem("updateFrequency01"));
        target.setStatisticOfficiality(coreDoMocks.mockStatisticOfficiality("statisticOfficiality01"));
        mockSiemacMetadataStatisticalResource(agencyID, resourceID, version, target.getSiemacMetadataStatisticalResource());
        return target;
    }

    private TemporalCode mockTemporalCode(String code) {
        return StatisticalResourcesDoMocks.mockTemporalCode(code, code);
    }

    private void mockSiemacMetadataStatisticalResource(String agencyID, String resourceID, String version, SiemacMetadataStatisticalResource target) {
        target.addStatisticalOperationInstance(StatisticalResourcesDoMocks.mockStatisticalOperationInstanceExternalItem("instance01"));
        target.addStatisticalOperationInstance(StatisticalResourcesDoMocks.mockStatisticalOperationInstanceExternalItem("instance02"));
        target.setResourceCreatedDate(new DateTime(2012, 1, 2, 3, 4, 5, 0));
        target.setLastUpdate(new DateTime(2013, 1, 2, 3, 4, 5, 0));
        target.setNewnessUntilDate(new DateTime(2013, 9, 2, 15, 4, 5, 0));
        target.setReplaces(mockDatasetRelatedResource(agencyID, "replace01", StatisticalResourcesMockFactory.INIT_VERSION));
        target.setCopyrightedDate(2014);
        mockLifeCycleStatisticalResource(agencyID, resourceID, version, target);
    }

    public RelatedResource mockDatasetRelatedResource(String agencyID, String resourceID, String version) {
        DatasetVersion dataset = mockDatasetVersionBasic(agencyID, resourceID, version);
        return StatisticalResourcesDoMocks.mockDatasetVersionRelated(dataset);
    }

    private void mockLifeCycleStatisticalResource(String agencyID, String resourceID, String version, LifeCycleStatisticalResource target) {
        target.setPublicationDate(new DateTime(2013, 1, 1, 3, 4, 13, 0));
        target.setReplacesVersion(mockDatasetRelatedResource(agencyID, version, StatisticalResourcesMockFactory.INIT_VERSION));
        mockVersionableStatisticalResource(agencyID, resourceID, version, target);
    }

    private void mockVersionableStatisticalResource(String agencyID, String resourceID, String version, VersionableStatisticalResource target) {
        target.setVersionRationale(StatisticalResourcesDoMocks.mockInternationalStringMetadata(resourceID, "versionRationale"));
        target.addVersionRationaleType(new VersionRationaleType(VersionRationaleTypeEnum.MAJOR_CATEGORIES));
        target.addVersionRationaleType(new VersionRationaleType(VersionRationaleTypeEnum.MINOR_METADATA));
        target.setValidFrom(new DateTime(2013, 1, 1, 3, 4, 12, 0));
        target.setValidTo(new DateTime(2013, 2, 16, 14, 4, 12, 0));
    }

    public DataStructureType mockDsd_ECB_EXR_RG() {
        DataStructureType dataStructureType = structureObjectFactory.createDataStructureType();
        dataStructureType.setAgencyID("ECB");
        dataStructureType.setId("ECB_EXR_RG");
        dataStructureType.setVersion("1.0");

        // Components
        {
            DataStructureComponentsType dataStructureComponentsType = structureObjectFactory.createDataStructureComponentsType();

            // DimensionList
            {
                DimensionListType dimensionListType = structureObjectFactory.createDimensionListType();
                dimensionListType.setId("DimensionDescriptor");

                // FREQ
                dimensionListType.getDimensionsAndMeasureDimensionsAndTimeDimensions().add(mockDimensionType("FREQ", mockConceptReferenceType("SDMX", "CROSS_DOMAIN_CONCEPTS", "1.0", "FREQ"), null));

                // CURRENCY
                dimensionListType.getDimensionsAndMeasureDimensionsAndTimeDimensions().add(mockDimensionType("CURRENCY", mockConceptReferenceType("SDMX", "CROSS_DOMAIN_CONCEPTS", "1.0", "CURRENCY"),
                        mockEnumerationCodelist(mockCodelistReferenceType("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=ISO:CL_CURRENCY(1.0)"))));

                // CURRENCY_DENOM
                dimensionListType.getDimensionsAndMeasureDimensionsAndTimeDimensions()
                        .add(mockDimensionType("CURRENCY_DENOM", mockConceptReferenceType("ECB", "ECB_CONCEPTS", "1.0", "CURRENCY_DENOM"), null));

                // EXR_TYPE
                dimensionListType.getDimensionsAndMeasureDimensionsAndTimeDimensions().add(mockDimensionType("EXR_TYPE", mockConceptReferenceType("ECB", "ECB_CONCEPTS", "1.0", "EXR_TYPE"), null));

                // EXR_VAR
                dimensionListType.getDimensionsAndMeasureDimensionsAndTimeDimensions().add(mockDimensionType("EXR_VAR", mockConceptReferenceType("ECB", "ECB_CONCEPTS", "1.0", "EXR_VAR"), null));

                // TIME_PERIOD
                dimensionListType.getDimensionsAndMeasureDimensionsAndTimeDimensions()
                        .add(mockTimeDimensionType(mockConceptReferenceType("SDMX", "CROSS_DOMAIN_CONCEPTS", "1.0", "TIME_PERIOD"), TimeDataType.OBSERVATIONAL_TIME_PERIOD));

                dataStructureComponentsType.setDimensionList(dimensionListType);
            }

            // SiblingGroup
            {
                GroupType groupType = structureObjectFactory.createGroupType();

                groupType.setId("SiblingGroup");
                groupType.getGroupDimensions().add(mockGroupDimensionType("CURRENCY"));
                groupType.getGroupDimensions().add(mockGroupDimensionType("CURRENCY_DENOM"));
                groupType.getGroupDimensions().add(mockGroupDimensionType("EXR_TYPE"));
                groupType.getGroupDimensions().add(mockGroupDimensionType("EXR_VAR"));

                dataStructureComponentsType.getGroups().add(groupType);
            }

            // RateGroup
            {
                GroupType groupType = structureObjectFactory.createGroupType();

                groupType.setId("RateGroup");
                groupType.getGroupDimensions().add(mockGroupDimensionType("EXR_TYPE"));
                groupType.getGroupDimensions().add(mockGroupDimensionType("EXR_VAR"));

                dataStructureComponentsType.getGroups().add(groupType);
            }

            // AttributeList
            {
                AttributeListType attributeListType = structureObjectFactory.createAttributeListType();
                attributeListType.setId("AttributeDescriptor");

                {
                    // COLL_METHOD
                    attributeListType.getAttributesAndReportingYearStartDaies().add(mockAttributeType("COLL_METHOD", mockConceptReferenceType("SDMX", "CROSS_DOMAIN_CONCEPTS", "1.0", "COLL_METHOD"),
                            UsageStatusType.CONDITIONAL, mockAttributeRelationshipGroupType("RateGroup"), null, (ConceptReferenceType[]) null));
                }

                {
                    // DECIMALS
                    attributeListType.getAttributesAndReportingYearStartDaies()
                            .add(mockAttributeType("DECIMALS", mockConceptReferenceType("SDMX", "CROSS_DOMAIN_CONCEPTS", "1.0", "DECIMALS"), UsageStatusType.MANDATORY,
                                    mockAttributeRelationshipDimensionType(Arrays.asList("CURRENCY", "CURRENCY_DENOM", "EXR_TYPE"), Arrays.asList("SiblingGroup")), null,
                                    (ConceptReferenceType[]) null));
                }

                {
                    // UNIT_MEASURE
                    attributeListType.getAttributesAndReportingYearStartDaies()
                            .add(mockAttributeType("UNIT_MEASURE", mockConceptReferenceType("SDMX", "CROSS_DOMAIN_CONCEPTS", "1.0", "UNIT_MEASURE"), UsageStatusType.MANDATORY,
                                    mockAttributeRelationshipDimensionType(Arrays.asList("CURRENCY", "CURRENCY_DENOM", "EXR_TYPE"), Arrays.asList("SiblingGroup")),
                                    mockEnumerationCodelist(mockCodelistReferenceType("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=ISO:CL_CURRENCY(1.0)")), (ConceptReferenceType[]) null));
                }

                {
                    // UNIT_MULT
                    attributeListType.getAttributesAndReportingYearStartDaies()
                            .add(mockAttributeType("UNIT_MULT", mockConceptReferenceType("SDMX", "CROSS_DOMAIN_CONCEPTS", "1.0", "UNIT_MULT"), UsageStatusType.MANDATORY,
                                    mockAttributeRelationshipDimensionType(Arrays.asList("CURRENCY", "CURRENCY_DENOM", "EXR_TYPE"), Arrays.asList("SiblingGroup")), null,
                                    (ConceptReferenceType[]) null));
                }

                {
                    // CONF_STATUS_OBS
                    attributeListType.getAttributesAndReportingYearStartDaies()
                            .add(mockAttributeType("CONF_STATUS_OBS", mockConceptReferenceType("SDMX", "CROSS_DOMAIN_CONCEPTS", "1.0", "CONF_STATUS_OBS"), UsageStatusType.CONDITIONAL,
                                    mockAttributeRelationshipPrimaryMeasureType(), null, (ConceptReferenceType[]) null));
                }

                {
                    // OBS_STATUS
                    attributeListType.getAttributesAndReportingYearStartDaies().add(mockAttributeType("OBS_STATUS", mockConceptReferenceType("SDMX", "CROSS_DOMAIN_CONCEPTS", "1.0", "OBS_STATUS"),
                            UsageStatusType.MANDATORY, mockAttributeRelationshipPrimaryMeasureType(), null, (ConceptReferenceType[]) null));
                }

                {
                    // TITLE
                    attributeListType.getAttributesAndReportingYearStartDaies().add(mockAttributeType("TITLE", mockConceptReferenceType("SDMX", "CROSS_DOMAIN_CONCEPTS", "1.0", "TITLE"),
                            UsageStatusType.MANDATORY, mockAttributeRelationshipGroupType("SiblingGroup"), null, (ConceptReferenceType[]) null));
                }

                dataStructureComponentsType.setAttributeList(attributeListType);
            }

            // MeasureList
            {
                MeasureListType measureListType = structureObjectFactory.createMeasureListType();
                measureListType.setPrimaryMeasure(mockPrimaryMeasureType(mockConceptReferenceType("SDMX", "CROSS_DOMAIN_CONCEPTS", "1.0", "OBS_VALUE"), null));
                dataStructureComponentsType.setMeasureList(measureListType);
            }

            dataStructureType.setDataStructureComponents(dataStructureComponentsType);
        }

        return dataStructureType;
    }

    public DataStructureType mockDsd_ECB_EXR_NG() {
        DataStructureType dataStructureType = structureObjectFactory.createDataStructureType();
        dataStructureType.setAgencyID("ECB");
        dataStructureType.setId("ECB_EXR_NG");
        dataStructureType.setVersion("1.0");

        // Components
        {
            DataStructureComponentsType dataStructureComponentsType = structureObjectFactory.createDataStructureComponentsType();

            // DimensionList
            {
                DimensionListType dimensionListType = structureObjectFactory.createDimensionListType();
                dimensionListType.setId("DimensionDescriptor");

                // FREQ
                dimensionListType.getDimensionsAndMeasureDimensionsAndTimeDimensions().add(mockDimensionType("FREQ", mockConceptReferenceType("SDMX", "CROSS_DOMAIN_CONCEPTS", "1.0", "FREQ"), null));

                // CURRENCY
                dimensionListType.getDimensionsAndMeasureDimensionsAndTimeDimensions().add(mockDimensionType("CURRENCY", mockConceptReferenceType("SDMX", "CROSS_DOMAIN_CONCEPTS", "1.0", "CURRENCY"),
                        mockEnumerationCodelist(mockCodelistReferenceType("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=ISO:CL_CURRENCY(1.0)"))));

                // CURRENCY_DENOM
                dimensionListType.getDimensionsAndMeasureDimensionsAndTimeDimensions()
                        .add(mockDimensionType("CURRENCY_DENOM", mockConceptReferenceType("ECB", "ECB_CONCEPTS", "1.0", "CURRENCY_DENOM"), null));

                // EXR_TYPE
                dimensionListType.getDimensionsAndMeasureDimensionsAndTimeDimensions().add(mockDimensionType("EXR_TYPE", mockConceptReferenceType("ECB", "ECB_CONCEPTS", "1.0", "EXR_TYPE"), null));

                // EXR_VAR
                dimensionListType.getDimensionsAndMeasureDimensionsAndTimeDimensions().add(mockDimensionType("EXR_VAR", mockConceptReferenceType("ECB", "ECB_CONCEPTS", "1.0", "EXR_VAR"), null));

                // TIME_PERIOD
                dimensionListType.getDimensionsAndMeasureDimensionsAndTimeDimensions()
                        .add(mockTimeDimensionType(mockConceptReferenceType("SDMX", "CROSS_DOMAIN_CONCEPTS", "1.0", "TIME_PERIOD"), TimeDataType.OBSERVATIONAL_TIME_PERIOD));

                dataStructureComponentsType.setDimensionList(dimensionListType);
            }

            // AttributeList
            {
                AttributeListType attributeListType = structureObjectFactory.createAttributeListType();
                attributeListType.setId("AttributeDescriptor");

                {
                    // COLL_METHOD
                    attributeListType.getAttributesAndReportingYearStartDaies().add(mockAttributeType("COLL_METHOD", mockConceptReferenceType("SDMX", "CROSS_DOMAIN_CONCEPTS", "1.0", "COLL_METHOD"),
                            UsageStatusType.CONDITIONAL, mockAttributeRelationshipDimensionType(Arrays.asList("EXR_TYPE", "EXR_VAR"), null), null, (ConceptReferenceType[]) null));
                }

                {
                    // DECIMALS
                    attributeListType.getAttributesAndReportingYearStartDaies().add(mockAttributeType("DECIMALS", mockConceptReferenceType("SDMX", "CROSS_DOMAIN_CONCEPTS", "1.0", "DECIMALS"),
                            UsageStatusType.MANDATORY, mockAttributeRelationshipDimensionType(Arrays.asList("CURRENCY", "CURRENCY_DENOM", "EXR_TYPE"), null), null, (ConceptReferenceType[]) null));
                }

                {
                    // UNIT_MEASURE
                    attributeListType.getAttributesAndReportingYearStartDaies()
                            .add(mockAttributeType("UNIT_MEASURE", mockConceptReferenceType("SDMX", "CROSS_DOMAIN_CONCEPTS", "1.0", "UNIT_MEASURE"), UsageStatusType.MANDATORY,
                                    mockAttributeRelationshipDimensionType(Arrays.asList("CURRENCY", "CURRENCY_DENOM", "EXR_TYPE"), null),
                                    mockEnumerationCodelist(mockCodelistReferenceType("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=ISO:CL_CURRENCY(1.0)")), (ConceptReferenceType[]) null));
                }

                {
                    // UNIT_MULT
                    attributeListType.getAttributesAndReportingYearStartDaies().add(mockAttributeType("UNIT_MULT", mockConceptReferenceType("SDMX", "CROSS_DOMAIN_CONCEPTS", "1.0", "UNIT_MULT"),
                            UsageStatusType.MANDATORY, mockAttributeRelationshipDimensionType(Arrays.asList("CURRENCY", "CURRENCY_DENOM", "EXR_TYPE"), null), null, (ConceptReferenceType[]) null));
                }

                {
                    // CONF_STATUS_OBS
                    attributeListType.getAttributesAndReportingYearStartDaies()
                            .add(mockAttributeType("CONF_STATUS_OBS", mockConceptReferenceType("SDMX", "CROSS_DOMAIN_CONCEPTS", "1.0", "CONF_STATUS_OBS"), UsageStatusType.CONDITIONAL,
                                    mockAttributeRelationshipPrimaryMeasureType(), null, (ConceptReferenceType[]) null));
                }

                {
                    // OBS_STATUS
                    attributeListType.getAttributesAndReportingYearStartDaies().add(mockAttributeType("OBS_STATUS", mockConceptReferenceType("SDMX", "CROSS_DOMAIN_CONCEPTS", "1.0", "OBS_STATUS"),
                            UsageStatusType.MANDATORY, mockAttributeRelationshipPrimaryMeasureType(), null, (ConceptReferenceType[]) null));
                }

                {
                    // TITLE
                    attributeListType.getAttributesAndReportingYearStartDaies()
                            .add(mockAttributeType("TITLE", mockConceptReferenceType("SDMX", "CROSS_DOMAIN_CONCEPTS", "1.0", "TITLE"), UsageStatusType.MANDATORY,
                                    mockAttributeRelationshipDimensionType(Arrays.asList("CURRENCY", "CURRENCY_DENOM", "EXR_TYPE", "EXR_VAR"), null), null, (ConceptReferenceType[]) null));
                }

                dataStructureComponentsType.setAttributeList(attributeListType);
            }

            // MeasureList
            {
                MeasureListType measureListType = structureObjectFactory.createMeasureListType();
                measureListType.setPrimaryMeasure(mockPrimaryMeasureType(mockConceptReferenceType("SDMX", "CROSS_DOMAIN_CONCEPTS", "1.0", "OBS_VALUE"), null));
                dataStructureComponentsType.setMeasureList(measureListType);
            }

            dataStructureType.setDataStructureComponents(dataStructureComponentsType);
        }

        return dataStructureType;
    }

    public DataStructureType mockDsd_ECB_EXR_SG() {
        DataStructureType dataStructureType = structureObjectFactory.createDataStructureType();
        dataStructureType.setAgencyID("ECB");
        dataStructureType.setId("ECB_EXR_SG");
        dataStructureType.setVersion("1.0");

        // Components
        {
            DataStructureComponentsType dataStructureComponentsType = structureObjectFactory.createDataStructureComponentsType();

            // DimensionList
            {
                DimensionListType dimensionListType = structureObjectFactory.createDimensionListType();
                dimensionListType.setId("DimensionDescriptor");

                // FREQ
                dimensionListType.getDimensionsAndMeasureDimensionsAndTimeDimensions().add(mockDimensionType("FREQ", mockConceptReferenceType("SDMX", "CROSS_DOMAIN_CONCEPTS", "1.0", "FREQ"), null));

                // CURRENCY
                dimensionListType.getDimensionsAndMeasureDimensionsAndTimeDimensions().add(mockDimensionType("CURRENCY", mockConceptReferenceType("SDMX", "CROSS_DOMAIN_CONCEPTS", "1.0", "CURRENCY"),
                        mockEnumerationCodelist(mockCodelistReferenceType("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=ISO:CL_CURRENCY(1.0)"))));

                // CURRENCY_DENOM
                dimensionListType.getDimensionsAndMeasureDimensionsAndTimeDimensions()
                        .add(mockDimensionType("CURRENCY_DENOM", mockConceptReferenceType("ECB", "ECB_CONCEPTS", "1.0", "CURRENCY_DENOM"), null));

                // EXR_TYPE
                dimensionListType.getDimensionsAndMeasureDimensionsAndTimeDimensions().add(mockDimensionType("EXR_TYPE", mockConceptReferenceType("ECB", "ECB_CONCEPTS", "1.0", "EXR_TYPE"), null));

                // EXR_VAR
                dimensionListType.getDimensionsAndMeasureDimensionsAndTimeDimensions().add(mockDimensionType("EXR_VAR", mockConceptReferenceType("ECB", "ECB_CONCEPTS", "1.0", "EXR_VAR"), null));

                // TIME_PERIOD
                dimensionListType.getDimensionsAndMeasureDimensionsAndTimeDimensions()
                        .add(mockTimeDimensionType(mockConceptReferenceType("SDMX", "CROSS_DOMAIN_CONCEPTS", "1.0", "TIME_PERIOD"), TimeDataType.OBSERVATIONAL_TIME_PERIOD));

                dataStructureComponentsType.setDimensionList(dimensionListType);
            }

            // SiblingGroup
            {
                GroupType groupType = structureObjectFactory.createGroupType();

                groupType.setId("SiblingGroup");
                groupType.getGroupDimensions().add(mockGroupDimensionType("CURRENCY"));
                groupType.getGroupDimensions().add(mockGroupDimensionType("CURRENCY_DENOM"));
                groupType.getGroupDimensions().add(mockGroupDimensionType("EXR_TYPE"));
                groupType.getGroupDimensions().add(mockGroupDimensionType("EXR_VAR"));

                dataStructureComponentsType.getGroups().add(groupType);
            }

            // AttributeList
            {
                AttributeListType attributeListType = structureObjectFactory.createAttributeListType();
                attributeListType.setId("AttributeDescriptor");

                {
                    // COLL_METHOD
                    attributeListType.getAttributesAndReportingYearStartDaies().add(mockAttributeType("COLL_METHOD", mockConceptReferenceType("SDMX", "CROSS_DOMAIN_CONCEPTS", "1.0", "COLL_METHOD"),
                            UsageStatusType.CONDITIONAL, mockAttributeRelationshipDimensionType(Arrays.asList("EXR_TYPE", "EXR_VAR"), null), null, (ConceptReferenceType[]) null));
                }

                {
                    // DECIMALS
                    attributeListType.getAttributesAndReportingYearStartDaies().add(mockAttributeType("DECIMALS", mockConceptReferenceType("SDMX", "CROSS_DOMAIN_CONCEPTS", "1.0", "DECIMALS"),
                            UsageStatusType.MANDATORY, mockAttributeRelationshipDimensionType(Arrays.asList("CURRENCY", "CURRENCY_DENOM", "EXR_TYPE"), null), null, (ConceptReferenceType[]) null));
                }

                {
                    // UNIT_MEASURE
                    attributeListType.getAttributesAndReportingYearStartDaies()
                            .add(mockAttributeType("UNIT_MEASURE", mockConceptReferenceType("SDMX", "CROSS_DOMAIN_CONCEPTS", "1.0", "UNIT_MEASURE"), UsageStatusType.MANDATORY,
                                    mockAttributeRelationshipDimensionType(Arrays.asList("CURRENCY", "CURRENCY_DENOM", "EXR_TYPE"), null),
                                    mockEnumerationCodelist(mockCodelistReferenceType("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=ISO:CL_CURRENCY(1.0)")), (ConceptReferenceType[]) null));
                }

                {
                    // UNIT_MULT
                    attributeListType.getAttributesAndReportingYearStartDaies().add(mockAttributeType("UNIT_MULT", mockConceptReferenceType("SDMX", "CROSS_DOMAIN_CONCEPTS", "1.0", "UNIT_MULT"),
                            UsageStatusType.MANDATORY, mockAttributeRelationshipDimensionType(Arrays.asList("CURRENCY", "CURRENCY_DENOM", "EXR_TYPE"), null), null, (ConceptReferenceType[]) null));
                }

                {
                    // CONF_STATUS_OBS
                    attributeListType.getAttributesAndReportingYearStartDaies()
                            .add(mockAttributeType("CONF_STATUS_OBS", mockConceptReferenceType("SDMX", "CROSS_DOMAIN_CONCEPTS", "1.0", "CONF_STATUS_OBS"), UsageStatusType.CONDITIONAL,
                                    mockAttributeRelationshipPrimaryMeasureType(), null, (ConceptReferenceType[]) null));
                }

                {
                    // OBS_STATUS
                    attributeListType.getAttributesAndReportingYearStartDaies().add(mockAttributeType("OBS_STATUS", mockConceptReferenceType("SDMX", "CROSS_DOMAIN_CONCEPTS", "1.0", "OBS_STATUS"),
                            UsageStatusType.MANDATORY, mockAttributeRelationshipPrimaryMeasureType(), null, (ConceptReferenceType[]) null));
                }

                {
                    // TITLE
                    attributeListType.getAttributesAndReportingYearStartDaies().add(mockAttributeType("TITLE", mockConceptReferenceType("SDMX", "CROSS_DOMAIN_CONCEPTS", "1.0", "TITLE"),
                            UsageStatusType.MANDATORY, mockAttributeRelationshipGroupType("SiblingGroup"), null, (ConceptReferenceType[]) null));
                }

                dataStructureComponentsType.setAttributeList(attributeListType);
            }

            // MeasureList
            {
                MeasureListType measureListType = structureObjectFactory.createMeasureListType();
                measureListType.setPrimaryMeasure(mockPrimaryMeasureType(mockConceptReferenceType("SDMX", "CROSS_DOMAIN_CONCEPTS", "1.0", "OBS_VALUE"), null));
                dataStructureComponentsType.setMeasureList(measureListType);
            }

            dataStructureType.setDataStructureComponents(dataStructureComponentsType);
        }

        return dataStructureType;
    }

    public static DimensionType mockDimensionType(String code, ConceptReferenceType conceptIdentity, SimpleDataStructureRepresentationType localRepresentation, ConceptReferenceType... roles) {
        DimensionType dimensionType = structureObjectFactory.createDimensionType();
        dimensionType.setId(code);

        dimensionType.setConceptIdentity(conceptIdentity);
        dimensionType.setLocalRepresentation(localRepresentation);
        if (roles != null && roles.length > 0) {
            dimensionType.getConceptRoles().addAll(Arrays.asList(roles));
        }

        dimensionType.setType(DimensionTypeType.DIMENSION);
        return dimensionType;
    }

    public static TimeDimensionType mockTimeDimensionType(ConceptReferenceType conceptIdentity, TimeDataType timeDataType) {
        TimeDimensionType timeDimensionType = structureObjectFactory.createTimeDimensionType();

        timeDimensionType.setId("TIME_PERIOD");
        timeDimensionType.setConceptIdentity(conceptIdentity);
        timeDimensionType.setLocalRepresentation(mockTimeDimensionRepresentationType(timeDataType));

        timeDimensionType.setPosition(1);
        timeDimensionType.setType(DimensionTypeType.TIME_DIMENSION);

        return timeDimensionType;
    }

    public static TimeDimensionRepresentationType mockTimeDimensionRepresentationType(TimeDataType timeDataType) {
        TimeDimensionRepresentationType timeDimensionRepresentationType = structureObjectFactory.createTimeDimensionRepresentationType();

        TimeTextFormatType timeTextFormatType = new TimeTextFormatType();
        timeTextFormatType.setTextType(timeDataType);

        timeDimensionRepresentationType.setTextFormat(timeTextFormatType);

        return timeDimensionRepresentationType;
    }

    public static SimpleDataStructureRepresentationType mockEnumerationCodelist(CodelistReferenceType codelistReferenceType) {
        SimpleDataStructureRepresentationType simpleDataStructureRepresentationType = structureObjectFactory.createSimpleDataStructureRepresentationType();
        simpleDataStructureRepresentationType.setEnumeration(codelistReferenceType);

        return simpleDataStructureRepresentationType;
    }

    public static SimpleDataStructureRepresentationType mockSimpleDataStructureRepresentationType() {
        SimpleDataStructureRepresentationType simpleDataStructureRepresentationType = structureObjectFactory.createSimpleDataStructureRepresentationType();

        SimpleComponentTextFormatType simpleComponentTextFormatType = structureObjectFactory.createSimpleComponentTextFormatType();
        simpleComponentTextFormatType.setTextType(SimpleDataType.ALPHA_NUMERIC);
        simpleComponentTextFormatType.setMaxLength(new BigInteger("10"));

        simpleDataStructureRepresentationType.setTextFormat(simpleComponentTextFormatType);

        return simpleDataStructureRepresentationType;
    }

    public static GroupDimensionType mockGroupDimensionType(String dimensionID) {
        GroupDimensionType groupDimensionType = structureObjectFactory.createGroupDimensionType();
        groupDimensionType.setDimensionReference(mockLocalDimensionReferenceType(dimensionID));

        return groupDimensionType;
    }

    public static AttributeType mockAttributeType(String code, ConceptReferenceType conceptIdentity, UsageStatusType usageStatusType, AttributeRelationshipType attributeRelationshipType,
            SimpleDataStructureRepresentationType localRepresentation, ConceptReferenceType... roles) {
        AttributeType attributeType = structureObjectFactory.createAttributeType();

        attributeType.setId(code);

        attributeType.setConceptIdentity(conceptIdentity);
        attributeType.setLocalRepresentation(mockSimpleDataStructureRepresentationType());
        if (roles != null && roles.length > 0) {
            attributeType.getConceptRoles().addAll(Arrays.asList(roles));
        }

        attributeType.setAssignmentStatus(UsageStatusType.CONDITIONAL);
        attributeType.setAttributeRelationship(attributeRelationshipType);

        return attributeType;
    }

    public static AttributeRelationshipType mockAttributeRelationshipDimensionType(List<String> dimensionIDs, List<String> attachmentgroupIds) {
        AttributeRelationshipType attributeRelationshipType = structureObjectFactory.createAttributeRelationshipType();

        // Dimensions
        for (String dimensionID : dimensionIDs) {
            attributeRelationshipType.getDimensions().add(mockLocalDimensionReferenceType(dimensionID));
        }

        // Attachments groups
        if (attachmentgroupIds != null) {
            for (String attachmentgroupId : attachmentgroupIds) {
                attributeRelationshipType.getAttachmentGroups().add(mockLocalGroupKeyDescriptorReferenceType(attachmentgroupId));
            }
        }

        return attributeRelationshipType;
    }

    public static AttributeRelationshipType mockAttributeRelationshipGroupType(String groupID) {
        AttributeRelationshipType attributeRelationshipType = structureObjectFactory.createAttributeRelationshipType();

        attributeRelationshipType.setGroup(mockLocalGroupKeyDescriptorReferenceType(groupID));

        return attributeRelationshipType;
    }

    public static AttributeRelationshipType mockAttributeRelationshipPrimaryMeasureType() {
        AttributeRelationshipType attributeRelationshipType = structureObjectFactory.createAttributeRelationshipType();

        attributeRelationshipType.setPrimaryMeasure(mockLocalPrimaryMeasureReferenceType());

        return attributeRelationshipType;
    }

    public static PrimaryMeasureType mockPrimaryMeasureType(ConceptReferenceType conceptIdentity, SimpleDataStructureRepresentationType localRepresentation) {

        PrimaryMeasureType primaryMeasureType = structureObjectFactory.createPrimaryMeasureType();

        primaryMeasureType.setId("OBS_VALUE");
        primaryMeasureType.setConceptIdentity(conceptIdentity);
        primaryMeasureType.setLocalRepresentation(localRepresentation);

        return primaryMeasureType;
    }

    public static Categorisation mockCategorisation(DatasetVersion datasetVersion, String maintainerCode, String categoryCode, DateTime validFrom, DateTime validTo) {
        Categorisation categorisation = new Categorisation();
        categorisation.setDatasetVersion(datasetVersion);
        categorisation.setCategory(StatisticalResourcesDoMocks.mockCategoryExternalItem(categoryCode));
        categorisation.setVersionableStatisticalResource(mockVersionableStatisticalResource(new VersionableStatisticalResource(), TypeRelatedResourceEnum.CATEGORISATION));
        categorisation.getVersionableStatisticalResource().setTitle(null); // autogenerated by service
        categorisation.setMaintainer(StatisticalResourcesDoMocks.mockAgencyExternalItem(maintainerCode, "ISTAC." + maintainerCode));
        categorisation.getVersionableStatisticalResource().setValidTo(validTo);
        categorisation.getVersionableStatisticalResource().setValidTo(validFrom);

        return categorisation;
    }

    protected static VersionableStatisticalResource mockVersionableStatisticalResource(VersionableStatisticalResource resource, TypeRelatedResourceEnum artefactType) {
        mockNameableStatisticalResorce(resource, artefactType);
        return resource;
    }

    protected static NameableStatisticalResource mockNameableStatisticalResorce(NameableStatisticalResource resource, TypeRelatedResourceEnum artefactType) {
        mockIdentifiableStatisticalResource(resource, artefactType);
        String resourceCode = resource.getCode();

        if (resource.getTitle() == null) {
            resource.setTitle(mockInternationalStringMetadata(resourceCode, "title"));
        }

        if (resource.getDescription() == null) {
            resource.setDescription(mockInternationalStringMetadata(resourceCode, "description"));
        }

        return resource;
    }

    protected static IdentifiableStatisticalResource mockIdentifiableStatisticalResource(IdentifiableStatisticalResource resource, TypeRelatedResourceEnum artefactType) {
        mockStatisticalResource(resource);
        return resource;
    }

    protected static StatisticalResource mockStatisticalResource(StatisticalResource resource) {
        return resource;
    }

    public static InternationalString mockInternationalStringMetadata(String resource, String metadata) {
        assertNotNull(metadata);

        InternationalString internationalString = new InternationalString();
        internationalString.setVersion(Long.valueOf(0));
        {
            LocalisedString es = new LocalisedString();
            if (resource != null) {
                es.setLabel(metadata + "-" + resource + " en Espanol");
            } else {
                es.setLabel(metadata + " en Espanol");
            }
            es.setLocale("es");
            es.setVersion(Long.valueOf(0));
            internationalString.addText(es);
        }
        {
            LocalisedString en = new LocalisedString();
            if (resource != null) {
                en.setLabel(metadata + "-" + resource + " in English");
            } else {
                en.setLabel(metadata + " in English");
            }
            en.setLocale("en");
            en.setVersion(Long.valueOf(0));
            internationalString.addText(en);
        }
        return internationalString;
    }
}