 #include <stdio.h>
 #include "hello_world.h"

 JNIEXPORT void JNICALL
 Java_HelloWorld_print(JNIEnv *env, jobject obj)
 {
     printf("Hello World!\n");
     return;
 }