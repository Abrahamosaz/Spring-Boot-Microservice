#!/usr/bin/env bash

set -e # stops the script if any command fails

echo "Deleting existing stack..."
aws --endpoint-url=http://localhost:4566 cloudformation delete-stack --stack-name patient-management

echo "Waiting for stack to be deleted..."
aws --endpoint-url=http://localhost:4566 cloudformation wait stack-delete-complete --stack-name patient-management

echo "Deploying new stack..."
aws --endpoint-url=http://localhost:4566 cloudformation deploy \
    --stack-name patient-management \
    --template-file "./cdk.out/localStack.template.json"

echo "Retrieving Load Balancer DNS..."
aws --endpoint-url=http://localhost:4566 elbv2 describe-load-balancers \
    --query "LoadBalancers[0].DNSName" --output text