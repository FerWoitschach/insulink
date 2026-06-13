set shell := ["bash", "-c"]

import 'scripts/adb.jt'
import 'scripts/gradle.jt'

alias debug := gradle.debug

gradle.debug:
	cp ./app/.gradle/settings.kts ./app/.gradle/settings.gradle.kts
	cp ./app/.gradle/build.kts    ./app/.gradle/build.gradle.kts

	gradle --settings-file=./app/.gradle/settings.gradle.kts assembleDebug --warning-mode all --project-cache-dir=./app/.gradle/.cache/; \
		STATUS=$?; \
		rm -f ./app/.gradle/settings.gradle.kts ./app/.gradle/build.gradle.kts; \
		[ $STATUS -eq 0 ] && echo -e "\a"; \
		exit $STATUS

gradle.clean:
	rm -rf ./app/.gradle/.cache/daemon
