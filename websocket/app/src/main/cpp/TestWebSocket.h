#ifndef WEBSOCKET_TESTWEBSOCKET_H
#define WEBSOCKET_TESTWEBSOCKET_H

#include <websocketpp/config/asio_no_tls_client.hpp>
#include <websocketpp/client.hpp>

#include <memory>

typedef websocketpp::client<websocketpp::config::asio_client> client;

class TestWebSocket {
public:
    TestWebSocket();
    ~TestWebSocket() {}
    int DoTesting(const std::string &uri);

private:
    void on_open(websocketpp::connection_hdl hdl);
    void on_fail(websocketpp::connection_hdl hdl);
    void on_message(websocketpp::connection_hdl hdl, client::message_ptr msg);

private:
    std::unique_ptr<client> client_;
};


#endif //WEBSOCKET_TESTWEBSOCKET_H
