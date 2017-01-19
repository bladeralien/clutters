name := "hbase_test"

version := "1.0"

scalaVersion := "2.11.8"

resolvers += "Apache HBase" at "https://repository.apache.org/content/repositories/releases"

resolvers += "Thrift" at "http://people.apache.org/~rawson/repo/"

libraryDependencies ++= Seq(
  "org.apache.hadoop" % "hadoop-core" % "0.20.2",
  "org.apache.hbase" % "hbase-client" % "1.2.0-cdh5.8.2",
  "org.apache.hbase" % "hbase-common" % "1.2.0-cdh5.8.2",
  "org.apache.hadoop" % "hadoop-common" % "2.7.3"
)
