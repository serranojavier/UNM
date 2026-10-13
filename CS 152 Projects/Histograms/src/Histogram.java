import java.awt.Color;
import java.awt.Graphics;

/**
 * Author: Javier Serrano
 * Date: 11/21/12
 */
public class Histogram {
    private static Picture orgPic;
    private static Picture normPic;

    // Three arrays for the normal image, and 3 for the modified image
    private static int[] redOrg = new int[256];
    private static int[] greenOrg = new int[256];
    private static int[] blueOrg = new int[256];
    private static int[] redMod = new int[256];
    private static int[] greenMod = new int[256];
    private static int[] blueMod = new int[256];

    private static final int ORIGINAL_IMAGE = 0;
    private static final int NORMALIZED_IMAGE = 1;

    /**
     * Constructor passes references of the two pictures to the global
     * variables in this class
     *
     * @param originalPicture   Original picture reference
     * @param normalizedPicture Normalized picture reference
     */
    public Histogram(Picture originalPicture, Picture normalizedPicture) {
        orgPic = originalPicture;
        normPic = normalizedPicture;
    }

    private static void createHistogram(int originalOrModified) {
        if (originalOrModified == ORIGINAL_IMAGE) {
            for (int x = 0; x < orgPic.getImageWidth(); x++) {
                for (int y = 0; y < orgPic.getImageHeight(); y++) {
                    // Get rgb values from the pixel
                    int r = orgPic.getRed(x, y);
                    int g = orgPic.getGreen(x, y);
                    int b = orgPic.getBlue(x, y);

                    // Increment index values at r,g,b
                    redOrg[r]++;
                    greenOrg[g]++;
                    blueOrg[b]++;
                }
            }
        } else { // Or the normalized image
            for (int x = 0; x < orgPic.getImageWidth(); x++) {
                for (int y = 0; y < orgPic.getImageHeight(); y++) {
                    // Get the rgb values
                    int r = normPic.getRed(x, y);
                    int g = normPic.getGreen(x, y);
                    int b = normPic.getBlue(x, y);

                    // Increment index values at r,g, and b
                    redMod[r]++;
                    greenMod[g]++;
                    blueMod[b]++;
                }
            }
        }
    }

    private static void normalizeImage(Picture orgPic, int max, int min,
                                       Picture normalizedPic) {
        int targetMax = 255;
        int range = max - min;

        // Get the off-screen buffer and paint the window white
        Graphics canvas = normalizedPic.getOffScreenGraphics();
        canvas.setColor(Color.WHITE);
        canvas.fillRect(0, 0, normalizedPic.getImageWidth(), normalizedPic.getImageHeight());
        normalizedPic.repaint();

        /*
        Loop through every pixel, get its r,g, and b values and normalize them and paint them onto the
        new canvas.
         */
        for (int x = 0; x < orgPic.getImageWidth(); x++) {
            for (int y = 0; y < orgPic.getImageHeight(); y++) {
                // Get the values of the current pixel
                int red = orgPic.getRed(x, y);
                int green = orgPic.getGreen(x, y);
                int blue = orgPic.getBlue(x, y);

                // Normalize the values
                red = ((red - min) * targetMax) / range;
                green = ((green - min) * targetMax) / range;
                blue = ((blue - min) * targetMax) / range;

                // Paint the normalized pixel onto the new image
                normalizedPic.setPixel(x, y, new Color(red, green, blue));
            }
        }
        // Repaint the window
        normalizedPic.repaint();
    }

    private static void normalizeArray(int[] array) {
        int targetMax = 100;

        int min = getMinFromHist(array);
        int max = getMaxFromHist(array);

        int range = max - min;

        for (int i = 0; i < 256; i++) {

            // Zeros don't need to be normalized
            if (array[i] != 0) {
                array[i] = ((array[i] - min) * targetMax) / range;
            }
        }
    }

    private static int getMin(Picture picture) {
        int min = 255;
        for (int x = 0; x < picture.getImageWidth(); x++) {
            for (int y = 0; y < picture.getImageHeight(); y++) {
                // Retrieve the current values
                int red = picture.getRed(x, y);
                int green = picture.getGreen(x, y);
                int blue = picture.getBlue(x, y);

                // Find the minimum
                if (red < min)
                    min = red;
                if (green < min)
                    min = green;
                if (blue < min)
                    min = blue;
            }
        }
        return min;
    }

    private static int getMax(Picture picture) {
        int max = 0;
        for (int x = 0; x < picture.getImageWidth(); x++) {
            for (int y = 0; y < picture.getImageHeight(); y++) {
                // Retrieve color values
                int red = picture.getRed(x, y);
                int green = picture.getGreen(x, y);
                int blue = picture.getBlue(x, y);

                // Find the highest
                if (red > max)
                    max = red;
                if (green > max)
                    max = green;
                if (blue > max)
                    max = blue;
            }
        }
        return max;
    }

    private static int getMinFromHist(int[] array) {
        int min = array[0];
        for (int anArray : array) {
            if (anArray < min) {
                min = anArray;
            }
        }
        return min;
    }

    private static int getMaxFromHist(int[] array) {
        int max = array[0];
        for (int anArray : array) {
            if (anArray > max) {
                max = anArray;
            }
        }
        return max;
    }

