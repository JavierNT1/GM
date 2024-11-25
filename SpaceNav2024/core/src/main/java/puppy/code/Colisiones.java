package puppy.code;

// Interfaz Colisionable

public interface Colisiones {
    void colisionar(Colisiones elemento); // Punto de entrada genérico
    void colisionarConAsteroide(Asteroide2 asteroide2); // Reacción específica
    void colisionarConNave(Nave nave); // Reacción específica
}
