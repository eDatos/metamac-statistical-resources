-- --------------------------------------------------------------------------------------------------
-- EDATOS-4945 - Añadir un nuevo tipo de atributo que sea internationalString

--  Añadir nuevo campo para albergar atributos del tipo international string válidos desde SDMX 3.0. 
-- Quitar como obligatorio el campo Title ya que para los international string se crea un nuevo campo.


-- --------------------------------------------------------------------------------------------------

alter table tb_attribute_values add column value_fk BIGINT;

ALTER TABLE tb_attribute_values ADD CONSTRAINT FK_TB_ATTRIBUTE_VALUES_VALUE_FK	FOREIGN KEY (VALUE_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID) DEFERRABLE initially IMMEDIATE;

CREATE INDEX pk_tb_attribute_values_value_fk ON tb_attribute_values (value_fk);

ALTER TABLE tb_attribute_values ALTER COLUMN TITLE DROP NOT NULL;

commit;