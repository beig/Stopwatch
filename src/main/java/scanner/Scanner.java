package scanner;

import com.google.common.eventbus.EventBus;
import event.ScannerEvent;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.apache.commons.io.input.TailerListenerAdapter;

import java.io.File;

public class Scanner extends Thread {

    private static int DELAY = 50;
    private EventBus eventBus;
    private File file;

    public Scanner(EventBus bus, File file) {
        this.file = file;
        this.eventBus = bus;
    }

    @Override
    public void run() {
        TailerListener listener = new MyTailerListenerAdapter();
        Tailer tailer = Tailer.create(file, listener, DELAY);
        while (true) {
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public class MyTailerListenerAdapter extends TailerListenerAdapter {

        @Override
        public void handle(String line) {
            if (line.startsWith("START BOMB TIMER")) {
                eventBus.post(new ScannerEvent(1));
            } else if (line.startsWith("RESET BOMB TIMER")) {
                eventBus.post(new ScannerEvent(2));
            }
        }
    }
}
