#include "irtc_engine_impl.h"

#include <iostream>

namespace vic {
    namespace rtc {

        IRtcEngineImpl::IRtcEngineImpl() {
        }

        IRtcEngineImpl::~IRtcEngineImpl() {
        }

        void IRtcEngineImpl::initialize(const RtcEngineContext &context) {
            if (context.eventHandler) {
                context.eventHandler->onError(0, nullptr);
            }
            std::cout << "IRtcEngine initialized" << std::endl;
        }
    }
}

