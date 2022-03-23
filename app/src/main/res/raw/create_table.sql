CREATE TABLE users (`user_id` INT IDENTITY(1, 1) NOT NULL PRIMARY KEY CLUSTERED
    ,`email_id` TEXT,`hashed_password` TEXT
    ,`role` VARCHAR(6));;;