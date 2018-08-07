package ir.org.acm.ui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class MainForm extends JFrame {

    JTextArea receiveText;
    JTextArea patternTextArea ;

    private MainForm() {
        initUI();
    }

    private void initUI() {

        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new GridLayout(3, 1));

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenuItem item = new JMenuItem(" ");
        menuBar.add(item);

        JLabel label = new JLabel("Ussd Code:");
        patternTextArea = new JTextArea(2, 15);
        JScrollPane scrollPane = new JScrollPane(patternTextArea);

        JPanel panel = new JPanel(new GridLayout(1, 2));
        JPanel panelPattern = new JPanel();
        panel.add(label);
        panel.add(scrollPane);
        panelPattern.add(panel);
        Border border = BorderFactory.createEtchedBorder();
        Border title = BorderFactory.createTitledBorder(border, "send operation");
        panelPattern.setBorder(title);
        add(panelPattern,BorderLayout.NORTH);

        JPanel panelSendButton = new JPanel();
        JButton sendButton = new JButton("send");
        sendButton.addActionListener(new SubmitAction());
        panelSendButton.add(sendButton);
        add(panelSendButton,BorderLayout.CENTER);

        JPanel receivePanel = new JPanel();
        Border receiveBorder = BorderFactory.createTitledBorder(border, "receive operation");
        receiveText = new JTextArea();
        scrollPane = new JScrollPane(receiveText);
        scrollPane.setBorder(receiveBorder);
        receivePanel.add(scrollPane);
        add(scrollPane,BorderLayout.SOUTH);

    }
    private static Socket socket0;
    public static void main(String[] args) {
        MainForm form = new MainForm();
        form.setVisible(true);
    }

    private class SubmitAction implements ActionListener {

        private  Socket socket;

        @Override
        public void actionPerformed(ActionEvent e) {
            try
            {
                String host = "localhost";
                int port = 25000;
                InetAddress address = InetAddress.getByName(host);
                socket = new Socket(address, port);

                //Send the message to the server
                OutputStream os = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);

                String pattern =patternTextArea.getText();

                String sendMessage = pattern + "\n";
                bw.write(sendMessage);
                bw.flush();
                System.out.println("Message sent to the server : "+sendMessage);

                //Get the return method from the server
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String message = br.readLine();
               // receiveText.setText("Message received from the server : " +message);
                System.out.println("Message received from the server : " +message);
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }
            finally
            {
                //Closing the socket
                try
                {
                    socket.close();
                }
                catch(Exception e1)
                {
                    e1.printStackTrace();
                }
            }
        }
        }
    }

