#!/bin/sh

cp ../akrivis-ingestor/target/kubernetes/kubernetes.yml scorecards-helm-chart/templates/ingestor.yml
cp ../akrivis-evaluator/target/kubernetes/kubernetes.yml scorecards-helm-chart/templates/evaluator.yml
cp ../akrivis-processor/target/kubernetes/kubernetes.yml scorecards-helm-chart/templates/processor.yml
