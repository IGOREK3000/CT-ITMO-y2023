#include <stdio.h>
#include <stdint.h>

#define MERSENNE 2147483647
#define POW16 65536


int countDigits(int number);
int countDigitsU(unsigned int number);
void printChars(int length, char c);
void printBorder(int len1, int len2);
void printMiddle(int max1, int max2, int num1, int num2, int i, uint64_t fact);
void printMiddleN(int maxDigits1, int maxDigits2);
uint64_t nextFact(uint64_t fact, int i);
unsigned int max(unsigned int a, unsigned int b);

int main() {
    int n_start, n_end, align, flag = 0;
    uint64_t fact = 1;
    scanf("%d%d%d", &n_start, &n_end, &align);

    if (n_start < 0 || n_end < 0) {
        fprintf(stderr, "Incorrect input");
        return 1;
    }


    for (uint16_t i = 1; i < n_start; i++) {
        fact = (fact * i) % MERSENNE;
    }

    uint64_t startFact = fact;
    uint64_t maxFact = 1;



    for (uint16_t i = n_start; i != (n_end + 1) % POW16 || (flag == 0) && n_start != (n_end + 1); i = (i + 1) % POW16) {
        flag = 1;
        maxFact = max((fact = fact * i) % MERSENNE, maxFact);
    }
    flag = 0;
    fact = startFact;



    uint16_t maxDigits1 = countDigits(n_start <= n_end ? n_end : POW16);
    uint16_t maxDigits2 = max(countDigitsU(n_start <= n_end ? maxFact : MERSENNE), 2);

    printBorder(maxDigits1, maxDigits2);
    if (align == -1) {
        printf("| %-*s | %-*s |\n", maxDigits1, "n", maxDigits2,  "n!");
        printBorder(maxDigits1, maxDigits2);
        for (int i = n_start; i != n_end + 1 || n_start == n_end + 1; i = (i + 1) % POW16) {
            flag = 1;
            printf("| %-*d | %-*d |\n", maxDigits1, i, maxDigits2,  fact = nextFact(fact, i));
        }
    } else if (align == 0) {
        printMiddleN(maxDigits1, maxDigits2);
        printBorder(maxDigits1, maxDigits2);
        for (unsigned int i = n_start; i != (n_end + 1) % POW16 || (flag == 0 && n_start == (n_end + 1)); i = (i + 1) % POW16) {
            flag = 1;
            printMiddle(maxDigits1, maxDigits2, countDigits(i),
                        countDigitsU(fact), i, fact = nextFact(fact, i));
        }
    } else if (align == 1) {
        printf("| %*s | %*s |\n", maxDigits1, "n", maxDigits2,  "n!");
        printBorder(maxDigits1, maxDigits2);
        for (int i = n_start; i != n_end + 1 || n_start == n_end + 1; i = (i + 1) % POW16) {
            flag = 1;
            printf("| %*d | %*d |\n", maxDigits1, i, maxDigits2,  fact = nextFact(fact, i));

        }
    } else {
        printf("Align can be: -1, 0, 1");
    }
    printBorder(maxDigits1, maxDigits2);
}

void printMiddle(int max1, int max2, int num1, int num2, int i, uint64_t fact) {
    int d1 = (max1 - num1) / 2;
    int d2 = (max2 - num2) / 2;
    int diff1 = max1 % 2 != num1 % 2;
    int diff2 = max2 % 2 != num2 % 2;
    printf("| %*s%d%*s | %*s%d%*s |\n",  (diff1 == 1) ? (d1 + 1) : (d1) , "", i, d1, "",
           (diff2 == 1) ? (d2 + 1) : (d2), "", fact, d2,  "");

}

void printMiddleN(int maxDigits1, int maxDigits2) {
    printf("| %*s%s%*s | %*s%s%*s |\n",
           (maxDigits1 % 2 != 1) ? ((maxDigits1 - 1) / 2 + 1) : ((maxDigits1 - 1) / 2) ,"", "n",
           (maxDigits1 - 1) / 2, "",
           (maxDigits2 % 2 != 0) ? ((maxDigits2) / 2) : (maxDigits2) / 2 - 1, "", "n!",
           (maxDigits2) / 2 - 1,  "");
}

void printBorder(int len1, int len2) {
    printf("+");
    printChars(len1 + 2, '-');
    printf("+");
    printChars(len2 + 2, '-');
    printf("+\n");
}

void printChars(int length, char ch) {
    for (int i = 0; i < length; i++) {
        printf("%c", ch);
    }
}


uint64_t nextFact(uint64_t fact, int i) {
    return i == 0 ? 1 : fact * i % MERSENNE;
}

int countDigits(int number) {
    int count = 0;
    if (number == 0) {
        return 1;
    }
    while (number != 0) {
        count++;
        number /= 10;
    }
    return count;
}

int countDigitsU(unsigned int number) {
    int count = 0;
    while (number != 0) {
        count++;
        number /= 10;
    }
    return count;
}

unsigned int max(unsigned int a, unsigned int b) {
    if (a > b) {
        return a;
    } else {
        return b;
    }
}



