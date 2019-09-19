package spell;

public class Edit implements Comparable<Edit> {
    public String word;
    public int distance;
    public int count;

    Edit(String word, int distance) {
        this.word = word;
        this.distance = distance;
        this.count = 0;
    }

    @Override
    public int compareTo(Edit e) {
        if (this.distance != e.getDistance()) {
            return Integer.compare(this.distance, e.getDistance());
        } else if (this.count != e.getCount()){
            return Integer.compare(e.getCount(), this.count);
        } else {
            return this.word.compareTo(e.getWord());
        }
    }

    public String getWord() {
        return this.word;
    }

    public int getDistance() {
        return this.distance;
    }

    public int getCount(){
        return this.count;
    }

    public void setCount(int count){
        this.count = count;
    }

    @Override
    public String toString() {
        return "word: " + this.word + "\ndist: " + this.distance + "\ncount: " + this.count;
    }
}
