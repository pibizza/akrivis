#!/bin/sh
# Move between panels with ctrl+b and o
tmux new-session -d 'cd akrivis-processor; mvn compile quarkus:dev'
tmux split-window 'cd examples/rest-service-mock; mvn compile quarkus:dev -Dquarkus.http.port=8081 -Ddebug=5006'
tmux split-window 'cd akrivis-evaluator; mvn compile quarkus:dev -Dquarkus.http.port=8082 -Ddebug=5007'
tmux split-window 'cd akrivis-ingestor; mvn compile quarkus:dev -Dquarkus.http.port=8083 -Ddebug=5008'
tmux select-layout tiled
tmux set -g pane-border-status top
tmux set -g pane-border-format "#{pane_index} #{pane_current_path}"
tmux -2 attach-session -d