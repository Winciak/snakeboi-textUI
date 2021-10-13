package code;

public class Board {
    final int XX, YY;
    private Block[][] blocks;

    public Board(int xx, int yy){
        this.XX=xx;
        this.YY=yy;

        blocks = new Block[XX][YY];
        for(int x=0;x<XX;x++){
            for(int y=0;y<YY;y++){
                blocks[x][y]=new Block(x,y);
            }
        }
    }

    public Block[][] getBlocks() {
        return blocks;
    }

    public void setBlocks(Block[][] blocks) {
        this.blocks = blocks;
    }

    public Block generateFood()
    {
        //System.out.println("generate");
        int x=(int)(Math.random() * XX);
        int y=(int)(Math.random() * YY);

        if(blocks[x][y].getBlocktype()==blockType.SNAKEPART){
           return generateFood();
        }
       // else {
            blocks[x][y].setBlocktype(blockType.FOOD);
            System.out.println("generateded "+x+" "+y);
            Block FoodBlock = new Block(x,y);
            return FoodBlock;
       // }

    }
}
