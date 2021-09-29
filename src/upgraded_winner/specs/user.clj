(ns upgraded-winner.specs.user
  (:require [spec-tools.core :as st]
            [spec-tools.data-spec :as ds]))

;; specs
(def user-id-spec
  (st/spec
   {:name ::user-id
    :spec pos-int?
    :reason "user-id must be a positive integer"}))

(def user-name-spec
  (st/spec
   {:name ::user-name
    :type :string
    :spec #(re-matches #"[a-zA-Z]+" %)}))

(let [email-regex #"(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])"]
  (def user-email-spec
    (st/spec
     {:name ::user-email
      :type :string
      :spec #(re-matches email-regex %)
      :reason "email is not valid"})))

(def user-password-spec
  (st/spec
   {:name ::user-password
    :spec (complement nil?)
    :reason "password is not valid"}))

(def user-phone-spec
  (st/spec
   {:name ::user-phone
    :spec #(re-matches #"\+?[0-9]+" %)
    :reason "phone must be a string of digits"}))

(def sign-up-spec
  (st/spec
   {:spec (ds/spec
           {:first_name user-name-spec
            :last_name user-name-spec
            :password user-password-spec
            :email user-email-spec
            :job (complement nil?)
            :bio (complement nil?)
            :phone user-phone-spec
            :email_private boolean?
            :bio_private boolean?
            :phone_private boolean?
            :job_private boolean?
            :network_private boolean?
            :picture pos-int?})
    :name ::sign-up
    :reason "Please fill all fields to continue"}))
