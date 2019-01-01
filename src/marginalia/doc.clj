(ns marginalia.doc)


(def docs (atom []))
(def metadata (atom {}))

(defn add-doc [doc]
  (swap! docs conj doc ))

(defn reset-docs []
  (reset! docs []))

(defn add-metadata [m]
  (reset! metadata m))

(defn reset-metadata []
  (reset! metadata {}))

(defn shoot [return f]
  (assoc return :image (f)))

(defn check [return]
  {:result (boolean return)})

(defmacro doc [docstring & form]
  `(let [docstring# ~docstring
         ;; return false to indicate failure
         return# (do ~@form)
         ;; return meta :image to show an image
         image# (and (map? return#) (:image return#))
         ;; detect result which could be false
         result-present# (and (map? return#) (contains? return# :result))
         result# (and result-present# (:result return#))
         doc# {:docstring docstring#}]
     (add-doc (cond-> doc#
                image# (assoc :image image#)
                result-present# (assoc :result result#)))
     (if (or image# result#)
       (dissoc return# :image :result)
       return#)))
