# PictureX - Image Viewer and Processor

![alt text](https://github.com/aleksandarstojkovski/PictureX-Image-Processor/blob/master/img/screenshots/picturex-main-full.png)

### Descrizione
PictureX è un software per la elaborazione delle immagini in formato JPG e PNG. 
Il programma permette all’utente di visualizzare le immagini di una directory (in forma di miniatura/anteprima). 
L'utente può visualizzare i metadati principali (dati dell'immagine e metadati EXIF) selezionando un'immagine. 
L'utente può filtrare la visualizzazione attraverso dei pattern applicati al nome del file. 
L'utente può visualizzare un'immagine con livelli di zoom a sua scelta. 
Il programma deve inoltre permettere all’utente di applicare alle immagini una o più modifiche (per es. scala, ruota,...) e di salvare il risultato in un nuovo file.

### Requisiti Espliciti
* Il programma deve permettere la visualizzazione (miniatura/anteprima) delle immagini di una cartella.
* L'utente può visualizzare un'immagine con livelli di zoom a sua scelta. 
* L'utente può filtrare la lista delle immagini attraverso un pattern (globbing) sul nome del file.
* L'utente deve poter cambiare cartella in qualsiasi momento (navigazione).
* Il programma visualizza le informazioni dell'immagine selezionata (tipo, risoluzione, dimensione, dimensione del file, metadati EXIF, ...).
* Il programma permette di selezionare una o più immagini per effettuare modifiche (selezione multipla).
* Il programma permette diverse operazioni di modifica (scala, ruota, converti in bianco e nero, taglia, ...).
* Il programma memorizza le operazioni di modifica effettuate su un'immagine in un file di testo (logging).
* Il programma mostra un'anteprima delle modifiche selezionate.
* L'utente permette di salvare l'immagine modificata in un nuovo file (save as).
* Il programma dovrà supportare la localizzazione in almeno due lingue (inglese, italiano).

### Personalizzazioni
* UI ispirata a HIG di Applle (MacOS)

### Tecnologie
* Java e JavaFX
* Librerie di manipolazione di immagini per Java (disponibili su Maven) 
* Librerie per l'estrazione dei metadati EXIF (disponibili su Maven)

### Piattaforme
* Windows
* Linux
* OSX (opzionale)
