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
javafx.controls,\
com.dlsc.formsfx,\
org.bytedeco.javacv,\
org.bytedeco.opencv,\
org.bytedeco.ffmpeg,\
org.bytedeco.javacpp,\
org.bytedeco.openblas,\
org.controlsfx.controls,\
org.kordamp.ikonli.javafx,\
javafx.graphics,javafx.fxml,\
org.bytedeco.ffmpeg.linux.x86_64,\
org.bytedeco.opencv.linux.x86_64,\
org.bytedeco.openblas.linux.x86_64"

CMD="${JAVA_HOME}/bin/jlink --module-path libs:${JAVAFX_HOME}  --add-modules ${J_LINK_MODULES} --output=${J_LINK_OUT_PATH}"
echo $CMD
$CMD
$J_LINK_OUT_PATH/bin/java -jar ./xtm.jar