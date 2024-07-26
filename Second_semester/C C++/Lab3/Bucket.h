#include <cstdint>
#include <iostream>
#include <utility>

template <typename T>
class Bucket {
public:
    // Конструктор
    explicit Bucket(size_t capacity)
        : size(0), capacity(capacity), head_skipblock(0), data(new DataUnion[capacity]), skipfield(new size_t[capacity]) {

        std::fill(skipfield, skipfield + capacity, 0);
        skipfield[0] = capacity;
        skipfield[capacity - 1] = capacity;

        data[0].linker = std::make_pair(size_t(-1), size_t(-1));
    }

    ~Bucket() {
        delete[] data;
        delete[] skipfield;
    }

   size_t insert(T value) {
        size_t index = findFreeIndex();
        if (index == size_t(-1)) {
            std::cerr << "Bucket is full or no free space available for insert." << std::endl;
            return size_t(-1);
        }

        size_t prev = data[index].linker.first;
        if (skipfield[index] == 1) {
             // index of prev skipblock
            head_skipblock = prev;
            data[index].value = value;
            if (prev != size_t(-1)) {
                data[prev].linker.second = size_t(-1);
            }

        } else {
            data[index + 1].linker = data[index].linker;
            head_skipblock++;
            data[index].value = value;
            if (prev != size_t(-1)) {
                data[prev].linker.second = index + 1;
            }
        }

        size++;
        updateSkipfieldOnInsert(index);



        return 0;
    }

    size_t erase(size_t i) {
        auto next_active = size_t(-1);

        if ( active_left(i) && active_right(i) ) { // ( 0 0 |0| 0 0 )
            data[i].linker = std::make_pair(head_skipblock, size_t(-1));
            head_skipblock = i;
            next_active = is_last ? size_t(-1) : i + 1;
        }
        if ( active_left && unactive_right ) { // right ( 0 |0| 3 0 3 )
            size_t right_offset = skipfield[i + 1];
            size_t prev = data[i + 1].linker.first;
            size_t next = data[i + 1].linker.second;
            data[i].linker = data[i + 1].linker;
            if (prev != size_t(-1)) {
                data[prev].linker.second = i;
            }
            if (next != size_t(-1)) {
                data[next].linker.first = i;
            }
            next_active = (i + right_offset + 1) > (capacity - 1) ? size_t(-1) : (i + right_offset + 1);
        }
        if ( unactive_left && active_right ) { // left ( 3 0 3 |0| 0 )
            next_active = i == (capacity - 1) ? size_t(-1) : i + 1;
        }
        if ( unactive_left && unactive_right ) { // left-right ( 3 0 3 |0| 4 0 0 4 )
            size_t right_offset = skipfield[i + 1];
            size_t right_prev = data[i + 1].linker.first;
            size_t right_next = data[i + 1].linker.second;
            if (right_next != size_t(-1)) {
                data[right_next].linker.first = data[i + 1].linker.first;
            }
            if (right_prev != size_t(-1)) {
                data[right_prev].linker.second = data[i + 1].linker.second;
            }
            next_active = (i + right_offset + 1) > (capacity - 1) ? size_t(-1) : (i + right_offset + 1);
        }

        size--;
        updateSkipfieldOnErase(i);
        return next_active;
    }

    void printSkipField() {
        for (size_t i = 0; i < capacity; i++) {
            std::cout << skipfield[i] << " ";
        }
        std::cout << "\n";
    }

    void printData() {
        for (size_t i = 0; i < capacity; i++) {
            std::cout << data[i] << " ";
        }
    }

private:
    size_t size;
    size_t capacity;
    size_t head_skipblock;

    union DataUnion {
        T value;
        std::pair<size_t, size_t> linker;

        DataUnion() {}
        ~DataUnion() {}
    };

    DataUnion* data;
    size_t* skipfield;

    size_t findFreeIndex() {
        if (size == capacity) {
            return size_t(-1);
        }

        return head_skipblock;
    }

    void updateSkipfieldOnInsert(size_t index) {
        size_t new_length = skipfield[index] - 1;
        if (new_length == 0) {
            skipfield[index] = 0;
            return;
        }
        skipfield[index + new_length] = new_length;
        skipfield[index + 1] = new_length;
        skipfield[index] = 0;
    }

    void updateSkipfieldOnErase(size_t index) {

       if (index - 1 >= 0 && skipfield[index - 1] > 0) {
           size_t offset = skipfield[index - 1];
           skipfield[index - offset] = offset + 1;
           skipfield[index - 1] = offset + 1;
       }

       if (index + 1 < capacity && skipfield[index + 1] > 0) {
           size_t right_offset = skipfield[index + 1];
           size_t left_offset = skipfield[index];
           if (left_offset > 0) {
               skipfield[index + right_offset] = right_offset + left_offset;
               skipfield[index - left_offset + 1] = right_offset + left_offset;
               skipfield[index] = 0;
               skipfield[index + 1] = 0;
           } else {
               skipfield[index] = right_offset + 1;
               skipfield[index + right_offset] = right_offset + 1;
               skipfield[index + 1] = 0;
           }
       }


    }
};


