package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Asteroide extends ElementoMovil implements Colisiones {
    private Sprite spr;
    private final int daño; 
    private ColisionStrategy estrategia;

    public Asteroide(int x, int y, int size, int xSpeed, int ySpeed, Texture tx, ColisionStrategy estrategia) {
        super(x, y, xSpeed, ySpeed);
        this.daño = 1;
        this.estrategia = estrategia;
        spr = new Sprite(tx);
        
        // Margen de seguridad de 50 píxeles
        int margen = 50; 
    
        // Validación de posición para que la esfera no quede fuera del borde
        if (getX() - size < margen) setX(margen + size);
        if (getX() + size > Gdx.graphics.getWidth() - margen) setX(Gdx.graphics.getWidth() - margen - size);
        if (getY() - size < margen) setY(margen + size);
        if (getY() + size > Gdx.graphics.getHeight() - margen) setY(Gdx.graphics.getHeight() - margen - size);
    
        spr.setPosition(getX(), getY());
    }
        
    @Override
    public void update() {
        this.setX(this.getX() + getxSpeed());
        this.setY(this.getY() + getySpeed());

        // Rebote en los bordes de la pantalla
        if (getX() + getxSpeed() < 0 || getX() + getxSpeed() + spr.getWidth() > Gdx.graphics.getWidth()) {
            setxSpeed(getxSpeed() * -1);
        }
        if (getY() + getySpeed() < 0 || getY() + getySpeed() + spr.getHeight() > Gdx.graphics.getHeight()) {
            setySpeed(getySpeed() * -1);
        }
        spr.setPosition(getX(), getY());
    }

    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }

    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    @Override
    public void colisionar(Colisiones elemento) {
        estrategia.manejarColision(this, elemento);
    }

    @Override
    public void colisionarConAsteroide(Asteroide asteroide) {
        if (this.obtenerArea().overlaps(asteroide.obtenerArea())) {
            ajustarRebote(asteroide);
        }
    }
     
    public void ajustarRebote(Colisiones elemento) {
        Asteroide otroAsteroide = (Asteroide) elemento;
        float separacionMinima = spr.getWidth();
        float dx = otroAsteroide.getX() - this.getX();
        float dy = otroAsteroide.getY() - this.getY();
        float distancia = (float) Math.sqrt(dx * dx + dy * dy);

        if (distancia < separacionMinima) {
            // Corregir posición
            float ajuste = (separacionMinima - distancia) / 2;
            float ajusteX = dx / distancia * ajuste;
            float ajusteY = dy / distancia * ajuste;

            setX(getX() - ajusteX);
            setY(getY() - ajusteY);
            otroAsteroide.setX(otroAsteroide.getX() + ajusteX);
            otroAsteroide.setY(otroAsteroide.getY() + ajusteY);

            // Rebote elástico
            float vxTotal = getxSpeed() - otroAsteroide.getxSpeed();
            float vyTotal = getySpeed() - otroAsteroide.getySpeed();

            setxSpeed((int) (getxSpeed() - vxTotal));
            setySpeed((int) (getySpeed() - vyTotal));
            otroAsteroide.setxSpeed((int) (otroAsteroide.getxSpeed() + vxTotal));
            otroAsteroide.setySpeed((int) (otroAsteroide.getySpeed() + vyTotal));
        }
    }

    @Override
    public void colisionarConNave(Nave nave) {
        // Lógica de colisión con una nave
        nave.recibirDaño(this.getDaño());
    }
       
    public int getDaño() {
        return daño; // Daño base para Ball2
    }

    @Override
    public Rectangle obtenerArea() {
    return spr.getBoundingRectangle();
    }

}
