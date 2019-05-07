package sample;

public class BreezometerRecord {
    public String datetime;
    public String key;
    public int value;

    BreezometerRecord(String time, String key, int value){
        this.datetime = time;
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString(){
        return datetime + " " + key + " " + String.valueOf(value);
    }

}
