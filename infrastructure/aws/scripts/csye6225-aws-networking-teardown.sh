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

route_tbl_id=$(aws ec2 describe-route-tables \
 --filters "Name=vpc-id,Values=$vpc_id" "Name=association.main, Values=false" \
 --query 'RouteTables[*].{RouteTableId:RouteTableId}' \
 --output text)

IGW_Id=$(aws ec2 describe-internet-gateways \
  --query 'InternetGateways[*].{InternetGatewayId:InternetGatewayId}' \
  --filters "Name=attachment.vpc-id,Values=$vpc_id" \
  --output text)

cho "Start to delete!!"
while
sub=$(aws ec2 describe-subnets \
 --filters Name=vpc-id,Values=$vpc_id \
 --query 'Subnets[*].SubnetId' \
 --output text)
[[ ! -z $sub ]]
do
        var1=$(echo $sub | cut -f1 -d" ")
       # echo $var1 is deleted 
        aws ec2 delete-subnet --subnet-id $var1
	ret=$?
	if [ $ret -ne 0 ];
	then
        	echo "Error while deleting subnet"
        	exit $ret
	fi
done
echo "Subnets delete--------------------->OK"

aws ec2 delete-route-table --route-table-id $route_tbl_id
ret=$?
if [ $ret -ne 0 ];
then
        echo "Error while deleting route table"
        exit $ret
fi
echo "Route-Table delete------------------------>OK"

aws ec2 detach-internet-gateway \
 --internet-gateway-id $IGW_Id \
 --vpc-id $vpc_id
ret=$?
if [ $ret -ne 0 ];
then
        echo "Error while detaching internet gateway"
        exit $ret
fi
echo "IGW detached------------------------>OK"
