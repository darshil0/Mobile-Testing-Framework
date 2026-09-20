package com.mobile.testing.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

/** Adds the configured retry analyzer to every TestNG test method. */
public class AnnotationTransformer implements IAnnotationTransformer {
  @Override
  @SuppressWarnings("rawtypes")
  public void transform(
      ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
    if (annotation.getRetryAnalyzerClass() == null) {
      annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
  }
}
