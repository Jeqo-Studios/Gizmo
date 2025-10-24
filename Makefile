# Variables
DOCKER_COMPOSE_FILE = gizmo_sdk/docker-compose.yml
SERVICE_PREFIX = gizmo-
SERVER_SUFFIX = -server

# Building details
BUILD_SERVER = main
TARGET_PLUGIN_DIR = gizmo_sdk/$(BUILD_SERVER)/plugins/

# Container lists
CONTAINERS = main$(SERVER_SUFFIX)

# Grouped containers
SERVER_CONTAINERS = $(foreach container, $(filter %$(SERVER_SUFFIX) %$(PROXY_SUFFIX), $(CONTAINERS)), $(SERVICE_PREFIX)$(container))

# Phony targets
.PHONY: help up up-build down start stop up-servers restart-servers attach bash send-command pbuild

# Help command
help:
	@echo "Available commands:"
	@echo "  up                Start all services"
	@echo "  up-build          Build and start all services"
	@echo "  down              Stop all services"
	@echo "  start             Start a specific service"
	@echo "  stop              Stop a specific service"
	@echo "  up-servers        Start all server services"
	@echo "  restart-servers   Restart server services"
	@echo "  stop-servers      Stop all server services"
	@echo "  attach            Attach to a running container. Use CTRL+Q to detach"
	@echo "  bash              Open a bash shell in a running container. Only meant to interact with container infrastructure"
	@echo "  send-command      Send a command to a Wynncraft server container"
	@echo "  pbuild            Clean, build, and deploy the plugin, then reload the server"

# General control commands
up:
	docker compose -f $(DOCKER_COMPOSE_FILE) up -d

up-build:
	docker compose -f $(DOCKER_COMPOSE_FILE) up -d --build

down:
	docker compose -f $(DOCKER_COMPOSE_FILE) down

attach:
	docker compose -f $(DOCKER_COMPOSE_FILE) attach $(SERVICE_PREFIX)$(container)

bash:
	docker compose -f $(DOCKER_COMPOSE_FILE) exec $(SERVICE_PREFIX)$(container) bash

# Individual service control commands
start:
	docker compose -f $(DOCKER_COMPOSE_FILE) up -d $(SERVICE_PREFIX)$(service)

stop:
	docker compose -f $(DOCKER_COMPOSE_FILE) stop $(SERVICE_PREFIX)$(service)

# Grouped service control commands
up-servers:
	docker compose -f $(DOCKER_COMPOSE_FILE) up -d $(SERVER_CONTAINERS)

restart-servers:
	docker compose -f $(DOCKER_COMPOSE_FILE) restart $(SERVER_CONTAINERS)

stop-servers:
	docker compose -f $(DOCKER_COMPOSE_FILE) stop $(SERVER_CONTAINERS)

# Gizmo related commands

# Note: This only works with actual servers; not proxies
send-command:
	docker exec $(SERVICE_PREFIX)$(container)$(SERVER_SUFFIX) rcon-cli $(command)

pbuild:
	mvn clean package
	mv target/$$(ls target | grep '\.jar$$' | grep -v 'original-' | head -n 1) $(TARGET_PLUGIN_DIR)
	make send-command container=$(BUILD_SERVER) command="reload confirm"