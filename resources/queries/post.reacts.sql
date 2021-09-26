-- :name add-react :! :returning-execute
-- :doc adds a reaction of type :reaction from :usr on :post
IF NOT EXISTS (SELECT * FROM usr_react_post WHERE usr=:usr AND post=:post)
INSERT INTO usr_react_post (usr, reaction, post)
VALUES (:usr, :reaction, :post)
ELSE
UPDATE usr_react_post
SET reaction=:reaction
WHERE usr=:usr AND post=:post
RETURNING usr, react, post

-- :name delete-react :! :returning-execute
-- :doc deletes a reaction
DELETE FROM post
WHERE usr=:usr AND post=:post
RETURNING usr, post

-- :name get-reacts :? :*
-- :doc gets all reactions from :post
SELECT * from user_react_post
WHERE post=:post