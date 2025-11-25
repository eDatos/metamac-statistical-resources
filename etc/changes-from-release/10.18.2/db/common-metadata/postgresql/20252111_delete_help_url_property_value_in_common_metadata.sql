-- -----------------------------------------------------------------------------------------------------
-- EDATOS-3941 El botón de ayuda de las aplicaciones de gestión interna apunta a un recurso inexistente
-- -----------------------------------------------------------------------------------------------------

UPDATE tb_data_configurations SET conf_value = NULL WHERE conf_key = 'metamac.statistical_resources.help.url';

commit;



