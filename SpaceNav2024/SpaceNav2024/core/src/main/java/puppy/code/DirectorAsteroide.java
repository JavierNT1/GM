/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package puppy.code;
 
public class DirectorAsteroide {
    private AsteroideBuilder builder;

    public void setBuilder(AsteroideBuilder builder) {
        this.builder = builder;
    }

    public Asteroide1 construirAsteroideNormal() {
        builder.setTipo("Normal");
        builder.setTamaño("Medio");
        builder.setVelocidad(8);
        builder.setDaño(15);
        return builder.build();
    }

    public Asteroide1 construirAsteroideHielo() {
        builder.setTipo("Hielo");
        builder.setTamaño("Medio");
        builder.setVelocidad(8);
        builder.setDaño(15);
        return builder.build();
    }

    public Asteroide1 construirAsteroideFuego() {
        builder.setTipo("Fuego");
        builder.setTamaño("Medio");
        builder.setVelocidad(8);
        builder.setDaño(15);
        return builder.build();
    }
}

