CREATE OR REPLACE FUNCTION sdmx_to_date(txt text)
RETURNS date
LANGUAGE plpgsql
IMMUTABLE
RETURNS NULL ON NULL INPUT          -- evita comprobar valores NULL
AS $$
DECLARE
    y int;
    m int := 1;
    d int := 1;
BEGIN
    /* 0)  YYYY  (ANUAL)  --------------------------------------------- */
    IF txt ~ '^\d{4}$' THEN
        y := txt::int;
        RETURN make_date(y, m, d);          -- 1-ene del año
    END IF;

    /* 1)  YYYY-MM-DD  ------------------------------------------------- */
    IF txt ~ '^\d{4}-\d{2}-\d{2}$' THEN
        RETURN to_date(txt,'YYYY-MM-DD');
    END IF;

    /* 2)  YYYY-M##  (mes)  ------------------------------------------- */
    IF txt ~ '^\d{4}-M\d{2}$' THEN
        y := substring(txt,1,4)::int;
        m := substring(txt,7,2)::int;       -- p. ej. M02 → 02
        RETURN make_date(y,m,d);
    END IF;

    /* 3)  YYYY-Q#  (trimestre)  -------------------------------------- */
    IF txt ~ '^\d{4}-Q[1-4]$' THEN
        y := substring(txt,1,4)::int;
        m := (substring(txt,7,1)::int - 1)*3 + 1;   -- Q3 → julio
        RETURN make_date(y,m,d);
    END IF;

    /* 4)  YYYY-W##  (semana ISO)  ------------------------------------ */
    IF txt ~ '^\d{4}-W\d{2}$' THEN
        RETURN to_date(txt||'-1','IYYY-"W"IW-ID');  -- lunes de la semana
    END IF;

    /* 5)  Sin patrón reconocido  ------------------------------------- */
    RAISE EXCEPTION 'Formato SDMX no soportado: %', txt;
END;
$$;
