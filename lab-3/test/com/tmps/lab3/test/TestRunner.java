package com.tmps.lab3.test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("          TMPS Laboratory 3 Unit Test Suite (Behavioral Patterns)");
        System.out.println("================================================================================\n");

        List<Class<?>> testClasses = new ArrayList<>();
        testClasses.add(StrategyPatternTest.class);
        testClasses.add(StatePatternTest.class);
        testClasses.add(ObserverPatternTest.class);
        testClasses.add(CommandPatternTest.class);

        int totalTests = 0;
        int passedTests = 0;
        int failedTests = 0;

        long suiteStartTime = System.currentTimeMillis();

        for (Class<?> testClass : testClasses) {
            System.out.println(">> Running Test Suite: " + testClass.getSimpleName());
            Object testInstance;
            try {
                testInstance = testClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                System.err.println("Failed to instantiate " + testClass.getName() + ": " + e.getMessage());
                continue;
            }

            for (Method method : testClass.getDeclaredMethods()) {
                if (method.getName().startsWith("test")) {
                    totalTests++;
                    long testStart = System.nanoTime();
                    try {
                        method.invoke(testInstance);
                        long elapsedMs = (System.nanoTime() - testStart) / 1_000_000;
                        System.out.printf("   [PASS] %-65s (%d ms)%n", method.getName(), elapsedMs);
                        passedTests++;
                    } catch (Throwable ex) {
                        long elapsedMs = (System.nanoTime() - testStart) / 1_000_000;
                        Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                        System.out.printf("   [FAIL] %-65s (%d ms)%n", method.getName(), elapsedMs);
                        System.out.println("          Reason: " + cause.getMessage());
                        cause.printStackTrace(System.out);
                        failedTests++;
                    }
                }
            }
            System.out.println();
        }

        long totalElapsed = System.currentTimeMillis() - suiteStartTime;

        System.out.println("================================================================================");
        System.out.printf("Test Execution Summary: Total: %d | Passed: %d | Failed: %d | Time: %d ms%n",
                totalTests, passedTests, failedTests, totalElapsed);
        System.out.println("================================================================================");

        if (failedTests > 0) {
            System.exit(1);
        } else {
            System.out.println("ALL TESTS PASSED SUCCESSFULLY! (Behavioral pattern invariants verified)");
        }
    }
}
