/**
 * Marie-Christine Cloutier
 * Jean-Francois Girard-Baril
 */


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
/**
 * Classe principale et vue du jeu
 * @width largeur du jeu
 * @height hauteur de jeu
 * @window sera utilise comme fenetre de jeu
 * @context le contexte de jeu sur lequel on ajoutera les elements mis a jour
 * @liste liste des meilleurs scores
 * @newHighScore : true si le score de la partie actuel doit etre place dans la liste des meilleurs scores
 * @partieCommence : true si on a commence une partie...
 * @gameOver : true si on a perdu
 * decompte : permettra d'afficher certains element pour une courte periode de temps
 */
public class FishHunt extends Application {

    protected Stage window;
    protected int width=640;
    protected int height=480;
    protected ListView<String> liste = new ListView<>();
    protected boolean partieCommencee = false;
    protected boolean gameOver = false;
    protected GraphicsContext context;
    protected double decompte;

    public static void main(String[] args) {
        launch(args);
    }
    /**
     * on instancie un controleur qui fera le pont entre la vue et le modele de jeu
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage)throws Exception {
        Controleur controleur = new Controleur(this);
        window = primaryStage;

        /**
         * Boutons utilises dans les scenes
         */
        Button newGame = new Button("Nouvelle partie!");
        Button highScores = new Button("Meilleurs Scores");
        Button retourMenu = new Button("Menu");

        /**
         * Menu principal, seule scene creee des le demarrage d'un nouveau jeu, on y cree aussi le pane et le imageView qui
         * serviront a la sceneJeu
         * Les 2 autres scenes seront crees par des methodes apres la methode start
         **/
        VBox layoutMenu = new VBox();
        layoutMenu.setAlignment(Pos.CENTER);
        layoutMenu.setBackground(new Background( new BackgroundFill( Color.DARKBLUE, CornerRadii.EMPTY, Insets.EMPTY) ));

        VBox boutonsMenu = new VBox();
        boutonsMenu.setAlignment((Pos.CENTER));
        boutonsMenu.setSpacing(8);
        boutonsMenu.getChildren().add(newGame);
        boutonsMenu.getChildren().add(highScores);

        Image logo = new Image("/images/logo.png");
        ImageView imageViewLogo = new ImageView(logo);
        layoutMenu.getChildren().add(imageViewLogo);
        layoutMenu.getChildren().add(boutonsMenu);

        Scene sceneMenu = new Scene(layoutMenu,width,height);

        Pane pane = new Pane();
        Scene sceneJeu = new Scene(pane,width,height);
        Image cible = new Image( "/images/cible.png");
        ImageView imageViewCible = new ImageView(cible);
        imageViewCible.setFitWidth(50);
        imageViewCible.setFitHeight(50);


        /**
         * Actions des boutons
         */

