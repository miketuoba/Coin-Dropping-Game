/*
 * DropBox.java
 * @Author: Mike Wu, Student ID. 100342872
 * @Date: Apr 2, 2022
 * a drop box object class which creates a coin drop box randomly, or from a file
 */

package DropCoin;

import java.util.*;
import java.io.*;

public class DropBox {
    //instance variables
    public final static int ROW = 22;
    public final static int COL = 21;
    public static char[][] box;
    private int startPos;

    //default contrusctor to randomly create a drop box
    public DropBox()
    {
        this.startPos = (int)(Math.random()*19 + 1);
        box = new char[ROW][COL];
        getBox();
        getBasket();
    }

    //one-arg contrusctor to create a drop box from file
    public DropBox(String file) throws IOException
    {
        this.startPos = (int)(Math.random()*19 + 1);
        box = new char[ROW][COL];
        setBox(file);
        getBasket();
    }

    /**
    * a gettor method to get a dropbox randomly
    * @param: none
    * @return: none
    */
    public void getBox ()
    {
        for (int i = 0; i < ROW-1; i++)
        {
            for (int j = 0; j < COL; j++)
            {
                if (j == 0 || j == COL - 1)
                    box[i][j] = '*';
                else if (i != 0 && i != COL - 1)
                {
                    if (j - 1 > 0)
                    {
                        if (box[i][j - 1] != '*')
                            box[i][j] = (int)(Math.random()*2) == 0? '*': ' ';
                        else
                            box[i][j] = ' ';
                    }
                    else
                        box[i][j] = (int)(Math.random()*2) == 0? '*': ' ';
                }
                else
                    box[i][j] = ' ';
            }
        }
        box[0][startPos] = 'O'; // set a ball at a random start position on the first row of box
    }

    /**
    * a settor method to read a drop box from a file
    * @param fileName: a String of file name to be read
    * @return: none
    */
    public void setBox (String fileName) throws IOException
    {
        File file = new File (fileName);
        Scanner sc = new Scanner (file);
        sc.useDelimiter("\n");
        char[] line = new char[21];
        int i = 0;
        while (sc.hasNext() && i < ROW - 1)
        {
            line = sc.next().toCharArray();
            for (int j = 0; j < COL; j++)
            {
                box[i][j] = line[j] == '.'? ' ': line[j]; //treat '.' as spaces in the box char array
            }
            i++;
        }
        sc.close();
        box[0][startPos] = 'O'; // set a ball at a random start position on the first row of box
    }

    /**
    * a test method used to modulate a coin dropping just one line down from the box
    * @param: none
    * @return: none
    */
    public void dropCoin ()
    {
        // box[0][startPos] = 'O';
        if (box[1][startPos] != '*')
        {
            box[1][startPos] = 'O';
            box[0][startPos] = ' ';
        }
        else
        {
            if(box[1][startPos - 1] == '*' && box[1][startPos + 1] == ' '){
                box[1][startPos + 1] = 'O';
                box[0][startPos] = ' ';
            }
            else if (box[1][startPos - 1] == ' ' && box[1][startPos + 1] == '*'){
                box[1][startPos - 1] = 'O';
                box[0][startPos] = ' ';
            }
            else if (box[1][startPos - 1] == ' ' && box[1][startPos + 1] == ' ')
            {
                if ((int)(Math.random()*2) == 0){
                    box[1][startPos + 1] = 'O';
                    box[0][startPos] = ' ';
                }
                else{
                    box[1][startPos - 1] = 'O';
                    box[0][startPos] = ' ';
                }
            }
        }
    }

    /**
    * a gettor method to get five winning baskets at the bottom of the box
    * @param: none
    * @return: none
    */
    public void getBasket()
    {
        for (int i = 0; i < COL-1; i ++)
        {
            box[ROW - 1][i] = ' ';
        }
        for (int i = 0; i < 5; i ++)
        {
            box[ROW - 1][(int)(Math.random() * 19) + 1] = 'W';
        }
    }
}
