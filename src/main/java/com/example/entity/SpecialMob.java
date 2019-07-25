package com.example.entity;

import com.example.entity.ext.Shoot;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.annotation.CollisionInfo;
import de.gurkenlabs.litiengine.annotation.EntityInfo;
import de.gurkenlabs.litiengine.annotation.MovementInfo;
import de.gurkenlabs.litiengine.entities.Creature;

import java.util.logging.Logger;

@EntityInfo(width = 18, height = 18)
@MovementInfo(velocity = 260)
@CollisionInfo(collisionBoxWidth = 16, collisionBoxHeight = 18, collision = false)
public class SpecialMob extends Creature implements IUpdateable {
  private static final Logger log = Logger.getLogger(SpecialMob.class.getName());

  private Shoot shoot;

  public SpecialMob() {
    super("gurknukem");
    setTeam(0);
    shoot = new Shoot(this);
    addDeathListener(e -> {
      log.info(() -> e + " is dead...");
      Game.world().environment().remove(e);
    });
  }

  @Override
  public void update() {
    if (isDead()) {
      return;
    }
    Game.physics().move(this, this.getTickVelocity());
    Game.world().environment().getCollisionBoxes().stream()
        .filter(c -> c.getTags().contains("wall"))
        .filter(c -> getHitBox().intersects(c.getCollisionBox()))
        .forEach(c -> die());
    shoot.cast();
  }
}
