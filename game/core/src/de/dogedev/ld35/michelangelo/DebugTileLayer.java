package de.dogedev.ld35.michelangelo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

/** @brief Layer for a TiledMap */
public class DebugTileLayer extends TiledMapTileLayer {

	private int width;
	private int height;

	private float tileWidth;
	private float tileHeight;

	private String layerName;
	private Cell cell;
	private Cell collisionCell;

	/** @return layer's width in tiles */
	public int getWidth () {
		return width;
	}

	/** @return layer's height in tiles */
	public int getHeight () {
		return height;
	}

	/** @return tiles' width in pixels */
	public float getTileWidth () {
		return tileWidth;
	}

	/** @return tiles' height in pixels */
	public float getTileHeight () {
		return tileHeight;
	}

	/** Creates TiledMap layer
	 *
	 * @param tileWidth tile width in pixels
	 * @param tileHeight tile height in pixels */
	public DebugTileLayer(int tileWidth, int tileHeight, String layerName) {
		super(1, 1, tileWidth, tileHeight);
		this.width = Integer.MAX_VALUE;
		this.height = Integer.MAX_VALUE;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.layerName = layerName;
	}

	/** @param x X coordinate
	 * @param y Y coordinate
	 * @return {@link Cell} at (x, y) */
	public Cell getCell (int x, int y) {
		if(cell == null){
			cell = new Cell();
			cell.setTile(new StaticTiledMapTile(new TextureRegion(new Texture("debug_tile.png"))));
		}
		return cell;
	}

	/** Sets the {@link Cell} at the given coordinates.
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param cell the {@link Cell} to set at the given coordinates. */
	public void setCell (int x, int y, Cell cell) {
		//ChunkBuffer setCell(cell, x, y, layer) should exist here
	}

}