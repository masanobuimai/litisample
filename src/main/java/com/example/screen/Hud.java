package com.example.screen;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.GuiComponent;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Hud extends GuiComponent {
  public static Color hudRedColor = new Color(188, 12, 12);
  public static Color shadowColor = new Color(48, 48, 48, 200);

  public Hud(double x, double y) {
    super(x, y);
  }

  public void render(Graphics2D g) {
    this.renderHealthBar(g);
  }

  private void renderHealthBar(Graphics2D g) {
    double screenWidth = Game.screens().current().getWidth();
    double screenHeight = Game.screens().current().getHeight();
    double healthBarMaxWidth = screenWidth * 12.0D / 64.0D;
    double healthBarHeight = screenHeight * 2.0D / 64.0D;
    double healthBarX = screenWidth / 2.0D - healthBarMaxWidth / 2.0D;
    double healthBarY = screenHeight * 58.0D / 64.0D;
    double currentHealthRatio = 0.5d;
    Rectangle2D healthShadowRect = new Rectangle2D.Double(healthBarX - 2.0D, healthBarY - 2.0D,
                                                          healthBarMaxWidth + 4.0D, healthBarHeight + 4.0D);
    Rectangle2D healthRect = new Rectangle2D.Double(healthBarX, healthBarY,
                                                    currentHealthRatio * healthBarMaxWidth, healthBarHeight);
    g.setColor(shadowColor);
    g.fill(healthShadowRect);
    g.setColor(hudRedColor);
    g.fill(healthRect);
  }
}
