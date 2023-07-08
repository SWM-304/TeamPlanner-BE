CREATE TABLE board_like (
                           board_like_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                           BOARD_ID BIGINT,
                           MEMBER_ID BIGINT,
                           created_at datetime(6),
                           updated_at datetime(6)
) engine=InnoDB;

ALTER TABLE board_like
    ADD CONSTRAINT FK_board_like_member
        FOREIGN KEY (MEMBER_ID) REFERENCES member (member_id);
