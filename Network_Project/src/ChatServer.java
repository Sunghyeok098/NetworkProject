
import java.awt.HeadlessException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.util.Date;

public class ChatServer {

	private static Set<PrintWriter> writers = new HashSet<>();
	private static ArrayList<String> connecting_user_id = new ArrayList<>();

	public static void main(String[] args) throws Exception {

		System.out.println("The chat server is running...");
		ExecutorService pool = Executors.newFixedThreadPool(500);
		try (ServerSocket listener = new ServerSocket(59001)) {
			while (true) {
				pool.execute(new Handler(listener.accept()));


			}
		}
	}


	static void file_update(String s) {
		String name="";
		name=name.concat(s);
		String split_line1;
		String split_line2;
		String result="";
		int position1, position2;
		String fileName1 = "회원정보.txt";
		String fileName2 = "temp.txt";
		PrintWriter outputStream = null;
		Scanner inputStream = null;
		try {
			outputStream = new PrintWriter(new File(fileName2));
			inputStream = new Scanner(new File(fileName1)); 
		}catch(FileNotFoundException e) {
			e.printStackTrace();

		}

		SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd kk:mm:ss");
		Date time = new Date();
		String cur_time= format.format(time);


		while (inputStream.hasNext()) {
			String line = inputStream.nextLine();
			position1 = line.indexOf("/");
			split_line1= line.substring(0, position1);
			if(name.equalsIgnoreCase(split_line1)) {
				position2 = line.lastIndexOf("/");
				split_line2 = line.substring(0, position2);
				result=result.concat(split_line2);
				result=result.concat("/");
				result=result.concat(cur_time);
				System.out.println(result);
				outputStream.println(result);
				result="";


			}
			else {
				result=result.concat(line);
				System.out.println(result);
				outputStream.println(result);
				result="";

			}

		}

		inputStream.close();
		outputStream.close();

		try {
			outputStream = new PrintWriter(new File(fileName1));
			inputStream = new Scanner(new File(fileName2)); 
		}catch(FileNotFoundException e) {
			e.printStackTrace();

		}
		while (inputStream.hasNext()) {
			String line = inputStream.nextLine();
			outputStream.println(line);
		}
		inputStream.close();
		outputStream.close();

	}





	private static class Handler implements Runnable {

		private String name;
		private Socket socket;
		private Scanner in;
		private PrintWriter out;

		public Handler(Socket socket) {
			this.socket = socket;
		}

		public boolean checkID(String ID, String pw) throws FileNotFoundException {

			String s;
			String[] array;
			int check=0;
			BufferedReader bos = new BufferedReader(new FileReader("회원정보.txt"));

			try {

				while((s=bos.readLine())!=null){

					array=s.split("/");

					int decry_value = Integer.parseInt(array[2]);
					String str1 = "";
					for(int i=0;i<pw.length();i++) {
						char temp = (char) (pw.charAt(i) + decry_value);
						str1 = str1 + temp;
					}


					if(ID.equals(array[0]) && str1.equals(array[1])){

						check = 1;
						SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd kk:mm:ss");
						Date time = new Date();
						String cur_time= format.format(time);
						array[4] = cur_time;
						break;
					}

				}
				bos.close();

			} catch (IOException e) {
				
				e.printStackTrace();
			}

			if(check == 1) {

				return true;
			}
			else
			{

				return false;
			} 

		}

		public boolean whisper(String a) {
			if(a.contains("<")&&a.contains("/>"))
				return true;
			else
				return false;
		}

		public boolean saveInfo(String id, String pw) throws IOException {


			String s;
			String[] array;
			int check2=0;
			BufferedReader bos = new BufferedReader(new FileReader("회원정보.txt"));
			while((s=bos.readLine())!=null){
				array=s.split("/");
				if(id.equals(array[0]))
				{
					check2=1;
					break;
				}
				else
				{
					check2=0;
				}
			}
			bos.close();

			if(check2==1)
			{

				return false;

			}
			else
			{
				BufferedWriter bos2 = new BufferedWriter(new FileWriter("회원정보.txt",true));
				SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd kk:mm:ss");
				Date time = new Date();
				String cur_time= format.format(time);

				Random random = new Random();
				int randomValue = random.nextInt(5) +1;
				String str = "";
				for(int i=0;i<pw.length();i++) {
					char temp = (char) (pw.charAt(i) + randomValue);
					str = str + temp;
				}

				bos2.write(id +"/");
				bos2.write(str +"/");
				bos2.write(randomValue+"/");
				bos2.write(0+"/");
				bos2.write(0+"/");
				bos2.write(cur_time + "\r\n");
				bos2.close();
				return true;


			}

		}

		public void run() {

			try {
				in = new Scanner(socket.getInputStream());
				out = new PrintWriter(socket.getOutputStream(), true);


				out.println("SUBMITNAME");
				while (true) {



					String check = in.nextLine();
					System.out.println(check);

					if(check.startsWith("/signup")) {

						String id = check.split(" ")[1];
						String pwd = check.split(" ")[2];
						System.out.println(id + pwd);
						if(!saveInfo(id, pwd)) {
							out.println("/Notsign ");

						}
						else {
							out.println("/Oksign ");
						}


						
					}

					else if(check.startsWith("/login")) {

						String ID = check.split(" ")[1];
						String pw = check.split(" ")[2];

						if(checkID(ID, pw)) {
							out.println("NAMEACCEPTED " + ID);
							name = ID;

							file_update(ID);




							if (name == null) {
								return;
							}

							synchronized (connecting_user_id) {
								if (name.length() > 0 && !connecting_user_id.contains(name)) {
									connecting_user_id.add(name);
									break;
								}
							}

							break;


						}

						else {
							out.println("NOTNAMEACCEPTED ");
						}

					}
				}


				for(PrintWriter writer : writers) {
					writer.println("MESSAGE " + name + " has joined");
				}
				writers.add(out);


				Iterator iterator = connecting_user_id.iterator();

				while(iterator.hasNext()) {
					String element = (String) iterator.next();
					for (PrintWriter writer : writers) {
						writer.println("/c " + element);
					}
				}


				while (true) {

					String input = in.nextLine();
					if (input.toLowerCase().startsWith("/quit")) {
						return;
					}

					if(whisper(input) == true) {
						for (PrintWriter writer : writers) {
							writer.println("WHISPER " + name +": " + input);
						}
					}

					else if(input.startsWith("/gamerequest ")) {

						String a = input.split(" ")[1];
						String b = input.split(" ")[2];
						for (PrintWriter writer : writers) {
							writer.println("/gamerequest " + a + " " + b);
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
					synchronized (connecting_user_id) {
						connecting_user_id.remove(name);
					}

					for (PrintWriter writer : writers) {
						writer.println("/leave " + name + " has left");
					}
				}
				try { socket.close(); } catch (IOException e) {}
			}
		}
	}
}