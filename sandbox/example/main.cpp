#include <iostream>

#include "irtc_engine_impl.h"

using namespace vic::rtc;
using namespace std;

class RtcEventHandler : public IRtcEngineEventHandler {
public:
    void onError(int err, const char *msg) override {
        cout << "error occur while initializing!" << endl;
    }
};

int main() {
    cout << "Hello World Again!" << endl;

    auto engine = new IRtcEngineImpl;
    RtcEngineContext context;
    context.eventHandler = new RtcEventHandler;
    engine->initialize(context);
}
