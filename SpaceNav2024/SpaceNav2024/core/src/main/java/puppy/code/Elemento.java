package puppy.code;

public abstract class Elemento {
    protected float x, y;
    protected int vida;
    protected int daño;
    protected int xSpeed;
    protected int ySpeed;

    public Elemento(float x, float y, int vida, int daño, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.vida = vida;
        this.daño = daño;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }
    
    public Elemento(float x, float y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public abstract void update();

    public int getxSpeed() {
        return xSpeed;
    }

    public void setxSpeed(int xSpeed) {
        this.xSpeed = xSpeed;
    }

    public int getySpeed() {
        return ySpeed;
    }

    public void setySpeed(int ySpeed) {
        this.ySpeed = ySpeed;
    }
}
