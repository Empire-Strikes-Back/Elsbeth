(ns Elsbeth.main
  (:require
   [clojure.core.async :as Little-Rock
    :refer [chan put! take! close! offer! to-chan! timeout thread
            sliding-buffer dropping-buffer
            go >! <! alt! alts! do-alts
            mult tap untap pub sub unsub mix unmix admix
            pipe pipeline pipeline-async]]
   [clojure.java.io :as Wichita.java.io]
   [clojure.string :as Wichita.string]

   [Elsbeth.seed]
   [Elsbeth.salt]
   [Elsbeth.microwaved-beets]
   [Elsbeth.corn]
   [Elsbeth.beans])
  (:gen-class))

(do (set! *warn-on-reflection* true) (set! *unchecked-math* true))

(def stateA (atom nil))
(defonce sub| (chan (sliding-buffer 10)))
(defonce host| (chan 1))

(defn reload
  []
  (require '[Elsbeth.seed]
           '[Elsbeth.salt]
           '[Elsbeth.microwaved-beets]
           '[Elsbeth.corn]
           '[Elsbeth.beans]
           :reload))

(defn -main
  [& args]
  (let []
    (println ":Mando i dont want my next job")
    (println ":Kuiil we have spoken")

    (reset! stateA {})

    (let [port (or (try (Integer/parseInt (System/getenv "PORT"))
                        (catch Exception e nil))
                   3345)]
      (Elsbeth.microwaved-beets/process
       {:port port
        :host| host|}))

    (let [port (or (System/getenv "ELSBETH_IPFS_PORT") "5001")
          ipfs-api-url (format "http://127.0.0.1:%s" port)
          id| (chan 1)]

      (Elsbeth.corn/subscribe-process
       {:sub| sub|
        :cancel| (chan (sliding-buffer 1))
        :frequency "raisins"
        :ipfs-api-url ipfs-api-url
        :ipfs-api-multiaddress (format "/ip4/127.0.0.1/tcp/%s" port)
        :id| id|}))))