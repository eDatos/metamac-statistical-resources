-- --------------------------------------------------------------------------------------------------
-- EDATOS-5200 - Eliminar código exclusivo de adecuación de datos para permitir descripciones en vistas
--
-- En EDATOS-5154 ha sido necesario crear jobs tanto en indicadores como en statistical-resources 
-- para hacer una adecuación de datos y añadir descripciones a las vistas de datos.
-- Se debe borrar la constante creada ya que el job temporal ya ha sido ejecutado.
-- --------------------------------------------------------------------------------------------------

-- Eliminar la configuración del cron expression del job temporal de ajuste de vistas de datos
DELETE FROM TB_DATA_CONFIGURATIONS 
WHERE CONF_KEY = 'metamac.statistical_resources.data_view_adjustment.cron_expression';

commit;
 