        newGame.setOnAction((event)-> {

            nouvellePartie(pane, imageViewCible, sceneJeu);
            AnimationTimer timer = new AnimationTimer() {
                private long lastTime = 0;

                @Override
                public void handle(long now) {

                    if (lastTime == 0) {
                        lastTime = now;
                        return;
                    }
                    /**
                     * l'intervalle de temps est mis a jour et le controleur va chercher les informations a afficher a
                     * l'ecran
                     */
                    double deltaTime = (now - lastTime) * 1e-9;
                    decompte += deltaTime;
                    controleur.draw();
                    /**
                     * on affiche seulement le niveau pendant 3 secondes avant que la partie commence, le controleur
                     * commence alors la mise a jour de chaque element a afficher
                     */
                    if (decompte >3){

                        partieCommencee = true;
                        controleur.update(deltaTime);
                        /**
                         * si un nouveau niveau est atteint, on pause la partie et on affiche le nouveau niveau pendant 3 secondes
                         */
                        if(controleur.checkLevelUp()){
                            decompte = 0;
                            partieCommencee = false;
                            controleur.setLevelUp(false);
                        }
                    }
                    /**
                     * si la partie est perdue, on affiche le game over pendant 3 secondes avant de passer dans le menu des meilleurs scores
                     */
                    if (controleur.checkGameOver()){

                        if (!gameOver){
                            decompte = 0;
                        }

                        gameOver = true;
                        partieCommencee = false;
                        controleur.draw();

                        if (decompte > 3){
                            pane.getChildren().clear();
                            this.stop();
                            retourMenu.setOnAction((event)-> {
                                window.setScene(sceneMenu);
                            });
                            try {
                                FileReader gen = new FileReader("highscore.txt");
                                BufferedReader gen1 = new BufferedReader(gen);
                                String mot = gen1.readLine();
                                String last = "";
                                int j= 0;
                                while (mot != null && !mot.equalsIgnoreCase("")){
                                    liste.getItems().add(mot);
                                    j++;
                                    last = mot;
                                    mot = gen1.readLine();
                                }
                                if (j<10){
                                    window.setScene(genererHighScoreNew(controleur, retourMenu,controleur.getScore(),sceneMenu));
                                    this.stop();
                                }
                                else if (last != null){
                                    String[] words = last.split("-");
                                    if (controleur.getScore()>=Integer.parseInt(words[1])){
                                        window.setScene(genererHighScoreNew(controleur, retourMenu,controleur.getScore(),sceneMenu));
                                        this.stop();
                                    }
                                    else {
                                        window.setScene(genererHighScore(controleur, retourMenu));
                                        this.stop();
                                    }
                                }
                                else {
                                    window.setScene(genererHighScore(controleur, retourMenu));
                                    this.stop();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    lastTime = now;
                }
            };
            timer.start();
        });
        highScores.setOnAction((event)-> {
            try {
                window.setScene(genererHighScore(controleur, retourMenu));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        retourMenu.setOnAction((event)-> {
            window.setScene(sceneMenu);
        });

        /**
         * Actions des touches debug
         */
        sceneJeu.setOnKeyPressed((value) -> {
            switch (value.getCode()){
                case H : controleur.debugH();
                    break;
                case J : controleur.debugJ();
                    break;
                case K : controleur.debugK();
                    break;
                case L : controleur.debugL();
                    break;
            }
        });
        /**
         * Action de la souris
         * Je sais que ça fonctionne pour les deplacements mais pas pour le clic.
         */

        sceneJeu.setOnMouseClicked((event) -> {
            if(partieCommencee) {
                controleur.ajouterBalles(event.getX(), event.getY());
            }
        });
        /**
         * Action de la souris, si la partie est commencee, le clic va creer une nouvelle balle alors que le deplacement
         * affiche une cible centree sur le curseur
         */
        sceneJeu.setOnMouseMoved((event) -> {
            double w = imageViewCible.getBoundsInLocal().getWidth();
            double h = imageViewCible.getBoundsInLocal().getHeight();
            imageViewCible.setX(event.getX()- w/2);
            imageViewCible.setY(event.getY()-h/2);
        });
        /**
         * titre du jeu, scene de depart, icone de la fenetre et fenetre a grandeur fixe
         */
        window.setTitle("Fish Hunt");
        window.setScene(sceneMenu);
        window.getIcons().add(logo);
        window.setResizable(false);
        window.show();
    }
    /**
     * Methode pour creer une nouvelle partie
     * @pane : le pane sur lequel la scene est affiche
     * @param imageViewCible : pour afficher la cible la ou le curseur pointe
     * @param sceneJeu : la scene de jeu principale
     */
    public void nouvellePartie(Pane pane, ImageView imageViewCible, Scene sceneJeu){
        Canvas canvas = new Canvas(width, height);
        pane.getChildren().add(canvas);
        pane.getChildren().add(imageViewCible);
        context = canvas.getGraphicsContext2D();
        window.setScene(sceneJeu);
        gameOver = false;
        decompte = 0;
    }

    /**
     * Methode pour afficher tous les elements du modele
     * @param balles : les balles en jeu
     * @param context le graphics context sur lequel on dessinera
     * @param level le niveau actuel
     * @param lives le nombre de vies restantes
     * @param pointage le score actuel
     * @param bulles : les bulles en jeu
     * @param poissons : les poissons en jeu
     */
    public void draw(ArrayList<Bulles> bulles, ArrayList<Balles> balles, ArrayList<Poissons> poissons, GraphicsContext context, int pointage, int lives, int level){

        /**
         * fond d'ecran de base
         */
        context.setFill(Color.DARKBLUE);
        context.fillRect(0,0,width,height);
        context.setFill(Color.WHITE);
        context.setFont(Font.font("Purisa", 24));
        context.setTextAlign(TextAlignment.CENTER);
        context.setTextBaseline(VPos.CENTER);
        /**
         * on ajoute autant de petits poissons qu'on a de vies
         */
        Image life = new Image("/images/fish/00.png");
        for (int i=0;i<lives; i++){
            context.drawImage(life, 280+i*35,55, 25,25);
        }
        /**
         * on ajoute le pointage
         */
        context.fillText(pointage +"", width/2+1,35);
        context.setFont(Font.font("Purisa", 60));
        /**
         * on affiche le niveau actuel pendant 3 secondes
         */
        if(this.decompte < 3 && !this.gameOver) {
            context.fillText("Level " + level, width / 2, height / 2);
            /**
             * on affiche game over pendant 3 secondes, si la partie est perdue
             */
        }else if(this.gameOver){
            context.setFill(Color.RED);
            context.fillText("GAME OVER", Math.round(width / 2),
                   Math.round(height / 2));
            /**
             * Sinon, on affiche tous les elements qui font actuellement partie du jeu
             */
        }else{
            for (Bulles b : bulles){
                double rayon = b.getRayon();
                context.setFill(Color.rgb(0,0,255,.4));
                context.fillOval(b.getX()- rayon, b.getY() - rayon, rayon, rayon);
            }
            for (Balles ba : balles){
                double rayon = ba.getRayon();
                double distance = Math.cos(Math.PI/4)*rayon +8;
                context.setFill(Color.BLACK);
                context.fillOval(ba.getX() - distance , ba.getY() - distance, 2*rayon, 2*rayon);
            }
            for (Poissons p : poissons){

                Color couleur;
                Image image;

                if (p instanceof Crabe){
                    image = new Image("/images/crabe.png");
                    couleur = Color.CORAL;
                }else if (p instanceof Etoile){
                    image = new Image("/images/star.png");
                    couleur = Color.YELLOW;
                }else {
                    image = new Image("/images/fish/0" + p.getNumeroImage() + ".png");
                    couleur =  Color.rgb(p.getR(),p.getG(),p.getB());
                }
                if (p.getVx()<0){
                    image = ImageHelpers.flop(image);
                }

                image = ImageHelpers.colorize(image, couleur);
                context.drawImage(image, p.getX(), p.getY(), p.getLargeur(), p.getHauteur() );
            }
        }

    }

    /**
     * Methodes pour ajouter ou simplement generer les highscores
     * @param  s : le score actuel
     * @throws Exception
     */
    public static void addScore(Score s) throws Exception {
        ArrayList<Score> scores = new ArrayList<>();
        FileReader fr = new FileReader("highscore.txt");
        BufferedReader br = new BufferedReader(fr);
        String mot = br.readLine();
        while (mot != null && !mot.equalsIgnoreCase("")){
            String[] words = mot.split("-");
            scores.add(new Score(words[0],Integer.parseInt(words[1])));
            mot = br.readLine();
        }
        br.close();
        if (scores.size()<10){
            int j = 0;
            while(j <scores.size() && scores.get(j).getScore()>s.getScore()){
                j++;
            }
            scores.add(j,s);
        }
        else if(s.getScore() >= scores.get(9).getScore()){
            scores.remove(9);
            int j = 0;
            while(j <scores.size() && scores.get(j).getScore()>s.getScore()){
                j++;
            }
            scores.add(j,s);
        }
        PrintWriter writer = new PrintWriter("highscore.txt");
        writer.print("");
        writer.close();
        if (scores.size()>10){
            scores = (ArrayList<Score>) scores.subList(0,10);
        }
        FileWriter fw = new FileWriter("highscore.txt",true);
        BufferedWriter bw = new BufferedWriter(fw);
        for (int i =0;i<scores.size();i++){
            Score temp = scores.get(i);
            bw.append(temp.getName()+"-"+temp.getScore()+"\n");
        }
        bw.close();

    }

    /**
     * Si on doit seulement afficher les high scores
     * @param controleur : le controleur du jeu
     * @param retourMenu : le bouton pour revenir au menu principal
     * @return : la scene a afficher
     * @throws IOException
     */
    public Scene genererHighScore(Controleur controleur, Button retourMenu) throws IOException {
        VBox layoutScores = new VBox();
        layoutScores.setAlignment(Pos.CENTER);
        layoutScores.setSpacing(8);
        Text titreScores = new Text("Meilleurs scores");
        titreScores.setFont(Font.font("Purisa", 28));
        FileReader gen = new FileReader("highscore.txt");
        BufferedReader gen1 = new BufferedReader(gen);
        String mot = gen1.readLine();
        liste = new ListView<>();
        while (mot != null && !mot.equalsIgnoreCase("")){
            liste.getItems().add(mot);
            mot = gen1.readLine();
        }
        VBox joueurs = new VBox(liste);
        joueurs.setMaxWidth(550);
        joueurs.setMaxHeight(370);
        joueurs.setAlignment(Pos.CENTER);

        layoutScores.getChildren().addAll(titreScores, joueurs, retourMenu);
        controleur.nouvellePartie();

        Scene sceneScores = new Scene(layoutScores, width, height);
        return sceneScores;
    }

    /**
     * Si par contre un high score est battu
     * @param controleur voir methode plus haut
     * @param retourMenu
     * @param points
     * @param menu
     * @return
     * @throws IOException
     */
    public Scene genererHighScoreNew(Controleur controleur, Button retourMenu, int points, Scene menu) throws IOException {
        VBox layoutScores = new VBox();
        layoutScores.setAlignment(Pos.CENTER);
        layoutScores.setSpacing(8);
        Text titreScores = new Text("Meilleurs scores");
        titreScores.setFont(Font.font("Purisa", 28));
        FileReader gen = new FileReader("highscore.txt");
        BufferedReader gen1 = new BufferedReader(gen);
        String mot = gen1.readLine();
        liste = new ListView<>();
        while (mot != null && !mot.equalsIgnoreCase("")){
            liste.getItems().add(mot);
            mot = gen1.readLine();
        }
        VBox joueurs = new VBox(liste);
        joueurs.setMaxWidth(550);
        joueurs.setMaxHeight(370);
        joueurs.setAlignment(Pos.CENTER);
        joueurs.setMaxHeight(360);
        HBox newScore = new HBox();
        newScore.setAlignment(Pos.CENTER);
        newScore.setSpacing(8);
        Text newEntree = new Text("Votre nom :");
        TextField nomJoueur = new TextField();
        Text reste = new Text("a fait " + controleur.getScore() + " points !");
        Button ajouter = new Button("Ajouter!");
        newScore.getChildren().addAll(newEntree, nomJoueur, reste, ajouter);
        layoutScores.getChildren().addAll(titreScores, joueurs, newScore, retourMenu);
        ajouter.setOnAction((event)-> {
            try {
                String name = nomJoueur.getText();
                Score s = new Score(name,points);
                addScore(s);
                controleur.nouvellePartie();
                window.setScene(menu);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        controleur.nouvellePartie();
        Scene sceneScores = new Scene(layoutScores, width, height);
        return sceneScores;
    }
    public GraphicsContext getContext(){
        return this.context;
    }
    public int getHeight(){
        return this.height;
    }
    public int getWidth(){
        return this.width;
    }
}