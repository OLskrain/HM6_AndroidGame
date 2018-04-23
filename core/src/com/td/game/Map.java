package com.td.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Map {
    public class Route {
        private int startX, startY;
        private Vector2[] directions;

        public int getStartX() {
            return startX;
        }

        public int getStartY() {
            return startY;
        }

        public Vector2[] getDirections() {
            return directions;
        }

        public Route(String str) {
            String[] data = str.split(",");
            startX = Integer.parseInt(data[0]);
            startY = Integer.parseInt(data[1]);
            directions = new Vector2[data[2].length()];
            for (int i = 0; i < data[2].length(); i++) {
                if (data[2].charAt(i) == 'L') {
                    directions[i] = new Vector2(-1, 0);
                }
                if (data[2].charAt(i) == 'R') {
                    directions[i] = new Vector2(1, 0);
                }
                if (data[2].charAt(i) == 'U') {
                    directions[i] = new Vector2(0, 1);
                }
                if (data[2].charAt(i) == 'D') {
                    directions[i] = new Vector2(0, -1);
                }
            }
        }
    }

    private int width;
    private int height;

    private byte[][] data;
    private List<Route> routes;
    private TextureRegion textureGrass;
    private TextureRegion textureRoad;
    private TextureRegion textureCursor;

    private int cursorCX, cursorCY;

    public List<Route> getRoutes() {
        return routes;
    }

    public Map(TextureAtlas atlas) {
        textureGrass = atlas.findRegion("grass");
        textureRoad = atlas.findRegion("road");
        textureCursor = atlas.findRegion("cursor");
        loadMap("map1");
    }

    public void loadMap(String mapName) {
        BufferedReader br = null;
        List<String> lines = new ArrayList<String>();

        try {
            br = Gdx.files.internal(mapName + ".dat").reader(8192);
            String str;
            while ((str = br.readLine()) != null) {
                lines.add(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).equals("routes")) {
                height = i;
                break;
            }
        }
        width = lines.get(0).split(",").length;
        data = new byte[width][height];
        for (int i = 0; i < height; i++) {
            String[] arr = lines.get(i).split(",");
            for (int j = 0; j < width; j++) {
                if (arr[j].equals("0")) {
                    data[j][height - i - 1] = 0;
                }
                if (arr[j].equals("1")) {
                    data[j][height - i - 1] = 1;
                }
                if (arr[j].equals("2")) {
                    data[j][height - i - 1] = 2;
                }
            }
        }
        routes = new ArrayList<Route>();
        for (int i = height + 1; i < lines.size(); i++) {
            routes.add(new Route(lines.get(i)));
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (data[i][j] == 0) {
                    batch.draw(textureGrass, i * 80, j * 80);
                }
                if (data[i][j] == 1 || data[i][j] == 2) {
                    batch.draw(textureRoad, i * 80, j * 80);
                }
            }
        }
        batch.setColor(1, 1, 1, 0.4f);
        batch.draw(textureCursor, cursorCX * 80, cursorCY * 80);
        batch.setColor(1, 1, 1, 1);
    }

    public void update(float dt) {
        cursorCX = (int)(Gdx.input.getX() / 80);
        cursorCY = (int)((720 - Gdx.input.getY()) / 80);
    }

    public boolean isCrossroad(int cx, int cy) {
        return data[cx][cy] == 2;
    }

    public boolean isCellEmpty(int cx, int cy) {
        return data[cx][cy] == 0;
    }
}
