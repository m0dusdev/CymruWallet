package cymruWallet;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Gets data from daemon json output
 */

public class FromProcess {

    static AtomicBoolean running = new AtomicBoolean(false);


    // set running
    private void setRunning(boolean set){
        running.set(set);
    }

    // run a command in the directory
    static void doCmd(String command){
        try {
            Process commandProc = Runtime.getRuntime().exec(command);
            commandProc.waitFor();
            Gui.addToConsole("Command successful ");
        } catch (IOException | InterruptedException exp) {
            Gui.addToConsole("Error : Command failed");
        }

    }


    static void getLog() {
       try {
           Process getInfo = Runtime.getRuntime().exec("./cymrucoind getinfo > log.txt");
            getInfo.waitFor();


           // some processing
           String path = "log.txt";

           List<String> lines = Files.readAllLines(Paths.get(path), Charset.defaultCharset());

           StringBuilder buffer = new StringBuilder();

           // get string into buffer for data extraction
           for (String Final : lines) {
               buffer.append(Final);
               buffer.append("\n");

               if (Final.contains("balance")) {
                   // format
                   Gui.setBlance(Final.replaceAll("\"balance\"", "Balance "));

               } else if (Final.contains("version")) {
                   // format
                   Gui.updateVersion(Final.replaceAll(" \"walletversion\"?, : ", "Version "));


               }

           }
           // update console buffer
           Gui.addToConsole(buffer.toString());

       } catch (IOException io ) {

           Gui.addToConsole("IO error : File not found");

       } catch (InterruptedException ie) {

           Gui.addToConsole("Error : Could not run ./cymrucoind getinfo > " +
                   "log.txt | Is cymruWallet.jar in /src/ ?");
       }







    }

    // starts the update thread
    public void startUpdateThread(){
        updateThread thread = new updateThread();
        setRunning(true);
        thread.run();
    }

    public static void stopDeamon() {
        try {
            Process stopDeamon = Runtime.getRuntime().exec("./cymrucoind stop");
            stopDeamon.waitFor();
            Gui.addToConsole("Daemon stopped successfully");
        } catch (IOException | InterruptedException ioi) {
            ioi.printStackTrace();
            Gui.addToConsole("IOError : Failed to stop daemon - Try ./cymrucoind stop in cymrucoin/src/");
        }


    }

    // thread that updates logs
    private class updateThread implements Runnable {
        @Override
        public void run() {
                    try {
                        Process startDeamon = Runtime.getRuntime().exec("./cymrucoind &");
                        startDeamon.waitFor();
                        System.out.print("started");
                        Process getInfo = Runtime.getRuntime().exec("./cymrucoind getinfo > log.txt");
                        getInfo.waitFor();
                        Gui.addToConsole("UPDATED WALLET INFO @ " + new Date().toString());
                        Gui.addToConsole("Started update\n");

                        getLog(); // read from file

                        System.out.print("sleep");
                        Thread.sleep(2000);


                    } catch (IOException | InterruptedException io) {
                        Gui.addToConsole("IOError : updating wallet data from file failed");
                    }
        }
    }
}


