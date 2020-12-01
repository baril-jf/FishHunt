import java.util.Random;


/**
 *Classe poisson, permet de generer tous les poissons du jeu
 * @versGauche permet de flopper l image dans la vue si vrai
 * @numeroImage permet de choisir une image associee a chaque instanciation
 * @r,g,b code de couleur associe a chaque instance de poisson
 */
public class Poissons extends Entity {

    protected boolean versGauche;
    protected int numeroImage;
    protected int r; protected int g; protected int b;

    /**
     * Contructeur de chaque poisson
     * @param level niveau du jeu
     * @param width largeur du jeu
     * @param height hauteur du jeu
     */
    public Poissons(int level, int width, int height){

        super();
        double rand = Math.random();
        if (rand < 0.5){
            this.x = -5;
            this.vx = (100 * Math.pow(level, (1/3)) + 200);
            versGauche = false;
        }else{
            this.x = width + 5;
            this.vx = -1*(100 * Math.pow(level, (1/3)) + 200);
            versGauche = true;
        }
        this.ax = 0;
        this.ay = 100;

        Random random = new Random();
        this.numeroImage = random.nextInt(8);
        this.vy = -random.nextInt(100) + 100;
        this.y = random.nextInt(4)*0.20*height;
        this.r = random.nextInt(256);
        this.g = random.nextInt(256);
        this.b = random.nextInt(256);
        this.largeur = random.nextInt(40) + 80;
        this.hauteur = this.largeur;
    }
    public void update(double dt) {
        super.update(dt);
    }
    public int getNumeroImage(){
        return this.numeroImage;
    }
    public int getR(){
        return this.r;
    }
    public int getG(){
        return this.g;
    }
    public int getB(){
        return this.b;
    }
    public boolean getVersGauche(){
        return this.versGauche;
    }
    public void setVersGauche(boolean vF){
        this.versGauche = vF;
    }

}
