-- :name get-login :1
-- :doc Returns user information from email and password
SELECT * FROM usr
WHERE email=:email AND password=:password
