# scala-spark

Project to demonstrate spark functionality using a simple word count example.


## Prerequisites

* Java 11
* Sbt 1.5.3
* Python 3

### pre-commit

1.  OPTIONAL: create a virtual environment to install pre-commit and activate it in the shell e.g.
    ```shell
    python -m venv .venv
    . .venv/bin/activate 
    ```
    Note: it's recommended to create a new environment for each project that uses `pre-commit` so that the configurations
    do not need to be initialised when switching projects.  The environment must be active in the console when running
    `git commit`.

2.  Install `pre-commit`
    ```shell
    pip install pre-commit
    ```
    
3.  Configure `pre-commit`
    ```shell
    pre-commit install
    ```

## Format

```shell
sbt scalafmt
```

```shell
sbt scalafmtSbt
```
The project uses the `scalafmt` plugin and rules defined in `.scala.fmt.conf`.  More advanced execution options are 
available at https://scalameta.org/scalafmt/docs/installation.html#task-keys

## Compile

```shell
sbt clean compile
```

## Test

### Unit and integration

```shell
sbt clean test
```

### Coverage

```shell
sbt jacoco
```

## Package

### Fat JAR

```shell
sbt clean assembly
```

### Docker

```shell
sbt clean Docker/publishLocal
```

## Execute

TBD
