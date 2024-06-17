#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

###################################################################
#title          : install.sh
#description    : Installs argocd with helm
#author         : Yannick Englert
#email          : yannick.englert@students.ffhs.ch
#date           : 31.05.2024
#version        : 0.1
#usage          : ./install.sh
###################################################################

NAMESPACE=$(sed -n -e 's/^.*namespace: //p' argocd/values.yml)

kubectl -n "$NAMESPACE" get deployment argocd-server &>/dev/null && { echo "ArgoCD already deployed" ; exit 1 ;}
kubectl create namespace "$NAMESPACE" || echo "Error creating namespace..."
kubectl get namespace "$NAMESPACE" &>/dev/null || { echo "Error checking namespace..."; exit 1;  }

helm install --values argocd/values.yml argocd vendor/argocd/argocd --namespace "$NAMESPACE"

