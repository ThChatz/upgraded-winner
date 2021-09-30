-- :name remove-friend-req :! :n
-- :doc Removes a friend request
DELETE FROM usr_friend_req
WHERE usr=:usr AND friend=:friend

-- :name add-friend :!
-- :doc Marks the usr and friend as friends
INSERT INTO usr_friend (usr, friend)
VALUES (:usr, :friend), (:friend, :usr)
ON CONFLICT(usr, friend) DO NOTHING

-- :name add-friend-req :!
-- :doc Adds a friend request
INSERT INTO usr_friend_req (usr, friend)
VALUES (:usr, :friend)
ON CONFLICT(usr, friend) DO NOTHING

-- :name friend? :? :1
SELECT * FROM usr_friend
WHERE usr=:usr AND friend=:friend

-- :name friend-req? :? :1
SELECT * FROM usr_friend_req
WHERE usr=:usr AND friend=:friend

-- :name get-friend-reqs :? :*
SELECT * FROM usr_friend_req
WHERE friend=:usr 

-- :name get-friends :? :*
-- :doc Returns a list of friends
SELECT * FROM usr_friend
WHERE usr=:usr
