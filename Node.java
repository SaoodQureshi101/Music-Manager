public class Node {
    public Song song;
    public Node next;
    public Node prev;

    public Node(Song song) {
        this.song = song;
        this.next = null;
        this.prev = null;
    }
}
