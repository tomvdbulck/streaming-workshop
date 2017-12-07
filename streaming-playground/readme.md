# Apache Flink

## Install
https://ci.apache.org/projects/flink/flink-docs-release-1.2/quickstart/setup_quickstart.html

Download
http://flink.apache.org/downloads.html

Start a local cluster
```
cd flink-1.3.2

./bin/start-local.sh

```


```
brew install apache-flink
```

Then go to the install folder:

```
cd /usr/local/Cellar/apache-flink/1.3.2


./bin/start-local.sh
```


Go to http://localhost:8081 and check it is up.

Open netstat to port 9000
```
nc -l 9000
```

Run SocketWindowWordCount.java with as argument:  --port 9000


```
nc -l 9000
a word
and another word
more words
word word word
all but words
```


Stop the local server
```
./bin/stop-local.sh
```

# Apache Spark

Download Apache Spark on : http://spark.apache.org/downloads.html


Run SocketWordCount
```
nc -l 9999
a word
and another word
more words
word word word
all but words
super original
```

words will be printed after every run (5 seconds)

```
...
-------------------------------------------
Time: 1512680975000 ms
-------------------------------------------
(a,1)
(word,1)

...
```
