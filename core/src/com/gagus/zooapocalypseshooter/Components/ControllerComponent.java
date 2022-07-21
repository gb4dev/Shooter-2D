package com.gagus.zooapocalypseshooter.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

/**
 * Created by Gaetan on 21/07/2018.
 */

public class ControllerComponent implements Component {
	public int plateform = 0;
	public static final int MOBILE = 0;
	public static final int COMPUTER = 1;
	public Touchpad joystick;
	public Vector2 joystickPosition;
	public int deadzoneTouchpad;
}
