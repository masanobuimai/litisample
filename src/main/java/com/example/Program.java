package com.example;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.Camera;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.resources.Resources;

import java.awt.event.KeyEvent;

public class Program {
  public static void main(String[] args) {
    Game.init(args);
    Game.graphics().setBaseRenderScale(2.0f);

    Game.screens().add(new MainScreen());
    Resources.load("game.litidata");
    Game.world().loadEnvironment("map");
    Input.keyboard().onKeyPressed(KeyEvent.VK_ESCAPE, e -> System.exit(0));

    Camera cam = new Camera();
    cam.setFocus(Game.world().environment().getCenter());
    Game.world().setCamera(cam);
    Game.world().setGravity(120);

    Game.start();
  }
}
