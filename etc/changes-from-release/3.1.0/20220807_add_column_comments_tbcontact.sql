-- --------------------------------------------------------------------------------------------------
-- EDATOS-3710 -ISTAC. Añadir un campo de comentarios a los contactos de las organizaciones
-- --------------------------------------------------------------------------------------------------

ALTER TABLE TB_CONTACTS
    ADD COLUMN COMMENT_FK BIGINT;

ALTER TABLE TB_CONTACTS ADD CONSTRAINT FK_TB_CONTACTS_COMMENT_FK FOREIGN KEY
    (COMMENT_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID);

COMMIT;

