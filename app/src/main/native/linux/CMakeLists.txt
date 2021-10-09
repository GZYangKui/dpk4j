find_package(X11 REQUIRED)
find_package(PkgConfig REQUIRED)

#检查是否存在GTK3
pkg_check_modules(GTK3 REQUIRED gtk+-3.0)
#添加GTK3头文件进入当前程序
include_directories("${GTK3_INCLUDE_DIRS}")

aux_source_directory(. DIR_LIB_SRCS)
#aux_source_directory(../include INCLUDE_DIRS)
add_library(ltm SHARED ${DIR_LIB_SRCS})

target_link_libraries(ltm "${X11_LIBRARIES}")
target_link_libraries(ltm "${GTK3_LIBRARIES}")
