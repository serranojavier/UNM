//Simulation of Diffusion-Limited Aggregation
//  By Joel Castellanos
//  Modified by: Javier Serrano on 11/9/12
//
//This class requires within the same
//  source directory the CS-152 Picture Class version 2012.10.24 or newer

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.Timer;

public class DLA_Sliders implements ActionListener {
    private Random rand = new Random();
    private Timer myTimer;
    private Picture myPic;
    private Graphics canvas;

    //For debugging, it is helpful to use a very small window
    //  For normal use, the width and height would be near the screen size
    private static final int DRAW_WIDTH = 600;
    private static final int DRAW_HEIGHT = 600;

    private static final Color PARTICLE_COLOR = new Color(0, 0, 0);
    private static final Color CRYSTAL_COLOR = new Color(200, 0, 0);


    //For debugging, it is helpful to use a very small number of particles.
    //   For normal use, 10,000 particles is more interesting.
    private static int PARTICLE_COUNT = 10000;
    private int[] dotX = new int[PARTICLE_COUNT];
    private int[] dotY = new int[PARTICLE_COUNT];

    private int frameCount = 0;
    private long startTime;

    private double edgeProbability;
    private double cornerProbability;

    // Used in the overloaded method countNeighbors,
    // flag that makes the method return the count
    // of crystals on the edges
    private static final int EDGE = 1;
    // this makes it return the count of crystals on the corners
    private static final int CORNER = 2;

    private DLA_CtrlFrame ctrlFrame;

    private int picNum = 60;

    /**
     * This Constructor does the following:
     * <ul>
     * <li>1) Creates a drawing window</li>
     * <li>2) Set window's title and background</li>
     * <li>3) Assigns the </li>
     * </ul>
     */
    public DLA_Sliders(DLA_CtrlFrame control) {
        ctrlFrame = control;
        edgeProbability = control.getEdgeProbability();
        cornerProbability = control.getCornerProbability();

        myPic = new Picture(DRAW_WIDTH, DRAW_HEIGHT);

    /*Since FRAME_WIDTH of the DLA_CtrlFrame class is public
     *  it can be referenced outside of its class.
     *  Since it is static, it can be referenced by using
     *  the class name: DLA_CtrlFrame.FRAME_WIDTH (you do not need the
          name of an instance of the DLA_CtrlFrame class
     */
        myPic.setLocation(DLA_CtrlFrame.FRAME_WIDTH, 0);
        canvas = myPic.getOffScreenGraphics();

        myPic.setTitle("Diffusion-Limited Aggregation (Sliders)");

        myTimer = new Timer(0, this); // milliseconds
    }


    /**
     * This method is called by the DLA_CrtlFrame when the user clicks the
     * reset button. It:
     * <ul>
     * <li>1) Sets the initial seed crystals equal to the input seedCount</li>
     * <li>2) Resets the value of PARICLE_COUNT to the value retrieved
     * from the slider</li>
     * <li>3) Sets all particles to random locations within the window,
     * but NOT on any of the seed crystals.</li>
     * <li>4) Creates a timer.</li>
     * </ul>
     */
    public void init(int seedCount, int particleCount) {
        canvas.setColor(Color.WHITE);
        canvas.fillRect(0, 0, DRAW_WIDTH, DRAW_HEIGHT);

    /*
     * Set seed crystals.
     * Note: this method may sometimes create a crystal on the
     * same location where one already exists. Each time this occurs,
     *  the total initial seed count is reduced by one.
     */
        if (seedCount == 1) {
            //Set seed crystal in center of window
            int x = DRAW_WIDTH / 2;
            int y = DRAW_HEIGHT / 2;
            myPic.setPixel(x, y, CRYSTAL_COLOR);
        } else {
            for (int i = 0; i < seedCount; i++) {
                int x = rand.nextInt(DRAW_WIDTH);
                int y = rand.nextInt(DRAW_HEIGHT);

                myPic.setPixel(x, y, CRYSTAL_COLOR);
            }
        }

    /*
     * The amount of particles on the screen have been changed.
     * To change this create two array with the new size, then simply
     * assign the new arrays to the two wold array(dotX and dotY).
     * Then just simply assign the old value of PARTICLE_COUNT
     * to particleCount.
     */
        // The two new arrays
        int[] arrayX = new int[particleCount];
        int[] arrayY = new int[particleCount];

        // Assign them to the old ones.
        dotX = arrayX;
        dotY = arrayY;

        // reset the value of PARTICLE_COUNT.
        PARTICLE_COUNT = particleCount;

        // Walk through the list of particles and give each a random location.
        // If that location is on the seed crystal, then pick a new location.
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            boolean foundGoodStartingSpot = false;
            while (! foundGoodStartingSpot) {
                dotX[i] = rand.nextInt(DRAW_WIDTH);
                dotY[i] = rand.nextInt(DRAW_HEIGHT);
                if (! isCrystal(dotX[i], dotY[i])) foundGoodStartingSpot = true;
            }
        }

