CREATE TABLE users (`user_id` INTEGER PRIMARY KEY AUTOINCREMENT
    ,`email_id` TEXT NOT NULL,
    `hashed_password` TEXT NOT NULL
    ,`role` VARCHAR(6) NOT NULL,
    UNIQUE(`email_id`));;;