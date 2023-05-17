-- ---------------------------------------------------------------------------------------------------
-- EDATOS-4087 - Error en la generación de uri y managementapplink de external items del srm
-- ---------------------------------------------------------------------------------------------------
-- Script de adecuación para arreglar los external items cuya uri y management_app_link están mal
-- construidos así como informar los international string de aquellos elementos que corresponda.
-- Atención: Una parte de este script GENERA las sentencias a ejecutar para adecuar los datos. Es
-- importante seguir los pasos indicados en el mismo.
-- ---------------------------------------------------------------------------------------------------

-- ***************************************************************************************************
-- Adecuación del metadato uri mal construido de los external items.
-- ***************************************************************************************************
-- ---------------------------------------------------------------------------------------------------
-- PASO 1: Verificar el número de casos afectados existentes ejecutando la siguiente sentencia
-- ---------------------------------------------------------------------------------------------------
     
select count(1) from tb_external_items where uri ilike '%http%';

-- ---------------------------------------------------------------------------------------------------
-- PASO 2: Ejecutar la siguiente sentencia update para adecuar el metadato
-- Importante: debe sustituirse el valor de los FILL_ME por los enlaces existentes en cada entorno
-- ---------------------------------------------------------------------------------------------------
-- DESARROLLO: 
-- -- https://estadisticas.arte-consultores.com/structural-resources-internal/apis/structural-resources-internal
-- -- http:/estadisticas.arte-consultores.com/metamac-common-metdata-external-web/apis/cmetadata

-- PRO ISTAC: 
-- -- https://www3.gobiernodecanarias.net/aplicaciones/structural-resources-internal-istac/apis/structural-resources-internal

-- PRO IESTADIS: 
-- -- https://iestadis.edatos.io/structural-resources-internal/apis/structural-resources-internal

-- PRE IBESTAT: 
-- -- https://pre-ibestat.edatos.io/private/apps/structural-resources-internal/apis/structural-resources-internal
-- ---------------------------------------------------------------------------------------------------

update
     tb_external_items
 set
     uri = replace(replace(replace(uri, 'FILL_ME', ''), 'v1.0', 'latest'), '//', '/')
 where
     uri ilike '%http%';

-- Ejemplo entorno desarrollo:
-- update
--     tb_external_items
-- set
--     uri = replace(replace(replace(uri, 'https://estadisticas.arte-consultores.com/structural-resources-internal/apis/structural-resources-internal', ''), 'v1.0', 'latest'), '//', '/')
-- where
--     uri ilike '%http%';

-- ---------------------------------------------------------------------------------------------------
-- PASO 3: Verificar que no existen casos sin adecuar ejecutando la siguiente sentencia
-- ---------------------------------------------------------------------------------------------------
     
select count(1) from tb_external_items where uri ilike '%http%';

-- ***************************************************************************************************
-- Adecuación del metadato management_app_url mal construido de los external items.
-- ***************************************************************************************************
-- ---------------------------------------------------------------------------------------------------
-- PASO 1: Verificar el número de casos afectados existentes ejecutando la siguiente sentencia
-- ---------------------------------------------------------------------------------------------------

select count(1) from tb_external_items where management_app_url ilike '%http%';

-- ---------------------------------------------------------------------------------------------------
-- PASO 2: Ejecutar la siguiente sentencia update para adecuar el metadato
-- Importante: debe sustituirse el valor de los FILL_ME por los enlaces existentes en cada entorno
-- ---------------------------------------------------------------------------------------------------
-- DESARROLLO: 
-- -- https://estadisticas.arte-consultores.com/structural-resources-internal
-- -- https:/estadisticas.arte-consultores.com/statistical-operations-internal
-- -- https:/estadisticas.arte-consultores.com/common-metadata-internal
 
-- PRO ISTAC: 
-- -- https://www3.gobiernodecanarias.net/aplicaciones/structural-resources-internal-istac
-- -- http://exp-istac-t8/common-metadata-internal

-- PRO IESTADIS: 
-- -- https://iestadis.edatos.io/structural-resources-internal

-- PRE IBESTAT: 
-- -- https://pre-ibestat.edatos.io/private/apps/structural-resources-internal
-- ---------------------------------------------------------------------------------------------------

update
	tb_external_items
set
	management_app_url = replace(replace(management_app_url, 'FILL_ME', ''), '//', '/')
where
	management_app_url ilike '%http%';

-- Ejemplo entorno desarrollo:
-- update
--     tb_external_items
-- set
-- 	management_app_url = replace(replace(management_app_url, 'https://estadisticas.arte-consultores.com/structural-resources-internal', ''), '//', '/')
-- where
--     management_app_url ilike '%http%';	

-- ---------------------------------------------------------------------------------------------------
-- PASO 3: Verificar que no existen casos sin adecuar ejecutando la siguiente sentencia
-- ---------------------------------------------------------------------------------------------------

select count(1) from tb_external_items where management_app_url ilike '%http%';

