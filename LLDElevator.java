package LLDElevatorDesign;

import java.util.*;

// Enums for direction and state
enum Direction {
    UP, DOWN, IDLE
}

enum ElevatorState {
    IDLE, MOVING
}

// Class to manage trips and requests
class ElevatorController {
    private PriorityQueue<Integer> upQueue;
    private PriorityQueue<Integer> downQueue;
    private ElevatorCar elevatorCar;

    public ElevatorController(ElevatorCar elevatorCar) {
        this.elevatorCar = elevatorCar;
        upQueue = new PriorityQueue<>();
        downQueue = new PriorityQueue<>(Collections.reverseOrder());
    }

    // External request from a floor
    public void submitExternalRequest(int floor, Direction direction) {
        if (direction == Direction.UP) {
            upQueue.offer(floor);
        } else {
            downQueue.offer(floor);
        }
    }

    // Internal request from inside the elevator
    public void submitInternalRequest(int floor) {
        if (floor > elevatorCar.getCurrentFloor()) {
            upQueue.offer(floor);
        } else {
            downQueue.offer(floor);
        }
    }

    // Elevator operation logic
    public void controlElevator() {
        new Thread(() -> {
            while (true) {
                if (!upQueue.isEmpty()) {
                    processRequests(upQueue, Direction.UP);
                } else if (!downQueue.isEmpty()) {
                    processRequests(downQueue, Direction.DOWN);
                } else {
                    elevatorCar.setState(ElevatorState.IDLE);
                }

                try {
                    Thread.sleep(500); // Simulating delay between actions
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void processRequests(PriorityQueue<Integer> queue, Direction direction) {
        elevatorCar.setDirection(direction);
        while (!queue.isEmpty()) {
            int nextFloor = queue.poll();
            elevatorCar.moveToFloor(nextFloor);
        }
    }
}

// Class for elevator car operations
class ElevatorCar {
    private int id;
    private int currentFloor;
    private ElevatorState state;
    private Direction direction;
    private ElevatorDisplay display;

    public ElevatorCar(int id) {
        this.id = id;
        this.currentFloor = 1; // Default starting floor
        this.state = ElevatorState.IDLE;
        this.direction = Direction.IDLE;
        this.display = new ElevatorDisplay();
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setState(ElevatorState state) {
        this.state = state;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void moveToFloor(int floor) {
        System.out.println("Elevator " + id + " moving to floor " + floor);
        while (currentFloor != floor) {
            if (floor > currentFloor) currentFloor++;
            else currentFloor--;
            display.updateDisplay(currentFloor, direction);
        }
        System.out.println("Elevator " + id + " reached floor " + floor);
        new ElevatorDoor().operate();
    }
}

// Class for elevator display
class ElevatorDisplay {
    private int currentFloor;
    private Direction direction;

    public void updateDisplay(int floor, Direction direction) {
        this.currentFloor = floor;
        this.direction = direction;
        System.out.println("Current Floor: " + currentFloor + ", Direction: " + direction);
    }
}

// Class for managing elevator doors
class ElevatorDoor {
    public void operate() {
        System.out.println("Door Opening...");
        System.out.println("Door Closing...");
    }
}

// Floor class for external requests
class Floor {
    private int level;
    private ExternalDispatcher dispatcher;

    public Floor(int level, ExternalDispatcher dispatcher) {
        this.level = level;
        this.dispatcher = dispatcher;
    }

    public void pressButton(Direction direction) {
        dispatcher.handleRequest(level, direction);
    }
}

// Dispatcher for handling external requests
class ExternalDispatcher {
    private List<ElevatorController> controllers;

    public ExternalDispatcher(List<ElevatorController> controllers) {
        this.controllers = controllers;
    }

    public void handleRequest(int floor, Direction direction) {
        ElevatorController bestController = controllers.get(0); // Logic to select the best elevator
        bestController.submitExternalRequest(floor, direction);
    }
}

// Main building class
class Building {
    private List<Floor> floors;
    private List<ElevatorController> controllers;

    public Building(int numFloors, int numElevators) {
        floors = new ArrayList<>();
        controllers = new ArrayList<>();

        ExternalDispatcher dispatcher = new ExternalDispatcher(controllers);
        for (int i = 1; i <= numFloors; i++) {
            floors.add(new Floor(i, dispatcher));
        }
        for (int i = 1; i <= numElevators; i++) {
            ElevatorController controller = new ElevatorController(new ElevatorCar(i));
            controllers.add(controller);
            controller.controlElevator(); // Start elevator control in a separate thread
        }
    }

    public void pressButton(int floor, Direction direction) {
        floors.get(floor - 1).pressButton(direction);
    }
}

// Main class
public class Main {
    public static void main(String[] args) {
        Building building = new Building(10, 2);

        building.pressButton(5, Direction.UP);
        building.pressButton(3, Direction.DOWN);

        // Simulating delay for user actions
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        building.pressButton(8, Direction.UP);
        building.pressButton(1, Direction.DOWN);
    }
}
