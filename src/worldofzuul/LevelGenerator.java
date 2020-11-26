package worldofzuul;

import java.util.ArrayList;
import java.util.HashSet;
import worldofzuul.Room.ROOMTYPE;

public class LevelGenerator {

    Room[][] rooms;
    int startX;
    int startY;

    public LevelGenerator(Room[][] rooms, int startX, int startY) {
        this.rooms = rooms;
        this.startX = startX;
        this.startY = startY;
    }

    public void setRandomRoomTypes() {
        for (int row = 0; row < rooms.length - 1; row++) {
            for (int col = 0; col < rooms[row].length - 1; col++) {
                Room room = rooms[row][col];
                room.setRoomType(getRandomRoomType());
            }
        }
    }

    public void createRooms() {
        for (int row = 0; row < rooms.length; row++) {
            for (int col = 0; col < rooms[row].length; col++) {
                rooms[row][col] = new Room("Row: " + row + ", Col: " + col);
            }
        }
    }

    public void createExits() {
        for (int row = 0; row < rooms.length; row++) {
            for (int col = 0; col < rooms[row].length; col++) {
                Room loopedRoom = rooms[row][col];

                if (row - 1 >= 0) {
                    loopedRoom.setExit("north", rooms[row - 1][col]);
                }

                if (col + 1 <= (rooms[row].length - 1)) {
                    loopedRoom.setExit("east", rooms[row][col + 1]);
                }

                if (row + 1 <= (rooms.length - 1)) {
                    loopedRoom.setExit("south", rooms[row + 1][col]);
                }

                if (col - 1 >= 0) {
                    loopedRoom.setExit("west", rooms[row][col - 1]);
                }
            }
        }
    }

    public void removeExitsFromVoid() {
        ArrayList<Room> voidRooms = new ArrayList<>();
        for (Room[] rArr : rooms) {
            for (Room r : rArr) {
                if (r.getRoomType() == Room.ROOMTYPE.VOID) {
                    voidRooms.add(r);
                }
            }
        }

        for (Room r : voidRooms) {
            HashSet<String> neighbors = new HashSet<>(r.getExits().keySet());
            for (String neighborString : neighbors) {
                Room neighbor = r.getExit(neighborString);
                if (neighborString == "north") {
                    neighbor.removeExit("south");
                } else if (neighborString == "east") {
                    neighbor.removeExit("west");
                } else if (neighborString == "south") {
                    neighbor.removeExit("north");
                } else if (neighborString == "west") {
                    neighbor.removeExit("east");
                }
                r.removeExit(neighborString);

            }
        }
    }

    public ArrayList<Room> getRoomsFromAtoB(int startRoomX, int startRoomY, int endRoomX, int endRoomY) {
        ArrayList<Room> roomPath = new ArrayList<Room>();
        Room endRoom = rooms[endRoomX][endRoomY];
        Room currentRoom = rooms[startRoomX][startRoomY];
        int xMax = rooms.length;
        int yMax = rooms.length;
        int steps;
        while (true) {
            steps = (int) (Math.random() * yMax);
            yMax -= steps;
            while (steps > 0) {
                roomPath.add(currentRoom);
                currentRoom = currentRoom.getExit("south");
                steps--;
                if (currentRoom == endRoom) {
                    return roomPath;
                }
            }

            steps = (int) (Math.random() * xMax);
            xMax -= steps;
            while (steps > 0) {
                roomPath.add(currentRoom);
                currentRoom = currentRoom.getExit("east");
                steps--;
                if (currentRoom == endRoom) {
                    return roomPath;
                }
            }
        }
    }

    public ArrayList<Room> getPath(int[] startPoint, int[] endPoint) {
        ArrayList<Room> path = new ArrayList<Room>();
        int xDif = endPoint[0] - startPoint[0];
        int yDif = endPoint[1] - startPoint[1];

        while (true) {

            if (Math.random() > 0.5) {
                if (xDif != 0) {
                    path.add(rooms[startPoint[0] + xDif][startPoint[1] + yDif]);
                    xDif = oneCloserToZero(xDif);
                }
            } else {
                if (yDif != 0) {
                    path.add(rooms[startPoint[0] + xDif][startPoint[1] + yDif]);
                    yDif = oneCloserToZero(yDif);
                }
            }
            if (xDif == 0 && yDif == 0) {
                break;
            }
        }
        return path;
    }

    public ArrayList<int[]> getPoints(int maxSize, int amount) {
        ArrayList<int[]> points = new ArrayList<>();
        for (int i = (int) (Math.random() * (maxSize * 2 + 1)); i < (rooms.length - (maxSize * 2)) - 2; i += maxSize * 2 + 2) {
            for (int j = (int) (Math.random() * (maxSize * 2 + 1)); j < (rooms.length - (maxSize * 2)) - 2; j += maxSize * 2 + 2) {
                int[] point = {i + maxSize + 1, j + maxSize + 1};
                points.add(point);
                //System.out.println("i: " + (i + maxSize + 1) + " j: " + (j + maxSize + 1));
            }
        }
        while (points.size() > amount) {
            int randomRoom = (int) (Math.random() * points.size());
            points.remove(randomRoom);
        }
        for (int[] point : points) {
            // System.out.println(point[0] + " " + point[1]);
        }
        return points;
    }
    
