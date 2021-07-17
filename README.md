# scala-spark

Project to demonstrate spark functionality using a simple word count example.

Note: Depending on the system, it may be necessary to add `sudo` before all commands and scripts that interact with Docker.

## Prerequisites

* Java 11
* Sbt 1.5.3
* Python 3
* Minio CLI client (https://docs.min.io/docs/minio-client-quickstart-guide.html)

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

### Local

1.  Download and extract Apache Spark 3.0.x. 

2.  Create a directory and copy the input file to it:
    ```shell
    mkdir -p /tmp/wordcount/input
    cp src/it/resources/data/declaration_of_independence.txt /tmp/wordcount/input/.
    ```

3.  Build the fat JAR.

4.  Execute the application:
    ```shell
    $SPARK_HOME/bin/spark-submit --master "local[2]" \
    --name scala-spark \
    --conf "spark.executor.extraJavaOptions=-Dconfig.file=src/it/resources/local/application.conf" \
    --conf "spark.driver.extraJavaOptions=-Dconfig.file=src/it/resources/local/application.conf" \
    --class uk.org.aeb.Main \
    target/scala-spark.jar
    ```
    Where `$SPARK_HOME` is the home directory of Apache Spark on your machine.

5.  Inspect the output at `/tmp/wordcount/output`

### Local kubernetes

Note: Parquet and Delta format files result in a runtime error when the application attempts to write them to MinIO.  Only CSV files are written successfully.

1.  Create a kind kubernetes cluster by running:
    ```shell
    ./src/it/k8s/kind/create_local_cluster_with_registry.sh -c spark-scala -n registry -p 5000
    ```
    This will also create a private docker registry, `registry:2`, accessible on port 5000.

2.  Set `kubectl` to use the cluster by executing:
    ```shell
    kubectl cluster-info --context kind-spark-scala
    ```

3.  Download, tag and push the minio Docker image to the private Docker registry:
    ```shell
    docker pull minio/minio:RELEASE.2021-06-17T00-10-46Z
    docker tag minio/minio:RELEASE.2021-06-17T00-10-46Z localhost:5000/minio/minio:RELEASE.2021-06-17T00-10-46Z
    docker push localhost:5000/minio/minio:RELEASE.2021-06-17T00-10-46Z
    ```
    
4.  Build the application Docker image.

5.  Tag and push the application Docker image to the private Docker registry:
    ```shell
    docker tag scala-spark:0.1 localhost:5000/scala-spark:0.1
    docker push localhost:5000/scala-spark:0.1 
    ```

6.  Deploy the `minio` cluster:
    ```shell
    kubectl apply -f src/it/k8s/minio/
    ```

7.  Forward the `minio` cluster port:
    ```shell
    kubectl port-forward service/minio-service 30090:9000
    ```
    
8.  Populate the `minio` cluster:
    ```shell
    ./src/it/resources/minio/init_minio.sh -c $MC_PATH
    ```
    where `$MC_PATH` is the directory the `mc` client is located in on your local machine.

9.  Execute the spark job:
    ```shell
    kubectl apply -f src/it/k8s/spark/
    ```
