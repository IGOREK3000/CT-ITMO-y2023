#pragma once

#include <bits/move.h>
template<typename T>
class Node {
public:
    Node* next;   // Указатель на следующий узел
    Node* prev;   // Указатель на предыдущий узел
    T value;      // Значение узла

    // Конструкторы
    Node() : next(nullptr), prev(nullptr), value(T()) {}
    explicit Node(const T& val) : next(nullptr), prev(nullptr), value(val) {}
    explicit Node(const T& val, Node* next, Node* prev) : next(next), prev(prev), value(val) {}
    explicit Node(T&& val) : next(nullptr), prev(nullptr), value(std::move(val)) {}
    Node(const Node & node) : next(node.next), prev(node.prev), value(node.value) {}

    // Методы доступа
    Node* next_node() {
        return next;
    }

    Node* prev_node() {
        return prev;
    }

    T& node_value() {
        return value;
    }

    const T& node_value() const {
        return value;
    }

    void set_next(Node* next_node) {
        next = next_node;
    }

    void set_prev(Node* prev_node) {
        prev = prev_node;
    }

    void set_value(const T& val) {
        value = val;
    }

    void set_value(T&& val) {
        value = std::move(val);
    }
};