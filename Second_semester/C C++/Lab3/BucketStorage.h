#pragma once

#include <cstddef>
#include <cstdint>
#include <iostream>


template<typename T>
class BucketStorage {
private:

#include <iostream>
#include <utility>

    template <typename T>
    class Bucket {
    public:
        // Конструктор
        Bucket(size_t capacity)
            : size(0), capacity(capacity), head_skipblock(static_cast<size_t>(-1)), data(new DataUnion[capacity]), skipfield(new size_t[capacity]) {
            std::fill(skipfield, skipfield + capacity, 0);
        }

        // Деструктор
        ~Bucket() {
            delete[] data;
            delete[] skipfield;
        }

        // Вставка элемента
        void insert(T value) {
            size_t index = findFreeIndex();
            if (index == static_cast<size_t>(-1)) {
                std::cerr << "Bucket is full or no free space available for insert." << std::endl;
                return;
            }

            data[index].value = value;
            skipfield[index] = 0;
            size++;
        }

        // Удаление элемента
        void erase(size_t index) {
            if (index >= capacity || skipfield[index] != 0) {
                std::cerr << "Invalid index or element already erased." << std::endl;
                return;
            }

            if (head_skipblock != static_cast<size_t>(-1)) {
                data[index].linker = {head_skipblock, static_cast<size_t>(-1)};
                data[head_skipblock].linker.second = index;
            } else {
                data[index].linker = {static_cast<size_t>(-1), static_cast<size_t>(-1)};
            }

            head_skipblock = index;
            skipfield[index] = 1;
            size--;
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

        // Поиск свободного индекса
        size_t findFreeIndex() {
            if (head_skipblock != static_cast<size_t>(-1)) {
                size_t free_index = head_skipblock;
                head_skipblock = data[free_index].linker.first;

                if (head_skipblock != static_cast<size_t>(-1)) {
                    data[head_skipblock].linker.second = static_cast<size_t>(-1);
                }

                return free_index;
            }

            for (size_t i = 0; i < capacity; ++i) {
                if (skipfield[i] == 0) {
                    return i;
                }
            }

            return static_cast<size_t>(-1);
        }
    };




    template<typename BucketType>
    class Node {
    public:
        Node* next;   // Указатель на следующий узел
        Node* prev;   // Указатель на предыдущий узел
        BucketType value;      // Значение узла

        // Конструкторы
        Node() : next(nullptr), prev(nullptr), value(BucketType()) {}
        explicit Node(const BucketType & val) : next(nullptr), prev(nullptr), value(val) {}
        explicit Node(const BucketType & val, Node* next, Node* prev) : next(next), prev(prev), value(val) {}
        explicit Node(BucketType && val) : next(nullptr), prev(nullptr), value(std::move(val)) {}
        Node(const Node & node) : next(node.next), prev(node.prev), value(node.value) {}

        // Методы доступа
        Node* next_node() {
            return next;
        }

        Node* prev_node() {
            return prev;
        }

        BucketType & node_value() {
            return value;
        }

        const BucketType & node_value() const {
            return value;
        }

        void set_next(Node* next_node) {
            next = next_node;
        }

        void set_prev(Node* prev_node) {
            prev = prev_node;
        }

        void set_value(const BucketType & val) {
            value = val;
        }

        void set_value(BucketType && val) {
            value = std::move(val);
        }
    };

    class BucketList {
    private:
        Node<Bucket>* start;
        Node<Bucket>* end;
        size_t size; // количество блоков

        void addFirstBucket(size_t capacity) {
            Node<Bucket> new_node = Node(Bucket(capacity), nullptr, nullptr);
            start = new_node;
            end = new_node;
        }

    public:
        BucketList() : start(nullptr), end(nullptr), size(0) {
        }

        explicit BucketList(Node<Bucket>* node) : start(node), end(node), size(1) {
        }

        Node<Bucket>* get_start_node() {
            return start;
        }

        Node<Bucket>* get_end_node() {
            return end;
        }


