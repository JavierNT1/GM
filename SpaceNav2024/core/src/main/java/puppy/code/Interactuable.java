package puppy.code;

public interface Interactuable {
    void atacar(Elemento objetivo);
    void defender();
    void subirDeNivel();
    void colisionarCon(Elemento elemento);
    void recibirMejora(int cantidad);
}