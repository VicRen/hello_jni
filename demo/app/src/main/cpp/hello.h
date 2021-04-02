class Hello {
public:
    Hello(int n);
    ~Hello();

    void SayHello() const;
    int Name() const;

private:
    int n_;
};