/*
 * Connect4.java
 * 
 */

package Connect4;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.*;
import java.util.EventObject;

public class Connect4 extends Player {
    
    Player P = new Player(); 
    
    private static boolean spacePlayed (String button) {
        return true;
    }
    
    private static void drawGui() {
        
        // creates the frame 
        JFrame frame = new JFrame("Connect Four");
        JCanvas canvas = new JCanvas();
        frame.add(canvas);
        frame.setSize(900,800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(new Color(255, 240, 180));
        
        //labels
        JLabel connect4 = new JLabel("Connect Four");
        connect4.setFont(new Font("Lucida Grande",Font.BOLD, 23));
        JLabel play = new JLabel("Playing...");
        play.setFont(new Font("Lucida Grande",Font.BOLD, 16));
        
        //buttons
        JButton quit = new JButton("Quit");
        quit.setFont(new Font("Lucida Grande",Font.BOLD, 14));
        quit.setEnabled(true);
        JButton reset = new JButton("Reset");
        reset.setFont(new Font("Lucida Grande",Font.BOLD, 14));
        reset.setEnabled(true);
        
        // colors declared for the board and game pieces
        Color beige = new Color(255, 240, 180);
        Color lightBlue = new Color(0, 125, 250);
        Color lightRed = new Color(255, 85, 85);
        
        JButton[][] board = new JButton[8][8];
        int[][] boardInt = new int[8][8];
        
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new JButton();
                board[i][j].setEnabled(true);
                board[i][j].setBorder(BorderFactory.createEtchedBorder());
                JBox.setSize(board[i][j], 80, 80);
                board[i][j].setOpaque(true);
            }
        }
        // board[0][0].setBackground(lightRed);
        // board[0][1].setBackground(lightBlue);
        // board[1][0].setBackground(Color.red);
        // board[1][1].setBackground(Color.blue);
        
        JBox body =
            JBox.vbox(JBox.CENTER,
                      JBox.vspace(15),
                      JBox.hbox(JBox.CENTER, connect4),
                      JBox.vspace(15),
                      JBox.hbox(JBox.CENTER, board[0][0], board[0][1], board[0][2], board[0][3], board[0][4], board[0][5], board[0][6], board[0][7]),
                      JBox.vspace(0),
                      JBox.hbox(JBox.CENTER, board[1][0], board[1][1], board[1][2], board[1][3], board[1][4], board[1][5], board[1][6], board[1][7]),
                      JBox.vspace(0),
                      JBox.hbox(JBox.CENTER, board[2][0], board[2][1], board[2][2], board[2][3], board[2][4], board[2][5], board[2][6], board[2][7]),
                      JBox.vspace(0),
                      JBox.hbox(JBox.CENTER, board[3][0], board[3][1], board[3][2], board[3][3], board[3][4], board[3][5], board[3][6], board[3][7]),
                      JBox.vspace(0),
                      JBox.hbox(JBox.CENTER, board[4][0], board[4][1], board[4][2], board[4][3], board[4][4], board[4][5], board[4][6], board[4][7]),
                      JBox.vspace(0),
                      JBox.hbox(JBox.CENTER, board[5][0], board[5][1], board[5][2], board[5][3], board[5][4], board[5][5], board[5][6], board[5][7]),
                      JBox.vspace(0),
                      JBox.hbox(JBox.CENTER, board[6][0], board[6][1], board[6][2], board[6][3], board[6][4], board[6][5], board[6][6], board[6][7]),
                      JBox.vspace(0),
                      JBox.hbox(JBox.CENTER, board[7][0], board[7][1], board[7][2], board[7][3], board[7][4], board[7][5], board[7][6], board[7][7]),                      
                      JBox.vspace(8),
                      JBox.hbox(JBox.CENTER, JBox.hspace(75), quit, JBox.hspace(40), reset, JBox.hspace(40), play, JBox.hspace(75)),
                      JBox.vspace(15)
                     );
        frame.add(body);
        
        frame.setVisible(true);
        
