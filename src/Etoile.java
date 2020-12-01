/**
 * Classe de l etoile
 * comme un poisson mais sans gravite, son deplacement unique sera defini dans le modele
 */

public class Etoile extends Poissons {

    public Etoile(int level, int width, int height){
        super(level, width,height);

        this.ay = 0;
        this.vy = 100;
    }
}
