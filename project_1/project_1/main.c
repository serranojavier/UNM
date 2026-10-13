/*************************************************/
/* Jqvier Serrano  				 */
/* August 22 2013				 */
/* CS-241 Section 2				 */
/*************************************************/

#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <stdlib.h>

#define ARRAY_SIZE 22
#define MAX_COLS 3
#define MAX_ROWS 3
#define TRUE 0
#define FALSE -1
#define UNKNOWN -2

// These 4 constant specify the starting position of a diagonal in the square
#define TOP_LEFT 10
#define TOP_RIGHT 11

#define MAX_SQUARE_LENGTH 18

// Gives the # of elements of an array
// got it from this stackoverflow post
//stackoverflow.com/questions/37538/how-do-i-determine-the-size-of-my-array-in-c
#define NoOfElements(x) (sizeof(x) / sizeof(x[0]))

void formatSquare(int square[][MAX_COLS], char *string);
void printString(char *string);
int hasUnpairedSequence(char *string);
int hasValidChars(char *string);
int validateInput(char *string);
int hasRepeatedValues(char *string);
void printSquare(int square[][MAX_COLS]);
void solveSquare(int square[][MAX_COLS], char *string);
int findMagicConst(int square[][MAX_COLS]);
void solveCorners(int square[][MAX_COLS]);
int scanRow(int square[][MAX_COLS], int row);
int scanCol(int square[][MAX_COLS], int col);
int scanDiagonal(int square[][MAX_COLS], int corner);

void solveRow(int square[][MAX_COLS], int row, int constant);
void solveCol(int square[][MAX_COLS], int col, int constant);
void solveDiagonals(int square[][MAX_COLS], int constant, int corner);
int isSolved(int square[][MAX_COLS]);

int main(void)
{

    char string[ARRAY_SIZE];


    int square[MAX_ROWS][MAX_COLS];
    int row, col;
    //Populate array with 0
    for (row = 0; row < MAX_ROWS; row++)
    {
        for (col = 0; col < MAX_COLS; col++)
        {
            square[row][col] = 0;
        }
    }

    while (fgets(string, sizeof(string), stdin) != NULL)
    {

        if (validateInput(string) != TRUE)
        {
            printString(string);
            printf(" Error\n");
        }
        else
        {

            formatSquare(square, string);
            solveSquare(square, string);
            printString(string);
            printf(" -> ");
            printSquare(square);
            printf("\n");
        }
    }
    return 0;
}

void solveSquare(int square[][MAX_COLS], char *string)
{
    // A property of 3x3 magic squares is the the sum of
    // a row, column or diagonal is the
    // number is the center multiplied times 3

    // find the center constant,
    int constant = FALSE;
    if (square[1][1] == UNKNOWN)
    {
        constant = findMagicConst(square);
        int solved = FALSE;
        while (solved == FALSE) {
            solveRow(square, 0, constant);
            solveRow(square, 1, constant);
            solveRow(square, 2, constant);

            solveCol(square, 0, constant);
            solveCol(square, 1, constant);
            solveCol(square, 2, constant);
            solveDiagonals(square, constant, TOP_LEFT);
            solveDiagonals(square, constant, TOP_RIGHT);
            if (isSolved(square) != FALSE) solved = TRUE;
        }
    }
    else
    {
        constant = 3 * (square[1][1]);
        int solved = FALSE;

        while (solved == FALSE) {
            solveRow(square, 0, constant);
            solveRow(square, 1, constant);
            solveRow(square, 2, constant);

            solveCol(square, 0, constant);
            solveCol(square, 1, constant);
            solveCol(square, 2, constant);
            solveDiagonals(square, constant, TOP_LEFT);
            solveDiagonals(square, constant, TOP_RIGHT);
            if (isSolved(square) != FALSE) solved = TRUE;
        }
    }
}

int isSolved(int square[][MAX_COLS])
{
    int row, col;
    for (row = 0; row < MAX_ROWS; row++)
    {
        for (col = 0; col < MAX_COLS; col++)
        {
            if (square[row][col] == UNKNOWN)
            {
                return FALSE;
            }
        }
    }
    return TRUE;
}

void solveRow(int square[][MAX_COLS], int row, int constant)
{
    int numUnkowns = 0;
    int col;
    int unkownCol;
    for (col = 0; col < MAX_COLS; col++)
    {
        if (square[row][col] == UNKNOWN)
        {
            numUnkowns++;
            unkownCol = col;
        }
    }

    if (numUnkowns == 1)
    {
        int a, b, z;
        if (square[row][0] == UNKNOWN)
        {
            a = square[row][1];
            b = square[row][2];
        }
        else if (square[row][1] == UNKNOWN)
        {
            a = square[row][0];
            b = square[row][2];
        }
        else
        {
            a = square[row][0];
            b = square[row][1];
        }

        z = constant - (a + b);
        square[row][unkownCol] = z;
    }
}

