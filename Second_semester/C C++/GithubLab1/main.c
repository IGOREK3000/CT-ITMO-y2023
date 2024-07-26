#include <inttypes.h>
#include <stdio.h>

struct float32_t {
    int64_t sign;    // 1 бит
    int64_t exp;     // 8 бит
    int64_t mantissa;// 23 бит
};

struct float32_t getMaxPositiveValue(int64_t size, int64_t expSize) {
    return (struct float32_t){0, (1 << expSize) - 2, (1 << (size - expSize - 1)) - 1};
}

struct float32_t getMaxNegativeValue(int64_t size, int64_t expSize) {
    return (struct float32_t){1, (1 << expSize) - 2, (1 << (size - expSize - 1)) - 1};
}

struct float32_t getMinPositiveValue(int64_t size, int64_t expSize) {
    return (struct float32_t){0, -(size - expSize - 2), 0};
}

struct float32_t getMinNegativeValue(int64_t size, int64_t expSize) {
    return (struct float32_t){1, -(size - expSize - 2), 0};
}

struct float32_t getPositiveZero() {
    return (struct float32_t){0, 0, 0};
}

struct float32_t getNegativeZero() {
    return (struct float32_t){1, 0, 0};
}

struct float32_t getPositiveInf(int64_t expSize) {
    return (struct float32_t){0, (1 << expSize) - 1, 0};
}

struct float32_t getNegativeInf(int64_t expSize) {
    return (struct float32_t){1, (1 << expSize) - 1, 0};
}

struct float32_t getNaN(int64_t expSize) {
    return (struct float32_t){1, (1 << expSize) - 1, 1};
}

int64_t isNaN(struct float32_t *number, int64_t expSize) {
    return (number->exp == (1 << expSize) - 1) && number->mantissa != 0;
}

int64_t isPositiveInf(struct float32_t *number, int64_t expSize) {
    return (number->exp == (1 << expSize) - 1) && (number->mantissa == 0) && (number->sign == 0);
}

int64_t isNegativeInf(struct float32_t *number, int64_t expSize) {
    return (number->exp == (1 << expSize) - 1) && (number->mantissa == 0) && (number->sign == 1);
}

int64_t isZero(struct float32_t *number) {
    return (number->exp == 0 && number->mantissa == 0);
}

void normalize(int64_t *mant, int64_t *exp, int64_t mantSize, int64_t expSize) {
    if (*exp != -(1ll << (expSize - 1)) + 1) {
        *mant += 1ll << mantSize;
    } else {
        (*exp)++;
        while (*mant < (1ll << mantSize)) {
            (*exp)--;
            (*mant) <<= 1;
        }
    }
}

void roundSpecialValues(struct float32_t *number, int64_t rounding, int64_t size, int64_t expSize) {
    //   struct float32_t *newNumber = number;
    if (rounding == 0) {
        if (isPositiveInf(number, expSize)) {
            *number = getMaxPositiveValue(size, expSize);
        }
    }
}

void writeFloatingPoint(struct float32_t *number, int64_t size, int64_t expSize) {
    int64_t bytesInHex = 4;
    int64_t mantSize = size - 1 - expSize;
    int64_t mantissa = number->mantissa;
    int64_t exp = number->exp;
    exp -= (1ll << (expSize - 1)) - 1;
    if (isNaN(number, expSize)) {
        printf("nan\n");
    } else if (isPositiveInf(number, expSize)) {
        printf("inf\n");
    } else if (isNegativeInf(number, expSize)) {
        printf("-inf\n");
    } else if (isZero(number)) {
        int64_t sign = number->sign;
        printf("%s0x0.", (sign == 1 ? "-" : ""));
        printf("%0*llx", (int) (mantSize / bytesInHex) + 1, 0);
        printf("p+0\n");
    } else if (exp == -(1ll << (expSize - 1)) + 1) {
        exp++;
        int64_t norm = 1ll << mantSize;
        while (mantissa < norm) {
            mantissa <<= 1;
            exp--;
        }
        mantissa -= norm;
        fprintf(stderr, "exp: %lld\n", exp);
        printf("%s0x1.", number->sign == 0 ? "" : "-");
        printf("%0*llx", (int) (mantSize / bytesInHex + 1), (mantissa << (bytesInHex - mantSize % bytesInHex)));
        printf("%sp%lld\n", (exp >= 0 ? "+" : ""), exp);
    } else {
        //    printf("%d %lld %lld\n", (int)(mantSize / bytesInHex) + 1,  (mantissa << (bytesInHex - mantSize % bytesInHex)), mantissa);


        printf("%s0x1.", number->sign == 0 ? "" : "-");
        printf("%0*llx", (int) (mantSize / bytesInHex) + 1, (mantissa << (bytesInHex - mantSize % bytesInHex)));
        printf("%s%lld\n", (exp >= 0 ? "p+" : "p"), exp);
    }
}


