SonarQube Custom Terraform Plugin
==========

An SonarQube plugin compatible with SonarQube 10.x. Adds to linting rules for Terraform:
* Azure policy can not include mock policy
* Policy must include base tag

This is a work in progress in the sense that it needs to be cleaned up.
You can add the Jar to your <SONARQUBE_HOME>/extensions/plugins


### Building/ Deploying

To build the plugin JAR file, run:

```
mvn clean package
```

The JAR will be deployed to `target/sonar-example-plugin-VERSION.jar`. Copy this to your SonarQube's `extensions/plugins/` directory, and re-start SonarQube.
