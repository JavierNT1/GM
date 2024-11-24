/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package puppy.code;
 
public class NaveColisionStrategy implements ColisionStrategy {
    @Override
    public void manejarColision(Colisiones colisionador, Colisiones colisionado) {
        colisionado.colisionarConNave((Nave) colisionador);
    }
}
