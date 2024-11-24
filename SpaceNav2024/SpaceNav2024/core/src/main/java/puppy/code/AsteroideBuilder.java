/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package puppy.code;

// Interfaz del Builder que define los pasos comunes para todos los productos
public interface AsteroideBuilder {
    AsteroideBuilder setTipo(String tipo);
    AsteroideBuilder setTamaño(String tamaño);
    AsteroideBuilder setVelocidad(double velocidad);
    AsteroideBuilder setDaño(double daño);

    String getTipo();
    String getTamaño();
    double getVelocidad();
    double getDaño();

    Asteroide1 build();  // Método para obtener el objeto construido
}
