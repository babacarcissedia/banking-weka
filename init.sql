#create user "weka_banking"@"localhost" identified with mysql_native_password by "weka_banking";
#create database weka_banking charset utf8 collate utf8;
#grant all privileges on weka_banking.* to "weka_banking"@"localhost";

create table clients (
    id int auto_increment,
    first_name varchar(255),
    last_name varchar(255),
    age int,
    region varchar(255),
    child_count int unsigned,
    income int unsigned,
    sex varchar(10),
    is_married tinyint(1),
    has_save_act tinyint(1),
    has_current_act tinyint(1),
    has_mortgage tinyint(1),
    has_car tinyint(1),
    has_pep tinyint(1),
    constraint pk_clients primary key (id)
);