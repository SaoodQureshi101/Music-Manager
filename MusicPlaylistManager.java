import java.util.Scanner;

public class MusicPlaylistManager {
    private static final String SAVE_FILE = "src/playlist.txt";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DoublyLinkedList playlist = new DoublyLinkedList();

        // Attempt to load saved playlist
        playlist.loadFromFile(SAVE_FILE);

        int choice = -1;
        while (choice != 0) {
            printMenu();
            try {
                System.out.print("Enter choice: ");
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                choice = -1;
            }

            switch (choice) {
                case 1:
                    System.out.print("Title: ");
                    String title = sc.nextLine();
                    System.out.print("Artist: ");
                    String artist = sc.nextLine();
                    System.out.print("Album: ");
                    String album = sc.nextLine();
                    System.out.print("Duration (ms): ");
                    int dur = 0;
                    try { dur = Integer.parseInt(sc.nextLine().trim()); } catch (Exception ex) {}
                    playlist.addSong(new Song(title, artist, album, dur));
                    break;
                case 2:
                    System.out.print("Search iTunes for song title (e.g. 'Believer Imagine Dragons'): ");
                    String q = sc.nextLine();
                    Song s = iTunesFetcher.fetchFirstMatch(q);
                    if (s == null) {
                        System.out.println("No result from iTunes for: " + q);
                    } else {
                        System.out.println("Found: " + s);
                        System.out.print("Add this to playlist? (y/n): ");
                        String ans = sc.nextLine().trim().toLowerCase();
                        if (ans.equals("y") || ans.equals("yes")) {
                            playlist.addSong(s);
                        } else {
                            System.out.println("Not added.");
                        }
                    }
                    break;
                case 3:
                    System.out.print("Enter title to remove: ");
                    String rt = sc.nextLine();
                    boolean removed = playlist.removeSong(rt);
                    if (!removed) System.out.println("Song not found: " + rt);
                    break;
                case 4:
                    playlist.showPlaylist();
                    break;
                case 5:
                    playlist.playNext();
                    break;
                case 6:
                    playlist.playPrevious();
                    break;
                case 7:
                    playlist.shuffle();
                    break;
                case 8:
                    playlist.saveToFile(SAVE_FILE);
                    break;
                case 9:
                    playlist.loadFromFile(SAVE_FILE);
                    break;
                case 0:
                    System.out.println("Exiting. Goodbye!"); break;
                default:
                    System.out.println("Invalid choice. Try again."); break;
            }
        }

        sc.close();
    }

    private static void printMenu() {
        System.out.println("\n=== Music Playlist Manager ===");
        System.out.println("1. Add song manually");
        System.out.println("2. Search iTunes and add");
        System.out.println("3. Remove song by title");
        System.out.println("4. Show playlist");
        System.out.println("5. Play next");
        System.out.println("6. Play previous");
        System.out.println("7. Shuffle playlist");
        System.out.println("8. Save playlist to file");
        System.out.println("9. Load playlist from file");
        System.out.println("0. Exit");
    }
}
