-- ---------------------------------------------------------------------------------------------------
-- EDATOS-3555 - Creación de vistas en el data de statistical-resources
-- ---------------------------------------------------------------------------------------------------

ALTER TABLE TB_DATASETS ADD VIEW_CODE VARCHAR2(30 CHAR);

UPDATE TB_DATASETS td SET VIEW_CODE = (SELECT 'DV_' || CODE FROM TB_STAT_RESOURCES tsr WHERE tsr.id = td.IDENTIFIABLE_RESOURCE_FK);

ALTER TABLE TB_DATASETS MODIFY (VIEW_CODE NOT NULL);

commit;