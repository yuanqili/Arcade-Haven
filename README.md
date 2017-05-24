# Arcade Haven

![img](https://raw.githubusercontent.com/yuanqili/CS48-S17-UCSB/master/resources/logo.png)

## Server

The server runs correctly on CSIL machine. Other platform is not guaranteed.
Copy the code onto a CSIL machine, and runs the server like this. **(Note the
working directory.)**

```sh
g01-draft/program/out/ $ java -cp ../lib/mysql-connector-java-5.1.41-bin.jar:. ServerMain [port=23334]
```

Or you can run this on your local machine.

The default port is 23334, you can use other port as you wish.

## Game

To start the game, runs it like this, where the first argument is host name, the
second argument is port number. If you run it on you local machine, the host
name is "localhost". **(Note the working directory.)**

```sh
g01-draft/program/ $ java -jar out/Arcade-Haven.jar [host="localhost"] [port=23334]
```

You can also run the game calling `make default-game`, which runs game using
the default arguments.

You are allowed to register a new user in the start screen. Or you can use
the following user.

- username: timcook
- password: 123456

## Known issues

It has several issues to be fixed in the next two weeks.

- Collision detection doesn't work properly. Sometimes when Pacman hits a ghost,
  it doesn't die.
- Sometimes the dialog box cannot handle incoming messages properly, it will
  displays a wrong message.
- Sequence number of messages are not synchronized correctly, this is a issue
  caused by Java's GUI library, which makes multi-threading buggy.
