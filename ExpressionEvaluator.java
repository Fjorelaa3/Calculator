package Project;

import java.util.Stack;

public class ExpressionEvaluator {

    private static double factorial(double n) {
        if (n <= 1) {
            return 1;
        } else {
            return n * factorial(n - 1);
        }
    }

    public static double evaluate(String expression) {
        expression = expression.replaceAll("\\s+", ""); // Remove whitespace

        Stack<Double> operandStack = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            if (Character.isDigit(ch) || ch == '.') {
                StringBuilder operandBuilder = new StringBuilder();
                operandBuilder.append(ch);

                // Append the following digits or decimal points
                while (i + 1 < expression.length()
                        && (Character.isDigit(expression.charAt(i + 1)) || expression.charAt(i + 1) == '.')) {
                    operandBuilder.append(expression.charAt(i + 1));
                    i++;
                }

                double operand = Double.parseDouble(operandBuilder.toString());
                operandStack.push(operand);
            } else if (ch == '(') {
                operatorStack.push(ch);
            } else if (ch == ')') {
                while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                    performOperation(operandStack, operatorStack);
                }
                if (!operatorStack.isEmpty() && operatorStack.peek() == '(') {
                    operatorStack.pop(); // Pop the '('
                } else {
                    throw new IllegalArgumentException("Invalid expression: Unmatched parentheses");
                }
            } else if (isOperator(ch)) {
                while (!operatorStack.isEmpty() && precedence(ch) <= precedence(operatorStack.peek())) {
                    performOperation(operandStack, operatorStack);
                }
                operatorStack.push(ch);
            } else if (ch == '!') {
                if (!operandStack.isEmpty()) {
                    double operand = operandStack.pop();
                    double result = factorial(operand);
                    operandStack.push(result);
                } else {
                    throw new IllegalArgumentException("Invalid expression: Missing operand for factorial");
                }
            } else if (ch == '^') {
                operatorStack.push(ch);
            } else if (ch == '|') {
                operatorStack.push(ch);
            } else {
                throw new IllegalArgumentException("Invalid character in expression: " + ch);
            }
        }

        while (!operatorStack.isEmpty()) {
            performOperation(operandStack, operatorStack);
        }

        if (operandStack.size() != 1 || !operatorStack.isEmpty()) {
            throw new IllegalArgumentException("Invalid expression: Unable to evaluate");
        }

        return operandStack.pop();
    }

    private static void performOperation(Stack<Double> operandStack, Stack<Character> operatorStack) {
        char operator = operatorStack.pop();
        if (operator == '^') {
            if (operandStack.size() >= 2) {
                double operand2 = operandStack.pop();
                double operand1 = operandStack.pop();
                double result = Math.pow(operand1, operand2);
                operandStack.push(result);
            } else {
                throw new IllegalArgumentException("Invalid expression: Insufficient operands for power operation");
            }
        } else if (operator == '|') {
            if (!operandStack.isEmpty()) {
                double operand = operandStack.pop();
                double result = Math.abs(operand);
                operandStack.push(result);
            } else {
                throw new IllegalArgumentException("Invalid expression: Missing operand for absolute value");
            }
        } else {
            if (operandStack.size() >= 2) {
                double operand2 = operandStack.pop();
                double operand1 = operandStack.pop();
                double result = 0;
                switch (operator) {
                    case '+':
                        result = operand1 + operand2;
                        break;
                    case '-':
                        result = operand1 - operand2;
                        break;
                    case '*':
                        result = operand1 * operand2;
                        break;
                    case '/':
                        if (operand2 == 0) {
                            throw new ArithmeticException("Division by zero");
                        }
                        result = operand1 / operand2;
                        break;
                }
                operandStack.push(result);
            } else {
                throw new IllegalArgumentException("Invalid expression: Insufficient operands for operator: " + operator);
            }
        }
    }

    private static int precedence(char operator) {
        if (operator == '^') {
            return 3;
        } else if (operator == '*' || operator == '/') {
            return 2;
        } else if (operator == '+' || operator == '-') {
            return 1;
        } else {
            return -1;
        }
    }

    private static boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }
}
