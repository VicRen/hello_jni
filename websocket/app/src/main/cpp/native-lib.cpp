#include <jni.h>
#include <string>

#include "TestWebSocket.h"

extern "C" JNIEXPORT jstring JNICALL
Java_com_lstaas_websocket_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}extern "C"
JNIEXPORT jint JNICALL
Java_com_lstaas_websocket_MainActivity_DoTesting(JNIEnv *env, jobject thiz, jstring uri) {
    const char *nUri = env->GetStringUTFChars(uri, nullptr);
    auto tw = std::make_unique<TestWebSocket>();
    auto ret = tw->DoTesting(nUri);
    env->ReleaseStringUTFChars(uri, nUri);
    return ret;
}