#!/bin/bash
# Script to delete the networking resources


region="us-east-1"

if [ $# -eq 0 ]; then
 echo " PLEASE PASS <VPC_NAME> as parameter while running this script "
 exit 1
fi

echo "Prepare for deleting,please wait........"

vpc="$1-csye6225-vpc-1"
vpcname=$(aws ec2 describe-vpcs \
 --query "Vpcs[?Tags[?Key=='Name']|[?Value=='$vpc']].Tags[0].Value" \
 --output text)

vpc_id=$(aws ec2 describe-vpcs \
 --query 'Vpcs[*].{VpcId:VpcId}' \
 --filters Name=is-default,Values=false \
 --output text \
  --region $region)
