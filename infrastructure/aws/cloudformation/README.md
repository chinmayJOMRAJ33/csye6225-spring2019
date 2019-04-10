There are 6 scripts:
1. "csye6225-aws-cf-create-stack.sh" - This is the script to setup AWS network.
2. "csye6255-aws-cf-create-cicd.sh" - This is the script for policies and roles.
3. "csye6225-aws-cf-create-application-stack.sh" - This is the script to setup the application.
4. "csye6225-aws-cf-terinate-stack.sh" - This is to terminate the entire network stack.
5. "csye6255-aws-cf-terminate-cicd.sh" - This is to terminate the entire cicd stack.
6. "csye6225-aws-cf-terminate-application-stack.sh" - This is to terminate the entire application stack.

Network Setup Script:
1. The "csye6225-aws-cf-create-stack.sh" takes the stack name from the user, checks whether the satck exists or not.
2. If the stack exists then the message is displayed as the stack exists and terminates it.
3. If the stack does not exist then the stack name is passed to the "csye6225-cf-networking.json" template.
4. The VPC, 3 private and public subnets, 1 private, public route table, 1 internet gateway is created to setup the network.
4. Finally once the stack is created successfully and the script is excuted the messages are displayed accordingly.
5. The script "csye6225-aws-cf-terminate-stack.sh" for termination also checks if the stack exists or not.
6. If the stack exists then the script terminates the stack resource and waits for the resource termination.
7. After successful completion of termination the message is displyed.

Cicd Script:
1. The "csye6225-aws-cf-create-cicd.sh" creates the roles and policies for the code deployment. 
2. The following role and policies are present:
  a. CodeDeployEC2ServiceRole
  b. CodeDeployServiceRole
Policies:
  a. CircleCiCodeDeploy
  b. CodeDeployEC2S3
  c. CircleCiUploadToS3
3. The "csye6225-aws-cf-delete-cicd.sh" deletes the roles and policies.

Application Setup Script:
1. The "csye6225-aws-cf-create-application-stack.sh" takes the stack name from the user, checks whether the satck exists or not.
2. If the stack exists then the message is displayed as the stack exists and terminates it.
3. If the stack does not exist then the stack name and various other parameters are passed to the "csye6225-cf-application.json"   template.
4. The application stack created EC2 instance, RDS instance, DynamoDB and the security groups for EC2 and RDS instance.
5. Finally once the stack is created successfully and the script is excuted the messages are displayed accordingly.
6. The script "csye6225-aws-cf-terminate-application-stack.sh" for termination also checks if the stack exists or not.
7. If the stack exists then the script terminates the stack resource and waits for the resource termination.
8. After successful completion of termination the message is displyed.