    public ArrayList<ArrayList<Room>> getSpacedRooms(ArrayList<int[]> points) {

        ArrayList<ArrayList<Room>> spacedRooms = new ArrayList<>();
        ArrayList<Room> randomPoints = new ArrayList<Room>();

        for (int[] point : points) {
            randomPoints.add(rooms[point[0]][point[1]]);
        }

        for (Room room : randomPoints) {
            ArrayList<Room> cluster = new ArrayList<Room>();
            room.setRoomType(ROOMTYPE.HALLWAY);
            cluster.add(room);
            spacedRooms.add(cluster);
        }

        //System.out.println(randomPoints.size());
        return spacedRooms;
    }
    
    public void createNPC(NPC npc, ArrayList<NPC> npcs) {
        npcs.add(npc);
    }

    public void setNPCRandomPosition(ArrayList<NPC> npcs) {
        for (NPC n : npcs) {

            do {
                int randomX = (int) (Math.random() * rooms.length);
                int randomY = (int) (Math.random() * rooms.length);
                Room randomRoom = rooms[randomX][randomY];

                boolean found = false;

                for (NPC nPos : npcs) {
                    if (randomRoom == nPos.getCurrentRoom() || randomRoom == rooms[startX][startY] || randomRoom.getRoomType() == Room.ROOMTYPE.VOID) {
                        found = true;
                    }
                }

                if (!found) { // Proceed with adding NPC to 
                    randomRoom.addNPC(n);

                    // Print room that guard was added to!
                    System.out.println(n.getName() + " was added to room: " + randomRoom.getShortDescription());
                    break;
                }

            } while (true);

        }

        // Set next room for all guards
        for (NPC n : npcs) {
            if (n instanceof NPC_Guard) {
                NPC_Guard guard = (NPC_Guard) n;
                guard.setNextRoom();
            }
        }
    }

    public void placeItemRandom(Item item) {
        getRandomNonVoidRoom().addItem(item);
    }

    public Room getRandomNonVoidRoom() {
        Room room = null;
        do {
            room = rooms[(int) (Math.random() * rooms.length)][(int) (Math.random() * rooms.length)];
            if (room.getRoomType() != Room.ROOMTYPE.VOID) {
                return room;
            }
        } while (true);
    }

    public Room getRandomRoomFromArray(ArrayList<Room> facultyRooms) {

        return facultyRooms.get((int) (facultyRooms.size() * Math.random()));
    }

    public ArrayList<Room> ExpandedFaculty(ArrayList<Room> startRooms, int maxFacultySize) {
        ArrayList<Room> faculty = new ArrayList(startRooms);
        int verticalSize = maxFacultySize;
        int horizontalSize = maxFacultySize;

        int verticalCount = 0;
        int horizontalCount = 0;

        float enlarge = 0.5f;

        for (int i = 0; i < verticalSize; i++) {
            if (Math.random() > enlarge) {
                faculty = expandDirection(faculty, "north");
                verticalCount++;
            }
        }
        for (int i = 0; i < verticalSize; i++) {
            if (Math.random() > enlarge) {
                faculty = expandDirection(faculty, "south");
                verticalCount++;
            }
        }
        if (verticalCount == 0) {
            if (Math.random() > enlarge) {
                faculty = expandDirection(faculty, "north");
            } else {
                faculty = expandDirection(faculty, "south");
            }
        }
        for (int i = 0; i < horizontalSize; i++) {
            if (Math.random() > enlarge) {
                faculty = expandDirection(faculty, "east");
                horizontalCount++;
            }
        }
        for (int i = 0; i < horizontalSize; i++) {
            if (Math.random() > enlarge) {
                faculty = expandDirection(faculty, "west");
                horizontalCount++;
            }
        }
        if (horizontalCount == 0) {
            if (Math.random() > 0.5) {
                faculty = expandDirection(faculty, "east");
            } else {
                faculty = expandDirection(faculty, "west");
            }
        }
        System.out.println(faculty.size());
        return faculty;
    }

    public static void setRoomTypeAll(ArrayList<Room> roomArray, Room.ROOMTYPE roomType) {
        for (Room r : roomArray) {
            r.setRoomType(roomType);
        }
    }

    private static Room.ROOMTYPE getRandomRoomType() {
        return Room.ROOMTYPE.values()[(int) (Math.random() * Room.ROOMTYPE.values().length)];
    }

    private ArrayList<Room> expandDirection(ArrayList<Room> facultyRooms, String direction) {
        ArrayList<Room> faculty = new ArrayList(facultyRooms);

        for (Room room : facultyRooms) {
            if (room.getExit(direction).getRoomType() == ROOMTYPE.VOID) {
                faculty.add(room.getExit(direction));
                room.getExit(direction).setRoomType(ROOMTYPE.HALLWAY);
            }
        }
        return faculty;
    }

    private int oneCloserToZero(int num) {
        if (num != 0) {
            if (num > 0) {
                num--;
            } else {
                num++;
            };
        }
        return num;
    }
}
