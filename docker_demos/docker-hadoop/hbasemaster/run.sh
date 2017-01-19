#!/bin/bash

namedir=`echo $HDFS_CONF_dfs_namenode_name_dir | perl -pe 's#file://##'`
if [ ! -d $namedir ]; then
  echo "Namenode name directory not found: $namedir"
  exit 2
fi

if [ -z "$CLUSTER_NAME" ]; then
  echo "Cluster name not specified"
  exit 2
fi

# $HBASE_PREFIX/bin/hbase-daemons.sh --config "${HBASE_CONF_DIR}" start zookeeper # autorestart start
# $HBASE_PREFIX/bin/hbase-daemons.sh --config "${HBASE_CONF_DIR}" start master
# $HADOOP_PREFIX/bin/hdfs --config $HADOOP_CONF_DIR datanode

$HBASE_PREFIX/bin/hbase-daemons.sh --config "${HBASE_CONF_DIR}" start zookeeper
$HBASE_PREFIX/bin/hbase-daemon.sh --config "${HBASE_CONF_DIR}" foreground_start master
 # | tee $HBASE_PREFIX/logs/hbase-master.log
# TODO foreground
# $HADOOP_PREFIX/bin/hdfs --config $HADOOP_CONF_DIR datanode
