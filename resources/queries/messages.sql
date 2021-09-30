-- :name insert-message :!
-- :doc inserts a message
INSERT INTO conversation_message (usr, conversation, message)
VALUES (:usr, :conversation, :message)

-- :name insert-usr-conv :!
-- :doc inserts a new conversation to a user
INSERT INTO usr_conversation (usr, conversation)
VALUES (:usr, :conversation)

-- :name new-conv :returning-execute :1
-- :doc creates a new conversation in the database
INSERT INTO conversation DEFAULT VALUES
RETURNING conv_id AS id


-- :name get-usr-convs :?
-- :doc get the conversations of a user
SELECT * FROM usr_conversation 
WHERE usr=:usr

-- :name get-conv-messages :?
-- :doc get the messages of a conversation
-- :require [upgraded-winner.db :refer [page]]
SELECT * FROM conversation_message 
WHERE conversation=:conversation
--~ (page "time" params)

