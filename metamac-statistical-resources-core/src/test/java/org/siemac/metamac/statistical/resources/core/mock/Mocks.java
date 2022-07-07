package org.siemac.metamac.statistical.resources.core.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.Mockito;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Attribute;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AttributeUsageStatusType;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Attributes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Concept;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Concepts;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructureComponents;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataType;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Dimensions;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Groups;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ItemResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Measure;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Representation;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.TextFormat;
import org.siemac.metamac.statistical.resources.core.constants.StatisticalResourcesConstants;
import org.siemac.metamac.statistical.resources.core.invocation.service.SrmRestInternalService;
import org.siemac.metamac.statistical.resources.core.utils.SrmMockUtils;

import com.arte.statistic.parser.sdmx.v2_1.domain.IdValuePair;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceDto;
import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceObservationDto;
import es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto;
import es.gobcan.istac.edatos.dataset.repository.dto.LocalisedStringDto;

public class Mocks {

    // --------------------------------------------------------------------
    // LIST OF CODES
    // --------------------------------------------------------------------

    public static Codes mock_CL_DECIMALS() {
        Codes codes = new Codes();

        codes.getCodes().add(SrmMockUtils.buildCode("0", "Zero", "en", "ZERO"));
        codes.getCodes().add(SrmMockUtils.buildCode("1", "One", "en", "ONE"));
        codes.getCodes().add(SrmMockUtils.buildCode("2", "Two", "en", "TWO"));
        codes.getCodes().add(SrmMockUtils.buildCode("3", "Three", "en", "THREE"));
        codes.getCodes().add(SrmMockUtils.buildCode("4", "Four", "en", "FOUR"));
        codes.getCodes().add(SrmMockUtils.buildCode("5", "Five", "en", "FIVE"));
        codes.getCodes().add(SrmMockUtils.buildCode("6", "Six", "en", "SIX"));
        codes.getCodes().add(SrmMockUtils.buildCode("7", "Seven", "en", "SEVEN"));
        codes.getCodes().add(SrmMockUtils.buildCode("8", "Eight", "en", "EIGHT"));
        codes.getCodes().add(SrmMockUtils.buildCode("9", "Nine", "en", "NINE"));

        return codes;
    }

    public static Codes mock_CL_FREQ() {
        Codes codes = new Codes();

        codes.getCodes().add(SrmMockUtils.buildCode("A", "Annual", "en", "ANNUAL"));
        codes.getCodes().add(SrmMockUtils.buildCode("B", "Daily - business week", "en", "DAILY_B"));
        codes.getCodes().add(SrmMockUtils.buildCode("D", "Daily", "en", "DAILY_D"));
        codes.getCodes().add(SrmMockUtils.buildCode("M", "Monthly", "en", "MONTHLY"));
        codes.getCodes().add(SrmMockUtils.buildCode("N", "Minutely", "en", "MINUTELY"));
        codes.getCodes().add(SrmMockUtils.buildCode("Q", "Quarterly", "en", "QUARTERLY"));
        codes.getCodes().add(SrmMockUtils.buildCode("S", "Half Yearly, semester", "en", "SEMESTER"));
        codes.getCodes().add(SrmMockUtils.buildCode("W", "Weekly", "en", "WEEKLY"));

        return codes;
    }

    public static Codes mock_CL_CONF_STATUS() {
        Codes codes = new Codes();

        codes.getCodes().add(SrmMockUtils.buildCode("C", "Confidential statistical information", "en", "CONF_STATUS_C"));
        codes.getCodes().add(SrmMockUtils.buildCode("D", "Secondary confidentiality set by the sender, not for   publication", "en", "CONF_STATUS_D"));
        codes.getCodes().add(SrmMockUtils.buildCode("F", "Free", "en", "CONF_STATUS_F"));
        codes.getCodes().add(SrmMockUtils.buildCode("N", "Not for publication, restricted for internal use only", "en", "CONF_STATUS_N"));
        codes.getCodes().add(SrmMockUtils.buildCode("S", "Secondary confidentiality set and managed by the receiver, not for publication", "en", "CONF_STATUS_S"));

        return codes;
    }

