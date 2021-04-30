-- :name create-db
-- :command :execute
-- :doc Creates the database
create database upgraded_winner
       with
       owner = postgres
       encoding = 'UTF-8'
       tablespace = pg_default
       connection limit = -1;

-- :name create-users-table
-- :command :execute
-- :doc Creates the `users` table
create table users (
       user_id serial primary key,
       username varchar(50) unique not null,
       password varchar(64) not null,
       email varchar(255) unique not null
);
