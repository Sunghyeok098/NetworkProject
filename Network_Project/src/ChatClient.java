
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;

public class ChatClient implements ActionListener {

	private static ArrayList<String> connecting_user_id = new ArrayList<>();
	Socket socket;
	String serverAddress;
	Scanner in;
	PrintWriter out;
	static ChatClient user;
	JFrame frame = new JFrame("Waiting Room");
	JTextField textField = new JTextField(30);
	JTextArea messageArea = new JTextArea(16, 30);
	JTextField ID_field = new JTextField(10);
	JTextField curTime_field = new JTextField(20);
	JTextField UserName_field = new JTextField(10);
	JTextField chat_label = new JTextField(10);
	JTextArea onlineUser_area = new JTextArea(10, 10);
	JButton random = new JButton("Random Match");
	JButton selectOpposent = new JButton("Select Opposent");
	String temp;
	String name;
	int value;
	int width = 711;
	int height = 480;


	TextField PW_field;
	JButton login_button;
	JButton sign_button;
	Image img = null;
	JFrame frame1;

	public ChatClient() {


		frame.setSize(width, height);     
		frame.setResizable(false);
		UserName_field.setText("Online User");
		chat_label.setText("Chat");
		messageArea.setEditable(false);//messageArea를 변경못하게 지정
		onlineUser_area.setEditable(false);
		UserName_field.setEditable(false);
		chat_label.setEditable(false);
		frame.setLayout(new BorderLayout());

		JPanel chatPanel = new JPanel();
		JPanel userPanel = new JPanel();
		chatPanel.setPreferredSize(new Dimension ((width/3)*2-30, height));
		userPanel.setPreferredSize(new Dimension (width/3, height));

		JPanel chat1 = new JPanel();
		JPanel user1 = new JPanel();
		JPanel user2 = new JPanel();

		chat1.setPreferredSize(new Dimension (width/3, height));

		chat1.setLayout(new BorderLayout());
		chat1.add(textField, BorderLayout.SOUTH);
		chat1.add(new JScrollPane(messageArea), BorderLayout.CENTER);

		user2.setLayout(new GridLayout(2,1));
		user2.add(selectOpposent);
		user2.add(random);

		user1.setPreferredSize(new Dimension (width/3, height));
		user1.setLayout(new BorderLayout());
		user1.add(UserName_field, BorderLayout.NORTH);
		user1.add(new JScrollPane(onlineUser_area), BorderLayout.CENTER);
		user1.add(user2,BorderLayout.SOUTH);

		SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd kk:mm:ss");
		Date time = new Date();
		String cur_time= format.format(time);

		userPanel.setLayout(new BorderLayout());
		userPanel.add(curTime_field, BorderLayout.NORTH);
		userPanel.add(user1, BorderLayout.CENTER);

		chatPanel.setLayout(new BorderLayout());
		chatPanel.add(chat_label, BorderLayout.NORTH);
		chatPanel.add(chat1, BorderLayout.CENTER);

		frame.getContentPane().add(chatPanel, BorderLayout.WEST);

		frame.getContentPane().add(userPanel, BorderLayout.EAST);

		curTime_field.setText(cur_time);
		frame.setLocationRelativeTo(null);
		frame.pack();

		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				temp = textField.getText();
				out.println(temp);
				textField.setText("");
			}
		});

		selectOpposent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String receiver = JOptionPane.showInputDialog("Enter receiver's name"); //받는사람의 이름을 입력받는 창을 띄운다

				if(!receiver.equals(null)) { //message 가 입력될 때만 귓속말을 보낸다.

					out.println("/gamerequest " + name + " "+receiver);

				}
			}
		});

	}
	
	public ChatClient(String name) {


		frame.setSize(width, height);     
		frame.setResizable(false);
		UserName_field.setText("Online User");
		chat_label.setText("Chat");
		messageArea.setEditable(false);//messageArea를 변경못하게 지정
		onlineUser_area.setEditable(false);
		UserName_field.setEditable(false);
		chat_label.setEditable(false);
		frame.setLayout(new BorderLayout());

		JPanel chatPanel = new JPanel();
		JPanel userPanel = new JPanel();
		chatPanel.setPreferredSize(new Dimension ((width/3)*2-30, height));
		userPanel.setPreferredSize(new Dimension (width/3, height));

		JPanel chat1 = new JPanel();
		JPanel user1 = new JPanel();
		JPanel user2 = new JPanel();

		chat1.setPreferredSize(new Dimension (width/3, height));

		chat1.setLayout(new BorderLayout());
		chat1.add(textField, BorderLayout.SOUTH);
		chat1.add(new JScrollPane(messageArea), BorderLayout.CENTER);

		user2.setLayout(new GridLayout(2,1));
		user2.add(selectOpposent);
		user2.add(random);

		user1.setPreferredSize(new Dimension (width/3, height));
		user1.setLayout(new BorderLayout());
		user1.add(UserName_field, BorderLayout.NORTH);
		user1.add(new JScrollPane(onlineUser_area), BorderLayout.CENTER);
		user1.add(user2,BorderLayout.SOUTH);

		SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd kk:mm:ss");
		Date time = new Date();
		String cur_time= format.format(time);

		userPanel.setLayout(new BorderLayout());
		userPanel.add(curTime_field, BorderLayout.NORTH);
		userPanel.add(user1, BorderLayout.CENTER);

		chatPanel.setLayout(new BorderLayout());
		chatPanel.add(chat_label, BorderLayout.NORTH);
		chatPanel.add(chat1, BorderLayout.CENTER);

		frame.getContentPane().add(chatPanel, BorderLayout.WEST);

		frame.getContentPane().add(userPanel, BorderLayout.EAST);

		curTime_field.setText(cur_time);
		frame.setLocationRelativeTo(null);
		frame.pack();

		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				temp = textField.getText();
				out.println(temp);
				textField.setText("");
			}
		});

		selectOpposent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String receiver = JOptionPane.showInputDialog("Enter receiver's name"); //받는사람의 이름을 입력받는 창을 띄운다

				if(!receiver.equals(null)) { //message 가 입력될 때만 귓속말을 보낸다.

					out.println("/gamerequest " + name + " "+receiver);

				}
			}
		});

	}

	public void getName() { //이름을 입력받는 창을 띄운다.

		frame1 = new JFrame();
		JPanel panel = new JPanel();

		frame1.add(panel);
		frame1.setResizable(false);
		frame1.setPreferredSize(new Dimension(500, 500));
		frame1.setSize(500, 500);
		frame1.setLocationRelativeTo(null);


		JPanel p = new JPanel();
		p.setLayout(null);
		JLabel label = new JLabel(new ImageIcon("rockpaperscissors.jpg"));
		p.add(label);
		Label b2= new Label("ID :");
		p.add(b2);
		Label b3= new Label("Password :");
		p.add(b3);
		TextField ID_field = new TextField();
		p.add(ID_field);
		TextField PW_field = new TextField();
		p.add(PW_field);
		PW_field.setEchoChar('*');//암호화
		JButton login_button = new JButton("Login");
		p.add(login_button);
		JButton sign_button = new JButton("Signup");
		p.add(sign_button);

		label.setBounds(107, 100, 450, 450);
		b2.setBounds(40, 595, 40, 40);
		b3.setBounds(40, 635, 60, 40);
		ID_field.setBounds(150, 595, 200, 30);
		PW_field.setBounds(150, 635, 200, 30);
		login_button.setBounds(380, 600, 100, 60);
		sign_button.setBounds(500, 600, 100, 60);
		frame1.add(p);
		frame1.setSize(685, 725);
		//frame1.setLocationRelativeTo(null);
		frame1.setTitle("Login Console ");
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setVisible(true);

		login_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String a = "/login " + ID_field.getText() + " " + PW_field.getText();
				System.out.println(a);
				out.println(a); 
			}
		});
		sign_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String a = "/signup " + ID_field.getText() + " " + PW_field.getText();
				System.out.println(a);
				out.println(a); 
			}
		});
	}



	private void run() throws IOException  {

		try {

			socket = new Socket("127.0.0.1", 59001);
			in = new Scanner(socket.getInputStream());
			out = new PrintWriter(socket.getOutputStream(), true);

			System.out.println(socket);
			while (in.hasNextLine()) {
				String line = in.nextLine();
				String str = "";

				if (line.startsWith("SUBMITNAME")) {
					getName();
				}
				else if (line.startsWith("NAMEACCEPTED")) {


					System.out.println("correct");
					JOptionPane.showMessageDialog(null, "Login success!");

					frame1.dispose();
					//frame.dispose();
					user.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					user.frame.setVisible(true);

					this.frame.setTitle("Chatter - " + line.substring(13));

					textField.setEditable(true); 
					name =  line.substring(13);

				}

				else if (line.startsWith("NOTNAMEACCEPTED")) {

					System.out.println("wrong");
					JOptionPane.showMessageDialog(null, "Login Fail!");
					frame1.dispose();
					getName();

				}

				else if(line.startsWith("/Notsign")) {

					System.out.println("ID Duplicate!!");
					JOptionPane.showMessageDialog(null, "ID Duplicate!!");
					frame1.dispose();
					getName();
				}

				else if(line.startsWith("/Oksign")) {

					System.out.println("Complete Sign up!!");
					JOptionPane.showMessageDialog(null, "Complete Sign up!!");
					frame1.dispose();
					getName();

				}

				else if(line.startsWith("/gamerequest")) {

					String a = line.split(" ")[1];
					String b = line.split(" ")[2];

					if(name.equals(a) || name.equals(b)){
						frame.setVisible(false);
						out.println("/quit");
						GameClient user = new GameClient(name);
						user.run();
					}
				}




				else if (line.startsWith("MESSAGE")) {
					messageArea.append(line.substring(8) + "\n");
				}



				else if(line.startsWith("/c")) {

					String a = line.split(" ")[1];

					if(!connecting_user_id.contains(a)) {

						connecting_user_id.add(a);
					}

				}

				else if(line.startsWith("/leave")) {
					messageArea.append(line.substring(7) + "\n");
					String b = line.split(" ")[1];
					connecting_user_id.remove(b);
				}

				Iterator iterator = connecting_user_id.iterator();

				while(iterator.hasNext()) {
					String element = (String) iterator.next();
					str = str + element + "\n";
					onlineUser_area.setText(str);
				}

			}

		} finally {
			frame.setVisible(false);
			frame.dispose();
		}
	}

	public void actionPerformed(ActionEvent event) {

	}

	public static void main(String[] args) throws Exception{


		user = new ChatClient();
		user.run();
	}

}