#!/usr/bin/env bash

set -o errexit
set -o nounset
set -o pipefail

source "./scripts/settings.sh"

${settings[container.backend]} run \
	--detach \
	--interactive \
	--tty \
	--name "${settings[repo.name]}" \
	--network "${settings[app.name]}-net" \
	--volume "${PWD}/:${PWD}/" \
	--hostname "${settings[repo.name]}" \
	--workdir "${PWD}/" \
	--publish "${_ssh_port}:22" \
	--group-add keep-groups \
	--userns=keep-id:uid=$(id -u),gid=$(id -g) \
	--env DISPLAY=$DISPLAY \
	--env WAYLAND_DISPLAY=$WAYLAND_DISPLAY \
	--env XDG_RUNTIME_DIR=/tmp \
	--volume $XDG_RUNTIME_DIR/$WAYLAND_DISPLAY:/tmp/$WAYLAND_DISPLAY:ro \
	--env QT_QPA_PLATFORM=wayland \
	--volume /tmp/.X11-unix:/tmp/.X11-unix:ro \
	--device /dev/dri/renderD128:/dev/dri/renderD128 \
	"${settings[repo.host]}/${settings[repo.user]}/${settings[repo.name]}:latest" \
	/bin/zsh
