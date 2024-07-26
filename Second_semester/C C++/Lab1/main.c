#include "return_codes.h"

#include <inttypes.h>
#include <malloc.h>
#include <math.h>
#include <stdio.h>

typedef struct
{
	int64_t sign;
	int64_t exp;
	int64_t mantissa;
} float32_t;

void getMaxPositiveValue(int64_t size, int64_t expSize, float32_t *result)
{
	*result = (float32_t){ 0, (1 << expSize) - 2, (1 << (size - expSize - 1)) - 1 };
}

void getMaxNegativeValue(int64_t size, int64_t expSize, float32_t *result)
{
	*result = (float32_t){ 1, (1 << expSize) - 2, (1 << (size - expSize - 1)) - 1 };
}

void getMinPositiveValue(int64_t size, int64_t expSize, float32_t *result)
{
	*result = (float32_t){ 0, -(size - expSize - 2), 0 };
}

void getMinNegativeValue(int64_t size, int64_t expSize, float32_t *result)
{
	*result = (float32_t){ 1, -(size - expSize - 2), 0 };
}

void getPositiveZero(float32_t *result)
{
	*result = (float32_t){ 0, 0, 0 };
}

void getNegativeZero(float32_t *result)
{
	*result = (float32_t){ 1, 0, 0 };
}

void getPositiveInf(int64_t expSize, float32_t *result)
{
	*result = (float32_t){ 0, (1 << expSize) - 1, 0 };
}

void getNegativeInf(int64_t expSize, float32_t *result)
{
	*result = (float32_t){ 1, (1 << expSize) - 1, 0 };
}

void getNaN(int64_t expSize, float32_t *result)
{
	*result = (float32_t){ 1, (1 << expSize) - 1, 1 };
}

int64_t isNaN(float32_t *number, int64_t expSize)
{
	return (number->exp == (1 << expSize) - 1) && number->mantissa != 0;
}

int64_t isPositiveInf(float32_t *number, int64_t expSize)
{
	return (number->exp == (1 << expSize) - 1) && (number->mantissa == 0) && (number->sign == 0);
}

int64_t isNegativeInf(float32_t *number, int64_t expSize)
{
	return (number->exp == (1 << expSize) - 1) && (number->mantissa == 0) && (number->sign == 1);
}

int64_t isZero(float32_t *number)
{
	return (number->exp == 0 && number->mantissa == 0);
}

int64_t highestBit(uint64_t x)
{
	int64_t bits = (int64_t)log2((double)x) - 1;
	return bits;
}

void normalize(int64_t *mant, int64_t *exp, int64_t mantSize, int64_t expSize)
{
	if (*exp != -(1ll << (expSize - 1)) + 1)
	{
		*mant += 1ll << mantSize;
	}
	else
	{
		int64_t shift = highestBit(1ll << mantSize) - highestBit(*mant);
		*mant <<= shift;
		*exp -= shift - 1;
	}
}

void writeFloatingPoint(float32_t *number, int64_t size, int64_t expSize)
{
	int64_t bytesInHex = 4;
	int64_t mantSize = size - 1 - expSize;
	int64_t mantissa = number->mantissa;
	int64_t exp = number->exp;
	exp -= (1ll << (expSize - 1)) - 1;
	if (isNaN(number, expSize))
	{
		printf("nan\n");
	}
	else if (isPositiveInf(number, expSize))
	{
		printf("inf\n");
	}
	else if (isNegativeInf(number, expSize))
	{
		printf("-inf\n");
	}
	else if (isZero(number))
	{
		int64_t sign = number->sign;
		printf("%s0x0.", (sign == 1 ? "-" : ""));
		printf("%0*" PRIx64 "", (int)(mantSize / bytesInHex) + 1, 0ull);
		printf("p+0\n");
	}
	else if (exp == -(1ll << (expSize - 1)) + 1)
	{
		exp++;
		int64_t norm = 1ll << mantSize;
		int64_t shift = highestBit(norm) - highestBit(mantissa);
		mantissa <<= shift;
		exp -= shift;
		mantissa -= norm;
		printf("%s0x1.", number->sign == 0 ? "" : "-");
		printf("%0*" PRIx64 "", (int)(mantSize / bytesInHex + 1), (mantissa << (bytesInHex - mantSize % bytesInHex)));
		printf("%sp%" PRId64 "\n", (exp >= 0 ? "+" : ""), exp);
	}
	else
	{
		printf("%s0x1.", number->sign == 0 ? "" : "-");
		printf("%0*" PRIx64 "", (int)(mantSize / bytesInHex) + 1, (mantissa << (bytesInHex - mantSize % bytesInHex)));
		printf("%s%" PRId64 "\n", (exp >= 0 ? "p+" : "p"), exp);
	}
}

