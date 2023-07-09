drop database expensetrackerdb;
drop user expensetracker;

create user expensetracker with password 'kiz9r';
create database expensetrackerdb with template=template0 owner=expensetracker; 
\connect expensetrackerdb;

alter default privileges grant all on tables to expensetracker;
alter default privileges grant all on sequences to expensetracker;

create table et_users(
    user_id integer primary key not null,
    first_name varchar(20) not null,
    last_name varchar(20) not null,
    email varchar(30) not null,
    password text not null
);

create table et_catagories(
    catagory_id integer primary key not null,
    user_id integer not null,
    title varchar(20) not null,
    description varchar(50) not null
);

alter table et_catagories add constraint cat_user_fk foreign key(user_id) references et_users(user_id);

create table et_transactions(
    transaction_id integer primary key not null,
    catagory_id integer not null,
    user_id integer not null,
    amount numeric(10,2) not null,
    note varchar(50) not null,
    transaction_date bigint not null
);

alter table et_transactions add constraint trans_cat_fk foreign key(catagory_id) references et_catagories(catagory_id);
alter table et_transactions add constraint trans_user_fk foreign key(user_id) references et_users(user_id);


create sequence et_users_seq increment 1 start 1;
create sequence et_catagories_seq increment 1 start 1;
create sequence et_transactions_seq increment 1 start 1000;