package student;

import game.EscapeState;
import game.ExplorationState;
import sun.jvm.hotspot.debugger.posix.elf.ELFSectionHeader;

import java.util.TreeSet;
import java.util.SortedSet;
import java.util.ArrayList;







public class Explorer {

  /**
   * Explore the cavern, trying to find the orb in as few steps as possible.
   * Once you find the orb, you must return from the function in order to pick
   * it up. If you continue to move after finding the orb rather
   * than returning, it will not count.
   * If you return from this function while not standing on top of the orb,
   * it will count as a failure.
   *   
   * <p>There is no limit to how many steps you can take, but you will receive
   * a score bonus multiplier for finding the orb in fewer steps.</p>
   * 
   * <p>At every step, you only know your current tile's ID and the ID of all
   * open neighbor tiles, as well as the distance to the orb at each of these tiles
   * (ignoring walls and obstacles).</p>
   * 
   * <p>To get information about the current state, use functions
   * getCurrentLocation(),
   * getNeighbours(), and
   * getDistanceToTarget()
   * in ExplorationState.
   * You know you are standing on the orb when getDistanceToTarget() is 0.</p>
   *
   * <p>Use function moveTo(long id) in ExplorationState to move to a neighboring
   * tile by its ID. Doing this will change state to reflect your new position.</p>
   *
   * <p>A suggested first implementation that will always find the orb, but likely won't
   * receive a large bonus multiplier, is a depth-first search.</p>
   *
   * @param state the information available at the current state
   */


    private SortedSet open;
    private SortedSet closed;
    private ExplorationState stateExplore;
    private EscapeState stateEscape;
    private Vertex currentVertex;




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


    public Vertex findNode(SortedSet ss, long id) {

        java.util.Iterator<Vertex> iterator = ss.iterator();
        while(iterator.hasNext()) {
            Vertex v = iterator.next();
            if(v.id == id)
                return v;
        }

        return null;
    }





    //open = unexplored paths
    //closed = all explored
    public void GoToClosestOpenTile(Vertex closestOpenVertex){
        if (closestOpenVertex.id == stateExplore.getCurrentLocation()){
            printVertexOpen(currentVertex);
            System.out.println("GOTO: IS ON CLOSEST NOW");
            return;
        }

        //iterate through close, find current, move to parent
        while (closestOpenVertex.id != currentVertex.id) {

            //TODO inefficient might not be fastest
            System.out.println("GOTO BACK: id: " + currentVertex.parent.id);
            stateExplore.moveTo(currentVertex.parent.id);
            currentVertex = currentVertex.parent;

        }
        printVertexOpen(currentVertex);
    }

    public void ProcessNeighbours (){

        for (java.util.Iterator<game.NodeStatus> i = stateExplore.getNeighbours().iterator(); i.hasNext(); ) {
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
               // }
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





    public void explore(ExplorationState state) {

        int step = 0;
        open = new TreeSet();
        closed = new TreeSet();
        stateExplore = state;

        currentVertex = new Vertex(state.getCurrentLocation(), step, stateExplore.getDistanceToTarget(), null, state);
        open.add(currentVertex);

        while (state.getDistanceToTarget() != 0) {

            GoToClosestOpenTile((Vertex)open.first());


            ProcessNeighbours();




            //decide
            //MoveToClosed(v.id);

            //((Vertex)open.first()).distanceToTarget = 999;
            //Vertex fv = findNode(open,0);
            //open.remove(fv);
            //fv.distanceToTarget = 10;
            //open.add(fv);

            //state.moveTo(((Vertex)open.first()).id);

        }



    }

  /**
   * Escape from the cavern before the ceiling collapses, trying to collect as much
   * gold as possible along the way. Your solution must ALWAYS escape before time runs
   * out, and this should be prioritized above collecting gold.
   *
   * <p>You now have access to the entire underlying graph, which can be accessed 
   * through EscapeState.
   * getCurrentNode() and getExit() will return you Node objects of interest, and getVertices()
   * will return a collection of all nodes on the graph.</p>
   * 
   * <p>Note that time is measured entirely in the number of steps taken, and for each step
   * the time remaining is decremented by the weight of the edge taken. You can use
   * getTimeRemaining() to get the time still remaining, pickUpGold() to pick up any gold
   * on your current tile (this will fail if no such gold exists), and moveTo() to move
   * to a destination node adjacent to your current node.</p>
   * 
   * <p>You must return from this function while standing at the exit. Failing to do so before time
   * runs out or returning from the wrong location will be considered a failed run.</p>
   * 
   * <p>You will always have enough time to escape using the shortest path from the starting
   * position to the exit, although this will not collect much gold.</p>
   *
   * @param state the information available at the current state
   */
  public void escape(EscapeState state) {
    //TODO: Escape from the cavern before time runs out
  }
}
