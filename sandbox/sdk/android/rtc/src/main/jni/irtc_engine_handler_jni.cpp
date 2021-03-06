#include "irtc_engine_handler_jni.h"
#include <string>
#include "data/event.h"
#include <rtc_base/thread.h>
#include <android/log.h>

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, "RtcEngineHandlerJni", ##__VA_ARGS__)

namespace vic {
    namespace rtc {
        const int shortLen = sizeof(short);
        const int intLen = sizeof(int);
        const int boolLen = sizeof(bool);

        RtcEngineHandlerJni::RtcEngineHandlerJni(JNIEnv *env, jobject listener) {
            jclass cls = env->GetObjectClass(listener);
            jmethodID methodId = env->GetMethodID(cls, "onEvent", "(Ljava/lang/String;Ljava/lang/String;)V");
            //JavaVM是虚拟机在JNI中的表示，等下再其他线程回调java层需要用到
            env->GetJavaVM(&g_VM);
            // 生成一个全局引用保留下来，以便回调
            g_obj = env->NewGlobalRef(listener);
            LOGI("----->Constructor Thread: %s", ::rtc::ThreadManager::Instance()->CurrentThread()->name().c_str());
        }

        void RtcEngineHandlerJni::onError(int err, const char *msg) {
//            const int totalLen = shortLen + intLen;
//            auto p_totalLen = (unsigned char *) &totalLen;
//            auto p_err = (unsigned char *) &err;
//
//            unsigned char b[totalLen];
//            memcpy(b, p_totalLen, shortLen);
//            memcpy(b + shortLen, p_err, intLen);
//            onEvent(ERROR, b, totalLen);
//            onEvent(ERROR, nullptr, 0);
            onEvent(RTC_ERROR, "{message: \"TestingString\"}");
        }

        void RtcEngineHandlerJni::onEvent(int event, unsigned char *evt, int evtLen) {
            LOGI("------>RtcEngineHandlerJni::onEvent: ");
            JNIEnv *env;
            //获取当前native线程是否有没有被附加到jvm环境中
            int getEnvStat = g_VM->GetEnv((void **) &env,JNI_VERSION_1_6);
            if (getEnvStat == JNI_EDETACHED) {
                LOGI("------>RtcEngineHandlerJni::onEvent: JNI_EDETACHED");
                //如果没有， 主动附加到jvm环境中，获取到env
                if (g_VM->AttachCurrentThread(&env, NULL) != 0) {
                    return;
                }
//                mNeedDetach = JNI_TRUE;
            }
            jclass cls = env->GetObjectClass(g_obj);
            jbyteArray array = env->NewByteArray(evtLen);
            env->SetByteArrayRegion(array, 0, evtLen, (jbyte *) evt);

            jmethodID methodId = env->GetMethodID(cls, "onEvent", "(Ljava/lang/String;Ljava/lang/String;)V");
//            env->CallVoidMethod(g_obj, methodId, event, array);
            jstring jStringParam = env->NewStringUTF(std::string("testing").c_str());
            env->CallVoidMethod(g_obj, methodId, jStringParam, jStringParam);
        }

        void RtcEngineHandlerJni::onEvent(const char *name, const char *info) {
            LOGI("----->onEvent Thread: %s", ::rtc::ThreadManager::Instance()->CurrentThread()->name().c_str());
            LOGI("------>RtcEngineHandlerJni::onEvent: ");
            JNIEnv *env;
            //获取当前native线程是否有没有被附加到jvm环境中
            int getEnvStat = g_VM->GetEnv((void **) &env,JNI_VERSION_1_6);
            if (getEnvStat == JNI_EDETACHED) {
                LOGI("------>RtcEngineHandlerJni::onEvent: JNI_EDETACHED");
                //如果没有， 主动附加到jvm环境中，获取到env
                if (g_VM->AttachCurrentThread(&env, NULL) != 0) {
                    return;
                }
//                mNeedDetach = JNI_TRUE;
            }
            jclass cls = env->GetObjectClass(g_obj);
            jmethodID methodId = env->GetMethodID(cls, "onEvent", "(Ljava/lang/String;Ljava/lang/String;)V");
            jstring jStringParam1 = env->NewStringUTF(name);
            jstring jStringParam2 = env->NewStringUTF(info);
            env->CallVoidMethod(g_obj, methodId, jStringParam1, jStringParam2);
        }
    }
}