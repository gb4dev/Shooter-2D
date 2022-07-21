package com.gagus.zooapocalypseshooter.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;

import com.badlogic.gdx.math.Vector2;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Created by Gaetan on 27/07/2018.
 */

public class MapManager {
	public static int tileWidth, tileHeight, mapWidth, mapHeight, mapPixelsWidth, mapPixelsHeight;
	SpriteBatch batch;
	ShapeRenderer shapeRenderer;
	OrthographicCamera camera;

	Tile[][] map;
	public Polygon[][] polygons;
	//ArrayList collidableTiles;

	public int tileColumnOffset, tileRowOffset;

	public MapManager(int tileWidth, int tileHeight, int mapWidth, int mapHeight, OrthographicCamera camera, SpriteBatch batch){
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		this.mapPixelsWidth = tileWidth * mapWidth;
		this.mapPixelsHeight = tileHeight * mapHeight;
		this.batch = batch;
		shapeRenderer = new ShapeRenderer();
		this.camera = camera;
		map = new Tile[mapHeight][mapWidth];
		createPolygons();
		this.tileColumnOffset = tileWidth/2;
		this.tileRowOffset = tileHeight/2;
	}

	public MapManager(OrthographicCamera camera, SpriteBatch batch){
		this.batch = batch;
		shapeRenderer = new ShapeRenderer();
		this.camera = camera;
	}

	public void drawMap(){
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for(int y = 0 ; y< map.length; y++) {
			for (int x = map[y].length-1; x >= 0 ; x--) {
				if(map[y][x] != null){
					map[y][x].drawTile(batch);
				}
			}
		}
		batch.end();
	}

	public void drawPolygons(){
		//Color[] colors = new Color[]{ Color.BLUE, Color.BROWN, Color.YELLOW, Color.RED, Color.GREEN};
		//int a = 0;
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		//shapeRenderer.setColor(Color.BLACK);
		for(int y = 0 ; y< polygons.length; y++) {
			shapeRenderer.setColor(Color.DARK_GRAY);
			//a = ++a>=5?0:a;
			for (int x = 0; x < polygons[y].length; x++) {
				shapeRenderer.polygon(polygons[y][x].getTransformedVertices());
			}
		}
		//shapeRenderer.rect(100,100,200,200);
		shapeRenderer.end();
	}

	public void createPolygons(){
		/* map rectangulaire
		for(int y = 0 ; y< polygons.length; y++){
			for(int x = 0; x< polygons[y].length; x++){
				int posX = x*tileWidth - x*tileWidth/2;
				int posY = y*tileHeight + ((x%2)*tileHeight/2);
				float [] vectrices = new float[8];
				vectrices[0] = posX;
				vectrices[1] = posY + tileHeight/2;

				vectrices[2] = posX + tileWidth/2;
				vectrices[3] = posY + tileHeight;

				vectrices[4] = posX + tileWidth;
				vectrices[5] = posY + tileHeight/2;

				vectrices[6] = posX + tileWidth/2;
				vectrices[7] = posY;

				Polygon polygon = new Polygon(vectrices);
				polygon.setOrigin(posX, posY);
				polygons[y][x] = polygon;
			}
		}
		*/
		polygons = new Polygon[mapWidth][mapHeight];
		for(int y = 0 ; y< polygons.length; y++){
			for(int x = 0; x< polygons[y].length; x++){
				int posX = x*tileWidth/2 + y*tileWidth/2;
				int posY = x*tileHeight/2 - y*tileHeight/2;
				float [] vectrices = new float[8];
				vectrices[0] = posX;
				vectrices[1] = posY + tileHeight/2;

				vectrices[2] = posX + tileWidth/2;
				vectrices[3] = posY + tileHeight;

				vectrices[4] = posX + tileWidth;
				vectrices[5] = posY + tileHeight/2;

				vectrices[6] = posX + tileWidth/2;
				vectrices[7] = posY;

				Polygon polygon = new Polygon(vectrices);
				polygon.setOrigin(posX, posY);
				polygons[y][x] = polygon;
			}
		}

	}

