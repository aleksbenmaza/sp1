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
DROP PROCEDURE insert_random_vehicles $
CREATE PROCEDURE insert_random_vehicles()
BEGIN
	DECLARE v_id INTEGER;
	DECLARE v_insert_count INTEGER DEFAULT 200;
	DECLARE v_insuree_id INTEGER;
	DECLARE v_year_id INTEGER;
	DECLARE v_model_id INTEGER;
	DECLARE v_random_int INTEGER;

	my_loop: LOOP
		IF !v_insert_count THEN
			LEAVE my_loop;
		END IF;
		SELECT id INTO v_random_int FROM insurees GROUP BY id ORDER BY FLOOR(1 + RAND() * MAX(ID)) LIMIT 1;
		IF v_random_int % 3 <> 0 AND v_random_int % 5 <> 0 THEN
	    SELECT id INTO v_insuree_id FROM insurees GROUP BY id ORDER BY FLOOR(1 + RAND() * MAX(id)) LIMIT 1;
	  END IF;
	  SELECT id INTO v_model_id FROM models GROUP BY id ORDER BY FLOOR(1 + RAND() * MAX(id)) LIMIT 1;
	  SELECT year_id INTO v_year_id FROM models__years WHERE model_id=v_model_id GROUP BY year_id ORDER BY FLOOR(1 + RAND() * MAX(year_id)) LIMIT 1;
		INSERT INTO vehicles(model_id, insuree_id, value, year_id) VALUES(v_model_id, v_insuree_id, (500 + RAND() * 2400), v_year_id);
		SET v_insert_count := v_insert_count - 1;
	END LOOP my_loop;
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


#FULL JOIN
SELECT insuree_id, model_id, COUNT(*) AS vehicle_count
FROM vehicles v
  RIGHT JOIN insurees i ON insuree_id=i.id
  RIGHT JOIN models m   ON model_id=m.id
GROUP BY i.id, m.id
UNION
SELECT insuree_id, model_id, COUNT(*) AS vehicle_count
FROM vehicles v
  LEFT JOIN insurees i ON insuree_id=i.id
    LEFT JOIN models m   ON model_id=m.id
GROUP BY i.id, m.id;

SELECT insuree_id, model_id, count(*) AS vehicle_count
FROM vehicles v
  RIGHT JOIN models m   ON model_id=m.id
  RIGHT JOIN insurees i ON insuree_id=i.id
WHERE
  insuree_id IS NOT NULL OR
   model_id   IS NOT NULL
GROUP BY insuree_id, model_id;