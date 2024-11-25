/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package puppy.code;

public class AsteroideColisionFuegoStrategy implements ColisionStrategy {
    @Override
    public void manejarColision(Colisiones colisionador, Colisiones colisionado) {
        if (colisionado instanceof Nave) {
            Nave nave = (Nave) colisionado;
            nave.recibirQuemadura(3); // Aplica quemaduras a la nave por 10 segundos (ajustar según lo que desees)
            nave.recibirDaño(((Asteroide2) colisionador).getDaño()); // También recibe daño
        } else if (colisionado instanceof Asteroide2) {
            colisionador.colisionarConAsteroide((Asteroide2) colisionado);
        }
    }
}