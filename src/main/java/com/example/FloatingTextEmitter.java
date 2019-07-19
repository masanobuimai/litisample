package com.example;

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
    particleMinTTL = 900,
    particleMaxTTL = 900
)
public class FloatingTextEmitter extends Emitter {
  public final String text;
  public final Color color;

  public FloatingTextEmitter(String text, Point2D origin, Color color) {
    super(origin);
    this.text = text;
    this.color = color;
  }

  protected Particle createNewParticle() {
    TextParticle particle = new TextParticle(text, color, getParticleMaxTTL());
    particle.setY(-16f);
    particle.setDeltaIncY(-0.05f);
    particle.setFont(FontManager.getPlainFont());
    return particle;
  }
}
