package org.siemac.metamac.statistical.resources.core.publication.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.PublicationsAsserts.assertEqualsChapter;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.PublicationsAsserts.assertEqualsCube;
import static org.siemac.metamac.statistical.resources.core.utils.asserts.PublicationsAsserts.assertEqualsPublicationVersion;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.ChapterMockFactory.CHAPTER_01_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.DatasetMockFactory.DATASET_01_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_01_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_02_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_03_BASIC_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_41_PUB_NOT_VISIBLE_REPLACES_PUB_VERSION_42_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.PublicationVersionMockFactory.PUBLICATION_VERSION_42_PUB_IS_REPLACED_BY_PUB_VERSION_41_NAME;
import static org.siemac.metamac.statistical.resources.core.utils.mocks.factories.QueryMockFactory.QUERY_01_SIMPLE_NAME;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.test.utils.mocks.configuration.MetamacMock;
import org.siemac.metamac.statistical.resources.core.StatisticalResourcesBaseTest;
import org.siemac.metamac.statistical.resources.core.dto.publication.ChapterDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.CubeDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.PublicationVersionDto;
import org.siemac.metamac.statistical.resources.core.error.ServiceExceptionType;
import org.siemac.metamac.statistical.resources.core.publication.domain.Chapter;
import org.siemac.metamac.statistical.resources.core.publication.domain.Cube;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.utils.asserts.PublicationsAsserts;
import org.siemac.metamac.statistical.resources.core.utils.mocks.templates.StatisticalResourcesDtoMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/statistical-resources/include/rest-services-mockito.xml", "classpath:spring/statistical-resources/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
public class PublicationDto2DoMapperTest extends StatisticalResourcesBaseTest {

    @Autowired
    private PublicationDto2DoMapper publicationDto2DoMapper;

    @Test
    public void testPublicationDtoToDo() throws MetamacException {
        PublicationVersionDto dto = StatisticalResourcesDtoMocks.mockPublicationVersionDto();
        PublicationVersion entity = publicationDto2DoMapper.publicationVersionDtoToDo(dto);
        assertEqualsPublicationVersion(dto, entity);
    }

    @Test
    public void testChapterDtoToDo() throws MetamacException {
        ChapterDto dto = StatisticalResourcesDtoMocks.mockChapterDto();
        Chapter entity = publicationDto2DoMapper.chapterDtoToDo(dto);
        assertEqualsChapter(dto, entity);
    }

    @Test
    @MetamacMock(CHAPTER_01_BASIC_NAME)
    public void testChapterDtoToDoWithParent() throws MetamacException {
        String parentChapterUrn = chapterMockFactory.retrieveMock(CHAPTER_01_BASIC_NAME).getNameableStatisticalResource().getUrn();
        ChapterDto dto = StatisticalResourcesDtoMocks.mockChapterDtoWithParent(parentChapterUrn);
        Chapter entity = publicationDto2DoMapper.chapterDtoToDo(dto);
        assertEqualsChapter(dto, entity);
    }

    @Test
    @MetamacMock(DATASET_01_BASIC_NAME)
    public void datasetCubeDtoToDo() throws MetamacException {
        String datasetUrn = datasetMockFactory.retrieveMock(DATASET_01_BASIC_NAME).getIdentifiableStatisticalResource().getUrn();
        CubeDto dto = StatisticalResourcesDtoMocks.mockDatasetCubeDto(datasetUrn);
        Cube entity = publicationDto2DoMapper.cubeDtoToDo(dto);
        assertEqualsCube(dto, entity);
    }

    @Test
    @MetamacMock({DATASET_01_BASIC_NAME, CHAPTER_01_BASIC_NAME})
    public void datasetCubeDtoToDoWithParent() throws MetamacException {
        String datasetUrn = datasetMockFactory.retrieveMock(DATASET_01_BASIC_NAME).getIdentifiableStatisticalResource().getUrn();
        String parentChapterUrn = chapterMockFactory.retrieveMock(CHAPTER_01_BASIC_NAME).getNameableStatisticalResource().getUrn();
        CubeDto dto = StatisticalResourcesDtoMocks.mockDatasetCubeDtoWithParent(parentChapterUrn, datasetUrn);
        Cube entity = publicationDto2DoMapper.cubeDtoToDo(dto);
        assertEqualsCube(dto, entity);
    }

