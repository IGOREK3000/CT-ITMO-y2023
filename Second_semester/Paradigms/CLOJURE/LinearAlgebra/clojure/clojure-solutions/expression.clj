(require '[clojure.math :as math])

(def constant constantly)
(defn variable [v] (fn [values] (get values v)))
(defn anyArgsOp [f] (fn [& args] (fn [values] (apply f (mapv #(% values) args)))))
(defn binOp [f] (fn [x y] (fn [values] (f (x values) (y values)))))
(defn unOp [f] (fn [x] (fn [values] (f (x values)))))

(def add (binOp +))
(def subtract (binOp -))
(def multiply (binOp *))
(defn div [x y] (/ (double x) (double y)))
(def divide (binOp div))
(def sinh (unOp math/sinh))
(def negate (unOp -))
(def cosh (unOp math/cosh))

(def functionOperators {'+        add
                        '-        subtract
                        '*        multiply
                        '/        divide
                        'negate   negate
                        'sinh     sinh
                        'cosh     cosh
                        'variable variable
                        'constant constant})

(definterface MainExpression
  (^Number evaluate [vars])
  (^String toStringPostfix []))

(deftype Const [value]
  MainExpression
  (evaluate [this vars] value)
  (toStringPostfix [this] (str value))
  Object
  (toString [this] (str value)))

(deftype Var [name]
  MainExpression
  (evaluate [this vars] (get vars (clojure.string/lower-case (first name))))
  (toStringPostfix [this] (.toString this))
  Object
  (toString [this] name))
(defn abs [a] (cond
                (< a 0) (- a)
                :else a))
(def log (fn [a b] (div (math/log (abs b)) (math/log (abs a)))))
(def opFromSymbol {"+"      +
                   "-"      -
                   "*"      *
                   "/"      div
                   "negate" -
                   "pow"    math/pow
                   "log"    log
                   "min"    min
                   "max"    max})

(deftype AbstractOperation [sign operands]
  MainExpression
  (evaluate [this vars] (apply (get opFromSymbol sign) (mapv #(.evaluate % vars) operands)))
  (toStringPostfix [this] (str "(" (clojure.string/join " " (mapv #(.toStringPostfix %) operands)) " " sign ")"))
  Object
  (toString [this] (str "(" sign " " (clojure.string/join " " operands) ")"))
  )


(defn Constant [value] (Const. value))
(defn Variable [name] (Var. name))
(defn makeOperation [sign] (fn [& operands] (AbstractOperation. sign operands)))

(def Add (makeOperation "+"))
(def Subtract (makeOperation "-"))
(def Multiply (makeOperation "*"))
(def Divide (makeOperation "/"))
(def Pow (makeOperation "pow"))
(def Log (makeOperation "log"))
(def Negate (makeOperation "negate"))
(def Min (makeOperation "min"))
(def Max (makeOperation "max"))

(defn evaluate [expression vars] (.evaluate expression vars))
(defn toString [expression] (.toString expression))

(def objectOperators {'+        Add
                      '-        Subtract
                      '*        Multiply
                      '/        Divide
                      'negate   Negate
                      'pow      Pow
                      'log      Log
                      'variable Variable
                      'constant Constant
                      'min      Min
                      'max      Max})

(defn parseMethod [methodMap]
  (fn [expr-str] (letfn [(parse-inner [expr]
                           (cond
                             (symbol? expr) ((get methodMap 'variable) (str expr))
                             (number? expr) ((get methodMap 'constant) expr)
                             :else (apply (get methodMap (first expr)) (map parse-inner (rest expr)))
                             ))]
                   (parse-inner (read-string expr-str)))))

(def parseObject (parseMethod objectOperators))
(def parseFunction (parseMethod functionOperators))

(load-file "parser.clj")

(def *all-chars (mapv char (range 0 128)))
(def *letter (+char (apply str (filter #(Character/isLetter %) *all-chars))))
(def *digit (+char (apply str (filter #(Character/isDigit %) *all-chars))))
(def *space (+char (apply str (filter #(Character/isWhitespace %) *all-chars))))
(def *ws (+ignore (+star *space)))
(def *openingBracket (+ignore (+char "(")))
(def *closingBracket (+ignore (+char ")")))
(def *identifier (+str (+seqf cons *letter (+star (+or *letter *digit)))))
(def *variable (+map Variable (+map clojure.string/join (+plus (+char "xyzXYZ")))))
(defn *word [word] (apply (partial +seqf str) (mapv #(+char (str %)) (seq word))))
(defn +ignore-nil [parser] (+map #(if (nil? %) 'ignore %) parser))
(defn sign [s tail] (if (#{\- \+} s) (cons s tail) tail))

(def *number (+map (comp Constant #(Double/parseDouble %) clojure.string/join) (+seqf sign (+opt (+char "-+")) (+plus (+or *digit (+char "."))))))
(declare *brackets)
(def *operand (+or *number *variable (delay *brackets)))
(def *operands (+plus (+seqn 0 *ws *operand *ws)))
(def *operation (+seq *ws (+or (*word "negate") (*word "min") (*word "max") (+char "+-*/")) *ws))
(defn makeOp [opAndOp] (let [operands (butlast opAndOp)
                             operation (last opAndOp)]
                         (apply (get objectOperators (symbol (str operation))) operands)))

(def *operandsAndOperation (let [opAndOp (+seqf #(concat %1 %2) *operands *operation)]
                             (+map makeOp opAndOp)))
(def *brackets (+seqn 0 *ws (+or (+seqn 0 *openingBracket *operandsAndOperation *closingBracket) *variable *number) *ws))

(defn parseObjectPostfix [expression] (-value (*brackets expression)))
(defn toStringPostfix [expression] (.toStringPostfix expression))
