
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

public class GameClient implements ActionListener  {

	ChatClient user;
	String serverAddress;
	Scanner in;
	PrintWriter out;

	JFrame frame = new JFrame("Game");
	JTextField textField = new JTextField(30);
	JTextArea messageArea = new JTextArea(16, 30);
	JTextField ID_field = new JTextField(10);
	JTextField curTime_field = new JTextField(20);
	JTextField timer_field = new JTextField(10);
	JLabel user1_field = new JLabel();
	JTextField user1_ID = new JTextField(10);

	//setHorizontalAlignment(JTextField.Right);
	JButton rock = new JButton("rock");
	JButton sisor= new JButton("sisor");
	JButton paper = new JButton("paper");
	JButton wait_room = new JButton("Go wait Room");
	Socket socket;
	int value;

	int width = 711;
	int height = 480;

	String ID;

	public GameClient(String name) {
		
		try {
			socket = new Socket("127.0.0.1", 4444);
		} catch (UnknownHostException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		this.ID = name;
		frame.setSize(width, height);

		frame.setLayout(new BorderLayout());
		frame.setResizable(false);
		JPanel gamePanel = new JPanel();
		JPanel chatPanel = new JPanel();
		gamePanel.setPreferredSize(new Dimension ((width/3)*2-16, height));
		chatPanel.setPreferredSize(new Dimension (width/3 + 10, height));

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		JPanel user1 = new JPanel();
		//JPanel user2 = new JPanel();

		JPanel selectPanel = new JPanel();
		selectPanel.add(rock);
		selectPanel.add(sisor);
		selectPanel.add(paper);

		user1.setPreferredSize(new Dimension ((width/3)*2-16, height));
		//user2.setPreferredSize(new Dimension (width/3-8, height));

		user1.setLayout(new BorderLayout());
		user1.add(user1_ID, BorderLayout.NORTH);
		user1.add(user1_field, BorderLayout.CENTER);
		user1.add(selectPanel, BorderLayout.SOUTH);



		//user2.setLayout(new BorderLayout());
		//user2.add(new JTextField(10), BorderLayout.NORTH);
		//user2.add(new JButton("C"), BorderLayout.CENTER);
		//user2.add(new JButton("S"), BorderLayout.SOUTH);

		//SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd kk:mm:ss");
		//Date time = new Date();
		//String cur_time= format.format(time);

		chatPanel.setLayout(new BorderLayout());
		chatPanel.add(curTime_field, BorderLayout.NORTH);
		chatPanel.add(messageArea, BorderLayout.CENTER);
		chatPanel.add(textField, BorderLayout.SOUTH);

		gamePanel.setLayout(new BorderLayout());
		gamePanel.add(wait_room, BorderLayout.NORTH);
		gamePanel.add(user1, BorderLayout.WEST);
		//gamePanel.add(user2, BorderLayout.EAST);

		frame.getContentPane().add(gamePanel, BorderLayout.WEST);

		frame.getContentPane().add(chatPanel, BorderLayout.EAST);

		Timer m_timer = new Timer();

		TimerTask m_task = new TimerTask() {

			@Override
			public void run() {

				setTime();

			}
		};
		m_timer.schedule(m_task, 10);


		//curTime_field.setText(cur_time);

		rock.addActionListener(this);
		sisor.addActionListener(this);
		paper.addActionListener(this);


		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println(textField.getText()); //텍스트 필드에서 텍스르를 입력받아 서버로 보낸다.
				textField.setText(""); //텍스트를 입력받고 나면 다시 ""로 텍스트 창을 초기화한다.

			}
		});

		wait_room.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println("/endgame ");

			}
		});
	}

	public void actionPerformed(ActionEvent event) {

		if(event.getSource() == rock) {
			ImageIcon icon = new ImageIcon("rock.jpg");
			Image im = icon.getImage(); 
			Image im2 = im.getScaledInstance(460, 400, Image.SCALE_DEFAULT);
			ImageIcon icon2 = new ImageIcon(im2);
			user1_field.setIcon(icon2);
			user1_field.setText("Rock");
			System.out.println(user1_field.getText());
			String a = "/result " + ID + " " + user1_field.getText();
			System.out.println(a);
			out.println(a); 
		}
		else if(event.getSource() == sisor) {
			ImageIcon icon = new ImageIcon("sisor.png");
			Image im = icon.getImage(); //뽑아온 이미지 객체 사이즈를 새롭게 만들기!
			Image im2 = im.getScaledInstance(460, 400, Image.SCALE_DEFAULT);
			ImageIcon icon2 = new ImageIcon(im2);
			user1_field.setIcon(icon2);
			user1_field.setText("Sisor");
			System.out.println(user1_field.getText());
			String a = "/result " + ID + " " + user1_field.getText();
			System.out.println(a);
			out.println(a); //텍스트 필드에서 텍스르를 입력받아 서버로 보낸다.
		}
		else {
			ImageIcon icon = new ImageIcon("paper.png");
			Image im = icon.getImage(); //뽑아온 이미지 객체 사이즈를 새롭게 만들기!
			Image im2 = im.getScaledInstance(460, 400, Image.SCALE_DEFAULT);
			ImageIcon icon2 = new ImageIcon(im2);
			user1_field.setIcon(icon2);
			user1_field.setText("Paper");
			System.out.println(user1_field.getText());
			String a = "/result " + ID + " " + user1_field.getText();
			System.out.println(a);
			out.println(a); //텍스트 필드에서 텍스르를 입력받아 서버로 보낸다.
		}

	}

	public void setTime() {



		SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd kk:mm:ss");
		Date time = new Date();
		String cur_time= format.format(time);
		curTime_field.setText(cur_time);

	}

	public void run() throws IOException {


		try {
			//Socket socket = new Socket("127.0.0.1", 4444);

			in = new Scanner(socket.getInputStream());
			out = new PrintWriter(socket.getOutputStream(), true);



			while (in.hasNextLine()) { 

				String line = in.nextLine(); 
				if (line.startsWith("SUBMITNAME")) {
					out.println(ID); 
				} 

				else if (line.startsWith("NAMEACCEPTED")) { 
					this.frame.setTitle("Game - " + line.substring(13)); 
					ID = line.substring(13); 
					user1_ID.setText(ID);
					textField.setEditable(true); 
				} 

				else if (line.startsWith("MESSAGE")) { 
					messageArea.append(line.substring(8) + "\n"); 
				}

				else if(line.startsWith("result")) {

		               String user2_ID = line.split(" ")[1];
		               String user2_input = line.split(" ")[2];

		               if(!user2_ID.equals(ID)) {

		                  if(user1_field.getText().equals("Rock")) {

		                     if(user2_input.equals("Rock")) {
		                        messageArea.append("Draw\n"); 
		                        JOptionPane.showMessageDialog(null, "Draw");
		                     }
		                     else if(user2_input.equals("Sisor")) {

		                        messageArea.append("You Win\n"); 
		                        JOptionPane.showMessageDialog(null, "You win");
		                     }
		                     else if(user2_input.equals("Paper")) {

		                        messageArea.append("You lose\n");
		                        JOptionPane.showMessageDialog(null, "You lose");
		                     }
		                  }

		                  else if(user1_field.getText().equals("Sisor")) {

		                     if(user2_input.equals("Rock")) {
		                        messageArea.append("You lose\n"); 
		                        JOptionPane.showMessageDialog(null, "You lose");
		                     }
		                     else if(user2_input.equals("Sisor")) {
		                        messageArea.append("Draw\n"); 
		                        JOptionPane.showMessageDialog(null, "Draw");
		                     }
		                     else if(user2_input.equals("Paper")) {
		                        messageArea.append("You Win\n"); 
		                        JOptionPane.showMessageDialog(null, "You win");

		                     }
		                  }

		                  else if(user1_field.getText().equals("Paper")) {

		                     if(user2_input.equals("Rock")) {

		                        messageArea.append("You Win\n");
		                        JOptionPane.showMessageDialog(null, "You win");
		                     }
		                     else if(user2_input.equals("Sisor")) {

		                        messageArea.append("You lose\n"); 
		                        JOptionPane.showMessageDialog(null, "You lose");
		                     }
		                     else if(user2_input.equals("Paper")) {
		                        messageArea.append("Draw\n"); 
		                        JOptionPane.showMessageDialog(null, "Draw");

		                     }
		                  }
		               }

		            }


		         }
		} finally {
			frame.setVisible(false);
			frame.dispose();
			

		}

	}


}