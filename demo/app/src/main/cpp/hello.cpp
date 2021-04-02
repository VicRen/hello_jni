#include "hello.h"

#include <iostream>


Hello::Hello(int n) : n_(n) {

}

Hello::~Hello() = default;

void Hello::SayHello() const {
    std::cout << "Hello from C++! " << n_ << std::endl;
}

int Hello::Name() const {
    return n_;
}
