cmake_minimum_required(VERSION 3.16)
project(print-server LANGUAGES CXX)

# C++ standard requirements.
set(CMAKE_CXX_STANDARD 14)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

message(STATUS "CMAKE_CURRENT_SOURCE_DIR = " ${CMAKE_CURRENT_SOURCE_DIR})

set(
        SOURCE_FILES
        main.cpp
#        debug_client.cpp
)

message("\n=========== websocket print server Build Configuration ===========\n")

# Project configuration.
#set(LIBBOOST_INCLUDE_PATH ${BOOST_HEADER_PATH} CACHE STRING "libboost include path" FORCE)
#set(LIBBOOST_BINARY_PATH ${BOOST_LIB_PATH} CACHE STRING "libboost binary path" FORCE)

set(LIBBOOST_INCLUDE_PATH /Users/vic/Developer/deps/boost_1_73_0)
set(LIBBOOST_BINARY_PATH /Users/vic/Developer/deps/boost_1_73_0/stage/lib)
set(BOOST_HEADER_PATH ${LIBBOOST_INCLUDE_PATH})
set(BOOST_LIB_PATH ${LIBBOOST_BINARY_PATH})
message(STATUS "LIBBOOST_INCLUDE_PATH : " ${LIBBOOST_INCLUDE_PATH})

#if (NOT LIBWEBRTC_INCLUDE_PATH)
#    message(FATAL_ERROR "LIBWEBRTC_INCLUDE_PATH not provided")
#endif ()

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_executable( # Sets the name of the library.
        ${PROJECT_NAME}
        # Provides a relative path to your source file(s).
        ${SOURCE_FILES}
        )


include_directories(
        websocketpp
)

target_include_directories(
        ${PROJECT_NAME} PUBLIC
        websocketpp
        ${LIBBOOST_INCLUDE_PATH}
)

target_link_libraries(
        ${PROJECT_NAME} PUBLIC
        "${BOOST_LIB_PATH}/libboost_system.a"
)