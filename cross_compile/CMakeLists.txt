cmake_minimum_required(VERSION 3.10)
project(mymath)

set(CMAKE_CXX_STANDARD 14)

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -std=gnu99")

# Android 5.0 以上需要在此处设置 PIE
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -fPIE")
set(CMAKE_EXE_LINKER_FLAGS "${CMAKE_EXE_LINKER_FLAGS} -fPIE -pie")

# 配置使用 NDK Standalone Toolchain 编译
set(NDK_STANDALONE_TOOLCHAIN /Users/vic/Developer/ndk_toolchain)
set(CMAKE_SYSTEM_NAME Android)
set(CMAKE_SYSTEM_VERSION 21)
set(CMAKE_C_COMPILER ${NDK_STANDALONE_TOOLCHAIN}/bin/clang)
set(CMAKE_CXX_COMPILER ${NDK_STANDALONE_TOOLCHAIN}/bin/clang++)
set(CMAKE_FIND_ROOT_PATH ${NDK_STANDALONE_TOOLCHAIN})
set(CMAKE_RANLIB ${NDK_STANDALONE_TOOLCHAIN}/bin/arm-linux-androideabi-ranlib)
#set(CMAKE_C_COMPILER_RANLIB ${NDK_STANDALONE_TOOLCHAIN}/bin/arm-linux-androideabi-ranlib)
#set(CMAKE_CXX_COMPILER_RANLIB ${NDK_STANDALONE_TOOLCHAIN}/bin/arm-linux-androideabi-ranlib)
#set(CMAKE_AR ${NDK_STANDALONE_TOOLCHAIN}/bin/arm-linux-androideabi-ar)

# 使用 NDK 提供的头文件
add_definitions("--sysroot=${NDK_STANDALONE_TOOLCHAIN}/sysroot")

add_library(mymath STATIC mymath.h mymath.cpp)