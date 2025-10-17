
Flatcraft (2D Minecraft Clone)

Ein selbst entwickeltes 2D-Sandbox-Spiel, inspiriert von Minecraft.
Ziel des Projekts war es, prozedurale Weltgenerierung, performante Speicherstrukturen und physikalisch korrekte Kollisionserkennung in einem eigenständigen Spielprototypen umzusetzen.

Hauptfunktionen:

    Bewegung & Sprungphysik
    
    Flüssige 2D-Steuerung mit präziser Kollisionserkennung und stabiler Physik-Logik.


    Dynamisches Blocksystem

        Platzieren und Abbauen von Blöcken

        Drei Blocktypen mit direkter Auswahl

        DDA-basiertes Raycasting, um den Block in Blickrichtung zu treffen - auch bei Hindernissen


    Prozedurale Weltgenerierung

        Unendliche Welt basierend auf Perlin Noise

        Kombination aus Dirt-Oberfläche und Stone-Layern mit Heightmaps

        Multithreading zur ruckelfreien Generierung während des Spielens


    Persistente Welt

        Speicherung aller Änderungen chunkweise (inkl. Subchunks)

        Weltzustand bleibt über Spielsessions hinweg erhalten


    Optimierte Performance

        Thread-basierte Weltgenerierung

        Chunk-Caching und effiziente Datenstruktur

        Minimierte Ladezeiten und stabile Framerate


Technische Umsetzung


Programmiersprache: Java mit der libgdx API

Weltgenerierung: 	Perlin Noise Heightmaps, unendliche Chunk-basierte Welt

Interaktion:	DDA Raycasting für Blockauswahl

Physik:	Kollisionserkennung mit jedem Block

Speichersystem:	Chunk-/Subchunk-basierte Serialisierung

Performance:	Multithreaded World Loading & Saving


Datenspeicherung

Die Spielwelt wird in Chunks aufgeteilt, die asynchron geladen, gespeichert und modifiziert werden.
Spielerinteraktionen (Blockänderungen etc.) werden persistiert, um eine dauerhafte Welt zu gewährleisten.


Ziel & Lernfokus

Dieses Projekt diente als praktische Anwendung und Vertiefung in folgenden Bereichen:

    Prozedurale Generierung & Noise-Algorithmen

    Multithreading & Performance-Optimierung

    Speicherstrukturen (Chunk-Systeme)

    2D-Kollisionssysteme & Physik

    Code-Architektur für erweiterbare Spieleprojekte


Steuerung:


Bewegen: 	Pfeiltasten <-  ->
Springen:	Space
Block abbauen / platzieren:	Linke / Rechte Maustaste
Blocktyp wechseln:	1-9 (Nur die ersten 3 Hotbar Slots enthalten Blöcke) oder Mausrad


![Welt mit vom spieler platzierten Blöcken](https://i.imgur.com/rRVhPEg.png)
![Weltgeneration mit Perlin Noise](https://i.imgur.com/HTkDLkg.png)


Fazit

Dieses Projekt zeigt meine Fähigkeit,

    komplexe Systeme wie prozedurale Generierung und Threading zu implementieren,

    saubere Datenstrukturen zu entwerfen,

    und Gameplay-Mechaniken technisch solide umzusetzen.


Es demonstriert technisches Verständnis, Lösungsorientierung und die Fähigkeit, komplexe Probleme (z. B. Weltpersistenz oder Kollision) eigenständig zu lösen.
