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
public class NoMockPolicySensor implements Sensor {
  


  @Override
  public void describe(SensorDescriptor descriptor) {
    descriptor.name("Cannot define mock-policy in terraform");

    // optimisation to disable execution of sensor if project does
    // not contain Java files or if the example rule is not activated
    // in the Quality profile
    descriptor.onlyOnLanguage("terraform");
    descriptor.createIssuesForRuleRepositories(NoMockPolicyDefinition.REPOSITORY);
  }

  @Override
  public void execute(SensorContext context) {
    
    FileSystem fs = context.fileSystem();
    Iterable<InputFile> files = fs.inputFiles(fs.predicates().hasLanguage("terraform"));
    for (InputFile file : files) {
        
      String content;
      
        try {
          content = file.contents();
          String regex = "<\\s*mock-response[\\s\\S]*?/mock-response\\s*>";

          Pattern pattern = Pattern.compile(regex);
          Matcher matcher = pattern.matcher(content);
          int lineNumber = 0;
          while(matcher.find())
          {
            lineNumber = getLineNumber(content, matcher.start());
            NewIssue newIssue = context.newIssue()
            .forRule(NoMockPolicyDefinition.NO_MOCK_POLICY);
  
            NewIssueLocation primaryLocation = newIssue.newLocation()
              .on(file)
              .at(file.selectLine(lineNumber))
              .message("Cannot define mock-policy in resource.");
            newIssue.at(primaryLocation);
            newIssue.save();
            
          } 
          
          

        } catch (Exception e) {
          System.out.println(e.getMessage());
        }
      } 
    }
    private static int getLineNumber(String content, int index) {
      return content.substring(0, index).split("\r\n|\r|\n").length;
    }
}

