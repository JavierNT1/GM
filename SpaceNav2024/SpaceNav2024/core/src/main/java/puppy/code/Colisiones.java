package puppy.code;

// Interfaz Colisionable

public interface Colisiones {
    void colisionar(Colisiones elemento); // Punto de entrada genérico
    void colisionarConAsteroide(Asteroide asteroide); // Reacción específica
    void colisionarConNave(Nave nave); // Reacción específica
}
