package com.example;

import com.example.ui.MainScreen;
import com.example.ui.TitleScreen;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.Camera;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.resources.Resources;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

public class Program {
  public static void main(String[] args) {
    try (InputStream resource = Program.class.getResourceAsStream("/logging.properties")) {
      if (resource != null) {
        LogManager.getLogManager().readConfiguration(resource);
      }
    } catch (IOException ignore) {
    }
    Resources.load(Program.class.getClassLoader().getResource("game.litidata"));
    Game.init(args);
    Game.graphics().setBaseRenderScale(1.0f);

    Game.screens().add(new MainScreen());
    Game.screens().add(new TitleScreen());
    Input.keyboard().onKeyPressed(KeyEvent.VK_ESCAPE, e -> System.exit(0));

    Game.screens().display("title");
    Camera cam = new Camera();
    cam.setFocus(Game.world().environment().getCenter());
    Game.world().setCamera(cam);
    Game.start();
    System.out.println("finish");
  }
}
