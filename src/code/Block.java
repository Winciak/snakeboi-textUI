package code;

public class Block {
    private final int x,y;
    private blockType blocktype;

    public Block(int x, int y){
        this.x=x;
        this.y=y;
        this.blocktype=blockType.EMPTY;
    }

    public blockType getBlocktype(){
        return blocktype;
    }

    public void setBlocktype(blockType blocktype){
        this.blocktype=blocktype;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
