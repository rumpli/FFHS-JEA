#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

###################################################################
#title          : install_tools.sh
#description    : Installs tools needed to operate an k8s cluster
#author         : Yannick Englert
#email          : yannick.englert@students.ffhs.ch
#date           : 31.05.2024
#version        : 0.1
#usage          : ./install_tools.sh
###################################################################

print_script_header() {
    local script_name
    local script_description
    local script_author
    local script_email
    local script_date
    local script_version
    local script_usage
    local lines=()

    # Extract header information until the first line without #
    while IFS= read -r line && [[ $line =~ ^# ]]; do
        lines+=("$line")
    done < <(sed -n -e '/^#/p' "$0")

    for line in "${lines[@]}"; do
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

check_options() {
    local option
    local option_arg

    # Check for the presence of options
    while [[ $# -gt 0 ]]; do
        option="$1"

        case "$option" in
            -h|--help)
                print_script_header
                exit 0
                ;;
            -m|--myoption)
                # Handle -m or --myoption
                echo "Option -m or --myoption selected."
                ;;
            -n|--anotheroption)
                # Check if the next argument is available
                if [[ -n "${2:-}" && "${2:-}" != -* ]]; then
                    option_arg="${2}"
                    echo "Option -n or --anotheroption selected with argument: ${option_arg}"
                    shift # Skip the argument processed
                else
                    echo "Option -n or --anotheroption requires an argument."
                    exit 1
                fi
                ;;
            *)
                # Handle other options or arguments here
                ;;
        esac
        shift # Move to the next option or argument
    done
}

err() {
    echo "[$(date +'%Y-%m-%dT%H:%M:%S%z')]: $*" >&2
}

retry() {
    local -r -i max_attempts="$1"; shift
    local -r -i sleep_time="$1"; shift
    local -i attempt_num=1
    until "$@"; do
        if (( attempt_num == max_attempts ))
    then
        echo "#$attempt_num failures!"
        exit 1
    else
        echo "#$(( attempt_num++ )): trying again in $sleep_time seconds ..."
        sleep $sleep_time
        fi
    done
}

install_tool() {
    local -r tool_name="$1"; shift
    local -r tool_url="$1"; shift
    local -r tool_checksum="$1"; shift
    if ! type "$tool_name" &>/dev/null; then
        if [[ ! -f "$HOME/bin/${tool_name}" ]]; then
            echo "downloading [${tool_name}] ..."
            wget -q "${tool_url}" -O "$HOME/bin/${tool_name}"
            chmod +x "$HOME/bin/${tool_name}"
        fi
        sha256sum "$HOME/bin/${tool_name}" | grep "${tool_checksum}" >/dev/null || (echo "checksum failed for [${tool_name}]" && rm -f "$HOME/bin/${tool_name}" && exit 1)
        echo "installed [${tool_name}] ..."
    fi
}

install_tool_brew() {
    local -r tool_name="$1"; shift
    if ! type "$tool_name" &>/dev/null; then
        echo "Installing [${tool_name}] ..."
        brew install "$tool_name"
    fi
}

install_tool_from_tarball() {
    local -r tool_path="$1"; shift
    local -r tool_name="$1"; shift
    local -r tool_url="$1"; shift
    local -r tool_checksum="$1"; shift
    if ! type "$tool_name" &>/dev/null; then
        if [[ ! -f "$HOME/bin/${tool_name}" ]]; then
            echo "downloading [${tool_name}] ..."
            wget -q "${tool_url}" -O "$HOME/bin/${tool_name}.tgz"
            echo "unpacking [${tool_name}.tgz] ..."
            STRIP_COMPONENTS=$(echo "${tool_path}" | awk -F"/" '{print NF-1}')
            tar -xvzf "$HOME/bin/${tool_name}.tgz" --strip-components="${STRIP_COMPONENTS}" -C "$HOME/bin/" "${tool_path}" 1>/dev/null
            chmod +x "$HOME/bin/${tool_name}"
            rm -f "$HOME/bin/${tool_name}.tgz"
        fi
        sha256sum "$HOME/bin/${tool_name}" | grep "${tool_checksum}" >/dev/null || (echo "checksum failed for [${tool_name}]" && rm -f "$HOME/bin/${tool_name}" && exit 1)
        echo "installed [${tool_name}] ..."
    fi
}

main() {
    tools=(
        kubectl
        kustomize
        vendir
        argocd
        helm
    )

    if [[ "$(uname -s)" == 'Darwin'* ]]; then
        # brew for macosx
        if ! type brew &>/dev/null; then
            echo "Installing brew..."
            /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
        else
            echo "Updating brew..."
            brew update
        fi
        brew tap carvel-dev/carvel
        for tool in "${tools[@]}"; do
            if ! install_tool_brew "${tool}"; then
                err "There was an error while installing ${tool}"
            fi
        done
        echo "Brew cleanup..."
        brew cleanup
    else
        # $HOME/bin
        if [[ ! -d "$HOME/bin" ]]; then
            mkdir "$HOME/bin"
        fi
        new_path="$HOME/bin"
        # shellcheck source=/dev/null
        case ":${PATH:=$new_path}:" in
            *:"$new_path":*)  ;;
            *) export PATH="$new_path:$PATH"; echo "PATH=\"\$HOME/bin:\$PATH\"" >> "$HOME/.bashrc"; source "$HOME/.bashrc"  ;;
        esac
        install_tool "kubectl" "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl" "$(curl -Ls "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl.sha256")"
        install_tool "vendir" "https://github.com/k14s/vendir/releases/download/v0.37.0/vendir-linux-amd64" "f1472bf7995506830fa79473f0ae406ea3885e0881fbbb096240efb1b053dd15"
        install_tool "argocd" "https://github.com/argoproj/argo-cd/releases/download/v2.7.15/argocd-linux-amd64" "80ac8f8d608f262ec8324c9b9f39924c0dd6a1db1c7ee539cbee3dd2c84791e7"
        install_tool "kustomize" "https://github.com/kubernetes-sigs/kustomize/releases/download/kustomize%2Fv5.3.0/kustomize_v5.3.0_linux_amd64.tar.gz" "3ab32f92360d752a2a53e56be073b649abc1e7351b912c0fb32b960d1def854c"
        install_tool_from_tarball "linux-amd64/helm" "helm" "https://get.helm.sh/helm-v3.13.2-linux-amd64.tar.gz" "acf97d8f2410e4b1347956aef2c5b8ecf7bcac157da7ea2f6553af39b5cfd137"
    fi
}

check_options "$@"
main "$@"

