#include <jni.h>
#include "hello.h"
#include <string>

extern "C" {
JNIEXPORT jstring JNICALL Java_ren_vic_jnidemo_Testing_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

JNIEXPORT jint JNICALL Java_ren_vic_jnidemo_Testing_helloFromJNI(
        JNIEnv *env,
        jobject thiz) {
    jclass cls = env->GetObjectClass(thiz);
    jmethodID methodId = env->GetMethodID(cls, "Test", "()V");
    env->CallVoidMethod(thiz, methodId);
    auto hello = new Hello(1);
    hello->SayHello();
    return hello->Name();
}
}