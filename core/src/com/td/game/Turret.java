package com.td.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Turret {
    private TowerDefenseGame game;
    private Map map;
    private TextureRegion texture;
    private Vector2 position;
    private float angle;
    private float range;
    private float fireDelay;
    private float fireTimer;
    private float rotationSpeed;
    private Monster target;

    private Vector2 tmpVector;

    public Turret(TextureAtlas atlas, TowerDefenseGame game, Map map, float cellX, float cellY) {
        this.texture = atlas.findRegion("turret");
        this.game = game;
        this.map = map;
        this.range = 300;
        this.rotationSpeed = 270.0f;
        this.fireDelay = 0.1f;
        this.position = new Vector2(cellX * 80 + 40, cellY * 80 + 40);
        this.angle = 0;
        this.tmpVector = new Vector2(0, 0);
    }

    public void setTurretToCell(int cellX, int cellY) {
        if (map.isCellEmpty(cellX, cellY)) {
            position.set(cellX * 80 + 40, cellY * 80 + 40);
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 40, position.y - 40, 40, 40, 80, 80, 1, 1, angle);
    }

    public boolean checkMonsterInRange(Monster monster) {
        return Vector2.dst(position.x, position.y, monster.getPosition().x, monster.getPosition().y) < range;
    }

    public void update(float dt) {
        if (target != null && (!checkMonsterInRange(target) || !target.isActive())) {
            target = null;
        }
        if (target == null) {
            Monster[] monsters = game.getMonsterEmitter().getMonsters();
            for (int i = 0; i < monsters.length; i++) {
                if (monsters[i].isActive() && checkMonsterInRange(monsters[i])) {
                    target = monsters[i];
                    break;
                }
            }
        }
        checkRotation(dt);
        tryToFire(dt);
    }

    public float getAngleToTarget() {
        return tmpVector.set(target.getPosition()).sub(position).angle();
    }

    public void checkRotation(float dt) {
        if (target != null) {
            float angleTo = getAngleToTarget();
            if (angle > angleTo) {
                if (Math.abs(angle - angleTo) <= 180.0f) {
                    angle -= rotationSpeed * dt;
                } else {
                    angle += rotationSpeed * dt;
                }
            }
            if (angle < angleTo) {
                if (Math.abs(angle - angleTo) <= 180.0f) {
                    angle += rotationSpeed * dt;
                } else {
                    angle -= rotationSpeed * dt;
                }
            }
            if (angle < 0.0f) {
                angle += 360.0f;
            }
            if (angle > 360.0f) {
                angle -= 360.0f;
            }
        }
    }

    public void tryToFire(float dt) {
        fireTimer += dt;
        if (target != null && fireTimer >= fireDelay && Math.abs(angle - getAngleToTarget()) < 15) {
            fireTimer = 0.0f;
            float time = 0.2f;
            float toX = target.getPosition().x + target.getVelocity().x * time;
            float toY = target.getPosition().y + target.getVelocity().y * time;
            game.getParticleEmitter().setupByTwoPoints(position.x, position.y, toX, toY, 0.5f, 1.2f, 1.5f, 1, 1, 0, 1, 1, 0, 0, 1);
            game.getParticleEmitter().setupByTwoPoints(position.x, position.y, toX + MathUtils.random(-10, 10), toY + MathUtils.random(-10, 10), 0.5f, 1.2f, 1.5f, 1, 1, 0, 1, 1, 0, 0, 1);
            target.takeDamage(1);
        }
    }
}
