-- :name remove-friend-req :! :n
-- :doc Removes a friend request
IF (SELECT COUNT(1) FROM usr WHERE id=:usr OR id=:friend) = 2
    DELETE FROM usr_friend_req
    WHERE usr=:usr AND friend=:friend
ELSE 
    NULL;

-- :name add-friend :!
-- :doc Marks the usr and friend as friends
IF (SELECT COUNT(1) FROM usr WHERE id=:usr OR id=:friend) = 2

    IF (SELECT COUNT(1) 
            FROM usr_friend_req 
            WHERE usr=:friend AND  friend=:usr) > 0
        INSERT INTO usr_friend (usr, friend)
        VALUES (:usr, :friend), (:friend, :usr)
	WHERE NOT EXISTS (SELECT * FROM usr_friend WHERE usr=:usr AND friend=:friend)
    ELSE
        INSERT INTO usr_friend_req (usr, friend)
        VALUES (:usr, :friend)
	WHERE NOT EXISTS (SELECT * FROM usr_friend WHERE usr=:usr AND friend=:friend)
ELSE 
    NULL;
    
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
