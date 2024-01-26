package org.siemac.metamac.statistical_resources.rest.internal.invocation;

import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Agency;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelist;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Concept;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Concepts;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ContentConstraint;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VariableElementsGeoInfo;

public interface SrmRestInternalFacade {

    public DataStructure retrieveDataStructureByUrn(String urn);

    public Codelist retrieveCodelistByUrn(String urn);
    public Codes retrieveCodesByCodelistUrn(String urn, String order, String openness, String fields);

    public Concepts retrieveConceptsByConceptSchemeByUrn(String urn, String fields);
    public Concept retrieveConceptByUrn(String urn);

    public VariableElementsGeoInfo findVariableElementsGeoInfo(String urn);

    public Agency retrieveAgency(String urn);

    public ContentConstraint retrieveDatasetContentConstraint(String datasetUrn);
}
