package com.example.entity.ext;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.abilities.Ability;
import de.gurkenlabs.litiengine.abilities.effects.Effect;
import de.gurkenlabs.litiengine.abilities.effects.EffectTarget;
import de.gurkenlabs.litiengine.annotation.AbilityInfo;
import de.gurkenlabs.litiengine.entities.Creature;

@AbilityInfo(name = "Charge", cooldown = 1000, duration = 10)
public class Charge extends Ability {
  private static int damage = 20;
  private ChargeEffect chargeEffect;

  public Charge(Creature executor) {
    super(executor);

    this.chargeEffect = new ChargeEffect(this);
    this.addEffect(chargeEffect);
  }

  public class ChargeEffect extends Effect {
    public ChargeEffect(final Ability ability) {
      super(ability, EffectTarget.EXECUTINGENTITY);
    }

    @Override
    public void update() {
      Game.world().environment()
          .findCombatEntities(this.getAbility().getExecutor().getCollisionBox(),
                              combatEntity -> getAbility().getExecutor().getTeam() != combatEntity.getTeam())
          .forEach(e -> e.hit(damage, getAbility()));
      super.update();
    }
  }
}
