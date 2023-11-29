-- --------------------------------------------------------------------------------------------------
-- EDATOS-4271 - Mostrar el número de celdas de una tabla
-- 
-- Actualizar ckan con nueva dimensión de datasets para que los xlsx que superan tamaño máximo no aparezcan en ckan.
-- --------------------------------------------------------------------------------------------------


update tb_data_configurations
set conf_value = '0 03 09 03 NOV ? 2023' --0 30 20 29 AUG ? 2023  29 de agosto de 2023 a las 20:30   
where conf_key = 'metamac.statistical_resources.resend_dataset_kafka_message.cron_expression'

commit;

--!!ATENCIÓN Requiere reinicio de servidor para que coja el cambio. Ver hora de ejecución ya que tarda bastante. Ver lo que se puso en release anterior.