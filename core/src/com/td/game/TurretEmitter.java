package com.td.game;

        import com.badlogic.gdx.graphics.g2d.SpriteBatch;
        import com.badlogic.gdx.graphics.g2d.TextureAtlas;
        import com.badlogic.gdx.math.Vector2;

public class TurretEmitter {
    private Turret [] turrets;
    private Vector2 vector2;



    public TurretEmitter(TextureAtlas atlas, TowerDefenseGame game, Map map, int maxSize){
        this.vector2 = new Vector2(0,0);
        this.turrets = new Turret[maxSize];
        for (int i = 0; i < turrets.length ; i++) {
            this.turrets[i] = new Turret(atlas, game, map, 0, 0 );
        }
    }

    public void createTurret(int cx, int cy){
        for (int i = 0; i < turrets.length ; i++) {
            if(!turrets[i].isActive()){
                turrets[i].activate(cx,cy);
                break;
            }
        }
    }
    public void destructionTurret(int cx, int cy){
        vector2.set(cx * 80 + 40, cy * 80 + 40);
        for (int i = 0; i < turrets.length ; i++) {
            if(turrets[i].isActive() && turrets[i].getPosition().equals(vector2)){
                turrets[i].deactivation();

            }
        }
    }
    public void render(SpriteBatch batch){
        for (int i = 0; i < turrets.length ; i++) {
            if(turrets[i].isActive()){
                turrets[i].render(batch);
            }
        }
    }
    public void update(float dt){
        for (int i = 0; i < turrets.length ; i++) {
            if(turrets[i].isActive()){
                turrets[i].update(dt);
            }
        }
    }
}
