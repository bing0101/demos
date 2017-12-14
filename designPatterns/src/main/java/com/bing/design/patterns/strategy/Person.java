package com.bing.design.patterns.strategy;

/**
 * Created by bing on 2017/12/14.
 */
public class Person {

    public static void main(String[] args) {
        System.out.println(sum(1, 1));
        System.out.println(minus(1, 1));
    }

    private static int sum(int firstNumber, int secondNumber) {
        CalculateStrategy sum = new SumCalculateStrategy();
        Calculator calculator = new Calculator(sum);
        return calculator.calculate(firstNumber, secondNumber);
    }

    private static int minus(int firstNumber, int secondNumber) {
        CalculateStrategy minus = new MinusCalculateStrategy();
        Calculator calculator = new Calculator(minus);
        return calculator.calculate(firstNumber, secondNumber);
    }
}
