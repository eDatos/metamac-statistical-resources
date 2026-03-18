package org.siemac.metamac.statistical.resources.web.client.publication.utils;

import org.siemac.metamac.statistical.resources.core.dto.publication.PublicationVersionBaseDto;
import org.siemac.metamac.statistical.resources.core.dto.publication.PublicationVersionDto;
import org.siemac.metamac.statistical.resources.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.statistical.resources.core.security.shared.SharedPublicationsSecurityUtils;
import org.siemac.metamac.statistical.resources.core.security.shared.SharedSecurityUtils;
import org.siemac.metamac.statistical.resources.web.client.base.utils.LifecycleClientSecurityUtils;

public class PublicationClientSecurityUtils extends LifecycleClientSecurityUtils {

    // ------------------------------------------------------------------------
    // PUBLICATION VERSIONS
    // ------------------------------------------------------------------------

    public static boolean canCreatePublication() {
        return SharedPublicationsSecurityUtils.canCreatePublication(getMetamacPrincipal(), getCurrentStatisticalOperationCode());
    }

    public static boolean canUpdatePublicationVersion(PublicationVersionDto publicationVersionDto) {
        if (isPublished(publicationVersionDto.getProcStatus())) {
            return false;
        }
        return SharedPublicationsSecurityUtils.canUpdatePublicationVersion(getMetamacPrincipal(), getCurrentStatisticalOperationCode(), publicationVersionDto.getProcStatus());
    }

    public static boolean canDeletePublicationVersion(PublicationVersionBaseDto dto) {
        return canDeletePublicationVersion(dto.getProcStatus());
    }

    public static boolean canDeletePublicationVersion(PublicationVersionDto dto) {
        return canDeletePublicationVersion(dto.getProcStatus());
    }

    private static boolean canDeletePublicationVersion(ProcStatusEnum publicationProcStatus) {
        if (isPublished(publicationProcStatus)) {
            return false;
        }
        return SharedPublicationsSecurityUtils.canDeletePublicationVersion(getMetamacPrincipal(), getCurrentStatisticalOperationCode(), publicationProcStatus);
    }

    public static boolean canPreviewDataPublicationVersion(PublicationVersionDto publicationVersionDto) {
        return SharedPublicationsSecurityUtils.canPreviewDataPublicationVersion(getMetamacPrincipal(), getCurrentStatisticalOperationCode(), publicationVersionDto.getProcStatus());
    }

    public static boolean canUpdateElementLocation(ProcStatusEnum publicationProcStatus) {
        if (isPublished(publicationProcStatus)) {
            return false;
        }
        return SharedPublicationsSecurityUtils.canUpdateElementLocation(getMetamacPrincipal(), getCurrentStatisticalOperationCode(), publicationProcStatus);
    }

    // ------------------------------------------------------------------------
    // PUBLICATION VERSIONS LIFECYCLE
    // ------------------------------------------------------------------------

    public static boolean canSendPublicationVersionToProductionValidation(PublicationVersionDto dto) {
        return canSendPublicationVersionToProductionValidation(dto.getProcStatus());
    }

    public static boolean canSendPublicationVersionToProductionValidation(PublicationVersionBaseDto dto) {
        return canSendPublicationVersionToProductionValidation(dto.getProcStatus());
    }

    public static boolean canSendPublicationVersionToDiffusionValidation(PublicationVersionDto dto) {
        return canSendPublicationVersionToDiffusionValidation(dto.getProcStatus());
    }

    public static boolean canSendPublicationVersionToDiffusionValidation(PublicationVersionBaseDto dto) {
        return canSendPublicationVersionToDiffusionValidation(dto.getProcStatus());
    }

    public static boolean canSendPublicationVersionToValidationRejected(PublicationVersionDto dto) {
        return canSendPublicationVersionToValidationRejected(dto.getProcStatus());
    }

    public static boolean canSendPublicationVersionToValidationRejected(PublicationVersionBaseDto dto) {
        return canSendPublicationVersionToValidationRejected(dto.getProcStatus());
    }

    public static boolean canPublishPublicationVersion(PublicationVersionDto dto) {
        return canPublishPublicationVersion(dto.getProcStatus());
    }

    public static boolean canPublishPublicationVersion(PublicationVersionBaseDto dto) {
        return canPublishPublicationVersion(dto.getProcStatus());
    }

    public static boolean canResendStreamMessageDatasetVersion(PublicationVersionDto dto) {
        if (!dto.getLastVersion()) {
            return false;
        }

        return canResendStreamMessageDatasetVersion(dto.getProcStatus());
    }

    public static boolean canVersionPublication(PublicationVersionDto dto) {
        return canVersionPublication(dto.getProcStatus());
    }

    public static boolean canVersionPublication(PublicationVersionBaseDto dto) {
        return canVersionPublication(dto.getProcStatus());
    }

    private static boolean canSendPublicationVersionToProductionValidation(ProcStatusEnum procStatus) {
        if (!canSendToProductionValidation(procStatus)) {
            return false;
        }
        return SharedPublicationsSecurityUtils.canSendPublicationVersionToProductionValidation(getMetamacPrincipal(), getCurrentStatisticalOperationCode());
    }

