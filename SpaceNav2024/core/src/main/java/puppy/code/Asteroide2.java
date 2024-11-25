/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Asteroide2 extends ElementoMovil implements Colisiones {
    private Sprite spr;
    private final int daño; 
    private ColisionStrategy estrategia;

    // Nuevos atributos de configuración
    private String tipo;
    private int tamaño;
    private double velocidad;
    
    // Constructor privado, solo accesible mediante el Builder
    private Asteroide2(AsteroideBuilder builder) {
        super(builder.x, builder.y, builder.xSpeed, builder.ySpeed);
        this.daño = builder.daño;
        this.estrategia = builder.estrategia;
        this.tipo = builder.tipo;
        this.tamaño = builder.tamaño;
        this.velocidad = builder.velocidad;
        spr = new Sprite(builder.texture);
        
        
        // Margen de seguridad de 50 píxeles
        int margen = 50; 
        
        // Validación de posición para que la esfera no quede fuera del borde
        if (getX() - tamaño < margen) setX(margen + tamaño);
        if (getX() + tamaño > Gdx.graphics.getWidth() - margen) setX(Gdx.graphics.getWidth() - margen - tamaño);
        if (getY() - tamaño < margen) setY(margen + tamaño);
        if (getY() + tamaño > Gdx.graphics.getHeight() - margen) setY(Gdx.graphics.getHeight() - margen - tamaño);
    
        spr.setPosition(getX(), getY());
    }

    public void setEstrategia(ColisionStrategy estrategia) {
        this.estrategia = estrategia;
    }
    
    
    // Getter para los nuevos atributos
    public String getTipo() { return tipo; }
    public int getTamaño() { return tamaño; }
    public double getVelocidad() { return velocidad; }
    public int getDaño() { return daño; }

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

    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    @Override
    public void colisionar(Colisiones elemento) {
        estrategia.manejarColision(this, elemento);
    }

    @Override
    public void colisionarConAsteroide(Asteroide2 asteroide) {
        if (this.obtenerArea().overlaps(asteroide.obtenerArea())) {
            ajustarRebote(asteroide);
        }
    }
    
    public void ajustarRebote(Colisiones elemento) {
        Asteroide2 otroAsteroide = (Asteroide2) elemento;
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
        nave.recibirDaño(this.getDaño());
    }
    
    @Override
    public Rectangle obtenerArea() {
        return spr.getBoundingRectangle();
    }

    public void setTexture(Texture texture) {
        spr.setTexture(texture);
    }
    
    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }
    
    // Clase interna para implementar el patrón Builder
    public static class AsteroideBuilder {
        private int x, y, xSpeed, ySpeed;
        private Texture texture;
        private ColisionStrategy estrategia;
        private int daño = 1;
        private String tipo = "Normal";
        private int tamaño = 3;
        private double velocidad = 1.0;

        // Constructor inicial para los parámetros obligatorios
        public AsteroideBuilder(int x, int y, int xSpeed, int ySpeed, Texture texture, ColisionStrategy estrategia) {
            this.x = x;
            this.y = y;
            this.xSpeed = xSpeed;
            this.ySpeed = ySpeed;
            this.texture = texture;
            this.estrategia = estrategia;
        }

        // Métodos para configurar los atributos opcionales
        public AsteroideBuilder setDaño(int daño) {
            this.daño = daño;
            return this;
        }

        public AsteroideBuilder setTipo(String tipo) {
            this.tipo = tipo;
            return this;
        }

        public AsteroideBuilder setTamaño(int tamaño) {
            this.tamaño = tamaño;
            return this;
        }

        public AsteroideBuilder setVelocidad(double velocidad) {
            this.velocidad = velocidad;
            return this;
        }

        // Método para construir el objeto final
        public Asteroide2 build() {
            return new Asteroide2(this);
        }
    }
}
