typeset -gA settings

eval "settings=($(
	yq -o=json '.' ./app/settings.toml | \
	jq -r 'paths(scalars) as $path | [($path | map(tostring) | join(".")), getpath($path)] | @sh' | \
	tr '\n' ' '
))"
