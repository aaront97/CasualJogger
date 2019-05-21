package sample.api;

public class LocationNotFoundException extends Exception {
    public LocationNotFoundException(String locationErr){
        super(locationErr);
    }

}