    @Test
    @MetamacMock(QUERY_01_SIMPLE_NAME)
    public void queryCubeDtoToDo() throws MetamacException {
        String queryUrn = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME).getIdentifiableStatisticalResource().getUrn();
        CubeDto dto = StatisticalResourcesDtoMocks.mockQueryCubeDto(queryUrn);
        Cube entity = publicationDto2DoMapper.cubeDtoToDo(dto);
        assertEqualsCube(dto, entity);
    }

    @Test
    @MetamacMock({QUERY_01_SIMPLE_NAME, CHAPTER_01_BASIC_NAME})
    public void queryCubeDtoToDoWithParent() throws MetamacException {
        String queryUrn = queryMockFactory.retrieveMock(QUERY_01_SIMPLE_NAME).getIdentifiableStatisticalResource().getUrn();
        String parentChapterUrn = chapterMockFactory.retrieveMock(CHAPTER_01_BASIC_NAME).getNameableStatisticalResource().getUrn();
        CubeDto dto = StatisticalResourcesDtoMocks.mockQueryCubeDtoWithParent(parentChapterUrn, queryUrn);
        Cube entity = publicationDto2DoMapper.cubeDtoToDo(dto);
        assertEqualsCube(dto, entity);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_03_BASIC_NAME, PUBLICATION_VERSION_41_PUB_NOT_VISIBLE_REPLACES_PUB_VERSION_42_NAME})
    public void testPublicationDtoToDoCanNotReplaceAPublicationVersionThatHasBeenReplacedAlready() throws MetamacException {
        PublicationVersion resourceAlreadyReplaced = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_42_PUB_IS_REPLACED_BY_PUB_VERSION_41_NAME);

        PublicationVersion source = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_03_BASIC_NAME);
        PublicationVersionDto dto = buildPublicationVersionDtoFromDo(source);

        dto.setReplaces(StatisticalResourcesDtoMocks.mockNotPersistedRelatedResourcePublicationVersionDto(resourceAlreadyReplaced));

        expectedMetamacException(new MetamacException(ServiceExceptionType.PUBLICATION_VERSION_ALREADY_BEEN_REPLACED_BY_OTHER_PUBLICATION_VERSION, source.getSiemacMetadataStatisticalResource()
                .getUrn(), resourceAlreadyReplaced.getSiemacMetadataStatisticalResource().getUrn()));

        publicationDto2DoMapper.publicationVersionDtoToDo(dto);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_41_PUB_NOT_VISIBLE_REPLACES_PUB_VERSION_42_NAME})
    public void testPublicationDtoToDoReplaceAPublicationVersionThatHasBeenReplacedAlreadyByThisPublication() throws MetamacException {
        PublicationVersion resourceAlreadyReplaced = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_42_PUB_IS_REPLACED_BY_PUB_VERSION_41_NAME);

        PublicationVersion source = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_41_PUB_NOT_VISIBLE_REPLACES_PUB_VERSION_42_NAME);
        PublicationVersionDto dto = buildPublicationVersionDtoFromDo(source);

        dto.setReplaces(StatisticalResourcesDtoMocks.mockNotPersistedRelatedResourcePublicationVersionDto(resourceAlreadyReplaced));

        PublicationVersion entity = publicationDto2DoMapper.publicationVersionDtoToDo(dto);
        assertNotNull(entity.getSiemacMetadataStatisticalResource().getReplaces());
        PublicationVersion replacedPublication = entity.getSiemacMetadataStatisticalResource().getReplaces().getPublicationVersion();
        assertEquals(resourceAlreadyReplaced.getSiemacMetadataStatisticalResource().getUrn(), replacedPublication.getSiemacMetadataStatisticalResource().getUrn());
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_01_BASIC_NAME, PUBLICATION_VERSION_02_BASIC_NAME})
    public void testPublicationDtoToDoCanReplaceAPublicationVersionThatHasNotBeenReplacedYet() throws MetamacException {
        PublicationVersion resourceAlreadyReplaced = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_02_BASIC_NAME);

        PublicationVersion source = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_01_BASIC_NAME);
        PublicationVersionDto dto = buildPublicationVersionDtoFromDo(source);

        dto.setReplaces(StatisticalResourcesDtoMocks.mockNotPersistedRelatedResourcePublicationVersionDto(resourceAlreadyReplaced));

        PublicationVersion entity = publicationDto2DoMapper.publicationVersionDtoToDo(dto);

        assertNotNull(entity.getSiemacMetadataStatisticalResource().getReplaces());
        PublicationVersion replacedPublication = entity.getSiemacMetadataStatisticalResource().getReplaces().getPublicationVersion();
        PublicationsAsserts.assertEqualsPublicationVersion(resourceAlreadyReplaced, replacedPublication);
    }

    @Test
    @MetamacMock({PUBLICATION_VERSION_01_BASIC_NAME, PUBLICATION_VERSION_02_BASIC_NAME})
    public void testPublicationDtoToDoReplaceItself() throws MetamacException {
        PublicationVersion source = publicationVersionMockFactory.retrieveMock(PUBLICATION_VERSION_01_BASIC_NAME);
        PublicationVersionDto dto = buildPublicationVersionDtoFromDo(source);

        dto.setReplaces(StatisticalResourcesDtoMocks.mockNotPersistedRelatedResourcePublicationVersionDto(source));

        expectedMetamacException(new MetamacException(ServiceExceptionType.PUBLICATION_VERSION_CANT_REPLACE_ITSELF, source.getSiemacMetadataStatisticalResource().getUrn()));

        publicationDto2DoMapper.publicationVersionDtoToDo(dto);
    }

    private PublicationVersionDto buildPublicationVersionDtoFromDo(PublicationVersion publicationVersion) {
        PublicationVersionDto dto = StatisticalResourcesDtoMocks.mockPublicationVersionDto();
        dto.setCode(publicationVersion.getSiemacMetadataStatisticalResource().getCode());
        dto.setUrn(publicationVersion.getSiemacMetadataStatisticalResource().getUrn());
        dto.setOptimisticLockingVersion(publicationVersion.getVersion());
        dto.setId(publicationVersion.getId());
        return dto;
    }
}
