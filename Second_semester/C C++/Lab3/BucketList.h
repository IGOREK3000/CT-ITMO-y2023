#pragma once

#include "Node.h"
#include "Bucket.h"

template<typename T>
class BucketList {
private:
    Node<Bucket<T>>* start;
    Node<Bucket<T>>* end;
public:
    BucketList() : start(nullptr), end(nullptr) {
    }

    BucketList(Node<Bucket<T>>* node) : start(node), end(node) {
    }

    void removeBucket(Node<Bucket<T>>* node) {
        ~(node->value); // нужно очищать bucket при удалении node
        Node<Bucket<T>>* prev = node->prev_node();
        Node<Bucket<T>>* next = node->next_node();
        prev->next = next;
        next->prev = prev;
    }

    void addEndBucket() {
        size_t new_capacity = end->value.get_capacity();
        Node<Bucket<T>> new_node = Node(Bucket<T>(2*new_capacity), nullptr, end);
        end->next = new_node;
        end = new_node;
    }

    void addStartBucket() {
        size_t new_capacity = start->value.get_capacity();
        Node<Bucket<T>> new_node = Node(Bucket<T>(new_capacity), start, nullptr);
        start->next = new_node;
        start = new_node;
    }
};

