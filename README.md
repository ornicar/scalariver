Scalariform HTTP server

### Motivation

Scalariform is a great tool for formatting scala code. 
But JVM warmup is slow! making it improper to CLI tools.

By running scalariform in an HTTP server, we can get much faster formatting.

### Try it

  ./scalariver src/main/scala/Scalariver.scala

This `scalariver` script takes a file argument,
sends an HTTP request to the scalariform server,
and prints the formatted scala code to stdout.

### Using public server

There is an instance of scalariver deployed on http://scalariver.org for everybody's use.
By default, the `scalariver` CLI client will use it. 

### Using your private server

If you care about code privacy, or are looking for better response time,
then you should run your own scalariver instance.
Clone this repository, then run:

  sbt run 8098

It's enough to get the server running on http://localhost:8098.

To tell the `scalariver` client to use your private server:

  export SCALARIVER_URL="http://localhost:8098"

### API

The only entry point is a `POST` request on `/`.

  curl scalariver.org -d source="class Foo { }"

It accepts the following optional parameters:

|---------------------------------------|
| name | description | default |
| source | scala source code to format | "" |
| scalaVersion | scala version of the source code | 2.10 |
| initialIndentLevel | indent level to add to every line | 0 |
|---------------------------------------|

And all [scalariform formatting preference options](link to readme).

More complex example:

  curl scalariver.org \
    --data-urlencode source@src/main/scala/Scalariver.scala \
    -d scalaVersion=2.11 \
    -d rewriteArrowSymbols=true

### Credits

[tiscaf](link to tiscaf) - a minimalist and dependency-less HTTP server for scala
[scalariform](link) - best scala formatter in the wild

### Contribute

It would be nice to have scalariver integration for SBT and your favorite code editor.
