.PHONY : main
main: library
	mvn -Dmaven.test.skip=true install android:redeploy  android:run -q

library:
	$(MAKE) -C ../library/ all

.PHONY : test
test: library
	mvn test

#.DEFAULT_GOAL := test
.DEFAULT_GOAL := main
