package student;
import game.EscapeState;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by polfilm on 14/05/2017.
 */
public class MapGold implements Runnable{
    private ArrayList open;
    private ArrayList closed;
    private EscapeState stateEscape;
    private VertexGold currentVertexGold;
    private VertexGold firstVertexGold;
    private VertexGold lastVertexGold;
    private int step;
    private VertexGold closestOpenVertex;
    private VertexGold mapBuildingStepVertexGold;
    private java.util.List mapClosestOpenVertex;
    private ArrayList goldMap;
    private ArrayList mostProfitableMap;
    private int goldTotalOnSimulation;
    private int goldTotalCollected;

    MapGold (EscapeState s){
        step = 0;
        closestOpenVertex = null;
        firstVertexGold = null;
        lastVertexGold = null;
        mapBuildingStepVertexGold = null;
        mapClosestOpenVertex = null;
        open = new ArrayList();
        closed = new ArrayList();
        stateEscape = s;
        goldMap = new ArrayList();
        mostProfitableMap = new ArrayList();
        goldTotalOnSimulation = 0;
        goldTotalCollected = 0;
    }

    public void AddToOpenWithCheck(VertexGold vnew){

        VertexGold vold = findNode(open, vnew.id);
        if (vold!=null) {
            if (vnew.id == vold.id && vnew.f < vold.f) {
                //replace vertex only if same vertex but node calculated closer path if closer
                //replace values plus parent
                open.remove(vold);
            }
        }
        open.add(vnew);
    }

    /*
    public void UpdateGoldMapHeuristics(VertexGold start, VertexGold end){

        java.util.Iterator<VertexGold> iterator = goldMap.iterator();
        while(iterator.hasNext()) {
            VertexGold vg = iterator.next();
            vg.h = CalcManhattanDist(start, end);
            System.out.println("");
        }

    }
    */

    public int CalcManhattanDist (game.Node start, game.Node end)
    {
        return Math.abs(end.getTile().getColumn() - start.getTile().getColumn()) + Math.abs(end.getTile().getRow() - start.getTile().getRow());
    }

    public VertexGold findNode(ArrayList al, long id) {

        java.util.Iterator<VertexGold> iterator = al.iterator();
        while(iterator.hasNext()) {
            VertexGold vg = iterator.next();
            if(vg.id == id)
                return vg;
        }

        return null;
    }

    public void buildGoldMap(){



        java.util.Iterator<game.Node> iterator = stateEscape.getVertices().iterator();
        while(iterator.hasNext()) {
            game.Node node = iterator.next();

            //calculate heuristic Manhattan distance to exit (similar to explore)
            int h = CalcManhattanDist(node, stateEscape.getExit());
            goldMap.add(new VertexGold(node, h, null));
        }

        //descending - most gold first
        java.util.Collections.sort(goldMap);
        java.util.Collections.reverse(goldMap);
    }



    public void CloseVertex(long id)
    {
        VertexGold fv = findNode(open,id);
        open.remove(fv);
        closed.add(fv);
        printVertexClosed(fv);
    }


    public VertexGold findClosestOpenVertex() {

        java.util.Iterator<VertexGold> iterator = open.iterator();
        int closest = 32000;
        VertexGold ret = null;
        while(iterator.hasNext()) {
            VertexGold v = iterator.next();
            if(v.getDistanceToTarget() < closest){
                closest = v.getDistanceToTarget();
                ret = v;
            }
        }

        return ret;
    }


    public void ProcessNeighbours (int richTileMutliplier) {


        java.util.Iterator<game.Node> iterator = currentVertexGold.node.getNeighbours().iterator();
        while(iterator.hasNext()) {
            game.Node node = iterator.next();

            VertexGold neighbourOpenVertex = findNode(goldMap, node.getId());
            VertexGold vertexGold_closed_result = findNode(closed, neighbourOpenVertex.id);
            if (vertexGold_closed_result!=null) {
                printNeighbourExistingClosed (vertexGold_closed_result);
                continue;
            }

            VertexGold vertexGold_open_result = findNode(open, neighbourOpenVertex.id);
            if (vertexGold_open_result!=null) {
                printNeighbourExistingOpen (vertexGold_open_result);
                continue;
            }

            neighbourOpenVertex.step = currentVertexGold.step + 1;
            neighbourOpenVertex.g = currentVertexGold.step;
            neighbourOpenVertex.f = currentVertexGold.h + currentVertexGold.g;
            neighbourOpenVertex.parent = currentVertexGold;

            AddToOpenWithCheck(neighbourOpenVertex);
            printNeighbourOpen (neighbourOpenVertex);
        }

    }


    public void buildMostProfitableMap(int richTileMutliplier)
    {

        open = new ArrayList();
        goldTotalOnSimulation = 0;

        currentVertexGold = firstVertexGold;
        AddToOpenWithCheck(firstVertexGold);

        while (currentVertexGold.getDistanceToTarget() != 0) {

            ProcessNeighbours(richTileMutliplier);
            CloseVertex (currentVertexGold.id);
            currentVertexGold = findClosestOpenVertex();
            printVertexCurrent(currentVertexGold);

        }

        buildOpenVertexGoldMap();


    }


