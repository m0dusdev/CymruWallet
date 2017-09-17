package cymruWallet;

import java.util.Date;

/**
 * class that crates the update loop for the application
 */
class UpdateThread implements Runnable {

    public void run() {
        FromProcess.doCmd("./cymrucoind &"); // start the daemon
        System.out.print("started update");
        FromProcess.doCmd("./cymrucoind getinfo > log.txt"); // get wallet info
        Gui.addToConsole("UPDATED WALLET INFO @ " + new Date().toString());
        Gui.addToConsole("Started update\n");
        FromProcess.getLog(); // read from file

        Gui.addToConsole("Started sleep");
        System.out.print("Started sleep ");
    }
}


