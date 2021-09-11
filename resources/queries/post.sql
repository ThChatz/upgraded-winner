-- :name insert-post :! :n
-- :doc Creates a new post in the database
INSERT INTO public.post (usr, media, content)
VALUES (:usr, :media, :content)

-- :name get-post :1
-- :doc Get a post from its post id
SELECT * FROM post
WHERE id=:id

-- :name edit-post :! :n
-- :doc Edit contents of a post
UPDATE post
SET text=:text
WHERE id=:id AND usr=:usr
