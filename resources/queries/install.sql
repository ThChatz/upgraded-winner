-- :name insert-new-admin :! :n
-- :doc Creates a new admin account in the database
/* :require [buddy.core.hash :refer [sha256]]
            [buddy.core.codecs :refer [bytes->hex]]*/
INSERT INTO public.usr 
(first_name, 
last_name, 
password, 
email,
picture,
bio,
job,
phone,
is_admin,
email_private,
bio_private,
phone_private,
job_private,
network_private)
VALUES 
(:name,"",/*~(str \" (-> params :password sha256 bytes->hex) \")~*/,
:email,-1,"","","",TRUE,TRUE,TRUE,TRUE,TRUE,TRUE)

