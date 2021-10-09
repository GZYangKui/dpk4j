cmake_minimum_required(VERSION 3.20)
project(native C)

set(CMAKE_C_STANDARD 99)

add_library(native main.c)
add_executable(main main.c robot/robot.c robot/robot.h)