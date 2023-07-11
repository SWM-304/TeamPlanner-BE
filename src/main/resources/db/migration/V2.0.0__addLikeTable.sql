DROP TABLE IF EXISTS board_like;

CREATE TABLE board_like (
                            board_like_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            BOARD_ID BIGINT,
                            MEMBER_ID BIGINT,
                            created_at DATETIME(6),
                            updated_at DATETIME(6),
                            CONSTRAINT FK_board_like_member FOREIGN KEY (MEMBER_ID) REFERENCES member (member_id)
) ENGINE=InnoDB;
