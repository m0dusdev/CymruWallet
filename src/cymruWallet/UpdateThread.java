package cymruWallet;

import java.util.Date;

/**
 * <li>runnable that performs update operations</>
 * @see cymruWallet.FromProcess <code>startUpdate()</code>
 */

class UpdateThread implements Runnable {

    public void run() {
        FromProcess.doCmd("./cymrucoind &", false); // start the daemon
        System.out.print("started update");
        FromProcess.doCmd("./cymrucoind getinfo > log.txt", false); // get wallet info

        Gui.addToConsole("UPDATED WALLET INFO @ " + new Date().toString());
    }
}


