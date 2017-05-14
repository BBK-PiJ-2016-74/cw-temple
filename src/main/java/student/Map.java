package student;
import game.EscapeState;
import game.ExplorationState;

import java.util.HashSet;
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
    private Vertex firstVertex;
    private Boolean isProcessing;
    private ExplorationState stateExploration;
    private int step;
    private Vertex closestOpenVertex;
    private Vertex mapBuildingStepVertex;
    private java.util.List mapClosestOpenVertex;

    Map (ExplorationState s){
        step = 0;
        closestOpenVertex = null;
        firstVertex = null;
        mapBuildingStepVertex = null;
        mapClosestOpenVertex = null;
        open = new ArrayList();
        closed = new ArrayList();
        //sequence = new ArrayList();
        //stateExplore = state;
        stateExploration = s;
    }



    public void AddToOpenWithCheck(Vertex vnew){

        //((Vertex)open.first()).distanceToTarget = 999;
        Vertex vold = findNode(open, vnew.id);
        if (vold!=null) {
            if (vnew.id == vold.id && vnew.f < vold.f) {
                //replace vertex only if same vertex but node calculated closer path if closer
                //replace values plus parent
                open.remove(vold);
            }
        }
        open.add(vnew);
    }



    public void CloseVertex(long id)
    {
        Vertex fv = findNode(open,id);
        open.remove(fv);
        closed.add(fv);
        printVertexClosed(fv);
    }


    public void printVertexCurrent(Vertex v) {
        System.out.println("==========================");
        System.out.println("VERTEX CURRENT: " + v.id + " g-travelledFromSource:" + v.step + " h-distanceToTarget:" + v.distanceToTarget + " f-weightedDistance:" + v.f);
    }

    public void printVertexClosed(Vertex v) {
        System.out.println("VERTEX NOW CLOSED:" + v.id + " g-travelledFromSource:" + v.step + " h-distanceToTarget:" + v.distanceToTarget + " f-weightedDistance:" + v.f);
    }

    public void printNeighbourOpen(Vertex v) {
        System.out.println("neighbour NEW OPEN:" + v.id + " g-travelledFromSource:" + v.step + " h-distanceToTarget:" + v.distanceToTarget + " f-weightedDistance:" + v.f);
    }

    public void printNeighbourExistingOpen(Vertex v) {
        System.out.println("neighbour EXISTING OPEN:" + v.id + " g-travelledFromSource:" + v.step + " h-distanceToTarget:" + v.distanceToTarget + " f-weightedDistance:" + v.f);
    }

    public void printNeighbourExistingClosed(Vertex v) {
        System.out.println("neighbour EXISTING CLOSED:" + v.id + " g-travelledFromSource:" + v.step + " h-distanceToTarget:" + v.distanceToTarget + " f-weightedDistance:" + v.f);
    }

    public void printDistanceToTarget() {
        System.out.println("DISTANCE TO TARGET:" + stateExploration.getDistanceToTarget());
    }

    public void printMapAddStep(Vertex v) {
        System.out.println("mapAddStep MAP STEP:" + v.id + " g-travelledFromSource:" + v.step + " h-distanceToTarget:" + v.distanceToTarget + " f-weightedDistance:" + v.f);
    }

    public void printWormholeAddStep(long l) {
        System.out.println("wormholeAddStep MAP STEP:" + l );
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

    public Vertex findClosestOpenVertex() {

        java.util.Iterator<Vertex> iterator = open.iterator();
        int closest = 32000;
        Vertex ret = null;
        while(iterator.hasNext()) {
            Vertex v = iterator.next();
            if(v.distanceToTarget < closest){
                closest = v.distanceToTarget;
                ret = v;
            }
        }

        return ret;
    }


    public Boolean buildOpenVertexMap()
    {

        Boolean isIntersectingWithCurrentVertex = false;
        mapClosestOpenVertex = new ArrayList<Long>();
        mapBuildingStepVertex = closestOpenVertex;
        while (mapBuildingStepVertex != null)
        {
            mapClosestOpenVertex.add(mapBuildingStepVertex.id);
            printMapAddStep (mapBuildingStepVertex);
            if (currentVertex.id == mapBuildingStepVertex.id){
                isIntersectingWithCurrentVertex = true;
                return isIntersectingWithCurrentVertex;
            }
            mapBuildingStepVertex = mapBuildingStepVertex.parent;
        }

        return isIntersectingWithCurrentVertex;

    }



    public void run() {

        //initial tile pre loop
        currentVertex = new Vertex(stateExploration.getCurrentLocation(), step, stateExploration.getDistanceToTarget(), null, stateExploration);
        firstVertex = currentVertex;
        AddToOpenWithCheck(currentVertex);

        while (stateExploration.getDistanceToTarget() != 0) {

            ProcessNeighbours();
            CloseVertex (currentVertex.id);
            closestOpenVertex = findClosestOpenVertex();
            GoToClosestOpenTile(closestOpenVertex);


        }



    }


    //open = unexplored paths
    //closed = all explored
    public long GoToClosestOpenTile(Vertex closestOpenVertex){
        //if (closestOpenVertex.id == stateExploration.getCurrentLocation()){
        //    printVertexOpen(currentVertex);
        //    System.out.println("GOTO: IS ON CLOSEST NOW");
        //    return currentVertex.id;
        //}

        //iterate through closed, find current, move to parent
        while (currentVertex.id != closestOpenVertex.id) {




            //if closest open is in state, go forward one
            //if closest open is not in state go to parent (and hope parent not null)

            //TODO inefficient might not be fastest
            if (currentVertex.parent != null)
            {

                //if closest open is in state, go forward one
                //this is wrong try this
                  //if closestopenvertex
                if (closestOpenVertex.parent.id == currentVertex.id) {
                    System.out.println("GO FWD: id: " + closestOpenVertex.id);
                    stateExploration.moveTo(closestOpenVertex.id);
                    currentVertex = closestOpenVertex;
                }
                else
                {

                    //build back map from open to start
                    Boolean isIntersectingWithCurrent = buildOpenVertexMap();

                    //keep going back until we get back onto known path (expensive)
                    while (!mapClosestOpenVertex.contains(currentVertex.id))
                    {

                        //we are still not intersecting
                        System.out.println("GO BACK ONE: id: " + currentVertex.parent.id);
                        stateExploration.moveTo(currentVertex.parent.id);
                        currentVertex = currentVertex.parent;
                    }


                    //we know map forward now, use it
                    System.out.println("INTERSECTION FOUND AT :" + currentVertex.id + " USING MAP TO GET TO CLOSEST OPEN: id: " + closestOpenVertex.id);

                    //iterate through known map from the point of intersection and towards closest node

                    int intersectionLocationWithinKnownMap = mapClosestOpenVertex.indexOf(currentVertex.id);
                    //long[] arr2 = new long[intersectionLocationWithinKnownMap];
                    //int i = 0;
                    //for (java.util.Iterator<Long> e : mapClosestOpenVertex) {
                    //    arr2[i++] = e; // autoboxing does the job here
                    //}

                    for (int l = intersectionLocationWithinKnownMap-1; l >=0; l--){
                        long pos = (long)mapClosestOpenVertex.get(l);
                        stateExploration.moveTo(pos);
                        printWormholeAddStep(pos);
                    }
                    //we can assume but we wont, look up pos node
                    //current vertex should be equal to closestOpenVertex.id
                    //pos should have been stateExploration.getCurrentLocation()
                    //we are in open node now closest to destination (according to calculations)
                    currentVertex = findNode(open,stateExploration.getCurrentLocation());

                    /*
                    for (java.util.Iterator<Long> iter = mapClosestOpenVertex.iterator(); iter.hasNext(); )
                    {
                        Long l = iter.next();
                        stateExploration.moveTo(l);
                    }
                    currentVertex = closestOpenVertex;


                    if (isIntersectingWithCurrent){
                        //we know map forward now, use it
                        System.out.println("INTERSECTION FOUND. USE MAP TO GET TO CLOSEST: id: " + currentVertex.parent.id);
                        java.util.Collections.sort(mapClosestOpenVertex, java.util.Collections.reverseOrder());
                        for (java.util.Iterator<Long> iter = mapClosestOpenVertex.iterator(); iter.hasNext(); )
                        {
                            Long l = iter.next();
                            stateExploration.moveTo(l);
                        }
                        currentVertex = closestOpenVertex;
                    }
                    else
                    {
                        //we are still not intersecting
                        System.out.println("GO BACK ONE: id: " + currentVertex.parent.id);
                        stateExploration.moveTo(currentVertex.parent.id);
                        currentVertex = currentVertex.parent;
                    }
                    */
                }


            }
            else {
                //first tile has no parent but might have to go back there at some point
                // this should hard coded point to 1st
                //no parent means INITIAL
                System.out.println("INITIAL: id: " + firstVertex.id);
                if (stateExploration.getCurrentLocation() != closestOpenVertex.id)
                    stateExploration.moveTo(closestOpenVertex.id);
                currentVertex = closestOpenVertex;
            }

        }
        printVertexCurrent(currentVertex);
        return currentVertex.id;

    }

    public void ProcessNeighbours (){

        for (java.util.Iterator<game.NodeStatus> i = stateExploration.getNeighbours().iterator(); i.hasNext(); ) {
            game.NodeStatus n = i.next();



            //find existing in closed, if there we can skip iteration
            Vertex vertex_closed_result = findNode(closed, n.getId());
            if (vertex_closed_result!=null) {
                printNeighbourExistingClosed (vertex_closed_result);
                continue;
            }

            //find existing in open, if there we can skip iteration
            Vertex vertex_open_result = findNode(open, n.getId());
            if (vertex_open_result!=null) {
                printNeighbourExistingOpen (vertex_open_result);
                continue;
            }


            Vertex neighbourOpenVertex = new Vertex(n.getId(), currentVertex.step + 1, n.getDistanceToTarget(), currentVertex, null);
            AddToOpenWithCheck(neighbourOpenVertex);
            printNeighbourOpen (neighbourOpenVertex);


            //TODO: if distance closer from here, replace open

        }
    }






    private synchronized void MoveTo(long id){


        if (stateExploration.getCurrentLocation() != id)
            stateExploration.moveTo(id);
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


