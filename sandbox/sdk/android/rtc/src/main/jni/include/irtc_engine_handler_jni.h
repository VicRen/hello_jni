#ifndef SANDBOX_IRTC_ENGINE_HANDLER_JNI_H
#define SANDBOX_IRTC_ENGINE_HANDLER_JNI_H

#include <jni.h>
#include "irtc_engine.h"

namespace vic {
    namespace rtc {
        class RtcEngineHandlerJni : public IRtcEngineEventHandler {
        public:
            RtcEngineHandlerJni(JNIEnv* env, jobject listener);
            ~RtcEngineHandlerJni() {}

            void onError(int err, const char *msg) override;
            void onEvent(int event, unsigned char evt[], int evtLen);

        private:
            JavaVM *g_VM;
            jobject g_obj;
        };
    } // namespace rtc
} // namespace vic

#endif //SANDBOX_IRTC_ENGINE_HANDLER_JNI_H
