#!/bin/bash

if [ $# -eq 0 ];
then 
	echo "Please provide a stack name"
	exit 1
fi
stack_name=$1
aws cloudformation create-stack --stack-name $stack_name --template-body file://csye6225-cf-networking.json
ret=$?
if [ $ret -eq 0 ];
then 
	echo "Stack is being created...."
	aws cloudformation wait stack-create-complete --stack-name $stack_name
	echo "Stack created successfully!"
else 
	echo "Stack could not be created"
	#exit sc
fi
