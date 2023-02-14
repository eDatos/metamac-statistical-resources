package org.siemac.metamac.statistical.resources.core.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Attribute;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AttributeBase;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AttributeQualifierType;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AttributeRelationship;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AttributeUsageStatusType;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Attributes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Concept;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructureComponents;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Dimension;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DimensionBase;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Dimensions;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Group;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Groups;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ItemResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.MeasureDimension;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.PrimaryMeasure;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Representation;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.TextFormat;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.TimeDimension;
import org.siemac.metamac.statistical.resources.core.invocation.service.SrmRestInternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DsdProcessor {

    private static SrmRestInternalService srmRestInternalService;

    public static SrmRestInternalService getSrmRestInternalService() {
        if (srmRestInternalService == null) {
            srmRestInternalService = ApplicationContextProvider.getApplicationContext().getBean(SrmRestInternalService.class);
        }
        return srmRestInternalService;
    }

    public static List<DsdDimension> getDimensions(DataStructure dsd) throws MetamacException {
        List<DsdDimension> dimensions = new ArrayList<DsdProcessor.DsdDimension>();
        DataStructureComponents components = dsd.getDataStructureComponents();
        if (components != null && components.getDimensions() != null) {
            Dimensions dimensionList = components.getDimensions();
            for (DimensionBase dimObj : dimensionList.getDimensions()) {
                if (dimObj instanceof Dimension) {
                    dimensions.add(new DsdDimension((Dimension) dimObj));
                } else if (dimObj instanceof MeasureDimension) {
                    dimensions.add(new DsdDimension((MeasureDimension) dimObj));
                } else if (dimObj instanceof TimeDimension) {
                    dimensions.add(new DsdDimension((TimeDimension) dimObj));
                } else {
                    throw new IllegalArgumentException("Found a dimension of unknown type " + dimObj);
                }
            }
        }
        return dimensions;
    }

    public static DsdDimension getDimension(DataStructure dsd, String dimensionId) throws MetamacException {
        DataStructureComponents components = dsd.getDataStructureComponents();

        DimensionBase foundDimension = null;
        if (components != null && components.getDimensions() != null) {
            Dimensions dimensionList = components.getDimensions();
            for (DimensionBase dimObj : dimensionList.getDimensions()) {
                if (dimensionId.equals(dimObj.getId())) {
                    foundDimension = dimObj;
                }
            }
        }
        if (foundDimension != null) {
            if (foundDimension instanceof Dimension) {
                return new DsdDimension((Dimension) foundDimension);
            } else if (foundDimension instanceof MeasureDimension) {
                return new DsdDimension((MeasureDimension) foundDimension);
            } else if (foundDimension instanceof TimeDimension) {
                return new DsdDimension((TimeDimension) foundDimension);
            } else {
                throw new IllegalArgumentException("Found a dimension of unknown type " + foundDimension);
            }
        }

        return null;
    }

    public static DsdAttribute getSpatialAttributeFromDsd(DataStructure dsd) throws MetamacException {
        List<DsdAttribute> attributes = DsdProcessor.getAttributes(dsd);
        for (DsdAttribute attr : attributes) {
            if (DsdComponentType.SPATIAL.equals(attr.getType())) {
                return attr;
            }
        }
        return null;
    }
    
    public static List<DsdAttribute> getAttributes(DataStructure dsd) throws MetamacException {
        List<DsdAttribute> attributes = new ArrayList<DsdAttribute>();
        DataStructureComponents components = dsd.getDataStructureComponents();

        if (components != null && components.getAttributes() != null) {
            Attributes attributeList = components.getAttributes();
            for (AttributeBase attrObj : attributeList.getAttributes()) {
                if (attrObj instanceof Attribute) {
                    attributes.add(new DsdAttribute((Attribute) attrObj));
                } else {
                    throw new IllegalArgumentException("Found an attribute of unknown type " + attrObj);
                }
            }
        }
        return attributes;
    }

    public static DsdAttribute getAttribute(DataStructure dsd, String attributeId) throws MetamacException {
        DataStructureComponents components = dsd.getDataStructureComponents();

        if (components != null && components.getAttributes() != null) {
            Attributes attributeList = components.getAttributes();
            for (AttributeBase attrObj : attributeList.getAttributes()) {
                if (attrObj instanceof Attribute && attributeId.equals(attrObj.getId())) {
                    return new DsdAttribute((Attribute) attrObj);
                }
            }
        }
        return null;
    }

    /**
     * Gets groups as a map indexed by groupId and list of dimensions id as value
     */
    public static Map<String, List<String>> getGroups(DataStructure dsd) throws MetamacException {
        Map<String, List<String>> groups = new HashMap<String, List<String>>();
        DataStructureComponents components = dsd.getDataStructureComponents();
        if (components != null && components.getGroups() != null) {
            Groups groupList = components.getGroups();
            for (Group group : groupList.getGroups()) {
                groups.put(group.getId(), group.getDimensions().getDimensions());
            }
        }
        return groups;
    }

    public abstract static class DsdComponent {

        protected DsdComponentType     type                           = null;
        protected String               codelistRepresentationUrn      = null;
        protected String               conceptSchemeRepresentationUrn = null;
        protected TextFormat           textFormat                     = null;
        protected ItemResourceInternal conceptIdentity                = null;

        private static Logger          logger                         = LoggerFactory.getLogger(DsdComponent.class);

        protected void setConceptIdentity(ItemResourceInternal conceptIdentity) {
            if (conceptIdentity != null) {
                this.conceptIdentity = conceptIdentity;
            } else {
                throw new IllegalArgumentException("Concept identity is required");
            }
        }

        protected void setRepresentationFromLocalRepresentation(String urn, Representation localRepresentation) {
            extractRepresentation(urn, localRepresentation);
        }

        protected void setRepresentationFromConceptIdentity(ItemResourceInternal conceptIdentity) throws MetamacException {
            if (conceptIdentity == null) {
                throw new RuntimeException("Concept identity can not be null");
            }

            // EDATOS-3227
            logger.info("getSrmRestInternalService Class: ", getSrmRestInternalService().getClass());
            Concept concept = getSrmRestInternalService().retrieveConceptByUrn(conceptIdentity.getUrn());
            extractRepresentation(conceptIdentity.getUrn(), concept.getCoreRepresentation());
        }

        public abstract String getComponentId();

        public DsdComponentType getType() {
            return type;
        }

        public String getCodelistRepresentationUrn() {
            return codelistRepresentationUrn;
        }

        public String getConceptSchemeRepresentationUrn() {
            return conceptSchemeRepresentationUrn;
        }

        public TextFormat getTextFormatRepresentation() {
            return textFormat;
        }

        public String getEnumeratedRepresentationUrn() {
            if (StringUtils.isNotEmpty(this.codelistRepresentationUrn) && StringUtils.isNotEmpty(this.conceptSchemeRepresentationUrn)) {
                throw new RuntimeException("Two enumerated representation aren't allowed.");
            }
            if (StringUtils.isNotEmpty(this.codelistRepresentationUrn)) {
                return this.codelistRepresentationUrn;
            }
            if (StringUtils.isNotEmpty(this.conceptSchemeRepresentationUrn)) {
                return this.conceptSchemeRepresentationUrn;
            }
            return null;
        }

        public ItemResourceInternal getConceptIdentity() {
            return conceptIdentity;
        }

        private void extractRepresentation(String componentUrn, Representation representation) {
            if (representation == null) {
                throw new IllegalArgumentException("Found a component " + componentUrn + " with representation null");
            }

            if (representation.getEnumerationCodelist() != null) {
                codelistRepresentationUrn = representation.getEnumerationCodelist().getUrn();
            } else if (representation.getEnumerationConceptScheme() != null) {
                conceptSchemeRepresentationUrn = representation.getEnumerationConceptScheme().getUrn();
            } else if (representation.getTextFormat() != null) {
                textFormat = representation.getTextFormat();
            } else {
                throw new IllegalArgumentException("Found a component " + componentUrn + " with representation null");
            }
        }
    }

    public static class DsdDimension extends DsdComponent {

        private final String dimensionId;

        public DsdDimension(Dimension dim) throws MetamacException {
            dimensionId = dim.getId();
            if (BooleanUtils.isTrue(dim.isIsSpatial())) {
                type = DsdComponentType.SPATIAL;
            } else {
                type = DsdComponentType.OTHER;
            }

            if (dim.getLocalRepresentation() != null) {
                setRepresentationFromLocalRepresentation(dim.getUrn(), dim.getLocalRepresentation());
            } else if (dim.getConceptIdentity() != null) {
                setRepresentationFromConceptIdentity(dim.getConceptIdentity());
            } else {
                throw new IllegalArgumentException("Found a dimension with no representation info " + dim.getUrn());
            }

            setConceptIdentity(dim.getConceptIdentity());
        }

        public DsdDimension(MeasureDimension dim) {
            dimensionId = dim.getId();
            type = DsdComponentType.MEASURE;
            // note: measure dimension always must have a concept scheme enumerated representation
            if (dim.getLocalRepresentation() != null && dim.getLocalRepresentation().getEnumerationConceptScheme() != null) {
                conceptSchemeRepresentationUrn = dim.getLocalRepresentation().getEnumerationConceptScheme().getUrn();
            } else {
                throw new IllegalArgumentException("Found a dimension with no representation info " + dim.getUrn());
            }

            setConceptIdentity(dim.getConceptIdentity());
        }

        public DsdDimension(TimeDimension dim) {
            dimensionId = dim.getId();
            type = DsdComponentType.TEMPORAL;
            // note: time dimension always must have a non enumerated representation
            if (dim.getLocalRepresentation() != null && dim.getLocalRepresentation().getTextFormat() != null) {
                textFormat = dim.getLocalRepresentation().getTextFormat();
            } else {
                throw new IllegalArgumentException("Found a dimension with no representation info " + dim.getUrn());
            }

            setConceptIdentity(dim.getConceptIdentity());
        }

        @Override
        public String getComponentId() {
            return dimensionId;
        }
    }

    public static class DsdAttribute extends DsdComponent {

        private final String                attributeId;
        private final boolean               isAttributeAtObservationLevel;
        private final AttributeRelationship attributeRelationship;
        private final boolean               isMandatory;

        public DsdAttribute(Attribute attr) throws MetamacException {
            attributeId = attr.getId();
            if (AttributeQualifierType.SPATIAL.equals(attr.getType())) {
                type = DsdComponentType.SPATIAL;
            } else if (AttributeQualifierType.MEASURE.equals(attr.getType())) {
                type = DsdComponentType.MEASURE;
            } else if (AttributeQualifierType.TIME.equals(attr.getType())) {
                type = DsdComponentType.TEMPORAL;
            } else {
                type = DsdComponentType.OTHER;
            }

            if (attr.getLocalRepresentation() != null) {
                setRepresentationFromLocalRepresentation(attr.getUrn(), attr.getLocalRepresentation());
            } else if (attr.getConceptIdentity() != null) {
                setRepresentationFromConceptIdentity(attr.getConceptIdentity());
            } else {
                throw new IllegalArgumentException("Found an attribute with no representation info " + attr.getUrn());
            }

            setConceptIdentity(attr.getConceptIdentity());

            isAttributeAtObservationLevel = (attr.getAttributeRelationship().getPrimaryMeasure() != null);
            attributeRelationship = attr.getAttributeRelationship();
            isMandatory = AttributeUsageStatusType.MANDATORY.equals(attr.getAssignmentStatus());
        }

        @Override
        public String getComponentId() {
            return attributeId;
        }

        public boolean isAttributeAtObservationLevel() {
            return isAttributeAtObservationLevel;
        }

        public AttributeRelationship getAttributeRelationship() {
            return attributeRelationship;
        }

        public boolean isMandatory() {
            return isMandatory;
        }

        public boolean isDatasetAttribute() {
            return getAttributeRelationship().getNone() != null;
        }

        public boolean isDimensionAttribute() {
            return (!isDatasetAttribute() && (getAttributeRelationship().getGroup() != null || !getAttributeRelationship().getDimensions().isEmpty()));
        }

        public boolean isObservationAttribute() {
            return (!isDatasetAttribute() && !isDimensionAttribute() && getAttributeRelationship().getPrimaryMeasure() != null);
        }

    }

    public static class DsdPrimaryMeasure extends DsdComponent {

        private final String primaryMeasureId;

        public DsdPrimaryMeasure(PrimaryMeasure primaryMeasure) throws MetamacException {
            primaryMeasureId = primaryMeasure.getId();
            if (primaryMeasure.getLocalRepresentation() != null) {
                setRepresentationFromLocalRepresentation(primaryMeasure.getUrn(), primaryMeasure.getLocalRepresentation());
            } else if (primaryMeasure.getConceptIdentity() != null) {
                setRepresentationFromConceptIdentity(primaryMeasure.getConceptIdentity());
            } else {
                throw new IllegalArgumentException("Found a primary measure with no representation info " + primaryMeasure.getUrn());
            }
            setConceptIdentity(primaryMeasure.getConceptIdentity());
        }

        @Override
        public String getComponentId() {
            return primaryMeasureId;
        }
    }

    public static enum DsdComponentType {
        OTHER, SPATIAL, TEMPORAL, MEASURE
    }

}
