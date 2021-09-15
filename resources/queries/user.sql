-- :name insert-new-user :! :n
-- :doc Creates a new account in the database
INSERT INTO public.usr (first_name, last_name, password, email)
VALUES (:first-name, :last-name, :password, :email)

-- :name get-usr :1
-- :doc Returns user information from id
SELECT * FROM usr
WHERE id=:id
