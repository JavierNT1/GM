package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Ball2 extends ElementoMovil {
    private Sprite spr;
    protected final int daño; 

    public Ball2(int x, int y, int size, int xSpeed, int ySpeed, Texture tx) {
        super(x, y, xSpeed, ySpeed);
        this.daño = 1;
        spr = new Sprite(tx);
        
        // Margen de seguridad de 50 píxeles
        int margen = 50; 
    
        // Validación de posición para que la esfera no quede fuera del borde
        if (x - size < margen) this.x = margen + size;
        if (x + size > Gdx.graphics.getWidth() - margen) this.x = Gdx.graphics.getWidth() - margen - size;
        if (y - size < margen) this.y = margen + size;
        if (y + size > Gdx.graphics.getHeight() - margen) this.y = Gdx.graphics.getHeight() - margen - size;
    
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

     @Override
     public boolean colisionarCon(Colisionable elemento) {
         if (elemento instanceof Ball2) {
             Ball2 otraBola = (Ball2) elemento;
             if (this.obtenerArea().overlaps(otraBola.obtenerArea())) {
                 ajustarRebote(otraBola);
                 return true;
             }
         }
         return false;
     }
     
    @Override
    public void ajustarRebote(Colisionable elemento) {
        if (elemento instanceof Ball2) {
            Ball2 otraBola = (Ball2) elemento;
            float separacionMinima = spr.getWidth(); // Distancia mínima para que no se toquen
            float dx = otraBola.x - this.x;
            float dy = otraBola.y - this.y;
            float distanciaActual = (float) Math.sqrt(dx * dx + dy * dy);
        
            // Verificar si están superpuestos
            if (distanciaActual < separacionMinima) {
                // Corregir posiciones para establecer la separación mínima
                float ajuste = (separacionMinima - distanciaActual) / 2;
                float ajusteX = (dx / distanciaActual) * ajuste;
                float ajusteY = (dy / distanciaActual) * ajuste;
        
                this.x -= ajusteX;
                this.y -= ajusteY;
                otraBola.x += ajusteX;
                otraBola.y += ajusteY;
        
                // Actualizar posiciones del sprite después del ajuste
                this.spr.setPosition(this.x, this.y);
                otraBola.spr.setPosition(otraBola.x, otraBola.y);
            }
        
            // Proporción de masa (si tienen masas iguales, puedes omitir esto)
            float masaTotal = 2; // Suponiendo masa = 1 para ambos, puedes ajustar según el caso
        
            // Calculamos las nuevas velocidades (rebote elástico)
            int nuevaVelocidadX1 = (int) (((this.getxSpeed() * (1 - 1)) + (2 * otraBola.getxSpeed())) / masaTotal);
            int nuevaVelocidadY1 = (int) (((this.getySpeed() * (1 - 1)) + (2 * otraBola.getySpeed())) / masaTotal);
            int nuevaVelocidadX2 = (int) (((otraBola.getxSpeed() * (1 - 1)) + (2 * this.getxSpeed())) / masaTotal);
            int nuevaVelocidadY2 = (int) (((otraBola.getySpeed() * (1 - 1)) + (2 * this.getySpeed())) / masaTotal);
        
            // Establecemos las velocidades corregidas
            this.setxSpeed(nuevaVelocidadX1);
            this.setySpeed(nuevaVelocidadY1);
            otraBola.setxSpeed(nuevaVelocidadX2);
            otraBola.setySpeed(nuevaVelocidadY2);
        }
    }
       
    public int getDaño() {
        return daño; // Daño base para Ball2
    }

    @Override
    public Rectangle obtenerArea() {
    return spr.getBoundingRectangle();
    }

}
