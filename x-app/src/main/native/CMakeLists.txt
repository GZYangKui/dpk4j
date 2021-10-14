cmake_minimum_required(VERSION 3.20)
project(dkg4j C)

set(CMAKE_C_STANDARD 99)

add_subdirectory(linux)

aux_source_directory(. SRC_DIR)
aux_source_directory(include INCLUDE_DIR)

add_executable(dkg4j ${SRC_DIR} ${INCLUDE_DIR} flzo.c include/flzo.h)

target_link_libraries(dkg4j ltm)