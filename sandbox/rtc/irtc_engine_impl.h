#ifndef SANDBOX_IRTCENGINEIMPL_H
#define SANDBOX_IRTCENGINEIMPL_H

#include "irtc_engine.h"

namespace vic {
    namespace rtc {

        class IRtcEngineImpl : public IRtcEngine {

        public:
            IRtcEngineImpl();

            ~IRtcEngineImpl();

            void initialize(const RtcEngineContext &context) override;

            int testingInt();

        private:
            IRtcEngineEventHandler *handler;
        };
    }
}


#endif //SANDBOX_IRTCENGINEIMPL_H
