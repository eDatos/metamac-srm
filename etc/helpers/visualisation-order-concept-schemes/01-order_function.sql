-- --------------------------------------------------------------------------------------------------
-- EDATOS-4957 - [srm] Añadir órdenes de visualización a los esquemas de conceptos.

/*
-- Para un conjunto específico de esquemas
SELECT update_order_by_scheme_versions(ARRAY[61002, 61003, 61004]);

-- Para todos los esquemas existentes automáticamente
SELECT update_order_by_scheme_versions(ARRAY(
  SELECT DISTINCT item_scheme_version_fk FROM tb_concepts
));

  🧪 Cómo usarla
sql
Copiar
Editar
-- Para un conjunto específico de esquemas
SELECT update_order2_by_scheme_versions(ARRAY[61002, 61003, 61004]);

-- Para todos los esquemas existentes automáticamente
SELECT update_order2_by_scheme_versions(ARRAY(
  SELECT DISTINCT item_scheme_version_fk FROM tb_concepts
));
*/
-- --------------------------------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION update_order_by_scheme_versions(schemes bigint[])
RETURNS void AS $$
BEGIN
  WITH RECURSIVE concept_tree AS (
    -- Raíces del árbol por cada item_scheme_version_fk
    SELECT
      c.id,
      c.parent_fk,
      c.item_scheme_version_fk,
      n.code AS urn,
      LPAD(n.code, 10, '0') AS path,
      0 AS level
    FROM tb_concepts c
    JOIN tb_annotable_artefacts n ON n.id = c.nameable_artefact_fk
    WHERE c.item_scheme_version_fk = ANY(schemes)
      AND c.parent_fk IS NULL

    UNION ALL

    -- Recursivamente construir el árbol con path
    SELECT
      c.id,
      c.parent_fk,
      c.item_scheme_version_fk,
      n.code,
      ct.path || '-' || LPAD(ROW_NUMBER() OVER (
        PARTITION BY c.parent_fk
        ORDER BY n.code
      )::text, 5, '0') AS path,
      ct.level + 1
    FROM tb_concepts c
    JOIN tb_annotable_artefacts n ON n.id = c.nameable_artefact_fk
    JOIN concept_tree ct ON c.parent_fk = ct.id
    WHERE c.item_scheme_version_fk = ANY(schemes)
  ),

 -- Calcular el nuevo orden reiniciando por cada subnivel
  ordered_concepts AS (
  SELECT
    c.id,
    c.parent_fk,
    ROW_NUMBER() OVER (
      PARTITION BY c.parent_fk
      ORDER BY aa.urn COLLATE "es-x-icu"
    ) - 1 AS new_order
  FROM concept_tree c
  JOIN tb_m_concepts mc ON mc.tb_concepts = c.id
  JOIN tb_concepts tc ON tc.id = mc.tb_concepts
  JOIN tb_annotable_artefacts aa ON aa.id = tc.nameable_artefact_fk
  WHERE c.parent_fk IS NOT NULL
),

root_concepts AS (
  SELECT
    c.id,
    NULL::bigint AS parent_fk,
    ROW_NUMBER() OVER (
      ORDER BY aa.urn COLLATE "es-x-icu"
    ) - 1 AS new_order
  FROM concept_tree c
  JOIN tb_m_concepts mc ON mc.tb_concepts = c.id
  JOIN tb_concepts tc ON tc.id = mc.tb_concepts
  JOIN tb_annotable_artefacts aa ON aa.id = tc.nameable_artefact_fk
  WHERE c.parent_fk IS null
  )

  -- Aplicar actualización sobre la primera columna que será order1
 UPDATE tb_m_concepts mc
SET order1 = oc.new_order
FROM (
  SELECT * FROM ordered_concepts
  UNION ALL
  SELECT * FROM root_concepts
) oc
WHERE mc.tb_concepts = oc.id;

END;
$$ LANGUAGE plpgsql;
