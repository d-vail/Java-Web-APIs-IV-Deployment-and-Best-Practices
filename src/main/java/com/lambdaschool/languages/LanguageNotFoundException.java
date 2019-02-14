package com.lambdaschool.languages;

public class LanguageNotFoundException extends RuntimeException {
  public LanguageNotFoundException(Long id) {
    super("Could not find language");
  }
}
