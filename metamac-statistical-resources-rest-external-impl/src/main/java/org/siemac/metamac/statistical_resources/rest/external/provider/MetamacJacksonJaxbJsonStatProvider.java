package org.siemac.metamac.statistical_resources.rest.external.provider;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.siemac.metamac.rest.json.MetamacJacksonJaxbJsonProvider;

@Provider
@Consumes({MediaType.APPLICATION_JSON, "text/json", "application/jsonstat+json"})
@Produces({MediaType.APPLICATION_JSON, "text/json", "application/jsonstat+json"})
public class MetamacJacksonJaxbJsonStatProvider extends MetamacJacksonJaxbJsonProvider {
    public MetamacJacksonJaxbJsonStatProvider() {
        super();
    }
}
