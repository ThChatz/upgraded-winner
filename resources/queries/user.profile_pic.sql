-- :name add-profile-pic :! :n
-- :doc Creates a new account in the database
UPDATE user
SET picture=:pic-id
WHERE id=:id
RETURNING picture
