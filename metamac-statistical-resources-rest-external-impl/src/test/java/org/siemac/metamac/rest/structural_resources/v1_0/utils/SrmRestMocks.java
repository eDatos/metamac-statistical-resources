package org.siemac.metamac.rest.structural_resources.v1_0.utils;

import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.ATTRIBUTE_10_OBSERVATION;
import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.ATTRIBUTE_11_OBSERVATION;
import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.ATTRIBUTE_1_GLOBAL;
import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.ATTRIBUTE_2_GLOBAL;
import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.ATTRIBUTE_3_DIMENSION;
import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.ATTRIBUTE_4_DIMENSION;
import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.ATTRIBUTE_5_DIMENSION;
import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.ATTRIBUTE_6_DIMENSION;
import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.ATTRIBUTE_7_DIMENSION;
import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.ATTRIBUTE_8_DIMENSION;
import static org.siemac.metamac.rest.statistical_resources.constants.RestTestConstants.ATTRIBUTE_9_DIMENSION;
import static org.siemac.metamac.rest.structural_resources.v1_0.utils.RestMocks.mockInternationalString;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Agency;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Attribute;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.AttributeBase;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.AttributeQualifierType;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.AttributeRelationship;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Attributes;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CodeResource;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Codelist;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Codes;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Concept;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.ConceptResource;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Concepts;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Contact;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Contacts;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.DataStructure;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.DataStructureComponents;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.DataType;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Dimension;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.DimensionReferences;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.DimensionType;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Dimensions;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Empty;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Group;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Groups;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.MeasureDimension;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Quantity;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Representation;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.ShowDecimalPrecision;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.ShowDecimalPrecisions;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.TextFormat;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.TimeDimension;

public class SrmRestMocks {

    public static DataStructure mockDataStructure(String agencyID, String resourceID, String version) {
        DataStructure dataStructure = new DataStructure();
        dataStructure.setUrn("urn:sdmx:org.sdmx.infomodel.datastructure.DataStructure=" + agencyID + ":" + resourceID + "(" + version + ")");
        dataStructure.setUrnProvider(dataStructure.getUrn());
        dataStructure.setAgencyID(agencyID);
        dataStructure.setId(resourceID);
        dataStructure.setVersion(version);
        dataStructure.setAutoOpen(Boolean.TRUE);
        dataStructure.setShowNullValuesByDefault(Boolean.TRUE);
        dataStructure.setShowZeroValuesByDefault(Boolean.TRUE);

        dataStructure.setStub(new DimensionReferences());
        dataStructure.getStub().getDimensions().add("GEO_DIM");
        dataStructure.getStub().getDimensions().add("TIME_PERIOD");
        dataStructure.setHeading(new DimensionReferences());
        dataStructure.getHeading().getDimensions().add("measure01");
        dataStructure.getHeading().getDimensions().add("dim01");

        DataStructureComponents components = new DataStructureComponents();
        dataStructure.setDataStructureComponents(components);

        components.setDimensions(new Dimensions());
        components.getDimensions().getDimensions().add(mockDimension("GEO_DIM", Boolean.TRUE));
        components.getDimensions().getDimensions().add(mockTimeDimension("TIME_PERIOD"));
        components.getDimensions().getDimensions().add(mockMeasureDimension("measure01"));
        components.getDimensions().getDimensions().add(mockDimension("dim01", Boolean.FALSE));

        components.setGroups(new Groups());
        components.getGroups().getGroups().add(mockGroup("group01", Arrays.asList("TIME_PERIOD", "GEO_DIM")));
        components.getGroups().getGroups().add(mockGroup("group02", Arrays.asList("GEO_DIM", "TIME_PERIOD", "dim01")));

        components.setAttributes(new Attributes());
        components.getAttributes().getAttributes().add(mockAttributeDataset(ATTRIBUTE_1_GLOBAL, null, null));
        components.getAttributes().getAttributes().add(mockAttributeDataset(ATTRIBUTE_2_GLOBAL, mockCodelistResource("agency01", "abc", "01.000"), null));
        components.getAttributes().getAttributes().add(mockAttributeDimension(ATTRIBUTE_3_DIMENSION, null, Arrays.asList("GEO_DIM"), null, null));
        components.getAttributes().getAttributes().add(mockAttributeDimension(ATTRIBUTE_4_DIMENSION, null, Arrays.asList("GEO_DIM", "dim01"), null, null));
        components.getAttributes().getAttributes().add(mockAttributeDimension(ATTRIBUTE_5_DIMENSION, AttributeQualifierType.TIME, Arrays.asList("GEO_DIM", "TIME_PERIOD"), null, null));
        components.getAttributes().getAttributes()
                .add(mockAttributeDimension(ATTRIBUTE_6_DIMENSION, null, Arrays.asList("GEO_DIM", "TIME_PERIOD", "measure01"), null, mockConceptSchemeResource("agency01", "123", "01.000")));
        components.getAttributes().getAttributes().add(mockAttributeDimension(ATTRIBUTE_7_DIMENSION, null, Arrays.asList("GEO_DIM", "dim01", "measure01", "TIME_PERIOD"), null, null));
        components.getAttributes().getAttributes().add(mockAttributeGroup(ATTRIBUTE_8_DIMENSION, "group01", null, null));
        components.getAttributes().getAttributes().add(mockAttributeGroup(ATTRIBUTE_9_DIMENSION, "group02", null, null));
        components.getAttributes().getAttributes().add(mockAttributePrimaryMeasure(ATTRIBUTE_10_OBSERVATION, null, null));
        components.getAttributes().getAttributes().add(mockAttributePrimaryMeasure(ATTRIBUTE_11_OBSERVATION, null, null));

        dataStructure.setShowDecimals(Integer.valueOf(2));
        dataStructure.setShowDecimalsPrecisions(new ShowDecimalPrecisions());
        dataStructure.getShowDecimalsPrecisions().getShowDecimalPrecisions().add(mockShowDecimalPrecision("agency01", "measure01-conceptScheme01", "01.000", "measure01-conceptScheme01-concept01", 4));
        dataStructure.getShowDecimalsPrecisions().getShowDecimalPrecisions().add(mockShowDecimalPrecision("agency01", "measure01-conceptScheme01", "01.000", "measure01-conceptScheme01-concept05", 1));
        return dataStructure;
    }

