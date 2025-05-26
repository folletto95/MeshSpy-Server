PROTO_REPO=https://github.com/meshtastic/protobufs.git
PROTO_DIR=internal/protobufs
GEN_DIR=$(PROTO_DIR)/generated
PROTO_FILES=$(shell find $(PROTO_DIR)/protobufs -name '*.proto')

.PHONY: all build run proto clean cross-linux-arm

all: build

build:
	go build -o meshspy ./cmd/meshspy

run: build
	./meshspy

proto: $(PROTO_FILES)
	@echo "📦 Compilazione Protobuf..."
	@if [ ! -d $(PROTO_DIR)/protobufs ]; then \
		git clone $(PROTO_REPO) $(PROTO_DIR)/protobufs; \
	else \
		cd $(PROTO_DIR)/protobufs && git pull; \
	fi
	@mkdir -p $(GEN_DIR)
	protoc --experimental_allow_proto3_optional -I=$(PROTO_DIR)/protobufs --go_out=$(GEN_DIR) --go_opt=paths=source_relative $(PROTO_DIR)/protobufs/meshtastic/*.proto

clean:
	rm -rf meshspy
	rm -rf $(GEN_DIR)

cross-linux-arm:
	GOOS=linux GOARCH=arm GOARM=6 go build -o meshspy ./cmd/meshspy