package com.td.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Monster {
    private Map map;
    private TextureRegion texture;
    private TextureRegion textureHp;
    private Vector2 position;
    private Vector2 velocity;
    private float speed;
    private boolean active;
    private Map.Route route;
    private int routeCounter;
    private int lastCellX, lastCellY;
    private float offsetX, offsetY;
    private int hp;
    private int hpMax;

    public boolean isActive() {
        return active;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Monster(TextureAtlas atlas, Map map, int routeIndex) {
        this.map = map;
        this.texture = atlas.findRegion("monster");
        this.textureHp = atlas.findRegion("monsterHp");
        this.speed = 100.0f;
        this.route = map.getRoutes().get(routeIndex);
        this.offsetX = MathUtils.random(10, 70);
        this.offsetY = MathUtils.random(10, 70);
        this.position = new Vector2(route.getStartX() * 80 + offsetX, route.getStartY() * 80 + offsetY);
        this.lastCellX = route.getStartX();
        this.lastCellY = route.getStartY();
        this.routeCounter = 0;
        this.velocity = new Vector2(route.getDirections()[0].x * speed, route.getDirections()[0].y * speed);
        this.hpMax = 5;
        this.hp = this.hpMax;
        this.active = false;
    }

    public boolean takeDamage(int dmg) {
        hp -= dmg;
        if (hp <= 0) {
            active = false;
            return true;
        }
        return false;
    }

    public void activate(int routeIndex) {
        this.offsetX = MathUtils.random(10, 70);
        this.offsetY = MathUtils.random(10, 70);
        this.route = map.getRoutes().get(routeIndex);
        this.position.set(route.getStartX() * 80 + offsetX, route.getStartY() * 80 + offsetY);
        this.lastCellX = route.getStartX();
        this.lastCellY = route.getStartY();
        this.routeCounter = 0;
        this.velocity.set(route.getDirections()[0].x * speed, route.getDirections()[0].y * speed);
        this.hpMax = 5;
        this.hp = this.hpMax;
        this.active = true;
        this.speed = MathUtils.random(80.0f, 120.0f);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - texture.getRegionWidth() / 2, position.y - texture.getRegionHeight() / 2, 40, 40,80,80,0.8f,0.8f,0);
        batch.draw(textureHp, position.x - 40, position.y + 60, ((float)hp / hpMax) * 80, 20);
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);

        int cx = (int) (position.x / 80);
        int cy = (int) (position.y / 80);

        float dx = Math.abs(cx * 80 + offsetX - position.x);
        float dy = Math.abs(cy * 80 + offsetY - position.y);

        if (map.isCrossroad(cx, cy) && Vector2.dst(0, 0, dx, dy) < velocity.len() * dt * 2) {
            if (!(lastCellX == cx && lastCellY == cy)) {
                position.set(cx * 80 + offsetX, cy * 80 + offsetY);
                routeCounter++;
                lastCellX = cx;
                lastCellY = cy;
                if (routeCounter > route.getDirections().length - 1) {
                    velocity.set(0, 0);
                    return;
                }
                velocity.set(route.getDirections()[routeCounter].x * speed, route.getDirections()[routeCounter].y * speed);
            }
        }
    }
}
