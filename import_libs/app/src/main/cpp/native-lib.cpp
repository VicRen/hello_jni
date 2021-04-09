#include <jni.h>
#include <string>
#include <android/log.h>
#include <rtc_base/thread.h>

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, "TRY_WEBRTC", ##__VA_ARGS__)

struct TestFunctor {
    void operator()() {
        LOGI("TestFunctor run");
    }
};

extern "C" JNIEXPORT jstring JNICALL
Java_ren_vic_importlibs_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    LOGI("---------->test start");

    auto thread = rtc::Thread::Create();
    thread->Start();
    thread->Invoke<void>(RTC_FROM_HERE, TestFunctor());
    thread->Stop();

    LOGI("test end");
    return env->NewStringUTF(hello.c_str());
}