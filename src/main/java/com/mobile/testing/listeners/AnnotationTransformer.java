package com.mobile.testing.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

/** Adds the configured retry analyzer to every TestNG test method. */
public class AnnotationTransformer implements IAnnotationTransformer {
  @Override
  public void transform(
      ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
    if (annotation.getRetryAnalyzer() == null) {
      annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
  }
}
