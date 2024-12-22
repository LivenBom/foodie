#!/bin/bash

# 应用名称
APP_NAME="foodie-api"
# PID文件
PID_FILE="/opt/foodie/app.pid"

# 检查PID文件是否存在
if [ ! -f "$PID_FILE" ]; then
    echo "${APP_NAME} is not running"
    exit 0
fi

# 读取PID
PID=`cat $PID_FILE`

# 检查进程是否存在
if [ -n "$PID" ]; then
    echo "Stopping ${APP_NAME} ..."
    kill $PID
    
    # 等待进程结束
    for i in {1..30}; do
        if ! ps -p $PID > /dev/null; then
            echo "${APP_NAME} stopped successfully"
            rm -f $PID_FILE
            exit 0
        fi
        sleep 1
    done
    
    # 如果进程还在运行，强制结束
    echo "Force killing ${APP_NAME} ..."
    kill -9 $PID
    rm -f $PID_FILE
fi
