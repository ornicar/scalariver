## A server for scalariform

[Scalariform](https://github.com/mdr/scalariform) is a great tool for formatting scala code,
but the JVM warmup makes it too slow for a CLI application.

By using a client/server architecture, we can fix it.

### Benchmark

```sh
time java -jar ~/bin/scalariform.jar src/main/scala/Scalariver.scala
3.09s user 0.08s system 129% cpu 2.456 total
```
Scalariform CLI = 3.09 seconds

```sh
time ./scalariver src/main/scala/Scalariver.scala
0.07s user 0.03s system 84% cpu 0.153 total
```
Scalariver CLI = 0.07 seconds

Scalariver is 44 times faster than scalariform \o/ Bye bye, JVM warmup.

### Command line client

Get the client, make it executable and run it on some scala file.
```sh
wget https://raw.github.com/ornicar/scalariver/master/scalariver
chmod +x scalariver
./scalariver src/main/scala/Scalariver.scala
```

By default, this `scalariver` script takes a file argument,
sends an HTTP request to the server at http://river.scalex.org,
then writes the formatted code to the file.

The client script supports standard scalariform options.

For example, this line will use some formatting preferences,
read from stdin and write to stdout even if
the source cannot be parsed correctly:

```sh
echo src/main/scala/Scalariver.scala | ./scalariver +rewriteArrowSymbols --indentSpaces=4 --stdin --stdout -f
```

### Using your private server

Everybody can use [http://river.scalex.org](http://river.scalex.org) API freely.
By default, the `scalariver` CLI client uses it.

But if you need to format offline,
or if you feel uncomfortable about
having your code sent to the Internet,
then you should run your own scalariver instance.

```sh
wget http://download.river.scalex.org/scalariver-1.0.jar
java -jar scalariver-1.0.jar
```

**Done!**

You can now specify the new server url to the client:

```sh
./scalariver --url=http://localhost:8098 src/main/scala/Scalariver.scala
```

To make the url change permanent for the scalariver client, add this to your .bashrc/.zshrc:
```sh
export SCALARIVER_URL=http://localhost:8098
```

#### Listen to a custom port

```sh
java -jar scalariver-1.0.jar 12345 # listen to port 12345
```

Scalariver now listens to port 12345

#### Develop

To modify or contribute to scalariver, clone this repository and launch sbt. `sbt run 12345` will start scalariver on port 12345.

### Integration

#### Vim

Simplest integration:

```vim
au BufEnter *.scala setl formatprg=/path/to/scalariver\ --stdin\ --stdout\ -f
```

Exemple with custom server URL and formatting preferences:

```vim
au BufEnter *.scala setl formatprg=/path/to/scalariver\ --url=http://localhost\:8098\ --stdin\ --stdout\ -f\ +rewriteArrowSymbols\ +alignSingleLineCaseStatements\ +compactControlReadability\ +doubleIndentConstructorArguments\ +rewriteArrowSymbols
```

Bonus:

```vim
" Format the whole file by pressing <leader>f
" This sets a marker on y, goes to the top, formats to the bottom, and returns to the y marker
nmap <leader>f mygggqG'y
```

#### Sublime Text

Check the [scalariver sublime text plugin](https://github.com/dohzya/sublime_scalariver) created by [dohzya](https://github.com/dohzya)

#### More wanted!

Do you use your favorite text editor? Wanna integrate scalariver? Cool.

You need to find a way to send the content of
the current file to an external program,
then replace the file content with that program output.
Sounds easy, right? Please share your work!

#### Alternatives

Another way to run scalariform without the JVM warmup is to integrate it with SBT.

See these project skeletons for [playframework 2.1.5](https://gist.github.com/Timshel/7580837) or [playframework 2.2.1](https://gist.github.com/Timshel/7581175).

### Web UI

The server also exposes its functionality through a web form: [http://river.scalex.org](http://river.scalex.org)

### Server HTTP API

The only entry point is a `POST` request on `/`.

```sh
curl river.scalex.org -d source="class A ( n  :Int )"
```

It accepts the following optional parameters:

name | description | default
--- | --- | ---
**source** | scala source code to format | ""
**scalaVersion** | scala version of the source code | 2.10
**initialIndentLevel** | indent level to add to every line | 0
**forceOutput** | print the source unchanged if it cannot be parsed correctly | false

And all [scalariform formatting preference options](https://github.com/mdr/scalariform#preferences).

More complex example:

```sh
curl river.scalex.org \
  --data-urlencode source@src/main/scala/Scalariver.scala \
  -d scalaVersion=2.11 \
  -d rewriteArrowSymbols=true \
  -d indentSpaces=4
```

### Credits

- [scalariform](https://github.com/mdr/scalariform) - best scala formatter in the wild
- [tiscaf](https://github.com/gnieh/tiscaf) - a minimalist and dependency-less HTTP server for scala

### Contribute

It would be nice to have scalariver integration for SBT and your favorite code editor.
