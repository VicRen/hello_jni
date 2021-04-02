#include <jni.h>
#include "hello.h"
#include <string>

extern "C" {
JNIEXPORT jstring JNICALL Java_ren_vic_jnidemo_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

JNIEXPORT jint JNICALL Java_ren_vic_jnidemo_MainActivity_helloFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    auto hello = new Hello(1);
    hello->SayHello();
    return hello->Name();
}
}