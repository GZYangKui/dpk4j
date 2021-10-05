#!/bin/bash
MODULE_PATH="bin:lib"
LIB_PATH="bin/*:lib/*"
runtime/bin/java -cp ${LIB_PATH} --module-path ${MODULE_PATH} --add-modules ALL-MODULE-PATH -m cn.navclub.xtm.app