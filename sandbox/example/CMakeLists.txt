cmake_minimum_required(VERSION 3.17)
project(sandbox_example LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 14)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

message("\n=========== sandbox_example Build Configuration ===========\n")

include_directories(
        ../rtc
)
link_directories(
        ../cmake-build-debug
)

add_executable(${PROJECT_NAME} main.cpp)

target_link_libraries(${PROJECT_NAME} PUBLIC
        sandbox
        )