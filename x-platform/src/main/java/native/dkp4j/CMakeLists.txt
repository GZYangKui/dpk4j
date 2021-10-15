aux_source_directory(. SRC_DIR)
aux_source_directory(../include/dpk4j INCLUDE_DIR)

# 添加动态链接库
add_library(dkp4j STATIC ${SRC_DIR} ${INCLUDE_DIR})