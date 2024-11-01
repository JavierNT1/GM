package puppy.code;

// Interfaz Colisionable

public interface Colisionable {
    boolean colisionarCon(Colisionable elemento);
    void ajustarRebote(Colisionable elemento);
}