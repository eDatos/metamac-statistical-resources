-- ---------------------------------------------------------------------------------------------------
-- EDATOS-3555 - Creación de vistas en el data de statistical-resources
-- ---------------------------------------------------------------------------------------------------
-- Script de adecuación para la creación de las vistas asociadas a los datasets existentes previamente
-- a este desarrollo. Este script GENERA las sentencias a ejecutar para crear dichas vistas. Es
-- importante seguir los pasos indicados en el mismo.
-- ---------------------------------------------------------------------------------------------------
-- NOTA IMPORTANTE: Este script requiere de la ejecución PREVIA de los siguientes scripts:
-- - /etc/changes-from-release/3.10.2/db/common-metadata/DB_TYPE/20220221_create_role_property.sql
-- - /etc/changes-from-release/3.10.2/db/statistical-resources/DB_TYPE/20220218_adding_view_code_column.sql
-- ---------------------------------------------------------------------------------------------------

-- ---------------------------------------------------------------------------------------------------
-- PASO 1: Obtener el rol al que se le darán permisos de select sobre las vistas creadas
-- ---------------------------------------------------------------------------------------------------
-- La siguiente sentencia debe ejecutarse en el esquema common-metadata y sustituir en la consulta
-- del PASO 2 el FILL_ME_WITH_ROLE con dicho valor.
-- ---------------------------------------------------------------------------------------------------
select conf_value from tb_data_configurations where conf_key = 'metamac.statistical_resources.bbbd.data_views_role';

-- ---------------------------------------------------------------------------------------------------
-- PASO 2: Generar las sentencias que crearán las vistas y asignarar los permisos de select
-- ---------------------------------------------------------------------------------------------------
-- La siguiente sentencia debe ejecutarse en el esquema de METADATOS del statistical-resources. Es
-- importante revisar que se ha sustituido el FILL_ME_WITH_ROLE con el valor obtenido en el PASO 1.
-- ---------------------------------------------------------------------------------------------------
SELECT sql_statements || case when row_number() over(order by td_id) = count(*) over() then ';' else ' UNION ALL' end sql_script 
    FROM
    ( SELECT 'SELECT ''CREATE OR REPLACE VIEW ' || td.view_code || ' AS SELECT * FROM '' || TABLE_NAME || ''; '' || ''GRANT SELECT ON ' || td.view_code || ' TO FILL_ME_WITH_ROLE;'' FROM TB_DATASETS WHERE DATASET_ID = ''' || tdv.dataset_repository_id || '''' AS sql_statements,
            td.id AS td_id, tdv.id AS tvd_id, td.view_code, tdv.dataset_repository_id, tsr.proc_status, tsr.valid_to, tsr.version_logic, to_number(tsr.version_logic, '99999.99999') AS version_number
        FROM tb_datasets td, tb_datasets_versions tdv, tb_stat_resources tsr
        WHERE
            tdv.dataset_fk = td.id
            AND tdv.siemac_resource_fk = tsr.id
            AND ( td.id, to_number(tsr.version_logic, '99999.99999') ) IN (
                SELECT tdx.id, MAX(to_number(tsrx.version_logic, '99999.99999'))
                FROM tb_datasets tdx, tb_datasets_versions tdvx, tb_stat_resources tsrx
                WHERE
                    tdvx.dataset_fk = tdx.id
                    AND tdvx.siemac_resource_fk = tsrx.id
                GROUP BY tdx.id
            )
        ORDER BY td.id ASC, tdv.id ASC
    ) query;


-- ---------------------------------------------------------------------------------------------------
-- PASO 3: Completar las sentencias con la tabla del data a la que debe apuntar la vista
-- ---------------------------------------------------------------------------------------------------
-- Las sentencias generadas en la columna sql_script de la consulta del PASO 2 deben ejecutarse en el 
-- esquema de DATOS del statistical-resources. En este paso se generarán nuevas sentencias SQL que 
-- incorporarán el nombre de la tabla del data a la que deben apuntar las vistas.
-- A continuación puede verse un ejemplo de las consultas generadas.
-- ---------------------------------------------------------------------------------------------------
-- SELECT 'CREATE OR REPLACE VIEW DV_C00010A_000031 AS SELECT * FROM ' || TABLE_NAME || '; ' || 'GRANT SELECT ON DV_C00010A_000031 TO INDICATORS_DATA_ROLE;' FROM TB_DATASETS WHERE DATASET_ID = 'urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Dataset=ISTAC:C00010A_000031(1.0)' UNION ALL
-- SELECT 'CREATE OR REPLACE VIEW DV_C00010A_000032 AS SELECT * FROM ' || TABLE_NAME || '; ' || 'GRANT SELECT ON DV_C00010A_000032 TO INDICATORS_DATA_ROLE;' FROM TB_DATASETS WHERE DATASET_ID = 'urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Dataset=ISTAC:C00010A_000032(1.0)' UNION ALL
-- SELECT 'CREATE OR REPLACE VIEW DV_C00010A_000033 AS SELECT * FROM ' || TABLE_NAME || '; ' || 'GRANT SELECT ON DV_C00010A_000033 TO INDICATORS_DATA_ROLE;' FROM TB_DATASETS WHERE DATASET_ID = 'urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Dataset=ISTAC:C00010A_000033(1.0)';    
    
-- ---------------------------------------------------------------------------------------------------
-- PASO 4: Ejecución efectiva de las sentencias generadas
-- ---------------------------------------------------------------------------------------------------
-- Las sentencias generadas en el PASO 3 deben ejecutarse para hacer efectivos los cambios que 
-- proponen. Estas sentencias deben ejecutarse en el esquema de DATOS del statistical-resources. A 
-- continuación puede verse un ejemplo de las consultas a ejecutar.
-- ---------------------------------------------------------------------------------------------------
-- CREATE OR REPLACE VIEW DV_C00010A_000031 AS SELECT * FROM DATA_C00010A_000031_10; GRANT SELECT ON DV_C00010A_000031 TO FILL_ME_WITH_ROLE;
-- CREATE OR REPLACE VIEW DV_C00010A_000032 AS SELECT * FROM DATA_C00010A_000032_10; GRANT SELECT ON DV_C00010A_000032 TO FILL_ME_WITH_ROLE;
-- CREATE OR REPLACE VIEW DV_C00010A_000033 AS SELECT * FROM DATA_C00010A_000033_10; GRANT SELECT ON DV_C00010A_000033 TO FILL_ME_WITH_ROLE;
    
    