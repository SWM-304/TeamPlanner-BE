DROP TABLE IF EXISTS member_team;

CREATE TABLE member_team (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             MEMBER_ID BIGINT,
                             TEAM_ID BIGINT,
                             role VARCHAR(50),
                             FOREIGN KEY (MEMBER_ID) REFERENCES member (MEMBER_ID),
                             FOREIGN KEY (TEAM_ID) REFERENCES team (team_id)
)engine=InnoDB;

