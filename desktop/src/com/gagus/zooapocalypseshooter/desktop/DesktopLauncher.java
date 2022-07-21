package com.gagus.zooapocalypseshooter.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gagus.zooapocalypseshooter.ZooApocalypseShooter;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		config.title = "Zoo Apocalyspe Shooter";
		new LwjglApplication(new ZooApocalypseShooter(), config);
	}
}
