#include <iostream>

template <typename T>
class Quat {
private:
    T a;
    T b;
    T c;
    T d;

public:
    Quat(T a, T b, T c, T d) {
        this->a = a;
        this->b = b;
        this->c = c;
        this->d = d;
    }

    Quat() {
        this->a = T();
        this->b = T();
        this->c = T();
        this->d = T();
    }

    T get_a() const {
        return a;
    }
    T get_b() const {
        return b;
    }
    T get_c() const {
        return c;
    }
    T get_d() const {
        return d;
    }

};


int main() {
    std::cout << "Hello, World!" << std::endl;
    return 0;
}
