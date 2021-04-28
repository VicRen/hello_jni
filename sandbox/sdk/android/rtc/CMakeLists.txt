# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.10.2)

# Declares and names the project.

project("lsrtc")

set(CMAKE_CXX_STANDARD 14)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

set(
        SOURCE_DIR
        ${lsrtc_SOURCE_DIR}/src/main/jni
)

set(
        SOURCE_FILES
        ${SOURCE_DIR}/native-lib.cpp
        ${SOURCE_DIR}/irtc_engine_handler_jni.cpp
)

add_library( # Sets the name of the library.
        lsrtc
        SHARED
        ${SOURCE_FILES})

find_library(
        log-lib
        log)

include_directories(
        ../../../rtc
        src/main/jni/include
        ${LIBWEBRTC_INCLUDE_PATH}
        ${LIBWEBRTC_INCLUDE_PATH}/third_party/abseil-cpp
        ../../../rtc/protoo_client/include
)

target_include_directories(${PROJECT_NAME} PUBLIC
        ${SOURCE_DIR}
        ${SOURCE_DIR}/include
)

target_link_libraries(
        ${PROJECT_NAME} PUBLIC
        android
        sandbox
        protooclient
        ${log-lib}
)