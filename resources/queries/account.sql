-- :name insert-new-user :! :n
-- :doc Creates a new account in the database
INSERT INTO public.usr (username, password, email)
VALUES (:username, :password, :email)

-- :name get-login :1
-- :doc Returns user information from username
SELECT * FROM usr
WHERE username=:username AND password=:password