
(ns sockso-repl.print
    (:require [clojure.string :as string]))

(defn error
    "Print an error message"
    [message]
    (println (format "ERROR: %s" message)))

(defn prompt
    "Prints the shell prompt"
    []
    (print "sockso=> ")
    (flush))

(defn welcome
    "Prints the welcome message"
    []
    (println "Welcome to the Sockso REPL")
    (println "Type 'help' for commands")
    (println ""))

(defn- format-music-item
    "Print a music item"
    [{:keys [id name type]}]
    (format "#%s%s %s" 
        (or type "") id name))

(defn music-items
    [lst type]
    (doseq [item lst]
        (println (format-music-item 
            (assoc item :type type)))))

(defn artists
    [lst]
    (music-items lst "ar"))

(defn help
    "Prints help information"
    []
    (println "List of commands:")
    (println "")
    (println "\tconnect host=XXX")
    (println "\tserver")
    (println "\tsearch query=XXX")
    (println "\tartists")
    (println "\tplaylist")
    (println "\tplay id=XXX")
    (println "\texit")
    (println ""))

(defn server
    "Print server info"
    [host]
    (println (format "Server: %s" host)))

(defn search-results
    "Print a vector of search results"
    [results]
    (doseq [item results]
        (println (format-music-item item))))

(defn playlist
    "Print the current playlist"
    [playlist]
    (doseq [item playlist]
        (println (format "#tr%s %s" 
            (item :id)
            (item :name)))))

(defn artist-album
    "Print an artists album"
    [{:keys [id name]}]
    (println (format "\tAlbum: %s (#al%s)" name id)))

(defn artist
    "Print an artists info"
    [{:keys [id name albums]}]
    (println (format "Artist: %s (#ar%s)" name id))
    (doseq [album albums]
        (artist-album album)))

