package code;


import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenWriter;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;

import java.io.IOException;
import java.util.LinkedList;

public class Play {
    private static final int DIRECTION_NONE = 0, DIRECTION_RIGHT = 1, DIRECTION_LEFT = -1,
            DIRECTION_UP=2, DIRECTION_DOWN=-2;
    private static final int BOARD_WIDTH = 20, BOARD_HEIGTH = 10;
    private int score = 0, slow = 800;
    private int level = 1;
    private int diff = 1;
    private Snake snake;
    private Board board;
    private int direction;
    private boolean gameOver;
    private static Screen screen = new Screen(new SwingTerminal(2*BOARD_WIDTH,2*BOARD_HEIGTH));
    ScreenWriter writer = new ScreenWriter(screen);

    public Play(Snake snake,Board board){
        this.snake=snake;
        this.board=board;
    }

    public Snake getSnake() {
        return snake;
    }

    public void setSnake(Snake snake) {
        this.snake = snake;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public boolean isGameOver(){
        return gameOver;
    }

    public void setGameOver(boolean gameOver){
        this.gameOver=gameOver;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void check(){
       //System.out.println("checking");
        if(!gameOver){
            if(direction!=DIRECTION_NONE){
                Block nextBlock = getNextBlock(snake.getHead());


                if(snake.checkCrash(nextBlock) ){
                    gameOver();
                    score = 0;
                    level = 1;
                    slow = 800;
                    setDirection(DIRECTION_NONE);
                    Block Pos = new Block(BOARD_WIDTH/2, BOARD_HEIGTH/2);
                    snake = new Snake(Pos);
                    snake.evolve();
                    board = new Board(BOARD_WIDTH, BOARD_HEIGTH);
                    gameOver=true;
                }
                else{
                    screen.putString(snake.getSnakePartList().getLast().getX(),snake.getSnakePartList().getLast().getY()," ", Terminal.Color.DEFAULT, Terminal.Color.DEFAULT);
                    //terminal.putCharacter(' ');
                    snake.move(nextBlock);

                    if(nextBlock.getBlocktype()==blockType.FOOD){
                        System.out.println("evolve");
                        snake.evolve();

                        LinkedList<Block> snek = snake.getSnakePartList();
                        int snakeLength=snek.size();

                        for (int i=0;i<snakeLength;i++){
                            int x=snek.get(i).getX();
                            int y=snek.get(i).getY();
                            board.getBlocks()[x][y].setBlocktype(blockType.SNAKEPART);
                        }

                        score = score + 5*level;
                        if(score==10) {level++; slow=slow-100;}
                        if(score==50) {level++; slow=slow-100;}
                        if(score==110) {level++; slow=slow-100;}
                        if(score==190) {level++; slow=slow-100;}

                        Block Foodblock = board.generateFood();

                        writer.setForegroundColor(Terminal.Color.GREEN);
                        writer.drawString(Foodblock.getX(),Foodblock.getY(),"#");

                        writer.setForegroundColor(Terminal.Color.GREEN);
                        writer.drawString(BOARD_WIDTH/4, 2*BOARD_HEIGTH-BOARD_HEIGTH/2, "Score: "+score);
                        writer.drawString(BOARD_WIDTH, 2*BOARD_HEIGTH-BOARD_HEIGTH/2, "Level: "+level);
                        writer.setForegroundColor(Terminal.Color.DEFAULT);
                    }
                }

            }
        }
    }

    private Block getNextBlock(Block currentBlock){
        //System.out.println("getNExtBlock");
        int x = currentBlock.getX();
        int y = currentBlock.getY();
        int error=0;
        Block nextBlock = board.getBlocks()[BOARD_WIDTH/2][ BOARD_HEIGTH/2];
        if(direction == DIRECTION_LEFT){
            x--;
        }
        if(direction == DIRECTION_RIGHT){
            x++;
        }
        if(direction == DIRECTION_UP){
            y--;
        }
        if(direction == DIRECTION_DOWN){
            y++;
        }

        try {
            nextBlock = board.getBlocks()[x][y];
        } catch (ArrayIndexOutOfBoundsException e){
            //e.printStackTrace();
            error=1;
        }

        if(error==0){
            return nextBlock;
        }
        else if(error==1){

            gameOver();
            score = 0;
            level = 1;
            slow = 800;
            setDirection(DIRECTION_NONE);
            Block Pos = new Block(BOARD_WIDTH/2, BOARD_HEIGTH/2);
            snake = new Snake(Pos);
            snake.evolve();
            board = new Board(BOARD_WIDTH, BOARD_HEIGTH);
            gameOver=true;

            return nextBlock;
        }
        return nextBlock;
    }



    public void drawSnek(){

        LinkedList<Block> snek = snake.getSnakePartList();
        int snakeLength=snek.size();

        for (int i=0;i<snakeLength;i++){
            if(i==0) writer.drawString(snek.get(i).getX(),snek.get(i).getY(),"@");//terminal.putCharacter('@');
            else
                writer.drawString(snek.get(i).getX(),snek.get(i).getY(),"o");
        }

    }

    public void drawInit(){

        if(diff==1) {
            slow=800;
        }
        if(diff==2) {
            slow=650;
        }
        if(diff==3) {
            slow=500;
        }

        for(int i=0;i<board.YY;i++) {
            //terminal.moveCursor(0,i);
            for (int j = 0; j < board.XX; j++) {
                if (board.getBlocks()[j][i].getBlocktype() == blockType.EMPTY)
                   writer.drawString(j,i," ");// terminal.putCharacter( ' ');
                if (board.getBlocks()[j][i].getBlocktype() == blockType.FOOD) {
                    writer.setForegroundColor(Terminal.Color.GREEN);
                    writer.drawString(j,i,"#");
                    writer.setForegroundColor(Terminal.Color.DEFAULT);

                }
            }
        }

        for(int i=0;i<board.YY;i++) {
            writer.setForegroundColor(Terminal.Color.RED);
            writer.drawString(board.XX,i,"|");
        }
        for(int i=0;i<=board.XX;i++) {
            writer.drawString(i,board.YY,"*");
        }

        writer.setForegroundColor(Terminal.Color.GREEN);
        writer.drawString(BOARD_WIDTH/4, 2*BOARD_HEIGTH-BOARD_HEIGTH/2, "Score: "+score);
        writer.drawString(BOARD_WIDTH, 2*BOARD_HEIGTH-BOARD_HEIGTH/2, "Level: "+level);

        writer.setForegroundColor(Terminal.Color.DEFAULT);

    }

    public void getInput() {
        Key key;
        key = screen.readInput();
        if (key != null) {
            if (key.getKind() == Key.Kind.ArrowUp)
                direction = DIRECTION_UP;
            if (key.getKind() == Key.Kind.ArrowDown)
                direction = DIRECTION_DOWN;
            if (key.getKind() == Key.Kind.ArrowLeft)
                direction = DIRECTION_LEFT;
            if (key.getKind() == Key.Kind.ArrowRight)
                direction = DIRECTION_RIGHT;
        }
    }

    public int menu(){

        screen.clear(); screen.refresh();
        int i=1;


        writer.drawString(BOARD_WIDTH/4,2*BOARD_HEIGTH/5,"1. New Game ");
        writer.drawString(BOARD_WIDTH/4,4*BOARD_HEIGTH/5,"2. Change Level              ");
        writer.drawString(BOARD_WIDTH/4,6*BOARD_HEIGTH/5,"3. Quit ");

        Key key;
        key = screen.readInput();

        while(1==1) {

            key = screen.readInput();


            if (key != null){
                if (key.getKind() == Key.Kind.ArrowUp) {
                    i--;
                    if (i == 0) i = 3;
                }
                if (key.getKind() == Key.Kind.ArrowDown){
                    i++;
                    if(i == 4) i = 1;
                }
                if(key.getKind() == Key.Kind.Enter && i!=2){
                    return i;
                }
                if(key.getKind() == Key.Kind.ArrowLeft && i==2){
                    diff--;
                    if(diff==0) diff=3;
                }
                if(key.getKind() == Key.Kind.ArrowRight && i==2){
                    diff++;
                    if(diff==4) diff=1;
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e){
                e.printStackTrace();
            }


            if(i==1){
                writer.setForegroundColor(Terminal.Color.CYAN);
                writer.drawString(BOARD_WIDTH/4,2*BOARD_HEIGTH/5,"1. New Game ");
                writer.setForegroundColor(Terminal.Color.DEFAULT);
                writer.drawString(BOARD_WIDTH/4,4*BOARD_HEIGTH/5,"2. Change Level           ");
                writer.drawString(BOARD_WIDTH/4,6*BOARD_HEIGTH/5,"3. Quit ");
            }

            if(i==2){
                writer.setForegroundColor(Terminal.Color.CYAN);

                if(diff==1) {
                    writer.drawString(BOARD_WIDTH / 4, 4 * BOARD_HEIGTH / 5, "2. Change Level: EASY  ");
                }
                if(diff==2) {
                    writer.drawString(BOARD_WIDTH/4,4*BOARD_HEIGTH/5,"2. Change Level: MEDIUM ");
                }
                if(diff==3) {
                    writer.drawString(BOARD_WIDTH/4,4*BOARD_HEIGTH/5,"2. Change Level: HARD  ");
                }
                writer.setForegroundColor(Terminal.Color.DEFAULT);
                writer.drawString(BOARD_WIDTH/4,2*BOARD_HEIGTH/5,"1. New Game ");
                writer.drawString(BOARD_WIDTH/4,6*BOARD_HEIGTH/5,"3. Quit ");
            }

            if(i==3){
                writer.setForegroundColor(Terminal.Color.CYAN);
                writer.drawString(BOARD_WIDTH/4,6*BOARD_HEIGTH/5,"3. Quit ");
                writer.setForegroundColor(Terminal.Color.DEFAULT);
                writer.drawString(BOARD_WIDTH/4,2*BOARD_HEIGTH/5,"1. New Game ");
                writer.drawString(BOARD_WIDTH/4,4*BOARD_HEIGTH/5,"2. Change Level              ");
            }

            screen.refresh();
        }
    }

    public int gameOver(){

        screen.clear(); screen.refresh();

        stringGameOver();

        Key key;
        key = screen.readInput();

        while(1==1) {

            key = screen.readInput();

            stringGameOver();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e){
                e.printStackTrace();
            }

            if (key != null){
                return 1;
            }

            screen.refresh();
        }



    }

    private void stringGameOver() {
        writer.setForegroundColor(Terminal.Color.RED);
        writer.drawString(BOARD_WIDTH/3,(BOARD_HEIGTH/5)-1,"   ___   _   __  __ ___  ");
        writer.drawString(BOARD_WIDTH/3,BOARD_HEIGTH/5,"  / __| /_\\ |  \\/  | __|  ");
        writer.drawString(BOARD_WIDTH/3,(BOARD_HEIGTH/5)+1," | (_ |/ _ \\| |\\/| | _|  ");
        writer.drawString(BOARD_WIDTH/3,(BOARD_HEIGTH/5)+2,"  \\___/_/ \\_\\_|  |_|___|  ");

        writer.drawString(2+BOARD_WIDTH/3,(BOARD_HEIGTH/5)-1+4,"   _____   _____ ___");
        writer.drawString(2+BOARD_WIDTH/3,BOARD_HEIGTH/5+4,"  / _ \\ \\ / / __| _ \\");
        writer.drawString(2+BOARD_WIDTH/3,(BOARD_HEIGTH/5)+1+4," | (_) \\ V /| _||   /");
        writer.drawString(2+BOARD_WIDTH/3,(BOARD_HEIGTH/5)+2+4,"  \\___/ \\_/ |___|_|_\\");

        writer.setForegroundColor(Terminal.Color.GREEN);
        writer.drawString(4+BOARD_WIDTH/3, (BOARD_HEIGTH/5)+9, "Score: "+score*diff);
        writer.drawString(4+BOARD_WIDTH/3, (BOARD_HEIGTH/5)+10, "Level: "+level);
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        Block initPos = new Block(BOARD_WIDTH/2, BOARD_HEIGTH/2);
        Snake initSnake = new Snake(initPos);
        initSnake.evolve();
        Board board = new Board(BOARD_WIDTH, BOARD_HEIGTH);
        Play newGame = new Play(initSnake, board);
        newGame.gameOver = false;
        newGame.direction = DIRECTION_NONE;

        newGame.screen.startScreen();
        newGame.screen.setCursorPosition(null);

        while(1==1) {
            int menu=newGame.menu();
            if (menu == 1) {

                newGame.gameOver = false;

                newGame.screen.clear();
                newGame.board.generateFood();
                newGame.drawInit();
                newGame.screen.refresh();

                Thread.currentThread();

                while (!newGame.gameOver) {
                    newGame.getInput();

                    newGame.check();
                    if(!newGame.gameOver)
                    newGame.drawSnek();
                    newGame.screen.refresh();


                    newGame.getInput();
                    try {
                        Thread.sleep(newGame.slow);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    newGame.getInput();


                }
            }

            if(menu == 3){
                newGame.screen.stopScreen();
                break;
            }
        }
    }
}
