-- ---------------------------------------------------------------------------------------------------
-- EDATOS-4232 La generación de ficheros xlsx de datasets da error si se supera 1.048.576 observaciones (https://support.microsoft.com/en-gb/office/excel-specifications-and-limits-1672b34d-7043-467e-8e27-269d656771c3#ID0EDBD=Office_2010)
    -- "edatos.rest.export.max_xlsx_rows"
-- ---------------------------------------------------------------------------------------------------


insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'edatos.rest.export.max_xlsx_rows','1048576',false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

commit;

