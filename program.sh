#!/bin/bash


repl(){
  clj \
    -J-Dclojure.core.async.pool-size=1 \
    -X:Ripley Ripley.core/process \
    :main-ns Elsbeth.main
}

main(){
  clojure \
    -J-Dclojure.core.async.pool-size=1 \
    -M -m Elsbeth.main
}

ui(){
  npm i --no-package-lock
  mkdir -p out/ui/
  cp src/Elsbeth/index.html out/ui/index.html
  cp src/Elsbeth/style.css out/ui/style.css
}

ui_repl(){
  ui
  clj -A:Moana:ui -M -m shadow.cljs.devtools.cli clj-repl
  # (shadow/watch :ui)
  # (shadow/repl :ui)
  # :repl/quit
}

ui_release(){
  rm -rf out/ui
  ui
  clj -A:Moana:ui -M -m shadow.cljs.devtools.cli release ui
}

tag(){
  COMMIT_HASH=$(git rev-parse --short HEAD)
  COMMIT_COUNT=$(git rev-list --count HEAD)
  TAG="$COMMIT_COUNT-$COMMIT_HASH"
  git tag $TAG $COMMIT_HASH
  echo $COMMIT_HASH
  echo $TAG
}

jar(){

  rm -rf out/*.jar
  COMMIT_HASH=$(git rev-parse --short HEAD)
  COMMIT_COUNT=$(git rev-list --count HEAD)
  clojure \
    -X:Genie Genie.core/process \
    :main-ns Elsbeth.main \
    :filename "\"out/Elsbeth-$COMMIT_COUNT-$COMMIT_HASH.jar\"" \
    :paths '["src" "out/ui"]'
}

release(){
  ui_release
  jar
}

"$@"