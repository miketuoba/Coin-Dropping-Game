/*
 * DropBoxTester.java
 * @Author: Mike Wu, Student ID. 100342872
 * @Date: Apr 2, 2022
 * a testing class to test the created drop box, and to test a coin dropping down one line
 */

package DropCoin;

import java.io.IOException;

public class DropBoxTester
{
    public static void main(String[] args) throws IOException
    {
        // DropBox dropBox = new DropBox();
        DropBox dropBox = new DropBox("CoinDropBox.txt");
        System.out.println("Coin before dropping" + "\n");
        printBox(dropBox);
        drop (dropBox);
    }

    /**
    * print the box onto the console
    * @param dropBox: the DropBox object randomly created
    * @return: none
    */
    public static void printBox(DropBox dropBox)
    {
        for (int i = 0; i < DropBox.ROW; i++)
        {
            for (int j = 0; j < DropBox.COL; j++)
            {
                System.out.print(DropBox.box[i][j]);
            }
            System.out.println();
        }
        System.out.println("\n"+"\n");
    }

    /**
    * mudulate the dropping of coin one line down from the first row
    * @param dropBox: the DropBox object randomly created
    * @return: none
    */
    public static void drop(DropBox dropBox)
    {
        dropBox.dropCoin();
        System.out.println("Coin dropping down one line" + "\n");
        printBox(dropBox);
    }
}
