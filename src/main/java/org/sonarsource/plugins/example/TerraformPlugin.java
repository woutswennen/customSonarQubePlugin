
package org.sonarsource.plugins.example;

import org.sonar.api.Plugin;
import org.sonarsource.plugins.example.rules.NoMockPolicySensor;
import org.sonarsource.plugins.example.rules.BaseElementPolicyDefinition;
import org.sonarsource.plugins.example.rules.BaseElementPolicySensor;
import org.sonarsource.plugins.example.rules.NoMockPolicyDefinition;


/**
 * This class is the entry point for all extensions. It is referenced in pom.xml.
 */
public class TerraformPlugin implements Plugin {
  
  @Override
  public void define(Context context) {
    // adding a rule
    context.addExtensions(NoMockPolicyDefinition.class, NoMockPolicySensor.class);
    context.addExtensions(BaseElementPolicyDefinition.class, BaseElementPolicySensor.class);
 
  } 
}
