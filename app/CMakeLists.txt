cmake_minimum_required(VERSION 3.23)

project(Records)

add_library(
        timestamp
        SHARED
        src/main/jni/timestamp.c)

find_library(
        log-lib
        log)

target_link_libraries(
        timestamp
        ${log-lib})
