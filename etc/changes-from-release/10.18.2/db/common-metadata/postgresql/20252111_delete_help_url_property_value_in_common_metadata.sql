-- -----------------------------------------------------------------------------------------------------
-- EDATOS-3941 El botón de ayuda de las aplicaciones de gestión interna apunta a un recurso inexistente
-- -----------------------------------------------------------------------------------------------------

--Ejecutar en todos los entornos EXCEPTO en istac pre e istac pro
UPDATE tb_data_configurations SET conf_value = NULL WHERE conf_key = 'metamac.statistical_resources.help.url';

commit;



