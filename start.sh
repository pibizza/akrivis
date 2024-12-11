#!/bin/sh

### How it works:
# - If you run `./script.sh split`, it will use the split-pane layout.
# - If you run `./script.sh tab`, it will use the tab-based layout.

if [ "$1" = "split" ]; then
  if [ -d "../backstage-plugins" ]; then
    # Split-pane layout
    tmux new-session -d 'cd akrivis-processor; mvn compile quarkus:dev -Ddebug=5009'
    tmux split-window -h 'cd examples/rest-service-mock; mvn compile quarkus:dev -Dquarkus.http.port=8081 -Ddebug=5006'
    tmux split-window -h 'cd akrivis-evaluator; mvn compile quarkus:dev -Dquarkus.http.port=8082 -Ddebug=5007'
    tmux split-window -v 'cd akrivis-ingestor; mvn compile quarkus:dev -Dquarkus.http.port=8083 -Ddebug=5008'
    tmux split-window -h 'cd database; docker compose up' # Wait?
    tmux split-window -h 'cd ../backstage-plugins; yarn start:backstage'
    tmux select-layout tiled
    tmux set -g pane-border-status top
    tmux set -g pane-border-format "#{pane_index} #{pane_current_path}"
    tmux -2 attach-session -d
  else
    echo "../backstage-plugins does not exist."
  fi
elif [ "$1" = "tab" ]; then
  # Tab-based layout
  tmux new-session -d -s scorecard -n "Processor" 'cd akrivis-processor; mvn compile quarkus:dev -Ddebug=5009'

  tmux new-window -t scorecard -n "Database" 'cd scripts; podman compose -f compose.yaml up'
  tmux new-window -t scorecard -n "RestMock" 'cd examples/rest-service-mock; mvn compile quarkus:dev -Dquarkus.http.port=8081 -Ddebug=5006'
  tmux new-window -t scorecard -n "Evaluator" 'cd akrivis-evaluator; mvn compile quarkus:dev -Dquarkus.http.port=8082 -Ddebug=5007'
  tmux new-window -t scorecard -n "Ingestor" 'cd akrivis-ingestor; mvn compile quarkus:dev -Dquarkus.http.port=8083 -Ddebug=5008'
  tmux new-window -t scorecard -n "Backstage" 'cd ../backstage-plugins; yarn start:backstage'

  tmux attach-session -t scorecard
else
  echo "Usage: $0 {split|tab}"
  exit 1
fi