    public static Codes mock_CL_OBS_STATUS() {
        Codes codes = new Codes();

        codes.getCodes().add(SrmMockUtils.buildCode("A", "Normal", "en", "OBS_STATUS_A"));
        codes.getCodes().add(SrmMockUtils.buildCode("B", "Break", "en", "OBS_STATUS_B"));
        codes.getCodes().add(SrmMockUtils.buildCode("E", "Estimated value", "en", "OBS_STATUS_E"));
        codes.getCodes().add(SrmMockUtils.buildCode("F", "Forecast value", "en", "OBS_STATUS_F"));
        codes.getCodes().add(SrmMockUtils.buildCode("I", "Imputed value (CCSA definition)", "en", "OBS_STATUS_I"));
        codes.getCodes().add(SrmMockUtils.buildCode("M", "Missing value", "en", "OBS_STATUS_M"));
        codes.getCodes().add(SrmMockUtils.buildCode("P", "Provisional value", "en", "OBS_STATUS_P"));
        codes.getCodes().add(SrmMockUtils.buildCode("S", "Strike", "en", "OBS_STATUS_S"));

        return codes;
    }

    public static Codes mock_CL_UNIT_MULT() {
        Codes codes = new Codes();

        codes.getCodes().add(SrmMockUtils.buildCode("0", "Units", "en", "UNITS"));
        codes.getCodes().add(SrmMockUtils.buildCode("1", "Tens", "en", "TENS"));
        codes.getCodes().add(SrmMockUtils.buildCode("2", "Hundreds", "en", "HUNDREDS"));
        codes.getCodes().add(SrmMockUtils.buildCode("3", "Thousands", "en", "THOUSANDS"));
        codes.getCodes().add(SrmMockUtils.buildCode("4", "Tens of thousands", "en", "TENS_OF_THOUSANDS"));
        codes.getCodes().add(SrmMockUtils.buildCode("6", "Millions", "en", "MILLIONS"));
        codes.getCodes().add(SrmMockUtils.buildCode("9", "Billions", "en", "BILLIONS"));
        codes.getCodes().add(SrmMockUtils.buildCode("12", "Trillions", "en", "TRILLIONS"));
        codes.getCodes().add(SrmMockUtils.buildCode("15", "Quadrillions", "en", "QUADRILLIONS"));

        return codes;
    }

    public static Codes mock_CL_EXR_TYPE() {
        Codes codes = new Codes();

        codes.getCodes().add(SrmMockUtils.buildCode("CR00", "Official fixing", "en", "CR00"));
        codes.getCodes().add(SrmMockUtils.buildCode("RR00", "Reference rate", "en", "RR00"));
        codes.getCodes().add(SrmMockUtils.buildCode("SP00", "Spot", "en", "SP00"));

        return codes;
    }

    public static Codes mock_CL_EXR_VAR() {
        Codes codes = new Codes();

        codes.getCodes().add(SrmMockUtils.buildCode("E", "End-of-period", "en", "E"));
        codes.getCodes().add(SrmMockUtils.buildCode("P", "Growth rate over previous period", "en", "P"));

        return codes;
    }

    public static Codes mock_CL_CURRENCY() {
        Codes codes = new Codes();

        codes.getCodes().add(SrmMockUtils.buildCode("CHF", "Swiss franc", "en", "CHF"));
        codes.getCodes().add(SrmMockUtils.buildCode("EUR", "Euro", "en", "EUR"));
        codes.getCodes().add(SrmMockUtils.buildCode("GBP", "Pound sterling", "en", "GBP"));
        codes.getCodes().add(SrmMockUtils.buildCode("JPY", "Japanese yen", "en", "JPY"));
        codes.getCodes().add(SrmMockUtils.buildCode("USD", "United States dollar", "en", "USD"));

        return codes;
    }

