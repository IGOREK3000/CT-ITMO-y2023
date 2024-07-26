const cnst = (value) => (...args) => value;

const variable = (name) => {
    const getIndex = { x: 0, y: 1, z: 2 }[name];
    return (...args) => args[getIndex];
};

const binaryFunction = f => (a, b) => (...args) => f(a(...args), b(...args));

const unaryFunction = f => (a) => (...args) => f(a(...args));

const add = binaryFunction((a, b) => a + b);

const subtract = binaryFunction((a, b) => a - b);

const multiply = binaryFunction((a, b) => a * b);

const divide = binaryFunction((a, b) => a / b);

const negate = unaryFunction((a) => -a);

const cube = unaryFunction((a) => Math.pow(a, 3));

const cbrt = unaryFunction((a) => Math.cbrt(a));

const pi = cnst(Math.PI);

const e = cnst(Math.E);



