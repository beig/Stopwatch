package event;

/**
 * Created by thomas on 18.12.2015.
 */
public class ScannerEvent {

    private final int message;

    public ScannerEvent(int message) {
        this.message = message;
    }

    public int getMessage() {
        return message;
    }
}
