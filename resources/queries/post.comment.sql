-- :name insert-comment :! :n
-- :doc Creates a new post in the database
INSERT INTO comment_post (usr, post, text)
IF (SELECT COUNT(1) FROM post WHERE id=:id) > 0
   VALUES (:usr, :post, :text);
ELSE
   SELECT NULL;



-- :name edit-comment :! :n
-- :doc Edit contents of a comment
UPDATE comment_post
SET text=:text
WHERE id=:id AND post=:post AND usr=:usr

-- :name delete-comment :! :n
-- :doc Delete a comment
DELETE FROM post
WHERE id=:id AND post=:post AND usr=:usr
