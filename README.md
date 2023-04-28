SonarQube Custom Terraform Plugin
==========

An terraform SonarQube plugin compatible with SonarQube 10.x.

Back-end
--------

Todo...

### Building

To build the plugin JAR file, call:

```
mvn clean package
```

The JAR will be deployed to `target/sonar-example-plugin-VERSION.jar`. Copy this to your SonarQube's `extensions/plugins/` directory, and re-start SonarQube.
