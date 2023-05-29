package org.siemac.metamac.statistical.resources.core.utils;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Attribute;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AttributeQualifierType;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AttributeRelationship;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AttributeUsageStatusType;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodeResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelist;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Concept;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Concepts;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructureComponents;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataType;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Dimension;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DimensionReferences;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DimensionType;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Dimensions;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Group;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ItemResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.MeasureDimension;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.PrimaryMeasure;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Representation;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.TextFormat;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.TimeDimension;

public class SrmMockUtils {

    private static final String DEFAULT_LOCALE = "es";

    // -------------------------------------------------------------------------------------
    // COMMON
    // -------------------------------------------------------------------------------------

    private static ResourceLink buildResourceLink(TypeExternalArtefactsEnum type) {
        ResourceLink resourceLink = new ResourceLink();
        resourceLink.setHref(UUID.randomUUID().toString());
        resourceLink.setKind(type.getName());
        return resourceLink;
    }

    public static InternationalString buildInternationalStringResource(String label, String lang) {
        InternationalString internationalString = new InternationalString();
        internationalString.getTexts().add(buildLocalisedStringResource(label, lang));
        return internationalString;
    }

    public static LocalisedString buildLocalisedStringResource(String label, String lang) {
        LocalisedString text = new LocalisedString();
        text.setLang(lang);
        text.setValue(label);
        return text;
    }

    // -------------------------------------------------------------------------------------
    // CONCEPT SCHEME
    // -------------------------------------------------------------------------------------

    public static ConceptScheme buildConceptScheme(String id, String name, String lang, String urn) {
        ConceptScheme scheme = new ConceptScheme();
        scheme.setId(id);
        scheme.setUrn(urn);
        scheme.setName(buildInternationalStringResource(name, lang));
        return scheme;
    }

    public static ResourceInternal buildConceptSchemeRef(String conceptSchemeUrn) {
        ResourceInternal conceptScheme = new ResourceInternal();
        conceptScheme.setUrn(conceptSchemeUrn);
        return conceptScheme;
    }

    // -------------------------------------------------------------------------------------
    // CONCEPTS
    // -------------------------------------------------------------------------------------

    public static Concepts buildConcepts(int numConcepts) {
        Concepts concepts = new Concepts();
        for (int i = 1; i <= numConcepts; i++) {
            concepts.getConcepts().add(buildConcept("concept-0" + i, "Concept 0" + i));
        }
        return concepts;
    }

    public static ItemResourceInternal buildConceptReferenceTypeWithURN(String conceptIdentityURN) {
        ItemResourceInternal conceptReferenceType = new ItemResourceInternal();
        conceptReferenceType.setUrn(conceptIdentityURN);
        return conceptReferenceType;
    }

    public static Concept buildConcept(String id, String urn, String name, Representation conceptRepresentation) {
        Concept concept = new Concept();
        concept.setId(id);
        concept.setUrn(urn);
        concept.setUri(UUID.randomUUID().toString());
        concept.setName(buildInternationalStringResource(name, DEFAULT_LOCALE));
        concept.setCoreRepresentation(conceptRepresentation);
        return concept;
    }

    public static ConceptResourceInternal buildConcept(String id, String name) {
        ConceptResourceInternal concept = new ConceptResourceInternal();
        concept.setId(id);
        concept.setUrn("urn:uuid:" + id);
        concept.setUrnProvider("urn:uuid:provider:" + id);
        concept.setSelfLink(buildResourceLink(TypeExternalArtefactsEnum.CONCEPT));
        concept.setManagementAppLink("http://srm/concepts/" + id);
        concept.setName(buildInternationalStringResource(name, DEFAULT_LOCALE));
        concept.setKind(TypeExternalArtefactsEnum.CONCEPT.getValue());
        return concept;
    }

    public static Representation buildConceptRepresentation(String codelistUrn) {
        Representation conceptRepresentation = new Representation();
        conceptRepresentation.setEnumerationCodelist(new ResourceInternal());
        conceptRepresentation.getEnumerationCodelist().setUrn(codelistUrn);
        return conceptRepresentation;
    }

