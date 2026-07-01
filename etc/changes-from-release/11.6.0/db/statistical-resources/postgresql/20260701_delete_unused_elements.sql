----------------------------------------------------------------------------------------------------------------------------
-- EDATOS-5796 - Los valores que han de ser precargados en la aplicación deben estar en los create
----------------------------------------------------------------------------------------------------------------------------

-- Elimina de la BD elementos que ya no se usan

DROP SEQUENCE SEQ_GEOCOV_VARELEM_CACHE_DATASETS_VERSIONS;

alter table TB_QUERIES_VERSIONS
    drop constraint FK_TB_QUERIES_VERSIONS_X_TEMPLATES_FK;

alter table TB_QUERIES_VERSIONS
    drop column X_TEMPLATES_FK;

COMMIT;
