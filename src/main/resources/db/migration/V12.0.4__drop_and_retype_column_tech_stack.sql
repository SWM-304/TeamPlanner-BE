alter table tech_stack
    drop column experienced_year,
    drop column experienced_month,
    modify column skill_level INT;
