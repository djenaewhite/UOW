import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class RateBike extends JFrame {
    private JTextField bikeID = new JTextField(20);
    private JTextField grade = new JTextField(20);
    private JTextArea experience = new JTextArea();
    private JTextArea improvements = new JTextArea();

    private JPanel pnlDisplay;
    private JPanel pnlPicture;
    private JPanel pnlButton;

    private JButton cmdRate;
    private JButton cmdMenu;
    private RateBike thisframe;

    public RateBike() {
        setTitle("Rate a Bike");
        thisframe = this;

        pnlDisplay = new JPanel(new GridLayout(4, 2));
        pnlPicture = new JPanel();
        pnlButton = new JPanel();

        addLabelAndField("Please enter the Bike's ID:", bikeID);
        addLabelAndField("Letter Grade [A, B, C, D, E]:", grade);
        addLabelAndTextArea("Tell us about your riding experience:", experience);
        addLabelAndTextArea("What are some improvements to be made:", improvements);

        BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File("rates.jpg"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        JLabel picLabel = new JLabel(new ImageIcon(myPicture));
        Dimension size = new Dimension(750, 400);
        picLabel.setPreferredSize(size);
        pnlPicture.add(picLabel);

        cmdRate = new JButton("Rate Bike");
        cmdMenu = new JButton("Return to Menu");

        cmdMenu.addActionListener(new CloseButtonListener());
        cmdRate.addActionListener(new RateBikeButtonListener());



        //thisframe.setBackground(Color.PINK);
        cmdRate.setBackground(Color.GREEN); 
        cmdMenu.setBackground(Color.GREEN);

        pnlButton.add(cmdMenu);
        pnlButton.add(cmdRate);

        add(pnlDisplay, BorderLayout.CENTER);
        add(pnlPicture, BorderLayout.NORTH);
        add(pnlButton, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void addLabelAndField(String labelText, JTextField textField) {
        JPanel rowPanel = new JPanel(new GridLayout(1, 2));
        rowPanel.add(new JLabel(labelText));
        rowPanel.add(textField);
        pnlDisplay.add(rowPanel);
    }
    private void addLabelAndTextArea(String labelText, JTextArea textArea) {
        JPanel rowPanel = new JPanel(new GridLayout(1, 2));
        rowPanel.add(new JLabel(labelText));
        rowPanel.add(new JScrollPane(textArea));
        pnlDisplay.add(rowPanel);
    }
    

    // private void addLabelAndTextArea(String labelText, JTextArea textArea) {
    //     JPanel rowPanel = new JPanel(new GridLayout(1, 2));
    //     rowPanel.add(new JLabel(labelText));
    //     textArea = new JTextArea();
    //     rowPanel.add(new JScrollPane(textArea));
    //     pnlDisplay.add(rowPanel);
    // }

    private boolean isValidGrade(String grade) {
        return grade.equals("A") || grade.equals("B") || grade.equals("C") || grade.equals("D") || grade.equals("E");
    }

    public class CloseButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            // thisframe.setVisible(false);
            dispose();
            MainPage main = new MainPage();
        }
    }

    private boolean isValidID(String filePath, String ID) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("file.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("_");
                for (String part : parts) {
                    if (part.equals(ID)) {
                        return true; 
                    }
                }
            }
        }
        return false; 
    }

    

    public class RateBikeButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("Rates.txt", true))) {
                String ID = bikeID.getText();
                String bikegrade = grade.getText();
                String bikingexperience = experience.getText();
                String improve = improvements.getText();

                while (!isValidGrade(bikegrade)) {
                    JOptionPane.showMessageDialog(thisframe, "Please enter a valid grade from the specified list");
                    bikegrade = JOptionPane.showInputDialog(thisframe, "Enter grade:");

                    if (bikegrade == null) {
                        // Handle the cancellation
                        JOptionPane.showMessageDialog(thisframe, "Grade input canceled. Please provide a valid grade.");
                        return; // Exit the method without writing to the file
                    }
                }
                while(!isValidID("file.txt",ID)){
                    JOptionPane.showMessageDialog(thisframe, "Please enter a valid bike ID number ");
                    ID = JOptionPane.showInputDialog(thisframe, "Enter bike ID:");

                    if (ID == null) {
                        // Handle the cancellation
                        JOptionPane.showMessageDialog(thisframe, "Bike ID input canceled. Please provide a valid ID.");
                        return; // Exit the method without writing to the file
                    }

                }


                // Write to file
                writer.write("BikeID: " + ID);
                writer.newLine();
                writer.write("Bike Rating: " + bikegrade);
                writer.newLine();
                writer.write("Experience: " + bikingexperience);
                writer.newLine();
                writer.write("Improvements: "+ improve);
                writer.newLine();
                writer.newLine();

                JOptionPane.showMessageDialog(thisframe, "Thank you for your rating!");
                

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(thisframe, "Invalid ID number");
                dispose();
                MainPage main = new MainPage();
            } catch (IOException error) {
                JOptionPane.showMessageDialog(thisframe, error);
                dispose();
                MainPage main = new MainPage();
            } finally {
                System.out.println(" ");
            }
            thisframe.setVisible(false);
            dispose();
            MainPage main = new MainPage();
        }
    }

    
}
