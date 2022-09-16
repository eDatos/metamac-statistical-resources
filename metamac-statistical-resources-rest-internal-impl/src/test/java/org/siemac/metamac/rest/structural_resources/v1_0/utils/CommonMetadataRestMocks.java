package org.siemac.metamac.rest.structural_resources.v1_0.utils;

import static org.siemac.metamac.rest.structural_resources.v1_0.utils.RestMocks.mockInternationalString;

import org.siemac.metamac.rest.common_metadata.v1_0.domain.Configuration;
import org.siemac.metamac.rest.common_metadata.v1_0.domain.ResourceInternal;

public class CommonMetadataRestMocks {

    public static Configuration mockConfiguration(String id) {
        Configuration configuration = new Configuration();
        configuration.setId(id);
        configuration.setLicense(mockInternationalString("license"));
        configuration.setContact(mockResourceInternal("contact", "structuralResources#organisation"));
        return configuration;
    }

    public static ResourceInternal mockResourceInternal(String id, String kind) {
        ResourceInternal resource = new ResourceInternal();
        resource.setId(id);
        resource.setName(mockInternationalString(id));
        resource.setKind(kind);
        resource.setUrn("urn:sdmx:org.sdmx.infomodel.base.Agency=SDMX:AGENCIES(1.0).MOCKAGENCY");
        return resource;
    }
}