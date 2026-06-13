#!/usr/bin/env bash

set -o errexit
set -o nounset
set -o pipefail

source "./scripts/settings.sh"

./scripts/ssh.sh

name="${settings[app.name]}"

podman network exists ${name}-net || podman network create ${name}-net

file="container/file"
logs="container/build.log"

${settings[container.backend]} build \
	--progress=plain \
	--format docker \
	--dns 8.8.8.8 \
	--dns 8.8.4.4 \
	--file $file \
	--tag "${settings[repo.host]}/${settings[repo.user]}/${settings[repo.name]}:latest" \
	--build-arg os="${settings[container.os]}" \
	--build-arg app_name="${settings[app.name]}" \
	--build-arg user_name="${USER}" \
	--build-arg user_id=$(id -u) \
	--build-arg user_group_name=host_$(getent group $(id -g) | awk -F: '{print $1}') \
	--build-arg user_group_id=$(id -g) \
	--build-arg container_workdir="${PWD}/" \
	--build-arg buildcfg="${PWD}/${file}" \
	--build-arg ssh_port="$_ssh_port" \
	. 2>&1 | tee ./${logs}
