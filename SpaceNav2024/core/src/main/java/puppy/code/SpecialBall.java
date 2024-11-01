package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpecialBall extends Ball2 {
    static final float INCREMENTO_VELOCIDAD = 5.50f;
    private static final int VELOCIDAD_MAXIMA = 100; // Límite opcional de velocidad

    public SpecialBall(int x, int y, int size, int xSpeed, int ySpeed, Texture tx) {
        super(x, y, size, xSpeed, ySpeed, tx);
        setxSpeed((int) (xSpeed * 2.5));
        setySpeed((int) (ySpeed * 2.5));
    }

    @Override
    public void update() {
        // Aumenta gradualmente la velocidad de la bola especial
        int nuevoXSpeed = (int) (getxSpeed() * INCREMENTO_VELOCIDAD);
        int nuevoYSpeed = (int) (getySpeed() * INCREMENTO_VELOCIDAD);

        // Límite de velocidad
        setxSpeed(Math.min(nuevoXSpeed, VELOCIDAD_MAXIMA));
        setySpeed(Math.min(nuevoYSpeed, VELOCIDAD_MAXIMA));

        // Llama al método de actualización de Ball2
        super.update();
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }

    @Override
    public boolean colisionarCon(Colisionable elemento) {
        if (super.colisionarCon(elemento)) {
            // Aquí puedes agregar la lógica adicional específica para SpecialBall si es necesario.
            return true;
        }
        return false;
    }    

}