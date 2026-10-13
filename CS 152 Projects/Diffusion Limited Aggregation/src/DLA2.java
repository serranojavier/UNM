//Simulation of Diffusion-Limited Aggregation
//  By Joel Castellanos
//
//This class requires within the same
//  source directory the CS-152 Picture Class version 2012.10.24 or newer
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.Timer;


public class DLA2 implements ActionListener
{
  private Random rand = new Random();
  private Timer myTimer;
  private Picture myPic;
  private Graphics canvas;
  
  int picNum = 60;
  
  //For debugging, it is helpful to use a very small window
  //  For normal use, the width and height would be near the screen size
  private static final int DRAW_WIDTH = 400;
  private static final int DRAW_HEIGHT = 400;
  
  //TODO
  private static final Color EMPTY_COLOR = Color.WHITE;
  private static final Color PARTICLE_COLOR = new Color(0,0,0);
  
  //private static final Color CRYSTAL_COLOR = new Color(200,0,0);
  private static final Color[] CRYSTAL_COLOR =
  {
    new Color(200, 0, 0),     //0 neighbors 
    new Color(192, 192, 144), //1 neighbors
    new Color(168, 168, 168), //2 neighbors
    new Color(120, 144, 120), //3 neighbors 
    new Color(192, 144, 72),  //4 neighbors
    new Color(116, 68, 20),   //5 neighbors
    new Color(96, 48, 0),     //6 neighbors
    new Color(86, 38, 0),     //7 neighbors
    new Color(76, 28, 0),     //8 neighbors               
  };
  
    
    
    private static int[] hexColor = { 0xa8a8a8, 0xc0c090, 0x789078, 0xc09048, 0x603000};

  //For debugging, it is helpful to use a very small number of particles.
  //   For normal use, 10,000 particles is more interesting.
  private static final int PARTICLE_COUNT = 10000;
  private int[] dotX = new int[PARTICLE_COUNT];
  private int[] dotY = new int[PARTICLE_COUNT];
  
  private int frameCount=0;
  private long startTime;
  
