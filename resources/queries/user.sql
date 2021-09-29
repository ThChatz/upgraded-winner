-- :name insert-new-user :! :n
-- :doc Creates a new account in the database
INSERT INTO public.usr 
(first_name, 
last_name, 
password, 
email,
picture,
bio,
job,
phone,
is_admin,
email_private,
bio_private,
phone_private,
job_private,
network_private)
VALUES 
(:first_name, 
:last_name, 
:password, 
:email,
:picture,
:bio,
:job,
:phone,
FALSE,
:email_private,
:bio_private,
:phone_private,
:job_private,
:network_private)

-- :name get-usr :1
-- :doc Returns user information from id
/* :require [environ.core :refer [env]]
   	    [upgraded-winner.db :refer [sql-quote]]*/
SELECT
usr.id,
usr.first_name,
usr.last_name,
usr.email,
usr.bio,
usr.job,
usr.phone,
usr.is_admin,
usr.email_private,
usr.bio_private,
usr.phone_private,
usr.job_private,
usr.network_private,
CONCAT(/*~(-> env :api-root sql-quote)~*/, '/', media.filename)
AS picture
FROM usr
LEFT JOIN media
ON media.id = usr.picture
WHERE usr.id=:id

-- :name get-users-are-connected :1
-- :doc TRUE if user1 and user2 are friends
SELECT COUNT(1) > 0 AS result FROM usr_friend
WHERE ((:user2 < :user1 AND usr=:user2 AND friend=:user1) OR
    (usr=:user1 AND friend=:user2))
