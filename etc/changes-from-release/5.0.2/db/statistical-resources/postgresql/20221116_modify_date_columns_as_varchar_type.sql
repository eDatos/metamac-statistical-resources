-- --------------------------------------------------------------------------------------------------
-- EDATOS-3744 Formato de fecha de próxima actualización debe ser del tipo sdmx
      -- DATE_NEXT_UPDATE
	  -- NEXT_VERSION_DATE 
-- Lo siguientes scripts permitirán cambiar el tipo de dato de los dos campos anteriores de DateTime a String en formato sdxm. Además, se realiza un proceso de migración del valor en formato datetime al valor en formato fecha en sdmx (YYY-MM-DD)	

-- Se crea EDATOS-3804 como tarea de soporte para borrar las tablas temporales una vez la tarea lleve en PRO un tiempo prudencial.  
-- --------------------------------------------------------------------------------------------------

------------- PASAR "DATE_START" DE DATE A VARCHAR(255)

--1) Crear campo temporal DATE_START_CHAR
ALTER TABLE TB_DATASETS_VERSIONS ADD COLUMN DATE_START_CHAR varchar(255);

--2) Actualizar el campo nuevo creado con 
update TB_DATASETS_VERSIONS D_V
set DATE_START_CHAR = TO_CHAR(d_v1.DATE_START, 'YYYY-MM-DD') 
from TB_DATASETS_VERSIONS D_V1
where D_V1.id = D_V.id;

--3) Eliminamos la antigua columna DATE_START
ALTER TABLE TB_DATASETS_VERSIONS drop column DATE_START;

--4) Modificamos el nombre de la columna DATE_START_CHAR A DATE_START
ALTER TABLE TB_DATASETS_VERSIONS RENAME COLUMN DATE_START_CHAR TO DATE_START;


------------- PASAR "DATE_END" DE DATE A VARCHAR(255)

--1) Crear campo temporal DATE_END_CHAR
ALTER TABLE TB_DATASETS_VERSIONS ADD COLUMN DATE_END_CHAR varchar(255);

--2) Actualizar el campo nuevo creado con 
update TB_DATASETS_VERSIONS D_V
set DATE_END_CHAR = TO_CHAR(d_v1.DATE_END, 'YYYY-MM-DD') 
from TB_DATASETS_VERSIONS D_V1
where D_V1.id = D_V.id;

--3) Eliminamos la antigua columna DATE_END
ALTER TABLE TB_DATASETS_VERSIONS drop column DATE_END;

--4) Modificamos el nombre de la columna DATE_END_CHAR A DATE_END
ALTER TABLE TB_DATASETS_VERSIONS RENAME COLUMN DATE_END_CHAR TO DATE_END;

commit;