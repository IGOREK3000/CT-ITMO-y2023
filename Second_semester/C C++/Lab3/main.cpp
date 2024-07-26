#include <iostream>
#include <vector>
#include "Bucket.h"

int main() {
    Bucket<int> bucket(10);


    bucket.insert(5);
    bucket.printSkipField();
    bucket.insert(10);
    bucket.printSkipField();
    bucket.insert(15);
    bucket.printSkipField();
//    bucket.erase(7);
//    bucket.printSkipField();

    bucket.erase(1);
    bucket.insert(20);

    return 0;
}
