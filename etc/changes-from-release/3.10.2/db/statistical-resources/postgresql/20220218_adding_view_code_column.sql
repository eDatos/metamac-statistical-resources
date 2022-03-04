-- ---------------------------------------------------------------------------------------------------
-- EDATOS-3555 - Creación de vistas en el data de statistical-resources
-- ---------------------------------------------------------------------------------------------------

ALTER TABLE TB_DATASETS ADD COLUMN VIEW_CODE VARCHAR(30);

UPDATE TB_DATASETS SET VIEW_CODE = SUBQUERY.VIEW_NAME FROM (SELECT 'DV_' || CODE as VIEW_NAME, ID FROM TB_STAT_RESOURCES) AS SUBQUERY WHERE IDENTIFIABLE_RESOURCE_FK = SUBQUERY.ID;

ALTER TABLE TB_DATASETS ALTER COLUMN VIEW_CODE SET NOT NULL;

commit;