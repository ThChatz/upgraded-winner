-- :name get-job
-- :command :?
-- :result :1
SELECT jobs.id,
usr,
title,
description_short,
description_full, 
CONCAT(/*~(-> env :api-root sql-quote)~*/, '/', media.filename)
AS pic
FROM jobs
LEFT JOIN media
ON jobs.pic = media.id
WHERE jobs.id=:id

-- :name get-jobs
-- :command :?
-- :result :*
SELECT jobs.id,
usr,
title,
description_short,
description_full, 
CONCAT(/*~(-> env :api-root sql-quote)~*/, '/', media.filename)
AS pic
FROM jobs
LEFT JOIN media
ON jobs.pic = media.id

-- :name put-job
-- :command :returning-execute
-- :result :1
INSERT INTO jobs
(usr, pic, title, description_short, description_full)
VALUES
(:usr, :pic, :title, :description_short, :description_full)
RETURNING id;

