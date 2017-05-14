package student;
import java.util.Comparator;

/**
 * Created by polfilm on 14/05/2017.
 */
public class VertexGold implements Comparable<VertexGold>{
    public int id;
    public int gold;
    public game.Node node;
    public game.Tile tile;

    VertexGold(game.Node n) {
        this.node = n;
        this.tile = n.getTile();
        this.gold = this.tile.getGold();

    }

    public int getGold(){
        return this.gold;
    }

    @Override
    public int compareTo(VertexGold vertexGold) {
        return (this.getGold() < vertexGold.getGold() ? -1 :
                (this.getGold() == vertexGold.getGold() ? 0 : 1));
    }


}
