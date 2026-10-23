#!/bin/sh
set -eu
APP_HOME=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
JAR="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"
if [ ! -f "$JAR" ]; then
  echo 'Downloading verified Gradle 8.9 wrapper on first use...'
  command -v curl >/dev/null || { echo 'curl is required to bootstrap Gradle' >&2; exit 1; }
  curl --fail --location --retry 2 --output "$JAR.tmp" https://services.gradle.org/distributions/gradle-8.9-wrapper.jar
  SUM=$( (sha256sum "$JAR.tmp" 2>/dev/null || shasum -a 256 "$JAR.tmp") | cut -d ' ' -f 1 )
  [ "$SUM" = '498495120a03b9a6ab5d155f5de3c8f0d986a449153702fb80fc80e134484f17' ] || { rm -f "$JAR.tmp"; echo 'Gradle wrapper checksum mismatch' >&2; exit 1; }
  mv "$JAR.tmp" "$JAR"
fi
if [ -n "${JAVA_HOME:-}" ]; then JAVA="$JAVA_HOME/bin/java"; else JAVA=java; fi
exec "$JAVA" -cp "$JAR" org.gradle.wrapper.GradleWrapperMain "$@"
