CC=gcc
CFLAGS=-c -Wall -Werror -Wpedantic -O2 -fPIC -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux
LDFLAGS=-fPIC -shared

SOURCES_DIR=src/main/c/net/ksmr/sched
OBJECTS_DIR=target/c
EXECUTABLE=target/classes/net_ksmr_sched_Sched.so

SOURCES=$(shell find '$(SOURCES_DIR)' -type f -name '*.c')
OBJECTS=$(SOURCES:$(SOURCES_DIR)/%.c=$(OBJECTS_DIR)/%.o)

all: $(EXECUTABLE)

$(EXECUTABLE): $(OBJECTS)
	$(CC) $(LDFLAGS) $(OBJECTS) -o $@

$(OBJECTS): $(SOURCES)
	mkdir -p $(OBJECTS_DIR)
	$(CC) $(CFLAGS) $< -o $@

clean:
	rm -rf $(OBJECTS_DIR) $(EXECUTABLE)
