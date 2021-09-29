-- :name insert-post :! :n
-- :doc Creates a new post in the database
INSERT INTO public.post (usr, media, content)
VALUES (:usr, :media, :content)

-- :name get-post :1
-- :doc Get a post from its post id
SELECT 
post.id,
post.usr,
post.target_timeline,
post.created_at,
post.last_edit_at,
post.media,
post.content,
(SELECT COUNT(reaction) FROM usr_react_post WHERE reaction='like' AND post=:id) AS like_count,
(SELECT COUNT(reaction) FROM usr_react_post WHERE reaction='dislike' AND post=:id) AS dislike_count,
(SELECT COUNT(reaction) FROM usr_react_post WHERE reaction='love' AND post=:id) AS love_count,
(SELECT COUNT(id) FROM comment_post WHERE post=:id) AS comment_count
/*~
(if (contains? params :usr) ", (SELECT reaction from usr_react_post WHERE post=:id AND usr=:usr)")
~*/
FROM post
WHERE id=:id

-- :name edit-post :! :n
-- :doc Edit contents of a post
UPDATE post
SET text=:text, last_edit_at=Now()
WHERE id=:id AND usr=:usr

-- :name delete-post :! :n
-- :doc Delete a post
DELETE FROM post
WHERE id=:id AND usr=:usr
