
(ns sockso-repl.core
    (:gen-class)
    (:use [sockso-repl.urls :as urls])
    (:use [clojure.java.io :as io])
    (:use [clojure.string :as string]))

(def server (atom {
    :host "127.0.0.1:4444"
}))

(defn server-set
    "Set a server parameter"
    [key value]
    (swap! server assoc (keyword key) value))

(defn server-get
    "Fetch a server parameter"
    [key]
    ((keyword key) @server))

(defmulti command :name)

(defmacro defcommand [name comment args & body]
    `(defmethod command ~name
        [{args# :args}]
            (let [{:keys [~@args]} args#] 
                (do ~@body))))

(defcommand :connect
    "Connect to a new server"
    [host]
    (server-set :host host)
    (println (format "Server changed to %s" host)))

(defn format-search-result
    "Format a search result"
    [res]
    (format "#%s %s" 
        (get res "id")
        (get res "name")))

(defcommand :search
    "Search the current server"
    [query]
    (let [url (format "http://%s/json/search/%s" (server-get :host) query)]
        (println (join "\n"
            (map format-search-result (urls/url-get-json url))))))

(defcommand :default
    "Print error on known command"
    []
    (println "ERROR: Unknown command"))

(defn parse-command-arg
    "Parse a command arg pair into a map with :name and :value keys"
    [pair]
    (let [[name value] (string/split pair #"=")]
        (hash-map (keyword name) value)))

(defn parse-command-args
    "Parses name/value argument pairs into a map" 
    [lst]
    (reduce #(merge %1 (parse-command-arg %2)) {} lst))

(defn parse-command-str
    "Parse command string into map of name and arguments"
    [cmd]
    (let [[name & args] (string/split cmd #"\s+")]
        {:name (keyword name)
         :args (parse-command-args args)}))

(defn print-prompt
    "Prints the shell prompt"
    []
    (print "=> ")
    (flush))

(defn run-repl
    "Read commands and execute them"
    []
    (loop [] 
        (print-prompt)
        (let [line (read-line)]
            (if (> (.length line) 0)
                (command (parse-command-str line)))
        (recur))))

(defn -main[]
    (run-repl))