void solveCol(int square[][MAX_COLS], int col, int constant)
{
    int numUknowns = 0;
    int row;
    int unkownRow;
    for (row = 0; row < MAX_ROWS; row++)
    {
        if (square[row][col] == UNKNOWN)
        {
            numUknowns++;
            unkownRow = row;
        }
    }

    if (numUknowns == 1)
    {
        int a, b, z;
        if (square[0][col] == UNKNOWN)
        {
            a = square[1][col];
            b = square[2][col];
        }
        else if (square[1][col] == UNKNOWN)
        {
            a = square[0][col];
            b = square[2][col];
        }
        else
        {
            a = square[0][col];
            b = square[1][col];
        }

        z = constant - (a + b);
        square[unkownRow][col] = z;
    }
}

void solveDiagonals(int square[][MAX_COLS], int constant, int corner)
{
    if (corner == TOP_LEFT)
    {
        int numUnkowns = 0;
        int row, col, unknownRow, unknownCol;
        col = 0;
        for (row = 0; row < MAX_ROWS; row++)
        {
            if (square[row][col] == UNKNOWN)
            {
                numUnkowns++;
                unknownCol = col;
                unknownRow = row;
            }
            col++;
        }

        if (numUnkowns == 1)
        {
            int a, b, z;
            if (square[0][0] == UNKNOWN)
            {
                a = square[1][1];
                b = square[2][2];
            }
            else if (square[1][1] == UNKNOWN)
            {
                a = square[0][0];
                b = square[2][2];
            }
            else
            {
                a = square[0][0];
                b = square[1][1];
            }

            z = constant - (a + b);
            square[unknownRow][unknownCol] = z;
        }
    }
    else
    {
        int numUnkowns = 0;
        int row, col, unknownRow, unknownCol;
        col = 0;
        for (row = 0; row < MAX_ROWS; row++)
        {
            if (square[row][col] == UNKNOWN)
            {
                numUnkowns++;
                unknownCol = col;
                unknownRow = row;
            }
            col++;
        }

        if (numUnkowns == 1)
        {
            int a, b, z;
            if (square[0][2] == UNKNOWN)
            {
                a = square[1][1];
                b = square[2][0];
            }
            else if (square[1][1] == UNKNOWN)
            {
                a = square[0][2];
                b = square[2][0];
            }
            else
            {
                a = square[0][2];
                b = square[1][1];
            }

            z = constant - (a + b);
            square[unknownRow][unknownCol] = z;
        }
    }
}

int findMagicConst(int square[][MAX_COLS])
{
    solveCorners(square);
    // Scan through the rows and cols, if the constant is found return it
    // Otherwise return FALSE
    int constant;
    int row, col;
    for (col = 0; col < MAX_COLS; col++)
    {
        constant = scanCol(square, col);
        if (constant != FALSE) return scanCol(square, col);
    }

    for (row = 0; row < MAX_ROWS; row++)
    {
        constant = scanRow(square, row);
        if (constant != FALSE) return scanRow(square, row);
    }
    return FALSE;
}

int scanRow(int square[][MAX_COLS], int row)
{
    int i = 0;
    if (square[row][i] != UNKNOWN &&
            square[row][i + 1] != UNKNOWN &&
            square[row][i + 2] != UNKNOWN)
    {
        int sum = 0;
        for (i = 0; i < MAX_COLS; i++)
        {
            sum += square[row][i];
        }
        return sum;
    }
    else
    {
        return FALSE;
    }
}

int scanCol(int square[][MAX_COLS], int col)
{
    int i = 0;
    if (square[i][col] != UNKNOWN &&
            square[i + 1][col] != UNKNOWN &&
            square[i + 2][col] != UNKNOWN)
    {
        int sum = 0;
        for (i = 0; i < MAX_ROWS; i++)
        {
            sum += square[i][col];
        }
        return sum;
    }
    else
    {
        return FALSE;
    }
}

void solveCorners(int square[][MAX_COLS])
{
    /**************************************
     * \\ . \\ . \\ z
     * \\ a \\ . \\ .
     * \\ . \\ b \\ .
     * A property of 3x3 magic squares is that
     * b + a = 2z
     * The same applies for the other corners
     ***************************************/
    // Top right
    int a = square[1][0];
    int b = square[2][1];
    if (a != UNKNOWN && b != UNKNOWN)
    {
        square[0][2] = (a + b) / 2;
    }
    // Top left
    a = square[2][1];
    b = square[1][2];
    if (a != UNKNOWN && b != UNKNOWN)
    {
        square[0][0] = (a + b) / 2;
    }
    // Bottom left
    a = square[0][1];
    b = square[1][2];
    if (a != UNKNOWN && b != UNKNOWN)
    {
        square[2][0] = (a + b) / 2;
    }
    // Bottom right
    a = square[0][1];
    b = square[1][0];
    if (a != UNKNOWN && b != UNKNOWN)
    {
        square[2][2] = (a + b) / 2;
    }
}

