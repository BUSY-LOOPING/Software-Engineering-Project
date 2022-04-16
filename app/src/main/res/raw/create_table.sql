CREATE TABLE users (`user_id` INTEGER PRIMARY KEY AUTOINCREMENT
    ,`email_id` TEXT NOT NULL,
    `hashed_password` TEXT NOT NULL
    ,`role` VARCHAR(15) NOT NULL,
    `signed_in` BOOL DEFAULT `0` NOT NULL,
    `name` TEXT,
    `address` TEXT,
    `phone_no` VARCHAR(15),
    UNIQUE(`email_id`));;;

CREATE TABLE booking(`booking_id` TEXT PRIMARY KEY NOT NULL,
    `price` REAL NOT NULL,
    `no_tickets` INTEGER NOT NULL,
    `discount` REAL,
    `tax` REAL,
    `event` INTEGER NOT NULL,
    `booked_by` INTEGER NOT NULL,
    FOREIGN KEY(`booked_by`) REFERENCES users(`user_id`),
    FOREIGN KEY(`event`) REFERENCES events(`event_id`));;;

CREATE TABLE events(`event_id` INTEGER PRIMARY KEY AUTOINCREMENT,
    `price` REAL NOT NULL,
    `no_seats` INTEGER NOT NULL,
    `event_name` TEXT NOT NULL,
    `event_type` TEXT NOT NULL,
    `small_description` TEXT NUL,
    `full_description` TEXT,
    `date_n_time` TEXT NOT NULL,
    `venue` TEXT NOT NULL,
    `confirmed` BOOL DEFAULT `1` NOT NULL,
    `photo_path` TEXT,
    `added_by` INTEGER NOT NULL,
    FOREIGN KEY(`added_by`) REFERENCES users(`user_id`));;;
