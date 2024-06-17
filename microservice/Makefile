.DEFAULT_GOAL := help

# Detect the operating system
UNAME_S := $(shell uname -s)

# Set default shell based on the operating system
ifeq ($(UNAME_S),Darwin)
    SHELL := /bin/zsh
else
    SHELL := /bin/bash
endif

ROOT_DIR = $(realpath .)

# ======================================================================================================================
.PHONY: help
## help: print this help message
help:
	@echo "Usage:"
	@sed -n 's/^##//p' ${MAKEFILE_LIST} | column -t -s ':' |  sed -e 's/^/ /'
# ======================================================================================================================

# ======================================================================================================================
.PHONY: install
## install: makes everything
install: check-env sync install-argocd argocd-deploy

.PHONY: build
## build: builds all images
build: build-blog-rest-image build-auth-image build-blog-frontend-image

.PHONY: push
## push: push all images
push: push-blog-rest-image push-auth-image push-blog-frontend-image

.PHONY: deploy
## deploy: deploys the full application
deploy: create-namespace deploy-mysql deploy-blog-frontend deploy-blog-rest deploy-auth

.PHONY: argocd-deploy
## argocd-deploy: deploys everything with argocd
argocd-deploy: create-namespace argocd-deploy-mysql argocd-deploy-blog-frontend argocd-deploy-blog-rest argocd-deploy-auth

.PHONE: create-namespace
## create-namespace: creates the jea namespace
create-namespace:
	@kubectl create ns jea || echo "Namespace exists already"

.PHONY: delete
## delete: deletes everything
delete: check-env argocd-delete delete-argocd

.PHONY: argocd-delete
## argocd-delete: deletes everything from ArgoCD
argocd-delete: delete-argocd-mysql delete-argocd-blog-frontend delete-argocd-blog-rest delete-argocd-auth delete-secret

.PHONY: uninstall
## uninstall: delete the full application
uninstall: delete-mysql delete-blog-frontend delete-blog-rest delete-auth

.PHONY: create-demo-image
## create-demo-image: builds and push docker image
create-demo-image: build-demo-image push-demo-image

.PHONY: deploy-demo-image
## deploy-demo-image: Deploys demo image to Kubernetes with ArgoCD
deploy-demo-image: deploy-demo-image

.PHONY: delete-secret
## delete-secret: Deletes the github secret
delete-secret:
	@kubectl delete -f ops/argocd/github_secret.yml || echo "Already deleted"
# ======================================================================================================================
.PHONY: check-env
## check-env: verify current working environment
check-env:
	@type bash 1>/dev/null || (echo 'You have no [bash] ???' && exit 1)
	@type kubectl 1>/dev/null || (echo '[kubectl] is missing! Install it with [make install-tools] ...' && exit 1)
	@type kustomize 1>/dev/null || (echo '[kustomize] is missing! Install it with [make install-tools] ...' && exit 1)
	@type vendir 1>/dev/null || (echo '[vendir] is missing! Install it with [make install-tools] ...' && exit 1)
	@type argocd 1>/dev/null || (echo '[argocd] is missing! Install it with [make install-tools] ...' && exit 1)
	@type helm 1>/dev/null || (echo '[helm] is missing! Install it with [make install-tools] ...' && exit 1)
	@echo '[check-env] successfully'
# ======================================================================================================================

# ======================================================================================================================
.PHONY: sync
## vendir-sync: sync the files declared in vendir
sync:
	@vendir sync

.PHONY: install-tools
## install-tools: install all tools
install-tools:
	@${ROOT_DIR}/install_tools.sh
# ======================================================================================================================

# ======================================================================================================================
.PHONY: install-argocd
## install-argocd: installs argocd
install-argocd:
	@${ROOT_DIR}/argocd/install.sh
	@${ROOT_DIR}/argocd/change_password.sh
# ======================================================================================================================

# ======================================================================================================================
.PHONY: argocd-deploy-mysql
## argocd-deploy-mysql: Deploys the image to Kubernetes with ArgoCD
argocd-deploy-mysql:
	@kubectl apply -f ops/argocd/jea-mysql_application.yml
	@echo "Lets give it 30sec to spin up!" && sleep 30

.PHONY: delete-argocd-mysql
## delte-argocd-mysql: Deletes the app from ArgoCD
delete-argocd-mysql:
	@kubectl delete -f ops/argocd/jea-mysql_application.yml || echo "Already deleted"

.PHONY: deploy-mysql
## deploy-mysql: Deploys the image to Kubernetes
deploy-mysql:
	@kubectl apply -f ops/mysql/
	@echo "Lets give it 30sec to spin up!" && sleep 30

.PHONY: delete-mysql
## delete-mysql: Deletes the app
delete-mysql:
	@kubectl delete -f ops/mysql/ || echo "Already deleted"
# ======================================================================================================================

# ======================================================================================================================
.PHONY: build-blog-frontend-image
## build-blog-frontend-image: builds the blog-frontend docker image
build-blog-frontend-image:
	@${ROOT_DIR}/dev/blog-frontend/build.sh

.PHONY: push-blog-frontend-image
## push-blog-frontend-image: push the blog-frontend docker image
push-blog-frontend-image:
	@${ROOT_DIR}/dev/blog-frontend/push.sh

