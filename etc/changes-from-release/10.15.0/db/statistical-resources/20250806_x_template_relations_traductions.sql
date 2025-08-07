-- ---------------------------------------------------------------------------------------------------
-- EDATOS-5143 Correcciones visuales sobre el campo Finalidad para el envío de tuits 
-- ---------------------------------------------------------------------------------------------------

INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), 'Xarxes socials', 'ca', (select tis.id
  from tb_localised_strings tls 
  join tb_international_strings tis on tis.id = tls.international_string_fk 
  where tls."label" = 'Redes sociales'), 1);
  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), 'Filtre de dades', 'ca', (select tis.id
  from tb_localised_strings tls 
  join tb_international_strings tis on tis.id = tls.international_string_fk 
  where tls."label" = 'Filtro de datos'), 1);
