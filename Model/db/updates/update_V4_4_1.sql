ALTER TABLE library_registration_request ADD COLUMN adult_content_authorization bool;

ALTER TABLE school_canteen_registration_request ADD COLUMN which_food_allergy varchar(255);

create table global_school_registration_request (
    id int8 not null,
    acceptation_reglement_interieur bool,
    est_derogation bool,
    est_periscolaire bool,
    est_restauration bool,
    id_ecole_derog varchar(255),
    id_ecole_secteur varchar(255),
    informations_complementaires_derogation varchar(1024),
    label_ecole_derog varchar(255),
    label_ecole_secteur varchar(255),
    primary key (id)
);

create table global_school_registration_request_motifs_derogation_ecole (
    global_school_registration_request_id int8 not null,
    motifs_derogation_ecole_id int8 not null,
    motifs_derogation_ecole_index int4 not null,
    primary key (global_school_registration_request_id, motifs_derogation_ecole_index)
);

create table global_school_registration_request_regime_alimentaire (
    global_school_registration_request_id int8 not null,
    regime_alimentaire_id int8 not null,
    regime_alimentaire_index int4 not null,
    primary key (global_school_registration_request_id, regime_alimentaire_index)
);

alter table global_school_registration_request_motifs_derogation_ecole
    add constraint FKCC5E50B85DE12C2
    foreign key (global_school_registration_request_id)
    references global_school_registration_request;

alter table global_school_registration_request_motifs_derogation_ecole
    add constraint FKCC5E50B315EEA61
    foreign key (motifs_derogation_ecole_id)
    references local_referential_data;

alter table global_school_registration_request_regime_alimentaire
    add constraint FK261E5D0CA7322BAE
    foreign key (regime_alimentaire_id)
    references local_referential_data;

alter table global_school_registration_request_regime_alimentaire
    add constraint FK261E5D0C85DE12C2
    foreign key (global_school_registration_request_id)
    references global_school_registration_request;

create table school_transport_registration_request (
    id int8 not null,
    acceptation_reglement_interieur bool,
    autorisation varchar(255),
    est_maternelle_elementaire_autorisations bool,
    frere_ou_soeur_classe varchar(255),
    frere_ou_soeur_ecole varchar(255),
    frere_ou_soeur_nom varchar(38),
    frere_ou_soeur_prenom varchar(38),
    id_arret varchar(255),
    id_ligne varchar(255),
    label_arret varchar(255),
    label_ligne varchar(255),
    primary key (id)
);

create table tiers_informations (
    id int8 not null,
    tiers_nom varchar(38),
    tiers_prenom varchar(38),
    tiers_telephone varchar(10),
    school_transport_registration_request_id int8,
    tiers_autorises_index int4,
    primary key (id)
);

alter table tiers_informations
    add constraint FK58C18B7589395924
    foreign key (school_transport_registration_request_id)
    references school_transport_registration_request;

create table renewal_perischool_activities_request (
    id int8 not null,
    acceptation_reglement_interieur bool,
    est_periscolaire bool,
    est_restauration bool,
    primary key (id)
);

create table renewal_perischool_activities_request_regime_alimentaire_renouvellement (
    renewal_perischool_activities_request_id int8 not null,
    regime_alimentaire_renouvellement_id int8 not null,
    regime_alimentaire_renouvellement_index int4 not null,
    primary key (renewal_perischool_activities_request_id, regime_alimentaire_renouvellement_index)
);

alter table renewal_perischool_activities_request_regime_alimentaire_renouvellement
    add constraint FK8AFF2E83E35CAE2
    foreign key (renewal_perischool_activities_request_id)
    references renewal_perischool_activities_request;

alter table renewal_perischool_activities_request_regime_alimentaire_renouvellement
    add constraint FK8AFF2E837F587126
    foreign key (regime_alimentaire_renouvellement_id)
    references local_referential_data;

create table leisure_center_registration_request (
    id int8 not null,
    acceptation_reglement_interieur bool,
    est_derogation bool,
    est_transport bool,
    id_arret varchar(255),
    id_centre_loisirs varchar(255),
    id_ligne varchar(255),
    label_arret varchar(255),
    label_centre_loisirs varchar(255),
    label_ligne varchar(255),
    primary key (id)
);

create table leisure_center_registration_request_motifs_derogation_centre_loisirs (
    leisure_center_registration_request_id int8 not null,
    motifs_derogation_centre_loisirs_id int8 not null,
    motifs_derogation_centre_loisirs_index int4 not null,
    primary key (leisure_center_registration_request_id, motifs_derogation_centre_loisirs_index)
);

alter table leisure_center_registration_request_motifs_derogation_centre_loisirs
    add constraint FK6899CCB79D930190
    foreign key (leisure_center_registration_request_id)
    references leisure_center_registration_request;

alter table leisure_center_registration_request_motifs_derogation_centre_loisirs
    add constraint FK6899CCB75852F322
    foreign key (motifs_derogation_centre_loisirs_id)
    references local_referential_data;

create table holiday_camp_registration_request (
    id int8 not null,
    acceptation_reglement_interieur bool,
    id_centre_sejours varchar(255),
    label_centre_sejours varchar(255),
    primary key (id)
);

create table pessac_animation_request (
    id int8 not null,
    acceptation_reglement_interieur bool,
    email_sujet varchar(255),
    telephone_sujet varchar(10),
    primary key (id)
);
