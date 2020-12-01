
/**
 * classe dont herite tous les elements du jeu
 * x,y,vx,vy,ax,ay sont les positions, vitesses et acceleration
 * yAffiche permet de calculer la position des entites dans la fenetre de jeu
 * update met a jour chacun des attributs des entites a chaque intervale de temps
 * draw permet de d afficher a l ecran les entites et sera ajuste selon la classe qui en herite
 */
public abstract class Entity {

    protected double largeur, hauteur;
    protected double x, y;
    protected double vx, vy;
    protected double ax, ay;

    public void update(double dt) {
        this.vx += dt * this.ax;
        this.vy += dt * this.ay;
        this.x += dt * this.vx;
        this.y += dt * this.vy;
    }

    public double getHauteur() {
        return this.hauteur;
    }
    public double getLargeur() {
        return this.largeur;
    }
    public void setLargeur(double largeur) {
        this.largeur = largeur;
    }
    public void setHauteur(double hauteur) {
        this.hauteur = hauteur;
    }
    public double getX() {
        return this.x;
    }
    public double getY() {
        return this.y;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getVx() {
        return this.vx;
    }
    public void setVx(double vx) {
        this.vx = vx;
    }
    public double getVy() {
        return this.vy;
    }
    public void setVy(double vy) {
        this.vy = vy;
    }
    public double getAx() {
        return this.ax;
    }
    public void setAx(double ax) {
        this.ax = ax;
    }
    public double getAy() {
        return this.ay;
    }
    public void setAy(double ay) {
        this.ay = ay;
    }
}
