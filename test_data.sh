#!/bin/bash

# Add some test accounts and money
content="Content-Type: application/json"

function create_account {
  accounts="http://localhost:8083/api/v1/accounts"
  body="{\"currency\":\"EUR\"}"
  curl "$accounts" -X POST -H "$content" -s -d "$body"
}

function deposit {
  balances="http://localhost:8082/api/v1/accounts/$1/balances"
  body="{\"amount\":\"$2\"}"
  curl "$balances" -X POST -H "$content" -s -d "$body"
}

function transfer {
  transfers="http://localhost:8085/api/v1/transfers"
  body="{\"fromAccountId\":\"$1\",\"toAccountId\":\"$2\",\"amount\":$3}"
  curl "$transfers" -X POST -H "$content" -s -d "$body"
}

echo ""

account_1=$(create_account)
account_1_id=$(echo "$account_1" | jq -r '.id')
echo "Created account:"
echo "$account_1" | jq
account_2=$(create_account)
account_2_id=$(echo "$account_2" | jq -r '.id')
echo "Created account:"
echo "$account_2" | jq

echo ""

deposit_1=$(deposit "$account_1_id" 1000)
echo "Created deposit:"
echo "$deposit_1" | jq
deposit_2=$(deposit "$account_2_id" 2000)
echo "Created deposit:"
echo "$deposit_2" | jq

echo ""

transfer_1=$(transfer "$account_1_id" "$account_2_id" 100)
echo "Created transfer:"
echo "$transfer_1" | jq
transfer_2=$(transfer "$account_2_id" "$account_1_id" 200)
echo "Created transfer:"
echo "$transfer_2" | jq

echo ""
