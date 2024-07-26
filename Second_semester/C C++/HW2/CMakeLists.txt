cmake_minimum_required(VERSION 3.27)
project(HW2)

set(CMAKE_CXX_STANDARD 20)

#find_package(GTest CONFIG REQUIRED)

add_executable(HW2 main.cpp
        ct-c24-hw-quaternion-IGOREK3000/quat.hpp
)

#target_link_libraries(GTestCLion PRIVATE GTest::gtest GTest::gtest_main)
