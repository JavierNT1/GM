package puppy.code;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public abstract class ElementoMovil implements Colisionable { // Implementa la interfaz
    protected float x, y;  // Posición del elemento
    protected float xSpeed, ySpeed; // Velocidades en x e y
    protected Sprite spr;  // Sprite que representa el elemento
    protected boolean destruido; // Estado de destrucción
    protected boolean herido; // Estado herido (opcional)

    public ElementoMovil(float x, float y, float xSpeed, float ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.destruido = false;
        this.herido = false;
    }
    
    public ElementoMovil (float x, float y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public abstract void update();

    // Método abstracto para obtener el área de colisión del elemento
    public abstract Rectangle obtenerArea();

    // Método abstracto para manejar colisiones con otros elementos
    @Override
    public abstract boolean colisionarCon(Colisionable elemento);

    public boolean isDestruido() { return destruido; }
    public boolean isHerido() { return herido; }

    // Método para marcar el elemento como destruido
    public void marcarComoDestruido() {
        destruido = true;
    }

    // Getters y Setters para x, y, xSpeed, ySpeed
    public float getX() { return x; }
    public float getY() { return y; }
    public float getxSpeed() { return xSpeed; }
    public float getySpeed() { return ySpeed; }
    public void setxSpeed(float xSpeed) { this.xSpeed = xSpeed; }
    public void setySpeed(float ySpeed) { this.ySpeed = ySpeed; }


}
