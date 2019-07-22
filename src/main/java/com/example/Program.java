package com.example;

import com.example.entity.Player;
import com.example.ui.MainScreen;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.Spawnpoint;
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
    Player player = new Player();
    Spawnpoint point = Game.world().environment().getSpawnpoint("respawn");
    point.spawn(player);
    player.setY(point.getY() - player.getHeight());

    Game.start();
    System.out.println("finish");
  }
}
