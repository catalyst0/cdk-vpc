package com.myorg;

import software.amazon.awscdk.core.App;

public final class CdkVpcApp {
    public static void main(final String[] args) {
        App app = new App();

        // new CdkVpcStack(app, "CdkVpcStack");
        new CustomStack(app, "CustomStack");

        app.synth();
    }
}
