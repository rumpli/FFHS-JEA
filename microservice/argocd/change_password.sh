#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

###################################################################
#title          : change password
#description    : change initial argocd password
#author         : Yannick Englert
#email          : yannick.englert@students.ffhs.ch
#date           : 31.05.2024
#version        : v0.1
#usage          : ./change_password.sh
###################################################################

NAMESPACE=$(sed -n -e 's/^.*namespace: //p' argocd/values.yml)
ADMIN_PASSWORD=$(sed -n -e 's/^.*admin_password: //p' argocd/values.yml)

err() {
	echo "[ERROR][$(date +'%Y-%m-%dT%H:%M:%S%z')]: $*" >&2
}

put() {
	echo "[INFO][$(date +'%Y-%m-%dT%H:%M:%S%z')]: $*" >&1
}

port_forward() {
    ( kubectl port-forward svc/argocd-server -n argocd 8082:443 1>/dev/null ) &
    PID=$!
    sleep 1
}

argocd_login() {
    local -r ARGOCD_PASSWORD=$(kubectl -n "${NAMESPACE}" get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 -d)
    sleep 1
    if argocd login localhost:8082 --insecure --username admin --password "${ARGOCD_PASSWORD}" &>/dev/null; then
        put "Login successful"
    else
        err "Login unsuccessful"
        exit 1
    fi
    argocd account update-password --current-password "${ARGOCD_PASSWORD}" --new-password "${ADMIN_PASSWORD}"
}

main() {
    put "Wait 60sec for startup of ArgoCD.."
    sleep 60
    port_forward
    if ! ps -p $PID &>/dev/null; then
        err "Creating port forward failed"
        if [[ $(curl https://localhost:8082/healthz -k -s) == "ok" ]]; then
            put "ArgoCD seems to be reachable"
        else
            err "ArgoCD is not reachable"
            exit 1
        fi
    fi
    argocd_login
}

main "@"
