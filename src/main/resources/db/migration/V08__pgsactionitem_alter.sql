ALTER TABLE pgs_action_items DROP COLUMN due_date, DROP COLUMN equip_serial_number, DROP COLUMN level_value, DROP COLUMN outage_id;

ALTER TABLE pgs_action_items ADD attribute_type VARCHAR(255) NOT NULL, ADD attribute_value VARCHAR(255) NOT NULL, ADD cal_date TIMESTAMP, ADD ref_date_id INT4, ADD span_from_ref INT4 NOT NULL;