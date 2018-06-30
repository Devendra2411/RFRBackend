ALTER  TABLE rfr_workflow ADD  COLUMN site_name character varying(255) NOT NULL;
 
DROP INDEX rfr_workflow_unq_name;
   
 CREATE UNIQUE INDEX rfr_workflow_unq_name ON rfr_workflow (LOWER(equip_serial_number),outage_id);
 