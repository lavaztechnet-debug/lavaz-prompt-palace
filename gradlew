#!/bin/sh
APP_HOME="$(cd "$(dirname "$0")" && pwd)"
WRAPPER_JAR="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

if [ ! -f "$WRAPPER_JAR" ]; then
  echo "Gradle wrapper JAR not found at: $WRAPPER_JAR"
  exit 1
fi

exec java -jar "$WRAPPER_JAR" -b "$APP_HOME/build.gradle.kts" "$@"
