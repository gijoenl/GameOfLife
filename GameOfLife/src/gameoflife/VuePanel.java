/**
 * Panel for the Game Of Life simulation. This uses JFrame to draw the grid and cells
 * @author Nicolas_Online
 */
package gameoflife;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class VuePanel extends JPanel {
    
    boolean[][] cells;                                                          // VARIABLES AND OBJECTS
    double width;
    double height;
    
    public VuePanel(boolean[][] in) {                                           // now the cells of the Main method and these are pointing on the same thing
        cells = in;
    }
    
   @Override
   public void paintComponent(Graphics g){                                      // to draw something on the graphic object
       super.paintComponent(g);                                                 // using the parent version of paintComponent, included in JPanel
       width = (double)this.getWidth()/cells[0].length;                         // Dividing the width of the screen by the number of cells to get a grid
       height = (double)this.getHeight()/cells.length;                          // Dividing the heigh of the screen by the nember of cells to get a grid
                                                                                // DRAWING THE CELLS
       g.setColor(new Color(255, 200, 0));
       for (int row = 0; row < cells.length; row++) {
           for (int column = 0; column < cells[0].length; column++) {
               if(cells[row][column]==true) {
                   g.fillRect((int)(column*width+1), (int)(row*height+1), (int)width, (int)height);// fill any ALIVE rectangle
               }
           }
       }
                                                                                // DRAWING THE GRID
       g.setColor(new Color(0, 0, 0));                                          // Setting the RGB values of the lines
       
       for(int x = 0; x < cells[0].length+1; x++){                              // drawing all the x-axed lines
           g.drawLine((int)Math.round(x*width), 0, (int)Math.round(x*width), this.getHeight()); // rounding and casting to get cleaner version of the grid
       }
       
       for(int y = 0; y < cells.length+1; y++){                                 // drawing all the y-axed lines
           g.drawLine(0,(int)Math.round(y*height), this.getWidth(), (int)Math.round(y*height)); // rounding and casting to get cleaner version of the grid
       }
   }
   
   public void setCells(boolean[][] nextCells) {
       cells = nextCells;
   }
}