        void removeBucket(Node<Bucket>* node) {
            size--;
            ~(node->value); // нужно очищать bucket при удалении node
            if (node->prev_node() == nullptr && node->next_node() == nullptr) {
                start = nullptr;
                end = nullptr;
            } else if (node->prev_node() == nullptr) {
                start = node->next_node();
                start->prev_node() = nullptr;
            } else if (node->next_node() == nullptr) {
                end = node->prev_node();
                end->next_node() = nullptr;
            } else {
                Node<Bucket>* prev = node->prev_node();
                Node<Bucket>* next = node->next_node();
                prev->next = next;
                next->prev = prev;
            }
        }

        void addEndBucket(size_t initial_capacity) {
            size++;
            if (size == 1) {
                addFirstBucket(initial_capacity);
                return;
            }
            size_t new_capacity = end->value.get_capacity();
            Node<Bucket> new_node = Node(Bucket(2*new_capacity), nullptr, end);
            end->next = new_node;
            end = new_node;
        }

        void addStartBucket(size_t initial_capacity) {
            size++;
            if (size == 1) {
                addFirstBucket(initial_capacity);
                return;
            }
            size_t new_capacity = start->value.get_capacity();
            Node<Bucket> new_node = Node(Bucket(new_capacity), start, nullptr);
            start->next = new_node;
            start = new_node;
        }
    };

public:

    explicit BucketStorage(size_t init_block_capacity) : init_block_capacity(init_block_capacity),
    all_capacity(init_block_capacity), size(0), all_blocks(BucketList(Node(Bucket(init_block_capacity)))),
    active_blocks(BucketList()) {
    }

    class iterator {
    private:
        Node<Bucket>* current_node;
        size_t pos;
        size_t offset;
        //using skipfield = current_node->node_value()->get_skipfield();
    public:
        iterator(Node<Bucket>* current_node, size_t pos, size_t offset)
            :current_node(current_node), pos(pos), offset(offset) {
        }

        explicit iterator(Node<Bucket>* current_node)
            :current_node(current_node), pos(0), offset(0) {
            find_first_active();
        }


        iterator & operator++() {
            if (pos == current_bucket->get_capacity() - 1) {
                current_bucket = current_bucket
            }
            return *this;
        }

        iterator operator++(int) {
            iterator temp = *this;
            ++(*this);
            return temp;
        }

        iterator& operator--() {
            do {
                --index;
            } while (index > 0 && skipfield[index] >= 0);
            return *this;
        }

        iterator operator--(int) {
            iterator temp = *this;
            --(*this);
            return temp;
        }

        bool operator==(const iterator & other) const {
            return index == other.index;
        }

        bool operator!=(const iterator & other) const {
            return index != other.index;
        }

    private:

        void find_first_active() { //если мы находимся в начале блока
            if (current_node->node_value()->get_skipfield()[0] > 0) {
                pos = current_node->node_value()->get_skipfield()[0];
            } else {
                pos = 0;
            }
            offset = 1;
        }

        void find_last_active() { //если мы находимся в начале блока
            if (current_node->node_value()->get_skipfield()[] > 0) {
                pos = current_node->node_value()->get_skipfield()[0];
            } else {
                pos = 0;
            }
            offset = 1;
        }

        void find_first_inactive() {
            if ((get_skipfield())[] < 0) {
                pos = -get_skipfield()[0];
            } else {
                pos = 0;
            }
            offset = 0;
        }

        int32_t* get_skipfield() {
            return current_node->node_value()->get_skipfield();
        }

        int32_t* get_capacity() {
            return current_node->value;
        }
    };

    iterator begin() {
        return iterator(all_blocks.get_start_node(), );
    }

    iterator end() {
        return iterator(data, skipfield, capacity, capacity);
    }

    bool empty() {
        size == 0;
    }

    size_t capacity() {
        return all_capacity;
    }
private:
    size_t init_block_capacity;
    size_t all_capacity;
    size_t size;
    BucketList all_blocks;
    BucketList active_blocks; // blocks with deleted elements ./

};
