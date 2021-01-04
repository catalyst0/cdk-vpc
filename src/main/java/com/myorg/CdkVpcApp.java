package com.myorg;

import software.amazon.awscdk.core.App;

public final class CdkVpcApp {
    public static void main(final String[] args) {
        // App object processes all the core constructs
        App app = new App();

        // Instantiate a new CustomStack; where all the AWS constructs are defined
        new CustomStack(app, "CustomStack");
        app.synth();
        System.out.println("After Synth");
    }
}
