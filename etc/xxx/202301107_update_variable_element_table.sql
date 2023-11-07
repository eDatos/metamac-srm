-- --------------------------------------------------------------------------------------------------
-- EDATOS-3827 - Integración con códigos geográficos de e-Semántica
-- 
-- Añadir propiedad stream_message_status a tabla de elementos de variable para el estado de envíos a kafka.

-- --------------------------------------------------------------------------------------------------

-- Añade nueva columna a la tabla de elementos de variable que determina el estado de transmisión a través de Kafka

ALTER TABLE TB_M_VARIABLE_ELEMENTS
ADD COLUMN STREAM_MESSAGE_STATUS VARCHAR(255);

COMMIT;