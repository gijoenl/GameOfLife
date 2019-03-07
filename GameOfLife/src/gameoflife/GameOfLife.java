/**
 * This project is an implementation of the Game of Life.
 * @author Nicolas_Online
 */
package gameoflife;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameOfLife implements MouseListener, ActionListener, Runnable{
                                                                                // VARIABLES AND OBJECTS
    int size = 3;
    int counter = 0;
    boolean[][] cells = new boolean[size][size];
    JFrame frame = new JFrame("Nicolas's Game Of Life");
    VuePanel vue = new VuePanel(cells);
    Container bottomContainer = new Container();
    JButton step = new JButton("Step");
    JButton start = new JButton("Start");
    JButton load = new JButton("Load");
    JButton stop = new JButton("Stop!");
    private JLabel label;
    boolean running = false;
    
    public GameOfLife(){                                                        // CONSTRUCTOR
        frame.setSize(600, 600);                                                // Frame size
        frame.setLayout(new BorderLayout());                                    // Setting a border layout object
        frame.add(vue, BorderLayout.CENTER);                                    // Adding a centered panel to our frame
        
        vue.addMouseListener(this);
        
        bottomContainer.setLayout(new GridLayout(1, 4));                        // bottom container
        bottomContainer.add(step);                                              // adding each button and its listener
        step.addActionListener(this);
        bottomContainer.add(start);
        start.addActionListener(this);
        bottomContainer.add(stop);
        stop.addActionListener(this);
        bottomContainer.add(load);
        load.addActionListener(this);
        
        label = new JLabel("Generation " + counter);
        bottomContainer.add(label);
        frame.add(bottomContainer, BorderLayout.SOUTH);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                   // Closing the frame exits the programme
        frame.setVisible(true);                                                 // the Frame is visible
    }
                                                                                // Main method
    public static void main(String[] args) {
            new GameOfLife();
        }
    
    @Override                                                                   // ABSTRACT METHODS FROM MOUSE LISTENER
    public void mouseClicked(MouseEvent event) {}
    @Override
    public void mousePressed(MouseEvent event) {}
    @Override
    public void mouseReleased(MouseEvent event) {                               // updating the frame
        //System.out.println(event.getX() + ", " + event.getY());
        double width = (double)vue.getWidth()/cells[0].length;                  // converting the mouse position depending of the size of our frame
        double height = (double)vue.getHeight()/cells.length;                   //
        int column = Math.min(cells[0].length -1, (int)(event.getX()/width));   // getting the min value to avoid clicling 1px out of the frame(out of the
        int row = Math.min(cells.length -1, (int)(event.getY()/height));        // bounds of the arrays, just in case
        //System.out.println(column + ", " + row);
        
        cells[row][column] = !cells[row][column];                               // cheeky way to make the value to its opposite
        frame.repaint();
    }
    @Override
    public void mouseEntered(MouseEvent event) {}
    @Override
    public void mouseExited(MouseEvent event) {}
    @Override                                                                    // ABSTRACT METHOD FROM ACTION LISTENER
    public void actionPerformed(ActionEvent event) {
        if(event.getSource().equals(step)) {
            System.out.println("Step");
            step();
        }
        
        if(event.getSource().equals(start)) {                                   // Using multi-thread to exectute a separate thread
            System.out.println("Start");
            if (running == false) {
                running = true;
                Thread t = new Thread(this);
                t.start();
            }
        }
        
        if(event.getSource().equals(stop)) {
            System.out.println("Stop");
            try {              
                   fileWriter(toString());                                      // calling the fileWriter function and converting
            }                                                                   // the array values into a string
            catch (Exception ex) {
                Logger.getLogger(GameOfLife.class.getName()).log(Level.SEVERE, null, ex);
            }
            running = false;
        }        
        
        if(event.getSource().equals(load)) { // //
            System.out.println("Loading file");
            if (new File("C:/Users/Nicolas_Online/Documents/NetBeansProjects/GameOfLife/log.txt").exists()) {
                File file = new File("C:/Users/Nicolas_Online/Documents/NetBeansProjects/GameOfLife/log.txt");
                try {
                    fileLoader(file);
                } 
                catch (FileNotFoundException ex) {
                    Logger.getLogger(GameOfLife.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("File not found;");
                } catch (IOException ex) {
                    Logger.getLogger(GameOfLife.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }   
    }
    
    public void fileWriter(String something) throws Exception{                  // This function creates a new file
        BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt"));  
        writer.write(something);                                                // writing the string value in the file
        writer.close();
    }
    
    public void fileLoader(File file) throws FileNotFoundException, IOException{
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line1 = "";
        String line = "";
        while((line=reader.readLine()) != null) {
            for(int i = 0; i <line.length(); i++) {
                char c = line.charAt(i);
            }
            
            line1 = line;
            converter(line1);
            System.out.println(line1);
        }
        reader.close();
    }
    
    public void converter(String line){
      
        int row =0;
        int column = 0;
        
        for(int i=0; i<line.length(); i++){
            if (line.charAt(i) == 'O'){
                cells[row][column] = false;
                column++;
            }
            if (line.charAt(i) == 'X'){
               cells[row][column] = true;
                column++;
            }
            
            if (column == cells.length ){
                row ++;
                column = 0;
            }
            System.out.println(cells[row][column]);
        }
    
    }
    
    public String toString(){                                                   // Converts [][] into a string
        String str = "";
        for(int row = 0; row < cells.length; row++){
            str += "|";
            for(int column = 0; column < cells[0].length; column++) {
                if (cells[row][column]) {str += "X|";}
                else {str += "O|";}
            }
            str += "\r\n";
        }
        return str;
    }
    
    @Override                                                                   // ABSTRACT METHOD FROM RUNNABLE
    public void run() {
        while(running == true) {
            step();
            try {
                Thread.sleep(150);                                              // slowing down the thread process
            }   
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void step() {
        boolean[][] nextCells = new boolean[cells.length][cells[0].length];
        for (int row = 0; row < cells.length; row++) {
            for (int column = 0; column < cells[0].length; column++) {
                int neighborCount = 0;
                var save = cells[row][column];
                if (row > 0 && column > 0 && cells[row-1][column-1] == true) { neighborCount++; }                               // UP LEFT
                if (row > 0 && cells[row-1][column] == true) { neighborCount++; }                                               // UP 
                if (row > 0 && column < cells[0].length-1 && cells[row-1][column+1] == true) { neighborCount++; }               // UP RIGHT
                if (column > 0 && cells[row][column-1] == true) { neighborCount++; }                                            // LEFT
                if (column < cells[0].length-1 && cells[row][column+1] == true) { neighborCount++; }                            // RIGHT
                if (row < cells.length-1 && column > 0 && cells[row+1][column-1] == true) { neighborCount++; }                  // DOWN LEFT
                if (row < cells.length-1 && cells[row+1][column] == true) { neighborCount++; }                                  // DOWN
                if (row < cells.length-1 && column < cells[0].length-1 && cells[row+1][column+1] == true) { neighborCount++; }  // DOWN RIGHT

                if (cells[row][column] == true) {                               // If cell is alive
                    if (neighborCount == 2 || neighborCount == 3) {             
                        nextCells[row][column] = true;                          // alive next time
                    }
                    else {
                        nextCells[row][column] = false;                         // dead next time
                    }
                }
                else {                                                          // If cll is dead
                    if (neighborCount ==3 ) {
                        nextCells[row][column] = true;                          // alive next time
                    }
                    else {
                        nextCells[row][column] = false;                         // dead next time
                    }
                }
            }
        }
        cells = nextCells;                                                      // Updating cells data for next step()
        vue.setCells(nextCells);
        counter++;
        label.setText("Generation : "+Integer.toString(counter));
        frame.repaint();
    } 
}
