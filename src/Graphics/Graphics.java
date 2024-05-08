package Graphics;

import Generate.Generate;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;


public class Graphics extends JFrame implements ActionListener, KeyListener, WindowListener{
    JButton[][] board = new JButton[9][9];
    JButton[] numTab = new JButton[9];
    JLabel mDetail = new JLabel();
    JLabel tDetail = new JLabel("00:00");
    JPanel container = new JPanel();
    JPanel menu = new JPanel();
    JPanel customScreen = new JPanel();
    JPanel statisticScreen = new JPanel();
    JPanel hdScreen = new JPanel();
    
    JButton errorHint = new JButton("Ẩn hiển thị lỗi ô trống");
    JButton breakbutton = new JButton();
    
    JDialog upper = new JDialog();
    JPanel detailBox = new JPanel();
    
    //JLabel[][] noteS = new JLabel[9][9];
    
    private int[][][] note = new int[9][9][9];
    
    Generate game = new Generate();
    private int[][] gameBoard = new int[9][9];
    private int[][] tempBoard = new int[9][9];
    private int[] numCount = new int[9];
    //private ArrayList<Integer>[] noteCount = new ArrayList[81];
    
    private boolean fO = false;
    private boolean autoCheck = false;
    private boolean useNote;
    private int I = -1;
    private int J = -1;
    private int tNum = 0;
    private boolean mistakeOn = true;
    private boolean checkM = false;
    private int mistakeCount;
    private int mistakeMax = 3;
    private int hintCount;
    private int hintMax=3;
    private boolean newGame = false;
    private int[] totalGame = new int[3];
    private int[] bestMin = new int[3];
    private int[] bestSecond = new int[3];
    private int[] gameWon = new int[3];
    private int[] winWithNoMistake = new int[3];
    private boolean pastGame = false;
    private boolean isPastGame = false;
    private boolean reset = false;
    private int haveData = 0;
    private boolean con = false;
    
    private String fontName = "Bahnschrift";
    
    private Color colorError = Color.RED;
    private Color colorErrorBackground = new Color(242, 133, 133);
    private Color colorEmptyResultBG = new Color(255, 105, 105);
    private Color colorEditable = new Color(54, 84, 134);
    private Color colorLight = new Color(220, 242, 241);
    private Color colorMedium = new Color(123, 192, 226);
    private Color colorCurrent = new Color(45, 149, 150);
    private Color colorDark;
    private Color colorNote= new Color(96, 114, 116);
    private Color colorLightGray = new Color(221, 221, 221);
    
    AffineTransform affinetransform = new AffineTransform();     
    FontRenderContext frc = new FontRenderContext(affinetransform,true,true);  
    private Timer timer;
    DecimalFormat dFormat = new DecimalFormat("00");
    private int second;
    private int minute;
    private String ddS, ddM;
    
    String path = System.getProperty("user.home");
    
    String[] line =null;
    