int64_t roundFloatingPoint(int64_t mant, int64_t ost, int64_t half, int64_t sign, int64_t roundType) {
    if ((roundType == 1 && ost > half) || (ost == half && mant % 2 == 1)) {
        mant++;
    }
    if ((roundType == 2 && sign == 0 && ost != 0) || (roundType == 3 && sign == 1 && ost != 0)) {
        mant++;
    }
    return mant;
}

struct float32_t floatAdd(struct float32_t *x, struct float32_t *y, int64_t size, int64_t expSize, int64_t roundType) {
    if (isNaN(x, expSize) || isNaN(y, expSize)) {
        return getNaN(expSize);
    }
    if ((isPositiveInf(x, expSize) && isNegativeInf(y, expSize)) ||
        (isPositiveInf(y, expSize) && isNegativeInf(x, expSize))) {
        return getNaN(expSize);
    }
    if (isPositiveInf(x, expSize) || isPositiveInf(y, expSize)) {
        return getPositiveInf(expSize);
    }
    if (isNegativeInf(x, expSize) || isNegativeInf(y, expSize)) {
        return getNegativeInf(expSize);
    }
    if (isZero(x) && !isZero(y)) {
        return *y;
    }
    if (isZero(y) && !isZero(x)) {
        return *x;
    }
    int64_t sign1 = x->sign;
    int64_t sign2 = y->sign;
    if (isZero(x) && isZero(y)) {
        return (sign1 + sign2 == 2) ? getNegativeZero() : getPositiveZero();
    }
    int64_t mantSize = size - 1 - expSize;
    int64_t mant1 = x->mantissa;
    int64_t exp1 = x->exp - (1ll << (expSize - 1)) + 1;
    int64_t mant2 = y->mantissa;
    int64_t exp2 = y->exp - (1ll << (expSize - 1)) + 1;
    fprintf(stderr, "exp1:%lld mant1:%lld || exp2:%lld mant2:%lld\n", exp1, mant1, exp2, mant2);
    normalize(&mant1, &exp1, mantSize, expSize);
    normalize(&mant2, &exp2, mantSize, expSize);
    if (exp1 < exp2 || (exp1 == exp2 && mant1 < mant2)) {
        int64_t tmp = mant1;
        mant1 = mant2;
        mant2 = tmp;
        tmp = exp1;
        exp1 = exp2;
        exp2 = tmp;
        tmp = sign1;
        sign1 = sign2;
        sign2 = tmp;
    }
    //  printf("%lld\n", exp1);
    int64_t diff = exp1 - exp2;
    if (diff > mantSize + 1) {
        int64_t ost = 1;
        if (sign1 != sign2) {
            mant1--;
            ost = 3;
        }
        if (mant1 < (1ll << mantSize)) {
            mant1 <<= 1;
            mant1++;
            exp1--;
        }
        mant1 = roundFloatingPoint(mant1, ost, 2, sign1, roundType);
        if (mant1 >= (1ll << (mantSize + 1))) {
            mant1 >>= 1;
            exp1++;
        } else if (mant1 < (1ll << mantSize)) {
            mant1 <<= 1;
            exp1--;
        }
        mant1 -= (1 << mantSize);
        if (exp1 == -(1ll << (expSize - 1)) + 1) {
            exp1++;
            mant1 >>= 1;
        }
        exp1 += (1ll << (expSize - 1)) - 1;
        return (struct float32_t){sign1, exp1, mant1};
    }
    if (diff != 0) {
        mant1 <<= diff;
    }
    int64_t mant = (sign1 == 0 ? 1 : -1) * mant1 + (sign2 == 0 ? 1 : -1) * mant2;
    if (mant == 0) {
        return getPositiveZero();
    }
    while (mant < (1ll << mantSize)) {
        exp2--;
        mant <<= 1;
    }
    int64_t ost = 0;
    int64_t ostSize = 0;
    while (mant >= (1ll << (mantSize + 1))) {
        exp2++;
        ost += (mant % 2ll) << ostSize;
        ostSize++;
        mant >>= 1;
    }
    if (ostSize != 0) {
        mant = roundFloatingPoint(mant, ost, (1ll << (ostSize - 1)), sign1, roundType);
    }
    if (mant >= (1ll << (mantSize + 1))) {
        exp2++;
        mant >>= 1;
    }
    mant -= (1ll << mantSize);
    exp2 += (1ll << (expSize - 1)) - 1;
    return (struct float32_t){sign1, exp2, mant};
}


