#ifndef SANDBOX_IRTCENGINEIMPL_H
#define SANDBOX_IRTCENGINEIMPL_H

#include "irtc_engine.h"
#include <api/create_peerconnection_factory.h>
#include <memory>
#include "rtc_base/thread.h"
#include "peer.h"

namespace vic {
    namespace rtc {

        class RtcPeerListener : public lightspeed::protoo::PeerListener {
        public:
            void onOpen() override;

            void onFail() override;

            void onRequest(lightspeed::protoo::Request *request,
                           lightspeed::protoo::ServerRequestHandler *handler) override;

            void onNotification(lightspeed::protoo::Notification *notification) override;

            void onDisconnected() override;

            void onClose() override;

        };

        class IRtcEngineImpl : public IRtcEngine {

        public:
            IRtcEngineImpl();

            ~IRtcEngineImpl();

            void initialize(const RtcEngineContext &context) override;

            int testingInt();

            static IRtcEngineImpl* Instance();

        private:
            IRtcEngineEventHandler *handler;
            std::unique_ptr<::rtc::Thread> networkThread;
            std::unique_ptr<::rtc::Thread> signalingThread;
            std::unique_ptr<::rtc::Thread> workerThread;
            std::unique_ptr<::rtc::Thread> thread_;
            ::rtc::scoped_refptr<webrtc::PeerConnectionFactoryInterface> pf_;
            std::unique_ptr<RtcPeerListener> peerListener_;
        };
    }
}


#endif //SANDBOX_IRTCENGINEIMPL_H
