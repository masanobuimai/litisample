package com.example;

import com.example.entity.Enemy;
import com.example.entity.Player;
import com.example.entity.Tower;
import com.example.ui.MainScreen;
import com.example.ui.TitleScreen;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.Camera;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.resources.Resources;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class GameManager {
  private static final Logger log = Logger.getLogger(GameManager.class.getName());

  public enum GameState {
    READY, INGAME, GAMEOVER
  }

  private static GameState state;

  private static final int MAX_COUNT = 30;

  private static boolean timing() {
    return Game.loop().getTicks() % 30 == 0;
  }

  private static int count;

  private static Tower tower;

  public static Tower tower() {
    return tower;
  }

  public static void start() {
    try (InputStream resource = GameManager.class.getResourceAsStream("/logging.properties")) {
      if (resource != null) {
        LogManager.getLogManager().readConfiguration(resource);
      }
    } catch (IOException ignore) {
    }
    Resources.load(Program.class.getClassLoader().getResource("game.litidata"));

    Game.init();
    state = GameState.READY;
    Game.graphics().setBaseRenderScale(1.0f);

    Game.screens().add(new MainScreen());
    Game.screens().add(new TitleScreen());

    Game.screens().display("title");
    Camera cam = new Camera();
    cam.setFocus(Game.world().environment().getCenter());
    Game.world().setCamera(cam);

    initInputDevice();
    Game.start();
  }

  public static boolean gameover() {
    return state == GameState.GAMEOVER;
  }

  private static void initInputDevice() {
    Input.mouse().setGrabMouse(false);
    Input.keyboard().onKeyReleased(KeyEvent.VK_ESCAPE, e -> System.exit(0));
    Input.keyboard().onKeyReleased(KeyEvent.VK_SPACE, e -> startGame());
    Function<Runnable, Consumer<KeyEvent>> ke = r ->
        e -> { if (state == GameState.INGAME && !tower().isDead()) r.run(); };
    Input.keyboard().onKeyReleased(KeyEvent.VK_F2,
                                   ke.apply(() -> tower.consumePill()));
    Input.keyboard().onKeyReleased(KeyEvent.VK_F2,
                                   ke.apply(() -> tower.consumeShake()));
    Input.keyboard().onKeyReleased(KeyEvent.VK_F3,
                                   ke.apply(() -> tower.consumeShoot()));
  }


  private static void startGame() {
    log.info(() -> "state:" + state);
    if (state == GameState.INGAME) return;

    state = GameState.INGAME;
    count = 0;
    tower = new Tower();
    Game.window().getRenderComponent().fadeOut(500);
    Game.loop().perform(600, () -> {
      Game.window().getRenderComponent().fadeIn(500);
      Utils.spawn("tower", tower);
      Game.screens().display("main");
    });
  }

  public static void update() {
    if (timing() && count < MAX_COUNT) {
      Utils.spawn("spawn", new Enemy());
      Utils.spawn("respawn", new Player());
      count++;
    }
    if (count != 0 && Game.world().environment().getCombatEntities().size() == 1) {
      state = GameState.GAMEOVER;
    }
  }
}
