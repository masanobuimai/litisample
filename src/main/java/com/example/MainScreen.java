package com.example;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.Spawnpoint;
import de.gurkenlabs.litiengine.graphics.RenderEngine;
import de.gurkenlabs.litiengine.gui.screens.GameScreen;

import java.awt.*;

public class MainScreen extends GameScreen implements IUpdateable {
  private Hud hud;

  private int count;

  public MainScreen() {
    super("main");
    hud = new Hud(0, 0);
    getComponents().add(hud);
  }

  @Override
  public void update() {
    if (Game.loop().getTicks() % 30 == 0 && count < 10) {
      Enemy enemy = new Enemy();
      Spawnpoint point = Game.world().environment().getSpawnpoint("spawn");
      point.spawn(enemy);
      count++;
    }
  }

  @Override
  public void render(Graphics2D g) {
    super.render(g);
    RenderEngine.render(g, Game.world().environment().getEmitters());
  }

  @Override
  public void prepare() {
    super.prepare();
    Game.loop().attach(this);
  }

  @Override
  public void suspend() {
    super.suspend();
    Game.loop().detach(this);
  }
}
