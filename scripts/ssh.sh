#!/usr/bin/env bash

set -o errexit
set -o nounset
set -o pipefail

source "./scripts/settings.sh"

readonly ssh_dir="./.ssh"
readonly ssh_config="${HOME}/.ssh/config"

mkdir --parents "${ssh_dir}"

[ -f "${ssh_dir}/key" ] || ssh-keygen -t ed25519 -N "" -f "${ssh_dir}/key"

if [ -f "${ssh_config}" ]; then
	sed -i "/Host ${settings[repo.name]}/,+6d" "${ssh_config}"
fi

cat >> "${ssh_config}" <<-EOF
	Host ${settings[repo.name]}
	    HostName 127.0.0.1
	    Port $_ssh_port
	    User "${USER}"
	    IdentityFile "${PWD}/.ssh/key"
	    StrictHostKeyChecking no
	    UserKnownHostsFile /dev/null
EOF

chmod 700 "${ssh_dir}"
chmod 600 "${ssh_dir}/key"
chmod 644 "${ssh_dir}/key.pub"
