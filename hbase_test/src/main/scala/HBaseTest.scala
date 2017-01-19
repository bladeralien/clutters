import org.apache.hadoop.hbase.{Cell, HBaseConfiguration,TableName}
import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.util.Bytes

object HBaseTest {
  def main(args: Array[String]): Unit = {
    println("Hello, world!")
    //val conf = new HBaseConfiguration()
    val conf = HBaseConfiguration.create()
    conf.set("hbase.zookeeper.quorum", "192.168.199.207:2181")
//    conf.set("hbase.zookeeper.property.clientPort", "2181");

//    val admin = new HBaseAdmin(conf)
//
////     list the tables
//    val listtables=admin.listTables()
//    listtables.foreach(println)

    // let's insert some data in 'mytable' and get the row
    val con = ConnectionFactory.createConnection(conf)

    val table = con.getTable(TableName.valueOf("logs"))



    //val table = new HTable(conf, "mytable")

    val theput= new Put(Bytes.toBytes("rowkey1"))

    theput.add(Bytes.toBytes("r"),Bytes.toBytes("msg"),Bytes.toBytes("rawmsg"))
    table.put(theput)

    val theget= new Get(Bytes.toBytes("rowkey1"))
    val result=table.get(theget)
    val value=result.value()
    println(Bytes.toString(value))
  }
}




//sensorRDD.foreachRDD { rdd =>
//  // filter sensor data for low psi
//  val alertRDD = rdd.filter(sensor => sensor.psi < 5.0)
//
//  // convert sensor data to put object and write to HBase  Table CF data
//  rdd.map(Sensor.convertToPut).saveAsHadoopDataset(jobConfig)
//
//  // convert alert to put object write to HBase  Table CF alerts
//  rdd.map(Sensor.convertToPutAlert).saveAsHadoopDataset(jobConfig)
//}