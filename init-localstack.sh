#!/bin/bash

REGION=ap-northeast-2

echo "[init] Creating S3 buckets..."
awslocal s3 mb s3://my-bucket

echo "[init] Creating DynamoDB tables..."
awslocal dynamodb create-table \
    --region $REGION \
    --table-name user_curation_product \
    --attribute-definitions \
        AttributeName=PK,AttributeType=S \
        AttributeName=SK,AttributeType=S \
    --key-schema \
        AttributeName=PK,KeyType=HASH \
        AttributeName=SK,KeyType=RANGE \
    --billing-mode PAY_PER_REQUEST

echo "[init] Inserting DynamoDB initial data..."
awslocal dynamodb put-item \
    --region $REGION \
    --table-name user_curation_product \
    --item '{
        "PK": {"S": "USER#1"},
        "SK": {"S": "CURATION#11"},
        "productIds": {"L": [{"N": "1"}, {"N": "3"}, {"N": "11"}, {"N": "13"}, {"N": "26"}, {"N": "27"}, {"N": "35"}, {"N": "40"}]},
        "createdAt": {"S": "2025-03-01T10:30:00Z"},
        "updatedAt": {"S": "2025-03-01T10:30:00Z"}
    }'

awslocal dynamodb put-item \
    --region $REGION \
    --table-name user_curation_product \
    --item '{
        "PK": {"S": "USER#1"},
        "SK": {"S": "CURATION#12"},
        "productIds": {"L": [{"N": "10"}, {"N": "13"}, {"N": "9"}, {"N": "36"}, {"N": "5"}, {"N": "37"}, {"N": "29"}, {"N": "49"}]},
        "createdAt": {"S": "2025-03-01T10:30:00Z"},
        "updatedAt": {"S": "2025-03-01T10:30:00Z"}
    }'

awslocal dynamodb create-table \
    --region $REGION \
    --table-name user_product_badge \
    --attribute-definitions \
        AttributeName=PK,AttributeType=S \
        AttributeName=SK,AttributeType=S \
    --key-schema \
        AttributeName=PK,KeyType=HASH \
        AttributeName=SK,KeyType=RANGE \
    --billing-mode PAY_PER_REQUEST

echo "[init] Inserting user_product_badge initial data..."
awslocal dynamodb put-item \
    --region $REGION \
    --table-name user_product_badge \
    --item '{
        "PK": {"S": "USER#1"},
        "SK": {"S": "PRODUCT#1"},
        "isRecommended": {"BOOL": true},
        "isPersonalPick": {"BOOL": false},
        "createdAt": {"S": "2025-03-01T10:30:00Z"},
        "updatedAt": {"S": "2025-03-01T10:30:00Z"}
    }'

# 인천-방콕 왕복 (product 3) - 개인 맞춤 추천
awslocal dynamodb put-item \
    --region $REGION \
    --table-name user_product_badge \
    --item '{
        "PK": {"S": "USER#1"},
        "SK": {"S": "PRODUCT#3"},
        "isRecommended": {"BOOL": false},
        "isPersonalPick": {"BOOL": true},
        "createdAt": {"S": "2025-03-01T10:30:00Z"},
        "updatedAt": {"S": "2025-03-01T10:30:00Z"}
    }'

awslocal dynamodb put-item \
    --region $REGION \
    --table-name user_product_badge \
    --item '{
        "PK": {"S": "USER#1"},
        "SK": {"S": "PRODUCT#5"},
        "isRecommended": {"BOOL": true},
        "isPersonalPick": {"BOOL": true},
        "createdAt": {"S": "2025-03-01T10:30:00Z"},
        "updatedAt": {"S": "2025-03-01T10:30:00Z"}
    }'

awslocal dynamodb put-item \
    --region $REGION \
    --table-name user_product_badge \
    --item '{
        "PK": {"S": "USER#1"},
        "SK": {"S": "PRODUCT#9"},
        "isRecommended": {"BOOL": true},
        "isPersonalPick": {"BOOL": false},
        "createdAt": {"S": "2025-03-01T10:30:00Z"},
        "updatedAt": {"S": "2025-03-01T10:30:00Z"}
    }'

awslocal dynamodb put-item \
    --region $REGION \
    --table-name user_product_badge \
    --item '{
        "PK": {"S": "USER#1"},
        "SK": {"S": "PRODUCT#10"},
        "isRecommended": {"BOOL": false},
        "isPersonalPick": {"BOOL": true},
        "createdAt": {"S": "2025-03-01T10:30:00Z"},
        "updatedAt": {"S": "2025-03-01T10:30:00Z"}
    }'

