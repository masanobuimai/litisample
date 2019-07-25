package com.example.entity.ext;

import com.example.entity.Mob;
import com.example.entity.Tower;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.abilities.Ability;
import de.gurkenlabs.litiengine.abilities.effects.Effect;
import de.gurkenlabs.litiengine.abilities.effects.EffectTarget;
import de.gurkenlabs.litiengine.annotation.AbilityInfo;
import de.gurkenlabs.litiengine.entities.Creature;

import java.util.Optional;
import java.util.logging.Logger;

@AbilityInfo(name = "Charge", cooldown = 260, duration = 60)
public class Charge extends Ability {
  private static final Logger log = Logger.getLogger(Charge.class.getName());

  private static int damage = 20;
  private ChargeEffect chargeEffect;

  private float baseVelocity;
  private Optional<Creature> enemy;

  public Charge(Creature executor) {
    super(executor);

    this.baseVelocity = executor.getVelocity().getCurrentValue();
    this.chargeEffect = new ChargeEffect(this);
    this.addEffect(chargeEffect);
    this.enemy = Optional.empty();
  }

  public boolean isEngage(Creature e) {
    // 自分の相手以外と戦闘中か？
    return enemy.isPresent() && enemy.get() != e;
  }

  public class ChargeEffect extends Effect {
    public ChargeEffect(final Ability ability) {
      super(ability, EffectTarget.EXECUTINGENTITY);
    }

    @Override
    public void update() {
      super.update();
      Creature myEntity = this.getAbility().getExecutor();
      if (!enemy.isPresent() || enemy.get().isDead()) {
        // 戦闘中でなければ，次の相手を探す
        enemy = Game.world().environment()
            .findCombatEntities(myEntity.getCollisionBox(),  // 接触してる相手チームで
                                combatEntity -> myEntity.getTeam() != combatEntity.getTeam()).stream()
            .map(e -> (Creature) e)
            .filter(e -> e instanceof Tower // タワーか
                         // 戦闘中ではない相手（自分の相手は対象にする）
                         || (e instanceof Mob && !((Mob) e).isEngage(myEntity)))
            .findFirst();
        myEntity.setVelocity(baseVelocity);
        enemy.ifPresent(e -> {
          log.info(() -> myEntity + " next=> " + e);
          if (e instanceof Tower) {
            // タワーだった場合，歩みをちょっと遅くして通り抜けさせる
            myEntity.setVelocity(baseVelocity * 0.7f);
          } else {
            // 戦闘中は歩みを遅くする（0にするとキャラの向きがリセットされるのでやらない）
            myEntity.setVelocity(0.0001f);
          }
        });
      }
      if (enemy.isPresent()) {
        enemy.ifPresent(e -> {
          log.info(() -> myEntity + " in battle=> " + e);
          if (e.isDead() || e.hit((int) (damage * Math.random()), getAbility())) {
            // 相手が死んだら，また歩み始める
            enemy = Optional.empty();
            myEntity.setVelocity(baseVelocity);
            log.info(() -> myEntity + " win!! ->" + myEntity.getVelocity().getCurrentValue());
          }
        });
      }
    }
  }
}
