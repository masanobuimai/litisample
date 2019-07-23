package com.example.entity;

import com.example.entity.ext.Charge;
import com.example.entity.ext.FloatingTextEmitter;
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
@CollisionInfo(collisionBoxWidth = 16, collisionBoxHeight = 18, collision = false)
@CombatInfo(hitpoints = 100)
public abstract class Mob extends Creature implements IUpdateable {
  protected static final int LEFT_SIDE = 0;
  protected static final int RIGHT_SIDE = 1;

  private Charge charge;

  public Mob(String name, int team) {
    super(name);
    this.charge = new Charge(this);

    setTeam(team);
    addHitListener(e -> {
      setVelocity(0);   // 攻撃を受けても止まる
      Game.world().environment().add(new FloatingTextEmitter(String.valueOf((int) e.getDamage()),
                                                             e.getEntity().getCenter(), Color.WHITE));
      IAnimationController controller = e.getEntity().getAnimationController();
      controller.add(new OverlayPixelsImageEffect(50, Color.WHITE));
      Game.loop().perform(50, () -> controller.add(new OverlayPixelsImageEffect(50, Color.RED)));
    });
    addDeathListener(e -> {
      Emitter emitter = new FireEmitter((int) e.getX(), (int) e.getY());
      emitter.setHeight(e.getHeight());
      emitter.setTimeToLive(1600);
      Game.world().environment().add(emitter);
      Game.world().environment().remove(e);
    });
  }

  public boolean isEngage(Creature enemy) {
    return charge.isEngage(enemy);
  }

  @Override
  public void update() {
    if (isDead()) {
      return;
    }
    charge.cast();
    Game.physics().move(this, this.getTickVelocity());
    Game.world().environment().getCollisionBoxes().stream()
        .filter(c -> c.getTags().contains("wall"))
        .filter(c -> getHitBox().intersects(c.getCollisionBox()))
        .forEach(c -> die());
  }
}