struct float32_t floatMult(struct float32_t *x, struct float32_t *y, int64_t size, int64_t expSize, int64_t roundType) {
    if (isNaN(x, expSize) || isNaN(y, expSize)) {
        return getNaN(expSize);
    }
    if ((isZero(x) && (isPositiveInf(y, expSize) || isNegativeInf(y, expSize))) || ((isZero(y)) && (isPositiveInf(x, expSize) || isNegativeInf(x, expSize)))) {
        return getNaN(expSize);
    }

    int64_t sign = (x->sign + y->sign) % 2;
    if (isPositiveInf(x, expSize) || isPositiveInf(y, expSize) ||
        isNegativeInf(x, expSize) || isNegativeInf(y, expSize)) {
        return sign == 1 ? getNegativeInf(expSize) : getPositiveInf(expSize);
    }
    if (isZero(x) || isZero(y)) {
        return sign == 1 ? getNegativeZero() : getPositiveZero();
    }
    int64_t mantSize = size - 1 - expSize;

    int64_t mant1 = x->mantissa;
    int64_t exp1 = x->exp - (1ll << (expSize - 1)) + 1;

    int64_t mant2 = y->mantissa;
    int64_t exp2 = y->exp - (1ll << (expSize - 1)) + 1;
    normalize(&mant1, &exp1, mantSize, expSize);
    normalize(&mant2, &exp2, mantSize, expSize);
    //    fprintf(stderr, "%lld %lld %lld %lld\n", mant1, exp1, mant2, exp2);
    int64_t exp = exp1 + exp2;
    int64_t mant = mant1 * mant2;
    //    fprintf(stderr, "%lld %lld\n", mant, exp);
    int64_t decCount = 2 * mantSize;
    if ((mant >> decCount) >= 2) {
        exp++;
        decCount++;
    }
    struct float32_t result = {sign, exp, mant};
    roundSpecialValues(&result, roundType, size, expSize);
    if (exp > (1ll << (expSize - 1)) - 1) {
        if (roundType == 0) {
            return sign == 1 ? getNegativeInf(expSize) : getMaxPositiveValue(size, expSize);
        } else if (roundType == 3) {
            return sign == 1 ? getMaxNegativeValue(size, expSize) : getMaxPositiveValue(size, expSize);
        } else {
            return sign == 1 ? getNegativeInf(expSize) : getPositiveInf(expSize);
        }
    }
    if (exp <= -(1ll << (expSize - 1)) - mantSize && roundType == 2) {//too small value
        return sign == 1 ? getMinNegativeValue(size, expSize) : getMinPositiveValue(size, expSize);
    }
    if (exp <= -(1ll << (expSize - 1)) + 1) {
        decCount += -(1ll << (expSize - 1)) + 2 - exp;
        exp = -(1ll << (expSize - 1)) + 1;
    } else {
        mant -= 1ll << decCount;
    }
    int64_t ost = mant & ((1ll << (decCount - mantSize)) - 1);
    int64_t half = 1ll << (decCount - mantSize - 1);
    mant >>= decCount - mantSize;
    if (decCount > 64) {
        ost = (ost != 0);
        half = 2;
    }
    if (decCount - mantSize >= 64) {
        mant = 0;
    }
    exp += (1ll << (expSize - 1)) - 1;
    mant = roundFloatingPoint(mant, ost, half, sign, roundType);
    fprintf(stderr, "%lld %lld %lld\n", sign, exp, mant);
    result = (struct float32_t){sign, exp, mant};
    return result;
}

