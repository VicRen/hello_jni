#include <jni.h>
#include <string>
#include "irtc_engine.h"
#include "irtc_engine_impl.h"

extern "C" {
JNIEXPORT jint JNICALL Java_ren_vic_rtc_Testing_intFromJni(
        JNIEnv *env,
        jobject /* this */
        ) {
    vic::rtc::RtcEngineContext context;
    auto engine = new vic::rtc::IRtcEngineImpl();
    engine->initialize(context);
    return engine->testingInt();
}
}