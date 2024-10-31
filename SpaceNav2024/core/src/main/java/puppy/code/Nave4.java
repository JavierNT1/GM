package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Nave4 extends Elemento {
    private int armadura; 
    private boolean destruida = false;
    private boolean herido = false;
    private int tiempoHeridoMax = 50;
    private int tiempoHerido;
    private Sprite spr;
    private Sound sonidoHerido;
    private Sound soundBala;
    private Texture txBala;

    public Nave4(float x, float y, int vida, int armadura, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala) {
        super(x, y, vida, 0, 0, 0);  // vida se pasa a través de Elemento
        this.sonidoHerido = soundChoque;
        this.soundBala = soundBala;
        this.txBala = txBala;
        this.spr = new Sprite(tx);
        this.armadura = armadura;  
        spr.setPosition(x, y);
        spr.setBounds(x, y, 45, 45);
    }

    public void draw(SpriteBatch batch, PantallaJuego juego) {
        if (!herido) {
            mover();
            spr.setPosition(x + xSpeed, y + ySpeed);
            spr.draw(batch);
        } else {
            gestionarHerido(batch);
        }
        disparar(juego);
    }

    private void mover() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) xSpeed--;
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) xSpeed++;
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) ySpeed--;
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) ySpeed++;

        // Mantener dentro de los bordes
        if (x + xSpeed < 0 || x + xSpeed + spr.getWidth() > Gdx.graphics.getWidth()) xSpeed *= -1;
        if (y + ySpeed < 0 || y + ySpeed + spr.getHeight() > Gdx.graphics.getHeight()) ySpeed *= -1;
        
        // Actualizar la posición del sprite
        x += xSpeed;
        y += ySpeed;
    }

    private void disparar(PantallaJuego juego) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            Bullet bala = new Bullet(spr.getX() + spr.getWidth() / 2 - 5, spr.getY() + spr.getHeight() - 5, 0, 3, txBala);
            juego.agregarBala(bala);
            soundBala.play();
        }
    }

    private void gestionarHerido(SpriteBatch batch) {
        spr.setX(spr.getX() + MathUtils.random(-2, 2));
        spr.draw(batch);
        spr.setX(x); // Restaurar posición X
        tiempoHerido--;
        if (tiempoHerido <= 0) herido = false;
    }

    public boolean checkCollision(Ball2 b) {
        if (!herido && b.getArea().overlaps(spr.getBoundingRectangle())) {
            // Rebote
            if (xSpeed == 0) xSpeed += b.getxSpeed() / 2;
            if (b.getxSpeed() == 0) b.setxSpeed(b.getxSpeed() + (int) xSpeed / 2);
            xSpeed = -xSpeed;
            b.setxSpeed(-b.getxSpeed());

            if (ySpeed == 0) ySpeed += b.getySpeed() / 2;
            if (b.getySpeed() == 0) b.setySpeed(b.getySpeed() + (int) ySpeed / 2);
            ySpeed = -ySpeed;
            b.setySpeed(-b.getySpeed());

            // Calcular el daño considerando la armadura
            int daño = Math.max(1, 10 - armadura); // La armadura reduce el daño (mínimo de 1)
            vida -= daño;
            herido = true;
            tiempoHerido = tiempoHeridoMax;
            sonidoHerido.play();

            if (vida <= 0) destruida = true;
            return true;
        }
        return false;
    }
    
    @Override
    public void update() {
        if (!herido) {
            mover();  // Actualiza la posición de la nave si no está herida
        } else {
            tiempoHerido--;  // Disminuye el tiempo de herida
            if (tiempoHerido <= 0) herido = false;  // Sale del estado de herido si el tiempo llega a cero
        }
    }

    // Método para incrementar vida
    public void incrementarVida(int cantidad) {
        this.vida += cantidad;
    }

    // Método para incrementar armadura
    public void incrementarArmadura(int cantidad) {
        this.armadura += cantidad;
    }

    public boolean estaDestruido() {
        return !herido && destruida;
    }

    public boolean estaHerido() {
        return herido;
    }

    public int getVidas() {
        return vida;
    }

    public int getX() {
        return (int) spr.getX();
    }

    public int getY() {
        return (int) spr.getY();
    }

    public void setVidas(int vida) {
        this.vida = vida;
    }
}
