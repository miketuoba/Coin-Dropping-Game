/*
 * DropCoinFX.java
 * @Author: Mike Wu, Student ID. 100342872
 * @Date: Apr 2, 2022
 * a javaFX class to modulate the actual coin droppin game using graphics and animation,
 * with score of coins tallied
 */

package DropCoin;

import java.io.IOException;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.shape.Polygon;


public class DropCoinFX extends Application {
    //instance variables
    private Pane root;
    private Button[] button = new Button[19];
    private Text leftCoin;
    private Text winningCoins;
    private DropBox box;
    private int yVelocity = 3;
    private int posX = 30;
    private int posY = 30;
    private int coinNum = 100;
    private int coinWon = 0;

    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        root = new Pane();

        //get background
        Rectangle background = new Rectangle(0, 0, 600, 680);
        background.setFill(Color.WHITE);
        root.getChildren().addAll(background);
        
        //create a drop box either randomly or from file
        box = new DropBox();
        // box = new DropBox("Pagoda.txt");
        drawBox();

        //generator 19 buttons
        buttonGenerator();
        root.getChildren().addAll(button);

        //tally the game score (coins left)
        leftCoin = new Text (5, 42, "Coins: " + coinNum);
        leftCoin.setFont(Font.font("Courier New", 18));
        leftCoin.setFill(Color.RED);

        winningCoins = new Text (450, 42, "Coins won: " + coinWon);
        winningCoins.setFont(Font.font("Courier New", 18));
        winningCoins.setFill(Color.RED);

        root.getChildren().addAll(leftCoin, winningCoins);
        
        Scene scene = new Scene(root, 600, 680);
        primaryStage.setTitle("Drop Coin Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //an inner private class to modulate button-clicking action event
    private class CoinDropListener implements EventHandler<ActionEvent> {
        //instance variables
        private CoinAnimation animation;
        private Ellipse coin;

        /**
        * a handle method to reponse to the button-click event, and then set the coin to movement
        * @param e: ActionEvent object which is the event to be responded, i.e. button-clicking
        * @return: none
        */
        @Override
        public void handle(ActionEvent e) {
            setCoinNum();
            root.getChildren().remove(coin); // remove the previous coin
            for (int i = 0; i < 19; i ++)
            {
                if (e.getSource() == button[i])
                {
                    this.coin = new Ellipse(posX*(i+1),posY,10,10);
                    this.coin.setFill(Color.BLUE);
                    root.getChildren().add(this.coin);
                    animation = new CoinAnimation(i, this.coin);
                    animation.start();
                }
            }
        }

        /**
        * to set the total coin number on hand, as well as the number of coins won
        * @param: none
        * @return: none
        */
        private void setCoinNum()
        {
            root.getChildren().removeAll(leftCoin, winningCoins);
            
            //tally the coins on hand
            leftCoin = new Text (5, 42, "Coins: " + (--coinNum));
            leftCoin.setFont(Font.font("Courier New", 18));
            leftCoin.setFill(Color.RED);

            //tally the coins won
            winningCoins = new Text (450, 42, "Coins won: " + coinWon);
            winningCoins.setFont(Font.font("Courier New", 18));
            winningCoins.setFill(Color.RED);

            root.getChildren().addAll(leftCoin, winningCoins);
        }

        //an inner-inner private class to modulate coin dropping animation
        private class CoinAnimation extends AnimationTimer 
        {
            //instance variables
            private int buttonNum;
            private double x;
            private double y;
            private Ellipse coin;

            //a two-arg contructor to get the button clicked and the coin created from outer class
            private CoinAnimation(int num, Ellipse coin)
            {
                this.buttonNum = num + 1;
                this.coin = coin;
            }

            /**
            * a handle method to initiate the animation of coin
            * @param arg0: a long value to simulate time in nanoseconds
            * @return: none
            */
            @Override
            public void handle(long arg0) {
                this.x = this.coin.getCenterX();
                this.y = this.coin.getCenterY();

                dodgePegs();

                this.y += yVelocity;
                this.coin.setCenterY(this.y);

                gameWin();
            }
            
            /**
            * manipulate the coins dropping down the box without passing through the pegs
            * coins hitting on pegs will be going around to left or right randomly
            * @param: none
            * @return: none
            */
            private void dodgePegs()
            {
                if ((this.y > 30) && (this.y % posY == 0) && (this.y / posY < 21))
                {
                    if (DropBox.box[(int)((this.y-30)/posY)][this.buttonNum] == '*')
                    {
                        if (this.buttonNum == 1)
                        {                        
                            this.coin.setCenterX(this.x + posX);
                            ++this.buttonNum;
                        }
                        else if (this.buttonNum == 19)
                        {
                            this.coin.setCenterX(this.x - posX);
                            --this.buttonNum;
                        }
                        else
                        {
                            int sign = (int)(Math.random()*2);
                            this.coin.setCenterX(this.x + (posX*(sign == 0? 1:-1)));
                            this.buttonNum = sign == 0? ++this.buttonNum : --this.buttonNum;
                        }
                    }
                }
            }

            /**
            * determine if the game wins, and add coins accordingly
            * @param: none
            * @return: none
            */
            private void gameWin ()
            {
                if (this.y == posY * 22)
                {
                    if (DropBox.box[DropBox.ROW-1][this.buttonNum] == 'W')
                    {
                        coinNum += 11;
                        coinWon += 10;
                        setCoinNum();
                    }
                }
            }
        }
    }

    /**
    * draw the coin drop box onto the scene of graphics
    * @param: none
    * @return: none
    */
    public void drawBox()
    {
        for (int i = 1; i < DropBox.ROW; i ++)
        {
            for (int j = 1; j < DropBox.COL - 1; j ++)
            {
                if (DropBox.box[i][j] == '*')
                    root.getChildren().add(new Polygon(posX*j,posY*(i+1), posX*j-15,posY*(i+1)+30, posX*j+15,posY*(i+1)+30));
                if (DropBox.box[i][j] == 'W')
                {
                    Rectangle rec = new Rectangle(posX*j-15, posY*(i+1), 30, 30);
                    rec.setFill(Color.ORANGE);
                    root.getChildren().add(rec);
                }
            }
        }
    }

    /**
    * generate 19 buttons corresponding to the initial position of coin dropping
    * @param: none
    * @return: none
    */
    public void buttonGenerator()
    {
        for (int i = 1; i < DropBox.COL - 1; i++)
        {
            button[i - 1] = i < 10? new Button(" " + i + " "): new Button(i + "");
            button[i - 1].setLayoutX(posX*i-15);
            button[i - 1].setLayoutY(0);
            button[i - 1].setOnAction(new CoinDropListener());
        }
    }
}
