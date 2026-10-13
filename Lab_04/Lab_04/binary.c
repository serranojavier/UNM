/***************************************************************
* Javier Serrano
* CS 241 Section 2
*
* This program takes in command line arguments, it takes an argument to specify
* if the number passed in in binary or a decimal. It takes an argument to
* specify it's size, 8,16,32,64 bits, and it takes in the number as either
* a decimal or in binary. If the program takes in a decimal it displays
* the number in binary. If it takes in a binary number it displays it as a
* decimal.
****************************************************************/

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>
#include <math.h>
#include <locale.h>

#define TRUE 0
#define FALSE -1
#define decOption "-d"
#define binaryOption "-b"
#define BIT_8 8
#define BIT_16 16
#define BIT_32 32

#define OPTION  1
#define SIZE 2
#define NUMBER 3

#define OPTION_ERROR 10
#define SIZE_ERROR 11
#define ARGUMENTS_ERROR 12
#define NOT_BINARY_ERROR 13
#define NOT_DECIMAL_ERROR 14
#define NUM_OF_VALID_ARGUMENTS 4

void printBits(int size, int number);

void reverse(char *string);

void initializeArray(char *string, int size);

void printError(int error);

int isBinary(char *string);

void printDecimal(char *string, int size);

int main(int argc, char *argv[])
{
  // Check for the correct number of arguments
  if (argc != NUM_OF_VALID_ARGUMENTS)
  {
    printError(ARGUMENTS_ERROR);
  }
  // Check that only -b or -d were passed
  else if (strcmp(argv[OPTION], "-b") != TRUE &&
           strcmp(argv[OPTION], "-d") != TRUE)
  {
    printError(OPTION_ERROR);
  }
  // -d option was passed with a binary number
  else if (strcmp(argv[OPTION], decOption) == TRUE &&
           isBinary(argv[NUMBER]) == TRUE)
  {
    printError(NOT_DECIMAL_ERROR);
  }
  // -b option was passed with a decimal argument
  else if (strcmp(argv[OPTION], binaryOption) == TRUE &&
           isBinary(argv[NUMBER]) == FALSE)
  {
    printError(NOT_BINARY_ERROR);
  }
  // Check that either -8, -16, -32 or -64 was passed to SIZE
  else if (strcmp(argv[SIZE], "-8") != TRUE &&
           strcmp(argv[SIZE], "-16") != TRUE &&
           strcmp(argv[SIZE], "-32") != TRUE &&
           strcmp(argv[SIZE], "-64") != TRUE)
  {
    printError(SIZE_ERROR);
  }
  else if (strcmp(argv[OPTION], decOption) == TRUE ||
           strcmp(argv[OPTION], binaryOption) == TRUE)
  {
    // If option is binary
    if (strcmp(binaryOption, argv[OPTION]) == TRUE)
    {
      printDecimal(argv[NUMBER], -1 * atoi(argv[SIZE]));
    }
    // if option is decimal
    else
    {
      int size = -1 * (atoi(argv[SIZE]));
      int number = atoi(argv[NUMBER]);
      printBits(size, number);
    }
  }
  else
  {
    printError(ARGUMENTS_ERROR);
  }
  return 0;
}