    public static Codes mock_CL_DEMO() {
        Codes codes = new Codes();

        codes.getCodes().add(SrmMockUtils.buildCode("swiss", "Swiss franc", "en", "CHF"));
        codes.getCodes().add(SrmMockUtils.buildCode("euro", "Euro", "en", "EUR"));
        codes.getCodes().add(SrmMockUtils.buildCode("pound", "Pound sterling", "en", "GBP"));
        codes.getCodes().add(SrmMockUtils.buildCode("yen", "Japanese yen", "en", "JPY"));
        codes.getCodes().add(SrmMockUtils.buildCode("dollar", "United States dollar", "en", "USD"));

        return codes;
    }

    // --------------------------------------------------------------------
    // LIST OF CONCEPTS
    // --------------------------------------------------------------------

    public static Concepts mock_CROSS_DOMAIN_CONCEPTS() {
        Concepts concepts = new Concepts();

        concepts.getConcepts().add(mock_resource_SDMX_CROSS_DOMAIN_CONCEPTS_1_0_COLL_METHOD());
        concepts.getConcepts().add(SrmMockUtils.buildConcept("CONF", "Confidentiality"));
        concepts.getConcepts().add(mock_resource_SDMX_CROSS_DOMAIN_CONCEPTS_1_0_CONF_STATUS());
        concepts.getConcepts().add(SrmMockUtils.buildConcept("CURRENCY", "Currency"));
        concepts.getConcepts().add(mock_resource_SDMX_CROSS_DOMAIN_CONCEPTS_1_0_DECIMALS());
        concepts.getConcepts().add(mock_resource_SDMX_CROSS_DOMAIN_CONCEPTS_1_0_FREQ());
        concepts.getConcepts().add(SrmMockUtils.buildConcept("OBS_VALUE", "Observation value"));
        concepts.getConcepts().add(mock_resource_SDMX_CROSS_DOMAIN_CONCEPTS_1_0_OBS_STATUS());
        concepts.getConcepts().add(SrmMockUtils.buildConcept("TIME_PERIOD", "Time Period"));
        concepts.getConcepts().add(mock_resource_SDMX_CROSS_DOMAIN_1_0_TITLE());
        concepts.getConcepts().add(mock_resource_SDMX_CROSS_DOMAIN_CONCEPTS_1_0_UNIT_MULT());
        concepts.getConcepts().add(SrmMockUtils.buildConcept("UNIT_MEASURE", "Unit of Measure"));

        return concepts;
    }

    public static Concepts mock_ECB_CONCEPTS() {
        Concepts concepts = new Concepts();

        concepts.getConcepts().add(mock_resource_ECB_CONCEPTS_1_0_CURRENCY_DENOM());
        concepts.getConcepts().add(mock_resource_ECB_CONCEPTS_1_0_EXR_VAR());
        concepts.getConcepts().add(mock_resource_ECB_CONCEPTS_1_0_EXR_TYPE());

        return concepts;
    }

    // --------------------------------------------------------------------
    // CONCEPTS: RESOURCE INTERNAL (for retrieveConceptsOfConceptScheme)
    // --------------------------------------------------------------------

    public static ItemResourceInternal mock_resource_SDMX_CROSS_DOMAIN_1_0_TITLE() {
        return SrmMockUtils.buildConcept("TITLE", "Title");
    }

    public static ItemResourceInternal mock_resource_SDMX_CROSS_DOMAIN_CONCEPTS_1_0_OBS_STATUS() {
        return SrmMockUtils.buildConcept("OBS_STATUS", "Observation value");
    }

    public static ItemResourceInternal mock_resource_SDMX_CROSS_DOMAIN_CONCEPTS_1_0_CONF_STATUS() {
        return SrmMockUtils.buildConcept("CONF_STATUS", "Confidentiality - status");
    }

    public static ItemResourceInternal mock_resource_SDMX_CROSS_DOMAIN_CONCEPTS_1_0_UNIT_MULT() {
        return SrmMockUtils.buildConcept("UNIT_MULT", "Unit Multiplier");
    }

    public static ItemResourceInternal mock_resource_SDMX_CROSS_DOMAIN_CONCEPTS_1_0_COLL_METHOD() {
        return SrmMockUtils.buildConcept("COLL_METHOD", "Data Collection Method");
    }

    public static ItemResourceInternal mock_resource_SDMX_CROSS_DOMAIN_CONCEPTS_1_0_FREQ() {
        return SrmMockUtils.buildConcept("FREQ", "Frequency");
    }

