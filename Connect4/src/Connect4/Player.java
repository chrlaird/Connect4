package Connect4;

/* File: Player.java
 * algorithms are based on minMax search algorithms on lecture notes
 * (including alpha beta pruning)
 * and enumerate method posted on course website
 * 
 * 10 is Blue denoted by even number
 * 1 is Red denoted by Odd number 
 */

public class Player {
    
    //print the player number
    private String printPlayer(int player){
        if (player == 0) {
            return "-";
        } else if (player == 1) {
            return "1";
        } else if (player == 10) {
            return "10";
        } else {
            return null;
        }
    }
    
    //print the board
    private void printBoard(int [][] b, int move) {
        for (int i = 0 ; i < b.length; ++i) {
            for (int h = 0; h < move; ++h)
                System.out.print("  "); 
            
            for (int j = 0; j < b[0].length; j ++) {
                System.out.print(printPlayer(b[i][j]) + " ");
            }
            System.out.println();
        }
        System.out.println(); 
        System.out.println(); 
    }
    
    private static final int D = 7; //maximum search depth,now we can beat professor's!
    
    private static boolean isLeaf;
    
    //min max searching using alpha beta pruning(modified from lecture notes)
    private static float minMax(int[][] b, int depth, float alpha, float beta) {
        
        int piece; 
        if (depth % 2 == 0)
            piece = 10;                      // Blue's turn: even number
        else
            piece = 1;                      // red's turn: odd number
        
        int e = eval(b);
        //if board is full ,isLeaf is true
        isLeaf = true;
        for(int j = 0; j < b[1].length; j ++){
            if (b[0][j] == 0){
                isLeaf = false;
                break;
            }
        }
        
        //if there is a win or loss isLeaf is true
        if(e == Integer.MAX_VALUE ||
           e == Integer.MIN_VALUE){
            isLeaf = true;
        }

        if (isLeaf || depth == D || e > Integer.MAX_VALUE
           ||e < Integer.MIN_VALUE) {    //if its a leaf return evaluation
            return e;
        } else if (piece == 10) {
            for (int j = 0; j < b[0].length; j++) {   //blue's turn (Max)
                int i = b.length - 1;
                while (b[i][j] != 0 && i > 0) {
                    i--;
                }
                if (b[i][j] == 0) {              // square is empty
                    b[i][j] = piece;   
                    alpha = Math.max(alpha, minMax(b, depth + 1, alpha, beta));
                    b[i][j] = 0; 
                    if(beta <= alpha) break;
                }
            }
            return alpha;
            
        } else {
            for (int j = 0; j < b[0].length; j ++) {
                int i = b.length - 1;
                while (b[i][j] != 0 && i > 0) {  //red's turn MIN
                    i--;
                }
                if (b[i][j] == 0) {              // square is empty
                    b[i][j] = piece;                     // make a move for the piece
                    beta = Math.min(beta,minMax(b, depth + 1, alpha, beta));  
                    b[i][j] = 0;
                    if (beta <= alpha) break;
                }
            }
            return beta;
        }
    }

    //return the column number of the best move for MAX
    public static int move(int[][] b){
        int bestMove = 4;
        
        int numMoves = numberOfMoves(b);
        // if board is empty, choose column 4
        if (numMoves == 0){
            return bestMove;
        } else if (numMoves == 1) {
            int index = 0;
            for (int j = 0; j < 8; j++) {
                if (b[7][j] != 0) index = j;
            }
            if (index < 4) return index+1;
            else return index-1;
        } 

        float max = Float.NEGATIVE_INFINITY;
        
        //for each children:
        for(int j = 0; j < b[0].length; j ++){
            int i = b.length - 1;
            while(b[i][j] != 0 && i > 0 ){
                i--;
            }
            if(b[i][j] == 0)  {              // square is empty
                b[i][j] = 10;
                float val = minMax(b, 1, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY );
                b[i][j] = 0;
                if(val > max){
                    bestMove = j;  //update column number with the best children
                    max = val;
                }
                if(max == Integer.MAX_VALUE){
                    break;
                }
            }
            
        }
        return bestMove;
    }
    
