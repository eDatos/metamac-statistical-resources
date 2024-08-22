-- --------------------------------------------------------------------------------------------------
-- EDATOS-XXX - Añadir nivel de colecciones en eTerritorios
-- 
-- Script que crea las tablas necesarias para añadir una caché de colecciones asociada a los datasets de la caché de territorios
---- Guardar los datos principales de las colecciones de EDATOS
---- Guardar los datos principales de las colecciones de JAXI

-- --------------------------------------------------------------------------------------------------


--TABLE tb_geo_cache_resource
CREATE TABLE tb_geo_cache_resource (
	id int8 NOT NULL,
	urn varchar(4000) NOT NULL,
	code varchar(255) NOT NULL,
	"type" varchar(255) NOT NULL,
	version int8,
	title_fk int8,
	operation_code varchar(255) NOT NULL,
	operation_urn varchar(4000) NOT NULL,
	htmlLink varchar(4000),
	is_external_source boolean NOT NULL,
	is_last_version boolean NOT NULL,
	is_activated boolean NOT NULL,

	update_date_tz varchar(50),
	update_date timestamp,
	created_date_tz varchar(50),
	created_date timestamp,
	created_by varchar(50),
	last_updated_tz varchar(50),
	last_updated timestamp,
	last_updated_by varchar(50),
	CONSTRAINT pk_tb_geo_cache_resource PRIMARY KEY (id)
	);

	ALTER TABLE tb_geo_cache_resource ADD CONSTRAINT fk_tb_geo_cache_resource_title_fk FOREIGN KEY (title_fk) REFERENCES tb_international_strings(id);
CREATE INDEX pk_tb_geo_cache_resource_urn ON tb_geo_cache_resource (urn);
CREATE INDEX pk_tb_geo_cache_resource_title_fk ON tb_geo_cache_resource (title_fk);	

CREATE sequence SEQ_GEO_CACHE_RESOURCE;

--TABLE tb_geo_cache_related_resource
CREATE TABLE tb_geo_cache_related_resource (
	id int8 NOT NULL,
	urn varchar(4000) NOT NULL,
	code varchar(255) NOT NULL,
	"type" varchar(255) NOT NULL,
	version int8,
	title_fk int8,
	operation_code varchar(255) NOT NULL,
	operation_urn varchar(4000) NOT NULL,
	htmlLink varchar(4000),
	is_external_source boolean NOT NULL,
	is_last_version boolean NOT NULL,
	is_activated boolean NOT NULL,

	update_date_tz varchar(50),
	update_date timestamp,
	created_date_tz varchar(50),
	created_date timestamp,
	created_by varchar(50),
	last_updated_tz varchar(50),
	last_updated timestamp,
	last_updated_by varchar(50),
	CONSTRAINT pk_tb_geo_cache_related_resource PRIMARY KEY (id)
	);

	ALTER TABLE tb_geo_cache_related_resource ADD CONSTRAINT fk_tb_geo_cache_related_resource_title_fk FOREIGN KEY (title_fk) REFERENCES tb_international_strings(id);
CREATE INDEX pk_tb_geo_cache_related_resource_urn ON tb_geo_cache_related_resource (urn);
CREATE INDEX pk_tb_geo_cache_related_resource_title_fk ON tb_geo_cache_related_resource (title_fk);	

CREATE sequence SEQ_GEO_CACHE_RESOURCE_BY_RELATED_RESOURCE;

--TABLE tb_geo_cache_resource_by_related_resource
CREATE TABLE tb_geo_cache_resource_by_related_resource (
	id int8 NOT NULL,
	version int8,
	geo_cache_resource_fk int8 NOT NULL,
	geo_cache_related_resource_fk int8 NOT NULL,
	CONSTRAINT pk_tb_geo_cache_resource_by_related_resource PRIMARY KEY (id)
);

CREATE UNIQUE INDEX ix_tb_geo_cache_resource_by_related_resource ON tb_geo_cache_resource_by_related_resource (geo_cache_related_resource_fk, geo_cache_resource_fk);
CREATE INDEX ix_2_tb_geo_cache_resource_by_related_resource ON tb_geo_cache_resource_by_related_resource (geo_cache_resource_fk);
ALTER TABLE tb_geo_cache_resource_by_related_resource ADD CONSTRAINT fk_tb_geo_cache_resource_related_resource_geo_cache_res_fk FOREIGN KEY (geo_cache_resource_fk) REFERENCES tb_geo_cache_resource(id);
ALTER TABLE tb_geo_cache_resource_by_related_resource ADD CONSTRAINT fk_tb_geo_cache_resource_by_related_resource_tb_cache_col_fk FOREIGN KEY (geo_cache_related_resource_fk) REFERENCES tb_geo_cache_related_resource(id);

--TABLE tb_territories_by_geo_cache_resource
CREATE TABLE TB_TERRITORIES_BY_GEO_CACHE_RESOURCE (
	id int8 NOT NULL,
	version int8,
	variable_element_fk int8 NOT NULL,
	geo_cache_resource_fk int8 NOT NULL,
	CONSTRAINT pk_tb_territories_by_geo_cache_resource PRIMARY KEY (id)
);

CREATE UNIQUE INDEX ix_tb_territories_by_geo_cache_resource ON tb_territories_by_geo_cache_resource (variable_element_fk,geo_cache_resource_fk);
CREATE INDEX ix_2_tb_territories_by_geo_cache_resource ON tb_territories_by_geo_cache_resource (geo_cache_resource_fk);
ALTER TABLE tb_territories_by_geo_cache_resource ADD CONSTRAINT fk_tb_territories_by_geo_cache_resource_variable_element_fk FOREIGN KEY (variable_element_fk) REFERENCES tb_external_items(id);
ALTER TABLE tb_territories_by_geo_cache_resource ADD CONSTRAINT fk_tb_territories_by_geo_cache_resource_tb_cache_col_fk FOREIGN KEY (geo_cache_resource_fk) REFERENCES tb_geo_cache_resource(id);

CREATE sequence SEQ_TERRITORIES_BY_GEO_CACHE_RESOURCE;


commit;