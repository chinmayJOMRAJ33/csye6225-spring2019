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

echo "Creating subnet1 in vpc with a 10.0.0.0/24 CIDR block.."; 


subnet1=$(aws ec2 create-subnet --vpc-id "$vpcId" --cidr-block "10.0.0.0/24" --availability-zone-id "use1-az1")
subnetId1=$(echo -e "$subnet1" |  /usr/bin/jq '.Subnet.SubnetId' | tr -d '"')

ret=$?
if [ $ret -ne 0 ];
then
        echo "Error while creating subnet 1"
        exit $ret
fi

echo "subnet1 created successfully, creating subnet 2 in vpc with a 10.0.1.0/24 CIDR block..";

subnet2=$(aws ec2 create-subnet --vpc-id "$vpcId" --cidr-block "10.0.1.0/24" --availability-zone-id "use1-az2")
subnetId2=$(echo -e "$subnet2" |  /usr/bin/jq '.Subnet.SubnetId' | tr -d '"')

ret=$?
if [ $ret -ne 0 ];
then
        echo "Error while creating subnet 2"
        exit $ret
fi

echo "subnet2 created successfully, creating subnet 3 in vpc with a 10.0.2.0/24 CIDR block..";

subnet3=$(aws ec2 create-subnet --vpc-id "$vpcId" --cidr-block "10.0.2.0/24" --availability-zone-id "use1-az3")
subnetId3=$(echo -e "$subnet3" |  /usr/bin/jq '.Subnet.SubnetId' | tr -d '"')

ret=$?
if [ $ret -ne 0 ];
then
        echo "Error while creating subnet 3"
        exit $ret
fi

echo "subnet3 created successfully, creating internet-gateway";

internetGateway=$(aws ec2 create-internet-gateway)
gatewayId=$(echo -e "$internetGateway" |  /usr/bin/jq '.InternetGateway.InternetGatewayId' | tr -d '"')

ret=$?
if [ $ret -ne 0 ];
then
        echo "Error while creating internet gateway"
        exit $ret
fi

echo "internet gateway created successfully, attaching internet gateway to vpc";

aws ec2 attach-internet-gateway --vpc-id "$vpcId" --internet-gateway-id "$gatewayId"

ret=$?
if [ $ret -ne 0 ];
then
        echo "Error while attaching internet gateway to vpc"
        exit $ret
fi

echo "creating route table for vpc ..";

routeTable=$(aws ec2 create-route-table --vpc-id "$vpcId")
routeTableId=$(echo -e "$routeTable" |  /usr/bin/jq '.RouteTable.RouteTableId' | tr -d '"')

ret=$?
if [ $ret -ne 0 ];
then
        echo "Error while creating route table for vpc"
        exit $ret
fi

echo "route table created, creating a route in the route table that points all traffic (0.0.0.0/0) to the Internet gateway"

route=$(aws ec2 create-route --route-table-id $routeTableId --destination-cidr-block 0.0.0.0/0 --gateway-id $gatewayId)

#echo "route table description"

#aws ec2 describe-route-tables --route-table-id $routeTableId

