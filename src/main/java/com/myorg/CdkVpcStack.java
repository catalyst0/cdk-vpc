package com.myorg;

import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.cxapi.VpcSubnetGroup;
import software.amazon.awscdk.services.ec2.DefaultInstanceTenancy;
import software.amazon.awscdk.services.ec2.Subnet;
import software.amazon.awscdk.services.ec2.*;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.ec2.CfnNatGateway;

import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Arrays.parallelPrefix;


public class CdkVpcStack extends Stack {
    public CdkVpcStack(final Construct parent, final String id) {
        this(parent, id, null);
    }

    public CdkVpcStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);


        // Create 2 SubnetConfiguration objects
        SubnetConfiguration s1 = SubnetConfiguration.builder()
                .cidrMask(24)
                .name("Subnet0")
                .subnetType(SubnetType.PUBLIC)
                //.reserved(true)
                .build();

        SubnetConfiguration s2 = SubnetConfiguration.builder()
                .cidrMask(24)
                .name("Subnet1")
                .subnetType(SubnetType.PUBLIC)
                //.reserved(true)
                .build();

        // Create a Vpc object that uses the SubnetConfiguration objects created above
        // Note that this VPC will create 2 Nat Gateways in the 2 Public Subnets that will be generated from the 2 SubnetConfigurations above
        Vpc vpc = Vpc.Builder.create(this, "CdkVpc")
                .cidr("10.0.0.0/16")
                .defaultInstanceTenancy(DefaultInstanceTenancy.DEFAULT)
                .enableDnsSupport(true)
                .enableDnsHostnames(true)
                .maxAzs(2)
                .subnetConfiguration(asList(s1, s2))
                .natGateways(2)
                .natGatewayProvider(NatProvider.gateway())
                .build();

        // Now, I want to create 2 custom subnets - one private and another public
        // For the private subnet, I want to create a route table with a route that uses one of the Nat Gateways created above as the target
        // How do I do that programmatically?

        PrivateSubnet subnetA = PrivateSubnet.Builder.create(this, "SNa")
                .vpcId(vpc.getVpcId())
                .cidrBlock("10.0.10.0/24")
                .mapPublicIpOnLaunch(false)
                .availabilityZone("us-east-1a")
                .build();

        Subnet subnetB = Subnet.Builder.create(this, "SNb")
                .vpcId(vpc.getVpcId())
                .cidrBlock("10.0.20.0/24")
                .mapPublicIpOnLaunch(true)
                .availabilityZone("us-east-1b")
                .build();











    }
}
