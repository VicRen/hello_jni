#include "irtc_engine_impl.h"
#include "mymath.h"

#include <iostream>
#include <string>

namespace vic {
    namespace rtc {

        IRtcEngineImpl::IRtcEngineImpl() {
        }

        IRtcEngineImpl::~IRtcEngineImpl() {
        }

        void IRtcEngineImpl::initialize(const RtcEngineContext &context) {
            handler = context.eventHandler;
            std::cout << "IRtcEngine initialized" << std::endl;
        }

        int IRtcEngineImpl::testingInt() {
//            if (handler) {
//                handler->onError(0, nullptr);
//            }
            return myadd(25, 25);
        }
    }
}

