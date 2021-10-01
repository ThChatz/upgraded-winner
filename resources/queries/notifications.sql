-- :name insert-notification :returning-execute :1
-- :doc Inserts a new notification
-- :require [upgraded-winner.db :refer [kw->enum]]
INSERT INTO notifications (target, type, origin)
VALUES (:target, /*~(-> params :type kw->enum)~*/, :origin)
RETURNING id

-- :name insert-comment-notification :! 
-- :doc Inserts a new comment notification
INSERT INTO comment_notification (id, comment_id)
VALUES (:id, :comment_id)

-- :name insert-like-notification :! 
-- :doc Inserts a new comment notification
INSERT INTO like_notification (id, post_id)
VALUES (:id, :post_id, :me)

-- :name insert-friend-req-notification :! 
-- :doc Inserts a new comment notification
INSERT INTO friend_req_notification (id, friend_id)
VALUES (:id, :friend_id)

-- :name insert-friend-notification :! 
-- :doc Inserts a new comment notification
INSERT INTO friend_accepted_notification (id, friend_id)
VALUES (:id, :friend_id)

-- :name get-notifications :?
-- :doc Gets the notifications of a user
SELECT * FROM notifications
LEFT JOIN friend_req_notification on notifications.id=friend_req_notification.id
LEFT JOIN friend_accepted_notification on notifications.id=friend_accepted_notification.id
LEFT JOIN like_notification on notifications.id=like_notification.id
LEFT JOIN comment_notification on notifications.id=comment_notification.id
WHERE usr=:usr 
ORDER BY time

