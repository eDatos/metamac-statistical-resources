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
alter table tb_queries_versions add column purpose_fk int8 null;
commit;
alter table tb_queries_versions add CONSTRAINT fk_tb_queries_versions_purpose_fk FOREIGN KEY (purpose_fk) REFERENCES tb_purposes(id) deferrable;
commit;
INSERT INTO tb_purposes
(id, identifier, "version", description_fk)
VALUES(1, 'SOCIAL_NETWORK', 0, NULL);
INSERT INTO tb_purposes
(id, identifier, "version", description_fk)
VALUES(2, 'DATA_FILTER', 0, NULL);
commit;

alter table tb_queries_versions add column x_templates_fk int8 null;
commit;
alter table tb_queries_versions add CONSTRAINT fk_tb_queries_versions_x_templates_fk FOREIGN KEY (x_templates_fk) REFERENCES tb_international_strings(id) deferrable;

CREATE SEQUENCE metamac_statistical_resources_bd.seq_purposes
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), 'Redes sociales', 'es', currval('seq_i18nstrs'), 1);
insert into TB_PURPOSES(ID,IDENTIFIER,VERSION,DESCRIPTION_FK) values (nextval('SEQ_PURPOSES'),'SOCIAL_NETWORK',0, currval('seq_i18nstrs'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), 'Filtro de datos', 'es', currval('seq_i18nstrs'), 1);
insert into TB_PURPOSES(ID,IDENTIFIER,VERSION,DESCRIPTION_FK) values (nextval('SEQ_PURPOSES'),'DATA_FILTER',0, currval('seq_i18nstrs'));
commit;