    public static Codelist mockCodelist(String agencyID, String resourceID, String version) {
        Codelist codelist = new Codelist();
        codelist.setUrn("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=" + agencyID + ":" + resourceID + "(" + version + ")");
        codelist.setUrnProvider(codelist.getUrn());
        codelist.setAgencyID(agencyID);
        codelist.setId(resourceID);
        codelist.setVersion(version);
        if (resourceID.contains("GEO_DIM")) {
            codelist.setVariable(mockVariableResource("variable-" + resourceID));
        }
        return codelist;
    }

    private static ShowDecimalPrecision mockShowDecimalPrecision(String agencyID, String maintainableParentID, String maintainableVersionID, String resourceID, int value) {
        ShowDecimalPrecision showDecimalPrecision = new ShowDecimalPrecision();
        showDecimalPrecision.setConcept(mockConceptResource(agencyID, maintainableParentID, maintainableVersionID, resourceID, null));
        showDecimalPrecision.setShowDecimals(value);
        return showDecimalPrecision;
    }

    public static Codes mockCodesByCodelist(String agencyID, String resourceID, String version) {
        if ("GEO_DIM-codelist01".equals(resourceID)) {
            return mockCodesByCodelistGeographicalDimension(agencyID, resourceID, version);
        } else if ("abc".equals(resourceID)) {
            return mockCodesByCodelist(agencyID, resourceID, version, Arrays.asList("A", "B", "C"));
        } else {
            return mockCodesByCodelistWithHierarchy(agencyID, resourceID, version);
        }
    }