/***************************************************************
* Parameters:
*     char *string - The string containing the binary sequence
*     int size - The size of the number (8,16,32 or 64 bits)
* Description:
*     Prints the decimal equivalent of the binary sequence
*     passed in.
* Algorithm:
*     Iterates through the binary sequences, when it finds a 1
*     it multiplies the 1 with the current power of the sequence
*     (if it finds a 1 at post 2 it multiplies 1 * 2^1)
*     and it adds it to the sum variable.
****************************************************************/
void printDecimal(char *string, int size)
{
  long int sum, power;
  sum = 0;
  power = 0;
  int i;
  // If the sequence is bigger than the  specified size,
  // only use SIZE number of bits starting from the right
  if (strlen(string) > size)
  {
    int difference = strlen(string) - size;
    for (i = strlen(string); i > difference; i--, power++)
    {
      char c[1];
      c[0] = string[i - 1];
      int rem = atoi(c);
      sum = sum + rem * pow(2, power);
    }
  }
  else
  {
    for (i = strlen(string); i > 0; i--, power++)
    {
      char c[1];
      c[0] = string[i - 1];
      int rem = atoi(c);
      sum = sum + rem * pow(2, power);
    }
  }
  //Leanred how to use locales here
  // http://answers.yahoo.com/question/index?qid=20130303034159AADqGnC
  setlocale(LC_NUMERIC, "en_US");
  if (size == BIT_8) printf("%'3ld\n", sum);
  if (size == BIT_16) printf("%'6ld\n", sum);
  if (size == BIT_32) printf("%'13ld\n", sum);
}

/***************************************************************
* Parameters:
*     char *string - The string to be checked for only 1s and 0s
* Return Value:
*     TRUE if the string only contains 1s and 0s, FALSE otherwise
* DESCRIPTION:
*     Iterates through the string, when it finds a char that is
*     not a 1 or a 0 it returns FALSE. Otherwise it returns
*     TRUE once it finishes iterating.
****************************************************************/
int isBinary(char *string)
{
  int size = sizeof(string) / sizeof(string[0]);
  int i;
  for (i = 0; i < size; i++)
  {
    char c = string[i];
    if (c != '1' && c != '0')
    {
      if (isdigit(c)) return FALSE;
    }
  }
  return TRUE;
}

/***************************************************************
* Parameter:
*     int error - The error id
* Description:
*     Takes in a macro id, and it prints the error accordingly.
****************************************************************/
void printError(int error)
{
  if (error == OPTION_ERROR)
  {
    printf("ERROR: argument 1 must be -b | -d\n");
    printf("usage:\n");
    printf("binary OPTION SIZE NUMBER\n");
    printf("  OPTION:\n");
    printf("    -b   NUMBER is binary and output will be in decimal.\n");
    printf("    -d   NUMBER is decimal and output will be in binary.\n");
    printf("  SIZE:\n");
    printf("    -8   input is an unsigned 8-bit integer.\n");
    printf("    -16   input is an unsigned 16-bit integer.\n");
    printf("    -32   input is an unsigned 32-bit integer.\n");
    printf("    -64   input is an unsigned 64-bit integer.\n");
    printf("  NUMBER:\n");
    printf("    number to be converted.\n\n");
  }
  else if (error == SIZE_ERROR)
  {
    printf("ERROR: argument 2 must be: -8 | -16 | -32 | -64\n");
    printf("usage:\n");
    printf("binary OPTION SIZE NUMBER\n");
    printf("  OPTION:\n");
    printf("    -b   NUMBER is binary and output will be in decimal.\n");
    printf("    -d   NUMBER is decimal and output will be in binary.\n");
    printf("  SIZE:\n");
    printf("    -8   input is an unsigned 8-bit integer.\n");
    printf("    -16   input is an unsigned 16-bit integer.\n");
    printf("    -32   input is an unsigned 32-bit integer.\n");
    printf("    -64   input is an unsigned 64-bit integer.\n");
    printf("  NUMBER:\n");
    printf("    number to be converted.\n\n");

  }
  else if (error == NOT_BINARY_ERROR)
  {
    printf("ERROR: argument 3 is not a binary integer\n");
    printf("usage:\n");
    printf("binary OPTION SIZE NUMBER\n");
    printf("  OPTION:\n");
    printf("    -b   NUMBER is binary and output will be in decimal.\n");
    printf("    -d   NUMBER is decimal and output will be in binary.\n");
    printf("  SIZE:\n");
    printf("    -8   input is an unsigned 8-bit integer.\n");
    printf("    -16   input is an unsigned 16-bit integer.\n");
    printf("    -32   input is an unsigned 32-bit integer.\n");
    printf("    -64   input is an unsigned 64-bit integer.\n");
    printf("  NUMBER:\n");
    printf("    number to be converted.\n\n");

  }
  else if (error == NOT_DECIMAL_ERROR)
  {
    printf("ERROR: argument 3 is not a decimal integer\n");
    printf("usage:\n");
    printf("binary OPTION SIZE NUMBER\n");
    printf("  OPTION:\n");
    printf("    -b   NUMBER is binary and output will be in decimal.\n");
    printf("    -d   NUMBER is decimal and output will be in binary.\n");
    printf("  SIZE:\n");
    printf("    -8   input is an unsigned 8-bit integer.\n");
    printf("    -16   input is an unsigned 16-bit integer.\n");
    printf("    -32   input is an unsigned 32-bit integer.\n");
    printf("    -64   input is an unsigned 64-bit integer.\n");
    printf("  NUMBER:\n");
    printf("    number to be converted.\n\n");
  }
  else if (error == ARGUMENTS_ERROR)
  {
    printf("ERROR: incorrect number of arguments\n");
    printf("usage:\n");
    printf("binary OPTION SIZE NUMBER\n");
    printf("  OPTION:\n");
    printf("    -b   NUMBER is binary and output will be in decimal.\n");
    printf("    -d   NUMBER is decimal and output will be in binary.\n");
    printf("  SIZE:\n");
    printf("    -8   input is an unsigned 8-bit integer.\n");
    printf("    -16   input is an unsigned 16-bit integer.\n");
    printf("    -32   input is an unsigned 32-bit integer.\n");
    printf("    -64   input is an unsigned 64-bit integer.\n");
    printf("  NUMBER:\n");
    printf("    number to be converted.\n\n");
  }
}

