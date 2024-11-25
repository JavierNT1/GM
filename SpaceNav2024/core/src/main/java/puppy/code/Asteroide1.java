/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package puppy.code;
 
public class Asteroide1 {
    private String tipo;
    private String tamaño;
    private double velocidad;
    private double daño;

    private Asteroide1(AsteroideBuilder builder) {
        this.tipo = builder.getTipo();
        this.tamaño = builder.getTamaño();
        this.velocidad = builder.getVelocidad();
        this.daño = builder.getDaño();
    }

    @Override
    public String toString() {
        return "Asteroide{" +
               "tipo='" + tipo + '\'' +
               ", tamaño='" + tamaño + '\'' +
               ", velocidad=" + velocidad +
               ", daño=" + daño +
               '}';
    }

    // Getter de los atributos 
    public String getTipo() { return tipo; }
    public String getTamaño() { return tamaño; }
    public double getVelocidad() { return velocidad; }
    public double getDaño() { return daño; }
    
}