    public static Codes mockCodesByCodelistGeographicalDimension(String agencyID, String resourceID, String version) {

        Codes codes = new Codes();
        {
            CodeResource parent = mockCodeResourceGeographical(agencyID, resourceID, version, "santa-cruz-tenerife", null, 1, true);
            codes.getCodes().add(parent);
            {
                CodeResource child = mockCodeResourceGeographical(agencyID, resourceID, version, "tenerife", parent.getUrn(), 1, false);
                codes.getCodes().add(child);
                {
                    CodeResource child2 = mockCodeResourceGeographical(agencyID, resourceID, version, "la-laguna", child.getUrn(), 1, true);
                    codes.getCodes().add(child2);
                }
                {
                    CodeResource child2 = mockCodeResourceGeographical(agencyID, resourceID, version, "santa-cruz", child.getUrn(), 2, true);
                    codes.getCodes().add(child2);
                }
            }
            {
                CodeResource child = mockCodeResourceGeographical(agencyID, resourceID, version, "la-palma", parent.getUrn(), 2, true);
                codes.getCodes().add(child);
                {
                    CodeResource child2 = mockCodeResourceGeographical(agencyID, resourceID, version, "los-llanos-de-aridane", child.getUrn(), 1, true);
                    codes.getCodes().add(child2);
                }
                {
                    CodeResource child2 = mockCodeResourceGeographical(agencyID, resourceID, version, "santa-cruz-la-palma", child.getUrn(), 2, true);
                    codes.getCodes().add(child2);
                }
            }
            {
                CodeResource child = mockCodeResourceGeographical(agencyID, resourceID, version, "la-gomera", parent.getUrn(), 3, true);
                codes.getCodes().add(child);
            }
            {
                CodeResource child = mockCodeResourceGeographical(agencyID, resourceID, version, "el-hierro", parent.getUrn(), 4, true);
                codes.getCodes().add(child);
            }
        }
        {
            CodeResource parent = mockCodeResourceGeographical(agencyID, resourceID, version, "las-palmas-gran-canaria", null, 1, false);
            codes.getCodes().add(parent);
            {
                CodeResource child = mockCodeResourceGeographical(agencyID, resourceID, version, "gran-canaria", parent.getUrn(), 1, true);
                codes.getCodes().add(child);
            }
            {
                CodeResource child = mockCodeResourceGeographical(agencyID, resourceID, version, "fuerteventura", parent.getUrn(), 2, true);
                codes.getCodes().add(child);
            }
            {
                CodeResource child = mockCodeResourceGeographical(agencyID, resourceID, version, "lanzarote", parent.getUrn(), 3, true);
                codes.getCodes().add(child);
            }
        }
        return codes;
    }

    public static Codes mockCodesByCodelist(String agencyID, String resourceID, String version, List<String> codesId) {
        Codes codes = new Codes();
        for (String codeId : codesId) {
            CodeResource code = mockCodeResource(agencyID, resourceID, version, codeId, null, null, true);
            codes.getCodes().add(code);
        }
        return codes;
    }

    public static Codes mockCodesByCodelistWithHierarchy(String agencyID, String resourceID, String version) {
        Codes codes = new Codes();
        {
            CodeResource parent = mockCodeResourceWithDescription(agencyID, resourceID, version, resourceID + "-code01", null, 1, true);
            codes.getCodes().add(parent);
        }
        {
            CodeResource parent = mockCodeResourceWithDescription(agencyID, resourceID, version, resourceID + "-code02", null, 2, false);
            codes.getCodes().add(parent);
            {
                CodeResource child = mockCodeResourceWithDescription(agencyID, resourceID, version, resourceID + "-code03", parent.getUrn(), 1, true);
                codes.getCodes().add(child);
            }
        }
        {
            CodeResource parent = mockCodeResourceWithDescription(agencyID, resourceID, version, resourceID + "-code04", null, 3, true);
            codes.getCodes().add(parent);
        }
        {
            CodeResource parent = mockCodeResourceWithDescription(agencyID, resourceID, version, resourceID + "-code05", null, 4, false);
            codes.getCodes().add(parent);
        }
        return codes;
    }

    public static Concepts mockConceptsByConceptScheme(String agencyID, String resourceID, String version) {
        if ("123".equals(resourceID)) {
            return mockConceptsByConceptScheme(agencyID, resourceID, version, Arrays.asList("1", "2", "3"));
        } else {
            return mockConceptsByConceptSchemeWithHierarchy(agencyID, resourceID, version);
        }
    }

    public static Concepts mockConceptsByConceptScheme(String agencyID, String resourceID, String version, List<String> conceptsId) {
        Concepts concepts = new Concepts();
        for (String conceptId : conceptsId) {
            ConceptResource concept = mockConceptResource(agencyID, resourceID, version, conceptId, null);
            concepts.getConcepts().add(concept);
        }
        return concepts;
    }

