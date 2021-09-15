-- tables
create table if not exists usr (
       id serial primary key,
       first_name varchar(50) not null,
       last_name varchar(50) not null,
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

create table if not exists post (
       id serial primary key,
       usr integer,
       created_at timestamp default Now(),
       media integer,
       content text
);

create table if not exists user_uploads (
       id serial primary key
);


create table if not exists comment_post (
       usr integer,
       post integer,
       comment serial primary key
);

create table if not exists usr_react_post (
       usr integer,
       react integer,
       post integer
);
