package project.boggle.model;

import java.util.Timer;
import java.util.TimerTask;

public class Chrono {
    private final Timer timer;
    private int sec;
    private boolean isFinish;

    public Chrono() {
        this.sec = 180;
        this.timer = new Timer();
        this.isFinish = false;
    }


    public Timer getTimer() {
        return timer;
    }

    public void Setsec(int newsec) {
        this.sec = newsec;
    }

    public void addSec(int newsec) {

        this.sec += newsec;

    }

    public void setIsFinish(boolean newboolean) {
        this.isFinish = newboolean;
    }

    public boolean GetIsFinish() {
        return this.isFinish;
    }

    public void starting() {
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                if (sec % 60 < 10) {
                    System.out.println("TIME LEFT: 0" + (sec % 3600) / 60 + ":0" + sec % 60);
                } else {
                    System.out.println("TIME LEFT: 0" + (sec % 3600) / 60 + ":" + sec % 60);
                }
                sec -= 30;

                if (sec < 0) {
                    timer.cancel();
                    System.out.println("Time over");
                    System.exit(0);
                }
            }
        }, 1000, 30000);

    }
}
