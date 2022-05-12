(ns Elsbeth.main
  (:require
   [clojure.core.async :as Little-Rock
    :refer [chan put! take! close! offer! to-chan! timeout
            sliding-buffer dropping-buffer
            go >! <! alt! alts! do-alts
            mult tap untap pub sub unsub mix unmix admix
            pipe pipeline pipeline-async]]
   [clojure.string :as Wichita.string]
   [cljs.core.async.impl.protocols :refer [closed?]]
   [cljs.core.async.interop :refer-macros [<p!]]
   [goog.string.format :as format]
   [goog.string :refer [format]]
   [goog.object]
   [cljs.reader :refer [read-string]]

   [sci.core :as Batty.core]
   
   [Elsbeth.drawing]
   [Elsbeth.seed]
   [Elsbeth.raisins]
   [Elsbeth.microwaved-beets]
   [Elsbeth.corn]
   [Elsbeth.beans]))


(defonce os (js/require "os"))
(defonce fs (js/require "fs"))
(defonce path (js/require "path"))
(defonce express (js/require "express"))
(set! (.-AbortController js/global) (.-AbortController (js/require "node-abort-controller")))
(defonce IPFSHttpClient (js/require "ipfs-http-client"))
(defonce IPFSdCtl (js/require "ipfsd-ctl"))
(defonce OrbitDB (js/require "orbit-db"))
(defonce GoIPFS (js/require "go-ipfs"))

(println :ipfs IPFSdCtl)

(defonce Batty-context (Batty.core/init {:namespaces {'foo.bar {'x 1}}}))

(defonce ^:const PORT 3000)
(def server (express))
(def api (express.Router.))

(.get api "/Little-Rock" (fn [request response]
                           (go
                             (<! (timeout 1000))
                             (.send response (str {})))))

(.get api "/Batty" (fn [request response]
                     (go
                       (<! (timeout 1000))
                       (.send response (str (Batty.core/eval-string* Batty-context (.. request -query -eval)))))))

(.use server (.static express "ui"))
(.use server "/api" api)

(.get server "*" (fn [request response]
                   (.sendFile response (.join path js/__dirname  "ui" "index.html"))))

(defn -main []
  (go
    (let [complete| (chan 1)]
      (.listen server PORT (fn [] (put! complete| true)))
      (<! complete|)
      (println (format "i Jedi plagues me - on http://localhost:%s" PORT))
      (println "i dont want my next job")
      (println "Kuiil has spoken"))))


(comment

  (let [ipfsd (<p! (.createController IPFSdCtl
                                      (clj->js {"ipfsHttpModule" IPFSHttpClient
                                                "ipfsBin" (.path GoIPFS)
                                                "ipfsOptions" {}})))
        id (<p! (.. ipfs -api (id)))]
    (println id))

  (let [ipfs (.create IPFSHttpClient "http://127.0.0.1:5001")
        orbitdb (<p!
                 (->
                  (.createInstance
                   OrbitDB ipfs
                   (clj->js
                    {"directory" (.join path (.homedir os) ".Elsbeth" "orbitdb")}))
                  (.catch (fn [ex]
                            (println ex)))))]
    (println (.. orbitdb -identity -id)))

  ;
  )