(in-ns 'io.github.humbleui.ui)

(util/deftype+ Padding [^:mut left
                        ^:mut top
                        ^:mut right
                        ^:mut bottom]
  :extends AWrapperNode  

  (-should-measure? [_ ctx cs]
    (some fn? [left top right bottom]))

  (-measure-impl [_ ctx cs]
    (let [left-px    (dimension left cs ctx)
          top-px     (dimension top cs ctx)
          right-px   (dimension right cs ctx)
          bottom-px  (dimension bottom cs ctx)
          child-cs   (util/ipoint
                       (-> (:width cs) (- left-px) (- right-px) (max 0))
                       (-> (:height cs) (- top-px) (- bottom-px) (max 0)))
          child-size (measure child ctx child-cs)]
      (util/ipoint
        (-> (:width child-size) (+ left-px) (+ right-px))
        (-> (:height child-size) (+ top-px) (+ bottom-px)))))
  
  (-draw-impl [_ ctx bounds viewport ^Canvas canvas]
    (let [left-px      (dimension left bounds ctx)
          top-px       (dimension top bounds ctx)
          right-px     (dimension right bounds ctx)
          bottom-px    (dimension bottom bounds ctx)
          width        (-> (:width bounds) (- left-px) (- right-px) (max 0))
          height       (-> (:height bounds) (- top-px) (- bottom-px) (max 0))
          child-bounds (util/irect-xywh
                         (+ (:x bounds) left-px)
                         (+ (:y bounds) top-px)
                         width
                         height)]
      (draw child ctx child-bounds viewport canvas)))
  
  (-reconcile-opts [this ctx new-element]
    (let [opts    (parse-opts new-element)
          left'   (or (:left opts)   (:horizontal opts) (:padding opts) 0)
          top'    (or (:top opts)    (:vertical opts)   (:padding opts) 0)
          right'  (or (:right opts)  (:horizontal opts) (:padding opts) 0)
          bottom' (or (:bottom opts) (:vertical opts)   (:padding opts) 0)]
      (when (or
              (not= left left')
              (not= top top')
              (not= right right')
              (not= bottom bottom'))
        (set! left left')
        (set! top top')
        (set! right right')
        (set! bottom bottom')
        (invalidate-size this)))))

(defn- padding-ctor [opts child]
  (map->Padding {}))
