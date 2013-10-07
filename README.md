Scalariform HTTP server

### Motivation

Scalariform is a great tool for formatting scala code. 
But JVM warmup is slow! making it improper to CLI tools.

By running scalariform in an HTTP server, we can get much faster formatting.

### Try it

```sh
./scalariver src/main/scala/Scalariver.scala
```

This `scalariver` script takes a file argument,
sends an HTTP request to the scalariform server,
and prints the formatted scala code to stdout.

### Using public server

There is an instance of scalariver deployed on http://river.scalex.org for everybody's use.
By default, the `scalariver` CLI client will use it. 

### Using your private server

If you care about code privacy, or are looking for better response time,
then you should run your own scalariver instance.
Clone this repository, then run:

```sh
sbt run 8098
```

It's enough to get the server running on http://localhost:8098.

To tell the `scalariver` client to use your private server:

```sh
export SCALARIVER_URL="http://localhost:8098"
```

### API

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

### Benchmark

Quick and dirty: 

```sh
time java -jar ~/bin/scalariform.jar src/main/scala/Scalariver.scala --stdout  
1.99s user 0.07s system 128% cpu 1.603 total
```

```sh
time ./scalariver src/main/scala/Scalariver.scala
0.06s user 0.00s system 98% cpu 0.064 total
```

Scalariver is 25 times faster than scalariform.

### Use with Vim

#### curl and HTTP API

```vim
nmap <leader>f :% !curl -s river.scalex.org --data-urlencode source@%<cr>
```

#### **Or** using the scalariver client

```vim
nmap <leader>F :% !/path/to/scalariver %<cr>
```

### Credits

[tiscaf](https://github.com/gnieh/tiscaf) - a minimalist and dependency-less HTTP server for scala
[scalariform](https://github.com/mdr/scalariform) - best scala formatter in the wild

### Contribute

It would be nice to have scalariver integration for SBT and your favorite code editor.
