/**
 * @author: Javier Serrano
 * Date: 11/14/12
 */

public class oldHistogram {
    // Instantiate a new Picture window
    private static Picture myPic;


    /*
    * These two flags are used with the method drawHistograms()
    * they indicate which histogram to graph.
    */
    private static final int ORIGINAL = 0;
    private static final int NORMALIZED = 1;

    // Indexes for the dimensions of the 2d array
    private static final int RED_ORIGINAL = 0;
    private static final int GREEN_ORIGINAL = 1;
    private static final int BLUE_ORIGINAL = 2;
    private static final int RED_NORMALIZED = 3;
    private static final int GREEN_NORMALIZED = 4;
    private static final int BLUE_NORMALIZED = 5;

    // Window names for the unnormalized graphs
    private static final String[] ORIGINAL_GRAPH_NAMES =
            {
                    "Original - Red",
                    "Original - Green",
                    "Original - Blue"
            };

    // Window names for the normalized graphs
    private static final String[] NORMALIZED_GRAPH_NAMES =
            {
                    "Normalized - Red",
                    "Normalized - Green",
                    "Normalized - Blue"
            };


    /**
     * Loops through every pixel in the picture and creates the RGB
     * count in the arrays.
     *
     * @param array The array where the histogram will be created.
     */
    private static void retrieveRGBValues(int[][] array) {
        /*
         * Loop through every pixel in the image, get the RGB values
         * and increment the rgb counts. If i find a red value of 155
         * increment the value by 1 at index 155.
         */

        for (int x = 0; x < myPic.getImageWidth(); x++) {
            for (int y = 0; y < myPic.getImageHeight(); y++) {
                // Retrieve RGB values
                int r = myPic.getRed(x, y);
                int g = myPic.getGreen(x, y);
                int b = myPic.getBlue(x, y);

                // Increment index values at r,g,b
                array[RED_ORIGINAL][r]++;
                array[GREEN_ORIGINAL][g]++;
                array[BLUE_ORIGINAL][b]++;
            }
        }
    }

    /**
     * Find the highest value in a 2d array and returns it.
     *
     * @param array    The array to be searched.
     * @param subArray The sub-array to be used
     * @return Maximum value
     */
    private static int getMax(int[][] array, int subArray) {
        int max = array[subArray][0];
        for (int i = 1; i < array[subArray].length; i++) {
            if (array[subArray][i] > max) {
                max = array[subArray][i];
            }
        }
        return max;
    }

    /**
     * Find the lowest value in the array.
     *
     * @param array    The 2d array to be searched
     * @param subArray The sub-array to be searched
     * @return Lowest int value.
     */
    private static int getMin(int[][] array, int subArray) {
        int min = array[subArray][0];
        for (int i = 1; i < array[subArray].length; i++) {
            if (array[subArray][i] < min) {
                min = array[subArray][i];
            }
        }
        return min;
    }

    /**
     * Normalizes the sub-array of a 2d array.
     *
     * @param array     The array to be normalized
     * @param targetMax This will be the maximum value once normalized
     * @param subArray  The sub-array to normalized
     */
    private static void normalize(int[][] array, int targetMax, int subArray) {
        int min = getMin(array, subArray);
        int max = getMax(array, subArray);

        int range = max - min;
        for (int i = 0; i < array[subArray].length; i++) {
            if (range != 0) {
                array[subArray][i] = ((array[subArray][i] - min) * targetMax) / range;
            }
        }
    }


    private static void printArray(int[][] array, int subArray) {
        System.out.print("[");
        for (int i = 0; i < array[subArray].length; i++) {
            if (i < array[subArray].length - 1) {

                // Don't print zeros
                if (array[subArray][i] != 0)
                    System.out.format("Index: %3d = %2d,,, ", i, array[subArray][i]);
            } else {
                System.out.format("%3d]\n", array[subArray][i]);
            }
        }
    }

    private static void printRGB(int[][] array) {
        System.out.print("RED_ORG --------> ");
        printArray(array, RED_ORIGINAL);

        System.out.print("Green org --------> ");
        printArray(array, GREEN_ORIGINAL);

        System.out.print("BLue Org --------> ");
        printArray(array, BLUE_ORIGINAL);
        System.out.println("");
    }

