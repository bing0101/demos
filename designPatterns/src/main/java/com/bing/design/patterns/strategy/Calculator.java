package com.bing.design.patterns.strategy;

/**
 * Created by bing on 2017/12/14.
 */
public class Calculator {
    private CalculateStrategy calculateStrategy;

    Calculator(CalculateStrategy calculateStrategy) {
        this.calculateStrategy = calculateStrategy;
    }

    public int calculate(int firstNumber, int secondNumber) {
        return calculateStrategy.calculate(firstNumber, secondNumber);
    }
}