struct float32_t floatSub(struct float32_t *x, struct float32_t *y, int64_t size, int64_t expSize, int64_t roundType) {
    if (isNaN(x, expSize) || isNaN(y, expSize)) {
        return getNaN(expSize);
    }
    y->sign = (y->sign == 0) ? 1 : 0;
    return floatAdd(x, y, size, expSize, roundType);
}

struct float32_t floatDiv(struct float32_t *x, struct float32_t *y, int64_t size, int64_t expSize, int64_t roundType) {
    if (isNaN(x, expSize) || isNaN(y, expSize)) {
        return getNaN(expSize);
    }
    if ((isPositiveInf(x, expSize) || isNegativeInf(x, expSize)) &&
        (isNegativeInf(y, expSize) || isPositiveInf(y, expSize))) {
        return getNaN(expSize);
    }
    if (isZero(x) && isZero(y)) {
        return getNaN(expSize);
    }
    int64_t sign = (x->sign + y->sign) % 2;
    if (isPositiveInf(y, expSize) || isNegativeInf(y, expSize) || isZero(x)) {
        return (sign == 1) ? getNegativeZero() : getPositiveZero();
    }
    if (isZero(y) || isPositiveInf(x, expSize) || isNegativeInf(x, expSize)) {
        return (sign == 1) ? getNegativeInf(expSize) : getPositiveInf(expSize);
    }
    int64_t mantSize = size - 1 - expSize;
    int64_t mant1 = x->mantissa;
    int64_t exp1 = x->exp;
    int64_t mant2 = y->mantissa;
    int64_t exp2 = y->exp;
    fprintf(stderr, "exp1:%lld mant1:%lld || exp2:%lld mant2:%lld\n", exp1, mant1, exp2, mant2);
    //   writeFloatingPoint(x, size, expSize);
    //   writeFloatingPoint(y, size, expSize);
    normalize(&mant1, &exp1, mantSize, expSize);
    normalize(&mant2, &exp2, mantSize, expSize);
    fprintf(stderr, "exp1:%lld mant1:%lld || exp2:%lld mant2:%lld\n", exp1, mant1, exp2, mant2);
    int64_t exp = exp1 - exp2;

