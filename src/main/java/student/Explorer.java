package student;

import game.EscapeState;
import game.ExplorationState;
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

    public void vertex_print(Vertex v) {
        System.out.println("==========================");
        System.out.println("vertex:" + v.id + " g-travelledFromSource:" + v.step + " h-distanceToTarget:" + v.distanceToTarget + " f-weightedDistance:" + v.f);
    }

    public void neighbour_print(Vertex v) {
        System.out.println("neighbour:" + v.id + " g-travelledFromSource:" + v.step + " h-distanceToTarget:" + v.distanceToTarget + " f-weightedDistance:" + v.f);
    }

    public Vertex findNode(SortedSet ss, int id) {

        java.util.Iterator<Vertex> iterator = ss.iterator();
        while(iterator.hasNext()) {
            Vertex v = iterator.next();
            if(v.id == id)
                return v;
        }

        return null;
    }

    public void MoveToClosed(long id)
    {
        Vertex fv = findNode(open,id);
        open.remove(fv);
        closed.add(fv);
    }

    public void explore(ExplorationState state) {
        //TODO:
        int step = 0;
        open = new TreeSet();
        closed = new TreeSet();

        //ArrayList open = new ArrayList();
        //ArrayList closed = new ArrayList();

        //populate first step
        Vertex v = new Vertex(
                state.getCurrentLocation(),
                step,
                state.getDistanceToTarget(),
                null,
                state);

        open.add (v);
        vertex_print(v);
        next_id = v.id;

        while (state.getDistanceToTarget() != 0) {

            v = findNode(open, next_id);


            for (java.util.Iterator<game.NodeStatus> i = state.getNeighbours().iterator(); i.hasNext(); ) {
                game.NodeStatus n = i.next();
                Vertex vn = new Vertex(n.getId(), v.step + 1, n.getDistanceToTarget(), v, null);
                neighbour_print(vn);

                open.add (vn);

            }


            //decide
            MoveToClosed(v.id);

            //((Vertex)open.first()).distanceToTarget = 999;
            Vertex fv = findNode(open,0);
            open.remove(fv);
            fv.distanceToTarget = 10;
            open.add(fv);

            state.moveTo(((Vertex)open.first()).id);

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
