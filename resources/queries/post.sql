-- :name insert-new-post :! :n
-- :doc Creates a new post in the database
INSERT INTO public.post (usr, media, content)
VALUES (:usr, :media, :content)