#!make
ifeq (, $(shell which mvn))
    $(error "Please install Maven.")
endif

define HELP

Usage:
    make help                show available commands
    make clean               remove generated files
    make install             install dependencies
    make test                run unit tests
    make lint                run lint inspections
    make format              format the source code
    make run                 run the web server
endef

export HELP

help:
	@echo "$$HELP"

clean:
	mvn clean

install:
	mvn install

test: clean format
	mvn test

lint:
	mvn pmd:pmd
	mvn spotbugs:check

format:
	mvn prettier:write

run: clean format
	mvn spring-boot:run