    public void buildOpenVertexGoldMap()
    {

        mostProfitableMap = new ArrayList<Long>();
        mapBuildingStepVertexGold = lastVertexGold;
        while (mapBuildingStepVertexGold != null)
        {
            mostProfitableMap.add(mapBuildingStepVertexGold.id);
            printMapAddStep (mapBuildingStepVertexGold);
            goldTotalOnSimulation = goldTotalOnSimulation + mapBuildingStepVertexGold.gold;
            printGold (mapBuildingStepVertexGold);
            mapBuildingStepVertexGold = mapBuildingStepVertexGold.parent;

        }

        //java.util.Collections.reverse(mostProfitableMap);
    }

    public void RunWithCollect()
    {

        //this runs in reverse no need to reverse, -1 (zero based)
        for (int l = mostProfitableMap.size()-1; l >=0; l--){
            long id = (long)mostProfitableMap.get(l);
            currentVertexGold = findNode(goldMap, id);
            game.Node currentVertexGoldNode = currentVertexGold.node;
            if (currentVertexGold.id != firstVertexGold.id) {
                stateEscape.moveTo(currentVertexGoldNode);
            }
            printTimeRemaining();
            if (currentVertexGold.gold > 0){
                stateEscape.pickUpGold();
                goldTotalCollected = goldTotalCollected + currentVertexGold.gold;
                printGoldPickedUp(currentVertexGold);
            }
        }

    }

    public void run() {

        buildGoldMap();

        firstVertexGold = findNode(goldMap, stateEscape.getCurrentNode().getId());
        lastVertexGold = findNode(goldMap, stateEscape.getExit().getId());



        //this must happen in a loop. simulation must make sure steps are sufficient
        //use distance multiplier to fool A* with better distance score for richer tiles
        //do several tests, pick last one tested possible before overestimation escape not possible
        buildMostProfitableMap(1);

        //escape using best map, pickup gold on the way.
        RunWithCollect();
        return;

    }









    public void printVertexCurrent(VertexGold v) {
        System.out.println("==========================");
        System.out.println("ESCAPE VERTEX CURRENT: " + v.id + " g-travelledFromSource:" + v.step + " h-distanceToTarget:" + v.getDistanceToTarget() + " f-weightedDistance:" + v.f);
    }

    public void printVertexClosed(VertexGold v) {
        System.out.println("ESCAPE VERTEX NOW CLOSED:" + v.id + " g-travelledFromSource:" + v.step + " h-distanceToTarget:" + v.getDistanceToTarget() + " f-weightedDistance:" + v.f);
    }

    public void printNeighbourOpen(VertexGold v) {
        System.out.println("ESCAPE neighbour NEW OPEN:" + v.id + " g-travelledFromSource:" + v.step + " h-distanceToTarget:" + v.getDistanceToTarget() + " f-weightedDistance:" + v.f);
    }

    public void printNeighbourExistingOpen(VertexGold v) {
        System.out.println("ESCAPE neighbour EXISTING OPEN:" + v.id + " g-travelledFromSource:" + v.step + " h-distanceToTarget:" + v.getDistanceToTarget() + " f-weightedDistance:" + v.f);
    }

    public void printNeighbourExistingClosed(VertexGold v) {
        System.out.println("ESCAPE neighbour EXISTING CLOSED:" + v.id + " g-travelledFromSource:" + v.step + " h-distanceToTarget:" + v.getDistanceToTarget() + " f-weightedDistance:" + v.f);
    }

    public void printDistanceToTarget() {
        System.out.println("ESCAPE DISTANCE TO TARGET:" + currentVertexGold.getDistanceToTarget());
    }

    public void printMapAddStep(VertexGold v) {
        System.out.println("ESCAPE mapAddStep MAP STEP:" + v.id + " g-travelledFromSource:" + v.step + " h-distanceToTarget:" + v.getDistanceToTarget() + " f-weightedDistance:" + v.f);
    }

    public void printWormholeAddStep(long l) {
        System.out.println("ESCAPE wormholeAddStep MAP STEP:" + l );
    }

    public void printGold(VertexGold v) {
        System.out.println("ESCAPE gold MAP STEP:" + v.gold + " TOTAL GOLD DETECTED:" + goldTotalOnSimulation);
    }

    public void printTimeRemaining() {
        System.out.println("ESCAPE TIME REMAINING:" + stateEscape.getTimeRemaining());
    }

    public void printGoldPickedUp(VertexGold v) {
        System.out.println(" >> GOLD PICKED UP >> :" + v.gold + " TOTAL GOLD COLLECTED DURING THIS ESCAPE:" + goldTotalCollected);
    }


    private synchronized void MoveTo(game.Node node){

        if (stateEscape.getCurrentNode().getId() != node.getId())
            stateEscape.moveTo(node);
    }
}