	public void loadMap(String mapName){ //load map with name . the map have to be in asset folder
		String jsonText;
		try{
			String path = Gdx.files.getLocalStoragePath();
			FileHandle mapFile = Gdx.files.internal(mapName);
			jsonText = mapFile.readString();
		}
		catch(Exception e){
			Gdx.app.log("load map fail","error when open file");
			Gdx.app.log("load map fail",e.getMessage());
			return;
		}

		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		Map valeurs;
		try{
			valeurs = gson.fromJson(jsonText, Map.class);
		}
		catch(Exception e){
			Gdx.app.log("load map fail","error when reading json");
			Gdx.app.log("load map fail",e.getMessage());
			return;
		}

		float mapWidth = Float.parseFloat(valeurs.get("mapWidth").toString());
		float mapHeight = Float.parseFloat(valeurs.get("mapHeight").toString());
		float tileWidth = Float.parseFloat(valeurs.get("tileWidth").toString());
		float tileHeight = Float.parseFloat(valeurs.get("tileHeight").toString());
		Map images = (Map<String, Integer>)valeurs.get("imagesTiles");
		//ArrayList<ArrayList<String>> mapNumbers = (ArrayList<ArrayList<String>>)jsonObject.get("tileMap");
		Gdx.app.log(valeurs.get("tileMap").getClass().toString(),"");
		ArrayList mapNumbers = (ArrayList) valeurs.get("tileMap");
		Gdx.app.log("get objects json mapNumber", mapNumbers.toString());
		Tile[][] mapLoaded = new Tile[(int)mapHeight][(int)mapWidth]; // new map
		Texture[] textures = new Texture[images.size()];
		//set textures in array
		Gdx.app.log("load images", Gdx.files.getLocalStoragePath()+" | "+ Gdx.files.getExternalStoragePath());
		Set<String> keys = images.keySet();
		for(String key : keys){
			Texture texture = new Texture(key);
			textures[(int)Float.parseFloat(images.get(key).toString())] = texture;
		}
		ArrayList collidableTiles = (ArrayList)valeurs.get("collidableTiles");
		for(int y = 0 ; y< mapLoaded.length; y++) {
			for (int x = 0; x < mapLoaded[y].length; x++) {
				Gdx.app.log("create tile", "");
				//int tileImage = Integer.parseInt(mapNumbers.get(y).get(x));
				float tileImage = Float.parseFloat(((ArrayList)mapNumbers.get(y)).get(x).toString());
				if((int)tileImage != -1){
					boolean collidable = collidableTiles.contains(Double.parseDouble(String.valueOf(tileImage)));
					mapLoaded[y][x] = new Tile(x, y, (int)tileWidth, (int)tileHeight, textures[(int)tileImage], (String)images.get((int)tileImage), collidable);
				}
				else{
					mapLoaded[y][x] = null;
				}
			}
		}

		this.tileWidth = (int)tileWidth;
		this.tileHeight = (int)tileHeight;
		this.mapWidth = (int)mapWidth;
		this.mapHeight = (int)mapHeight;
		this.mapPixelsWidth = (int)tileWidth * (int)mapWidth;
		this.mapPixelsHeight = (int)tileHeight * (int)mapHeight;
		createPolygons();
		this.map = mapLoaded;
		this.tileColumnOffset = this.tileWidth/2;
		this.tileRowOffset = this.tileHeight/2;
	}

	public Vector2 getTileWithPoint(Vector2 pos){
		int tileX = Math.round(pos.x/tileColumnOffset + pos.y/tileRowOffset)/2; // get tile x
		int tileY = Math.round(pos.x/tileColumnOffset - pos.y/tileRowOffset)/2; // get tile y

		//Gdx.app.log("controller mouse position ","X : "+pos.x+" Y : "+pos.y);
		//Gdx.app.log("controller mouse colided ","X : "+tileX+" Y : "+tileY);
		if(tileY>=0 && tileY<= this.polygons.length && tileX>=0 && tileX<=this.polygons[0].length) { // if mouse over tile

			//methode de delimitation des points a tester selon la position de la souris
			int startX = tileX - 1;
			int startY = tileY - 1;
			int endX = tileX + 1;
			int endY = tileY + 1;
			//Gdx.app.log("controller mouse position ","X : "+posX+" Y : "+posY);
			//Gdx.app.log("controller mouse position ","X : "+pos.x+" Y : "+pos.y);
			startX = startX < 0 ? 0 : startX;
			startY = startY < 0 ? 0 : startY;
			endX = endX>=mapWidth ? mapWidth-1 : endX;
			endY = endY>=mapHeight ? mapHeight-1 : endY;

			//methode brute pour trouver la lite en fonction de la position de la sourie et de la tile trouvé aproximativement
			for (int y = startY; y <= endY; y++) {
				for (int x = startX; x <= endX; x++) {
					if (this.polygons[y][x].contains(pos.x, pos.y)) {
						//Gdx.app.log("controller mouse search tile ", "X : " + x + " Y : " + y);
						return new Vector2(x,y);
					}
				}
			}
		}
		return null;
	}

	public Tile getTile(int x, int y){
		if(x>=0 && x < mapWidth && y>=0 && y< mapHeight){
			return map[y][x];
		}
		return null;
	}
}
