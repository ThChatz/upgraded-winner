-- :name insert-new-admin :! :n
-- :doc Creates a new admin account in the database
/* :require [buddy.core.hash :refer [sha256]]
            [buddy.core.codecs :refer [bytes->hex]]
	    [upgraded-winner.db :refer [sql-quote]]*/
INSERT INTO public.usr 
(first_name, 
password, 
email,
is_admin,
email_private,
bio_private,
phone_private,
job_private,
network_private)
VALUES 
(:name,
/*~(-> params :password sha256 bytes->hex sql-quote)~*/,
:email,TRUE,TRUE,TRUE,TRUE,TRUE,TRUE)

