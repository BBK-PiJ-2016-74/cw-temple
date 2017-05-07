package student;
import game.EscapeState;
import game.ExplorationState;
import java.util.TreeSet;
import java.util.SortedSet;
/**
 * Created by polfilm on 07/05/2017.
 */
public class Vertex implements Comparable<Vertex>{
//public class Vertex {
    public long id;
    public ExplorationState tile;
    public int vertex_id;
    public int step;
    public int distanceToTarget;
    public int f;
    public Vertex parent;

    Vertex(long id, int step, int distanceToTarget, Vertex parent, ExplorationState tile){
        super();
        this.id = id;
        this.step = step;
        this.distanceToTarget = distanceToTarget;
        this.f = step + distanceToTarget;
        this.parent = parent;
        this.tile = tile;
    }

    @Override
    public int compareTo(Vertex v) {
        return this.f - v.f;
    }
}