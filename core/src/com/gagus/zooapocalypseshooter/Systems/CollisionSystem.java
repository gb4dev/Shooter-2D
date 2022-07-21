package com.gagus.zooapocalypseshooter.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gagus.zooapocalypseshooter.Components.AnimationComponent;
import com.gagus.zooapocalypseshooter.Components.CollisionComponent;
import com.gagus.zooapocalypseshooter.Components.PositionComponent;
import com.gagus.zooapocalypseshooter.Components.VelocityComponent;
import com.gagus.zooapocalypseshooter.Map.MapManager;
import com.gagus.zooapocalypseshooter.Map.Tile;

import java.util.ArrayList;

/**
 * Created by Gaetan on 21/07/2018.
 */

public class CollisionSystem extends EntitySystem {

	private ImmutableArray<Entity> entities;
	ComponentMapper<CollisionComponent> collisionM;
	ComponentMapper<PositionComponent> positionM;
	ComponentMapper<VelocityComponent> velocityM;
	ArrayList<Integer> diagonalMoves;
	MapManager mapManager;


	public CollisionSystem(MapManager mapManager, OrthographicCamera camera){
		collisionM = ComponentMapper.getFor(CollisionComponent.class);
		positionM = ComponentMapper.getFor(PositionComponent.class);
		velocityM = ComponentMapper.getFor(VelocityComponent.class);
		diagonalMoves = new ArrayList<Integer>();
		diagonalMoves.add(AnimationComponent.BOTTOMLEFT);
		diagonalMoves.add(AnimationComponent.BOTTOMRIGHT);
		diagonalMoves.add(AnimationComponent.TOPLEFT);
		diagonalMoves.add(AnimationComponent.TOPRIGHT);
		this.mapManager = mapManager;
	}

	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.all(CollisionComponent.class).get());
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		wallsCollisions(deltaTime);
	}

	private void wallsCollisions(float deltaTime){
		ArrayList<TiledMapTileLayer.Cell> cells = new ArrayList<TiledMapTileLayer.Cell>();
		for (Entity entity : entities) {
			//get mob infos
			CollisionComponent collisionComponent = collisionM.get(entity);
			PositionComponent positionComponent = positionM.get(entity);
			VelocityComponent velocityComponent = velocityM.get(entity);
			Rectangle playerRectangle = collisionComponent.rectCollision;
			Polygon playerPolygon = new Polygon(new float[] { 0, 0, playerRectangle.width, 0, playerRectangle.width, playerRectangle.height, 0, playerRectangle.height });
			playerPolygon.setPosition(playerRectangle.x, playerRectangle.y);
			Vector2 centerPlayer = new Vector2(playerRectangle.x+playerRectangle.width/2, playerRectangle.y+playerRectangle.height/2);
			Vector2 tilePlayer = mapManager.getTileWithPoint(centerPlayer);
			//Gdx.app.log("Collisionsystem", "center player :" + centerPlayer.toString());
			if(tilePlayer != null){
				//Gdx.app.log("Collisionsystem", "tile player :" + tilePlayer.toString());
				float startX = tilePlayer.x-1;
				float endX = tilePlayer.x+1;
				float startY = tilePlayer.y-1;
				float endY = tilePlayer.y+1;

				startX = startX < 0 ? 0 : startX;
				startY = startY < 0 ? 0 : startY;
				endX = endX>mapManager.mapWidth ? mapManager.mapWidth : endX;
				endY = endY>mapManager.mapHeight ? mapManager.mapHeight : endY;
				for(int y = (int)startY; y < endY; y++){
					for(int x = (int)startX; x < endX; x++) {
						Tile tile  = mapManager.getTile(x,y);
						Polygon poly = tile.polygon;
						if(tile.collidable){
							//Gdx.app.log("in tile collidable","");
							if (Intersector.overlapConvexPolygons(poly, playerPolygon)) {
								if(diagonalMoves.contains(velocityComponent.direction)) // check if velocity x and y > 0 for diagonal move (cause : map not 45 degrees), use direction to check
								{
									Rectangle justX = new Rectangle(playerRectangle);
									Rectangle justY = new Rectangle(playerRectangle);
									justX.x += AnimationComponent.directions[velocityComponent.direction].x * velocityComponent.velocityX*deltaTime;
									justY.y += AnimationComponent.directions[velocityComponent.direction].y * velocityComponent.velocityY*deltaTime;
									Polygon justXPoly = new Polygon(new float[] { 0, 0, justX.width, 0, justX.width, justX.height, 0, justX.height });
									Polygon justYPoly = new Polygon(new float[] { 0, 0, justY.width, 0, justY.width, justY.height, 0, justY.height });
									if(!Intersector.overlapConvexPolygons(justXPoly, poly)){
										positionComponent.y = justY.y;
									}
									if(!Intersector.overlapConvexPolygons(justYPoly, poly)){
										positionComponent.x = justX.x;
									}
								}
								else{
									velocityComponent.statetime = 0;
									positionComponent.x = positionComponent.oldX;
									positionComponent.y = positionComponent.oldY;
								}

							}
						}
					}
				}
			}

		}
	}

}
