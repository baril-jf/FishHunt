/**
 * Classe des balles
 * @rayon est le rayon de chaque instance, il sera reduit avec une vitesse vz
 * @vz vitesse perpendiculaire a la surface de jeu
 */
public class Balles extends Entity {
    protected double rayon;
    protected double vz;
    public Balles(double x, double y){
        this.x = x;
        this.y = y;
        this.rayon = 50;
        this.vz = 300;
    }
    /**
     * permet de verifier si une balle va toucher un poisson, un virus ou une potion
     * @param autre toute instance de poisson, crabe, etoile
     * @return true si la balle touche le poisson
     */
    public boolean intersects(Poissons autre){
        if (this.rayon < 0) {
            return ((this.x > autre.x && this.x < autre.x + autre.largeur )
                    && (this.y > autre.y && this.y < autre.y + autre.hauteur ));
        }else {
            return false;
        }
    }
    /**
     * la seule difference des autres entites est que l on reduit le rayon a une vitesse vz
     * @param dt intervalle de temps de chaque update
     */
    public void update(double dt){

        this.rayon -= this.vz*dt;
    }
    public double getRayon(){
        return this.rayon;
    }
}
