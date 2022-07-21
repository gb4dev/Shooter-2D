package com.gagus.zooapocalypseshooter.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.gagus.zooapocalypseshooter.Components.AnimationComponent;
import com.gagus.zooapocalypseshooter.Components.CollisionComponent;
import com.gagus.zooapocalypseshooter.Components.ControllerComponent;
import com.gagus.zooapocalypseshooter.Components.PositionComponent;
import com.gagus.zooapocalypseshooter.Components.VelocityComponent;


/**
 * Created by Gaetan on 21/07/2018.
 */

public class ControllerSystem extends EntitySystem {
	private ImmutableArray<Entity> entities;
	private OrthographicCamera cam; // a reference to our camera
	private SpriteBatch batch; // a reference to our spritebatch
	private Stage stage;

	private ComponentMapper<PositionComponent> positionM;
	private ComponentMapper<ControllerComponent> controllerM;
	private ComponentMapper<VelocityComponent> velocityM;
	private ComponentMapper<CollisionComponent> collisionM;

	public ControllerSystem(SpriteBatch batch, OrthographicCamera camera, Stage stage){
		this.batch = batch;  // set our batch to the one supplied in constructor
		this.cam = camera;
		this.stage = stage;

		//creates out componentMappers
		positionM = ComponentMapper.getFor(PositionComponent.class);
		velocityM = ComponentMapper.getFor(VelocityComponent.class);
		controllerM = ComponentMapper.getFor(ControllerComponent.class);
		collisionM = ComponentMapper.getFor(CollisionComponent.class);
	}

	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.all(ControllerComponent.class).get());
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		for (Entity entity : entities) {

			PositionComponent positionComponent = positionM.get(entity);
			VelocityComponent velocityComponent = velocityM.get(entity);
			ControllerComponent controllerComponent = controllerM.get(entity);
			CollisionComponent collisionComponent = collisionM.get(entity);

			//Gdx.app.log("controller system", "plateform "+controllerComponent.plateform);
			positionComponent.oldX = positionComponent.x;
			positionComponent.oldY = positionComponent.y;

			if(controllerComponent.plateform == ControllerComponent.MOBILE){
				//Gdx.app.log("controller system", "mobile");
				controllerComponent.joystick.act(deltaTime);
				if(controllerComponent.joystick.isTouched()){
					//Gdx.app.log("controller system", "player rect : "+collisionComponent.rectCollision);
					velocityComponent.statetime += deltaTime;
					float percentX = controllerComponent.joystick.getKnobPercentX();
					float percentY = controllerComponent.joystick.getKnobPercentY();
					if(percentY>0.20) {
						if(percentX>0.20f) {
							velocityComponent.direction = AnimationComponent.TOPRIGHT;
							positionComponent.x += velocityComponent.velocityX*deltaTime;
						}
						else if(percentX<-0.20f) {
							velocityComponent.direction = AnimationComponent.TOPLEFT;
							positionComponent.x -= velocityComponent.velocityX * deltaTime;
						}
						else{
							velocityComponent.direction = AnimationComponent.TOP;
						}
						positionComponent.y += velocityComponent.velocityY*deltaTime;
					}
					else if(percentY<-0.20) {
						if(percentX>0.20f) {
							velocityComponent.direction = AnimationComponent.BOTTOMRIGHT;
							positionComponent.x += velocityComponent.velocityX*deltaTime;
						}
						else if(percentX<-0.20f) {
							velocityComponent.direction = AnimationComponent.BOTTOMLEFT;
							positionComponent.x -= velocityComponent.velocityX*deltaTime;
						}
						else{
							velocityComponent.direction = AnimationComponent.BOTTOM;
						}
						positionComponent.y -= velocityComponent.velocityY*deltaTime;
					}
					else if(percentX>0) {
						velocityComponent.direction = AnimationComponent.RIGHT;
						positionComponent.x += velocityComponent.velocityX*deltaTime;
					}
					else if(percentX<0) {
						velocityComponent.direction = AnimationComponent.LEFT;
						positionComponent.x -= velocityComponent.velocityX*deltaTime;
					}
				}
				cam.update();
				//batch.setProjectionMatrix(cam.combined);
				stage.getCamera().update();
				stage.getBatch().setProjectionMatrix(stage.getCamera().combined);
				stage.getBatch().enableBlending();
				stage.getBatch().begin();
				//controllerComponent.joystick.setX(cam.position.x-Gdx.graphics.getWidth());
				//controllerComponent.joystick.setY(cam.position.y-Gdx.graphics.getHeight());
				controllerComponent.joystick.draw(stage.getBatch(), 1);
				stage.getBatch().end();
			}
			else {
				if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
					Gdx.app.log("controller system", "Z");
					if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
						positionComponent.x -= velocityComponent.velocityX * deltaTime;
						velocityComponent.direction = AnimationComponent.TOPLEFT;
						Gdx.app.log("controller system", "Z Qzqz");
					} else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
						positionComponent.x += velocityComponent.velocityX * deltaTime;
						velocityComponent.direction = AnimationComponent.TOPRIGHT;
					} else {
						velocityComponent.direction = AnimationComponent.TOP;
					}
					positionComponent.y += velocityComponent.velocityY * deltaTime;
					velocityComponent.statetime += deltaTime;
				} else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
					if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
						positionComponent.x -= velocityComponent.velocityX * deltaTime;
						velocityComponent.direction = AnimationComponent.BOTTOMLEFT;
					} else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
						positionComponent.x += velocityComponent.velocityX * deltaTime;
						velocityComponent.direction = AnimationComponent.BOTTOMRIGHT;
					} else {
						velocityComponent.direction = AnimationComponent.BOTTOM;
					}
					positionComponent.y -= velocityComponent.velocityY * deltaTime;
					velocityComponent.statetime += deltaTime;
				} else if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
					positionComponent.x -= velocityComponent.velocityX * deltaTime;
					velocityComponent.direction = AnimationComponent.LEFT;
					velocityComponent.statetime += deltaTime;
				} else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
					positionComponent.x += velocityComponent.velocityX * deltaTime;
					velocityComponent.direction = AnimationComponent.RIGHT;
					velocityComponent.statetime += deltaTime;
				}
			}
			collisionComponent.rectCollision.setPosition(positionComponent.x,positionComponent.y);
			cam.position.set(positionComponent.x, positionComponent.y, 0);// cam position is player position
		}
	}
}