    private static void drawHistogram(int[] colorArray, Color color, Picture histogramPic) {
        for (int x = 0; x < colorArray.length; x++) {
            for (int y = 100 - colorArray[x]; y < 100; y++) {
                // No need to draw bars if it's zero
                if (colorArray[x] != 0) {
                    histogramPic.setPixel(x, y, color);
                }
            }
        }
        histogramPic.repaint();
    }

    public static void main(String[] args) {
        /**********************************************************************
         * Create the two windows for the original and normalized pictures
         *********************************************************************/
        Picture picture = new Picture();
        int pictureWidth = picture.getImageWidth();
        int pictureHeight = picture.getImageHeight();
        Picture normalizedImage = new Picture(pictureWidth, pictureHeight);
        // Pass the references for the two pictures
        new Histogram(picture, normalizedImage);


        /**********************************************************************
         * Normalize the image
         *********************************************************************/
        int max = getMax(orgPic);
        int min = getMin(orgPic);
        // Normalize the image
        normalizeImage(orgPic, max, min, normPic);
        // Position it below the the original image
        normPic.setLocation(0, pictureHeight);


        /**********************************************************************
         * Create and draw the histogram graphs for the original image
         *********************************************************************/
        // Create the six windows
        Picture redOrgHistPic = new Picture(256, 100);
        Picture greenOrgHistPic = new Picture(256, 100);
        Picture blueOrgHistPic = new Picture(256, 100);

        Picture redModHistPic = new Picture(256, 100);
        Picture greenModHistPic = new Picture(256, 100);
        Picture blueModHistPic = new Picture(256, 100);

        // Title them
        redOrgHistPic.setTitle("Red Histogram - Original Picture");
        greenOrgHistPic.setTitle("Green Histogram - Original Picture");
        blueOrgHistPic.setTitle("Blue Histogram - Original Picture");

        redModHistPic.setTitle("Red Histogram - Normalized Image");
        greenModHistPic.setTitle("Green Histogram - Normalized Image");
        blueModHistPic.setTitle("Blue Histogram - Normalized Image");

        // Get their offscreen canvas
        Graphics redOrgCanvas = redOrgHistPic.getOffScreenGraphics();
        Graphics greenOrgCanvas = greenOrgHistPic.getOffScreenGraphics();
        Graphics blueOrgCanvas = blueOrgHistPic.getOffScreenGraphics();

        Graphics redModCanvas = redModHistPic.getOffScreenGraphics();
        Graphics greenModCanvas = greenModHistPic.getOffScreenGraphics();
        Graphics blueModCanvas = blueModHistPic.getOffScreenGraphics();

        // Position them
        int offSet = redOrgHistPic.getWidth();
        // Add 1 offset to the green window, and 2 to the blue window
        redOrgHistPic.setLocation(pictureWidth, 0);
        greenOrgHistPic.setLocation(pictureWidth + offSet, 0);
        blueOrgHistPic.setLocation(pictureWidth + (offSet * 2), 0);

        // Add 1 offset to the green window, and 2 to the blue window
        redModHistPic.setLocation(pictureWidth, pictureHeight);
        greenModHistPic.setLocation(pictureWidth + offSet, pictureHeight);
        blueModHistPic.setLocation(pictureWidth + (offSet * 2), pictureHeight);

        // Paint them white
        redOrgCanvas.setColor(Color.WHITE);
        greenOrgCanvas.setColor(Color.WHITE);
        blueOrgCanvas.setColor(Color.WHITE);
        redOrgCanvas.fillRect(0, 0, 256, 100);
        greenOrgCanvas.fillRect(0, 0, 256, 100);
        blueOrgCanvas.fillRect(0, 0, 256, 100);

        redModCanvas.setColor(Color.WHITE);
        greenModCanvas.setColor(Color.WHITE);
        blueModCanvas.setColor(Color.WHITE);
        redModCanvas.fillRect(0, 0, 256, 100);
        greenModCanvas.fillRect(0, 0, 256, 100);
        blueModCanvas.fillRect(0, 0, 256, 100);


        /**********************************************************************
         * Create the histogram
         *********************************************************************/
        createHistogram(ORIGINAL_IMAGE);
        createHistogram(NORMALIZED_IMAGE);

        /**********************************************************************
         * Normalize the six histogram arrays before painting them onto their
         * canvases
         *********************************************************************/
        normalizeArray(redOrg);
        normalizeArray(greenOrg);
        normalizeArray(blueOrg);

        normalizeArray(redMod);
        normalizeArray(greenMod);
        normalizeArray(blueMod);


        /**********************************************************************
         * Draw the six histograms
         *********************************************************************/
        drawHistogram(redOrg, Color.RED, redOrgHistPic);
        drawHistogram(greenOrg, Color.GREEN, greenOrgHistPic);
        drawHistogram(blueOrg, Color.BLUE, blueOrgHistPic);

        drawHistogram(redMod, Color.RED, redModHistPic);
        drawHistogram(greenMod, Color.GREEN, greenModHistPic);
        drawHistogram(blueMod, Color.BLUE, blueModHistPic);
    }
}


