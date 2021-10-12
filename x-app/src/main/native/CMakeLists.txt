cmake_minimum_required(VERSION 3.20)
project(xtm-native C)

set(CMAKE_C_STANDARD 99)

add_subdirectory(linux)

aux_source_directory(. SRC_DIR)
aux_source_directory(include INCLUDE_DIR)

add_executable(xtm ${SRC_DIR} ${INCLUDE_DIR})

target_link_libraries(xtm ltm)