    if (exp > (1ll << (expSize - 1)) - 1) {
        if (roundType == 0) {
            return sign == 1 ? getMaxNegativeValue(size, expSize) : getMaxPositiveValue(size, expSize);
        }
        return (sign == 1 ? getNegativeInf(expSize) : getPositiveInf(expSize));
    }
    int64_t mant = (mant1 << mantSize) / mant2;
    //   printf("sign:%lld exp:%lld mant:%lld\n", sign ,exp, mant);
    if (mant < (1ll << mantSize)) {
        mant1 <<= 1;
        exp--;
        mant = (mant1 << mantSize) / mant2;
    }
    //  printf("sign:%lld exp:%lld mant:%lld\n", sign ,exp, mant);
    //    writeFloatingPoint(x, size, expSize);
    //    writeFloatingPoint(y, size, expSize);
    int64_t maxExp = 1ll << (expSize - 1);
    int64_t ost = (mant1 << mantSize) - mant * mant2;
    if (exp < -maxExp - mantSize) {
        if (roundType == 0) {
            return sign == 1 ? getNegativeZero() : getPositiveZero();
        }
        mant = roundFloatingPoint(0, 1, 2, sign, roundType);
        exp = -(1ll << (expSize - 1)) + 1;
    } else if (exp < -maxExp + 1) {
        int64_t shift = -maxExp + 2 - exp;
        //    printf("shift:%lld\n", shift);
        ost += (mant & ((1ll << shift) - 1)) << (mantSize + 1);
        mant >>= shift;
        mant2 <<= shift;
        mant = roundFloatingPoint(mant, ost * 2ll, mant2, sign, roundType);
        exp = -(1ll << (expSize - 1)) + 1;
        //      printf("exp:%lld mant:%lld\n", exp, mant);
    } else {
        mant -= (1ll << mantSize);
        mant = roundFloatingPoint(mant, 2ll * ost, mant2, sign, roundType);
    }
    //    printf(stderr, "exp:%lld\n",exp);
    exp += (1ll << (expSize - 1)) - 1;
    //    printf(stderr, "sign:%lld exp:%lld mant:%lld\n",exp);
    //    printf("sign:%lld exp:%lld mant:%lld\n", sign ,exp, mant);

    return (struct float32_t){sign, exp, mant};
    //  return (sign << (size - 1)) + (exp << mantSize) + mant;
}

struct float32_t makeOperation(struct float32_t *x, struct float32_t *y, char operation, int64_t size, int64_t expSize,
                               int64_t roundType) {

    if (operation == '*') {
        return floatMult(x, y, size, expSize, roundType);
    }
    if (operation == '/') {
        return floatDiv(x, y, size, expSize, roundType);
    }
    if (operation == '+') {
        return floatAdd(x, y, size, expSize, roundType);
    }

    return floatSub(x, y, size, expSize, roundType);
}

void get_size(char type, int64_t *size, int64_t *expSize) {
    if (type == 'h') {
        *size = 16;
        *expSize = 5;
    } else if (type == 'f') {
        *size = 32;
        *expSize = 8;
    } else {
        fprintf(stderr, "Wrong parameter type");
    }
}

int calculate_mode(int64_t n) {
    if (n == 4) {
        return 1;
    } else if (n == 6) {
        return 2;
    } else {
        fprintf(stderr, "Incorrect input");
        return -1;
    }
}

struct float32_t makeFloat(int64_t x, int64_t size, int64_t expSize) {
    struct float32_t number;
    int64_t mantSize = size - 1 - expSize;
    number.sign = x >> (size - 1);
    number.exp = (x >> mantSize) & ((1ll << expSize) - 1);
    number.mantissa = x & ((1ll << mantSize) - 1);
    return number;
}

int main(int argc, char *argv[]) {
    int mode = calculate_mode(argc);
    char format;
    int8_t rounding;
    int64_t size;
    int64_t expSize;
    sscanf(argv[1], "%c", &format);
    sscanf(argv[2], "%" SCNd8, &rounding);
    get_size(format, &size, &expSize);
    if (mode == 1) {
        int64_t x;
        sscanf(argv[3], "%x" SCNd32, &x);
        struct float32_t result = makeFloat(x, size, expSize);
        writeFloatingPoint(&result, size, expSize);

    } else if (mode == 2) {
        int64_t x;
        int64_t y;
        char operation;
        sscanf(argv[3], "%llx", &x);
        sscanf(argv[4], "%c", &operation);
        sscanf(argv[5], "%llx", &y);
        struct float32_t x_float = makeFloat(x, size, expSize);
        struct float32_t y_float = makeFloat(y, size, expSize);
        //        struct  float32_t a = getPositiveInf(expSize);
        //        writeFloatingPoint(&a, size, expSize);
        struct float32_t result = makeOperation(&x_float, &y_float, operation, size, expSize, rounding);
        //  printResult(&result);
        writeFloatingPoint(&result, size, expSize);
    } else {
        return 1;
    }


    return 0;
}
