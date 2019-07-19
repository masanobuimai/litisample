package com.example.entity;

import com.example.entity.ext.Charge;
import com.example.entity.ext.FloatingTextEmitter;
import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.annotation.CollisionInfo;
import de.gurkenlabs.litiengine.annotation.CombatInfo;
import de.gurkenlabs.litiengine.annotation.EntityInfo;
import de.gurkenlabs.litiengine.annotation.MovementInfo;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.graphics.OverlayPixelsImageEffect;
import de.gurkenlabs.litiengine.graphics.animation.IAnimationController;
import de.gurkenlabs.litiengine.graphics.emitters.Emitter;
import de.gurkenlabs.litiengine.graphics.emitters.FireEmitter;

import java.awt.*;

@EntityInfo(width = 18, height = 18)
@MovementInfo(velocity = 60)
@CollisionInfo(collisionBoxWidth = 8, collisionBoxHeight = 16, collision = true)
@CombatInfo(hitpoints = 100)
public abstract class Mob extends Creature implements IUpdateable {
  protected static final int LEFT = 0;
  protected static final int RIGHT = 1;

  private Charge charge;
  private Direction direction;

  public Mob(String name, Direction direction, int team) {
    super(name);
    this.charge = new Charge(this);
    this.direction = direction;

    setTeam(team);
    addHitListener(e -> {
      Game.world().environment().add(new FloatingTextEmitter(String.valueOf((int) e.getDamage()),
                                                             e.getEntity().getCenter(), Color.WHITE));
      IAnimationController controller = e.getEntity().getAnimationController();
      controller.add(new OverlayPixelsImageEffect(50, Color.RED));
      Game.loop().perform(50, () -> controller.add(new OverlayPixelsImageEffect(50, Color.WHITE)));
    });
    addDeathListener(e -> {
      Emitter emitter = new FireEmitter((int) e.getX(), (int) e.getY());
      emitter.setHeight(e.getHeight());
      emitter.setTimeToLive(1600);
      Game.world().environment().add(emitter);
      Game.world().environment().remove(e);
    });
  }

  @Override
  public void update() {
    if (isDead()) {
      return;
    }
    if (getX() < 4 || 300 < getX()) {
      die();
    }
    setAngle(direction.toAngle());
    charge.cast();
    Game.physics().move(this, this.getTickVelocity());
  }
}
