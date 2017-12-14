package com.bing.design.patterns.strategy;

/**
 * Created by bing on 2017/12/14.
 */
public class SumCalculateStrategy implements CalculateStrategy {
    public int calculate(int firstNumber, int secondNumber) {
        return firstNumber + secondNumber;
    }
}
