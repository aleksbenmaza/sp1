DROP DATABASE IF EXISTS aaa;

CREATE DATABASE aaa;
USE aaa;

# -----------------------------------------------------------------------------
#       TABLE : makes
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS makes(
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NULL,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id) 
 );

# -----------------------------------------------------------------------------
#       TABLE : models
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS models(
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NULL  ,
  year INTEGER(4) NULL,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  make_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY(make_id) REFERENCES makes(id) ON DELETE CASCADE

 );

# -----------------------------------------------------------------------------
#       TABLE : companies
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS companies(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(64)
);

# -----------------------------------------------------------------------------
#       TABLES : persons, insurees, third_parties, experts, managers, customers
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS persons(
  id BIGINT PRIMARY KEY  AUTO_INCREMENT,
  last_name VARCHAR(25) NULL,
  first_name VARCHAR(25) NULL,
  adress VARCHAR(25) NULL,
  city VARCHAR(25) NULL,
  zip_code SMALLINT NULL,
  phone_number VARCHAR(13) NULL,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  type TINYINT
 );

CREATE TABLE IF NOT EXISTS insurees(
  id BIGINT PRIMARY KEY,
  bonus_malus REAL(5,2) DEFAULT 1.0,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (id) REFERENCES persons(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS third_parties(
  id BIGINT PRIMARY KEY,
  company_id BIGINT NOT NULL,
  bonus_malus REAL(5,2) DEFAULT 1.0,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (id) REFERENCES insurees(id) ON DELETE CASCADE,
  FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE CASCADE
 );

CREATE TABLE IF NOT EXISTS experts(
  id BIGINT PRIMARY KEY,
  rank SMALLINT,
  siret_number CHAR(12) UNIQUE,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (id) REFERENCES persons(id) ON DELETE CASCADE
);

 CREATE TABLE IF NOT EXISTS managers(
   id BIGINT PRIMARY KEY,
   role VARCHAR(128) NULL,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   FOREIGN KEY(id) REFERENCES persons(id) ON DELETE CASCADE
 );

 CREATE TABLE IF NOT EXISTS customers(
  id BIGINT PRIMARY KEY,
  manager_id BIGINT,
  has_sepa_document BIT(1) DEFAULT 0,
  status TINYINT,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY(id) REFERENCES insurees(id) ON DELETE CASCADE,
  FOREIGN KEY(manager_id) REFERENCES managers(id) ON DELETE SET NULL
);

# -----------------------------------------------------------------------------
#       TABLES : vehicles, h_vehicles
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS vehicles(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  model_id BIGINT NOT NULL,
  vin_number CHAR(17) NOT NULL UNIQUE,
  value FLOAT,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY(model_id) REFERENCES models(id) ON DELETE CASCADE,
  FOREIGN KEY(insuree_id) REFERENCES insurees(id) ON DELETE SET NULL
 );

CREATE TABLE IF NOT EXISTS h_vehicles(
  id BIGINT NOT NULL AUTO_INCREMENT,
  vehicle_id BIGINT NOT NULL,
  insuree_id BIGINT,
  contract_id BIGINT,
  h_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  registration_number CHAR(7),
  purchase_date DATE,
  value FLOAT,
  PRIMARY KEY (id),
  FOREIGN KEY(vehicle_id) REFERENCES vehicles(id) ON DELETE CASCADE,
  FOREIGN KEY(insuree_id) REFERENCES insurees(id) ON DELETE SET NULL,
  FOREIGN KEY(contract_id) REFERENCES contracts(id) ON DELETE SET NULL
 );

# -----------------------------------------------------------------------------
#       TABLE : tokens
# -----------------------------------------------------------------------------

CREATE TABLE tokens (
  id BIGINT PRIMARY KEY AUTO_INCREMENT
  value VARCHAR(64) DEFAULT NULL,
  old_value VARCHAR(64) DEFAULT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  api_server VARCHAR(15) DEFAULT NULL
);

# -----------------------------------------------------------------------------
#       TABLE : user_accounts
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS user_accounts(
  id BIGINT PRIMARY KEY,
  email_address VARCHAR(50) NOT NULL,
  hash CHAR(40) NULL,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY(id) REFERENCES persons(id) ON DELETE CASCADE
);

# -----------------------------------------------------------------------------
#       TABLE : car_dealers
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS car_dealers(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(128) NULL,
  is_certified BIT(1) DEFAULT 0 DEFAULT 0,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
 );

# -----------------------------------------------------------------------------
#       TABLE : plain_sinister_types
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS plain_sinister_types(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code ENUM('INC','VOL','BRI') UNIQUE,
  name VARCHAR(50),
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

# -----------------------------------------------------------------------------
#       TABLES : sinisters, accidents, plain_sinisters
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS sinisters(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  contract_id BIGINT NOT NULL,
  expert_id BIGINT,
  comment VARCHAR(100) NULL,
  date DATE NULL,
  time TIME NULL,
  status TINYINT,
  is_closed BOOLEAN,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  type BOOLEAN,
  FOREIGN KEY(contract_id) REFERENCES contract(id) ON DELETE CASCADE,
  FOREIGN KEY(expert_id) REFERENCES experts(id) ON DELETE SET NULL
);


CREATE TABLE IF NOT EXISTS accidents(
  id BIGINT PRIMARY KEY,
  casualties_count BIGINT NULL,
  taux_resp FLOAT,
  FOREIGN KEY (id) REFERENCES sinisters(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS customer_accidents(
  id BIGINT PRIMARY KEY,
  FOREIGN KEY(id) REFERENCES accidents(id)
);

CREATE TABLE IF NOT EXISTS third_party_accidents(
  id BIGINT PRIMARY KEY,
  vehicle_id BIGINT NOT NULL,
  FOREIGN KEY(id) REFERENCES accidents(id),
  FOREIGN KEY(vehicle_id) REFERENCES vehicles(id)
);


CREATE TABLE IF NOT EXISTS plain_sinisters(
  id BIGINT PRIMARY KEY,
  plain_sinister_type_id BIGINT NOT NULL,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY(id) REFERENCES sinisters(id) ON DELETE CASCADE,
  FOREIGN KEY(type_id) REFERENCES plain_sinister_types(id) ON DELETE CASCADE
);

# -----------------------------------------------------------------------------
#       TABLE : insurances
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS insurances(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  code enum('tie','inc','vol','tou') UNIQUE,
  name VARCHAR(100) NULL,
  default_amount FLOAT(13,2) NULL,
  min_deductible FLOAT(13,2) NULL,
  max_deductible FLOAT(13,2) NULL,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

# -----------------------------------------------------------------------------
#       TABLES : damages, spoilages, destructions
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS damages(
  id BIGINT PRIMARY KEY,
  description VARCHAR(100) NULL,
  amount FLOAT(12,2) NULL,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  type BOOLEAN,
  FOREIGN KEY(id) REFERENCES sinisters(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS spoilages(
  id BIGINT PRIMARY KEY,
  description VARCHAR(100),
  amount FLOAT,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  car_dealer_id BIGINT,
  rate FLOAT,
  FOREIGN KEY(id) REFERENCES sinisters(id) ON DELETE CASCADE,
  FOREIGN KEY(car_dealer_id) REFERENCES car_dealers(id) ON DELETE SET NULL
);


CREATE TABLE IF NOT EXISTS destructions (
  id BIGINT NOT NULL,
  description VARCHAR(100),
  amount FLOAT,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  est_total TINYINT(1),
  PRIMARY KEY(id),
  FOREIGN KEY(id) REFERENCES sinisters(id) ON DELETE CASCADE
);

# -----------------------------------------------------------------------------
#       TABLES : contracts
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS contracts(
   id BIGINT PRIMARY KEY AUTO_INCREMENT,
   customer_id BIGINT NOT NULL,
   vehicle_id BIGINT NOT NULL,
   insurance_id BIGINT NOT NULL,
   amount REAL(5, 2) NULL,
   subscription_date DATE NULL,
   active BIT(1) DEFAULT 0 NOT NULL,
   has_contract_document BIT(1) DEFAULT 0 NULL,
   status TINYINT,
   updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   FOREIGN KEY(customer_id) REFERENCES customers(id) ON DELETE CASCADE,
   FOREIGN KEY(vehicle_id) REFERENCES vehicles(id) ON DELETE CASCADE,
   FOREIGN KEY(type_garantie_id) REFERENCES insurances(id) ON DELETE CASCADE
);

# -----------------------------------------------------------------------------
#       TABLE : plain_sinister_types__insurances,
#               accidents__accidents,
#               tokens__user_accounts
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS types_sinisters__insurances(
  plain_sinister_type_id BIGINT NOT NULL,
  insurance_id BIGINT NOT NULL,
  rate float,
  PRIMARY KEY (plain_sinister_type_id, insurance_id),
  FOREIGN KEY(plain_sinister_type_id) REFERENCES plain_sinister_types(id) ON DELETE CASCADE,
  FOREIGN KEY(insurance_id) REFERENCES insurances(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS vehicles__insurees(
  vehicle_id BIGINT PRIMARY KEY,
  insuree_id BIGINT NOT NULL,
  registration_number CHAR(7),
  purchase_date DATE,
  FOREIGN KEY(vehicle_id) REFERENCES vehicles(id) ON DELETE CASCADE,
  FOREIGN KEY (insuree_id) REFERENCES insurees(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS accidents__accidents(
	id_0 BIGINT NOT NULL UNIQUE,
	id_1 BIGINT NOT NULL UNIQUE,
	FOREIGN KEY(id_0) REFERENCES customer_accidents(id) ON DELETE CASCADE,
	FOREIGN KEY(id_1) REFERENCES customer_accidents(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tokens__user_accounts (
  token_id BIGINT NOT NULL UNIQUE,
  user_account_id BIGINT NOT NULL UNIQUE,
  FOREIGN KEY (token_id) REFERENCES tokens(id) ON DELETE CASCADE,
  FOREIGN KEY (user_account_id) REFERENCES user_accounts(id) ON DELETE CASCADE
)

# -----------------------------------------------------------------------------
#       VIEW : deductibles
# -----------------------------------------------------------------------------

DROP VIEW IF EXISTS deductibles;

CREATE VIEW deductibles(type_garantie_id, sinister_id, value) AS
SELECT t.id, s.id, compute_deductible(t.id, IF(sp.id IS NULL, v.value, sp.amount))
FROM sinisters s LEFT JOIN accidents a                       ON a.id                  = s.id
                 LEFT JOIN spoilages sp                       ON sp.id = s.id
                 LEFT JOIN vehicles v                       ON v.id                  = s.vehicle_id
                 LEFT JOIN contracts c                        ON c.vehicle_id         = v.id
                 LEFT JOIN insurances t                  ON c.insurance_id    = t.id
WHERE s.status = 0 GROUP BY s.id;

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

CREATE FUNCTION get_next_id(in_table_name VARCHAR(64)) RETURNS BIGINT
BEGIN
  RETURN (SELECT auto_increment FROM information_schema.tables WHERE auto_increment IS NOT NULL AND table_name = in_table_name LIMIT 1);
END $

DELIMITER ;


# -----------------------------------------------------------------------------
#       INSERT : makes
# -----------------------------------------------------------------------------

INSERT INTO
  makes(name)
SELECT DISTINCT make FROM vehicles_sources.vehicle_model_year;

# -----------------------------------------------------------------------------
#       INSERT : models
# -----------------------------------------------------------------------------

INSERT INTO 
  models(name, year, marque_id)
SELECT vsvmy.Model, Year, m.id
FROM Vehicles_sources.Vehicle_Model_Year vsvmy, Makes m
WHERE vsvmy.make = m.name;

# -----------------------------------------------------------------------------
#       INSERT : insurances
# -----------------------------------------------------------------------------

INSERT INTO 
  insurances
VALUES
  (NULL, 1, 'Tiers', 300, 300, 500, NULL),
  (NULL, 2, 'Incendie', 600, 200, 400, NULL),
  (NULL, 3, 'Vol',600 , 200, 400, NULL),
  (NULL, 4, 'Tout Risque', 1000, 0, 100, NULL);

# -----------------------------------------------------------------------------
#       INSERT : plain_sinister_types
# -----------------------------------------------------------------------------

INSERT INTO
  plain_sinister_types
VALUES
  (NULL,1, 'Incendie'),
  (NULL,2, 'Vol'),
  (NULL,3, 'Bris de glace');

# -----------------------------------------------------------------------------
#       FUNCTION : compute_deductible
# -----------------------------------------------------------------------------

DROP FUNCTION IF EXISTS compute_deductible;

DELIMITER $

CREATE FUNCTION compute_deductible(in_insurance_id BIGINT, in_amount FLOAT) RETURNS FLOAT
BEGIN
  DECLARE v_min_deductible, v_max_deductible, result FLOAT DEFAULT 0.0;

  IF NOT in_amount THEN
    RETURN 0.0;
  END IF;

  SET in_amount := 0.1 * in_amount;

  SELECT min_deductible, max_deductible
  INTO v_min_deductible, v_max_deductible
  FROM insurances
  WHERE id = in_insurance_id;

  IF in_amount BETWEEN min_deductible AND max_deductible THEN
    SET result := in_amount;

  ELSEIF in_amount > max_deductible THEN
      SET result := v_max_deductible;
  ELSE
      SET result := v_min_deductible;
  END IF;

  RETURN result;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       FUNCTION : is_person_an_insuree
# -----------------------------------------------------------------------------

DROP FUNCTION IF EXISTS is_person_an_insuree;

DELIMITER $

CREATE FUNCTION is_person_an_insuree(in_id BIGINT) RETURNS BIT
BEGIN
  DECLARE is_person_an_insuree BIT DEFAULT 0;
  SELECT IF(COUNT(*) > 0, 1, 0)
  INTO is_person_an_insuree
  FROM insurees
  WHERE id = in_id;
  RETURN is_person_an_insuree;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       FUNCTION : is_person_a_manager
# -----------------------------------------------------------------------------

DROP FUNCTION IF EXISTS is_person_a_manager;

DELIMITER $

CREATE FUNCTION is_person_a_manager(in_id BIGINT) RETURNS BIT
BEGIN
  DECLARE is_person_a_manager BIT DEFAULT 0;
  SELECT IF(COUNT(*) > 0, 1, 0)
  INTO is_person_a_manager
  FROM managers
  WHERE id = in_id;
  RETURN is_person_a_manager;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       FUNCTION : is_vehicle_covered
# -----------------------------------------------------------------------------

DROP FUNCTION IF EXISTS is_vehicle_covered;

DELIMITER $

CREATE FUNCTION is_vehicle_covered(in_id BIGINT) RETURNS BIT
BEGIN
  DECLARE is_vehicle_covered BIT DEFAULT 0;
  SELECT IF(COUNT(*) > 0, 1, 0)
  INTO is_vehicle_covered
  FROM contracts
  WHERE vehicle_id = in_id AND active = 1;
  RETURN is_vehicle_covered;
END $

DELIMITER ;



# -----------------------------------------------------------------------------
#       FUNCTION : is_vehicle_owned_by
# -----------------------------------------------------------------------------

DROP FUNCTION IF EXISTS is_vehicle_owned_by;

DELIMITER $

CREATE FUNCTION is_vehicle_owned_by(in_id BIGINT, in_insuree_id BIGINT) RETURNS BIT
BEGIN
  DECLARE result BIT DEFAULT 0;
  SELECT COUNT(*) != 0
  INTO result
  FROM vehicles
  WHERE id = in_id AND insuree_id = in_insuree_id;
  RETURN result;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       FUNCTION : is_present
# -----------------------------------------------------------------------------

DROP FUNCTION IF EXISTS is_present;

DELIMITER $

CREATE FUNCTION is_present(in_id BIGINT, VARCHAR(255) in_table_name) RETURNS BIT
BEGIN
  DECLARE stm VARCHAR(255);
  DECLARE result BIT DEFAULT 0;

  SET stm := CONCAT('SELECT IF(COUNT(*), 1, 0) INTO result FROM ', in_table_name,' WHERE id =', in_id);
  PREPARE query FROM stm;
  EXECUTE query;
  DEALLOCATE query;
  RETURN result;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       FUNCTION : is_sinister_a_plain_sinister
# -----------------------------------------------------------------------------

DROP FUNCTION IF EXISTS is_sinister_a_plain_sinister;

DELIMITER $

CREATE FUNCTION is_sinister_a_plain_sinister(in_id BIGINT) RETURNS BIT
BEGIN
  DECLARE result BIT DEFAULT 0;

  SELECT IF(COUNT(*), 1, 0) INTO result FROM plain_sinisters WHERE id = in_id;

  RETURN result;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       PROCEDURE : check_if_not_in_insurees
# -----------------------------------------------------------------------------

DROP PROCEDURE IF EXISTS check_if_not_in_insurees;

DELIMITER $

CREATE PROCEDURE check_if_not_in_insurees(IN in_id BIGINT)
BEGIN
  IF(is_present(in_id, 'insurees')) THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot insert into `managers` or `experts`, primary key found in `insurees`';
  END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       PROCEDURE : check_if_not_in_third_parties
# -----------------------------------------------------------------------------

DROP PROCEDURE IF EXISTS check_if_not_in_third_parties;

DELIMITER $

CREATE PROCEDURE check_if_not_in_third_parties(IN in_id BIGINT)
BEGIN
  IF is_present(in_id, 'third_parties') THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot insert into `customers`, primary key found in `third_parties`';
  END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       PROCEDURE : check_if_not_in_customers
# -----------------------------------------------------------------------------

DROP PROCEDURE IF EXISTS check_if_not_in_customers;

DELIMITER $

CREATE PROCEDURE check_if_not_in_customers(IN in_id BIGINT)
BEGIN
  IF is_present(in_id, 'customers') THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot insert into `third_parties`, primary key found in `customers`';
  END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       PROCEDURE : check_if_not_in_accidents
# -----------------------------------------------------------------------------

DROP PROCEDURE IF EXISTS check_if_not_in_accidents;

DELIMITER $

CREATE PROCEDURE check_if_not_in_accidents(IN in_id BIGINT)
BEGIN
  IF is_present(in_id, 'accidents') THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot insert into `plain_sinisters`, primary key found in `accidents';
  END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       PROCEDURE : check_if_not_in_plain_sinisters
# -----------------------------------------------------------------------------

DROP PROCEDURE IF EXISTS check_if_not_in_plain_sinisters;

DELIMITER $

CREATE PROCEDURE check_if_not_in_plain_sinisters(IN in_id BIGINT)
BEGIN
  IF is_present(in_id, 'plain_sinisters') THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot insert into `accidents`, primary key found in `plain_sinisters`';
  END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       PROCEDURE : check_if_not_in_destructions
# -----------------------------------------------------------------------------

DROP PROCEDURE IF EXISTS check_if_not_in_destructions;

DELIMITER $

CREATE PROCEDURE check_if_not_in_destructions(IN in_id BIGINT)
BEGIN
  IF is_present(in_id, 'destructions') THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot insert into `spoilages`, primary key found in `destructions';
  END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       PROCEDURE : check_if_not_in_spoilages
# -----------------------------------------------------------------------------

DROP PROCEDURE IF EXISTS check_if_not_in_spoilages;

DELIMITER $

CREATE PROCEDURE check_if_not_in_spoilages(IN in_id BIGINT)
BEGIN
  IF is_present(in_id, 'spoilages') THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot insert into `destructions`, primary key found in `spoilages';
  END IF;
END $

DELIMITER ;


# -----------------------------------------------------------------------------
#       TRIGGER : on_create_manager
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS on_create_manager;

DELIMITER $

CREATE TRIGGER on_create_manager
BEFORE INSERT ON managers
FOR EACH ROW
BEGIN

  IF is_present(NEW.id, 'insurees') THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cannot insert into `managers`, primary key found in `insurees` !';
  END IF;


  IF is_present(new.id, 'experts') THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cannot insert into `managers`, primary key found in `experts` !';
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
  IF is_present(NEW.id, 'insurees') THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cannot insert into `managers`, primary key found in `insurees` !';
  END IF;

  IF is_present(NEW.id, 'manager') THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cannot insert into `managers`, primary key found in `managers` !';
  END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : on_create_insuree
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS on_create_insuree;

DELIMITER $

CREATE TRIGGER on_create_insuree
BEFORE INSERT ON insurees
FOR EACH ROW
BEGIN
  IF is_present(NEW.id, 'managers') THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cannot insert into `insurees`, primary key found in `managers`!';
  END IF;
  IF is_present(NEW.id, 'experts') THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cannot insert into `insurees`, primary key found in `experts`!';
  END IF;
END $

DELIMITER ;


# -----------------------------------------------------------------------------
#       TRIGGER : on_create_customer
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS on_create_customer;

DELIMITER $

CREATE TRIGGER on_create_customer
BEFORE INSERT ON customers
FOR EACH ROW
BEGIN
  IF is_present(NEW.id, 'third_parties') THEN
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
BEFORE INSERT ON third_parties
FOR EACH ROW
BEGIN
  IF is_present(NEW.id, 'customers') THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cannot insert into `third_parties`, primary key found in `customers` !';
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
  CALL check_if_not_in_plain_sinisters(NEW.id);
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : on_create_customer_accident
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS on_create_customer_accident;

DELIMITER $

CREATE TRIGGER on_create_customer_accident
BEFORE INSERT ON customer_accidents
FOR EACH ROW
BEGIN
  IF is_present(NEW.id, 'third_party_accidents') THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot insert into `customer_accidents`, `id` already referenced in `third_party_accidents`';
  END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : on_create_third_party_accident
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS on_create_third_party_accident;

DELIMITER $

CREATE TRIGGER on_create_third_party_accident
BEFORE INSERT ON third_party_accidents
FOR EACH ROW
BEGIN
  IF is_present(NEW.id, 'customer_accidents') THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot insert into `third_party_accidents`, `id` already referenced in `customer_accidents`';
    END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : on_create_plain_sinister
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS on_create_plain_sinister;

DELIMITER $

CREATE TRIGGER on_create_plain_sinister
BEFORE INSERT ON plain_sinisters
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
  CALL check_if_not_in_spoilages(NEW.id);
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : on_create_spoilage
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS on_create_spoilage;

DELIMITER $

CREATE TRIGGER on_create_spoilage
BEFORE INSERT ON spoilages
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
AFTER UPDATE ON vehicles
FOR EACH ROW
BEGIN

  IF OLD.insuree_id IS NOT NULL
  AND OLD.insuree_id != NEW.insuree_id
  AND is_vehicle_covered(NEW.id)THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot update `vehicles`, column `vehicle_id` found in `contract` !';
  END IF;

  IF (OLD.registration_number != NEW.registration_number)
  OR (OLD.value != NEW.value) OR (OLD.insuree_id != NEW.insuree_id) THEN
    INSERT INTO h_vehicles
    VALUES(NULL, OLD.id, OLD.insuree_id, NULL, NULL, OLD.registration_number, OLD.purchase_date, OLD.value);
  END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : on_delete_contract
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS on_delete_contract;

DELIMITER $

CREATE TRIGGER on_delete_contract
AFTER DELETE ON contracts
FOR EACH ROW 
BEGIN
  INSERT INTO h_contracts(id, h_date, insurance_id, customer_id, vehicle_id, amount, subscription_date, status, has_contract_document)
  VALUES(OLD.id, NULL, OLD.insurance_id, OLD.customer_id, OLD.vehicle_id, OLD.amount, OLD.subscription_date, OLD.status, OLD.has_contract_document);
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : on_create_sinister
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS on_create_sinister;

DELIMITER $

CREATE TRIGGER on_create_sinister
BEFORE INSERT ON sinisters
FOR EACH ROW
BEGIN
  DECLARE v_active BIT;
  SELECT active INTO v_active FROM contracts WHERE id = NEW.id;
  IF NOT v_active THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot insert into `sinisters`, referenced `contracts` table row has `active` column set to `false`';
  END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : on_create_third_party_accident
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS on_create_third_party_accident;

DELIMITER $

CREATE TRIGGER on_create_third_party_accident
BEFORE INSERT ON third_party_accidents
FOR EACH ROW
BEGIN
  IF is_vehicle_covered(NEW.vehicle_id) THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot insert into `third_party_accidents`, `vehicle_id` is referenced by `contrats` row where `active` column is set to `true`';
  END IF;
END $

DELIMITER ;


# -----------------------------------------------------------------------------
#       TRIGGER : on_create_contract
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS on_create_contract;

DELIMITER $

CREATE TRIGGER on_create_contract
BEFORE INSERT ON contracts
FOR EACH ROW
BEGIN
  IF NOT is_vehicle_owned_by(NEW.vehicle_id, NEW.id) THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot insert into `contracts`, column `contracts.assure_id` does not match `vehicles.insuree_id`';
  END IF;

  IF NEW.active THEN
    IF is_vehicle_covered(NEW.vehicle_id) THEN
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Cannot insert into `contracts` there can be only one row with column `active` set to TRUE for the same `vehicle_id`!';
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
BEFORE UPDATE ON contracts
FOR EACH ROW
BEGIN
  IF NEW.active THEN
    IF is_vehicle_covered(NEW.vehicle_id) THEN
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Cannot update `contracts` there can be only one row with column `active` set to TRUE for the same `vehicle_id`!';
    END IF;
  END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       /!\ NOT TO APPLY /!\
#       TRIGGER : on_create_token, on_udpate_token
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS on_create_token;

DELIMITER $

CREATE TRIGGER on_create_token
BEFORE INSERT ON tokens
FOR EACH ROW
  BEGIN
    CALL delete_token_linked_to_account(NEW.id, NEW.user_account_id);
  END $

DELIMITER ;


DROP TRIGGER IF EXISTS on_update_token;

DELIMITER $

CREATE TRIGGER on_update_token
BEFORE UPDATE ON tokens
FOR EACH ROW
  BEGIN
    CALL delete_token_linked_to_account(NEW.id, NEW.user_account_id);
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
  WHERE TO_SECONDS(CURRENT_TIMESTAMP) - TO_SECONDS(updated_at) >= get_token_lifetime();