package frame;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import event.ScannerEvent;
import org.ini4j.Ini;
import org.ini4j.Wini;
import scanner.Scanner;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class App {

    private EventBus eventBus = new EventBus("scanner");
    private final ClockFrame frame;
    private boolean startup = true;

    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        File iniFile = new File("settings.ini");
        if (!iniFile.exists())
            iniFile.createNewFile();

        String path = "";
        Wini ini = new Wini(iniFile);
        if (ini.get("settings") != null) {
            path = ini.get("settings").fetch("path");
        }

        File file = null;

        if (path == null || path.equalsIgnoreCase("")) {
            file = new File(getDirectory() + File.separator + "console.log");

            checkConsoleLogFile(file);

            ini.put("settings", "path", file.getAbsolutePath());
            ini.store();
        } else {
            file = new File(path);
            checkConsoleLogFile(file);
        }

        new App(file);

    }

    public static void checkConsoleLogFile(File file) {
        if (!file.exists() || file.isDirectory()) {
            JOptionPane.showMessageDialog(null, "Did you add '-condebug' in your CS:GO launch options? \nLaunch CS:GO if you just added it.", "Error reading console.log", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public static String getDirectory() {
        String result = "";

        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setDialogTitle("Select CS:GO directory (..common\\Counter-Strike Global Offensive\\csgo)");

        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            result = fc.getSelectedFile().getAbsolutePath();
        } else {
            System.exit(2);
        }

        return result;
    }

    public App(File file) {
        eventBus.register(this);
        frame = new ClockFrame();

        Scanner scanner = new Scanner(eventBus, file);
        scanner.start();

        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(3000);
                    startup = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Subscribe
    public void listen(ScannerEvent event) {
        if (event.getMessage() == 1 && !startup) {
            frame.stopWatch.start();
        } else if (event.getMessage() == 2 && !startup) {
            frame.stopWatch.reset();
        }
    }

}
