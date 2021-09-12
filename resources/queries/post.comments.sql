-- :name get-comments-of-post :*
-- :doc Get all comments associated with a post
SELECT * FROM comment_post
WHERE post=:post
/*~ LIMIT
(if (contains? params :page-size) (str "LIMIT " (:page-size params)))	
*/
/*~ 
(if (and (contains? params :page-size) 
    	 (contains? params :page-num)) 
	 	    (str "OFFSET " (* (:page-size params) (:page-num params))))
*/