    private static boolean canSendPublicationVersionToDiffusionValidation(ProcStatusEnum procStatus) {
        if (!canSendToDiffusionValidation(procStatus)) {
            return false;
        }
        return SharedPublicationsSecurityUtils.canSendPublicationVersionToDiffusionValidation(getMetamacPrincipal(), getCurrentStatisticalOperationCode());
    }

    private static boolean canSendPublicationVersionToValidationRejected(ProcStatusEnum procStatus) {
        if (!canRejectValidation(procStatus)) {
            return false;
        }
        return SharedPublicationsSecurityUtils.canSendPublicationVersionToValidationRejected(getMetamacPrincipal(), getCurrentStatisticalOperationCode());
    }

    private static boolean canPublishPublicationVersion(ProcStatusEnum procStatus) {
        if (!canPublish(procStatus)) {
            return false;
        }
        return SharedPublicationsSecurityUtils.canPublishPublicationVersion(getMetamacPrincipal(), getCurrentStatisticalOperationCode());
    }

    private static boolean canResendStreamMessageDatasetVersion(ProcStatusEnum procStatus) {
        if (!canResendStreamMessage(procStatus)) {
            return false;
        }

        return SharedPublicationsSecurityUtils.canPublishPublicationVersion(getMetamacPrincipal(), getCurrentStatisticalOperationCode());
    }

    private static boolean canVersionPublication(ProcStatusEnum procStatus) {
        if (!canVersion(procStatus)) {
            return false;
        }
        return SharedPublicationsSecurityUtils.canVersionPublication(getMetamacPrincipal(), getCurrentStatisticalOperationCode());
    }

    // ------------------------------------------------------------------------
    // PUBLICATION STRUCTURE
    // ------------------------------------------------------------------------

    public static boolean canImportPublicationVersionStructure(ProcStatusEnum publicationProcStatus) {
        if (isPublished(publicationProcStatus)) {
            return false;
        }
        return SharedPublicationsSecurityUtils.canImportPublicationVersionStructure(getMetamacPrincipal(), getCurrentStatisticalOperationCode(), publicationProcStatus);
    }

    // ------------------------------------------------------------------------
    // CHAPTERS
    // ------------------------------------------------------------------------

    public static boolean canCreateChapter(ProcStatusEnum publicationProcStatus) {
        if (isPublished(publicationProcStatus)) {
            return false;
        }
        return SharedPublicationsSecurityUtils.canCreateChapter(getMetamacPrincipal(), getCurrentStatisticalOperationCode(), publicationProcStatus);
    }

    public static boolean canUpdateChapter(ProcStatusEnum publicationProcStatus) {
        if (isPublished(publicationProcStatus)) {
            return false;
        }
        return SharedPublicationsSecurityUtils.canUpdateChapter(getMetamacPrincipal(), getCurrentStatisticalOperationCode(), publicationProcStatus);
    }

    public static boolean canUpdateChapterLocation(ProcStatusEnum publicationProcStatus) {
        if (isPublished(publicationProcStatus)) {
            return false;
        }
        return SharedPublicationsSecurityUtils.canUpdateChapterLocation(getMetamacPrincipal(), getCurrentStatisticalOperationCode(), publicationProcStatus);
    }

    public static boolean canDeleteChapter(ProcStatusEnum publicationProcStatus) {
        if (isPublished(publicationProcStatus)) {
            return false;
        }
        return SharedPublicationsSecurityUtils.canDeleteChapter(getMetamacPrincipal(), getCurrentStatisticalOperationCode(), publicationProcStatus);
    }

    // ------------------------------------------------------------------------
    // CUBES
    // ------------------------------------------------------------------------

    public static boolean canCreateCube(ProcStatusEnum publicationProcStatus) {
        if (isPublished(publicationProcStatus)) {
            return false;
        }
        return SharedPublicationsSecurityUtils.canCreateCube(getMetamacPrincipal(), getCurrentStatisticalOperationCode(), publicationProcStatus);
    }

    public static boolean canUpdateCube(ProcStatusEnum publicationProcStatus) {
        if (isPublished(publicationProcStatus)) {
            return false;
        }
        return SharedPublicationsSecurityUtils.canUpdateCube(getMetamacPrincipal(), getCurrentStatisticalOperationCode(), publicationProcStatus);
    }

    public static boolean canUpdateCubeLocation(ProcStatusEnum publicationProcStatus) {
        if (isPublished(publicationProcStatus)) {
            return false;
        }
        return SharedPublicationsSecurityUtils.canUpdateCubeLocation(getMetamacPrincipal(), getCurrentStatisticalOperationCode(), publicationProcStatus);
    }

    public static boolean canDeleteCube(ProcStatusEnum publicationProcStatus) {
        if (isPublished(publicationProcStatus)) {
            return false;
        }
        return SharedPublicationsSecurityUtils.canDeleteCube(getMetamacPrincipal(), getCurrentStatisticalOperationCode(), publicationProcStatus);
    }

    public static boolean canUpdateGeoCacheRelatedResource(PublicationVersionDto dto) {
        if (!isPublished(dto.getProcStatus())) {
            return false;
        }

        // check is the last version published
        if (dto.getValidTo() != null) {
            return false;
        }

        return SharedSecurityUtils.canUpdateGeoCacheRelatedResource(getMetamacPrincipal());
    }

}