    public static ItemResourceInternal mock_resource_ECB_CONCEPTS_1_0_EXR_VAR() {
        return SrmMockUtils.buildConcept("EXR_VAR", "Series variation - EXR context");
    }

    public static ItemResourceInternal mock_resource_ECB_CONCEPTS_1_0_EXR_TYPE() {
        return SrmMockUtils.buildConcept("EXR_TYPE", "Exchange rate type");
    }

    public static ItemResourceInternal mock_resource_ECB_CONCEPTS_1_0_CURRENCY_DENOM() {
        return SrmMockUtils.buildConcept("CURRENCY_DENOM", "Currency denominator");
    }

    public static ItemResourceInternal mock_resource_SDMX_CROSS_DOMAIN_CONCEPTS_1_0_DECIMALS() {
        return SrmMockUtils.buildConcept("DECIMALS", "Decimals");
    }

    // --------------------------------------------------------------------
    // CONCEPTS (for retrieveConcept)
    // --------------------------------------------------------------------

    public static Concept mock_SDMX_CROSS_DOMAIN_1_0_OBS_VALUE() {
        return SrmMockUtils.buildConcept("OBS_VALUE", "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX:CROSS_DOMAIN_CONCEPTS(1.0).OBS_VALUE", "Observation value",
                SrmMockUtils.buildConceptRepresentation(DataType.STRING));
    }

    public static Concept mock_SDMX_CROSS_DOMAIN_1_0_TITLE() {
        return SrmMockUtils.buildConcept("TITLE", "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX:CROSS_DOMAIN_CONCEPTS(1.0).TITLE", "Title",
                SrmMockUtils.buildConceptRepresentation(DataType.STRING));
    }

    public static Concept mock_SDMX_CROSS_DOMAIN_CONCEPTS_1_0_OBS_STATUS() {
        return SrmMockUtils.buildConcept("OBS_STATUS", "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX:CROSS_DOMAIN_CONCEPTS(1.0).OBS_STATUS", "Observation value",
                SrmMockUtils.buildConceptRepresentation("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX:CL_OBS_STATUS(1.0)"));
    }

    public static Concept mock_SDMX_CROSS_DOMAIN_CONCEPTS_1_0_CONF_STATUS() {
        return SrmMockUtils.buildConcept("CONF_STATUS", "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX:CROSS_DOMAIN_CONCEPTS(1.0).CONF_STATUS", "Confidentiality - status",
                SrmMockUtils.buildConceptRepresentation("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX:CL_CONF_STATUS(1.0)"));
    }

    public static Concept mock_SDMX_CROSS_DOMAIN_CONCEPTS_1_0_UNIT_MULT() {
        return SrmMockUtils.buildConcept("UNIT_MULT", "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX:CROSS_DOMAIN_CONCEPTS(1.0).UNIT_MULT", "Unit Multiplier",
                SrmMockUtils.buildConceptRepresentation("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX:CL_UNIT_MULT(1.0)"));
    }

    public static Concept mock_SDMX_CROSS_DOMAIN_CONCEPTS_1_0_COLL_METHOD() {
        return SrmMockUtils.buildConcept("COLL_METHOD", "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX:CROSS_DOMAIN_CONCEPTS(1.0).COLL_METHOD", "Data Collection Method",
                SrmMockUtils.buildConceptRepresentation(DataType.STRING));
    }

    public static Concept mock_SDMX_CROSS_DOMAIN_CONCEPTS_1_0_FREQ() {
        return SrmMockUtils.buildConcept("FREQ", "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX:CROSS_DOMAIN_CONCEPTS(1.0).FREQ", "Frequency",
                SrmMockUtils.buildConceptRepresentation("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX:CL_FREQ(1.0)"));
    }

    public static Concept mock_SDMX_CROSS_DOMAIN_CONCEPTS_1_0_DECIMALS() {
        return SrmMockUtils.buildConcept("DECIMALS", "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX:CROSS_DOMAIN_CONCEPTS(1.0).DECIMALS", "Decimals",
                SrmMockUtils.buildConceptRepresentation("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX:CL_DECIMALS(1.0)"));
    }

