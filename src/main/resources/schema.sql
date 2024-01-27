CREATE TABLE employee
(
    id varchar(11) NOT NULL,
    name varchar(100) NOT NULL,
    address varchar(100) DEFAULT NULL,
    email varchar(100) DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE vacation_type
(
    vacation_id int GENERATED ALWAYS AS IDENTITY
                                        PRIMARY KEY
                                        NOT NULL,
    type varchar(100) NOT NULL
);

CREATE TABLE department
(
    department_id int GENERATED ALWAYS AS IDENTITY
                                          PRIMARY KEY
                                          NOT NULL,
    name_of_department varchar(45) NOT NULL
);

CREATE TABLE users
(
    user_id int GENERATED ALWAYS AS IDENTITY
                                    PRIMARY KEY
                                    NOT NULL,
    department_id int NOT NULL,
    name varchar(100) NOT NULL,
    surname varchar(100) NOT NULL,
    marca varchar(100) DEFAULT NULL,
    role varchar(100) NOT NULL,
    FOREIGN KEY (department_id) REFERENCES department(department_id)
);

CREATE TABLE vacation_form
(
    form_id int GENERATED ALWAYS AS IDENTITY
                                    PRIMARY KEY
                                    NOT NULL,
    user_id int NOT NULL,
    vacation_id int NOT NULL,
    data_start date NOT NULL,
    data_stop date NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (vacation_id) REFERENCES vacation_type(vacation_id)
);