package com.lambdaschool.languages;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LanguageLog implements Serializable {
  private final String TEXT;
  private final String FDATE;

  public LanguageLog(String text) {
    Date date = new Date();
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");

    this.TEXT = text;
    this.FDATE = dateFormat.format(date);
  }
}