    public static Concept mock_ECB_CONCEPTS_1_0_EXR_VAR() {
        return SrmMockUtils.buildConcept("EXR_VAR", "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ECB:ECB_CONCEPTS(1.0).EXR_VAR", "Series variation - EXR context",
                SrmMockUtils.buildConceptRepresentation("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=ECB:CL_EXR_VAR(1.0)"));
    }

    public static Concept mock_ECB_CONCEPTS_1_0_EXR_TYPE() {
        return SrmMockUtils.buildConcept("EXR_TYPE", "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ECB:ECB_CONCEPTS(1.0).EXR_TYPE", "Exchange rate type",
                SrmMockUtils.buildConceptRepresentation("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=ECB:CL_EXR_TYPE(1.0)"));
    }

    public static Concept mock_ECB_CONCEPTS_1_0_CURRENCY_DENOM() {
        return SrmMockUtils.buildConcept("CURRENCY_DENOM", "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ECB:ECB_CONCEPTS(1.0).CURRENCY_DENOM", "Currency denominator",
                SrmMockUtils.buildConceptRepresentation("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=ISO:CL_CURRENCY(1.0)"));
    }

    // --------------------------------------------------------------------
    // DSD
    // --------------------------------------------------------------------

    public static DataStructure mock_DSD_ECB_EXR_RG() {
        DataStructure dataStructure = new DataStructure();

        // Dsd attributes
        dataStructure.setAgencyID("ECB");
        dataStructure.setVersion("1.0");
        dataStructure.setId("ECB_EXR_RG");
        dataStructure.setUrn("urn:sdmx:org.sdmx.infomodel.datastructure.DataStructure=ECB:ECB_EXR_RG(1.0)");
        dataStructure.setName(SrmMockUtils.buildInternationalStringResource("Sample Data Structure Definition for exchange rates, with sibling and rate groups defined.", "en"));

        dataStructure.setDataStructureComponents(new DataStructureComponents());
        // Components: Dimensions
        Dimensions dimensions = new Dimensions();
        dimensions.getDimensions().add(SrmMockUtils.buildDimension("FREQ", "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX:CROSS_DOMAIN_CONCEPTS(1.0).FREQ", null));

        dimensions.getDimensions().add(SrmMockUtils.buildDimension("CURRENCY", "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX:CROSS_DOMAIN_CONCEPTS(1.0).FREQ",
                "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=ISO:CL_CURRENCY(1.0)"));

        dimensions.getDimensions().add(SrmMockUtils.buildDimension("CURRENCY_DENOM", "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ECB:ECB_CONCEPTS(1.0).CURRENCY_DENOM", null));

        dimensions.getDimensions().add(SrmMockUtils.buildDimension("EXR_TYPE", "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ECB:ECB_CONCEPTS(1.0).EXR_TYPE", null));

        dimensions.getDimensions().add(SrmMockUtils.buildDimension("EXR_VAR", "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ECB:ECB_CONCEPTS(1.0).EXR_VAR", null));

        dimensions.getDimensions().add(
                SrmMockUtils.buildTimeDimension("TIME_PERIOD", "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX:CROSS_DOMAIN_CONCEPTS(1.0).TIME_PERIOD", DataType.OBSERVATIONAL_TIME_PERIOD));

        dataStructure.getDataStructureComponents().setDimensions(dimensions);

        // Components: Groups
        dataStructure.getDataStructureComponents().setGroups(new Groups());
        dataStructure.getDataStructureComponents().getGroups().getGroups().add(SrmMockUtils.buildGroupType("SiblingGroup", "CURRENCY", "CURRENCY_DENOM", "EXR_TYPE", "EXR_VAR"));
        dataStructure.getDataStructureComponents().getGroups().getGroups().add(SrmMockUtils.buildGroupType("RateGroup", "EXR_TYPE", "EXR_VAR"));

        // Components: Attributes
        Attributes attributes = new Attributes();

        attributes.getAttributes().add(SrmMockUtils.buildAttributeTypeWithGroupRelationship("COLL_METHOD",
                "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX:CROSS_DOMAIN_CONCEPTS(1.0).COLL_METHOD", null, AttributeUsageStatusType.CONDITIONAL, "RateGroup"));

        attributes.getAttributes()
                .add(SrmMockUtils.buildAttributeTypeWithDimensionRelationship("DECIMALS", "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX:CROSS_DOMAIN_CONCEPTS(1.0).DECIMALS", null,
                        AttributeUsageStatusType.MANDATORY, Arrays.asList("CURRENCY", "CURRENCY_DENOM", "EXR_TYPE"), Arrays.asList("SiblingGroup")));

        attributes.getAttributes()
                .add(SrmMockUtils.buildAttributeTypeWithDimensionRelationship("UNIT_MEASURE", "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX:CROSS_DOMAIN_CONCEPTS(1.0).UNIT_MEASURE",
                        "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=ISO:CL_CURRENCY(1.0)", AttributeUsageStatusType.MANDATORY, Arrays.asList("CURRENCY", "CURRENCY_DENOM", "EXR_TYPE"),
                        Arrays.asList("SiblingGroup")));

        attributes.getAttributes()
                .add(SrmMockUtils.buildAttributeTypeWithDimensionRelationship("UNIT_MULT", "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX:CROSS_DOMAIN_CONCEPTS(1.0).UNIT_MULT", null,
                        AttributeUsageStatusType.MANDATORY, Arrays.asList("CURRENCY", "CURRENCY_DENOM", "EXR_TYPE"), Arrays.asList("SiblingGroup")));

        attributes.getAttributes().add(SrmMockUtils.buildAttributeTypeWithPrimaryMeasureRelationship("CONF_STATUS_OBS",
                "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX:CROSS_DOMAIN_CONCEPTS(1.0).CONF_STATUS", null, AttributeUsageStatusType.CONDITIONAL));

        attributes.getAttributes().add(SrmMockUtils.buildAttributeTypeWithPrimaryMeasureRelationship("OBS_STATUS",
                "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX:CROSS_DOMAIN_CONCEPTS(1.0).OBS_STATUS", null, AttributeUsageStatusType.MANDATORY));

        attributes.getAttributes().add(SrmMockUtils.buildAttributeTypeWithGroupRelationship("TITLE", "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX:CROSS_DOMAIN_CONCEPTS(1.0).TITLE", null,
                AttributeUsageStatusType.MANDATORY, "SiblingGroup"));

        dataStructure.getDataStructureComponents().setAttributes(attributes);

        // Components: Primary Measure
        Measure measure = new Measure();
        measure.setPrimaryMeasure(SrmMockUtils.buildPrimaryMeasure("urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX:CROSS_DOMAIN_CONCEPTS(1.0).OBS_VALUE", null));
        dataStructure.getDataStructureComponents().setMeasure(measure);

        return dataStructure;
    }

