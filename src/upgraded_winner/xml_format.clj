(ns upgraded-winner.xml-format
  (:require [clojure.data.xml :refer [element emit]]
            [muuntaja.format.core :as mfc])
  (:import (java.io OutputStream)))

(declare data->xml)

(defn- kv->xml [k v]
  (cond
    (sequential? v) (element "list" {:length (count v)} (map-indexed #(element k {:index %1} %2) v))
    (map? v) (element k {} (_data->xml v))
    (or (int? v) (string? v)) (element k {} v)
    :else (element k {} (str v))))

(defn _data->xml [data & {:keys [outer-name] :or {outer-name "results"}}]
  (cond
    (map? data) (map (fn [[k v]] (kv->xml k v)) data)
    (seq? data) (map (partial kv->xml outer-name) data)
    :else (kv->xml outer-name {} data)))

(defn data->xml [data & {:keys [outer-name] :or {outer-name "results"}}]
  (element outer-name {} (_data->xml data)))

(defn- xml->str [xml]
  (-> xml
      (emit ,,, (new java.io.StringWriter))
      str))

(defn data->xml->str [data]
  (-> data
      data->xml
      xml->str))




(defn encoder [_]
  (reify
    mfc/EncodeToBytes
    (encode-to-bytes [_ data charset]
      (.getBytes
       (data->xml->str data)
       ^String charset))
    mfc/EncodeToOutputStream
    (encode-to-output-stream [_ data charset]
      (fn [^OutputStream output-stream]
        (.write output-stream
                ((.getBytes
                  (data->xml->str data)
                  ^String charset)))))))

(def xml-format
  (mfc/map->Format
   {:name "application/xml"
    :encoder [encoder]}))

