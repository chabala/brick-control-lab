## Building

brick-control-lab uses [Maven Central][1] for distribution of pre-built jars. See [Usage](usage.html) to use the
pre-built jars. Building from source is only necessary for developing brick-control-lab itself.

[1]: https://search.maven.org/search?q=g:org.chabala.brick%20AND%20a:brick-control-lab&core=gav

### Prerequisites to build from source
* Java 8
* Maven 3.5.3 or newer

### Clone the source repository
```
$ git clone git@github.com:chabala/brick-control-lab.git
```

See [Source Code Management](source-repository.html) for more details regarding this.

### Building
Run ```mvn clean verify``` to clean, compile, test, and produce the artifact.