        JEventQueue events = new JEventQueue();
        events.listenTo(quit, "quit");
        events.listenTo(reset, "reset");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                events.listenTo(board[i][j], "button_" + i + "_" + j);
            }
        }
        while(true) {  
            EventObject event = events.waitEvent();
            String name = events.getName(event);
            if (name.equals("quit")) {
                frame.dispose();
            } else if (name.equals("reset")) {
                for (int i = 0; i < board.length; i++) {
                    for (int j = 0; j < board[i].length; j++) {
                        board[i][j].setBackground(beige);
                    }
                }
                for (int i = 0; i < boardInt.length; i++) {
                    for (int j = 0; j < boardInt[i].length; j++) {
                        boardInt[i][j] = 0;
                    }
                }
            } else if (name.equals("button_0_0") || name.equals("button_1_0") || name.equals("button_2_0") || name.equals("button_3_0") ||
                     name.equals("button_4_0") || name.equals("button_5_0") || name.equals("button_6_0") || name.equals("button_7_0")) {
                int row = rowVal(name);
                tiles(events, board, boardInt, row, 0, lightRed, beige, true);
                
                
                int move = Player.move(boardInt);
                tiles(events, board, boardInt, 0, move, lightBlue, beige, false);
                if (Player.eval(boardInt) == Integer.MAX_VALUE) {
                    // change color of the blocks
                    play.setText("Red Player Wins!");
                    // wait then reset
                } else if (Player.eval(boardInt) == Integer.MIN_VALUE) {
                    // change color of the blocks
                    play.setText("Blue Player Wins!");
                    // wait then reset
                }
            } else if (name.equals("button_0_1") || name.equals("button_1_1") || name.equals("button_2_1") || name.equals("button_3_1") ||
                       name.equals("button_4_1") || name.equals("button_5_1") || name.equals("button_6_1") || name.equals("button_7_1")) {
                int row = rowVal(name);
                tiles(events, board, boardInt, row, 1, lightRed, beige, true);
                
                
                int move = Player.move(boardInt);
                tiles(events, board, boardInt, 0, move, lightBlue, beige, false);
                if (Player.eval(boardInt) == Integer.MAX_VALUE) {
                    // change color of the blocks
                    play.setText("Red Player Wins!");
                    // wait then reset
                } else if (Player.eval(boardInt) == Integer.MIN_VALUE) {
                    // change color of the blocks
                    play.setText("Blue Player Wins!");
                    // wait then reset
                }
            } 
            else if (name.equals("button_0_2") || name.equals("button_1_2") || name.equals("button_2_2") || name.equals("button_3_2") ||
                     name.equals("button_4_2") || name.equals("button_5_2") || name.equals("button_6_2") || name.equals("button_7_2")) {
                int row = rowVal(name);
                tiles(events, board, boardInt, row, 2, lightRed, beige, true);
                
                
                int move = Player.move(boardInt);
                tiles(events, board, boardInt, 0, move, lightBlue, beige, false);
                if (Player.eval(boardInt) == Integer.MAX_VALUE) {
                    // change color of the blocks
                    play.setText("Red Player Wins!");
                    // wait then reset
                } else if (Player.eval(boardInt) == Integer.MIN_VALUE) {
                    // change color of the blocks
                    play.setText("Blue Player Wins!");
                    // wait then reset
                }
            } 
            else if (name.equals("button_0_3") || name.equals("button_1_3") || name.equals("button_2_3") || name.equals("button_3_3") ||
                     name.equals("button_4_3") || name.equals("button_5_3") || name.equals("button_6_3") || name.equals("button_7_3")) {
                int row = rowVal(name);
                tiles(events, board, boardInt, row, 3, lightRed, beige, true);
                
                
                int move = Player.move(boardInt);
                tiles(events, board, boardInt, 0, move, lightBlue, beige, false);
                if (Player.eval(boardInt) == Integer.MAX_VALUE) {
                    // change color of the blocks
                    play.setText("Red Player Wins!");
                    // wait then reset
                } else if (Player.eval(boardInt) == Integer.MIN_VALUE) {
                    // change color of the blocks
                    play.setText("Blue Player Wins!");
                    // wait then reset
                }
            } 
            else if (name.equals("button_0_4") || name.equals("button_1_4") || name.equals("button_2_4") || name.equals("button_3_4") ||
                     name.equals("button_4_4") || name.equals("button_5_4") || name.equals("button_6_4") || name.equals("button_7_4")) {
                int row = rowVal(name);
                tiles(events, board, boardInt, row, 4, lightRed, beige, true);
                
                
                int move = Player.move(boardInt);
                tiles(events, board, boardInt, 0, move, lightBlue, beige, false);
                if (Player.eval(boardInt) == Integer.MAX_VALUE) {
                    // change color of the blocks
                    play.setText("Red Player Wins!");
                    // wait then reset
                } else if (Player.eval(boardInt) == Integer.MIN_VALUE) {
                    // change color of the blocks
                    play.setText("Blue Player Wins!");
                    // wait then reset
                }
            } 
            else if (name.equals("button_0_5") || name.equals("button_1_5") || name.equals("button_2_5") || name.equals("button_3_5") ||
                     name.equals("button_4_5") || name.equals("button_5_5") || name.equals("button_6_5") || name.equals("button_7_5")) {
                int row = rowVal(name);
                tiles(events, board, boardInt, row, 5, lightRed, beige, true);
                
                
                int move = Player.move(boardInt);
                tiles(events, board, boardInt, 0, move, lightBlue, beige, false);
                if (Player.eval(boardInt) == Integer.MAX_VALUE) {
                    // change color of the blocks
                    play.setText("Red Player Wins!");
                    // wait then reset
                } else if (Player.eval(boardInt) == Integer.MIN_VALUE) {
                    // change color of the blocks
                    play.setText("Blue Player Wins!");
                    // wait then reset
                }
            } 
            else if (name.equals("button_0_6") || name.equals("button_1_6") || name.equals("button_2_6") || name.equals("button_3_6") ||
                     name.equals("button_4_6") || name.equals("button_5_6") || name.equals("button_6_6") || name.equals("button_7_6")) {
                int row = rowVal(name);
                tiles(events, board, boardInt, row, 6, lightRed, beige, true);
                
                
                int move = Player.move(boardInt);
                tiles(events, board, boardInt, 0, move, lightBlue, beige, false);
                if (Player.eval(boardInt) == Integer.MAX_VALUE) {
                    // change color of the blocks
                    play.setText("Red Player Wins!");
                    // wait then reset
                } else if (Player.eval(boardInt) == Integer.MIN_VALUE) {
                    // change color of the blocks
                    play.setText("Blue Player Wins!");
                    // wait then reset
                }
            } 
            else if (name.equals("button_0_7") || name.equals("button_1_7") || name.equals("button_2_7") || name.equals("button_3_7") ||
                     name.equals("button_4_7") || name.equals("button_5_7") || name.equals("button_6_7") || name.equals("button_7_7")) {
                int row = rowVal(name);
                tiles(events, board, boardInt, row, 7, lightRed, beige, true);
                
                
                int move = Player.move(boardInt);
                tiles(events, board, boardInt, 0, move, lightBlue, beige, false);
                if (Player.eval(boardInt) == Integer.MAX_VALUE) {
                    // change color of the blocks
                    play.setText("Red Player Wins!");
                    // wait then reset
                } else if (Player.eval(boardInt) == Integer.MIN_VALUE) {
                    // change color of the blocks
                    play.setText("Blue Player Wins!");
                    // wait then reset
                }
            }
        }
    }
    
    private static int rowVal(String str) {
        return Character.digit(str.charAt(7), 10);
    }
    
    private static void tiles(JEventQueue events, JButton[][] board, int[][] boardInt, int m, int n, Color c, Color d, boolean userMove) {
        for (int k = m; k < 8; k++) {
            board[k][n].setBackground(c);
            events.sleep(100);
            if (k == 7) {
                if (userMove) boardInt[k][n] = 1;
                else boardInt[k][n] = 10;
            } else if (boardInt[k + 1][n] != 0) {
                if (userMove) boardInt[k][n] = 1;
                else boardInt[k][n] = 10;
                break;
            } else { 
                board[k][n].setBackground(d);
            }
        }  
    }
    
    public static void main(String[] args) {
        drawGui();
    }
}

            