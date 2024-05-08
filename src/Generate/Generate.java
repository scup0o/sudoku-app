package Generate;

import static java.lang.System.exit;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Generate {
    
    private enum Level{
        EASY, MEDIUM, HARD, CUSTOM
    } //tập giá trị các chế độ màn chơi của trò chơi
    
    Level gameLevel;
    private int[][] board = new int[9][9];
    ArrayList<Integer> pList = new ArrayList<>();
    
    Random generator = new Random();
    
    public Generate(){
        pList.add(1);
        pList.add(2);
        pList.add(3);
        pList.add(4);
        pList.add(5);
        pList.add(6);
        pList.add(7);
        pList.add(8);
        pList.add(9);
    }
    
    public void testing(){
        int count=0;
        while (true){
            createBoard();
            for (int i=0; i<9; i++){
                for (int j=0; j<9; j++){
                    if (board[i][j]==0){
                        System.out.println(count);
                        printBoard();
                        return;
                    }
                }
            }
            count++;
        }
    } 
    
    public void resetBoard(){
        this.board = new int[9][9];
    }
    
    public void newBoard1(){
        /*//tao day dau
        ArrayList<Integer> pList1 = new ArrayList<Integer>();
        pList1.add(1);
        pList1.add(2);
        pList1.add(3);
        pList1.add(4);
        pList1.add(5);
        pList1.add(6);
        pList1.add(7);
        pList1.add(8);
        pList1.add(9);
        
        int tempPos = 0;
        
        for (int j=0; j<9; j++){
            tempPos = generator.nextInt(9-j);
            this.board[0][j] = pList1.get(tempPos);
            pList1.remove(tempPos);
        }
        
        //khoi tao day nho
        pList1.clear();
        for (int i=0; i<72; i++){
            this.pNum.add(pList1);
        }
        
        //tao ma tran
        int pCount=0;
        boolean back = false;
        for (int i=1; i<9; i++){
            for (int j=0; j<9; j++){
                ArrayList<Integer> pList = new ArrayList<Integer>();
                //di toi
                if (back == false){
                    for (int k=1; k<=9; k++){
                        if (checkAll(i, j, k) == true){
                            pList.add(k);
                        }
                    }
                
                    if (pList.isEmpty()){
                        back = true;
                        pCount--;
                        if (j==0){
                            j=7;
                            i--;
                        }
                        else{
                            j=j-2;
                        }
                    }
                    else{
                        this.pNum.set(pCount, pList);
                        tempPos = generator.nextInt(pList.size());
                        this.board[i][j] = pList.get(tempPos);
                        pCount++;
                        back = false;
                    }
                }
                //di lui
                else{
                    try{
                        tempPos = this.pNum.get(pCount).indexOf(this.board[i][j]);
                        this.pNum.get(pCount).remove(tempPos);
                        this.board[i][j]=0;
                        if (this.pNum.get(pCount).isEmpty()){
                            pCount--;
                            if (j==0){
                                j=7;
                                i--;
                            }
                            else{
                                j=j-2;
                            }
                        }
                        else{
                            tempPos = generator.nextInt(this.pNum.get(pCount).size());
                            this.board[i][j] = this.pNum.get(pCount).get(tempPos);
                            pCount++;
                            back = false;
                        }
                    }
                    catch(Exception e){
                        System.out.println("Fail");
                        System.out.println(this.board[i][j] + " " + pCount + " " + this.pNum.get(pCount).size());
                        this.printBoard();
                        return;
                    }
                }
            }
        }
        //this.printBoard();
        this.hideNumber();
        //this.printBoard();*/
    }
    
    public void createBoard(){
        this.board = new int[9][9];
        
        //tao day dau
        Collections.shuffle(pList);
        
        for (int j=0; j<9; j++){
            this.board[0][j] = pList.get(j);
        }
        
        newBoard(1,0);
        hideNumber();
    }
    
    
    public boolean newBoard(int row, int col){
        if (row == 9){
            return true;
        }
        else{
            ArrayList<Integer> tempList = new ArrayList<>(pList);
            Collections.shuffle(tempList);
            //tao ma tran
            for (int i=0; i<9; i++){
                if (checkAll(row, col, tempList.get(i))){
                    board[row][col]=tempList.get(i);
                    if (col==8){
                        if(newBoard(row+1,0))
                            return true;
                    }
                    else{
                        if(newBoard(row,col+1))
                            return true;
                        
                    }
                    board[row][col]=0;
                }
            }
        }
        return false;
    }
    
    private void hideNumber(){
        int numToHide = 0;
        switch (this.gameLevel){
            case EASY:
                numToHide = 30;
                break;
            case MEDIUM:
                numToHide = 45;
                break;
            case HARD:
                numToHide = 60;
                break;
        }
        int row;
        int col;
        int i=1;
        while(i<=numToHide){
            row = generator.nextInt(9);
            col = generator.nextInt(9);
            if (this.board[row][col]<10){
                this.board[row][col] = this.board[row][col] * 10;
                i++;
            }
        }
    }
    
    public boolean solveBoard(int row, int col){
        if (row == 9){
            return true;
        }
        if (board[row][col]==0){
            for (int i=1; i<=9; i++){
                if (checkAll(row, col, i)){
                    board[row][col]=i;
                    if (col==8){
                        if(solveBoard(row+1,0))
                            return true;
                        }
                    else{
                        
                        if(solveBoard(row,col+1))
                            return true;
                        }
                        board[row][col] = 0;
                    }
            }
            return false;  
        }
        else{
            if (col==8)
                return solveBoard(row+1,0);
            else
                return solveBoard(row,col+1);
        }
    }
    
    public void printBoard(){
        for (int i=0; i<9; i++){
            System.out.print("{");
            for (int j=0; j<9; j++){
                
                if (this.board[i][j]<10)
                    System.out.print(this.board[i][j]+", ");
                else
                    System.out.print("0, ");
            }
            System.out.print("}");
            System.out.println();
        }
    }
    
    private boolean rowCheck(int row, int number){
        for (int i=0; i<9; i++){
            if (number == this.board[row][i]) {
                //System.out.println(i);
                return false;}
        }
        return true;
    }
    
    private boolean colCheck(int col, int number){
        for (int i=0; i<9; i++){
            if (number == this.board[i][col]) {
                //System.out.println(i);
                return false;}
        }
        return true;
    }
    
    private boolean boxCheck(int row, int col, int number){
        int localRow = row - row % 3;
        int localCol = col - col % 3;
        for (int i = localRow; i < localRow + 3; i++){
            for (int j = localCol; j < localCol + 3; j++){
                if (number == this.board[i][j]){
                    //System.out.println(i+" "+j);
                    return false;}
            }
        }
        return true;
    }
    
    public boolean checkAll(int row, int col, int number){
        return rowCheck(row, number) && colCheck(col, number) && boxCheck(row, col, number);
    }
    
    public void setLevel(int level){
        switch(level){
            case 0:
                this.gameLevel = Level.EASY;
                break;
            case 1:
                this.gameLevel = Level.MEDIUM;
                break;
            case 2:
                this.gameLevel = Level.HARD;
                break;
            case 3:
                this.gameLevel = Level.CUSTOM;
                break;
        }
    }
    
    public boolean setBoard(int[][] customBoard, boolean custom){
            for (int i=0; i<9; i++){
                System.arraycopy(customBoard[i], 0, this.board[i], 0, 9);
            }
        if (custom){
            for (int row=0; row<9; row++){
                    for (int col=0; col<9; col++){
                        if (board[row][col]!=0){
                            int temp = board[row][col];
                            board[row][col]=0;
                            if (!checkAll(row,col,temp)) {
                                //System.out.println(row+" "+col);
                                return false;
                            }
                            board[row][col]=temp;
                        }
                    }
            }
            
            if (this.solveBoard(0,0)){
                for (int row=0; row<9; row++){
                    for (int col=0; col<9; col++){
                        if (this.board[row][col]!=customBoard[row][col]){
                            this.board[row][col]=this.board[row][col]*10;
                        }
                    }
                }
                
            }
            else{
                return false;
            }}
        return true;
    }
    
    public int[][] getBoard(){
        return this.board;
    }
    
    public String getLevel(){
        switch(this.gameLevel){
            case EASY:
                return "Dễ";
            case MEDIUM:
                return "Trung bình";
            case HARD:
                return "Khó";
            case CUSTOM:
                return "Tùy chỉnh";
        }
        return "error";
    }
}