int64_t roundFloatingPoint(int64_t mant, int64_t ost, int64_t half, int64_t sign, int64_t roundType)
{
	if ((roundType == 1 && ost > half) || (ost == half && mant % 2 == 1))
	{
		mant++;
	}
	if ((roundType == 2 && sign == 0 && ost != 0) || (roundType == 3 && sign == 1 && ost != 0))
	{
		mant++;
	}
	return mant;
}

void swap(int64_t *x, int64_t *y)
{
	int64_t tmp = *x;
	*x = *y;
	*y = tmp;
}

void floatAdd(float32_t *x, float32_t *y, int64_t size, int64_t expSize, int64_t roundType, float32_t *result)
{
	if ((isPositiveInf(x, expSize) && isNegativeInf(y, expSize)) ||
		(isPositiveInf(y, expSize) && isNegativeInf(x, expSize)) || isNaN(x, expSize) || isNaN(y, expSize))
	{
		getNaN(expSize, result);
		return;
	}
	if (isPositiveInf(x, expSize) || isPositiveInf(y, expSize))
	{
		getPositiveInf(expSize, result);
		return;
	}
	if (isNegativeInf(x, expSize) || isNegativeInf(y, expSize))
	{
		getNegativeInf(expSize, result);
		return;
	}
	if (isZero(x) && !isZero(y))
	{
		*result = *y;
		return;
	}
	if (isZero(y) && !isZero(x))
	{
		*result = *x;
		return;
	}
	int64_t sign1 = x->sign;
	int64_t sign2 = y->sign;
	if (isZero(x) && isZero(y))
	{
		(sign1 + sign2 == 2) ? getNegativeZero(result) : getPositiveZero(result);
		return;
	}
	int64_t maxExp = 1ll << (expSize - 1);
	int64_t mantSize = size - 1 - expSize;
	int64_t mant1 = x->mantissa;
	int64_t exp1 = x->exp - maxExp + 1;
	int64_t mant2 = y->mantissa;
	int64_t exp2 = y->exp - maxExp + 1;
	normalize(&mant1, &exp1, mantSize, expSize);
	normalize(&mant2, &exp2, mantSize, expSize);
	if (exp1 < exp2 || (exp1 == exp2 && mant1 < mant2))
	{
		swap(&mant1, &mant2);
		swap(&exp1, &exp2);
		swap(&sign1, &sign2);
	}
	int64_t diff = exp1 - exp2;
	if (diff > mantSize + 1)
	{
		int64_t ost = 1;
		if (sign1 != sign2)
		{
			mant1--;
			ost = 3;
		}
		if (mant1 < (1ll << mantSize))
		{
			mant1 <<= 1;
			mant1++;
			exp1--;
		}
		mant1 = roundFloatingPoint(mant1, ost, 2, sign1, roundType);
		if (mant1 >= (1ll << (mantSize + 1)))
		{
			mant1 >>= 1;
			exp1++;
		}
		else if (mant1 < (1ll << mantSize))
		{
			mant1 <<= 1;
			exp1--;
		}
		mant1 -= (1 << mantSize);
		if (exp1 == -maxExp + 1)
		{
			exp1++;
			mant1 >>= 1;
		}
		exp1 += maxExp - 1;
		*result = (float32_t){ sign1, exp1, mant1 };
		return;
	}
	if (diff != 0)
	{
		mant1 <<= diff;
	}
	int64_t mant = (sign1 == 0 ? 1 : -1) * mant1 + (sign2 == 0 ? 1 : -1) * mant2;
	if (mant == 0)
	{
		getPositiveZero(result);
		return;
	}
	while (mant < (1ll << mantSize))
	{
		exp2--;
		mant <<= 1;
	}
	int64_t ost = 0;
	int64_t ostSize = 0;
	while (mant >= (1ll << (mantSize + 1)))
	{
		exp2++;
		ost += (mant % 2ll) << ostSize;
		ostSize++;
		mant >>= 1;
	}
	if (ostSize != 0)
	{
		mant = roundFloatingPoint(mant, ost, (1ll << (ostSize - 1)), sign1, roundType);
	}
	if (mant >= (1ll << (mantSize + 1)))
	{
		exp2++;
		mant >>= 1;
	}
	mant -= (1ll << mantSize);
	exp2 += maxExp - 1;
	*result = (float32_t){ sign1, exp2, mant };
}

