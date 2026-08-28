package org.siemac.metamac.statistical.resources.core.publication.utils;

import static org.siemac.metamac.statistical.resources.core.base.utils.BaseVersioningCopyUtils.copyNameableStatisticalResource;
import static org.siemac.metamac.statistical.resources.core.base.utils.BaseVersioningCopyUtils.copySiemacMetadataStatisticalResource;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.statistical.resources.core.base.domain.SiemacMetadataStatisticalResource;
import org.siemac.metamac.statistical.resources.core.publication.domain.Chapter;
import org.siemac.metamac.statistical.resources.core.publication.domain.Cube;
import org.siemac.metamac.statistical.resources.core.publication.domain.ElementLevel;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;

public class PublicationVersioningCopyUtils {

    /**
     * Create a new {@link PublicationVersion} copying values from a source.
     */
    public static PublicationVersion copyPublicationVersion(PublicationVersion source) {
        PublicationVersion target = new PublicationVersion();
        target.setSiemacMetadataStatisticalResource(new SiemacMetadataStatisticalResource());
        copyPublicationVersion(source, target);
        return target;
    }

    /**
     * Copy values from a {@link PublicationVersion}
     */
    public static void copyPublicationVersion(PublicationVersion source, PublicationVersion target) {
        // Metadata
        target.setSiemacMetadataStatisticalResource(copySiemacMetadataStatisticalResource(source.getSiemacMetadataStatisticalResource(), target.getSiemacMetadataStatisticalResource()));
        target.setFormatExtentResources(null);
        target.setPublication(source.getPublication());

        // Structure
        copyElementsLevels(source, target);
    }

    private static void copyElementsLevels(PublicationVersion publicationVersionSource, PublicationVersion publicationVersionTarget) {
        List<ElementLevel> targets = new ArrayList<ElementLevel>();
        List<ElementLevel> sources = publicationVersionSource.getChildrenFirstLevel();
        for (ElementLevel source : sources) {
            ElementLevel target = copyElementLevel(source, publicationVersionTarget);
            target.setParent(null);
            target.setPublicationVersion(publicationVersionTarget);
            target.setPublicationVersionFirstLevel(publicationVersionTarget);
            publicationVersionTarget.getChildrenFirstLevel().add(target);
            publicationVersionTarget.getChildrenAllLevels().add(target);
            targets.add(target); // TODO METAMAC-2718 - Eliminar código muerto para evitar mantenerlo (revisar, esto parece innecesario)
        }
    }

    private static ElementLevel copyElementLevel(ElementLevel source, PublicationVersion publicationVersionTarget) {
        ElementLevel target = new ElementLevel();
        if (source.getChapter() != null) {
            Chapter chapterTarget = copyChapter(source.getChapter());
            chapterTarget.setElementLevel(target);
            target.setChapter(chapterTarget);
        } else if (source.getCube() != null) {
            Cube cubeTarget = copyCube(source.getCube());
            cubeTarget.setElementLevel(target);
            target.setCube(cubeTarget);
        }
        target.setOrderInLevel(source.getOrderInLevel());

        for (ElementLevel childrenSource : source.getChildren()) {
            ElementLevel childrenTarget = copyElementLevel(childrenSource, publicationVersionTarget);
            childrenTarget.setParent(target);
            childrenTarget.setPublicationVersion(publicationVersionTarget);
            childrenTarget.setPublicationVersionFirstLevel(null);
            target.addChildren(childrenTarget);
            publicationVersionTarget.getChildrenAllLevels().add(childrenTarget);
        }
        return target;
    }

    private static Cube copyCube(Cube source) {
        if (source == null) {
            return null;
        }
        Cube target = new Cube();
        target.setNameableStatisticalResource(copyNameableStatisticalResource(source.getNameableStatisticalResource(), target.getNameableStatisticalResource()));
        target.fillCodeAndUrn();

        target.setQuery(source.getQuery());
        target.setDataset(source.getDataset());
        target.setMultidataset(source.getMultidataset());
        target.setPublication(source.getPublication());
        target.setUrl(source.getUrl());

        return target;
    }

    private static Chapter copyChapter(Chapter source) {
        if (source == null) {
            return null;
        }
        Chapter target = new Chapter();
        target.setNameableStatisticalResource(copyNameableStatisticalResource(source.getNameableStatisticalResource(), target.getNameableStatisticalResource()));
        target.fillCodeAndUrn();
        target.setOpening(source.getOpening());
        return target;
    }

}