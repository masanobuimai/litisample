package com.example.entity.ext;

import com.example.Utils;
import de.gurkenlabs.litiengine.annotation.EmitterInfo;
import de.gurkenlabs.litiengine.graphics.emitters.Emitter;
import de.gurkenlabs.litiengine.graphics.emitters.particles.Particle;
import de.gurkenlabs.litiengine.graphics.emitters.particles.TextParticle;

import java.awt.*;
import java.awt.geom.Point2D;

@EmitterInfo(
    maxParticles = 1,
    spawnAmount = 1,
    emitterTTL = 900,
    particleMinTTL = 600,
    particleMaxTTL = 600
)
public class FloatingTextEmitter extends Emitter {
  private final String text;
  private final Color color;

  public FloatingTextEmitter(String text, Point2D origin, Color color) {
    super(origin);
    this.text = text;
    this.color = color;
  }

  protected Particle createNewParticle() {
    TextParticle particle = new TextParticle(text, color, getParticleMaxTTL());
    particle.setY(-16f);
    particle.setDeltaIncY(-0.05f);
    particle.setFont(Utils.fontPlain());
    return particle;
  }
}