void floatSubtract(float32_t *x, float32_t *y, int64_t size, int64_t expSize, int64_t roundType, float32_t *result)
{
	if (isNaN(x, expSize) || isNaN(y, expSize))
	{
		getNaN(expSize, result);
		return;
	}
	y->sign = (y->sign == 0) ? 1 : 0;
	floatAdd(x, y, size, expSize, roundType, result);
}

void bigExponentValue(int64_t roundType, int64_t sign, int64_t size, int64_t expSize, float32_t *result)
{
	if (roundType == 0)
	{
		sign == 1 ? getMaxNegativeValue(size, expSize, result) : getMaxPositiveValue(size, expSize, result);
		return;
	}
	if (roundType == 2)
	{
		sign == 1 ? getMaxNegativeValue(size, expSize, result) : getPositiveInf(expSize, result);
		return;
	}
	if (roundType == 3)
	{
		sign == 1 ? getNegativeInf(expSize, result) : getMaxPositiveValue(size, expSize, result);
		return;
	}
	sign == 1 ? getNegativeInf(expSize, result) : getPositiveInf(expSize, result);
}

void smallExponentValue(int64_t roundType, int64_t sign, int64_t size, int64_t expSize, float32_t *result)
{
	if (roundType == 0)
	{
		sign == 1 ? getNegativeZero(result) : getPositiveZero(result);
		return;
	}
	if (roundType == 2)
	{
		sign == 1 ? getNegativeZero(result) : getMinPositiveValue(size, expSize, result);
		return;
	}
	if (roundType == 3)
	{
		sign == 1 ? getMinNegativeValue(size, expSize, result) : getPositiveZero(result);
		return;
	}
	sign == 1 ? getNegativeZero(result) : getPositiveZero(result);
}

void floatMultiply(float32_t *x, float32_t *y, int64_t size, int64_t expSize, int64_t roundType, float32_t *result)
{
	if ((isZero(x) && (isPositiveInf(y, expSize) || isNegativeInf(y, expSize))) ||
		((isZero(y)) && (isPositiveInf(x, expSize) || isNegativeInf(x, expSize))) || isNaN(x, expSize) || isNaN(y, expSize))
	{
		getNaN(expSize, result);
		return;
	}

	int64_t sign = (x->sign + y->sign) % 2;
	if (isPositiveInf(x, expSize) || isPositiveInf(y, expSize) || isNegativeInf(x, expSize) || isNegativeInf(y, expSize))
	{
		sign == 1 ? getNegativeInf(expSize, result) : getPositiveInf(expSize, result);
		return;
	}
	if (isZero(x) || isZero(y))
	{
		sign == 1 ? getNegativeZero(result) : getPositiveZero(result);
		return;
	}
	int64_t maxExp = 1ll << (expSize - 1);
	int64_t mantSize = size - 1 - expSize;

	int64_t mant1 = x->mantissa;
	int64_t exp1 = x->exp - (1ll << (expSize - 1)) + 1;

	int64_t mant2 = y->mantissa;
	int64_t exp2 = y->exp - maxExp + 1;
	normalize(&mant1, &exp1, mantSize, expSize);
	normalize(&mant2, &exp2, mantSize, expSize);
	int64_t exp = exp1 + exp2;
	int64_t mant = mant1 * mant2;
	int64_t decCount = 2 * mantSize;
	if ((mant >> decCount) >= 2)
	{
		exp++;
		decCount++;
	}
	if (exp > maxExp - 1)
	{
		bigExponentValue(roundType, sign, size, expSize, result);
		return;
	}
	if (exp <= -maxExp - mantSize)
	{
		smallExponentValue(roundType, sign, size, expSize, result);
		return;
	}
	if (exp <= -maxExp + 1)
	{
		decCount += -maxExp + 2 - exp;
		exp = -maxExp + 1;
	}
	else
	{
		mant -= 1ll << decCount;
	}
	int64_t ost = mant & ((1ll << (decCount - mantSize)) - 1);
	int64_t half = 1ll << (decCount - mantSize - 1);
	mant >>= decCount - mantSize;
	if (decCount > 64)
	{
		ost = (ost != 0);
		half = 2;
	}
	if (decCount - mantSize >= 64) //
	{
		mant = 0;
	}
	exp += maxExp - 1;
	mant = roundFloatingPoint(mant, ost, half, sign, roundType);

	*result = (float32_t){ sign, exp, mant };
}

