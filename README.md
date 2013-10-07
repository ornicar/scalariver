## A server for scalariform

[Scalariform](https://github.com/mdr/scalariform) is a great tool for formatting scala code,
but the JVM warmup makes it too slow for a CLI application.

By using a client/server architecture, we can fix it.

### Benchmark

```sh
time java -jar ~/bin/scalariform.jar src/main/scala/Scalariver.scala --stdout  
1.99s user 0.07s system 128% cpu 1.603 total
```
Scalariform CLI = 1.6 seconds

```sh
time ./scalariver src/main/scala/Scalariver.scala
0.06s user 0.00s system 98% cpu 0.064 total
```
Scalariver CLI = 0.06 seconds

Scalariver is 25 times faster than scalariform.

### Try it

Get the client, make it executable and run it on some scala file.
```sh
wget https://raw.github.com/ornicar/scalariver/master/scalariver
chmod +x scalariver
./scalariver src/main/scala/Scalariver.scala
```

This `scalariver` script takes a file argument,
sends an HTTP request to the scalariform server,
and prints the formatted scala code to stdout.

If no file argument is given, scalariven will read from stdin:

```sh
echo src/main/scala/Scalariver.scala | ./scalariver
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

Using curl and HTTP API
```vim
nmap <leader>f :% !curl -s river.scalex.org --data-urlencode source@%<cr>
```

**Or** using the scalariver client
```vim
nmap <leader>F :% !/path/to/scalariver %<cr>
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
