//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;

public class FontManager {
  private static final HashMap<String, Font> fonts = new HashMap();

  public static Font getPlainFont() {
    return getFont("fsex300.ttf", 8.0f);
  }

  public static Font getBoldFont() {
    return getFont("04B_19.ttf", 8.0f);
  }

  private static Font getFont(String fontName, float fontSize) {
    if (fonts.containsKey(fontName)) {
      return fonts.get(fontName).deriveFont(fontSize);
    }
    try (InputStream fontStream = ClassLoader.getSystemResourceAsStream(fontName)) {
      Font font = Font.createFont(0, fontStream);
      GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
      fonts.put(fontName, font);
      return font.deriveFont(fontSize);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
