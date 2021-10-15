cmake_minimum_required(VERSION 3.20)

project(jnidkg4j C)

set(CMAKE_C_STANDARD 99)

# 添加子路径
add_subdirectory(dkp4j)

aux_source_directory(. SRC_DIRS)
aux_source_directory(include INCLUDE_DIR)

# 查询JNI依赖
find_package(JNI REQUIRED)

include_directories(${JNI_INCLUDE_DIRS})

add_library(jnidkp4j SHARED ${SRC_DIRS} ${INCLUDE_DIR} include/FileArchive.h)

add_executable(main ${SRC_DIRS} ${INCLUDE_DIR})

target_link_libraries(main dkp4j)
target_link_libraries(jnidkp4j dkp4j)
target_link_libraries(main ${JNI_LIBRARIES})