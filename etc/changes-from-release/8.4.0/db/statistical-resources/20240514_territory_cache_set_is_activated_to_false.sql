-- --------------------------------------------------------------------------------------------------
-- EDATOS-4439 - Error en el job de borrado de entradas de la caché geográfica
-- 
-- Se detectan dos entradas de caché que no están desactivadas cuando el resto de entradas para esa urn se desactivaron. 
-- No se logra reproducir el error, pero estas dos entradas hay que desactivarlas.

--"id","urn","variable_element_fk","code","title_fk","operation_code","operation_urn","operation_title_fk","htmllink","is_external_source","is_last_version","is_activated"
--1829,urn:siemac:es.caib.ibestat.infomodel.jaxi.Dataset=IBESTAT:pad_t2e2_22,2513,pad_t2e2_22,2807,"000001A",urn:siemac:org.siemac.metamac.infomodel.statisticaloperations.Operation=000001A,260365,https://proves.caib.es/ibestat-jaxi-web/tabla.do?pxId=#px&pag=1&nodeId=2acef6cf-175a-4826-b71e-8302b13c1262&pxName=pad_t2e2_22.px,true,true,true
--76548,urn:siemac:es.caib.ibestat.infomodel.jaxi.Dataset=IBESTAT:def_11290,91771,def_11290,147137,"000006A",urn:siemac:org.siemac.metamac.infomodel.statisticaloperations.Operation=000006A,289317,https://intranet.caib.es/ibestat-jaxi/tabla.do?px=3110a5c3-8331-4262-ba8b-fc5089f26cfd&pag=1&nodeId=c15a61f7-b12e-42d7-abed-bb6f03343656&pxName=def_11290.px,true,true,true
-- --------------------------------------------------------------------------------------------------


update tb_geocov_varelem_cache_datasets_versions a
set is_activated = FALSE
where id in(76548, 1829);

commit;