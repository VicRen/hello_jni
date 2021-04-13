cmake_minimum_required(VERSION 3.10)
project(sandbox)

set(CMAKE_CXX_STANDARD 14)

message("\n=========== sandbox Build Configuration ===========\n")

# Project configuration.
set(LIBWEBRTC_INCLUDE_PATH "" CACHE STRING "libwebrtc include path")
set(LIBWEBRTC_BINARY_PATH "" CACHE STRING "libwebrtc binary path")
set(LIBBOOST_INCLUDE_PATH ${BOOST_HEADER_PATH} CACHE STRING "libboost include path" FORCE)
set(LIBBOOST_BINARY_PATH ${BOOST_LIB_PATH} CACHE STRING "libboost binary path" FORCE)

set(LIBWEBRTC_INCLUDE_PATH /Users/vic/Developer/workspace/webrtc/code/src)
set(LIBBOOST_INCLUDE_PATH /Users/vic/Developer/deps/boost/arm64-v8a/include/boost-1_74)
set(LIBBOOST_BINARY_PATH /Users/vic/Developer/deps/boost/arm64-v8a/lib)
set(BOOST_HEADER_PATH ${LIBBOOST_INCLUDE_PATH})
set(BOOST_LIB_PATH ${LIBBOOST_BINARY_PATH})

if(NOT LIBWEBRTC_INCLUDE_PATH)
    message(FATAL_ERROR "LIBWEBRTC_INCLUDE_PATH not provided")
endif()

#if(NOT LIBWEBRTC_BINARY_PATH)
#    message(FATAL_ERROR "LIBWEBRTC_BINARY_PATH not provided")
#endif()

set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY ${PROJECT_BINARY_DIR}/lib)
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${PROJECT_BINARY_DIR}/bin)
set(CMAKE_INSTALL_PREFIX ${PROJECT_SOURCE_DIR}/out)
message(STATUS "CMAKE_CURRENT_SOURCE_DIR = " ${CMAKE_CURRENT_SOURCE_DIR})
message(STATUS "CMAKE_HOST_SYSTEM = " ${CMAKE_HOST_SYSTEM})
message(STATUS "CMAKE_HOST_SYSTEM_NAME = " ${CMAKE_HOST_SYSTEM_NAME})

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

# 使用 NDK 提供的头文件
add_definitions("--sysroot=${NDK_STANDALONE_TOOLCHAIN}/sysroot")
add_definitions(-DPROTOO_ENABLE_SSL)

set(
        SOURCE_DIR
        ${sandbox_SOURCE_DIR}
)

set(
        SOURCE_FILES
        ${SOURCE_DIR}/rtc/irtc_engine_impl.cpp
)

set(
        HEADER_FILES
        ${SOURCE_DIR}/rtc/irtc_engine.h
)

add_subdirectory("rtc/protoo_client")

add_library(${PROJECT_NAME} STATIC ${SOURCE_FILES})
add_library(webrtc STATIC IMPORTED)

set_target_properties(
        webrtc
        # Specifies the parameter you want to define.
        PROPERTIES IMPORTED_LOCATION

        # Provides the path to the library youar want to import.
        ${SOURCE_DIR}/sdk/android/rtc/libs/${ANDROID_ABI}/libwebrtc.a
)

target_include_directories(${PROJECT_NAME} PRIVATE
        ${HEADER_FILES}
        ${LIBWEBRTC_INCLUDE_PATH}
        ${LIBWEBRTC_INCLUDE_PATH}/third_party/abseil-cpp
        ${SOURCE_DIR}/rtc/protoo_client/include
        ${SOURCE_DIR}/rtc/deps/include
)

target_link_libraries(
        ${PROJECT_NAME} PUBLIC
        webrtc
        OpenSLES
        protooclient
)

# Compile definitions for libwebrtc.
target_compile_definitions(${PROJECT_NAME} PUBLIC
        $<$<NOT:$<PLATFORM_ID:Windows>>:WEBRTC_POSIX>
        $<$<PLATFORM_ID:Windows>:WEBRTC_WIN>
        $<$<PLATFORM_ID:Darwin>:WEBRTC_MAC>
        )

message(STATUS "cmake-install-prefix = " ${CMAKE_INSTALL_PREFIX})
install(TARGETS sandbox DESTINATION lib)
install(FILES ${HEADER_FILES} DESTINATION lib/include)

#add_subdirectory(example)
add_subdirectory(sdk/android/rtc)