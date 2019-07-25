package com.example.ui;

import com.example.Utils;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.graphics.TextRenderer;
import de.gurkenlabs.litiengine.gui.screens.GameScreen;

import java.awt.*;

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
    g.setFont(Utils.fontBold());
    TextRenderer.renderWithOutline(g, "push space key!!", Game.world().environment().getCenter(), Color.BLACK);
  }

  @Override
  public void prepare() {
    super.prepare();
    Game.loop().attach(this);
    Game.world().loadEnvironment("map");
  }

  @Override
  public void suspend() {
    super.suspend();
    Game.loop().detach(this);
  }
}