    public static Concepts mockConceptsByConceptSchemeWithHierarchy(String agencyID, String resourceID, String version) {
        Concepts concepts = new Concepts();
        concepts.getConcepts().add(mockConceptResourceWithDescription(agencyID, resourceID, version, resourceID + "-concept01", null));
        concepts.getConcepts().add(mockConceptResourceWithDescription(agencyID, resourceID, version, resourceID + "-concept02", concepts.getConcepts().get(0).getUrn()));
        concepts.getConcepts().add(mockConceptResourceWithDescription(agencyID, resourceID, version, resourceID + "-concept03", null));
        concepts.getConcepts().add(mockConceptResourceWithDescription(agencyID, resourceID, version, resourceID + "-concept04", null));
        concepts.getConcepts().add(mockConceptResourceWithDescription(agencyID, resourceID, version, resourceID + "-concept05", null));
        return concepts;
    }

    public static CodeResource mockCodeResourceGeographical(String agencyID, String maintainableParentID, String maintainableVersionID, String resourceID, String parentUrn, Integer order, Boolean open) {
        CodeResource code = mockCodeResource(agencyID, maintainableParentID, maintainableVersionID, resourceID, parentUrn, order, open);
        code.setDescription(mockInternationalString("Description " + resourceID));
        code.setVariableElement(mockVariableElementResource("variableElement-" + resourceID));
        return code;
    }

    public static CodeResource mockCodeResource(String agencyID, String maintainableParentID, String maintainableVersionID, String resourceID, String parentUrn, Integer order, Boolean open) {
        CodeResource code = new CodeResource();
        code.setUrn("urn:sdmx:org.sdmx.infomodel.codelist.Code=" + agencyID + ":" + maintainableParentID + "(" + maintainableVersionID + ")." + resourceID);
        code.setId(resourceID);
        code.setName(mockInternationalString(resourceID));
        code.setParent(parentUrn);
        code.setKind("structuralResources#code");
        code.setSelfLink(mockResourceLink("http://apis.metamac.org/metamac-srm-web/apis/structural-resources-internal/v1.0/codelists/" + agencyID + "/" + maintainableParentID + "/"
                + maintainableVersionID + "/codes/" + resourceID));
        code.setOrder(order != null ? Integer.valueOf(order) : null);
        code.setOpen(open);
        return code;
    }
    
    public static CodeResource mockCodeResourceWithDescription(String agencyID, String maintainableParentID, String maintainableVersionID, String resourceID, String parentUrn, Integer order, Boolean open) {
        CodeResource code = mockCodeResource(agencyID, maintainableParentID, maintainableVersionID, resourceID, parentUrn, order, open);
        code.setDescription(mockInternationalString("Description " + resourceID));
        return code;
    }

    public static ConceptResource mockConceptResource(String agencyID, String maintainableParentID, String maintainableVersionID, String resourceID, String parentUrn) {
        ConceptResource concept = new ConceptResource();
        concept.setUrn("urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=" + agencyID + ":" + maintainableParentID + "(" + maintainableVersionID + ")." + resourceID);
        concept.setId(resourceID);
        concept.setName(mockInternationalString(resourceID));
        concept.setParent(parentUrn);
        concept.setKind("structuralResources#concept");
        concept.setSelfLink(mockResourceLink("http://apis.metamac.org/metamac-srm-web/apis/structural-resources-internal/v1.0/conceptschemes/" + agencyID + "/" + maintainableParentID + "/"
                + maintainableVersionID + "/concepts/" + resourceID));
        return concept;
    }

    public static ConceptResource mockConceptResourceWithDescription(String agencyID, String maintainableParentID, String maintainableVersionID, String resourceID, String parentUrn) {
        ConceptResource concept = mockConceptResource(agencyID, maintainableParentID, maintainableVersionID, resourceID, parentUrn);
        concept.setDescription(mockInternationalString("Description " + resourceID));
        return concept;
    }
    
    public static Concept mockConcept(String agencyID, String maintainableParentID, String maintainableVersionID, String resourceID, boolean withQuatity) {
        Concept concept = new Concept();
        concept.setUrn("urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=" + agencyID + ":" + maintainableParentID + "(" + maintainableVersionID + ")." + resourceID);
        concept.setUrnProvider(concept.getUrn());
        concept.setId(resourceID);
        concept.setName(mockInternationalString(resourceID));
        if (withQuatity) {
            concept.setQuantity(mockQuantity(agencyID, "codelist01", maintainableVersionID, "code01"));
        }
        return concept;
    }

