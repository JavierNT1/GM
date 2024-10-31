package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Ball2 extends Elemento {
    private Sprite spr;

    public Ball2(int x, int y, int size, int xSpeed, int ySpeed, Texture tx) {
        super(x, y, xSpeed, ySpeed);
        spr = new Sprite(tx);

        // Validación de posición para que la esfera no quede fuera del borde
        if (x - size < 0) this.x = x + size;
        if (x + size > Gdx.graphics.getWidth()) this.x = x - size;
        if (y - size < 0) this.y = y + size;
        if (y + size > Gdx.graphics.getHeight()) this.y = y - size;

        spr.setPosition(this.x, this.y);
    }
    
    @Override
    public void update() {
        x += getxSpeed();
        y += getySpeed();

        // Rebote en los bordes de la pantalla
        if (x + getxSpeed() < 0 || x + getxSpeed() + spr.getWidth() > Gdx.graphics.getWidth()) {
            setxSpeed(getxSpeed() * -1);
        }
        if (y + getySpeed() < 0 || y + getySpeed() + spr.getHeight() > Gdx.graphics.getHeight()) {
            setySpeed(getySpeed() * -1);
        }
        spr.setPosition(x, y);
    }

    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }

    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    public void checkCollision(Ball2 b2) {
        if (spr.getBoundingRectangle().overlaps(b2.getArea())) {
            // Rebote en caso de colisión
            if (getxSpeed() == 0) setxSpeed(getxSpeed() + b2.getxSpeed() / 2);
            if (b2.getxSpeed() == 0) b2.setxSpeed(b2.getxSpeed() + getxSpeed() / 2);
            setxSpeed(-getxSpeed());
            b2.setxSpeed(-b2.getxSpeed());

            if (getySpeed() == 0) setySpeed(getySpeed() + b2.getySpeed() / 2);
            if (b2.getySpeed() == 0) b2.setySpeed(b2.getySpeed() + getySpeed() / 2);
            setySpeed(-getySpeed());
            b2.setySpeed(-b2.getySpeed());
        }
    }
}
