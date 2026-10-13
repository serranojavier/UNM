/**
 * @author Javier Serrano
 * @version 10.1012
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.Timer;

public class BouncingBall implements ActionListener {
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


    private int numBalls = 2;
    /*
    * This variable serves as an indicator to determine if the ball
    * needs to shrink or grow.
    *
    * While the ball is greater than 10 pixels, the ball will grow until it is
    * 200 pixels, then the ball will shrink until is is 10 pixels, and repeat.
    */
    private boolean ballIsGrowing;

    // Use this array to instantiate multiple ball instances.
    BouncingBall[] balls;


    //////////////////////////////////////////////////////////////////////
    // BouncingBall Constructor. This constructor:
    // 1) Creates an empty Picture Frame
    // 2) Sets the initial location, speed and color of the ball.
    // 3) Start the timer.
    /////////////////////////////////////////////////////////////////////
    public BouncingBall() {

        // Create 'numBall' instances
        for (int i = 0; i < numBalls; i++) {
            balls[i] = new BouncingBall();
        }

        myPic = new Picture(DRAW_WIDTH, DRAW_HEIGHT);
        canvas = myPic.getOffScreenGraphics();

        canvas.setColor(Color.WHITE);
        canvas.fillRect(0, 0, DRAW_WIDTH, DRAW_HEIGHT);

        myPic.setTitle("Random Walk");


        ballX = DRAW_WIDTH / 2;
        ballY = DRAW_HEIGHT / 2;


        ballSpeedX = rand.nextInt(25) - 12;
        ballSpeedY = rand.nextInt(25) - 12;


        ballSize = 10;

        ballRed = 200;
        ballGreen = 0;
        ballBlue = 0;

        myTimer = new Timer(30, this); // milliseconds, or about 30 frames/s
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
    public void actionPerformed(ActionEvent arg0) {
        // Perform ball animations
        animateBalls(balls);

        //Draw ball in new location
        canvas.setColor(new Color(ballRed, ballGreen, ballBlue));
        canvas.fillOval(ballX, ballY, ballSize, ballSize);
        myPic.repaint();
    }

    /**
     * Performs the animations for the balls including: bouncing off the window,
     * changing its position and changing its size as well as color.
     * @param bouncingBall  Array of ball objects.
     */
    private void animateBalls(BouncingBall[] bouncingBall) {
        // Erase the balls in their current location
        canvas.setColor(Color.WHITE);
        for (int i = 0; i < bouncingBall.length; i++) {
            // Copy the current instance to 'ball'
            BouncingBall ball = bouncingBall[i];

            canvas.fillOval(ball.ballX, ball.ballY,
                    ball.ballSize, ball.ballSize);

            ball.ballX += ballSpeedX;
            ball.ballY += ballSpeedY;

            // Make the balls bounce
            // Top and left walls
            if (ball.ballX < 0) ball.ballSpeedX = rand.nextInt(12) + 1;
            if (ball.ballY < 0) ball.ballSpeedY = rand.nextInt(12) + 1;

            // Right and bottom walls
            if (ball.ballX > DRAW_WIDTH - ball.ballSize) ball.ballSpeedX = -(rand.nextInt(1) + 1);
            if (ball.ballY > DRAW_HEIGHT - ball.ballSize) ball.ballSpeedY = -(rand.nextInt(1) + 1);

            ball.ballRed = bumpColor(ballRed);
            ball.ballGreen = bumpColor(ballGreen);
            ball.ballBlue = bumpColor(ballBlue);

            //Adjust ball size
            // If it's less than 10pixels set the flag 'ballIsGrowing' to true
            // else if its greater than 200 set the flag to false
            if (ball.ballSize <= 10) {
                ball.ballIsGrowing = true;
            }   else if (ball.ballSize >= 200) {
                ball.ballIsGrowing = false;
            }

            if (ball.ballIsGrowing) {
                if (rand.nextDouble() < 0.1) ball.ballSize++;
            }   else {
                if (rand.nextDouble() < 0.1) ball.ballSize--;
            }
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
    private int bumpColor(int value) {
        value += rand.nextInt(11) - 5;
        if (value < 0) value = 0;
        if (value > 200) value = 200;
        return value;
    }


    /////////////////////////////////////////////////////////////////////
    // Create and run BouncingBall
    /////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        new BouncingBall();
    }
}