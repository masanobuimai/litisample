package com.example;

import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.annotation.AnimationInfo;
import de.gurkenlabs.litiengine.annotation.EntityInfo;
import de.gurkenlabs.litiengine.annotation.MovementInfo;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.ICollisionEntity;
import de.gurkenlabs.litiengine.entities.ICombatEntity;
import de.gurkenlabs.litiengine.graphics.emitters.Emitter;
import de.gurkenlabs.litiengine.graphics.emitters.FireEmitter;

@EntityInfo(width = 18, height = 18)
@MovementInfo(velocity = 70)
@AnimationInfo(spritePrefix = "Jorge")
public class Enemy extends Creature implements IUpdateable {
  public Enemy() {
    addDeathListener(e -> {
      Emitter emitter = new FireEmitter(10, 10);
      emitter.setTimeToLive(1000);
      Game.world().environment().add(emitter);
      Game.world().environment().remove(e);
    });
  }

  @Override
  public boolean canCollideWith(ICollisionEntity otherEntity) {
    return otherEntity instanceof ICombatEntity
           && ((ICombatEntity) otherEntity).getTeam() == this.getTeam() ? false : super.canCollideWith(otherEntity);
  }

  @Override
  public void update() {
    if (isDead()) {
      return;
    }
    if (getX() < 0) {
      die();
    }
    setAngle(Direction.LEFT.toAngle());
    Game.physics().move(this, this.getTickVelocity());
  }
}
