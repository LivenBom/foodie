#!/bin/bash

# 应用名称
APP_NAME="foodie-api"

echo "Restarting ${APP_NAME} ..."

# 停止应用
sh stop.sh

# 等待几秒确保应用完全停止
sleep 5

# 启动应用
sh start.sh
