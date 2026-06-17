----------------------------------------------------------------------------------------------------------------------------
-- EDATOS-5431 - Modificar la API del statistical-resources para incluir cabeceras adecuadas de Cache / LastModified / E-tag
----------------------------------------------------------------------------------------------------------------------------

ALTER TABLE TB_STAT_RESOURCES
ADD COLUMN PATCH INTEGER NOT NULL DEFAULT 0;

COMMIT;