    public static Representation buildConceptRepresentation(DataType textType) {
        Representation conceptRepresentation = new Representation();
        TextFormat textFormat = new TextFormat();
        textFormat.setTextType(textType);
        conceptRepresentation.setTextFormat(textFormat);
        return conceptRepresentation;
    }

    // -------------------------------------------------------------------------------------
    // CODELIST
    // -------------------------------------------------------------------------------------

    public static Codelist buildCodelist(String id, String name, String lang, String urn) {
        Codelist scheme = new Codelist();
        scheme.setId(id);
        scheme.setUrn(urn);
        scheme.setName(buildInternationalStringResource(name, lang));
        return scheme;
    }

    public static ResourceInternal buildCodelistRef(String codelistUrn) {
        ResourceInternal ref = new ResourceInternal();
        ref.setUrn(codelistUrn);
        return ref;
    }

    // -------------------------------------------------------------------------------------
    // CODES
    // -------------------------------------------------------------------------------------

    public static Codes buildCodes(int numCodes) {
        Codes codes = new Codes();
        for (int i = 1; i <= numCodes; i++) {
            codes.getCodes().add(buildCode("code-0" + i, "Code 0" + i, DEFAULT_LOCALE, "variableElement-0" + i));
        }
        return codes;
    }

    public static CodeResourceInternal buildCode(String id, String name, String lang, String variableElementCode) {
        CodeResourceInternal code = new CodeResourceInternal();
        code.setId(id);
        code.setUrn("urn:uuid:" + id);
        code.setUrnProvider("urn:uuid:provider:" + id);
        code.setSelfLink(buildResourceLink(TypeExternalArtefactsEnum.CODE));
        code.setName(buildInternationalStringResource(name, lang));
        code.setKind(TypeExternalArtefactsEnum.CODE.getValue());
        code.setVariableElement(buildVariableElementRef(variableElementCode));
        return code;
    }

    public static ResourceInternal buildVariableElementRef(String codeId) {
        ResourceInternal ref = new ResourceInternal();
        ref.setId(codeId);
        return ref;
    }

    // -------------------------------------------------------------------------------------
    // DSD
    // -------------------------------------------------------------------------------------

    public static DataStructure mockDsdWithGeoTimeAndMeasureDimensions(String urn, String geoId, String timeId, String measureId, ResourceInternal measureConceptSchemeReference,
            ResourceInternal geoCodelistReference) {
        MeasureDimension measureDim = SrmMockUtils.buildMeasureDimension(measureId, measureConceptSchemeReference);
        TimeDimension timeDim = SrmMockUtils.buildTimeDimension(timeId, DataType.REPORTING_YEAR);
        Dimension geoDim = SrmMockUtils.buildGeoDimension(geoId, geoCodelistReference);

        DataStructure dsd = new DataStructure();
        dsd.setUrn(urn);
        DataStructureComponents components = new DataStructureComponents();
        components.setDimensions(new Dimensions());
        components.getDimensions().getDimensions().add(measureDim);
        components.getDimensions().getDimensions().add(timeDim);
        components.getDimensions().getDimensions().add(geoDim);
        dsd.setDataStructureComponents(components);

        return dsd;
    }

    public static MeasureDimension buildMeasureDimension(String id, ResourceInternal conceptSchemeRepresentationReference) {
        MeasureDimension measureDim = new MeasureDimension();
        measureDim.setId(id);
        measureDim.setType(DimensionType.MEASURE_DIMENSION);

        Representation representation = new Representation();
        representation.setEnumerationConceptScheme(conceptSchemeRepresentationReference);

        measureDim.setLocalRepresentation(representation);

        measureDim.setConceptIdentity(buildConceptReferenceTypeWithURN("urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ECB:ECB_CONCEPTS(1.0).EXR_TYPE"));
        return measureDim;
    }

