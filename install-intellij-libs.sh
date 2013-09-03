#!/bin/sh

INTELLIJ_HOME=$1
INTELLIJ_VERSION=12.0.4

if [ -z "$INTELLIJ_HOME" ]
then
  echo "Please provide the path to Intellij home directory. For example: install-intellij-libs.sh /Applications/Nika-IU-111.228.app"
  exit 1
fi

if [ ! -d "$INTELLIJ_HOME" ]
then
  echo "Directory does not exist: $INTELLIJ_HOME"
  exit 1
fi

echo 'Installing Intellij artifacts to Maven local repository'
echo "Intellij home: $INTELLIJ_HOME"

mvn install:install-file -Dfile="$INTELLIJ_HOME"/lib/openapi.jar -DgroupId=com.intellij -DartifactId=openapi -Dversion=$INTELLIJ_VERSION -Dpackaging=jar
mvn install:install-file -Dfile="$INTELLIJ_HOME"/lib/util.jar -DgroupId=com.intellij -DartifactId=util -Dversion=$INTELLIJ_VERSION -Dpackaging=jar
mvn install:install-file -Dfile="$INTELLIJ_HOME"/lib/extensions.jar -DgroupId=com.intellij -DartifactId=extensions -Dversion=$INTELLIJ_VERSION -Dpackaging=jar
mvn install:install-file -Dfile="$INTELLIJ_HOME"/lib/annotations.jar -DgroupId=com.intellij -DartifactId=annotations -Dversion=$INTELLIJ_VERSION -Dpackaging=jar
mvn install:install-file -Dfile="$INTELLIJ_HOME"/lib/idea.jar -DgroupId=com.intellij -DartifactId=idea -Dversion=$INTELLIJ_VERSION -Dpackaging=jar
mvn install:install-file -Dfile="$INTELLIJ_HOME"/lib/trove4j.jar -DgroupId=net.sf.trove4j -DartifactId=trove4j -Dversion=$INTELLIJ_VERSION-idea -Dpackaging=jar
