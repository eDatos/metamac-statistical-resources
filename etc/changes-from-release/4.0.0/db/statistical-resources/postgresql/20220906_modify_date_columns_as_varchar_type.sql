-- --------------------------------------------------------------------------------------------------
-- EDATOS-3744 Formato de fecha de próxima actualización debe ser del tipo sdmx
      -- DATE_NEXT_UPDATE
	  -- NEXT_VERSION_DATE 
-- Lo siguientes scripts permitirán cambiar el tipo de dato de los dos campos anteriores de DateTime a String en formato sdxm. Además, se realiza un proceso de migración del valor en formato datetime al valor en formato fecha en sdmx (YYY-MM-DD)	

-- Se crea EDATOS-3804 como tarea de soporte para borrar las tablas temporales una vez la tarea lleve en PRO un tiempo prudencial.  
-- --------------------------------------------------------------------------------------------------

------------- PASAR "DATE_NEXT_UPDATE" DE DATE A VARCHAR(255)

--1) Generar una tabla temporal para almacenar el antiguo valor tipo timestamp y el valor del timezone. También se almacena el nuevo valor en formato sdmx.
create  TABLE TEMP_TB_DATASETS_VERSIONS(
ID INT8,
DATE_NEXT_UPDATE timestamp,
DATE_NEXT_UPDATE_TZ varchar(50), 
DATE_NEXT_UPDATE_NEW varchar(255));

--2) Rellenar la tabla temporal a partir de la tabla TB_DATASETS_VERSIONS con el valor antiguo así como el nuevo valor en sdmx.
insert into TEMP_TB_DATASETS_VERSIONS(ID, DATE_NEXT_UPDATE, DATE_NEXT_UPDATE_TZ, DATE_NEXT_UPDATE_NEW)
select ID, DATE_NEXT_UPDATE, DATE_NEXT_UPDATE_TZ, TO_CHAR(DATE_NEXT_UPDATE, 'YYYY-MM-DD') from TB_DATASETS_VERSIONS where DATE_NEXT_UPDATE is not NULL;

--3) Asegurarse que se han volcado los datos en la tabla temporal
select count(*) from TEMP_TB_DATASETS_VERSIONS;

--4) Borrar los campos antiguos 
ALTER TABLE TB_DATASETS_VERSIONS drop column DATE_NEXT_UPDATE;
ALTER TABLE TB_DATASETS_VERSIONS drop column DATE_NEXT_UPDATE_TZ;

--5) Crear otra vez el campo como tipo varchar 
ALTER TABLE TB_DATASETS_VERSIONS ADD COLUMN DATE_NEXT_UPDATE varchar(255);

--6) Actualizar los valores en TB_DATASETS_VERSIONS a partir de los valores de la tabla temporal.
update TB_DATASETS_VERSIONS
   set DATE_NEXT_UPDATE = DATE_NEXT_UPDATE_NEW 
  from TEMP_TB_DATASETS_VERSIONS 
  where TEMP_TB_DATASETS_VERSIONS.id = TB_DATASETS_VERSIONS.id;


------------- PASAR "NEXT_VERSION_DATE" DE DATE A VARCHAR(255)

--Se hace un procedimiento similar al aplicado para TB_DATASETS_VERSIONS

--1) Generar una tabla temporal para almacenar el antiguo valor tipo timestamp y el valor del timezone. También se almacena el nuevo valor en formato sdmx.
create  TABLE TEMP_TB_STAT_RESOURCES(
ID INT8,
NEXT_VERSION_DATE timestamp,
NEXT_VERSION_DATE_TZ varchar(50),
NEXT_VERSION_DATE_NEW varchar(255));

--2) Rellenar la tabla temporal a partir de la tabla TB_STAT_RESOURCES con el valor antiguo así como el nuevo valor en sdmx.
insert into TEMP_TB_STAT_RESOURCES(ID, NEXT_VERSION_DATE, NEXT_VERSION_DATE_TZ,  NEXT_VERSION_DATE_NEW)
select ID, NEXT_VERSION_DATE, NEXT_VERSION_DATE_TZ, TO_CHAR(NEXT_VERSION_DATE, 'YYYY-MM-DD') from TB_STAT_RESOURCES where NEXT_VERSION_DATE is not NULL;

--3) OPCIONAL. Asegurarse que se han volcado los datos en la tabla temporal
select count(*) from TEMP_TB_STAT_RESOURCES;

--4) Borrar los campos antiguos 
ALTER TABLE TB_STAT_RESOURCES drop column NEXT_VERSION_DATE;
ALTER TABLE TB_STAT_RESOURCES drop column NEXT_VERSION_DATE_TZ;

--5) Crear otra vez el campo como tipo varchar
ALTER TABLE TB_STAT_RESOURCES ADD COLUMN NEXT_VERSION_DATE varchar(255);

--6) Actualizar los valores en TEMP_TB_STAT_RESOURCES a partir de los valores de la tabla temporal.
update TB_STAT_RESOURCES
set NEXT_VERSION_DATE =  NEXT_VERSION_DATE_NEW
from TEMP_TB_STAT_RESOURCES where TEMP_TB_STAT_RESOURCES.id = TB_STAT_RESOURCES.id;

commit;