    /**
     * mock the DSD_ECB_EXR_RG dsd's without mandatory in attribute observation and add a attribute observation level with OBS_NOTE code.
     *
     * @return
     */
    public static DataStructure mock_DSD_ECB_EXR_RG_for_PX() {
        DataStructure dataStructure = mock_DSD_ECB_EXR_RG();

        for (Object attribute : dataStructure.getDataStructureComponents().getAttributes().getAttributes()) {

            if (attribute instanceof Attribute) {
                ((Attribute) attribute).setAssignmentStatus(AttributeUsageStatusType.CONDITIONAL);
            }

        }

        {
            // Add OBS_NOTE attribute
            Attribute obsNote = SrmMockUtils.buildAttributeTypeWithPrimaryMeasureRelationship("OBS_NOTE",
                    "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX:CROSS_DOMAIN_CONCEPTS(1.0).CONF_STATUS", null, AttributeUsageStatusType.CONDITIONAL);

            // Not enumerated representation
            Representation simpleDataStructureRepresentationType = new Representation();
            TextFormat simpleComponentTextFormatType = new TextFormat();
            simpleComponentTextFormatType.setTextType(DataType.STRING);
            simpleDataStructureRepresentationType.setTextFormat(simpleComponentTextFormatType);
            obsNote.setLocalRepresentation(simpleDataStructureRepresentationType);

            dataStructure.getDataStructureComponents().getAttributes().getAttributes().add(obsNote);
        }

        {
            // Add OBS_CONF attribute
            Attribute obsConf = SrmMockUtils.buildAttributeTypeWithPrimaryMeasureRelationship("OBS_CONF",
                    "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX:CROSS_DOMAIN_CONCEPTS(1.0).CONF_STATUS", null, AttributeUsageStatusType.CONDITIONAL);

            // Not enumerated representation
            Representation simpleDataStructureRepresentationType = new Representation();
            TextFormat simpleComponentTextFormatType = new TextFormat();
            simpleComponentTextFormatType.setTextType(DataType.STRING);
            simpleDataStructureRepresentationType.setTextFormat(simpleComponentTextFormatType);
            obsConf.setLocalRepresentation(simpleDataStructureRepresentationType);

            dataStructure.getDataStructureComponents().getAttributes().getAttributes().add(obsConf);

        }

        return dataStructure;
    }

