#include <iostream>
#include <algorithm>
using namespace std;
typedef long long ll;

ll merge(ll* a, int n, ll* b, int m, ll k) {
    sort(a, a + n);
    sort(b, b + m);
    int j = 0;
    ll ans = 0;
    for (int i = 0; i < n; i++) {
        while (j < m && b[j] - a[i] < k) 
            j++;
        ans += m - j; 
    }
    return ans;
}

ll res(ll* a, int n, ll k) {
    if (n == 1) {
        return 0;
    }
    ll x = res(a, n / 2, k);
    ll y = res(a + n / 2, n - n / 2, k);
    return x + y + merge(a, n / 2, a + n / 2, n - n / 2, k);
}

int main() {
    ll n, k;
    cin >> n >> k;
    ll* a = new ll[n];
    for (int i = 0; i < n; i++)
        cin >> a[i];
    ll* b = new ll[n + 1];
    b[0] = 0;
    for (int i = 0; i < n; i++)
        b[i + 1] = b[i] + a[i];
    cout << res(b, n + 1, k);
}