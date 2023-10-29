-- V2__Add_TeamStateEnum_Column.sql

-- Add the new column
ALTER TABLE team
    ADD team_state_enum VARCHAR(255);
