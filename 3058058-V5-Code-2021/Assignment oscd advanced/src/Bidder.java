//Student Number:2942564 advanced version
import java.net.*;
import java.io.*;
import java.util.*;

public class Bidder {

	private Random rnd;

	private ServerSocket in_ss;

	private Socket in_soc;
	private Socket out_soc;

	String localhost = "127.0.0.1";
	String n_host_name;

	int in_port;
	int out_port;

	int token;

	// BIDDER:
	// receives the token by a socket communication through in_port;
	// performs its auction algorithm:
	// - reads from bid.txt,
	// - decides whether to bit or not,
	// - in case of bidding, updates bid;
	// forwards the lottery token by a socket communication through out_port- completed

	public Bidder(int inpor, int outpor) {

		rnd = new Random();
		in_port = inpor;
		out_port = outpor;

		RandomAccessFile current_bid;
		int the_bid;

		System.out.println("Bidder  : " + in_port + " of distributed lottry is active ....");

		try {

			// >>>
			// **** Wait for the token and receive it from a soket listening on the in_port- completed
			in_ss = new ServerSocket(in_port);
			/*
			 * the while loop below is used to create an infinite number of loops for the bidders
			 * so they will continue to run even after the number of rounds is done. 
			 */
			while (true) {
				in_soc= in_ss.accept();
			// >>>
			// **** Perform the auction algorithm (with suitable delay/sleep of 1 sec)- completed
				if (rnd.nextInt(2) == 0) {
					System.out.println("Bidder  : " + in_port + " - received token. No bid");
				} else {

					try {
						current_bid = new RandomAccessFile("bid.txt", "rw");
						current_bid.seek(0);
						int bid = current_bid.readInt() + 10;
						System.out.println("Bidder: " + in_port + " - token received. The bid is " + bid);
						current_bid.seek(0);
						current_bid.writeInt(bid);
						current_bid.close();

					} catch (IOException e) {
						System.out.println("Exception found when opening the file " + e);
					}

				}

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				// >>>
				// **** Forward the the token through the out_port;
				out_soc = new Socket(localhost, out_port);
				OutputStreamWriter  outWrite = new OutputStreamWriter(out_soc.getOutputStream());
				PrintWriter outputWrite = new PrintWriter( outWrite);
				outputWrite.println(token);
				outWrite.flush();
				//out socket is closed here for each bidder
				out_soc.close();
				System.out.println("Bidder: " + in_port + " - forwarded token to " + out_port);
			} 

		} catch (java.io.IOException e) {
			System.out.println(e);
			System.exit(1);
		}

	}

	public static void main(String args[]) {

		String n_host_name = "";
		int n_port;

		// receive own port and next port in the ring at launch time
		if (args.length != 2) {
			System.out.print("Usage: Bidder [port number] [forward port number] ");
			System.exit(1);
		}

		// get the IP address of the node - might be useful on multi-computer runs
		try {
			InetAddress n_inet_address = InetAddress.getLocalHost();
			n_host_name = n_inet_address.getHostName();
			System.out.println("node hostname is " + n_host_name + ":" + n_inet_address);
		} catch (java.net.UnknownHostException e) {
			System.out.println(e);
			System.exit(1);
		}

		Bidder b = new Bidder(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
	}

}
