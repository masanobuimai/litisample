package com.example.entity.ext;

import com.example.entity.Mob;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.abilities.Ability;
import de.gurkenlabs.litiengine.abilities.effects.Effect;
import de.gurkenlabs.litiengine.abilities.effects.EffectTarget;
import de.gurkenlabs.litiengine.annotation.AbilityInfo;
import de.gurkenlabs.litiengine.entities.Creature;

import java.util.Optional;
import java.util.logging.Logger;

@AbilityInfo(name = "Charge", cooldown = 1000, duration = 10)
public class Charge extends Ability {
  private static final Logger log = Logger.getLogger(Charge.class.getName());

  private static int damage = 20;
  private ChargeEffect chargeEffect;

  private float baseVelocity;
  private double baseAngle;
  private Optional<Mob> enemy;

  public Charge(Creature executor) {
    super(executor);

    this.chargeEffect = new ChargeEffect(this);
    this.addEffect(chargeEffect);
    this.enemy = Optional.empty();
  }

  public boolean isEngage(Creature e) {
    return enemy.isPresent() && enemy.get() != e;
  }

  public class ChargeEffect extends Effect {
    public ChargeEffect(final Ability ability) {
      super(ability, EffectTarget.EXECUTINGENTITY);
    }

    @Override
    public void update() {
      Creature myEntity = this.getAbility().getExecutor();
      if (!enemy.isPresent()) {
        // 戦闘中でなければ，次の相手を探す
        enemy = Game.world().environment()
            .findCombatEntities(myEntity.getCollisionBox(),
                                combatEntity -> myEntity.getTeam() != combatEntity.getTeam()).stream()
            .map(e -> (Mob) e)
            .filter(e -> !e.isEngage(myEntity))
            .findFirst();
        enemy.ifPresent(e -> {
          log.info("次の相手 " + e);
          baseAngle = myEntity.getAngle();
          baseVelocity = myEntity.getVelocity().getCurrentValue();
          log.info(myEntity + " v,a = " + baseVelocity + "," + baseAngle);
        });
      }
      if (enemy.isPresent()) {
        log.info("戦闘中 " + myEntity);
        myEntity.setVelocity(0);  // 戦闘中は止まる
        enemy.ifPresent(e -> {
          if (e.isDead() || e.hit((int) (damage * Math.random()), getAbility())) {
            log.info("相手をたおした " + myEntity);
            enemy = Optional.empty(); // 相手は死んじゃった
            myEntity.setVelocity(baseVelocity);
//            myEntity.setAngle(baseAngle);
          }
        });
      }
      super.update();
    }
  }
}
