# Progetto di Ingegneria del Software | A.A. 2021-2022

[![SonarQube analysis](https://github.com/mrmotta/ing-sw-2022-milici-motta-negro/actions/workflows/sonarqube.yml/badge.svg?branch=main)](https://github.com/mrmotta/ing-sw-2022-milici-motta-negro/actions/workflows/sonarqube.yml)
[![Maven test and package](https://github.com/mrmotta/ing-sw-2022-milici-motta-negro/actions/workflows/maven.yml/badge.svg)](https://github.com/mrmotta/ing-sw-2022-milici-motta-negro/actions/workflows/maven.yml)

Progetto realizzato da [Riccardo Milici](https://github.com/RiccardoMilici), [Riccardo Motta](https://github.com/mrmotta) e [Matteo Negro](https://github.com/Matteo-Negro).

---

## Esecuzione del software

Il software è suddiviso in due eseguibili: [client.jar](deliverables/jar/client.jar) e [server.jar](deliverables/jar/server.jar).

### Client

Per eseguire il client occorre lanciare da riga di comando la seguente istruzione:

    java -jar client.jar

Di default, questo farà partire l'interfaccia grafica (GUI) del progetto. Se si desidera avere invece avere la scelta dell'interfaccia da lanciare, si possono passare i seguenti parametri:

Parametro      | Descrizione
---------------|------------
`--cli` o `-c` | Lancia il programma da riga di comando (CLI).
`--gui` o `-g` | Lancia il programma da interfaccia grafica (GUI).

Se durante la fase iniziale non si iseriscono i dati del server, l'indirizzo a cui il client cercherà di collegarsi sarà `localhost:36803` (o `127.0.0.1:36803`, che è l'indirizzo a cui viene trasformata la stringa di prima).

### Server

Per eseguire il server occorre lanciare da riga di comando la seguente istruzione:

    java -jar server.jar

In automatico il server creerà una cartella `database` dove salva tutti i dati delle varie partite nella stessa cartella in cui si trova in quel momento e si metterà in ascolto sulla porta `36803`. Per cambiare questi parametri occorre passare al comando uno (o entrambi) dei seguenti parametri:

Parametro            | Argomento | Descrizione
---------------------|-----------|--------------------------------------------------------------------------------------------------------------------
`--database` o `-db` | `path`    | Permette di cambiare il luogo dove verrà creata la cartella `database`, impostanto un persorso assoluto o relativo.
`--port` o `-p`      | `int`     | Permette di cambiare la porta sulla quale il server si metterà in ascolto delle connessioni dei client.

---

## Funzionalità

Abbiamo implementato il gioco in modo tale che soddisfi tutte le regole di una partita (a.k.a. regole complete). Il tutto è fruibile interamente sia tramite intrefaccia grafica (GUI) sia da riga di comando (CLI).

### Funzionalità avanzate

Funzionalità                 | Implementazione
-----------------------------|----------------
Carte personaggio            | 🟢
Partita a 4 giocatori        | 🟢
Partite multiple             | 🟢
Persistenza                  | 🟢
Relienza alle disconnessioni | 🟡

🔴: funzionalità non implementata.

🟡: funzionalità implementa con qualche modifica.

🟢: funzionalità implementa secondo le specifiche.

**Nota**: per la funzionalità della resilienza alle disconnessioni abbiamo deciso di mettere in pausa la partita a tutti i giocatori collegati anziché saltare le rimanenti fasi di quest'ultimo.
