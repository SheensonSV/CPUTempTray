package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    private static Runtime runtime = Runtime.getRuntime();
    private static String[] commands = {"sysctl", "-a"}; // | grep machdep.xcpm.cpu_thermal_level:"};
    private static Process process = null;
    private static String s = null;
    private static String findingLine = "machdep.xcpm.cpu_thermal_level: ";
    private static Integer temperatureCPU = 0;

    public static void main(String[] args) {
        int lastTemperature = 0;
        int currentTemperature = 0;
        SystemTray systemTray = SystemTray.getSystemTray();
        TrayIcon trayIcon = null;
        try {
            while (true) {

                currentTemperature = getTemperature();
                if (currentTemperature < 1) currentTemperature = 1;
                if (currentTemperature > 99) currentTemperature = 99;

                if (lastTemperature != currentTemperature) {
                    String source = "src/resources/" + currentTemperature + ".png";

                    systemTray.remove(trayIcon);

                    Image icon = ImageIO.read(new File(source));
                    trayIcon = new TrayIcon(icon, "CPUTemp");
                    systemTray.add(trayIcon);

                    lastTemperature = currentTemperature;
                }
                else {
                    Thread.sleep(2000);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Integer getTemperature() {
        BufferedReader stdInput = null;
        BufferedReader stdError = null;
        try {
            process = runtime.exec(commands);
            stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            while ((s = stdInput.readLine()) != null) {

                if (s.contains(findingLine)) {
                    temperatureCPU = Integer.parseInt(s.substring(findingLine.length()));
                }
            }
            //lets be there. may be it helps.
            while ((s = stdError.readLine()) != null) {
            }
            stdInput.close();
            stdError.close();
            return temperatureCPU;
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                stdInput.close();
                stdError.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
}
