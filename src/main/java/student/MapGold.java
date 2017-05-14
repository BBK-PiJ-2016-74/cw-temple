package student;
import game.EscapeState;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by polfilm on 14/05/2017.
 */
public class MapGold {
    private ArrayList open;
    private ArrayList closed;
    private EscapeState stateEscape;
    private Vertex currentVertex;
    private Vertex firstVertex;
    private int step;
    private Vertex closestOpenVertex;
    private Vertex mapBuildingStepVertex;
    private java.util.List mapClosestOpenVertex;
    private ArrayList goldMap;

    MapGold (EscapeState s){
        step = 0;
        closestOpenVertex = null;
        firstVertex = null;
        mapBuildingStepVertex = null;
        mapClosestOpenVertex = null;
        open = new ArrayList();
        closed = new ArrayList();
        stateEscape = s;
        goldMap = new ArrayList();
    }



    public void buildGoldMap(){
        java.util.Iterator<game.Node> iterator = stateEscape.getVertices().iterator();
        while(iterator.hasNext()) {
            game.Node node = iterator.next();
            goldMap.add(new VertexGold(node));
        }

        //descending - most gold first
        java.util.Collections.sort(goldMap);
        java.util.Collections.reverse(goldMap);
    }


    public void run() {
        buildGoldMap();
        System.out.println("");
    }
}