        myPic.repaint();

        ctrlFrame.pauseTheProgram();
        startTime = System.currentTimeMillis();
    }

    /**
     * This method is called by the DLA_CrtlFrame when the user toggles the
     * pause button. It starts or stops the timer.
     *
     * @param state boolean value, <code>true</code> pauses the program
     *              <code>false</code> unpauses it.
     */
    public void setPause(boolean state) {
        if (state) myTimer.stop();
        else myTimer.start();
    }

    /**
     * This method is a callback function.
     * It is called each the timer fires.
     * This method:
     * <ul>
     * <li> 1) Walks through the list of particles and for each it:
     * <ul>
     * <li> a) Erases the particle from the off-screen buffer.</li>
     * <li> b) Moves the particles in a random direction.</li>
     * <li> c) If the particle moved out of bounds move it back
     * within bounds.</li>
     * <li> d) If the crystal moved on or next to a crystal, then the
     * particle precipitates onto the crystal and it is recycled.</li>
     * </ul>
     * </li>
     * <li>2) After all particles have been updated, redraw the screen.</li>
     * </ul>
     */
    public void actionPerformed(ActionEvent arg0) {
        frameCount++;

        for (int i = 0; i < PARTICLE_COUNT; i++) {
            //Erases the particle from the off-screen buffer.
            myPic.setPixel(dotX[i], dotY[i], Color.WHITE);

            //Moves the particle in a random direction.
            int dir = rand.nextInt(4);
            if (dir == 0) dotX[i]++;
            else if (dir == 1) dotY[i]++;
            else if (dir == 2) dotX[i]--;
            else dotY[i]--;


      /*If the particle moved out of bounds, move it back within bounds.
       * Note that a one pixel boundary is left around the boarder. Any
       * particle that moves into this boundary is bounced out. This
       * allows the program to examine the pixel that is above, below
       * to the left or to the right of any particle without the pixel
       * being examined being out of bounds.
       */
            if (dotX[i] <= 0) dotX[i] = 1;
            if (dotX[i] >= DRAW_WIDTH - 1) dotX[i] = DRAW_WIDTH - 2;

            if (dotY[i] <= 0) dotY[i] = 1;
            if (dotY[i] >= DRAW_HEIGHT - 1) dotY[i] = DRAW_HEIGHT - 2;


            if (isOnOrNextToCrystal(dotX[i], dotY[i])) {

        /*
         * Get the count of crystals on the edges and the count of
         * crystals on the corners, pass them to the method
         * calculateProbability()
         */
                // Update probabilities from the sliders
                updateProbabilities();

                int edgeCount = countNeighbors(dotX[i], dotY[i], EDGE);
                int cornerCount = countNeighbors(dotX[i], dotY[i], CORNER);

                double crystallizationProb = calculateProbability(edgeCount, cornerCount);

                if (rand.nextDouble() < crystallizationProb) {
                    // Zero probability don't crystallize
                    if (crystallizationProb == 0.0) {
                        continue;
                    }

                    // Get the number of neighbors around crystal.
                    int count = countNeighbors(dotX[i], dotY[i]);
                    Color color = genCrystalColor(count);

                    myPic.setPixel(dotX[i], dotY[i], color);
          /*
           * A new crystal has been added, crystals from the previous
           * iteration have a new neighbor and need to be repainted
           * call rePainCrystals with the 8 adjacent coordinates to this
           * new crystal.
           */
                    // Left and right
                    rePaintCrystal(dotX[i] - 1, dotY[i]);
                    rePaintCrystal(dotX[i] + 1, dotY[i]);

                    // Top and bottom
                    rePaintCrystal(dotX[i], dotY[i] - 1);
                    rePaintCrystal(dotX[i], dotY[i] + 1);

                    // Top left and top right
                    rePaintCrystal(dotX[i] - 1, dotY[i] - 1);
                    rePaintCrystal(dotX[i] + 1, dotY[i] - 1);

                    // Bottom left and bottom right
                    rePaintCrystal(dotX[i] - 1, dotY[i] + 1);
                    rePaintCrystal(dotX[i] + 1, dotY[i] + 1);

                    recycleParticle(i);
                }
            } else {
                myPic.setPixel(dotX[i], dotY[i], PARTICLE_COLOR);
            }
        }

        //Copy the off-screen buffer to the display window
        myPic.repaint();

        if (frameCount % 1000 == 0) {
            long curTime = System.currentTimeMillis();
            long deltaTime = curTime - startTime;
            System.out.println("Time for 1000 frames = " + deltaTime + " ms");
            startTime = curTime;
        }
    }


    /**
     * Returns true if and only if the pixel (x,y) is a crystal pixel or
     * if there is a crystal pixel directly above it, below it, to the
     * left or to the right.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return <code>true</code> if the coordinate in on or next to a crystal,
     * <code>false</code> otherwise.
     */
    private boolean isOnOrNextToCrystal(int x, int y) {
        // Current position
        if (isCrystal(x, y)) return true;

        // Left and right
        if (isCrystal(x - 1, y)) return true;
        if (isCrystal(x + 1, y)) return true;

        // Top and bottom
        if (isCrystal(x, y + 1)) return true;
        if (isCrystal(x, y - 1)) return true;

        // Top Left and top right
        if (isCrystal(x - 1, y - 1)) return true;
        if (isCrystal(x + 1, y - 1)) return true;

        // Bottom Left and bottom right
        if (isCrystal(x - 1, y + 1)) return true;
        if (isCrystal(x + 1, y + 1)) return true;

        return false;
    }

    /**
     * Checks if the passed screen coordinate contains a crystal.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return <code>true</code> if it is a crystal. <code>false</code>
     * if not.
     */
    private boolean isCrystal(int x, int y) {
        // Compare the single RGB values of the current pixel
        // to the single RGB values of crystal color.
        int r = myPic.getRed(x, y);
        int g = myPic.getGreen(x, y);
        int b = myPic.getBlue(x, y);

        // If the particle is black or white exit since it is not a crystal
        if ((r == 255 && g == 255 && b == 255) ||
                (r == 0 && g == 0 && b == 0)) {
            return false;
        }
        return true;
    }

    /**
     * Move this particle to one of the four edges of the window. The side
     * is determined randomly.
     *
     * @param index
     */
    private void recycleParticle(int index) {
        dotX[index] = 1;
        dotY[index] = rand.nextInt(DRAW_HEIGHT - 2) + 1;

    /*
     * Move the particle to the new location
     * Generate a value from 0 to 3 that corresponds to the side of
     * window in a clockwise direction.
     * 0 = left,
     * 1 = top,
     * 2 = right,
     * 3 = bottom
     */
        int n = rand.nextInt(4);

        if (n == 0) {
            dotX[index] = 1;
            dotY[index] = rand.nextInt(DRAW_HEIGHT - 2) + 1;
        } else if (n == 1) {
            dotX[index] = rand.nextInt(DRAW_WIDTH - 2) + 1;
            dotY[index] = 1;
        } else if (n == 2) {
            dotX[index] = DRAW_WIDTH - 1;
            dotY[index] = rand.nextInt(DRAW_HEIGHT - 2) + 1;
        } else {
            dotX[index] = rand.nextInt(DRAW_WIDTH - 2) + 1;
            dotY[index] = DRAW_HEIGHT - 1;
        }
    }

    /**
     * Creates the color of the crystal depending on how many
     * neighbors are around said particle.
     *
     * @param neighborCount - number of neighbors around crystal.
     */
    private Color genCrystalColor(int neighborCount) {
        switch (neighborCount) {
            case 1:
                return Color.GRAY;
            case 2:
                return Color.GREEN;
            case 3:
                return Color.BLUE;
            case 4:
                return Color.CYAN;
            case 5:
                return Color.ORANGE;
            case 6:
                return Color.MAGENTA;
            case 7:
                return Color.RED;
            case 8:
                return Color.YELLOW;
            default:
                // Should never reach this point
                return Color.PINK;
        }
    }

    /**
     * Counts how many crystal neighbors are around the particle
     * specified in the argument.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return The count of crystals around it as an int.
     */
    private int countNeighbors(int x, int y) {
        int count = 0;

        // Check all 8 adjacent places to see if a crystal is present
        // left and right
        if (isCrystal(x - 1, y)) count++;
        if (isCrystal(x + 1, y)) count++;

        // Top and bottom
        if (isCrystal(x, y - 1)) count++;
        if (isCrystal(x, y + 1)) count++;

        // Top left and top right
        if (isCrystal(x - 1, y - 1)) count++;
        if (isCrystal(x + 1, y - 1)) count++;

        // Bottom left and bottom right
        if (isCrystal(x - 1, y + 1)) count++;
        if (isCrystal(x + 1, y + 1)) count++;

        return count;
    }

    /**
     * Overloaded method; returns the count of neighbor crystals;
     * either the count of the edges or the corners. Specify EDGE for edge
     * count or CORNER for corner count.
     *
     * @param x                x coordinate
     * @param y                y coordinate
     * @param flagCornerOrEdge EDGE or CORNER
     * @return count of either crystals on the edges or on the corners
     */
    private int countNeighbors(int x, int y, int flagCornerOrEdge) {
        int count = 0;
        if (flagCornerOrEdge == EDGE) {
            // left and right
            if (isCrystal(x - 1, y)) count++;
            if (isCrystal(x + 1, y)) count++;

            // Top and bottom
            if (isCrystal(x, y - 1)) count++;
            if (isCrystal(x, y + 1)) count++;

            return count;
        } else // Return the corner count
        {
            // Top left and top right
            if (isCrystal(x - 1, y - 1)) count++;
            if (isCrystal(x + 1, y - 1)) count++;

            // Bottom left and bottom right
            if (isCrystal(x - 1, y + 1)) count++;
            if (isCrystal(x + 1, y + 1)) count++;

            return count;
        }
    }

    /**
     * Calculates the probability of crystallization.
     *
     * @param edgeNeighborCount   Number of crystals in the edge
     * @param cornerNeighborCount Number of crystals in the corner.
     * @return Returns the probability for a crystal to crystallize
     */
    private double calculateProbability(int edgeNeighborCount, int cornerNeighborCount) {
        // Only the corners
        if (edgeNeighborCount == 0 && cornerNeighborCount != 0) {
            double prob = 1.0 - calcCornerProb(cornerNeighborCount);
            return prob;
        } else if (cornerNeighborCount == 0 && edgeNeighborCount != 0) // Only the edges
        {
            double prob = 1.0 - calcEdgeProb(edgeNeighborCount);
            return prob;
        } else // Both edge and corners
        {
            double edgePercent = calcEdgeProb(edgeNeighborCount);
            double cornerPercent = calcCornerProb(cornerNeighborCount);
            return 1.0 - (edgePercent * cornerPercent);
        }
    }

    /**
     * Calculates the probability of crystallization ONLY for crystals
     * in the edges.
     *
     * @param edgeNeighborCount Number of crystals in the edges.
     * @return Returns the percentage as a two decimal float.
     */
    private double calcEdgeProb(int edgeNeighborCount) {
        switch (edgeNeighborCount) {
            case 1:
                return (1 - edgeProbability);
            case 2:
                return (1 - edgeProbability) *
                        (1 - edgeProbability);
            case 3:
                return (1 - edgeProbability) *
                        (1 - edgeProbability) *
                        (1 - edgeProbability);
            case 4:
                return (1 - edgeProbability) *
                        (1 - edgeProbability) *
                        (1 - edgeProbability) *
                        (1 - edgeProbability);
            default:
                return (1 - edgeProbability);
        }
    }

    /**
     * Calculates the probability of crystallization ONLY for crystals in the
     * corners.
     *
     * @param cornerNeighborCount Number of crystals in the corners.
     * @return Returns the percentage as a two decimal float.
     */
    private double calcCornerProb(int cornerNeighborCount) {
        switch (cornerNeighborCount) {
            // case number equals the number of neighbors
            case 1:
                return (1 - cornerProbability);
            case 2:
                return (1 - cornerProbability) *
                        (1 - cornerProbability);
            case 3:
                return (1 - cornerProbability) *
                        (1 - cornerProbability) *
                        (1 - cornerProbability);
            case 4:
                return (1 - cornerProbability) *
                        (1 - cornerProbability) *
                        (1 - cornerProbability) *
                        (1 - cornerProbability);
            default:
                return (1 - cornerProbability);
        }
    }

    /**
     * Updates the probability values of both edge and corner.
     */
    private void updateProbabilities() {
        edgeProbability = ctrlFrame.getEdgeProbability();
        cornerProbability = ctrlFrame.getCornerProbability();
    }

    /**
     * Counts the number of neighbor crystals around it and paints the particle
     * accordingly.
     */
    private void rePaintCrystal(int x, int y) {
        if (isCrystal(x, y)) {
            int count = countNeighbors(x, y);
            Color color = genCrystalColor(count);
            myPic.setPixel(x, y, color);
        }
    }
}