void floatDivide(float32_t *x, float32_t *y, int64_t size, int64_t expSize, int64_t roundType, float32_t *result)
{
	if (((isPositiveInf(x, expSize) || isNegativeInf(x, expSize)) && (isNegativeInf(y, expSize) || isPositiveInf(y, expSize))) ||
		isNaN(x, expSize) || isNaN(y, expSize) || (isZero(x) && isZero(y)))
	{
		getNaN(expSize, result);
		return;
	}
	int64_t sign = (x->sign + y->sign) % 2;
	if (isPositiveInf(y, expSize) || isNegativeInf(y, expSize) || isZero(x))
	{
		(sign == 1) ? getNegativeZero(result) : getPositiveZero(result);
		return;
	}
	if (isZero(y) || isPositiveInf(x, expSize) || isNegativeInf(x, expSize))
	{
		(sign == 1) ? getNegativeInf(expSize, result) : getPositiveInf(expSize, result);
		return;
	}
	int64_t maxExp = 1ll << (expSize - 1);
	int64_t mantSize = size - 1 - expSize;
	int64_t mant1 = x->mantissa;
	int64_t exp1 = x->exp - maxExp + 1;
	int64_t mant2 = y->mantissa;
	int64_t exp2 = y->exp - maxExp + 1;
	normalize(&mant1, &exp1, mantSize, expSize);
	normalize(&mant2, &exp2, mantSize, expSize);
	int64_t exp = exp1 - exp2;
	int64_t mant = (mant1 << mantSize) / mant2;
	if (mant < (1ll << mantSize))
	{
		mant1 <<= 1;
		exp--;
		mant = (mant1 << mantSize) / mant2;
	}
	if (exp > (1ll << (expSize - 1)) - 1)
	{
		bigExponentValue(roundType, sign, size, expSize, result);
		return;
	}
	int64_t ost = (mant1 << mantSize) - mant * mant2;
	if (exp < -maxExp - mantSize)
	{
		mant = roundFloatingPoint(0, 1, 2, sign, roundType);
		exp = -maxExp + 1;
	}
	else if (exp <= -maxExp + 1)
	{
		int64_t shift = -maxExp + 2 - exp;
		ost += (mant & ((1ll << shift) - 1)) << (mantSize + 1);
		mant >>= shift;
		mant2 <<= shift;
		mant = roundFloatingPoint(mant, ost * 2ll, mant2, sign, roundType);
		exp = -maxExp + 1;
	}
	else
	{
		mant -= (1ll << mantSize);
		mant = roundFloatingPoint(mant, 2ll * ost, mant2, sign, roundType);
	}
	exp += maxExp - 1;

	*result = (float32_t){ sign, exp, mant };
}

void makeOperation(float32_t *x, float32_t *y, char operation, int64_t size, int64_t expSize, int64_t roundType, float32_t *result)
{
	if (operation == '*')
	{
		floatMultiply(x, y, size, expSize, roundType, result);
		return;
	}
	if (operation == '/')
	{
		floatDivide(x, y, size, expSize, roundType, result);
		return;
	}
	if (operation == '+')
	{
		floatAdd(x, y, size, expSize, roundType, result);
		return;
	}

	floatSubtract(x, y, size, expSize, roundType, result);
}

