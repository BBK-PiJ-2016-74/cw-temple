package student;
import java.util.Comparator;

/**
 * Created by polfilm on 14/05/2017.
 */
public class VertexGold implements Comparable<VertexGold>{
    public long id;
    public int step;
    public int gold;
    public int rowY;
    public int colX;
    public int g;
    public int h;
    public int f;
    public game.Node node;
    public game.Tile tile;
    public VertexGold parent;

    VertexGold(game.Node n, int h, VertexGold parent) {
        this.id = n.getId();
        this.step = 0;
        this.node = n;
        this.tile = n.getTile();
        this.rowY = this.tile.getRow();
        this.colX = this.tile.getColumn();
        this.gold = this.tile.getGold();
        this.g = 0;
        this.h = h;
        this.f = 0;
        this.parent = parent;


    }

    public int getGold(){
        return this.gold;
    }

    public int getDistanceToTarget(){
        return this.h;
    }

    @Override
    public int compareTo(VertexGold vertexGold) {
        return (this.getGold() < vertexGold.getGold() ? -1 :
                (this.getGold() == vertexGold.getGold() ? 0 : 1));
    }


}