    public Graphics(){
        path = path.replaceAll("\\\\", "/");
        System.out.print(path);
        for (int i = 0; i < 9; i++)
		for (int j = 0; j < 9; j++) {
                        board[i][j] = new JButton();}
        
        this.game.setLevel(1);
        readData();
        this.setTitle("Sudoku");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground( Color.WHITE);
        
        this.addWindowListener(this);
        
            errorHint.setVisible(false);
            errorHint.setBackground(null);
            errorHint.setFont(new Font("UTM Micra", 1, 15));
            errorHint.setForeground(colorEditable);
            errorHint.setBorder(BorderFactory.createEmptyBorder());
            errorHint.setPreferredSize(new Dimension(170,40));
            errorHint.addActionListener((ActionEvent e )->{
                errorHint.setVisible(false);
                breakbutton.setVisible(false);
                I=-1;
                J=-1;
                for (int i=0; i<9; i++){
                    for (int j=0; j<9; j++){
                        board[i][j].setBackground(Color.WHITE);
                    }
                }
            });
            errorHint.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    errorHint.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    errorHint.setForeground(Color.WHITE);
                    errorHint.setBackground(colorEditable);
                    
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    errorHint.setBackground(null);
                    errorHint.setForeground(colorEditable);
                }
            });
        
        /*for (int i=0; i<9; i++){
            for (int j=0;j<9; j++)
                for (int k=0; k<9; k++){
                    System.out.print(note[k][i][j]+" ");
                }
            System.out.println();
            }
        System.out.println();*/
        
        //
        menu();
        
        //this.setSize(700, 700);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        //this.setResizable(false);
        
        //this.setLayout(null);
        //this.pack(); 
    }
    
    private void readData(){
        
        try {
            File f = new File(path+"/sudoku/statistic.txt");
            Scanner sc = new Scanner(new BufferedReader(new FileReader(f)));
            int count=0;
            line = sc.nextLine().trim().split(" ");
            if (Integer.parseInt(line[0])==0) mistakeOn = true;
                    else mistakeOn = false;
                    
            if (Integer.parseInt(line[1])==0) autoCheck = true;
                else autoCheck = false;
            
            while (sc.hasNextLine()) {
              line = sc.nextLine().trim().split(" ");
              totalGame[count] = Integer.parseInt(line[0]);
              gameWon[count]= Integer.parseInt(line[1]);
              winWithNoMistake[count]=Integer.parseInt(line[2]);
              bestMin[count]=Integer.parseInt(line[3]);
              bestSecond[count]=Integer.parseInt(line[4]);
              count++;
            }
            sc.close();
          } 
        catch (FileNotFoundException e) {
            new File(path+"/sudoku").mkdirs();
            Arrays.fill(totalGame,0);
            Arrays.fill(bestMin,0);
            Arrays.fill(bestSecond,0);
            Arrays.fill(gameWon, 0);
            Arrays.fill(winWithNoMistake, 0);
          }
        
        /*for (int i=0; i<3; i++){
                System.out.print(totalGame[i]+" ");
                System.out.print(gameWon[i]+" ");
                System.out.print(winWithNoMistake[i]+" ");
                System.out.print(bestMin[i]+" ");
                System.out.print(bestSecond[i]+"");
                System.out.print("\n");
            }*/
        
        try {
            File f = new File(path+"/sudoku/game.txt");
            Scanner sc = new Scanner(new BufferedReader(new FileReader(f)));
                while(sc.hasNextLine()) {
                    line = sc.nextLine().trim().split(" ");
                    mistakeCount = Integer.parseInt(line[0]);
                    hintCount = Integer.parseInt(line[1]);
                    second = Integer.parseInt(line[2]);
                    minute = Integer.parseInt(line[3]);
                    this.game.setLevel(Integer.parseInt(line[4]));
                    
                    
                    for (int i=0; i<9; i++) {
                       line = sc.nextLine().trim().split(" ");
                       for (int j=0; j<line.length; j++) {
                          gameBoard[i][j] = Integer.parseInt(line[j]);
                       }
                    }
                    this.game.setBoard(gameBoard, false);
                    for (int i=0; i<9; i++) {
                       line = sc.nextLine().trim().split(" ");
                       for (int j=0; j<line.length; j++) {
                          tempBoard[i][j] = Integer.parseInt(line[j]);
                       }
                    }
                    line = sc.nextLine().trim().split(" ");
                    int countline=0;
                    for (int i=0; i<9; i++){
                        for (int j=0; j<9;j++){
                            boolean check = false;
                            for (int n=0; n<9; n++){
                                note[n][i][j]=Integer.parseInt(line[countline]);
                                //System.out.println(note[n][i][j]);
                                if (note[n][i][j]!=0) check=true;
                                countline++;
                            }
                            if (check == true){ 
                                String[] tempString = {"", "",""};
                                int countString=0;
                                    for (int m=0; m<9; m++){
                                        if (note[m][i][j]!=0){
                                            tempString[countString] = tempString[countString] + String.valueOf(note[m][i][j]);
                                        } 
                                        else tempString[countString] = tempString[countString] + "&nbsp;&nbsp;";
                                        if (m!=2 && m!=5 && m!=8) tempString[countString] = tempString[countString] + "&nbsp;&nbsp;";
                                        if (m==2 || m==5) {
                                            countString++;
                                        }
                                    }
                                board[i][j].setText("<html>"+tempString[0] +"<br/>"+tempString[1] +"<br/>"+tempString[2]+ "</html>");
                                //System.out.print(board[i][j].getText());
                                board[i][j].setFont(new Font("UTM Micra", 1, 19));
                                board[i][j].setForeground(colorNote);
                                //System.out.println(check);
                            }
                        }
                    }
                    
                    if (sc.hasNextLine()) line = sc.nextLine().trim().split(" ");
                    else line=null;
                 }
                
                
            
            /*while (myReader.hasNextLine()) {
              String data = myReader.nextLine();
              System.out.println(data);
            }*/
            haveData=1;
            pastGame = true;
            sc.close();
          } 
        catch (FileNotFoundException e) {
            pastGame = false;
          }
    }
    
   private void writeData(){
        System.out.print("write");
        File f = new File(path+"/sudoku/game.txt"); 
        if (haveData!=0) {f.delete();
        if (haveData==1){
            try {
                f = new File(path+"/sudoku/game.txt");
                f.createNewFile();
                f.setWritable(true);
                f.setReadable(true);
                FileWriter myWriter = new FileWriter(f);
                
                myWriter.write(mistakeCount +" "+ hintCount +" "+ second + " " + minute+" ");
                
                if (game.getLevel().equals("Dễ")) myWriter.write(0+" ");
                if (game.getLevel().equals("Trung bình")) myWriter.write(1+" ");
                if (game.getLevel().equals("Khó")) myWriter.write(2+" ");
                if (game.getLevel().equals("Tùy chỉnh")) myWriter.write(3+" ");
                
                if (mistakeOn) myWriter.write(0+" ");
                else myWriter.write(1+" ");
                
                if (autoCheck) myWriter.write(0+" ");
                else myWriter.write(1+" ");
                
                myWriter.write("\n");
                
                for (int i=0; i<9; i++){
                    for (int j=0; j<9; j++){
                        myWriter.write(gameBoard[i][j]+" ");
                    }
                    myWriter.write("\n");
                }
                
                for (int i=0; i<9; i++){
                    for (int j=0; j<9; j++){
                        myWriter.write(tempBoard[i][j]+" ");
                    }
                    myWriter.write("\n");
                }
                
                for (int i=0; i<9; i++){
                    for (int j=0; j<9;j++){
                        for (int n=0; n<9; n++){
                            myWriter.write(note[n][i][j]+" ");
                        }
                    }
                }
                
                myWriter.write("\n");
                
                for (int i=0; i<9; i++){
                    for (int j=0; j<9; j++){
                        if (board[i][j].getForeground()==colorError || board[i][j].getBackground()==colorEmptyResultBG)
                            myWriter.write(i+""+j+" ");
                    }
                }
                
                myWriter.close();
              
            } 
            catch (IOException e) {
            }
        }
        }
        
        f = new File(path+"/sudoku/statistic.txt");
        f.delete();
        try{
            f = new File(path+"/sudoku/statistic.txt");
            f.createNewFile();
            f.setWritable(true);
            f.setReadable(true);
            FileWriter myWriter = new FileWriter(f);
            if (mistakeOn) myWriter.write("0 ");
            else myWriter.write("1 ");
            
            if (autoCheck) myWriter.write("0\n");
            else myWriter.write("1\n");
            
            for (int i=0; i<3; i++){
                myWriter.write(totalGame[i]+" ");
                myWriter.write(gameWon[i]+" ");
                myWriter.write(winWithNoMistake[i]+" ");
                if (bestMin[i]<10) myWriter.write("0"+bestMin[i]+" ");
                else myWriter.write(bestMin[i]+" ");
                if (bestSecond[i]<10) myWriter.write("0"+bestSecond[i]);
                else myWriter.write(bestSecond[i]+"");
                myWriter.write("\n");
            }
            myWriter.close();
        }
        catch (IOException e) {
        }
    }
    
    private void initGame(){
        checkM=false;
        line = null;
        isPastGame = false;
        this.mistakeCount = 0;
        //this.mistakeMax = 3;
        this.hintCount = 0;
        this.hintMax = 3;
        this.useNote = false;
        this.second=0;
        this.minute=0;
        this.I=-1;
        this.J=-1;
        
        container = new JPanel();
        
        if (!reset && !this.game.getLevel().equals("Tùy chỉnh")){
            int level =0;
                switch(game.getLevel()){
                    case "Trung bình":
                        level = 1;
                        break;
                    case "Khó":
                        level=2;
                        break;
                }
            //this.game.setLevel(1);
            this.game.createBoard();
            //this.gameBoard = this.game.getBoard();
            newGame = true;
            totalGame[level]++;
        }
        else{
            newGame=false;
        }
        
        this.gameBoard = this.game.getBoard();
        breakbutton.setVisible(false);
        errorHint.setVisible(false);
        
        this.reset=false;
        Arrays.fill(this.numCount, 0);
        
        for (int i=0; i<9; i++){
            for (int j=0;j<9; j++)
                Arrays.fill(note[i][j], 0);
            }
        
        for (int i=0; i<9; i++){
                System.arraycopy(gameBoard[i], 0, this.tempBoard[i], 0, 9);
            }
        //System.out.println(checkM);
        
        gameplayScreen();
        
        
        
        /*for (int i=0; i<9; i++){
            System.out.print("{");
            for (int j=0; j<9; j++){
                
                if (this.tempBoard[i][j]<10)
                    System.out.print(this.tempBoard[i][j]+", ");
                else
                    System.out.print("0, ");
            }
            System.out.print("}");
            System.out.println();
        }*/
    }
    
    private void menu(){
        Color c = colorMedium;
        customScreen.setVisible(false);
        menu = new JPanel();
        this.setMinimumSize(new Dimension(600, 780));
        this.setLocationRelativeTo(null);
        
        menu.setBackground(c);
        menu.setLayout(new BorderLayout());
        
        this.add(menu);
        /*JLabel picLabel = new JLabel(new ImageIcon("src/img/menu-image2.jpg"));
        picLabel.setBounds(0,0,600,730);
        menu.add(picLabel);
        menu.setComponentZOrder(picLabel, 2);*/
        
        ImageIcon tImg = new ImageIcon(this.getClass().getResource("/img/sudoku-icon.png"));
        Image image = tImg.getImage().getScaledInstance(300, 300, java.awt.Image.SCALE_SMOOTH);
        tImg = new ImageIcon(image);
        setIconImage(image);
        
        JPanel topPanel = new JPanel();
               topPanel.setPreferredSize(new Dimension(this.getWidth(), (int)this.getHeight()*3/100));
               topPanel.setBackground(null);
        
        //bottom
        JPanel bottomMenu = new JPanel();
               bottomMenu.setPreferredSize(new Dimension(this.getWidth(), (int)this.getHeight()*14/100));
               bottomMenu.setBackground(null);
               FlowLayout flowLayout = new FlowLayout();
                          flowLayout.setAlignment(FlowLayout.RIGHT);
                          flowLayout.setHgap(0);
               bottomMenu.setLayout(flowLayout);
               
        /*right
        JPanel right = new JPanel();
               right.setPreferredSize(new Dimension(this.getWidth()*12/100, (int)this.getHeight()-1000));
               right.setBackground(Color.WHITE);*/
        
        //left
        
        // main
        JPanel outer = new JPanel();
               outer.setPreferredSize(new Dimension(400,800));
               outer.setBackground(c);
               
        JPanel box = new JPanel();
               box.setBackground(c);
               box.setPreferredSize(new Dimension(400,800));
        
        JLabel titleImage = new JLabel(tImg);
               //titleImage.setBounds(this.getWidth()/2, this.getHeight()/2,250,250);
        
        JLabel breakOut = new JLabel("sssssssssssssssssssssssssssssssssssssssssssssssss");
               breakOut.setForeground(c);
               
        JLabel title = new JLabel("SUDOKU");
               title.setFont(new Font(fontName, Font.BOLD, 40));
               title.setForeground(Color.WHITE);
        
        JPanel buttonBox = new JPanel();
               if (haveData==1) buttonBox.setLayout(new GridLayout(3,1,0,0));
               else buttonBox.setLayout(new GridLayout(2,1,0,0));
               buttonBox.setBackground(null);
               
        JButton newGameButton = new JButton("Tạo màn chơi mới");
                optionButtonUI(newGameButton);
                newGameButton.setPreferredSize(new Dimension(250, 50));
                newGameButton.setBackground(Color.WHITE);
                newGameButton.setForeground(colorMedium);
                newGameButton.setFont(new Font(fontName, Font.CENTER_BASELINE, 20));
                newGameButton.addActionListener((ActionEvent e) -> {
                    this.setEnabled(false);
                    gameLevelTab(4);
                });
                menuButtonUI(newGameButton);
                menuHover(newGameButton);
                
        JButton continueButton = new JButton("Tiếp tục màn chơi");
                continueButton.addActionListener((ActionEvent e) ->{
                    isPastGame = true;
                    Arrays.fill(this.numCount, 9);
                    container = new JPanel();
                    gameplayScreen();
                    this.getContentPane().remove(menu);
                    Timer();
                    this.timer.start();
                });
                menuButtonUI(continueButton);
                menuHover(continueButton);
                
        JButton exitButton = new JButton("Thoát trò chơi");
                exitButton.addActionListener((ActionEvent e) ->{
                    if (fO) writeData();
                    System.exit(0);
                });
                menuButtonUI(exitButton);
                
                exitButton.setBackground(new Color(32, 14, 58));
                exitButton.setForeground(Color.WHITE);
                
                exitButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    exitButton.setForeground(Color.WHITE);
                    exitButton.setBackground(new Color(58, 14, 36));
                    exitButton.setBorder(new RoundedBorder(25,false,colorMedium));
                    
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                   exitButton.setBackground(new Color(32, 14, 58));
                   exitButton.setForeground(Color.WHITE);
                   exitButton.setBorder(new RoundedBorder(25,false,colorMedium));
                }
            });
        
        ImageIcon sIcon = new ImageIcon(this.getClass().getResource("/img/statistic-icon.png"));
                          Image image1 = sIcon.getImage().getScaledInstance(35, 35,  java.awt.Image.SCALE_SMOOTH);
                          sIcon = new ImageIcon(image1);
                       
        JButton statistic = new JButton();
                statistic.setIcon(sIcon);
                bottomMenuButtonUI(statistic);
                statistic.addActionListener((ActionEvent e)->{
                    statistic();
                    this.revalidate();
                });
                statistic.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    statistic.setText("<html>"+"Thống kê"+"<br/>" +"thông số"+"</html>");
                    ImageIcon sIcon = new ImageIcon(this.getClass().getResource("/img/statistic-icon-hover.png"));
                          Image image1 = sIcon.getImage().getScaledInstance(35, 35,  java.awt.Image.SCALE_SMOOTH);
                          sIcon = new ImageIcon(image1);
                    statistic.setIcon(sIcon);
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    statistic.setText("");
                    ImageIcon sIcon = new ImageIcon(this.getClass().getResource("/img/statistic-icon.png"));
                          Image image1 = sIcon.getImage().getScaledInstance(35, 35,  java.awt.Image.SCALE_SMOOTH);
                          sIcon = new ImageIcon(image1);
                    statistic.setIcon(sIcon);
                }
                });
        
        ImageIcon stIcon = new ImageIcon(this.getClass().getResource("/img/setting-icon.png"));
                          image1 = stIcon.getImage().getScaledInstance(35, 35,  java.awt.Image.SCALE_SMOOTH);
                          stIcon = new ImageIcon(image1);        
        JButton settingButton = new JButton(stIcon);
                bottomMenuButtonUI(settingButton);
                settingButton.addActionListener((ActionEvent e)->{
                    setting();
                });
                settingButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    settingButton.setText("Cài đặt");
                    ImageIcon stIcon = new ImageIcon(this.getClass().getResource("/img/setting-icon-hover.png"));
                          Image image1 = stIcon.getImage().getScaledInstance(35, 35,  java.awt.Image.SCALE_SMOOTH);
                          stIcon = new ImageIcon(image1);    
                    settingButton.setIcon(stIcon);
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    settingButton.setText("");
                    ImageIcon stIcon = new ImageIcon(this.getClass().getResource("/img/setting-icon.png"));
                          Image image1 = stIcon.getImage().getScaledInstance(35, 35,  java.awt.Image.SCALE_SMOOTH);
                          stIcon = new ImageIcon(image1);    
                    settingButton.setIcon(stIcon);
                }
                });
        
        ImageIcon htpIcon = new ImageIcon(this.getClass().getResource("/img/guide-icon.png"));
                          image1 = htpIcon.getImage().getScaledInstance(35, 35,  java.awt.Image.SCALE_SMOOTH);
                          htpIcon = new ImageIcon(image1);         
        JButton htpButton = new JButton(htpIcon);
                bottomMenuButtonUI(htpButton);
                htpButton.addActionListener((ActionEvent e) ->{
                    hd();
                });
                htpButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    htpButton.setText("Hướng dẫn");
                    ImageIcon htpIcon = new ImageIcon(this.getClass().getResource("/img/guide-icon-hover.png"));
                          Image image1 = htpIcon.getImage().getScaledInstance(35, 35,  java.awt.Image.SCALE_SMOOTH);
                          htpIcon = new ImageIcon(image1);   
                    htpButton.setIcon(htpIcon);
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    htpButton.setText("");
                    ImageIcon htpIcon = new ImageIcon(this.getClass().getResource("/img/guide-icon.png"));
                          Image image1 = htpIcon.getImage().getScaledInstance(35, 35,  java.awt.Image.SCALE_SMOOTH);
                          htpIcon = new ImageIcon(image1);   
                    htpButton.setIcon(htpIcon);
                }
                });
                
         JButton br = new JButton("aaa");
                bottomMenuButtonUI(br);
                br.setVisible(true);
        
        if (haveData==1) buttonBox.add(continueButton);
        buttonBox.add(newGameButton);
        buttonBox.add(exitButton);
        
        box.add(titleImage);
        box.add(breakOut);
        box.add(title);
        box.add(buttonBox);
        
        outer.add(box);
        
        bottomMenu.add(statistic);
        //bottomMenu.add(br);
        bottomMenu.add(settingButton);
        bottomMenu.add(htpButton);
        
        menu.add(topPanel, BorderLayout.NORTH);
        menu.add(outer, BorderLayout.CENTER);
        menu.add(bottomMenu,BorderLayout.SOUTH);
        //menu.add(right, BorderLayout.EAST);
    }
    
    public void gameplayScreen(){
        haveData = 1;
        fO = true;
        
        this.setMinimumSize(new Dimension(1150, 840));
        this.setLocationRelativeTo(null);
        container.setBackground(Color.WHITE);
        container.setLayout(new BorderLayout());
        this.add(container);
        
//      board

        JPanel boardTable = new JPanel();
        boardTable.setBackground(Color.WHITE);
        boardTable.setLayout(new GridLayout(9, 9));
            for (int i = 0; i < 9; i++)
		for (int j = 0; j < 9; j++) {
                    
                    //if (!con){

                        if (!isPastGame) board[i][j] = new JButton();
                        else{
                            board[i][j].removeKeyListener(this);
                        }

                        //board[i][j].setFocusable(false);
                        if (isPastGame && tempBoard[i][j]!=0){
                            numCount[this.tempBoard[i][j]-1]--;
                            board[i][j].setText(this.tempBoard[i][j]+"");
                        }
                        if (this.gameBoard[i][j]>=10){
                            board[i][j].addKeyListener(this);
                            if (!isPastGame){
                                this.tempBoard[i][j]=0;
                                numCount[this.gameBoard[i][j]/10-1]++;}
                        }
                        /*else{
                            board[i][j].setEnabled(false);
                            board[i][j].setUI(new MetalButtonUI() {
                                @Override
                                protected Color getDisabledTextColor() {
                                    return Color.black;
                                }
                            });

                            board[i][j].addActionListener((ActionEvent e) ->{
                                //board[i][j].setBackground(new Color(54, 84, 134));
                            });

                        }*/
                        board[i][j].addActionListener(this);
                        board[i][j].setActionCommand(i + " " + j);
                            board[i][j].addActionListener((ActionEvent e) ->{
                                String s = e.getActionCommand();
                                int k = s.indexOf(32);
                                this.I = Integer.parseInt(s.substring(0, k));
                                this.J = Integer.parseInt(s.substring(k + 1, s.length()));
                                System.out.println(I + " " + J);

                            });

                        board[i][j].setFocusPainted(false);
                        if (board[i][j].getBackground()!=colorEmptyResultBG) board[i][j].setBackground(Color.white);
                        else
                            if (!errorHint.isVisible()) errorHint.setVisible(true);
                        if (board[i][j].getForeground()!=colorNote) board[i][j].setFont(new Font("UTM Micra", 1, 30));
                        

                        if (this.gameBoard[i][j]<10){
                            board[i][j].setText(String.valueOf(this.gameBoard[i][j]));
                            board[i][j].setForeground(Color.black);}
                        else{
                            if (!isPastGame) {
                                if (board[i][j].getForeground()!=colorNote)
                                    board[i][j].setText(" ");}
                            if (board[i][j].getForeground()!= colorError && board[i][j].getForeground()!=colorNote) board[i][j].setForeground(colorEditable);
                        }
                    //}
                    
                    boardTable.add(board[i][j]);
                }
            
            if (isPastGame && this.line!=null && pastGame){
                for (int i=0; i<line.length; i++)System.out.print(this.line[i]);
                            for (int t=0; t<line.length; t++){
                                if (tempBoard[Integer.parseInt(line[t])/10][Integer.parseInt(line[t])%10]!=0) board[Integer.parseInt(line[t])/10][Integer.parseInt(line[t])%10].setForeground(colorError);
                                else {
                                    board[Integer.parseInt(line[t])/10][Integer.parseInt(line[t])%10].setBackground(colorEmptyResultBG);
                                    errorHint.setVisible(true);}
                            }
                            line=null;
                        }
            
            /*for (int i=0; i<9; i++){
                for (int j=0; j<9; j++){
                    for (int k=0; k<9; k++) System.out.print(note[k][i][j]);
                }
            }*/
            
            for (int i=0; i<9; i++){
                //System.out.print(numCount[i]+" ");
            }        
            
            for (int i = 0; i < 9; i += 3)
		for (int j = 0; j < 9; j += 3) {
                    board[i][j].setBorder(new CompoundBorder(
                            BorderFactory.createMatteBorder(2,2,0,0, Color.black),
                            BorderFactory.createMatteBorder(0,0,1,1, Color.gray)));
                    board[i][j + 1].setBorder(new CompoundBorder(
                            BorderFactory.createMatteBorder(2,0,0,0, Color.black),
                            BorderFactory.createMatteBorder(0,0,1,1, Color.gray)));
                    board[i][j + 2].setBorder(new CompoundBorder(
                            BorderFactory.createMatteBorder(2,0,0,2, Color.black),
                            BorderFactory.createMatteBorder(0,0,1,0, Color.gray)));
                    board[i+1][j].setBorder(new CompoundBorder(
                            BorderFactory.createMatteBorder(0,2,0,0, Color.black),
                            BorderFactory.createMatteBorder(0,0,0,1, Color.gray)));
                    board[i+2][j].setBorder(new CompoundBorder(
                            BorderFactory.createMatteBorder(0,2,2,0, Color.black),
                            BorderFactory.createMatteBorder(1,0,0,1, Color.gray)));
                    board[i+1][j+1].setBorder(BorderFactory.createMatteBorder(0,0,0,1, Color.gray));
                    board[i+1][j+2].setBorder(new CompoundBorder(
                            BorderFactory.createMatteBorder(0,0,0,2, Color.black),
                            BorderFactory.createMatteBorder(0,0,0,0, Color.gray)));
                    board[i+2][j+1].setBorder(new CompoundBorder(
                            BorderFactory.createMatteBorder(0,0,2,0, Color.black),
                            BorderFactory.createMatteBorder(1,0,0,1, Color.gray)));
                    board[i+2][j+2].setBorder(new CompoundBorder(
                            BorderFactory.createMatteBorder(0,0,2,2, Color.black),
                            BorderFactory.createMatteBorder(1,0,0,0, Color.gray)));
		}
        //boardTable.setPreferredSize(new Dimension(600,600));
        //boardTable.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

//      top menu        
        JPanel topMenu = new JPanel();
        topMenu.setPreferredSize(new Dimension(this.getWidth(), (int)this.getHeight()*6/100));
        topMenu.setBackground(Color.WHITE);
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.RIGHT);
        flowLayout.setHgap(20);
        topMenu.setLayout(flowLayout);
        
        //  pause button
        ImageIcon p1Icon = new ImageIcon(this.getClass().getResource("/img/pause-icon-WHITE.png"));
                    Image image1 = p1Icon.getImage().getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH);
                    p1Icon = new ImageIcon(image1);
        ImageIcon pIcon = new ImageIcon(this.getClass().getResource("/img/pause-icon.png"));
                     image1 = pIcon.getImage().getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH);
                    pIcon = new ImageIcon(image1);
            JButton pauseButton1 = new JButton(p1Icon);
            JButton pauseButton = new JButton(pIcon);
                    pauseButton.setBorder(BorderFactory.createEmptyBorder());
                    pauseButton.setPreferredSize(new Dimension(40,40));
                    pauseButton.setBackground(null);
                    pauseButton.addActionListener((ActionEvent e)->{
                        pause();
                    });
                    pauseButton.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseEntered(java.awt.event.MouseEvent evt) {
                            pauseButton1.setVisible(true);
                            pauseButton.setVisible(false);
                        }

                        public void mouseExited(java.awt.event.MouseEvent evt) {
                            
                        }
                    });
                    
        
                    
            
                    pauseButton1.setBorder(BorderFactory.createEmptyBorder());
                    pauseButton1.setPreferredSize(new Dimension(40,40));
                    pauseButton1.setBackground(null);
                    pauseButton1.setVisible(false);
                    pauseButton1.addActionListener((ActionEvent e)->{
                        pause();
                    });
                    pauseButton1.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseEntered(java.awt.event.MouseEvent evt) {

                        }

                        public void mouseExited(java.awt.event.MouseEvent evt) {
                            pauseButton1.setVisible(false);
                            pauseButton.setVisible(true);
                        }
                    });
            
        //  new game
            JButton newGameButton = new JButton("Tạo trò chơi mới");
                    newGameButton.setBackground(null);
                    newGameButton.setFont(new Font("UTM Micra", 1, 15));
                    newGameButton.setForeground(colorEditable);
                    newGameButton.setBorder(BorderFactory.createEmptyBorder());
                    newGameButton.setPreferredSize(new Dimension(150,40));
                    newGameButton.addActionListener((ActionEvent e )->{
                        
                        this.setEnabled(false);
                        this.timer.stop();
                        
                        gameLevelTab(0);
                    });
                    newGameButton.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseEntered(java.awt.event.MouseEvent evt) {
                            newGameButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                            newGameButton.setForeground(Color.WHITE);
                            newGameButton.setBackground(colorEditable);

                        }

                        public void mouseExited(java.awt.event.MouseEvent evt) {
                            newGameButton.setBackground(Color.WHITE);
                            newGameButton.setForeground(colorEditable);
                        }
                    });
        
            
                    breakbutton.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, colorEditable));
                    breakbutton.setPreferredSize(new Dimension(5,20));
                    breakbutton.setBackground(null);
                    breakbutton.setVisible(errorHint.isVisible());
                  
            
                    
        topMenu.add(errorHint);
        topMenu.add(breakbutton);
        topMenu.add(newGameButton);
        topMenu.add(pauseButton);
        topMenu.add(pauseButton1);
        
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(new Dimension(this.getWidth(), (int)this.getHeight()*3/100));
        bottomPanel.setBackground(Color.WHITE);
        
        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension((int)this.getWidth()*2/100, this.getHeight()));
        leftPanel.setBackground(Color.WHITE);

