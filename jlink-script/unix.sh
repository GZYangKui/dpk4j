#!/bin/bash

#JAVA_HOME
export JAVA_HOME="/home/yangkui/application/jdk-17"
export J_LINK_OUT_PATH="./runtime"
export JAVAFX_HOME="/home/yangkui/application/javafx17/"
if [ ! -f "${J_LINK_OUT_PATH}" ]; then
  rm -rf ${J_LINK_OUT_PATH}
fi

# shellcheck disable=SC2034
J_LINK_MODULES="\
org.slf4j,\
java.naming,\
javafx.controls,\
com.dlsc.formsfx,\
org.bytedeco.javacv,\
org.bytedeco.opencv,\
org.bytedeco.ffmpeg,\
org.bytedeco.javacpp,\
ch.qos.logback.classic,\
org.bytedeco.openblas,\
org.controlsfx.controls,\
org.kordamp.ikonli.javafx,\
com.fasterxml.jackson.core,\
javafx.graphics,javafx.fxml,\
com.fasterxml.jackson.databind,\
org.bytedeco.ffmpeg.linux.x86_64,\
org.bytedeco.opencv.linux.x86_64,\
org.bytedeco.openblas.linux.x86_64"

CMD="${JAVA_HOME}/bin/jlink \
--module-path modules:${JAVAFX_HOME}  --add-modules ${J_LINK_MODULES} --output=${J_LINK_OUT_PATH}"
echo $CMD
$CMD

