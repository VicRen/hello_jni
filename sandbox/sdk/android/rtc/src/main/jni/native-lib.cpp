#include <jni.h>
#include <string>
#include "irtc_engine.h"
#include "irtc_engine_impl.h"
#include "irtc_engine_handler_jni.h"

#include <sdk/android/native_api/jni/java_types.h>
#include <android/log.h>

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, "TRY_WEBRTC", ##__VA_ARGS__)

struct TestFunctor {
    void operator()() {
        LOGI("--------->TestFunctor run");
    }
};

extern "C" {

    JNIEXPORT jlong JNICALL
    Java_ren_vic_rtc_internal_RtcEngineImpl_nativeObjectInit(JNIEnv *env, jobject thiz,
        jobject context) {
        auto handler = new vic::rtc::RtcEngineHandlerJni(env, thiz);
        vic::rtc::RtcEngineContext ctx;
        ctx.eventHandler = handler;
        vic::rtc::IRtcEngineImpl::Instance()->initialize(ctx);
        return webrtc::NativeToJavaPointer(handler);
    }

    JNIEXPORT jint JNICALL
    Java_ren_vic_rtc_internal_RtcEngineImpl_nativeTestingInt(JNIEnv *env, jobject thiz) {
        vic::rtc::IRtcEngineImpl::Instance()->testingInt();
        return 128;
    }

    JNIEXPORT jint JNICALL
    Java_ren_vic_rtc_internal_RtcEngineImpl_nativeJoinChannel(JNIEnv *env, jobject thiz,
                                                              jlong native_handle,
                                                              jbyteArray app_context, jstring token,
                                                              jstring channel_name, jstring info,
                                                              jint uid) {
    }
}
