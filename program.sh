#!/bin/bash

repl(){
  npm i --no-package-lock
  mkdir -p out/jar/ui/
  cp src/Elsbeth/index.html out/jar/ui/index.html
  cp package.json out/jar/package.json
  clj -A:Moana:main:ui -M -m shadow.cljs.devtools.cli clj-repl
  # (shadow/watch :main)
  # (shadow/watch :ui)
  # (shadow/repl :main)
  # :repl/quit
}

shadow(){
  # watch release
  npm i --no-package-lock
  mkdir -p out/jar/ui/
  cp src/Elsbeth/index.html out/jar/ui/index.html
  cp package.json out/jar/package.json
  clj -A:Moana:main:ui -M -m shadow.cljs.devtools.cli $1 ui main
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
  rm -rf out
  shadow release
  COMMIT_HASH=$(git rev-parse --short HEAD)
  COMMIT_COUNT=$(git rev-list --count HEAD)
  echo Elsbeth-$COMMIT_COUNT-$COMMIT_HASH.zip
  cd out/jar
  zip -r ../Elsbeth-$COMMIT_COUNT-$COMMIT_HASH.zip ./ && \
  cd ../../
}

release(){
  jar
}


"$@"