//      right menu       
        JPanel rightMenu = new JPanel();
        rightMenu.setPreferredSize(new Dimension((int)this.getWidth()*40/100, this.getHeight()));
        rightMenu.setBackground(Color.WHITE);
        BoxLayout boxlayout = new BoxLayout(rightMenu, BoxLayout.Y_AXIS);
        rightMenu.setLayout(boxlayout);
        /*rightMenu.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                // do stuff
                rightMenu.setPreferredSize(new Dimension((int)this.getWidth()*40/100, this.getHeight()));
            }
        });*/
        
    //      detail box
            detailBox = new JPanel();
            detailBox.setBackground(Color.WHITE);
            detailBox.setPreferredSize(new Dimension(200,80));
            BoxLayout boxlayout2 = new BoxLayout(detailBox, BoxLayout.Y_AXIS);
            
            /*Box.Filler glue = (Box.Filler) Box.createVerticalGlue();
            glue.changeShape(glue.getMinimumSize(), 
                    new Dimension(0, 0), // make glue greedy
                    glue.getMaximumSize());*/
            
                int tempH = 70;
                if (this.game.getLevel().equals("Dễ") || this.game.getLevel().equals("Khó")) tempH = 45;
                
                JPanel dB = new JPanel();
                dB.setBackground(Color.WHITE);
                FlowLayout f = new FlowLayout();
                f.setAlignment(FlowLayout.CENTER);
                f.setHgap(tempH);
                dB.setLayout(f);
                
                JPanel dB2 = new JPanel();
                dB2.setBackground(Color.WHITE);
                 FlowLayout f2 = new FlowLayout();
                f2.setAlignment(FlowLayout.CENTER);
                f2.setHgap(70);
                dB2.setLayout(f2);
            
            
            //mistake
                JLabel mLabel = new JLabel("Lỗi sai");
                mDetail.setText(this.mistakeCount+"/"+this.mistakeMax);
            
            // level
                JLabel lLabel = new JLabel("Chế độ");
                JLabel lDetail = new JLabel(this.game.getLevel());
            
            //Timer
                JLabel tLabel = new JLabel("Thời gian");
                
                detailTabUI(mLabel);
                detailTabUI(lLabel);
                detailTabUI(tLabel);
                detailTabUI(mDetail);
                detailTabUI(lDetail);
                detailTabUI(tDetail);
                
                if (mistakeOn) dB.add(mLabel);
                dB.add(lLabel);
                dB.add(tLabel);
                
                if (mistakeOn) dB2.add(mDetail);
                dB2.add(lDetail);
                dB2.add(tDetail);
            
            detailBox.add(dB);
            detailBox.add(dB2);
            //detailBox.add(glue);
        
    //      button box
            JPanel buttonBox = new JPanel();
            JPanel buttonBox2 = new JPanel();
            buttonBox2.setBackground(Color.WHITE);
            buttonBox.setBackground(Color.WHITE);
            //BoxLayout boxlayout2 = new BoxLayout(buttonBox, BoxLayout.Y_AXIS);
            //buttonBox.setLayout(new GridLayout(4,1 ,0,20));
            
        //      utility button
                JPanel utilityButton = new JPanel();
                utilityButton.setBackground(Color.WHITE);
                FlowLayout f3 = new FlowLayout();
                f3.setHgap(30);
                utilityButton.setLayout(f3);
        
            //      hint button
                    ImageIcon hIcon = new ImageIcon(this.getClass().getResource("/img/hint-icon.png"));
                    Image image = hIcon.getImage().getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH);
                    hIcon = new ImageIcon(image);
                    
                    JButton hintButton = new JButton(hIcon);
                    if (hintCount==3) hintButton.setEnabled(false);
                    hintButton.setText("<html>"+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Gợi ý" + "<br/>" + "(còn " + (this.hintMax - this.hintCount) + " lượt)" +"</html>");
                    hintButton.setIconTextGap(10);
                    hintButton.setHorizontalTextPosition(SwingConstants.CENTER);
                    //hintButton.setVerticalAlignment(SwingConstants.CENTER);
                    hintButton.setVerticalTextPosition(SwingConstants.BOTTOM);
                    utilityUI(hintButton);
                    hintButton.addActionListener(this);
                    hintButton.addActionListener((ActionEvent e) ->{
                        if (this.I !=-1 && this.J!=-1 && this.gameBoard[I][J]>=10){
                            if (tempBoard[I][J]!=gameBoard[I][J]/10){
                                
                                numCount[gameBoard[I][J]/10-1]--;
                                System.out.println("tru");
                                if (numCount[gameBoard[I][J]/10-1]==0)
                                    numTab[gameBoard[I][J]/10-1].setVisible(false);
                            }
                            this.hintCount++;
                            hintButton.setText("<html>"+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Gợi ý" + "<br/>" + "(còn " + (this.hintMax - this.hintCount) + " lượt)" +"</html>");
                            this.tempBoard[this.I][this.J]=this.gameBoard[this.I][this.J]/10;
                            this.board[I][J].setForeground(Color.BLACK);
                            board[I][J].setFont(new Font("UTM Micra", 1, 30));
                            this.board[I][J].setText(String.valueOf(this.tempBoard[I][J]));
                            if (this.hintMax - this.hintCount == 0){
                               hintButton.setEnabled(false);
                            }
                            
                            System.out.println(gameBoard[I][J]);
                            this.board[I][J].removeKeyListener(this);
                            
                            this.gameBoard[this.I][this.J]=this.gameBoard[this.I][this.J]/10;
                            
                            //gameBoard[I][J]=gameBoard[this.I][this.J]/10;
                        }
                    });

                //  note Button
                    ImageIcon nIcon = new ImageIcon(this.getClass().getResource("/img/pencil-icon.png"));
                    image = nIcon.getImage().getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH);
                    nIcon = new ImageIcon(image);
                    
                    JButton noteButton = new JButton(nIcon);
                    noteButton.setText("<html>"+"Ghi chú:"+"<br/>"+"&nbsp;&nbsp;&nbsp;TẮT"+"</html>");
                    utilityUI(noteButton);
                    //noteButton.setBounds(0, 0, 20, 30);
                    noteButton.setIconTextGap(10);
                    noteButton.setHorizontalTextPosition(SwingConstants.CENTER);
                    noteButton.setVerticalTextPosition(SwingConstants.BOTTOM);
                    noteButton.addActionListener((ActionEvent e) ->{
                        if (this.useNote){
                            this.useNote=false;
                            noteButton.setText("<html>"+"Ghi chú:"+"<br/>"+"&nbsp;&nbsp;&nbsp;TẮT"+"</html>");
                        }
                        else{
                            this.useNote=true;
                            noteButton.setText("<html>"+"Ghi chú:"+"<br/>"+"&nbsp;&nbsp;&nbsp;BẬT"+"</html>");
                        }
                    });
                    noteButton.addActionListener(this);
                    noteButton.addKeyListener(this);

                //eraser button
                    ImageIcon eIcon = new ImageIcon(this.getClass().getResource("/img/eraser-icon.png"));
                    image = eIcon.getImage().getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH);
                    eIcon = new ImageIcon(image);
                    
                    JButton eraserButton = new JButton(eIcon);
                    utilityUI(eraserButton);
                    eraserButton.setText("<html>"+"&nbsp;&nbsp;&nbsp;&nbsp;Xóa&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"<br/>"+"&nbsp;"+"</html>");
                    eraserButton.setIconTextGap(10);
                    eraserButton.setHorizontalTextPosition(SwingConstants.CENTER);
                    eraserButton.setVerticalTextPosition(SwingConstants.BOTTOM);
                    eraserButton.addActionListener(this);
                    eraserButton.addActionListener((ActionEvent e) ->{
                        if (I!=-1 && J!=-1){
                            if (this.board[I][J].getForeground()!=Color.BLACK){
                                this.board[this.I][this.J].setText(" ");
                                if (this.tempBoard[I][J]!=0){
                                    if (this.numCount[this.tempBoard[this.I][this.J]-1]==0){
                                        this.numTab[this.tempBoard[this.I][this.J]-1].setVisible(true);
                                    }
                                    this.numCount[this.tempBoard[this.I][this.J]-1]++;
                                    this.tempBoard[this.I][this.J] = 0;}
                                    this.board[this.I][this.J].setForeground(colorEditable);
                                    
                                if (board[I][J].getForeground()!=colorNote){
                                    note[I][J]= new int[]{0,0,0,0,0,0,0,0,};
                                }
                            }}
                    });
                    eraserButton.addKeyListener(this);
                
                // check button
                    JButton checkButton = new JButton("Kiểm tra kết quả");
                            buttonUI(checkButton);
                            gpsHover(checkButton);
                            checkButton.addActionListener((ActionEvent e)->{
                                checkResult();
                                //gameOver(2);
                            });
                
                // solve button
                    JButton solveButton = new JButton("Giải");
                            buttonUI(solveButton);
                            gpsHover(solveButton);
                            solveButton.addActionListener((ActionEvent e)->{
                                gameOver(3);
                            });
                            
                
                //      autocheck button
                    JButton autoCheckButton = new JButton();
                            buttonUI(autoCheckButton);
                            gpsHover(autoCheckButton);
                    
                    if (this.autoCheck == false){
                        autoCheckButton.setText("Tự động kiểm tra kết quả: TẮT");
                    }
                    else autoCheckButton.setText("Tự động kiểm tra kết quả: BẬT");
                    autoCheckButton.addActionListener((ActionEvent e) -> {
                        if (this.autoCheck==false){
                            this.autoCheck=true;
                            autoCheckButton.setText("Tự động kiểm tra kết quả: BẬT");
                        }
                        else{
                            this.autoCheck=false;
                            autoCheckButton.setText("Tự động kiểm tra kết quả: TẮT");
                            //
                            /*
                            this.getContentPane().remove(container);
                            this.initGame();*/
                        }
                    });
                 
                utilityButton.add(eraserButton);
                utilityButton.add(noteButton);
                utilityButton.add(hintButton);
                
            //buttonBox.add(utilityButton);
            buttonBox.add(autoCheckButton);
            buttonBox.add(checkButton);
            buttonBox.add(solveButton);
            
            buttonBox.setPreferredSize(new Dimension(320,200));
            
            buttonBox2.add(buttonBox);
            
    //      num box
            JPanel numBox = new JPanel();
            numBox.setPreferredSize(new Dimension(200,350));
            numBox.setBackground(Color.WHITE);
            
            JPanel numBox2 = new JPanel();
            numBox2.setBackground(Color.WHITE);
            numBox2.setLayout(new GridLayout(3,3,7,7));
            for (int i=0; i<9; i++){
                numTab[i] = new JButton((i+1)+"");
                //numTab[i].setFocusable(false);
                if (numCount[i]<=0){
                    numTab[i].setVisible(false);
                }
                else{
                    numTab[i].addActionListener(this);
                    numTab[i].addKeyListener(this);
                    numTab[i].setActionCommand(i+"");
                    numTab[i].addActionListener((ActionEvent e) ->{
                        this.tNum = Integer.parseInt(e.getActionCommand());
                    });
                }
                keyPadUI(numTab[i]);
                numBox2.add(numTab[i]);
            }
            numBox2.setPreferredSize(new Dimension(320,320));
            //numBox2.setLocation(300,800);
            
            numBox.add(numBox2);
        
        rightMenu.add(detailBox);
        rightMenu.add(numBox);
        rightMenu.add(utilityButton);
        rightMenu.add(buttonBox2);
    
        container.add(boardTable, BorderLayout.CENTER);
        container.add(leftPanel, BorderLayout.WEST);
        container.add(rightMenu, BorderLayout.EAST);
        container.add(topMenu, BorderLayout.NORTH);
        container.add(bottomPanel, BorderLayout.SOUTH);
        
        if (!isPastGame){
            tDetail.setText("00:00");
            Timer();
            this.timer.start();}
        
        pastGame=false;
        con=false;
    }
    
    private void gameOver(int result){
        
        setDimmed(true);
        
        //1 = lose
        //2 = win
        //3 = solve
        
        //this.getFocusOwner().setFocusable(false);
        
        haveData = 2;
        this.timer.stop();
        
        JDialog resultMenu = new JDialog();
        JLabel status = new JLabel();
        JLabel line = new JLabel();
        JButton newGame = new JButton();
        JButton restart = new JButton();
        JButton home = new JButton();
        int panelW=0, panelH=0;
        
        if (result==1){
            panelW = 450;
            panelH = 300;
            status.setText("Bạn đã phạm 3 lỗi và thua trò chơi");
            newGame.setBounds(20, 165, panelW-40,60);
            restart.setBounds(20, 110, panelW-40,60);
            home.setBounds(20, 220, panelW-40,60);
        }
        else{
            panelW = 600;
            panelH = 800;
            newGame.setBounds(20, 670, panelW-40,60);
            //restart.setBounds(20, 110, panelW-40,50);
            home.setBounds(20, 730, panelW-40,60);
            JPanel boardTable = new JPanel();
                boardTable.setBackground(Color.WHITE);
                boardTable.setLayout(new GridLayout(9, 9));
                    for (int i = 0; i < 9; i++)
                        for (int j = 0; j < 9; j++) {
                            board[i][j].removeActionListener(this);
                            board[i][j].removeKeyListener(this);
                            
                            board[i][j].setFocusPainted(false);
                            board[i][j].setBackground(Color.white);
                            board[i][j].setFont(new Font("UTM Micra", 1, 30));
                            if (gameBoard[i][j]>=10){
                                board[i][j].setForeground(colorEditable);
                                board[i][j].setText(gameBoard[i][j]/10+"");
                            }
                            else{
                                board[i][j].setForeground(Color.BLACK);
                                board[i][j].setText(gameBoard[i][j]+"");
                            }
                            
                            boardTable.add(board[i][j]);
                        }
                boardTable.setBounds(25, 100, 550, 550);
            resultMenu.add(boardTable);
                    
            if (result==2){
                panelW = 600;
                panelH = 850;
                status.setText("Chúc mừng, bạn đã thắng trò chơi");
                boardTable.setBounds(25, 160, 550, 550);
                if (mistakeOn)
                    detailBox.setBounds(25, 85, 550, 550);
                else detailBox.setBounds(70, 85, 400, 550);
                newGame.setBounds(20, 720, panelW-40,60);
                //restart.setBounds(20, 110, panelW-40,50);
                home.setBounds(20, 780, panelW-40,60);
                
                resultMenu.add(detailBox);
                
                int level =0;
                switch(game.getLevel()){
                    case "Trung bình":
                        level = 1;
                        break;
                    case "Khó":
                        level=2;
                        break;
                }
                //System.out.print("\n"+minute+" ");
                //System.out.print(second+"\n");
                    System.out.println(checkM+" "+mistakeCount);
                    
                    if (mistakeCount==0 && !checkM) winWithNoMistake[level]++;
                    if (gameWon[level]==0){
                        bestMin[level]=minute;
                        bestSecond[level]=second;
                    }
                    else{
                        if (bestMin[level]>=minute){
                            if(bestMin[level]>minute){
                                bestMin[level]=minute;
                                bestSecond[level]=second;
                            }
                            else{
                                if (bestSecond[level]>second){
                                    bestMin[level]=minute;
                                    bestSecond[level]=second;
                                }
                            }
                        }
                    }
                    gameWon[level]++;
                    //System.out.print("\n"+bestMin[level]+" "+bestSecond[level]+"\n");
                
            }
            else{
                status.setText("Trò chơi đã được giải tự động");
            }
            
        }
        
        status.setFont(new Font(fontName, Font.CENTER_BASELINE, 20));
        Font font1 = status.getFont();
        status.setBounds((panelW-(int)font1.getStringBounds(status.getText(), frc).getWidth())/2,40,450,60);
        status.setForeground(colorEditable);
        
        line.setText("Trò chơi kết thúc");
        line.setFont(new Font(fontName, Font.CENTER_BASELINE, 25));
        Font font2 = line.getFont();
        line.setBounds((panelW-(int)font2.getStringBounds("Trò chơi kết thúc", frc).getWidth())/2,20,450,40);
        line.setForeground(colorEditable);
        
        optionButtonUI(newGame);
        newGame.setText("Tạo màn chơi mới");
        optionButtonUI(newGame);
        newGame.setPreferredSize(new Dimension(panelW-40, 60));
        pHover(newGame);
        newGame.addActionListener((ActionEvent e) ->{
            
            setDimmed(false);
            gameLevelTab(result);
            resultMenu.dispose();
        });
        
        optionButtonUI(restart);
        restart.setText("Chơi lại màn chơi");
        pHover(restart);
        restart.setPreferredSize(new Dimension(panelW-40, 60));
        
        restart.addActionListener((ActionEvent e) ->{
            
            
            reset=true;
            this.setEnabled(true);
            this.getContentPane().remove(container);
            this.initGame();
            
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(Graphics.class.getName()).log(Level.SEVERE, null, ex);
            }
            setDimmed(false);
            resultMenu.dispose();
        });
        
        
        
                optionButtonUI(home);
                home.setText("Quay về trang chủ");
                home.setPreferredSize(new Dimension(panelW-40, 60));
                pHover(home);
                home.addActionListener((ActionEvent e) ->{
                    setDimmed(false);
                    this.setEnabled(true);
                    this.getContentPane().remove(container);
                    
                    menu();
                    this.setSize(new Dimension(600, 780));
                    this.setLocationRelativeTo(null);
                    resultMenu.dispose();
                });

        resultMenu.setLayout(null);
        resultMenu.setSize(new Dimension(panelW, panelH));
        resultMenu.setUndecorated(true);
        resultMenu.setLocationRelativeTo(null);
        resultMenu.setVisible(true);
        //resultMenu.setAlwaysOnTop(true);
        resultMenu.getContentPane().setBackground(Color.WHITE);
        
        resultMenu.add(line);
        resultMenu.add(status);
        if (result == 1) resultMenu.add(restart);
        resultMenu.add(newGame);
        resultMenu.add(home);
        
        
        this.setEnabled(false);
        //setDimmed(this,true);
        
    }
    
    private void gameLevelTab(int over){
        setDimmed(true);
        JDialog levelTab = new JDialog();
                levelTab.setLayout(null);
                levelTab.setSize(new Dimension(200, 400));
                levelTab.setUndecorated(true);
                levelTab.setLocationRelativeTo(null);
                levelTab.setVisible(true);
                //levelTab.setAlwaysOnTop(true);
                levelTab.getContentPane().setBackground(Color.WHITE);
        
        JLabel s = new JLabel("Chọn chế độ");        
               s.setFont(new Font(fontName, Font.CENTER_BASELINE, 25));
               s.setForeground(colorEditable);
               Font font = s.getFont();
               s.setBounds((200-(int)font.getStringBounds("Chọn chế độ", frc).getWidth())/2,20,200,40);
        JButton easy = new JButton();
                easy.setText("Dễ");
                optionButtonUI(easy);
                pHover(easy);
                easy.setBounds(20, 70, 160, 55);
                easy.addActionListener((ActionEvent e) ->{
                    
                    this.game.setLevel(0);
                    
                    this.setEnabled(true);
                    this.getContentPane().remove(container);
                    this.getContentPane().remove(menu);
                    menu = new JPanel();
                    this.initGame();
                    setDimmed(false);
                    levelTab.dispose();
                });
        JButton medium = new JButton();
                medium.setText("Trung Bình");
                optionButtonUI(medium);
                pHover(medium);
                medium.setBounds(20, 130, 160, 55);
                medium.addActionListener((ActionEvent e) ->{
                    
                    this.game.setLevel(1);
                    
                    this.setEnabled(true);
                    this.getContentPane().remove(container);
                    this.getContentPane().remove(menu);
                    this.initGame();
                    
                    setDimmed(false);
                    levelTab.dispose();
                    
                });        
        JButton hard = new JButton();
                hard.setText("Khó");
                optionButtonUI(hard);
                pHover(hard);
                hard.setBounds(20, 190, 160, 55);
                hard.addActionListener((ActionEvent e) ->{
                    
                    this.game.setLevel(2);
                    
                    this.setEnabled(true);
                    this.getContentPane().remove(container);
                    this.getContentPane().remove(menu);
                    this.initGame();
                    setDimmed(false);
                    levelTab.dispose();
                    
                }); 
        JButton custom = new JButton();
                custom.setText("Tùy chỉnh");
                optionButtonUI(custom);
                pHover(custom);
                custom.setBounds(20, 250, 160, 55);
                custom.addActionListener((ActionEvent e) ->{
                    setDimmed(false);
                    //this.game.setLevel(3);
                    
                    this.setEnabled(true);
                    this.getContentPane().remove(container);
                    this.getContentPane().remove(menu);
                    
                    customBoard(0);
                    this.revalidate();
                    levelTab.dispose();
                    
                });
        if (this.game.getLevel().equals("Tùy chỉnh")){
            
        }
        
        JButton lineB = new JButton();
                lineB.setBackground(null);
                lineB.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, colorEditable));
                lineB.setPreferredSize(new Dimension(160,10));
                lineB.setBounds(20,315,160,10);
                
        JButton cancel = new JButton();
                cancel.setText("Hủy");
                optionButtonUI(cancel);
                cancel.setBackground(Color.BLACK);
                cancel.setBounds(20, 330, 160, 55);
                cancel.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        cancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        cancel.setForeground(Color.BLACK);
                        cancel.setOpaque(false);
                        //button.setBorder(BorderFactory.createLineBorder(colorEditable));
                        cancel.setBorder(new RoundedBorder(25,true,Color.WHITE));

                    }

                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        cancel.setBackground(Color.BLACK);
                        cancel.setForeground(Color.WHITE);
                        cancel.setOpaque(true);
                    }
                });
                cancel.addActionListener((ActionEvent e) ->{
                    
                setDimmed(false);
                    
                    if (over==0){
                        this.setEnabled(true);
                        this.timer.start();
                    }
                    else{
                        if (over==4){
                            this.setEnabled(true);
                        }   
                        else
                            gameOver(over);
                    }
                    
                    levelTab.dispose();
                });
        
        levelTab.add(s);
        levelTab.add(easy);
        levelTab.add(medium);
        levelTab.add(hard);
        levelTab.add(custom);
        levelTab.add(lineB);
        levelTab.add(cancel);
        /*if (over!=0){
            levelTab.add(cancel);
        }*/
        
    }
    
    private void customBoard(int state){
        
        customScreen = new JPanel();
        customScreen.setVisible(true);
        if (state==0)writeData();
        this.autoCheck = false;
        this.useNote = false;
        
        this.setMinimumSize(new Dimension(1150, 840));
        this.setLocationRelativeTo(null);
        customScreen.setBackground(Color.WHITE);
        customScreen.setLayout(new BorderLayout());
        this.add(customScreen);
        
        //int[][] temp = new int[9][9];
        //int[][] temp2 = new int[9][9];
        //JButton[][] tboard = new JButton[9][9];
        
        tempBoard = new int[9][9];
        gameBoard = new int[9][9];
        
        for (int i=0; i<9; i++){
                //.arraycopy(tempBoard[i], 0, temp[i], 0, 9);
                //System.arraycopy(gameBoard[i], 0, temp2[i], 0, 9);
                Arrays.fill(tempBoard[i], 0);
                Arrays.fill(gameBoard[i], 10);
            }
        
        game.setBoard(tempBoard, false);
        
        System.out.print("gb");
        for (int i=0; i<9; i++){
            for (int j=0; j<9; j++){
                System.out.print(gameBoard[i][j]+" ");
            }
        }
        
//      board
        JPanel boardTable = new JPanel();
        boardTable.setBackground(Color.WHITE);
        boardTable.setLayout(new GridLayout(9, 9));
            for (int i = 0; i < 9; i++)
		for (int j = 0; j < 9; j++) {
                    
                    board[i][j] = new JButton();
                   
                    //board[i][j].setFocusable(false);
                    board[i][j].addKeyListener(this);
                    board[i][j].addActionListener(this);
                    board[i][j].setActionCommand(i + " " + j);
                        board[i][j].addActionListener((ActionEvent e) ->{
                            String s = e.getActionCommand();
                            int k = s.indexOf(32);
                            this.I = Integer.parseInt(s.substring(0, k));
                            this.J = Integer.parseInt(s.substring(k + 1, s.length()));
                            System.out.println(I + " " + J);
                        });
                                
                    board[i][j].setFocusPainted(false);
                    board[i][j].setBackground(Color.white);
                    board[i][j].setFont(new Font("UTM Micra", 1, 30));
                    board[i][j].setForeground(colorEditable);
                    
                    boardTable.add(board[i][j]);
                }
            
            
            for (int i = 0; i < 9; i += 3)
		for (int j = 0; j < 9; j += 3) {
                    board[i][j].setBorder(new CompoundBorder(
                            BorderFactory.createMatteBorder(2,2,0,0, Color.black),
                            BorderFactory.createMatteBorder(0,0,1,1, Color.gray)));
                    board[i][j + 1].setBorder(new CompoundBorder(
                            BorderFactory.createMatteBorder(2,0,0,0, Color.black),
                            BorderFactory.createMatteBorder(0,0,1,1, Color.gray)));
                    board[i][j + 2].setBorder(new CompoundBorder(
                            BorderFactory.createMatteBorder(2,0,0,2, Color.black),
                            BorderFactory.createMatteBorder(0,0,1,0, Color.gray)));
                    board[i+1][j].setBorder(new CompoundBorder(
                            BorderFactory.createMatteBorder(0,2,0,0, Color.black),
                            BorderFactory.createMatteBorder(0,0,0,1, Color.gray)));
                    board[i+2][j].setBorder(new CompoundBorder(
                            BorderFactory.createMatteBorder(0,2,2,0, Color.black),
                            BorderFactory.createMatteBorder(1,0,0,1, Color.gray)));
                    board[i+1][j+1].setBorder(BorderFactory.createMatteBorder(0,0,0,1, Color.gray));
                    board[i+1][j+2].setBorder(new CompoundBorder(
                            BorderFactory.createMatteBorder(0,0,0,2, Color.black),
                            BorderFactory.createMatteBorder(0,0,0,0, Color.gray)));
                    board[i+2][j+1].setBorder(new CompoundBorder(
                            BorderFactory.createMatteBorder(0,0,2,0, Color.black),
                            BorderFactory.createMatteBorder(1,0,0,1, Color.gray)));
                    board[i+2][j+2].setBorder(new CompoundBorder(
                            BorderFactory.createMatteBorder(0,0,2,2, Color.black),
                            BorderFactory.createMatteBorder(1,0,0,0, Color.gray)));
		}
        //boardTable.setPreferredSize(new Dimension(600,600));
        //boardTable.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

//      top menu        
        JPanel topMenu = new JPanel();
        topMenu.setPreferredSize(new Dimension(this.getWidth(), (int)this.getHeight()*6/100));
        topMenu.setBackground(Color.WHITE);
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(new Dimension(this.getWidth(), (int)this.getHeight()*3/100));
        bottomPanel.setBackground(Color.WHITE);
        
        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension((int)this.getWidth()*2/100, this.getHeight()));
        leftPanel.setBackground(Color.WHITE);

