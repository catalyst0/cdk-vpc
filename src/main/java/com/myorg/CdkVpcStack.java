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

//        VpcSubnetGroup v = VpcSubnetGroup.builder()
//                .subnets()
//                .type()
//                .build();


//        SubnetConfiguration s2 = SubnetConfiguration.builder()
//                .cidrMask(24)
//                .name("SubnetB")
//                .subnetType(SubnetType.PRIVATE)
//                .reserved(true) // indicates that the address space is reserved only; subnet is not actually created
//                .build();
//
//        SubnetConfiguration s3 = SubnetConfiguration.builder()
//                .cidrMask(24)
//                .name("SubnetC")
//                .subnetType(SubnetType.PRIVATE)
//                .reserved(true)
//                .build();

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







        List<ISubnet> v = vpc.getPublicSubnets();
        //System.out.println(v.toString());
        //System.out.println(v.size());
        //String sid = v.get(1).getSubnetId();
        //System.out.println(sid);
        for (ISubnet s: v) {
            System.out.println(s.getRouteTable().getRouteTableId());
            s.getInternetConnectivityEstablished();
        }

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







//        CfnNatGateway ngwA = new CfnNatGateway(this, "123", new CfnNatGatewayProps() {
//            @Override
//            public @NotNull String getAllocationId() {
//                CfnEIP e = new CfnEIP(subnetB, "1234", new CfnEIPProps() {
//                });
//                return e.getAttrAllocationId();
//            }
//
//            @Override
//            public @NotNull String getSubnetId() {
//                return subnetB.getSubnetId();
//            }
//        });




//        Subnet subnetC = Subnet.Builder.create(this, "SNc")
//                .vpcId(vpc.getVpcId())
//                .cidrBlock("10.0.3.0/24")
//                .mapPublicIpOnLaunch(false)
//                .availabilityZone("us-east-1c")
//                .build();
//
//        Subnet subnetD = Subnet.Builder.create(this, "SNd")
//                .vpcId(vpc.getVpcId())
//                .cidrBlock("10.0.4.0/24")
//                .mapPublicIpOnLaunch(false)
//                .availabilityZone("us-east-1d")
//                .build();

//        SubnetSelection s = SubnetSelection.builder()
//                            .subnets(asList(subnetA, subnetB))
//                           .build();






    }
}
