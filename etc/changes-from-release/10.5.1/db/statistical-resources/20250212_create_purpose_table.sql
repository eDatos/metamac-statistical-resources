-- ---------------------------------------------------------------------------------------------------
-- EDATOS-4824 Permitir la generación automática de tuits en la cuenta del  IBESTAT de la red social X cada vez que se actualiza un  determinado conjunto de datasets seleccionados
-- ---------------------------------------------------------------------------------------------------
CREATE TABLE tb_purposes (
	id int8 NOT NULL,
	identifier varchar(255) NOT NULL,
	"version" int8 NOT NULL,
	description_fk int8 NULL,
	CONSTRAINT pk_tb_purposes PRIMARY KEY (id),
	CONSTRAINT fk_tb_purposes_description_fk FOREIGN KEY (description_fk) REFERENCES tb_international_strings(id) DEFERRABLE
);
commit;
CREATE INDEX pk_tb_purposes_description_fk ON tb_purposes USING btree (description_fk);
commit;
alter table tb_stat_resources add column purpose_fk int8 null;
commit;
alter table tb_stat_resources add CONSTRAINT fk_tb_datasets_versions_purpose_fk FOREIGN KEY (purpose_fk) REFERENCES tb_purposes(id) deferrable;
commit;
INSERT INTO tb_purposes
(id, identifier, "version", description_fk)
VALUES(1, 'SOCIAL_NETWORK', 0, NULL);
INSERT INTO tb_purposes
(id, identifier, "version", description_fk)
VALUES(2, 'DATA_FILTER', 0, NULL);
commit;