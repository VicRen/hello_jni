# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.10.2)

# Declares and names the project.

project("importlibs")

# Project configuration.
set(LIBWEBRTC_INCLUDE_PATH "" CACHE STRING "libwebrtc include path")
set(LIBWEBRTC_BINARY_PATH "" CACHE STRING "libwebrtc binary path")

set(LIBWEBRTC_INCLUDE_PATH /Users/vic/Developer/workspace/hello_jni/import_libs/app/deps/webrtc/src)

if(NOT LIBWEBRTC_INCLUDE_PATH)
    message(FATAL_ERROR "LIBWEBRTC_INCLUDE_PATH not provided")
endif()

set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY ${PROJECT_BINARY_DIR}/lib)
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${PROJECT_BINARY_DIR}/bin)
set(CMAKE_INSTALL_PREFIX ${PROJECT_SOURCE_DIR}/out)
message(STATUS "CMAKE_CURRENT_SOURCE_DIR = " ${CMAKE_CURRENT_SOURCE_DIR})
message(STATUS "CMAKE_HOST_SYSTEM = " ${CMAKE_HOST_SYSTEM})
message(STATUS "CMAKE_HOST_SYSTEM_NAME = " ${CMAKE_HOST_SYSTEM_NAME})

#set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -std=gnu99")
#
## Android 5.0 以上需要在此处设置 PIE
#set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -fPIE")
#set(CMAKE_EXE_LINKER_FLAGS "${CMAKE_EXE_LINKER_FLAGS} -fPIE -pie")
#
## 配置使用 NDK Standalone Toolchain 编译
#set(NDK_STANDALONE_TOOLCHAIN /Users/vic/Developer/ndk_toolchain)
#set(CMAKE_SYSTEM_NAME Android)
#set(CMAKE_SYSTEM_VERSION 21)
#set(CMAKE_C_COMPILER ${NDK_STANDALONE_TOOLCHAIN}/bin/clang)
#set(CMAKE_CXX_COMPILER ${NDK_STANDALONE_TOOLCHAIN}/bin/clang++)
#set(CMAKE_FIND_ROOT_PATH ${NDK_STANDALONE_TOOLCHAIN})
#
## 使用 NDK 提供的头文件
#add_definitions("--sysroot=${NDK_STANDALONE_TOOLCHAIN}/sysroot")

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.


set(
        SOURCE_DIR
        ${importlibs_SOURCE_DIR}
)

message(STATUS "SOURCE_DIR = " ${SOURCE_DIR})
message(STATUS "WEBRTC_LIB = " ${SOURCE_DIR}/libs/${ANDROID_ABI}/libwebrtc.a)

#add_library(webrtc STATIC IMPORTED)
#
#set_target_properties(
#        webrtc
#        # Specifies the parameter you want to define.
#        PROPERTIES IMPORTED_LOCATION
#
#        # Provides the path to the library you want to import.
#        ${SOURCE_DIR}/libs/${ANDROID_ABI}/libwebrtc.a
#)

add_library( # Sets the name of the library.
            ${PROJECT_NAME}

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/native-lib.cpp )

target_include_directories(${PROJECT_NAME} PRIVATE
        ${HEADER_FILES}
        ${LIBWEBRTC_INCLUDE_PATH}
        ${LIBWEBRTC_INCLUDE_PATH}/third_party/abseil-cpp
        )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        ${PROJECT_NAME} PUBLIC
        android
        /Users/vic/Developer/workspace/hello_jni/import_libs/app/libs/${ANDROID_ABI}/libwebrtc.a
        OpenSLES

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )

# Compile definitions for libwebrtc.
target_compile_definitions(${PROJECT_NAME} PUBLIC
        $<$<NOT:$<PLATFORM_ID:Windows>>:WEBRTC_POSIX>
        $<$<PLATFORM_ID:Windows>:WEBRTC_WIN>
        $<$<PLATFORM_ID:Darwin>:WEBRTC_MAC>
        )