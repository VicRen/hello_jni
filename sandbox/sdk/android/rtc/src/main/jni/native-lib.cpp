#include <jni.h>
#include <string>
#include "irtc_engine.h"
#include "irtc_engine_impl.h"
#include "irtc_engine_handler_jni.h"

extern "C" {
    vic::rtc::IRtcEngineImpl *engine;

    JNIEXPORT jlong JNICALL
    Java_ren_vic_rtc_internal_RtcEngineImpl_nativeObjectInit(JNIEnv *env, jobject thiz,
        jobject context) {
        auto handler = new vic::rtc::RtcEngineHandlerJni(env, thiz);
        engine = new vic::rtc::IRtcEngineImpl();
        vic::rtc::RtcEngineContext ctx;
        ctx.eventHandler = handler;
        engine->initialize(ctx);
        return reinterpret_cast<intptr_t>(handler);
    }

    JNIEXPORT jint JNICALL
    Java_ren_vic_rtc_internal_RtcEngineImpl_nativeTestingInt(JNIEnv *env, jobject thiz) {
        if (engine) {
            return engine->testingInt();
        }
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
