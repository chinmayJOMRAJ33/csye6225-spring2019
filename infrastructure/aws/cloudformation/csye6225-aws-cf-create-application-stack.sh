#!/bin/bash
NET_STACK_NAME=$1
keyPair=$2
EC2="${NET_STACK_NAME}-csye6225-ec2"

export vpcID=$(aws ec2 describe-vpcs --filters "Name=tag-key,Values=Name" --query "Vpcs[*].[CidrBlock, VpcId][-1]" --output text|grep 10.0.0.0/16|awk '{print $2}')

export subnet1=$(aws ec2 describe-subnets --filters "Name=vpc-id,Values=$vpcID" --query 'Subnets[*].[SubnetId, VpcId, AvailabilityZone, CidrBlock]' --output text|grep 10.0.11.0/24|grep us-east-1a|awk '{print $1}')

export subnet2=$(aws ec2 describe-subnets --filters "Name=vpc-id,Values=$vpcID" --query 'Subnets[*].[SubnetId, VpcId, AvailabilityZone, CidrBlock]' --output text|grep 10.0.12.0/24|grep us-east-1b|awk '{print $1}')

export subnet3=$(aws ec2 describe-subnets --filters "Name=vpc-id,Values=$vpcID" --query 'Subnets[*].[SubnetId, VpcId, AvailabilityZone, CidrBlock]' --output text|grep 10.0.14.0/24|grep us-east-1c|awk '{print $1}')

export AMI=$(aws ec2 describe-images --filters "Name=name,Values=csye6225" --query 'sort_by(Images, &CreationDate)[-1].ImageId' --output text)


"
