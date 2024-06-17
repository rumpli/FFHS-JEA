#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

###################################################################
#title          : uninstall.sh
#description    : Uninstalls argocd with helm
#author         : Yannick Englert
#email          : yannick.englert@students.ffhs.ch
#date           : 31.05.2024
#version        : 0.1
#usage          : ./uninstall.sh
###################################################################

NAMESPACE=$(sed -n -e 's/^.*namespace: //p' argocd/values.yml)

kubectl wait --for=delete -f ops/argocd

helm uninstall argocd --namespace "$NAMESPACE"

kubectl -n "$NAMESPACE" get deployment argocd-server &>/dev/null || echo "ArgoCD successfully uninstalled"
kubectl delete namespace "$NAMESPACE" || echo "Error deleting namespace..."