    public static List<AttributeInstanceDto> mockAttributesInstances() {
        List<AttributeInstanceDto> attributeInstanceDtos = new ArrayList<AttributeInstanceDto>();

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

            attributeInstanceDtos.add(attributeInstanceDto);
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

            attributeInstanceDtos.add(attributeInstanceDto);
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

            attributeInstanceDtos.add(attributeInstanceDto);
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

            attributeInstanceDtos.add(attributeInstanceDto);
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

            attributeInstanceDtos.add(attributeInstanceDto);
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

            attributeInstanceDtos.add(attributeInstanceDto);
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

            attributeInstanceDtos.add(attributeInstanceDto);
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

            attributeInstanceDtos.add(attributeInstanceDto);
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

            attributeInstanceDtos.add(attributeInstanceDto);
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

            attributeInstanceDtos.add(attributeInstanceDto);
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

            attributeInstanceDtos.add(attributeInstanceDto);
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

            attributeInstanceDtos.add(attributeInstanceDto);
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

            attributeInstanceDtos.add(attributeInstanceDto);
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

            attributeInstanceDtos.add(attributeInstanceDto);
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

            attributeInstanceDtos.add(attributeInstanceDto);
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

            attributeInstanceDtos.add(attributeInstanceDto);
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

            attributeInstanceDtos.add(attributeInstanceDto);
        }

        return attributeInstanceDtos;
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

