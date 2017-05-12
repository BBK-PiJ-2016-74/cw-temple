package student;
import game.EscapeState;
import game.ExplorationState;

import java.util.TreeSet;
import java.util.SortedSet;
import java.util.ArrayList;
/**
 * Created by polfilm on 08/05/2017.
 */
public class Map implements Runnable{

    private ArrayList open;
    private ArrayList closed;
    //private ArrayList sequence;
    //private ExplorationState stateExplore;
    private EscapeState stateEscape;
    private Vertex currentVertex;
    private Boolean isProcessing;
    private ExplorationState stateExploration;
    private int step;
    private long bestCandidate;

    Map (ExplorationState s){
        step = 0;
        bestCandidate = 0;
        open = new ArrayList();
        closed = new ArrayList();
        //sequence = new ArrayList();
        //stateExplore = state;
        stateExploration = s;
    }

    public void run() {

        //initial tile pre loop
        currentVertex = new Vertex(stateExploration.getCurrentLocation(), step, stateExploration.getDistanceToTarget(), null, stateExploration);
        open.add(currentVertex);

        while (stateExploration.getDistanceToTarget() != 0) {
            bestCandidate = GoToClosestOpenTile(currentVertex);
            ProcessNeighbours();
            stateExploration.moveTo(bestCandidate);
        }



    }

    public void AddToOpenWithCheck(Vertex vnew){

        //((Vertex)open.first()).distanceToTarget = 999;
        Vertex vold = findNode(open, vnew.id);
        if (vold!=null) {
            if (vnew.id == vold.id && vnew.f < vold.f) {
                //replace vertex only if same vertex but node calculated closer path if closer
                open.remove(vold);
            }
        }
        open.add(vnew);
    }



    public void MoveToClosed(long id)
    {
        Vertex fv = findNode(open,id);
        open.remove(fv);
        closed.add(fv);
    }


    public void printVertexOpen(Vertex v) {
        System.out.println("==========================");
        System.out.println("vertex:" + v.id + " g-travelledFromSource:" + v.step + " h-distanceToTarget:" + v.distanceToTarget + " f-weightedDistance:" + v.f);
    }

    public void printNeighbourClosed(Vertex v) {
        System.out.println("neighbour CLOSED:" + v.id + " g-travelledFromSource:" + v.step + " h-distanceToTarget:" + v.distanceToTarget + " f-weightedDistance:" + v.f);
    }

    public void printNeighbourOpen(Vertex v) {
        System.out.println("neighbour OPEN:" + v.id + " g-travelledFromSource:" + v.step + " h-distanceToTarget:" + v.distanceToTarget + " f-weightedDistance:" + v.f);
    }

    public void printNeighbourFoundOpen(Vertex v) {
        System.out.println("neighbour FOUND OPEN:" + v.id + " g-travelledFromSource:" + v.step + " h-distanceToTarget:" + v.distanceToTarget + " f-weightedDistance:" + v.f);
    }


    public Vertex findNode(ArrayList al, long id) {

        java.util.Iterator<Vertex> iterator = al.iterator();
        while(iterator.hasNext()) {
            Vertex v = iterator.next();
            if(v.id == id)
                return v;
        }

        return null;
    }






    //open = unexplored paths
    //closed = all explored
    public long GoToClosestOpenTile(Vertex closestOpenVertex){
        if (closestOpenVertex.id == stateExploration.getCurrentLocation()){
            printVertexOpen(currentVertex);
            System.out.println("GOTO: IS ON CLOSEST NOW");
            return currentVertex.id;
        }

        //iterate through close, find current, move to parent
        while (closestOpenVertex.id != currentVertex.id) {

            //TODO inefficient might not be fastest
            System.out.println("GOTO BACK: id: " + currentVertex.parent.id);
            stateExploration.moveTo(currentVertex.parent.id);
            currentVertex = currentVertex.parent;

        }
        printVertexOpen(currentVertex);
        return currentVertex.id;

    }

    public void ProcessNeighbours (){

        for (java.util.Iterator<game.NodeStatus> i = stateExploration.getNeighbours().iterator(); i.hasNext(); ) {
            game.NodeStatus n = i.next();

            //find in closed, if there we can skip iteration
            Vertex vertex_closed_result = findNode(closed, n.getId());
            if (vertex_closed_result!=null) {
                printNeighbourClosed (vertex_closed_result);
                continue;
            }


            Vertex neighbourVertex = new Vertex(n.getId(), currentVertex.step + 1, n.getDistanceToTarget(), currentVertex, null);



            //find in open, if found compare distance, if closer replace
            Vertex vertex_open_result = findNode(open, n.getId());
            if (vertex_open_result!=null){
                //OPEN FIND OUT IF CLOSER
                //if (vertex_open_result.f < currentVertex.f)
                //{
                //    open.remove(currentVertex);
                //    open.add(vertex_open_result);
                //}
                printNeighbourFoundOpen (vertex_open_result);
            }
            else
            {
                //VERTEX NOT IN CLOSED, NOT IN OPEN, ADD TO OPEN
                open.add(neighbourVertex);
                printNeighbourOpen(neighbourVertex);
            }



        }
    }






    private synchronized void MoveTo(long tile){


        if (stateExploration.getCurrentLocation() != tile)
            stateExploration.moveTo(tile);
        //currentVertex = new Vertex(state.getCurrentLocation(), step, stateExplore.getDistanceToTarget(), null, state);
        //open.add(currentVertex);

        /*

        while (state.getDistanceToTarget() != 0) {

            //GoToClosestOpenTile((Vertex)open.first());


            //ProcessNeighbours();
            //sync();



            //decide
            //MoveToClosed(v.id);

            //((Vertex)open.first()).distanceToTarget = 999;
            //Vertex fv = findNode(open,0);
            //open.remove(fv);
            //fv.distanceToTarget = 10;
            //open.add(fv);

            //state.moveTo(((Vertex)open.first()).id);

        }



        try {
            System.out.println("MAP");
            System.out.println(Thread.currentThread().getName());
            System.out.println(System.currentTimeMillis());
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        */
    }

}


