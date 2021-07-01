import java.awt.*;
import java.io.*;
import java.net.*;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Client extends JFrame{

    Socket socket;
    BufferedReader br;
    PrintWriter out;

    //Declare Components
    private JLabel heading=new JLabel("Client Area");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Roboto",Font.PLAIN,20);

    //Constructor
    public Client()
    {
        try
        {
            System.out.println("Sending request to server");
            socket=new Socket("127.0.0.1",7778);
            System.out.println("Connection established");

            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();

            startReading();
            // startWriting();

        }
        catch(Exception e)
        {
            // e.printStackTrace();
            
        }

    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()==10)
                {
                    String contentToSend=messageInput.getText();
                    out.println(contentToSend);
                    out.flush();
                    messageArea.append("Me : "+contentToSend+"\n");
                    messageInput.setText("");
                    messageInput.requestFocus();


                }
                
            }
            
        });
    }

    private void createGUI()
    {
        //gui code
        this.setTitle("Client Messenger[END]");
        this.setSize(600,700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //component coding
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        
        heading.setIcon(new ImageIcon("clientlogo.jpg"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);

        //setting the layout of frame
        this.setLayout(new BorderLayout());

        //adding components to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);


        this.setVisible(true);
    }
    public void startReading()
    {
               //thread to read data
               Runnable r1=()->{
                System.out.println("reader started");
                try
                {
                    while(true)
                {
                    String msg=br.readLine();
                    if(msg.equals("exit"))
                    {
                        System.out.println("Server has terminated the chat");
                        JOptionPane.showMessageDialog(this, "Server has terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    // System.out.println("Server : "+msg);
                    messageArea.append("Server : "+msg+"\n");
                }  

                }
                 
                    catch(Exception e)
                    {
                        //e.printStackTrace();   
                        System.out.println("Connection closed");
                    }
                    
                
            };
            new Thread(r1).start();

    }
    public void startWriting()
    {
         //thread to take data from user and send it to client
         Runnable r2=()->{
            System.out.println("Writer Started");
            try
            {
                while(!socket.isClosed())
            {
                 BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content= br1.readLine();
                    out.println(content);
                    out.flush();
                    if(content.equals("exit"))
                    {
                        socket.close();
                        break;
                    }
            }
           
        }
                catch(Exception e)
                {
                    e.printStackTrace();
                   
                }
        };
        new Thread(r2).start();

    }
    public static void main(String[] args)
    {
        System.out.println("this is client");
        new Client();
    }
    
}
