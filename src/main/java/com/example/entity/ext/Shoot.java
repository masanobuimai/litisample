package com.example.entity.ext;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.abilities.Ability;
import de.gurkenlabs.litiengine.abilities.effects.Effect;
import de.gurkenlabs.litiengine.abilities.effects.EffectTarget;
import de.gurkenlabs.litiengine.annotation.AbilityInfo;
import de.gurkenlabs.litiengine.entities.Creature;

import java.util.logging.Logger;

@AbilityInfo(name = "Shoot", cooldown = 60, duration = 60)
public class Shoot extends Ability {
  private static final Logger log = Logger.getLogger(Shoot.class.getName());

  private static int damage = 100;

  public Shoot(Creature executor) {
    super(executor);
    this.addEffect(new ShootEffect(this));
  }

  public class ShootEffect extends Effect {
    public ShootEffect(final Ability ability) {
      super(ability, EffectTarget.EXECUTINGENTITY);
    }

    @Override
    public void update() {
      super.update();
      Creature myEntity = this.getAbility().getExecutor();
      Game.world().environment()
          .findCombatEntities(myEntity.getCollisionBox(),
                              combatEntity -> myEntity.getTeam() != combatEntity.getTeam()).stream()
          .forEach(e -> e.hit(damage, getAbility()));
    }
  }
}

