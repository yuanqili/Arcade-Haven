# Arcade Haven

## Server end

The server runs correctly on CSIL machine. Other platform is not guaranteed.
Copy the code onto a CSIL machine, and runs the server like this

```sh
g01-draft/program/out/ $ java -cp ../lib/mysql-connector-java-5.1.41-bin.jar:. ServerMain
```

Or you can run this on your local machine.

## Game

To start the game, runs it like this, where the first argument is host name, the
second argument is port number. If you run it on you local machine, the host
name is "localhost".

```sh
g01-draft/program/out/ $ java -jar out/Arcade-Haven.jar "csil-08.cs.ucsb.edu" 23333
```

## Known issues

It has several issues to be fixed in the next two weeks.

- Collision detection doesn't work properly. Sometimes when Pacman hits a ghost,
  it doesn't die.
- Sometimes the dialog box cannot handle incoming messages properly, it will
  displays a wrong message.
