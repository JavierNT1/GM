package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Nave extends ElementoMovil implements Colisiones{
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
    private ColisionStrategy estrategia;
    private boolean congelada;   
    private int tiempoCongelado; 
    private boolean quemada;     
    private int tiempoQuemado;  
    private boolean puedeAccionar;  

    public Nave(float x, float y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala, int vidaInicial) {
        super(x, y,0, 0);  // vida se pasa a través de Elemento
        this.sonidoHerido = soundChoque;
        this.soundBala = soundBala;
        this.txBala = txBala;
        this.spr = new Sprite(tx);
        spr.setPosition(x, y);
        spr.setBounds(x, y, 45, 45);
        this.vida = vidaInicial;
        this.congelada = false;
        this.quemada = false;
        this.tiempoCongelado = 0;
        this.tiempoQuemado = 0;
        this.puedeAccionar = true;  
    }

    public void setEstrategia(ColisionStrategy estrategia) {
        this.estrategia = estrategia;
    }
    
    // Método para ser congelada
    public void recibirCongelamiento(int tiempo) {
        this.congelada = true;
        this.tiempoCongelado = tiempo;
        this.puedeAccionar = false;  
    }

    // Método para ser quemada
    public void recibirQuemadura(int tiempo) {
        this.quemada = true;
        this.tiempoQuemado = tiempo;
    }    
    
    // Método para actualizar el estado de la nave
    public void actualizarEstado() {
        if (congelada) {
            if (tiempoCongelado > 0) {
                tiempoCongelado--;
            } else {
                // Se descongela después de un tiempo
                congelada = false;
                puedeAccionar = true;
            }
        }

        if (quemada) {
            if (tiempoQuemado > 0) {
                // Reducir vida por quemadura
                vida -= 1; // Puedes ajustar la cantidad de daño por segundo aquí
                tiempoQuemado--;
            } else {
                quemada = false;
            }
        }
    }    
    
    public void draw(SpriteBatch batch, PantallaJuego juego) {
        if (!herido) {
            mover();
            actualizarEstado();
            spr.setPosition(getX() + getxSpeed(), getY() + getySpeed());
            spr.draw(batch);
        } else {
            gestionarHerido(batch);
        }
        disparar(juego);
    }

    private void mover() {
        if (puedeAccionar) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) setxSpeed(-3);
            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) setxSpeed(3);
            if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) setySpeed(-3);
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) setySpeed(3);

            // Mantener dentro de los bordes
            if (getX() + getxSpeed() < 0 || getX() + getxSpeed() + spr.getWidth() > Gdx.graphics.getWidth()) setxSpeed(0);
            if (getY() + getySpeed() < 0 || getY() + getySpeed() + spr.getHeight() > Gdx.graphics.getHeight()) setySpeed(0);

            // Actualizar la posición del sprite
            this.setX(this.getX() + getxSpeed());
            this.setY(this.getY() + getySpeed());
        }
    }

    private void disparar(PantallaJuego juego) {
        if(puedeAccionar){
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                Bullet bala = new Bullet(spr.getX() + spr.getWidth() / 2 - 5, spr.getY() + spr.getHeight() - 5, 0, 3, txBala);
                juego.agregarBala(bala);
                soundBala.play();
            }
        }
    }

    private void gestionarHerido(SpriteBatch batch) {
        spr.setX(spr.getX() + MathUtils.random(-2, 2));
        spr.draw(batch);
        spr.setX(getX()); // Restaurar posición X
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
        actualizarEstado(); // Llama a actualizar el estado de los efectos
    }

    @Override
    public void colisionar(Colisiones elemento) {
        if (elemento instanceof Asteroide2) {
            estrategia.manejarColision(this, elemento);
        }
    }

    @Override
    public void colisionarConAsteroide(Asteroide2 asteroide) {
        if (!herido && asteroide.obtenerArea().overlaps(obtenerArea())) {
            ajustarRebote(asteroide);
            vida -= asteroide.getDaño();

            System.out.println("Daño recibido: " + asteroide.getDaño());
            System.out.println("Vida después del daño: " + vida);

            herido = true;
            tiempoHerido = tiempoHeridoMax;
            sonidoHerido.play();

            if (vida <= 0) destruida = true;
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
        // Lógica para manejar la colisión con otra nave (si es necesario)
        System.out.println("Colisión entre naves.");
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

    public void setVidas(int vida) {
        this.vida = vida;
    }
    
    public void recibirDaño(int daño) {
        if (!herido) {
            vida -= daño;
            System.out.println("Daño recibido: " + daño);
            System.out.println("Vida después del daño: " + vida);

            herido = true;
            tiempoHerido = tiempoHeridoMax;
            sonidoHerido.play();

            if (vida <= 0) {
                destruida = true;
            }
        }
    }
    
    public boolean checkCollision(Asteroide2 b2) {
        if(spr.getBoundingRectangle().overlaps(b2.getArea())){
            recibirDaño(b2.getDaño());
            return true;

            }
        return false;
    }
    
    
}
