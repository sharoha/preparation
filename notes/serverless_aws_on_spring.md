# Going Serverless with your Spring App


## Problems that it solves
1. In a client-server bases setup with backend deployed on host/cloud has some issues with Auto Scaling. Say for instance, you have an instance that expects 100 request max.  
- What happens when you receive the 101 request? - Create a new instance of this server.  
- What is the cost of creating this server? - A whole lot of cpu and memory gets assigned that you won't even need.  

## Solution - Serverless
Note that serverless doesn't mean that you won't have a server. But from the usage perspective you don't need to think about how the server is managed. Instead you are just concerned with what functions to run.  
Hence, AWS provides the service called `Lambda` that lets you run this functions.  

Advertised benefits are -  
1. Reduce operational cost. Why? 
2. Automatic scaling by unit of consumption.
3. Only need to pay for the amount of time function gets executed.

## How does it work
Under the hood, your `Lamda` is getting deployed into an EC2 instance that runs [Firecracker](https://aws.amazon.com/blogs/aws/firecracker-lightweight-virtualization-for-serverless-computing/) on it. 
