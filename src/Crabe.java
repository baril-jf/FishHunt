/**
 * classe du crabe
 * @recule permet de faire le mouvement de va et vient du crabe
 * son deplacement unique sera defini dans le modele
 */

public class Crabe extends Poissons {

    protected boolean recule;

    /**
     * constructeur du crabe, herite de Poissons avec la vitesse ajustee selon les specifications
     * @param level niveau du jeu
     * @param width largeur du jeu
     * @param height hauteur du jeu
     */
    public Crabe(int level, int width, int height){
        super(level,width,height);
        this.recule = false;
        this.ay = 0;
        this.vy = 0;
        double rand = Math.random();
        if (rand < 0.5){
            this.x = -5;
            this.vx = 1.3 * (100 * Math.pow((double)level, (1/3)) + 200);
        }else{
            this.x = width + 5;
            this.vx = -1.3 * (100 * Math.pow((double)level, (1/3)) + 200);
        }

    }
}
