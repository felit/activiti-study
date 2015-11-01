package com.activiti.demo;

import java.math.BigDecimal;

/**
 *
 */
public class TestJava {
    @org.testng.annotations.Test
    public void tt(){
        System.out.println(new BigDecimal(123456789.01).toString());
        System.out.println(Double.toString(new BigDecimal(Double.toString(123456789.01)).doubleValue()));
        System.out.println(new BigDecimal(Double.toString(123456789.01)).toString());
    }
}
