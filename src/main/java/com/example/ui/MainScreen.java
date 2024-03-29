package com.example.ui;

import com.example.GameManager;
import com.example.Utils;
import com.example.entity.Mob;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.graphics.TextRenderer;
import de.gurkenlabs.litiengine.gui.screens.GameScreen;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class MainScreen extends GameScreen implements IUpdateable {
  private Hud hud;

  public MainScreen() {
    super("main");
    hud = new Hud(0, 0);
    getComponents().add(hud);
    Game.graphics().onEntityRendered(e -> {
      if (e.getRenderedObject() instanceof Mob) {
        renderLifeBar((Mob) e.getRenderedObject(), e.getGraphics());
      }
    });
  }

  @Override
  public void update() {
    GameManager.update();
  }

  @Override
  public void render(Graphics2D g) {
    super.render(g);
    if (GameManager.gameover()) {
      // タワーだけになったらゲームオーバー
      String title = "game over:" + (GameManager.tower().isDead() ? "loose" : "win!!");
      g.setColor(Color.WHITE);
      g.setFont(Utils.fontBold());
      TextRenderer.renderWithOutline(g, title, Game.world().environment().getCenter(), Color.BLACK);
    }
  }

  public void renderLifeBar(Mob e, Graphics2D g) {
    if (!e.isDead()) {
      double hpRatio = e.getHitPoints().getCurrentValue().doubleValue() / e.getHitPoints().getMaxValue().doubleValue();
      double healthBarMaxWidth = e.getWidth();
      double healthBarWidth = healthBarMaxWidth * hpRatio;
      double healthBarHeight = 2.0D;
      double x = Game.world().camera()
                     .getViewportDimensionCenter(e).getX() - healthBarWidth / 2.0D;
      double y = Game.world().camera()
                     .getViewportDimensionCenter(e).getY() - (e.getHeight() * 3.0F / 4.0F);
      Point2D healthBarOrigin = new Point2D.Double(x, y);
      Rectangle2D rect = new Rectangle2D.Double(healthBarOrigin.getX(), healthBarOrigin.getY(), healthBarWidth, healthBarHeight);
      g.setColor(Color.BLACK);
      g.fill(new Rectangle2D.Double(rect.getX() - 1, rect.getY() - 1, healthBarMaxWidth + 2, rect.getHeight() + 2));
      g.setColor(hpRatio < 0.5 ? Color.RED : Color.GREEN);
      g.fill(rect);
    }
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