    public static Attribute buildMeasureAttribute(String id, ResourceInternal conceptSchemeRepresentationReference, AttributeRelationship attributeRelationship) {
        Attribute attribute = new Attribute();
        attribute.setId(id);
        attribute.setType(AttributeQualifierType.MEASURE);
        Representation conceptSchemeRepresentation = new Representation();
        conceptSchemeRepresentation.setEnumerationConceptScheme(conceptSchemeRepresentationReference);
        attribute.setLocalRepresentation(conceptSchemeRepresentation);
        attribute.setConceptIdentity(buildConceptReferenceTypeWithURN("urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ECB:ECB_CONCEPTS(1.0).EXR_TYPE"));
        attribute.setAttributeRelationship(attributeRelationship);
        return attribute;
    }

    public static TimeDimension buildTimeDimension(String id, DataType type) {
        TimeDimension dim = new TimeDimension();
        dim.setId(id);
        dim.setType(DimensionType.TIME_DIMENSION);

        Representation representation = new Representation();
        TextFormat textFormat = new TextFormat();
        textFormat.setTextType(type);
        representation.setTextFormat(textFormat);

        dim.setLocalRepresentation(representation);
        dim.setConceptIdentity(buildConceptReferenceTypeWithURN("urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ECB:ECB_CONCEPTS(1.0).EXR_TYPE"));
        return dim;
    }

    public static TimeDimension buildTimeDimension(String id, String conceptIdentityURN, DataType type) {
        TimeDimension dim = new TimeDimension();
        dim.setId(id);
        dim.setType(DimensionType.TIME_DIMENSION);

        Representation representation = new Representation();
        TextFormat textFormat = new TextFormat();
        textFormat.setTextType(type);
        representation.setTextFormat(textFormat);

        dim.setLocalRepresentation(representation);

        dim.setConceptIdentity(buildConceptReferenceTypeWithURN(conceptIdentityURN));
        return dim;
    }

    public static Attribute buildTimeAttribute(String id, TextFormat textFormat, AttributeRelationship attributeRelationship) {
        Attribute attribute = new Attribute();
        attribute.setId(id);
        attribute.setType(AttributeQualifierType.TIME);
        Representation nonEnumeratedRepresentation = new Representation();
        nonEnumeratedRepresentation.setTextFormat(textFormat);
        attribute.setLocalRepresentation(nonEnumeratedRepresentation);
        attribute.setConceptIdentity(buildConceptReferenceTypeWithURN("urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ECB:ECB_CONCEPTS(1.0).EXR_TYPE"));
        attribute.setAttributeRelationship(attributeRelationship);
        return attribute;
    }

    public static Dimension buildGeoDimension(String id, ResourceInternal codelistReference) {
        Dimension dim = new Dimension();
        dim.setId(id);
        dim.setIsSpatial(true);
        dim.setType(DimensionType.DIMENSION);

        Representation representation = new Representation();
        representation.setEnumerationCodelist(codelistReference);

        dim.setLocalRepresentation(representation);

        dim.setConceptIdentity(buildConceptReferenceTypeWithURN("urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ECB:ECB_CONCEPTS(1.0).EXR_TYPE"));
        return dim;
    }

    public static Attribute buildSpatialAttribute(String id, ResourceInternal codelistReference, AttributeRelationship attributeRelationship) {
        Attribute attribute = new Attribute();
        attribute.setId(id);
        attribute.setType(AttributeQualifierType.SPATIAL);
        Representation codelistRepresentation = new Representation();
        codelistRepresentation.setEnumerationCodelist(codelistReference);
        attribute.setLocalRepresentation(codelistRepresentation);
        attribute.setConceptIdentity(buildConceptReferenceTypeWithURN("urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=ECB:ECB_CONCEPTS(1.0).EXR_TYPE"));
        attribute.setAttributeRelationship(attributeRelationship);
        return attribute;
    }

