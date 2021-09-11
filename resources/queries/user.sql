-- :name insert-new-user :! :n
-- :doc Creates a new account in the database
INSERT INTO public.usr (username, password, email)
VALUES (:username, :password, :email)

-- :name get-usr :1
-- :doc Returns user information from id
SELECT * FROM usr
WHERE id=:id
