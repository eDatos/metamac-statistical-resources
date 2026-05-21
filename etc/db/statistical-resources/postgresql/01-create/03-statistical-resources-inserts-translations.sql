-- YEAR
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy}', 'en', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy}', 'es', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy}', 'pt', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy}', 'ca', currval('seq_i18nstrs'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('seq_translations'), 'TIME_SDMX.YEAR', currval('seq_i18nstrs'));

-- MONTH
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{MM}/{yyyy}', 'en', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{MM}/{yyyy}', 'es', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{MM}/{yyyy}', 'pt', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{MM}/{yyyy}', 'ca', currval('seq_i18nstrs'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('seq_translations'), 'TIME_SDMX.MONTH', currval('seq_i18nstrs'));

-- DATE
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{dd}/{MM}/{yyyy}', 'en', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{dd}/{MM}/{yyyy}', 'es', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{dd}/{MM}/{yyyy}', 'pt', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{dd}/{MM}/{yyyy}', 'ca', currval('seq_i18nstrs'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('seq_translations'), 'TIME_SDMX.DATE', currval('seq_i18nstrs'));

-- DATE RANGE
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{dd}/{MM}/{yyyy} to {dd_END}/{MM_END}/{yyyy_END}', 'en', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{dd}/{MM}/{yyyy} a {dd_END}/{MM_END}/{yyyy_END}', 'es', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{dd}/{MM}/{yyyy} a {dd_END}/{MM_END}/{yyyy_END}', 'pt', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{dd}/{MM}/{yyyy} a {dd_END}/{MM_END}/{yyyy_END}', 'ca', currval('seq_i18nstrs'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('seq_translations'), 'TIME_SDMX.DATE_RANGE', currval('seq_i18nstrs'));

-- DATETIME
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{dd}/{MM}/{yyyy} - {hh}:{mm}:{ss}', 'en', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{dd}/{MM}/{yyyy} - {hh}:{mm}:{ss}', 'es', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{dd}/{MM}/{yyyy} - {hh}:{mm}:{ss}', 'pt', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{dd}/{MM}/{yyyy} - {hh}:{mm}:{ss}', 'ca', currval('seq_i18nstrs'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('seq_translations'), 'TIME_SDMX.DATETIME', currval('seq_i18nstrs'));

-- DATETIME RANGE
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{dd}/{MM}/{yyyy} - {hh}:{mm}:{ss} to {dd_END}/{MM_END}/{yyyy_END} - {hh_END}:{mm_END}:{ss_END}', 'en', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{dd}/{MM}/{yyyy} - {hh}:{mm}:{ss} a {dd_END}/{MM_END}/{yyyy_END} - {hh_END}:{mm_END}:{ss_END}', 'es', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{dd}/{MM}/{yyyy} - {hh}:{mm}:{ss} a {dd_END}/{MM_END}/{yyyy_END} - {hh_END}:{mm_END}:{ss_END}', 'pt', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{dd}/{MM}/{yyyy} - {hh}:{mm}:{ss} a {dd_END}/{MM_END}/{yyyy_END} - {hh_END}:{mm_END}:{ss_END}', 'ca', currval('seq_i18nstrs'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('seq_translations'), 'TIME_SDMX.DATETIME_RANGE', currval('seq_i18nstrs'));

-- SEMESTER
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} First semester', 'en', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Primer semestre', 'es', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Primeiro semestre', 'pt', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Primer semestre', 'ca', currval('seq_i18nstrs'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('seq_translations'), 'TIME_SDMX.SEMESTER.S1', currval('seq_i18nstrs'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Second semester', 'en', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Segundo semestre', 'es', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Segundo semestre', 'pt', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Segon semestre', 'ca', currval('seq_i18nstrs'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('seq_translations'), 'TIME_SDMX.SEMESTER.S2', currval('seq_i18nstrs'));

-- TRIMESTER
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} First four month period', 'en', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Primer cuatrimestre', 'es', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Primeiro quadrimestre', 'pt', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Primer quadrimestre', 'ca', currval('seq_i18nstrs'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('seq_translations'), 'TIME_SDMX.TRIMESTER.T1', currval('seq_i18nstrs'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Second four month period', 'en', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Segundo cuatrimestre', 'es', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Segundo quadrimestre', 'pt', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Segon quadrimestre', 'ca', currval('seq_i18nstrs'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('seq_translations'), 'TIME_SDMX.TRIMESTER.T2', currval('seq_i18nstrs'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Third four month period', 'en', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Tercer cuatrimestre', 'es', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Terceiro quadrimestre', 'pt', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Tercer quadrimestre', 'ca', currval('seq_i18nstrs'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('seq_translations'), 'TIME_SDMX.TRIMESTER.T3', currval('seq_i18nstrs'));

-- QUARTER
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} First quarter', 'en', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Primer trimestre', 'es', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Primeiro trimestre', 'pt', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Primer trimestre', 'ca', currval('seq_i18nstrs'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('seq_translations'), 'TIME_SDMX.QUARTER.Q1', currval('seq_i18nstrs'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Second quarter', 'en', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Segundo trimestre', 'es', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Segundo trimestre', 'pt', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Segon trimestre', 'ca', currval('seq_i18nstrs'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('seq_translations'), 'TIME_SDMX.QUARTER.Q2', currval('seq_i18nstrs'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Third quarter', 'en', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Tercer trimestre', 'es', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Terceiro trimestre', 'pt', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Tercer trimestre', 'ca', currval('seq_i18nstrs'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('seq_translations'), 'TIME_SDMX.QUARTER.Q3', currval('seq_i18nstrs'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Fourth quarter', 'en', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Cuarto trimestre', 'es', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Quarto trimestre', 'pt', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Quart trimestre', 'ca', currval('seq_i18nstrs'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('seq_translations'), 'TIME_SDMX.QUARTER.Q4', currval('seq_i18nstrs'));

-- WEEK
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Week {ww}', 'en', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Semana {ww}', 'es', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Semana {ww}', 'pt', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), '{yyyy} Setmana {ww}', 'ca', currval('seq_i18nstrs'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('seq_translations'), 'TIME_SDMX.WEEK', currval('seq_i18nstrs'));

-- PURPOSES
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), 'Social networks', 'en', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), 'Redes sociales', 'es', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), 'Xarxes socials', 'ca', currval('seq_i18nstrs'), 1);
INSERT INTO TB_PURPOSES (ID, IDENTIFIER, VERSION, DESCRIPTION_FK) values (nextval('SEQ_PURPOSES'), 'SOCIAL_NETWORK', 0, currval('seq_i18nstrs'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), 'Data filter', 'en', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), 'Filtro de datos', 'es', currval('seq_i18nstrs'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('seq_l10nstrs'), 'Filtre de dades', 'ca', currval('seq_i18nstrs'), 1);
INSERT INTO TB_PURPOSES (ID, IDENTIFIER, VERSION, DESCRIPTION_FK) values (nextval('SEQ_PURPOSES'), 'DATA_FILTER', 0, currval('seq_i18nstrs'));