    private static Quantity mockQuantity(String agencyID, String maintainableParentID, String maintainableVersionID, String resourceID) {
        Quantity quantity = new Quantity();
        quantity.setUnitCode(mockCodeResource(agencyID, maintainableParentID, maintainableVersionID, resourceID, null, 1, false));
        return quantity;
    }

    private static Dimension mockDimension(String id, Boolean isSpatial) {
        Dimension dimension = new Dimension();
        dimension.setType(DimensionType.DIMENSION);
        dimension.setId(id);
        dimension.setIsSpatial(isSpatial);
        dimension.setLocalRepresentation(new Representation());
        dimension.getLocalRepresentation().setEnumerationCodelist(mockCodelistResource("agency01", id + "-codelist01", "01.000"));
        dimension.setConceptIdentity(mockConceptResource("agency01", "conceptScheme01", "01.000", id + "-concept01", null));
        return dimension;
    }

    private static MeasureDimension mockMeasureDimension(String id) {
        MeasureDimension dimension = new MeasureDimension();
        dimension.setType(DimensionType.MEASURE_DIMENSION);
        dimension.setId(id);
        dimension.setLocalRepresentation(new Representation());
        dimension.getLocalRepresentation().setEnumerationConceptScheme(mockConceptSchemeResource("agency01", id + "-conceptScheme01", "01.000"));
        dimension.setConceptIdentity(mockConceptResource("agency01", "conceptScheme01", "01.000", id + "-conceptMeasureDimension01", null));
        return dimension;
    }

    private static TimeDimension mockTimeDimension(String id) {
        TimeDimension dimension = new TimeDimension();
        dimension.setType(DimensionType.TIME_DIMENSION);
        dimension.setId(id);
        dimension.setLocalRepresentation(new Representation());
        dimension.getLocalRepresentation().setTextFormat(mockTimeTextFormatType());
        dimension.setConceptIdentity(mockConceptResource("agency01", "conceptScheme01", "01.000", id + "-conceptTimeDimension01", null));
        return dimension;
    }

    private static Group mockGroup(String id, List<String> dimensions) {
        Group group = new Group();
        group.setId(id);
        group.setDimensions(new DimensionReferences());
        group.getDimensions().getDimensions().addAll(dimensions);
        return group;
    }

    private static AttributeBase mockAttributeDataset(String id, Resource enumeratedCodelist, Resource enumeratedConceptScheme) {
        Attribute attribute = new Attribute();
        mockAttributeBase(id, attribute, enumeratedCodelist, enumeratedConceptScheme);
        attribute.setAttributeRelationship(new AttributeRelationship());
        attribute.getAttributeRelationship().setNone(new Empty());
        return attribute;
    }

    private static AttributeBase mockAttributeDimension(String id, AttributeQualifierType type, List<String> dimensions, Resource enumeratedCodelist, Resource enumeratedConceptScheme) {
        Attribute attribute = new Attribute();
        mockAttributeBase(id, attribute, enumeratedCodelist, enumeratedConceptScheme);
        attribute.setAttributeRelationship(new AttributeRelationship());
        attribute.getAttributeRelationship().getDimensions().addAll(dimensions);
        attribute.setType(type);
        return attribute;
    }

    private static AttributeBase mockAttributeGroup(String id, String group, Resource enumeratedCodelist, Resource enumeratedConceptScheme) {
        Attribute attribute = new Attribute();
        mockAttributeBase(id, attribute, enumeratedCodelist, enumeratedConceptScheme);
        attribute.setAttributeRelationship(new AttributeRelationship());
        attribute.getAttributeRelationship().setGroup(group);
        return attribute;
    }

    private static AttributeBase mockAttributePrimaryMeasure(String id, Resource enumeratedCodelist, Resource enumeratedConceptScheme) {
        Attribute attribute = new Attribute();
        mockAttributeBase(id, attribute, enumeratedCodelist, enumeratedConceptScheme);
        attribute.setAttributeRelationship(new AttributeRelationship());
        attribute.getAttributeRelationship().setPrimaryMeasure("OBS_VALUE");
        return attribute;
    }

