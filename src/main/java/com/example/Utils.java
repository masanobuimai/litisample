package com.example;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.Spawnpoint;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;

public class Utils {
  private static final HashMap<String, Font> fonts = new HashMap();

  public static void spawn(String name, Creature entity) {
    Spawnpoint point = Game.world().environment().getSpawnpoint(name);
    point.spawn(entity);
    entity.setX(entity.getX() - (entity.getWidth() - point.getWidth()) / 2);
    entity.setY(point.getY() - entity.getHeight());
  }

  public static void checkCorner(Creature entity) {
    Game.world().environment().getCollisionBoxes().stream()
        .filter(c -> c.getTags().contains("wall"))
        .filter(c -> entity.getHitBox().intersects(c.getCollisionBox()))
        .forEach(c -> entity.die());
  }

  public static Font fontPlain() {
    return getFont("PICO-8.ttf", 8.0f);
  }

  public static Font fontBold() {
    return getFont("PICO-8.ttf", 16.0f);
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
