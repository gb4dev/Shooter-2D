package com.gagus.zooapocalypseshooter.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gagus.zooapocalypseshooter.Components.AnimationComponent;
import com.gagus.zooapocalypseshooter.Components.PositionComponent;
import com.gagus.zooapocalypseshooter.Components.VelocityComponent;

/**
 * Created by Gaetan on 20/07/2018.
 */

public class RenderingSystem extends EntitySystem {
	private ImmutableArray<Entity> entities; // an array used to allow sorting of images allowing us to draw images on top of each other
	private OrthographicCamera cam; // a reference to our camera
	private SpriteBatch batch; // a reference to our spritebatch

	// component mappers to get components from entities
	private ComponentMapper<AnimationComponent> textureM;
	private ComponentMapper<PositionComponent> positionM;
	private ComponentMapper<VelocityComponent> velocityM;

	public RenderingSystem(SpriteBatch batch, OrthographicCamera camera) {

		//creates out componentMappers
		textureM = ComponentMapper.getFor(AnimationComponent.class);
		positionM = ComponentMapper.getFor(PositionComponent.class);
		velocityM = ComponentMapper.getFor(VelocityComponent.class);

		this.batch = batch;  // set our batch to the one supplied in constructor
		this.cam = camera;
	}

	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.all(AnimationComponent.class).get());
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		// update camera and sprite batch
		cam.update();
		batch.setProjectionMatrix(cam.combined);
		batch.enableBlending();
		batch.begin();

		// loop through each entity in our render queue
		for (Entity entity : entities) {

			AnimationComponent animation = textureM.get(entity);
			PositionComponent t = positionM.get(entity);
			VelocityComponent velocityComponent = velocityM.get(entity);

			//Gdx.app.log("rendering system", "entity");
			//Gdx.app.log("system rendering","frame number: "+String.valueOf(velocityComponent.statetime));
			TextureRegion region =  animation.animations[velocityComponent.direction].getKeyFrame(velocityComponent.statetime);

			batch.draw(region,t.x,t.y);
		}

		batch.end();
	}


}
