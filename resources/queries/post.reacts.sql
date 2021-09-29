-- :name add-react :returning-execute :1
-- :doc adds a reaction of type :reaction from :usr on :post
-- :require [upgraded-winner.db :refer [kw->enum]]
INSERT INTO usr_react_post (usr, reaction, post)
VALUES (:usr, /*~(-> params :reaction kw->enum)~*/, :post)
ON CONFLICT (usr, post) DO 
UPDATE SET reaction=/*~(-> params :reaction kw->enum)~*/
RETURNING usr, reaction, post

-- :name delete-react :! :returning-execute
-- :doc deletes a reaction
DELETE FROM post
WHERE usr=:usr AND post=:post
RETURNING usr, post

-- :name get-reacts :? :*
-- :doc gets all reactions from :post
SELECT * from user_react_post
WHERE post=:post