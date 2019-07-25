package com.example;

import de.gurkenlabs.litiengine.resources.Resources;

public class Program {
  public static void main(String[] args) {
    GameManager.start();

    System.out.println("-----");
    Resources.spritesheets().getAll().forEach(x -> System.out.println(x.getName()));
    System.out.println("-----");
    Resources.fonts().getAll().forEach(System.out::println);
    System.out.println("-----");
  }
}
