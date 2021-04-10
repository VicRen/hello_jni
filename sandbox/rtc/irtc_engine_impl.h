#ifndef SANDBOX_IRTCENGINEIMPL_H
#define SANDBOX_IRTCENGINEIMPL_H

#include "irtc_engine.h"
#include <api/create_peerconnection_factory.h>
#include <memory>
#include "rtc_base/thread.h"

namespace vic {
    namespace rtc {

        class IRtcEngineImpl : public IRtcEngine {

        public:
            IRtcEngineImpl();

            ~IRtcEngineImpl();

            void initialize(const RtcEngineContext &context) override;

            int testingInt();

            static IRtcEngineImpl* Instance();

        private:
            IRtcEngineEventHandler *handler;
            std::unique_ptr<::rtc::Thread> thread_;
            ::rtc::scoped_refptr<webrtc::PeerConnectionFactoryInterface> pf_;
        };
    }
}


#endif //SANDBOX_IRTCENGINEIMPL_H
