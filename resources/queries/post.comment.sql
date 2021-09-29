-- :name insert-comment :returning-execute :1
-- :doc Creates a new post in the database
INSERT INTO comment_post (usr, post, content)
VALUES (:usr, :post, :content)
RETURNING id, usr, post, content




-- :name edit-comment :! :n
-- :doc Edit contents of a comment
UPDATE comment_post
SET text=:text
WHERE id=:id AND post=:post AND usr=:usr

-- :name delete-comment :! :n
-- :doc Delete a comment
DELETE FROM post
WHERE id=:id AND post=:post AND usr=:usr
