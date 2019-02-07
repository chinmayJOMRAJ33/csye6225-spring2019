#!/bin/bash

if [ $# -eq 0 ];
then
        echo "Please provide a stack name"
	exit 1
fi

vpcName=$1
echo "Create a VPC "$vpcName" with a 10.0.0.0/16 CIDR block";
vpc=$(aws ec2 create-vpc --cidr-block 10.0.0.0/16 --output json)
vpcId=$(echo -e "$vpc" | /usr/bin/jq '.Vpc.VpcId' | tr -d '"')
aws ec2 create-tags --resources "$vpcId" --tags Key=Name,Value="$vpcName" 
echo $vpcId;
ret=$?
if [ $ret -ne 0 ];
then
        echo "Error while creating vpc"
        exit $ret
fi

echo "vpc created successfully, creating 3 public subnet in same region with different availability zone";