  //=======================================================================
  //This Constructor does the following:
  //  1) Creates drawing window
  //  2) Set window's title and background color
  //  3) Sets the initial seed crystal
  //  4) Sets all particles to random locations within the window, but NOT
  //       on the seed crystal.
  //  5) Creates and starts a timer.
  //=======================================================================
  public DLA2() 
  {
    myPic = new Picture(DRAW_WIDTH, DRAW_HEIGHT);
    canvas = myPic.getOffScreenGraphics();
    
    
    //TODO
    //canvas.setColor(Color.WHITE);
    canvas.setColor(EMPTY_COLOR);
    canvas.fillRect(0, 0, DRAW_WIDTH, DRAW_HEIGHT);

    myPic.setTitle("Diffusion-Limited Aggregation (Version 2)");
    
    //Set seed crystal in center of window
    int x = DRAW_WIDTH/2;
    int y = DRAW_HEIGHT/2;    
    
    //TODO
    //myPic.setPixel(x, y, CRYSTAL_COLOR);
    myPic.setPixel(x, y, CRYSTAL_COLOR[0]);
    
    
    //Walk through the list of particles and give each a random location. 
    //If that location is on the seed crystal, then pick a new location.
    for (int i=0; i<PARTICLE_COUNT; i++)
    {
      boolean foundGoodStartingSpot = false;
      while (!foundGoodStartingSpot) 
      {
        dotX[i] = rand.nextInt(DRAW_WIDTH);
        dotY[i] = rand.nextInt(DRAW_HEIGHT);
        if (!isCrystal(dotX[i], dotY[i]))  foundGoodStartingSpot = true;
      }
    }
    
    
    startTime = System.currentTimeMillis();

    myTimer = new Timer(20, this); // miliseconds
    myTimer.start();

  }
  
  
  
  
  
  
  //=======================================================================
  // This method is a callback function.
  // It is called each time the timer fires.
  // This method:
  // 1) Walks through the list of particles and for each it: 
  //    a) Erases the particle from the off-screen buffer.
  //    b) Moves the particle in a random direction.
  //    c) If the particle moved out of bounds, move it back within bounds.
  //    d) If the crystal moved on or next to a crystal, then the 
  //       particle is precipitates onto the crystal and recycled.
  // 2) After all particles have been updated, redraw the screen.
  //=======================================================================
  public void actionPerformed(ActionEvent arg0)
  {
    frameCount++;
    
    for (int i=0; i<PARTICLE_COUNT; i++)
    { 
      //Erases the particle from the off-screen buffer.
      myPic.setPixel(dotX[i], dotY[i], Color.WHITE);
      
      //Moves the particle in a random direction.
      int dir = rand.nextInt(4);
      if (dir == 0) dotX[i]++;
      else if (dir == 1) dotY[i]++;
      else if (dir == 2) dotX[i]--;
      else dotY[i]--;
      
      
      //If the particle moved out of bounds, move it back within bounds.
      //Note that a one pixel boundary is left around the boarder. Any 
      //  particle that moves into this boundary is bounced out. This
      //  allows the program to examine the pixel that is above, below
      //  to the left or to the right of any particle without the pixel
      //  being examined being out of bounds.
      if (dotX[i] <= 0) dotX[i] = 1;
      if (dotX[i] >= DRAW_WIDTH-1) dotX[i] = DRAW_WIDTH-2;
      
      if (dotY[i] <= 0) dotY[i] = 1;
      if (dotY[i] >= DRAW_HEIGHT-1) dotY[i] = DRAW_HEIGHT-2;
      
      
      
      
      if (isOnOrNextToCrystal(dotX[i], dotY[i]))
      {
        //TODO
        //myPic.setPixel(dotX[i], dotY[i], CRYSTAL_COLOR);
        int count =  countNeighbors(dotX[i], dotY[i]);
        myPic.setPixel(dotX[i], dotY[i], CRYSTAL_COLOR[count]);
        
        colorCrystalBasedOnNumberOfNeighbors(dotX[i], dotY[i]-1);
        colorCrystalBasedOnNumberOfNeighbors(dotX[i], dotY[i]+1);
        colorCrystalBasedOnNumberOfNeighbors(dotX[i]-1, dotY[i]);
        colorCrystalBasedOnNumberOfNeighbors(dotX[i]+1, dotY[i]);
        
        colorCrystalBasedOnNumberOfNeighbors(dotX[i]-1, dotY[i]-1);
        colorCrystalBasedOnNumberOfNeighbors(dotX[i]+1, dotY[i]-1);
        colorCrystalBasedOnNumberOfNeighbors(dotX[i]-1, dotY[i]+1);
        colorCrystalBasedOnNumberOfNeighbors(dotX[i]+1, dotY[i]+1);
        

        recyclePartical(i);
      }
      else
      {
        myPic.setPixel(dotX[i], dotY[i], PARTICLE_COLOR);
      }
    }
    
    //Copy the off-screen buffer to the display window
    myPic.repaint();
    
    if (frameCount % 1000 == 0)
    { 
      long curTime = System.currentTimeMillis();
      long deltaTime = curTime - startTime;
      System.out.println("Time for 1000 frames = " + deltaTime + " ms");
      startTime = curTime;
      picNum++;
      myPic.saveImage("/Users/javierserrano/Documents/Pics/here/", picNum);
    }

  }

  
  private void colorCrystalBasedOnNumberOfNeighbors(int x, int y)
  {
    if (isCrystal(x, y))
    { int count =  countNeighbors(x, y);
      myPic.setPixel(x, y, CRYSTAL_COLOR[count]);
    }
  }
  
