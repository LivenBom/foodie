#!/bin/bash

# 设置Java环境变量（如果需要）
# export JAVA_HOME=/path/to/java
# export PATH=$JAVA_HOME/bin:$PATH

# 应用名称
APP_NAME="foodie-api"
# JAR包路径
JAR_PATH="/opt/foodie/app/foodie-api-1.0-SNAPSHOT.jar"
# 日志路径
LOG_PATH="/opt/foodie/logs"
# PID文件
PID_FILE="/opt/foodie/app.pid"

# 创建日志目录
mkdir -p ${LOG_PATH}

# 检查是否已经运行
if [ -f "$PID_FILE" ]; then
    echo "${APP_NAME} is already running. pid=`cat $PID_FILE`"
    exit 1
fi

# 启动应用
echo "Starting ${APP_NAME} ..."
nohup java -jar ${JAR_PATH} \
    --spring.profiles.active=prod \
    > ${LOG_PATH}/foodie.log 2>&1 &

# 记录PID
echo $! > ${PID_FILE}

# 检查是否启动成功
sleep 5
if [ -f "$PID_FILE" ]; then
    echo "${APP_NAME} started successfully. pid=`cat $PID_FILE`"
else
    echo "${APP_NAME} failed to start."
    exit 1
fi
