#include "irtc_engine_handler_jni.h"
#include <string>
#include "data/event.h"

namespace vic {
    namespace rtc {
        const int shortLen = sizeof(short);
        const int intLen = sizeof(int);
        const int boolLen = sizeof(bool);

        RtcEngineHandlerJni::RtcEngineHandlerJni(JNIEnv *env, const jobject listener) {
            j_env_ = env;
            j_listener_ = listener;
        }

        void RtcEngineHandlerJni::onError(int err, const char *msg) {
            const int totalLen = shortLen + intLen;
            auto *p_totalLen = (unsigned char *) &totalLen;
            auto *p_err = (unsigned char *) &err;

            unsigned char b[totalLen];
            memcpy(b, p_totalLen, shortLen);
            memcpy(b + shortLen, p_err, intLen);
            onEvent(ERROR, b, totalLen);
        }

        void RtcEngineHandlerJni::onEvent(int event, unsigned char *evt, int evtLen) {
            jclass cls = j_env_->GetObjectClass(j_listener_);
            jbyteArray array = j_env_->NewByteArray(evtLen);
            j_env_->SetByteArrayRegion(array, 0, evtLen, (jbyte *) evt);

            jmethodID methodId = j_env_->GetMethodID(cls, "onEvent", "(I[B)V");
            j_env_->CallVoidMethod(j_listener_, methodId, event, array);
        }
    }
}