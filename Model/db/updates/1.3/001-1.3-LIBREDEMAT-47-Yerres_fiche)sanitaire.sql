UPDATE child_information_sheet SET autorisation_droit_image=null where autorisation_droit_image=false;
UPDATE child_information_sheet SET autorisation_hospitalisation=null where autorisation_hospitalisation=false;
UPDATE child_information_sheet SET autorisation_maquillage=null where autorisation_maquillage=false;
UPDATE child_information_sheet SET autorisation_rentrer_seul=null where autorisation_rentrer_seul=false;
UPDATE child_information_sheet SET autorisation_transporter_transport_commun=null where autorisation_transporter_transport_commun=false;
UPDATE child_information_sheet SET autorisation_transporter_vehicule_municipal=null where autorisation_transporter_vehicule_municipal=false;

ALTER TABLE adult ADD COLUMN sms_permission boolean default false;
