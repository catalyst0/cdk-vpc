package com.myorg;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.amazon.awscdk.core.*;
import software.amazon.awscdk.services.ec2.*;

import java.util.Arrays;


public class CustomStack extends Stack {
    // Constructor
    public CustomStack(@Nullable Construct scope, @Nullable String id) {
        super(scope, id);
        System.out.println("CONSTRUCTOR OF CUSTOMSTACK");
    }

    CfnTag vpcXNameTag = new CfnTag() {
        @Override
        public @NotNull String getKey() {
            return "name";
        }

        @Override
        public @NotNull String getValue() {
            return "vpcX";
        }
    };



    CfnVPC vpcX = new CfnVPC(this, "VPC.X",
            CfnVPCProps.builder()
                    .cidrBlock("10.0.0.0/16")
                    .tags(Arrays.asList(vpcXNameTag))
                    .build()
    );





    CfnSubnet snA = new CfnSubnet(vpcX, "snA",
                                     CfnSubnetProps.builder()
                                            .availabilityZone("us-east-1a")
                                            .cidrBlock("10.0.1.0/24")
                                            .vpcId(vpcX.getRef())
                                            .mapPublicIpOnLaunch(false)
                                            .build()
    );

    CfnSubnet snB = new CfnSubnet(vpcX, "snB",
                                     CfnSubnetProps.builder()
                                             .availabilityZone("us-east-1c")
                                             .cidrBlock("10.0.2.0/24")
                                             .vpcId(vpcX.getRef())
                                             .mapPublicIpOnLaunch(true)
                                             .build()
                                    );

    CfnInternetGateway igw = new CfnInternetGateway(vpcX, "IGW.A",
                                        CfnInternetGatewayProps.builder()
                                        .build()
                                    );

    CfnVPCGatewayAttachment gwa = new CfnVPCGatewayAttachment(vpcX, "gwaA",
                                                CfnVPCGatewayAttachmentProps.builder()
                                                        .internetGatewayId(igw.getRef())
                                                        .vpcId(vpcX.getRef())
                                                        .build()
                                        );
        


    CfnRouteTable snARouteTable = new CfnRouteTable(snA, "snART", CfnRouteTableProps.builder()
                                            .vpcId(vpcX.getRef())
                                            .build()
                                      );

    CfnRouteTable snBRouteTable = new CfnRouteTable(snB, "snBRT", CfnRouteTableProps.builder()
                                            .vpcId(vpcX.getRef())
                                            .build()
                                      );

    CfnSubnetRouteTableAssociation snARTA = new CfnSubnetRouteTableAssociation(snA, "snARTA",
                                                 CfnSubnetRouteTableAssociationProps.builder()
                                                .routeTableId(snARouteTable.getRef())
                                                .subnetId(snA.getRef())
                                                .build()
                                              );

    CfnSubnetRouteTableAssociation snARTB = new CfnSubnetRouteTableAssociation(snB, "snARTB",
            CfnSubnetRouteTableAssociationProps.builder()
                    .routeTableId(snBRouteTable.getRef())
                    .subnetId(snB.getRef())
                    .build()
    );

    CfnEIP snAEIP = new CfnEIP(snA, "snAEIP",
                            CfnEIPProps.builder()
                            .domain("vpc")
                            .build()
    );

    CfnNatGateway snBNGW = new CfnNatGateway(snB, "snBNGW",
                                CfnNatGatewayProps.builder()
                                        .allocationId(snAEIP.getAttrAllocationId())
                                        .subnetId(snB.getRef())
                                        .build()
    );

    CfnRoute snANGWRoute = new CfnRoute(snA, "snANGWRoute", CfnRouteProps.builder()
            .destinationCidrBlock("0.0.0.0/0")
            .natGatewayId(snBNGW.getRef())
            .routeTableId(snARouteTable.getRef())
            .build()
    );

    CfnRoute snBIGWWRoute = new CfnRoute(snB, "snBIGWRoute", CfnRouteProps.builder()
            .destinationCidrBlock("0.0.0.0/0")
            .gatewayId(igw.getRef())
            .routeTableId(snBRouteTable.getRef())
            .build()
    );




}

