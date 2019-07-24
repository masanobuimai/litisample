package com.example;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.Spawnpoint;

public class Utils {
  public static void spawn(String name, Creature entity) {
    Spawnpoint point = Game.world().environment().getSpawnpoint(name);
    point.spawn(entity);
    entity.setX(entity.getX() - (entity.getWidth() - point.getWidth()) / 2);
    entity.setY(point.getY() - entity.getHeight());
  }
}
