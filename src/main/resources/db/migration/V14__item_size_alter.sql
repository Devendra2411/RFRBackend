ALTER TABLE rfr_pgs_fspevent ALTER COLUMN e_unit_startup_date DROP NOT NULL;
ALTER TABLE rfr_pgs_fspevent ALTER COLUMN block_id DROP NOT NULL;
ALTER TABLE rfr_pgs_fspevent
    ALTER COLUMN block_id TYPE VARCHAR(255),
    ALTER COLUMN train_id TYPE VARCHAR(255);
ALTER TABLE pgs_action_items ALTER COLUMN item_title TYPE varchar(2500);
ALTER TABLE rfr_action_items ALTER COLUMN item_title TYPE varchar(2500);
ALTER TABLE rfr_workflow ALTER COLUMN train_id TYPE varchar(255);
ALTER TABLE rfr_workflow ALTER COLUMN calenco_doc_id DROP NOT NULL;