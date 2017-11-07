DROP TABLE IF EXISTS sequences;
CREATE TABLE sequences AS
SELECT table_name, auto_increment AS value
FROM information_schema.tables
WHERE table_schema='aaa' AND auto_increment IS NOT NULL;


DROP PROCEDURE IF EXISTS create_auto_increment_triggers;
DELIMITER $
CREATE PROCEDURE create_auto_increment_triggers()
BEGIN
  DECLARE v_table_name VARCHAR(255);
	DECLARE done BIT DEFAULT 0;
	DECLARE c CURSOR FOR (SELECT table_name FROM sequences);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

	OPEN c;

  my_loop: LOOP
		FETCH c INTO v_table_name;
		if(done) THEN
			LEAVE my_loop;
		END IF;
    SET @create_auto_increment_triggers.stm := CONCAT(
    'CREATE TRIGGER ',  v_table_name, '___auto_increment ',
    'BEFORE INSERT ON ', v_table_name, ' ',
    'FOR EACH ROW ',
    'BEGIN ',
      'DECLARE v_sequence_value INTEGER; ',
      'SELECT value INTO v_sequence_value ',
      'FROM sequences ',
      'WHERE table_name=', v_table_name, '; ',
      'IF NEW.id IS NULL THEN ',
        'SET NEW.id := v_sequence_value; ',
        'UPDATE sequences ',
        'SET value := value + 1 ',
        'WHERE table_name=' , v_table_name, '; '
      'ELSE IF NEW.id > v_sequence_value THEN ',
        'UPDATE sequences ',
        'SET value := NEW.id + 1; ',
      'END IF; ',
    'END; '
    );
    PREPARE query FROM @create_auto_increment_triggers.stm;
    EXECUTE query;
    DEALLOCATE PREPARE query;
    SET @create_auto_increment_triggers.stm := NULL;
	END LOOP my_loop;
	CLOSE c;
END $

DELIMITER ;

CALL create_auto_increment_triggers;

SET @v_table_name := 'persons';
SET @stm := CONCAT('CREATE TRIGGER ',  @v_table_name, '___auto_increment ',
    'BEFORE INSERT ON ', @v_table_name, ' ',
    'FOR EACH ROW ',
    'BEGIN ',
      'DECLARE v_sequence_value INTEGER; ',
      'SELECT value INTO v_sequence_value ',
      'FROM sequences ',
      'WHERE table_name=', @v_table_name, '; ',
      'IF NEW.id IS NULL THEN ',
        'SET NEW.id := v_sequence_value; ',
        'UPDATE sequences ',
        'SET value := value + 1; ',
      'ELSE IF NEW.id > v_sequence_value THEN ',
        'UPDATE sequences ',
        'SET value := NEW.id + 1; ',
      'END IF; ',
    'END; '
    );