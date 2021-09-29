-- :name remove-friend-req :! :n
-- :doc Removes a friend request
IF (SELECT COUNT(1) FROM usr WHERE id=:usr OR id=:friend) = 2
    DELETE FROM usr_friend_req
    WHERE usr=:usr AND friend=:friend
ELSE 
    NULL;

-- :name add-friend :!
-- :doc Marks the usr and friend as friends
INSERT INTO usr_friend (usr, friend)
VALUES (:usr, :friend), (:friend, :usr)
WHERE NOT EXISTS (SELECT * FROM usr_friend WHERE usr=:usr AND friend=:friend)


-- :name add-friend-req :!
-- :doc Adds a friend request
INSERT INTO usr_friend_req (usr, friend)
VALUES (:usr, :friend)
WHERE NOT EXISTS (SELECT * FROM usr_friend WHERE usr=:usr AND friend=:friend OR friend=:usr AND usr=:friend)

-- :name friend? :? :1
SELECT * FROM usr_friend
WHERE usr=:usr AND friend=:friend

-- :name get-friends :? :*
-- :doc Returns a list of friends (pageified)
SELECT * FROM 
WHERE usr=:usr
/*~ LIMIT
(if (contains? params :page-size) (str "LIMIT " (:page-size params)))	
*/
/*~ 
(if (and (contains? params :page-size) 
    	 (contains? params :page-num)) 
	 	    (str "OFFSET " (* (:page-size params) (:page-num params))))
*/
