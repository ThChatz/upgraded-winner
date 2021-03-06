-- :name insert-new-user :! :n
-- :doc Creates a new account in the database
INSERT INTO public.usr (username, password, email)
VALUES (:username, :password, :email)

-- :name get-login :1
-- :doc Returns user information from email and password
SELECT * FROM usr
WHERE email=:email AND password=:password

-- :name get-usr :1
-- :doc Returns user information from id
SELECT * FROM usr
WHERE id=:id
