-- :name add-media :! :n
-- :doc Add new media file to the database; returns the id
INSERT INTO media
(media_type)
VALUES (:media_type)
RETURNING id
