import java.util.ArrayList;

public class Modele {

    protected int score;
    protected int level;
    protected ArrayList<Poissons> poissons = new ArrayList<>();
    protected ArrayList<Bulles> bulles = new ArrayList<>();
    protected ArrayList<Balles> balles = new ArrayList<>();
    protected double minuteriePoisson;
    protected double minuterieVirus;
    protected double minuteriePoissonSpecial;
    protected double minuterieCrabe;
    protected double minuterieEtoile;
    protected double minuteriePotion;
    protected boolean gameOver;
    protected int lives;
    private int width;
    private int height;
    protected boolean levelUp;

    public Modele(int width, int height) {//hauteur + largeur en argument
        this.width = width;
        this.height = height;
        this.gameOver = false;
        this.lives = 3;
        this.level = 1;
        this.minuterieCrabe = 0;
        this.minuteriePoisson = 0;
        this.minuteriePoissonSpecial = 0;
        this.minuterieVirus = 0;
        this.minuteriePotion = 0;
        this.minuterieEtoile = 0;
        this.levelUp = false;
        ajouterBulles();
        ajouterPoisson();
    }
    public void debugLevel() {
        this.levelUp = true;
        if (this.score % 5 != 0 || this.score == 0){
            this.score += this.level * 5 - this.score;
        }
        else if(this.score % 5 == 0){
            this.score += 5;
        }
    }

    public void debugVies() {
        if (this.lives < 3) {
            this.lives++;
        }
    }
    public void debugGameOver() {
        this.gameOver = true;
    }

    public void debugScore() {
        this.score++;
    }

    public void ajouterBalles(double x, double y) {
        balles.add(new Balles(x, y));
    }

    public void ajouterBulles(){
        for (int i =0;i < 3; i++){
            double baseX = Math.random() * (width + 1);
            for (int j = 0; j < 5; j++) {
                int gOuD = 1;
                if (Math.random() < 0.5) {
                    gOuD *= -1;
                }
                Bulles bullesAjoutees = new Bulles(baseX + gOuD * 20 , height);
                bulles.add(bullesAjoutees);
            }
        }
    }

    public void ajouterPoisson() {
        Poissons poissonAjoute = new Poissons(level, width, height);
        poissons.add(poissonAjoute);
    }

    public void update(double dt) {
        if (lives == 0) {
            gameOver = true;
        }

        if((score/level) %  5 == 0 && score > 0  || this.levelUp) {
            level++;
            for (int i = poissons.size()-1;i>=0;i--){
                poissons.remove(poissons.get(i));
            }
            for (int i = balles.size() -1;i >=0; i--) {
                balles.remove(balles.get(i));
            }
            this.levelUp = true;
        }
        minuterieCrabe += dt;
        minuterieEtoile += dt;
        minuteriePoisson += dt;
        minuteriePoissonSpecial += dt;
        for (Balles ba : balles) {
            ba.update(dt);
        }
        for(int i=balles.size()-1; i>=0;i--) {
            if(balles.get(i).getRayon() < -3) {
                balles.remove(i);
            }
        }
        for (Poissons p : poissons) {
            p.update(dt);
            if (p instanceof Crabe && minuterieCrabe >= 0.5  && minuterieCrabe < 1) {
                p.setVx(p.getVx() * -1);
                p.setVersGauche(!p.getVersGauche());
                minuterieCrabe = 1;
            }
            if (p instanceof Crabe && minuterieCrabe >= 1.25) {
                p.setVx(p.getVx() * -1);
                p.setVersGauche(!p.getVersGauche());
                minuterieCrabe = 0;
            }
            if (p instanceof Etoile && minuterieEtoile > 0.5){
                p.setVy(p.getVy() * -1);
                minuterieEtoile = 0;
            }
        }
        for (int i = poissons.size()-1;i>=0;i--){
            if ((poissons.get(i).getX() < 0 && poissons.get(i).getVx() < 0) ||
                    (poissons.get(i).getX() > width + 10 && poissons.get(i).getVx() > 0) ||
                    poissons.get(i).getY() > height) {
                poissons.remove(poissons.get(i));
                lives--;
            }
        }
        for (Balles b : balles) {
            for (int i = poissons.size() - 1; i >= 0; i--) {
                if (b.intersects(poissons.get(i))) {
                    score++;
                    poissons.remove(poissons.get(i));
                }
            }
        }
        for (Bulles b : bulles) {
            b.update(dt);
        }
        for(int i=0; i<bulles.size();i++) {
            if(bulles.get(i).getY() < 0) {
                bulles.remove(i);
            }
        }

        if (minuteriePoisson >= 3) {
            ajouterBulles();
            ajouterPoisson();
            minuteriePoisson = 0;
        }

        if (minuteriePoissonSpecial >= 5 && level > 1) {
            double rand = Math.random();
            if (rand < 0.5) {
                Etoile etoile = new Etoile(level, width,height);
                poissons.add(etoile);
                minuterieEtoile = 0;
            } else {
                Crabe crabe = new Crabe(level, width, height);
                poissons.add(crabe);
                minuterieCrabe = 0;
            }
            minuteriePoissonSpecial = 0;
        }
    }
    public ArrayList<Poissons> getPoissons(){
        return poissons;
    }
    public ArrayList<Bulles> getBulles(){
        return bulles;
    }
    public ArrayList<Balles> getBalles(){
        return balles;
    }
    public int getLevel() {
        return this.level;
    }
    public int getScore() {
        return this.score;
    }
    public int getLives() {
        return this.lives;
    }
    public boolean getGameOver(){
        return this.gameOver;
    }
    public boolean getLevelUp(){
        return this.levelUp;
    }

}


