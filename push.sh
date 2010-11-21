#!/bin/bash

lein jar
lein pom
scp pom.xml dr-evil-*.jar clojars@clojars.org:
