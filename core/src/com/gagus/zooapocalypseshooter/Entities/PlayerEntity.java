package com.gagus.zooapocalypseshooter.Entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.gagus.zooapocalypseshooter.Components.AnimationComponent;
import com.gagus.zooapocalypseshooter.Components.CollisionComponent;
import com.gagus.zooapocalypseshooter.Components.ControllerComponent;
import com.gagus.zooapocalypseshooter.Components.PositionComponent;
import com.gagus.zooapocalypseshooter.Components.VelocityComponent;
import com.gagus.zooapocalypseshooter.ZooApocalypseShooter;

/**
 * Created by Gaetan on 20/07/2018.
 */

public class PlayerEntity extends Entity {
	PositionComponent positionComponent; // component for player position to draw it

	//animation component for every positions
	AnimationComponent animationComponent;
	Animation<TextureRegion> topLeftAnimation;
	Animation<TextureRegion> topAnimation;
	Animation<TextureRegion> topRightAnimation;
	Animation<TextureRegion> rightAnimation;
	Animation<TextureRegion> bottomRightAnimation;
	Animation<TextureRegion> bottomAnimation;
	Animation<TextureRegion> bottomLeftAmination;
	Animation<TextureRegion> leftAnimation;

	ControllerComponent controllerComponent;
	VelocityComponent velocityComponent;
	CollisionComponent collisionComponent;

