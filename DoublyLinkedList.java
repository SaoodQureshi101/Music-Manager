import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DoublyLinkedList {
    private Node head;
    private Node tail;
    private Node current;

    public DoublyLinkedList() {
        head = tail = current = null;
    }

    public void addSong(Song song) {
        Node node = new Node(song);
        if (head == null) {
            head = tail = current = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        System.out.println("Added: " + song);
    }

    public boolean removeSong(String title) {
        Node temp = head;
        while (temp != null) {
            if (temp.song.getTitle().equalsIgnoreCase(title)) {
                if (temp.prev != null) temp.prev.next = temp.next;
                else head = temp.next;

                if (temp.next != null) temp.next.prev = temp.prev;
                else tail = temp.prev;

                // adjust current if it pointed to removed node
                if (current == temp) current = temp.next != null ? temp.next : temp.prev;

                System.out.println("Removed: " + temp.song.getTitle());
                return true;
            }
            temp = temp.next;
        }
        return false;
    }

    public void showPlaylist() {
        if (head == null) {
            System.out.println("Playlist is empty.");
            return;
        }
        System.out.println("\n--- Playlist ---");
        Node temp = head;
        int idx = 1;
        while (temp != null) {
            String marker = (temp == current) ? " <-- Now Playing" : "";
            System.out.println(String.format("%2d. %s%s", idx, temp.song.toString(), marker));
            temp = temp.next;
            idx++;
        }
        System.out.println("----------------\n");
    }

    public void playNext() {
        if (current == null) {
            System.out.println("Playlist is empty."); return;
        }
        if (current.next != null) {
            current = current.next;
            System.out.println("Now playing: " + current.song);
        } else {
            System.out.println("End of playlist reached."); 
        }
    }

    public void playPrevious() {
        if (current == null) {
            System.out.println("Playlist is empty."); return;
        }
        if (current.prev != null) {
            current = current.prev;
            System.out.println("Now playing: " + current.song);
        } else {
            System.out.println("Start of playlist reached."); 
        }
    }

    public Song getCurrentSong() {
        return current == null ? null : current.song;
    }

    public void setCurrentToHead() {
        current = head;
    }

    public void shuffle() {
        List<Song> songs = new ArrayList<>();
        Node temp = head;
        while (temp != null) {
            songs.add(temp.song);
            temp = temp.next;
        }
        Collections.shuffle(songs);
        // rebuild list
        head = tail = current = null;
        for (Song s : songs) addSong(s);
        System.out.println("Playlist shuffled!");
    }

    public void saveToFile(String filepath) {
        try (java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter(filepath))) {
            Node temp = head;
            while (temp != null) {
                // Save as CSV: title|artist|album|durationMs
                pw.println(String.format("%s|%s|%s|%d", escape(temp.song.getTitle()), escape(temp.song.getArtist()), escape(temp.song.getAlbum()), temp.song.getDurationMs()));
                temp = temp.next;
            }
            System.out.println("Playlist saved to " + filepath);
        } catch (Exception e) {
            System.out.println("Error saving playlist: " + e.getMessage());
        }
    }

    public void loadFromFile(String filepath) {
        java.io.File f = new java.io.File(filepath);
        if (!f.exists()) {
            System.out.println("No saved playlist found at " + filepath);
            return;
        }
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(f))) {
            head = tail = current = null;
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|", -1);
                String title = unescape(parts.length > 0 ? parts[0] : "");
                String artist = unescape(parts.length > 1 ? parts[1] : "");
                String album = unescape(parts.length > 2 ? parts[2] : "");
                int dur = 0;
                try { dur = Integer.parseInt(parts.length > 3 ? parts[3] : "0"); } catch (Exception ex) {}
                addSong(new Song(title, artist, album, dur));
            }
            System.out.println("Playlist loaded from " + filepath);
        } catch (Exception e) {
            System.out.println("Error loading playlist: " + e.getMessage());
        }
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("|", "\\|").replace("\n","\\n"); 
    }
    private String unescape(String s) {
        return s == null ? "" : s.replace("\\n","\n").replace("\\|","|"); 
    }
}
