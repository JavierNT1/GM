package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Nave4 extends ElementoMovil implements Colisionable{
    private boolean destruida = false;
    private boolean herido = false;
    private int tiempoHeridoMax = 50;
    private int tiempoHerido;
    private Sprite spr;
    private Sound sonidoHerido;
    private Sound soundBala;
    private Texture txBala;
    private int poderAtaque;
    private int vida;

    public Nave4(float x, float y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala, int vidaInicial) {
        super(x, y,0, 0);  // vida se pasa a través de Elemento
        this.sonidoHerido = soundChoque;
        this.soundBala = soundBala;
        this.txBala = txBala;
        this.spr = new Sprite(tx);
        spr.setPosition(x, y);
        spr.setBounds(x, y, 45, 45);
        this.vida = vidaInicial;
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
        if (x + xSpeed < 0 || x + xSpeed + spr.getWidth() > Gdx.graphics.getWidth()) xSpeed *= 0;
        if (y + ySpeed < 0 || y + ySpeed + spr.getHeight() > Gdx.graphics.getHeight()) ySpeed *= 0;
        
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

    @Override
    public void update() {
        if (!herido) {
            mover();  // Actualiza la posición de la nave si no está herida
        } else {
            tiempoHerido--;  // Disminuye el tiempo de herida
            if (tiempoHerido <= 0) herido = false;  // Sale del estado de herido si el tiempo llega a cero
        }
    }

    @Override
    public boolean colisionarCon(Colisionable elemento) {
        if (elemento instanceof Ball2) {
            Ball2 b = (Ball2) elemento;
    
            if (!herido && b.getArea().overlaps(spr.getBoundingRectangle())) {
                ajustarRebote(b); // Llamada a método que encapsula la lógica de rebote
    
                System.out.println("Daño recibido: " + b.getDaño());
                System.out.println("Vida antes de recibir daño: " + vida);
                vida -= b.getDaño();
                System.out.println("Vida después de recibir daño: " + vida);
    
                herido = true;
                tiempoHerido = tiempoHeridoMax;
                sonidoHerido.play();
    
                if (vida <= 0) destruida = true;
                return true;
            }
        }
        return false; // No hubo colisión
    }
    

    // Método privado para manejar la lógica de rebote entre objetos
    @Override
    public void ajustarRebote(Colisionable elemento) {
        if (elemento instanceof Ball2) {
            Ball2 b = (Ball2) elemento;

            if (xSpeed == 0) xSpeed += b.getxSpeed() / 3;
            if (b.getxSpeed() == 0) b.setxSpeed(b.getxSpeed() + xSpeed / 2);

            // Rebote en x
            xSpeed = -xSpeed;
            b.setxSpeed(-b.getxSpeed());

            if (ySpeed == 0) ySpeed += b.getySpeed() / 3;
            if (b.getySpeed() == 0) b.setySpeed(b.getySpeed() + ySpeed / 2);

            // Rebote en y
            ySpeed = -ySpeed;
            b.setySpeed(-b.getySpeed());
        }
    }

    @Override
    public Rectangle obtenerArea() {
        return spr.getBoundingRectangle(); // Implementación para obtener el área de colisión
    }

    

    // Método para incrementar vida
    public void incrementarVida(int cantidad) {
        this.vida += cantidad;
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

    @Override
    public float getX() {
        return (int) spr.getX();
    }
    
    @Override
    public float getY() {
        return (int) spr.getY();
    }

    public void setVidas(int vida) {
        this.vida = vida;
    }
}
