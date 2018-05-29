cmake_minimum_required(VERSION 3.4.1)

add_library(
            imagelib
            SHARED
            Yuv2Rgb.c)

find_library(
          log-lib
          log
)


# Include libraries needed for Yuv2Rgb.c lib
target_link_libraries(imagelib
                      android
                      ${log-lib})

