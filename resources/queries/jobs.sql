-- :name get-job
-- :command :?
-- :result :1
SELECT * FROM jobs
LEFT JOIN media
ON jobs.pic = media.id
WHERE jobs.id=:id


-- :name put-job
-- :command :returning-execute
-- :result :1
INSERT INTO jobs
(usr, pic, title, description_short, description_full)
VALUES
(:usr, :pic, :title, :description-short, :description-full)
RETURNING id;

