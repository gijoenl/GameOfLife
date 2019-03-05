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
import javax.swing.JButton;
import javax.swing.JPanel;

public class GameOfLife implements MouseListener, ActionListener, Runnable{
                                                                                // VARIABLES AND OBJECTS
    int size = 10;
    boolean[][] cells = new boolean[size][size];
    JFrame frame = new JFrame("Nicolas's Game Of Life");
    GameOfLifeController controller = new GameOfLifeController(cells);
    Container bottomContainer = new Container();
    JButton step = new JButton("Step");
    JButton start = new JButton("Start");
    JButton stop = new JButton("Stop!");       
    boolean running = false;
    
    public GameOfLife(){                                                        // CONSTRUCTOR
        frame.setSize(600, 600);                                                // Frame size
        frame.setLayout(new BorderLayout());                                    // Setting a border layout object
        frame.add(controller, BorderLayout.CENTER);                             // Adding a centered panel to our frame
        controller.addMouseListener(this);
        
        bottomContainer.setLayout(new GridLayout(1, 3));                        // bottom container
        bottomContainer.add(step);                                              // adding each button and its listener
        step.addActionListener(this);
        bottomContainer.add(start);
        start.addActionListener(this);
        bottomContainer.add(stop);
        stop.addActionListener(this);
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
    public void mouseReleased(MouseEvent event) {
        //System.out.println(event.getX() + ", " + event.getY());
        double width = (double)controller.getWidth()/cells[0].length;           // converting the mouse position depending of the size of our frame
        double height = (double)controller.getHeight()/cells.length;            //
        int column = Math.min(cells[0].length -1, (int)(event.getX()/width));   // getting the min value to avoid clicling 1px out of the frame(out of the
        int row = Math.min(cells.length -1, (int)(event.getY()/height));        // bounds of the arrays, just in case
        System.out.println(column + ", " + row);
        
        cells[row][column] = !cells[row][column];                               // cheeky way to make the value to its opposite
        frame.repaint();                                                        // updating the frame
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
            running = false;
        }
    }
    @Override                                                                   // ABSTRACT METHOD FROM RUNNABLE
    public void run() {
        while(running == true) {
            step();
            try {
                Thread.sleep(300);                                              // slowing down the thread process
            }   
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    /*
    *   row-1, column-1     row-1, column       row-1, column+1
    *   row, column-1       row, column         row, column+1
    *   row+1, column-1     row+1, column       row+1, column+1
    */
    public void step() {
        boolean[][] nextCells = new boolean[cells.length][cells[0].length];
        for (int row = 0; row < cells.length; row++) {
            for (int column = 0; column < cells[0].length; column++) {
                int neighborCount = 0;
                if (row > 0 && column > 0 && cells[row-1][column-1] == true) { neighborCount++; }                   // UP LEFT
                if (row > 0 && cells[row-1][column] == true) { neighborCount++; }                                   // UP 
                if (row > 0 && column < cells[0].length-1 && cells[row-1][column+1] == true) { neighborCount++; }   // UP RIGHT
                if (column > 0 && cells[row][column-1] == true) { neighborCount++; }                                // LEFT
                if (column < cells[0].length-1 && cells[row][column+1] == true) { neighborCount++; }                // RIGHT
                if (row < cells.length-1 && column > 0 && cells[row+1][column-1] == true) { neighborCount++; }      // DOWN LEFT
                if (row < cells.length-1 && cells[row+1][column] == true) { neighborCount++; }                      // DOWN
                if (row < cells.length-1 && column < cells[0].length-1 && cells[row+1][column+1] == true) { neighborCount++; }// DOWN RIGHT
                
                if (cells[row][column] == true) {                               // I'm alive
                    if (neighborCount == 2 || neighborCount == 3) {             
                        nextCells[row][column] = true;                          // alive next time
                    }
                    else {
                        nextCells[row][column] = false;                         // dead next time
                    }
                }
                else {                                                          // I'm dead
                    if (neighborCount ==3 ) {
                        nextCells[row][column] = true;                          // alive next time
                    }
                    else {
                        nextCells[row][column] = false;                         // dead next time
                    }
                }
            }
        }
        cells = nextCells;
        controller.setCells(nextCells);
        frame.repaint();
    }

    
}
