"use strict"

function AbstractBinaryOperation(left, right) {
    this.left = left;
    this.right = right;
}

AbstractBinaryOperation.prototype.evaluate = function (...args) {
    return this.operation(this.left.evaluate(...args), this.right.evaluate(...args));
};

AbstractBinaryOperation.prototype.toString = function () {
    return this.left.toString() + " " + this.right.toString() + " " + this.operationSymbol;
};

AbstractBinaryOperation.prototype.prefix = function () {
    return  "(" + this.operationSymbol + " " + this.left.prefix() + " " + this.right.prefix() + ")";
};

function AbstractUnaryOperation(value) {
    this.value = value;
}

AbstractUnaryOperation.prototype.evaluate = function (...args) {
    return this.operation(this.value.evaluate(...args));
};

AbstractUnaryOperation.prototype.toString = function () {
    return this.value.toString() + " " + this.operationSymbol;
};

AbstractUnaryOperation.prototype.prefix = function () {
    return  "(" + this.operationSymbol + " " + this.value.prefix() + ")";
};

function AbstractAnyArgumentsOperation(...args) {
    this.args = args;
}

AbstractAnyArgumentsOperation.prototype.evaluate = function (...args) {
    return this.operation(...this.args.map(x => x.evaluate(...args)));
};

AbstractAnyArgumentsOperation.prototype.toString = function () {
    return this.args.map(x => x.toString()).join(" ") + " " + this.operationSymbol;
};

AbstractAnyArgumentsOperation.prototype.prefix = function () {
    return  "(" + this.operationSymbol + " " + this.args.map(x => x.prefix()).join(" ") + ")";
};


function createBinaryOperation(operationSymbol, operation) {
    let Operation = function (left, right) {
        AbstractBinaryOperation.call(this, left, right);
    }
    Operation.prototype = Object.create(AbstractBinaryOperation.prototype);
    Operation.prototype.operationSymbol = operationSymbol;
    Operation.prototype.operation = operation;
    return Operation;
}

function createUnaryOperation(operationSymbol, operation) {
    let Operation = function (value) {
        AbstractUnaryOperation.call(this, value);
    }
    Operation.prototype = Object.create(AbstractUnaryOperation.prototype);
    Operation.prototype.operationSymbol = operationSymbol;
    Operation.prototype.operation = operation;
    return Operation;
}

function createAnyArgumentsOperation(operationSymbol, operation) {
    let Operation = function (...args) {
        AbstractAnyArgumentsOperation.call(this, ...args);
    }
    Operation.prototype = Object.create(AbstractAnyArgumentsOperation.prototype);
    Operation.prototype.operationSymbol = operationSymbol;
    Operation.prototype.operation = operation;
    return Operation;
}

const variables = ["x", "y", "z"];

function Const(value) {
    this.value = value;
}
Const.prototype.evaluate = function() {return this.value;}
Const.prototype.toString = function () {return this.value.toString();}
Const.prototype.prefix = function () {return this.value.toString();};

function Variable(name) {
    this.name = name;
    this.number = variables.indexOf(name);
}
Variable.prototype.evaluate = function (...values) {return values[this.number]};
Variable.prototype.toString = function () {return this.name;};
Variable.prototype.prefix = function () {return this.name;};

const Add = createBinaryOperation("+", (x, y) => x + y)

const Subtract = createBinaryOperation("-", (x, y) => x - y);

const Multiply = createBinaryOperation("*", (x, y) => x * y);

const Divide = createBinaryOperation("/", (x, y) => x / y);

const Negate = createUnaryOperation("negate", (value) => -value);

const Sinh = createUnaryOperation("sinh", (value) => Math.sinh(value));

const Cosh = createUnaryOperation("cosh", (value) => Math.cosh(value));

const Product = createAnyArgumentsOperation("product", (...args) => multiplication(...args));

const Geom = createAnyArgumentsOperation("geom", (...args) => Math.pow(Math.abs(multiplication(...args)), 1/args.length));

function foldLeft(init, func, ...args) {
    let result = init;
    for (const a of args) {
        result = func(result, a);
    }
    return result;
}

const multiplication = (...args) => foldLeft(1, (x, y) => x * y, ...args);

const allOperations = new Map([
    ["+", Add],
    ["-", Subtract],
    ["*", Multiply],
    ["/", Divide],
    ["negate", Negate],
    ["sinh", Sinh],
    ["cosh", Cosh],
    ["product", Product],
    ["geom", Geom]
]);

