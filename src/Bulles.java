

/**
 * cree des bulles
 * @rayon nouvel attribut rayon genere aleatoirement a chaque instanciation
 * @baseX point de depart aleatoire en x, on ajoute ou soustrait 20 de cette valeur pour chaue bulle
 *
 */
public class Bulles extends Entity {
    protected double rayon;

    /**
     * contructeur
     * @param baseX base à partir duquel les bulles sont generees a gauche ou a droite
     * @param yInit hauteur (en dessous de la base du jeu)
     */
    public Bulles(double baseX, double yInit) {
        this.x = baseX;
        this.y = yInit;
        this.rayon = Math.random() * 41 + 10;
        this.vy = (Math.random() * 451 + 350);
        this.vx = 0;
    }

    public double getRayon() {
        return this.rayon;
    }

    /**
     * met a jour le deplacement vertical de chaque bulle
     *
     * @param dt variation de temps entre chaque update
     */
    public void update(double dt) {
        y -= dt * vy;
    }
}