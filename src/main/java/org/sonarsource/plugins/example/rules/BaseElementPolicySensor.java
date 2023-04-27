package org.sonarsource.plugins.example.rules;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Generates issues on all java files at line 1. This rule
 * must be activated in the Quality profile.
 */
public class BaseElementPolicySensor implements Sensor {



  @Override
  public void describe(SensorDescriptor descriptor) {
    descriptor.name("Cannot define a policy without base element in terraform");

    // optimisation to disable execution of sensor if project does
    // not contain Java files or if the example rule is not activated
    // in the Quality profile
    descriptor.onlyOnLanguage("terraform");
    descriptor.createIssuesForRuleRepositories(BaseElementPolicyDefinition.REPOSITORY);
  }

  @Override
  public void execute(SensorContext context) {
    FileSystem fs = context.fileSystem();
    Iterable<InputFile> files = fs.inputFiles(fs.predicates().hasLanguage("terraform"));
    for (InputFile file : files) {
        
      String content;
        try {
          content = file.contents();
          // <policies>     </policies>
          String policiesRegex = "<\\s*(inbound|outbound|backend|on-error)[\\s\\S]*?<\\s*/(inbound|outbound|backend|on-error)\\s*>";
          // <base>         </base>
          String baseRegex = "<\\s*/base\\s*>";
          int lineNumber = 0;
  
          Pattern policiesPattern = Pattern.compile(policiesRegex);
          Matcher policiesMatcher = policiesPattern.matcher(content);
  
          while (policiesMatcher.find()) {
              String policiesTag = policiesMatcher.group();
              Pattern basePattern = Pattern.compile(baseRegex);
              Matcher baseMatcher = basePattern.matcher(policiesTag);
              if (!baseMatcher.find()) {

                lineNumber = getLineNumber(content, policiesMatcher.start());

                NewIssue newIssue = context.newIssue()
                .forRule(BaseElementPolicyDefinition.BASE_TAG_IN_POLICY);
                
                NewIssueLocation primaryLocation = newIssue.newLocation()
                  .on(file)
                  .at(file.selectLine(lineNumber))
                  .message("This element should have a <base> element in it.");

                newIssue.at(primaryLocation);
                newIssue.save(); 
              }

            }
          }
          catch (Exception e) {
            System.out.println(e.getMessage());
          }
        

      }

      
    }
    private static int getLineNumber(String content, int index) {
        return content.substring(0, index).split("\r\n|\r|\n").length;
    }

}

