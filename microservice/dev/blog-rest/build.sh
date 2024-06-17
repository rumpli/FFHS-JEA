#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

###################################################################
#title          : build docker image
#description    : Builds the docker image
#author         : Yannick Englert
#email          : yannick.englert@students.ffhs.ch
#date           : 31.05.2024
#version        : 0.1
#usage          : build.sh
###################################################################

print_script_header() {
    local script_name
    local script_description
    local script_author
    local script_email
    local script_date
    local script_version
    local script_usage

    # Extract header information
    while IFS= read -r line || [ -n "$line" ]; do
        case "$line" in
            "#title"*) script_name="${line#*: }" ;;
            "#description"*) script_description="${line#*: }" ;;
            "#author"*) script_author="${line#*: }" ;;
            "#email"*) script_email="${line#*: }" ;;
            "#date"*) script_date="${line#*: }" ;;
            "#version"*) script_version="${line#*: }" ;;
            "#usage"*) script_usage="${line#*: }" ;;
        esac
    done < "$0"

    # Print header information
    echo "Script Name    : $script_name"
    echo "Description    : $script_description"
    echo "Author         : $script_author"
    echo "Email          : $script_email"
    echo "Date           : $script_date"
    echo "Version        : $script_version"
    echo "Usage          : $script_usage"
}

err() {
    echo "[$(date +'%Y-%m-%dT%H:%M:%S%z')]: $*" >&2
}

pushd ../blog-rest
./gradlew build
cp build/libs/*.jar ../microservice/dev/blog-rest
popd

pushd dev/blog-rest
if [[ ! -f dockerhub.config ]]; then
    err "dockerhub.config not found!"
    err "Please fill in your docker hub config to push this image (see example_dockerhub.config)"
fi
DOCKER_USER=$(sed -n -e 's/^.*docker_account: //p' dockerhub.config)
DOCKER_REPO=$(sed -n -e 's/^.*docker_repository: //p' dockerhub.config)
docker build -f Dockerfile -t "${DOCKER_USER}"/"${DOCKER_REPO}" . --platform linux/arm64
popd

