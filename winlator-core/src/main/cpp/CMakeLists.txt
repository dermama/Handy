cmake_minimum_required(VERSION 3.22.1)
project(winlator-core C CXX)

# Box86/Box64 and Wine wrapper libraries
# These will be populated when the Winlator submodule is initialized

add_library(box86_wrapper SHARED box86_wrapper.c)
add_library(x11_platform SHARED x11_platform.c)
add_library(wine_launcher SHARED wine_launcher.c)

target_include_directories(box86_wrapper PRIVATE ${CMAKE_CURRENT_SOURCE_DIR})
target_include_directories(x11_platform PRIVATE ${CMAKE_CURRENT_SOURCE_DIR})
target_include_directories(wine_launcher PRIVATE ${CMAKE_CURRENT_SOURCE_DIR})
