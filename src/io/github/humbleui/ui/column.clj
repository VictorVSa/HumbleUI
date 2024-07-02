(in-ns 'io.github.humbleui.ui)

(core/deftype+ Column []
  :extends AContainerNode
    
  protocols/IComponent
  (-measure-impl [_ ctx cs]
    (let [[_ opts _] (parse-element element)
          gap        (-> (:gap opts 0)
                       (* (:scale ctx))
                       (core/iceil))]
      (core/loopr
        [width  0
         height 0]
        [child children]
        (let [child-size (measure child ctx cs)]
          (recur
            (max width (:width child-size))
            (if (= 0 height)
              (+ height (:height child-size))
              (+ height gap (:height child-size)))))
        (core/ipoint width height))))
  
  (-draw-impl [_ ctx rect ^Canvas canvas]
    (let [[_ opts _]    (parse-element element)
          gap           (-> (:gap opts 0)
                          (* (:scale ctx))
                          (core/iceil))
          cs            (core/ipoint (:width rect) (:height rect))
          known         (for [child children]
                          (let [meta (meta (:element child))]
                            (when (= :hug (:stretch meta :hug))
                              (measure child ctx cs))))
          space         (-> (:height rect)
                          (- (transduce (keep :height) + 0 known))
                          (- (* gap (dec (count children))))
                          (max 0))
          total-stretch (transduce (keep #(:stretch (meta (:element %)))) + 0 children)]
      (loop [known    known
             children children
             height   0]
        (when-not (empty? children)
          (let [[size & known']     known
                [child & children'] children
                child-height        (long
                                      (or
                                        (:height size)
                                        (let [stretch (:stretch (meta (:element child)))]
                                          (-> space (/ total-stretch) (* stretch) (math/round)))))
                child-rect          (core/irect-xywh
                                      (:x rect)
                                      (+ (:y rect) height)
                                      (max 0 (:width rect))
                                      (max 0 child-height))]
            (draw-child child ctx child-rect canvas)
            (recur known' children' (+ height gap child-height))))))))

(defn- column-ctor [& children]
  (map->Column {}))

