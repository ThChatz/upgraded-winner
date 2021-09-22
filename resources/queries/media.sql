-- :name add-media
-- :command :returning-execute
-- :result :1
-- :doc Add new media file to the database; returns the id
INSERT INTO media (type)

VALUES (
--~ (str \' (name (:type params)) \' "::media_type")
)
RETURNING id

-- :name update-filename :! :1
-- :doc Changes the filename of the specified media 
UPDATE media
SET filename=:filename
WHERE id=:id

-- :name get-media :? :1
-- :doc Returns the media of the provided id and type(optional)
SELECT * FROM media
--~ (str "WHERE id=" (:id params) (if (contains? params :type) (str " AND '" (name (:type params)) \' "::media_type") ""))
