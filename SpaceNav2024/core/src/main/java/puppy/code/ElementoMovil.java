package puppy.code;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public abstract class ElementoMovil { // Implementa la interfaz
    private float x, y;  // Posición del elemento
    private float xSpeed, ySpeed; // Velocidades en x e y
    private Sprite spr;  // Sprite que representa el elemento
    private boolean destruido; // Estado de destrucción
    private boolean herido; // Estado herido (opcional)

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

    // Getters y setters para la posición X
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    // Getters y setters para la posición Y
    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    // Getters y setters para la velocidad en X
    public float getxSpeed() {
        return xSpeed;
    }

    public void setxSpeed(float xSpeed) {
        this.xSpeed = xSpeed;
    }

    // Getters y setters para la velocidad en Y
    public float getySpeed() {
        return ySpeed;
    }

    public void setySpeed(float ySpeed) {
        this.ySpeed = ySpeed;
    }

    // Getter para el Sprite
    public Sprite getSprite() {
        return spr;
    }

    public void setSprite(Sprite spr) {
        this.spr = spr;
    }

    public boolean isDestruido() {
        return destruido;
    }

    public void setDestruido(boolean destruido) {
        this.destruido = destruido;
    }

    public boolean isHerido() {
        return herido;
    }

    public void setHerido(boolean herido) {
        this.herido = herido;
    }

    public void marcarComoDestruido() {
        this.destruido = true;
    }

}
