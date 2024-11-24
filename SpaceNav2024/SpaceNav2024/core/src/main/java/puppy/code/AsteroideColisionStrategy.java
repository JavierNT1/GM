/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package puppy.code;
 
public class AsteroideColisionStrategy implements ColisionStrategy {
    @Override
    public void manejarColision(Colisiones colisionador, Colisiones colisionado) {
        if (colisionado instanceof Nave) {
            Nave nave = (Nave) colisionado;
            nave.recibirDaño(((Asteroide) colisionador).getDaño());
        } else if (colisionado instanceof Asteroide) {
            colisionador.colisionarConAsteroide((Asteroide) colisionado);
        }
    }
}

