#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

###################################################################
#title          : frontend port forward
#description    : reach the frontend with port forward
#author         : Yannick Englert
#email          : yannick.englert@students.ffhs.ch
#date           : 31.05.2024
#version        : v0.1
#usage          : ./port-forward-frontend.sh
###################################################################

err() {
	echo "[ERROR][$(date +'%Y-%m-%dT%H:%M:%S%z')]: $*" >&2
}

put() {
	echo "[INFO][$(date +'%Y-%m-%dT%H:%M:%S%z')]: $*" >&1
}

port_forward-frontend() {
    ( kubectl port-forward svc/blog-frontend-svc -n jea 3000:3000 ) &
    PID=$!
    sleep 1
}

port_forward-backend() {
    ( kubectl port-forward svc/blog-rest-svc -n jea 8081:8081 ) &
    PID=$!
    sleep 1
}

port_forward-auth() {
    ( kubectl port-forward svc/auth-svc -n jea 8080:8080 ) &
    PID=$!
    sleep 1
}

main() {
    port_forward-frontend
    if ! ps -p $PID &>/dev/null; then
        err "Creating port forward to frontend failed"
    fi
    port_forward-backend
    if ! ps -p $PID &>/dev/null; then
        err "Creating port forward to backend failed"
    fi
    port_forward-auth
    if ! ps -p $PID &>/dev/null; then
        err "Creating port forward to auth failed"
    fi
}

main "@"