.PHONY: argocd-deploy-blog-frontend
## argocd-deploy-blog-frontend: Deploys the image to Kubernetes with ArgoCD
argocd-deploy-blog-frontend:
	@kubectl apply -f ops/argocd/github_secret.yml
	@kubectl apply -f ops/argocd/jea-blog-frontend_application.yml
	@kubectl apply -f ops/argocd/jea_project.yml
	@echo "Lets give it 30sec to spin up!" && sleep 30

.PHONY: delete-argocd-blog-frontend
## delete-argocd-blog-frontend: Deletes the app from ArgoCD
delete-argocd-blog-frontend:
	@kubectl delete -f ops/argocd/jea-blog-frontend_application.yml || echo "Already deleted"
	@kubectl delete -f ops/argocd/jea_project.yml || echo "Already deleted"

.PHONY: deploy-blog-frontend
## deploy-blog-frontend: Deploys the image to Kubernetes
deploy-blog-frontend:
	@kubectl apply -f ops/blog-frontend/
	@echo "Lets give it 30sec to spin up!" && sleep 30

.PHONY: delete-blog-frontend
## delete-blog-frontend: Deletes the app
delete-blog-frontend:
	@kubectl delete -f ops/blog-frontend/ || echo "Already deleted"
# ======================================================================================================================

# ======================================================================================================================
.PHONY: build-blog-rest-image
## build-blog-rest-image: builds the blog-rest docker image
build-blog-rest-image:
	@${ROOT_DIR}/dev/blog-rest/build.sh

.PHONY: push-blog-rest-image
## push-blog-rest-image: push the blog-rest docker image
push-blog-rest-image:
	@${ROOT_DIR}/dev/blog-rest/push.sh

.PHONY: argocd-deploy-blog-rest
## argocd-deploy-blog-rest: Deploys the image to Kubernetes with ArgoCD
argocd-deploy-blog-rest:
	@kubectl apply -f ops/argocd/github_secret.yml
	@kubectl apply -f ops/argocd/jea-blog-rest_application.yml
	@kubectl apply -f ops/argocd/jea_project.yml
	@echo "Lets give it 30sec to spin up!" && sleep 30

.PHONY: delete-argocd-blog-rest
## delete-argocd-blog-rest: Deletes the app from ArgoCD
delete-argocd-blog-rest:
	@kubectl delete -f ops/argocd/jea-blog-rest_application.yml || echo "Already deleted"
	@kubectl delete -f ops/argocd/jea_project.yml || echo "Already deleted"

.PHONY: deploy-blog-rest
## deploy-blog-rest: Deploys the image to Kubernetes
deploy-blog-rest:
	@kubectl apply -f ops/blog-rest
	@echo "Lets give it 30sec to spin up!" && sleep 30

.PHONY: delete-blog-rest
## delete-blog-rest: Deletes the application
delete-blog-rest:
	@kubectl delete -f ops/blog-rest || echo "Already deleted"
# ======================================================================================================================

# ======================================================================================================================
.PHONY: build-auth-image
## build-auth-image: builds the auth docker image
build-auth-image:
	@${ROOT_DIR}/dev/auth/build.sh

.PHONY: push-auth-image
## push-auth-image: push the auth docker image
push-auth-image:
	@${ROOT_DIR}/dev/auth/push.sh

.PHONY: argocd-deploy-auth
## argocd-deploy-auth: Deploys the image to Kubernetes with ArgoCD
argocd-deploy-auth:
	@kubectl apply -f ops/argocd/github_secret.yml
	@kubectl apply -f ops/argocd/jea-auth_application.yml
	@kubectl apply -f ops/argocd/jea_project.yml
	@echo "Lets give it 30sec to spin up!" && sleep 30

.PHONY: delete-argocd-auth
## delete-argocd-auth: Deletes the application from ArgoCD
delete-argocd-auth:
	@kubectl delete -f ops/argocd/jea-auth_application.yml || echo "Already deleted"
	@kubectl delete -f ops/argocd/jea_project.yml || echo "Already deleted"

.PHONY: deploy-auth
## deploy-auth: Deploys the image to Kubernetes 
deploy-auth:
	@kubectl apply -f ops/auth
	@echo "Lets give it 30sec to spin up!" && sleep 30

.PHONY: delete-auth
## delete-auth: Delete the application
delete-auth:
	@kubectl delete -f ops/auth || echo "Already deleted"
# ======================================================================================================================

# ======================================================================================================================
.PHONY: port-forward-frontend
## port-forward-frontend: creates port-forward for frontend
port-forward-frontend:
	@${ROOT_DIR}/ops/port-forward-frontend.sh
# ======================================================================================================================

# ======================================================================================================================
.PHONY: delete-argocd
## delete-argocd: deletes argocd
delete-argocd:
	@${ROOT_DIR}/argocd/uninstall.sh
# ======================================================================================================================

# ======================================================================================================================
.PHONY: argocd-login
## argocd-login: login to ArgoCD
argocd-login:
	@${ROOT_DIR}/argocd/login.sh
# ======================================================================================================================

# ======================================================================================================================
.PHONY: check-scripts
## check-scripts: checks all scripts ending with .sh with 'shellcheck'
check-scripts:
	@shellcheck **/*.sh || (echo 'Failed' && exit 1)
# ======================================================================================================================
