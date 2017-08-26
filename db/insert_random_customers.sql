DELIMITER $

DROP PROCEDURE insert_random_customers $
CREATE PROCEDURE insert_random_customers()
BEGIN
	DECLARE v_id INTEGER;
	DECLARE done BIT DEFAULT 0;
	DECLARE c CURSOR FOR (SELECT id FROM personnes WHERE id >= 433);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

	OPEN c;

	my_loop: LOOP
		FETCH c INTO v_id;
		SELECT v_id;
		if(done) THEN
			LEAVE my_loop;
		END IF;

		INSERT INTO assures(id, bonus_malus) values(v_id, RAND(1));
		INSERT INTO clients(id, statut, admin_id) values(v_id, 0, 252);
	END LOOP my_loop;

	CLOSE c;
END $

DELIMITER ;

DELIMITER $

CREATE FUNCTION models__get_min_id(in_name VARCHAR (255)) RETURNS INTEGER
BEGIN
  DECLARE min_id INTEGER;
  SELECT min(id) INTO min_id FROM models WHERE name = in_name;
  RETURN min_id;
END $
DELIMITER ;

UPDATE vehicles set year     := (SELECT year FROM models WHERE id = model_id);

UPDATE vehicles set model_id := (SELECT models__get_min_id(name) FROM models WHERE id = model_id);