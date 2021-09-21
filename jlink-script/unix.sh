#!/bin/bash

#JAVA_HOME
export JAVA_HOME="/home/yangkui/application/jdk-17"
export J_LINK_OUT_PATH="./runtime"
export JAVAFX_HOME="/home/yangkui/application/javafx-jmods-17/"
if [ ! -f "${J_LINK_OUT_PATH}" ]; then
  echo "runtime 文件已存在,执行删除操作!"
  rm -rf ${J_LINK_OUT_PATH}
fi
J_LINK_OPTIONS=" --module-path ${JAVAFX_HOME} \
--add-modules=javafx.base,javafx.graphics,javafx.fxml,javafx.controls  \
--output ${J_LINK_OUT_PATH}"

${JAVA_HOME}/bin/jlink ${J_LINK_OPTIONS}