#!/bin/sh

if [ -z CLASSPATH ]; then
    CLASSPATH=${CLASSPATH}:.
else
    CLASSPATH=.
fi

jars=`ls ../lib`
for jar in ${jars}
do
    CLASSPATH=${CLASSPATH}:../lib/${jar}
done
echo ${CLASSPATH}

ROCKETMQ_ENABLE_SSL=true
export CLASSPATH ROCKETMQ_ENABLE_SSL
