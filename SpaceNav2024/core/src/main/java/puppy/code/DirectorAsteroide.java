/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package puppy.code;
import com.badlogic.gdx.graphics.Texture;

public class DirectorAsteroide {
    public Asteroide2 construirAsteroideNormal(int x, int y, int xSpeed, int ySpeed) {
        return new Asteroide2.AsteroideBuilder(x, y, xSpeed, ySpeed, new Texture("aGreyMedium4.png"), new AsteroideColisionStrategy())
                .setTipo("Normal")
                .setTamaño(3) // Tamaño en píxeles
                .setVelocidad(6.0)
                .setDaño(2)
                .build();
    }

    public Asteroide2 construirAsteroideHielo(int x, int y, int xSpeed, int ySpeed) {
        return new Asteroide2.AsteroideBuilder(x, y, xSpeed, ySpeed, new Texture("bGreyMedium4.png"), new AsteroideColisionHieloStrategy())
                .setTipo("Hielo")
                .setTamaño(3)
                .setVelocidad(4.0)
                .setDaño(1)
                .build();
    }

    public Asteroide2 construirAsteroideFuego(int x, int y, int xSpeed, int ySpeed) {
        return new Asteroide2.AsteroideBuilder(x, y, xSpeed, ySpeed, new Texture("cGreyMedium4.png"), new AsteroideColisionFuegoStrategy())
                .setTipo("Fuego")
                .setTamaño(3)
                .setVelocidad(4.0)
                .setDaño(1)
                .build();
    }
}


