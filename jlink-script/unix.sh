#!/bin/bash

#JAVA_HOME
export JAVA_HOME="/home/yangkui/application/jdk-17"
export J_LINK_OUT_PATH="./runtime"
export JAVAFX_HOME="/home/yangkui/application/javafx17/"
if [ ! -f "${J_LINK_OUT_PATH}" ]; then
  rm -rf ${J_LINK_OUT_PATH}
fi
J_LINK_OPTIONS="--module-path ${JAVAFX_HOME} --add-modules=javafx.graphics,javafx.fxml,javafx.controls --output ${J_LINK_OUT_PATH}"

CMD="${JAVA_HOME}/bin/jlink ${J_LINK_OPTIONS}"
$CMD
$J_LINK_OUT_PATH/bin/java -jar ./xtm.jar