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
sends an HTTP request to the scalariform server,
and writes the formatted code to the file.

The client script tries to support all scalariform options.
For example, this will use a custom server URL, read from stdin and 
write to stdout even if the source cannot be parsed correctly:

```sh
echo src/main/scala/Scalariver.scala | ./scalariver --url=http://localhost:8098 --stdin --stdout -f
```

### Using your private server

There is an instance of scalariver deployed on http://river.scalex.org for everybody's use.
By default, the `scalariver` CLI client will use it. 

If you care about code privacy, or are looking for better response time,
then you should run your own scalariver instance.
Clone this repository, then run:

```sh
sbt run 8098
```

It's enough to get the server running on [http://localhost:8098.](http://localhost:8098).

Now tell the `scalariver` client to use your private server:

```sh
export SCALARIVER_URL="http://localhost:8098"
```

### Use with Vim

Simplest integration:

```vim
au BufEnter *.scala setl formatprg=/path/to/scalariver\ --stdin\ --stdout\ -f
```

Exemple with custom server URL and formatting preferences:

```vim
au BufEnter *.scala setl formatprg=/path/to/scalariver\ --url=http://localhost\:8098\ --stdin\ --stdout\ -f\ +rewriteArrowSymbols\ +alignSingleLineCaseStatements\ +compactControlReadability\ +doubleIndentClassDeclaration\ +rewriteArrowSymbols\ +preserveDanglingCloseParenthesis
```

Bonus:

```
" Format the whole file by pressing <leader>f
" This sets a marker on y, goes to the top, formats to the bottom, and returns to the y marker
nmap <leader>f mygggqG'y
```

### Server HTTP API

The only entry point is a `POST` request on `/`.

```sh
curl river.scalex.org -d source="class Foo { }"
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
curl scalariver.org \
  --data-urlencode source@src/main/scala/Scalariver.scala \
  -d scalaVersion=2.11 \
  -d rewriteArrowSymbols=true
```

### Credits

- [scalariform](https://github.com/mdr/scalariform) - best scala formatter in the wild
- [tiscaf](https://github.com/gnieh/tiscaf) - a minimalist and dependency-less HTTP server for scala

### Contribute

It would be nice to have scalariver integration for SBT and your favorite code editor.