/***************************************************************
* Parameters:
*     int size - Argument SIZE
*     int number - Argument NUMBER
* Description:
*     Prints the binary equivalent of number.
* Algorithm:
*     Creates a character array of (SIZE + 1) size, then it
*     it checks the binary sequence of number. Where it finds
*     a 1 it stores a 1 on the character array, and a 0 everywhere
*     where a 0 is found.
****************************************************************/
void printBits(int size, int number)
{
  char bits[size + 1];
  initializeArray(bits, size + 1);
  int mask = 1;
  int i;
  //If size is bigger than 32, only work 32 bits to avoid repeating sequences
  if (size > BIT_32)
  {
    for (i = 0; i < BIT_32; i++)
    {
      if ((mask << i) & number)
      {
        bits[i] = '1';
      }
      else
      {
        bits[i] = '0';
      }
    }
  }
  else
  {
    for (i = 0; i < size; i++)
    {
      if ((mask << i) & number)
      {
        bits[i] = '1';
      }
      else
      {
        bits[i] = '0';
      }
    }
  }

  reverse(bits);
  // Print the bits adding a space every 4 characters
  for (i = 0; i < strlen(bits); i++)
  {
    if (i % 4 == 0 && i != 0)
    {
      printf(" ");
      printf("%c", bits[i]);
    }
    else
    {
      printf("%c", bits[i]);
    }
  }
  printf("\n");
}

/***************************************************************
* Parameters:
*     char *string - Character array to be initialized
*     int size - SIZE argument
* Description:
*     Populates the array with '0's
****************************************************************/
void initializeArray(char *string, int size)
{
  int i;
  for (i = 0; i < size - 1; i++)
  {
    string[i] = '0';
  }
  string[size] = '\0';
}

/***************************************************************
* Parameters:
*     char *string - The character array to be reversed
* Description:
*     Reverse a character array
****************************************************************/
void reverse(char *string)
{
  int length = strlen(string);
  int c, i, j;

  for (i = 0, j = length - 1; i < j; i++, j--)
  {
    c = string[i];
    string[i] = string[j];
    string[j] = c;
  }
}