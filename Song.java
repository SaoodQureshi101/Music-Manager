public class Song {
    private String title;
    private String artist;
    private String album;
    private int durationMs; // duration in milliseconds

    public Song(String title, String artist, String album, int durationMs) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.durationMs = durationMs;
    }

    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getAlbum() { return album; }
    public int getDurationMs() { return durationMs; }

    public String getDurationString() {
        int totalSeconds = durationMs / 1000;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    @Override
    public String toString() {
        return String.format("%s - %s [%s] (%s)", title, artist, album == null ? "Unknown Album" : album, getDurationString());
    }
}
