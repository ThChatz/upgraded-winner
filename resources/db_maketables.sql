-- tables
create table if not exists usr (
       id serial primary key,
       username varchar(50) unique not null,
       password varchar(64) not null,
       email varchar(255) unique not null
);

create table if not exists usr_friend (
       usr integer,
       friend integer
);

create table if not exists usr_blocked (
       usr integer,
       blocked integer
);

create table if not exists conversation_message (
       conversation integer,
       message text
);

create table if not exists conversation (
       conv_id serial primary key     
);

create table if not exists usr_conversation (
       usr integer,
       conversation integer
);
