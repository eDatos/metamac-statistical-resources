package org.siemac.metamac.statistical.resources.core.utils.mocks.factories;

import org.siemac.metamac.core.common.test.utils.mocks.configuration.MockProvider;
import org.siemac.metamac.statistical.resources.core.publication.domain.Cube;

@SuppressWarnings("unused")
@MockProvider
public class CubeMockFactory extends StatisticalResourcesMockFactory<Cube> {

    public static final String     CUBE_01_BASIC_NAME                           = "CUBE_01_BASIC";

    public static final String     CUBE_02_BASIC_NAME                           = "CUBE_02_BASIC";

    public static final String     CUBE_03_BASIC_NAME                           = "CUBE_03_BASIC";

    public static final String     CUBE_04_DATASET_NAME                         = "CUBE_04_DATASET";

    public static final String     CUBE_05_QUERY_NAME                           = "CUBE_05_QUERY";

    public static final String     CUBE_06_DATASET_WITH_PARENT_NAME             = "CUBE_06_DATASET_WITH_PARENT";

    public static final String     CUBE_07_QUERY_WITH_PARENT_NAME               = "CUBE_07_QUERY_WITH_PARENT";

    public static final String     CUBE_08_EMPTY_IN_PUBLICATION_VERSION_90_NAME = "CUBE_08_EMPTY_IN_PUBLICATION_VERSION_90";
    
    public static final String     CUBE_09_URL_NAME                           = "CUBE_05_URL";

    private static CubeMockFactory instance                                     = null;

    private CubeMockFactory() {
    }

    public static CubeMockFactory getInstance() {
        if (instance == null) {
            instance = new CubeMockFactory();
        }
        return instance;
    }

    private static Cube getCube01Basic() {
        return createQueryCube();
    }

    private static Cube getCube02Basic() {
        return createQueryCube();
    }

    private static Cube getCube03Basic() {
        return createQueryCube();
    }

    private static Cube getCube04Dataset() {
        return createDatasetCube();
    }

    private static Cube getCube05Query() {
        return createQueryCube();
    }

    private static Cube getCube09Url() {
        return createUrlCube();
    }
    
    private static Cube getCube06DatasetWithParent() {
        return createDatasetCubeWithParent();
    }

    private static Cube getCube07QueryWithParent() {
        return createQueryCubeWithParent();
    }

    private static Cube createUrlCube() {
        return getStatisticalResourcesPersistedDoMocks().mockUrlCube("http://www.pruebas.com");
    }
    
    private static Cube createQueryCube() {
        return getStatisticalResourcesPersistedDoMocks().mockQueryCube(getStatisticalResourcesPersistedDoMocks().mockQueryWithGeneratedQueryVersion());
    }

    private static Cube createDatasetCube() {
        return getStatisticalResourcesPersistedDoMocks().mockDatasetCube(getStatisticalResourcesPersistedDoMocks().mockDatasetWithGeneratedDatasetVersion());
    }

    private static Cube createDatasetCubeWithParent() {
        return getStatisticalResourcesPersistedDoMocks().mockDatasetCubeWithParent(getStatisticalResourcesPersistedDoMocks().mockDatasetWithGeneratedDatasetVersion());
    }

    private static Cube createQueryCubeWithParent() {
        return getStatisticalResourcesPersistedDoMocks().mockQueryCubeWithParent(getStatisticalResourcesPersistedDoMocks().mockQueryWithGeneratedQueryVersion());
    }
}
