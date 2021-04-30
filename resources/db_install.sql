-- make the database
create database upgraded_winner
       with
       owner = postgres
       encoding = 'UTF-8'
       tablespace = pg_default
       connection limit = -1;
