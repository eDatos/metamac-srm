-- Verify that statistic-sdmx-srm scripts have been executed prior to SRM scripts
DO $$
DECLARE
    missing_tables TEXT;
BEGIN
    SELECT string_agg(t.table_name, ', ')
    INTO missing_tables
    FROM (VALUES ('tb_annotable_artefacts'), ('tb_item_schemes_versions'), ('tb_sequences')) AS t(table_name)
    WHERE NOT EXISTS (
        SELECT 1 FROM information_schema.tables ist WHERE ist.table_name = t.table_name
    );

    IF missing_tables IS NOT NULL THEN
        RAISE EXCEPTION 'statistic-sdmx-srm scripts must be executed prior. Missing tables: %', missing_tables;
    END IF;
END
$$;
