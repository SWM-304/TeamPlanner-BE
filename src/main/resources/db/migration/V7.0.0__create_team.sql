drop table if exists team;
CREATE TABLE team (
                      team_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                      team_name VARCHAR(255),
                      team_size BIGINT,
                      team_capacity BIGINT,
                      team_leader VARCHAR(255),
                      RECRUITMENT_ID BIGINT,
                      start_date DATE,
                      end_date DATE,
                      created_at DATETIME(6),
                      updated_at DATETIME(6),
                      FOREIGN KEY (RECRUITMENT_ID) REFERENCES recruitment (recruitment_id)
) ENGINE=InnoDB;

