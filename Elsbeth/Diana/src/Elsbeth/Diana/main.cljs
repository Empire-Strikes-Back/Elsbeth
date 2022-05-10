(ns Elsbeth.Diana.main
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
   
   [Elsbeth.Diana.seed]
   [Elsbeth.Diana.salt]
   [Elsbeth.Diana.microwaved-beets]
   [Elsbeth.Diana.corn]
   [Elsbeth.Diana.beans]))

(defonce fs (js/require "fs"))
(defonce path (js/require "path"))
(defonce express (js/require "express"))

(defonce Batty-context (Batty.core/init {:namespaces {'foo.bar {'x 1}}}))

(defonce ^:const PORT 8080)
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

#_(.get server "*" (fn [request response]
                   (.sendFile response (.join path js/__dirname  "ui" "index.html"))))

(.listen server PORT
         (fn []
           (js/console.log (format "server started on %s" PORT))))

(defn -main []
  (println "i Jedi plagues me")
  (println "i dont want my next job")
  (println "Kuiil has spoken"))