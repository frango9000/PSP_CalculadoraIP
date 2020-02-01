package psp.c_calc2;

import static org.junit.Assert.assertEquals;

import java.util.EmptyStackException;
import java.util.Stack;
import org.junit.Test;

public class CalculadoraTest {

    @Test
    public void whenAddExpression_shouldAdd() {
        assertEquals(4, Calculadora.evaluarExpresion("2+2"), 0.001);
    }

    @Test
    public void whenSubtractExpression_shouldSubtract() {
        assertEquals(2, Calculadora.evaluarExpresion("10-8"), 0.001);
    }

    @Test
    public void whenMultiplyExpression_shouldMultiply() {
        assertEquals(15, Calculadora.evaluarExpresion("5*3"), 0.001);
    }

    @Test
    public void whenDivideExpression_shouldDivide() {
        assertEquals(5, Calculadora.evaluarExpresion("25/5"), 0.001);
    }

    @Test
    public void whenCorrectGrouping_shouldDoArithmetic() {
        assertEquals(-1, Calculadora.evaluarExpresion("(2*2)-(10/2)"), 0.001);
    }

    @Test(expected = EmptyStackException.class)
    public void whenIncorrectGrouping_shouldThrow() {
        assertEquals(-1, Calculadora.evaluarExpresion("(2*2)-(10/2"), 0.001);
    }

    @Test(expected = NumberFormatException.class)
    public void whenNonNumber_shouldThrow() {
        assertEquals(-1, Calculadora.evaluarExpresion("(2*2)-A"), 0.001);
    }

    @Test
    public void whenCorrectStacks_shouldDoArithmetic() {
        Stack<Double> operandStack = new Stack<>();
        operandStack.push(2.0);
        operandStack.push(8.0);
        Stack<Character> operatorStack = new Stack<>();
        operatorStack.push('*');
        Calculadora.processAnOperator(operandStack, operatorStack);
        assertEquals(16, operandStack.pop(), 0.001);
        assertEquals(0, operatorStack.size());
    }

    @Test
    public void whenString_shouldInsertBlanks() {
        assertEquals(Calculadora.insertBlanks("61)/*+*/)-568(9-*954)*-"), "61 )  /  *  +  *  /  )  - 568 ( 9 -  * 954 )  *  - ");
    }

}