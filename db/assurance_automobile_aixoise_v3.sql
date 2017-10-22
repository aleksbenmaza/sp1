DROP DATABASE IF EXISTS aaa;

CREATE DATABASE aaa;
USE aaa;

# -----------------------------------------------------------------------------
#       TABLE : makes
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS makes(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) NULL,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
 );

# -----------------------------------------------------------------------------
#       TABLE : models
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS models(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) NULL  ,
  year INTEGER(4) NULL,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  make_id BIGINT NOT NULL,
  FOREIGN KEY(make_id) REFERENCES makes(id) ON DELETE CASCADE

 );

# -----------------------------------------------------------------------------
#       TABLE : years
# -----------------------------------------------------------------------------

CREATE TABLE years(
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  value SMALLINT
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
#       TABLES : vehicles, vehicles_h
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS vehicles(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  model_id BIGINT NOT NULL,
  year_id BIGINT NOT NULL,
  vin_number CHAR(17) NOT NULL UNIQUE,
  value FLOAT,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY(model_id, year_id) REFERENCES models__years(model_id, year_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS vehicles_h(
  vehicle_id BIGINT NOT NULL,
  insuree_id BIGINT,
  contract_id BIGINT,
  h_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  registration_number CHAR(7),
  purchase_date DATE,
  value FLOAT,
  PRIMARY KEY (vehicle_id, h_date),
  FOREIGN KEY(vehicle_id) REFERENCES vehicles(id) ON DELETE CASCADE,
  FOREIGN KEY(insuree_id) REFERENCES insurees(id) ON DELETE SET NULL
);

# -----------------------------------------------------------------------------
#       TABLE : tokens
# -----------------------------------------------------------------------------

CREATE TABLE tokens (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  value VARCHAR(64),
  old_value VARCHAR(64),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  api_server VARCHAR(15)
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
  FOREIGN KEY(contract_id) REFERENCES contracts(id) ON DELETE CASCADE,
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
  FOREIGN KEY(plain_sinister_type_id) REFERENCES plain_sinister_types(id) ON DELETE CASCADE
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
   FOREIGN KEY(insurance_id) REFERENCES insurances(id) ON DELETE CASCADE
);

# -----------------------------------------------------------------------------
#       TABLE : plain_sinister_types__insurances,
#               accidents__accidents,
#               tokens__user_accounts
# -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS types_sinisters__insurances(
  plain_sinister_type_id BIGINT,
  insurance_id BIGINT,
  rate float,
  PRIMARY KEY(plain_sinister_type_id, insurance_id),
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
);

CREATE TABLE IF NOT EXISTS models__years(
  model_id BIGINT,
  year_id BIGINT,
  PRIMARY KEY(model_id, year_id),
  FOREIGN KEY(model_id) REFERENCES models(id) ON DELETE CASCADE,
  FOREIGN KEY(year_id) REFERENCES years(id) ON DELETE CASCADE
);



  # -----------------------------------------------------------------------------
#       VIEW : deductibles
# -----------------------------------------------------------------------------

DROP VIEW IF EXISTS deductibles;

CREATE VIEW deductibles(damage_id, value) AS
SELECT DISTINCT si.id, compute_deductible(i.id, IF(sp.id IS NULL, v.value, sp.amount))
FROM sinisters si LEFT JOIN destructions d   ON si.id          = d.id
                  LEFT JOIN spoilages    sp  ON si.id          = sp.id
                  LEFT JOIN contracts    c   ON si.contract_id = c.id
                  LEFT JOIN vehicles     v   ON c.vehicle_id   = c.id
                  LEFT JOIN insurances   i   ON c.insurance_id = i.id
WHERE si.status = 0 AND (d.id IS NOT NULL OR sp.id IS NOT NULL);

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
  (NULL, 1, 'Incendie', NULL),
  (NULL, 2, 'Vol', NULL),
  (NULL, 3, 'Bris de glace', NULL);

# -----------------------------------------------------------------------------
#       FUNCTION : allocate_auto_increment
# -----------------------------------------------------------------------------

DROP PROCEDURE IF EXISTS allocate_auto_increment;

DELIMITER $

CREATE PROCEDURE allocate_auto_increment(IN in_table_name VARCHAR(64), IN in_number SMALLINT, OUT out_next_id BIGINT)
BEGIN
  DECLARE message_text VARCHAR(64);
  SET message_text := CONCAT('Table `', in_table_name,'` does not have AUTO_INCREMENT defined.');
  SET @allocate_auto_increment.in_table_name := in_table_name;
  SET @allocate_auto_increment.number := in_number;
  SELECT auto_increment INTO out_next_id FROM information_schema.tables WHERE auto_increment IS NOT NULL AND table_name = in_table_name LIMIT 1;
  IF out_next_id IS NULL THEN
    SIGNAL SQLSTATE '42000' SET MESSAGE_TEXT = message_text;
  END IF;
  SET @allocate_auto_increment.stmt := CONCAT('ALTER TABLE ', in_table_name,' AUTO_INCREMENT=', out_next_id + in_number);
  PREPARE query FROM @allocate_auto_increment.stmt;
  EXECUTE query;
  DROP PREPARE query;
END $

DELIMITER ;
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

  IF in_amount BETWEEN v_min_deductible AND v_max_deductible THEN
    SET result := in_amount;

  ELSEIF in_amount > v_max_deductible THEN
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
  FROM vehicles__insurees
  WHERE vehicle_id = in_id AND insuree_id = in_insuree_id;
  RETURN result;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       PROCEDURE : id_exists
# -----------------------------------------------------------------------------

DROP PROCEDURE IF EXISTS id_exists;

DELIMITER $

CREATE PROCEDURE id_exists(IN in_table_name VARCHAR(255))
BEGIN
  SET @id_exists.var_stm := CONCAT(
      'SELECT IF(COUNT(*), 1, 0)
       INTO @aaa_is_present_out_result
       FROM ', in_table_name, '
       WHERE id=? LIMIT 1'
  );
  PREPARE query FROM @id_exists.var_stm;
  EXECUTE query USING @id_exists.in_id;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       FUNCTION : id_exists
# -----------------------------------------------------------------------------

DROP FUNCTION IF EXISTS id_exists;

DELIMITER $

CREATE FUNCTION id_exists(in_table_name VARCHAR(255), in_id BIGINT) RETURNS BIT
BEGIN
  DECLARE id_exists BIT;
  SET @id_exists.in_id := in_id;
  SET @id_exists.var_stm := CONCAT(
      'SELECT IF(COUNT(*), 1, 0)
       INTO @aaa_is_present_out_result
       FROM ', in_table_name, '
     WHERE id=? LIMIT 1'
  );
  PREPARE query FROM @id_exists.var_stm;
  EXECUTE query USING @id_exists.in_id;
  DROP PREPARE query;
  SET id_exists := @aaa_is_present_out_result;
  SET @aaa_is_present_out_result := NULL;
  SET @id_exists.var_stm := NULL;
  SET @id_exists.in_id := NULL;
  RETURN id_exists;
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
  DECLARE id_exists BIT DEFAULT 0;
  SET @id_exists.in_id := in_id;
  CALL id_exists('insurees');
  SET id_exists := @id_exists.out_result;
  IF id_exists THEN
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
  DECLARE id_exists BIT DEFAULT 0;
  SET @id_exists.in_id := in_id;
  CALL id_exists('third_parties');
  SET id_exists := @id_exists.out_result;
  IF id_exists THEN
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
  DECLARE id_exists BIT DEFAULT 0;
  SET @id_exists.in_id := in_id;
  CALL id_exists('customers');
  SET id_exists := @id_exists.out_result;
  IF id_exists THEN
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
  DECLARE id_exists BIT DEFAULT 0;
  SET @id_exists.in_id := in_id;
  CALL id_exists('accidents');
  SET id_exists := @id_exists.out_result;
  IF id_exists THEN
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
  DECLARE id_exists BIT DEFAULT 0;
  SET @id_exists.in_id := in_id;
  CALL id_exists('plain_sinisters');
  SET id_exists := @id_exists.out_result;
  IF id_exists THEN
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
  DECLARE id_exists BIT DEFAULT 0;
  SET @id_exists.in_id := in_id;
  CALL id_exists('destructions');
  SET id_exists := @id_exists.out_result;
  IF id_exists THEN
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
  DECLARE id_exists BIT DEFAULT 0;
  SET @id_exists.in_id := in_id;
  CALL id_exists('spoilages');
  SET id_exists := @id_exists.out_result;
  IF id_exists THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot insert into `destructions`, primary key found in `spoilages';
  END IF;
END $

DELIMITER ;


# -----------------------------------------------------------------------------
#       TRIGGER : managers___before_insert
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS managers___before_insert;

DELIMITER $

CREATE TRIGGER managers___before_insert
BEFORE INSERT ON managers
FOR EACH ROW
BEGIN
  DECLARE id_exists BIT DEFAULT 0;
  SET @id_exists.in_id := NEW.id;

  CALL id_exists('insurees');

  SET id_exists := @id_exists.out_result;

  IF id_exists THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cannot insert into `managers`, primary key found in `insurees` !';
  END IF;

  CALL id_exists('experts');
  SET id_exists := @id_exists.out_result;

  IF id_exists THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cannot insert into `managers`, primary key found in `experts` !';
  END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : experts___before_insert
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS experts___before_insert;

DELIMITER $

CREATE TRIGGER experts___before_insert
BEFORE INSERT ON experts
FOR EACH ROW
BEGIN
  DECLARE id_exists BIT DEFAULT 0;
  SET @id_exists.in_id := NEW.id;

  CALL id_exists('insurees');

  SET id_exists := @id_exists.out_result;

  IF id_exists THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cannot insert into `managers`, primary key found in `insurees` !';
  END IF;

  CALL id_exists('managers');

  SET id_exists := @id_exists.out_result;

  IF id_exists THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cannot insert into `managers`, primary key found in `managers` !';
  END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : insurees___before_insert
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS insurees___before_insert;

DELIMITER $

CREATE TRIGGER insurees___before_insert
BEFORE INSERT ON insurees
FOR EACH ROW
BEGIN

  SET @id_exists.in_id := NEW.id;

  CALL id_exists('managers');

  IF @id_exists.out_result THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cannot insert into `insurees`, primary key found in `managers`!';
  END IF;

  CALL id_exists('experts');

  IF @id_exists.out_result THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cannot insert into `insurees`, primary key found in `experts`!';
  END IF;
END $

DELIMITER ;


# -----------------------------------------------------------------------------
#       TRIGGER : customers___before_insert
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS customers___before_insert;

DELIMITER $

CREATE TRIGGER customers___before_insert
BEFORE INSERT ON customers
FOR EACH ROW
BEGIN
  SET @id_exists.in_id := NEW.id;

  CALL id_exists('third_parties');
  IF @id_exists.out_result THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cannot insert into `clients`, primary key found in `tiers` !';
  END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : third_parties___before_insert
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS third_parties___before_insert;

DELIMITER $

CREATE TRIGGER third_parties___before_insert
BEFORE INSERT ON third_parties
FOR EACH ROW
BEGIN
  SET @id_exists.in_id := NEW.id;

  CALL id_exists('customers');
  IF @id_exists.out_result THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cannot insert into `third_parties`, primary key found in `customers` !';
  END IF;

END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : accidents___before_insert
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS accidents___before_insert;

DELIMITER $

CREATE TRIGGER accidents___before_insert
BEFORE INSERT ON accidents
FOR EACH ROW
BEGIN
  CALL check_if_not_in_plain_sinisters(NEW.id);
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : customer_accidents___before_insert
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS customer_accidents___before_insert;

DELIMITER $

CREATE TRIGGER customer_accidents___before_insert
BEFORE INSERT ON customer_accidents
FOR EACH ROW
BEGIN
  SET @id_exists.in_id := NEW.id;

  CALL id_exists('third_party_accidents');

  IF @id_exists.in_id THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot insert into `customer_accidents`, `id` already referenced in `third_party_accidents`';
  END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : third_party_accidents___before_insert
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS third_party_accidents___before_insert;

DELIMITER $

CREATE TRIGGER third_party_accidents___before_insert
BEFORE INSERT ON third_party_accidents
FOR EACH ROW
BEGIN
  SET @id_exists.in_id := NEW.id;

  CALL id_exists('customer_accidents');
  IF @id_exists.out_result THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot insert into `third_party_accidents`, `id` already referenced in `customer_accidents`';
    END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : plain_sinisters___before_insert
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS plain_sinisters___before_insert;

DELIMITER $

CREATE TRIGGER plain_sinisters___before_insert
BEFORE INSERT ON plain_sinisters
FOR EACH ROW
BEGIN
  CALL check_if_not_in_accidents(NEW.id);
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : destructions___before_insert
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS destructions___before_insert;

DELIMITER $

CREATE TRIGGER destructions___before_insert
BEFORE INSERT ON destructions
FOR EACH ROW
BEGIN
  CALL check_if_not_in_spoilages(NEW.id);
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : spoilages___before_insert
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS spoilages___before_insert;

DELIMITER $

CREATE TRIGGER spoilages___before_insert
BEFORE INSERT ON spoilages
FOR EACH ROW
BEGIN
  CALL check_if_not_in_destructions(NEW.id);
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : vehicles__insurees___after_update
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS vehicles__insurees___after_update;

DELIMITER $

CREATE TRIGGER vehicles__insurees___after_update
AFTER UPDATE ON vehicles__insurees
FOR EACH ROW
BEGIN
  DECLARE var_value FLOAT;

  IF  OLD.insuree_id IS NOT NULL
  AND OLD.insuree_id != NEW.insuree_id
  AND is_vehicle_covered(NEW.vehicle_id) THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot update `vehicles`, column `vehicle_id` found in `contract` !';
  END IF;

  SELECT value
  INTO var_value
  FROM vehicles
  WHERE id = OLD.vehicle_id;

  IF OLD.registration_number != NEW.registration_number
  OR OLD.insuree_id != NEW.insuree_id THEN
    INSERT INTO vehicles_h
    VALUES(OLD.vehicle_id, OLD.insuree_id, NULL, NULL, OLD.registration_number, OLD.purchase_date, var_value);
  END IF;
END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : vehicles___after_update
# -----------------------------------------------------------------------------
DROP TRIGGER IF EXISTS vehicles___after_update;

DELIMITER $

CREATE TRIGGER vehicles___after_update
AFTER UPDATE ON vehicles
FOR EACH ROW
  BEGIN
    DECLARE var_insuree_id BIGINT;
    DECLARE var_registration_number VARCHAR(255);
    DECLARE var_purchase_date DATE;

    SELECT insuree_id, registration_number, purchase_date
    INTO var_insuree_id, var_registration_number, var_purchase_date
    FROM vehicles__insurees
    WHERE vehicle_id = OLD.id;

    IF OLD.value != NEW.value THEN
      INSERT INTO vehicles_h
      VALUES(OLD.id, var_insuree_id, NULL, NULL, var_registration_number, var_purchase_date, OLD.value);
    END IF;
  END $

DELIMITER ;

# -----------------------------------------------------------------------------
#       TRIGGER : sinisters___before_insert
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS sinisters___before_insert;

DELIMITER $

CREATE TRIGGER sinisters___before_insert
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
#       TRIGGER : third_party_accidents___before_insert
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS third_party_accidents___before_insert;

DELIMITER $

CREATE TRIGGER third_party_accidents___before_insert
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
#       TRIGGER : contracts___before_insert
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS contracts___before_insert;

DELIMITER $

CREATE TRIGGER contracts___before_insert
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
#       TRIGGER : contracts___before_update
# -----------------------------------------------------------------------------

DROP TRIGGER IF EXISTS contracts___before_update;

DELIMITER $

CREATE TRIGGER contracts___before_update
BEFORE UPDATE ON contracts
FOR EACH ROW
BEGIN
  DECLARE var_insuree_id INTEGER;
  DECLARE var_value FLOAT;
  DECLARE var_registration_number CHAR(7);
  DECLARE var_purchase_date DATE;

  IF NEW.active THEN
    IF is_vehicle_covered(NEW.vehicle_id) THEN
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Cannot update `contracts` there can be only one row with column `active` set to TRUE for the same `vehicle_id`!';
    END IF;
  ELSEIF !NEW.active AND OLD.active THEN
    SELECT insuree_id, registration_number, purchase_date, value
    INTO   var_insuree_id, var_registration_number, var_purchase_date, var_value
    FROM   vehicles_h
    WHERE  vehicle_id = NEW.vehicle_id
      AND h_date = (
        SELECT MAX(h_date)
        FROM vehicles_h
        WHERE vehicle_id = NEW.vehicle_id LIMIT 1
      );
    INSERT INTO vehicles_h
    VALUES(OLD.vehicle_id, var_insuree_id, NULL, NULL, var_registration_number, var_purchase_date, var_value);
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
    DECLARE var_user_account_id BIGINT;
    SELECT user_account_id FROM tokens__user_accounts
    WHERE token_id = OLD.id;
    CALL delete_token_linked_to_account(NEW.id, var_user_account_id);
  END $

DELIMITER ;


DROP TRIGGER IF EXISTS on_update_token;

DELIMITER $

CREATE TRIGGER on_update_token
BEFORE UPDATE ON tokens
FOR EACH ROW
  BEGIN
    DECLARE var_user_account_id BIGINT;
    SELECT user_account_id FROM tokens__user_accounts
    WHERE token_id = OLD.id;
    CALL delete_token_linked_to_account(NEW.id, var_user_account_id);
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