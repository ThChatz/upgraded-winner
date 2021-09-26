-- :name get-login :? :1
-- :doc Returns user information from email and password
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
media.filename AS picture
FROM usr
LEFT JOIN media
ON media.id = usr.picture
WHERE email=:email AND password=:password
