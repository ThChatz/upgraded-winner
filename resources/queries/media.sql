-- :name add-media
-- :command :returning-execute
-- :result :1
-- :doc Add new media file to the database; returns the id
-- :require [upgraded-winner.db :refer [kw->enum]]
INSERT INTO media (type)

VALUES (
--~ (kw->enum (:type params))
)
RETURNING id

-- :name update-filename :! :1
-- :doc Changes the filename of the specified media 
UPDATE media
SET filename=:filename
WHERE id=:id

-- :name get-media :? :1
-- :doc Returns the media of the provided id and type(optional)
-- :require [upgraded-winner.db :refer [kw->enum]]
SELECT * FROM media
WHERE id=:id AND filename IS NOT NULL
/*~
(if (contains? params :type) (str " AND type=" (kw->enum (:type params))))
~*/

