-- :name insert-notification :! 
-- :doc Inserts a new notification
INSERT INTO notifications (usr, type, pic)
VALUES (:usr, :type, :pic)

-- :name insert-comment-notification :! 
-- :doc Inserts a new comment notification
INSERT INTO comment_notification (id, comment_id)
VALUES (:id, :comment_id)

-- :name insert-like-notification :! 
-- :doc Inserts a new comment notification
INSERT INTO like_notification (id, post_id, friend_id)
VALUES (:id, :post_id, :friend_id)

-- :name insert-friend-req-notification :! 
-- :doc Inserts a new comment notification
INSERT INTO friend_req_notification (id, friend_id)
VALUES (:id, :friend_id)

-- :name insert-friend-req-accepted-notification :! 
-- :doc Inserts a new comment notification
INSERT INTO friend_req_accepted_notification (id, friend_id)
VALUES (:id, :friend_id)

-- :name get-notifications :?
-- :doc Gets the notifications of a user
--~ :require [upgraded-winner.db :refer [page]]
SELECT * FROM notifications
LEFT JOIN friend_req_notification on notifications.id=friend_req_notification.id
LEFT JOIN friend_req_accepted_notification on notifications.id=friend_req_accepted_notification.id
LEFT JOIN like_notification on notifications.id=like_notification.id
LEFT JOIN comment_notification on notifications.id=comment_notification.id
WHERE usr=:usr 
ORDER BY time
--~ (page params)