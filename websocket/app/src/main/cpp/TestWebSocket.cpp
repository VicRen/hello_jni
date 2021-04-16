#include "TestWebSocket.h"
#include <android/log.h>

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, "WEBSOCKET_TESTING", ##__VA_ARGS__)

TestWebSocket::TestWebSocket() {
    client_ = std::make_unique<client>();
}

int TestWebSocket::DoTesting(const std::string &uri) {
    LOGI("DoTesting: %s", uri.c_str());
    try {
        // Set logging to be pretty verbose (everything except message payloads)
        client_->set_access_channels(websocketpp::log::alevel::all);
        client_->clear_access_channels(websocketpp::log::alevel::frame_payload);
        client_->set_error_channels(websocketpp::log::elevel::all);

        // Initialize ASIO
        client_->init_asio();

        // Register our message handler
        client_->set_open_handler(websocketpp::lib::bind(
                &TestWebSocket::on_open,
                this,
                websocketpp::lib::placeholders::_1
        ));
        client_->set_fail_handler(websocketpp::lib::bind(
                &TestWebSocket::on_fail,
                this,
                websocketpp::lib::placeholders::_1
        ));
        client_->set_message_handler(websocketpp::lib::bind(
                &TestWebSocket::on_message,
                this,
                websocketpp::lib::placeholders::_1,
                websocketpp::lib::placeholders::_2
        ));

        websocketpp::lib::error_code ec;
        client::connection_ptr con = client_->get_connection(uri, ec);
        if (ec) {
            std::cout << "could not create connection because: " << ec.message() << std::endl;
            return 0;
        }

        // Note that connect here only requests a connection. No network messages are
        // exchanged until the event loop starts running in the next line.
        client_->connect(con);

        // Start the ASIO io_service run loop
        // this will cause a single connection to be made to the server. c.run()
        // will exit when this connection is closed.
        client_->run();
    } catch (websocketpp::exception const & e) {
        std::cout << e.what() << std::endl;
    }
    return 0;
}

void TestWebSocket::on_message(websocketpp::connection_hdl hdl, client::message_ptr msg) {
    LOGI("onMessage: %s", msg->get_payload().c_str());

//    websocketpp::lib::error_code ec;
//
//    client_->send(hdl, msg->get_payload(), msg->get_opcode(), ec);
//    if (ec) {
//        LOGI("Echo failed because: %s", ec.message().c_str());
//    }
}

void TestWebSocket::on_open(websocketpp::connection_hdl hdl) {
    LOGI("on_open");
    std::string SIP_msg="OPTIONS sip:carol@chicago.com SIP/2.0\r\nVia: SIP/2.0/WS df7jal23ls0d.invalid;rport;branch=z9hG4bKhjhs8ass877\r\nMax-Forwards: 70\r\nTo: <sip:carol@chicago.com>\r\nFrom: Alice <sip:alice@atlanta.com>;tag=1928301774\r\nCall-ID: a84b4c76e66710\r\nCSeq: 63104 OPTIONS\r\nContact: <sip:alice@pc33.atlanta.com>\r\nAccept: application/sdp\r\nContent-Length: 0\r\n\r\n";
    client_->send(hdl, SIP_msg, websocketpp::frame::opcode::text);
}

void TestWebSocket::on_fail(websocketpp::connection_hdl hdl) {
    LOGI("on_fail");
}
