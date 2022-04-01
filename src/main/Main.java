package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Main {
    private static Runtime runtime = Runtime.getRuntime();
    private static final String commands = "sudo -S powermetrics -s smc -n 1 -i 50 | grep 'CPU die'";
    private static Process process = null;
    private static String s = null;
    private static final String findingLine = "CPU die temperature: ";
    private static Integer temperatureCPU = 0;
    private static final byte[] password = System.getenv("SUDOPSWD").concat("\n\r").getBytes(StandardCharsets.UTF_8);

    public static void main(String[] args) {
        if (!System.getProperties().getProperty("os.name").equals("Mac OS X")) {
            System.exit(0);
        }
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
                    trayIcon.addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (e.getButton() == 3) {
                                System.exit(0);
                            }
                        }
                        @Override
                        public void mousePressed(MouseEvent e) {}
                        @Override
                        public void mouseReleased(MouseEvent e) {}
                        @Override
                        public void mouseEntered(MouseEvent e) {}
                        @Override
                        public void mouseExited(MouseEvent e) {}
                    });
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
//        BufferedReader stdError = null;
        OutputStream outputStream = null;
        try {
            process = runtime.exec(commands);
            stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

            //need to use sudo permission
            outputStream = process.getOutputStream();
            outputStream.write(password);
            outputStream.flush();

            while ((s = stdInput.readLine()) != null) {
                if (s.contains(findingLine)) {
                    String[] split = Main.s.split(" ");
                    String temperatureTextFromArray = split[3];
                    temperatureCPU = (int) Double.parseDouble(temperatureTextFromArray);
                }
            }

            //lets be there. may be it helps.
//            stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//            while ((s = stdError.readLine()) != null) {
//                System.out.println(s);
//            }

            stdInput.close();
//            stdError.close();
            outputStream.close();
            return temperatureCPU;
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                stdInput.close();
//                stdError.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
}
