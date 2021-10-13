package code;
import java.util.LinkedList;

public class Snake {
    private LinkedList<Block> snakePartList = new LinkedList<>();
    private Block snakeHead;

    public Snake(Block initBlock){
        snakeHead = initBlock;
        snakeHead.setBlocktype(blockType.SNAKEPART);
        snakePartList.add(snakeHead);
    }

    public void evolve(){
        snakePartList.add(snakePartList.getLast());
    }

    public void move(Block nextBlock)
    {
        System.out.println("Snake is moving to " +
                nextBlock.getX() + " " + nextBlock.getY());
        Block tail = snakePartList.removeLast();
        tail.setBlocktype(blockType.EMPTY);

        snakeHead = nextBlock;
        //snakeHead.setBlocktype(blockType.SNAKEPART);
        snakePartList.addFirst(snakeHead);
    }

    public boolean checkCrash(Block nextBlock)
    {
        System.out.println("Going to check for Crash");
        for (Block block : snakePartList) {
            if (block == nextBlock) {
                System.out.println("CRASH");
                return true;
            }
        }

        return false;
    }

    public LinkedList<Block> getSnakePartList()
    {
        return snakePartList;
    }

    public void setSnakePartList(LinkedList<Block> snakePartList)
    {
        this.snakePartList = snakePartList;
    }

    public Block getHead()
    {
        return snakeHead;
    }

    public void setHead(Block snakeHead)
    {
        this.snakeHead = snakeHead;
    }
}