awslocal dynamodb put-item \
    --region $REGION \
    --table-name user_product_badge \
    --item '{
        "PK": {"S": "USER#1"},
        "SK": {"S": "PRODUCT#13"},
        "isRecommended": {"BOOL": true},
        "isPersonalPick": {"BOOL": true},
        "createdAt": {"S": "2025-03-01T10:30:00Z"},
        "updatedAt": {"S": "2025-03-01T10:30:00Z"}
    }'

awslocal dynamodb put-item \
    --region $REGION \
    --table-name user_product_badge \
    --item '{
        "PK": {"S": "USER#1"},
        "SK": {"S": "PRODUCT#26"},
        "isRecommended": {"BOOL": true},
        "isPersonalPick": {"BOOL": false},
        "createdAt": {"S": "2025-03-01T10:30:00Z"},
        "updatedAt": {"S": "2025-03-01T10:30:00Z"}
    }'

awslocal dynamodb put-item \
    --region $REGION \
    --table-name user_product_badge \
    --item '{
        "PK": {"S": "USER#1"},
        "SK": {"S": "PRODUCT#27"},
        "isRecommended": {"BOOL": false},
        "isPersonalPick": {"BOOL": true},
        "createdAt": {"S": "2025-03-01T10:30:00Z"},
        "updatedAt": {"S": "2025-03-01T10:30:00Z"}
    }'

awslocal dynamodb put-item \
    --region $REGION \
    --table-name user_product_badge \
    --item '{
        "PK": {"S": "USER#1"},
        "SK": {"S": "PRODUCT#29"},
        "isRecommended": {"BOOL": true},
        "isPersonalPick": {"BOOL": false},
        "createdAt": {"S": "2025-03-01T10:30:00Z"},
        "updatedAt": {"S": "2025-03-01T10:30:00Z"}
    }'

awslocal dynamodb put-item \
    --region $REGION \
    --table-name user_product_badge \
    --item '{
        "PK": {"S": "USER#1"},
        "SK": {"S": "PRODUCT#35"},
        "isRecommended": {"BOOL": true},
        "isPersonalPick": {"BOOL": true},
        "createdAt": {"S": "2025-03-01T10:30:00Z"},
        "updatedAt": {"S": "2025-03-01T10:30:00Z"}
    }'

awslocal dynamodb put-item \
    --region $REGION \
    --table-name user_product_badge \
    --item '{
        "PK": {"S": "USER#1"},
        "SK": {"S": "PRODUCT#36"},
        "isRecommended": {"BOOL": false},
        "isPersonalPick": {"BOOL": true},
        "createdAt": {"S": "2025-03-01T10:30:00Z"},
        "updatedAt": {"S": "2025-03-01T10:30:00Z"}
    }'

awslocal dynamodb put-item \
    --region $REGION \
    --table-name user_product_badge \
    --item '{
        "PK": {"S": "USER#1"},
        "SK": {"S": "PRODUCT#37"},
        "isRecommended": {"BOOL": true},
        "isPersonalPick": {"BOOL": false},
        "createdAt": {"S": "2025-03-01T10:30:00Z"},
        "updatedAt": {"S": "2025-03-01T10:30:00Z"}
    }'

awslocal dynamodb put-item \
    --region $REGION \
    --table-name user_product_badge \
    --item '{
        "PK": {"S": "USER#1"},
        "SK": {"S": "PRODUCT#40"},
        "isRecommended": {"BOOL": true},
        "isPersonalPick": {"BOOL": true},
        "createdAt": {"S": "2025-03-01T10:30:00Z"},
        "updatedAt": {"S": "2025-03-01T10:30:00Z"}
    }'

awslocal dynamodb put-item \
    --region $REGION \
    --table-name user_product_badge \
    --item '{
        "PK": {"S": "USER#1"},
        "SK": {"S": "PRODUCT#49"},
        "isRecommended": {"BOOL": false},
        "isPersonalPick": {"BOOL": true},
        "createdAt": {"S": "2025-03-01T10:30:00Z"},
        "updatedAt": {"S": "2025-03-01T10:30:00Z"}
    }'

echo "[init] DynamoDB setup completed!"
awslocal dynamodb list-tables --region $REGION

echo "[init] Creating SQS queues..."
awslocal sqs create-queue --queue-name email-queue --region $REGION

echo "[init] SQS setup completed!"
awslocal sqs list-queues --region $REGION

echo "[init] Verifying SES email identity..."
awslocal ses verify-email-identity --email-address noreply@example.com --region $REGION
