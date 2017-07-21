DROP DATABASE IF EXISTS aaa;

CREATE DATABASE aaa;
USE aaa;

# -----------------------------------------------------------------------------
#       TABLE : marques
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS marques(
  id INTEGER NOT NULL AUTO_INCREMENT,
  nom VARCHAR(50) NULL,
  maj TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (ID) 
 );

# -----------------------------------------------------------------------------
#       TABLE : modeles
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS modeles(
  id INTEGER NOT NULL AUTO_INCREMENT,
  nom VARCHAR(50) NULL  ,
  annee BIGINT(4) NULL,
  maj TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  marque_id INTEGER,
  PRIMARY KEY (id),
  FOREIGN KEY(marque_id) REFERENCES marques(id) ON DELETE CASCADE

 );

# -----------------------------------------------------------------------------
#       TABLES : vehicules, h_vehicules
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS vehicules(
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  modele_id INTEGER NOT NULL,
  assure_id INTEGER,
  vin CHAR(17) NOT NULL UNIQUE,
  immatriculation CHAR(7),
  carte_grise VARCHAR(128),
  date_achat DATE,
  valeur FLOAT,
  maj TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY(modele_id) REFERENCES modeles(id) ON DELETE CASCADE,
  FOREIGN KEY(assure_id) REFERENCES assures(id) ON DELETE SET NULL
 );

CREATE TABLE IF NOT EXISTS h_vehicules(
  id INTEGER NOT NULL AUTO_INCREMENT,
  vehicule_id INTEGER NOT NULL,
  assure_id INTEGER,
  contrat_id INTEGER,
  date_h TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  immatriculation CHAR(7),
  date_achat DATE,
  valeur FLOAT,
  PRIMARY KEY (id),
  FOREIGN KEY(vehicule_id) REFERENCES vehicules(id) ON DELETE CASCADE,
  FOREIGN KEY(assure_id) REFERENCES assures(id) ON DELETE SET NULL,
  FOREIGN KEY(contrat_id) REFERENCES contrats(id) ON DELETE SET NULL
 );

# -----------------------------------------------------------------------------
#       TABLE : compagnie
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS compagnies(
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  nom VARCHAR(64)
);

# -----------------------------------------------------------------------------
#       TABLES : personnes, assures, utilisateurs
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS personnes(
  id INTEGER PRIMARY KEY  AUTO_INCREMENT,
  nom VARCHAR(25) NULL,
  prenom VARCHAR(25) NULL,
  adresse VARCHAR(25) NULL,
  ville VARCHAR(25) NULL,
  code_postal BIGINT(5) NULL,
  tel VARCHAR(13) NULL,
  maj TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  type TINYINT
 );