void get_size(char type, int64_t *size, int64_t *expSize)
{
	if (type == 'h')
	{
		*size = 16;
		*expSize = 5;
	}
	else if (type == 'f')
	{
		*size = 32;
		*expSize = 8;
	}
	else
	{
		*size = -1;
		*expSize = -1;
	}
}

int calculate_mode(int64_t n)
{
	if (n == 4)
	{
		return 1;
	}
	else if (n == 6)
	{
		return 2;
	}
	else
	{
		return -1;
	}
}

void makeFloat(int64_t x, int64_t size, int64_t expSize, float32_t *result)
{
	*result = (float32_t){ x >> (size - 1), (x >> (size - 1 - expSize)) & ((1ll << expSize) - 1), x & ((1ll << (size - 1 - expSize)) - 1) };
}

int checkOperation(char operation)
{
	char operations[] = { '+', '-', '/', '*' };
	int num_operations = sizeof(operations) / sizeof(operations[0]);
	for (int i = 0; i < num_operations; ++i)
	{
		if (operations[i] == operation)
		{
			return 0;
		}
	}
	return -1;
}

int main(int argc, char *argv[])
{
	int mode = calculate_mode(argc);
	if (mode == -1)
	{
		perror("Incorrect number of arguments (should be 3 or 5)");
		return ERROR_ARGUMENTS_INVALID;
	}
	char format;
	int64_t rounding;
	int64_t size;
	int64_t expSize;
	int64_t checkFormat = sscanf(argv[1], "%c", &format);
	if (checkFormat != 1)
	{
		perror("Incorrect type of float (should be char)");
		return ERROR_ARGUMENTS_INVALID;
	}
	int64_t checkRounding = sscanf(argv[2], "%" PRIx64, &rounding);
	if (checkRounding != 1)
	{
		perror("Incorrect format of rounding (should be decimal number)");
		return ERROR_ARGUMENTS_INVALID;
	}
	get_size(format, &size, &expSize);
	if (size == -1)
	{
		perror("Incorrect format");
		return ERROR_ARGUMENTS_INVALID;
	}
	if (rounding < 0 || rounding > 3)
	{
		perror("Incorrect rounding");
		return ERROR_ARGUMENTS_INVALID;
	}
	if (mode == 1)
	{
		int64_t x;
		sscanf(argv[3], "%" PRIx64, &x);
		float32_t *result = (float32_t *)malloc(sizeof(float32_t));
		makeFloat(x, size, expSize, result);
		writeFloatingPoint(result, size, expSize);
		free(result);
	}
	else
	{
		int64_t x;
		int64_t y;
		char operation;
		int64_t checkX = sscanf(argv[3], "%" PRIx64, &x);
		if (checkX != 1)
		{
			perror("Incorrect format of first number (should be octal number)");
			return ERROR_ARGUMENTS_INVALID;
		}
		int64_t checkOp = sscanf(argv[4], "%c", &operation);
		if (checkOp != 1)
		{
			perror("Incorrect format of operation (should be char)");
			return ERROR_ARGUMENTS_INVALID;
		}
		int64_t checkY = sscanf(argv[5], "%" PRIx64, &y);
		if (checkY != 1)
		{
			perror("Incorrect format of second number (should be octal number)");
			return ERROR_ARGUMENTS_INVALID;
		}
		if (checkOperation(operation) != 0)
		{
			perror("Unsupported operation");
			return ERROR_ARGUMENTS_INVALID;
		}

		float32_t *x_float = (float32_t *)malloc(sizeof(float32_t));
		float32_t *y_float = (float32_t *)malloc(sizeof(float32_t));
		makeFloat(x, size, expSize, x_float);
		makeFloat(y, size, expSize, y_float);
		float32_t *result = (float32_t *)malloc(sizeof(float32_t));
		makeOperation(x_float, y_float, operation, size, expSize, rounding, result);
		free(x_float);
		free(y_float);
		writeFloatingPoint(result, size, expSize);
		free(result);
	}
	return 0;
}
