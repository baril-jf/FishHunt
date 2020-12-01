/**
 * Le controleur fait le pont entre le modele et la vue
 */
public class Controleur {

    private Modele jeu;
    private FishHunt vue;

    /**
     * contructeur du controleur
     * @jeu  on cree un nouveau modele
     * @param vue le controleur est cree dans la vue avec cette vue en argument
     */
    public Controleur(FishHunt vue) {
        this.vue = vue;
        this.jeu = new Modele(vue.getWidth(), vue.getHeight());
    }

    /**
     * met à jour tous les éléments du modèle à afficher
     * @param dt intervalle de temps de chaque update
     */
    public void update(double dt){
        jeu.update(dt);
    }

    /**
     * permet d'afficher les elements du jeu dans la vue
     */
    public void draw(){
        vue.draw(jeu.getBulles(),jeu.getBalles(), jeu.getPoissons(), vue.getContext(), jeu.getScore(), jeu.getLives(), jeu.getLevel());
    }

    /**
     * verifie si le jeu a atteint un prochain niveau
     * @return boolean selon qu un nouveau niveau a ete atteint ou non
     */
    public boolean checkLevelUp(){
        return jeu.getLevelUp();
    }

    /**
     * verifie si le joueur a perdu la partie
     * @return boolean selon que la partie est perdue ou pas
     */
    public boolean checkGameOver(){
        return jeu.getGameOver();
    }

    /**
     * les 4 prochaines methode sont pour chaque fonction du mode debug et actionne le changement adequat dans le modele
     */
    public void debugH(){
        jeu.debugLevel();
    }
    public void debugJ(){
        jeu.debugScore();
    }
    public void debugK(){
        jeu.debugVies();
    }
    public void debugL(){
        jeu.debugGameOver();
    }

    /**
     * permet d'instancier une balle centree a la position du curseur dans la vue
     * @param x position en x du curseur
     * @param y position en y du curseur
     */
    public void ajouterBalles(double x, double y){
        jeu.ajouterBalles(x,y);
    }

    /**
     * permet de creer une nouvelle partie après la fin de la derniere
     */
    public void nouvellePartie() {
        jeu = new Modele(vue.getWidth(), vue.getHeight()); }

    public void setLevelUp(boolean vF){
        jeu.levelUp = vF;
    }
    public int getScore(){
        return jeu.getScore();
    }
}