    public static Dimension buildDimension(String id, String conceptIdentityURN, String representationURN) {
        Dimension dimension = new Dimension();
        dimension.setId(id);
        dimension.setIsSpatial(false);
        dimension.setType(DimensionType.DIMENSION);

        dimension.setConceptIdentity(buildConceptReferenceTypeWithURN(conceptIdentityURN));

        if (StringUtils.isNotEmpty(representationURN)) {
            dimension.setLocalRepresentation(buildSimpleDataStructureRepresentationTypeWithURN(representationURN));
        }
        return dimension;
    }

    public static Group buildGroupType(String groupId, String... dimensions) {
        Group group = new Group();
        group.setId(groupId);

        for (int i = 0; i < dimensions.length; i++) {
            group.setDimensions(new DimensionReferences());
            group.getDimensions().getDimensions().add(dimensions[i]);
        }

        return group;
    }

    private static Attribute buildAttributeType(String attributeId, String conceptIdentityURN, AttributeUsageStatusType usageStatusType, String representationURN) {
        Attribute attribute = new Attribute();
        attribute.setId(attributeId);
        attribute.setAssignmentStatus(usageStatusType);

        attribute.setConceptIdentity(buildConceptReferenceTypeWithURN(conceptIdentityURN));

        if (StringUtils.isNotEmpty(representationURN)) {
            attribute.setLocalRepresentation(buildSimpleDataStructureRepresentationTypeWithURN(representationURN));
        }

        return attribute;
    }

    public static Attribute buildAttributeTypeWithGroupRelationship(String attributeId, String conceptIdentityURN, String representationURN, AttributeUsageStatusType usageStatusType, String groupId) {
        Attribute attribute = buildAttributeType(attributeId, conceptIdentityURN, usageStatusType, representationURN);

        AttributeRelationship attributeRelationship = new AttributeRelationship();
        attributeRelationship.setGroup(groupId);
        attribute.setAttributeRelationship(attributeRelationship);

        return attribute;
    }

    public static Attribute buildAttributeTypeWithDimensionRelationship(String attributeId, String conceptIdentityURN, String representationURN, AttributeUsageStatusType usageStatusType,
            List<String> dimensions, List<String> groups) {
        Attribute attribute = buildAttributeType(attributeId, conceptIdentityURN, usageStatusType, representationURN);

        AttributeRelationship attributeRelationship = new AttributeRelationship();

        for (String dimension : dimensions) {
            attributeRelationship.getDimensions().add(dimension);
        }

        for (String group : groups) {
            attributeRelationship.getAttachmentGroups().add(group);
        }

        attribute.setAttributeRelationship(attributeRelationship);

        return attribute;
    }

    public static Attribute buildAttributeTypeWithPrimaryMeasureRelationship(String attributeId, String conceptIdentityURN, String representationURN, AttributeUsageStatusType usageStatusType) {
        Attribute attribute = buildAttributeType(attributeId, conceptIdentityURN, usageStatusType, representationURN);

        AttributeRelationship attributeRelationship = new AttributeRelationship();
        attributeRelationship.setPrimaryMeasure("OBS_VALUE");

        attribute.setAttributeRelationship(attributeRelationship);

        return attribute;
    }

    private static Representation buildSimpleDataStructureRepresentationTypeWithURN(String representationURN) {
        Representation simpleDataStructureRepresentationType = new Representation();
        simpleDataStructureRepresentationType.setEnumerationCodelist(buildCodelistRef(representationURN));
        return simpleDataStructureRepresentationType;
    }

    public static PrimaryMeasure buildPrimaryMeasure(String conceptIdentityURN, String representationURN) {
        PrimaryMeasure primaryMeasure = new PrimaryMeasure();
        primaryMeasure.setId("OBS_VALUE");
        primaryMeasure.setConceptIdentity(buildConceptReferenceTypeWithURN(conceptIdentityURN));

        if (StringUtils.isNotEmpty(representationURN)) {
            primaryMeasure.setLocalRepresentation(buildSimpleDataStructureRepresentationTypeWithURN(representationURN));
        }

        return primaryMeasure;
    }

}
