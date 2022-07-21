package com.gagus.zooapocalypseshooter.Screens;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.gagus.zooapocalypseshooter.Components.AnimationComponent;
import com.gagus.zooapocalypseshooter.Components.CollisionComponent;
import com.gagus.zooapocalypseshooter.Components.ControllerComponent;
import com.gagus.zooapocalypseshooter.Components.PositionComponent;
import com.gagus.zooapocalypseshooter.Components.VelocityComponent;
import com.gagus.zooapocalypseshooter.Entities.PlayerEntity;
import com.gagus.zooapocalypseshooter.Map.MapManager;
import com.gagus.zooapocalypseshooter.OrthoCamController;
import com.gagus.zooapocalypseshooter.Systems.CollisionSystem;
import com.gagus.zooapocalypseshooter.Systems.ControllerSystem;
import com.gagus.zooapocalypseshooter.Systems.RenderingSystem;
import com.gagus.zooapocalypseshooter.ZooApocalypseShooter;

/**
 * Created by Gaetan on 16/07/2018.
 */

public class GameScreen extends ScreenAdapter {
	ZooApocalypseShooter game;
	TiledMap map;
	OrthographicCamera camera;
	SpriteBatch batch;
	IsometricTiledMapRenderer renderer;
	Texture img;
	OrthoCamController cameraController;
	Texture playerImage;
	float posX;
	float posY;
	Stage stage;

	Engine engine;
	RenderingSystem renderingSystem;
	ControllerSystem controllerSystem;
	CollisionSystem collisionSystem;
	PlayerEntity playerEntity;

	ShapeRenderer shapeRenderer;

	MapManager mapManager;

	@Override
	public void render(float delta) {
		camera.update();
		//renderer.setView(camera);
		//renderer.render();

		mapManager.drawMap(); // display the map
		//mapManager.drawPolygons();
		engine.update(delta); // call systems
		playerEntity.drawRect(shapeRenderer, camera); // draw the collidable zone of player
		/*MapObjects objects = map.getLayers().get(2).getObjects();
		Array<RectangleMapObject> rectanglesObjects = objects.getByType(RectangleMapObject.class);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for(RectangleMapObject rectangleObject : rectanglesObjects){
			Rectangle rect = rectangleObject.getRectangle();
			batch.draw(playerImage,rect.x,rect.y,rect.width,rect.height);
		}
		batch.end();*/

		camera.update(); // update the camera
		/*MapObjects objects = map.getLayers().get(2).getObjects();
		Array<PolygonMapObject> rectanglesObjects = objects.getByType(PolygonMapObject.class);
		psb.setProjectionMatrix(camera.combined);
		psb.begin(ShapeRenderer.ShapeType.Line);
		for(PolygonMapObject rectangleObject : rectanglesObjects){
			Polygon rect = rectangleObject.getPolygon();
			psb.setColor(Color.RED);
			psb.polygon(rect.getVertices());
			psb.setColor(Color.GREEN);
			psb.polygon(rect.getTransformedVertices());
		}
		Rectangle playerRectangle = playerEntity.getComponent(CollisionComponent.class).rect;
		Polygon playerPolygon = new Polygon(new float[] { 0, 0, playerRectangle.width, 0, playerRectangle.width, playerRectangle.height, 0, playerRectangle.height });
		playerPolygon.setPosition(playerRectangle.x, playerRectangle.y);
		psb.polygon(playerPolygon.getTransformedVertices());
		psb.end();*/
		/*//batch.setProjectionMatrix(camera.combined);
		//batch.begin();
		//batch.draw(playerImage,posX, posY);
		//batch.end();*/

	}

	@Override
	public void resize(int width, int height) {
		game.viewport.update(width, height);
	}

	public GameScreen(ZooApocalypseShooter game) {
		Gdx.app.log("GameScreen","debut constructor");
		this.game = game;
		//map = new TmxMapLoader().load("ttt.tmx"); // loading map

		/*MapProperties prop = map.getProperties(); // get properties to get informations from map
		int mapWidth = prop.get("width", Integer.class);
		int mapHeight = prop.get("height", Integer.class);
		int tilePixelWidth = prop.get("tilewidth", Integer.class);
		int tilePixelHeight = prop.get("tileheight", Integer.class);
		int mapPixelWidth = mapWidth * tilePixelWidth;
		int mapPixelHeight = mapHeight * tilePixelHeight;
		Gdx.app.log("GameScreen map width in tiles", String.valueOf(mapWidth));
		Gdx.app.log("GameScreen map height in tiles", String.valueOf(mapHeight));
		Gdx.app.log("GameScreen tile width in pixels", String.valueOf(tilePixelWidth));
		Gdx.app.log("GameScreen tile heigth in pixels", String.valueOf(tilePixelHeight));
		Gdx.app.log("GameScreen map width in pixels", String.valueOf(mapPixelWidth));
		Gdx.app.log("GameScreen map height in pixels", String.valueOf(mapPixelHeight));*/

		//renderer = new IsometricTiledMapRenderer(map); // renderer of map
		batch = game.batch;
		camera = game.camera;
		//camera.position.set(mapPixelWidth/2,mapPixelHeight/2,0);
		//camera.position.set(mapPixelWidth/2f, 0,0); // set camera position ion the middle of the map
		camera.position.set(0, 0,0); // set camera position ion the middle of the map
		//camera.rotate(90);
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.zoom = 2;

		Gdx.app.log("gameScreen player position", posX+", "+posY);
		camera.update();
		Gdx.app.log("GameScreen camera position", camera.position.toString());
		//renderer.setView(camera);

		mapManager = new MapManager(camera, batch);
		mapManager.loadMap("map.json");


		//Camera controller
		//cameraController = new OrthoCamController(camera);
		//Gdx.input.setInputProcessor(cameraController);

		//dipslay player image
		playerImage = new Texture("player images/55.png");
		//Gdx.app.log("GameScreen player image", playerImage.toString());
		//playerImage = new Texture("badlogic.jpg");
		posX = mapManager.mapPixelsWidth/2f-playerImage.getWidth()/2;
		posY = playerImage.getHeight()/2;

		//set stage for inputs
		stage = new Stage(new FitViewport(game.WIDTH, game.HEIGHT));


		//creating ECS
		engine = new Engine();

		renderingSystem = new RenderingSystem(batch, camera);
		controllerSystem = new ControllerSystem(batch, camera, stage);
		collisionSystem = new CollisionSystem(mapManager, camera);


		engine.addSystem(controllerSystem);
		engine.addSystem(collisionSystem);
		engine.addSystem(renderingSystem);

		//create player entity

		playerEntity = new PlayerEntity(posX, posY, ControllerComponent.MOBILE, stage);

		//add entities to engine
		engine.addEntity(playerEntity);

		shapeRenderer = new ShapeRenderer();
	}

	@Override
	public void dispose() {
		map.dispose();
		super.dispose();
	}
}
