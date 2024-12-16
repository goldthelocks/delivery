#!/usr/bin/env zsh

REGION="ap-southeast-2"
TABLE="Rule"

echo "Creating Rules DynamoDB table..."
awslocal dynamodb create-table \
    --table-name "$TABLE" \
    --key-schema AttributeName=id,KeyType=HASH AttributeName=priority,KeyType=RANGE \
    --attribute-definitions \
        AttributeName=id,AttributeType=S \
        AttributeName=priority,AttributeType=N \
    --billing-mode PAY_PER_REQUEST \
    --region "$REGION"

echo "Inserting dummy data..."
awslocal dynamodb put-item \
    --table-name "$TABLE" \
    --item '{
        "id": {"S": "58e4e4fb-1518-47f0-ad2f-cd0b9ad4740d"},
        "priority": {"N": "1"},
        "name": {"S": "Reject"},
        "condition": {"S": "weight > 50"},
        "updatedTime": {"S": "2024-12-16T12:00:00Z"}
    }'

awslocal dynamodb put-item \
    --table-name "$TABLE" \
    --item '{
        "id": {"S": "99e2be8d-7b34-4dc1-997e-dc203bc890d4"},
        "priority": {"N": "2"},
        "name": {"S": "Heavy Parcel"},
        "condition": {"S": "weight > 10"},
        "costFormula": {"S": "20 * weight"},
        "updatedTime": {"S": "2024-12-16T12:00:00Z"}
    }'

awslocal dynamodb put-item \
    --table-name "$TABLE" \
    --item '{
        "id": {"S": "1752fb45-e0c9-4350-ac52-97042e53f8f7"},
        "priority": {"N": "3"},
        "name": {"S": "Small Parcel"},
        "condition": {"S": "volume < 1500"},
        "costFormula": {"S": "0.03 * volume"},
        "updatedTime": {"S": "2024-12-16T12:00:00Z"}
    }'

awslocal dynamodb put-item \
    --table-name "$TABLE" \
    --item '{
        "id": {"S": "2cfce5c9-55c4-426f-8f09-123f650b76d0"},
        "priority": {"N": "4"},
        "name": {"S": "Medium Parcel"},
        "condition": {"S": "volume < 2500"},
        "costFormula": {"S": "0.04 * volume"},
        "updatedTime": {"S": "2024-12-16T12:00:00Z"}
    }'

awslocal dynamodb put-item \
    --table-name "$TABLE" \
    --item '{
        "id": {"S": "d6b48729-74dd-46a9-8d6a-43659c89d048"},
        "priority": {"N": "5"},
        "name": {"S": "Large Parcel"},
        "costFormula": {"S": "0.05 * volume"},
        "updatedTime": {"S": "2024-12-16T12:00:00Z"}
    }'

echo "DDB init done!"