/**************************************************
 * Javier Serrano
 **************************************************/

#include <stdio.h>
#include <ctype.h>
#include <stdlib.h>
#include <string.h>

#define BUFFER_SIZE 160
#define ENCRYPT -1
#define DECRYPT -2
#define ERROR 0

unsigned long lcg_c;
unsigned long lcg_m;
unsigned long lcg_a;
unsigned long lcg_x;
int status = 0;

int findM(char *line);
int parseInput(char *line, int *lcg_m, int *lcg_c, int *status);

//TODO write a method that finds a given m so that lcg has a full period
//TODO write a method that finds a

int main(int argc, const char * argv[])
{
    FILE *file = fopen("/Users/javierserrano/Documents/workspace/Xcode projects/CS241/cipher/cipher/input.in", "r");
    if (file == NULL)
    {
        return -1;
    }

    char line[BUFFER_SIZE];
    while (fgets(line, sizeof(line), file) != NULL)
    {
        int lcg_m;
        int lcg_c;
        parseInput(line, &lcg_m, &lcg_c, &status);
    }
    fclose(file);
    return 0;
}

/**************************************************
 * This function takes the line string and finds M
 *
 */
int findM(char *line)
{
    int count = 0;
    char m[BUFFER_SIZE];

    int size = strlen(line);
    printf("The string %s", line);
    printf("Has the length = %d\n", size);
    int i = 0;
    while (line[i] != ',')
    {
        if (isdigit(line[i]))
        {
            char m[i] = line[i];
        }
        i++;
    }

    return atoi(m);
}

/**************************************************
 * This method parses the input line taken from
 * stdin and find m, c and the Data to be encrypted
 * decrypted.
 *
 * The function takes in the address of m and c and
 * stores the values at those addresses.
 **************************************************/
int parseInput(char *line, int *lcg_m, int *lcg_c, int *status)
{
    //e126,25,Byte\n
    //m = 126
    // c= 25
    // Data = Byte\n
    // Missing a
    int i;
    int lineSize = sizeof(line)/sizeof(line[0]);

    // Determine if we're encrypting or decrypting
    char c = line[0];
    if (c == 'e') *status = ENCRYPT;
    else if (c == 'd') *status = DECRYPT;
    else return ERROR;

    if (findM(line) == ERROR) return ERROR;

    return 0;
}
