package psp.c_calc2;

import java.util.Stack;

public class Calculadora {

    public static void main(String[] args) {
        System.out.println(evaluarExpresion("8-9"));
    }

    /**
     * Evaluar una expresion
     */
    public static double evaluarExpresion(String expresion) {
        // double stack para almacenar operandos
        Stack<Double> operandStack = new Stack<>();

        // char stack para almacenar operadores
        Stack<Character> operatorStack = new Stack<>();

        // insertar espacios alrededor: (, ), +, -, /, and *
        expresion = insertBlanks(expresion);

        // extraemos operandos y operadores
        String[] tokens = expresion.split(" ");

        // Fase 1: Leer tokens
        for (String token : tokens) {
            if (token.length() == 0) //  espacio
                continue; // volvemos al while a extraer el siguiente token
            else if (token.charAt(0) == '+' || token.charAt(0) == '-') {
                // procesamos todos los +, -, *, / en la cima de la pila de operadores
                while (!operatorStack.isEmpty() && (operatorStack.peek() == '+' || operatorStack.peek() == '-' || operatorStack.peek() == '*' || operatorStack.peek() == '/')) {
                    processAnOperator(operandStack, operatorStack);
                }

                // Push el operador + o - en la pila de operadores
                operatorStack.push(token.charAt(0));
            } else if (token.charAt(0) == '*' || token.charAt(0) == '/') {
                // procesamos  los *, / en la cima de la pila de operadores
                while (!operatorStack.isEmpty() && (operatorStack.peek() == '*' || operatorStack.peek() == '/')) {
                    processAnOperator(operandStack, operatorStack);
                }
                // Push el operador * o / en la pila de operadores
                operatorStack.push(token.charAt(0));
            } else if (token.trim().charAt(0) == '(') {
                operatorStack.push('('); // Push '(' to stack
            } else if (token.trim().charAt(0) == ')') {
                // procesamos los operadores en la pila hasta llegar a '('
                while (operatorStack.peek() != '(') {
                    processAnOperator(operandStack, operatorStack);
                }
                operatorStack.pop(); // Pop el '(' de la pila
            } else { // Un operando leido
                // Push un operando a la pila
                operandStack.push(new Double(token));
            }
        }

        // Fase 2: Procesar el resto de operandos en la pila
        while (!operatorStack.isEmpty()) {
            processAnOperator(operandStack, operatorStack);
        }
        return operandStack.pop();
    }

    /**
     * Procesar un operador: tomar un operando de la pila de operadores y aplicarlo a los operandos en la pila de
     * operandos
     */
    public static void processAnOperator(
        Stack<Double> operandStack, Stack<Character> operatorStack) {
        char op = operatorStack.pop();
        double op1 = operandStack.pop();
        double op2 = operandStack.pop();
        if (op == '+')
            operandStack.push(op2 + op1);
        else if (op == '-')
            operandStack.push(op2 - op1);
        else if (op == '*')
            operandStack.push(op2 * op1);
        else if (op == '/')
            operandStack.push(op2 / op1);
    }

    public static String insertBlanks(String s) {
        String result = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(' || s.charAt(i) == ')' || s.charAt(i) == '+' || s.charAt(i) == '-' || s.charAt(i) == '*' || s.charAt(i) == '/')
                result += " " + s.charAt(i) + " ";
            else
                result += s.charAt(i);
        }
        return result;
    }
}