    private static void spreadHistogram(int[][] array) {
        /*
         * To spread the histogram:
         * 1) Find the lowest nonzero value
         * 2)
         */
        // Find lowest value in r, g, and b
        int redIndex = getLowestIndex(array, RED_ORIGINAL);
        int greenIndex = getLowestIndex(array, GREEN_ORIGINAL);
        int blueIndex = getLowestIndex(array, BLUE_ORIGINAL);

        // Find which index is the lowest
        int lowestIndex = Math.min(redIndex, Math.min(greenIndex, blueIndex));

        /**
         * Subtract lowestIndex from r,g, and b Indexes in order to shift
         * the histogram to the left
         */
        shiftHistToLeft(array, RED_ORIGINAL, redIndex, lowestIndex);
        shiftHistToLeft(array, GREEN_ORIGINAL, greenIndex, lowestIndex);
        shiftHistToLeft(array, BLUE_ORIGINAL, blueIndex, lowestIndex);
    }


    /**
     * Find the index where the first(going from 0 to 255) lowest nonzero value
     * is located in the histogram.
     *
     * @param array    The 2d array
     * @param subArray The sub-array to be searched
     * @return Index where the lowest nonzero value is located.
     */
    private static int getLowestIndex(int[][] array, int subArray) {
        // Loop through all value until we find a nonzero value
        int i = 0;
        while (i < array[subArray].length && array[subArray][i] == 0) {
            i++;
        }
        return i;
    }

    /**
     * Finds lowest nonzero value in the histogram and returns it
     *
     * @param array    2d array
     * @param subArray Sub-array to be searched
     * @return Lowest nonzero value in the array.
     */
    private static int getLowestNonZeroValue(int[][] array, int subArray) {
        // Loop through all value until we find a nonzero value
        int i = 0;
        while (i < array[subArray].length && array[subArray][i] == 0) {
            i++;
        }
        return array[subArray][i];
    }

    /**
     * Finds the lowest value in the three passed integers.
     *
     * @param a
     * @param c
     * @param c
     * @return Lowest int among the passed values
     */
    private static int findLowestInt(int a, int b, int c) {
        int smallest;
        if (a < b && a < c) {
            smallest = a;
        } else if (b < c && b < a) {
            smallest = b;
        } else {
            smallest = c;
        }
        return smallest;
    }

    /**
     * Shifts the histogram in of a sub-array in a 2d array.
     *
     * @param array       The 2d array
     * @param subArray    The sub-array to be shifted
     * @param index       The location of the lowest nonzero value in the array
     * @param shiftAmount oldHistogram will be shifted by this amount
     */
    private static void shiftHistToLeft(int[][] array, int subArray, int index, int shiftAmount) {
        //Shift all values to the left by lowestIndex, starting at index.
        int startingIndex = index - shiftAmount;
        for (int i = index; i < array[subArray].length; i++, startingIndex++) {
            array[subArray][startingIndex] = array[subArray][i];
        }
    }

    public static void main(String[] args) {
        // Instantiate Picture
        Picture picture = new Picture();
        myPic = picture;

        /*
         * Create 2d array with 6 dimensions of 256 indexes.
         * The first 3 dimensions are used for the originals picture.
         * The last 3 dimensions are used for the normalized picture.
         * They follow the order of RGB
         * the six indexes go in this order
         * 0 - red original picture
         * 1 - green original picture
         * 2 - blue original picture
         * 3 - red normalized
         * 4 - green normalized
         * 5 - blue normalized
         */
        int histogramArray[][] = new int[6][256];


        // 100, since it will be drawn in a 255 * 100 window
        int histogramMax = 100;
        // 255, since it will be drawn in a 255 * 255 window
        int imageMax = 255;

        // Get the histogram
        retrieveRGBValues(histogramArray);
        printRGB(histogramArray);


        // Normalize the image to a max of 255
        // Then spread out the histogram
        normalize(histogramArray, imageMax, RED_ORIGINAL);
        normalize(histogramArray, imageMax, GREEN_ORIGINAL);
        normalize(histogramArray, imageMax, BLUE_ORIGINAL);

        System.out.println("Array has been normalized");
        printRGB(histogramArray);

        /* Spread the histogram
         * 1) Find the first nonzero value in the three histograms
         *
         */
        spreadHistogram(histogramArray);


        printRGB(histogramArray);
    }
}

