int scanDiagonal(int square[][MAX_COLS], int corner)
{
    int row, col, sum;
    if (corner == TOP_LEFT)
    {
        if (square[0][0] != UNKNOWN &&
                square[1][1] != UNKNOWN &&
                square[2][2] != UNKNOWN)
        {
            row = 0;
            sum = 0;
            for (col = 0; col < MAX_COLS; col++)
            {
                sum += square[row][col];
                row++;
            }
            return sum;
        }
        else
        {
            return FALSE;
        }
    }
    else
    {
        if (square[0][2] != UNKNOWN &&
                square[1][1] != UNKNOWN &&
                square[0][2] != UNKNOWN)
        {
            col = 2;
            sum = 0;
            for (row = 0; row < MAX_ROWS; row++)
            {
                sum += square[row][col];
                col--;
            }
            return sum;
        }
        else
        {
            return FALSE;
        }
    }
}

void printSquare(int square[][MAX_COLS])
{
    int row, col;
    for (row = 0; row < MAX_COLS; row++)
    {
        for (col = 0; col < MAX_ROWS; col++)
        {
            printf("%02d", square[row][col]);
        }
    }

}

int validateInput(char *string)
{
    // Check for this criteria
    // 1) input is 18 characters long
    // 2) input does not have unpaired '?'
    // 3) input does not contain characters other than '?' and 0-9
    // 4) repeated values, all values must be unique
    if ((strlen(string) - 1) != MAX_SQUARE_LENGTH) return FALSE;
    else if (hasUnpairedSequence(string) == TRUE) return FALSE;
    else if (hasValidChars(string) == FALSE) return FALSE;
    else if (hasRepeatedValues(string) == TRUE) return FALSE;
    else return TRUE;
}

/**
 *
 *
 *
 */
int hasRepeatedValues(char *string)
{

    /*****************************************
     * listOfPairs holds the integer representation of the input
     * if a pair of chairs can be converted to an int
     * they're added to this array.
     * Then the value at each index is compared against the whole
     * if a repeated value is found, TRUE is returned.
     *****************************************/
    int i;
    int listOfPairs[MAX_SQUARE_LENGTH / 2] = {-1};
    // Populate the array with -1
    for (i = 0; i < NoOfElements(listOfPairs); i++)
    {
        listOfPairs[i] = -1;
    }


    /*****************************************
     * Conver char pairs to ints
     *****************************************/
    char pair[2];
    // j is the index of listOfPairs[]
    // since
    int j = 0;
    for (i = 0; i < strlen(string); i += 2)
    {
        // Get a pair of chars from string and make sure they're both numbers before
        // making the conversion
        if (isdigit(string[i]) && isdigit(string[i + 1]))
        {
            pair[0] = string[i];
            pair[1] = string[i + 1];
            listOfPairs[j] = atoi(pair);
            j++;
        }
    }


    /*****************************************
     * Find any duplicates
     *****************************************/
    int state = FALSE;
    // compare all pairs to all other pairs in the array
    // NOTE: sizeof(pairs)/sizeof(int) gives the # of elements in the array
    for (i = 0; i < NoOfElements(listOfPairs); i++)
    {
        // sizeof - 1 since we're starting at 1 for this loop
        for (j = 0; j < NoOfElements(listOfPairs); j++)
        {
            if (i != j)
            {
                // Break out when either one of then it's an unknow
                if (listOfPairs[j] == -1 || listOfPairs[i] == -1)
                {
                    break;
                }
                //printf("Comparing %d == %d", listOfPairs[i], listOfPairs[j]);
                if (listOfPairs[i] == listOfPairs[j])
                {
                    //printf(" True\n");
                    return TRUE;
                }
                //printf(" False\n");
            }
        }
    }
    return state;
}

// Return FALSE if char is not '?' or 0-9
int hasValidChars(char *string)
{
    int state = TRUE;
    int i;
    for (i = 0; i < strlen(string) - 1; i++)
    {
        char c = string[i];
        if (isalpha(c))
        {
            return FALSE;
        }
    }
    return state;
}

// Will return TRUE if the are an unpaired '?' character
int hasUnpairedSequence(char *string)
{
    int state = FALSE;
    int i;
    char nextChar;
    char c;

    for (i = 0; i < strlen(string); i += 2)
    {
        c = string[i];
        nextChar = string[i + 1];

        // If either c or prevChar is '?' perform the comparison,
        if (c == '?' || nextChar == '?')
        {
            // Return as soon as we find an unpaired sequence
            if (c != nextChar)
            {
                return TRUE;
            }
        }
    }
    return state;
}

void printString(char *string)
{
    int i;
    for (i = 0; i < strlen(string); i++)
    {
        char c = string[i];
        // Skip printing \n, since Error is printed afterwards on the same line
        if (c == '\n')
        {
            break;
        }
        else printf("%c", c);
    }
}


void formatSquare(int square[][MAX_COLS], char *string)
{

    int row, col, index;
    index = 0;
    char pair[2];
    for (row = 0; row < MAX_COLS; row++)
    {
        for (col = 0; col < MAX_ROWS; col++)
        {
            if (isdigit(string[index]) && isdigit(string[index + 1]))
            {
                pair[0] = string[index];
                pair[1] = string[index + 1];
                square[row][col] = atoi(pair);
            }
            else
            {
                square[row][col] = UNKNOWN;
            }
            index += 2;
        }
    }
}