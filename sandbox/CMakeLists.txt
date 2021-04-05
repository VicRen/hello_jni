cmake_minimum_required(VERSION 3.17)
project(sandbox)

set(CMAKE_CXX_STANDARD 14)

message("\n=========== sandbox Build Configuration ===========\n")

set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY ${PROJECT_BINARY_DIR}/lib)
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${PROJECT_BINARY_DIR}/bin)
set(CMAKE_INSTALL_PREFIX ${PROJECT_SOURCE_DIR}/out)
message(STATUS "CMAKE_CURRENT_SOURCE_DIR = " ${CMAKE_CURRENT_SOURCE_DIR})
message(STATUS "CMAKE_HOST_SYSTEM = " ${CMAKE_HOST_SYSTEM})
message(STATUS "CMAKE_HOST_SYSTEM_NAME = " ${CMAKE_HOST_SYSTEM_NAME})

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

add_library(${PROJECT_NAME} STATIC ${SOURCE_FILES})

target_include_directories(${PROJECT_NAME} PRIVATE
        ${HEADER_FILES}
)

message(STATUS "cmake-install-prefix = " ${CMAKE_INSTALL_PREFIX})
install(TARGETS sandbox DESTINATION lib)
install(FILES ${HEADER_FILES} DESTINATION lib/include)

add_subdirectory(example)