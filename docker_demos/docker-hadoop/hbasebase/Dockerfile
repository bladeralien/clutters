FROM bde2020/hadoop-base:1.0.0
MAINTAINER Ivan Ermilov <ivan.s.ermilov@gmail.com>

ENV HBASE_VERSION 1.2.3
ENV HBASE_URL http://mirrors.cnnic.cn/apache/hbase/$HBASE_VERSION/hbase-$HBASE_VERSION-bin.tar.gz
RUN set -x \
    && curl -fSL "$HBASE_URL" -o /tmp/hbase.tar.gz \
    && curl -fSL "$HBASE_URL.mds" -o /tmp/hbase.tar.gz.mds \
    # TODO
    # && gpg --verify /tmp/hbase.tar.gz.mds \
    && tar -xvf /tmp/hbase.tar.gz -C /opt/ \
    && rm /tmp/hbase.tar.gz /tmp/hbase.tar.gz.mds

RUN ln -s /opt/hbase-$HBASE_VERSION/conf /etc/hbase
RUN mkdir /opt/hbase-$HBASE_VERSION/logs

RUN mkdir /hbase-data

ENV HBASE_PREFIX=/opt/hbase-$HBASE_VERSION
ENV HBASE_CONF_DIR=/etc/hbase

RUN apt-get update && DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends openssh-blacklist openssh-blacklist-extra openssh-client openssh-server

# TODO
# export JAVA_HOME=$JAVA_HOME
# export HBASE_CLASSPATH=$HBASE_CONF_DIR
# export HBASE_MANAGES_ZK=false
# Set Java home for hbase servers
# sed -i "s,^. export JAVA_HOME.*,export JAVA_HOME=$JAVA_HOME," $HBASE_CONF_DIR/hbase-env.sh

ENV USER=root
ENV PATH $HBASE_PREFIX/bin/:$PATH

ADD entrypoint.sh /entrypoint.sh
RUN chmod a+x /entrypoint.sh

CMD ["/entrypoint.sh"]