	public PlayerEntity(float playerPositionX, float playerPositionY, int plateform, Stage stage){
		//create components
		animationComponent = new AnimationComponent();
		positionComponent = new PositionComponent();
		controllerComponent = new ControllerComponent();
		velocityComponent = new VelocityComponent();
		collisionComponent = new CollisionComponent();

		//set components
		//animation component
		animationComponent.texturesAnimation = new TextureRegion[8][6];
		animationComponent.animations = new Animation[8];
		velocityComponent.statetime = 0f;
		float frameDuration = 0.05f;

		//top left
		Array<TextureRegion> textures = new Array<TextureRegion>();
		for(int y=18; y< 24; y++){
			textures.add(new TextureRegion(new Texture("player images/"+String.valueOf(y)+".png")));
		}
		animationComponent.texturesAnimation[0] = textures.toArray(TextureRegion.class);
		topLeftAnimation = new Animation<TextureRegion>(frameDuration, textures, Animation.PlayMode.LOOP);

		//top
		textures = new Array<TextureRegion>();
		for(int y=12; y< 18; y++){
			textures.add(new TextureRegion(new Texture("player images/"+String.valueOf(y)+".png")));
		}
		animationComponent.texturesAnimation[1] = textures.toArray(TextureRegion.class);
		topAnimation = new Animation(frameDuration, textures, Animation.PlayMode.LOOP);

		//top left
		textures = new Array<TextureRegion>();
		for(int y=6; y< 12; y++){
			textures.add(new TextureRegion(new Texture("player images/"+String.valueOf(y)+".png")));
		}
		animationComponent.texturesAnimation[2] = textures.toArray(TextureRegion.class);
		topRightAnimation = new Animation(frameDuration, textures, Animation.PlayMode.LOOP);

		//top left
		textures = new Array<TextureRegion>();
		for(int y=0; y< 6; y++){
			textures.add(new TextureRegion(new Texture("player images/"+String.valueOf(y)+".png")));
		}
		animationComponent.texturesAnimation[3] = textures.toArray(TextureRegion.class);
		rightAnimation = new Animation(frameDuration, textures, Animation.PlayMode.LOOP);

		//top left
		textures = new Array<TextureRegion>();
		for(int y=36; y< 42; y++){
			textures.add(new TextureRegion(new Texture("player images/"+String.valueOf(y)+".png")));
		}
		animationComponent.texturesAnimation[4] = textures.toArray(TextureRegion.class);
		bottomRightAnimation = new Animation(frameDuration, textures, Animation.PlayMode.LOOP);

		//top left
		textures = new Array<TextureRegion>();
		for(int y=42; y< 48; y++){
			textures.add(new TextureRegion(new Texture("player images/"+String.valueOf(y)+".png")));
		}
		animationComponent.texturesAnimation[5] = textures.toArray(TextureRegion.class);
		bottomAnimation= new Animation(frameDuration, textures, Animation.PlayMode.LOOP);

		//top left
		textures = new Array<TextureRegion>();
		for(int y=30; y< 36; y++){
			textures.add(new TextureRegion(new Texture("player images/"+String.valueOf(y)+".png")));
		}
		animationComponent.texturesAnimation[6] = textures.toArray(TextureRegion.class);
		bottomLeftAmination = new Animation(frameDuration, textures, Animation.PlayMode.LOOP);

		//top left
		textures = new Array<TextureRegion>();
		for(int y=24; y< 30; y++){
			textures.add(new TextureRegion(new Texture("player images/"+String.valueOf(y)+".png")));
		}
		animationComponent.texturesAnimation[7] = textures.toArray(TextureRegion.class);
		leftAnimation = new Animation(frameDuration, textures, Animation.PlayMode.LOOP);

		animationComponent.animations[0] = topLeftAnimation;
		animationComponent.animations[1] = topAnimation;
		animationComponent.animations[2] = topRightAnimation;
		animationComponent.animations[3] = rightAnimation;
		animationComponent.animations[4] = bottomRightAnimation;
		animationComponent.animations[5] = bottomAnimation;
		animationComponent.animations[6] = bottomLeftAmination;
		animationComponent.animations[7] = leftAnimation;
		animationComponent.imageHeight = textures.get(0).getRegionHeight();
		animationComponent.imageWidth = textures.get(0).getRegionWidth();

		//position component
		positionComponent.x = playerPositionX;
		positionComponent.y = playerPositionY;

		//velocity component
		velocityComponent.direction = AnimationComponent.BOTTOM;
		velocityComponent.velocityX = 500;
		velocityComponent.velocityY = 500;

		//controller component
		controllerComponent.plateform = plateform;
		if(plateform == ControllerComponent.MOBILE){
			// Joystick creation
			Touchpad.TouchpadStyle stylepad = new Touchpad.TouchpadStyle(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/buttons/joystickBackground.png")))),new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/buttons/knob.png")))));
			controllerComponent.deadzoneTouchpad = 10;
			controllerComponent.joystick = new Touchpad(controllerComponent.deadzoneTouchpad,stylepad);
			controllerComponent.joystick.setSize(200,200);
			controllerComponent.joystickPosition = new Vector2(0,0);
			controllerComponent.joystick.setPosition(controllerComponent.joystickPosition.x,controllerComponent.joystickPosition.y);
			stage.getCamera().position.set(ZooApocalypseShooter.WIDTH/2-50, ZooApocalypseShooter.HEIGHT/2-50, 0);
			stage.getCamera().update();
			Gdx.app.log("in player ","joystick created");
			//Gdx.input.setInputProcessor();
			for(EventListener e : controllerComponent.joystick.getListeners())  stage.addListener(e);
			//stage.addActor(controllerComponent.joystick);
			Gdx.input.setInputProcessor(stage);
		}

		collisionComponent.rectCollision = new Rectangle(positionComponent.x,positionComponent.y, textures.get(0).getRegionWidth(), 40); // 30 = shadow/feets (ombre/pieds)


		this.add(animationComponent);
		this.add(positionComponent);
		this.add(controllerComponent);
		this.add(velocityComponent);
		this.add(collisionComponent);
	}

	public void drawRect(ShapeRenderer shapeRenderer, OrthographicCamera camera){ // draw the rect lines to debug
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.rect(positionComponent.x, positionComponent.y, collisionComponent.rectCollision.width, 40);
		shapeRenderer.end();
	}

}
