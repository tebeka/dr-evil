=======
dr-evil
=======

Dr. Evil is a "web debugger". You add Dr. Evil at a secret path in your web
application and you get a Clojure REPL on your running web application.

Usage
=====
Just include the EVIL macro inside your `defroutes`, for example::

    (defroutes app
      (GET "/" [] "Nothing here! (try /evil)")
      (EVIL "/evil")
      (route/not-found "Dude! I can't find it."))

Screenshot
==========
.. image:: https://bitbucket.org/tebeka/dr-evil/raw/tip/screenshot.png

License
=======

Copyright (C) 2010 Miki Tebeka <miki.tebeka@gmail.com>

Distributed under the Eclipse Public License, the same as Clojure.
