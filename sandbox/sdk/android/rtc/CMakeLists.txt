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

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
        lsrtc

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        ${SOURCE_FILES})

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

include_directories(
        ../../../rtc
        src/main/jni/include
        ${LIBWEBRTC_INCLUDE_PATH}
        ${LIBWEBRTC_INCLUDE_PATH}/third_party/abseil-cpp
        ../../../rtc/protoo_client/include
)
#link_directories(
#        ../../../cmake-build-debug/lib
#)

target_include_directories(${PROJECT_NAME} PUBLIC
        ${SOURCE_DIR}
        ${SOURCE_DIR}/include
)

target_link_libraries( # Specifies the target library.
        ${PROJECT_NAME} PUBLIC
        android
        sandbox
        protooclient
                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib}
        )