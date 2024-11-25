/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package puppy.code;
 
public class AsteroideColisionHieloStrategy implements ColisionStrategy {
    @Override
    public void manejarColision(Colisiones colisionador, Colisiones colisionado) {
        if (colisionado instanceof Nave) {
            Nave nave = (Nave) colisionado;
            nave.recibirCongelamiento(3); // Congela la nave por 5 segundos (ajustar según lo que desees)
            nave.recibirDaño(((Asteroide2) colisionador).getDaño()); // También recibe daño
        } else if (colisionado instanceof Asteroide2) {
            colisionador.colisionarConAsteroide((Asteroide2) colisionado);
        }
    }
}