    public static void mockRestService(SrmRestInternalService srmRestInternalService) throws MetamacException {
        // DSD
        Mockito.doReturn(Mocks.mock_DSD_ECB_EXR_RG()).when(srmRestInternalService).retrieveDsdByUrn(Mockito.anyString());

        // CODELIST
        Mockito.doReturn(Mocks.mock_CL_DECIMALS()).when(srmRestInternalService).retrieveCodesOfCodelistEfficiently("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX:CL_DECIMALS(1.0)");
        Mockito.doReturn(Mocks.mock_CL_FREQ()).when(srmRestInternalService).retrieveCodesOfCodelistEfficiently("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX:CL_FREQ(1.0)");
        Mockito.doReturn(Mocks.mock_CL_CONF_STATUS()).when(srmRestInternalService).retrieveCodesOfCodelistEfficiently("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX:CL_CONF_STATUS(1.0)");
        Mockito.doReturn(Mocks.mock_CL_OBS_STATUS()).when(srmRestInternalService).retrieveCodesOfCodelistEfficiently("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX:CL_OBS_STATUS(1.0)");
        Mockito.doReturn(Mocks.mock_CL_UNIT_MULT()).when(srmRestInternalService).retrieveCodesOfCodelistEfficiently("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX:CL_UNIT_MULT(1.0)");
        Mockito.doReturn(Mocks.mock_CL_EXR_TYPE()).when(srmRestInternalService).retrieveCodesOfCodelistEfficiently("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=ECB:CL_EXR_TYPE(1.0)");
        Mockito.doReturn(Mocks.mock_CL_EXR_VAR()).when(srmRestInternalService).retrieveCodesOfCodelistEfficiently("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=ECB:CL_EXR_VAR(1.0)");
        Mockito.doReturn(Mocks.mock_CL_CURRENCY()).when(srmRestInternalService).retrieveCodesOfCodelistEfficiently("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=ISO:CL_CURRENCY(1.0)");
        Mockito.doReturn(Mocks.mock_CL_DEMO()).when(srmRestInternalService).retrieveCodesOfCodelistEfficiently("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX:CL_DEMO(1.0)");

        // CONCEPT SCHEME
        Mockito.doReturn(Mocks.mock_CROSS_DOMAIN_CONCEPTS()).when(srmRestInternalService)
                .retrieveConceptsOfConceptSchemeEfficiently("urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX:CROSS_DOMAIN_CONCEPTS(1.0)");

        Mockito.doReturn(Mocks.mock_ECB_CONCEPTS()).when(srmRestInternalService)
                .retrieveConceptsOfConceptSchemeEfficiently("urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=ECB:ECB_CONCEPTS(1.0)");

        // CONCEPT
        Mockito.doReturn(Mocks.mock_SDMX_CROSS_DOMAIN_CONCEPTS_1_0_FREQ()).when(srmRestInternalService)
                .retrieveConceptByUrn("urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX:CROSS_DOMAIN_CONCEPTS(1.0).FREQ");

        Mockito.doReturn(Mocks.mock_ECB_CONCEPTS_1_0_CURRENCY_DENOM()).when(srmRestInternalService)
                .retrieveConceptByUrn("urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ECB:ECB_CONCEPTS(1.0).CURRENCY_DENOM");

        Mockito.doReturn(Mocks.mock_ECB_CONCEPTS_1_0_EXR_TYPE()).when(srmRestInternalService).retrieveConceptByUrn("urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ECB:ECB_CONCEPTS(1.0).EXR_TYPE");

        Mockito.doReturn(Mocks.mock_ECB_CONCEPTS_1_0_EXR_VAR()).when(srmRestInternalService).retrieveConceptByUrn("urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ECB:ECB_CONCEPTS(1.0).EXR_VAR");

        Mockito.doReturn(Mocks.mock_SDMX_CROSS_DOMAIN_CONCEPTS_1_0_COLL_METHOD()).when(srmRestInternalService)
                .retrieveConceptByUrn("urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX:CROSS_DOMAIN_CONCEPTS(1.0).COLL_METHOD");

        Mockito.doReturn(Mocks.mock_SDMX_CROSS_DOMAIN_CONCEPTS_1_0_DECIMALS()).when(srmRestInternalService)
                .retrieveConceptByUrn("urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX:CROSS_DOMAIN_CONCEPTS(1.0).DECIMALS");

        Mockito.doReturn(Mocks.mock_SDMX_CROSS_DOMAIN_CONCEPTS_1_0_UNIT_MULT()).when(srmRestInternalService)
                .retrieveConceptByUrn("urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX:CROSS_DOMAIN_CONCEPTS(1.0).UNIT_MULT");

        Mockito.doReturn(Mocks.mock_SDMX_CROSS_DOMAIN_CONCEPTS_1_0_CONF_STATUS()).when(srmRestInternalService)
                .retrieveConceptByUrn("urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX:CROSS_DOMAIN_CONCEPTS(1.0).CONF_STATUS");

        Mockito.doReturn(Mocks.mock_SDMX_CROSS_DOMAIN_CONCEPTS_1_0_OBS_STATUS()).when(srmRestInternalService)
                .retrieveConceptByUrn("urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX:CROSS_DOMAIN_CONCEPTS(1.0).OBS_STATUS");

        Mockito.doReturn(Mocks.mock_SDMX_CROSS_DOMAIN_1_0_TITLE()).when(srmRestInternalService)
                .retrieveConceptByUrn("urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX:CROSS_DOMAIN_CONCEPTS(1.0).TITLE");

        Mockito.doReturn(Mocks.mock_SDMX_CROSS_DOMAIN_1_0_TITLE()).when(srmRestInternalService)
                .retrieveConceptByUrn("urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX:CROSS_DOMAIN_CONCEPTS(1.0).OBS_VALUE");
    }
}
