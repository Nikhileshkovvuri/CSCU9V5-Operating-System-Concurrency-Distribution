//Student Number:2942564 basic version
import java.net.*;
import java.io.*;
import java.util.*;

public class Auctioneer {

	private Random rnd;

	private ServerSocket in_ss;

	private Socket in_soc;
	private Socket out_soc;

	String localhost = "127.0.0.1";
	String n_host_name;

	int in_port;
	int out_port;

	int token;

	public Auctioneer(int inpor, int outpor) {

		rnd = new Random();
		in_port = inpor;
		out_port = outpor;
		RandomAccessFile current_bid;
		int the_bid = 100; // >>> THis is used to set the initial price

		System.out.println("Auctioneer: " + in_port + " of distributed lottery is active ....");

		// AUCTIONEER:
		// creates the file bid.txt containing the initial price (the_bid)
		// generates and forwards the token to the next node in the ring
		// waits to receive the token back and concludes the auction- completed

		try {

			// >>> Create and instantiates the file named bid.txt with the initial price
			// (the_bid)-completed
			// below the file bid.txt is created and the initial auction bid is written.
			current_bid = new RandomAccessFile("bid.txt", "rw");
			current_bid.seek(0);
			current_bid.writeInt(the_bid);
			current_bid.close();

		}

		// >>> Select a more specific exception type where possible/appropriate!- Done
		catch (IOException e) {
			System.out.println("Exception when opening the file " + e);
		}

		System.out.println("Auctioneer: " + in_port + " -  STARTING AUCTION  with price = " + the_bid);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}

		try {

			// >>>
			// **** Forward the the token through a socket that connects to the out_port- completed
			/*
			 * the token is forwarded from the out socket of the auctioneer to the local
			 * host or more precisely the first bidder
			 */
			out_soc = new Socket(localhost, out_port);
			OutputStreamWriter outWrite = new OutputStreamWriter(out_soc.getOutputStream());
			PrintWriter outputWrite = new PrintWriter(outWrite);
			outputWrite.println(token);
			outputWrite.flush();
			out_soc.close();
			System.out.println("Auctioneer: " + in_port + " :: sent token to " + out_port);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}

			// >>>
			// **** Wait for the token and receive it from the in_port through a suitable
			// socket-based server mechanism;
			
			in_ss = new ServerSocket(in_port);
			in_soc = in_ss.accept();

			System.out.println("Auctioneer: " + in_port + " :: received token back");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}

		}

		// >>> Select a more specific exception type where possible/appropriate! -done
		catch (IOException e) {
			System.out.println(e);
			System.exit(1);
		}

	}

	public static void main(String args[]) {

		String n_host_name = "";
		int n_port;

		if (args.length != 2) {
			System.out.print("Usage: Auctioneer [port number] [forward port number]");
			System.exit(1);
		}

		// get the IP address and the port number of the node
		try {
			InetAddress n_inet_address = InetAddress.getLocalHost();
			n_host_name = n_inet_address.getHostName();
			System.out.println("node hostname is " + n_host_name + ":" + n_inet_address);
		} catch (java.net.UnknownHostException e) {
			System.out.println(e);
			System.exit(1);
		}

		Auctioneer a = new Auctioneer(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
	}

}
