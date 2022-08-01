-- ---------------------------------------------------------------------------------------------------
-- EDATOS-3696 - [statistical-resources] Cargar traducciones al catalán
-- ---------------------------------------------------------------------------------------------------

INSERT
INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION)
VALUES (nextval('seq_l10nstrs'), '{yyyy}', 'ca', (SELECT TITLE_FK
                                                  FROM TB_TRANSLATIONS
                                                  WHERE CODE = 'TIME_SDMX.YEAR'), 1);

INSERT
INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION)
VALUES (nextval('seq_l10nstrs'), '{MM}/{yyyy}', 'ca', (SELECT TITLE_FK
                                                  FROM TB_TRANSLATIONS
                                                  WHERE CODE = 'TIME_SDMX.MONTH'), 1);

INSERT
INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION)
VALUES (nextval('seq_l10nstrs'), '{dd}/{MM}/{yyyy}', 'ca', (SELECT TITLE_FK
                                                  FROM TB_TRANSLATIONS
                                                  WHERE CODE = 'TIME_SDMX.DATE'), 1);

INSERT
INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION)
VALUES (nextval('seq_l10nstrs'), '{dd}/{MM}/{yyyy} a {dd_END}/{MM_END}/{yyyy_END}', 'ca', (SELECT TITLE_FK
                                                  FROM TB_TRANSLATIONS
                                                  WHERE CODE = 'TIME_SDMX.DATE_RANGE'), 1);

INSERT
INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION)
VALUES (nextval('seq_l10nstrs'), '{dd}/{MM}/{yyyy} - {hh}:{mm}:{ss}', 'ca', (SELECT TITLE_FK
                                                  FROM TB_TRANSLATIONS
                                                  WHERE CODE = 'TIME_SDMX.DATETIME'), 1);

INSERT
INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION)
VALUES (nextval('seq_l10nstrs'), '{dd}/{MM}/{yyyy} - {hh}:{mm}:{ss} a {dd_END}/{MM_END}/{yyyy_END} - {hh_END}:{mm_END}:{ss_END}', 'ca', (SELECT TITLE_FK
                                                  FROM TB_TRANSLATIONS
                                                  WHERE CODE = 'TIME_SDMX.DATETIME_RANGE'), 1);

INSERT
INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION)
VALUES (nextval('seq_l10nstrs'), '{yyyy} Primer semestre', 'ca', (SELECT TITLE_FK
                                                  FROM TB_TRANSLATIONS
                                                  WHERE CODE = 'TIME_SDMX.SEMESTER.S1'), 1);

INSERT
INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION)
VALUES (nextval('seq_l10nstrs'), '{yyyy} Segon semestre', 'ca', (SELECT TITLE_FK
                                                  FROM TB_TRANSLATIONS
                                                  WHERE CODE = 'TIME_SDMX.SEMESTER.S2'), 1);

INSERT
INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION)
VALUES (nextval('seq_l10nstrs'), '{yyyy} Primer quadrimestre', 'ca', (SELECT TITLE_FK
                                                  FROM TB_TRANSLATIONS
                                                  WHERE CODE = 'TIME_SDMX.TRIMESTER.T1'), 1);

INSERT
INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION)
VALUES (nextval('seq_l10nstrs'), '{yyyy} Segon quadrimestre', 'ca', (SELECT TITLE_FK
                                                  FROM TB_TRANSLATIONS
                                                  WHERE CODE = 'TIME_SDMX.TRIMESTER.T2'), 1);

INSERT
INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION)
VALUES (nextval('seq_l10nstrs'), '{yyyy} Tercer quadrimestre', 'ca', (SELECT TITLE_FK
                                                  FROM TB_TRANSLATIONS
                                                  WHERE CODE = 'TIME_SDMX.TRIMESTER.T3'), 1);

INSERT
INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION)
VALUES (nextval('seq_l10nstrs'), '{yyyy} Primer trimestre', 'ca', (SELECT TITLE_FK
                                                  FROM TB_TRANSLATIONS
                                                  WHERE CODE = 'TIME_SDMX.QUARTER.Q1'), 1);

INSERT
INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION)
VALUES (nextval('seq_l10nstrs'), '{yyyy} Segon trimestre', 'ca', (SELECT TITLE_FK
                                                  FROM TB_TRANSLATIONS
                                                  WHERE CODE = 'TIME_SDMX.QUARTER.Q2'), 1);

INSERT
INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION)
VALUES (nextval('seq_l10nstrs'), '{yyyy} Tercer trimestre', 'ca', (SELECT TITLE_FK
                                                  FROM TB_TRANSLATIONS
                                                  WHERE CODE = 'TIME_SDMX.QUARTER.Q3'), 1);

INSERT
INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION)
VALUES (nextval('seq_l10nstrs'), '{yyyy} Quart trimestre', 'ca', (SELECT TITLE_FK
                                                  FROM TB_TRANSLATIONS
                                                  WHERE CODE = 'TIME_SDMX.QUARTER.Q4'), 1);

INSERT
INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION)
VALUES (nextval('seq_l10nstrs'), '{yyyy} Setmana {ww}', 'ca', (SELECT TITLE_FK
                                                  FROM TB_TRANSLATIONS
                                                  WHERE CODE = 'TIME_SDMX.WEEK'), 1);

COMMIT;