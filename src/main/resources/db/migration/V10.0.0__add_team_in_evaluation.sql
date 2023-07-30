ALTER TABLE evaluation
    ADD COLUMN team_id BIGINT;

ALTER TABLE evaluation
    ADD FOREIGN KEY (team_id) REFERENCES team (team_id);
