#include <iostream>
#include <vector>
using namespace std;

int sift_up(vector<int>& heap, int ind) {
    while (heap[ind] > heap[(ind - 1) / 2] && ind > 0) {
        swap(heap[ind], heap[(ind - 1) / 2]);
        ind = (ind - 1) / 2;
    }
    return ind;
}


int sift_down(vector<int>& heap, int ind) {
    while (2 * ind + 1 < heap.size()) {
        int left = 2 * ind + 1;
        int right = 2 * ind + 2;
        int j = left;
        if (2 * ind + 2 < heap.size() && heap[right] > heap[left]) {
            j = right;
        }
        if (heap[ind] >= heap[j]) {
            break;
        }
        swap(heap[ind], heap[j]);
        ind = j;
    }
    return ind;
}

pair<int, int> extract_max(vector<int>& heap) {
    int max_val = heap[0];
    swap(heap[0], heap[heap.size() - 1]);
    heap.pop_back();
    int ind = sift_down(heap, 0);
    ind++;
    if (heap.size() == 0) {
        ind = 0;
    }
    return make_pair(ind, max_val);
}

int add_element(vector<int>& heap, int element) {
    heap.push_back(element);
    int ind = sift_up(heap, heap.size() - 1);
    return ind + 1;
}

int remove_element(vector<int>& heap, int ind) {
    int value = heap[ind];
    swap(heap[ind], heap[heap.size() - 1]);
    heap.pop_back();
    sift_down(heap, ind);
    sift_up(heap, ind);
    return value;
}

int main()
{
    int n, m, type, num, r = 0;
    cin >> n >> m;
    vector<int> heap;
    vector<pair<int, int>> res;
    for (int i = 0; i < m; i++) {
        cin >> type;
        if (type == 1) {
            if (heap.size() == 0) {
                res.push_back(make_pair(-1, -1));
            }
            else {
                res.push_back(extract_max(heap));
            }
        }
        else if (type == 2) {
            cin >> num;
            if (heap.size() > n) {
                while (true) {
                    r++;
                }
            }
            if (heap.size() == n) {
                res.push_back(make_pair(-1, -1));
            }
            else {
                res.push_back(make_pair(-2, add_element(heap, num)));
            }
        }
        else if (type == 3) {
            cin >> num;
            if (num < 1 || num > heap.size()) {
                res.push_back(make_pair(-1, -1));
            }
            else {
                res.push_back(make_pair(-2, remove_element(heap, num - 1)));
            }
        }
    }
    for (int i = 0; i < res.size(); i++) {
        if (res[i].first == -1) {
            cout << -1 << endl;
        }
        else if (res[i].first == -2) {
            cout << res[i].second << endl;
        }
        else {
            cout << res[i].first << " " << res[i].second << endl;
        }

    }
    for (int i = 0; i < heap.size(); i++) {
        cout << heap[i] << " ";
    }
    return 0;
}
