package org.siemac.metamac.statistical.resources.core.utils.mocks.factories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.siemac.metamac.core.common.test.utils.mocks.configuration.MockDescriptor;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MockFactory;
import org.siemac.metamac.statistical.resources.core.common.domain.Translation;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Dataset;
import org.siemac.metamac.statistical.resources.core.dataset.domain.DatasetVersion;
import org.siemac.metamac.statistical.resources.core.dataset.domain.Datasource;
import org.siemac.metamac.statistical.resources.core.dataset.domain.GeoCovVarElementCacheDatasetVersion;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.Multidataset;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetCube;
import org.siemac.metamac.statistical.resources.core.multidataset.domain.MultidatasetVersion;
import org.siemac.metamac.statistical.resources.core.publication.domain.Chapter;
import org.siemac.metamac.statistical.resources.core.publication.domain.Cube;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.query.domain.Query;
import org.siemac.metamac.statistical.resources.core.query.domain.QueryVersion;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesPersistedDoMocks;

public abstract class StatisticalResourcesMockFactory<EntityMock> extends MockFactory<EntityMock> {

    public static final String OPERATION_01_CODE               = "C00025A";
    public static final String OPERATION_02_CODE               = "C00025B";
    public static final String OPERATION_03_CODE               = "C00025C";

    public static final String INIT_VERSION                    = "1.0";
    public static final String SECOND_VERSION                  = "2.0";
    public static final String THIRD_VERSION                   = "3.0";
    public static final String NOT_INITIAL_VERSION             = "2.2";
    public static final String ANOTHER_NOT_INITIAL_VERSION     = "1.5";
    public static final String MAXIMUM_VERSION_AVAILABLE       = "99999.9";
    public static final String MAXIMUM_MINOR_VERSION_AVAILABLE = "1.99999";

    protected static StatisticalResourcesPersistedDoMocks getStatisticalResourcesPersistedDoMocks() {
        return StatisticalResourcesPersistedDoMocks.getInstance();
    }

    protected static Dataset getDatasetMock(String id) {
        return DatasetMockFactory.getInstance().getMock(id);
    }

    protected static Query getQueryMock(String id) {
        return QueryMockFactory.getInstance().getMock(id);
    }

    protected static MockDescriptor getDatasetMockDescriptor(String id) {
        return DatasetMockFactory.getInstance().getMockWithDependencies(id);
    }

    protected static MockDescriptor getPublicationMockDescriptor(String id) {
        return PublicationMockFactory.getInstance().getMockWithDependencies(id);
    }

    protected static MockDescriptor getQueryMockDescriptor(String id) {
        return QueryMockFactory.getInstance().getMockWithDependencies(id);
    }

    protected static MockDescriptor getMultidatasetMockDescriptor(String id) {
        return MultidatasetMockFactory.getInstance().getMockWithDependencies(id);
    }

    protected static DatasetVersion getDatasetVersionMock(String id) {
        return DatasetVersionMockFactory.getInstance().getMock(id);
    }

    protected static QueryVersion getQueryVersionMock(String id) {
        return QueryVersionMockFactory.getInstance().getMock(id);
    }

    protected static Datasource getDatasourceMock(String id) {
        return DatasourceMockFactory.getInstance().getMock(id);
    }

    protected static PublicationVersion getPublicationVersionMock(String id) {
        return PublicationVersionMockFactory.getInstance().getMock(id);
    }

    protected static MultidatasetVersion getMultidatasetVersionMock(String id) {
        return MultidatasetVersionMockFactory.getInstance().getMock(id);
    }

    protected static void registerDatasetMock(String id, Dataset dataset) {
        DatasetMockFactory.getInstance().registerMock(id, dataset);
    }

    protected static void registerChapterMock(String id, Chapter chapter) {
        ChapterMockFactory.getInstance().registerMock(id, chapter);
    }

    protected static void registerCubeMock(String id, Cube cube) {
        CubeMockFactory.getInstance().registerMock(id, cube);
    }

    protected static void registerMultidatasetCubeMock(String id, MultidatasetCube cube) {
        MultidatasetCubeMockFactory.getInstance().registerMock(id, cube);
    }

    protected static void registerTranslationMock(String id, Translation translation) {
        TranslationMockFactory.getInstance().registerMock(id, translation);
    }

    protected static void registerQueryMock(String id, Query query) {
        QueryMockFactory.getInstance().registerMock(id, query);
    }

    protected static void registerDatasetVersionMock(String id, DatasetVersion datasetVersion) {
        DatasetVersionMockFactory.getInstance().registerMock(id, datasetVersion);
    }

    protected static void registerQueryVersionMock(String id, QueryVersion queryVersion) {
        QueryVersionMockFactory.getInstance().registerMock(id, queryVersion);
    }

    protected static void registerPublicationVersionMock(String id, PublicationVersion publicationVersion) {
        PublicationVersionMockFactory.getInstance().registerMock(id, publicationVersion);
    }

    protected static void registerMultidatasetVersionMock(String id, MultidatasetVersion multidatasetVersion) {
        MultidatasetVersionMockFactory.getInstance().registerMock(id, multidatasetVersion);
    }

    protected static List<Object> buildObjectList(Object... objs) {
        return new ArrayList<Object>(Arrays.asList(objs));
    }

    protected static void registerMultidatasetMock(String id, Multidataset multidataset) {
        MultidatasetMockFactory.getInstance().registerMock(id, multidataset);
    }
    
    protected static void registerGeoCovVarElementCacheDatasetVersionMock(String id, GeoCovVarElementCacheDatasetVersion geoCovVarElementCacheDatasetVersion) {
        GeoCovVarElementCacheDatasetVersionFactory.getInstance().registerMock(id, geoCovVarElementCacheDatasetVersion);
    }
    
    protected static GeoCovVarElementCacheDatasetVersion getGeoCovVarElementCacheDatasetVersionMock(String id) {
        return GeoCovVarElementCacheDatasetVersionFactory.getInstance().getMock(id);
    }

}
