
#include <iostream>
using namespace std;
typedef long long ll;

ll inv = 0;

void merge(int* arr, int left, int mid, int right) {
    int ch1 = 0;
    int ch2 = 0;
    int k = 0;
    int* result = new int[right - left + 1];
    while (left + ch1 < mid && mid + ch2 < right) {
        if (arr[left + ch1] < arr[mid + ch2]) {
            result[ch1 + ch2] = arr[left + ch1];
            ch1++;
            inv += k;
        }
        else {
            result[ch1 + ch2] = arr[mid + ch2];
            ch2++;
            k++;
        }   
    }
    while (left + ch1 < mid) {
        result[ch1 + ch2] = arr[left + ch1];
        ch1++;
        inv += k;
    }
    while (mid + ch2 < right) {
        result[ch1 + ch2] = arr[mid + ch2];
        ch2++;
    }
    for (int i = 0; i < ch1 + ch2; i++) {
        arr[left + i] = result[i];
    }
    delete[] result;
}

void mergeSort(int* arr, int left, int right) {
    if (left + 1 >= right) {
        return;
    }
    int mid = (left + right) / 2;
    mergeSort(arr, left, mid);
    mergeSort(arr, mid, right);
    merge(arr, left, mid, right);
}

int main()
{
    int n, i, j;
    cin >> n;
    int* arr = new int[n];

    for (i = 0; i < n; i++) {
        cin >> arr[i];
    }

    mergeSort(arr, 0, n);

    cout << inv;
    return 0;
}