CREATE TABLE IF NOT EXISTS assures(
  id INTEGER PRIMARY KEY,
  bonus_malus REAL(5,2) DEFAULT 1.0,
  maj TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (ID) REFERENCES PERSONNES(ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tiers(
  id INTEGER PRIMARY KEY,
  compagnie_id INTEGER NOT NULL,
  bonus_malus REAL(5,2) DEFAULT 1.0,
  maj TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (id) REFERENCES assures(id) ON DELETE CASCADE,
  FOREIGN KEY (compagnie_id) REFERENCES compagnies(id) ON DELETE CASCADE
 );

CREATE TABLE IF NOT EXISTS experts(
  id INTEGER PRIMARY KEY,
  siret CHAR(12) UNIQUE,
  maj TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (id) REFERENCES personnes(id) ON DELETE CASCADE
);

 CREATE TABLE IF NOT EXISTS administrateurs(
   id INTEGER PRIMARY KEY,
   role VARCHAR(128) NULL,
   maj TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   FOREIGN KEY(id) REFERENCES personnes(id) ON DELETE CASCADE
 );

 CREATE TABLE IF NOT EXISTS clients(
  id INTEGER PRIMARY KEY,
  admin_id INTEGER,
  carte_identite VARCHAR(100),
  sepa VARCHAR(100),
  statut TINYINT,
  maj TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY(ID) REFERENCES assures(id) ON DELETE CASCADE,
  FOREIGN KEY(admin_id) REFERENCES administrateurs(id) ON DELETE SET NULL
);

# -----------------------------------------------------------------------------
#       TABLE : COMPTES_UTILISATEUR
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS comptes_utilisateur(
  id INTEGER PRIMARY KEY,
  email VARCHAR(50) NOT NULL,
  hash CHAR(40) NULL,
  token char(8) NULL,
  maj TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY(id) REFERENCES personnes(ID) ON DELETE CASCADE
);

# -----------------------------------------------------------------------------
#       TABLE : tokens
# -----------------------------------------------------------------------------

CREATE TABLE tokens (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  compte_utilisateur_id INTEGER UNIQUE,
  value VARCHAR(64) DEFAULT NULL,
  old_value VARCHAR(64) DEFAULT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  maj TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  api_server VARCHAR(15) DEFAULT NULL,
  FOREIGN KEY(compte_utilisateur_id) REFERENCES comptes_utilisateur(id) ON DELETE SET NULL
);

# -----------------------------------------------------------------------------
#       TABLE : indemnisations
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS indemnisations(
   id INTEGER AUTO_INCREMENT PRIMARY KEY,
   assure_id INTEGER NOT NULL,
   montant FLOAT(10,2) NULL,
   franchise FLOAT(10,2) NULL,
   maj TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   FOREIGN KEY(assure_id) REFERENCES assures(id) ON DELETE CASCADE
);

# -----------------------------------------------------------------------------
#       TABLE : garagistes
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS garagistes(
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  nom VARCHAR(128) NULL,
  agree BOOLEAN DEFAULT 0,
  maj TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
 );

# -----------------------------------------------------------------------------
#       TABLE : types_sinistres_sans_tiers
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS types_sinistres_sans_tiers(
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  code ENUM('INC','VOL','BRI') UNIQUE,
  libelle VARCHAR(50),
  maj TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

# -----------------------------------------------------------------------------
#       TABLES : sinistres, accidents, sinistres_sans_tiers
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS sinistres(
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  vehicule_id INTEGER NOT NULL,
  contrat_id INTEGER,
  expert_id INTEGER,
  commentaire VARCHAR(100) NULL,
  date_s DATE NULL,
  heure TIME NULL,
  constat VARCHAR(100) NULL,
  statut TINYINT,
  cloture BOOLEAN,
  maj TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  type BOOLEAN,
  FOREIGN KEY(vehicule_id) REFERENCES vehicules(id) ON DELETE CASCADE,
  FOREIGN KEY(contrat_id) REFERENCES contrats(id) ON DELETE SET NULL,
  FOREIGN KEY(expert_id) REFERENCES experts(id) ON DELETE SET NULL
);


CREATE TABLE IF NOT EXISTS accidents(
  id INTEGER PRIMARY KEY,
  pertes_humaines BOOLEAN NULL,
  taux_resp FLOAT,
  FOREIGN KEY (id) REFERENCES sinistres(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS sinistres_sans_tiers(
  id INTEGER PRIMARY KEY,
  type_id INTEGER NOT NULL,
  maj TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY(id) REFERENCES sinistres(id) ON DELETE CASCADE,
  FOREIGN KEY(type_id) REFERENCES types_sinistres_sans_tiers(id) ON DELETE CASCADE
);

# -----------------------------------------------------------------------------
#       TABLE : types_garantie
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS types_garantie(
  id INTEGER AUTO_INCREMENT PRIMARY KEY,
  code enum('tie','inc','vol','tou') UNIQUE,
  libelle VARCHAR(100) NULL,
  montant_defaut FLOAT(13,2) NULL,
  franchise_min FLOAT(13,2) NULL,
  franchise_max FLOAT(13,2) NULL,
  maj TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

# -----------------------------------------------------------------------------
#       TABLES : DOMMAGES, DETERIORATION, DESTRUCTIONS
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS dommages(
  id INTEGER PRIMARY KEY,
  description VARCHAR(100) NULL,
  montant FLOAT(12,2) NULL,
  maj TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  type BOOLEAN,
  FOREIGN KEY(id) REFERENCES sinistres(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS deteriorations(
  id INTEGER PRIMARY KEY,
  description VARCHAR(100),
  montant FLOAT,
  maj TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  garagiste_id INTEGER,
  taux FLOAT,
  FOREIGN KEY(id) REFERENCES sinistres(id) ON DELETE CASCADE,
  FOREIGN KEY(garagiste_id) REFERENCES garagistes(id) ON DELETE SET NULL
);


CREATE TABLE IF NOT EXISTS destructions (
  id INTEGER NOT NULL,
  description VARCHAR(100),
  montant FLOAT,
  maj TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  est_total TINYINT(1),
  PRIMARY KEY(id),
  FOREIGN KEY(id) REFERENCES sinistres(id) ON DELETE CASCADE
);

# -----------------------------------------------------------------------------
#       TABLES : contrats
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS contrats(
   id INTEGER PRIMARY KEY AUTO_INCREMENT,
   client_id INTEGER NOT NULL,
   vehicule_id INTEGER NOT NULL,
   type_garantie_id INTEGER NOT NULL,
   montant REAL(5, 2) NULL,
   date_souscription DATE NULL,
   etat BIGINT(4) NULL,
   actif BOOLEAN NOT NULL,
   contrat VARCHAR(100) NULL,
   statut TINYINT,
   maj TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   FOREIGN KEY(client_id) REFERENCES clients(id) ON DELETE CASCADE,
   FOREIGN KEY(vehicule_id) REFERENCES vehicules(id) ON DELETE CASCADE,
   FOREIGN KEY(type_garantie_id) REFERENCES types_garantie(id) ON DELETE CASCADE
);

# -----------------------------------------------------------------------------
#       TABLE : types_sinistres_types_garantie, sinistres_contrats_experts, accidents_accidents, comptes_utilisateur_tokens
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS types_sinistres_types_garantie(
  type_sinistres_id INTEGER NOT NULL,
  type_garantie_id INTEGER NOT NULL,
  PRIMARY KEY (type_sinistres_id, type_garantie_id),
  FOREIGN KEY(type_sinistres_id) REFERENCES types_sinistres_sans_tiers(id) ON DELETE CASCADE,
  FOREIGN KEY(type_garantie_id) REFERENCES types_garantie(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS sinistres_contrats_experts(
  sinistre_id INTEGER PRIMARY KEY,
  contrat_id INTEGER NOT NULL,
  expert_id INTEGER NULL,
  FOREIGN KEY(sinistre_id) REFERENCES sinistres(id) ON DELETE CASCADE,
  FOREIGN KEY(contrat_id) REFERENCES contrats(id) ON DELETE CASCADE,
  FOREIGN KEY(expert_id) REFERENCES experts(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS accidents_accidents(
	id_0 INTEGER NOT NULL UNIQUE,
	id_1 INTEGER NOT NULL UNIQUE,
	FOREIGN KEY(id_0) REFERENCES accidents(id) ON DELETE CASCADE,
	FOREIGN KEY(id_1) REFERENCES accidents(id) ON DELETE CASCADE
);

# -----------------------------------------------------------------------------
#       VIEW : franchises
# -----------------------------------------------------------------------------

DROP VIEW IF EXISTS franchises;

CREATE VIEW franchises(type_garantie_id, dommage_id, valeur) AS
SELECT t.id, d.id, compute_deductible(t.id, d.montant)
FROM sinistres s LEFT JOIN accidents a                       ON a.id                  = s.id
                 LEFT JOIN dommages d                        ON d.id                  = s.id
                 LEFT JOIN vehicules v                       ON v.id                  = s.vehicule_id
                 LEFT JOIN contrats c                        ON c.vehicule_id         = v.id
                 LEFT JOIN types_garantie t                  ON c.type_garantie_id    = t.id
WHERE d.montant IS NOT NULL AND s.statut = 0 GROUP BY s.id;

# -----------------------------------------------------------------------------
#       function : get_hash_salt
# -----------------------------------------------------------------------------

DROP FUNCTION IF EXISTS get_hash_salt;

DELIMITER $

CREATE FUNCTION get_hash_salt() RETURNS VARCHAR(12)
BEGIN
  RETURN '#^dza2455ç?';
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       function : get_token_lifetime
# -----------------------------------------------------------------------------

DROP FUNCTION IF EXISTS get_token_lifetime;

DELIMITER $

CREATE FUNCTION get_token_lifetime() RETURNS INTEGER
BEGIN
  RETURN 0x4B0;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       function : get_next_id
# -----------------------------------------------------------------------------

DROP FUNCTION IF EXISTS get_next_id;

DELIMITER $

CREATE FUNCTION get_next_id(in_table_name VARCHAR(64)) RETURNS INTEGER
BEGIN
  RETURN (SELECT auto_increment FROM information_schema.tables WHERE auto_increment IS NOT NULL AND table_name = in_table_name LIMIT 1);
END $

DELIMITER ;


# -----------------------------------------------------------------------------
#       INSERT : marques
# -----------------------------------------------------------------------------

INSERT INTO
  marques(nom)
SELECT DISTINCT make FROM vehicles_sources.vehicle_model_year;

# -----------------------------------------------------------------------------
#       INSERT : modeles
# -----------------------------------------------------------------------------

INSERT INTO 
  modeles(nom, annee, marque_id)
SELECT vsvmy.Model, Year, m.ID
FROM Vehicles_sources.Vehicle_Model_Year vsvmy, Marques m
WHERE vsvmy.make = m.NOM;

# -----------------------------------------------------------------------------
#       INSERT : types_garantie
# -----------------------------------------------------------------------------

INSERT INTO 
  types_garantie
VALUES
  (NULL, 1, 'Tiers', 300, 300, 500, NULL),
  (NULL, 2, 'Incendie', 600, 200, 400, NULL),
  (NULL, 3, 'Vol',600 , 200, 400, NULL),
  (NULL, 4, 'Tout Risque', 1000, 0, 100, NULL);

# -----------------------------------------------------------------------------
#       INSERT : types_sinistres_sans_tiers
# -----------------------------------------------------------------------------

INSERT INTO
  types_sinistres_sans_tiers
VALUES
  (NULL,1, 'Incendie'),
  (NULL,2, 'Vol'),
  (NULL,3, 'Bris de glace');

# -----------------------------------------------------------------------------
#       FUNCTION : compute_deductible
# -----------------------------------------------------------------------------

DROP FUNCTION IF EXISTS compute_deductible;

DELIMITER $

CREATE FUNCTION compute_deductible(in_id_insurance INTEGER, in_amount FLOAT) RETURNS FLOAT
BEGIN
  DECLARE min_deductible, max_deductible, result FLOAT DEFAULT 0.0;

  IF NOT in_amount THEN
    RETURN 0.0;
  END IF;

  SET in_amount := 0.1*in_amount;

  SELECT franchise_min, franchise_max
  INTO min_deductible, max_deductible
  FROM types_garantie
  WHERE id = in_id_insurance;

  IF in_amount BETWEEN min_deductible AND max_deductible THEN
    SET result := in_amount;

  ELSEIF in_amount > max_deductible THEN
      SET result := max_deductible;
  ELSE
      SET result := min_deductible;
  END IF;

  RETURN result;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       FUNCTION : is_person_an_insuree
# -----------------------------------------------------------------------------

DROP FUNCTION IF EXISTS is_person_an_insuree;

DELIMITER $

CREATE FUNCTION is_person_an_insuree(in_id INTEGER) RETURNS BIT
BEGIN
  DECLARE is_person_an_insuree BIT DEFAULT 0;
  SELECT IF(COUNT(*) > 0, 1, 0)
  INTO is_person_an_insuree
  FROM assures
  WHERE id = in_id;
  RETURN is_person_an_insuree;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       FUNCTION : is_person_an_admin
# -----------------------------------------------------------------------------

DROP FUNCTION IF EXISTS is_person_an_admin;

DELIMITER $

CREATE FUNCTION is_person_an_admin(in_id INTEGER) RETURNS BIT
BEGIN
  DECLARE is_person_an_admin BIT DEFAULT 0;
  SELECT IF(COUNT(*) > 0, 1, 0)
  INTO is_person_an_admin
  FROM administrateurs
  WHERE id = in_id;
  RETURN is_person_an_admin;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       FUNCTION : is_person_an_expert
# -----------------------------------------------------------------------------

DROP FUNCTION IF EXISTS is_person_an_expert;

DELIMITER $

CREATE FUNCTION is_person_an_expert(in_id INTEGER) RETURNS BIT
BEGIN
  DECLARE is_person_an_expert BIT DEFAULT 0;
  SELECT IF(COUNT(*) > 0, 1, 0)
  INTO is_person_an_expert
  FROM experts
  WHERE id = in_id;
  RETURN is_person_an_expert;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       FUNCTION : is_vehicle_covered
# -----------------------------------------------------------------------------

DROP FUNCTION IF EXISTS is_vehicle_covered;

DELIMITER $

CREATE FUNCTION is_vehicle_covered(in_id INTEGER) RETURNS BIT
BEGIN
  DECLARE is_vehicle_covered BIT DEFAULT 0;
  SELECT IF(COUNT(*) > 0, 1, 0)
  INTO is_vehicle_covered
  FROM contracts
  WHERE vehicle_id = in_id AND actif = 1;
  RETURN is_vehicle_covered;
END $

DELIMITER ;



# -----------------------------------------------------------------------------
#       FUNCTION : is_vehicle_owned_by
# -----------------------------------------------------------------------------

DROP FUNCTION IF EXISTS is_vehicle_owned_by;

DELIMITER $

CREATE FUNCTION is_vehicle_owned_by(in_id INTEGER, in_assure_id INTEGER) RETURNS BIT
BEGIN
  DECLARE result BIT DEFAULT 0;
  SELECT COUNT(*) != 0
  INTO result
  FROM vehicules
  WHERE id = in_id AND assure_id = in_assure_id;
  RETURN result;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       FUNCTION : coverage__sinister_matches_contract
# -----------------------------------------------------------------------------
DROP FUNCTION IF EXISTS coverage__sinister_matches_contract;

DELIMITER $

CREATE FUNCTION coverage__sinister_matches_contract(in_sinister_id INTEGER, in_contract_id INTEGER) RETURNS BIT
BEGIN
  DECLARE same_vehicle BIT DEFAULT 0;
  SELECT s.vehicule_id = c.vehicule_id
  INTO same_vehicle 
  FROM contrats c, sinistres s
  WHERE c.id = in_contract_id AND s.id = in_sinister_id;
  RETURN same_vehicle;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       PROCEDURE : check_if_in_assures
# -----------------------------------------------------------------------------

DROP PROCEDURE IF EXISTS check_if_in_assures;

DELIMITER $

CREATE PROCEDURE check_if_in_assures(IN in_id INTEGER)
BEGIN
  IF USER() != 'doctrine@localhost' AND NOT is_person_an_insuree(in_id) THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot insert into `clients` or `tiers`, primary key not found in `assures`';
  END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       PROCEDURE : check_if_not_in_assures
# -----------------------------------------------------------------------------

DROP PROCEDURE IF EXISTS check_if_not_in_assures;

DELIMITER $

CREATE PROCEDURE check_if_not_in_assures(IN in_id INTEGER)
BEGIN
  IF(is_person_an_insuree(in_id)) THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot insert into `admins` or `experts`, primary key found in `assures`';
  END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       PROCEDURE : check_if_not_in_tiers
# -----------------------------------------------------------------------------

DROP PROCEDURE IF EXISTS check_if_not_in_tiers;

DELIMITER $

CREATE PROCEDURE check_if_not_in_tiers(IN in_id INTEGER)
BEGIN
  DECLARE is_third_party BIT DEFAULT 0;
  SELECT IF(COUNT(*) > 0, 1, 0)
  INTO is_third_party
  FROM tiers
  WHERE id = in_id;

  IF(is_third_party) THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot insert into `clients`, primary key found in `tiers`';
  END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       PROCEDURE : check_if_not_in_clients
# -----------------------------------------------------------------------------

DROP PROCEDURE IF EXISTS check_if_not_in_clients;

DELIMITER $

CREATE PROCEDURE check_if_not_in_clients(IN in_id INTEGER)
BEGIN
  DECLARE is_customer BIT DEFAULT 0;
  SELECT IF(COUNT(*) > 0, 1, 0)
  INTO is_customer
  FROM clients
  WHERE id = in_id;

  IF(is_customer) THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot insert into `tiers`, primary key found in `clients`';
  END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       PROCEDURE : check_if_not_in_accidents
# -----------------------------------------------------------------------------

DROP PROCEDURE IF EXISTS check_if_not_in_accidents;

DELIMITER $

CREATE PROCEDURE check_if_not_in_accidents(IN in_id INTEGER)
BEGIN
  DECLARE is_in_accidents BIT DEFAULT 0;
  SELECT IF(COUNT(*) > 0, 1, 0)
  INTO is_in_accidents
  FROM accidents
  WHERE id = in_id;

  IF(is_in_accidents) THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot insert into `sinistres_sans_tiers`, primary key found in `accidents';
  END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       PROCEDURE : check_if_not_in_sinistres_sans_tiers
# -----------------------------------------------------------------------------

DROP PROCEDURE IF EXISTS check_if_not_in_sinistres_sans_tiers;

DELIMITER $

CREATE PROCEDURE check_if_not_in_sinistres_sans_tiers(IN in_id INTEGER)
BEGIN
  DECLARE is_in_sinistres_sans_tiers BIT DEFAULT 0;
  SELECT IF(COUNT(*) > 0, 1, 0)
  INTO is_in_sinistres_sans_tiers
  FROM sinistres_sans_tiers
  WHERE id = in_id;

  IF(is_in_sinistres_sans_tiers) THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot insert into `accidents`, primary key found in `sinistre_sans_tiers';
  END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       PROCEDURE : check_if_not_in_destructions
# -----------------------------------------------------------------------------

DROP PROCEDURE IF EXISTS check_if_not_in_destructions;

DELIMITER $

CREATE PROCEDURE check_if_not_in_destructions(IN in_id INTEGER)
BEGIN
  DECLARE is_in_destructions BIT DEFAULT 0;
  SELECT IF(COUNT(*) > 0, 1, 0)
  INTO is_in_destructions
  FROM destructions
  WHERE id = in_id;

  IF(is_in_destructions) THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot insert into `deteriorations`, primary key found in `destructions';
  END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       PROCEDURE : check_if_not_in_deteriorations
# -----------------------------------------------------------------------------

DROP PROCEDURE IF EXISTS check_if_not_in_deteriorations;

DELIMITER $

CREATE PROCEDURE check_if_not_in_deteriorations(IN in_id INTEGER)
BEGIN
  DECLARE is_in_deteriorations BIT DEFAULT 0;
  SELECT IF(COUNT(*) > 0, 1, 0)
  INTO is_in_deteriorations
  FROM deteriorations
  WHERE id = in_id;

  IF(is_in_deteriorations) THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot insert into `destructions`, primary key found in `deteriorations';
  END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       PROCEDURE : delete_token_linked_to_account
# -----------------------------------------------------------------------------

DELIMITER ;

DROP PROCEDURE IF EXISTS delete_token_linked_to_account;

DELIMITER $

CREATE PROCEDURE delete_token_linked_to_account(IN in_token_id INTEGER, IN in_account_id INTEGER)
BEGIN
    IF in_account_id THEN
      DELETE FROM tokens WHERE id != in_account_id AND compte_utilisateur_id = in_account_id;
    END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       PROCEDURE : save_or_update_token
# -----------------------------------------------------------------------------

DELIMITER ;

DROP PROCEDURE IF EXISTS save_or_update_token;

DELIMITER $

CREATE PROCEDURE save_or_update_token(INOUT inout_id INTEGER, OUT out_created_at TIMESTAMP, OUT out_maj TIMESTAMP, IN in_value VARCHAR(64), IN in_api_server VARCHAR(15), IN in_account_id INTEGER)
  BEGIN
    IF in_account_id THEN
      DELETE FROM tokens WHERE compte_utilisateur_id = in_account_id;
    END IF;

    IF !in_account_id THEN
      INSERT INTO tokens(value, api_server, compte_utilisateur_id)
      VALUES(in_value, in_api_server, IF(in_account_id, in_account_id, null));
      SELECT id, created_at, maj INTO inout_id, out_created_at, out_maj
      FROM tokens WHERE id = (SELECT MAX(id) FROM tokens);
  ELSE
      UPDATE tokens
      SET value                := in_value,
          api_server           := in_api_server,
         compte_utilisateur_id := IF(in_account_id, in_account_id, null)
      WHERE id = inout_id;
  END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : on_create_admin
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS on_create_admin;

DELIMITER $

CREATE TRIGGER on_create_admin
BEFORE INSERT ON administrateurs
FOR EACH ROW
BEGIN
  DECLARE is_expert BIT DEFAULT 0;

  SELECT IF(e.id IS NULL, 0, 1)
  INTO is_expert
  FROM personnes p LEFT JOIN experts e ON p.id = e.id
  WHERE p.id = NEW.id
  LIMIT 1;

  IF(is_person_an_insuree(NEW.id)) THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cannot insert into `administrateurs`, primary key found in `assures` !';
  END IF;


  IF(is_expert) THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cannot insert into `administrateurs`, primary key found in `experts` !';
  END IF;

END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : on_create_expert
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS on_create_expert;

DELIMITER $

CREATE TRIGGER on_create_expert
BEFORE INSERT ON experts
FOR EACH ROW
BEGIN
  DECLARE is_admin BIT DEFAULT 0;

  SELECT IF(adm.id IS NULL, 0, 1)
  INTO is_admin
  FROM personnes p LEFT JOIN administrateurs adm  ON p.id = adm.id
  WHERE p.id = NEW.id
  LIMIT 1;

  IF(is_person_an_insuree(NEW.id)) THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cannot insert into `administrateurs`, primary key found in `assures` !';
  END IF;

  IF(is_admin) THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cannot insert into `administrateurs`, primary key found in `experts` !';
  END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : on_create_insuree
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS on_create_insuree;

DELIMITER $

CREATE TRIGGER on_create_insuree
BEFORE INSERT ON assures
FOR EACH ROW
BEGIN
  IF is_person_an_admin(NEW.id) OR is_person_an_expert(NEW.ID) THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cannot insert into `assures`, primary key found in `experts` or `administrateurs`!';
  END IF;
END $

DELIMITER ;


# -----------------------------------------------------------------------------
#       TRIGGER : on_create_customer
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS on_create_customer;

DELIMITER $

CREATE TRIGGER on_create_customer
BEFORE INSERT ON clients
FOR EACH ROW
BEGIN

  DECLARE is_third_party BIT DEFAULT 0;
  CALL check_if_in_assures(NEW.id);

  SELECT IF(t.id IS NULL, 0, 1)
  INTO is_third_party
  FROM personnes p LEFT JOIN tiers t
  ON p.id = t.id
  WHERE p.id = NEW.id
  LIMIT 1;

  IF(is_third_party) THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cannot insert into `clients`, primary key found in `tiers` !';
  END IF;

END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : on_create_third_party
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS on_create_third_party;

DELIMITER $

CREATE TRIGGER on_create_third_party
BEFORE INSERT ON tiers
FOR EACH ROW
BEGIN
  DECLARE is_customer BIT DEFAULT 0;

  CALL check_if_in_assures(NEW.id);

  SELECT IF(c.id IS NULL, 0, 1)
  INTO is_customer
  FROM personnes p LEFT JOIN clients c
  ON p.id = c.id
  WHERE p.id = NEW.id
  LIMIT 1;

  IF(is_customer) THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cannot insert into `tiers`, primary key found in `clients` !';
  END IF;

END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : on_create_accident
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS on_create_accident;

DELIMITER $

CREATE TRIGGER on_create_accident
BEFORE INSERT ON accidents
FOR EACH ROW
BEGIN
  CALL check_if_not_in_sinistres_sans_tiers(NEW.id);
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : on_create_plain_sinister
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS on_create_plain_sinister;

DELIMITER $

CREATE TRIGGER on_create_plain_sinister
BEFORE INSERT ON sinistres_sans_tiers
FOR EACH ROW
BEGIN
  CALL check_if_not_in_accidents(NEW.id);
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : on_create_destruction
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS on_create_destruction;

DELIMITER $

CREATE TRIGGER on_create_destruction
BEFORE INSERT ON destructions
FOR EACH ROW
BEGIN
  CALL check_if_not_in_deteriorations(NEW.id);
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : on_create_deterioration
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS on_create_deterioration;

DELIMITER $

CREATE TRIGGER on_create_deterioration
BEFORE INSERT ON deteriorations
FOR EACH ROW
BEGIN
  CALL check_if_not_in_destructions(NEW.id);
END $

DELIMITER ;



# -----------------------------------------------------------------------------
#       TRIGGER : on_update_vehicle
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS on_update_vehicle;

DELIMITER $

CREATE TRIGGER on_update_vehicle
AFTER UPDATE ON vehicules
FOR EACH ROW
BEGIN

  IF OLD.assure_id IS NOT NULL 
  AND OLD.assure_id != NEW.assure_id 
  AND is_vehicle_covered(NEW.id)THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot update `vehicules`, column `vehicule_id` found in `contrat` !';  
  END IF;

  IF (OLD.immatriculation != NEW.immatriculation AND OLD.carte_grise != NEW.carte_grise) 
  OR (OLD.valeur != NEW.valeur) OR (OLD.assure_id != NEW.assure_id) THEN
    INSERT INTO h_vehicules
    VALUES(NULL, OLD.id, OLD.assure_id, NULL, NULL, OLD.immatriculation, OLD.carte_grise, OLD.date_achat, OLD.valeur);
  END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : on_delete_contract
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS on_delete_contract;

DELIMITER $

CREATE TRIGGER on_delete_contract
AFTER DELETE ON contrats
FOR EACH ROW 
BEGIN
  INSERT INTO h_contrats(id, date_h, type_garantie_id, client_id, vehicule_id, montant, date_souscription, etat, contrat )
  VALUES(OLD.id, NULL, OLD.type_garantie_id, OLD.client_id, OLD.vehicule_id, OLD.montant, OLD.date_souscription, OLD.etat, OLD.contrat);
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : on_update_sinister
# -----------------------------------------------------------------------------
/* TODO FIX THIS
DROP TRIGGER IF EXISTS on_update_coverage;

DELIMITER $

CREATE TRIGGER on_update_coverage
BEFORE UPDATE ON sinistres_contrats_experts
FOR EACH ROW
BEGIN
  DECLARE is_vehicle_owned BIT DEFAULT 0;
  DECLARE is_vehicle_covered BIT DEFAULT 0;
  SELECT IF(assure_id IS NULL, 0, 1),
         IF(c.id IS NULL, 0, 1)
  INTO is_vehicle_owned, is_vehicle_covered
  FROM vehicules v LEFT JOIN contrats c ON c.vehicule_id = v.id LEFT JOIN sinistres s ON NEW.sinistre_id = s.id
  WHERE v.id = NEW.vehicule_id;

  IF NOT is_vehicle_owned THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot insert into `sinister`, column `assure_id` must not be null in `vehicle`!';
  END IF;

  IF NEW.expert_id AND NOT is_vehicle_covered THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot insert into `sinistres` with column `expert_id` not null, column `vehicule_id` not found in `contrats`!';
  END IF;
END $

DELIMITER ; */

# -----------------------------------------------------------------------------
#       TRIGGER : on_create_accident
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS on_create_spoilage;

DELIMITER $

CREATE TRIGGER on_create_coverage
BEFORE INSERT ON deteriorations
FOR EACH ROW
BEGIN
  DECLARE is_vehicle_covered BIT DEFAULT 0;
  IF NEW.garagiste_id IS NOT NULL THEN
    SELECT IF(c.vehicule_id IS NULL, 0, 1)
    INTO is_vehicle_covered
    FROM sinistres s LEFT JOIN vehicules v ON s.vehicule_id = v.id
                     LEFT JOIN contrats  c ON c.vehicule_id = v.id
    WHERE s.id = NEW.id;

    IF NOT is_vehicle_covered THEN
      DELETE FROM dommages 
      WHERE id = NEW.id;
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Cannot insert into `deteriorations` with column `garagiste_id` not null, column `vehicule_id` not found in `contrats`!';
    END IF;
  END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : on_update_accident
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS on_update_accident;

DELIMITER $

CREATE TRIGGER on_update_accident
BEFORE INSERT ON accidents
FOR EACH ROW
BEGIN
  DECLARE is_vehicle_covered BIT DEFAULT 0;
  SELECT IF(c.vehicule_id IS NULL, 0, 1)
  INTO is_vehicle_covered
  FROM sinistres s LEFT JOIN vehicules v ON s.vehicule_id = v.id
                   LEFT JOIN contrats  c ON c.vehicule_id = v.id
  WHERE s.id = NEW.id;

  IF NOT is_vehicle_covered THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot insert into `accidents` with column `garagiste_id` not null, column `vehicule_id` not found in `contrats`!';
  END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : on_create_contract
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS on_create_contract;

DELIMITER $

CREATE TRIGGER on_create_contract
BEFORE INSERT ON contrats
FOR EACH ROW
BEGIN
  DECLARE already_active BIT DEFAULT 0;

  IF NOT is_vehicle_owned_by(NEW.vehicule_id, NEW.id) THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot insert into `contracts`, column `contrats.assure_id` does not match `vehicule.assure_id`';
  END IF;

  IF NEW.actif THEN
    SELECT COUNT(*) > 0 INTO already_active
    FROM contrats c WHERE actif = TRUE AND vehicule_id = NEW.vehicule_id LIMIT 1;
    IF already_active THEN
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Cannot insert into `contrats` there can be only one row with column `actif` set to TRUE !';
    END IF;
  END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : on_update_contract
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS on_update_contract;

DELIMITER $

CREATE TRIGGER on_update_contract
BEFORE UPDATE ON contrats
FOR EACH ROW
BEGIN
  DECLARE already_active BIT DEFAULT 0;
  IF NEW.actif THEN
    SELECT COUNT(*) > 0 INTO already_active
    FROM contrats c WHERE actif = TRUE AND vehicule_id = NEW.vehicule_id LIMIT 1;
    IF already_active THEN
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Cannot update `contrats` there can be only one row with column `actif` set to TRUE !';
    END IF;
  END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : on_create_coverage
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS on_create_coverage;

DELIMITER $

CREATE TRIGGER on_create_coverage
BEFORE INSERT ON sinistres_contrats_experts
FOR EACH ROW
BEGIN
  IF NOT coverage__sinister_matches_contract(NEW.sinistre_id, NEW.contrat_id) THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot insert into `sinistres_contrats_experts` : column `sinistre.vehicule_id` does not match column `contrats.vehicule_id`';
  END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : on_update_sinister
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS on_update_coverage;

DELIMITER $

CREATE TRIGGER on_update_coverage
BEFORE UPDATE ON sinistres_contrats_experts
FOR EACH ROW
BEGIN
  IF NOT coverage__sinister_matches_contract(NEW.sinistre_id, NEW.contrat_id) THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot update `sinistres_contrats_experts` : column `sinistre.vehicule_id` does not match column `contrats.vehicule_id`';
  END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : on_create_token, on_udpate_token
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS on_create_token;

DELIMITER $

CREATE TRIGGER on_create_token
BEFORE INSERT ON tokens
FOR EACH ROW
  BEGIN
    CALL delete_token_linked_to_account(NEW.id, NEW.compte_utilisateur_id);
  END $

DELIMITER ;


DROP TRIGGER IF EXISTS on_update_token;

DELIMITER $

CREATE TRIGGER on_update_token
BEFORE UPDATE ON tokens
FOR EACH ROW
  BEGIN
    CALL delete_token_linked_to_account(NEW.id, NEW.compte_utilisateur_id);
  END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       EVENT : clear_tokens
# -----------------------------------------------------------------------------

DROP EVENT IF EXISTS clear_tokens;

CREATE EVENT clear_tokens
  ON SCHEDULE AT CURRENT_TIMESTAMP + INTERVAL 1 HOUR
DO
  DELETE FROM tokens
  WHERE TO_SECONDS(CURRENT_TIMESTAMP) - TO_SECONDS(maj) >= get_token_lifetime();