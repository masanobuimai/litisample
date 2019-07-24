package com.example.ui;

import com.example.FontManager;
import com.example.Utils;
import com.example.entity.Tower;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.graphics.TextRenderer;
import de.gurkenlabs.litiengine.gui.screens.GameScreen;
import de.gurkenlabs.litiengine.input.Input;

import java.awt.*;
import java.awt.event.KeyEvent;

public class TitleScreen extends GameScreen implements IUpdateable {

  public TitleScreen() {
    super("title");
  }

  @Override
  public void update() {
  }

  @Override
  public void render(Graphics2D g) {
    super.render(g);
    g.setFont(FontManager.getBoldFont());
    TextRenderer.renderWithOutline(g, "push space key!!", Game.world().environment().getCenter(), Color.BLACK);
  }

  @Override
  public void prepare() {
    super.prepare();
    Game.loop().attach(this);
    Game.world().loadEnvironment("map");
    Input.keyboard().onKeyPressed(KeyEvent.VK_SPACE, e -> startGame());
  }

  private void startGame() {
    if (Game.screens().current().getName().equals("main")) return;

    Game.window().getRenderComponent().fadeOut(500);
    Game.loop().perform(600, () -> {
      Game.window().getRenderComponent().fadeIn(500);
      Game.screens().display("main");
      Utils.spawn("tower", Tower.instance());
    });
  }

  @Override
  public void suspend() {
    super.suspend();
    Game.loop().detach(this);
  }
}
