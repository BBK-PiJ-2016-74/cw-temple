package student;

/**
 * Created by polfilm on 07/05/2017.
 */
public class Vertex implements Comparable<Vertex>{

    public long id;
    public int step;
    public int distanceToTarget;
    public int f;
    public Vertex parent;

    Vertex(long id, int step, int distanceToTarget, Vertex parent){
        super();
        this.id = id;
        this.step = step;
        this.distanceToTarget = distanceToTarget;
        this.f = step + distanceToTarget;
        this.parent = parent;
    }

    @Override
    public int compareTo(Vertex v) {
        return this.f - v.f;
    }
}