//      right menu       
        JPanel rightMenu = new JPanel();
        rightMenu.setPreferredSize(new Dimension((int)this.getWidth()*40/100, this.getHeight()));
        rightMenu.setBackground(Color.WHITE);
        BoxLayout boxlayout = new BoxLayout(rightMenu, BoxLayout.Y_AXIS);
        rightMenu.setLayout(boxlayout);
        
        JPanel detailBox = new JPanel();
            detailBox.setBackground(Color.WHITE);
            detailBox.setPreferredSize(new Dimension(200,70));
        
        
    //      button box
            JPanel buttonBox = new JPanel();
            JPanel buttonBox2 = new JPanel();
            buttonBox2.setBackground(Color.WHITE);
            buttonBox.setBackground(Color.WHITE);
            //BoxLayout boxlayout2 = new BoxLayout(buttonBox, BoxLayout.Y_AXIS);
            //buttonBox.setLayout(new GridLayout(4,1 ,0,20));
            
        //      utility button
                JPanel utilityButton = new JPanel();
                utilityButton.setBackground(Color.WHITE);
                FlowLayout f3 = new FlowLayout();
                f3.setHgap(20);
                utilityButton.setLayout(f3);

                //eraser button
                    ImageIcon eIcon = new ImageIcon(this.getClass().getResource("/img/eraser-icon.png"));
                    Image image = eIcon.getImage().getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH);
                    eIcon = new ImageIcon(image);
                    
                    JButton eraserButton = new JButton(eIcon);
                            utilityUI(eraserButton);
                            eraserButton.setText("Xóa");
                            eraserButton.setIconTextGap(10);
                            eraserButton.setHorizontalTextPosition(SwingConstants.CENTER);
                            eraserButton.setVerticalTextPosition(SwingConstants.BOTTOM);
                            eraserButton.addActionListener(this);
                            eraserButton.addActionListener((ActionEvent e) ->{
                                if (I!=-1 && J!=-1){
                                    if (this.gameBoard[I][J]>=10){
                                        this.board[this.I][this.J].setText(" ");
                                        if (this.tempBoard[I][J]!=0){
                                            if (this.numCount[this.tempBoard[this.I][this.J]-1]==0){
                                                this.numTab[this.tempBoard[this.I][this.J]-1].setVisible(true);
                                            }
                                            this.numCount[this.tempBoard[this.I][this.J]-1]++;
                                            this.tempBoard[this.I][this.J] = 0;}
                                            this.board[this.I][this.J].setForeground(colorEditable);
                                    }}
                            });
                            eraserButton.addKeyListener(this);
                
                // play button
                    JButton play = new JButton("Tạo màn chơi");
                            buttonUI(play);
                            play.addActionListener((ActionEvent e) ->{
                                if (this.game.setBoard(tempBoard, true)){
                                    this.game.setLevel(3);
                                    this.initGame();
                                    customScreen.setVisible(false);
                                    this.getContentPane().remove(customScreen);
                                    this.revalidate();
                                    this.game.printBoard();
                                }
                                else{
                                    errorCustom();
                                    
                                }
                            });
                            gpsHover(play);
                
                // menu button
                    JButton back = new JButton("Trở về trang chủ");
                            buttonUI(back);
                            back.addActionListener((ActionEvent e) ->{
                                this.getContentPane().remove(customScreen);
                                menu();
                                this.revalidate();
                                this.setSize(new Dimension(600, 780));
                                this.setLocationRelativeTo(null);
                                readData();
                                game.setBoard(gameBoard,false);
                            });
                            gpsHover(back);
                 
                utilityButton.add(eraserButton);
                
            //buttonBox.add(utilityButton);
            buttonBox.add(play);
            buttonBox.add(back);
            
            buttonBox.setPreferredSize(new Dimension(320,200));
            
            buttonBox2.add(buttonBox);
            
    //      num box
            JPanel numBox = new JPanel();
            numBox.setPreferredSize(new Dimension(200,350));
            numBox.setBackground(Color.WHITE);
            
            JPanel numBox2 = new JPanel();
            numBox2.setBackground(Color.WHITE);
            numBox2.setLayout(new GridLayout(3,3,7,7));
            Arrays.fill(numCount, 9);
            for (int i=0; i<9; i++){
                numTab[i] = new JButton((i+1)+"");
                //numTab[i].setFocusable(false);
                if (numCount[i]<=0){
                    numTab[i].setVisible(false);
                }
                else{
                    numTab[i].addActionListener(this);
                    numTab[i].addKeyListener(this);
                    numTab[i].setActionCommand(i+"");
                    numTab[i].addActionListener((ActionEvent e) ->{
                        this.tNum = Integer.parseInt(e.getActionCommand());
                    });
                }
                keyPadUI(numTab[i]);
                numBox2.add(numTab[i]);
            }
            numBox2.setPreferredSize(new Dimension(320,320));
            //numBox2.setLocation(300,800);
            
            numBox.add(numBox2);
        
        rightMenu.add(detailBox);
        rightMenu.add(numBox);
        rightMenu.add(utilityButton);
        rightMenu.add(buttonBox2);
    
        customScreen.add(boardTable, BorderLayout.CENTER);
        customScreen.add(leftPanel, BorderLayout.WEST);
        customScreen.add(rightMenu, BorderLayout.EAST);
        customScreen.add(topMenu, BorderLayout.NORTH);
        customScreen.add(bottomPanel, BorderLayout.SOUTH);
        
        I = -1; J=-1;
        
    }
    
    private void setting(){
        setDimmed(true);
        this.setEnabled(false);
        JDialog setting = new JDialog();
                setting.setLayout(null);
                setting.setSize(new Dimension(300, 290));
                setting.setUndecorated(true);
                setting.setLocationRelativeTo(null);
                setting.setVisible(true);
                setting.getContentPane().setBackground(Color.WHITE);
                
                JLabel title = new JLabel("Cài đặt");
                       title.setFont(new Font(fontName, Font.CENTER_BASELINE, 25));
                       title.setBounds((300-(int)title.getFont().getStringBounds("Cài đặt", frc).getWidth())/2,20,300,40);
                       title.setForeground(colorEditable);
        
                JCheckBox mCheck = new JCheckBox("Tính năng giới hạn lỗi sai");
                          mCheck.setBounds(20,70,300,40);
                          mCheck.setFont(new Font(fontName, Font.CENTER_BASELINE, 20));
                          mCheck.setBackground(null);
                          if (mistakeOn) mCheck.setSelected(true);
                          mCheck.addItemListener(new ItemListener() {
                            public void itemStateChanged(ItemEvent e) {
                                mistakeOn = !mistakeOn;
                                System.out.print(mistakeOn);
                            }
                        });
                        
                JLabel des = new JLabel("Giới hạn lỗi sai mỗi màn chơi là 3");
                       des.setFont(new Font(fontName, Font.CENTER_BASELINE, 15));
                       des.setForeground(Color.GRAY);
                       des.setBounds(30,110,250,20);
                       
                JCheckBox aCheck = new JCheckBox("Tự động kiểm tra kết quả");
                          aCheck.setBounds(20,145,300,40);
                          aCheck.setFont(new Font(fontName, Font.CENTER_BASELINE, 20));
                          aCheck.setBackground(null);
                          if (autoCheck) aCheck.setSelected(true);
                          aCheck.addItemListener(new ItemListener() {
                            public void itemStateChanged(ItemEvent e) {
                                autoCheck = !autoCheck;
                            }
                        });
                        
                
                JButton back = new JButton("Trở về trang chủ");
                        back.setBounds(50,190,200,70);
                        optionButtonUI(back);
                        pHover(back);
                        back.addActionListener((ActionEvent e)->{
                            setDimmed(false);
                            this.setEnabled(true);
                            setting.dispose();
                        });
                          
                setting.add(title);          
                setting.add(mCheck);
                setting.add(des);
                setting.add(aCheck);
                setting.add(back);
    }
    
    private void hd(){
        this.getContentPane().remove(menu);
        this.setMinimumSize(new Dimension(1000, 850));
        this.setLocationRelativeTo(null);
        
        hdScreen = new JPanel();
        this.add(hdScreen);
        hdScreen.setBackground(Color.WHITE);
        hdScreen.setLayout(new BorderLayout());
        
        JPanel cen = new JPanel();
               cen.setBackground(null);
               cen.setLayout(null);
               
               JButton mainmenu = new JButton("Trở về trang chủ");
                       optionButtonUI(mainmenu);
                       pHover(mainmenu);
                       mainmenu.setBounds(180,getHeight()-110, 360, 60);
                       mainmenu.setPreferredSize(new Dimension(150,50));
                       mainmenu.addActionListener((ActionEvent e) -> {
                           this.getContentPane().remove(hdScreen);
                           menu();
                           this.revalidate();
                           this.setSize(new Dimension(600, 780));
                           this.setLocationRelativeTo(null);
                       });
                       
               JLabel text = new JLabel("<html>"+"Trò chơi Sudoku bao gồm một bảng ô vuông 9x9 được chia thành 9 ô con 3x3. "
                                        + "Một số ô trong bảng Sudoku sẽ được điền trước, tạo thành các gợi ý để người chơi bắt đầu giải quyết câu đố"
                                        + "<br/><br/>"
                                        +"Nhiệm vụ của người chơi là điền các số từ 1 đến 9 vào các ô trống sao cho thỏa quy luật:"
                                        +"<br/>"
                                        +"&nbsp;&nbsp;1. Mỗi số chỉ xuất hiện một lần trên mỗi hàng, mỗi cột và mỗi ô con 3x3;"
                                        +"<br/>"
                                        +"&nbsp;&nbsp;2. Các ô ở mỗi hàng ngang phải có đủ các số từ 1 tới 9, không cần theo thứ tự;"
                                        +"<br/>"
                                        +"&nbsp;&nbsp;3. Các ô ở mỗi hàng dọc phải có đủ các số từ 1 tới 9, không cần theo thứ tự;"
                                        +"<br/>"
                                        +"&nbsp;&nbsp;4. Các ô con 3x3 phải có đủ các số từ 1 tới 9, không cần theo thứ tự; "
                                        +"</html>");
                          text.setBounds(40, -80, 600, getHeight());
                          text.setFont(new Font("Bahnschrift Light", Font.PLAIN, 20));
                          text.setBackground(null);
                          
               JLabel img = new JLabel();
                      img.setVisible(false);
                      
               /*JLabel img2 = new JLabel();
                      img2.setVisible(false);
                      
               JLabel img3 = new JLabel();
                      img3.setVisible(false);
                      
               JLabel img4 = new JLabel();
                      img4.setVisible(false);
                      
               JLabel img5 = new JLabel();
                      img5.setVisible(false);
                      
               JLabel img6 = new JLabel();
                      img6.setVisible(false);
                      
               JLabel img7 = new JLabel();
                      img7.setVisible(false);
                      
               JLabel img8 = new JLabel();
                      img8.setVisible(false);
                      
               JLabel img9 = new JLabel();
                      img9.setVisible(false);
                      
               JLabel img10 = new JLabel();
                      img10.setVisible(false);
                      
               JLabel img11 = new JLabel();
                      img11.setVisible(false);
                      
                JLabel img12 = new JLabel();
                      img12.setVisible(false);
                
                JLabel img13 = new JLabel();
                      img13.setVisible(false);*/
               
               cen.add(text);
               cen.add(img);
               /*
               cen.add(img2);
               cen.add(img3);
               cen.add(img4);
               cen.add(img5);
               cen.add(img6);
               cen.add(img7);
               cen.add(img8);
               cen.add(img9);
               cen.add(img10);
               cen.add(img11);
               cen.add(img12);
               cen.add(img13);*/
               cen.add(mainmenu);
               //cen.setComponentZOrder(img,0);
               
        JPanel left = new JPanel();
               left.setBackground(colorMedium);
               left.setPreferredSize(new Dimension(this.getWidth()*30/100, this.getHeight()));
               
               JLabel t = new JLabel("Hướng dẫn");
                      t.setFont(new Font("Bahnschrift Light", Font.BOLD, 35));
                      t.setPreferredSize(new Dimension(getWidth()*25/100,75));
                      t.setForeground(Color.WHITE);
               
               JButton gt = new JButton("Giới thiệu");
               JButton ng = new JButton("Tạo màn chơi ngẫu nhiên");
               JButton cg = new JButton("Tùy chỉnh màn chơi");
               JButton gp = new JButton("Hướng dẫn màn chơi");
               JButton ss = new JButton("Cài đặt và thông số");
               //
                       hdButton(gt,true);
                       hdHover(gt);
                       gt.addActionListener((ActionEvent e)->{
                           hdButton(gt,true);hdButton(ng,false);hdButton(cg,false);hdButton(gp,false);hdButton(ss,false);
                           
                           text.setText("<html>"+"Trò chơi Sudoku bao gồm một bảng ô vuông 9x9 được chia thành 9 ô con 3x3. "
                                        + "Một số ô trong bảng Sudoku sẽ được điền trước, tạo thành các gợi ý để người chơi bắt đầu giải quyết câu đố"
                                        + "<br/><br/>"
                                        +"Nhiệm vụ của người chơi là điền các số từ 1 đến 9 vào các ô trống sao cho thỏa quy luật:"
                                        +"<br/>"
                                        +"&nbsp;&nbsp;1. Mỗi số chỉ xuất hiện một lần trên mỗi hàng, mỗi cột và mỗi ô con 3x3;"
                                        +"<br/>"
                                        +"&nbsp;&nbsp;2. Các ô ở mỗi hàng ngang phải có đủ các số từ 1 tới 9, không cần theo thứ tự;"
                                        +"<br/>"
                                        +"&nbsp;&nbsp;3. Các ô ở mỗi hàng dọc phải có đủ các số từ 1 tới 9, không cần theo thứ tự;"
                                        +"<br/>"
                                        +"&nbsp;&nbsp;4. Các ô con 3x3 phải có đủ các số từ 1 tới 9, không cần theo thứ tự; "
                                        +"</html>");
                           text.setBounds(40, -80, 600, getHeight());
                           text.setFont(new Font("Bahnschrift Light", Font.PLAIN, 20));
                           
                           img.setVisible(false); /*img2.setVisible(false); img3.setVisible(false); img4.setVisible(false);
                           img5.setVisible(false); img6.setVisible(false); img7.setVisible(false); img8.setVisible(false);
                           img9.setVisible(false); img10.setVisible(false); img11.setVisible(false); 
                           img12.setVisible(false); img13.setVisible(false);*/
                       });
                       
               //
                       hdButton(ng,false);
                       hdHover(ng);
                       ng.addActionListener((ActionEvent e)->{
                           hdButton(gt,false); hdButton(ng,true); hdButton(cg,false); hdButton(gp,false); hdButton(ss,false);
                           /*
                           text.setText("<html>"+"Người chơi có thể bắt đầu màn chơi bằng cách chọn nút"
                                        + "<br/><br/><br/><br/><br/>"
                                        + "trên menu chính và chọn các chế độ chơi mong muốn (Dễ, Trung bình, Khó)"
                                        + "<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>"
                                        + "hoặc người chơi có thể tiếp tục màn chơi trước đó bằng cách chọn nút"+"</html>");
                           text.setBounds(40, -140, 600, getHeight());
                           text.setFont(new Font("Bahnschrift Light", Font.PLAIN, 20));*/
                           text.setText("");
                           
                           img.setIcon(new ImageIcon(this.getClass().getResource("/img/1.PNG")));
                           img.setBounds(20,-140,1000,1000);
                           img.setVisible(true);
                           
                           /*ImageIcon temp = new ImageIcon("src/img/level-tab.PNG");
                           img2.setIcon(new ImageIcon(temp.getImage().getScaledInstance(200, 300, java.awt.Image.SCALE_SMOOTH)));
                           img2.setBounds(220,180,200,350);
                           img2.setVisible(true);
                           
                           img3.setIcon(new ImageIcon("src/img/continue-button.png"));
                           img3.setBounds(150,560,350,100);
                           img3.setVisible(true);
                           
                           img4.setVisible(false); img5.setVisible(false); img6.setVisible(false); img7.setVisible(false); img8.setVisible(false);
                           img9.setVisible(false); img10.setVisible(false); img11.setVisible(false); img12.setVisible(false); img13.setVisible(false);
                           */
                       });
               
               //
                       hdButton(cg,false);
                       hdHover(cg);
                       cg.addActionListener((ActionEvent e)->{
                           hdButton(gt,false); hdButton(ng,false); hdButton(cg,true); hdButton(gp,false); hdButton(ss,false);
                           
                           text.setText("");
                           
                           img.setIcon(new ImageIcon(this.getClass().getResource("/img/2.PNG")));
                           img.setBounds(20,-140,1000,1000);
                           img.setVisible(true);
                           
                           /*text.setText("<html>"+"Người chơi có thể <b>tùy chỉnh một màn chơi</b> bằng cách chọn nút"
                                        + "<br/><br/><br/><br/>"
                                        + "trên menu chính và chọn chế độ chơi <b>Tùy chỉnh</b>"
                                        + "<br/><br/><br/>"
                                        + "Giao diện tùy chỉnh trò chơi sẽ được hiển thị,"
                                        +"<br/>"
                                        + " người dùng có thể nhấn chọn ô bất kì và <b>nhập</b>"
                                        +"<br/>"
                                        + "<b>giá trị</b> mong muốn bằng bàn phím hoặc sử dụng "
                                        +"<br/>"
                                        + "ô số trên giao diện"
                                        +"<br/><br/><br/>"
                                        +"Người dùng có thể <b>xóa giá trị</b> một ô bằng phím DEL hoặc BACKSPACE, hoặc có thể sử dụng nút"
                                        +"<br/><br/>"
                                        +"Sau khi nhập các giá trị mong muốn, nhấn nút"
                                        +"<br/><br/>"
                                        +"để <b>tạo màn chơi</b> từ bàn cờ đã nhập. "
                                        +"<br/><br/><br/>"
                                        + "Nếu dữ liệu nhập lỗi hoặc bàn cờ không thể giải,"
                                        +"<br/>"
                                        + " thông báo lỗi sẽ được hiện lên"
                                        +"<br/><br/><br/>"
                                        +"Người dùng có thể thực hiện <b>tạo mới lại bàn cờ trống</b> bằng cách chọn"
                                        +"<br/><br/><br/>"
                                        +"Người dùng có thể thực hiện <b>chỉnh sửa bàn cờ hiện tại</b> bằng cách chọn"
                                        +"</html>");
                           text.setBounds(40, -80, 630, getHeight());
                           text.setFont(new Font("Bahnschrift Light", Font.PLAIN, 17));
                           
                           ImgeIcon temp = new ImageIcon("src/img/new-game-button.png");
                           img.setIcon(new ImageIcon(temp.getImage().getScaledInstance(250, 50, java.awt.Image.SCALE_SMOOTH)));
                           img.setBounds(200,15,350,100);
                           img.setVisible(true);
                           
                           temp = new ImageIcon("src/img/custom-game-button.png");
                           img2.setIcon(new ImageIcon(temp.getImage().getScaledInstance(150, 50, java.awt.Image.SCALE_SMOOTH)));
                           img2.setBounds(420,90,150,50);
                           img2.setVisible(true);
                           
                           temp = new ImageIcon("src/img/numtab.png");
                           img3.setIcon(new ImageIcon(temp.getImage().getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH)));
                           img3.setBounds(450,80,300,300);
                           img3.setVisible(true);
                           
                           temp = new ImageIcon("src/img/delete-button.png");
                           img4.setIcon(new ImageIcon(temp.getImage().getScaledInstance(50, 60, java.awt.Image.SCALE_SMOOTH)));
                           img4.setBounds(160,320,60,70);
                           img4.setVisible(true);
                           
                           temp = new ImageIcon("src/img/new-game-button-1.png");
                           img5.setIcon(new ImageIcon(temp.getImage().getScaledInstance(250, 60, java.awt.Image.SCALE_SMOOTH)));
                           img5.setBounds(400,360,250,60);
                           img5.setVisible(true); 
                           
                           temp = new ImageIcon("src/img/error-custom-notification.png");
                           img6.setIcon(new ImageIcon(temp.getImage().getScaledInstance(250, 150, java.awt.Image.SCALE_SMOOTH)));
                           img6.setBounds(420,260,500,500);
                           img6.setVisible(true); 
                           
                           temp = new ImageIcon("src/img/new-error-custom.png");
                           img7.setIcon(new ImageIcon(temp.getImage().getScaledInstance(250, 50, java.awt.Image.SCALE_SMOOTH)));
                           img7.setBounds(200,385,500,500);
                           img7.setVisible(true); 
                           
                           temp = new ImageIcon("src/img/edit-error-custom.png");
                           img8.setIcon(new ImageIcon(temp.getImage().getScaledInstance(250, 50, java.awt.Image.SCALE_SMOOTH)));
                           img8.setBounds(200,455,500,500);
                           img8.setVisible(true); 
                           
                           img9.setVisible(false); img10.setVisible(false); img11.setVisible(false); img12.setVisible(false); img13.setVisible(false);
                           */
                           });
                       
               //
                       hdButton(gp,false);
                       hdHover(gp);
                       gp.addActionListener((ActionEvent e)->{
                           hdButton(gt,false);
                           hdButton(ng,false);
                           hdButton(cg,false);
                           hdButton(gp,true);
                           hdButton(ss,false);
                           
                           text.setText("");
                           
                           img.setIcon(new ImageIcon(this.getClass().getResource("/img/3.PNG")));
                           img.setBounds(10,-130,1000,1000);
                           img.setVisible(true);
                           
                           /*text.setText("<html>"+"Sau khi chọn chế độ chơi mong muốn, giao diện màn chơi sẽ được hiển thị."
                                        +"<br/><br/>"
                                        + "Người dùng có thể nhấn chọn ô bất kì và <b>nhập</b>"
                                        +"<br/>"
                                        + "<b>giá trị</b> mong muốn bằng bàn phím hoặc sử dụng "
                                        +"<br/>"
                                        + "ô số trên giao diện"
                                        +"<br/><br/>"
                                        +"Để <b>xóa giá trị</b> sử dụng phím DEL hoặc BACKSPACE, hoặc có thể sử dụng nút"
                                        +"<br/><br/>"
                                        +"Để sử dụng <b>gợi ý</b> (mỗi màn chơi có 3 gợi ý) chọn nút"
                                        +"<br/><br/>"
                                        +"Để bật/ tắt chức năng <b>ghi chú</b> chọn nút"
                                        +"<br/><br/>"
                                        +"Để <b>tạo màn chơi mới</b>, chọn"
                                        +"<br/><br/>"
                                        +"Người dùng có thể <b>tạm dừng màn chơi</b> bằng cách chọn"
                                        +"<br/><br/>"
                                        +"Giao diện tạm dừng sẽ được hiển thị. Ở giao diện tạm dừng:"
                                        +"<br/>"
                                        +"&nbsp;&nbsp;Để <b>chơi lại màn chơi</b>, chọn"
                                        +"<br/><br/>"
                                        +"&nbsp;&nbsp;Để <b>quay lại trang chủ</b>, chọn"
                                        +"<br/><br/>"
                                        +"&nbsp;&nbsp;Khi muốn <b>tiếp tục màn chơi</b>, chọn"
                                        +"<br/>"
                                        +"Người dùng có thể bật/ tắt <b>chức năng tự kiểm tra kết quả</b> ô số bằng cách chọn"
                                        +"<br/><br/><br/>"
                                        +"Người dùng có thể yêu cầu <b>giải bàn cờ</b> bằng cách chọn"
                                        +"<br/><br/>"
                                        +"Để <b>kiểm tra kết quả bàn cờ</b>, chọn"
                                        +"<br/><br/>"
                                        +"Sau khi chọn Kiểm tra kết quả, giao diện sẽ tô đỏ các ô không có giá trị, người dùng có thể <b>ẩn các ô lỗi</b> đó bằng cách chọn"
                                        +"</html>");
                           text.setBounds(40, -60, 630, getHeight());
                           text.setFont(new Font("Bahnschrift Light", Font.PLAIN, 17));
                           
                           ImageIcon temp = new ImageIcon(this.getClass().getResource("/img/numtab.PNG"));
                           img.setIcon(new ImageIcon(temp.getImage().getScaledInstance(120, 120, java.awt.Image.SCALE_SMOOTH)));
                           img.setBounds(430,-57,350,300);
                           img.setVisible(true);
                           
                           temp = new ImageIcon(this.getClass().getResource("/img/delete-button.PNG"));
                           img2.setIcon(new ImageIcon(temp.getImage().getScaledInstance(50, 60, java.awt.Image.SCALE_SMOOTH)));
                           img2.setBounds(620,130,150,60);
                           img2.setVisible(true);
                           
                           temp = new ImageIcon(this.getClass().getResource("/img/hint-icon.PNG"));
                           img3.setIcon(new ImageIcon(temp.getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
                           img3.setBounds(450,50,300,300);
                           img3.setVisible(true);
                           
                           temp = new ImageIcon(this.getClass().getResource("/img/pencil-icon.PNG"));
                           img4.setIcon(new ImageIcon(temp.getImage().getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
                           img4.setBounds(350,210,60,70);
                           img4.setVisible(true);
                           
                           temp = new ImageIcon(this.getClass().getResource("/img/new-ingame-buttonPNGpng"));
                           img5.setIcon(new ImageIcon(temp.getImage().getScaledInstance(250, 60, java.awt.Image.SCALE_SMOOTH)));
                           img5.setBounds(270,270,250,60);
                           img5.setVisible(true); 
                           
                           temp = new ImageIcon(this.getClass().getResource("/img/pause-icon.PNG"));
                           img6.setIcon(new ImageIcon(temp.getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
                           img6.setBounds(480,320,500,40);
                           img6.setVisible(true); 
                           
                           temp = new ImageIcon(this.getClass().getResource("/img/replay-button.PNG"));
                           img7.setIcon(new ImageIcon(temp.getImage().getScaledInstance(220, 35, java.awt.Image.SCALE_SMOOTH)));
                           img7.setBounds(270,395,500,50);
                           img7.setVisible(true); 
                           
                           temp = new ImageIcon(this.getClass().getResource("/img/menu-button.PNG"));
                           img8.setIcon(new ImageIcon(temp.getImage().getScaledInstance(220, 35, java.awt.Image.SCALE_SMOOTH)));
                           img8.setBounds(280,435,500,50);
                           img8.setVisible(true); 
                           
                           temp = new ImageIcon(this.getClass().getResource("/img/continue-game-button.PNG"));
                           img9.setIcon(new ImageIcon(temp.getImage().getScaledInstance(220, 35, java.awt.Image.SCALE_SMOOTH)));
                           img9.setBounds(320,475,500,50);
                           img9.setVisible(true); 
                           
                           temp = new ImageIcon(this.getClass().getResource("/img/auto-check-button.PNG"));
                           img10.setIcon(new ImageIcon(temp.getImage().getScaledInstance(270, 40, java.awt.Image.SCALE_SMOOTH)));
                           img10.setBounds(200,535,500,50);
                           img10.setVisible(true); 
                           
                           temp = new ImageIcon(this.getClass().getResource("/img/solve-button.PNG"));
                           img11.setIcon(new ImageIcon(temp.getImage().getScaledInstance(60, 30, java.awt.Image.SCALE_SMOOTH)));
                           img11.setBounds(470,575,500,50);
                           img11.setVisible(true); 
                           
                           temp = new ImageIcon(this.getClass().getResource("/img/check-button.PNG"));
                           img12.setIcon(new ImageIcon(temp.getImage().getScaledInstance(200, 40, java.awt.Image.SCALE_SMOOTH)));
                           img12.setBounds(330,620,500,50);
                           img12.setVisible(true);
                           
                           temp = new ImageIcon(this.getClass().getResource("/img/hide-error-button.PNG"));
                           img13.setIcon(new ImageIcon(temp.getImage().getScaledInstance(230, 50, java.awt.Image.SCALE_SMOOTH)));
                           img13.setBounds(350,465,500,500);
                           img13.setVisible(true);*/
                       });
                       
               //
                       hdButton(ss,false);
                       hdHover(ss);
                       ss.addActionListener((ActionEvent e)->{
                           hdButton(gt,false);
                           hdButton(ng,false);
                           hdButton(cg,false);
                           hdButton(gp,false);
                           hdButton(ss,true);
                           text.setText("");
                           
                           img.setIcon(new ImageIcon(this.getClass().getResource("/img/4.PNG")));
                           img.setBounds(10,-125,1000,1000);
                           img.setVisible(true);
                           /*
                           text.setText("<html>"
                                        +"Để mở giao diện <b>Cài đặt</b>, chọn"
                                        +"<br/><br/>"
                                        + "Ở giao diện cài đặt: "
                                        +"<br/><br/>"
                                        + "Người dùng có thể bật/ tắt tính năng <b>giới hạn lỗi sai</b>"
                                        +"<br/><br/><br/>"
                                        + "Người dùng có thể bật/ tắt tính năng <b>tự động kiểm tra kết quả</b>"
                                        +"<br/><br/><br/><br/>"
                                        +"Để mở giao diện <b>Thống kê thông số</b>, chọn"
                                        +"<br/><br/>"
                                        +"Người dùng có thể thực hiện xem các thông số ở các cấp độ khác nhau bằng cách chọn cấp độ mình muốn xem trên thanh cấp độ"
                                        +"</html>");
                           text.setBounds(40, -70, 630, getHeight());
                           text.setFont(new Font("Bahnschrift Light", Font.PLAIN, 20));
                           
                           img4.setVisible(false);img5.setVisible(false); img6.setVisible(false); img7.setVisible(false); img8.setVisible(false);
                           img9.setVisible(false); img10.setVisible(false); img11.setVisible(false); img12.setVisible(false); img13.setVisible(false);
                           */
                        });
                       
               left.add(t);
               left.add(gt);
               left.add(ng);
               left.add(cg);
               left.add(gp);
               left.add(ss);
               
        hdScreen.add(cen, BorderLayout.CENTER);
        hdScreen.add(left, BorderLayout.WEST);
    }
    
    private void statistic(){
        this.getContentPane().remove(menu);
        this.setMinimumSize(new Dimension(600, 750));
        this.setLocationRelativeTo(null);
        
        statisticScreen = new JPanel();
        this.add(statisticScreen);
        statisticScreen.setBackground(Color.WHITE);
        statisticScreen.setLayout(new BorderLayout());
        
        JButton gP = new JButton();
        JButton gW = new JButton();
        JButton nM = new JButton();
        JButton time = new JButton();
        
        JButton easy = new JButton("Dễ");
        JButton medium = new JButton("Trung bình");
        JButton hard = new JButton("Khó");
        
        JPanel top = new JPanel();
               top.setBackground(null);
               top.setPreferredSize(new Dimension(this.getWidth(), (int)this.getHeight()*13/100));
               top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
               
               JLabel bO = new JLabel("a");
                      bO.setForeground(Color.WHITE);
                      
               JLabel detailTop = new JLabel("Thống kê thông số");
                      detailTop.setHorizontalAlignment(JLabel.CENTER);
                      detailTop.setHorizontalTextPosition(JLabel.CENTER);
                      detailTop.setAlignmentX(Component.CENTER_ALIGNMENT);
                      detailTop.setFont(new Font("Bahnschrift Light", Font.BOLD, 22));
                   
               JPanel buttonTop = new JPanel();
                      buttonTop.setBackground(null);
                      
                      FlowLayout flowLayout = new FlowLayout();
                          flowLayout.setAlignment(FlowLayout.CENTER);
                          flowLayout.setHgap(100);
                          flowLayout.setVgap(20);
                      buttonTop.setLayout(flowLayout);
               
                      //easy
                              statisticOptionButton(easy, true);
                              easy.addActionListener((ActionEvent e)->{
                                  statisticOptionButton(easy, true);
                                  statisticOptionButton(medium, false);
                                  statisticOptionButton(hard, false);
                                    String tS = "Số màn đã chơi:                                                           ";
                                    tS = tS.substring(0, tS.length()-(String.valueOf(totalGame[0]).length()*2));
                                    gP.setText(tS+totalGame[0]);
                                    
                                    tS = "Số màn thắng:                                                              ";
                                    tS = tS.substring(0, tS.length()-String.valueOf(gameWon[0]).length()*2);
                                    gW.setText(tS+gameWon[0]);
                                    
                                    tS = "Số màn thắng không lỗi:                                            ";
                                    tS = tS.substring(0, tS.length()-String.valueOf(winWithNoMistake[0]).length()*2);
                                    nM.setText(tS+winWithNoMistake[0]);
                                    
                                    if (gameWon[0]!=0) time.setText("Thời gian giải nhanh nhất:                                  "
                                                        + dFormat.format(bestMin[0])+":"+dFormat.format(bestSecond[0]));
                                    else time.setText("Thời gian giải nhanh nhất:                                      -");
                              });

                      //medium
                              statisticOptionButton(medium, false);
                              medium.addActionListener((ActionEvent e)->{
                                  statisticOptionButton(easy, false);
                                  statisticOptionButton(medium, true);
                                  statisticOptionButton(hard, false);
                                    String tS = "Số màn đã chơi:                                                           ";
                                    tS = tS.substring(0, tS.length()-String.valueOf(totalGame[1]).length()*2);
                                    gP.setText(tS+totalGame[1]);
                                    
                                    tS = "Số màn thắng:                                                              ";
                                    tS = tS.substring(0, tS.length()-String.valueOf(gameWon[1]).length()*2);
                                    gW.setText(tS+gameWon[1]);
                                    
                                    tS = "Số màn thắng không lỗi:                                            ";
                                    tS = tS.substring(0, tS.length()-String.valueOf(winWithNoMistake[1]).length()*2);
                                    nM.setText(tS+winWithNoMistake[1]);
                                    
                                    if (gameWon[1]!=0) time.setText("Thời gian giải nhanh nhất:                                  "
                                                        + dFormat.format(bestMin[1])+":"+dFormat.format(bestSecond[1]));
                                    else time.setText("Thời gian giải nhanh nhất:                                      -");
                                        });

                      //hard
                              statisticOptionButton(hard, false);
                              hard.addActionListener((ActionEvent e)->{
                                  statisticOptionButton(hard, true);
                                  statisticOptionButton(easy, false);
                                  statisticOptionButton(medium, false);
                                    String tS = "Số màn đã chơi:                                                           ";
                                    tS = tS.substring(0, tS.length()-String.valueOf(totalGame[2]).length()*2);
                                    gP.setText(tS+totalGame[2]);
                                    
                                    tS = "Số màn thắng:                                                              ";
                                    tS = tS.substring(0, tS.length()-String.valueOf(gameWon[2]).length()*2);
                                    gW.setText(tS+gameWon[2]);
                                    
                                    tS = "Số màn thắng không lỗi:                                            ";
                                    tS = tS.substring(0, tS.length()-String.valueOf(winWithNoMistake[2]).length()*2);
                                    nM.setText(tS+winWithNoMistake[2]);
                                    
                                    if (gameWon[2]!=0) time.setText("Thời gian giải nhanh nhất:                                  "
                                                        + dFormat.format(bestMin[2])+":"+dFormat.format(bestSecond[2]));
                                    else time.setText("Thời gian giải nhanh nhất:                                      -");
                                        });

                      buttonTop.add(easy);
                      buttonTop.add(medium);
                      buttonTop.add(hard);
               
               top.add(bO);       
               top.add(detailTop);
               top.add(buttonTop);
               
               
        JPanel cen = new JPanel();
               cen.setBackground(null);
               //BoxLayout boxlayout1 = new BoxLayout(cen, BoxLayout.Y_AXIS);
               
               JPanel content = new JPanel();
                      //content.setLayout(new GridLayout(7,1,20,10));
                      content.setPreferredSize(new Dimension(this.getWidth()-100, this.getHeight()));
                      content.setBackground(null);
                      content.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, colorNote));
               
               JLabel t1 = new JLabel("Màn chơi");
                      //t1.setHorizontalAlignment(JLabel.LEFT);
                      t1.setHorizontalTextPosition(JLabel.LEFT);
                      t1.setPreferredSize(new Dimension(this.getWidth()-100,40));
                      t1.setFont(new Font("Bahnschrift Light", Font.BOLD, 20));
               
               //gP
                       String tS = "Số màn đã chơi:                                                          ";
                       tS = tS.substring(0, tS.length()-String.valueOf(totalGame[0]).length()*2);
                       gP.setText(tS+totalGame[0]);
                       statisticButton(gP);
                       
                       
               
               //gW
                       tS = "Số màn thắng:                                                              ";
                       tS = tS.substring(0, tS.length()-String.valueOf(gameWon[0]).length()*2);
                       gW.setText(tS+gameWon[0]);
                       statisticButton(gW);
               
               //nM     
                       tS = "Số màn thắng không lỗi:                                            ";
                       tS = tS.substring(0, tS.length()-String.valueOf(winWithNoMistake[0]).length()*2);
                       nM.setText(tS+winWithNoMistake[0]);
                       statisticButton(nM);
                       
               JLabel t2 = new JLabel("Thời gian");
                      t2.setHorizontalTextPosition(JLabel.LEFT);
                      t2.setPreferredSize(new Dimension(this.getWidth()-100,40));
                      t2.setFont(new Font("Bahnschrift Light", Font.BOLD, 20));
               
               //time
                       if (gameWon[0]!=0) time.setText("Thời gian giải nhanh nhất:                                  "
                                                        + dFormat.format(bestMin[0])+":"+dFormat.format(bestSecond[0]));
                       else time.setText("Thời gian giải nhanh nhất:                                      -");
                       statisticButton(time);
                       
               JButton mainmenu = new JButton("Trở về trang chủ");
                       optionButtonUI(mainmenu);
                       pHover(mainmenu);
                       mainmenu.addActionListener((ActionEvent e) -> {
                           menu();
                           this.getContentPane().remove(statisticScreen);
                           this.revalidate();
                       });
                      
               content.add(t1);
               content.add(gP);
               content.add(gW);
               content.add(nM);
               content.add(t2);
               content.add(time);
               content.add(mainmenu);
               
               cen.add(content);
                       
        statisticScreen.add(top, BorderLayout.NORTH);
        statisticScreen.add(cen,BorderLayout.CENTER);
    }
    
    private void errorCustom(){
        setDimmed(true);
        System.out.println("error");
        this.setEnabled(false);
        JDialog error = new JDialog();
                error.setLayout(null);
                error.setSize(new Dimension(400, 300));
                error.setUndecorated(true);
                error.setLocationRelativeTo(null);
                error.setVisible(true);
                error.getContentPane().setBackground(Color.WHITE);
                                            
        JLabel title = new JLabel("Dữ liệu nhập không hợp lệ");
               title.setFont(new Font(fontName, Font.CENTER_BASELINE, 25));
               Font font2 = title.getFont();
               title.setBounds((400-(int)font2.getStringBounds(title.getText(), frc).getWidth())/2,30,400,40);
               title.setForeground(colorEditable);
               
        JButton edit = new JButton("Chỉnh sửa dữ liệu");
                optionButtonUI(edit);
                pHover(edit);
                edit.setPreferredSize(new Dimension(400-40, 60));
                edit.setBounds(20, 80, 400-40,60);
                edit.addActionListener((ActionEvent e) ->{
                    this.setEnabled(true);
                    error.dispose();
                    setDimmed(false);
                });
                
        JButton newG = new JButton("Tạo lại màn chơi mới");
                optionButtonUI(newG);
                pHover(newG);
                newG.setPreferredSize(new Dimension(400-40, 60));
                newG.setBounds(20, 140, 400-40,60);
                newG.addActionListener((ActionEvent e) ->{
                    for (int i=0; i<9; i++){
                        Arrays.fill(tempBoard[i], 0);
                    }
                    this.game.setBoard(tempBoard,false);
                    this.getContentPane().remove(customScreen);
                    this.setEnabled(true);
                    customBoard(1);
                    this.revalidate();
                    error.dispose();
                    setDimmed(false);
                });        
                
        JButton menuB = new JButton("Trở về trang chủ");
                optionButtonUI(menuB);
                pHover(menuB);
                menuB.setPreferredSize(new Dimension(400-40, 60));
                menuB.setBounds(20, 200, 400-40,60);
                menuB.addActionListener((ActionEvent e) ->{
                    this.setEnabled(true);
                    this.getContentPane().remove(customScreen);
                    menu();
                    this.revalidate();
                    this.setSize(new Dimension(600, 780));
                    this.setLocationRelativeTo(null);
                    readData();
                    setDimmed(false);
                    error.dispose();
                    
                });   
               
        error.add(title);
        error.add(edit);
        error.add(newG);
        error.add(menuB);
    }
    
    private void pause(){
        setDimmed(true);
        this.setEnabled(false);
        this.timer.stop();
        
        JDialog pause = new JDialog();
                pause.setLayout(null);
                pause.setSize(new Dimension(360, 300));
                pause.setUndecorated(true);
                pause.setLocationRelativeTo(null);
                pause.setVisible(true);
                pause.getContentPane().setBackground(Color.WHITE);
                pause.setShape(new RoundRectangle2D.Double(0d, 0d, getWidth(), getHeight(), 50, 50));
                //pause.getRootPane().setBorder( new BevelBorder(BevelBorder.RAISED, Color.gray,Color.gray) );
       
                
        //JLabel title = new JLabel("<html>"+"Trò chơi đã" +"<br/>"+"tạm dừng"+"</html>");        
        JLabel title = new JLabel("Trò chơi đã tạm dừng");
               title.setFont(new Font(fontName, Font.CENTER_BASELINE, 25));
               title.setForeground(colorEditable);
               Font font2 = title.getFont();
               title.setBounds((360-(int)font2.getStringBounds(title.getText(), frc).getWidth())/2,30,400,40);
        
        JButton con = new JButton("Quay lại màn chơi");
                optionButtonUI(con);
                pHover(con);
                con.setBounds(20, 80, 360-40,65);
                con.addActionListener((ActionEvent e) -> {
                    setDimmed(false);
                    this.setEnabled(true);
                    pause.dispose();
                    this.timer.start();
                });
                
        
        JButton res = new JButton("Chơi lại màn chơi");
                optionButtonUI(res);
                pHover(res);
                res.setBounds(20, 145, 360-40,65);
                res.addActionListener((ActionEvent e) -> {
                    reset=true;
                    setDimmed(false);
                    this.setEnabled(true);
                    this.getContentPane().remove(container);
                    this.initGame();
                    pause.dispose();
                    
                });        
                
        JButton menuB = new JButton("Trở về trang chủ");
                optionButtonUI(menuB);
                pHover(menuB);
                menuB.setBounds(20, 210, 360-40,65);
                menuB.addActionListener((ActionEvent e)->{
                    setDimmed(false);
                    this.con = true;
                    pastGame = true;
                    this.setEnabled(true);
                    this.getContentPane().remove(container);
                    
                    menu();
                    this.setSize(new Dimension(600, 780));
                    this.setLocationRelativeTo(null);
                    pause.dispose();
                    
                });
                
        
        pause.add(title);
        pause.add(con);
        pause.add(res);
        pause.add(menuB);
    }
    
    private void hdButton(JButton comp, boolean tick){
        comp.setFont(new Font("Bahnschrift Light", Font.BOLD, 19));
	//comp.setForeground(Color.WHITE);
	comp.setCursor(new Cursor(Cursor.HAND_CURSOR));
        comp.setFocusPainted(false);
        comp.setPreferredSize(new Dimension(getWidth()*29/100, 70));
        comp.setForeground(Color.WHITE);
        if (tick){
            comp.setBorder(new RoundedBorder(25,false,colorMedium));
            comp.setBackground(colorEditable);
        }
        else{
            comp.setBorder(BorderFactory.createEmptyBorder());
            comp.setBackground(null);
        }
        
    }
    
    
    private void hdHover(JButton button){
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        if (button.getBackground()!=colorEditable) {
                            button.setBackground(Color.WHITE);
                            button.setForeground(colorEditable);
                            button.setBorder(new RoundedBorder(25,false,colorMedium));
                            button.setPreferredSize(new Dimension(getWidth()*30/100, 70));
                        }

                    }

                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        if (button.getBackground()!=colorEditable){
                        button.setBackground(null);
                        button.setForeground(Color.WHITE);
                        button.setOpaque(true);
                        button.setBorder(BorderFactory.createEmptyBorder());}
                    }
                });
    }
    
    private void menuButtonUI(JButton comp){
        comp.setFont(new Font("Bahnschrift Light", Font.BOLD, 20));
        //comp.setBackground(new Color(235, 217, 180));
        comp.setBackground(Color.WHITE);
	comp.setForeground(colorMedium);
	comp.setCursor(new Cursor(Cursor.HAND_CURSOR));
	//comp.setBorder(BorderFactory.createEmptyBorder());
        comp.setFocusPainted(false);
        comp.setPreferredSize(new Dimension(300, 70));
        comp.setBorder(new RoundedBorder(25,false,colorMedium));
    }
    
    private void menuHover(JButton button){
        button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    button.setForeground(Color.WHITE);
                    button.setBackground(new Color(56, 135, 190));
                    //button.setOpaque(false);
                    //button.setBorder(BorderFactory.createLineBorder(colorEditable));
                    button.setBorder(new RoundedBorder(25,true,colorMedium));
                    
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                   button.setBackground(Color.WHITE);
                   button.setForeground(colorMedium);
                   button.setBorder(new RoundedBorder(25,false,colorMedium));
                }
            });
    }
    
    private void statisticButton(JButton comp){
        comp.setFont(new Font("Bahnschrift Light", Font.BOLD, 18));
        comp.setHorizontalAlignment(JLabel.LEFT);
        comp.setBackground(colorLight);
	comp.setForeground(Color.BLACK);
	//comp.setBorder(BorderFactory.createEmptyBorder());
        comp.setFocusPainted(false);
        comp.setPreferredSize(new Dimension(this.getWidth()-100, 100));
        comp.setBorder(new RoundedBorder(25,false,Color.WHITE));
    }
    
    private void statisticOptionButton(JButton comp, boolean tick){
        Color c = colorEditable;
        if (!tick) c=colorLightGray;
        
        comp.setFont(new Font("Bahnschrift Light", Font.BOLD, 20));
        comp.setBackground(null);
	comp.setForeground(c);
	comp.setBorder(BorderFactory.createEmptyBorder());
        comp.setFocusPainted(false);
        //comp.setPreferredSize(new Dimension(120, 30));
        //comp.setPreferredSize(new Dimension(this.getWidth()-100, 65));
    }
    
    private void utilityUI(JComponent component){
        component.setBackground(null);
	component.setForeground(colorNote);
	component.setFont(new Font(fontName, Font.CENTER_BASELINE, 17));
        //component.setVerticalTextPosition(SwingConstants.BOTTOM);
	component.setCursor(new Cursor(Cursor.HAND_CURSOR));
	component.setBorder(BorderFactory.createEmptyBorder());
        //component.setPreferredSize(new Dimension(120, 70));
    }
    
    private void buttonUI(JComponent component){
        component.setBackground(colorEditable);
	component.setForeground(Color.WHITE);
        component.setFont(new Font("Bahnschrift Light", Font.BOLD, 18));
	//component.setFont();
	component.setCursor(new Cursor(Cursor.HAND_CURSOR));
	component.setBorder(BorderFactory.createLineBorder(colorEditable));
        component.setPreferredSize(new Dimension(320, 60));
        //component.setBorder(new RoundedBorder(25,false));
    }
    
    private void optionButtonUI(JButton comp){
        comp.setFont(new Font("Bahnschrift Light", Font.BOLD, 18));
        
        comp.setBackground(colorEditable);
	comp.setForeground(Color.WHITE);
	comp.setCursor(new Cursor(Cursor.HAND_CURSOR));
	//comp.setBorder(BorderFactory.createEmptyBorder());
        comp.setFocusPainted(false);
        comp.setPreferredSize(new Dimension(400-40, 65));
        comp.setBorder(new RoundedBorder(25,false,Color.WHITE));
    }
    
    private void keyPadUI(JComponent component){
        component.setCursor(new Cursor(Cursor.HAND_CURSOR));
        component.setForeground(colorEditable);
        component.setBackground(colorLight);
        component.setBorder(BorderFactory.createEmptyBorder());
        component.setFont(new Font("Bahnschrift Light", Font.PLAIN, 30));
    }
    
    private void detailTabUI(JComponent component){
        component.setFont(new Font("Bahnschrift Condensed", Font.BOLD, 16));
    }
    
    private void bottomMenuButtonUI(JButton comp){
        comp.setIconTextGap(10);
        comp.setHorizontalTextPosition(SwingConstants.CENTER);
        comp.setVerticalTextPosition(SwingConstants.BOTTOM);
        
        comp.setPreferredSize(new Dimension(110, 100));
        comp.setFont(new Font(fontName, Font.CENTER_BASELINE, 18));
        comp.setBackground(null);
        comp.setBorder(null);
        comp.setForeground(new Color(32, 14, 58));
        /*comp.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    comp.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    comp.setForeground(colorMedium);
                    comp.setBackground(Color.WHITE);
                    comp.setBorder(new RoundedBorder(25,false,colorMedium));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    
                }
            });*/
    }
    
    private void gpsHover(JButton button){
        button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    button.setForeground(colorEditable);
                    button.setBackground(Color.WHITE);
                    
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(colorEditable);
                    button.setForeground(Color.WHITE);
                }
            });
    }
    
    private void pHover(JButton button){
        button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    button.setForeground(colorEditable);
                    button.setOpaque(false);
                    //button.setBorder(BorderFactory.createLineBorder(colorEditable));
                    button.setBorder(new RoundedBorder(25,true,Color.WHITE));
                    
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(colorEditable);
                    button.setForeground(Color.WHITE);
                    button.setOpaque(true);
                }
            });
    }
    
    private void Timer(){
        this.timer = new Timer(1000, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                second++;
                
                if (second == 60){
                    minute++;
                    second=0;
                    
                }
                
                ddS = dFormat.format(second);
                ddM = dFormat.format(minute);
                tDetail.setText(ddM+":"+ddS);
                
            }
        });
    }
    
    private void setDimmed(boolean dimmed) {
        if (dimmed){
            
        upper.setUndecorated(true);
        upper.setOpacity(0.5f);
        upper.getContentPane().setBackground (new Color (0, 0, 0, 125));
        upper.setSize(new Dimension(this.getWidth()-14, this.getHeight()-5));
        upper.setVisible(true);
        upper.setLocation(this.getX()+7, this.getY());
        upper.setEnabled(false);
        }
        else{
            upper.dispose();
        }
    }
    
    private void checkResult(){
        boolean check=true;
        for (int i=0; i<9; i++){
            for (int j=0; j<9; j++){
                if (gameBoard[i][j]>=10){
                    if (gameBoard[i][j]/10!=tempBoard[i][j]){
                            check=false;
                                if (mistakeOn)
                                {
                                    mistakeCount++; 
                                    if (mistakeCount==mistakeMax){
                                        gameOver(1);
                                    }
                                    mDetail.setText(this.mistakeCount+"/"+this.mistakeMax);
                                }
                                
                                else checkM = true;
                            
                        
                        if (tempBoard[i][j]==0) board[i][j].setBackground(colorEmptyResultBG);
                        else board[i][j].setForeground(colorError);
                    }
                }
            }
        }
        
        if (check==true){
            gameOver(2);
        }
        else{
            errorHint.setVisible(true);
            breakbutton.setVisible(true);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        boolean buttonPressed = false;
        boolean count=false;
        boolean checkEmpty=false;
        if (e.getSource() == numTab) {
            
        }
        if (I!=-1 && J!=-1){
            if (board[I][J].getBackground()==colorEmptyResultBG) checkEmpty=true;
            if (this.gameBoard[I][J]>=10){
                for (JButton btn : numTab) {
                    if (btn.equals(e.getSource())) {
                        System.out.print("press");
                        if (!this.useNote){
                            buttonPressed = true;
                            //System.out.println(tNum);
                            if ((tNum+1)!=this.tempBoard[I][J]){
                                if (tempBoard[I][J]!=0){
                                    numCount[tempBoard[I][J]-1]++;
                                    if (!numTab[tempBoard[I][J]-1].isVisible()) numTab[tempBoard[I][J]-1].setVisible(true);
                                }
                                numCount[tNum]--;
                            }

                            if (numCount[tNum]==0){
                                numTab[tNum].setVisible(false);
                            }
                        
                            System.out.print("value");

                                    this.board[this.I][this.J].setText(tNum+1+"");
                                    this.tempBoard[this.I][this.J] = tNum+1;
                                    board[I][J].setFont(new Font("UTM Micra", 1, 30));

                                    boolean c = this.game.checkAll(this.I, this.J, tNum+1);
                                    if (c){
                                        this.board[this.I][this.J].setForeground(colorEditable);
                                    }
                                    else{
                                        this.board[this.I][this.J].setForeground(colorError);
                                        
                                    }

                                    if (this.autoCheck){
                                        if ((tNum+1)*10!=this.gameBoard[this.I][this.J]){
                                            //System.out.println(tempBoard[I][J]);
                                            if (c && !customScreen.isShowing()){
                                                this.board[this.I][this.J].setForeground(colorError);
                                                
                                                    count =true;
                                                    if (mistakeOn)mistakeCount++; else checkM = true;
                                                    mDetail.setText(mistakeCount+"/"+mistakeMax);
                                                if (mistakeCount==mistakeMax){
                                                    gameOver(1);
                                                    return;
                                                }
                                            }
                                        }
                                        else{
                                            this.board[this.I][this.J].setForeground(colorEditable);
                                        }
                                    }

                            
                        }
                        
                //      note        
                        else{
                            System.out.print("note");
                            if (board[I][J].getForeground()!=colorNote){
                                System.out.println("del");
                                for (int i=0; i<9; i++) note[i][I][J]= 0;
                            }
                            
                            if (note[tNum][I][J]!=tNum+1){
                                if (tempBoard[I][J]!=0){
                                    numCount[tempBoard[I][J]-1]++;
                                    if (!numTab[tempBoard[I][J]-1].isVisible()) numTab[tempBoard[I][J]-1].setVisible(true);
                                }
                                note[tNum][I][J]=tNum+1;
                            }
                            else{
                                note[tNum][I][J]=0;
                            }
                            
                            String[] tempString = {"", "",""};
                            int countString=0;
                            for (int i=0; i<9; i++){
                                if (note[i][I][J]!=0){
                                    tempString[countString] = tempString[countString] + String.valueOf(note[i][I][J]);
                                } 
                                else tempString[countString] = tempString[countString] + "&nbsp;&nbsp;";
                                if (i!=2 && i!=5 && i!=8) tempString[countString] = tempString[countString] + "&nbsp;&nbsp;";
                                if (i==2 || i==5) {
                                    //tempString[countString].
                                    countString++;
                                }
                            }
                            board[I][J].setText("<html>"+tempString[0] +"<br/>"+tempString[1] +"<br/>"+tempString[2]+ "</html>");
                            System.out.print(board[I][J].getText());
                                
                            board[I][J].setFont(new Font("UTM Micra", 1, 19));
                            board[I][J].setForeground(colorNote);
                            tempBoard[I][J] =0;
                            checkEmpty=false;
                        }
                        break;
                    }
                }
            }
            
            for (int i=0; i<9;i++){
                for (int j=0; j<9; j++){
                        if (board[i][j].getBackground()!=colorEmptyResultBG) this.board[i][j].setBackground(Color.WHITE);
                }
            }
            
            for (int i=0; i<9; i++){
                        if (board[I][i].getBackground()!=colorEmptyResultBG) this.board[this.I][i].setBackground(colorLight);
                        if (board[i][J].getBackground()!=colorEmptyResultBG) this.board[i][this.J].setBackground(colorLight);
                }
                int localRow = this.I - this.I % 3;
                int localCol = this.J - this.J % 3;
                for (int i = localRow; i < localRow + 3; i++){
                    for (int j = localCol; j < localCol + 3; j++){
                            if (board[i][j].getBackground()!=colorEmptyResultBG) this.board[i][j].setBackground(colorLight);
                    }
                }
            
            if (this.tempBoard[this.I][this.J]!=0){
                boolean check = true;
                //this.board[this.I][this.J].setBackground(colorMedium);
                for (int i=0; i<9;i++){
                    for (int j=0; j<9; j++){
                        if (this.tempBoard[this.I][this.J]==this.tempBoard[i][j]){
                            this.board[i][j].setBackground(colorMedium);
                            if (i==I || j==J || ((I - I%3)<=i && (I-I%3 + 3)>i && (J - J%3)<=j && (J-J%3 + 3)>j) ){
                                if (i==I && j==J){}
                                else{
                                    this.board[i][j].setBackground(colorErrorBackground);
                                    if (buttonPressed)
                                        check = false;}
                            }
                        }
                        
                    }
                }
                if (!check && !count && !customScreen.isShowing()){
                    if (mistakeOn)mistakeCount++; else checkM = true;
                    mDetail.setText(mistakeCount+"/"+mistakeMax);
                    if (mistakeCount == mistakeMax){
                        gameOver(1);
                        return;
                    }}
            }

            else{
                
                    
            }
            
            /*
            // trung so
            if (this.board[I][J].getForeground()==colorError && this.tempBoard[I][J]!=0){
                //row check
                    for (int i=0; i<9; i++){
                        if (this.tempBoard[I][J] == this.tempBoard[this.I][i]){
                            this.board[this.I][i].setBackground(colorErrorBackground);
                        }
                        
                    //col check
                        if (this.tempBoard[I][J] == this.tempBoard[i][this.J]){
                                this.board[i][this.J].setBackground(colorErrorBackground);
                        }
                        
                    }
                    
                    //box check
                    for (int i = localRow; i < localRow + 3; i++){
                        for (int j = localCol; j < localCol + 3; j++){
                            if (this.tempBoard[I][J] == this.tempBoard[i][j]){  
                                this.board[i][j].setBackground(colorErrorBackground);
                            }
                        }
                    }
            }*/
            
            if (this.gameBoard[I][J]>=10) this.board[this.I][this.J].setBackground(colorCurrent);
            //if (checkEmpty && tempBoard[I][J]==0) board[I][J].setBackground(colorEmptyResultBG);
        }
        
      
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        boolean check = false;
        boolean count = false;
        boolean checkEmpty = false;
        if (I!=-1 && J!=-1){
                if (board[I][J].getBackground()==colorEmptyResultBG) checkEmpty=true;
                System.out.print("press");
                if (this.gameBoard[this.I][this.J]>=10){
                        int num = e.getKeyCode();
                        
                        if (num >= 49 && num <= 57){
                            num -= 48;
                            check = true;
                        }
                        else
                            if (num >= 97 && num <= 105){
                                check = true;
                                num -= 96;}
                        if (check==true){
                            System.out.print(num);
                            if (!this.useNote){
                                if (numTab[num-1].isVisible()){
                                    if (tempBoard[I][J]==0){
                                        this.numCount[num-1]--;
                                        if (numCount[num-1]==0) numTab[num-1].setVisible(false);
                                    }
                                    else{
                                    if (num!=this.tempBoard[I][J]){
                                        this.numCount[num-1]--;
                                        if (numCount[num-1]==0) numTab[num-1].setVisible(false);
                                        numCount[tempBoard[I][J]-1]++;
                                        if (numCount[tempBoard[I][J]-1]==1) numTab[tempBoard[I][J]-1].setVisible(true);
                                    }}
                                }
                                else{
                                    if (num!=this.tempBoard[I][J]){
                                        this.numCount[num-1]--;
                                    }
                                }

                                this.board[this.I][this.J].setText(num+"");
                                this.tempBoard[this.I][this.J] = num;

                                board[I][J].setFont(new Font("UTM Micra", 1, 30));

                                boolean c = this.game.checkAll(this.I, this.J, num);

                                if (c){
                                    this.board[this.I][this.J].setForeground(colorEditable);
                                }
                                else{
                                    this.board[this.I][this.J].setForeground(colorError);
                                }

                                if (this.autoCheck){
                                    if (num*10!=this.gameBoard[this.I][this.J]){
                                        //System.out.println(tempBoard[I][J]);
                                        if (c && !customScreen.isShowing()){
                                            this.board[this.I][this.J].setForeground(colorError);
                                            if (mistakeOn){
                                                count=true;
                                                System.out.println("Sai dap an");
                                                mistakeCount++; 
                                                mDetail.setText(mistakeCount+"/"+mistakeMax);
                                                if (mistakeCount == mistakeMax){
                                                    gameOver(1);
                                                    return;
                                                }
                                            }
                                            else checkM = true;
                                        }
                                    }
                                    else{
                                        this.board[this.I][this.J].setForeground(colorEditable);
                                    }
                                }
                            }
//      note        
                            else{
                                    System.out.print("note");
                                    if (board[I][J].getForeground()!=colorNote){
                                        for (int i=0; i<9; i++) note[i][I][J]= 0;
                                    }

                                    if (note[num-1][I][J]!=num){
                                        if (tempBoard[I][J]!=0){
                                            numCount[tempBoard[I][J]-1]++;
                                            if (!numTab[tempBoard[I][J]-1].isVisible()) numTab[tempBoard[I][J]-1].setVisible(true);
                                        }
                                        note[num-1][I][J]=num;
                                    }
                                    else{
                                        note[num-1][I][J]=0;
                                    }

                                    String[] tempString = {"", "",""};
                                    int countString=0;
                                    for (int i=0; i<9; i++){
                                        if (note[i][I][J]!=0){
                                            tempString[countString] = tempString[countString] + String.valueOf(note[i][I][J]);
                                        } 
                                        else tempString[countString] = tempString[countString] + "&nbsp;&nbsp;";
                                        if (i!=2 && i!=5 && i!=8) tempString[countString] = tempString[countString] + "&nbsp;&nbsp;";
                                        if (i==2 || i==5) {
                                            //tempString[countString].
                                            countString++;
                                        }
                                    }
                                    board[I][J].setText("<html>"+tempString[0] +"<br/>"+tempString[1] +"<br/>"+tempString[2]+ "</html>");
                                    //System.out.print(board[I][J].getText());

                                    board[I][J].setFont(new Font("UTM Micra", 1, 19));
                                    board[I][J].setForeground(colorNote);
                                    tempBoard[I][J] =0;
                                    checkEmpty=false;
                            }
                    }  
                
            }

  
       
//      delete
        if((e.getKeyChar() == KeyEvent.VK_BACK_SPACE || e.getKeyChar() == KeyEvent.VK_DELETE)&&this.board[I][J].getForeground()!=Color.BLACK){
            /*for (int i=0; i<9;i++){
                for (int j=0; j<9; j++){
                    if (this.tempBoard[this.I][this.J]==this.tempBoard[i][j]){
                        this.board[i][j].setBackground(Color.WHITE);
                    }
                }
            }*/
            if (board[I][J].getForeground()==colorNote) for (int i=0; i<9; i++) note[i][I][J]= 0;
            this.board[this.I][this.J].setForeground(colorEditable);
            this.board[this.I][this.J].setText(" ");
            if (this.tempBoard[this.I][this.J]!=0){
                if (this.numCount[tempBoard[I][J]-1]>=0){
                    //System.out.println(this.numCount[tempBoard[I][J]-1]);
                    this.numTab[this.tempBoard[I][J]-1].setVisible(true);
                                    }
                this.numCount[this.tempBoard[this.I][this.J]-1]++;
                this.tempBoard[this.I][this.J]=0;
            }
        }

//      repaint  
            
        for (int i=0; i<9;i++){
                for (int j=0; j<9; j++){
                        if (board[i][j].getBackground()!=colorEmptyResultBG) this.board[i][j].setBackground(Color.WHITE);
                }
            }
            
            for (int i=0; i<9; i++){
                        if (board[I][i].getBackground()!=colorEmptyResultBG) this.board[this.I][i].setBackground(colorLight);
                        if (board[i][J].getBackground()!=colorEmptyResultBG) this.board[i][this.J].setBackground(colorLight);
                }
                int localRow = this.I - this.I % 3;
                int localCol = this.J - this.J % 3;
                for (int i = localRow; i < localRow + 3; i++){
                    for (int j = localCol; j < localCol + 3; j++){
                            if (board[i][j].getBackground()!=colorEmptyResultBG) this.board[i][j].setBackground(colorLight);
                    }
                }
            
            if (this.tempBoard[this.I][this.J]!=0){
                boolean check1=true;
                this.board[this.I][this.J].setBackground(colorMedium);

                for (int i=0; i<9;i++){
                    for (int j=0; j<9; j++){
                        if (this.tempBoard[this.I][this.J]==this.tempBoard[i][j]){
                            this.board[i][j].setBackground(colorMedium);
                            if (i==I || j==J || ((I - I%3)<=i && (I-I%3 + 3)>i && (J - J%3)<=j && (J-J%3 + 3)>j) ){
                                if (i==I && j==J){}
                                else{
                                    this.board[i][j].setBackground(colorErrorBackground);
                                    if (check)
                                        check1 = false;}
                            }
                        }
                        
                    }
                }   
                        if (!check1 && !count && !customScreen.isShowing()){
                            if (mistakeOn){
                                System.out.println("trung so go");
                                mistakeCount++; 
                                mDetail.setText(mistakeCount+"/"+mistakeMax);
                                if (mistakeCount == mistakeMax){
                                    gameOver(1);
                                    return;
                                }
                            }
                            else checkM = true;
                            
                        }
                    
            }

            else{
                
                    
            }
            /*
            // trung so
            if (this.board[I][J].getForeground()==colorError && this.tempBoard[I][J]!=0){
                //row check
                    for (int i=0; i<9; i++){
                        if (this.tempBoard[I][J] == this.tempBoard[this.I][i]){
                            this.board[this.I][i].setBackground(colorErrorBackground);
                        }
                        
                    //col check
                        if (this.tempBoard[I][J] == this.tempBoard[i][this.J]){
                                this.board[i][this.J].setBackground(colorErrorBackground);
                        }
                        
                    }
                    
                    //box check
                    for (int i = localRow; i < localRow + 3; i++){
                        for (int j = localCol; j < localCol + 3; j++){
                            if (this.tempBoard[I][J] == this.tempBoard[i][j]){  
                                this.board[i][j].setBackground(colorErrorBackground);
                            }
                        }
                    }
            }*/
            
            if (this.gameBoard[I][J]>=10) this.board[this.I][this.J].setBackground(colorCurrent);
            //if (checkEmpty && tempBoard[I][J]==0) board[I][J].setBackground(colorEmptyResultBG);

            if (!this.useNote){

            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.out.print(customScreen.isVisible());
        if (!customScreen.isVisible())
            writeData();
        
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
    
}
