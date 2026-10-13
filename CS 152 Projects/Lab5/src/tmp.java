import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.Timer;

public class tmp implements ActionListener
{
  private Random rand = new Random();
  private Timer myTimer;
  private Picture myPic;
  private Graphics canvas;
  
  private static final int DRAW_WIDTH = 600;
  private static final int DRAW_HEIGHT = 600;
  
  private int ballX, ballY;
  private int ballSpeedX, ballSpeedY;
  private int ballSize;
  private int ballRed, ballGreen, ballBlue;
  
  /*
   * This variable serves as an indicator to determine if the ball
   * needs to shrink or grow.
   * 
   * While the ball is greater than 10 pixels, the ball will grow until it is
   * 200 pixels, then the ball will shrink until is is 10 pixels, and repeat.
   */
  private boolean ballIsGrowing;
  
  // Create an array to store multiple balls
  tmp[] balls;

  
  //////////////////////////////////////////////////////////////////////
  // tmp Constructor. This constructor:
  // 1) Creates an empty Picture Frame
  // 2) Sets the initial location, speed and color of the ball.
  // 3) Start the timer.
  /////////////////////////////////////////////////////////////////////
  public tmp() 
  { 
    myPic = new Picture(DRAW_WIDTH, DRAW_HEIGHT);
    canvas = myPic.getOffScreenGraphics();
    
    canvas.setColor(Color.WHITE);
    canvas.fillRect(0, 0, DRAW_WIDTH, DRAW_HEIGHT);

    myPic.setTitle("Random Walk");
    
    
    ballX = DRAW_WIDTH/2;
    ballY = DRAW_HEIGHT/2;
    
    
    ballSpeedX = rand.nextInt(25)-12;
    ballSpeedY = rand.nextInt(25)-12;
    
    
    ballSize = 10; 
    
    
    ballRed = 200;
    ballGreen = 0;
    ballBlue = 0;

    myTimer = new Timer(30, this); // miliseconds, or about 30 frames/s
    myTimer.start();

  }
  
  
  
  
  
  ///////////////////////////////////////////////////////////////////////
  // Called each time the timer fires.
  // 1) Erase the current ball
  // 2) Move the ball
  // 3) Check to see if the ball moved outside the frame. If so,
  //    give the ball a random speed in the direction away from 
  //    the edge it moved off.
  // 4) Make a slight, random change to the ball's color.
  // 5) Sometimes, make the ball a bit larger.
  // 6) Draw the ball in its new location with its new color and 
  //    its new size.
  ////////////////////////////////////////////////////////////////////////
  public void actionPerformed(ActionEvent arg0)
  {
    for (int i = 0; i < balls.length; i++)
    {
      //Erase ball in its current location
      canvas.setColor(Color.WHITE);
      canvas.fillOval(balls[i].ballX, balls[i].ballY, balls[i].ballSize, 
          balls[i].ballSize);
      
      balls[i].ballX += balls[i].ballSpeedX;
      balls[i].ballY += balls[i].ballSpeedY;
     
      
      
      // Make the ball bounce
      if (balls[i].ballX < 0) balls[i].ballSpeedX = rand.nextInt(12)+1;
      if (balls[i].ballY < 0) balls[i].ballSpeedY = rand.nextInt(12)+1;
      
      
      if (balls[i].ballX > DRAW_WIDTH - ballSize) balls[i].ballSpeedX = -(rand.nextInt(1)+1);
      if (balls[i].ballY > DRAW_HEIGHT - ballSize) balls[i].ballSpeedY = -(rand.nextInt(1)+1);
      
      
      
      ballRed = bumpColor(ballRed);
      ballGreen = bumpColor(ballGreen);
      ballBlue = bumpColor(ballBlue);
      
      // Increase or decrease the ball size 
      // Ball 1
      if (balls[i].ballSize <= 10)
      {
        ballIsGrowing = true;
      }
      else if (balls[i].ballSize >= 200)
      {
        ballIsGrowing = false;
      }
      
      if (balls[i].ballIsGrowing)
      {
        if (rand.nextDouble() < 0.1) ballSize++;      
      }
      else 
      {
        if (rand.nextDouble() < 0.1) ballSize--;
      }
      
     
      
      //Draw ball in new location
      canvas.setColor(new Color(ballRed, ballGreen, ballBlue));
      canvas.fillOval(balls[i].ballX, balls[i].ballY, 
                      balls[i].ballSize, balls[i].ballSize);
      myPic.repaint();
    }
  }
  
  

  ////////////////////////////////////////////////////////////////////
  //Adds a random number between -5 and +5 to the input value.
  //
  //If the new value is smaller than 0 or larger than 200, then
  //  the value is set equal to 0 or 200.
  //
  //Returns the modified value.
  ////////////////////////////////////////////////////////////////////
  private int bumpColor(int value)
  {
    value += rand.nextInt(11)-5;
    if (value < 0) value = 0;
    if (value > 200) value = 200;
    return value;
  }
  
  
  /////////////////////////////////////////////////////////////////////
  // Create and run tmp
  /////////////////////////////////////////////////////////////////////
  public static void main(String[] args)
  {
    new tmp();
  }
}