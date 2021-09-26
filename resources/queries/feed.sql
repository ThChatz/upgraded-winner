-- :name get-feed :? :*
SELECT id FROM post
WHERE usr=:usr OR
usr IN (SELECT usr from usr_friend WHERE friend=:usr)
ORDER BY created_at