    private static void mockAttributeBase(String id, AttributeBase attribute, Resource enumeratedCodelist, Resource enumeratedConceptScheme) {
        attribute.setId(id);
        attribute.setConceptIdentity(mockConceptResource("agency01", "conceptScheme01", "01.000", id + "-concept01", null));
        attribute.setLocalRepresentation(new Representation());
        if (enumeratedCodelist != null) {
            attribute.getLocalRepresentation().setEnumerationCodelist(enumeratedCodelist);
        } else if (enumeratedConceptScheme != null) {
            attribute.getLocalRepresentation().setEnumerationConceptScheme(enumeratedConceptScheme);
        } else {
            attribute.getLocalRepresentation().setTextFormat(mockTimeTextFormatType());
        }
    }

    private static Resource mockCodelistResource(String agencyID, String resourceID, String version) {
        Resource codelist = new Resource();
        codelist.setUrn("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=" + agencyID + ":" + resourceID + "(" + version + ")");
        codelist.setId(resourceID);
        codelist.setName(mockInternationalString(resourceID));
        codelist.setKind("structuralResources#codelist");
        codelist.setSelfLink(mockResourceLink("http://apis.metamac.org/metamac-srm-web/apis/structural-resources-internal/v1.0/codelists/" + agencyID + "/" + resourceID + "/" + version));
        return codelist;
    }

    private static Resource mockConceptSchemeResource(String agencyID, String resourceID, String version) {
        Resource conceptScheme = new Resource();
        conceptScheme.setUrn("urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=" + agencyID + ":" + resourceID + "(" + version + ")");
        conceptScheme.setId(resourceID);
        conceptScheme.setName(mockInternationalString(resourceID));
        conceptScheme.setKind("structuralResources#conceptScheme");
        conceptScheme.setSelfLink(mockResourceLink("http://apis.metamac.org/metamac-srm-web/apis/structural-resources-internal/v1.0/conceptschemes/" + agencyID + "/" + resourceID + "/" + version));
        return conceptScheme;
    }

    private static Resource mockVariableResource(String resourceID) {
        Resource variable = new Resource();
        variable.setUrn("urn:siemac:org.siemac.metamac.infomodel.structuralresources.Variable=" + resourceID);
        variable.setId(resourceID);
        variable.setName(mockInternationalString(resourceID));
        variable.setKind("structuralResources#variable");
        variable.setSelfLink(mockResourceLink("http://apis.metamac.org/metamac-srm-web/apis/structural-resources-internal/v1.0/variables/" + resourceID));
        return variable;
    }

    private static Resource mockVariableElementResource(String resourceID) {
        Resource variableElement = new Resource();
        variableElement.setUrn("urn:siemac:org.siemac.metamac.infomodel.structuralresources.Variable=variable01." + resourceID);
        variableElement.setId(resourceID);
        variableElement.setName(mockInternationalString(resourceID));
        variableElement.setKind("structuralResources#variableElement");
        variableElement.setSelfLink(mockResourceLink("http://apis.metamac.org/metamac-srm-web/apis/structural-resources-internal/v1.0/variables/variable01/variableelements/" + resourceID));
        return variableElement;
    }

    private static TextFormat mockTimeTextFormatType() {
        TextFormat timeTextFormatType = new TextFormat();
        timeTextFormatType.setTextType(DataType.BASIC_TIME_PERIOD);
        timeTextFormatType.setEndTime("2013");
        return timeTextFormatType;
    }

    private static ResourceLink mockResourceLink(String link) {
        ResourceLink resourceLink = new ResourceLink();
        resourceLink.setHref(link);
        return resourceLink;
    }

    public static Agency mockAgency(String agencyID, String resourceID, String version, String organisationID) {
        Agency agency = new Agency();
        agency.setUrn("urn:sdmx:org.sdmx.infomodel.base.Agency=" + agencyID + ":" + resourceID + "(" + version + ")." + organisationID);
        agency.setUrnProvider(agency.getUrn());
        agency.setId(resourceID);
        agency.setName(mockInternationalString(resourceID));
        agency.setKind("structuralResources#agency");
        agency.setContacts(mockContacts());
        return agency;
    }

    private static Contacts mockContacts() {
        Contacts contacts = new Contacts();
        contacts.getContacts().add(mockContact("Oficina Tenerife"));
        contacts.getContacts().add(mockContact("Oficina Las Palmas"));
        contacts.setTotal(BigInteger.valueOf(contacts.getContacts().size()));
        return contacts;
    }

    private static Contact mockContact(String contactId) {
        Contact contact = new Contact();
        contact.setName(mockInternationalString(contactId));
        return contact;
    }

}
