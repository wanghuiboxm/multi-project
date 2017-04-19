package string;

import java.util.Scanner;

/**
 * Created by wanghb on 2017/4/3.
 */
public class FormatPrintString {

    public static void printString(String s) {
        char[] chars = s.toCharArray();
        int cycle = (chars.length/16)+1;
        for(int i=0; i<cycle; ++i) {
            System.out.printf("%h", Integer.toHexString(i));
            int count = 16;
            if(i== cycle-1) {
                count = chars.length - i*16;
            }
            char[] ss = new char[count];
            for(int j=0; j<count; ++j) {
                System.out.print("  " + Integer.toHexString((int)chars[i*16+j]));
                ss[j] = chars[i*16+j];
            }
            System.out.print("  " + new String(ss));
            System.out.println();
        }

    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        printString(s);
    }
}
