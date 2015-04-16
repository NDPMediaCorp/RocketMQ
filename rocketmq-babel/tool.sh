#!/usr/bin/env bash
APP_LOG=/dianyi/log
if [ ! -d "$APP_LOG" ]; then
   if [ ! -d ${HOME}/log ]; then
      mkdir ${HOME}/log
   fi

   APP_LOG=${HOME}/log
fi

#Run producer server on default.
if [ $# == 0 ]; then
    server="ProducerServer"
else
    server=$1;
fi
APP_MAIN="com.ndpmedia.rocketmq.babel.$server"

LIB_JARS="target/classes"

for file in `ls target/lib/*.jar`
do
    LIB_JARS="${LIB_JARS}:${file}"
done

echo $LIB_JARS

#JAVA_OPTS="-Duser.timezone=GMT+8 -server -Xms512m -Xmx512m -Xloggc:$APP_LOG/producerServer.gc.log -Denable_ssl=true -Drocketmq.namesrv.domain=172.30.50.54 "
JAVA_OPTS=" -Duser.timezone=GMT+8 -server -Xms512m -Xmx512m -Xloggc:$APP_LOG/producerServer.gc.log -Denable_ssl=true -Drocketmq.namesrv.domain=172.30.50.54 -DRocketMQClientPassword=VVYZZ9NLVdy849XIy/tM3Q== -Dlog.home=$APP_LOG "
JAVA_OPTS="${JAVA_OPTS} -Dworkdir=. "
JAVA_OPTS="${JAVA_OPTS} -cp ${LIB_JARS}"

if [ -z "${JAVA_HOME}" ]; then
    JAVA_HOME="/usr/java/default";
else
    echo "Java Home: $JAVA_HOME";
fi;

touch ${APP_LOG}/${server}.nohup.log
nohup ${JAVA_HOME}/bin/java $JAVA_OPTS $APP_MAIN > $APP_LOG/$server.nohup.log &
