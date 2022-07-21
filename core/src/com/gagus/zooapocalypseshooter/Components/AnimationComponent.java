package com.gagus.zooapocalypseshooter.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Gaetan on 20/07/2018.
 */

public class AnimationComponent implements Component {
	public TextureRegion[][] texturesAnimation;
	public Animation<TextureRegion>[] animations;
	public static final int LEFT = 7, TOPLEFT = 0, BOTTOMLEFT = 6, TOP = 1, BOTTOM = 5, TOPRIGHT = 2, BOTTOMRIGHT = 4, RIGHT = 3;
	public static final Vector2[] directions = new Vector2[]{new Vector2(-1,1), new Vector2(0,1),
		new Vector2(1, 1), new Vector2(1, 0), new Vector2(1, -1), new Vector2(0, -1), new Vector2(-1, -1), new Vector2(-1, 0)};
	public float imageWidth, imageHeight;
}
