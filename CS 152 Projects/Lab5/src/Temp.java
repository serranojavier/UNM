public class Temp 
{
  public static int foo(int a, int b) 
  {
    for (int i = a; i > 1; i--)
    {
      System.out.print(i + " ");
      if ((a % i == 0) && (b % i == 0)) return i;
    }
    return 1;
  }
  
  public static void main(String[] args)
  {
    int a = 6, b = 25, c = 50;
    System.out.println("foo=" + foo(a, c));
    System.out.println("foo=" + foo(a, b));
    System.out.println("foo=" + foo(b, c));
  }
}