    //returns the number of filled spaces
    private static int numberOfMoves(int[][] b){
        int count = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (b[i][j] != 0) count++;
            }
        }
        return count;
    }
    
    // returns the index of the top block in the specified lane, returns 7 if empty
    private static int vIndex(int[][] b, int j) {
        for (int i = 0; i < 8; i++) {
            if (b[i][j] != 0) {
                return i;
            }
        }
        return 7;
    }
    
    public static int eval(int[][] b) {
        // check for vertical game over
        for (int j = 0; j < 8; j++) {
            for (int i = vIndex(b, j); i < 5; i++) {
                if ((b[i][j] == 10) && (b[i+1][j] == 10) && (b[i+2][j] == 10) && (b[i+3][j] == 10)) return Integer.MAX_VALUE;
                if ((b[i][j] == 1) && (b[i+1][j] == 1) && (b[i+2][j] == 1) && (b[i+3][j] == 1)) return Integer.MIN_VALUE;
            }
        }

        // check for horizontal game over
        for (int j = 0; j < 5; j++) {
            for (int i = vIndex(b, j); i < 8; i++) {
                if ((b[i][j] == 10) && (b[i][j+1] == 10) && (b[i][j+2] == 10) && (b[i][j+3] == 10)) return Integer.MAX_VALUE;
                if ((b[i][j] == 1) && (b[i][j+1] == 1) && (b[i][j+2] == 1) && (b[i][j+3] == 1)) return Integer.MIN_VALUE;
            }
        }
        
        // check for diagonal bottom left to top right game over
        for (int j = 7; j > 2; j--) {
            for (int i = vIndex(b, j); i < 5; i++) {
                if ((b[i][j] == 10) && (b[i+1][j-1] == 10) && (b[i+2][j-2] == 10) && (b[i+3][j-3] == 10)) return Integer.MAX_VALUE;
                if ((b[i][j] == 1) && (b[i+1][j-1] == 1) && (b[i+2][j-2] == 1) && (b[i+3][j-3] == 1)) return Integer.MIN_VALUE;
            }
        }

        // check for diagonal top left to bottom right game over
        for (int j = 0; j < 5; j++) {
            for (int i = vIndex(b, j); i < 5; i++) {
                if ((b[i][j] == 10) && (b[i+1][j+1] == 10) && (b[i+2][j+2] == 10) && (b[i+3][j+3] == 10)) return Integer.MAX_VALUE;
                if ((b[i][j] == 1) && (b[i+1][j+1] == 1) && (b[i+2][j+2] == 1) && (b[i+3][j+3] == 1)) return Integer.MIN_VALUE;
            }
        }
        
        int score = 0;
        
        // add five points for each vertical two in a row, subtract five points for opponent
        // only add/subtract three if it occurs on either side edge of the board
        for (int j = 0; j < 8; j++) {
            for (int i = 7; i > vIndex(b, j); i--) {
                if ((b[i][j] == 10) && (b[i-1][j] == 10)) {
                    score += 5;
                    if (j == 0 || j == 7) score -= 2;
                }
                if ((b[i][j] == 1) && (b[i-1][j] == 1)) {
                    score -= 5;
                    if (j == 0 || j == 7) score += 2;
                }
            }
        }
        
        // add five points for each horizontal two in a row, subtract five points for opponent
        // only add/subtract three if it occurs on either side edge of the board
        for (int j = 0; j < 7; j++) {
            for (int i = 7; i >= vIndex(b, j); i--) {
                if ((b[i][j] == 10) && (b[i][j+1] == 10)) {
                    score += 5;
                    if (j == 0 || j == 6) score -= 2;
                }
                if ((b[i][j] == 1) && (b[i][j+1] == 1)) {
                    score -= 5;
                    if (j == 0 || j == 6) score += 2;
                }
            }
        }

        // add six points for each diagonal bottom left to top right two in a row, subtract for opponent
        // only add/subtract four if it occurs on either side edge of the board
        for (int j = 7; j > 0; j--) {
            for (int i = vIndex(b, j); i < 7; i++) {
                if ((b[i][j] == 10) && (b[i+1][j-1] == 10)) {
                    score += 6;
                    if (j == 1 || j == 7) score -= 2;
                }
                if ((b[i][j] == 1) && (b[i+1][j-1] == 1)) {
                    score -= 6;
                    if (j == 1 || j == 7) score += 2;
                }
            }
        }

        // add six points for each diagonal top left to bottom right two in a row, subtract for opponent
        // only add/subtract four if it occurs on either side edge of the board
        for (int j = 0; j < 7; j++) {
            for (int i = vIndex(b, j); i < 7; i++) {
                if ((b[i][j] == 10) && (b[i+1][j+1] == 10)) {
                    score += 6;
                    if (j == 0 || j == 6) score -= 2;
                }
                if ((b[i][j] == 1) && (b[i+1][j+1] == 1)) {
                    score -= 6;
                    if (j == 0 || j == 6) score += 2;
                }
            }
        }
        
        // add seven points for each vertical three in a row, subtract seven for opponent vertical three in a row
        for (int j = 0; j < 8; j++) {
            for (int i = vIndex(b, j); i < 6; i++) {
                if ((b[i][j] == 10) && (b[i+1][j] == 10) && (b[i+2][j] == 10)) score += 7;
                if ((b[i][j] == 1) && (b[i+1][j] == 1) && (b[i+2][j] == 1)) score -= 7;
            }
        }

        // add seven points for each horizontal three in a row, subtract seven for opponent horizontal three in a row
        for (int j = 0; j < 6; j++) {
            for (int i = vIndex(b, j); i < 8; i++) {
                if ((b[i][j] == 10) && (b[i][j+1] == 10) && (b[i][j+2] == 10)) score += 7;
                if ((b[i][j] == 1) && (b[i][j+1] == 1) && (b[i][j+2] == 1)) score -= 7;
            }
        }

        // add eight points for each diagonal three in a row from bottom left to top right
        // subtract eight points for each opponent three in a row from bottom left to top right
        for (int j = 7; j > 1; j--) {
            for (int i = vIndex(b, j); i < 6; i++) {
                if ((b[i][j] == 10) && (b[i+1][j-1] == 10) && (b[i+2][j-2] == 10)) score += 8;
                if ((b[i][j] == 1) && (b[i+1][j-1] == 1) && (b[i+2][j-2] == 1)) score -= 8;
            }
        }

        // add eight points for each diagonal three in a row from top left to bottom right
        // subtract eight points for each opponent three in a row from top left to bottom right
        for (int j = 0; j < 6; j++) {
            for (int i = vIndex(b, j); i < 6; i++) {
                if ((b[i][j] == 10) && (b[i+1][j+1] == 10) && (b[i+2][j+2] == 10)) score += 8;
                if ((b[i][j] == 1) && (b[i+1][j+1] == 1) && (b[i+2][j+2] == 1)) score -= 8;
            }
        }
        return score;
    }
    
    //unit test
    public static void main(String [] args) {
        
        int[][] b = new int[8][8];
        Player p = new Player();
        b[7][3] = 1;
        b[7][4] = 10;
        System.out.println(p.move(b));
    }
}
            