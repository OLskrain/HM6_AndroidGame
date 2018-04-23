package com.td.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;

public class TowerDefenseGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Map map;
    private Turret turret;
    private MonsterEmitter monsterEmitter;
    private ParticleEmitter particleEmitter;
    private TextureAtlas atlas;

    // План работ:
    // Система экранов
    // Система ресурсов
    // Работа с интерфейсом
    // Работа со текстом(вывод текста, генерация шрифтов)
    // Подгонка под разные экраны
    // Звуки/музыка
    // --------------------
    // TurretEmitter
    // Подкорректировать систему частиц
    // Добавить домик
    // Добавить монетки
    // Улучшение пушек
    // Портирование на андроид

    public ParticleEmitter getParticleEmitter() {
        return particleEmitter;
    }

    public MonsterEmitter getMonsterEmitter() {
        return monsterEmitter;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        atlas = new TextureAtlas(Gdx.files.internal("game.pack"));
        map = new Map(atlas);
        turret = new Turret(atlas, this, map, 0, 0);
        monsterEmitter = new MonsterEmitter(atlas, map, 60);
        particleEmitter = new ParticleEmitter(atlas.findRegion("star16"));
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        update(dt);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        map.render(batch);
        turret.render(batch);
        monsterEmitter.render(batch);
        particleEmitter.render(batch);
        batch.end();
    }

    public void update(float dt) {
        map.update(dt);
        monsterEmitter.update(dt);
        turret.update(dt);
        particleEmitter.update(dt);
        if (Gdx.input.justTouched()) {
            int cx = (int) (Gdx.input.getX() / 80);
            int cy = (int) ((720 - Gdx.input.getY()) / 80);
            turret.setTurretToCell(cx, cy);
        }
        particleEmitter.checkPool();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
