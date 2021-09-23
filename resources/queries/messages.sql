-- :name insert-message :!
-- :doc inserts a message
INSERT INTO conversation_message (conversation, message)
VALUES (:conversation, :message)

-- :name insert-usr-conv :!
-- :doc inserts a new conversation to a user
INSERT INTO usr_conversation (usr, conversation)
VALUES (:usr, :conversation)

-- :name get-usr-convs :?
-- :doc get the conversations of a user
--~ :require [upgraded-winner.db :refer [page]]
SELECT * FROM usr_conversation 
WHERE usr=:usr
--~ (page params)

-- :name get-conv-messages :?
-- :doc get the messages of a conversation
--~ :require [upgraded-winner.db :refer [page]]
SELECT * FROM conversation_message 
WHERE conversation=:conversation
ORDER BY time
--~ (page params)

