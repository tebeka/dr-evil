(ns dr.evil
  (:use compojure.core)
  (:use [clojure.stacktrace :only (print-stack-trace)])
  (:use [clojure.contrib.json :only (json-str)]))

; FIXME: Give use ability to change namespace
(defn wrapped-expr [expr]
  (str "(binding [*print-length* 100] " expr ")"))

(defn eval-expr [expr]
  (try
    (let [result (load-string (wrapped-expr expr))]
      { :result (with-out-str (println result)) :error false })
    (catch Exception e
      (let [result (with-out-str (print-stack-trace e))]
        { :result result :error true }))))

(defn evil [params]
  (json-str (eval-expr (params "expr"))))

(declare html)

(defn EVIL [path]
  (GET path [] (html path))
  (POST path {params :params} (evil params)))

; HTML goes here (this is at the end since it's long).
; We embed the HTML in the clojure file so we won't have to muck around with
; serving files from unknown locations
(defn html [path] 
  (format "<html>
    <head>
        <title>Dr. Evil Web Debugger</title>
        <style>
            body {
                margin: 50px;
                font-family: Georgia, serif;
            }
            #log {
                font-family: Monospace;
                border: 3px solid black;
                width: 100%;
                height: 500px;
                overflow: auto;
            }
            #entry {
                width: 80em;
            }
            div.error {
                color: red;
            }
            div.result {
                white-space: pre;
            }
        </style>
    </head>
    <body>
        <h1>Dr. Evil Web Debugger</h1>
        <div id=\"log\"></div>
        <input id=\"entry\" /> <button id=\"submit\">Run</button>
    </body>
    <script
        src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.4.4/jquery.min.js\">
    </script>
    <script
        src=\"http://flesler-plugins.googlecode.com/files/jquery.scrollTo-1.4.2-min.js\">
    </script>
    <script>
        var HISTORY = [];
        var INDEX = 0;
        var ENTER_KEY = 13;
        var UP_KEY = 38;
        var DOWN_KEY = 40;

        function append_log(div) {
            $('#log').append(div);
            $('#log').scrollTo(div);
        }

        function on_result(data) {
            var div = $('<div class=\"result\" />');
            if (data.error) {
                div.addClass('error');
                if (data.result.length == 0) {
                    data.result = \"Unknown Error!\";
                }
            }
            div.text(data.result);
            append_log(div);
        }

        function on_run() {
            var expr = $.trim($('#entry').val());
            if (expr.length == 0) {
                return;
            }

            HISTORY.push(expr);

            var div = $('<div/>').text('=> ' + expr);
            append_log(div);
            $.ajax({
                type: 'POST',
                url: '%s',
                data: { \"expr\" : expr },
                success: on_result,
                dataType: 'json'
            });
            $('#entry').val('');
        }

        function on_history(direction) {
            if (HISTORY.length == 0) {
                return;
            }

            INDEX = (INDEX + direction);
            if (INDEX < 0) {
                INDEX = HISTORY.length - 1;
            } else if (INDEX >= HISTORY.length) {
                INDEX = 0;
            }

            $('#entry').val(HISTORY[INDEX]);
        }

        function on_entry_keyup(e) {
            if (e.keyCode == ENTER_KEY) {
                on_run();
            } else if (e.keyCode == UP_KEY) {
                on_history(-1);
            } else if (e.keyCode == DOWN_KEY) {
                on_history(1);
            }
        }

        $(document).ready(function() {
            $('#submit').click(on_run);
            $('#entry').keyup(on_entry_keyup).val('').focus();
        });
    </script>
</html>" path))

