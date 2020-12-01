/**
 * Classe qui permet de creer une nouvelle entree dans les meilleurs scores, on attache un nom a un score et on garde ca
 * en memoire
 */

public class Score {

    String name;
    int score;
    public Score(String a, int b){
        this.name=a;
        this.score=b;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public int getScore() {
        return score;
    }
    public String getName() {
        return name;
    }
}
