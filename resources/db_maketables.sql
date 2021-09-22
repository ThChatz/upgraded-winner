DO $$ BEGIN
      CREATE TYPE reaction_t AS ENUM ('like', 'dislike', 'love');
EXCEPTION
	WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
      CREATE TYPE media_type AS ENUM ('photo', 'video', 'audio');
EXCEPTION
	WHEN duplicate_object THEN null;
END $$;


-- tables
create table if not exists usr (
       id serial primary key,
       first_name varchar(50) not null,
       last_name varchar(50) not null,
       password varchar(64) not null,
       email varchar(255) unique not null,
       picture int,
       bio text not null,
       job varchar(50) not null,
       phone varchar(20) not null,
       is_admin boolean not null default false,
       email_private bool not null default true,
       bio_private bool not null default true,
       phone_private bool not null default true,
       job_private bool not null default true,
       network_private bool not null default true
);

create table if not exists qualifications (
       id serial primary key,
       name text
);

create table if not exists usr_qualifications (
       usr integer,
       is_private bool not null,
       qualification integer
);

create table if not exists usr_friend (
       usr integer,
       friend integer
);

create table if not exists usr_friend_req (
       usr integer,
       created_at timestamp default Now(), 
       friend integer
);

-- create table if not exists usr_blocked (
--        usr integer,
--        blocked integer
-- );

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
       target_timeline integer,
       created_at timestamp default Now(),
       last_edit_at timestamp,
       media integer,
       content text
);

create table if not exists comment_post (
       usr integer,
       post integer,
       comment serial primary key,
       created_at timestamp default Now(),
       content text
);



create table if not exists usr_react_post (
       usr integer,
       reaction reaction_t,
       post integer
);



create table if not exists media (
       id serial primary key,
       type media_type not null,
       content_type not null,
       filename varchar(50)
);


create table if not exists jobs (
       id serial primary key,
       usr integer,
       pic int,
       title varchar(50),
       description_short varchar(100),
       description_full text
);

create table if not exists job_qualification (
       job integer,
       qualification integer
);
