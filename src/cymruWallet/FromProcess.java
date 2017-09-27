package cymruWallet;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * <li>performs daemon operations needed by the gui</li>
 * @see cymruWallet.Gui - The GUI
 */

final class FromProcess {

    /**
     * runs commands to interact with wallet daemon
     * @param command - The command to run
     * @param verbose - true if we want to display the commands output to the console
     */
    static void doCmd(String command, boolean verbose) {

        if (verbose) {
            try {
                Process commandProc = Runtime.getRuntime().exec(command);
                commandProc.waitFor();

                Gui.addToConsole(command + " = Successful");
            } catch (IOException | InterruptedException exp) {
                Gui.addToConsole(command + " = Failed");
            }
        }


        // else
        try {
            Process commandProc = Runtime.getRuntime().exec(command);
            commandProc.waitFor();
        } catch (IOException | InterruptedException exp) {
            exp.printStackTrace();
        }


    }

    /**
     * <li>gets daemon log and updates gui info</li>
     */
    static void getLog() {
        try {
            Process getInfo = Runtime.getRuntime().exec("./cymrucoind getinfo > log.txt");
            getInfo.waitFor();


            // some processing
            String path = "log.txt";

            List<String> lines = Files.readAllLines(Paths.get(path), Charset.defaultCharset());



            // get string into buffer for data extraction
            for (String Final : lines) {
                //buffer.append(Final);
                //buffer.append("\n");

                if (Final.contains("balance")) {
                    // format
                    Gui.setBlance(Final.replaceAll("\"balance\"", "Balance "));

                } else if (Final.contains("version")) {
                    // format
                    Gui.updateVersion(Final.replaceAll(" \"walletversion\"?, : ", "Version "));


                }

            }

            // update console buffer
            //Gui.addToConsole(buffer.toString());

        } catch (IOException io) {

            Gui.addToConsole("IO error : File not found");

        } catch (InterruptedException ie) {

            Gui.addToConsole("Error : Could not run ./cymrucoind getinfo > " +
                    "log.txt | Is cymruWallet.jar in /src/ ?");
        }
    }

    /**
     * Creates a single-threaded executor and binds it to our update thread
     * @see cymruWallet.UpdateThread
     */
     void startUpdate() {

        // create a single threaded executor and pass our update runnable to it
        final ScheduledExecutorService scheduledUpdate = Executors.newScheduledThreadPool(1);

        // create new update thread every 10 seconds
        scheduledUpdate.scheduleAtFixedRate(new UpdateThread(), 0, 10, TimeUnit.SECONDS);

    }

    /**
     * helper function to stop the cymruCoin daemon
     */
    void stopDeamon() {
        doCmd("./cymrucoind stop", true);
    }


}


