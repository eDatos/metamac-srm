-- --------------------------------------------------------------------------------------------------
-- EDATOS-4804 - Mover las extensiones actualmente instaladas en los esquemas de bbdd al esquema de
-- extensiones
-- DEBE EJECUTARSE SIEMPRE después de los cambios introducidos en EDATOS-4658
-----------------------------------------------

-- DESARROLLO
-- BBDD: metamac_structural_resources_bd
-- extensiones: pg_trgm y uuid-ossp

-- psql -U postgres -d metamac_structural_resources_bd
ALTER EXTENSION "pg_trgm" SET SCHEMA extensions;
ALTER EXTENSION "uuid-ossp" SET SCHEMA extensions;

-----------------------------------------------
-- DEMO
-- BBDD: istac_structural_resources_bd
-- extensiones: pg_trgm y uuid-ossp

-- psql -U postgres -d istac_structural_resources_bd
ALTER EXTENSION "pg_trgm" SET SCHEMA extensions;
ALTER EXTENSION "uuid-ossp" SET SCHEMA extensions;

-----------------------------------------------
-- PRE IBESTAT
-- BBDD: metamac_structural_resources_bd
-- extensiones: pg_trgm y uuid-ossp

-- psql -U postgres -d metamac_structural_resources_bd
ALTER EXTENSION "pg_trgm" SET SCHEMA extensions;
ALTER EXTENSION "uuid-ossp" SET SCHEMA extensions;

-----------------------------------------------
-- PRO IBESTAT
-- BBDD: metamac_structural_resources_bd
-- extensiones: pg_trgm y uuid-ossp

-- psql -U postgres -d metamac_structural_resources_bd
ALTER EXTENSION "pg_trgm" SET SCHEMA extensions;
ALTER EXTENSION "uuid-ossp" SET SCHEMA extensions;

-----------------------------------------------
-- PRE IESTADIS
-- BBDD: metamac_structural_resources_bd
-- extensiones: pg_trgm y uuid-ossp

-- psql -U postgres -d metamac_structural_resources_bd
ALTER EXTENSION "pg_trgm" SET SCHEMA extensions;
ALTER EXTENSION "uuid-ossp" SET SCHEMA extensions;

-----------------------------------------------
-- PRO IESTADIS
-- BBDD: metamac_structural_resources_bd
-- extensiones: pg_trgm y uuid-ossp

-- psql -U postgres -d metamac_structural_resources_bd
ALTER EXTENSION "pg_trgm" SET SCHEMA extensions;
ALTER EXTENSION "uuid-ossp" SET SCHEMA extensions;

-----------------------------------------------
-- PRE ISTAC
-- BBDD: metamac_structural_resources_bd
-- extensiones: pg_trgm y uuid-ossp

-- psql -h localhost -p 7036 -U postgres -d istac_structural_resources_bd
ALTER EXTENSION "pg_trgm" SET SCHEMA extensions;
ALTER EXTENSION "uuid-ossp" SET SCHEMA extensions;

-----------------------------------------------
-- PRO ISTAC
-- BBDD: metamac_structural_resources_bd
-- extensiones: pg_trgm y uuid-ossp

-- psql -h localhost -p 7042 -U postgres -d istac_structural_resources_bd
ALTER EXTENSION "pg_trgm" SET SCHEMA extensions;
ALTER EXTENSION "uuid-ossp" SET SCHEMA extensions;