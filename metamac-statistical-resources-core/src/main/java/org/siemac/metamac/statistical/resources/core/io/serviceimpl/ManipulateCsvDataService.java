package org.siemac.metamac.statistical.resources.core.io.serviceimpl;

import java.io.File;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure;
import org.siemac.metamac.statistical.resources.core.io.serviceimpl.validators.ValidateDataVersusDsd;

public interface ManipulateCsvDataService {

    public static final String BEAN_ID = "manipulateCsvDataService";

    public void importCsv(ServiceContext ctx, File csvFile, DataStructure dataStructure, String datasetID, String dataSourceID, ValidateDataVersusDsd validateDataVersusDsd) throws Exception;

    public void importCsvAttributes(File csvFile) throws Exception;
}
