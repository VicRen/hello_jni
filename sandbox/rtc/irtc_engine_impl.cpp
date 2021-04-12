#include "irtc_engine_impl.h"
#include <api/audio_codecs/builtin_audio_decoder_factory.h>
#include <api/audio_codecs/builtin_audio_encoder_factory.h>
#include <api/video_codecs/builtin_video_decoder_factory.h>
#include <api/video_codecs/builtin_video_encoder_factory.h>

#include <iostream>
#include <string>
#include <android/log.h>

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, "RtcEngineImpl", ##__VA_ARGS__)

namespace vic {
    namespace rtc {
        static IRtcEngineImpl *engine = nullptr;
        IRtcEngineImpl* IRtcEngineImpl::Instance() {
            if (!engine) {
                engine = new IRtcEngineImpl();
            }
            return engine;
        }

        IRtcEngineImpl::IRtcEngineImpl() {
        }

        IRtcEngineImpl::~IRtcEngineImpl() {
        }

        void IRtcEngineImpl::initialize(const RtcEngineContext &context) {
            handler = context.eventHandler;
            LOGI("-------->IRtcEngine initialized");

            networkThread   = ::rtc::Thread::CreateWithSocketServer();
            signalingThread = ::rtc::Thread::Create();
            workerThread    = ::rtc::Thread::Create();

            networkThread->SetName("network_thread", nullptr);
            signalingThread->SetName("signaling_thread", nullptr);
            workerThread->SetName("worker_thread", nullptr);

            if (!networkThread->Start() || !signalingThread->Start() || !workerThread->Start()) {
                return;
            }
            LOGI("-------->thread stared");
            thread_ = ::rtc::Thread::Create();
            pf_ = webrtc::CreatePeerConnectionFactory(
                    networkThread.get(),
                    workerThread.get(),
                    signalingThread.get(),
                    nullptr /*default_adm*/,
                    webrtc::CreateBuiltinAudioEncoderFactory(),
                    webrtc::CreateBuiltinAudioDecoderFactory(),
                    webrtc::CreateBuiltinVideoEncoderFactory(),
                    webrtc::CreateBuiltinVideoDecoderFactory(),
                    nullptr /*audio_mixer*/,
                    nullptr /*audio_processing*/);
            LOGI("-------->peerConnectionFactory created");
        }

        int IRtcEngineImpl::testingInt() {
//            thread_->PostTask(RTC_FROM_HERE, [this](){
//                if (handler) {
//                    handler->onError(0, nullptr);
//                }
//            });
            thread_->Start();
            thread_->Invoke<void>(RTC_FROM_HERE, [this](){
                LOGI("-------->posted task");
                if (handler) {
                    handler->onError(0, nullptr);
                }
            });
            thread_->Stop();
            return 25;
        }
    }
}