const parse = expression => {
    let tokens = expression.trim().split(/\s+/);
    let stack = [];

    tokens.forEach(token => {
        if (allOperations.has(token)) {
            let operation = allOperations.get(token);
            let size = operation.length;
            stack.push(new operation(...stack.splice(-size)));
        } else if (variables.includes(token)) {
            stack.push(new Variable(token));
        } else {
            stack.push(new Const(parseInt(token)));
        }
    });

    return stack[0];
};

// Token Source
function TokenSource(expression) {
    this.tokens = expression.replace(/([()])/g, ' $1 ').trim().split(/\s+/);
    this.tokens.push(END);
    this.pos = -1;
//    console.log("START|" + expression + "|END\n")
}
const END = "END";
TokenSource.prototype.previous = function() {
    if (pos === 0) {
        return "START";
    }
    return this.tokens[this.pos - 1];
}
TokenSource.prototype.currentToken = function() {
    return this.tokens[this.pos];
}
TokenSource.prototype.nextToken = function() {
    this.pos++;
    return this.currentToken();
}
TokenSource.prototype.eof = function() {
    return this.tokens[this.pos] === END;
}

// PrefixParser
const isEmpty = token => token.trim() === "";
const isNumber = token => !isNaN(token) && !isEmpty(token);
const isBracket = token => token === "(";
const isVariable = token => variables.includes(token);
const isEOF = token => token === END;
const isFactorStart = token => isNumber(token) || isVariable(token) || isBracket(token);
const isOperation = token => allOperations.has(token);

const parseUntil = function(source, stack, tokenCond) {
    let token = source.currentToken();
    if (tokenCond(token)) {
        throw new ParsingError("Empty input on position " + source.pos);
    }
    while (!tokenCond(token) && !isEOF(token)) {
        if (isFactorStart(token) && stack.length === 0) {
            parseFactor(source, stack, 0);
        } else if (isOperation(token)) {
            let operation = allOperations.get(token);
            let size = operation.length;
            parseOperation(source, stack, size, operation);
        } else {
            throw new ParsingError("Unexpected Token: " + token + " on position " + source.pos);
        }
        token = source.currentToken();
    }
}

function parseFactor(source, stack, prev) {
    let token = source.currentToken();
    if (isBracket(token)) {
        parseBrackets(source, stack);
    } else if (isNumber(token)) {
        stack.push(new Const(parseInt(token)));
    } else if (isVariable(token)) {
        stack.push(new Variable(token));
    }  else {
        throw new ParsingError("Factor expected, found: " + token + " on position " + source.pos);
    }
    source.nextToken();
}

const parseBrackets = function(source, stack) {
    let token = source.nextToken();
    if (isVariable(token)) {
        throw new ParsingError("Variable is not expected on position " + source.pos);
    } else if (isNumber(token)) {
        throw new ParsingError("Const is not expected on position " + source.pos);
    }
    parseUntil(source, stack, token => token === ")");
}

const parseAll = function(source, stack) {
    let token = source.nextToken();
    parseUntil(source, stack, isEOF);
}


const parseOperation = function(source, stack, size, operation) {
    source.nextToken();
    if (size == 0) {
        let count = 0;
        while(isFactorStart(source.currentToken())) {
            parseFactor(source, stack);
            count++;
        }
        if (isOperation(source.currentToken())) {
            throw new ParsingError("Expected factor or closing bracket, found operation " + token + " on position " + source.pos);
        }
        if (count == 0) {
            stack.push(new operation());
        } else {
            stack.push(new operation(...stack.splice(-count)));
        }
    } else {
        for (let i = 0; i < size; i++) {
            parseFactor(source, stack);
        }
        stack.push(new operation(...stack.splice(-size)));
    }
}

const parsePrefix = expression => {
    let source = new TokenSource(expression);
    let stack = [];
    parseAll(source, stack);
    return stack[0];
}

function ExpressionError(message) {
    Error.call(this, message);
    this.message = message;
}
ExpressionError.prototype = Object.create(Error.prototype);
ExpressionError.prototype.name = "ExpressionError";
ExpressionError.prototype.constructor = ExpressionError;

function ParsingError(message) {
    ExpressionError.call(this, message);
    this.message = message
}
ParsingError.prototype = Object.create(ExpressionError.prototype);
ParsingError.prototype.name = "ParsingError";
ParsingError.prototype.constructor = ParsingError;

//print(parsePrefix("()"))

