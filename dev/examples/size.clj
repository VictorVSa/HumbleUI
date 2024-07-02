(ns examples.size
  (:require
    [examples.util :as util]
    [io.github.humbleui.core :as core]
    [io.github.humbleui.paint :as paint]
    [io.github.humbleui.signal :as signal]
    [io.github.humbleui.ui :as ui]))

(ui/defcomp ui []
  [ui/valign {:position 0.5}
   [ui/vscrollbar
    [ui/halign {:position 0.5}
     (let [fill (paint/fill 0xFFAACCFF)]
       (util/table
         "Force child’s width"
         [ui/size {:width 50}
          [ui/rect {:paint fill}
           [ui/halign {:position 0.5}
            "abc"]]]
    
         "Force child’s height"
         [ui/size {:height 50}
          [ui/rect {:paint fill}
           [ui/valign {:position 0.5}
            "abc"]]]
      
         "Force both width and height"
         [ui/size {:width 50 :height 50}
          [ui/rect {:paint fill}
           [ui/center
            "abc"]]]
         
         "Does not grow on overflow"
         [ui/size {:width 10 :height 20}
          [ui/rect {:paint fill}
           [ui/center
            "abc"]]]
         
         "Accepts functions of parent size"
         [ui/size {:width (fn [cs] (* 0.25 (:width cs)))}
          [ui/rect {:paint fill}
           "abc"]]
    
         "Child can be omitted"
         [ui/rect {:paint fill}
          [ui/size {:width 50 :height 50}]]
         ))]]])