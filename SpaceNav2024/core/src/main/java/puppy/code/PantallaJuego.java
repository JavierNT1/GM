package puppy.code;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PantallaJuego implements Screen {

    private SpaceNavigation game;
    private OrthographicCamera camera;    
    private SpriteBatch batch;
    private Sound explosionSound;
    private Music gameMusic;
    private int score;
    private int ronda;
    private int velXAsteroides; 
    private int velYAsteroides; 
    private int cantAsteroides;
    private Texture background;
    private Nave nave;
    private ArrayList<Asteroide2> balls1 = new ArrayList<>();
    private ArrayList<Asteroide2> balls2 = new ArrayList<>();
    private ArrayList<Bullet> balas = new ArrayList<>();

    public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int score,  
                         int velXAsteroides, int velYAsteroides, int cantAsteroides) {
        this.game = game;
        this.ronda = ronda;
        this.score = score;
        this.velXAsteroides = velXAsteroides;
        this.velYAsteroides = velYAsteroides;
        this.cantAsteroides = cantAsteroides;
        
        batch = game.getBatch();
        camera = new OrthographicCamera();    
        camera.setToOrtho(false, 800, 640);
        
        // Inicializar assets; música de fondo y efectos de sonido
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
        explosionSound.setVolume(1, 0.3f);
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav")); 
        background = new Texture(Gdx.files.internal("backgroundMoon.jpg"));
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.3f);
        gameMusic.play();
        
        // Cargar imagen de la nave, 64x64   
        nave = new Nave(Gdx.graphics.getWidth()/2 - 50, 30, 
                         new Texture(Gdx.files.internal("MainShip3.png")),
                         Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")), 
                         new Texture(Gdx.files.internal("Rocket2.png")), 
                         Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3")), 40); 
        nave.setVidas(vidas);
        
        // Crear asteroides
        crearAsteroides();
    }
    
    private void crearAsteroides() {
        Random r = new Random();
        DirectorAsteroide director = new DirectorAsteroide();

        // Crear asteroides normales
        for (int i = 0; i < cantAsteroides; i++) {
            int x = r.nextInt((int) Gdx.graphics.getWidth());
            int y = 50 + r.nextInt((int) Gdx.graphics.getHeight() - 50);
            int xSpeed = r.nextInt(5) - 2; // Velocidad aleatoria entre -2 y 2
            int ySpeed = r.nextInt(5) - 2;

            // Decide aleatoriamente el tipo de asteroide a crear (normal, hielo o fuego)
            int tipoAsteroide = r.nextInt(3); // 0 = normal, 1 = hielo, 2 = fuego
            Asteroide2 asteroide;

            if (tipoAsteroide == 0) {
                asteroide = director.construirAsteroideNormal(x, y, xSpeed, ySpeed);
                asteroide.setEstrategia(new AsteroideColisionStrategy()); // No hay efecto especial
            } else if (tipoAsteroide == 1) {
                asteroide = director.construirAsteroideHielo(x, y, xSpeed, ySpeed);
                asteroide.setEstrategia(new AsteroideColisionHieloStrategy()); // Congelamiento
            } else {
                asteroide = director.construirAsteroideFuego(x, y, xSpeed, ySpeed);
                asteroide.setEstrategia(new AsteroideColisionFuegoStrategy()); // Quemadura
            }

            balls1.add(asteroide);
            balls2.add(asteroide);
        }
    }

	    
    public void dibujaEncabezado() {
        CharSequence str = "Vidas: " + nave.getVidas() + " Ronda: " + ronda;
        game.getFont().getData().setScale(2f);        
        game.getFont().draw(batch, str, 10, 30);
        game.getFont().draw(batch, "Score: " + this.score, Gdx.graphics.getWidth() - 150, 30);
        game.getFont().draw(batch, "HighScore: " + game.getHighScore(), Gdx.graphics.getWidth() / 2 - 100, 30);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        // Dibuja el fondo
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        dibujaEncabezado();

        if (!nave.estaHerido()) {
            // Colisiones entre balas y asteroides y su destrucción  
            for (int i = 0; i < balas.size(); i++) {
                Bullet b = balas.get(i);
                b.update();
                for (int j = 0; j < balls1.size(); j++) {    
                    if (b.checkCollision(balls1.get(j))) {          
                        explosionSound.play();
                        balls1.remove(j);
                        balls2.remove(j);
                        j--;
                        score += 10;
                    }       
                }
                
                // Eliminar balas destruidas
                if (b.isDestroyed()) {
                    balas.remove(i);
                    i--; // para no saltarse 1 tras eliminar del ArrayList
                }
            }

            // Actualizar movimiento de asteroides dentro del área
            for (Asteroide2 ball : balls1) {
                ball.update();
            }

            // Colisiones entre asteroides y sus rebotes  
            for (int i = 0; i < balls1.size(); i++) {
                Asteroide2 ball1 = balls1.get(i);   
                for (int j = 0; j < balls2.size(); j++) {
                    Asteroide2 ball2 = balls2.get(j); 
                    if (i < j) {
                        ball1.colisionar(ball2);
                    }
                }
            } 
        }

        // Dibujar balas
        for (Bullet b : balas) {       
            b.draw(batch);
        }
        nave.draw(batch, this);

        // Dibujar asteroides y manejar colisión con nave
        for (int i = 0; i < balls1.size(); i++) {
            Asteroide2 b = balls1.get(i);
            b.draw(batch);
            // Verificar si nave perdió vida o game over
            if (nave.checkCollision(b)) {
                // Asteroide se destruye con el choque             
                balls1.remove(i);
                balls2.remove(i);
                i--;
            }       
        }
      
        // Manejo de game over
        if (nave.estaDestruido()) {
            if (score > game.getHighScore()) {
                game.setHighScore(score);
            }
            Screen ss = new PantallaGameOver(game);
            ss.resize(1200, 800);
            game.setScreen(ss);
            dispose();
        }
      
        // Nivel completado
        if (balls1.size() == 0) {
            nave.incrementarVida(5); // Incrementa la vida en 5 puntos
            Screen ss = new PantallaJuego(game, ronda + 1, nave.getVidas(), score, 
                                           velXAsteroides + 2, velYAsteroides + 2, 
                                           cantAsteroides + 10);
            ss.resize(1200, 800);
            game.setScreen(ss);
            dispose();
        }

        batch.end();
    }
    
    public boolean agregarBala(Bullet bb) {
        return balas.add(bb);
    }
    
    @Override
    public void show() {
        gameMusic.play();
    }

    @Override
    public void resize(int width, int height) {
        // Implementar si es necesario
    }

    @Override
    public void pause() {
        // Implementar si es necesario
    }

    @Override
    public void resume() {
        // Implementar si es necesario
    }

    @Override
    public void hide() {
        // Implementar si es necesario
    }

    @Override
    public void dispose() {
        this.explosionSound.dispose();
        this.gameMusic.dispose();
        this.background.dispose();
    }
}
