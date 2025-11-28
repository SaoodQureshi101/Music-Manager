# Music Playlist Manager (with iTunes search)
Author: Saood Ahmed Qureshi

## Description
A console-based Music Playlist Manager implemented in Java using a Doubly Linked List.
Features include adding/removing songs, forward/backward navigation, shuffle, save/load,
and searching iTunes for song metadata (title, artist, album, duration).

## How iTunes integration works
This project uses the public **iTunes Search API** (no key required). When you choose
"Search iTunes and add", the program queries the iTunes API and parses the first result
returned. This is done with plain `HttpURLConnection` and a lightweight string parser
(so no external JSON libraries are required).

## How to compile and run
1. Make sure you have Java 8+ installed.
2. From the `src` directory, compile all files:
   ```bash
   javac *.java
   ```
3. Run the program:
   ```bash
   java MusicPlaylistManager
   ```

## Files
- `src/Song.java` - Song model
- `src/Node.java` - Doubly linked list node
- `src/DoublyLinkedList.java` - Playlist implementation
- `src/iTunesFetcher.java` - Calls iTunes Search API and parses first result
- `src/MusicPlaylistManager.java` - Main menu-driven program
- `src/playlist.txt` - (optional) saved playlist created by the program

## Notes and Limitations
- The iTunes Search API is public and rate-limited; use responsibly.
- The JSON parsing in `iTunesFetcher` is minimal and tailored to expected responses; for
production use, prefer a JSON library such as `org.json` or `Gson`.