  //=======================================================================
  //  Returns true if and only if the pixel (x,y) is a crystal pixel or 
  //  if there is a crystal pixel directly above it, below it, to the  
  //  left or to the right.
  //=======================================================================
  private boolean isOnOrNextToCrystal(int x, int y)
  {
    if (isCrystal(x, y)) return true;
    
    if (isCrystal(x-1, y)) return true;
    if (isCrystal(x+1, y)) return true;
    
    if (isCrystal(x, y+1)) return true;
    if (isCrystal(x, y-1)) return true;
    
    return false;
    
  }
  
  
  
  
  //=======================================================================
  // Returns true if and only if color of the pixel (x, y) is the same 
  //   as CRYSTAL_COLOR.
  //=======================================================================
  private boolean isCrystal(int x, int y)
  {
    //TODO
    //if (myPic.getRed(x, y) != CRYSTAL_COLOR.getRed()) return false;
    //if (myPic.getGreen(x, y) != CRYSTAL_COLOR.getGreen()) return false;
    //if (myPic.getBlue(x, y) != CRYSTAL_COLOR.getBlue()) return false;
    //return true;
    
    if ((!isEmptyColor(x,y)) && (!isParticleColor(x,y))) return true;
    return false;
  }
  
  
  
  
  //TODO: New Method
  //=======================================================================
  // Returns true if and only if color of the pixel (x, y) is the same 
  //   as EMPTY_COLOR.
  //=======================================================================
  private boolean isEmptyColor(int x, int y)
  {
    if (myPic.getRed(x, y) != EMPTY_COLOR.getRed()) return false;
    if (myPic.getGreen(x, y) != EMPTY_COLOR.getGreen()) return false;
    if (myPic.getBlue(x, y) != EMPTY_COLOR.getBlue()) return false;
    return true;
  }
  
  
  //TODO: New Method
  //=======================================================================
  // Returns true if and only if color of the pixel (x, y) is the same 
  //   as PARTICLE_COLOR.
  //=======================================================================
  private boolean isParticleColor(int x, int y)
  {
    if (myPic.getRed(x, y) != PARTICLE_COLOR.getRed()) return false;
    if (myPic.getGreen(x, y) != PARTICLE_COLOR.getGreen()) return false;
    if (myPic.getBlue(x, y) != PARTICLE_COLOR.getBlue()) return false;
    return true;
  }
  

  //=======================================================================
  //=======================================================================
  private int countNeighbors(int x, int y)
  {
    int count = 0;
    
    if (isCrystal(x-1, y)) count++;
    if (isCrystal(x+1, y)) count++;
    
    if (isCrystal(x, y-1)) count++;
    if (isCrystal(x, y+1)) count++;
    
    if (isCrystal(x-1, y-1)) count++;
    if (isCrystal(x-1, y+1)) count++;
    if (isCrystal(x+1, y-1)) count++;
    if (isCrystal(x+1, y+1)) count++;
 
    return count;
    
  }
  
  
  
  
  //=======================================================================
  // Sets the location of idx particle to a random location along the 
  // edge of the window (but not on the particle free boarder). 
  //=======================================================================
  private void recyclePartical(int idx)
  {
    //TODO
    int r = rand.nextInt(4);
    if (r==0) //Random location on left edge
    { dotX[idx] = 1;
      dotY[idx] = rand.nextInt(DRAW_HEIGHT-2)+1;
    }
    else if (r==1) //Random location on right edge
    { dotX[idx] = DRAW_WIDTH-2;
      dotY[idx] = rand.nextInt(DRAW_HEIGHT-2)+1;
    }
    else if (r==2) //Random location on top edge
    { dotX[idx] = rand.nextInt(DRAW_WIDTH-2)+1;
      dotY[idx] = 1;
    }
    else //Random location on botton edge
    { dotX[idx] = rand.nextInt(DRAW_WIDTH-2)+1;
      dotY[idx] = DRAW_HEIGHT+1;
    }
  }
  
  
  
  
  
  public static void main(String[] args)
  {
    new DLA2();
  }
}