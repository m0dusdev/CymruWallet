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

class FromProcess {

    private static AtomicBoolean running = new AtomicBoolean(false);


    // run a command in the working directory of the wallet
    static void doCmd(String command) {
        try {
            Process commandProc = Runtime.getRuntime().exec(command);
            commandProc.waitFor();
            Gui.addToConsole(command + " = Successful");
        } catch (IOException | InterruptedException exp) {
            Gui.addToConsole(command + " = Failed");
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

        } catch (IOException io) {

            Gui.addToConsole("IO error : File not found");

        } catch (InterruptedException ie) {

            Gui.addToConsole("Error : Could not run ./cymrucoind getinfo > " +
                    "log.txt | Is cymruWallet.jar in /src/ ?");
        }


    }

    /**
     * Starts the update loop that reads the output of
     * <Code>"./cymrucoind getinfo > log.txt" </Code>
     */
    void startUpdate() {
        doCmd("./cymrucoind &");
        System.out.print("started");
        doCmd("./cymrucoind getinfo > log.txt");
        Gui.addToConsole("UPDATED WALLET INFO @ " + new Date().toString());
        Gui.addToConsole("Started update\n");


        getLog(); // read from file

        Gui.addToConsole("Started sleep");


    }


    static void stopDeamon() {
        doCmd("./cymrucoind stop");
    }


}


