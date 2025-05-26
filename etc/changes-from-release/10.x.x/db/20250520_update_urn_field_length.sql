-- --------------------------------------------------------------------------------------------------
-- EDATOS-5034 - Número de caracteres admitidos como contenido en una dimensión
-- 
-- Amplicar la urn en las tablas a 4000 caracteres

-- --------------------------------------------------------------------------------------------------
  
ALTER TABLE TB_STAT_RESOURCES ALTER COLUMN URN  TYPE VARCHAR(4000);

ALTER TABLE TB_M_DIMENSION_ORDERS ALTER COLUMN URN_DIM_COMPONENT_FK  TYPE VARCHAR(4000);

ALTER TABLE TB_CATEGORISATION_SEQUENCES ALTER COLUMN MAINTAINER_URN  TYPE VARCHAR(4000);

COMMIT;
