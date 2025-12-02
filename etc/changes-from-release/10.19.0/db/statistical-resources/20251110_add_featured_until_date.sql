-- --------------------------------------------------------------------------------------------------
-- EDATOS-5336 - Implementar etiquetas destacado y reciente en gestor de recursos estadísticos
-- --------------------------------------------------------------------------------------------------

-- añadir nuevo campo destacado hasta la fecha

ALTER TABLE TB_STAT_RESOURCES
ADD COLUMN FEATURED_UNTIL_DATE_TZ VARCHAR(50);
  
COMMIT;
  
ALTER TABLE TB_STAT_RESOURCES
ADD COLUMN  FEATURED_UNTIL_DATE TIMESTAMP;

commit;
 
