#include <iostream>
#include "hello_world.h"

JNIEXPORT void JNICALL
Java_HelloWorld_print(JNIEnv *env, jobject obj)
{
    std::cout << "Hello World from C++!" << std::endl;
    return;
}