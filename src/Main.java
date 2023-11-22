import java.util.*;
import java.util.stream.*;

public class Main {

    static Map<Integer, City> cities = new HashMap<>();
    static Map<Integer, Road> roads = new HashMap<>();

    public static void main(String[] args) {
        showMenu();
    }

    private static void showMenu() {
        System.out.println("Main Menu - Select an action: \n" +
                "1. Help\n" +
                "2. Add\n" +
                "3. Delete\n" +
                "4. Path\n" +
                "5. Exit");

        getMenuOption();
    }

    private static void getMenuOption() {
        try {
            Scanner scanner = new Scanner(System.in);
            int menuOption = scanner.nextInt();

            switch (menuOption) {
                case 1:
                    help();
                    break;
                case 2:
                    addModel();
                    break;
                case 3:
                    deleteModel();
                    break;
                case 4:
                    showPath();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid input. Please enter 1 for more info.");
            }
        } catch (Exception ex) {
            System.out.println("Invalid input. Please enter 1 for more info.");
            getMenuOption();
        }
    }

    private static void showPath() {

        try {
            Scanner scanner = new Scanner(System.in);
            String path = scanner.next();
            String[] splitted= path.split(":");

            int start = Integer.parseInt(splitted[0]);
            int end = Integer.parseInt(splitted[1]);

            //todo: not completed but ...
            List<Road> roads1 =  roads.values().stream().filter(road ->
                         (road.getFrom() == start || road.getThrough().contains(start)
                                && (road.getTo() == end || road.getThrough().contains(end))) )
                     .collect(Collectors.toList());

            roads1.forEach(road -> {
                System.out.println(cities.get(start)+ ":"+cities.get(end)+" via Road "+road.getName()+": Takes 00:01:\n");
            });

        } catch (Exception ex) {
            System.out.println("Invalid input.");
            getMenuOption();
        }
    }

    private static void deleteModel() {

        System.out.println("Select model:\n" +
                "1. City\n" +
                "2. Road");

        try {
            Scanner scanner = new Scanner(System.in);
            int model = scanner.nextInt();

            switch (model) {
                case 1:
                    deleteModel("City");
                    break;
                case 2:
                    deleteModel("Road");
                    break;
                default:
                    System.out.println("Invalid input. Please enter 1 for more info.");
                    showMenu();
            }
        } catch (Exception ex) {
            System.out.println("Invalid input. Please enter 1 for more info.");
            showMenu();
        }

    }

    private static void deleteModel(String model) {
        Scanner scanner = new Scanner(System.in);
        int id = scanner.nextInt();

        if (model.equals("City")) {
            if (cities.containsKey(id)) {
                cities.remove(id);
                showDeletedMessage(model, id, true);
            } else
                showDeletedMessage(model, id, false);
        } else {
            if (roads.containsKey(id)) {
                roads.remove(id);
                showDeletedMessage(model, id, true);
            } else
                showDeletedMessage(model, id, false);
        }

        showMenu();
    }

    private static void showDeletedMessage(String model, int id, boolean success) {
        if (success)
            System.out.println(model + ":" + id + " deleted!");
        else
            System.out.println(model + " with id " + id + " not found!");
    }

    private static void addModel() {

        System.out.println("Select model:\n" +
                "1. City\n" +
                "2. Road");

        try {
            Scanner scanner = new Scanner(System.in);
            int model = scanner.nextInt();

            switch (model) {
                case 1:
                    addCity();
                    break;
                case 2:
                    addRoad();
                    break;
                default:
                    System.out.println("Invalid input. Please enter 1 for more info.");
            }
        } catch (Exception ex) {
            System.out.println("Invalid input. Please enter 1 for more info.");
        }

    }

    private static void addRoad() {

        Scanner scanner = new Scanner(System.in);
        System.out.print("id=?");
        int id = scanner.nextInt();

        System.out.print("name=?");
        String name = scanner.next();

        System.out.print("from=?");
        int from = scanner.nextInt();

        System.out.print("to=?");
        int to = scanner.nextInt();

        System.out.print("through=?");
        String through = scanner.next();
        List<Integer> path;

        if (through == null || through == "" || through.equals("[]"))
            path = new ArrayList<>();
        else {
            path = Stream.of(through.substring(1, through.length() - 2).split(","))
                    .map(String::trim)
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());
        }

        System.out.print("speed_limit=?");
        int speed_limit = scanner.nextInt();

        System.out.print("length=?");
        int length = scanner.nextInt();

        System.out.print("bi_directional=?");
        boolean bi_directional = scanner.nextInt() == 1 ? true : false;

        roads.put(id, new Road(id, name, to, from, path, speed_limit, length, bi_directional));
        showNextMenu("Road", id);
    }

    private static void addCity() {

        Scanner scanner = new Scanner(System.in);
        System.out.print("id=?");
        int id = scanner.nextInt();

        System.out.print("name=?");
        String name = scanner.next();

        cities.put(id, new City(id, name));

        showNextMenu("City", id);
    }

    private static void showNextMenu(String model, int id) {
        System.out.print(model + " with id=" + id + " added! \nSelect your next action \n" +
                "1. Add another " + model + "\n2. Main Menu \n");

        try {
            Scanner scanner = new Scanner(System.in);
            int input = scanner.nextInt();

            switch (input) {
                case 1:
                    if (model.equals("City"))
                        addCity();
                    else
                        addRoad();
                    break;
                case 2:
                    showMenu();
                    break;
                default:
                    System.out.println("Invalid input. Please enter 1 for more info.");
            }
        } catch (Exception ex) {
            System.out.println("Invalid input. Please enter 1 for more info.");
        }

    }

    private static void help() {
        System.out.println(" Select a number from shown menu and enter. For example 1 is for help.");
        showMenu();
        getMenuOption();
    }
}

class City {
    private  int id;
    private String name;
    public City(int id, String name)
    {
        this.id=id;
        this.name=name;
    }
}

class Road {
    private int id;
    private String name;
    private int from;
    private int to;
    private List<Integer> through;
    private int speed_limit;
    private int length;
    private boolean bi_directional;

    public Road(int id, String name, int from, int to, List<Integer> through, int speed_limit, int length, boolean bi_directional) {
        this.id = id;
        this.name = name;
        this.from = from;
        this.to = to;
        this.through = through;
        this.speed_limit = speed_limit;
        this.length = length;
        this.bi_directional = bi_directional;

    }

    int getId() {
        return this.id;
    }

    String getName() {
        return this.name;
    }

    int getFrom() {
        return this.from;
    }

    int getTo() {
        return this.to;
    }

    int getSpeedLimit() {
        return this.speed_limit;
    }

    int getLength() {
        return this.length;
    }

    boolean getBiDirectional() {
        return this.bi_directional;
    }

    List<Integer> getThrough()
    {return this.through;}

}


