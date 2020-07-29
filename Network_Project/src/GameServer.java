
import java.io.IOException;
import java.util.*;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class GameServer {

	private static ArrayList<String> names = new ArrayList<>(); 
	private static ArrayList<PrintWriter> writers = new ArrayList<>(); 
	private static HashMap<String, String> list = new HashMap(); 

	public static void main(String[] args) throws Exception {
		System.out.println("The Game server is running...");
		ExecutorService pool = Executors.newFixedThreadPool(5); 

		try (ServerSocket listener = new ServerSocket(4444)) { 
			while (true) {
				pool.execute(new Handler(listener.accept())); 
			}
		}
	}


	private static class Handler implements Runnable {
		
		ChatClient user;
		private String name;
		private Socket socket;
		private Scanner in;
		private PrintWriter out;

		public Handler(Socket socket) {
			this.socket = socket;
		}

		public void run() {

			try {
				in = new Scanner(socket.getInputStream()); 
				out = new PrintWriter(socket.getOutputStream(), true); 


				while (true) {
					out.println("SUBMITNAME"); 
					name = in.nextLine(); 
					if (name == null) {
						return;
					}
					synchronized (names) { 
						if (name.length() > 0 && !names.contains(name)) { 
							names.add(name); 
							break;
						}
					}
				}

				out.println("NAMEACCEPTED " + name);
				for (PrintWriter writer : writers) { 
					writer.println("MESSAGE " + name + " has joined"); 
				}
				writers.add(out); 

				while (true) {
					String input = in.nextLine(); 

					if (input.toLowerCase().startsWith("/quit")) { //문자열이 quit이면 채팅을 끝낸다.
						return;
					}
					
					else if(input.startsWith("/endgame")) {
						return;
					}

					else if(input.startsWith("/result")) {

						String a = input.split(" ")[1]; //ID
						String b = input.split(" ")[2]; //낸거

						System.out.println(a);
						
						synchronized (list) { 
							
							if(list.containsKey(a)) {
								String q = list.get(a);
								
								if(!q.equals(b)) {
									list.put(a, b);
								}
								
							}

							else if(!list.containsKey(a)){
								list.put(a, b);
							}
							
							
						}


						System.out.println(list.size());

						if(list.size() == 2) {

							for (PrintWriter writer : writers) {
								Iterator<String> keys = list.keySet().iterator();

								while(keys.hasNext()) {
									String p = keys.next(); //ID
									String q = list.get(p); //낸거

									System.out.println("result " + p + " " + q);
									writer.println("result " + p + " " + q);

								}
							}

							list.clear();
						}
					}

					else { 
						for (PrintWriter writer : writers) { 
							writer.println("MESSAGE " + name + ": " + input);
						}
					}

				}

			} catch (Exception e) {
				System.out.println(e);
			} finally {
				
				
				
				if (out != null) {
					writers.remove(out); 
				}
				if (name != null) {
					System.out.println(name + " is leaving");
					names.remove(name);
					for (PrintWriter writer : writers) {
						writer.println("MESSAGE " + name + " has left");
					}
				}
				try { socket.close(); } catch (IOException e) {}
			}
		}

	}
}