-- ***************************************************************************************************
-- Adecuación de los international strings no informados en los external items de los proveedores de datos
-- ***************************************************************************************************
-- ---------------------------------------------------------------------------------------------------
-- PASO 1: Verificar el número de casos afectados existentes ejecutando la siguiente sentencia
-- ---------------------------------------------------------------------------------------------------

select count(1) from TB_EI_DATA_PROVIDERS tbedp, tb_external_items tei where tbedp.data_provider_fk = tei.id and type = 'structuralResources#dataProvider' and title_fk is null;

-- ---------------------------------------------------------------------------------------------------
-- PASO 2: Generar las consultas para obtener los títulos de los external items no informados
-- ---------------------------------------------------------------------------------------------------
-- La siguiente sentencia debe ejecutarse en el esquema de METADATOS del statistical-resources. 
-- Esta sentencia generá una serie de sentencias SELECT que deben ejecutarse en el esquema del srm
-- destinadas a consultar el título de los proveedores datos.
-- ---------------------------------------------------------------------------------------------------

select	sql_statements || case when row_number() over(order by id) = count(*) over() then ' order by identifier asc, pos asc;' else ' UNION ALL' end sql_script
from
	(select id, code, '(select ''INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''''seq_i18nstrs''''), 1);'' as sql_statement, ''1'' as pos, ' || id || ' as identifier from tb_organisations tbo, tb_annotable_artefacts tbaa where tbaa.id = tbo.nameable_artefact_fk and tbo.organisation_type = ''DATA_PROVIDER'' and tbaa.code = ''' || code || ''') union (select ''INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) VALUES (nextval(''''seq_l10nstrs''''), '''''' || tls."label" || '''''', '''''' || tls.locale ||'''''', currval(''''seq_i18nstrs''''), 1);'' as sql_statement, ''2'' as pos, ' || id || ' as identifier from tb_organisations tbo, tb_annotable_artefacts tbaa, tb_localised_strings tls where tbaa.id = tbo.nameable_artefact_fk and tbaa.name_fk = tls.international_string_fk and tbo.organisation_type = ''DATA_PROVIDER'' and tbaa.code = ''' || code || ''')  union (select ''UPDATE TB_EXTERNAL_ITEMS SET TITLE_FK = currval(''''seq_i18nstrs'''') WHERE ID = ' || id || ';'' as sql_statement, ''3'' as pos, ' || id || ' as identifier from tb_organisations tbo, tb_annotable_artefacts tbaa where tbaa.id = tbo.nameable_artefact_fk and tbo.organisation_type = ''DATA_PROVIDER'' and tbaa.code = ''' || code || ''')' as sql_statements
	from
		tb_external_items
	where
		id in (select id from TB_EI_DATA_PROVIDERS tbedp, tb_external_items tei where tbedp.data_provider_fk = tei.id and type = 'structuralResources#dataProvider' and title_fk is null)
	order by
		id) as query;
		
-- ---------------------------------------------------------------------------------------------------
-- PASO 3: Generar los inserts/updates para informar los títulos de los external items no informados
-- ---------------------------------------------------------------------------------------------------
-- Las sentencias generadas en la columna sql_script de la consulta del PASO 2 deben ejecutarse en el 
-- esquema del SRM . En este paso se generarán nuevas sentencias SQL para informar el 
-- international string a los proveedores de datos en función de los datos existentes en el SRM
-- A continuación puede verse un ejemplo de las consultas generadas.
-- ---------------------------------------------------------------------------------------------------		
-- (select 'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''seq_i18nstrs''), 1);' as sql_statement, '1' as pos, 624801 as identifier from tb_organisations tbo, tb_annotable_artefacts tbaa where tbaa.id = tbo.nameable_artefact_fk and tbo.organisation_type = 'DATA_PROVIDER' and tbaa.code = 'DREM') union (select 'INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) VALUES (nextval(''seq_l10nstrs''), ''' || tls."label" || ''', ''' || tls.locale ||''', currval(''seq_i18nstrs''), 1);' as sql_statement, '2' as pos, 624801 as identifier from tb_organisations tbo, tb_annotable_artefacts tbaa, tb_localised_strings tls where tbaa.id = tbo.nameable_artefact_fk and tbaa.name_fk = tls.international_string_fk and tbo.organisation_type = 'DATA_PROVIDER' and tbaa.code = 'DREM')  union (select 'UPDATE TB_EXTERNAL_ITEMS SET TITLE_FK = currval(''seq_i18nstrs'') WHERE ID = 624801;' as sql_statement, '3' as pos, 624801 as identifier from tb_organisations tbo, tb_annotable_artefacts tbaa where tbaa.id = tbo.nameable_artefact_fk and tbo.organisation_type = 'DATA_PROVIDER' and tbaa.code = 'DREM') UNION ALL
-- (select 'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''seq_i18nstrs''), 1);' as sql_statement, '1' as pos, 629272 as identifier from tb_organisations tbo, tb_annotable_artefacts tbaa where tbaa.id = tbo.nameable_artefact_fk and tbo.organisation_type = 'DATA_PROVIDER' and tbaa.code = 'DREM') union (select 'INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) VALUES (nextval(''seq_l10nstrs''), ''' || tls."label" || ''', ''' || tls.locale ||''', currval(''seq_i18nstrs''), 1);' as sql_statement, '2' as pos, 629272 as identifier from tb_organisations tbo, tb_annotable_artefacts tbaa, tb_localised_strings tls where tbaa.id = tbo.nameable_artefact_fk and tbaa.name_fk = tls.international_string_fk and tbo.organisation_type = 'DATA_PROVIDER' and tbaa.code = 'DREM')  union (select 'UPDATE TB_EXTERNAL_ITEMS SET TITLE_FK = currval(''seq_i18nstrs'') WHERE ID = 629272;' as sql_statement, '3' as pos, 629272 as identifier from tb_organisations tbo, tb_annotable_artefacts tbaa where tbaa.id = tbo.nameable_artefact_fk and tbo.organisation_type = 'DATA_PROVIDER' and tbaa.code = 'DREM') UNION ALL
-- (select 'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''seq_i18nstrs''), 1);' as sql_statement, '1' as pos, 633753 as identifier from tb_organisations tbo, tb_annotable_artefacts tbaa where tbaa.id = tbo.nameable_artefact_fk and tbo.organisation_type = 'DATA_PROVIDER' and tbaa.code = 'DREM') union (select 'INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) VALUES (nextval(''seq_l10nstrs''), ''' || tls."label" || ''', ''' || tls.locale ||''', currval(''seq_i18nstrs''), 1);' as sql_statement, '2' as pos, 633753 as identifier from tb_organisations tbo, tb_annotable_artefacts tbaa, tb_localised_strings tls where tbaa.id = tbo.nameable_artefact_fk and tbaa.name_fk = tls.international_string_fk and tbo.organisation_type = 'DATA_PROVIDER' and tbaa.code = 'DREM')  union (select 'UPDATE TB_EXTERNAL_ITEMS SET TITLE_FK = currval(''seq_i18nstrs'') WHERE ID = 633753;' as sql_statement, '3' as pos, 633753 as identifier from tb_organisations tbo, tb_annotable_artefacts tbaa where tbaa.id = tbo.nameable_artefact_fk and tbo.organisation_type = 'DATA_PROVIDER' and tbaa.code = 'DREM') UNION ALL

-- ---------------------------------------------------------------------------------------------------
-- PASO 4: Ejecutar los inserts/updates para informar los títulos de los proveedores de datos no informados
-- ---------------------------------------------------------------------------------------------------
-- Las sentencias generadas en la columna sql_statement de la consulta del PASO 3 deben ejecutarse en el 
-- esquema de METADATOS del statistical-resources. En este paso se hará efectiva la creación de los 
-- international string y se actualizarán los registros correspondientes a los proveedores de datos con los
-- international string recien creados.
-- A continuación puede verse un ejemplo de las consultas generadas.
-- ---------------------------------------------------------------------------------------------------				
-- INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval('seq_i18nstrs'), 1);
-- INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) VALUES (nextval('seq_l10nstrs'), 'Direção Regional de Estatística da Madeira', 'pt', currval('seq_i18nstrs'), 1);
-- INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) VALUES (nextval('seq_l10nstrs'), 'Dirección Regional de Estadísticas de Madeira', 'es', currval('seq_i18nstrs'), 1);
-- UPDATE TB_EXTERNAL_ITEMS SET TITLE_FK = currval('seq_i18nstrs') WHERE ID = 624801;
-- INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval('seq_i18nstrs'), 1);
-- INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) VALUES (nextval('seq_l10nstrs'), 'Direção Regional de Estatística da Madeira', 'pt', currval('seq_i18nstrs'), 1);
-- INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) VALUES (nextval('seq_l10nstrs'), 'Dirección Regional de Estadísticas de Madeira', 'es', currval('seq_i18nstrs'), 1);
-- UPDATE TB_EXTERNAL_ITEMS SET TITLE_FK = currval('seq_i18nstrs') WHERE ID = 629272;
-- INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval('seq_i18nstrs'), 1);
-- INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) VALUES (nextval('seq_l10nstrs'), 'Dirección Regional de Estadísticas de Madeira', 'es', currval('seq_i18nstrs'), 1);
-- INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) VALUES (nextval('seq_l10nstrs'), 'Direção Regional de Estatística da Madeira', 'pt', currval('seq_i18nstrs'), 1);
-- UPDATE TB_EXTERNAL_ITEMS SET TITLE_FK = currval('seq_i18nstrs') WHERE ID = 633753;

-- ---------------------------------------------------------------------------------------------------
-- PASO 5: Verificar que no existen casos sin adecuar ejecutando la siguiente sentencia
-- ---------------------------------------------------------------------------------------------------	

select count(1) from TB_EI_DATA_PROVIDERS tbedp, tb_external_items tei where tbedp.data_provider_fk = tei.id and type = 'structuralResources#dataProvider' and title_fk is null;
		
    
    