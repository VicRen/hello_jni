#ifndef SANDBOX_IRTC_ENGINE_H
#define SANDBOX_IRTC_ENGINE_H

namespace vic {
    namespace rtc {

        class IRtcEngineEventHandler {
        public:
            virtual ~IRtcEngineEventHandler() {
            }

            virtual void onError(int err, const char *msg) {
                (void) err;
                (void) msg;
            }

        };


        struct RtcEngineContext {
            IRtcEngineEventHandler *eventHandler;
            const char *appId;
            void *context;

            RtcEngineContext() : eventHandler(nullptr), appId(nullptr), context(nullptr) {
            }
        };

        class IRtcEngine {
        public:
            virtual void initialize(const RtcEngineContext& context) = 0;
        };
    } // namespace rtc
} // namespace vic

#endif //SANDBOX_IRTC_ENGINE_H
