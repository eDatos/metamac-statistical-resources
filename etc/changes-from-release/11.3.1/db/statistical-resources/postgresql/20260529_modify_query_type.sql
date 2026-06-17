----------------------------------------------------------------------------------------------------------------------------
-- EDATOS-5431 - Modificar la API del statistical-resources para incluir cabeceras adecuadas de Cache / LastModified / E-tag
----------------------------------------------------------------------------------------------------------------------------

ALTER TABLE TB_QUERIES_VERSIONS
RENAME COLUMN TYPE TO QUERY_TYPE;

UPDATE TB_STAT_RESOURCES
SET TYPE = 'QUERY'
WHERE URN ILIKE 'urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Query%';

COMMIT;
