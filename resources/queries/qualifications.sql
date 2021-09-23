-- :name add-qualification :returning-execute :1
-- :doc Add a qualification
INSERT INTO qualifications (name)
VALUES (:name)
RETURNING id

-- :name add-user-qualification :returning-execute :n
-- :doc Add a user qualification
INSERT INTO usr_qualifications (usr, is_private, qualification)
VALUES (:usr, :is-private, :qualification)

-- :name delete-user-qualification :! :n
-- :doc Delete a user qualification
DELETE FROM usr_qualifications 
WHERE usr=:usr AND qualification=:qualification

-- :name get-qualification :? :1
-- :doc Get qualification by name
SELECT * FROM qualifications 
WHERE name=:name

-- :name get-user-qualifications :? :*
-- :doc Get all qualifications of :user
--~ :require [upgraded.winner.db :refer [page]]
SELECT * FROM usr_qualifications
WHERE usr=:usr
--~ (page params)