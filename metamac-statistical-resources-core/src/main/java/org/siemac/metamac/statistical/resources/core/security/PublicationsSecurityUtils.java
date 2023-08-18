package org.siemac.metamac.statistical.resources.core.security;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.sso.utils.SecurityUtils;
import org.siemac.metamac.statistical.resources.core.dto.publication.PublicationVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.publication.domain.Chapter;
import org.siemac.metamac.statistical.resources.core.publication.domain.Cube;
import org.siemac.metamac.statistical.resources.core.publication.domain.PublicationVersion;
import org.siemac.metamac.statistical.resources.core.security.shared.SharedPublicationsSecurityUtils;

public class PublicationsSecurityUtils extends SecurityUtils {

    // ------------------------------------------------------------------------
    // PUBLICATIONS
    // ------------------------------------------------------------------------

    public static void canFindPublicationsByCondition(ServiceContext ctx) throws MetamacException {
        if (!SharedPublicationsSecurityUtils.canFindPublications(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    // ------------------------------------------------------------------------
    // PUBLICATIONS VERSIONS
    // ------------------------------------------------------------------------

    public static void canCreatePublication(ServiceContext ctx, String operationCode) throws MetamacException {
        if (!SharedPublicationsSecurityUtils.canCreatePublication(getMetamacPrincipal(ctx), operationCode)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canUpdatePublicationVersion(ServiceContext ctx, PublicationVersionDto publicationVersionDto) throws MetamacException {
        String operationCode = publicationVersionDto.getStatisticalOperation().getCode();
        ProcStatusEnum procStatus = publicationVersionDto.getProcStatus();
        if (!SharedPublicationsSecurityUtils.canUpdatePublicationVersion(getMetamacPrincipal(ctx), operationCode, procStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canDeletePublicationVersion(ServiceContext ctx, PublicationVersionDto publicationVersionDto) throws MetamacException {
        String operationCode = publicationVersionDto.getStatisticalOperation().getCode();
        ProcStatusEnum procStatus = publicationVersionDto.getProcStatus();

        if (!SharedPublicationsSecurityUtils.canDeletePublicationVersion(getMetamacPrincipal(ctx), operationCode, procStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canFindPublicationsVersionsByCondition(ServiceContext ctx) throws MetamacException {
        if (!SharedPublicationsSecurityUtils.canFindPublicationsVersionsByCondition(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrievePublicationVersionByUrn(ServiceContext ctx, PublicationVersion publicationVersion) throws MetamacException {
        String operationCode = publicationVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();
        ProcStatusEnum procStatus = publicationVersion.getSiemacMetadataStatisticalResource().getEffectiveProcStatus();

        if (!SharedPublicationsSecurityUtils.canRetrievePublicationVersionByUrn(getMetamacPrincipal(ctx), operationCode, procStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveLatestPublicationVersion(ServiceContext ctx, String operationCode, ProcStatusEnum procStatus) throws MetamacException {
        if (!SharedPublicationsSecurityUtils.canRetrieveLatestPublicationVersion(getMetamacPrincipal(ctx), operationCode, procStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveLatestPublishedPublicationVersion(ServiceContext ctx) throws MetamacException {
        if (!SharedPublicationsSecurityUtils.canRetrieveLatestPublishedPublicationVersion(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrievePublicationVersions(ServiceContext ctx) throws MetamacException {
        if (!SharedPublicationsSecurityUtils.canRetrievePublicationVersions(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canVersionPublication(ServiceContext ctx, String operationCode) throws MetamacException {
        if (!SharedPublicationsSecurityUtils.canVersionPublication(getMetamacPrincipal(ctx), operationCode)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrievePublicationVersionStructure(ServiceContext ctx, PublicationVersion publicationVersion) throws MetamacException {
        String operationCode = publicationVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();
        ProcStatusEnum procStatus = publicationVersion.getSiemacMetadataStatisticalResource().getEffectiveProcStatus();

        if (!SharedPublicationsSecurityUtils.canRetrievePublicationVersionStructure(getMetamacPrincipal(ctx), operationCode, procStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canImportPublicationVersionStructure(ServiceContext ctx, PublicationVersion publicationVersion) throws MetamacException {
        String operationCode = publicationVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();
        ProcStatusEnum procStatus = publicationVersion.getLifeCycleStatisticalResource().getProcStatus();
        if (!SharedPublicationsSecurityUtils.canImportPublicationVersionStructure(getMetamacPrincipal(ctx), operationCode, procStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canSendPublicationVersionToProductionValidation(ServiceContext ctx, String operationCode) throws MetamacException {
        if (!SharedPublicationsSecurityUtils.canSendPublicationVersionToProductionValidation(getMetamacPrincipal(ctx), operationCode)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canSendPublicationVersionToDiffusionValidation(ServiceContext ctx, String operationCode) throws MetamacException {
        if (!SharedPublicationsSecurityUtils.canSendPublicationVersionToDiffusionValidation(getMetamacPrincipal(ctx), operationCode)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canSendPublicationVersionToValidationRejected(ServiceContext ctx, String operationCode) throws MetamacException {
        if (!SharedPublicationsSecurityUtils.canSendPublicationVersionToValidationRejected(getMetamacPrincipal(ctx), operationCode)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canPublishPublicationVersion(ServiceContext ctx, String operationCode) throws MetamacException {
        if (!SharedPublicationsSecurityUtils.canPublishPublicationVersion(getMetamacPrincipal(ctx), operationCode)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canResendPublishedPublicationVersionStreamMessage(ServiceContext ctx, String operationCode) throws MetamacException {
        if (!SharedPublicationsSecurityUtils.canPublishPublicationVersion(getMetamacPrincipal(ctx), operationCode)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    // ------------------------------------------------------------------------
    // CHAPTERS
    // ------------------------------------------------------------------------

    public static void canCreateChapter(ServiceContext ctx, PublicationVersion publicationVersion) throws MetamacException {
        String operationCode = publicationVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();
        ProcStatusEnum procStatus = publicationVersion.getSiemacMetadataStatisticalResource().getEffectiveProcStatus();

        if (!SharedPublicationsSecurityUtils.canCreateChapter(getMetamacPrincipal(ctx), operationCode, procStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canUpdateChapter(ServiceContext ctx, Chapter chapter) throws MetamacException {
        String operationCode = chapter.getElementLevel().getPublicationVersion().getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();
        ProcStatusEnum procStatus = chapter.getElementLevel().getPublicationVersion().getSiemacMetadataStatisticalResource().getEffectiveProcStatus();

        if (!SharedPublicationsSecurityUtils.canUpdateChapter(getMetamacPrincipal(ctx), operationCode, procStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canUpdateChapterLocation(ServiceContext ctx, Chapter chapter) throws MetamacException {
        String operationCode = chapter.getElementLevel().getPublicationVersion().getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();
        ProcStatusEnum procStatus = chapter.getElementLevel().getPublicationVersion().getSiemacMetadataStatisticalResource().getEffectiveProcStatus();

        if (!SharedPublicationsSecurityUtils.canUpdateChapterLocation(getMetamacPrincipal(ctx), operationCode, procStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveChapter(ServiceContext ctx, Chapter chapter) throws MetamacException {
        String operationCode = chapter.getElementLevel().getPublicationVersion().getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();
        ProcStatusEnum procStatus = chapter.getElementLevel().getPublicationVersion().getSiemacMetadataStatisticalResource().getEffectiveProcStatus();

        if (!SharedPublicationsSecurityUtils.canRetrieveChapter(getMetamacPrincipal(ctx), operationCode, procStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canDeleteChapter(ServiceContext ctx, Chapter chapter) throws MetamacException {
        String operationCode = chapter.getElementLevel().getPublicationVersion().getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();
        ProcStatusEnum procStatus = chapter.getElementLevel().getPublicationVersion().getSiemacMetadataStatisticalResource().getEffectiveProcStatus();

        if (!SharedPublicationsSecurityUtils.canDeleteChapter(getMetamacPrincipal(ctx), operationCode, procStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    // ------------------------------------------------------------------------
    // CUBES
    // ------------------------------------------------------------------------

    public static void canCreateCube(ServiceContext ctx, PublicationVersion publicationVersion) throws MetamacException {
        String operationCode = publicationVersion.getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();
        ProcStatusEnum procStatus = publicationVersion.getSiemacMetadataStatisticalResource().getEffectiveProcStatus();

        if (!SharedPublicationsSecurityUtils.canCreateCube(getMetamacPrincipal(ctx), operationCode, procStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canUpdateCube(ServiceContext ctx, Cube cube) throws MetamacException {
        String operationCode = cube.getElementLevel().getPublicationVersion().getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();
        ProcStatusEnum procStatus = cube.getElementLevel().getPublicationVersion().getSiemacMetadataStatisticalResource().getEffectiveProcStatus();

        if (!SharedPublicationsSecurityUtils.canUpdateCube(getMetamacPrincipal(ctx), operationCode, procStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canUpdateCubeLocation(ServiceContext ctx, Cube cube) throws MetamacException {
        String operationCode = cube.getElementLevel().getPublicationVersion().getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();
        ProcStatusEnum procStatus = cube.getElementLevel().getPublicationVersion().getSiemacMetadataStatisticalResource().getEffectiveProcStatus();

        if (!SharedPublicationsSecurityUtils.canUpdateCubeLocation(getMetamacPrincipal(ctx), operationCode, procStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveCube(ServiceContext ctx, Cube cube) throws MetamacException {
        String operationCode = cube.getElementLevel().getPublicationVersion().getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();
        ProcStatusEnum procStatus = cube.getElementLevel().getPublicationVersion().getSiemacMetadataStatisticalResource().getEffectiveProcStatus();

        if (!SharedPublicationsSecurityUtils.canRetrieveCube(getMetamacPrincipal(ctx), operationCode, procStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canDeleteCube(ServiceContext ctx, Cube cube) throws MetamacException {
        String operationCode = cube.getElementLevel().getPublicationVersion().getSiemacMetadataStatisticalResource().getStatisticalOperation().getCode();
        ProcStatusEnum procStatus = cube.getElementLevel().getPublicationVersion().getSiemacMetadataStatisticalResource().getEffectiveProcStatus();

        if (!SharedPublicationsSecurityUtils.canDeleteCube(getMetamacPrincipal(ctx), operationCode, procStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

}
