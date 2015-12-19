package frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StopWatch extends JPanel {
    private Timer timer;
    public static final int TENTH_SEC = 100;

    private JLabel timeLbl;

    private int clockTick;
    private double clockTime;
    private String clockTimeString;

    private int DEFAULT_CLOCK_TIMER = 400;

    public StopWatch() {

        clockTick = DEFAULT_CLOCK_TIMER;
        clockTime = ((double) clockTick) / 10.0;

        clockTimeString = Double.toString(clockTime);
        Font myClockFont = new Font("Serif", Font.PLAIN, 50);

        timeLbl = new JLabel();
        timeLbl.setFont(myClockFont);
        timeLbl.setText(clockTimeString);


        timer = new Timer(TENTH_SEC, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                clockTick--;
                clockTime = ((double) clockTick) / 10.0;
                clockTimeString = Double.toString(clockTime);
                timeLbl.setText(clockTimeString);
                if (clockTimeString.equalsIgnoreCase("0.0")) {
                    reset();
                }
            }
        });
    }

    public void launchStopWatch() {
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);
        topPanel.add(timeLbl);

        this.setLayout(new BorderLayout());

        add(topPanel, BorderLayout.CENTER);

        setSize(300, 200);
        setBackground(Color.WHITE);

    }

    public void start() {
        timer.start();
    }

    public void reset() {
        timer.stop();

        clockTick = DEFAULT_CLOCK_TIMER;
        clockTime = ((double) clockTick) / 10.0;
        clockTimeString = Double.toString(clockTime);
        timeLbl.setText(clockTimeString);
    }

    public void toggleTimer() {
        if (DEFAULT_CLOCK_TIMER == 400) {
            DEFAULT_CLOCK_TIMER = 350;
        } else {
            DEFAULT_CLOCK_TIMER = 400;
        }

        reset();
    }
}


class ClockFrame extends JFrame {
    StopWatch stopWatch;

    public ClockFrame() {
        super("Timer");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container myPane = getContentPane();

        stopWatch = new StopWatch();
        stopWatch.launchStopWatch();
        setSize(300, 200);

        myPane.add(stopWatch);
        setVisible(true);
    }
}
